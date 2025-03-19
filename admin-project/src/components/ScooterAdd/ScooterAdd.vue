<template>
  <div class="page">
    <!-- 侧边栏 -->
    <div class="sidebar">
      <div class="user">
        <img src="@/static/center/avatar.png" alt="avatar" />
        <span>Administrator</span>
      </div>
      <div class="menu">
        <div
            class="menu-item"
            v-for="v in menus"
            :key="v.id"
            :class="{ active: activeMenu === v.id }"
            @click="selectMenu(v.id)"
        >
          <img :src="v.icon" alt="menu-icon" />
          <span>{{ v.name }}</span>
        </div>
      </div>
    </div>

    <!-- 主要内容区 -->
    <div class="content">
      <!-- 滑板车管理 -->
      <div v-if="activeMenu === 1">
        <h2 class="title">Scooter Management</h2>
        <!-- 添加滑板车表单 -->
        <div class="form">
          <input v-model="newScooter.location" placeholder="Enter location" class="input" />
          <input v-model="newScooter.priceHour" type="number" placeholder="Price per hour" class="input" />
          <input v-model="newScooter.priceFourHour" type="number" placeholder="Price for four hours" class="input" />
          <input v-model="newScooter.priceDay" type="number" placeholder="Price per day" class="input" />
          <input v-model="newScooter.priceWeek" type="number" placeholder="Price per week" class="input" />
          <input v-model="newScooter.status" type="number" placeholder="Status (0: inactive, 1: active)" class="input" />
          <input v-model="newScooter.longitude" type="number" placeholder="Longitude" class="input" />
          <input v-model="newScooter.latitude" type="number" placeholder="Latitude" class="input" />

          <button class="button" @click="addScooter">Add Scooter</button>
        </div>
      </div>

      <!-- 用户反馈管理 -->
      <div v-if="activeMenu === 2">
        <h2 class="title">User Feedback</h2>
        <div class="feedback-list">
          <div class="feedback-item" v-for="feedback in feedbacks" :key="feedback.id">
            <div class="feedback-info">
              <div class="info-row">
                <span class="label">ID:</span> <span class="value">{{ feedback.id }}</span>
              </div>
              <div class="info-row">
                <span class="label">User ID:</span> <span class="value">{{ feedback.userId }}</span>
              </div>
              <div class="info-row">
                <span class="label">Content:</span> <span class="value">{{ feedback.content }}</span>
              </div>
              <div class="info-row">
                <span class="label">Create Time:</span> <span class="value">{{ feedback.createTime }}</span>
              </div>
              <div class="info-row">
                <span class="label">Status:</span> <span class="value">{{ feedback.status }}</span>
              </div>
              <div class="info-row">
                <span class="label">Priority:</span> <span class="value">{{ feedback.priority }}</span>
              </div>
              <div class="info-row">
                <span class="label">Admin Response:</span> <span class="value">{{ feedback.adminResponse }}</span>
              </div>
              <div class="info-row">
                <span class="label">Response Time:</span> <span class="value">{{ feedback.responseTime }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 显示所有滑板车 -->
      <div v-if="activeMenu === 3">
        <h2 class="title">Show all scooters</h2>
        <div class="scooter-list">
          <div class="scooter-item" v-for="scooter in scooters" :key="scooter.id">
            <div class="scooter-info">
              <div class="info-row">
                <span class="label">ID:</span> <span class="value">{{ scooter.id }}</span>
              </div>
              <div class="info-row">
                <span class="label">Location:</span> <span class="value">{{ scooter.location }}</span>
              </div>
              <div class="info-row">
                <span class="label">Price:</span>
                <span class="value">
                  ${{ scooter.priceHour }}/hr | ${{ scooter.priceDay }}/day | ${{ scooter.priceWeek }}/week
                </span>
              </div>
              <div class="info-row">
                <span class="label">Coordinates:</span>
                <span class="value">({{ scooter.longitude }}, {{ scooter.latitude }})</span>
              </div>
              <div class="status-tag" :class="getStatusClass(scooter.status)">
                {{ getStatusText(scooter.status) }}
              </div>
            </div>
            <!-- 按钮 -->
            <button class="status-btn" @click="changeScooterStatus(scooter.id)">
              <span>Change Status</span>
            </button>
          </div>
        </div>
      </div>

      <!-- 添加到桌面 -->
      <div v-if="activeMenu === 4">
        <h2 class="title">Add to Desktop</h2>
      </div>

      <!-- 登出 -->
      <div v-if="activeMenu === 5">
        <h2 class="title">Logging Out...</h2>
      </div>
    </div>
  </div>
</template>


<script>
import axios from 'axios';

export default {
  data() {
    return {
      menus: [
        { id: 1, name: 'Scooter Management', icon: 'path/to/icon1.png' },
        { id: 2, name: 'User Feedback', icon: 'path/to/icon2.png' },
        { id: 3, name: 'Show all scooters', icon: 'path/to/icon3.png' },
        { id: 4, name: 'Add to Desktop', icon: 'path/to/icon4.png' },
        { id: 5, name: 'Logout', icon: 'path/to/icon5.png' }
      ],
      activeMenu: 1, // 默认选中第一个菜单项
      scooters: [],
      newScooter: {
        location: '',
        priceHour: '',
        priceDay: ''
      },
      feedbacks: []
    };
  },
  mounted() {
    // 检查 Token 是否存在
    const token = localStorage.getItem('token');
    console.log('Token 是否存在:', token !== null);
    if (!token) {
      this.$router.push('/login');
    } else {
      // 设置 axios 默认的 Authorization 头
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      this.fetchScooters();
    }
  },
  methods: {
    // 获取滑板车列表（已通过拦截器附加 Token）
    fetchScooters() {
      axios.get('http://localhost:8080/api/scooters/getAll')
          .then((res) => {
            this.scooters = res.data;
          })
          .catch((err) => {
            if (err.response?.status === 401) {
              alert('请重新登录');
              this.$router.push('/login');
            } else {
              alert('Failed to load scooters');
            }
          });
    },

    // 添加滑板车（已通过拦截器附加 Token）
    addScooter() {
      if (!this.newScooter.location || !this.newScooter.priceHour || !this.newScooter.priceDay) {
        alert('Please enter required fields');
        return;
      }
      axios.post('http://localhost:8080/api/scooters/add', this.newScooter)
          .then((res) => {
            if (res.status === 200) {
              alert('Scooter Added');
              this.newScooter = { location: '', priceHour: '', priceDay: '' }; // 重置表单
              this.fetchScooters();
            }
          })
          .catch((err) => {
            if (err.response?.status === 401) {
              this.$router.push('/login');
            } else {
              alert('Failed to add scooter');
            }
          });
    },

    // 修改滑板车状态（已通过拦截器附加 Token）
    async changeScooterStatus(id) {
      try {
        const res = await axios.get(`http://localhost:8080/api/scooters/changeStatus/${id}`);
        if (res.status === 200) {
          this.fetchScooters();
          alert('状态更新成功');
        }
      } catch (err) {
        if (err.response?.status === 401) {
          this.$router.push('/login');
        } else {
          alert('网络错误');
        }
      }
    },

    // 获取用户反馈数据
    fetchFeedbacks() {
      axios.get('http://localhost:8080/api/feedback/all')
          .then((res) => {
            if (res.status === 200) {
              this.feedbacks = res.data;
            } else {
              alert('Failed to load feedbacks');
            }
          })
          .catch(() => {
            alert('Failed to load feedbacks');
          });
    },

    getStatusClass(status) {
      const statusMap = {
        0: 'unavailable',
        1: 'available',
        2: 'maintenance',
      };
      return statusMap[status] || 'unknown';
    },

    getStatusText(status) {
      const textMap = {
        0: 'Unavailable',
        1: 'Available',
        2: 'Maintenance',
      };
      return textMap[status] || 'Unknown';
    },
  },
};
</script>

<style scoped lang="scss">
@use "ScooterAdd";

.page {
  display: flex;
  min-height: 100vh;
  background: #f5f5f5;
}

.sidebar {
  width: 250px;
  background: #58c4c9;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
  color: #fff;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
}

.content {
  flex: 1;
  padding: 20px;
  background: #fff;
  border-radius: 10px;
  margin: 20px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}

.form {
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;

  .input {
    margin: 5px 0;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
  }

  .button {
    background: #58c4c9;
    color: white;
    padding: 10px;
    text-align: center;
    border-radius: 5px;
    cursor: pointer;
    margin-top: 10px;
  }
}

/* 标题 */
.title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 15px;
}

/* 滑板车列表 */
.scooter-list {
  margin-top: 90rpx;
  display: flex;
  flex-direction: column;
  gap: 16px;
  width: 100%;
  max-width: 800px;
}

/* 滑板车卡片 */
.scooter-item {
  display: flex;
  flex-direction: column;
  background-color: white;
  padding: 16px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease-in-out;
}

.scooter-item:hover {
  transform: translateY(-3px);
}

/* 统一信息行样式 */
.info-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6px;
}

.label {
  font-weight: bold;
  color: #444;
}

.value {
  color: #666;
}

/* 状态标签 */
.status-tag {
  padding: 6px 12px;
  border-radius: 5px;
  font-size: 14px;
  font-weight: bold;
  text-align: center;
  display: inline-block;
  margin-top: 8px;
  width: fit-content;
}

/* 状态颜色 */
.available {
  background: #e8f5e9;
  color: #2e7d32;
}

.unavailable {
  background: #ffebee;
  color: #c62828;
}

.maintenance {
  background: #fff3e0;
  color: #ff9800;
}

/* 按钮样式 */
.status-btn {
  margin-top: 12px;
  background-color: #58c4c9;
  color: white;
  padding: 8px 14px;
  border-radius: 5px;
  text-align: center;
  transition: background 0.2s ease-in-out;
}

.status-btn:hover {
  background-color: #46a3a8;
}
</style>