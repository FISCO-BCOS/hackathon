'use strict';

var util = require('util');
var Message = require('./Message');

function ListMessage(options) {
    Message.call(this, options);
}

util.inherits(ListMessage, Message);

module.exports = ListMessage;