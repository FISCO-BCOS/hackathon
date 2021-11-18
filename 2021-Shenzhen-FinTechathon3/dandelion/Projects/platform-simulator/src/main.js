import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
import api from './api'


Vue.config.productionTip = false
import 'mdi/css/materialdesignicons.css'
Vue.prototype.$api = api

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount('#app')
