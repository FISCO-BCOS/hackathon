# Taishang Badges Backend


## 运行 taishang_badges_backend

**运行环境**

python3.7+

**运行 taishang_badges_backend**

~~~bash
git clone https://github.com/WeLightProject/Taishang-Badges-Backend.git

cd Taishang-Badges-Backend/

cp .envrc taishang_badges_backend/.env

pip install -r requirements.txt

python manager.py runserver 
# 运行服务
~~~
-----------------


~~~bash
positional arguments:
  {runserver,shell,reset_local_db,reset_server_db,reset_db,init_local_db,init_server_db,init_db,set_user}
    runserver           Runs the Flask development server i.e. app.run()
    shell               Runs a Python shell inside Flask application context.
~~~