import axios from 'axios'
export default {
  newUser(signUserId) {
    return axios.get('/sign/newUser', {
      params: {
        signUserId,
        appId: 1,
        encryptType: 0
      }
    })
    .then(res => {
      console.log(res)
      let {code, data, message} = res.data
      let type = 'success'
      if(message != 'success') {
        type = 'error'
        message = 'ID非法'
      }
      else message = '注册成功'
      return {
        type, data, message
      }
    })
  },
  userInfo(signUserId) {
    return axios.get(`/sign/${signUserId}/userInfo`)
    .then(res => {
      console.log(res)
      let {code, data, message} = res.data
      let type = 'success'
      if(message != 'success') {
        type = 'error'
        message = 'ID非法'
      }
      else message = '账户设置成功'
      return {
        type, data, message
      }
    })
  }
}