const Web3 = require('web3')
const Tx = require('ethereumjs-tx')
const fs = require('fs')
const cfg = require("./truffle-config")

const web3 = new Web3("http://" + cfg.networks.development.host + ":" + cfg.networks.development.port)
const genesisAccount = web3.eth.accounts.privateKeyToAccount("0x" + cfg.privateKey)

const abi = JSON.parse(fs.readFileSync("./build/" + cfg.contract + ".abi", "utf-8"))
const bin = "0x" + fs.readFileSync("./build/" + cfg.contract + ".bin", "utf-8")

const addrFile = "./build/" + cfg.contract + ".address"

function UnlockKeyJson(keystoreJsonV3, password) {
  var key = web3.eth.accounts.decrypt(keystoreJsonV3, password)
  return key
}

function DeployRaw(account) {
  let priKey = Buffer.from(account.privateKey.substr(2), 'hex')
  let myContract = new web3.eth.Contract(abi)
  let encodedABI = myContract.deploy({ data: bin, arguments: [] }).encodeABI()

  SignAndSendTx(null, priKey, encodedABI)
}

function testSet(account) {
  let priKey = Buffer.from(account.privateKey.substr(2), 'hex')
  let contractAddr = fs.readFileSync(addrFile, 'utf-8')
  let deployedContract = new web3.eth.Contract(abi, contractAddr)

  let curTime = new Date()
  let encoded = deployedContract.methods.set(
    curTime.toLocaleDateString().replace(/\//g, "-") + " " + curTime.toTimeString()
  ).encodeABI()

  SignAndSendTx(contractAddr, priKey, encoded)
}

function SendSignedTx(txObject, privateKey) {
  let tx = new Tx(null, 1)
  tx.randomid = txObject.randomid
  tx.gasPrice = txObject.gasPrice
  tx.gasLimit = txObject.gasLimit
  tx.blockLimit = txObject.blockLimit
  tx.to =  txObject.to
  tx.value =  txObject.value
  tx.data = txObject.data
  tx.chainId = txObject.chainId
  tx.groupId = txObject.groupId
  tx.extraData = txObject.extraData
  tx.sign(privateKey)
  let signedTx = '0x' + tx.serialize().toString('hex')

    web3.eth.sendSignedTransaction(1, signedTx, (err, txHash) => {
    if (err) {
      console.log(err)
      return
    }
    console.log("txHash:", txHash)
  })
}

async function getTransactionReceipt(txHash) {
  return new Promise (function (resolve, reject) {
    web3.eth.getTransactionReceipt(1, txHash, (err, receipt) => {
      if (err) {
        reject(err)
      }
      resolve(receipt)
      if (receipt != null && receipt.contractAddress != null) {
        console.log("contractAddress: ", receipt.contractAddress)
        fs.writeFile(addrFile, receipt.contractAddress, (err) => {
          if (err) {
            return
          }
        })
      }
    })
  })
}

function SignAndSendTx(contractAddr, priKey, encodedABI) {
  web3.eth.getBlockNumber(1, (err, blockNumber) => {
    let txObject = {
      randomid: Math.ceil(Math.random()*100000000),
      gasPrice: 0,
      gasLimit: 100000000,
      blockLimit: blockNumber + 100,
      to:  contractAddr,
      value:  0,
      data: encodedABI,
      chainId: 1,
      groupId: 1,
      extraData: '',
    }
    SendSignedTx(txObject, priKey)
  })
}

function testGet(account) {
  let fromAddr = account.address
  let addr = fs.readFileSync(addrFile, 'utf-8')
  let deployedContract = new web3.eth.Contract(abi, addr)
  deployedContract.methods.get()
    .call(1, {from: fromAddr}).then(console.log)
}

function makeChoice(choice) {
  if (choice.length < 1) {
    console.log("arguments length must be one!!!")
    console.log("1: DeployRaw contract")
    console.log("2: Call getTransactionReceipt")
    console.log("3: Call testGet().")
    console.log("4: Call testSet()")
    return
  }

  switch (choice[0]) {
    case "1":
      console.log("1: DeployRaw contract")
      DeployRaw(genesisAccount)
     	break
    case "2":
      console.log("2: Call getTransactionReceipt.")
      getTransactionReceipt(choice[1])
      break
    case "3":
      console.log("3: Call Get().")
      testGet(genesisAccount)
      break
    case "4":
      console.log("4: call testSet().")
      testSet(genesisAccount)
      break
    default:
      console.log("arguments length must be one!!!")
      console.log("1: DeployRaw contract")
      console.log("2: Call getTransactionReceipt")
      console.log("3: Call testGet().")
      console.log("4: Call testSet()")
  }
}

let args = process.argv.splice(2)
makeChoice(args)
