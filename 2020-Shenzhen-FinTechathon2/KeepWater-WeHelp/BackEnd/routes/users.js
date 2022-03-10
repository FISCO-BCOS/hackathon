var express = require('express');
var router = express.Router()
var testdb = require('../utils/test.js');//for database/* GET users listing. */
router.get('/', function(req, res, next) {
  //res.send('respond with a resource');
  //for get the user info 
  testdb.api_db_query1(JSON.stringify({}),'wangxin_for_helpchain_user').then((ress)=>{
	  console.log(ress);
	  list = [];
	  ress.forEach((data_f)=>{
		  list.push(JSON.parse(data_f))
	  })
	  res.send(list);
  })
  //for get the user  
  //插入和新建对应的group数据库
  //先写注册等
  
  
  
  
});

function randomString(length) {
  var str = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
  var result = '';
  for (var i = length; i > 0; --i) 
	result += str[Math.floor(Math.random() * str.length)];
  return result;
}


router.post('/login',function(req,res,next){
	//login and send info to the client
});
router.post('/re',function(req,res,next){
	console.log(req.body);
	let signUserId = randomString(10);
	let appId = randomString(11);
	axios.get('http://121.40.254.238:5004/WeBASE-Sign/user/newUser?signUserId='+signUserId+'&appId='+appId+'&encryptType=0').then((result)=>{
	let data = {
		name:req.body.username,
		password:req.body.password,
		uuid:randomString(36),
		avatar:'/static/images/Avatar-1.png',
		zan:0,
		reward:0,
		score:0,
		range:0,
		scorerange:0,
		address:result.data.address
	}
	testdb.api_db(JSON.stringify(data),'wangxin_for_helpchain_user').then((ress)=>{
		console.log(ress);
		//res.send("yess");
		res.send('0');
		
	})
	
	})
});

module.exports = router;
