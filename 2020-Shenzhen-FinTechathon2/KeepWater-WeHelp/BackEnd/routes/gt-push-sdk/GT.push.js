/**
 * 推送的主要接口
 */
var utils = require('./getui/utils');
var httpManager = require('./httpManager');
var ListMessage = require('./getui/message/ListMessage');
var AppMessage = require('./getui/message/AppMessage');
var GtConfig = require('./GtConfig');
var RequestError = require('./RequestError');
var util = require('util');
var request = require('request');
var BatchImpl = require('./BatchImpl');

var instance = [];
var serviceMap = [];

/**
 *
 * @param host required
 * @param appkey 第三方 标识 required
 * @param masterSecret 第三方 密钥 required
 * @constructor
 */
function GeTui(host, appkey, masterSecret) {
    if (instance[appkey] == null) {
        this._appkey = appkey;
        this._masterSecret = masterSecret;
		this._host = host;
        var _this = this;
		
        if (serviceMap[this._appkey] == null) {
			// 第一次，启动定时检测
			this.getOSPushDomainUrlList(function (err, resp) {
					if (resp != null && resp["result"] == "ok" && resp["osList"].length > 0) {
						serviceMap[_this._appkey] = resp["osList"];
						_this.inspect(_this);
					}
				});
        }
        instance[appkey] = this;
    }

    return instance[appkey];
}

function getFaster(_this) {
        var hosts = serviceMap[_this._appkey];
		
        if (hosts.length <= 1) {
            return;
        }
        var mint = 60000;
        
        for (var idx in hosts) {
            requestHead(hosts[idx]);
        }
		function requestHead(host){
			var s = Date.now();
			try {
                request({
                    method: 'head',
                    uri: host,
                    rejectUnauthorized: false
                }, function (err, res, data) {
                    if (!err && res.statusCode == 200) {
                        var diff = Date.now() - s;
                        if (diff < mint) {
                            mint = diff;
                            _this._host = host;
                        }
                    }
                });
            } catch (e) {
                //console.log(e);
            }
		}
    }

GeTui.prototype.inspect = function (_this) {
	// 间隔时间执行
    setInterval(function () {getFaster(_this);}, GtConfig.getHttpInspectInterval());
};

GeTui.prototype.getOSPushDomainUrlList = function (callback) {
    var postData = {
        action: 'getOSPushDomailUrlListAction',
        appkey: this._appkey
    };
    this.httpPostJson(this._host, postData, false, callback);
};

GeTui.prototype.getBatch = function () {
    return new BatchImpl(this._appkey, this);
};

/**
 * 与服务其建立连接
 * connect to the server
 * @return true -- 连接成功 false -- 连接失败
 *           true -- connection sucessful false -- connection failure
 * @throws IOException
 *         出现任何连接异常
 *         For any IO Exceptions
 */
GeTui.prototype.connect = function (callback) {
//    console.log("connect being invoked...");
    var timeStamp = new Date().getTime();
    // 计算sign值
    var sign = utils.md5(this._appkey + timeStamp + this._masterSecret); //必须按顺序
    var postData = {
        action: 'connect',
        appkey: this._appkey,
        timeStamp: timeStamp,
        sign: sign
    };
    httpManager.post(this._host, postData, false, function (err, data) {  //返回一个JSON格式的数据
        callback && callback(err, data && 'success' === data.result);
    });
};

/**
 * 关闭连接
 * disconnect to server
 * @throws IOException
 */
GeTui.prototype.close = function (callback) {
    var postData = {
        'action': 'close',
        'appkey': this._appkey
    };
    this.httpPostJson(this._host, postData, false, callback);
};

/**
 * 推送一条消息到某个客户端
 * push a message to a client appointed by target parameter
 * @param message
 *        消息
 * @param target
 *        目标用户
 *        target client
 * @return 推送结果
 *           push result
 */
GeTui.prototype.pushMessageToSingle = function (message, target, requestId, callback) {
    if (typeof requestId === 'function') {
        callback = requestId;
        requestId = utils.uuid();
    }

    var postData = utils.createPostParams(message, target, requestId, this._appkey);
    this.httpPostJson(this._host, postData, false, callback);
};

GeTui.prototype.httpPostJson = function (host, postData, needGzip, callback) {
    var _this = this;
    postData.version = GtConfig.getSDKVersion();
    httpManager.post(host, postData, needGzip, function (err, response) {
//        console.log("httpPostJson get:" + response.result);
        if (response && response.result == "sign_error") {
//            console.log("holy shit, get sign_error from server");
            _this.connect(function (err, result) {
                if (result == true) {
                    //console.log("connect success.");
//                    console.log("so retry", host, postData);
                    httpManager.post(host, postData, needGzip, callback);
                } else {
                    callback(new Error("connect failed"));
                }
            });
        } else if (response && response.result == "domain_error") {
//            console.log("get domain_error from server");
            serviceMap[this._appkey] = response['osList'];
            _this.host = response['osList'][0];
            httpManager.post(_this._host, postData, needGzip, callback);
        } else {
//          console.log(postData.action + ", response is null");
            if (response == null && postData.requestId != null) {
                if(err == null){
                    err = {};
                }
                err.exception = new RequestError(postData.requestId);
            }
            callback && callback(err, response);
        }
    });
};

/**
 * 批量推送前需要通过这个接口向服务其申请一个“ContentID”
 * @param message
 * @param taskGroupName 可为空
 * @param callback
 */
GeTui.prototype.getContentId = function (message, taskGroupName, callback) {
    var postData = {
        action: 'getContentIdAction',
        appkey: this._appkey,
        clientData: message.getData().getTransparent().toBase64(),
        transmissionContent: message.getData().getTransmissionContent(),
        isOffline: message.getOffline(),
        offlineExpireTime: message.getOfflineExpireTime(),
        pushType: message.getData().getPushType(),
        type: 2,
        pushNetWorkType: message.getPushNetWorkType() // 增加pushNetWorkType参数(0:不限;1:wifi;2:4G/3G/2G)
    };
    if (typeof taskGroupName === 'function') {
        callback = taskGroupName;
        taskGroupName = null;
    }
    if (taskGroupName) {
        postData.taskGroupName = taskGroupName;
    }
    if (message instanceof ListMessage) {
        postData.contentType = 1;
    } else if (message instanceof AppMessage) {
        postData.contentType = 2;
        postData.appIdList = message.getAppIdList();
        postData.phoneTypeList = message.getPhoneTypeList();
        postData.provinceList = message.getProvinceList();
        postData.tagList = message.getTagList();
        postData.speed = message.getSpeed();
    }
    this.httpPostJson(this._host, postData, false, function (err, response) {
        if (!err && response.result === 'ok' && response.contentId) {
            callback && callback(null, response.contentId);
        } else {
            callback && callback(new Error('host:[' + this._host + ']' + '获取contentId失败'), response);
        }
    });

};

/**
 * 根据contentId取消上传的消息体
 *
 * @param contentId contentId
 * @param callback
 * @return boolean 返回是否成功
 */
GeTui.prototype.cancelContentId = function (contentId, callback) {
    var postData = {
        action: 'cancleContentIdAction',
        appkey: this._appkey,
        contentId: contentId
    };
    this.httpPostJson(this._host, postData, false, function (err, response) {
        if (!err && 'ok' === response.result) {
            callback && callback(null, true);
        } else {
            callback && callback(new Error('host:[' + host + ']' + '取消contentId失败'), false);
        }
    });

};

/**
 * 停止某次任务，以ContentID作为标识
 *
 * 通过这个方法，可以取消正在发送中的某次任务，
 * 服务器会抛弃所有正在推送中该任务的所有消息，
 * 以及这个任务相关的离线消息，但是已经下发到
 * 手机的消息就无法再收回了。
 *
 * @param contentId 内容ID
 * @return 是否成功停止
 */
GeTui.prototype.stop = function (contentId, callback) {
    var postData = {
        action: 'stopTaskAction',
        appkey: this._appkey,
        contentId: contentId
    };
    this.httpPostJson(this._host, postData, false, function (err, response) {
        if (!err && 'ok' === response.result) {
            callback && callback(null, true);
        } else {
            callback && callback(new Error('host:[' + this._host + ']' + '取消任务失败'), false)
        }
    });
};

/**
 * 通过{@link IIGtPush.getContentId(ListMessage message)}接口 获得“ContentID”后，通过这个方法实现批量推送。
 *
 * @param contentId
 *        {@link IIGtPush.getContentId(ListMessage message)}接口返回的ID
 * @param targetList
 *        目标用户列表
 * @param callback
 * @return
 */
GeTui.prototype.pushMessageToList = function (contentId, targetList, callback) {
    var cidList = [];
    var aliasList = [];
    var appId = null;

    var needDetails = GtConfig.isPushListNeedDetails();
    var async = GtConfig.isPushListAsync();
    var limit;
    if (async && (!needDetails)) {
        limit = GtConfig.getAsyncListLimit();
    } else {
        limit = GtConfig.getSyncListLimit();
    }
    if (targetList.length > limit) {
        callback && callback(new Error('target size:' + targetList.length + ' beyond the limit:' + limit), null);
        return;
    }

    for (var idx in targetList) {
        var target = targetList[idx];
        var cid = target.getClientId();
        var alias = target.getAlias();
        if (cid != null && cid.trim() != "") {
            cidList.push(cid);
        } else if (alias != null && alias.trim() != "") {
            aliasList.push(alias);
        }
        if (appId == null || appId.trim() == "") {
            appId = target.getAppId();
        }
    }
    var postData = {
        action: 'pushMessageToListAction',
        appkey: this._appkey,
        appId: appId,
        clientIdList: cidList,
        aliasList: aliasList,
        contentId: contentId,
        type: 2,
        needDetails: needDetails,
        async: async
    };
    this.httpPostJson(this._host, postData, true, callback);
};

/**
 * 推送消息到条件限定的用户，限定条件由AppMessage中的参数控制， 如果没有任何限定条件，将会此App的对所有用户进行推送
 *
 * @param message
 *        推送消息
 * @param taskGroupName
 *
 * @param callback
 * @return
 */
GeTui.prototype.pushMessageToApp = function (message, taskGroupName, callback) {
    if (typeof taskGroupName === 'function') {
        callback = taskGroupName;
        taskGroupName = null;
    }
    var _this = this;
    this.getContentId(message, taskGroupName, function (err, contentId) {
        if (!err) {
            var postData = {
                action: 'pushMessageToAppAction',
                appkey: _this._appkey,
                type: 2,
                contentId: contentId
            };
            _this.httpPostJson(_this._host, postData, false, callback);
        } else {
            callback(err, null);
        }
    });
};
/**
 *
 * @param appId
 * @param deviceToken
 * @param message
 * @param callback
 */
GeTui.prototype.pushAPNMessageToSingle = function (appId, deviceToken, message, callback) {
    if (deviceToken.length !== 64) {
        throw new TypeError('deviceToken length must be 64');
    }
    var postData = {
        action: 'apnPushToSingleAction',
        appId: appId,
        appkey: this._appkey,
        DT: deviceToken,
        PI: message.getData().getPushInfo().toBase64()
    };
    this.httpPostJson(this._host, postData, false, callback);
};

GeTui.prototype.pushAPNMessageToList = function (appId, contentId, deviceTokenList, callback) {
    deviceTokenList.forEach(function (deviceToken) {
        if (deviceToken.length !== 64) {
            throw new TypeError('deviceToken length must be 64');
        }
    });

    var postData = {
        action: 'apnPushToListAction',
        appId: appId,
        appkey: this._appkey,
        contentId: contentId,
        DTL: deviceTokenList,
        needDetails: GtConfig.isPushListNeedDetails(),
        async: GtConfig.isPushListAsync()
    };
    this.httpPostJson(this._host, postData, true, callback);
};

GeTui.prototype.getAPNContentId = function (appId, message, callback) {
    var postData = {
        action: 'apnGetContentIdAction',
        appkey: this._appkey,
        appId: appId,
        PI: message.getData().getPushInfo().toBase64()
    };
    this.httpPostJson(this._host, postData, false, function (err, response) {
        if (!err && response.result === 'ok' && response.contentId) {
            callback && callback(null, response.contentId);
        } else {
            callback && callback(new Error('host:[' + this._host + '] 获取contentId失败:' + response.result));
        }
    });

};

GeTui.prototype.getClientIdStatus = function (appId, clientId, callback) {
    var postData = {
        action: 'getClientIdStatusAction',
        appkey: this._appkey,
        appId: appId,
        clientId: clientId
    };
    this.httpPostJson(this._host, postData, false, callback);
};

GeTui.prototype.setClientTag = function (appId, clientId, tags, callback) {
    var postData = {
        action: 'setTagAction',
        appkey: this._appkey,
        appId: appId,
        clientId: clientId,
        tagList: tags
    };
    this.httpPostJson(this._host, postData, false, callback);
};
/**
 * 绑定别名 或bindAlias(appId, targetList, callback)
 * @param appId
 * @param alias string or targetList array
 * @param clientId  如果是 alias, clientId为空
 * @param callback
 */
GeTui.prototype.bindAlias = function (appId, alias, clientId, callback) {
    var postData = {
        appkey: this._appkey,
        appid: appId
    };
    if (typeof alias === 'string') {
        postData.action = 'alias_bind';
        postData.alias = alias;
        postData.cid = clientId;
    } else {
        var targetList = alias;
        if (typeof clientId === 'function') {
            callback = clientId;
        }
        var aliaslist = [];
        targetList.forEach(function (target) {
            aliaslist.push({cid: target.getClientId(), alias: target.getAlias()});
        });
        postData.aliaslist = aliaslist;
        postData.action = 'alias_bind_list';
    }
    this.httpPostJson(this._host, postData, false, callback);
};

GeTui.prototype.queryClientId = function (appId, alias, callback) {
    var postData = {
        action: 'alias_query',
        appkey: this._appkey,
        appid: appId,
        alias: alias
    };
    this.httpPostJson(this._host, postData, false, callback);
};

GeTui.prototype.queryAlias = function (appId, clientId, callback) {
    var postData = {
        action: 'alias_query',
        appkey: this._appkey,
        appid: appId,
        cid: clientId
    };
    this.httpPostJson(this._host, postData, false, callback);
};
/**
 * 取消别名绑定
 * @param appId
 * @param alias
 * @param clientId  如果取消全部，clientId 为空
 * @param callback
 */
GeTui.prototype.unBindAlias = function (appId, alias, clientId, callback) {
    var postData = {
        action: 'alias_unbind',
        appkey: this._appkey,
        appid: appId,
        alias: alias
    };
    if (typeof clientId === 'string' && clientId.length > 0) {
        postData.cid = clientId;
    } else if (typeof clientId === 'function') {
        callback = clientId;
    }
    this.httpPostJson(this._host, postData, false, callback);
};

GeTui.prototype.getPushResult = function (taskId, callback) {
    var postData = {
        action: 'getPushMsgResult',
        appkey: this._appkey,
        taskId: taskId
    };
    this.httpPostJson(this._host, postData, false, callback);
};

GeTui.prototype.getUserTags = function (appId, clientId, callback) {
    var postData = {
        action: 'getUserTags',
        appkey: this._appkey,
        appId: appId,
        clientId: clientId
    };
    this.httpPostJson(this._host, postData, false, callback);
};

GeTui.prototype.queryAppPushDataByDate = function (appId, date, callback) {
    var postData = {
        action: 'queryAppPushData',
        appkey: this._appkey,
        appId: appId,
        date: date
    };
    this.httpPostJson(this._host, postData, false, callback);
};

GeTui.prototype.queryAppUserDataByDate = function (appId, date, callback) {
    var postData = {
        action: 'queryAppUserData',
        appkey: this._appkey,
        appId: appId,
        date: date
    };
    this.httpPostJson(this._host, postData, false,  callback);
};

module.exports = GeTui;