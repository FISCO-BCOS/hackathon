import request from './request'

export const update = (data) => {
    return request({
        url: '/update',
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        data
    })
}