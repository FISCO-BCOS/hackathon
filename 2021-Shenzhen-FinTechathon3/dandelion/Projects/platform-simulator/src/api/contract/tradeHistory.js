import Base from './Base'
class TradeHistory extends Base {
  constructor(config, valueFields) {
    super(config, valueFields)
  }
}

let tradeHistory = new TradeHistory(
  {
    contractAbi: [{"constant":false,"inputs":[{"name":"_from","type":"address"},{"name":"_to","type":"address"},{"name":"_value","type":"uint256"}],"name":"insert","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"filter_form","outputs":[{"name":"form","type":"string[]"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"record","type":"string[]"}],"name":"insert","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"record","type":"string[]"},{"name":"filter","type":"string[]"}],"name":"update","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"filter","type":"string[]"}],"name":"remove","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"key_field","type":"string"},{"name":"filter","type":"string[]"}],"name":"select_entries","outputs":[{"name":"result","type":"address"},{"name":"amount","type":"int256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"fields_length","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"key_field","type":"string"},{"name":"filter","type":"string[]"}],"name":"select","outputs":[{"name":"result","type":"string[]"},{"name":"amount","type":"int256"}],"payable":false,"stateMutability":"view","type":"function"}],
    contractAddress: "0x178849448ff08a51ba49cb42a0ffa8101b8ffb30",
    contractName: "TradeHistory",
    contractPath: "liuyan",
  },
  "fromAddress,value,timestamp".split(',')
)
export default tradeHistory