from contracts.SimpleInfo import SimpleInfo
import json
address = "0x0b441629d3c6d297165a10a137cf105bfbf89e89"
si = SimpleInfo("")
result = si.deploy("contracts/SimpleInfo.bin")
address = result['contractAddress']
print("new address = ", address)

(outputresult, receipt) = si.set("testeraaa", 888, '0x7029c502b4f824d19bd7921e9cb74ef92392fb1F')
logresult = si.data_parser.parse_event_logs(receipt["logs"])
print("receipt output :", outputresult)
i = 0
for log in logresult:
    if 'eventname' in log:
        i = i + 1
        print("{}): log name: {} , data: {}".format(i, log['eventname'], log['eventdata']))

print("test setbalance")
(output, receipt) = si.setbalance(999)
json.dumps(receipt, indent=4)
logresult = si.data_parser.parse_event_logs(receipt["logs"])
print("receipt output :", outputresult)
i = 0
for log in logresult:
    if 'eventname' in log:
        i = i + 1
        print("{}): log name: {} , data: {}".format(i, log['eventname'], log['eventdata']))


print(si.getall())
print(si.getbalance1(100))
