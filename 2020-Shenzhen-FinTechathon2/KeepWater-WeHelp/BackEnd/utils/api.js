var axios = require('axios');
//获取小程序的access_token,动态的获取，由于会不断的改变
var FormData = require('form-data');
var fs = require('fs');
var qs = require('qs');
var requestpost = require('request-promise');
var request = {
	api_yun: function (option){
	console.log("使用了api请求");
	var ACCESS_TOKEN = "";
	const appid = "wx104ea7e4f90396f7";
	const secret = "e56ca46caa3766e1f7af1486508ed688";
	const fileName = 'aa.png'
	const access_tokenurl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appid+"&secret="+secret;
	axios.get(access_tokenurl).then((response)=>{
		ACCESS_TOKEN = response.data.access_token;
	}).then(()=>{
		const baseurl = 'https://api.weixin.qq.com/tcb/uploadfile';
		const newurl = baseurl + '?access_token='+ACCESS_TOKEN;
		axios.post(newurl,option)
		.then((response)=>{
			console.log(response.data);
			console.log("临时请求成功");
	///axios错误了,请求发送
			options = {			
			    method: 'POST',			
			    uri: response.data.url,			
			    formData: {			
			        "Signature": response.data.authorization,			
			        "key": option.path,			
			        "x-cos-security-token": response.data.token,			
			        "x-cos-meta-fileid": response.data.cos_file_id,			
			        "file": {			
			            value: fs.createReadStream(option.path),		
			            options: {			
			            }			
			        }			
			    }			
			}	
			requestpost(options).then((response)=>{
				console.log("success");
				console.log(response);
			}).catch((err)=>{
				console.log("error");
			});
			//console.log(body);
		})
		.catch((error)=>{
			console.log(error);
		});
	})
}

}

module.exports = request;