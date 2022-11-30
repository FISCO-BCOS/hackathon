import axios from 'axios'
import {getToken} from '@/utils/setToken'
// import { Message } from 'element-ui'// 引入Message组件
import {getMessage} from '@/utils/getMessage.js'

// axios.defaults.baseURL = 'http://192.168.50.184:5000';
axios.defaults.baseURL = 'http://localhost:5000';

// 请求拦截器
axios.interceptors.request.use(config => {
    // 发送请求之前做些什么
    config.headers['token'] = getToken();// 将token添加到header头部
    return config;
}, err => {
    // 对请求错误做些什么
    // Message({
    //   type: 'error',
    //   message: '请求失败，请联系管理员！',
    //   duration: 2000,
    //   showClose: true
    // })
    getMessage('error', '请求失败，请联系管理员！');
    return Promise.reject(err);
})

// 响应拦截器
axios.interceptors.response.use(response => {
    // 对响应数据做些什么
    let {code, msg} = response.data;
    if (code != 20000) {// 如果请求出错就显示Message组件内容
        // Message({// Message组件的使用
        //   type: 'warning',
        //   message: msg || 'error',
        //   duration: 2000,
        //   showClose: true
        // })
        let message = msg || 'error'
        getMessage('warning', message);
    }
    return response;
}, err => {
    // 对响应错误做点什么
    return Promise.reject(err);
})

export default axios;
