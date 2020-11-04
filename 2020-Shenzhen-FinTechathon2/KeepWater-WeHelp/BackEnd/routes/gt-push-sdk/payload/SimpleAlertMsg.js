/**
 * Created by Administrator on 2015/5/15.
 */
var util = require('util');
var AlertMsg = require('./AlertMsg');

function SimpleAlertMsg() {
    this.alertMsg = null;
}

util.inherits(SimpleAlertMsg, AlertMsg);

SimpleAlertMsg.prototype.getAlertMsg = function () {
    return (this.alertMsg == null || this.alertMsg.length == 0) ? null : this.alertMsg;
};

module.exports = SimpleAlertMsg;