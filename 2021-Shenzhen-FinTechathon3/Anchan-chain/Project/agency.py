import os
from flask import request, render_template, session, redirect, make_response
from typing import Tuple
import traceback

from werkzeug.utils import secure_filename

from __init__ import app
from client.signer_impl import Signer_Impl
from eth_utils.address import to_checksum_address
from models import Contracts, Engineer, Enterprise, Audit, Agency,IPFSObject,Arbitrate, db, count_numbers
from blockchain import signin_account, create_account, deploy_contract, call_contract, call_contract2
from crypto import gen_rsakey, shamir_encode, aes_encode
from ipfs import ipfs_client

def login(username, password) -> Tuple[Agency, Signer_Impl]:
    if username == "":
        return None, None # input error
    agency = Agency.query.filter(Agency.username == username).first()
    if agency is None:
        # no such user
        return signup(username, password)

    if agency.password != password:
        return None, None # password error
    signer = signin_account(username, password)
    if signer is None:
        return None, None # password error
    return agency, signer

def signup(username, password) -> Tuple[Agency, Signer_Impl]:
    try:
        signer = create_account(username, password)

        agency = Agency(username = username, password = password)

        contract_addr = deploy_contract("Agency", signer = signer)
        agency.contract_addr = contract_addr
        agency.account_addr = str(signer.keypair.address)
        agency.account_pub = str(signer.keypair.public_key)
        agency.account_priv = str(signer.keypair.private_key)

        privkey, pubkey = gen_rsakey()
        agency.envelope_pub = pubkey
        agency.envelope_priv = privkey

        managementAddr = db.session.query(Contracts).filter(Contracts.name == "Management").first().addr
        EngineerListAddr = db.session.query(Contracts).filter(Contracts.name == "EngineerList").first().addr
        creditAddr = db.session.query(Contracts).filter(Contracts.name == "Credit").first().addr

        call_contract(contract_addr, "Agency", "setEngListAddr", args = [to_checksum_address(EngineerListAddr)], signer = signer)
        call_contract(contract_addr, "Agency", "setCreditAddr", args = [to_checksum_address(creditAddr)], signer = signer)
        agency.engineer_list_addr = EngineerListAddr

        # call_contract2(managementAddr, "Management", "addAgency", 
        #     args = [
        #         username, 
        #         to_checksum_address(agency.account_addr),
        #         to_checksum_address(agency.contract_addr),
        #         agency.envelope_pub, "" ], signer = signer)

    
        call_contract(managementAddr, "Management", "addAgency", 
            args = [
                username, 
                to_checksum_address(agency.account_addr),
                to_checksum_address(agency.contract_addr),
                agency.envelope_pub, "" ], signer = signer)

        db.session.add(agency)
        db.session.commit()
    except Exception as e:
        traceback.print_exc()
        db.session.rollback()
        return None, None
    return agency, signer

@app.route("/agency", methods = ["GET", "POST"])
def agency():
    if request.method == "GET":
        username = session.get("username", "")
        password = session.get("password", "")
        agency, signer = login(username, password)
        if agency is None:
            return render_template("agency2.html", is_login = False)
        return render_template("agency2.html", is_login = True, agency = agency, username = username)

    username = request.form.get("name")
    password = request.form.get("password")
    agency, signer = login(username, password)
    if agency is None:
        return render_template("agency2.html", is_login = False, fail_msg = "登录失败")
    
    session["username"] = username
    session["password"] = password
    return render_template("agency2.html", is_login = True, succ_msg = "登录成功", agency = agency, username = username)

@app.route("/agency/engineer", methods=["GET", "POST"])
def add_engineer():
    username = session.get("username", "")
    password = session.get("password", "")
    agency, signer = login(username, password)

    if agency is None:
        return redirect("/agency")

    if request.method == "GET":
        return render_template("agency2-1.html", is_login = True, agency = agency, username = username)

    ename = request.form.get("name")
    eid = request.form.get("id", "")
    efield = request.form.get("field", "")

    if ename is None:
        return render_template("agency2-1.html", is_login = True, agency = agency, username = username, fail_msg = "名称不能为空")

    try:
        EngineerListAddr = db.session.query(Contracts).filter(Contracts.name == "EngineerList").first().addr

        engineer = Engineer(username = ename, eid= eid, field= efield)
        call_contract(EngineerListAddr, "EngineerList", "addEngineer", args = [ename, eid, efield, "", agency.username], signer= signer)
        agency.engineers.append(engineer)

        db.session.add(engineer)
        db.session.add(agency)
        db.session.commit()
    except Exception as e:
        traceback.print_exc()
        return render_template("agency2-1.html", is_login = True, agency = agency, username = username, fail_msg = "添加失败")
    return render_template("agency2-1.html", is_login = True, agency = agency, username = username, succ_msg = "添加成功")

@app.route("/agency/get_engineer/<index>")
def get_engineer(index: str):
    username = session.get("username", "")
    password = session.get("password", "")
    agency, signer = login(username, password)

    if agency is None:
        return redirect("/agency")
    
    try:
        EngineerListAddr = db.session.query(Contracts).filter(Contracts.name == "EngineerList").first().addr
        res = call_contract2(EngineerListAddr, "EngineerList", "getEngineer", args = [index])
        res = res[0]
    except Exception as e:
        traceback.print_exc()
        return render_template("agency2-1.html", is_login = True, agency = agency, username = username, fail_msg = "查询失败")
    return render_template("engineer2.html", engineer_info = res, is_login = True, agency = agency, index = index, username = username, succ_msg = "查询成功")

@app.route("/agency/del_engineer/<index>")
def del_engineer(index: str):
    username = session.get("username", "")
    password = session.get("password", "")
    agency, signer = login(username, password)

    if agency is None:
        return redirect("/agency")

    try:
        EngineerListAddr = db.session.query(Contracts).filter(Contracts.name == "EngineerList").first().addr
        res = call_contract(EngineerListAddr, "EngineerList", "deleteAgency", args = [index], signer= signer)

        engineer = Engineer.query.filter(Engineer.eid == index).first()
        if engineer is not None:
            agency.engineers.remove(engineer)
            db.session.commit()

    except Exception as e:
        traceback.print_exc()
        return render_template("agency2-1.html", is_login = True, agency = agency, username = username, fail_msg = "删除失败")
    return render_template("agency2-1.html", is_login = True, agency = agency, username = username, succ_msg = "删除成功")

@app.route("/agency/dl_key")
def dl_key():
    username = session.get("username", "")
    password = session.get("password", "")
    agency, signer = login(username, password)

    if agency is None:
        return redirect("/agency")

    response = make_response()

    response.data = agency.envelope_priv

    downloadFileName = 'privkey.pem'    
    response.headers['Content-Disposition'] = 'attachment; filename=' + downloadFileName
    response.mimetype = "application/x-pem-file;charset=UTF-8"
    return response

@app.route("/agency/dl_pub")
def dl_pub():
    username = session.get("username", "")
    password = session.get("password", "")
    agency, signer = login(username, password)

    if agency is None:
        return redirect("/agency")

    response = make_response()
    response.data = agency.envelope_pub

    downloadFileName = 'pubkey.pem'    
    response.headers['Content-Disposition'] = 'attachment; filename=' + downloadFileName
    response.mimetype = "application/x-pem-file"
    return response

@app.route("/agency/upload", methods=["GET", "POST"])
def upload():
    username = session.get("username", "")
    password = session.get("password", "")
    agency, signer = login(username, password)

    if agency is None:
        return redirect("/agency")
    
    if request.method == "GET":
        return render_template("agency2-2.html", is_login = True, agency = agency, username = username)

    ent_name = request.form.get("ent-name")
    eng_list = request.form.get("eng-list", "")
    eng_list = [ e for e in eng_list.split()]
    data_hash = request.form.get("data-hash")
    if ent_name is None:
        return render_template("agency2-2.html", is_login = True, agency = agency, username = username, fail_msg = "缺少生产企业名称")

    _,_,aal,_ = count_numbers()
    if aal < 3:
        return render_template("agency2-2.html", is_login = True, agency = agency, username = username, fail_msg = "缺少足够的审查实体，至少需要3家审查实体")

    enterprise = Enterprise.query.filter(Enterprise.username == ent_name).first()
    if enterprise is None:
        return render_template("agency2-2.html", is_login = True, agency = agency, username = username, fail_msg = "生产企业名称错误")
    
    data_file = request.files.get("data-file")

    if data_file is None:
        return render_template("agency2-2.html", is_login = True, agency = agency, username = username, fail_msg = "缺少上传文件")
    if data_file.filename == "" : 
        return render_template("agency2-2.html", is_login = True, agency = agency, username = username, fail_msg = "缺少上传文件")

    data_file_path = os.path.join(app.config["UPLOAD_FOLDER"], secure_filename(data_file.filename))
    data_file.save(data_file_path)

    try:
        n = aal
        t = 3
        key, shares = shamir_encode(t, n)

        enc_data_path = os.path.join(app.config["UPLOAD_FOLDER"], "enc-"+secure_filename(data_file.filename))
        aes_encode(key, data_file_path, enc_data_path)

        data_file_addr = ipfs_client.add(enc_data_path)

        for i, audit in enumerate(Audit.query.all()):

            obj_data = IPFSObject(hash = data_file_addr["Hash"], name = secure_filename(data_file.filename), secret =  shares[i][1].hex(), idx = shares[i][0])
            audit.files.append(obj_data)
            db.session.add(obj_data)
            db.session.commit()
    except Exception as e:
        traceback(e)
        return render_template("agency2-2.html", is_login = True, agency = agency, username = username, succ_msg = "IPFS上传失败")

    try:
        res = call_contract(enterprise.contract_addr, "Enterprise", "update", args=[data_hash, data_file_addr["Hash"], to_checksum_address(agency.contract_addr), eng_list], signer = signer)
        enterprise.evaluation_addr = res[0]
        db.session.commit()

        res = call_contract(enterprise.evaluation_addr, "ReportEvaluation", "bussinessUpdate", args = [] , signer = signer)
        licenseAddr = res[0]
        enterprise.license_addr = licenseAddr
        db.session.commit()

    except Exception as e:
        traceback.print_exc()
        return render_template("agency2-2.html", is_login = True, agency = agency, username = username, succ_msg = "智能合约调用失败")
    return render_template("agency2-2.html", is_login = True, agency = agency, username = username, succ_msg = "添加成功")

@app.route("/agency/evaluation", methods = ["GET", "POST"])
def agency_evaluation():
    username = session.get("username", "")
    password = session.get("password", "")
    agency, signer = login(username, password)

    if agency is None:
        return redirect("/agency")
    
    if request.method == "GET":
        return render_template("agency2-3.html", is_login = True, agency = agency, username = username)

    eva_addr = request.form.get("eva-addr")
    eva_accept = request.form.get("eva-accept")
    eva_deny = request.form.get("eva-deny")
    
    if eva_accept is not None:
        result = True
    else:
        result = False

    eng_list = request.form.get("eng-list", "")
    eng_list = [ e for e in eng_list.split()]

    if eva_addr is None:
        return render_template("agency2-3.html", is_login = True, agency = agency, username = username, fail_msg = "缺少审查合约地址")
    
    try:
        if result:
            call_contract(agency.contract_addr, "Agency", "confirm", args = [to_checksum_address(eva_addr), eng_list], signer = signer)
        else:
            res = call_contract(agency.contract_addr, "Agency", "deny", args = [to_checksum_address(eva_addr), eng_list], signer = signer)

            arbitrate_addr = res[0]
            if "0x000000000000000" in arbitrate_addr:
                raise Exception(msg = "Error arbitrate contract address")
            app.logger.info(f"{arbitrate_addr}")
            arbitrate = Arbitrate(addr = arbitrate_addr)
            db.session.add(arbitrate)
            db.session.commit()
        # call_contract(agency.contract_addr, "Agency", "addBusiness", [[], f"审查：审查合约地址{eva_addr}"], signer = signer)
    except Exception as e:
        traceback.print_exc()
        return render_template("agency2-3.html", is_login = True, agency = agency, username = username, fail_msg = "调用合约失败")

    if result:
        return render_template("agency2-3.html", is_login = True, agency = agency, username = username, succ_msg = "审查通过")
    return render_template("agency2-3.html", is_login = True, agency = agency, username = username, succ_msg = "审查拒绝")