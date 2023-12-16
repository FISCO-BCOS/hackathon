import {createRouter, createWebHashHistory} from "vue-router";
import Home from "../views/Home.vue";

const routes = [
    {
        path: "/login",
        name: "Login",
        meta: {
            title: '登录'
        },
        component: () => import ( /* webpackChunkName: "login" */ "../views/login/Login.vue")
    }, 
    {
        path: "/register",
        name: "Register",
        meta: {
            title: '注册'
        },
        component: () => import ( /* webpackChunkName: "register" */ "../views/register/Register.vue")
    },
    {
        path: "/share",
        name: "Share",
        meta: {
            title: '系统首页'
        },
        component: () => import ( /* webpackChunkName: "register" */ "../views/share.vue")
    },
    {
        path: '/',
        redirect: '/dashboard'
    }, 
    {
        path: "/",
        name: "Home",
        component: Home,
        children: [
            {
                path: "/dashBoard",
                name: "dashBoard",
                meta: {
                    title: '系统首页'
                },
                component: () => import ( /* webpackChunkName: "dashboard" */ "../views/DashBoard.vue")
            }, 
            {
                path: "/projectSubmit",
                name: "projectSubmit",
                meta: {
                    title: 'ESG项目提交',
                    company: 'company',
                },
                component: () => import ( /* webpackChunkName: "form" */ "../views/company/ProjectSubmit.vue")
            }, 
            {
                path: "/projectManage",
                name: "projectManage",
                meta: {
                    title: 'ESG项目管理',
                    company: 'company',
                },
                component: () => import ( /* webpackChunkName: "table" */ "../views/company/ProjectManage.vue")
            }, 
            {
                path: "/projectScore",
                name: "projectScore",
                meta: {
                    title: 'ESG项目评分',
                    organization: 'organization',
                },
                component: () => import ( /* webpackChunkName: "form" */ "../views/organization/ProjectScore.vue")
            }, 
            {
                path: "/getProjectScore",
                name: "getProjectScore",
                meta: {
                    title: 'ESG评分查询',
                    organization: 'organization',
                },
                component: () => import ( /* webpackChunkName: "form" */ "../views/organization/GetProjectScore.vue")
            }, 
            {
                path: '/company403',
                name: 'company403',
                meta: {
                    title: '权限不足'
                },
                component: () => import (/* webpackChunkName: "403" */ '../views/company/Company403.vue')
            }, 
            {
                path: '/organization403',
                name: 'organization403',
                meta: {
                    title: '权限不足'
                },
                component: () => import (/* webpackChunkName: "403" */ '../views/organization/Organization403.vue')
            }, 
            {
                path: '/guest403',
                name: 'guest403',
                meta: {
                    title: '权限不足'
                },
                component: () => import (/* webpackChunkName: "403" */ '../views/guest/Guest403.vue')
            }, 
            {
                path: '/user',
                name: 'user',
                meta: {
                    title: '个人中心'
                },
                component: () => import (/* webpackChunkName: "user" */ '../views/User.vue')
            }, 
        ]
    }, 
];

const router = createRouter({
    history: createWebHashHistory(),
    routes
});

router.beforeEach((to, from, next) => {
    document.title = `${to.meta.title} | 问链`;
    // document.title = `问链ESG管理系统`;
    const userRole = localStorage.getItem('name');
    const userType = localStorage.getItem('accountType');

    // 需要拦截未注册未登录用户
    // if (!userRole && to.path !== '/login' && to.path !== '/register') {
    //     // next('/login');
    //     next('/login');
    // }
    
    if (!userRole && to.path !== '/login' && to.path !== '/register' && to.path !== '/share' ) {
        // next('/login');
        next('/share');
    }
    if (to.meta.permission) {

        console.log(userRole === 'admin');
        userRole === 'admin'
                ? next()
                : next('/403');
    }
    else if(to.meta.organization) {
    // if(to.meta.organization) {    
        if(userType === to.meta.organization || userRole === 'admin') {
            next();
        } else if(userType === 'company') {
            next('/organization403');
        } else {
            next('/guest403');
        }
    } 
    else if(to.meta.company) {
        if(userType === to.meta.company || userRole === 'admin') {
            next();
        } else if(userType === 'guest'){
            next('/guest403')
        } else {
            next('company403');
        }
    }
    else {
        next();
    }
});

export default router;