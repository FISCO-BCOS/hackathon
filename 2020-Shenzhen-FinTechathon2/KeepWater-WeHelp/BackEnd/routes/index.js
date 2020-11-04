var express = require('express');
var router = express.Router();
var axios = require("axios");
var sha256 = require("js-sha256");
var axios = require("axios");
var pg = require('pg');
var testdb = require('../utils/test.js');//for database
/* GET home page. */
var config = {  
    user:"postgres",
    database:"helpchain",
    password:"w2001218",
    port:5432,
    // 扩展属性
    max:20, // 连接池最大连接数
    idleTimeoutMillis:3000, // 连接最大空闲时间 3s
}
router.get('/all',function(req,res,next){
	var pool = new pg.Pool(config);
	// 查询
	pool.connect(function(err, client, done) {  
	  if(err) {
	    return console.error('数据库连接出错', err);
	  }
	  // 简单输出个 Hello World
	  client.query('SELECT * from company', [], function(err, result) {
	    done();// 释放连接（将其返回给连接池）
	    if(err) {
	      return console.error('查询出错', err);
	    }
	    console.log(result.rows); //output: Hello World
	  });
	});
	res.send("1");
});
router.get('/', function(req, res, next) {
	
	data = {
		  "groupId":1,
		  "uuidStateless":"123238233113",
		  "signType":2,
		  "contractAddress":"0xdfa5569ada457d3a2aa83822b9a20d53d971f6e4",
		  "funcName":"addEvidence",
		  "contractAbi":'[{"constant":false,"inputs":[],"name":"callTime","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"time","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"id","type":"address"},{"name":"time","type":"string"}],"name":"selectByIdAndTime","outputs":[{"name":"ok","type":"int256"},{"name":"location","type":"string"},{"name":"other","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"id","type":"address"},{"name":"time","type":"string"},{"name":"location","type":"string"},{"name":"other","type":"string"}],"name":"addEvidence","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"id","type":"address"},{"name":"time","type":"string"}],"name":"removeByIdAndTime","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[],"name":"create","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"id","type":"address"},{"name":"time","type":"string"},{"name":"location","type":"string"},{"name":"other","type":"string"}],"name":"updateEvidence","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"}]',
		  "funcParam":["0x1a1eacd00bc579f7b714fb000d6056","me","22","aaaaa"],
		  "signUserId":"71c98a8087e84aeb990ccda02169f964"
};

	try{
	axios.post('http://121.40.254.238:5003/WeBASE-Transaction/trans/send',data).then((ress)=>{
		console.log(ress.data);
	})
	}catch(e){
		console.log(111);
		console.log(e);
	}
	res.send("no");
  // res.render('index', { title: 'Express' });
});

function randomString(length) {
  var str = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
  var result = '';
  for (var i = length; i > 0; --i) 
	result += str[Math.floor(Math.random() * str.length)];
  return result;
}

router.post('/1', function(req, res, next) {
	
	'use strict';
	console.log(req.body);
	//获取位置 和求救类型
	var type = req.body.type?'紧急救援':'热心市民';
	var location_x = req.body.location_x;
	var location_y = req.body.location_y;
	var uuuuid = req.body.uuid_self;
	var groupuid = randomString(36);
	
	testdb.api_db(JSON.stringify({uuid:groupuid,name:location_x+location_y+'求救',userList:[uuuuid],owner:uuuuid}),'wangxin_for_help_chain_group').then((ress)=>{
		console.log(ress);
	})
	var GeTui = require('./gt-push-sdk/GT.push');
	var Target = require('./gt-push-sdk/getui/Target');
	var BaseTemplate = require('./gt-push-sdk/getui/template/BaseTemplate');
	var TransmissionTemplate = require('./gt-push-sdk/getui/template/TransmissionTemplate');
	var AppMessage = require('./gt-push-sdk/getui/message/AppMessage');
	
	// http的域名
	var HOST = 'http://api.getui.com/apiex.htm';
	
	//https的域名
	//var HOST = 'https://api.getui.com/apiex.htm';
	
	//定义常量, appId、appKey、masterSecret 采用本文档 "第二步 获取访问凭证 "中获得的应用配置
	const APPID = "plZncVZ4Ks8sbI1otUt3n3"
	const APPKEY = "cbgGPm2g2YAkQ1QeklwcF2"
	const MASTERSECRET = "gmIuBioaVyAsjoBu2uxav6"
	
	var gt = new GeTui(HOST, APPKEY, MASTERSECRET);
	
	pushMessageToApp();
	
	
	function pushMessageToApp() {
	    // var taskGroupName = 'test';
	    var taskGroupName = null;
	    // 定义"点击链接打开通知模板"，并设置透传内容，透传形式
	    var template = TransmissionTemplateDemo();
	
	
	    //定义"AppMessage"类型消息对象，设置消息内容模板、发送的目标App列表、是否支持离线发送、以及离线消息有效期(单位毫秒)
	    var message = new AppMessage({
	        isOffline: true,
	        offlineExpireTime: 3600 * 12 * 1000,
	        data: template,
	        appIdList: [APPID],
	//        phoneTypeList: ['IOS'],
	//        provinceList: ['浙江'],
	        //tagList: ['阿百川']
	        speed: 10000
	    });
		
		
		

	
	    gt.pushMessageToApp(message, taskGroupName, function (err, res) {
	        console.log(res);
	    });
	}
	function TransmissionTemplateDemo() {
	    var NotificationTemplate = require('./gt-push-sdk/getui/template/NotificationTemplate');
	        var template = new NotificationTemplate({
	            appId: APPID,
	            appKey: APPKEY,
	            title: type+'        '+groupuid,
	            text: location_x+'   '+location_y,
	            logoUrl: 'http://wwww.igetui.com/logo.png',
	            isRing: true,
	            isVibrate: true,
	            isClearable: false,
	            transmissionType: 2,
	            transmissionContent: '测试离线',
				setDuration:'200000'
	        });

	    return template;
	}
	res.send("yes");
	

	
	
	
	
	
	
	
	
	
	
//目前的新增用户  -----  个推的sdk   导航的使用     区块链交互完毕了  但是部分服务还是有问题  群聊也完毕 可以进行整合了
	
	
	
	
	
	
	
	
	
	
	
	
	
	
});
router.get('/a', function(req, res, next) {
	
	data = {
		  "groupId":1,  
		  "contractAbi":[{"constant":false,"inputs":[],"name":"returnsomething","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"}],
		  "funcName":"returnsomething",
		  "funcParam":[]
}

	try{
	axios.get('http://121.40.254.238:5003/WeBASE-Transaction/trans/output/1/12112333').then((ress)=>{
		console.log(ress.data);
	})
	}catch(e){
		console.log(111);
		console.log(e);
	}
	res.send("no");
  // res.render('index', { title: 'Express' });
});

router.get('/b', function(req, res, next) {
	baseurl  = "https://restapi.getui.com/v2/XmYO1zZXXa7C19JRp71ne2/auth";
	
	secondurl = "https://restapi.getui.com/v2/XmYO1zZXXa7C19JRp71ne2/push/all"
	appkey = 'R0hPSnafoz8XzRrHBZ1V87';
	masterSecret = 'kY2R3qjNbi8IlM7kXLvgoA';
	timestamp = new Date().getTime();
	sign = sha256(appkey+timestamp+masterSecret);
	//console.log(sign);
	data = {
    "sign": sign,
    "timestamp": timestamp,
    "appkey": appkey
}

	headers = {'content-type':'application/json;charset=utf-8'};
	try{
	axios.post(baseurl,data,headers).then((ress)=>{
		
		headers_forPush = {
		'token':ress.data.data.token,
		"content-type":"application/json;charset=utf-8"};
		console.log(ress.data.data.token);
		
		datas = {
			"request_id":"112332221233",
			"settings":{
				"ttl":3600000
			},
			"audience":'all',
			"push_message":{
				"notification":{
					"title":"请填写通知标题",
					"body":"请填写通知内容",
					"click_type":"url",
					"url":"http://www.baidu.com"
				}
			}
}
	
	console.log(11111);
		axios.post(secondurl,datas,headers_forPush).then((ress)=>{
			console.log(ress);
		})
	
		
		
	})
	}catch(e){
		console.log(111);
		console.log(e);
	}
	res.send("no");
  // res.render('index', { title: 'Express' });
});

router.get('/c', function(req, res, next) {
	secondurl = "https://restapi.getui.com/v2/bHTT3oyogk8Rhm4zB4aqq7/push/all";
	headers_forPush = {'token':'42fe3e0a71349d81989f0396564ea98e6c5b9c7c6763087f9c7de4db2d2157f7',
			"content-type":"application/json;charset=utf-8"};
			
			
			datas = {
				"request_id":"1awdwadwadwadwaawd3",
				"settings":{
					"ttl":36000
				},
				"audience":'all',
				"push_message":{
					"notification":{
						"title":"请填写通知标题",
						"body":"请填写通知内容",
						"click_type":"url",
						"url":"http://www.baidu.com"
					}
				}
	}

	axios.post(secondurl,datas,headers_forPush).then((ress)=>{
		console.log(ress);
	})
	res.send("no");
  // res.render('index', { title: 'Express' });
});


module.exports = router;
