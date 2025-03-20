<template>
  <div class="test-connection">
    <h2>后端连接测试</h2>
    <div class="card">
      <button @click="testConnection" :disabled="loading">
        {{ loading ? '测试中...' : '测试连接' }}
      </button>
      
      <div v-if="result" class="result" :class="{ success: !error, error: error }">
        <p>{{ result }}</p>
        <p v-if="timestamp">请求时间: {{ new Date(Number(timestamp)).toLocaleString() }}</p>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'TestConnection',
  data() {
    return {
      loading: false,
      result: '',
      error: false,
      timestamp: null
    }
  },
  methods: {
    async testConnection() {
      this.loading = true
      this.result = ''
      this.error = false
      
      try {
        const response = await axios.get('http://localhost:8080/api/test')
        this.result = response.data.message
        this.timestamp = response.data.timestamp
        this.error = false
      } catch (error) {
        this.result = '连接失败: ' + (error.response?.data?.message || error.message)
        this.error = true
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.test-connection {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}

.card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

button {
  background: #4CAF50;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

button:disabled {
  background: #cccccc;
  cursor: not-allowed;
}

.result {
  margin-top: 20px;
  padding: 15px;
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
</style> 