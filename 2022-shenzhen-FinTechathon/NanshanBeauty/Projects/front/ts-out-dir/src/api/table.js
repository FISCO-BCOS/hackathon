import request from 'axios';
export function getList(params) {
    return request({
        url: '/vue3-admin-template/table/list',
        method: 'get',
        data: params
    });
}
//# sourceMappingURL=table.js.map