import axios from 'axios';

const service = axios.create({
    // process.env.NODE_ENV === 'development' 来判断是否开发环境
    // easy-mock服务挂了，暂时不使用了
    // baseURL: 'https://console-mock.apipost.cn/mock/d89b27af-3909-44f6-a929-18a4c7aab5a4',
    // timeout: 5000
});

// 前置拦截
service.interceptors.request.use(
    config => {
        return config;
    },
    error => {
        console.log(error);
        return Promise.reject();
    }
);

// 后置拦截
service.interceptors.response.use(
    response => {
        // let res = response.data;
        // if (res.code === 200) {
        //     return response
        // } else if(res.code === 400) {

        // }
        if (response.status === 200) {
            return response.data;
        } else {
            Promise.reject();
        }
    },
    error => {
        console.log(error);
        return Promise.reject();
    }
);

export default service;
