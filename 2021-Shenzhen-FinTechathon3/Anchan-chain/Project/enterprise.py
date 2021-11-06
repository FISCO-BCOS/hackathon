from logging import Manager
import os
import random
from flask import request, render_template, session, redirect
from typing import List, Tuple
import traceback

from werkzeug.utils import secure_filename

from __init__ import app
from client.signer_impl import Signer_Impl
from eth_utils.address import to_checksum_address
from models import Contracts, Engineer, Enterprise, Audit, Agency, IPFSObject, db, count_numbers
from blockchain import signin_account, create_account, deploy_contract, call_contract, call_contract2
from crypto import gen_rsakey, shamir_encode, aes_encode
from ipfs import ipfs_client
def login(username, password) -> Tuple[Enterprise, Signer_Impl]:
    if username == "":
        return None, None # input error
    enterprise = Enterprise.query.filter(Enterprise.username == username).first()
    if enterprise is None:
        # no such user
        return signup(username, password)

    if enterprise.password != password:
        return None, None # password error
    signer = signin_account(username, password)
    if signer is None:
        return None, None # password error
    return enterprise, signer

def signup(username, password) -> Tuple[Enterprise, Signer_Impl]:
    try:
        signer = create_account(username, password)

        enterprise = Enterprise(username = username, password = password)

        contract_addr = deploy_contract("Enterprise", signer = signer)
        enterprise.contract_addr = contract_addr
        enterprise.account_addr = str(signer.keypair.address)
        enterprise.account_pub = str(signer.keypair.public_key)
        enterprise.account_priv = str(signer.keypair.private_key)

        privkey, pubkey = gen_rsakey()
        enterprise.envelope_pub = pubkey
        enterprise.envelope_priv = privkey

        managementAddr = db.session.query(Contracts).filter(Contracts.name == "Management").first().addr
        call_contract(managementAddr, "Management", "addEnterprise", 
            args = [
                username, 
                to_checksum_address(enterprise.account_addr),
                to_checksum_address(enterprise.contract_addr),
                enterprise.envelope_pub, "" ])

        db.session.add(enterprise)
        db.session.commit()
    except Exception as e:
        traceback.print_exc()
        db.session.rollback()
        return None, None
    return enterprise, signer

@app.route("/enterprise", methods = ["GET", "POST"])
def enterprise():
    if request.method == "GET":
        username = session.get("username", "")
        password = session.get("password", "")
        enterprise, signer = login(username, password)
        if enterprise is None or signer is None:
            return render_template("enterprise2.html", is_login = False)
        return render_template("enterprise2.html", is_login = True, enterprise = enterprise, username = username)

    username = request.form.get("name", "")
    password = request.form.get("password", "")
    enterprise, signer = login(username, password)

    if enterprise is None or signer is None:
        return render_template("enterprise2.html", is_login = False, fail_msg = "登录失败")
    
    session["username"] = username
    session["password"] = password
    return render_template("enterprise2.html", is_login = True, succ_msg = "登录成功", enterprise = enterprise, username = username)

@app.route("/enterprise/apply", methods=["POST"])
def enterprise_apply():
    username = session.get("username", "")
    password = session.get("password", "")

    enterprise, signer = login(username, password)
    if enterprise is None:
        return redirect("/enterprise")

    ent_name = request.form.get("ent-name", "")
    rep_name = request.form.get("rep-name", "")
    ent_addr = request.form.get("ent-addr", "")
    ent_type = request.form.get("ent-type", "")
    ent_range = request.form.get("ent-range", "")


    try:
        enterprise = Enterprise.query.filter(Enterprise.username == username).first()
        enterprise.ent_name = ent_name
        enterprise.rep_name = rep_name
        enterprise.ent_addr = ent_addr
        enterprise.ent_type = ent_type
        enterprise.ent_range = ent_range

        db.session.add(enterprise)
        db.session.commit()
        call_contract(enterprise.contract_addr, "Enterprise", "setInformation", args = [ent_name, rep_name, ent_addr, ent_type, ent_range], signer = signer)
    except Exception:
        db.session.rollback()
        return render_template("enterprise2.html", is_login = True, fail_msg = "添加信息失败", enterprise = enterprise, username = username)
    return render_template("enterprise2.html", is_login = True, succ_msg = "添加信息成功", enterprise = enterprise, username = username)

@app.route("/enterprise/evaluation")
def enterprise_evaluation():
    username = session.get("username", "")
    password = session.get("password", "")

    enterprise, signer = login(username, password)
    if enterprise is None:
        return redirect("/enterprise")

    evaluation_addr = enterprise.evaluation_addr
    _,al,_,_ = count_numbers()
    if al < 3:
        return render_template("enterprise2.html", is_login = True, fail_msg = "缺少足够的安评机构参与（至少3家安评机构)", enterprise = enterprise, username = username)
    if evaluation_addr is None:
        return render_template("enterprise2.html", is_login = True, fail_msg = "缺少审查合约地址", enterprise = enterprise, username = username)

    try:
        managementAddr = db.session.query(Contracts).filter(Contracts.name == "Management").first().addr

        call_contract(evaluation_addr, "ReportEvaluation", "getAgencyList", args = [to_checksum_address(managementAddr)])

        res = call_contract(evaluation_addr, "ReportEvaluation", "startEvaluation", signer = signer)
        ea_addr_list: List[str] = list(res[0])
    except Exception as e:
        traceback.print_exc()
        return render_template("enterprise2.html", is_login = True, fail_msg = "合约调用失败", enterprise = enterprise, username = username)

    result = []
    for addr in ea_addr_list:
        ag = Agency.query.filter(Agency.contract_addr == addr).first()
        if ag is not None:
            result.append((ag.username, addr, str(ag.envelope_pub,encoding = "utf-8")))
    # audit = random.choice(list(AuditList.values()))
    # result.append((audit.username, "")) 
    return render_template("evaluation2.html", evaluation_addr = evaluation_addr, result = result)

@app.route("/enterprise/result")
def enterprise_result():
    username = session.get("username", "")
    password = session.get("password", "")

    enterprise, signer = login(username, password)
    if enterprise is None:
        return redirect("/enterprise")

    evaluation_addr = enterprise.evaluation_addr
    if evaluation_addr is None:
        return render_template("enterprise2.html", is_login = True, fail_msg = "缺少审查合约地址", enterprise = enterprise, username = username)

    try:
        res = call_contract(evaluation_addr, "ReportEvaluation", "bussinessUpdate", args = [], signer = signer)
        license_addr = res[0]
    except Exception as e:
        return render_template("enterprise2.html", is_login = True, fail_msg = "合约调用失败", enterprise = enterprise, username = username)

    if "0x0000000000" in license_addr:
        return render_template("enterprise2.html", is_login = True, fail_msg = "证书暂未通过审查", enterprise = enterprise, username = username)
    enterprise.license_addr = license_addr

    db.session.add(enterprise)
    db.session.commit()
    return render_template("enterprise2.html", is_login = True, succ_msg = "证书已上链", enterprise = enterprise, username = username)


@app.route("/enterprise/upload", methods=["GET", "POST"])
def enterprise_upload():
    username = session.get("username", "")
    password = session.get("password", "")
    enterprise, signer = login(username, password)

    if enterprise is None:
        return redirect("/enterprise")
    
    if request.method == "GET":
        return render_template("enterprise2-1.html", is_login = True, enterprise = enterprise, username = username)


    data_hash = request.form.get("data-hash")
    data_file = request.files.get("data-file")

    if data_file is None:
        return render_template("enterprise2-1.html", is_login = True, enterprise = enterprise, username = username, fail_msg = "缺少上传文件")
    if data_file.filename == "" : 
        return render_template("enterprise2-1.html", is_login = True, enterprise = enterprise, username = username, fail_msg = "缺少上传文件")

    data_file_path = os.path.join(app.config["UPLOAD_FOLDER"], secure_filename(data_file.filename))
    data_file.save(data_file_path)

    try:
        _, _, n, _ = count_numbers()
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
        return render_template("enterprise2-1.html", is_login = True, enterprise = enterprise, username = username, succ_msg = "IPFS上传失败")

    try:
        call_contract(enterprise.contract_addr, "Enterprise", "updateData", args=[data_hash, data_file_addr["Hash"]], signer = signer)
    except Exception as e:
        traceback(e)
        return render_template("enterprise2-1.html", is_login = True, enterprise = enterprise, username = username, succ_msg = "智能合约调用失败")
    return render_template("enterprise2-1.html", is_login = True, enterprise = enterprise, username = username, succ_msg = "添加成功")