import Base from './Base'
class Integral extends Base {
  constructor(config) {
    super(config)
  }
  balance(address) {
    return this.transact('balance', [address])
  }
  addIntegral(address, value) {
    return this.transact('addIntegral', [address, value])
  }
  transfer(to, amount, advertiseId) {
    return this.transact('transfer', [to, amount, advertiseId])
  }
  getAllProportion() {
    console.log('test')
    return this.transact('getAllProportion', [])
    .then(res => {
      console.log('getAllProportion0', res)
    })
  }
}

let integral = new Integral({
  contractAbi: [{"constant":true,"inputs":[],"name":"balanceHistory","outputs":[{"name":"balance","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"account","type":"address"}],"name":"authorProp","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_owner","type":"address"},{"name":"_value","type":"uint256"}],"name":"addIntegral","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_to","type":"address"},{"name":"_value","type":"uint256"},{"name":"_advertiseId","type":"string"}],"name":"transfer","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"authorReceiver","type":"address"},{"name":"platformReceiver","type":"address"},{"name":"operator","type":"address"}],"name":"mint","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"authorReceiver","type":"address"},{"name":"platformReceiver","type":"address"},{"name":"operator","type":"address"},{"name":"amount","type":"uint256"}],"name":"mint","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_platform","type":"uint8"}],"name":"ProportionChange","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_value","type":"uint256"}],"name":"destroy","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"account","type":"address"}],"name":"getProportion","outputs":[{"name":"result","type":"uint8"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"_owner","type":"address"}],"name":"balance","outputs":[{"name":"balance","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"getAllProportion","outputs":[{"name":"","type":"string[]"}],"payable":false,"stateMutability":"view","type":"function"},{"inputs":[],"payable":false,"stateMutability":"nonpayable","type":"constructor"},{"anonymous":false,"inputs":[{"indexed":true,"name":"_from","type":"address"},{"indexed":true,"name":"_to","type":"address"},{"indexed":false,"name":"_value","type":"uint256"}],"name":"Transfer","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"_owner","type":"address"},{"indexed":false,"name":"_value","type":"uint256"}],"name":"Destroy","type":"event"}],
  contractAddress: "0xebf7c1a6139ae44cbf3e5b559f160b326ecd52e1",
  contractName: "Integral",
  contractPath: "liuyan",
})
export default integral
