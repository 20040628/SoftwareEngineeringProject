<template>
  <div class="login-container">
    <div class="login-form">
      <h2>用户登录</h2>
      
      <div v-if="errorMessage" class="alert alert-danger">
        {{ errorMessage }}
      </div>
      
      <form @submit.prevent="login">
        <div class="form-group">
          <label for="username">用户名</label>
          <input 
            type="text" 
            id="username" 
            v-model="loginForm.username" 
            class="form-control" 
            required
          />
        </div>
        
        <div class="form-group">
          <label for="password">密码</label>
          <input 
            type="password" 
            id="password" 
            v-model="loginForm.password" 
            class="form-control" 
            required
          />
        </div>
        
        <div class="form-group captcha-group">
          <label for="captcha">验证码</label>
          <div class="captcha-container">
            <input 
              type="text" 
              id="captcha" 
              v-model="loginForm.captcha" 
              class="form-control captcha-input" 
              required
              placeholder="请输入验证码"
            />
            <div class="captcha-image" @click="refreshCaptcha">
              <img v-if="captchaImage" :src="captchaImage" alt="验证码" />
              <div v-else class="captcha-loading">加载中...</div>
            </div>
          </div>
        </div>
        
        <div class="form-actions">
          <button type="submit" class="btn btn-primary">登录</button>
          <router-link to="/register" class="btn btn-link">注册新账号</router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ref, reactive, onMounted } from 'vue'

export default {
  name: 'UserLogin',
  setup() {
    const store = useStore()
    const router = useRouter()
    
    const errorMessage = ref('')
    const captchaImage = ref('')
    const captchaKey = ref('')
    
    const loginForm = reactive({
      username: '',
      password: '',
      captcha: '',
      captchaKey: ''
    })
    
    // 获取验证码
    const getCaptcha = async () => {
      try {
        const response = await axios.get('http://localhost:8080/api/auth/captcha')
        captchaImage.value = response.data.captchaImageBase64
        captchaKey.value = response.data.captchaKey
        loginForm.captchaKey = response.data.captchaKey
      } catch (error) {
        console.error('获取验证码失败:', error)
        errorMessage.value = '获取验证码失败，请刷新页面重试'
      }
    }
    
    // 刷新验证码
    const refreshCaptcha = () => {
      captchaImage.value = ''
      loginForm.captcha = ''
      getCaptcha()
    }
    
    // 登录
    const login = async () => {
      try {
        errorMessage.value = ''
        
        // 确保验证码已经加载
        if (!loginForm.captchaKey) {
          errorMessage.value = '验证码未加载，请刷新验证码'
          return
        }
        
        const response = await axios.post('http://localhost:8080/api/auth/login', loginForm)
        
        if (response.data && response.data.token) {
          console.log('登录成功获取的令牌：', response.data.token)
          console.log('用户信息：', {
            userId: response.data.userId,
            username: response.data.username,
            userType: response.data.userType,
            role: response.data.role
          })
          
          // 登录成功，保存令牌和用户信息到Vuex store
          store.dispatch('login', {
            token: response.data.token,
            user: {
              userId: response.data.userId,
              username: response.data.username,
              userType: response.data.userType,
              role: response.data.role
            }
          })
          
          // 根据用户角色导航到相应页面
          if (response.data.role === 0) {
            router.push('/admin/dashboard')
          } else {
            router.push('/scooters')
          }
        }
      } catch (error) {
        // 刷新验证码
        refreshCaptcha()
        
        if (error.response) {
          // 处理验证码错误
          if (error.response.data.captcha) {
            errorMessage.value = error.response.data.captcha
          } else {
            errorMessage.value = error.response.data.message || '登录失败，请检查用户名和密码'
          }
        } else {
          errorMessage.value = '无法连接到服务器，请稍后再试'
        }
      }
    }
    
    // 组件挂载时获取验证码
    onMounted(() => {
      getCaptcha()
    })
    
    return {
      loginForm,
      errorMessage,
      captchaImage,
      login,
      refreshCaptcha
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
  background-color: #f5f5f5;
}

.login-form {
  width: 100%;
  max-width: 400px;
  padding: 30px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  margin-bottom: 25px;
  color: #333;
  font-weight: 500;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #555;
}

.form-control {
  width: 100%;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.form-control:focus {
  border-color: #4a90e2;
  outline: none;
  box-shadow: 0 0 0 2px rgba(74, 144, 226, 0.2);
}

.alert {
  padding: 12px 15px;
  margin-bottom: 20px;
  border-radius: 4px;
}

.alert-danger {
  background-color: #ffebee;
  color: #d32f2f;
  border: 1px solid #ffcdd2;
}

.captcha-container {
  display: flex;
  align-items: center;
}

.captcha-input {
  flex: 1;
  margin-right: 10px;
}

.captcha-image {
  width: 130px;
  height: 48px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  border-radius: 4px;
  overflow: hidden;
}

.captcha-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.captcha-loading {
  font-size: 12px;
  color: #999;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 25px;
}

.btn {
  padding: 10px 20px;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary {
  background-color: #4a90e2;
  color: white;
  border: none;
}

.btn-primary:hover {
  background-color: #3a7fcb;
}

.btn-link {
  color: #4a90e2;
  text-decoration: none;
}

.btn-link:hover {
  text-decoration: underline;
}
</style> 