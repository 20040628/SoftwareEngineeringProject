<template>
  <div class="login-form">
    <h2>User Login</h2>
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
      <button @click="login" :disabled="loading">
        {{ loading ? 'Logging in...' : 'Login' }}
      </button>
    </div>

    <div v-if="message" :class="['message', messageType]">
      <p>{{ message }}</p>
      <template v-if="loggedInUser">
        <p>User ID: {{ loggedInUser.userId }}</p>
        <p>Username: {{ loggedInUser.username }}</p>
      </template>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'UserLogin',
  data() {
    return {
      form: {
        username: '',
        password: ''
      },
      loading: false,
      message: '',
      messageType: 'success',
      loggedInUser: null,
      errors: {}
    }
  },
  methods: {
    async login() {
      this.loading = true
      this.message = ''
      this.loggedInUser = null
      this.errors = {}
      
      // Basic frontend validation
      if (!this.form.username || !this.form.password) {
        this.message = 'Please fill in all required fields'
        this.messageType = 'error'
        this.loading = false
        return
      }
      
      try {
        const response = await axios.post('http://localhost:8080/api/auth/login', this.form)
        
        this.message = response.data.message
        this.messageType = 'success'
        this.loggedInUser = {
          userId: response.data.userId,
          username: response.data.username,
          userType: response.data.userType,
          email: response.data.email
        }
        
        // Clear form
        this.form = {
          username: '',
          password: ''
        }
        
        // Emit login event
        this.$emit('login-success', this.loggedInUser)
      } catch (error) {
        if (error.response?.data && typeof error.response.data === 'object') {
          // Handle validation errors
          this.errors = error.response.data
          this.message = 'Please correct the errors below'
        } else {
          // Handle other errors
          this.message = error.response?.data || 'Login failed'
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
.login-form {
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

input {
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
</style> 