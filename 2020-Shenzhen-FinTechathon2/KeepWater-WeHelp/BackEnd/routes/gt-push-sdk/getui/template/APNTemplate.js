/**
 * iOS模板
 */
'use strict';
var util = require('util');
var BaseTemplate = require('./BaseTemplate');
function APNTemplate(options) {
    BaseTemplate.call(this, options);
}

util.inherits(APNTemplate, BaseTemplate);

/**
 * templateid support, you do not need to call this function explicitly
 * @returns {number}
 */
APNTemplate.prototype.getTemplateId = function (){
    return 5;
};

module.exports = APNTemplate;