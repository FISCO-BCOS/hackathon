/**
 * Created by Administrator on 2015/5/13.
 */


function getProperty(envKey, defaultValue) {
    var value = process.env[envKey];
    if (value == null) {
        return defaultValue;
    }
    return value;
}
exports.isPushSingleBatchAsync = function (){
    return getProperty("gexin_pushSingleBatch_needAsync", false);
};

exports.isPushListAsync = function () {
    return getProperty("gexin_pushList_needAsync", false);
};

exports.isPushListNeedDetails = function() {
    return getProperty("gexin_pushList_needDetails", false);
};

exports.getSyncListLimit = function () {
    return getProperty("gexin_pushList_syncLimit", 1000);
};

exports.getAsyncListLimit = function () {
    return getProperty("gexin_pushList_asyncLimit", 10000);
};

exports.getHttpConnectionTimeOut = function () {
    return getProperty("gexin_http_connection_timeout", 60);
};

exports.getHttpSoTimeOut = function () {
    return getProperty("gexin_http_so_timeout", 30000);
};

exports.getHttpTryCount = function () {
    return getProperty("gexin_http_tryCount", 3);
};

exports.getHttpInspectInterval = function () {
    return getProperty("gexin_inspect_interval", 300000);
};

exports.getSDKVersion = function() {
    return "4.0.1.1";
};

