import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store'
// import Frame from '../views/Frame.vue'

let Frame = () => import('../views/Frame.vue')
let Explorer = () => import('../views/Explorer.vue')

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    component: () => import ('../views/Login.vue'),
    meta: {
      requireAuth: false
    }
  },
  {
    // root
    path: '/',
    component: Frame,
    redirect: '/index',
    children: [
      {
        path: 'index',
        component: () => import('../views/root/index.vue'),
        meta: {
          keepAlive: true
        }
      },
      {
        path: 'account',
        component: () => import('../views/root/account.vue')
      }
    ]
  },
  {
    path: '/explorer',
    component: Explorer,
    children: [
      // index
      {
        name: 'detail-ad',
        path: 'detail-ad/:id',
        component: () => import('../views/explorer/index/detail-ad.vue')
      },
      {
        path: 'detail-billboard/:id',
        component: () => import('../views/explorer/index/detail-billboard.vue')
      },
      // account assets
      {
        path: 'composition',
        component: () => import('../views/explorer/accout/assets/composition.vue')
      },
      {
        path: 'statistic',
        component: () => import('../views/explorer/accout/assets/statistic.vue')
      },
      // account tx
      {
        path: 'transactions',
        component: () => import('../views/explorer/accout/tx/transactions.vue')
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  let {requireAuth} = to.meta || {}
  if(requireAuth === false) {
    next()
    return
  }
  let {address} = store.state.account || {}
  if( address!= undefined) {
    next()
    return
  }
  next('/login')
})

export default router
