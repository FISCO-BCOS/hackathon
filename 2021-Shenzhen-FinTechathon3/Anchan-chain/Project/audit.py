import traceback
import time
from typing import Tuple
from flask import request, render_template, redirect, session, make_response

from client.signer_impl import Signer_Impl

from blockchain import call_contract, call_contract2, create_account, deploy_contract, signin_account
from eth_utils.address import to_checksum_address

from __init__ import app
from models import Arbitrate, Audit, Contracts, Enterprise, IPFSObject, db, Agency, Engineer
from crypto import gen_rsakey

print(app.logger.handlers)


def login(username = "", password = "") -> Tuple[Audit, Signer_Impl]:
    if username == "":
        return None, None # input error
    audit = Audit.query.filter(Audit.username == username).first()
    if audit is None:
        # no such user
        return signup(username, password)

    if audit.password != password:
        return None, None # password error
    signer = signin_account(username, password)
    if signer is None:
        return None, None # password error
    return audit, signer

def signup(username, password) -> Tuple[Audit, Signer_Impl]:
    try:
        signer = create_account(username, password)
        audit = Audit(username = username, password = password)

        audit.account_addr = str(signer.keypair.address)
        audit.account_pub = str(signer.keypair.public_key)
        audit.account_priv = str(signer.keypair.private_key)

        privkey, pubkey = gen_rsakey()
        audit.envelope_pub = pubkey
        audit.envelope_priv = privkey

        managementAddr = db.session.query(Contracts).filter(Contracts.name == "Management").first().addr

        call_contract(managementAddr, "Management", "addAudit", 
            args = [
                username, 
                to_checksum_address(audit.account_addr),
                audit.envelope_pub, ""], signer = signer)

        db.session.add(audit)
        db.session.commit()
    except Exception as e:
        traceback.print_exc()
        db.session.rollback()
        return None, None
    return audit, signer

@app.route("/audit", methods=["POST", "GET"])
def audit():
    if request.method == "GET":
        username = session.get("username")
        password = session.get("password")
        if username is None:
            return render_template("audit2.html", is_login = False)
        audit, signer = login(username=username, password=password)
        if audit is None:
            return render_template("audit2.html", is_login = False)
        return render_template("audit2.html", is_login = True, audit=audit, username = username)

    username = request.form.get("name")
    password = request.form.get("password")
    audit, signer = login(username=username, password=password)

    if audit is None:
        return render_template("audit2.html", is_login = False, fail_msg = "登录失败")
    session["username"] = username
    session["password"] = password
    return render_template("audit2.html", is_login = True, succ_msg = "登录成功", audit = audit, username = username)

@app.route("/audit/dl_key")
def audit_dl_key():
    username = session.get("username", "")
    password = session.get("password", "")
    audit, signer = login(username, password)

    if audit is None:
        return redirect("/audit")

    response = make_response()

    response.data = audit.envelope_priv

    downloadFileName = 'privkey.pem'    
    response.headers['Content-Disposition'] = 'attachment; filename=' + downloadFileName
    response.mimetype = "application/x-pem-file;charset=UTF-8"
    return response

@app.route("/audit/dl_pub")
def audit_dl_pub():
    username = session.get("username", "")
    password = session.get("password", "")
    audit, signer = login(username, password)

    if audit is None:
        return redirect("/audit")

    response = make_response()
    response.data = audit.envelope_pub

    downloadFileName = 'pubkey.pem'    
    response.headers['Content-Disposition'] = 'attachment; filename=' + downloadFileName
    response.mimetype = "application/x-pem-file"
    return response

@app.route("/audit/revoke", methods = ["GET", "POST"])
def audit_revoke():
    username = session.get("username", "")
    password = session.get("password", "")
    audit, signer = login(username, password)

    if audit is None:
        return redirect("/audit")

    if request.method == "GET":
        return render_template("audit2-1.html", is_login = True ,audit = audit, username = username)

    license_addr = request.form.get("license-addr", None)
    try:
        if license_addr is not None or license_addr != "":
            call_contract(license_addr, "License", "revokeLicense", args=[])
    except Exception as e:
        traceback.print_exc()
        return render_template("audit2-1.html", is_login = True, fail_msg = "撤销失败",audit = audit, username = username)
    return render_template("audit2-1.html", is_login = True, succ_msg = "撤销成功",audit = audit, username = username)

@app.route("/audit/log", methods = ["GET", "POST"])
def audit_log():
    username = session.get("username", "")
    password = session.get("password", "")
    audit, signer = login(username, password)

    if audit is None:
        return redirect("/audit")

    if request.method == "GET":
        return render_template("audit2-2.html", is_login = True, audit = audit, username = username)
    
    log_type = request.form.get("log-type")
    keyword = request.form.get("keyword")
    if log_type is None or keyword is None or log_type == "" or keyword == "":
        return render_template("audit2-2.html", is_login = True, fail_msg = "查询失败",audit = audit, username = username)
    
    log_result = None
    if log_type == "agency":
        target = Agency.query.filter(Agency.username == keyword).first()
        if target is not None:
            try:
                res = call_contract2(target.contract_addr, "Agency", "showBusiness", args=[])
                log_result = res[0]
            except Exception as e:
                traceback.print_exc()
    elif log_type == "engineer":
        target = Engineer.query.filter(Engineer.username == keyword).first()
        EngineerListAddr = Contracts.query.filter(Contracts.name == "EngineerList").first().addr
        if target is not None:
            try:
                res = call_contract2(EngineerListAddr, "EngineerList", "getEngineer", args=[target.eid])
                log_result = res[0]
                log_result = log_result[5]
            except Exception as e:
                traceback.print_exc()
    if log_result is None:
        return render_template("audit2-2.html", is_login = True, fail_msg = "查询失败",audit = audit, username = username)
    
    result = []
    for l in log_result:
        try:
            time_local = time.localtime(l[0]/ 1000)
            result.append((time.strftime("%Y-%m-%d %H:%M:%S", time_local), l[1], l[2]))
        except Exception as e:
            result.append(l)

    return render_template("audit2-2.html", is_login = True, succ_msg = "查询成功",audit = audit, log_result = result,username = username)

@app.route("/audit/search", methods=["GET", "POST"])
def audit_search():
    username = session.get("username", "")
    password = session.get("password", "")
    audit, signer = login(username, password)

    if audit is None:
        return redirect("/audit")

    if request.method == "GET":
        return render_template("audit2-3.html", is_login = True, audit = audit, username = username)
    
    result = []
    keyword = request.form.get("keyword", "")

    result = IPFSObject.query.filter(IPFSObject.audit == audit.id).filter(IPFSObject.name.like(f"%{keyword}%")).all()

    return render_template("audit2-3.html", is_login = True, result = result, audit = audit, username = username, succ_msg = "查询成功")

@app.route("/audit/accusation")
def audit_accusation():
    username = session.get("username", "")
    password = session.get("password", "")
    audit, signer = login(username, password)

    if audit is None:
        return redirect("/audit")

    result = []
    try:
        AccusationAddr = Contracts.query.filter(Contracts.name == "Accusation").first().addr
        res = call_contract2(AccusationAddr, "Accusation", "getAccusation", args = [username])
        res = list(res[0])

        for r in res:
            if r[0] == "":
                result.append(["匿名",r[1]])
            else:
                result.append([r[0], r[1]])
    except Exception as e:
        traceback.print_exc()
        return render_template("audit2-4.html", is_login = True, fail_msg = "查询举报合约失败",audit = audit, username = username)
    return render_template("audit2-4.html", is_login = True, audit = audit, username = username, result = result, succ_msg="举报合约查询成功")

@app.route("/audit/arbitrate", methods=["GET", "POST"])
def audit_arbitrate():
    username = session.get("username", "")
    password = session.get("password", "")
    audit, signer = login(username, password)

    if audit is None:
        return redirect("/audit")

    arbitrate_list = Arbitrate.query.all()
    
    if request.method == "GET":
        return render_template("audit2-5.html", is_login = True, audit = audit, username = username, arbitrate_list = arbitrate_list)

    ent_addr = request.form.get("ent-addr", "") 

    if ent_addr == "":
        return render_template("audit2-5.html", is_login = True, audit = audit, username = username, arbitrate_list = arbitrate_list, fail_msg = "企业名称错误")

    enterprise = Enterprise.query.filter(Enterprise.username == ent_addr).first()
    if enterprise is None:
        return render_template("audit2-5.html", is_login = True, audit = audit, username = username, arbitrate_list = arbitrate_list, fail_msg = "没有该实体")

    if enterprise.license_addr is None or enterprise.license_addr == "":
        return render_template("audit2-5.html", is_login = True, audit = audit, username = username, arbitrate_list = arbitrate_list, fail_msg = "该企业暂无证书")
        
    try:
        arbitrate_addr = deploy_contract("Arbitrate", compile = False, fn_args = [to_checksum_address(enterprise.contract_addr)], signer = signer)

        arbitrate = Arbitrate(addr = arbitrate_addr)
        db.session.add(arbitrate)
        db.session.commit()

        arbitrate_list = Arbitrate.query.all()
    except Exception as e:
        traceback.print_exc()
        return render_template("audit2-5.html", is_login = True, audit = audit, username = username, arbitrate_list = arbitrate_list, fail_msg = "仲裁合约部署失败")
    
    return render_template("audit2-5.html", is_login = True, audit = audit, username = username, arbitrate_list = arbitrate_list)

@app.route("/arbitrate/<arbitrate_addr>")
def public_arbitrate(arbitrate_addr: str):
    username = session.get("username", "")
    password = session.get("password", "")
    audit, signer = login(username, password)

    if audit is None:
        return redirect("/audit")

    arbitrate_list = Arbitrate.query.all()

    arbirate = Arbitrate.query.filter(Arbitrate.addr == arbitrate_addr).first()
    if arbirate is None:
        return render_template("audit2-5.html", is_login = True, audit = audit, username = username, arbitrate_list = arbitrate_list, fail_msg = "仲裁合约地址错误")

    try:    
        managementAddr = Contracts.query.filter(Contracts.name == "Management").first().addr
        call_contract(arbitrate_addr, "Arbitrate", "getAuditList", args = [to_checksum_address(managementAddr)], signer = signer)

        res = call_contract(arbitrate_addr, "Arbitrate", "startAudition", signer = signer)
        name_list = list(res[0])
        pub_list = list(res[1])
        addr_list = list(res[2])
    except Exception as e:
        traceback.print_exc()
        return render_template("audit2-5.html", is_login = True, audit = audit, username = username, arbitrate_list = arbitrate_list, fail_msg = "仲裁合约查询失败")

    result = zip(name_list, addr_list, pub_list)
    return render_template("arbitrate2.html", is_login = True, audit = audit, username = username, result = result, arbitrate_addr = arbitrate_addr)

@app.route("/audit/check", methods = ["GET", "POST"])
def audit_check():
    username = session.get("username", "")
    password = session.get("password", "")
    audit, signer = login(username, password)

    if audit is None:
        return redirect("/audit")
    
    if request.method == "GET":
        return render_template("audit2-6.html", is_login = True, audit = audit, username = username)
    
    arbitrate_addr = request.form.get("arbitrate-addr")
    arbitrate_accept = request.form.get("arbitrate-accept")
    arbitrate_deny = request.form.get("arbitrate-deny")

    if arbitrate_addr is None:
        return render_template("audit2-6.html", is_login = True, audit = audit, username = username, fail_msg = "仲裁合约地址错误")

    arbirate = Arbitrate.query.filter(Arbitrate.addr == arbitrate_addr).first()
    if arbirate is None:
        return render_template("audit2-6.html", is_login = True, audit = audit, username = username, fail_msg = "仲裁合约地址错误")

    result = True if arbitrate_accept is not None else False
        
    try:
        if result:
            res = call_contract(arbitrate_addr, "Arbitrate", "confirm", signer = signer)
            app.logger.info(f"{res}")
            return render_template("audit2-6.html", is_login = True, audit = audit, username = username, succ_msg = "仲裁通过")
        res = call_contract(arbitrate_addr, "Arbitrate", "deny", signer = signer)
        app.logger.info(f"{res}")
        return render_template("audit2-6.html", is_login = True, audit = audit, username = username, succ_msg = "仲裁拒绝")
    except Exception:
        traceback.print_exc()
    return render_template("audit2-6.html", is_login = True, audit = audit, username = username, fail_msg = "仲裁执行失败")
    