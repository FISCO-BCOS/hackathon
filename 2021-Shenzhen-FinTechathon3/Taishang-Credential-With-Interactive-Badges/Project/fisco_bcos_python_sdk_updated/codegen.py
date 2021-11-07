#!/usr/bin/env python
# -*- coding: utf-8 -*-

'''
  FISCO BCOS/Python-SDK is a python client for FISCO BCOS
   (https://github.com/FISCO-BCOS/FISCO-BCOS)
  FISCO BCOS/Python-SDK is free software: you can redistribute it and/or modify it
  under the terms of the MIT License as published by the Free Software Foundation
  This project is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE
  Thanks for authors and contributors of eth-abi，eth-account，eth-hash，eth-keys，eth-typing，
  eth-utils，rlp, eth-rlp , hexbytes ...and relative projects
  @author: kentzhang
  @date: 2019-07
'''

import sys
from client.datatype_parser import DatatypeParser
from utils.abi import (
    filter_by_type
)
import os
import json


class ABICodegen:
    parser = DatatypeParser()
    abi_file = ""
    name = ""
    # four spaces for indent
    indent = "    "
    template = ""
    template_file = "client/codegen_template.py"

    def __init__(self, abi_file):
        if len(abi_file) > 0:
            self.load_abi(abi_file)
        with open(self.template_file, "r") as f:
            self.template = f.read()
            f.close()

    def load_abi(self, abi_file):
        self.abi_file = abi_file
        self.parser.load_abi_file(abi_file)
        fname = os.path.basename(abi_file)
        (self.name, ext) = os.path.splitext(fname)

    def make_function(self, func_abi):
        func_lines = []
        func_def = "def {}(self".format(func_abi["name"])
        args_def = ""
        args_value = ""
        i = 0
        for param in func_abi["inputs"]:
            if i > 0:
                args_def += ", "
                args_value += ", "
            args_def += param["name"]
            if param['type'] == "address":
                args_value += "to_checksum_address({})".format(param["name"])
            else:
                args_value += param["name"]
            i += 1
        if len(args_def) > 0:
            func_def += ", " + args_def
        func_def += "):"
        func_lines.append(func_def)
        func_lines.append("{}func_name = '{}'".format(self.indent, func_abi["name"]))
        func_lines.append("{}args = [{}]".format(self.indent, args_value))
        if func_abi["constant"] is False:
            func_lines.append(self.indent + ("receipt = self.client.sendRawTransactionGetReceipt"
                                             "(self.address, self.contract_abi, func_name, args)"))
            func_lines.append(self.indent + ("outputresult = self.data_parser.parse_receipt_output"
                                             "(func_name, receipt['output'])"))
            func_lines.append(self.indent + "return outputresult, receipt")

        if func_abi["constant"] is True:
            func_lines.append(self.indent + ("result = self.client.call(self.address, "
                                             "self.contract_abi, func_name, args)"))
            func_lines.append(self.indent + "return result")
        return func_lines

    def gen_all(self):
        all_func_code_line = []
        func_abi_list = filter_by_type("function", self.parser.contract_abi)
        for func_abi in func_abi_list:
            func_lines = self.make_function(func_abi)
            all_func_code_line.append("")
            all_func_code_line.append("# ------------------------------------------")
            all_func_code_line.extend(func_lines)
            # print(func_lines)
        template = self.template
        template = template.replace("TEMPLATE_CLASSNAME", self.name)
        template = template.replace("TEMPLATE_ABIFILE", self.abi_file)
        contract_abi = json.dumps(self.parser.contract_abi).replace("\r", "")
        contract_abi = contract_abi.replace("\n", " ")
        template = template.replace("TEMPLATE_CONTRACT_ABI", contract_abi)
        for line in all_func_code_line:
            if not line:
                template += "\n"
            else:
                template += self.indent + line + "\n"
        return template


def usage():
    usagetext = '''usage:
        python codegen.py [abifile(eg: ./contracts/SimpleInfo.abi)]
        [outputpath(eg: ./contracts/)] '''
    print(usagetext)


if __name__ == '__main__':
    if(len(sys.argv) < 3):
        usage()
        sys.exit(0)
    abi_file = sys.argv[1]
    outputdir = sys.argv[2]
    forcewrite = False
    if(len(sys.argv) == 4):
        isSave = sys.argv[3]
        if isSave == "save":
            forcewrite = True
    codegen = ABICodegen(abi_file)
    template = codegen.gen_all()
    print(template)
    name = codegen.name + '.py'
    outputfile = os.path.join(outputdir, name)
    print(" output file : {}".format(outputfile))

    if os.access(outputfile, os.F_OK) and forcewrite is False:
        str = input(">> file [{}] exist , continue (y/n): ".format(outputfile))
        if str.lower() == "y":
            forcewrite = True
        else:
            forcewrite = False
    else:
        forcewrite = True
    if forcewrite:
        with open(outputfile, "wb") as f:
            f.write(bytes(template, "utf-8"))
            f.close()
        print("write {} done".format(outputfile))
