#!/usr/bin/env python3.7
# -*- coding: utf-8 -*-

import os
from datetime import timedelta
import logging
from flask import Flask

app = Flask(__name__)
app.config['SECRET_KEY'] = os.urandom(24)
app.config['PERMANENT_SESSION_LIFETIME'] = timedelta(days=7)  # 配置7天有效
app.config['UPLOAD_FOLDER'] = "data"
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///db.sqlite3'

app.logger.setLevel(logging.DEBUG)

handler = logging.FileHandler('server.log', encoding='UTF-8')
logging_format = logging.Formatter(
    '[%(asctime)s] %(levelname)s in %(module)s: %(message)s')
handler.setFormatter(logging_format)
app.logger.addHandler(handler)
handler.setLevel(logging.NOTSET)
