import request from './request'

export const setScore = (data) => {
    return request({
        url: '/score',
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        data
    })
}