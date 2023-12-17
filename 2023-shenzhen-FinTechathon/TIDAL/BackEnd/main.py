from flask import Flask, render_template, request, jsonify, abort
from deploy import *

app = Flask(__name__)

# @app.before_request
# def before_request()
app.config['user'] = get_current_user()

# 定义路由保护装饰器
def role_required(role):
    def decorator(func):
        @wraps(func)
        def decorated_function(*args, **kwargs):
            user = app.config['user']
            if not user or user.role.value < role.value:
                abort(403)  # 无权限访问
            return func(*args, **kwargs)
        return decorated_function
    return decorator

@app.route('/')
def home():
    # data = queryAllContents()
    return render_template('index.html')

@app.route('/changeUser', methods=['POST'])
def change_user():
    username = request.form['username']
    role_number = getUser(username)[0][0]  # Convert the string form data to an integer
    role = Role(role_number)  # Map the integer to the corresponding Role enum

    # Update the user configuration
    app.config['user'] = User(username, role)

    return jsonify({'success': True})

@app.route('/allUsers', methods=['GET'])
def allUsers():
    allUserData = selectAllUsers()
    
    return jsonify(allUserData)

@app.route('/changeRole', methods=['POST'])
def changeRole():
    username = request.form['username']
    role_number = int(request.form['role'])  # Convert the string form data to an integer
    
    modifyUser(username, role_number)
    
    app.config['user'].role = Role(role_number)
    
    return jsonify({'success': True})


@app.route('/selectAllTables', methods=['GET'])
@role_required(Role.READ_ONLY)
def select_tables():
    all_data = selectAllTablesContents()
    print('all_dd:', all_data)
    return jsonify(all_data)


@app.route('/search', methods=['POST'])
@role_required(Role.READ_ONLY)
def search():
    # Extract the key from the request
    search_key = request.form['key']
    # Perform your search here using the search_key
    item = queryHashIndices(search_key)
    return jsonify(item)


@app.route('/getAll', methods=['GET'])
@role_required(Role.READ_ONLY)
def get_all():
    records = queryAllContents()
    return jsonify(records)

@app.route('/insert', methods=['POST'])
@role_required(Role.READ_WRITE)
def insert():
    print("user_now!:",app.config['user'].username,"--", app.config['user'].role)
    key = request.form.get('key')
    content = request.form.get('content')
    indices = request.form.get('indices')
    success, result = insertModify(key, indices, content)

    if not success:
        return jsonify({"error": "增加数据出错"}), 400

    return jsonify(result), 200


@app.route('/modifyProcess', methods=['POST'])
@role_required(Role.READ_WRITE)
def modify():
    # Extract the new content from request
    new_content = request.form['new_footprint']
    aim_key = request.form['key']

    # Perform your modification here using the new_content
    chameleon = processModify(aim_key, new_content)

    new_data = {'Key': aim_key, 'chameleon': chameleon}

    return jsonify(new_data)


@app.route('/verifyProcess', methods=['POST'])
@role_required(Role.READ_ONLY)
def verify():
    # Extract the key from request
    key = request.form['key']

    # Perform your verification here using the key
    # For now, we just return a success message
    # result = 'verification success'
    result = verifyHash(key)

    return jsonify({'result': result})


if __name__ == '__main__':
    app.run(port=2023, host="127.0.0.1")
