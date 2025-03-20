import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 设置axios的基本URL
axios.defaults.baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

// 检查并设置存储的token
const token = store.state.auth?.token
if (token) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
}

// 添加请求拦截器
axios.interceptors.request.use(
  async config => {
    // 如果是验证token的请求，跳过验证
    if (config.url && config.url.includes('/api/auth/validate')) {
      return config
    }
    
    // 如果存在token，确保设置在请求头中
    if (store.state.auth?.token) {
      config.headers.Authorization = `Bearer ${store.state.auth.token}`
    }
    
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 添加响应拦截器
axios.interceptors.response.use(
  response => {
    return response
  },
  error => {
    if (error.response && error.response.status === 401) {
      // 401未授权，清除token并跳转到登录页面
      store.dispatch('logout')
      router.push('/login')
    }
    return Promise.reject(error)
  }
)

const app = createApp(App)

app.use(router)
app.use(store)
app.use(ElementPlus)
app.mount('#app')
