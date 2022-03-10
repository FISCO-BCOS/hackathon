'use strict';

var util = require('util');
var Message = require('./Message');

function AppMessage(options) {
    Message.call(this, options);
    options = util._extend({
        appIdList: [],
        phoneTypeList: [],
        provinceList: [],
        tagList: [],
        speed: 0
    }, options);
    util._extend(this, options);
}
util.inherits(AppMessage, Message);

AppMessage.prototype.getAppIdList = function() {
    return this.appIdList;
};
AppMessage.prototype.setAppIdList = function(appIdList) {
    this.appIdList = appIdList;
    return this;
};

AppMessage.prototype.getPhoneTypeList = function() {
    return this.phoneTypeList;
};
AppMessage.prototype.setPhoneTypeList = function(phoneTypeList) {
    this.phoneTypeList = phoneTypeList;
    return this;
};

AppMessage.prototype.getProvinceList = function() {
    return this.provinceList;
};
AppMessage.prototype.setProvinceList = function(provinceList) {
    this.provinceList = provinceList;
    return this;
};

AppMessage.prototype.getTagList = function() {
    return this.tagList;
};
AppMessage.prototype.setTagList = function(tagList) {
    this.tagList = tagList;
    return this;
};

AppMessage.prototype.getSpeed = function() {
    return this.speed;
};
AppMessage.prototype.setSpeed = function(speed) {
    this.speed = speed;
    return this;
};

module.exports = AppMessage;