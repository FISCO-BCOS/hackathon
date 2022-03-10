'use strict';
var Payload = require('./Payload');
module.exports.validatePayloadLength = function(options) {
    var json = this.processPayload(options);
    //将汉字转换成3个字符长度
    return json.replace(/[^\x00-\xff]/g,"011").length;
};
module.exports.processPayload = function(options) {
    var isValid = false;
    var pb = new Payload();
    if (options.locKey !== null && options.locKey.length > 0) {
        // loc-key
        pb.setAlertLocKey(options.locKey);
        // loc-args
        if (options.locArgs !== null && options.locArgs.length > 0) {
            pb.setAlertLocArgs(options.locArgs.split(","));
        }
        isValid = true;
    }

    // body
    if (options.message !== null && options.message.length > 0) {
        pb.setAlertBody(options.message);
        isValid = true;
    }

    // action-loc-key
    if (options.actionLocKey !== null && options.actionLocKey.length > 0) {
        pb.setAlertActionLocKey(options.actionLocKey);
    }

    // launch-image
    if (options.launchImage !== null && options.launchImage.length > 0) {
        pb.setAlertLaunchImage(options.launchImage);
    }

    // badge
    var badgeNum = isNaN(parseInt(options.badge)) ?  -1 : parseInt(options.badge);
    if (badgeNum >= 0) {
        pb.setBadge(badgeNum);
        isValid = true;
    }

    // sound
    if (options.sound !== null && options.sound.length > 0)  {
        pb.setSound(options.sound);
    } else {
        pb.setSound('default');
    }


    // payload
    if (options.payload !== null && options.payload.length > 0) {
        pb.addParam('payload', options.payload);
    }

    if (options.contentAvailable !== null && options.contentAvailable === 1) {
        pb.setContentAvailable(options.contentAvailable);
        isValid = true;
    }

    if(!isValid){
        throw new Error("one of the params(locKey,message,badge) must not be null or contentAvailable must be 1");
    }
    var json = pb.toString();

    if(!json){
        throw new Error("payload json is null");
    }
    return json;
};
