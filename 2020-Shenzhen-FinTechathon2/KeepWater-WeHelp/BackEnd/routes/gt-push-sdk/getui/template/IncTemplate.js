'use strict';
var util = require('util');
var GtReq = require('../GtReq');
var BaseTemplate = require('./BaseTemplate');
function IncTemplate(options) {
    BaseTemplate.call(this, options);
    options = util._extend({
        transmissionContent: '',
        incAppId: ''
    }, options);
    util._extend(this, options);
}
util.inherits(IncTemplate, BaseTemplate);

IncTemplate.prototype.getActionChain = function() {
    var actionChain1 = new GtReq.ActionChain({
        actionId: 1,
        type: GtReq.ActionChain.Type.Goto,
        next: 10030
    });
    var appStartUp = new GtReq.AppStartUp({
        android: '',
        symbia: '',
        ios: ''
    });
    // 启动app
    // Start the app
    var actionChain2 = new GtReq.ActionChain({
        actionId: 10030,
        type: GtReq.ActionChain.Type.startapp,
        appid: this.incAppId,
        autostart: 1 === this.transmissionType,
        appstartupid: appStartUp,
        failedAction: 100,
        next: 100
    });
    // 结束
    // Finish
    var actionChain3 = new GtReq.ActionChain({
        actionId: 100,
        type: GtReq.ActionChain.Type.eoa
    });
    var actionChains = [actionChain1, actionChain2, actionChain3];
    return actionChains;
};

IncTemplate.prototype.getTransmissionContent = function() {
    return this.transmissionContent;
};

IncTemplate.prototype.getPushType = function() {
    return 'TransmissionMsg';
};

/**
 * 设置 透传消息类型 1:收到通知立即启动应用 2:收到通知不启动应用
 * Set direct display message type 1:Start the app once gets notification.  2:Not to start the app once gets notification
 * @param transmissionType
 */
IncTemplate.prototype.setTransmissionType = function(transmissionType) {
    this.transmissionType = transmissionType;
    return this;
};

IncTemplate.prototype.setTransmissionContent = function(transmissionContent) {
    this.transmissionContent = transmissionContent;
    return this;
};

IncTemplate.prototype.setIncAppId = function(incAppId) {
    this.incAppId = incAppId;
    return this;
};

/**
 * templateid support, you do not need to call this function explicitly
 * @returns {number}
 */
IncTemplate.prototype.getTemplateId = function (){
    return 6;
};

module.exports = IncTemplate;