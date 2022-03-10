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

// 贷款申请
export function loanCreate(data) {
    return axios({
        method: 'post',
        url: '/api/loan/create',
        data
    })
}

// 申请管理
export function loanList(data) {
    return axios({
        method: 'get',
        url: '/api/loan/list',
        params: data
    })
}

// 申请管理 -- 编辑
export function updateRow(data) {
    return axios({
        method: 'put',
        url: '/api/loan/update',
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

export function readyList(data) {
    return axios({
        method: 'get',
        url: '/api/approve/ready/list',
        params: data
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

export function financialRegulationList() {
    return axios({
        method: 'get',
        url: '/api/financial/regulation/list',
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

//获取准确率
export function getAccuracy() {
    return axios({
        method: 'get',
        url: '/api/getAccuracy',
    })
}

//获取区块链基本信息 块高等
export function getchaindata() {
    return axios({
        method: 'get',
        url: '/api/getTotalTransactionCount',
    })
}


//计算信用分
export function getgrade(data) {
    return axios({
        method: 'post',
        url: '/api/user/regist',
        data,
        headers: {
            "Content-Type": "multipart/form-data"
        }
    })
}

export function inquireMarks(data) {
    return axios({
        method: 'post',
        url: '/api/user/inquire/marks',
        data,
        headers: {
            "Content-Type": "multipart/form-data"
        }
    })
}

export function repayNow(data) {
    return axios({
        method: 'post',
        url: '/api/user/repay',
        data,
        headers: {
            "Content-Type": "multipart/form-data"
        }
    })
}

export function donateNow(data) {
    return axios({
        method: 'post',
        url: '/api/user/donate',
        data,
        headers: {
            "Content-Type": "multipart/form-data"
        }
    })
}