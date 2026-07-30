import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('../components/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '仪表盘' }
      },
      {
        path: 'posts',
        name: 'PostManage',
        component: () => import('../views/PostManage.vue'),
        meta: { title: '文章管理' }
      },
      {
        path: 'projects',
        name: 'ProjectManage',
        component: () => import('../views/ProjectManage.vue'),
        meta: { title: '项目管理' }
      },
      {
        path: 'resume',
        name: 'ResumeManage',
        component: () => import('../views/ResumeManage.vue'),
        meta: { title: '简历管理' }
      },
      {
        path: 'activities',
        name: 'ActivityManage',
        component: () => import('../views/ActivityManage.vue'),
        meta: { title: '活动管理' }
      },
      {
        path: 'settings',
        name: 'Settings',
        component: () => import('../views/Settings.vue'),
        meta: { title: '系统设置' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router
