import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    account: JSON.parse(localStorage.account||'{}')
    // {
    //   signUserId: '123456'
    // }
  },
  mutations: {
    account(state, account) {
      localStorage.account = JSON.stringify(account)
      state.account = account
    }
  },
  getters: {
    account: ({account}) => account
  },
  actions: {
  },
  modules: {
  }
})
