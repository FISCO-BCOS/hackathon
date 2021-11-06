import blockchain
# from server.models import Enterprise 
# reportEvaluationAddress = "0x28f59b06f91e2a6ff8b5c5b83c12753555e3c7c3"
managementAddress = "0x1a8fc94ec54a39766fdd8a3453d0a62c7c37853e"
enterpriseAddress = blockchain.deploy_contract('Enterprise',False)
blockchain.deploy_contract('ReportEvaluation',True)
print(enterpriseAddress)
reportEvaluationAddress = blockchain.call_contract(enterpriseAddress,"Enterprise","update",args = ["123","123",blockchain.to_checksum_address(enterpriseAddress),[]])
print(reportEvaluationAddress[0])
print(blockchain.call_contract(reportEvaluationAddress[0],"ReportEvaluation","getAgencyList",args = [blockchain.to_checksum_address(managementAddress)]))
# print("after getAgencyList")
print(blockchain.call_contract(reportEvaluationAddress[0],"ReportEvaluation","startEvaluation"))
print(blockchain.call_contract(reportEvaluationAddress[0],"ReportEvaluation","startEvaluation"))




