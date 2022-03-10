from contracts.HelloWorld import HelloWorld

address = "0x7b6cb85c667c6ec8a4d81be837270ddfcf1838d5"
cli = HelloWorld(address)
(outputresult, receipt) = cli.set("testeraaa")
# outputresult  = si.data_parser.parse_receipt_output("set", receipt['output'])
print("receipt output :", outputresult)
logresult = cli.data_parser.parse_event_logs(receipt["logs"])
i = 0
for log in logresult:
    if 'eventname' in log:
        i = i + 1
        print("{}): log name: {} , data: {}".format(i, log['eventname'], log['eventdata']))
print(cli.get())
