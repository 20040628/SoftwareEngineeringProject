import { createRouter, createWebHistory } from 'vue-router'
import store from '../store'

// 延迟加载组件
const UserLogin = () => import('../views/login/index.vue')
const MainLayout = () => import('@/components/MainLayout.vue')
const Payment = () => import('@/components/Payment.vue')
const EditScooter = () => import('@/components/EditScooter.vue')
const ChoosePayment = () => import('@/components/ChoosePayment.vue')
const NewCard = () => import('@/components/NewCard.vue')

// 路由配置
const routes = [
  {
    path: '/',
    redirect: '/login'
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
    path: '/scooters/:id/edit',
    name: 'EditScooter',
    component: EditScooter,
    props: true
  },
  {
    path: '/',
    component: MainLayout,
    children: [
      { path: '/add_scooter', component: () => import('@/components/ScooterAdd.vue') },
      { path: '/user-feedback', component: () => import('@/components/UserFeedback.vue') },
      { path: '/all-scooters', component: () => import('@/components/AllScooters.vue') },
      { path: '/data-analysis', component: () => import('@/components/DataAnalysis.vue') },
      { path: '/order-management', component: () => import('@/components/BookingManagement.vue') },
      { path: '/user-management', component: () => import('@/components/UserManagement.vue') },
      { path: '/logout', component: () => import('@/components/Logout.vue') },
      { path: '/add_user', component: () => import('@/components/AddUser.vue') },
      { path: '/add_order', component: () => import('@/components/AddOrder.vue') },
      { path: '/add_store', component: () => import('@/components/AddStore.vue') },
    ],
  },
  {
    path: '/payment/:orderId/:userId',
    name: 'Payment',
    component: Payment,
  },
  {
    path: '/NewCard/:orderId/:userId',
    name: 'NewCard',
    component: NewCard,
  },
  {
    path: '/ChoosePayment/:orderId/:userId',
    name: 'ChoosePayment',
    component: ChoosePayment,
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login'
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
      console.log('Vuex Store 中的 token：', store.state.auth.token);
      if (!user || user.role !== 0) {
        // 没有管理员权限，重定向到滑板车列表页
        console.log("return to login");
        return next({ name: 'Login' })
      }
    }
  }

  // 其他情况正常导航
  next()
})

export default router
