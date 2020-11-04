// 透传消息模板
'use strict';
var util = require('util');
var GtReq = require('../GtReq');
var BaseTemplate = require('./BaseTemplate');
function TransmissionTemplate(options) {
    BaseTemplate.call(this, options);
}
util.inherits(TransmissionTemplate, BaseTemplate);

TransmissionTemplate.prototype.getActionChain = function() {
    // 设置actionChain
    // Set actionChain
    var actionChain1 = new GtReq.ActionChain({
        actionId: 1,
        type: GtReq.ActionChain.Type.Goto,
        next: 10030
    });
    //appstartupid
    var appStartUp = new GtReq.AppStartUp({
        android: '',
        symbia: '',
        ios: ''
    });
    //启动app
    //Start app
    var actionChain2 = new GtReq.ActionChain({
        actionId: 10030,
        type: GtReq.ActionChain.Type.startapp,
        appid: '',
        autostart: 1 === this.transmissionType,
        appstartupid: appStartUp,
        failedAction: 100,
        next: 100
    });
    //结束
    //Finish
    var actionChain3 = new GtReq.ActionChain({
        actionId: 100,
        type: GtReq.ActionChain.Type.eoa
    });
    var actionChains = [actionChain1, actionChain2, actionChain3];

    return actionChains;
};

TransmissionTemplate.prototype.getTransmissionContent = function() {
    return this.transmissionContent;
};

TransmissionTemplate.prototype.getPushType = function() {
    return 'TransmissionMsg';
};

/**
 * 设置 透传消息类型
 *  1:收到通知立即启动应用
 *  2:收到通知不启动应用
 *  Set payload information type, 1:intantly start app onced notified, 2:not to start app once notified
 * @param transmissionType
 */
TransmissionTemplate.prototype.setTransmissionType = function(transmissionType) {
    this.transmissionType = transmissionType;
    return this;
};

/**
 * 设置 透传内容 (payload)
 * Set payload content
 * @param transmissionContent
 */
TransmissionTemplate.prototype.setTransmissionContent = function(transmissionContent) {
    this.transmissionContent = transmissionContent;
    return this;
};

/**
 * 第三方厂商推送透传消息带通知处理
 * @param notify
 */
TransmissionTemplate.prototype.set3rdNotifyInfo = function (notify) {
    if(notify.getTitle() === null || notify.getContent() === null) {
        throw new Error("notify title or content cannot be null");
    }

    var notifyInfo = new GtReq.NotifyInfo({
            title: notify.getTitle(),
            content: notify.getContent()
        }
    );

    notifyInfo.payload = notify.getPayload();

    this.pushInfo.notifyInfo = notifyInfo;
    this.pushInfo.validNotify = true;
};

/**
 * templateid support, you do not need to call this function explicitly
 * @returns {number}
 */
TransmissionTemplate.prototype.getTemplateId = function (){
    return 4;
};

module.exports = TransmissionTemplate;