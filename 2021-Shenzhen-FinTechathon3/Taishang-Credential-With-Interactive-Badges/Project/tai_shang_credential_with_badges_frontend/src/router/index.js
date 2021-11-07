import Vue from 'vue';
import VueRouter from 'vue-router';
import Intro from '@/views/Intro.vue';

Vue.use(VueRouter);

const routes = [
  {
    path: '/',
    name: 'Intro',
    component: Intro,
    meta: { title: 'Introduction' },
  },
  {
    path: '/render',
    name: 'TaiShangRender',
    component: () => import('@/views/TaiShangRender.vue'),
    meta: { title: 'TaiShangRender' },
  },
  // {
  //   path: '/verify',
  //   name: 'TaiShangVerify',
  //   component: () => import('@/views/TaiShangVerify.vue'),
  //   meta: { title: 'TaiShangVerify' },
  // },
  // {
  //   path: '/taishang',
  //   name: 'TaiShang',
  //   component: () => import('@/views/TaiShang.vue'),
  //   meta: { title: 'TaiShang Series' },
  // },
  // {
  //   path: '/dapp',
  //   name: 'DApp',
  //   component: () => import('@/views/DApp.vue'),
  //   meta: { title: 'Web3.0 dApp Camp' },
  // },
  // {
  //   path: '/buidlers',
  //   name: 'Buidlers',
  //   component: () => import('@/views/Buidlers.vue'),
  //   meta: { title: 'Buidlers' },
  // },
  // {
  //   path: '/supporters',
  //   name: 'Supporters',
  //   component: () => import('@/views/Supporters.vue'),
  //   meta: { title: 'Supporters' },
  // },
  {
    path: '/about',
    name: 'About',
    component: () => import('@/views/About.vue'),
    meta: { title: 'About' },
  },
];

const router = new VueRouter({
  routes,
});

export default router;
