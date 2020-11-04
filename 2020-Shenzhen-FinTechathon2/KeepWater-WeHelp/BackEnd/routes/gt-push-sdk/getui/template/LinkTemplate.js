// 点击通知打开网页模板
'use strict';
var util = require('util');
var GtReq = require('../GtReq');
var BaseTemplate = require('./BaseTemplate');
function LinkTemplate(options) {
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
util.inherits(LinkTemplate, BaseTemplate);

LinkTemplate.prototype.getActionChain = function() {
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
    // 启动web
    // Start web
    var actionChain4 = new GtReq.ActionChain({
        actionId: 10030,
        type: GtReq.ActionChain.Type.startweb,
        url: this.url,
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
LinkTemplate.prototype.getPushType = function() {
    return 'LinkMsg';  // PushType.LinkMsg.toString();
};

/**
 * 设置通知内容
 * Set notification content
 * @param text
 */
LinkTemplate.prototype.setText = function(text) {
    this.text = text;
    return this;
};

/**
 * 设置通知标题
 * Set notification title
 * @param title
 */
LinkTemplate.prototype.setTitle = function(title) {
    this.title = title;
    return this;
};
/**
 * 设置通知logo
 * Set notification logo
 * @param logo
 */
LinkTemplate.prototype.setLogo = function(logo) {
    this.logo = logo;
    return this;
};

/**
 * 设置网页地址
 * Set webpage address
 * @param url
 */
LinkTemplate.prototype.setUrl = function(url) {
    this.url = url;
    return this;
};

/**
 * 收到是否响铃
 * Set whether to ring
 * @param isRing
 */
LinkTemplate.prototype.setIsRing = function(isRing) {
    this.isRing = isRing;
    return this;
};

/**
 * 收到是否震动
 * Set whether to vibrate
 * @param isVibrate
 */
LinkTemplate.prototype.setIsVibrate = function(isVibrate) {
    this.isVibrate = isVibrate;
    return this;
};

/**
 * 通知是否可清除
 * set whether to erease
 * @param isClearable
 */
LinkTemplate.prototype.setIsClearable = function(isClearable) {
    this.isClearable = isClearable;
    return this;
};

/**
 * 通知图标
 * set logoUrl
 * @param logoUrl
 */
LinkTemplate.prototype.setLogoUrl = function(logoUrl) {
    this.logoUrl = logoUrl;
    return this;
};

/**
 * templateid support, you do not need to call this function explicitly
 * @returns {number}
 */
LinkTemplate.prototype.getTemplateId = function (){
    return 1;
};

module.exports = LinkTemplate;
