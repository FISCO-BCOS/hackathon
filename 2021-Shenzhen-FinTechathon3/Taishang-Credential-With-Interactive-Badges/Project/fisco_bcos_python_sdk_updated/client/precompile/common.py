'''
  FISCO BCOS/Python-SDK is a python client for FISCO BCOS2.0 (https://github.com/FISCO-BCOS/)
  FISCO BCOS/Python-SDK is free software: you can redistribute it and/or modify it under the
  terms of the MIT License as published by the Free Software Foundation. This project is
  distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Thanks for
  authors and contributors of eth-abi, eth-account, eth-hash，eth-keys, eth-typing, eth-utils,
  rlp, eth-rlp , hexbytes ... and relative projects
  @file: consensus_precompile.py
  @function:
  @author: yujiechen
  @date: 2019-07
'''


class PrecompileCommon:
    """
    define common values related to precompile
    """
    SYS_TABLE = "_sys_tables_"
    SYS_TABLE_KEY = "table_name"
    USER_TABLE_PREFIX = "u_"
    SYS_TABLE_ACCESS = "_sys_table_access_"
    SYS_CONSENSUS = "_sys_consensus_"
    SYS_CNS = "_sys_cns_"
    SYS_CONFIG = "_sys_config_"
    # define the error information
    error_code = {}
    error_code["-50000"] = "PermissionPrecompiled: Permission Denied"
    error_code["-50001"] = "CRUD: Table Exist"
    error_code["-51502"] = "CRUD: Undefined operator"
    error_code["-51501"] = "CRUD: Parse condition error"
    error_code["-51500"] = "CRUD: Parse Entry error"
    error_code["-51000"] = "PermissionPrecompiled: Already Granted"
    error_code["-51001"] = "PermissionPrecompiled: TableName And Account Not Exist"
    error_code["-51100"] = "Invalid NodeId"
    error_code["-51101"] = "Last Sealer"
    error_code["-51102"] = "P2p Network"
    error_code["-51103"] = "Group Peers"
    error_code["-51104"] = "Sealer List"
    error_code["-51105"] = "Observer List"
    error_code["-51200"] = "CNS: ContractNameAndVersion Exist"
    error_code["-51201"] = "CNS: Version Exceeds"
    error_code["-51300"] = "SysConfig: Invalid Key, must be tx_gas_limit or tx_count_limit"
