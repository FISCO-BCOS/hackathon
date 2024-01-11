import axios from 'axios'
import { ElMessage } from 'element-plus'


const service = axios.create({
    // 配置URL
    // baseURL: '${pageContext.request.contextPath}/',
    baseURL: 'api',
    timeout: 5000,
})

// 前置拦截
service.interceptors.request.use(
    // (config) => {
    //   if (localStorage.getItem('token')) {
    //     if (diffTokenTime()) {
    //       store.dispatch('app/logout')
    //       return Promise.reject(new Error('token 失效了'))
    //     }
    //   }
    //   config.headers.Authorization = localStorage.getItem('token')
    //   return config
    // },
    // (error) => {
    //   return Promise.reject(new Error(error))
    // }
    config => {
      return config;
    },
    error => {
        console.log(error);
        return Promise.reject();
    }
  )

// service.interceptors.response.use(
//     (response) => {
//       const { data, meta } = response.data
//       if (meta.status === 200 || meta.status === 201) {
//         return data
//       } else {
//         ElMessage.error(meta.msg)
//         return Promise.reject(new Error(meta.msg))
//       }
//     },
//     (error) => {
//       console.log(error.response)
//       error.response && ElMessage.error(error.response.data)
//       return Promise.reject(new Error(error.response.data))
//     }
//   )

// 后置拦截
service.interceptors.response.use(
  response => {
        if (response.status === 200) {
            return response.data;
        } else {
            console.log("status not 200");
            Promise.reject();
        }
  },
  error => {
      console.log(error);
      return Promise.reject();
  }
);

export default service

