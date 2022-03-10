'use strict';
var util = require('util');
function Message(options) {
    options = util._extend({
        isOffline: true,
        offlineExpireTime: 60 * 1000,   //过多久该消息离线失效（单位毫秒） 支持1-72小时*60000秒，默认1小时 60*1000
        pushNetWorkType: 0              //0:联网方式不限;1:仅wifi;2:仅4G/3G/2G
    }, options);
    util._extend(this, options);
}

Message.prototype = {
    constructor: Message,
    getOffline: function() {
        return this.isOffline;
    },
    setOffline: function(isOffline) {  //原是 isOffline()
        this.isOffline = isOffline;
        return this;
    },
    getOfflineExpireTime: function() {
        return this.offlineExpireTime;
    },
    setOfflineExpireTime: function(offlineExpireTime) {
        this.offlineExpireTime = offlineExpireTime;
        return this;
    },

    getPushNetWorkType: function() {
        return this.pushNetWorkType;
    },
    setPushNetWorkType: function(pushNetWorkType) {
        this.pushNetWorkType = pushNetWorkType;
        return this;
    },
    getData: function() {
        return this.data;
    },
    setData: function(data) {
        this.data = data;
        return this;
    }
};
module.exports = Message;