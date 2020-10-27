// 点击通知打开应用模板
'use strict';
var util = require('util');
var GtReq = require('../GtReq');
var BaseTemplate = require('./BaseTemplate');
function NotificationTemplate(options) {
    BaseTemplate.call(this, options);
    options = util._extend({
        title: '',
        text: '',
        logo: '',
        logoUrl: '',
        isVibrate: true,
        isClearable: true,
        isRing: true
    }, options);
    util._extend(this, options);
}
util.inherits(NotificationTemplate, BaseTemplate);

NotificationTemplate.prototype.getActionChain = function() {
    var actionChain1 = new GtReq.ActionChain({
        actionId: 1,
        type: GtReq.ActionChain.Type.Goto,
        next: 10000
    });
    // 通知
    // Notification
    var actionChain2 = new GtReq.ActionChain({
        actionId: 10000,
        type: GtReq.ActionChain.Type.notification,
        title: this.title,
        text: this.text,
        logo: this.logo,
        logoURL: this.logoUrl,
        ring: this.isRing,
        clearable: this.isClearable,
        buzz: this.isVibrate,
        next: 10010
    });

    // goto
    var actionChain3 = new GtReq.ActionChain({
        actionId: 10010,
        type: GtReq.ActionChain.Type.Goto,
        next: 10030
    });
    var appStartUp = new GtReq.AppStartUp({
        android: '',
        symbia: '',
        ios: ''
    });

    // 启动app
    // Start app
    var actionChain4 = new GtReq.ActionChain({
        actionId: 10030,
        type: GtReq.ActionChain.Type.startapp,
        appid: '',
        autostart: 1 === this.transmissionType,
        appstartupid: appStartUp,
        failedAction: 100,
        next: 100
    });
    // 结束
    // Finish
    var actionChain5 = new GtReq.ActionChain({
        actionId: 100,
        type: GtReq.ActionChain.Type.eoa
    });
    var actionChains = [actionChain1, actionChain2, actionChain3, actionChain4, actionChain5];
    return actionChains;
};
NotificationTemplate.prototype.getTransmissionContent = function() {
    return this.transmissionContent;
};
NotificationTemplate.prototype.getPushType = function() {
    return 'NotifyMsg';  // PushType.LinkMsg;
};
/**
 * 设置 透传消息类型 1:收到通知立即启动应用 2:收到通知不启动应用
 * Set direct display message type 1:Start app once gets notification.  2:Not to start app once gets notification
 * @param transmissionType
 */
NotificationTemplate.prototype.setTransmissionType = function(transmissionType) {
    this.transmissionType = transmissionType;
    return this;
};
/**
 * 设置 透传内容 (payload)
 * set payload
 * @param transmissionContent
 */
NotificationTemplate.prototype.setTransmissionContent = function(transmissionContent) {
    this.transmissionContent = transmissionContent;
    return this;
};
/**
 * 设置通知内容
 * Set notification content
 * @param text
 */
NotificationTemplate.prototype.setText = function(text) {
    this.text = text;
    return this;
};

/**
 * 设置通知标题
 * Set notification title
 * @param title
 */
NotificationTemplate.prototype.setTitle = function(title) {
    this.title = title;
    return this;
};
/**
 * 设置通知logo
 * Set notification logo
 * @param logo
 */
NotificationTemplate.prototype.setLogo = function(logo) {
    this.logo = logo;
    return this;
};

/**
 * 设置网页地址
 * Set webpage address
 * @param url
 */
NotificationTemplate.prototype.setUrl = function(url) {
    this.url = url;
    return this;
};

/**
 * 收到是否响铃
 * Set whether to ring
 * @param isRing
 */
NotificationTemplate.prototype.setIsRing = function(isRing) {
    this.isRing = isRing;
    return this;
};

/**
 * 收到是否震动
 * Set whether to vibrate
 * @param isVibrate
 */
NotificationTemplate.prototype.setIsVibrate = function(isVibrate) {
    this.isVibrate = isVibrate;
    return this;
};

/**
 * 通知是否可清除
 * set whether to erease
 * @param isClearable
 */
NotificationTemplate.prototype.setIsClearable = function(isClearable) {
    this.isClearable = isClearable;
    return this;
};

/**
 * 通知图标
 * set logoUrl
 * @param logoUrl
 */
NotificationTemplate.prototype.setLogoUrl = function(logoUrl) {
    this.logoUrl = logoUrl;
    return this;
};

/**
 * templateid support, you do not need to call this function explicitly
 * @returns {number}
 */
NotificationTemplate.prototype.getTemplateId = function (){
    return 0;
};

module.exports = NotificationTemplate;