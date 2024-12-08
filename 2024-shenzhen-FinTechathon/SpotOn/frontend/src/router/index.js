import { createRouter, createWebHistory } from 'vue-router';
import Login from '@/views/Login.vue';
import ID_register from '@/components/ID_register.vue';
import login from '@/components/login.vue';
import face_login from '@/views/face_login.vue';
import face_register from '@/views/register.vue'; 
import app_choose from '@/views/app_choose.vue';
import bilibili from '@/views/bilibili.vue';
import rigister_app_choose from '@/views/rigister_app_choose.vue';
import test from '@/views/test.vue';
const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login,
  },
  {
    path: '/face-login',
    name: 'face_login',
    component: face_login,
  },
  {
    path: '/face_register',
    name: 'face_register',
    component: face_register,    
  },
  {
    path: '/ID_register',
    name: 'ID_register',
    component: ID_register,
  },
  {
    path: '/login',
    name: 'login',
    component: login,
  },  
  {
    path: '/app-choose',
    name: 'app_choose',
    component: app_choose,
  },  
  {
    path: '/bilibili',
    name: 'bilibili',
    component: bilibili,
  },  
  {
    path: '/rigister_app_choose',
    name: 'rigister_app_choose',
    component: rigister_app_choose,
  },  
  {
    path: '/test',
    name: 'test',
    component: test,
  },  
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
