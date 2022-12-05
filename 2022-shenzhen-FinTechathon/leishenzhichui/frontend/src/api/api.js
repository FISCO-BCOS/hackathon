import axios from '.'

// 登录
export function login(data) {
    return axios({
        method: 'post',
        url: '/api/user/login',
        data
    })
}

// 用户详情
export function userInfo() {
    return axios({
        method: 'get',
        url: '/api/user/info'
    })
}

// 退出
export function logout() {
    return axios({
        method: 'post',
        url: '/api/user/logout'
    })
}

//添加设备
export function deviceCreate(data) {
    return axios({
        method: 'post',
        url: '/api/device/create',
        // url: '/api/user/fix/rp',
        data
    })
}

// 申请管理
export function deviceList(data) {
    return axios({
        method: 'get',
        url: '/api/device/list',
        // url: '/api/user/fix/qp',
        // data
    })
}

// 申请管理 -- 编辑
export function updateRow(data) {
    return axios({
        method: 'put',
        url: '/api/device/update',
        data
    })
}

// 申请管理 -- 删除
export function deleteRow(data) {
    return axios({
        method: 'delete',
        url: `/api/loan/delete/${data.id}`
    })
}

// 申请管理 -- 提交审核
export function submitToApprove(data) {
    return axios({
        method: 'post',
        url: '/api/loan/submitToApprove',
        data
    })
}

export function failureList() {
    return axios({
        method: 'get',
        url: '/api/failure/list'
    })
}


export function normalList() {
    return axios({
        method: 'get',
        url: '/api/normal/list'
    })
}

// 贷款审批 - 准备 -- 拒绝
export function readyReject(data) {
    return axios({
        method: 'post',
        url: '/api/approve/ready/reject',
        data
    })
}

// 贷款审批 - 准备 -- 通过
export function readyPass(data) {
    return axios({
        method: 'post',
        url: '/api/approve/ready/pass',
        data
    })
}

// 贷款审批 - 初审 -- 查询
export function firstList(data) {
    return axios({
        method: 'get',
        url: '/api/approve/first/list',
        params: data
    })
}

// 贷款审批 - 初审 -- 拒绝
export function firstReject(data) {
    return axios({
        method: 'post',
        url: '/api/approve/first/reject',
        data
    })
}

// 贷款审批 - 初审 -- 通过
export function firstPass(data) {
    return axios({
        method: 'post',
        url: '/api/approve/first/pass',
        data
    })
}

// 贷款审批 - 初审 -- 查看详情
export function loanQuery(data) {
    return axios({
        method: 'post',
        url: '/api/loan/query',
        data
    })
}

// 贷款审批 - 终审 -- 查询
export function endList(data) {
    return axios({
        method: 'get',
        url: '/api/approve/end/list',
        params: data
    })
}

// 贷款审批 - 终审 -- 通过
export function endPass(data) {
    return axios({
        method: 'post',
        url: '/api/approve/end/pass',
        data
    })
}

// 贷款审批 - 终审 -- 拒绝
export function endReject(data) {
    return axios({
        method: 'post',
        url: '/api/approve/end/reject',
        data
    })
}

// 贷款审批 - 终审 -- 查看详情
export function endQuery(data) {
    return axios({
        method: 'get',
        url: '/api/loan/query',
        params: data
    })
}

// 标的管理 -- 查询
export function contractList(data) {
    return axios({
        method: 'get',
        url: '/api/contract/list',
        params: data
    })
}

// 标的管理 -- 生成合同
export function createFile(data) {
    return axios({
        method: 'post',
        url: '/api/contract/createFile',
        data
    })
}

// 标的管理 -- 下载合同
export function downloadFile(data) {
    return axios({
        method: 'get',
        url: '/api/contract/download',
        params: data
    })
}

// 权限管理 -- 列表展示
export function getUserList() {
    return axios({
        method: 'get',
        url: '/api/user/list'
    })
}

// 权限管理 -- 创建管理员
export function createPermission(data) {
    return axios({
        method: 'post',
        url: '/api/permission/createUser',
        data
    })
}

// 维修人员列表
export function staffList(data) {
    return axios({
        method: 'get',
        url: '/api/staff/list',
        // url: '/api/user/fix/qu',
        // data
    })
}

//维修人员选择故障设备
export function selectFailureList(data) {
    return axios({
        method: 'put',
        url: '/api/staff/select',
        data
    })
}

export function commitRepair(data) {
    return axios({
        method: 'put',
        url: '/api/device/repair',
        data
    })
}

export function userCreate(data) {
    return axios({
        method: 'post',
        url: '/api/user/trade/ru',
        data
    })
}

export function queryUser(data) {
    return axios({
        method: 'post',
        url: '/api/user/trade/qu',
        data
    })
}

//
export function staffCreate(data) {
    return axios({
        method: 'post',
        url: '/api/user/fix/ru',
        data
    })
}

//
// export function queryUser(data) {
//     return axios({
//         method: 'post',
//         url: '/api/user/trade/qu',
//         data
//     })
// }
