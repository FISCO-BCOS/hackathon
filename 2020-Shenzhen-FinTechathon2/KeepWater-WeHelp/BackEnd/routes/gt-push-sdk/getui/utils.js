var crypto = require('crypto');

exports.md5 = function(text) {
    return crypto.createHash('md5').update(text).digest('hex');  //hex是编码方式，可以为'hex', 'binary' 或者'base64'
};

exports.uuid = function() {
    var d = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (d + Math.random()*16)%16 | 0;
        d = Math.floor(d/16);
        return (c=='x' ? r : (r&0x7|0x8)).toString(16);
    });
    return uuid;
};

exports.createPostParams = function (message, target, requestId, appKey) {
    if (requestId == null) {
        requestId = this.uuid();
    }

    var postData = {
        'requestId': requestId,
        'action': 'pushMessageToSingleAction',
        'appkey': appKey,
        //message
        'clientData': message.getData().getTransparent().toBase64(),
        'transmissionContent': message.getData().getTransmissionContent(),
        'isOffline': message.getOffline(),
        'offlineExpireTime': message.getOfflineExpireTime(),
        'pushType': message.getData().getPushType(),
        'pushNetWorkType': message.getPushNetWorkType(),
        //target
        'appId': target.getAppId(),
        'clientId': target.getClientId(),
        'alias': target.getAlias(),
        // 默认都为消息
        // Default as message
        'type': 2
    };
    return postData;
};
