var bcrypt = require("bcrypt");
var jwt = require('jsonwebtoken');
var bcrypt_login = {
	
	hashPassword(pwd){
		return new Promise((resolve)=>{
			bcrypt.hash(pwd,10,(err,hash)=>{
				resolve(hash);
			})
		})
	},
	comparePassword(pwd,hash){
		return new Promise((resolve)=>{
			bcrypt.compare(pwd,hash,function(err,res){
				resolve(res);
			})
		})
	},
	gentoken(option,key){
		return new Promise((resolve)=>{
			let token = jwt.sign(option,key);
			resolve(token);
		})
	}
}
module.exports = bcrypt_login;