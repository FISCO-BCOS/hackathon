'use strict';

var utils = require('./getui/utils');
var httpManager = require('./httpManager');

//填写mastersecret
var MASTERSECRET = "TBokfpttQJ6aHIhBE9y867";
//填写appkey
var APPKEY = "tpDVam96sY8pxhwBupJ462";
//填写taskId
var TASKID = "OSA-0317_P27U10hFHD95qKFxvfoKN5";
var HOST = "http://sdk.open.api.igexin.com/apiex.htm";
getPushMsgResult(HOST, APPKEY, MASTERSECRET, TASKID, function(err, data) {
  console.log(data);
});

function getPushMsgResult(url, Appkey, masterSecret, taskId, callback) {
  var str = masterSecret + 'action' + 'getPushMsgResult' + 'appkey' + Appkey + 'taskId' + taskId;
  var sign = utils.md5(str);
  var postData = {
    action: 'getPushMsgResult',
    appkey: Appkey,
    taskId: taskId,
    sign: sign
  };
  httpManager.post(url, postData, function(err, data) {  //返回一个JSON格式的数据
    callback && callback(err, data);
  });
}

