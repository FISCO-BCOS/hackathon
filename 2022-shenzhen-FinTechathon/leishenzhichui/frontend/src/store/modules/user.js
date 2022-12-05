import {login, userInfo, logout} from '@/api/api.js'
import {setToken, getToken, removeToken} from '@/utils/setToken.js'

const state = {
    token: getToken(),
    roles: [],
    userLists: []
};

const getters = {
    roles(state) {
        return state.roles;
    },
    token(state) {
        return state.token;
    },
    getUserLists(state) {
        return state.userLists;
    }
};

const actions = {
    ULOGIN({commit}, inputData) {
        return new Promise((resolve, reject) => {
            login(inputData).then(res => {
                let {code, data} = res.data;
                if (code == 20000) {
                    let {token} = data;
                    commit('SETTOKEN', token);
                    setToken(token);
                    resolve();
                }
            }).catch(err => {
                reject(err);
            })
        })
    },
    GETINFO({commit}) {
        return new Promise((resolve, reject) => {
            userInfo().then(res => {
                let {code, data} = res.data;
                console.log(res.data);
                // if (code == 20000) {
                commit('SETROLES', data.roles);
                resolve();
                // }
            }).catch(err => {
                reject(err);
            })
        })
    },
    LOGOUT({commit}) {
        return new Promise((resolve, reject) => {
            logout().then(res => {
                let {code} = res.data;
                if (code === 20000) {
                    removeToken();
                    commit('SETROLES', []);
                    commit('SETTOKEN', '');
                    resolve();
                }
            }).catch(err => {
                reject(err);
            })
        })
    },
    SETUSERLISTS({commit}, list) {
        commit('SETUSERLISTS', list);
    },
    REMOVEUSERLISTS({commit}) {
        commit('REMOVEUSERLISTS');
    }
};

const mutations = {
    SETTOKEN(state, token) {
        state.token = token;
    },
    SETROLES(state, roles) {
        state.roles = roles;
    },
    SETUSERLISTS(state, list) {
        state.userLists = list;
    },
    REMOVEUSERLISTS(state) {
        state.userLists = [];
    }
};

export default {
    state,
    getters,
    actions,
    mutations
}
