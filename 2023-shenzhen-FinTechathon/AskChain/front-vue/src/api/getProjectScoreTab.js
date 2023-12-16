import request from './request';

export const fetchData = () => {
    return request({
        url: '/lookup/organization',
        method: 'get',
    });
};
