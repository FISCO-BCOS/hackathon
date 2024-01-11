import request from './request'

export const upload = (data) => {
    return request({
        url: '/upload',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        data
    })
}