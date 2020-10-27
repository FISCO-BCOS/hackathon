/**
 * Created by Administrator on 2015/5/15.
 */
var util = require('util');
var Payload = require('./Payload')
var AlertMsg = require('./AlertMsg');

function APNPayload() {
    this.APN_SOUND_SILENCE = 'com.gexin.ios.silence';

    this.alertMsg = null;
    this.badge = -1;
    this.sound = 'default';
    this.contentAvailable = 0;
    this.category = null;
    this.customMsg = {};
}

util.inherits(APNPayload, Payload);

APNPayload.PAYLOAD_MAX_BYTES = 2048;

APNPayload.prototype.getPayload = function () {
    try {
        var apsMap = {};
        if (this.alertMsg != null){
            var msg =  this.alertMsg.getAlertMsg();
            if(msg != null){
                apsMap['alert'] = msg;
            }
        }
        if (this.badge >= 0) {
            apsMap['badge'] = this.badge;
        }
        if (this.APN_SOUND_SILENCE != this.sound) {
            if (this.sound != null && this.sound.length != '') {
                apsMap['sound'] = this.sound;
            } else {
                apsMap['sound'] = 'default';
            }
        }
        if (Object.keys(apsMap).length == 0) {
            throw new Error("format error");
        }
        if (this.contentAvailable > 0) {
            apsMap['content-available'] = this.contentAvailable;
        }
        if (this.category != null && this.category != '') {
            apsMap['category'] = this.category;
        }
        var tmp = {};
        if (this.customMsg != null && Object.keys(this.customMsg).length > 0) {
            for (var idx in this.customMsg) {
                tmp[idx] = this.customMsg[idx];
            }
        }
        tmp['aps'] = apsMap;
        return JSON.stringify(tmp);
    }
    catch (e) {
        console.log(e);
        throw Error('create apn payload error:' +  e.message);
    }
};

module.exports = APNPayload;