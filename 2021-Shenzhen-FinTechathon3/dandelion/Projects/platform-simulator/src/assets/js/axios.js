import axios from 'axios'
import store from '@/store'
// import router from '@/router'

let conf = {
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  },
  responseType: 'json'
}

let instance = axios.create(conf)

instance.interceptors.request.use(
  config => {
    if(config.url.includes('/user/login')) return config
    const TOKEN = store.state.user.token || ''
    // console.log('TOKEN', TOKEN)
    if(TOKEN) config.headers.token = TOKEN
    return config
  }
)

instance.interceptors.response.use(
  response => {
    let {data, status, headers} = response
    if(status == 200) {
      let {code, message} = data
      if(code == 20) {
        console.log(response)
        store.commit('logout')
        // router.replace('/login')
        return {type: 'warning', message: '身份过期，请重新登陆'}
      }
      if(!isNaN(code) && code !=1) {
        return {type: 'warning', message}
      }
      return {type: 'success', message, data: data.data}
    }
    else {
      let {message} = data
      return {type: 'warning', message}
    }
  },
  error => {
    console.log(error)
    if(error.response) {
      let {status, statusText} = error.response
      let message = statusText
      return {type: 'error', message}
    }
  }
)

export default instance