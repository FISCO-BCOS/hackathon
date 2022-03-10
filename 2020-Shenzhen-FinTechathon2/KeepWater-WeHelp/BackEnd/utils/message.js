const Core = require('@alicloud/pop-core');

var messagerequest = {
	requestPhone:function(option){
		var client = new Core({
		  accessKeyId: 'LTAI4FysDqA3rmYVPb61Do8D',
		  accessKeySecret: 'HRnupQai9v0fo82J4tPd8xSkw58evU',
		  endpoint: 'https://dysmsapi.aliyuncs.com',
		  apiVersion: '2017-05-25'
		});
		
		var params = {
		  "RegionId": "cn-hangzhou",
		  "PhoneNumbers": option.phonenumber,
		  "SignName": "百姓渔村",
		  "TemplateCode": "SMS_200190821",
		  "TemplateParam": JSON.stringify(option.code)
		}
		
		var requestOption = {
		  method: 'POST'
		};
		
		client.request('SendSms', params, requestOption).then((result) => {
		  console.log(JSON.stringify(result));
		}, (ex) => {
		  console.log(ex);
		})
		
	}
}
module.exports = messagerequest;