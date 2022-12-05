import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'// 引入ElementUI框架
import VurParticles from 'vue-particles'//引入vue粒子组件

// 全局引入ElementUI框架样式
import 'element-ui/lib/theme-chalk/index.css'
// 全局样式引入
import '@/styles/index.scss'
// 导入路由拦截
import './router_intercept.js'

import Video from 'video.js'
import 'video.js/dist/video-js.css'

Vue.prototype.$video = Video


Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(VurParticles)

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
