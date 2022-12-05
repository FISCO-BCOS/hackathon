#sql终端启动命令 mysql -uroot -pymysql
import socket
import threading
import web_frame
import json
class HTTPWEBSEVER:
    # TCP服程序
    # 创建socket
    tcp_server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # 设置端口复用
    tcp_server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, True)
    # 绑定地址
    tcp_server_socket.bind(("", 8080))
    # 设置监听
    tcp_server_socket.listen(128)

    def handle_client_request(self,client_socket):
        # 获取浏览器的请求信息
        client_request_data = client_socket.recv(1024).decode()
        print(client_request_data)
        # 获取用户请求资源的路劲
        request_data = client_request_data.split(" ")
        print(request_data)
        #判断客户端是否关闭
        if len(request_data) == 1:
            client_socket.close()
            return
            # 请求资源路径
        request_path = request_data[1]
        if request_path == "/":
             request_path = "1_login.html"

        if  request_path == "/1_login.html":
            #客户只要请求这个页面
            '''登录界面'''
            try:
                # 登录界面
                with open("."+request_path, "rb") as f:
                    file_data = f.read()
            except Exception as e:
                # 应答行
                response_line = "HTTP/1.1 404 not found\r\n"
                # 应答头
                response_header = "Sever:pwb\r\n"
                # 用答题
                response_body = "404 not found"

                response_data = (response_line + response_header + "\r\n" + response_body).encode()
                client_socket.send(response_data)
            else:
                # 应答行
                response_line = "HTTP/1.1 200 OK\r\n"
                # 应答头
                response_header = "Sever:pwb\r\n"
                # 用答题
                # 登录界面的html
                response_body = file_data

                response_data = (response_line + response_header + "\r\n").encode() + response_body
                client_socket.send(response_data)
            finally:
                #关闭此线程
                client_socket.close()

        elif  request_path.endwith("_return"):
            try:
                #前端拿到数据进行数据组装与请求 ，并将账户的地址给到后端
                '''政府/机构/企业页面'''
                #pass
                response_line = "HTTP/1.1 200 OK\r\n"
                # 应答头
                response_header = "Sever:pwb\r\n"
                #账户名称 用于实力化对象，切换不同账户调用合约
                #response_account=request_data[]
                # 应答体
                with open("."+request_path+".html", "rb") as f:
                    file_data = f.read()
                #file_data.replace("账户名称",response_account)
                response_body = file_data.encode()
                response_data = (response_line + response_header + "\r\n").encode() + response_body
                client_socket.send(response_data)
                client_socket.close()
            except Exception as e:
                # 应答行
                response_line = "HTTP/1.1 404 not found\r\n"
                # 应答头
                response_header = "Sever:pwb\r\n"
                # 用答题
                response_body = "404 not found"
                response_data = (response_line + response_header + response_body).encode()
                client_socket.send(response_data)
            finally:
                client_socket.close()
        elif request_path.endswith("_button"):
            try:
                #相应功能界面
                response_line = "HTTP/1.1 200 OK\r\n"
                # 应答头
                response_header = "Sever:pwb\r\n"
                #账户名称 用于实力化对象，切换不同账户调用合约
                #response_account=request_data[]
                # 应答体
                with open("."+request_path+".html", "rb") as f:
                    file_data = f.read()
                #file_data.replace("账户名称",response_account)
                response_body = file_data.encode()
                response_data = (response_line + response_header + "\r\n").encode() + response_body
                client_socket.send(response_data)
                client_socket.close()
            except Exception as e:
                # 应答行
                response_line = "HTTP/1.1 404 not found\r\n"
                # 应答头
                response_header = "Sever:pwb\r\n"
                # 用答题
                response_body = "404 not found"
                response_data = (response_line + response_header + response_body).encode()
                client_socket.send(response_data)
            finally:
                client_socket.close()
        elif request_path.endswith("_fun"):
            try:
                response_line = "HTTP/1.1 200 OK\r\n"
                # 应答头
                response_header = "Sever:pwb\r\n"
                #账户名称解析
                #response_account=request_data[]
                #函数参数解析
                json_param=json.loads(request_data[2])
                #fun_name = request_path[]
                fun_name=json_param["fun_name"]
                args=json_param
                # 应答体
                response_body = web_frame.application(request_path=request_path,fun_name=fun_name,args=args).encode()
                response_data = (response_line + response_header + "\r\n").encode() + response_body
                client_socket.send(response_data)
                client_socket.close()
            except Exception as e:
                # 应答行
                response_line = "HTTP/1.1 404 not found\r\n"
                # 应答头
                response_header = "Sever:pwb\r\n"
                # 应答体
                response_body = "404 not found"

                response_data = (response_line + response_header + "\r\n" + response_body).encode()
                client_socket.send(response_data)
            finally:
                client_socket.close()
        else:
            pass

    def start(self):
        while True:
            client_socket,client_addr = self.tcp_server_socket.accept()
            # 创建子线程
            sub_thread = threading.Thread(target=self.handle_client_request, args=(client_socket,))
            sub_thread.start()

if __name__ == '__main__':
    my_web_sever = HTTPWEBSEVER()
    my_web_sever.start()