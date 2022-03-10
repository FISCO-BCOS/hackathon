var axios = require('axios');
//获取小程序的access_token,动态的获取，由于会不断的改变
//接入云数据库方式  --- 最后进行结合
var FormData = require('form-data');
var fs = require('fs');
var qs = require('qs');
var requestpost = require('request-promise');
var request = {
	api_db: function (option,setname){
	return new Promise(function(resolve,reject){
	console.log("使用了api请求");
	var ACCESS_TOKEN = "";
	const appid = "wx104ea7e4f90396f7";
	const secret = "e56ca46caa3766e1f7af1486508ed688"; //环境变量比较合适 保证安全
	const access_tokenurl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
	axios.get(access_tokenurl).then((response)=>{
		ACCESS_TOKEN = response.data.access_token;
	}).then(()=>{
		//进行更改，对数据库进行插入等;
		const baseurl = 'https://api.weixin.qq.com/tcb/databaseadd';
		const newurl = baseurl + '?access_token='+ACCESS_TOKEN; //获取了对应的url，直接进行post请求
		data = option;
		queryurl = "db.collection(\""+setname.toString()+"\").add({data:["+option.toString()+"]})";
		option = { //数据库插入 写成一个方法，通过对象传入query等进行三个接口的改变
			"env":"test-yb8a7",
			"query":queryurl
		}//进行测试
		axios.post(newurl,option)
		.then((response)=>{
			console.log(response.data);
			console.log("临时请求成功");
			resolve(response.data);
	///axios错误了,请求发送
		})
		.catch((error)=>{
			console.log(error);
		});
	})
})},
	api_db_update: function (option,option2,setname){
	return new Promise(function(resolve,reject){
	console.log("使用了api请求");
	var ACCESS_TOKEN = "";
	const appid = "wx104ea7e4f90396f7";
	const secret = "e56ca46caa3766e1f7af1486508ed688"; //环境变量比较合适 保证安全
	const fileName = 'aa.png'
	const access_tokenurl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
	axios.get(access_tokenurl).then((response)=>{
		ACCESS_TOKEN = response.data.access_token;
	}).then(()=>{
		//进行更改，对数据库进行插入等;
		const baseurl = 'https://api.weixin.qq.com/tcb/databaseupdate';
		const newurl = baseurl + '?access_token='+ACCESS_TOKEN; //获取了对应的url，直接进行post请求
		data = option;
		queryurl = "db.collection(\""+setname.toString()+"\").where("+option+").update({data:"+option2+"})";
		option = { //数据库插入 写成一个方法，通过对象传入query等进行三个接口的改变
			"env":"test-yb8a7",
			"query":queryurl
		}//进行测试
		axios.post(newurl,option)
		.then((response)=>{
			console.log(response.data);
			console.log("临时请求成功");
			resolve(response.data);
	///axios错误了,请求发送
		})
		.catch((error)=>{
			console.log(error);
		});
	})
})},

	api_db_delete: function (option,setname){
	return new Promise(function(resolve,reject){
	console.log("使用了api请求");
	var ACCESS_TOKEN = "";
	const appid = "wx104ea7e4f90396f7";
	const secret = "e56ca46caa3766e1f7af1486508ed688"; //环境变量比较合适 保证安全
	const fileName = 'aa.png'
	const access_tokenurl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
	axios.get(access_tokenurl).then((response)=>{
		ACCESS_TOKEN = response.data.access_token;
	}).then(()=>{
		//进行更改，对数据库进行插入等;
		const baseurl = 'https://api.weixin.qq.com/tcb/databasedelete';
		const newurl = baseurl + '?access_token='+ACCESS_TOKEN; //获取了对应的url，直接进行post请求
		data = option;
		queryurl = "db.collection(\""+setname.toString()+"\").where("+option+").remove()";
		option = { //数据库插入 写成一个方法，通过对象传入query等进行三个接口的改变
			"env":"test-yb8a7",
			"query":queryurl
		}//进行测试
		axios.post(newurl,option)
		.then((response)=>{
			console.log(response.data);
			console.log("临时请求成功");
			resolve(response.data);
	///axios错误了,请求发送
		})
		.catch((error)=>{
			console.log(error);
		});
	})
})
},

	api_db_query: async function (option,setname){
	console.log("使用了api请求");
	var ACCESS_TOKEN = "";
	const appid = "wx104ea7e4f90396f7";
	const secret = "e56ca46caa3766e1f7af1486508ed688"; //环境变量比较合适 保证安全
	const fileName = 'aa.png'
	const access_tokenurl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
	await axios.get(access_tokenurl).then((response)=>{
		ACCESS_TOKEN = response.data.access_token;
	}).then(()=>{
		//进行更改，对数据库进行插入等;
		const baseurl = 'https://api.weixin.qq.com/tcb/databasequery';
		const newurl = baseurl + '?access_token='+ACCESS_TOKEN; //获取了对应的url，直接进行post请求
		data = option;
		queryurl = "db.collection(\""+setname.toString()+"\").where("+option+").limit(100).get()";
		option = { //数据库插入 写成一个方法，通过对象传入query等进行三个接口的改变
			"env":"test-yb8a7",
			"query":queryurl
		}//进行测试
		axios.post(newurl,option)
		.then((response)=>{
			console.log(response.data);
			console.log("临时请求成功");
	///axios错误了,请求发送
		})
		.catch((error)=>{
			console.log(error);
		});
	})
	
},
	api_db_query1(option,setname){
	return new Promise(function(resolve,reject){
	console.log("使用了api请求");
	var ACCESS_TOKEN = "";
	const appid = "wx104ea7e4f90396f7";
	const secret = "e56ca46caa3766e1f7af1486508ed688"; //环境变量比较合适 保证安全
	const fileName = 'aa.png'
	const access_tokenurl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
	 axios.get(access_tokenurl).then((response)=>{
		ACCESS_TOKEN = response.data.access_token;
	}).then(()=>{
		//进行更改，对数据库进行插入等;
		const baseurl = 'https://api.weixin.qq.com/tcb/databasequery';
		const newurl = baseurl + '?access_token='+ACCESS_TOKEN; //获取了对应的url，直接进行post请求
		data = option;
		queryurl = "db.collection(\""+setname.toString()+"\").where("+option+").limit(100).get()";
		option = { //数据库插入 写成一个方法，通过对象传入query等进行三个接口的改变
			"env":"test-yb8a7",
			"query":queryurl
		}//进行测试
		axios.post(newurl,option)
		.then((response)=>{
			console.log(response.data);
			console.log("临时请求成功");
			resolve(response.data.data);
	///axios错误了,请求发送
		})
		.catch((error)=>{
			console.log(error);
		});
	})
	
})
}
}

module.exports = request;