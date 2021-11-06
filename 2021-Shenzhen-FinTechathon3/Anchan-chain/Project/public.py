
from flask import request, render_template, session
import traceback
import time


from __init__ import app
from blockchain import call_contract, call_contract2, to_checksum_address
from models import Agency, Arbitrate, Contracts, Enterprise, Audit, Engineer, count_numbers

def check_login():
    return session.get("username") is not None


@app.route("/license/<addr>")
def license(addr):
    is_login = check_login()

    try:
        res = call_contract(addr, "License", "getInfo", args = [])
        res2 = call_contract(addr, "License", "showInfo", args = [])
    except Exception as e:
        traceback.print_exc()
        return render_template("index2.html", is_login = is_login, fail_msg = "证书合约地址错误或合约调用失败", count = count_numbers())

    res2 = list(res2)
    license_info = list(res)
    license_info.extend(res2)
    time_local = None
    try:
        time_local = time.localtime(license_info[5]/ 1000)
    except:
        pass
    if time_local is not None:
        license_info[5] = time.strftime("%Y-%m-%d %H:%M:%S", time_local)
    try:
        agency = Agency.query.filter(Agency.contract_addr == license_info[10]).first()
        license_info[10] = agency.username
    except:
        pass
    
    try:
        engineer_list = []
        for e_eid in license_info[11]:
            engineer = Engineer.query.filter(Engineer.eid == e_eid).first()
            if engineer is not None:
                engineer_list.append(engineer.username)
            else:
                engineer_list.append(e_eid)
        license_info[11] = engineer_list
    except:
        pass
    return render_template("license2.html", is_login = is_login,license_info = license_info)

@app.route("/search", methods = ["GET", "POST"])
def search():
    is_login = check_login()

    if request.method == "GET":
        return render_template("search2.html", is_login = is_login)

    name = request.form.get("name", None)
    if name is None:
        return render_template("search2.html", is_login = is_login, fail_msg = "没有查询关键词")

    result  = Enterprise.query.filter(Enterprise.username.like(f"%{name}%")).all()
    return render_template("search2.html", is_login = is_login, result = result)

@app.route("/report", methods = ["GET", "POST"])
def search_report():
    is_login = check_login()

    if request.method == "GET":
        return render_template("report2.html", is_login = is_login)

    name = request.form.get("name", None)
    msg = request.form.get("msg", None)
    if name is None or msg is None or name == "" or msg == "":
        return render_template("report2.html", is_login = is_login, fail_msg = "举报对象或关键词为空")
    
    audit = Audit.query.filter(Audit.username == name).first()
    if audit is None:
        return render_template("report2.html", is_login = is_login, fail_msg = "未找到该监管部门")

    try:
        AccusationAddr = Contracts.query.filter(Contracts.name == "Accusation").first().addr
        call_contract(AccusationAddr, "Accusation", "addAccusation", args = ["", msg ,"", name])
    except Exception as e:
        traceback.print_exc()
        return render_template("report2.html", is_login = is_login, succ_msg = "举报合约执行失败")
    return render_template("report2.html", is_login = is_login, succ_msg = "举报成功，举报合约执行成功")


@app.route("/pubkey", methods = ["GET", "POST"])
def public_key():
    is_login = check_login()

    if request.method == "GET":
        return render_template("pubkey2.html", is_login = is_login)

    name = request.form.get("name", None)
    if name is None or name == "":
        return render_template("pubkey2.html", is_login = is_login, fail_msg = "输入错误")
   
    ent_type = request.form.get("ent-type", None)
    if ent_type is None or ent_type not in ["audit", "agency", "enterprise"]:
        return render_template("pubkey2.html", is_login = is_login, fail_msg = "实体类型错误")

    if ent_type == "enterprise":
        ent = Enterprise.query.filter(Enterprise.username == name).first()
    elif ent_type == "audit":
        ent = Audit.query.filter(Audit.username == name).first()
    else:
        ent = Agency.query.filter(Agency.username == name).first()

    if ent is None:
        return render_template("pubkey2.html", is_login = is_login, fail_msg = "未找到该实体")

    try:
        result = ent.envelope_pub
        if result is None:
            return render_template("pubkey2.html", is_login = is_login, fail_msg = "查询失败")
        result = str(result, encoding = "utf-8")
    except:
        return render_template("pubkey2.html", is_login = is_login, fail_msg = "查询失败")

    return render_template("pubkey2.html", succ_msg = "查询成功",is_login = is_login,  result = result)

@app.route("/credit", methods = ["GET", "POST"])
def credit():
    is_login = check_login()

    if request.method == "GET":
        return render_template("credit2.html", is_login = is_login)

    name = request.form.get("name", None)
    if name is None or name == "":
        return render_template("credit2.html", is_login = is_login, fail_msg = "输入错误")

    ent_type = request.form.get("ent-type", None)
    if ent_type is None or ent_type not in ["agency", "engineer"]:
        return render_template("credit2.html", is_login = is_login, fail_msg = "实体类型错误")

    if ent_type == "engineer":
        ent = Engineer.query.filter(Engineer.username == name).first()
    else:
        ent = Agency.query.filter(Agency.username == name).first()

    if ent is None:
        return render_template("credit2.html", is_login = is_login, fail_msg = "未找到该实体")

    result = None
    try:
        if ent_type == "engineer":
            
            engineerListAddr = Contracts.query.filter(Contracts.name == "EngineerList").first().addr
            CreditAddr = Contracts.query.filter(Contracts.name == "Credit").first().addr

            # call_contract(engineerListAddr,"EngineerList","getCreditContractAddr", args = [])

            call_contract(engineerListAddr,"EngineerList","setCreditContractAddr", args = [to_checksum_address(CreditAddr)])

            # call_contract(engineerListAddr,"EngineerList","getCreditContractAddr", args = [])

            call_contract(engineerListAddr, "EngineerList", "updateCredit", args = [ent.eid])
            res = call_contract(engineerListAddr, "EngineerList", "getCredit", args = [ent.eid])
            result = res[0]
        else:
            CreditAddr = Contracts.query.filter(Contracts.name == "Credit").first().addr

            call_contract(ent.contract_addr,"Agency","setCreditAddr", args = [to_checksum_address(CreditAddr)])
            call_contract(ent.contract_addr, "Agency", "updateCredit", args = [])
            res = call_contract(ent.contract_addr, "Agency", "getCredit", args = [])
            result = res[0]
    except:
        traceback.print_exc()
        return render_template("credit2.html", is_login = is_login, fail_msg = "查询失败")
    return render_template("credit2.html", is_login = is_login, succ_msg = "查询成功", result = result, name = name)
