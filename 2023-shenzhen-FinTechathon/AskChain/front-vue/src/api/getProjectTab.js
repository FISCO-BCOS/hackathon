import request from './request';

export const fetchData = () => {
    return request({
        url: '/lookup/company',
        method: 'get',
    });
};
