import Base from './Base'
class Advertise extends Base {
  constructor(config, valueFields) {
    super(config, valueFields)
  }
}

let advertise = new Advertise(
  {
    contractAbi: [{"constant":true,"inputs":[],"name":"filter_form","outputs":[{"name":"form","type":"string[]"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"record","type":"string[]"}],"name":"insert","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"record","type":"string[]"},{"name":"filter","type":"string[]"}],"name":"update","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"filter","type":"string[]"}],"name":"remove","outputs":[{"name":"","type":"int256"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"key_field","type":"string"},{"name":"filter","type":"string[]"}],"name":"select_entries","outputs":[{"name":"result","type":"address"},{"name":"amount","type":"int256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"fields_length","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"id","type":"string"},{"name":"integralNeed","type":"uint256"},{"name":"integralGet","type":"uint256"}],"name":"updateIntegral","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"key_field","type":"string"},{"name":"id","type":"string"}],"name":"getRestIntegral","outputs":[{"name":"integralNeed","type":"uint256"},{"name":"integralGet","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"key_field","type":"string"},{"name":"filter","type":"string[]"}],"name":"select","outputs":[{"name":"result","type":"string[]"},{"name":"amount","type":"int256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"key_field","type":"string"},{"name":"account","type":"address"},{"name":"targetId","type":"string"}],"name":"application","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"}],
    contractAddress: "0x6bcee411d5559338db7ce762fa3b238b967d1741",
    contractName: "Advertise",
    contractPath: "liuyan",
  },
  "id,title,adOwner,coverPic,integralNeed,integralGet,participantNeed,participants,target,rule,startTime,endTime,timestamp".split(',')
)
export default advertise