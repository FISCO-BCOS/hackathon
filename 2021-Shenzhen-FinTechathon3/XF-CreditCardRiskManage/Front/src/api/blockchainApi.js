import axios from '.'


export function getBlockNumber() {
    return axios({
        method: 'get',
        url: '/api/getBlockNumber'
    })
}

export function getSealerList() {
    return axios({
        method: 'get',
        url: '/api/getSealerList'
    })
}

export function getTotalTransactionCount() {
    return axios({
        method: 'get',
        url: '/api/getTotalTransactionCount'
    })
}

export function getConsensusStatus() {
    return axios({
        method: 'get',
        url: '/api/getConsensusStatus'
    })
}


export function getSyncStatus() {
    return axios({
        method: 'get',
        url: '/api/getSyncStatus'
    })
}


export function postGovern(data) {
    return axios({
        method: 'get',
        url: '/weid/api/postGovern',
        data
    })
}

export function postPolice(data) {
    return axios({
        method: 'get',
        url: '/weid/api/postPolice',
        data
    })
}

export function postCompany(data) {
    return axios({
        method: 'get',
        url: '/weid/api/postCompany',
        data
    })
}

export function postOther(data) {
    return axios({
        method: 'get',
        url: '/weid/api/postOther',
        data
    })
}

export function verifyGovern() {
    return axios({
        method: 'get',
        url: '/weid/api/verifyGovern'
    })
}

export function verifyPolice() {
    return axios({
        method: 'get',
        url: '/weid/api/verifyPolice'
    })
}

export function verifyCompany() {
    return axios({
        method: 'get',
        url: '/weid/api/verifyCompany'
    })
}

export function verifyOther() {
    return axios({
        method: 'get',
        url: '/weid/api/verifyOther'
    })
}