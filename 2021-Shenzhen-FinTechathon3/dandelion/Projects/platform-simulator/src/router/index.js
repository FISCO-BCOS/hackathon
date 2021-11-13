import Vue from 'vue'
import VueRouter from "vue-router";
let Frame = () => import('../views/Frame.vue')

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/index'
  },
  {
    // root
    path: '/index',
    redirect: '/index/play',
    component: Frame,
    children: [
      {
        path: 'play',
        name: "播放模拟",
        icon: "mdi-play-box-outline",
        component: () => import('../views/index/play.vue')
      },
      {
        path: 'issue',
        name: "视频上传模拟",
        icon: "mdi-hexagon-multiple",
        component: () => import('../views/index/issue.vue')
      },
      {
        path: 'portion',
        name: "分成管理",
        icon: "mdi-chart-arc",
        component: () => import('../views/index/portion.vue')
      },
      {
        path: 'platform',
        name: "平台方管理",
        icon: "mdi-theater",
        component: () => import('../views/index/platform.vue')
      },
      {
        path: 'adIssuer',
        name: "广告商管理",
        icon: "mdi-sign-text",
        component: () => import('../views/index/adIssuer.vue')
      }
    ]
  },
]

const router = new VueRouter({
  routes
})

export default router
