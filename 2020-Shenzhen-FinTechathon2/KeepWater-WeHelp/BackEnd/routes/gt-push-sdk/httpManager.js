'use strict';
var zlib = require('zlib');
var request = require('request');
var util = require('util');
var GtConfig = require('./GtConfig');

var httpManager = {
    /**
     *  HTTP POST请求，返回JSON数据格式
     * @param host
     * @param postData
     * @param callback
     * @return json数据
     */
    post: function (host, postData, needGzip, callback) {
        postData.version = GtConfig.getSDKVersion();
		var tries = GtConfig.getHttpTryCount(); // 最大重连次数
        attempt(host, tries);
        function attempt(ho, times) {
			var options = {
				uri: ho,
				method: 'post',
				timeout: GtConfig.getHttpSoTimeOut(),
				rejectUnauthorized: false,
				headers: {
					'Content-Type': 'text/html;charset=UTF-8',
					'User-Agent': 'Getui nodejs',
					'Accept': '*/*'
				}
			};
			if (needGzip) {
				options.gzip = true;
				options.headers['Content-Encoding'] = 'gzip';
				postData = zlib.gzipSync(JSON.stringify(postData));
			} else {
				options.json = true;
			}
			options.body = postData;
			var action = postData['action'];
			if (action != null && action.length > 0) {
				options.headers['Gt-Action'] = action;
			}
            request(
                options,
                function (err, res, data) {
                    if (!err && res.statusCode == 200) {
                        //console.log("what? got res:" + util.inspect(data));
                        if (typeof data == 'string') {
                            data = JSON.parse(data);
                        }
                        //console.log("result:" + data.result);
                        callback && callback(null, data);
                        //console.log("callback over");
                    } else if (--times) {
                        attempt(host, times);
                    } else {
                        callback && callback(err, null);
                    }
                });
        }
    }
};

module.exports = httpManager;
