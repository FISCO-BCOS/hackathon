import request from './request'


export const searchProject = (data) => {
    return request({
        url: '/lookup/{companyName}',
        method: 'GET',
        data
    })
}