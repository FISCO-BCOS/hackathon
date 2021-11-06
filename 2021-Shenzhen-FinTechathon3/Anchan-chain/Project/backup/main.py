#!/usr/bin/env python3.7
# -*- coding: utf-8 -*-

from datetime import timedelta
import time
import sys
from werkzeug.utils import secure_filename

sys.path.append("/home/anchan/anchan-chain/python-sdk")

import os
import traceback
import random

from flask import Flask
from flask import render_template, request, session, redirect
from werkzeug.utils import secure_filename

from client.bcosclient import BcosClient
from client.bcoserror import BcosError, BcosException
from client_config import client_config
from eth_utils import to_checksum_address
from console_utils.cmd_account import CmdAccount
from client.signer_impl import Signer_ECDSA

from models import Enterprise, Agency, Engineer, Audit, IPFSObject
from models import ManagementAddr, EngineerListAddr, AccusationAddr
from models import count_numbers, AgencyList, EnterpriseList, AuditList
from config import debug
from shamir import shamir_encode, aes_encode

app = Flask(__name__)
app.config['SECRET_KEY'] = os.urandom(24)
app.config['PERMANENT_SESSION_LIFETIME'] = timedelta(days=7)  # 配置7天有效
app.config['UPLOAD_FOLDER'] = "data"


@app.route("/")
def index():
    return render_template("index2.html", count = count_numbers())

@app.route("/help")
def help():
    return render_template("help2.html")

@app.route("/logout")
def logout():
    try:
        del session["username"]
    except KeyError:
        pass
    return render_template("index2.html", succ_msg="退出成功", count = count_numbers())

@app.route("/enterprise", methods = ["GET", "POST"])
def enterprise():
    if request.method == "GET":
        username = session.get("username")
        if username is None or EnterpriseList.get(username) is None:
            return render_template("enterprise2.html", is_login = False)
        enterprise = EnterpriseList.get(username)
        password = enterprise.password
        try:
            signer = Signer_ECDSA.from_key_file(f"{client_config.account_keyfile_path}/{username}.keystore", password)
        except:
            return render_template("enterprise2.html", is_login = False)
        return render_template("enterprise2.html", is_login = True, enterprise = enterprise, username = username)

    username = request.form.get("name")
    password = request.form.get("password")
    if username is None or username == "":
        return render_template("enterprise2.html", is_login = False, fail_msg = "请输入用户名")
    if EnterpriseList.get(username) is not None:
        enterprise = EnterpriseList.get(username)
        try:
            signer = Signer_ECDSA.from_key_file(f"{client_config.account_keyfile_path}/{username}.keystore", password)
            succ_msg = "登录成功"
        except:
            fail_msg = "用户名或密码错误，登录失败"
            return render_template("enterprise2.html", is_login = False, fail_msg = fail_msg)
    else:
        ca = CmdAccount()
        ca.create_ecdsa_account(username, password)
        signer = Signer_ECDSA.from_key_file(f"{client_config.account_keyfile_path}/{username}.keystore", password)
        enterprise = Enterprise(username = username, password=password)
        EnterpriseList[username] = enterprise

        contract_addr = deploy_contract("Contracts/Enterprise", signer = signer)
        enterprise.contract_addr = contract_addr
        succ_msg = "注册成功"

    session["username"] = username
    enterprise.account_addr = signer.keypair.address
    enterprise.account_pub = signer.keypair.public_key
    enterprise.account_priv = signer.keypair.private_key

    return render_template("enterprise2.html", is_login = True, succ_msg = succ_msg, enterprise = enterprise, username = username)

@app.route("/agency", methods = ["GET", "POST"])
def agency():
    if request.method == "GET":
        username = session.get("username")
        if username is None or AgencyList.get(username) is None:
            return render_template("agency2.html", is_login = False)
        agency = AgencyList.get(username)
        password = agency.password
        try:
            signer = Signer_ECDSA.from_key_file(f"{client_config.account_keyfile_path}/{username}.keystore", password)
        except:
            return render_template("agency2.html", is_login = False)
        return render_template("agency2.html", is_login = True, agency = agency, username = username)

    username = request.form.get("name")
    password = request.form.get("password")
    if username is None or username == "":
        return render_template("agency2.html", is_login = False, fail_msg = "请输入用户名")
    if AgencyList.get(username) is not None:
        agency = AgencyList.get(username)
        try:
            signer = Signer_ECDSA.from_key_file(f"{client_config.account_keyfile_path}/{username}.keystore", password)
            succ_msg = "登录成功"
        except:
            fail_msg = "用户名或密码错误，登录失败"
            return render_template("agency2.html", is_login = False, fail_msg = fail_msg)
    else:
        ca = CmdAccount()
        ca.create_ecdsa_account(username, password)
        signer = Signer_ECDSA.from_key_file(f"{client_config.account_keyfile_path}/{username}.keystore", password)
        agency = Agency(username = username, password = password)
        AgencyList[username] = agency

        contract_addr = deploy_contract("Contracts/Agency", signer = signer)
        agency.contract_addr = contract_addr

        call_contract(contract_addr, "Agency", "setEngListAddr", args = [to_checksum_address(EngineerListAddr)], signer = signer)

        try:
            agency.EngineerListAddr = EngineerListAddr
        except:
            pass
        succ_msg = "注册成功"

    session["username"] = username
    agency.account_addr = signer.keypair.address
    agency.account_pub = signer.keypair.public_key
    agency.account_priv = signer.keypair.private_key
    return render_template("agency2.html", is_login = True, succ_msg = succ_msg, agency = agency, username = username)

@app.route("/enterprise/apply", methods=["POST"])
def enterprise_apply():
    username = session.get("username")
    if username is None or EnterpriseList.get(username) is None:
        return redirect("/enterprise")
        return render_template("enterprise2.html", is_login = False, fail_msg = "请先登录")
    enterprise = EnterpriseList.get(username)
    password = enterprise.password
    try:
        signer = Signer_ECDSA.from_key_file(f"{client_config.account_keyfile_path}/{username}.keystore", password)
    except:
        return redirect("/enterprise")
        return render_template("enterprise2.html", is_login = False)

    ent_name = request.form.get("ent-name", "")
    rep_name = request.form.get("rep-name", "")
    ent_addr = request.form.get("ent-addr", "")
    ent_type = request.form.get("ent-type", "")
    ent_range = request.form.get("ent-range", "")

    try:
        call_contract(enterprise.contract_addr, "Enterprise", "set_information", args = [ent_name, rep_name, ent_addr, ent_type, ent_range], signer = signer)
    except BcosError:
        return render_template("enterprise2.html", is_login = True, fail_msg = "添加信息失败", enterprise = enterprise, username = username)
    return render_template("enterprise2.html", is_login = True, succ_msg = "添加信息成功", enterprise = enterprise, username = username)

@app.route("/enterprise/evaluation")
def enterprise_evaluation():
    username = session.get("username")
    if username is None or EnterpriseList.get(username) is None:
        return redirect("/enterprise")
        return render_template("enterprise2.html", is_login = False, fail_msg = "请先登录")
    enterprise = EnterpriseList.get(username)
    password = enterprise.password
    try:
        signer = Signer_ECDSA.from_key_file(f"{client_config.account_keyfile_path}/{username}.keystore", password)
    except:
        return redirect("/enterprise")
        return render_template("enterprise2.html", is_login = False)

    evaluation_addr = enterprise.evaluation_addr
    if len(list(AgencyList.keys())) < 3:
        return render_template("enterprise2.html", is_login = True, fail_msg = "缺少足够的安评机构参与（至少3家安评机构)", enterprise = enterprise, username = username)
    if evaluation_addr is None:
        return render_template("enterprise2.html", is_login = True, fail_msg = "缺少审查合约地址", enterprise = enterprise, username = username)

    try:
        res = call_contract(evaluation_addr, "ReportEvaluation", "start_evaluation", args = [], signer = signer)
        ea_addr_list = res[0]
    except BcosError as e:
        traceback(e)
        return render_template("enterprise2.html", is_login = True, fail_msg = "合约调用失败", enterprise = enterprise, username = username)
    print(res)
    result = []
    for addr in ea_addr_list:
        ret_name = None
        for ag_name, ag in AgencyList.items():
            if ag.contract_addr == addr:
                ret_name = ag_name
                break
        result.append((ret_name, addr))
    audit = random.choice(list(AuditList.values()))
    result.append((audit.username, "")) 
    return render_template("evaluation2.html", evaluation_addr = evaluation_addr, result = result)

@app.route("/enterprise/result")
def enterprise_result():
    username = session.get("username")
    if username is None or EnterpriseList.get(username) is None:
        return redirect("/enterprise")
        return render_template("enterprise2.html", is_login = False, fail_msg = "请先登录")
    enterprise = EnterpriseList.get(username)
    password = enterprise.password
    try:
        signer = Signer_ECDSA.from_key_file(f"{client_config.account_keyfile_path}/{username}.keystore", password)
    except:
        return redirect("/enterprise")
        return render_template("enterprise2.html", is_login = False)

    evaluation_addr = enterprise.evaluation_addr
    if evaluation_addr is None:
        return render_template("enterprise2.html", is_login = True, fail_msg = "缺少审查合约地址", enterprise = enterprise, username = username)

    try:
        res = call_contract(evaluation_addr, "ReportEvaluation", "bussiness_update", args = [], signer = signer)
        license_addr = res[0]
    except BcosError as e:
        traceback(e)
        return render_template("enterprise2.html", is_login = True, fail_msg = "合约调用失败", enterprise = enterprise, username = username)

    if "0x0000000000" in license_addr:
        return render_template("enterprise2.html", is_login = True, fail_msg = "证书暂未通过审查", enterprise = enterprise, username = username)
    enterprise.license_addr = license_addr
    return render_template("enterprise2.html", is_login = True, succ_msg = "证书已上链", enterprise = enterprise, username = username)
        

@app.route("/agency/engineer", methods=["GET", "POST"])
def add_engineer():
    username = session.get("username")
    if username is None or AgencyList.get(username) is None:
        return redirect("/agency")
        return render_template("agency2.html", is_login = False, fail_msg = "请先登录")
    
    agency = AgencyList.get(username)
    password = agency.password
    try:
        signer = Signer_ECDSA.from_key_file(f"{client_config.account_keyfile_path}/{username}.keystore", password)
    except:
        return redirect("/agency")
        return render_template("agency2.html", is_login = False, fail_msg = "请重新登录")

    if request.method == "GET":
        return render_template("agency2-1.html", is_login = True, agency = agency, username = username)

    ename = request.form.get("name")
    eid = request.form.get("id", "")
    efield = request.form.get("field", "")

    if ename is None:
        return render_template("agency2-1.html", is_login = True, agency = agency, username = username, fail_msg = "添加失败")
    engineer = Engineer(username = ename, eid= eid, field= efield)

    res = call_contract(EngineerListAddr, "EngineerList", "addEngineer", args = [ename, eid, efield, ""], signer= signer)
    print(res)
    engineer.index = res[0]
    agency.engineers.append(engineer)
    return render_template("agency2-1.html", is_login = True, agency = agency, username = username, succ_msg = "添加成功")

@app.route("/agency/del_engineer/<int:index>")
def del_engineer(index: int):
    username = session.get("username")
    if username is None or AgencyList.get(username) is None:
        return redirect("/agency")
        return render_template("agency2.html", is_login = False, fail_msg = "请先登录")
    
    agency = AgencyList.get(username)
    password = agency.password
    try:
        signer = Signer_ECDSA.from_key_file(f"{client_config.account_keyfile_path}/{username}.keystore", password)
    except:
        return redirect("/agency")
        return render_template("agency2.html", is_login = False)

    try:
        res = call_contract(EngineerListAddr, "EngineerList", "deleteEngineer", args = [index], signer= signer)
    except BcosError as e:
        print(e)
        return render_template("agency2-1.html", is_login = True, agency = agency, username = username, fail_msg = "删除失败")
    except BcosException as e:
        print(e)
        return render_template("agency2-1.html", is_login = True, agency = agency, username = username, fail_msg = "删除失败")

    try:
        agency.engineers.pop(index)
    except:
        return render_template("agency2-1.html", is_login = True, agency = agency, username = username, fail_msg = "删除失败")
    return render_template("agency2-1.html", is_login = True, agency = agency, username = username, succ_msg = "删除成功")

@app.route("/agency/get_engineer/<int:index>")
def get_engineer(index: int):
    username = session.get("username")
    if username is None or AgencyList.get(username) is None:
        return redirect("/agency")
        return render_template("agency2.html", is_login = False, fail_msg = "请先登录")
    
    agency = AgencyList.get(username)
    password = agency.password
    try:
        signer = Signer_ECDSA.from_key_file(f"{client_config.account_keyfile_path}/{username}.keystore", password)
    except:
        return redirect("/agency")
        return render_template("agency2.html", is_login = False)

    try:
        client = BcosClient()
        res = client.call(EngineerListAddr, abis["EngineerList"], "getInfo", args = [index])
        client.finish()
    except BcosError as e:
        print(e)
        return render_template("agency2-1.html", is_login = True, agency = agency, username = username, fail_msg = "查询失败")
    except BcosException as e:
        print(e)
        return render_template("agency2-1.html", is_login = True, agency = agency, username = username, fail_msg = "查询失败")
    return render_template("engineer2.html", engineer_info = res, is_login = True, agency = agency, username = username, succ_msg = "查询成功")

@app.route("/agency/evaluation", methods = ["GET", "POST"])
def agency_evaluation():
    username = session.get("username")
    if username is None or AgencyList.get(username) is None:
        return redirect("/agency")
        return render_template("agency2.html", is_login = False, fail_msg = "请先登录")
    
    agency = AgencyList.get(username)
    password = agency.password
    try:
        signer = Signer_ECDSA.from_key_file(f"{client_config.account_keyfile_path}/{username}.keystore", password)
    except:
        return redirect("/agency")
        return render_template("agency2.html", is_login = False, fail_msg="请重新登录")
    
    if request.method == "GET":
        print(2333)
        return render_template("agency2-3.html", is_login = True, agency = agency, username = username)

    eva_addr = request.form.get("eva-addr")

    if eva_addr is None:
        return render_template("agency2-3.html", is_login = True, agency = agency, username = username, fail_msg = "缺少审查合约地址")
    
    try:
        call_contract(agency.contract_addr, "Agency", "confirm", args = [to_checksum_address(eva_addr)], signer = signer)
        call_contract(agency.contract_addr, "Agency", "addBusiness", [[], f"审查：审查合约地址{eva_addr}"], signer = signer)
    except BcosError as e:
        traceback(e)
        return render_template("agency2-3.html", is_login = True, agency = agency, username = username, fail_msg = "调用合约失败")
    return render_template("agency2-3.html", is_login = True, agency = agency, username = username, succ_msg = "审查成功")


@app.route("/audit", methods=["POST", "GET"])
def audit():
    if request.method == "GET":
        username = session.get("username")
        if username is None or AuditList.get(username) is None:
            return render_template("audit2.html", is_login = False)
        audit = AuditList.get(username)
        password = audit.password
        return render_template("audit2.html", is_login = True, audit=audit, username = username)

    username = request.form.get("name")
    password = request.form.get("password")
    if username is None or username == "":
        return render_template("audit2.html", is_login = False, fail_msg = "请输入用户名")
    audit = AuditList.get(username)
    if audit is not None:
        if password != audit.password:
            return render_template("audit2.html", is_login = False, fail_msg = "登录失败")
        succ_msg = "登录成功"
    else:
        audit = Audit(username= username, password = password)
        AuditList[username] = audit
        succ_msg = "注册成功"
    session["username"] = username
    return render_template("audit2.html", is_login = True, succ_msg = succ_msg, audit = audit, username = username)

@app.route("/audit/search", methods=["GET", "POST"])
def audit_search():
    username = session.get("username")
    if username is None or AuditList.get(username) is None:
        return redirect("/audit")
        return render_template("audit2.html", is_login = False)
    audit = AuditList.get(username)

    if request.method == "GET":
        return render_template("audit2-3.html", is_login = True, audit = audit, username = username)
    
    result = []
    keyword = request.form.get("keyword", "")
    if keyword != "":
        for k, v in audit.secret_database.items():
            if keyword in v.name:
                result.append(v)
            if keyword in v.hash:
                result.append(v)
    print(keyword, result, audit.secret_database)
    return render_template("audit2-3.html", is_login = True, result = result, audit = audit, username = username, succ_msg = "查询成功")

@app.route("/audit/revoke", methods = ["GET", "POST"])
def audit_revoke():
    username = session.get("username")
    if username is None or AuditList.get(username) is None:
        return redirect("/audit")
        return render_template("audit2.html", is_login = False)
    audit = AuditList.get(username)

    if request.method == "GET":
        return render_template("audit2-1.html", is_login = True ,audit = audit, username = username)

    license_addr = request.form.get("license-addr", None)
    try:
        if license_addr is not None or license_addr != "":
            call_contract(license_addr, "License", "revoke", args=[])
    except BcosError as e:
        traceback(e)
        return render_template("audit2-1.html", is_login = True, fail_msg = "撤销失败",audit = audit, username = username)
    return render_template("audit2-1.html", is_login = True, succ_msg = "撤销成功",audit = audit, username = username)

@app.route("/audit/accusation")
def audit_accusation():
    username = session.get("username")
    if username is None or AuditList.get(username) is None:
        return redirect("/audit")
        return render_template("audit2.html", is_login = False)
    audit = AuditList.get(username)
    result = []
    try:
        client = BcosClient()
        res = client.call(AccusationAddr, abis["Accusation"], "getAccusation", args = [username])
        client.finish()
        res = list(res[0])

        for r in res:
            if r[0] == "":
                result.append(["匿名",r[1]])
            else:
                result.append([r[0], r[1]])
    except BcosError as e:
        traceback(e)
        return render_template("audit2-4.html", is_login = True, fail_msg = "查询举报合约失败",audit = audit, username = username)
    return render_template("audit2-4.html", is_login = True, audit = audit, username = username, result = result, succ_msg="举报合约查询成功")

@app.route("/audit/log", methods = ["GET", "POST"])
def audit_log():
    username = session.get("username")
    if username is None or AuditList.get(username) is None:
        return redirect("/audit")
        return render_template("audit2.html", is_login = False)
    audit = AuditList.get(username)

    if request.method == "GET":
        return render_template("audit2-2.html", is_login = True, audit = audit, username = username)
    
    log_type = request.form.get("log-type")
    keyword = request.form.get("keyword")
    if log_type is None or keyword is None or log_type == "" or keyword == "":
        return render_template("audit2-2.html", is_login = True, fail_msg = "查询失败",audit = audit, username = username)
    
    log_result = None
    if log_type == "agency":
        target = None
        for agency_name, agency in AgencyList.items():
            if agency_name == keyword:
                target = agency
                break
        if target is not None:
            try:
                res = call_contract(target.contract_addr, "Agency", "showBusiness", args=[])
                log_result = res[0]
            except BcosError as e:
                traceback(e)
            except IndexError as e:
                traceback(e)

    elif log_type == "engineer":
        target = None
        for _, agency in AgencyList.items():
            if target is not None:
                break
            for e in agency.engineers:
                if e.username == keyword:
                    target = e
                    break
        if target is not None:
            try:
                client = BcosClient()
                res = client.call(EngineerListAddr, abis["EngineerList"], "getInfo", args = [target.index])
                client.finish()
                log_result = res[4]
            except BcosError as e:
                print(e)
            except IndexError as e:
                print(e)
    if log_result is None:
        return render_template("audit2-2.html", is_login = True, fail_msg = "查询失败",audit = audit, username = username)
    return render_template("audit2-2.html", is_login = True, succ_msg = "查询成功",audit = audit, log_result = log_result,username = username)

@app.route("/agency/upload", methods=["GET", "POST"])
def upload():
    username = session.get("username")
    if username is None or AgencyList.get(username) is None:
        return redirect("/agency")
        return render_template("agency2.html", is_login = False, fail_msg = "请先登录")
    agency = AgencyList.get(username)
    password = agency.password
    try:
        signer = Signer_ECDSA.from_key_file(f"{client_config.account_keyfile_path}/{username}.keystore", password)
    except:
        return redirect("/agency")
        return render_template("agency2.html", is_login = False)
    
    if request.method == "GET":
        return render_template("agency2-2.html", is_login = True, agency = agency, username = username)

    ent_name = request.form.get("ent-name")
    eng_list = request.form.get("eng-list", "")
    eng_list = [ int(e) for e in eng_list.split()]
    if ent_name is None:
        return render_template("agency2-2.html", is_login = True, agency = agency, username = username, fail_msg = "缺少生产企业名称")

    if len(list(AuditList.keys())) < 3:
        return render_template("agency2-2.html", is_login = True, agency = agency, username = username, fail_msg = "缺少足够的审查实体，至少需要3家审查实体")
    enterprise = EnterpriseList.get(ent_name)
    if enterprise is None:
        return render_template("agency2-2.html", is_login = True, agency = agency, username = username, fail_msg = "生产企业名称错误")
    
    data_file = request.files.get("data-file")
    report_file = request.files.get("report-file")
    if data_file is None or report_file is None:
        return render_template("agency2-2.html", is_login = True, agency = agency, username = username, fail_msg = "缺少企业材料或证书文件")
    if data_file.filename == "" or report_file.filename == "": 
        return render_template("agency2-2.html", is_login = True, agency = agency, username = username, fail_msg = "缺少企业材料或证书文件")

    data_file_path = os.path.join(app.config["UPLOAD_FOLDER"], secure_filename(data_file.filename))
    data_file.save(data_file_path)

    report_file_path = os.path.join(app.config["UPLOAD_FOLDER"], secure_filename(report_file.filename))
    report_file.save(report_file_path)

    try:
        n = len(AuditList)
        if n < 3:
            t = 1
        else:
            t = 3
        key, shares = shamir_encode(t, n)

        enc_data_path = os.path.join(app.config["UPLOAD_FOLDER"], "enc-"+secure_filename(data_file.filename))
        enc_report_path = os.path.join(app.config["UPLOAD_FOLDER"], "enc-"+secure_filename(report_file.filename))
        aes_encode(key, data_file_path, enc_data_path)
        aes_encode(key, report_file_path, enc_report_path)

        data_file_addr = ipfs_client.add(enc_data_path)
        report_file_addr = ipfs_client.add(enc_report_path)

        for i, audit in enumerate(list(AuditList.values())):
            obj_data = IPFSObject(hash = data_file_addr["Hash"], name = secure_filename(data_file.filename), secret =  shares[i][1].hex(), idx = shares[i][0])
            obj_report = IPFSObject(hash = report_file_addr["Hash"], name = secure_filename(report_file.filename), secret = shares[i][1].hex(), idx = shares[i][0])
            audit.secret_database[secure_filename(data_file.filename)] = obj_data
            audit.secret_database[secure_filename(report_file.filename)] = obj_report
    except Exception as e:
        traceback(e)
        return render_template("agency2-2.html", is_login = True, agency = agency, username = username, succ_msg = "IPFS上传失败")

    try:
        res = call_contract(enterprise.contract_addr, "Enterprise", "update", args=[data_file_addr["Hash"], report_file_addr["Hash"], to_checksum_address(agency.contract_addr), eng_list], signer = signer)
        enterprise.evaluation_addr = res[0]

        ag_addres = []
        for _, a in AgencyList.items():
            ag_addres.append(to_checksum_address(a.contract_addr))
        call_contract(enterprise.evaluation_addr, "ReportEvaluation", "addAgencyList", args = [ag_addres], signer = signer)
        call_contract(agency.contract_addr, "Agency", "addBusiness", [eng_list, f"评价：企业名称：{ent_name}"], signer = signer)

    except BcosError as e:
        traceback(e)
        return render_template("agency2-2.html", is_login = True, agency = agency, username = username, succ_msg = "智能合约调用失败")
    return render_template("agency2-2.html", is_login = True, agency = agency, username = username, succ_msg = "添加成功")

@app.route("/license/<addr>")
def license(addr):
    try:
        res = call_contract(addr, "License", "getInfo", args = [])
        res2 = call_contract(addr, "License", "show", args = [])
    except BcosError as e:
        return render_template("index2.html", fail_msg = "证书合约地址错误或合约调用失败", count = count_numbers())

    res2 = list(res2)
    license_info = list(res)
    license_info.extend(res2)
    time_local = None
    try:
        time_local = time.localtime(license_info[5]/ 1000)
    except:
        pass
    if time_local is not None:
        print(license_info[5], time_local)
        license_info[5] = time.strftime("%Y-%m-%d %H:%M:%S", time_local)
    return render_template("license2.html", license_info = license_info)

@app.route("/search", methods = ["GET", "POST"])
def search():
    if request.method == "GET":
        return render_template("search2.html")
    name = request.form.get("name", None)
    if name is None:
        return render_template("search2.html", fail_msg = "没有查询关键词")
    result = []
    for n,e in EnterpriseList.items():
        if name in n:
            result.append(e)
    return render_template("search2.html", result = result)
    
@app.route("/report", methods = ["GET", "POST"])
def search_report():
    if request.method == "GET":
        return render_template("report2.html")
    name = request.form.get("name", None)
    msg = request.form.get("msg", None)
    if name is None or msg is None or name == "" or msg == "":
        return render_template("report2.html", fail_msg = "举报对象或关键词为空")
    
    audit = AuditList.get(name) 
    if audit is None:
        return render_template("report2.html", fail_msg = "未找到该监管部门")

    try:
        call_contract(AccusationAddr, "Accusation", "addAccusation", args = ["", msg ,"", name])
    except BcosError as e:
        traceback(e)
        return render_template("report2.html", succ_msg = "举报合约执行失败")
    audit.reports.append(msg)
    return render_template("report2.html", succ_msg = "举报成功，举报合约执行成功")

if __name__ == "__main__":
    debug = True
    compile_and_abis()
    EngineerListAddr = deploy_contract("Contracts/EngineerList")
    AccusationAddr = deploy_contract("Contracts/Accusation")
    print("deployed EngineerList contract at:", EngineerListAddr)
    if debug:
        init_accounts()
    print("starting server")
    app.run(host="::",port=8888, debug= debug)

