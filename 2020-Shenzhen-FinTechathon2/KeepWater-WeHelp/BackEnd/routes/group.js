var express = require('express');
var router = express.Router()
var testdb = require('../utils/test.js');//for database
// var dip = require('./contract.js');
/* GET users listing. */
router.get('/', function(req, res, next) {
  //res.send('respond with a resource');
  //for get the user info 
  testdb.api_db_query1(JSON.stringify({}),'wangxin_for_help_chain_group').then((ress)=>{
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
router.post('/update',function(req,res,next){
	//login and send info to the client
	console.log(req.body);
		
	testdb.api_db_query1(JSON.stringify({uuid:req.body.groupid}),'wangxin_for_help_chain_group').then((resss)=>{
		console.log(resss);
		userlist = [];
		resss = JSON.parse(resss);
		console.log(resss);
		userlist =  resss.userList.push(req.body.myself_uuid);
		testdb.api_db_update(JSON.stringify({uuid:resss.uuid}),JSON.stringify({userList:userlist}),'wangxin_for_help_chain_group').then((succ)=>{
			console.log(succ);
		})
	})
	// testdb.api_db_query1(JSON.stringify({uuid:}))
	res.send("yes");
});
// router.get('/re',function(req,res,next){
// 	dip.test();
// 	res.send("yes");
// });

module.exports = router;
