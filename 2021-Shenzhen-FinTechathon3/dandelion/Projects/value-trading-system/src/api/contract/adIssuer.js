import Base from './Base'
class AdIssuer extends Base {
  constructor(config, valueFields) {
    super(config, valueFields)
  }
}

let adIssuer = new AdIssuer(
  {
    contractAbi: [{"constant":true,"inputs":[],"name":"filter_form","outputs":[{"name":"form","type":"string[]"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"account","type":"address"}],"name":"exist_bill","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"record","type":"string[]"}],"name":"insert","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"record","type":"string[]"},{"name":"filter","type":"string[]"}],"name":"update","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"filter","type":"string[]"}],"name":"remove","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"key_field","type":"string"},{"name":"filter","type":"string[]"}],"name":"select_entries","outputs":[{"name":"result","type":"address"},{"name":"amount","type":"int256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"fields_length","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"key_field","type":"string"},{"name":"filter","type":"string[]"}],"name":"select","outputs":[{"name":"result","type":"string[]"},{"name":"amount","type":"int256"}],"payable":false,"stateMutability":"view","type":"function"}],
    contractAddress: "0x48a7f036acd9eb74db5f813eb25621d3e702e6e8",
    contractName: "AdIssuer",
    contractPath: "liuyan",
  },
  "address,name,avatar".split(',')
)
export default adIssuer