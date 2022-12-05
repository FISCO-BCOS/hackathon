import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

import user from './modules/user'
import permission from './modules/permission'

export default new Vuex.Store({
  modules: {
    user,
    permission
  }
})
