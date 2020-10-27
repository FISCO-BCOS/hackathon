let Web3 = require('web3')
var axios = require("axios");
const web3 = new Web3('https://ropsten.infura.io/v3/949cc06859e6467d81b8c28b3b6f54a2') //换成https://infura.io地址  主网选择  
const EthereumTx = require('ethereumjs-tx').Transaction

//智能合约地址
const registryAddress = "0x70B3Fc10fA462449753D8188CCeC75226876d775" ///合约地址
//智能合约对应Abi文件
const DidRegistryContract = require('./obi.json') //合约的abi;


//私钥转换为Buffer
const privateKey =  Buffer.from('',"hex")
//私钥转换为账号
const account = web3.eth.accounts.privateKeyToAccount("");
//私钥对应的账号地地址
const address = account.address
console.log("address: ",address)

//获取合约实例
var myContract = new web3.eth.Contract(DidRegistryContract,registryAddress)


	setInterval(
	function(){
		axios.get('http://dip.deipool.io/dipPool/coinPirce').then(result=>{
		console.log(result.data['ZB'].usd);
		
		var list = [result.data['ZB'].usd,result.data['QC'].usd,result.data['USDT'].usd*10**12,result.data['ETH'].usd,result.data['DIP'].usd,result.data['BNB'].usd,result.data['HT'].usd,result.data['OKB'].usd,result.data['LEO'].usd,result.data['BIKI'].usd,result.data['BKK'].usd];
		console.log(list.toString());
		web3.eth.getTransactionCount(address).then(
		    nonce => {
		        console.log("nonce: ",nonce)
				console.log()
				web3.eth.getGasPrice().then((ress)=>{
					console.log(ress);
					
					ress = ress*1.1;
					const txParams = {
					            nonce: nonce,
					            gasPrice: '0x'+ress.toString(16), 
					            gasLimit: '0x2dc6c0',
					            to: registryAddress,
					            data: myContract.methods.setPrice('['+list.toString()+']').encodeABI(), //智能合约中set方法的abi   使用updateTodayTotalRewards()函数
					           
					          }
					          const tx = new EthereumTx(txParams)
					        tx.sign(privateKey)
					        const serializedTx = tx.serialize()
					        web3.eth.sendSignedTransaction('0x' + serializedTx.toString('hex'))
					.on('receipt', console.log);
				})
		        
		          
		    },
		    e => console.log(e)
		)
	})}
	,1000*10*60);

// //获取nonce,使用本地私钥发送交易

