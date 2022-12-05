import { createRouter, createWebHashHistory, Router } from 'vue-router'
import Layout from '@/layout'
import { RouterTy } from '~/router'

export const constantRoutes: RouterTy = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/Login.vue'),
    hidden: true
  },
  // 消费者注册
  {
    path: '/customerRegist',
    component: () => import('@/views/regist/CustomerRegist.vue'),
    hidden: true
  },
  // 企业注册
  {
    path: '/entRegist',
    component: () => import('@/views/regist/EntRegist.vue'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404.vue'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401.vue'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: '首页',
        component: () => import('@/views/dashboard/index.vue'),
        //using el svg icon, the elSvgIcon first when at the same time using elSvgIcon and icon
        meta: { title: '首页', elSvgIcon: 'Fold' }
      }
    ]
  },
]
/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
// TODO: 权限路由位置，根据userInfo获取到的role
export const asyncRoutes: RouterTy = [
   // 业务菜单开始
  {
    path: '/enterprise-access',
    component: Layout,
    name: '机构准入',
    alwaysShow: true,
    meta: { title: '机构准入', icon: 'tree', roles: ['recycle', 'audit', 'admin', 'supervision'] },
    children: [
      {
        path: 'access',
        name: 'Access',
        component: () => import('@/views/enterprise-access/Access.vue'),
        meta: { title: '准入申请', roles: ['audit'] }
      },
      {
        path: 'access-result',
        name: 'AccessResult',
        component: () => import('@/views/enterprise-access/AccessResult.vue'),
        meta: { title: '准入结果查询', roles: ['recycle', 'audit']  }
      },
      {
        path: 'enterprise-list',
        name: 'EntList',
        component: () => import('@/views/enterprise-access/EntList.vue'),
        meta: { title: '企业列表', roles: ['admin', 'supervision'] }
      }
    ]
  },
  {
    path: '/battery',
    component: Layout,
    name: '电池管理',
    alwaysShow: true,
    meta: { title: '电池管理', icon: 'tree', roles: ['recycle', 'admin', 'supervision', 'customer', 'car', 'productor', 'rent', 'stored', 'safe', 'material'] },
    children: [
      {
        path: 'list',
        name: 'BatteryList',
        component: () => import('@/views/battery/BatteryList.vue'),
        meta: { title: '电池信息查询', roles: ['recycle', 'admin', 'supervision', 'customer', 'car', 'productor', 'rent', 'stored', 'safe', 'material'] }
      },
      {
        path: 'add',
        name: 'BatteryAdd',
        component: () => import('@/views/battery/BatteryAdd.vue'),
        meta: { title: '电池信息录入', roles: ['productor'] }
      },
    ]
  },
  {
    path: '/firstRecycle',
    component: Layout,
    name: '梯次利用',
    alwaysShow: true,
    meta: { title: '梯次利用', icon: 'tree', roles: ['productor', 'stored', 'car', 'rent']},
    children: [
      {
        path: 'apply',
        name: 'FirstRecycleAdd',
        component: () => import('@/views/battery/FirstRecycleAdd.vue'),
        meta: { title: '发起交易', roles: ['productor', 'stored', 'car', 'rent'] }
      },
      {
        path: 'myTrade',
        name: 'FirstRecycleMyTrade',
        component: () => import('@/views/battery/FirstRecycleMyTrade.vue'),
        meta: { title: '我的交易', roles: ['productor', 'stored', 'car', 'rent'] }
      },
      {
        path: 'tradeList',
        name: 'FirstRecycleTrade',
        component: () => import('@/views/battery/FirstRecycleTrade.vue'),
        meta: { title: '交易平台', roles: ['productor', 'stored', 'car', 'rent'] }
      },
    ]
  },
  {
    path: '/secondRecycle',
    component: Layout,
    name: '拆解回收',
    alwaysShow: true,
    meta: { title: '拆解回收', icon: 'example', roles: ['stored', 'recycle']},
    children: [
      {
        path: 'apply',
        name: 'SecondRecycleAdd',
        component: () => import('@/views/battery/SecondRecycleAdd.vue'),
        meta: { title: '发起交易', roles: ['stored', 'recycle'] }
      },
      {
        path: 'myTrade',
        name: 'SecondRecycleMyTrade',
        component: () => import('@/views/battery/SecondRecycleMyTrade.vue'),
        meta: { title: '我的交易', roles: ['stored', 'recycle'] }
      },
      {
        path: 'tradeList',
        name: 'SecondRecycleTrade',
        component: () => import('@/views/battery/SecondRecycleTrade.vue'),
        meta: { title: '交易平台', roles: ['stored', 'recycle'] }
      },
    ]
  },
  {
      path: '/point',
      component: Layout,
      name: '积分交易',
      alwaysShow: true, // will always show the root menu
      meta: { title: '积分交易', icon: 'tree', roles: ['recycle', 'admin', 'supervision', 'customer', 'car', 'productor', 'rent', 'stored', 'safe', 'material'] },
      children: [
        {
          path: 'tradeList',
          name: 'pointTradeList',
          component: () => import('@/views/point/TradeList.vue'),
          meta: { title: '交易所', roles: ['recycle', 'admin', 'supervision', 'customer', 'car', 'productor', 'rent', 'stored', 'safe', 'material'] }
        },
        {
          path: 'account',
          name: 'pointAccount',
          component: () => import('@/views/point/Account.vue'),
          meta: { title: '积分明细', roles: ['recycle', 'admin', 'supervision', 'customer', 'car', 'productor', 'rent', 'stored', 'safe', 'material']  }
        },
        {
          path: 'apply',
          name: 'pointApply',
          component: () => import('@/views/point/TradeApply.vue'),
          meta: { title: '发起交易', roles: ['recycle', 'admin', 'supervision', 'customer', 'car', 'productor', 'rent', 'stored', 'safe', 'material'] }
        },
        {
          path: 'myTradeList',
          name: 'pointMyTrade',
          component: () => import('@/views/point/MyTradeList.vue'),
          meta: { title: '查看我的交易', roles: ['recycle', 'admin', 'supervision', 'customer', 'car', 'productor', 'rent', 'stored', 'safe', 'material'] }
        },
      ]
    },



  // 404 page must be placed at the end !!!
  // using pathMatch install of "*" in vue-router 4.0
  { path: '/:pathMatch(.*)', redirect: '/404', hidden: true }
]

const router: Router = createRouter({
  history: createWebHashHistory(),
  scrollBehavior: () => ({ top: 0 }),
  routes: constantRoutes
})

// export function resetRouter() {
//   const newRouter = createRouter({
//     history: createWebHashHistory(),
//     scrollBehavior: () => ({ top: 0 }),
//     routes: constantRoutes
//   })
// }

export default router
