import request from './request';

export const fetchData = () => {
    return request({
        url: '/scores',
        method: 'get',
    });
};
