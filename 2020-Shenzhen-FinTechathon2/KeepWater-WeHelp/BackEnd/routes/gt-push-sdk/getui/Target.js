'use strict';
var util = require('util');
var Target = function(options) {
    util._extend({
        alias: '',
        appId: '',
        clientId: ''
    }, options);
    util._extend(this, options);
};
Target.prototype = {
    constructor: Target,
    /**
     * 获取应用唯一ID
     * Get the unique ID for app
     * @return
     */
    getAppId: function() {
        return this.appId;
    },

    /**
     * 设置应用唯一ID
     * Set the unique ID for app
     * @param appId
     */
    setAppId: function(appId) {
        this.appId = appId;
        return this;
    },

    /**
     * 获取 客户端身份ID
     * Get client ID
     * @return
     */
    getClientId: function() {
        return this.clientId;
    },

    /**
     * 设置客户端身份ID
     * Set client ID
     * @param clientId
     */
    setClientId: function(clientId) {
        this.clientId = clientId;
        return this;
    },

    /**
     * 获取用户别名
     * Get alias
     * @return
     */
    getAlias: function() {
        return this.alias;
    },

    /**
     * 设置用户别名
     * Set alias
     * @param alias
     */
    setAlias: function(alias) {
        this.alias = alias;
        return this;
    }
};
module.exports = Target;