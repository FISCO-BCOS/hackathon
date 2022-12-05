import deploy
import json

#路由列表
fun_list={}
print(fun_list)
#路由装饰器
def route(data):   #请求的页面/参数
    def fun_out(fun):
        fun_list[data] = fun
        def fun_inner(args,fun_name=None):
            result = fun(args, fun_name)
            return result
        return fun_inner
    return fun_out

#企业相关路由
# @route("enterpriseView")
# def fun_call(fun_name,args):
#     ret=Contract_institution.call(fun_name=fun_name,client_use=client_use_qy,args=args)
#     return ret

@route("enterpise_viewall")
def fun_1(args,fun_name=None):
    #查询企业上传数据每个dataName对应数量
    ret1=deploy.Contract_institution.call(fun_name="enterpriseView",client_use=deploy.client_use_qy,args=[args[""],args[""]])
    #查询企业在某份上传数据中的dataName名单
    ret2=deploy.Contract_institution.call(fun_name= "dataNameView",client_use=deploy.client_use_qy,args=[args[""],args[""]])
    #查看剩余碳排放量
    ret3=deploy.Contract_institution.call(fun_name= "ViewRight",client_use=deploy.client_use_qy,args=[args[""],args[""]])
    #对数据进行解析
    param1 = ret1["output"]
    param2 = ret2["output"]
    param3 = ret3["output"]
    json_list = [{"每个dataName对应数量":str(param1),"dataName名单":str(param2),"剩余碳排放量":str(param3)}]
    json_list=json.dumps(json_list)
    print(json_list)
    return json_list

#政府相关路由
#给企业分配初始碳排放权
@route("govermentWrite")
def fun_1(fun_name,args):
    ret=deploy.ontract_Gov.sendRawTransaction(fun_name,client_use=deploy.client_use_gov,args=[args[""],args[""]])
    #解析数据  拿到交易hash
    param1 = ret[0]
    param2 = ret[1]
    json_list = [{"交易hash": str(param1), "output": str(param2)}]
    json_list = json.dumps(json_list)
    print(json_list)
    return json_list


#公布不同行业碳排放标准
@route("publishStandard")
def fun_2(fun_name,args):
    #返回企业名单
    ret=deploy.Contract_Gov.sendRawTransaction(fun_name,client_use=deploy.client_use_gov,args=[args[""],args[""]])
    #解析数据拿到交易hash
    param1 = ret[0]
    param2 = ret[1]
    json_list = [{"交易hash": str(param1), "output": str(param2)}]
    json_list = json.dumps(json_list)
    print(json_list)
    return json_list

# @route("enterpriseNameView")
# def fun_call(fun_name,args):
#     #返回企业名单
#     ret=Contract_Gov.call(fun_name,client_use=client_use_gov,args=args)
#     return ret[]

@route("gov_viewall")
def fun_4(args,fun_name=None):
    Contract_Gov = deploy.Contract("text_bin", "text_abi_file", "text_sol")
    #返回企业名单
    ret1=Contract_Gov.call(fun_name="enterpriseNameView",client_use=deploy.client_use_gov,args=[args[""],args[""]])
    #查询企业数据
    ret2=Contract_Gov.call(fun_name= "enterpriseView",client_use=deploy.client_use_gov,args=[args[""],args[""]])
    #查看企业碳排放权及剩余碳排放量
    ret3=Contract_Gov.call(fun_name= "ViewRight",client_use=deploy.client_use_gov,args=[args[""],args[""]])
    #对数据进行解析
    param1 = ret1["output"]
    param2 = ret2["output"]
    param3 = ret3["output"]
    json_list = [{"企业名单": str(param1), "企业数据": str(param2), "企业碳排放权及剩余碳排放量": str(param3)}]
    json_list = json.dumps(json_list)
    print(json_list)
    return json_list

#Audit相关路由
@route("uploadReport")
def fun_5(fun_name,args):
    #返回企业名单
    ret=deploy.Contract_data.sendRawTransaction(fun_name,client_use=deploy.client_use_data,args=args)
    #解析数据拿到交易hash
    param1 = ret[0]
    param2 = ret[1]
    json_list = [{"交易hash": str(param1), "output": str(param2)}]
    json_list = json.dumps(json_list)
    print(json_list)
    return json_list

#将判断也解耦合
def application(request_path,fun_name,args):
    #解析参数列表
    func = fun_list[request_path]
    ret = func(fun_name=fun_name,args=args)
    return ret