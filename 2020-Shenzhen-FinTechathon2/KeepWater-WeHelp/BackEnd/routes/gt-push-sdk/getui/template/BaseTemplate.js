'use strict';
var util = require('util');
var GtReq = require('../GtReq');
var Payload = require('../../payload/Payload');
var APNPayload = require('../../payload/APNPayload');
var DictionaryAlertMsg = require('../../payload/DictionaryAlertMsg');

function BaseTemplate(options) {
    options = util._extend({
        appId: '',
        appkey: ''
    }, options);
    util._extend(this, options);
    this.pushInfo = new GtReq.PushInfo({
        invalidAPN: true,
        invalidMPN: true
    });
    this.duration = 0;
}

BaseTemplate.prototype.setAppId = function (appId) {
    this.appId = appId;
    return this;
};
BaseTemplate.prototype.setAppkey = function (appkey) {
    this.appkey = appkey;
    return this;
};

BaseTemplate.prototype.getTransparent = function () {
    var transparent = new GtReq.Transparent({
        id: '',
        templateId: this.getTemplateId(),
        messageId: '',
        action: 'pushmessage',
        taskId: '',
        pushInfo: this.getPushInfo(),
        appId: this.appId,
        appKey: this.appkey
    });
    var actionChainList = this.getActionChain();
    transparent.setActionChain(actionChainList);
    transparent.setCondition(this.getDurCondition());
    return transparent;
};
BaseTemplate.prototype.getTransmissionContent = function () {
    return '';
};
BaseTemplate.prototype.getPushType = function () {
    return '';
};
BaseTemplate.prototype.getActionChain = function () {
    return null;
};
BaseTemplate.prototype.getPushInfo = function () {
    return this.pushInfo;
};
/**
 *
 * @param options  actionLocKey, badge, message, sound, payload, locKey, locArgs, launchImage, contentAvailable
 * @returns {BaseTemplate}
 */
BaseTemplate.prototype.setPushInfo = function (options) {
    var alertMsg = new DictionaryAlertMsg();
    alertMsg.locKey = options.locKey;
    if (options.locArgs != null && options.locArgs != '') {
        alertMsg.locArgs = [options.locArgs];
    }
    alertMsg.actionLocKey = options.actionLocKey;
    alertMsg.body = options.message;
    alertMsg.launchImage = options.launchImage;

    var apn = new APNPayload();
    apn.alertMsg = alertMsg;
    apn.badge = options.badge;
    apn.sound = options.sound;
    apn.contentAvailable = options.contentAvailable;
    if (options.payload != null && options.payload != '') {
        apn.customMsg = {'payload': options.payload};
    }

    this.setApnInfo(apn);
};

BaseTemplate.prototype.setApnInfo = function (payload) {
    if (payload == null || !(payload instanceof Payload)) {
        return null;
    }
    var apn = payload.getPayload();
    if (apn == null || apn == '') {
        return null;
    }
    var len = apn.replace(/[^\x00-\xff]/g, "011").length;
    if (len > APNPayload.PAYLOAD_MAX_BYTES) {
        throw Error("APN payload length overlength (" + len + ">" + APNPayload.PAYLOAD_MAX_BYTES + ")");
    }
    this.pushInfo.setApnJson(apn);
    this.pushInfo.setInvalidAPN(false);
};

BaseTemplate.prototype.getDurCondition = function () {
    if(this.duration != null && this.duration.length > 0) {
        return 'duration=' + this.duration;
    }
    return "";
};

BaseTemplate.prototype.setDuration = function (begin, end) {
    var s = (new Date(begin)).getTime();
    var e = (new Date(end)).getTime();
    if (s <= 0 || e <= 0) {
        throw Error("DateFormat: yyyy-MM-dd HH:mm:ss");
    }
    if (s > e) {
        throw Error("startTime should be smaller than endTime");
    }
    this.duration = s.toString() + '-' + e.toString();
};

/**
 * templateid support, you do not need to call this function explicitly
 * @returns {number}
 */
BaseTemplate.prototype.getTemplateId = function (){return -1};

module.exports = BaseTemplate;
