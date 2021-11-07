# -*- coding: utf-8 -*-
import json
from flask import (
    Blueprint,
    current_app,
    flash,
    redirect,
    render_template,
    request,
    url_for,
    abort,
    jsonify,
    g
)

from taishang_badges_backend.extensions import db, csrf_protect
from taishang_badges_backend.settings import WEIDSERVICE_URL
from pyweidentity.weidentityService import weidentityService
from flask_cors import CORS

weid_bp = Blueprint("weid", __name__)

CORS(weid_bp)

class ValidationError(Exception):
    pass

@csrf_protect.exempt
@weid_bp.route("/create/weid", methods=["GET"])
def create_weid():
    weid = weidentityService(WEIDSERVICE_URL)
    weid_result = weid.create_weidentity_did()
    item = weid_result["respBody"].split(":")
    item[2] = "3"
    weid_return = ":".join(item)
    return jsonify({"weid": weid_return})

@csrf_protect.exempt
@weid_bp.route("/create/credential", methods=["POST"])
def create_credential():
    """
        创建 credential， 注意claim里的内容以及date的格式
    """
    weid = weidentityService(WEIDSERVICE_URL)

    data = request.get_json()
    
    cptId = data["cptId"]
    _weid = data["weid"]
    time = data["time"]
    claim = data["claim"]

    credential = weid.create_credentialpojo(cptId, _weid, time, claim, _weid)
    
    return jsonify(credential)


@csrf_protect.exempt
@weid_bp.route("/verify/credential", methods=["POST"])
def verify_credential():
    """
        根据用户上传的json文件 验证他上传的credential是否属于他。
    """
    weid = weidentityService(WEIDSERVICE_URL)
    
    for f in request.files:
        resBody = request.files.get(f).read()

    data = json.loads(resBody)
    
    cptId = data["cptId"]
    issuanceDate = data["issuanceDate"]
    context = data["context"]
    claim = data["claim"]
    _id = data["id"]
    proof = data["proof"]
    _type = data["type"]
    issuer = data["issuer"]
    expirationDate = data["expirationDate"]

    verify_credential_data = weid.verify_credentialpojo(cptId,issuanceDate,context,claim,_id,proof,_type,issuer,expirationDate)
    
    try:
        if verify_credential_data["respBody"] == True:
            return jsonify(data)
        return jsonify(
                {
                    "error": "Verify credential Failed",
                    "code": 400
                }
            )
    except:
        return jsonify(
                {
                    "error": "Verify credential Failed",
                    "code": 400
                }
            )

@csrf_protect.exempt
@weid_bp.route("/credential")
def get_credential():
    data = {
        "cptId": 100003,
        "issuanceDate": 1624611368,
        "context": "https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1",
        "claim": {
            "date": "2021-06-05",
            "evidence_addr": "0xeb634dc48cb1b21c9e13f1a35e9532097b18b8f2",
            "evidence_key": "0xd793822af0e8cc8e50fcd6aa1ec8bee356052734",
            "evidence_value": "{'resource':'75a6feff2aa8be5c70a16da7795a23b7', 'operator':'did:weid:1:0xd0e2d20b5681bbd17748f01f051e750f1d5adafb', 'action':'behavior.get.credit.credential'}",
            "extra": "",
            "lesson_name": "FISCO BCOS 金融联盟",
            "credit_level": 3,
            "name": "蔡聰明",
            "weid": "did:weid:1:0xd0e2d20b5681bbd17748f01f051e750f1d5adafb"
        },
        "id": "95c0a71f-fa5c-4955-90ec-a99a2b893990",
        "proof": {
            "created": 1624611368,
            "creator": "did:weid:1:0x6d938cb73ca1e952dfec18516eda7200bf5c383e#keys-0",
            "salt": {
                "date": "mGOCs",
                "evidence_addr": "cZVh3",
                "evidence_key": "XRXtv",
                "evidence_value": "ONXU8",
                "extra": "X4FLZ",
                "lesson_name": "VRint",
                "name": "fXSxU",
                "weid": "HWVtN"
            },
            "signatureValue": "cxQloEy98M5I4bT971DMDNcUjVmSSrmbPblHIaZdQzc/IxyV7KHKFGNWXnW6J/V3Nx6Vt5ldpa/+NPV/clUQUwA=",
            "type": "Secp256k1"
        },
        "type": [
            "VerifiableCredential",
            "original"
        ],
        "issuer": "did:weid:1:0x6d938cb73ca1e952dfec18516eda7200bf5c383e",
        "expirationDate": 4111737153
    }
    return jsonify(data)
