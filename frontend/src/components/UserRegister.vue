<template>
  <div class="register-form">
    <h2>User Registration</h2>
    <div class="form-group">
      <label>Username:</label>
      <input v-model="form.username" type="text" placeholder="Enter username">
      <span v-if="errors.username" class="error-message">{{ errors.username }}</span>
    </div>
    
    <div class="form-group">
      <label>Password:</label>
      <input v-model="form.password" type="password" placeholder="Enter password">
      <span v-if="errors.password" class="error-message">{{ errors.password }}</span>
    </div>
    
    <div class="form-group">
      <label>Email:</label>
      <input v-model="form.email" type="email" placeholder="Enter email">
      <span v-if="errors.email" class="error-message">{{ errors.email }}</span>
    </div>
    
    <div class="form-group">
      <label>Mobile:</label>
      <input v-model="form.mobile" type="text" placeholder="Enter mobile number">
      <span v-if="errors.mobile" class="error-message">{{ errors.mobile }}</span>
    </div>
    
    <div class="form-group">
      <label for="birthday">出生日期:</label>
      <input
        type="date"
        id="birthday"
        v-model="form.birthday"
        required
        class="form-control"
      />
      <small class="form-text text-muted">生日信息将用于自动判断您的折扣资格</small>
    </div>

    <p class="discount-info">
      注册后，系统将根据您的出生日期自动判断折扣资格:
    </p>
    <ul class="discount-list">
      <li>18-25岁的用户享受学生折扣 (15%)</li>
      <li>60岁以上的用户享受老年人折扣 (20%)</li>
      <li>每周租赁8小时以上的用户享受常客折扣 (10%)</li>
      <li>折扣可叠加使用，为您提供更多优惠!</li>
    </ul>

    <div class="form-group">
      <button @click="register" :disabled="loading">
        {{ loading ? 'Registering...' : 'Register' }}
      </button>
    </div>

    <div v-if="message" :class="['message', messageType]">
      <p>{{ message }}</p>
      <template v-if="registeredUser">
        <p>User ID: {{ registeredUser.userId }}</p>
        <p>Username: {{ registeredUser.username }}</p>
      </template>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'UserRegister',
  data() {
    return {
      form: {
        username: '',
        password: '',
        email: '',
        mobile: '',
        birthday: '',
        userType: 0,  // default: normal user
        paymentMethod: null  // default: none
      },
      loading: false,
      message: '',
      messageType: 'success',
      registeredUser: null,
      errors: {}
    }
  },
  methods: {
    async register() {
      this.loading = true
      this.message = ''
      this.registeredUser = null
      this.errors = {}
      
      // Basic frontend validation
      if (!this.form.username || !this.form.password || !this.form.email || 
          !this.form.mobile || !this.form.birthday) {
        this.message = 'Please fill in all required fields'
        this.messageType = 'error'
        this.loading = false
        return
      }
      
      try {
        // 打印生日信息，调试用
        console.log("注册用户生日（原始值）:", this.form.birthday);
        
        const birthDate = new Date(this.form.birthday);
        console.log("注册用户生日（Date对象）:", birthDate);
        console.log("注册用户生日（ISO字符串）:", birthDate.toISOString());
        
        // 使用ISO格式的日期字符串，保留日期部分
        const birthDateString = birthDate.toISOString().split('T')[0];
        console.log("注册用户生日（最终发送格式）:", birthDateString);
        
        const response = await axios.post('http://localhost:8080/api/auth/register', {
          ...this.form,
          birthday: birthDateString // 使用YYYY-MM-DD格式
        })
        
        this.message = response.data.message
        this.messageType = 'success'
        this.registeredUser = {
          userId: response.data.userId,
          username: response.data.username
        }
        
        // Clear form
        this.form = {
          username: '',
          password: '',
          email: '',
          mobile: '',
          birthday: '',
          userType: 0,
          paymentMethod: null
        }
      } catch (error) {
        if (error.response?.data && typeof error.response.data === 'object') {
          // Handle validation errors
          this.errors = error.response.data
          this.message = 'Please correct the errors below'
        } else {
          // Handle other errors
          this.message = error.response?.data || 'Registration failed'
        }
        this.messageType = 'error'
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.register-form {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input, select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  width: 100%;
  padding: 10px;
  background: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:disabled {
  background: #cccccc;
}

.message {
  margin-top: 15px;
  padding: 10px;
  border-radius: 4px;
}

.success {
  background: #E8F5E9;
  color: #2E7D32;
}

.error {
  background: #FFEBEE;
  color: #C62828;
}

.error-message {
  color: #C62828;
  font-size: 0.8em;
  margin-top: 5px;
  display: block;
}

.discount-list {
  margin-top: 0;
  margin-bottom: 15px;
  padding-left: 30px;
}

.discount-info {
  margin-bottom: 5px;
}
</style> 