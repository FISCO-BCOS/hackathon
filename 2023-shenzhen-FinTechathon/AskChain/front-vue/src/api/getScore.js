import request from './request'

export const getScore = (data) => {
    return request({
        url: '/score/{companyName}',
        method: 'GET',
        data
    })
}