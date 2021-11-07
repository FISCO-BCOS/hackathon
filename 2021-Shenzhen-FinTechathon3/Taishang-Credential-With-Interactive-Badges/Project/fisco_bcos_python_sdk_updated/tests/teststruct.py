import rlp
from client.bcostransactions import serializable_unsigned_transaction_from_dict
from client.datatype_parser import DatatypeParser
from eth_abi import encode_abi,decode_abi
from utils.contracts import encode_transaction_data, get_function_info
#from utils.contracts import encode_abi
from eth_utils import (
    function_signature_to_4byte_selector,
    event_abi_to_log_topic,
    encode_hex, decode_hex)

parser = DatatypeParser("contracts/TestStruct.abi")
print(parser)
fn_name = "addUser"
abi = parser.get_function_abi(fn_name)
args=[("alice",23)]
print(args)
#l = encode_abi(['bytes32', 'bytes32'], [b'a', b'b'])
#l = encode_abi([('string','uint256')],[args])

#print(encode_hex(l))
data = '0x0000000000000000000000000000000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000004000000000000000000000000000000000000000000000000000000000000000190000000000000000000000000000000000000000000000000000000000000005616c696365000000000000000000000000000000000000000000000000000000'



#p = decode_abi(['string','uint256'],decode_hex(data))
#print(p)

functiondata = encode_transaction_data(fn_name, parser.contract_abi, None, args)
print("functiondata",functiondata)
inputabi = ['(string,uint256)'];
decodedata = decode_abi(inputabi,decode_hex(data))
print("decodedata",decodedata)

import re
matchpattern = r"\(.*?\)"

s = '("bob1",88),("carol",11)'
res = re.split(matchpattern,s)
#print(res)
#print(res[2])
s ='[abc,"12,34"]'
ss = s.split(",")
#print(ss)

def split_param(inputparam:str):
    splitter = ','
    item = ""
    stopchar = ''
    arrayres=[]
    status = 0
    stopchardict = dict()
    stopchardict['\'']='\''
    stopchardict['"'] = '"'
    stopchardict['('] = ')'
    stopchardict['['] = ']'
    i=0
    oldstatus =0
    for i in range(0,len(inputparam)):
        c = inputparam[i]
        #print("c:[{}],itemis [{}],status {}".format(c,item,status) )
        if c == '\\': #转义字符\ ，跳过它，下一个字符默认纳入
            #print("skip [{}]".format(c))
            oldstatus =  status
            status = 2 #默认纳入下一个字符
            continue
        #print(c)
        if status ==0:
            if c == splitter: # 遇到分隔符,
                arrayres.append(item)
                item = ""
                continue
            if c in stopchardict:
                stopchar = stopchardict[c]
                status = 1 #status =1意思是这一段字符串必须到配对的stopchar才结束
            item = item + c
            continue
        if status == 1:
            item = item + c
            if c==stopchar:
                stopchar=''
                status = 0
            continue
        if status == 2:
            status = oldstatus
            item = item + c
            continue

    arrayres.append(item)
    return arrayres

s="'1\\',a',2,3,[(efg),(abc)]";
print("start ",s)
res = split_param(s)
print("---------")
for n in res:
    print(n)

print(split_param("1,2,3,4"))
print(split_param("1,2,3,4,[a,b,c],(11,22,33)"))
print(split_param("(aa,bb,cc),('xx','yy','zz'),(11,22,33)"))