<template>
  <div class="user-profile">
    <h2>用户资料</h2>
    
    <div v-if="loading" class="loading">
      正在加载用户资料...
    </div>
    
    <div v-else-if="error" class="error">
      {{ error }}
    </div>
    
    <div v-else class="profile-container">
      <div class="profile-section">
        <h3>账户信息</h3>
        <div class="profile-field">
          <label>用户名:</label>
          <span>{{ user.username }}</span>
        </div>
        
        <div class="profile-field">
          <label>邮箱:</label>
          <input type="email" v-model="userForm.email" />
        </div>
        
        <div class="profile-field">
          <label>手机号码:</label>
          <input type="tel" v-model="userForm.mobile" />
        </div>
        
        <div class="profile-field">
          <label>出生日期:</label>
          <input type="date" v-model="userForm.birthDate" />
          <span class="field-hint">用于自动判断折扣资格</span>
        </div>
        
        <!-- 添加调试信息 -->
        <div class="debug-info" v-if="user.birthday">
          <p>系统记录的生日: {{ formatDate(user.birthday) }}</p>
          <p>当前年龄: {{ calculateAge(user.birthday) }} 岁</p>
        </div>
      </div>
      
      <div class="profile-section">
        <h3>折扣资格状态</h3>
        <div class="discount-info">
          <p>系统会根据您的出生日期和订单信息自动判断折扣资格。</p>
        </div>
        
        <div class="status-item">
          <div class="status-label">学生身份 (15% 折扣):</div>
          <div class="status-badge" :class="{ active: user.isStudent === 1 }">
            {{ user.isStudent === 1 ? '已激活' : '未激活' }}
          </div>
          <p class="status-info" v-if="user.isStudent !== 1">
            18-25岁的用户自动获得学生折扣
          </p>
        </div>
        
        <div class="status-item">
          <div class="status-label">老年人身份 (20% 折扣):</div>
          <div class="status-badge" :class="{ active: user.isSenior === 1 }">
            {{ user.isSenior === 1 ? '已激活' : '未激活' }}
          </div>
          <p class="status-info" v-if="user.isSenior !== 1">
            60岁以上的用户自动获得老年人折扣
          </p>
        </div>
        
        <div class="status-item">
          <div class="status-label">常客身份 (10% 折扣):</div>
          <div class="status-badge" :class="{ active: user.isFrequentUser === 1 }">
            {{ user.isFrequentUser === 1 ? '已激活' : '未激活' }}
          </div>
          <p class="status-info" v-if="user.isFrequentUser !== 1">
            每周累计租赁8小时以上自动获得常客折扣
          </p>
        </div>
      </div>
      
      <div class="action-buttons">
        <button @click="saveProfile" :disabled="saving">
          {{ saving ? '保存中...' : '保存更改' }}
        </button>
        <button @click="goBack" class="secondary">取消</button>
      </div>
      
      <div v-if="message" :class="['message', messageType]">
        {{ message }}
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { useStore } from 'vuex';
import { computed } from 'vue';
import { useRouter } from 'vue-router';

export default {
  name: 'UserProfile',
  setup() {
    const store = useStore();
    const router = useRouter();
    
    const isAuthenticated = computed(() => store.getters.isAuthenticated);
    const token = computed(() => store.getters.token);
    const currentUser = computed(() => store.getters.user);
    
    return {
      isAuthenticated,
      token,
      currentUser,
      router
    };
  },
  data() {
    return {
      user: {},
      userForm: {
        email: '',
        mobile: '',
        birthDate: ''
      },
      loading: true,
      saving: false,
      error: null,
      message: '',
      messageType: 'success'
    };
  },
  mounted() {
    if (!this.isAuthenticated) {
      this.router.push('/login');
      return;
    }
    
    this.fetchUserProfile();
  },
  methods: {
    async fetchUserProfile() {
      this.loading = true;
      this.error = null;
      
      try {
        const userId = this.currentUser.userId;
        const response = await axios.get(`http://localhost:8080/api/users/${userId}`, {
          headers: {
            Authorization: `Bearer ${this.token}`
          }
        });
        
        this.user = response.data;
        
        // 复制需要编辑的字段到表单对象
        this.userForm = {
          email: this.user.email || '',
          mobile: this.user.mobile || '',
          birthDate: this.user.birthDate || ''
        };
      } catch (error) {
        console.error('获取用户资料失败:', error);
        this.error = '无法加载用户资料，请稍后再试';
      } finally {
        this.loading = false;
      }
    },
    
    async saveProfile() {
      this.saving = true;
      this.error = null;
      this.message = '';
      
      try {
        // 处理生日格式
        const formData = { ...this.userForm };
        
        // 如果有生日值，确保以YYYY-MM-DD格式发送
        if (formData.birthDate) {
          // 先转成Date对象再提取日期部分，以确保格式一致
          const birthDate = new Date(formData.birthDate);
          formData.birthDate = birthDate.toISOString().split('T')[0];
          console.log('保存的生日格式:', formData.birthDate);
        }
        
        const userId = this.currentUser.userId;
        const response = await axios.put(`http://localhost:8080/api/users/${userId}`, formData, {
          headers: {
            Authorization: `Bearer ${this.token}`
          }
        });
        
        this.user = response.data;
        this.message = '资料更新成功';
        this.messageType = 'success';
        
        // 3秒后自动关闭消息
        setTimeout(() => {
          this.message = '';
        }, 3000);
      } catch (error) {
        console.error('更新资料失败:', error);
        this.message = error.response?.data || '更新资料失败，请重试';
        this.messageType = 'error';
      } finally {
        this.saving = false;
      }
    },
    
    goBack() {
      this.router.push('/scooters');
    },
    
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleDateString();
    },
    
    calculateAge(dateString) {
      if (!dateString) return '';
      const birthday = new Date(dateString);
      const today = new Date();
      
      let age = today.getFullYear() - birthday.getFullYear();
      const monthDiff = today.getMonth() - birthday.getMonth();
      
      // 如果还没到生日，年龄减1
      if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthday.getDate())) {
        age--;
      }
      
      return age;
    }
  }
};
</script>

<style scoped>
.user-profile {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.profile-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.profile-section {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h2 {
  color: #333;
  margin-bottom: 20px;
}

h3 {
  color: #444;
  margin-bottom: 15px;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}

.profile-field {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.profile-field label {
  min-width: 120px;
  font-weight: bold;
  color: #555;
}

.profile-field input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1em;
}

.field-hint {
  margin-left: 10px;
  font-size: 0.9em;
  color: #888;
}

.status-item {
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.status-item:last-child {
  border-bottom: none;
}

.status-label {
  font-weight: bold;
  color: #555;
  margin-bottom: 5px;
}

.status-badge {
  background-color: #f0f0f0;
  color: #888;
  padding: 5px 10px;
  border-radius: 4px;
  display: inline-block;
  margin-bottom: 5px;
}

.status-badge.active {
  background-color: #dff0d8;
  color: #3c763d;
}

.status-info {
  color: #888;
  font-size: 0.9em;
}

.discount-info {
  margin-bottom: 15px;
  color: #666;
}

.action-buttons {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1em;
  background-color: #4caf50;
  color: white;
}

button:hover {
  background-color: #45a049;
}

button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

button.secondary {
  background-color: #f0f0f0;
  color: #333;
}

button.secondary:hover {
  background-color: #e0e0e0;
}

.loading, .error {
  text-align: center;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  margin-top: 20px;
}

.error {
  color: #d9534f;
}

.message {
  margin-top: 15px;
  padding: 10px;
  border-radius: 4px;
  text-align: center;
}

.message.success {
  background-color: #dff0d8;
  color: #3c763d;
}

.message.error {
  background-color: #f2dede;
  color: #a94442;
}

.debug-info {
  margin-top: 15px;
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 4px;
  color: #666;
  font-size: 0.9em;
}

.debug-info p {
  margin: 5px 0;
}
</style> 