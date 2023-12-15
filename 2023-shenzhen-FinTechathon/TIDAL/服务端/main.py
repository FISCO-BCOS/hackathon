from flask import Flask, render_template, request, jsonify
from deploy import *

app = Flask(__name__)

@app.route('/')
def home():
    data = queryAllContents()
    return render_template('index.html')


@app.route('/search', methods=['POST'])
def search():
    # Extract the key from the request
    search_key = request.form['key']
    
    # Perform your search here using the search_key
    item = queryHashIndices(search_key)
    
    return jsonify(item)

@app.route('/getAll', methods=['GET'])
def get_all():
    records = queryAllContents()
    return jsonify(records)

""" @app.route('/insert', methods=['POST'])
def insert():
    key = request.form.get('key')
    content = request.form.get('content')
    indices = request.form.get('indices')
    result = insertModify(key, indices, content)
    return jsonify(result), 200 """
    
@app.route('/insert', methods=['POST'])
def insert():
    key = request.form.get('key')
    content = request.form.get('content')
    indices = request.form.get('indices')
    success, result = insertModify(key, indices, content)
    
    if not success:
        return jsonify({"error": "增加数据出错"}), 200
    
    return jsonify(result), 200

@app.route('/modifyProcess', methods=['POST'])
def modify():
    # Extract the new content from request
    new_content = request.form['new_content']
    aim_key = request.form['key']
    
    # Perform your modification here using the new_content
    chameleon = processModify(aim_key, new_content)
    
    new_data = {'Key': aim_key, 'hashValue': sha256(new_content.encode('utf-8')).hexdigest(), 'chameleon': chameleon}
    
    return jsonify(new_data)

@app.route('/verifyProcess', methods=['POST'])
def verify():
    # Extract the key from request
    key = request.form['key']
    
    # Perform your verification here using the key
    # For now, we just return a success message
    # result = 'verification success'
    result = verifyHash(key)
    
    return jsonify({'result': result})

if __name__ == '__main__':
    app.run(port=2010, host="0.0.0.0")
