/**
 * 弹框 + 透传数据
 * popout window + payload data
 **/
'use strict';
var util = require('util');
var GtReq = require('../GtReq');
var BaseTemplate = require('./BaseTemplate');
function PopupTransmissionTemplate(options) {
    BaseTemplate.call(this, options);
    options = util._extend({
        title: '',
        text: '',
        img: '',
        confirmButtonText: '',
        cancelButtonText: '',
        transmissionContent: ''
    }, options);
    util._extend(this, options);
}
util.inherits(PopupTransmissionTemplate, BaseTemplate);

PopupTransmissionTemplate.prototype.getActionChain = function() {
    // 设置actionChain
    // Set actionChain
    var actionChain1 = new GtReq.ActionChain({
        actionId: 1,
        type: GtReq.ActionChain.Type.Goto,
        next: 10020
    });

    var confirmButton = new GtReq.Button({
        text: this.confirmButtonText,
        next: 10030
    });
    var cancelButton = new GtReq.Button({
        text: this.cancelButtonText,
        next: 100
    });
    var popupAction = new GtReq.ActionChain({
        actionId: 10020,
        title: this.title,
        img: this.img,
        buttons: [confirmButton, cancelButton],
        type: GtReq.ActionChain.Type.popup,
        next: 100
    });

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
    var actionChains = [actionChain1, popupAction, actionChain2, actionChain3];

    return actionChains;
};



PopupTransmissionTemplate.prototype.getPushType = function() {
    return 'TransmissionMsg';
};

PopupTransmissionTemplate.prototype.getTransmissionType = function() {
    return this.transmissionType;
};
/**
 * 设置 透传消息类型
 *  1:收到通知立即启动应用
 *  2:收到通知不启动应用
 * @param transmissionType
 * Set payload information type, 1:intantly start app onced notified, 2:not to start app once notified
 */
PopupTransmissionTemplate.prototype.setTransmissionType = function(transmissionType) {
    this.transmissionType = transmissionType;
    return this;
};
PopupTransmissionTemplate.prototype.getTransmissionContent = function() {
    return this.transmissionContent;
};
/**
 * 设置 透传内容 (payload)
 * Set payload information
 * @param transmissionContent
 */
PopupTransmissionTemplate.prototype.setTransmissionContent = function(transmissionContent) {
    this.transmissionContent = transmissionContent;
    return this;
};

PopupTransmissionTemplate.prototype.getTitle = function() {
    return this.title;
};

PopupTransmissionTemplate.prototype.setTitle = function(title) {
    this.title = title;
    return this;
};

PopupTransmissionTemplate.prototype.getText = function() {
    return this.text;
};

PopupTransmissionTemplate.prototype.setText = function(text) {
    this.text = text;
    return this;
};

PopupTransmissionTemplate.prototype.getImg = function() {
    return this.img;
};

PopupTransmissionTemplate.prototype.setImg = function(img) {
    this.img = img;
    return this;
};

PopupTransmissionTemplate.prototype.getConfirmButtonText = function() {
    return this.confirmButtonText;
};

PopupTransmissionTemplate.prototype.setConfirmButtonText = function(confirmButtonText) {
    this.confirmButtonText = confirmButtonText;
    return this;
};

PopupTransmissionTemplate.prototype.getCancelButtonText = function() {
    return this.cancelButtonText;
};

PopupTransmissionTemplate.prototype.setCancelButtonText = function(cancelButtonText) {
    this.cancelButtonText = cancelButtonText;
    return this;
};

/**
 * templateid support, you do not need to call this function explicitly
 * @returns {number}
 */
PopupTransmissionTemplate.prototype.getTemplateId = function (){
    return 3;
};

module.exports = PopupTransmissionTemplate;