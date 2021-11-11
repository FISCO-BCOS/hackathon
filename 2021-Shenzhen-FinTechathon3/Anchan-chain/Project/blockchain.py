import sys
import os
import json
from typing import List, Union
import traceback
import re

# Python SDK path
sys.path.append("/home/ubuntu/anchan/python-sdk")

from client.bcosclient import BcosClient
from client.bcoserror import BcosError, BcosException
from client.datatype_parser import DatatypeParser
from client.common.compiler import Compiler
from eth_utils import to_checksum_address
from client.signer_impl import Signer_ECDSA, Signer_Impl
from eth_account.account import Account

from client_config import client_config
from models import Contracts, Engineer, db
from config import debug
from __init__ import app

ContractsList = ["Agency", "Arbitrate", "Credit","EngineerList", "Enterprise", "License", "Management","ReportEvaluation", "Accusation"]

UniqueContractsList = ["Accusation", "Credit", "EngineerList", "Management"]

def create_account(name, password = ""):
    ac = Account.create(password)
    kf = Account.encrypt(ac.privateKey, password)
    keyfile = "{}/{}.keystore".format(client_config.account_keyfile_path, name)

    # if os.access(keyfile, os.F_OK):
    #     common.backup_file(keyfile)

    with open(keyfile, "w") as dump_f:
        json.dump(kf, dump_f)
        dump_f.close()

    return signin_account(name, password)

def signin_account(username, password) -> Union[Signer_Impl, None]:
    try:
        signer = Signer_ECDSA.from_key_file(f"{client_config.account_keyfile_path}/{username}.keystore", password)
    except Exception:
        return None
    return signer

def compile_and_abis(is_compile: bool = True):
    """
    Compiles all contracts and generates abi and bin files 
    """
    for c in ContractsList:
        if is_compile:
            Compiler.compile_file(f"Contracts/{c}.sol", output_path="Contracts")

def deploy_contract(contract, is_compile: bool = False, signer: Signer_Impl = None, fn_args = None):
    """
    Args:
        contract: the contract's name, e.g.: "EngineerList"
        is_compile (bool): compile or not
    Returns:
        the contract address
    """
    if is_compile and (os.path.isfile(client_config.solc_path) or os.path.isfile(client_config.solcjs_path)):
        Compiler.compile_file(f"Contracts/{contract}.sol", output_path="Contracts")

    data_parser = DatatypeParser()
    data_parser.load_abi_file(f"Contracts/{contract}.abi")

    client = BcosClient()
    try:
        with open(f"Contracts/{contract}.bin", 'r') as load_f:
            contract_bin = load_f.read()
            load_f.close()
        result = client.deploy(contract_bin, contract_abi = data_parser.contract_abi, fn_args= fn_args ,from_account_signer=signer)
        addr = result["contractAddress"]
    except BcosError:
        traceback.print_exc()
        return None
    except BcosException:
        traceback.print_exc()
        return None
    except Exception:
        traceback.print_exc()
        return None
    finally:
        client.finish()
    app.logger.info(f"deploy contract {contract} at {addr}")
    return addr

def call_contract(contract_addr: str, contract_name: str, fn_name: str, args: List = None, signer: Signer_Impl = None, gasPrice = 30000000):
    client = BcosClient()

    data_parser: DatatypeParser = DatatypeParser()
    data_parser.load_abi_file(f"Contracts/{contract_name}.abi")
    contract_abi = data_parser.contract_abi

    receipt = client.sendRawTransactionGetReceipt(to_address = contract_addr, contract_abi= contract_abi, fn_name = fn_name, args = args,from_account_signer= signer, gasPrice= gasPrice)

    if receipt["status"] != "0x0":
        msg = receipt.get("statusMsg", "")
        app.logger.warn(f"call contract {contract_name} at {contract_addr}. {fn_name} ({args}) error:{msg}")
        app.logger.warn(f"receipt: {receipt}")
        print(msg)
        raise Exception(f"contract error: {msg}")
    txhash = receipt['transactionHash']
    txresponse = client.getTransactionByHash(txhash)
    inputresult = data_parser.parse_transaction_input(txresponse['input'])
    outputresult = data_parser.parse_receipt_output(inputresult['name'], receipt['output'])
    client.finish()
    app.logger.info(f"call contract {contract_name} at {contract_addr}. {fn_name} ({args}) -> {outputresult}")
    return outputresult

def call_contract2(contract_addr: str, contract_name: str, fn_name: str, args: List = None, signer: Signer_Impl = None):
    client = BcosClient()
    if signer is not None:
        client.default_from_account_signer = signer

    data_parser: DatatypeParser = DatatypeParser()
    data_parser.load_abi_file(f"Contracts/{contract_name}.abi")
    contract_abi = data_parser.contract_abi

    ret = client.call(contract_addr, contract_abi, fn_name, args)
    app.logger.info(f"call contract {contract_name} at {contract_addr}. {fn_name} ({args}) -> {ret}")
    client.finish()
    return ret

def init_accounts():
    password = ""
    enterprise_name_list = [
        ("E1液体化工公司", "赵E1", "某某省某某市", "有限责任公司", "原油储运"),
    ]
    for elem in enterprise_name_list:
        name = elem[0]

        import enterprise
        ent, signer = enterprise.signup(name, password)
        call_contract(ent.contract_addr, "Enterprise", "setInformation", args = [name, elem[1], elem[2], elem[3], elem[4]], signer = signer)
        app.logger.info(f"创建账户:{ent}")

    agency_name_list = ["A1化工安全评价机构", "A2化工安全评价机构", "A3化工安全评价机构","A4化工安全评价机构","A5化工安全评价机构","A6化工安全评价机构","A7化工安全评价机构","A8化工安全评价机构","A9化工安全评价机构","A10化工安全评价机构"]
    engineerNames = ["罗*","梁*","宋*","唐*","许*","韩*","冯*","邓*","曹*","彭*",
        "曾*","萧*","田*","董*","袁*","潘*","于*","蒋*","蔡*","余*",
        "杜*","叶*","程*","苏*","魏*","吕*","丁*","任*","沈*","姚*",
        "卢*","姜*","崔*","钟*","谭*","陆*","汪*","范*","金*","石*",
        "廖*","贾*","夏*","韦*","付*","方*","白*","邹*","孟*","熊*",]
    nameIndex=0
    pattern = re.compile(r'A\d+')
    for name in agency_name_list:
        import agency
        ag, signer = agency.signup(name, password)

        EngineerListAddr = db.session.query(Contracts).filter(Contracts.name == "EngineerList").first().addr
        IDHead = pattern.match(ag.username).group()
        for i in range(1,4):
            engineerID=IDHead+"E"+str(i)
            engineerName=engineerNames[nameIndex]

            engineer = Engineer(username =engineerName , eid= engineerID, field= "化工安全")
            call_contract(EngineerListAddr, "EngineerList", "addEngineer", args = [engineerName, engineerID, "化工安全", "", ag.username], signer= signer)
            ag.engineers.append(engineer)
            db.session.add(engineer)
            db.session.add(ag)
            db.session.commit()
            nameIndex +=1
        app.logger.info(f"创建账户:{ag}")

    audit_name_list = ["G1监管部门", "G2监管部门", "G3监管部门", "G4监管部门", "G5监管部门","G6监管部门","G7监管部门","G8监管部门","G9监管部门","G10监管部门"]
    for name in audit_name_list:
        import audit
        au, signer = audit.signup(name, password)
        app.logger.info(f"创建账户:{au}")
    
def init():
    """
    compile all contracts and deploy common contracts
    """

    app.logger.warn("clean databases")
    db.drop_all()
    db.create_all()

    app.logger.warn("compile all contracts")
    compile_and_abis(is_compile = True)

    for c_name in UniqueContractsList:
        addr =deploy_contract(c_name)
        c = Contracts(name = c_name, addr = addr)
        db.session.add(c)
        db.session.commit()

    EngineerListAddr = Contracts.query.filter(Contracts.name == "EngineerList").first().addr
    CreditAddr = Contracts.query.filter(Contracts.name == "Credit").first().addr

    call_contract(EngineerListAddr,"EngineerList","setCreditContractAddr", args = [to_checksum_address(CreditAddr)])
    
    if debug:
        init_accounts()

if __name__ == "__main__":
    init()
