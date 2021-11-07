import sys
from client.bcosclient import BcosClient
from client.datatype_parser import DatatypeParser
from client.contractnote import ContractNote
import json
import time
from client.channel_push_dispatcher import ChannelPushHandler
from client.event_callback import BcosEventCallback
from client.event_callback import EventCallbackHandler
from client_config import client_config


def usage():
    usagetext = '\nUsage:\nparams: contractname address event_name indexed\n' \
                '\t1. contractname :\t合约的文件名,不需要带sol后缀,默认在当前目录的contracts目录下\n' \
                '\t2. address :\t十六进制的合约地址,或者可以为:last 或 latest,表示采用bin/contract.ini里的记录\n' \
                '\t3. event_name :\t可选,如不设置监听所有事件 \n' \
                '\t4. indexed :\t可选,根据event定义里的indexed字段,作为过滤条件)\n\n'
    usagetext = usagetext + "\teg: for contract sample [contracts/HelloEvent.sol], use cmdline:\n\n"

    usagetext = usagetext + "\tpython demo_event_callback.py HelloEvent last \n"
    usagetext = usagetext + "\t--listen all event at all indexed ： \n\n"

    usagetext = usagetext + "\tpython demo_event_callback.py HelloEvent last on_set \n"
    usagetext = usagetext + "\t--listen event on_set(string newname) （no indexed）： \n\n"

    usagetext = usagetext + \
        "\tpython demo_event_callback.py HelloEvent last on_number 5\n"
    usagetext = usagetext + \
        "\t--listen event on_number(string name,int indexed age), age ONLY  5 ： \n"
    usagetext = usagetext + "\n...(and other events)"
    print(usagetext)


class EventCallbackImpl01(EventCallbackHandler):
    """sample event push handler for application level,
    user can make a class base on "ChannelPushHandler" ,implement the on_push interface
    handle the message from nodes,message in ChannelPack type #see client/channelpack.py
    EVENT_LOG_PUSH type is 0x1002
    message in pack.data decode by utf-8
    EVENT_LOG  format see https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/sdk/java_sdk.html#id19
    """
    abiparser: DatatypeParser = None

    def on_event(self, eventdata):
        loglist = self.abiparser.parse_event_logs(eventdata["logs"])
        print("- FilterID >>> ", eventdata["filterID"])
        print(
            "--------------------EventCallbackImpl01--------------------\n",
            json.dumps(loglist, indent=4))


class EventCallbackImpl02(ChannelPushHandler):
    abiparser: DatatypeParser = None

    def on_event(self, eventdata):
        loglist = self.abiparser.parse_event_logs(eventdata["logs"])
        print(">> FilterID ", eventdata["filterID"])
        print(">>>>>>>>>>>>>>>>>>EventCallbackImpl02", json.dumps(loglist, indent=4))


def main(argv):
    if len(argv) < 2:
        usage()
        exit(0)

    contractname = argv[0]
    address = argv[1]
    event_name = None
    indexed_value = None
    if len(argv) > 2:
        event_name = argv[2]
        indexed_value = argv[3:]
    try:
        bcos_event = BcosEventCallback()
        if client_config.client_protocol is not client_config.PROTOCOL_CHANNEL:
            print("** using event callback, client prototol MUST be client_config.PROTOCOL_CHANNEL!!")
            print("** please check the configure file")
            sys.exit(-1)

        bcos_event.setclient(BcosClient())
        print(bcos_event.client.getinfo())

        print("usage input {},{},{},{}".format(contractname, address, event_name, indexed_value))
        print(address)
        if address == "last" or address == "latest":
            cn = ContractNote()
            address = cn.get_last(contractname)
            print("hex address :", address)
        abifile = "contracts/" + contractname + ".abi"
        abiparser = DatatypeParser(abifile)
        eventcallback01 = EventCallbackImpl01()
        eventcallback02 = EventCallbackImpl02()
        eventcallback01.abiparser = abiparser
        eventcallback02.abiparser = abiparser

        result = bcos_event.register_eventlog_filter(
            eventcallback01, abiparser, [address], event_name, indexed_value)
        #result = bcos_event.register_eventlog_filter(eventcallback02,abiparser, [address], "on_number")

        print(
            "after register ,event_name:{},result:{},all:{}".format(
                event_name,
                result['result'], result))

        while True:
            print("waiting event...")
            time.sleep(10)
    except Exception as e:
        print("Exception!")
        import traceback
        traceback.print_exc()
    finally:
        print("event callback finished!")
        if bcos_event.client is not None:
            bcos_event.client.finish()
    sys.exit(-1)


if __name__ == "__main__":
    main(sys.argv[1:])
