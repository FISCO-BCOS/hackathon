'use strict';

var util = require('util');
var Message = require('./Message');

function SingleMessage(options) {
    Message.call(this, options);
}

util.inherits(SingleMessage, Message);

module.exports = SingleMessage;