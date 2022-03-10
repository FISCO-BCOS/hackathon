import Base from './Base'
class AdInvesment extends Base {
  constructor(config, valueFields) {
    super(config, valueFields)
  }
  
  _insert(keyField, record) {
    let _record = this.oToA(record, this.recordValueFields)
    return this.transact('_insert', [keyField, _record])
  }
}

let adInvesment = new AdInvesment(
  {
    contractAbi: [{"constant":true,"inputs":[],"name":"filter_form","outputs":[{"name":"form","type":"string[]"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"record","type":"string[]"}],"name":"insert","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"id","type":"string"},{"name":"integralNeed","type":"uint256"},{"name":"integralGet","type":"uint256"},{"name":"_value","type":"uint256"}],"name":"updateRequest","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"record","type":"string[]"},{"name":"filter","type":"string[]"}],"name":"update","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"account","type":"address"}],"name":"getId","outputs":[{"name":"id","type":"string"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"filter","type":"string[]"}],"name":"remove","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"key_field","type":"string"},{"name":"filter","type":"string[]"}],"name":"select_entries","outputs":[{"name":"result","type":"address"},{"name":"amount","type":"int256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"id","type":"string"}],"name":"getRequest","outputs":[{"name":"integralNeed","type":"uint256"},{"name":"integralGet","type":"uint256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"fields_length","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"key_field","type":"string"},{"name":"filter","type":"string[]"}],"name":"select","outputs":[{"name":"result","type":"string[]"},{"name":"amount","type":"int256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"record","type":"string[]"}],"name":"_insert","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"}],
    contractAddress: "0xca77386b860c77eeec27070c58dce4d9d741d828",
    contractName: "AdInvesment",
    contractPath: "liuyan",
  },
  "id,title,adOwner,coverPic,roe,integralNeed,integralGet,targetAd,startTime,endTime,timestamp".split(',')
)
export default adInvesment