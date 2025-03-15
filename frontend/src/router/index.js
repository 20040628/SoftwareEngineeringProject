import { createRouter, createWebHistory } from 'vue-router'
import store from '../store'

// 延迟加载组件
const UserLogin = () => import('../components/UserLogin.vue')
const UserRegister = () => import('../components/UserRegister.vue')
const ScooterAll = () => import('../components/ScooterAll.vue')
const ScooterAdd = () => import('../components/ScooterAdd.vue')

// 路由配置
const routes = [
  {
    path: '/',
    redirect: '/scooters'
  },
  {
    path: '/login',
    name: 'Login',
    component: UserLogin,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: UserRegister,
    meta: {
      requiresAuth: false
    }
  },
  {
    path: '/scooters',
    name: 'ScooterList',
    component: ScooterAll,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/admin/dashboard',
    name: 'AdminDashboard',
    component: ScooterAdd,
    meta: {
      requiresAuth: true,
      requiresAdmin: true
    }
  },
  {
    path: '/my-bookings',
    name: 'MyBookings',
    component: () => import('../components/OrderAll.vue'),
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/scooters'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 导航守卫
router.beforeEach(async (to, from, next) => {
  // 如果路由需要认证
  if (to.meta.requiresAuth) {
    // 检查是否已登录
    if (!store.getters.isAuthenticated) {
      // 未登录，重定向到登录页
      return next({ name: 'Login' })
    }
    
    // 如果路由需要管理员权限
    if (to.meta.requiresAdmin) {
      const user = store.getters.user
      if (!user || user.userType !== 'ADMIN') {
        // 没有管理员权限，重定向到滑板车列表页
        return next({ name: 'ScooterList' })
      }
    }
  }
  
  // 如果已登录但尝试访问登录/注册页
  if (!to.meta.requiresAuth && store.getters.isAuthenticated) {
    if (to.name === 'Login' || to.name === 'Register') {
      // 已登录用户重定向到滑板车列表页
      return next({ name: 'ScooterList' })
    }
  }
  
  // 其他情况正常导航
  next()
})

export default router 