import { createRouter, createWebHistory } from 'vue-router'
import store from '../store'

// 延迟加载组件
const UserLogin = () => import('../views/login/index.vue')
// const UserRegister = () => import('../components/UserRegister.vue')
const ScooterAdd = () => import('../components/ScooterAdd/ScooterAdd.vue')
// const ScooterAdd = () => import('../components/ScooterAdd.vue')

// 路由配置
const routes = [
  {
    path: '/',
    redirect: '/admin/ScooterAdd'
  },
  {
    path: '/login',
    name: 'Login',
    component: UserLogin,
    meta: {
      requiresAuth: false
    }
  },
  // {
  //   path: '/register',
  //   name: 'Register',
  //   component: UserRegister,
  //   meta: {
  //     requiresAuth: false
  //   }
  // },
  // {
  //   path: '/scooters',
  //   name: 'ScooterList',
  //   component: ScooterAll,
  //   meta: {
  //     requiresAuth: true
  //   }
  // },
  {
    path: '/admin/ScooterAdd',
    name: 'ScooterAdd',
    component: ScooterAdd,
    meta: {
      requiresAuth: true,
      requiresAdmin: true
    }
  },
  // {
  //   path: '/my-bookings',
  //   name: 'MyBookings',
  //   component: () => import('../components/OrderAll.vue'),
  //   meta: {
  //     requiresAuth: true
  //   }
  // },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/admin/ScooterAdd'
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
      console.log('User:', user.role); // 输出完整的 user 对象
      console.log('User:', user); // 输出完整的 user 对象
      if (!user || user.role !== 0) {
        // 没有管理员权限，重定向到滑板车列表页
        return next({ name: 'Login' })
      }
    }
  }

  // 其他情况正常导航
  next()
})

export default router