import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './registerServiceWorker'
import * as faceapi from 'face-api.js'  
import router from './router'
import zkp from './utils/ZKP.mjs';
const app = createApp(App)
// 注册组件
app.use(ElementPlus)
app.use(router)
app.mount('#app')

