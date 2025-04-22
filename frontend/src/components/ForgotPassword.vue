<template>
  <div class="forgot-password-container">
    <div class="forgot-password-form">
      <h2>找回密码</h2>
      
      <div v-if="successMessage" class="alert alert-success">
        {{ successMessage }}
      </div>
      
      <div v-if="errorMessage" class="alert alert-danger">
        {{ errorMessage }}
      </div>
      
      <form @submit.prevent="sendResetCode">
        <div class="form-group">
          <label for="email">邮箱</label>
          <input 
            type="email" 
            id="email" 
            v-model="email" 
            class="form-control" 
            required
            placeholder="请输入您的注册邮箱"
          />
        </div>
        
        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="loading">
            {{ loading ? '发送中...' : '发送验证码' }}
          </button>
          <router-link to="/login" class="btn btn-link">返回登录</router-link>
        </div>
      </form>
      
      <div v-if="codeSent" class="reset-section">
        <h3>重置密码</h3>
        <form @submit.prevent="resetPassword">
          <div class="form-group">
            <label for="resetCode">验证码</label>
            <input 
              type="text" 
              id="resetCode" 
              v-model="resetCode" 
              class="form-control" 
              required
              placeholder="请输入邮箱收到的6位验证码"
            />
          </div>
          
          <div class="form-group">
            <label for="newPassword">新密码</label>
            <input 
              type="password" 
              id="newPassword" 
              v-model="newPassword" 
              class="form-control" 
              required
              placeholder="请设置新密码"
            />
            <small class="form-text text-muted">
              密码至少8位，需包含大小写字母和数字
            </small>
          </div>
          
          <div class="form-group">
            <label for="confirmPassword">确认密码</label>
            <input 
              type="password" 
              id="confirmPassword" 
              v-model="confirmPassword" 
              class="form-control" 
              required
              placeholder="请再次输入新密码"
            />
          </div>
          
          <div class="form-actions">
            <button type="submit" class="btn btn-primary" :disabled="resetLoading">
              {{ resetLoading ? '提交中...' : '重置密码' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'ForgotPassword',
  setup() {
    const router = useRouter()
    
    const email = ref('')
    const resetCode = ref('')
    const newPassword = ref('')
    const confirmPassword = ref('')
    
    const loading = ref(false)
    const resetLoading = ref(false)
    const codeSent = ref(false)
    const successMessage = ref('')
    const errorMessage = ref('')
    
    // 发送重置验证码
    const sendResetCode = async () => {
      loading.value = true
      errorMessage.value = ''
      successMessage.value = ''
      
      try {
        const response = await axios.post('http://localhost:8080/api/auth/forgot-password', {
          email: email.value
        })
        
        codeSent.value = true
        successMessage.value = response.data.message
      } catch (error) {
        if (error.response?.data?.error) {
          errorMessage.value = error.response.data.error
        } else {
          errorMessage.value = '发送验证码失败，请稍后重试'
        }
      } finally {
        loading.value = false
      }
    }
    
    // 重置密码
    const resetPassword = async () => {
      if (newPassword.value !== confirmPassword.value) {
        errorMessage.value = '两次输入的密码不一致'
        return
      }
      
      resetLoading.value = true
      errorMessage.value = ''
      successMessage.value = ''
      
      try {
        const response = await axios.post('http://localhost:8080/api/auth/reset-password', {
          email: email.value,
          resetCode: resetCode.value,
          newPassword: newPassword.value
        })
        
        successMessage.value = response.data.message
        
        // 3秒后跳转到登录页
        setTimeout(() => {
          router.push('/login')
        }, 3000)
      } catch (error) {
        if (error.response?.data?.error) {
          errorMessage.value = error.response.data.error
        } else {
          errorMessage.value = '重置密码失败，请稍后重试'
        }
      } finally {
        resetLoading.value = false
      }
    }
    
    return {
      email,
      resetCode,
      newPassword,
      confirmPassword,
      loading,
      resetLoading,
      codeSent,
      successMessage,
      errorMessage,
      sendResetCode,
      resetPassword
    }
  }
}
</script>

<style scoped>
.forgot-password-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 80vh;
  background-color: #f5f5f5;
}

.forgot-password-form {
  width: 100%;
  max-width: 400px;
  padding: 30px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

h2, h3 {
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

.alert-success {
  background-color: #e8f5e9;
  color: #388e3c;
  border: 1px solid #c8e6c9;
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

.btn-primary:disabled {
  background-color: #a0c3e8;
  cursor: not-allowed;
}

.btn-link {
  color: #4a90e2;
  text-decoration: none;
}

.btn-link:hover {
  text-decoration: underline;
}

.reset-section {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.form-text {
  display: block;
  margin-top: 5px;
  font-size: 12px;
}
</style> 