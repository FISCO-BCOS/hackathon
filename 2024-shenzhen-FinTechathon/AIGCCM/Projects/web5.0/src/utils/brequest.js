import axios from 'axios' // 引入axios



const bservice = axios.create({
  baseURL: '/aaaa',
  timeout: 1000
})

let acitveAxios = 0
let timer
const showLoading = () => {
  acitveAxios++
  if (timer) {
    clearTimeout(timer)
  }
  timer = setTimeout(() => {
    if (acitveAxios > 0) {
      emitter.emit('showLoading')
    }
  }, 400)
}

const closeLoading = () => {
  acitveAxios--
  if (acitveAxios <= 0) {
    clearTimeout(timer)
    emitter.emit('closeLoading')
  }
}
// http request 拦截器
bservice.interceptors.request.use(
  config => {

    config.headers = {
      'Content-Type': 'application/json',
      ...config.headers
    }
    return config
  },
  error => {
    ElMessage({
      showClose: true,
      message: error,
      type: 'error'
    })
    return error
  }
)

// http response 拦截器
bservice.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
      return
    }
)
export default bservice
