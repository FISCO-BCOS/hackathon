import Base from './Base'
class PlatOwner extends Base {
  constructor(config, valueFields) {
    super(config, valueFields)
  }
}

let platOwner = new PlatOwner(
  {
    contractAbi: [{"constant":true,"inputs":[],"name":"filter_form","outputs":[{"name":"form","type":"string[]"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"record","type":"string[]"}],"name":"insert","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"account","type":"address"}],"name":"exist_plat","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"record","type":"string[]"},{"name":"filter","type":"string[]"}],"name":"update","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"filter","type":"string[]"}],"name":"remove","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"key_field","type":"string"},{"name":"filter","type":"string[]"}],"name":"select_entries","outputs":[{"name":"result","type":"address"},{"name":"amount","type":"int256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"fields_length","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"key_field","type":"string"},{"name":"filter","type":"string[]"}],"name":"select","outputs":[{"name":"result","type":"string[]"},{"name":"amount","type":"int256"}],"payable":false,"stateMutability":"view","type":"function"}],
    contractAddress: "0xb15c90772a35fd1284df83be41371c3184c35590",
    contractName: "platOwner",
    contractPath: "liuyan",
  },
  "address,name,avatar,totalAsset".split(',')
)
export default platOwner