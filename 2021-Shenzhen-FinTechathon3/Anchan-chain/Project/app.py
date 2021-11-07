#!/usr/bin/env python3.7
# -*- coding: utf-8 -*-

from flask import render_template, session, request
import logging

from __init__ import app
from config import host, port, debug

from models import Enterprise, db, count_numbers, Agency
from blockchain import compile_and_abis, call_contract
import enterprise
import agency
import audit
import public

@app.route("/")
def index():
    is_login = public.check_login()
    return render_template("index2.html", is_login = is_login, count = count_numbers())

@app.route("/help")
def help_handle():
    is_login = public.check_login()
    return render_template("help2.html", is_login = is_login)

@app.route("/logout")
def logout():
    try:
        del session["username"]
        del session["password"]
    except KeyError:
        pass
    return render_template("index2.html", succ_msg="退出成功", is_login = False, count = count_numbers())

@app.errorhandler(404)
def handle_404(e):
    is_login = public.check_login()
    return render_template('error2.html', is_login = is_login, error_code = "404, Not Found", error_msg = "您访问的页面不存在，请返回其他页面"), 404

@app.errorhandler(403)
def handle_403(e):
    is_login = public.check_login()
    return render_template('error2.html', is_login = is_login, error_code = "403, Forbidden", error_msg = "您访问的页面受限，请返回其他页面"), 403

@app.errorhandler(500)
def handle_500(e):
    is_login = public.check_login()
    return render_template('error2.html', is_login = is_login, error_code = "500, Internal Server Error", error_msg = "服务器错误，请稍后重试"), 500

if __name__ == "__main__":
    compile_and_abis(compile=False)
    
    app.logger.info(f"starting server at {host}:{port} (debug={debug})")
    app.run(host=host, port=port, debug=debug)
