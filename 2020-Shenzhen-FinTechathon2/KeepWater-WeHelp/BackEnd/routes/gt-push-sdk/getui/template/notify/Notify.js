/**
 * 透传消息带通知
 * Created by zqzhao5 on 17-7-21.
 */
'use strict';
var util = require('util');

function Notify(options){
    this.title = "";
    this.content = "";
    this.payload = "{}"

    util._extend(this, options);
}

Notify.prototype.setTitle = function(title) {
    this.title = title;
    return this;
};

Notify.prototype.getTitle = function() {
    return this.title;
};

Notify.prototype.setContent = function(content) {
    this.content = content;
    return this;
};

Notify.prototype.getContent = function () {
    return this.content;
};

Notify.prototype.setPayload = function (payload) {
    this.payload = payload;
    return this;
};

Notify.prototype.getPayload = function()
{
    return this.payload;
};

module.exports = Notify;