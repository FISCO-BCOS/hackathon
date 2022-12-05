import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 解决路由重复点击报错
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

const routes = [
    {
        path: '/',
        redirect: '/login',
    },
    {
        path: '/login',// 登录
        name: 'login',
        component: () => import('../views/login/index.vue')
    },
    {
        path: '/layout',// 布局页
        name: 'layout',
        redirect: '/home',
        meta: {title: '首页', roles: ['input', 'approve']},
        component: () => import('../layout/index.vue'),
        children: [
            {
                path: '/home',// 首页
                name: 'home',
                meta: {title: '首页', roles: ['input', 'approve']},
                component: () => import('../views/home/index.vue')
            },
            {
                path: '/loan-application',// 贷款申请
                name: 'loan-application',
                meta: {title: '添加设备', roles: ['input']},
                component: () => import('../views/loan-application/index.vue')
            },
            {
                path: '/loan-management',
                name: 'loan-management',
                meta: {title: '设备管理', roles: ['input']},
                component: () => import('../views/loan-management/index.vue')
            },
            {
                path: '/loan-approve',// 贷款审批
                name: 'loan-approve',
                meta: {title: '智慧运维', roles: ['approve']},
                redirect: '/loan-approve/first',
                component: () => import('../views/loan-approve/index.vue'),
                children: [
                    {
                        path: 'maintain',// 初审
                        name: 'maintain',
                        meta: {title: '设备维修'},
                        component: () => import('../views/loan-approve/maintain.vue')
                    },
                    {
                        path: 'end',// 终审
                        name: 'end',
                        meta: {title: '终审'},
                        component: () => import('../views/loan-approve/end.vue')
                    }
                ]
            },
            {
                path: '/fault-management',
                name: 'fault-management',
                meta: {title: '维修管理', roles: ['approve']},
                component: () => import('../views/fault-management/index.vue'),
                children: [
                    // {
                    //     path: 'faultModel',
                    //     name: 'faultModel',
                    //     meta: {title: 'BGFL模型'},
                    //     component: () => import('../views/fault-management/faultModel.vue')
                    // },
                    {
                        path: 'fault',
                        name: 'fault',
                        meta: {title: '故障管理'},
                        component: () => import('../views/fault-management/fault.vue')
                    },
                    // {
                    //     path: 'faultModelNB',
                    //     name: 'faultModelNB',
                    //     meta: {title: '性能对比'},
                    //     component: () => import('../views/fault-management/faultModelNB')
                    // }
                ]
            },
            {
                path: '/maintain-management',
                name: 'maintain-management',
                meta: {title: '维修管理', roles: ['approve']},
                component: () => import('../views/maintain-management/index.vue'),
                children: [
                    {
                        path: 'createStaff',
                        name: 'createStaff',
                        meta: {title: '添加维修人员'},
                        component: () => import('../views/maintain-management/createStaff.vue')
                    },
                    {
                        path: 'staff',
                        name: 'staff',
                        meta: {title: '维修人员'},
                        component: () => import('../views/maintain-management/staff.vue')
                    },
                    {
                        path: 'equipmentRepair',
                        name: 'equipmentRepair',
                        meta: {title: '设备维修'},
                        component: () => import('../views/maintain-management/equipmentRepair.vue')
                    }
                ]
            },
            {
                path: '/access-control',
                name: 'control',
                meta: {title: '权限管理'},
                redirect: '/access-control/userList',
                component: () => import('../views/access-control/index.vue'),
                children: [
                    {
                        path: 'userList',
                        name: 'userList',
                        meta: {title: '用户展示'},
                        component: () => import('../views/access-control/userList.vue')
                    },
                    {
                        path: 'createUser',
                        name: 'createUser',
                        meta: {title: '创建管理员'},
                        component: () => import('../views/access-control/createUser')
                    }
                ]
            },
            {
                path: '/user-logs',//用户日志
                name: 'user-logs',
                meta: {title: '用户日志', roles: ['approve']},
                component: () => import('../views/user-logs/index.vue')
            },
            {
                path: '/transactions',
                name: 'transactions',
                meta: {title: '资源交易', roles: ['approve']},
                component: () => import('../views/transactions/index.vue'),
                children: [
                    {
                        path: 'createUser',
                        name: 'createUser',
                        meta: {title: '创建用户'},
                        component: () => import('../views/transactions/createUser')
                    },
                    {
                        path: 'model',
                        name: 'model',
                        meta: {title: '博弈模型'},
                        component: () => import('../views/transactions/model.vue')
                    },
                    {
                        path: 'resourceTransaction',
                        name: 'resourceTransaction',
                        meta: {title: '资源交易'},
                        component: () => import('../views/transactions/resourceTransaction.vue')
                    },
                    {
                        path: 'modelNB',
                        name: 'modelNB',
                        meta: {title: '性能对比'},
                        component: () => import('../views/transactions/modelNB.vue')
                    }
                ]
            },
        ]
    }
]

const router = new VueRouter({
    routes
})

export default router
