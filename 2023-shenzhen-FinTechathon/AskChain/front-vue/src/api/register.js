import request from './request';

export const register = (data) => {
    return request({
        url: '/register',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        data
    })
}