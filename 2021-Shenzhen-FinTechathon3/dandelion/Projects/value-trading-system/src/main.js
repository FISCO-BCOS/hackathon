import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import * as echarts from 'echarts'
Vue.prototype.$echarts = echarts

import api from '../src/api'
Vue.prototype.$api = api

import 'mdi/css/materialdesignicons.css'

Vue.config.productionTip = false

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App),
  icons: {
    iconfont: 'mdi', // default - only for display purposes
  },
}).$mount('#app')
