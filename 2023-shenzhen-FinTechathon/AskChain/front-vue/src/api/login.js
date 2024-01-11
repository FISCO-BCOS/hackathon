import request from './request'

export const login = (data) => {
    // const params = new URLSearchParams();
    // for (const key in data) {
    //     params.append(key, data[key]);
    // }

    return request({
        url: '/login',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        data
    })
}