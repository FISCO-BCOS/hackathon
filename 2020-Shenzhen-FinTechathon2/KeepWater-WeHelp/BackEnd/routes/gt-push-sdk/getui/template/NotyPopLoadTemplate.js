// 点击通知弹窗下载模板
'use strict';
var util = require('util');
var GtReq = require('../GtReq');
var BaseTemplate = require('./BaseTemplate');
function NotPopLoadTemplate(options) {
    BaseTemplate.call(this, options);

    options = util._extend({
        notyIcon: '',           //通知栏图标
        logoUrl: '',            //从网络获取图标
        notyTitle: '',          //通知栏标题
        notyContent: '',        //通知栏内容
        isVibrate: true,
        isClearable: true,      //通知是否可清除
        isRing: true,
        popTitle: '',           //弹框标题
        popContent: '',         //弹框内容
        popImage: '',           //弹框图片
        popButton1: '',         //左边按钮名称
        popButton2: '',         //右边按钮名称
        loadIcon: '',           //下载图标
        loadTitle: '',          //下载标题
        loadUrl: '',            //下载地址
        isAutoInstall: false,   //是否自动安装
        isActived: false,       //是否激活
        androidMark: '',        //安卓标识
        symbianMark: '',        //塞班标识
        iphoneMark: ''          //苹果标识
    }, options);
    util._extend(this, options);
}
util.inherits(NotPopLoadTemplate, BaseTemplate);

NotPopLoadTemplate.prototype.getActionChain = function() {
    // 设置actionChain
    // Set actionChain

    var actionChain = new GtReq.ActionChain({
        actionId: 1,
        type: GtReq.ActionChain.Type.Goto,
        next: 10000
    });
    var actionChain1 = new GtReq.ActionChain({
        actionId: 10000,
        type: GtReq.ActionChain.Type.notification,
        title: this.notyTitle,
        text: this.notyContent,
        logo: this.notyIcon,
        logoURL: this.logoUrl,
        ring: this.isRing,
        clearable: this.isClearable,
        buzz: this.isVibrate,
        next: 10010
    });
    // 通知
    // Notification
    var actionChain2 = new GtReq.ActionChain({
        actionId: 10001,
        type: GtReq.ActionChain.Type.notification,
        title: this.notyTitle,
        text: this.notyContent,
        logo: this.notyIcon,
        ring: this.isRing,
        clearable: true,
        buzz: true,
        next: 10010
    });

    var actionChain3 = new GtReq.ActionChain({
        actionId: 10010,
        type: GtReq.ActionChain.Type.Goto,
        next: 10020
    });
    //弹框按钮
    var button1 = new GtReq.Button({
        text: this.popButton1,
        next: 10040
    });
    var button2 = new GtReq.Button({
        text: this.popButton2,
        next: 100
    });
    //弹框
    var actionChain4 = new GtReq.ActionChain({
        actionId: 10020,
        type: GtReq.ActionChain.Type.popup,
        title: this.popTitle,
        text: this.popContent,
        img: this.popImage,
        buttons: [button1, button2],
        next: 6
    });
    //appstartupid
    var appStartUp = new GtReq.AppStartUp({
        android: this.androidMark,
        ios: this.iphoneMark,
        symbia: this.symbianMark
    });
    var actionChain5 = new GtReq.ActionChain({
        actionId: 10040,
        type: GtReq.ActionChain.Type.appdownload,
        name: this.loadTitle,
        url: this.loadUrl,
        logo: this.loadIcon,
        autoInstall: this.isAutoInstall,
        autostart: this.isActived,
        appstartupid: appStartUp,
        next: 100
    });
    var actionChain6 = new GtReq.ActionChain({
        actionId: 100,
        type: GtReq.ActionChain.Type.eoa
    });
    var actionChains = [actionChain, actionChain1, actionChain2, actionChain3, actionChain4, actionChain5, actionChain6];
    return actionChains;
};
NotPopLoadTemplate.prototype.getPushType = function() {
    return 'NotyPopLoad';
};
/**
 * 设置通知栏图标
 *
 * @param notyIcon
 */
NotPopLoadTemplate.prototype.setNotyIcon = function(notyIcon) {
    this.notyIcon = notyIcon;
    return this;
};
NotPopLoadTemplate.prototype.setLogoUrl = function(logoUrl) {
    this.logoUrl = logoUrl;
    return this;
};
/**
 * 设置通知栏标题
 *
 * @param notyTitle
 */
NotPopLoadTemplate.prototype.setNotyTitle = function(notyTitle) {
    this.notyTitle = notyTitle;
    return this;
};
/**
 * 设置通知栏内容
 *
 * @param notyContent
 */

NotPopLoadTemplate.prototype.setNotyContent = function(notyContent) {
    this.notyContent = notyContent;
    return this;
};
/**
 * 通知是否可清除
 *
 * @param isClearable
 */

NotPopLoadTemplate.prototype.setIsClearable = function(isClearable) {
    this.isClearable = isClearable;
    return this;
};
/**
 * 是否响铃
 *
 * @param isRing
 */
NotPopLoadTemplate.prototype.setIsRing = function(isRing) {
    this.isRing = isRing;
    return this;
};
/**
 * 是否震动
 *
 * @param isVibrate
 */
NotPopLoadTemplate.prototype.setIsVibrate = function(isVibrate) {
    this.isVibrate = isVibrate;
    return this;
};
/**
 * 设置弹框标题
 *
 * @param popTitle
 */
NotPopLoadTemplate.prototype.setPopTitle = function(popTitle) {
    this.popTitle = popTitle;
    return this;
};
/**
 * 设置弹框内容
 *
 * @param popContent
 */
NotPopLoadTemplate.prototype.setPopContent = function(popContent) {
    this.popContent = popContent;
    return this;
};

/**
 * 设置弹框图片
 *
 * @param popImage
 */
NotPopLoadTemplate.prototype.setPopImage = function(popImage) {
    this.popImage = popImage;
    return this;
};
/**
 * 设置左边按钮名称
 *
 * @param popButton1
 */
NotPopLoadTemplate.prototype.setPopButton1 = function(popButton1) {
    this.popButton1 = popButton1;
    return this;
};
/**
 * 设置右边按钮名称
 *
 * @param popButton2
 */
NotPopLoadTemplate.prototype.setPopButton2 = function(popButton2) {
    this.popButton2 = popButton2;
    return this;
};
/**
 * 设置下载图标
 *
 * @param loadIcon
 */
NotPopLoadTemplate.prototype.setLoadIcon = function(loadIcon) {
    this.loadIcon = loadIcon;
    return this;
};
/**
 * 设置下载标题
 *
 * @param loadTitle
 */
NotPopLoadTemplate.prototype.setLoadTitle = function(loadTitle) {
    this.loadTitle = loadTitle;
    return this;
};
/**
 * 设置下载地址
 *
 * @param loadUrl
 */
NotPopLoadTemplate.prototype.setLoadUrl = function(loadUrl) {
    this.loadUrl = loadUrl;
    return this;
};
/**
 * 设置是否自动安装
 *
 * @param isAutoInstall
 */
NotPopLoadTemplate.prototype.setAutoInstall = function(isAutoInstall) {
    this.isAutoInstall = isAutoInstall;
    return this;
};
/**
 * 设置是否激活
 *
 * @param isActived
 */
NotPopLoadTemplate.prototype.setActived = function(isActived) {
    this.isActived = isActived;
    return this;
};
/**
 * 设置安卓标识
 *
 * @param androidMark
 */
NotPopLoadTemplate.prototype.setAndroidMark = function(androidMark) {
    this.androidMark = androidMark;
    return this;
};
/**
 * 设置塞班标识
 *
 * @param symbianMark
 */
NotPopLoadTemplate.prototype.setSymbianMark = function(symbianMark) {
    this.symbianMark = symbianMark;
    return this;
};
/**
 * 设置苹果标识
 *
 * @param iphoneMark
 */
NotPopLoadTemplate.prototype.setIphoneMark = function(iphoneMark) {
    this.iphoneMark = iphoneMark;
    return this;
};

/**
 * templateid support, you do not need to call this function explicitly
 * @returns {number}
 */
NotPopLoadTemplate.prototype.getTemplateId = function (){
    return 2;
};

module.exports = NotPopLoadTemplate;