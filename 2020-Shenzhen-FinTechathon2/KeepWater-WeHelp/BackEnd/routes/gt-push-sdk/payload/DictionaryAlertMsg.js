/**
 * Created by Administrator on 2015/5/15.
 */
var util = require('util');
var AlertMsg = require('./AlertMsg');

function DictionaryAlertMsg() {
    this.title = null;
    this.body = null;
    this.titleLocKey = null;
    this.titleLocArgs = Array();
    this.actionLocKey = null;
    this.locKey = null;
    this.locArgs = Array();
    this.launchImage = null;
}

util.inherits(DictionaryAlertMsg, AlertMsg);

DictionaryAlertMsg.prototype.getAlertMsg = function () {
    var alertMsg = {};
    if (this.title != null && this.title != '') {
        alertMsg['title'] = this.title;
    }
    if (this.body != null && this.body != '') {
        alertMsg['body'] = this.body;
    }
    if (this.titleLocKey != null && this.titleLocKey != '') {
        alertMsg['title-loc-key'] = this.titleLocKey;
    }
    if (this.titleLocArgs != null && this.titleLocArgs instanceof Array && this.titleLocArgs.length > 0) {
        alertMsg['title-loc-args'] = this.titleLocArgs;
    }
    if (this.actionLocKey != null && this.actionLocKey != '') {
        alertMsg['action-loc-key'] = this.actionLocKey;
    }
    if (this.locKey != null && this.locKey != '') {
        alertMsg['loc-key'] = this.locKey;
    }
    if (this.locArgs instanceof Array && this.locArgs.length > 0) {
        alertMsg['loc-args'] = this.locArgs;
    }
    if (this.launchImage != null && this.launchImage != '') {
        alertMsg['launch-image'] = this.launchImage;
    }
    return Object.keys(alertMsg).length > 0 ? alertMsg : alertMsg;
};

module.exports = DictionaryAlertMsg;