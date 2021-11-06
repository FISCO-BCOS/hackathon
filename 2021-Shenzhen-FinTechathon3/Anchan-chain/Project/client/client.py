from typing import Dict, Optional

import requests
import logging
import argparse
import hashlib
import traceback

from requests.sessions import session

from config import base_url, output_dir
from model import User
import word

agency_url = base_url + "agency"
agency_upload_url = base_url + "agency/upload"

enterprise_url = base_url + "enterprise"
enterprise_upload_url = base_url + "enterprise/upload"

logout_url = base_url + "logout"

logging.basicConfig(level=logging.INFO, format="%(asctime)s %(levelname)s %(message)s")
headers = {
    'User-Agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36'
}

def login(user: User) -> requests.Session:
    s = requests.Session()
    s.headers.update(headers)

def logout(user: User, s: Optional[requests.Session] = None):
    if s is not None:
        pass

def init_session() -> requests.Session:
    s = requests.Session()
    s.headers.update(headers)
    return s

def enterprise_upload(args, user: User):
    filename = args.file
    if filename == "":
        print("缺少上传文件名")

    s = init_session()
    ret = s.post(enterprise_url, data = {"name": user.username, "password": user.password})

    if ret.status_code != 200 or "登录失败" in ret.text:
        print("登录失败，请检查用户名或密码")
        exit(1)
    try:
        files = {'data-file':(args.file, open(args.file, 'rb'))}

        with open(args.file, 'rb') as f:
            sha256obj = hashlib.sha256()
            sha256obj.update(f.read())
            hash_value = sha256obj.hexdigest()

        data = {
            "data-hash": hash_value
        }
        ret = s.post(url = enterprise_upload_url, data=data, files=files)
    except:
        traceback.print_exc()
        print("文件不存在或读取失败")
        exit(1)

    if ret.status_code != 200 or "登录失败" in ret.text:
        print("文件上传失败")
        exit(1)
    print("文件上传成功")

    print(f"进行自动化数据切片，切片存储到 {output_dir}")
    try:
        word.split_word(filename)
    except:
        traceback.print_exc()
        print("自动化数据分配失败")
        exit(1)
    exit(0)

def agency_upload(args, user: User):
    filename = args.file
    if filename == "":
        print("缺少上传文件名")
        exit(1)

    if args.enterprise == "":
        print("缺少企业名称")
        exit(1)

    s = init_session()
    ret = s.post(enterprise_url, data = {"name": user.username, "password": user.password})

    if ret.status_code != 200 or "登录失败" in ret.text:
        print("登录失败，请检查用户名或密码")
        exit(1)
    try:
        files = {'data-file':(args.file, open(args.file, 'rb'))}

        with open(args.file, 'rb') as f:
            sha256obj = hashlib.sha256()
            sha256obj.update(f.read())
            hash_value = sha256obj.hexdigest()

        data = {
            "data-hash": hash_value,
            "eng-list": args.englist,
            "ent-name": args.enterprise
        }
        ret = s.post(url = enterprise_upload_url, data=data, files=files)
    except:
        traceback.print_exc()
        print("文件不存在或读取失败")
        exit(1)

    if ret.status_code != 200 or "登录失败" in ret.text:
        print("文件上传失败")
        exit(1)
    print("文件上传成功")

    print(f"进行自动化数据切片，切片存储到 {output_dir}")
    try:
        word.split_word(filename)
    except:
        traceback.print_exc()
        print("自动化数据分配失败")
        exit(1)
    exit(0)

def arg_login(args):
    username = args.username or ""
    password = args.password or ""
    entity_type = args.entity or ""
    if username == "" or entity_type == "":
        print("need input username, password and entity arguments")
        exit(1)
    
    user = User(username = username, password = password, entity_type = entity_type, is_login= True)
    try:
        user.dump()
    except:
        print("dump user information error")
        exit(1)
    exit(0)

def arg_logout(args):
    try:
        user = User()
        user.try_load()
        if user.is_login:
            user.logout()
    except:
        print("logout error")
        exit(1)

def arg_upload(args):
    try:
        user = User()
        user.try_load()
        if not user.is_login:
            print("need to login first")
            exit(1)
    except:
        print("login error")
        exit(1)
    
    if user.entity_type == "enterprise":
        enterprise_upload(args, user)
    else:
        agency_upload(args, user)

def arg_default(args):
    print("need a sub command: login, logout, upload")
    exit(0)

def main():
    parser = argparse.ArgumentParser(description='Client for upload files and reports')
    parser.set_defaults(func=arg_default)

    sub_parsers = parser.add_subparsers(help='sub-commands')

    login_parser = sub_parsers.add_parser('login', help='login an account')
    login_parser.add_argument('-u', dest='username', help='username', default=  "")
    login_parser.add_argument('-p', dest='password', help='password', default= "")
    login_parser.add_argument('-e', dest='entity', help='enterprise or agency', default="")
    login_parser.set_defaults(func=arg_login)

    logout_parser = sub_parsers.add_parser('logout', help='logout an account')
    logout_parser.set_defaults(func=arg_logout)

    upload_parser = sub_parsers.add_parser('upload', help='upload report or data')
    upload_parser.add_argument('-f', dest='file', help='the uploading file')
    upload_parser.add_argument('-l', dest='englist', help='the engineer list', type=str, default="")
    upload_parser.add_argument('-e', dest='enterprise', help='企业名称', type=str, default="")
    upload_parser.set_defaults(func=arg_upload)


    args = parser.parse_args()
    args.func(args)

if __name__ == '__main__':
    main()
