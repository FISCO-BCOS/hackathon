module.exports = require("protobufjs").newBuilder({})["import"]({
    "package": "GtReq",
    "messages": [
        {
            "name": "GtAuth",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "sign",
                    "id": 1
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "appkey",
                    "id": 2
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "int64",
                    "name": "timestamp",
                    "id": 3
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "seqId",
                    "id": 4
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "GtAuthResult",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "int32",
                    "name": "code",
                    "id": 1
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "redirectAddress",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "seqId",
                    "id": 3
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "info",
                    "id": 4
                },
                {
                    "rule": "repeated",
                    "options": {},
                    "type": "string",
                    "name": "appid",
                    "id": 5
                }
            ],
            "enums": [
                {
                    "name": "GtAuthResultCode",
                    "values": [
                        {
                            "name": "successed",
                            "id": 0
                        },
                        {
                            "name": "failed_noSign",
                            "id": 1
                        },
                        {
                            "name": "failed_noAppkey",
                            "id": 2
                        },
                        {
                            "name": "failed_noTimestamp",
                            "id": 3
                        },
                        {
                            "name": "failed_AuthIllegal",
                            "id": 4
                        },
                        {
                            "name": "redirect",
                            "id": 5
                        }
                    ],
                    "options": {}
                }
            ],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "ReqServList",
            "fields": [
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "seqId",
                    "id": 1
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "int64",
                    "name": "timestamp",
                    "id": 3
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "ReqServListResult",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "int32",
                    "name": "code",
                    "id": 1
                },
                {
                    "rule": "repeated",
                    "options": {},
                    "type": "string",
                    "name": "host",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "seqId",
                    "id": 3
                }
            ],
            "enums": [
                {
                    "name": "ReqServHostResultCode",
                    "values": [
                        {
                            "name": "successed",
                            "id": 0
                        },
                        {
                            "name": "failed",
                            "id": 1
                        },
                        {
                            "name": "busy",
                            "id": 2
                        }
                    ],
                    "options": {}
                }
            ],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "PushListResult",
            "fields": [
                {
                    "rule": "repeated",
                    "options": {},
                    "type": "PushResult",
                    "name": "results",
                    "id": 1
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "PushResult",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "EPushResult",
                    "name": "result",
                    "id": 1
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "taskId",
                    "id": 2
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "messageId",
                    "id": 3
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "seqId",
                    "id": 4
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "target",
                    "id": 5
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "info",
                    "id": 6
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "traceId",
                    "id": 7
                }
            ],
            "enums": [
                {
                    "name": "EPushResult",
                    "values": [
                        {
                            "name": "successed_online",
                            "id": 0
                        },
                        {
                            "name": "successed_offline",
                            "id": 1
                        },
                        {
                            "name": "successed_ignore",
                            "id": 2
                        },
                        {
                            "name": "failed",
                            "id": 3
                        },
                        {
                            "name": "busy",
                            "id": 4
                        },
                        {
                            "name": "success_startBatch",
                            "id": 5
                        },
                        {
                            "name": "success_endBatch",
                            "id": 6
                        },
                        {
                            "name": "successed_async",
                            "id": 7
                        }
                    ],
                    "options": {}
                }
            ],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "PushOSSingleMessage",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "seqId",
                    "id": 1
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "OSMessage",
                    "name": "message",
                    "id": 2
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "Target",
                    "name": "target",
                    "id": 3
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "PushMMPSingleMessage",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "seqId",
                    "id": 1
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "MMPMessage",
                    "name": "message",
                    "id": 2
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "Target",
                    "name": "target",
                    "id": 3
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "requestId",
                    "id": 4
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "StartMMPBatchTask",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "MMPMessage",
                    "name": "message",
                    "id": 1
                },
                {
                    "rule": "required",
                    "options": {
                        "default": 72
                    },
                    "type": "int64",
                    "name": "expire",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "seqId",
                    "id": 3
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "taskGroupName",
                    "id": 4
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "StartOSBatchTask",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "OSMessage",
                    "name": "message",
                    "id": 1
                },
                {
                    "rule": "required",
                    "options": {
                        "default": 72
                    },
                    "type": "int64",
                    "name": "expire",
                    "id": 2
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "SingleBatchRequest",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "batchId",
                    "id": 1
                },
                {
                    "rule": "repeated",
                    "options": {},
                    "type": "SingleBatchItem",
                    "name": "batchItem",
                    "id": 2
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "SingleBatchItem",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "int32",
                    "name": "seqId",
                    "id": 1
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "data",
                    "id": 2
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "PushListMessage",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "seqId",
                    "id": 1
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "taskId",
                    "id": 2
                },
                {
                    "rule": "repeated",
                    "options": {},
                    "type": "Target",
                    "name": "targets",
                    "id": 3
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "EndBatchTask",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "taskId",
                    "id": 1
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "seqId",
                    "id": 2
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "StopBatchTask",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "taskId",
                    "id": 1
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "appkey",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "appId",
                    "id": 3
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "seqId",
                    "id": 4
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "StopBatchTaskResult",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "bool",
                    "name": "result",
                    "id": 1
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "info",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "seqId",
                    "id": 3
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "PushMMPAppMessage",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "MMPMessage",
                    "name": "message",
                    "id": 1
                },
                {
                    "rule": "repeated",
                    "options": {},
                    "type": "string",
                    "name": "appIdList",
                    "id": 2
                },
                {
                    "rule": "repeated",
                    "options": {},
                    "type": "string",
                    "name": "phoneTypeList",
                    "id": 3
                },
                {
                    "rule": "repeated",
                    "options": {},
                    "type": "string",
                    "name": "provinceList",
                    "id": 4
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "seqId",
                    "id": 5
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "ServerNotify",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "NotifyType",
                    "name": "type",
                    "id": 1
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "info",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "bytes",
                    "name": "extradata",
                    "id": 3
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "seqId",
                    "id": 4
                }
            ],
            "enums": [
                {
                    "name": "NotifyType",
                    "values": [
                        {
                            "name": "normal",
                            "id": 0
                        },
                        {
                            "name": "serverListChanged",
                            "id": 1
                        },
                        {
                            "name": "exception",
                            "id": 2
                        }
                    ],
                    "options": {}
                }
            ],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "ServerNotifyResult",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "seqId",
                    "id": 1
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "info",
                    "id": 2
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "OSMessage",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "bool",
                    "name": "isOffline",
                    "id": 2
                },
                {
                    "rule": "required",
                    "options": {
                        "default": 1
                    },
                    "type": "int64",
                    "name": "offlineExpireTime",
                    "id": 3
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "Transparent",
                    "name": "transparent",
                    "id": 4
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "extraData",
                    "id": 5
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "int32",
                    "name": "msgType",
                    "id": 6
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "int32",
                    "name": "msgTraceFlag",
                    "id": 7
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "int32",
                    "name": "priority",
                    "id": 8
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "MMPMessage",
            "fields": [
                {
                    "rule": "optional",
                    "options": {},
                    "type": "Transparent",
                    "name": "transparent",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "extraData",
                    "id": 3
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "int32",
                    "name": "msgType",
                    "id": 4
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "int32",
                    "name": "msgTraceFlag",
                    "id": 5
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "int64",
                    "name": "msgOfflineExpire",
                    "id": 6
                },
                {
                    "rule": "optional",
                    "options": {
                        "default": true
                    },
                    "type": "bool",
                    "name": "isOffline",
                    "id": 7
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "int32",
                    "name": "priority",
                    "id": 8
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "cdnUrl",
                    "id": 9
                },
                {
                    "rule": "optional",
                    "options": {
                        "default": true
                    },
                    "type": "bool",
                    "name": "isSync",
                    "id": 10
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "Transparent",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "id",
                    "id": 1
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "action",
                    "id": 2
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "taskId",
                    "id": 3
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "appKey",
                    "id": 4
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "appId",
                    "id": 5
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "messageId",
                    "id": 6
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "PushInfo",
                    "name": "pushInfo",
                    "id": 7
                },
                {
                    "rule": "repeated",
                    "options": {},
                    "type": "ActionChain",
                    "name": "actionChain",
                    "id": 8
                },
                {
                    "rule": "repeated",
                    "options": {},
                    "type": "string",
                    "name": "condition",
                    "id": 9
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "int32",
                    "name": "templateId",
                    "id": 10
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "taskGroupId",
                    "id": 11
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "PushInfo",
            "fields": [
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "message",
                    "id": 1
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "actionKey",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "sound",
                    "id": 3
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "badge",
                    "id": 4
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "payload",
                    "id": 5
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "locKey",
                    "id": 6
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "locArgs",
                    "id": 7
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "actionLocKey",
                    "id": 8
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "launchImage",
                    "id": 9
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "int32",
                    "name": "contentAvailable",
                    "id": 10
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "bool",
                    "name": "invalidAPN",
                    "id": 11
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "apnJson",
                    "id": 12
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "bool",
                    "name": "invalidMPN",
                    "id": 13
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "mpnXml",
                    "id": 14
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "bool",
                    "name": "validNotify",
                    "id": 15
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "NotifyInfo",
                    "name": "notifyInfo",
                    "id": 16
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "NotifyInfo",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "title",
                    "id": 1
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "content",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "payload",
                    "id": 3
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "ActionChain",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "int32",
                    "name": "actionId",
                    "id": 1
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "Type",
                    "name": "type",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "int32",
                    "name": "next",
                    "id": 3
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "logo",
                    "id": 100
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "logoURL",
                    "id": 101
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "title",
                    "id": 102
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "text",
                    "id": 103
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "bool",
                    "name": "clearable",
                    "id": 104
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "bool",
                    "name": "ring",
                    "id": 105
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "bool",
                    "name": "buzz",
                    "id": 106
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "bannerURL",
                    "id": 107
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "img",
                    "id": 120
                },
                {
                    "rule": "repeated",
                    "options": {},
                    "type": "Button",
                    "name": "buttons",
                    "id": 121
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "appid",
                    "id": 140
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "AppStartUp",
                    "name": "appstartupid",
                    "id": 141
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "bool",
                    "name": "autostart",
                    "id": 142
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "int32",
                    "name": "failedAction",
                    "id": 143
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "url",
                    "id": 160
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "withcid",
                    "id": 161
                },
                {
                    "rule": "optional",
                    "options": {
                        "default": false
                    },
                    "type": "bool",
                    "name": "is_withnettype",
                    "id": 162
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "address",
                    "id": 180
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "content",
                    "id": 181
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "int64",
                    "name": "ct",
                    "id": 182
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "SMSStatus",
                    "name": "flag",
                    "id": 183
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "int32",
                    "name": "successedAction",
                    "id": 200
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "int32",
                    "name": "uninstalledAction",
                    "id": 201
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "name",
                    "id": 220
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "bool",
                    "name": "autoInstall",
                    "id": 223
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "bool",
                    "name": "wifiAutodownload",
                    "id": 225
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "bool",
                    "name": "forceDownload",
                    "id": 226
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "bool",
                    "name": "showProgress",
                    "id": 227
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "post",
                    "id": 241
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "headers",
                    "id": 242
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "bool",
                    "name": "groupable",
                    "id": 260
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "mmsTitle",
                    "id": 280
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "mmsURL",
                    "id": 281
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "bool",
                    "name": "preload",
                    "id": 300
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "taskid",
                    "id": 320
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "int64",
                    "name": "duration",
                    "id": 340
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "date",
                    "id": 360
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "stype",
                    "id": 380
                },
                {
                    "rule": "repeated",
                    "options": {},
                    "type": "InnerFiled",
                    "name": "field",
                    "id": 381
                }
            ],
            "enums": [
                {
                    "name": "Type",
                    "values": [
                        {
                            "name": "Goto",
                            "id": 0
                        },
                        {
                            "name": "notification",
                            "id": 1
                        },
                        {
                            "name": "popup",
                            "id": 2
                        },
                        {
                            "name": "startapp",
                            "id": 3
                        },
                        {
                            "name": "startweb",
                            "id": 4
                        },
                        {
                            "name": "smsinbox",
                            "id": 5
                        },
                        {
                            "name": "checkapp",
                            "id": 6
                        },
                        {
                            "name": "eoa",
                            "id": 7
                        },
                        {
                            "name": "appdownload",
                            "id": 8
                        },
                        {
                            "name": "startsms",
                            "id": 9
                        },
                        {
                            "name": "httpproxy",
                            "id": 10
                        },
                        {
                            "name": "smsinbox2",
                            "id": 11
                        },
                        {
                            "name": "mmsinbox2",
                            "id": 12
                        },
                        {
                            "name": "popupweb",
                            "id": 13
                        },
                        {
                            "name": "dial",
                            "id": 14
                        },
                        {
                            "name": "reportbindapp",
                            "id": 15
                        },
                        {
                            "name": "reportaddphoneinfo",
                            "id": 16
                        },
                        {
                            "name": "reportapplist",
                            "id": 17
                        },
                        {
                            "name": "terminatetask",
                            "id": 18
                        },
                        {
                            "name": "reportapp",
                            "id": 19
                        },
                        {
                            "name": "enablelog",
                            "id": 20
                        },
                        {
                            "name": "disablelog",
                            "id": 21
                        },
                        {
                            "name": "uploadlog",
                            "id": 22
                        }
                    ],
                    "options": {}
                }
            ],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "InnerFiled",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "key",
                    "id": 1
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "val",
                    "id": 2
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "Type",
                    "name": "type",
                    "id": 3
                }
            ],
            "enums": [
                {
                    "name": "Type",
                    "values": [
                        {
                            "name": "str",
                            "id": 0
                        },
                        {
                            "name": "int32",
                            "id": 1
                        },
                        {
                            "name": "int64",
                            "id": 2
                        },
                        {
                            "name": "floa",
                            "id": 3
                        },
                        {
                            "name": "doub",
                            "id": 4
                        },
                        {
                            "name": "bool",
                            "id": 5
                        }
                    ],
                    "options": {}
                }
            ],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "AppStartUp",
            "fields": [
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "android",
                    "id": 1
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "symbia",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "ios",
                    "id": 3
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "Button",
            "fields": [
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "text",
                    "id": 1
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "int32",
                    "name": "next",
                    "id": 2
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        },
        {
            "name": "Target",
            "fields": [
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "appId",
                    "id": 1
                },
                {
                    "rule": "required",
                    "options": {},
                    "type": "string",
                    "name": "clientId",
                    "id": 2
                },
                {
                    "rule": "optional",
                    "options": {},
                    "type": "string",
                    "name": "alias",
                    "id": 3
                }
            ],
            "enums": [],
            "messages": [],
            "options": {},
            "oneofs": {}
        }
    ],
    "enums": [
        {
            "name": "CmdID",
            "values": [
                {
                    "name": "GTHEARDBT",
                    "id": 0
                },
                {
                    "name": "GTAUTH",
                    "id": 1
                },
                {
                    "name": "GTAUTH_RESULT",
                    "id": 2
                },
                {
                    "name": "REQSERVHOST",
                    "id": 3
                },
                {
                    "name": "REQSERVHOSTRESULT",
                    "id": 4
                },
                {
                    "name": "PUSHRESULT",
                    "id": 5
                },
                {
                    "name": "PUSHOSSINGLEMESSAGE",
                    "id": 6
                },
                {
                    "name": "PUSHMMPSINGLEMESSAGE",
                    "id": 7
                },
                {
                    "name": "STARTMMPBATCHTASK",
                    "id": 8
                },
                {
                    "name": "STARTOSBATCHTASK",
                    "id": 9
                },
                {
                    "name": "PUSHLISTMESSAGE",
                    "id": 10
                },
                {
                    "name": "ENDBATCHTASK",
                    "id": 11
                },
                {
                    "name": "PUSHMMPAPPMESSAGE",
                    "id": 12
                },
                {
                    "name": "SERVERNOTIFY",
                    "id": 13
                },
                {
                    "name": "PUSHLISTRESULT",
                    "id": 14
                },
                {
                    "name": "SERVERNOTIFYRESULT",
                    "id": 15
                },
                {
                    "name": "STOPBATCHTASK",
                    "id": 16
                },
                {
                    "name": "STOPBATCHTASKRESULT",
                    "id": 17
                }
            ],
            "options": {}
        },
        {
            "name": "SMSStatus",
            "values": [
                {
                    "name": "unread",
                    "id": 0
                },
                {
                    "name": "read",
                    "id": 1
                }
            ],
            "options": {}
        }
    ],
    "imports": [],
    "options": {
        "optimize_for": "SPEED"
    },
    "services": []
}).build("GtReq");
