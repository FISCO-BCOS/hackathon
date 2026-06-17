import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [{
  path: '/',
  redirect: '/login'
},
{
  path: '/init',
  name: 'Init',
  component: () => import('@/view/init/index.vue')
},
{
  path: '/login',
  name: 'Login',
  component: () => import('@/view/login/index.vue')
},
{
  path: '/txt2img',
  name: 'Txt2img',
  component: () => import('@/view/txt2img/index.vue')
},
{
  path: '/:catchAll(.*)',
  meta: {
    closeTab: true,
  },
  component: () => import('@/view/error/index.vue')
},
{
  path: '/img2img',
  name: 'Img2img',
  component: () => import('@/view/img2img/img2img.vue')
},
{
  path: '/img2imgForm',
  name: 'Img2imgForm',
  component: () => import('@/view/img2img/img2imgForm.vue')
},
{
  path: '/test111',
  name: 'Test111',
  component: () => import('@/view/test111/index.vue')
},
{
  path: '/search',
  name: 'Search',
  component: () => import('@/view/search/index.vue')
},
  {
    path: '/transaction',
    name: 'Transaction',
    component: () => import('@/view/transaction/index.vue')
  },


]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

export default router
