/**
 * Created by Administrator on 2015/5/18.
 */

function RequestError(requestId) {
    this.name = 'RequestError';
    this.requestId = requestId;
}

RequestError.prototype = Object.create(Error.prototype);
RequestError.prototype.constructor = RequestError;


module.exports = RequestError;