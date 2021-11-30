import axios from 'axios'
import store from '../../store'

function getUserId() {
  let {signUserId} = store.getters.account
  return signUserId
}
class Base {
  constructor({contractAbi, contractAddress, contractName, contractPath}, valueFields) {
  // constructor({contractAbi, contractAddress, contractName, contractPath}, valueFields) {
    this.config = {
      cnsName: "",
      contractAbi,
      contractAddress,
      contractName,
      contractPath,
      useAes: false,
      useCns: false,
      groupId: "1",
      // user: "0x6fcf760fe39de597c899be8e3dbc022debb53ed8",
      // signUserId: getUserId(),
      version: ""
    }
    valueFields = valueFields || []
    this.recordValueFields = valueFields

    this.filterValueFields = [...valueFields]
    if(valueFields[valueFields.length-1] == 'timestamp') {
      // this.recordValueFields.length -= 1
      this.filterValueFields.splice(valueFields.length-1, 1,'startTimestamp','endTimestamp')
    }
    this.filterValueFields = this.filterValueFields.concat('page', 'num')
  }
  setConfig(config) {
    for (const key in config) {
      if (Object.hasOwnProperty.call(config, key)) {
        this.config[key] = config[key]
      }
    }
  }

  aToO(arr) {
    arr = arr.split('^,^')
    let obj = {}
    let index = 0
    for (const key of this.recordValueFields) {
      obj[key] = arr[index++] || ""
    }
    return obj
  }

  oToA(object, valueFields) {
    let arr = []
    for (const key of valueFields) {
      if(key == 'timestamp') continue
      arr.push(object[key] || "")
    }
    return arr
  }

  transact(funcName, funcParam) {
    return axios.post('/trans', {
      ...this.config,
      funcName,
      funcParam,
      // user: "0x6fcf760fe39de597c899be8e3dbc022debb53ed8"||store.getters.account.address,
      signUserId: getUserId()
    })
  }
  insert(keyField, record) {
    console.log('insert')
    let _record = this.oToA(record, this.recordValueFields)
    return this.transact('insert', [keyField, _record])
  }
  select(keyField, filter) {
    let _filter = this.oToA(filter, this.filterValueFields)
    return this.transact('select', [keyField, _filter])
    .then(res => {
      let str = res.data[0]
      // str = str.substr(3, str.length-6)
      let arr = JSON.parse(str)
      if(str.length ==0) {
        return {
          list: [],
          total: 0
        }
      }
      // let arr = str.split('", "')
      return {
        list: arr.map(record => this.aToO(record)),
        total: res.data[1]
      }
    })
  }
  update(keyField, record, filter) {
    let _record = this.oToA(record, this.recordValueFields)
    let _filter = this.oToA(filter, this.filterValueFields)
    return this.transact('update', [keyField, _record, _filter])
  }
  remove(keyField, filter) {
    let _filter = this.oToA(filter, this.filterValueFields)
    return this.transact('remove', [keyField, _filter])
  }
}
export default Base