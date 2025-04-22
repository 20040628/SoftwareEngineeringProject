<template>
  <div>
    <h2 class="title">Create Booking for Unregistered User</h2>
    <div class="form">
      <!-- User ID row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>User ID</label>
          <input
              v-model="bookingData.userId"
              type="number"
              placeholder="Enter user ID"
              class="input"
          />
          <span class="error-message" v-if="errors.userId">{{ errors.userId }}</span>
        </div>
      </div>

      <!-- Scooter ID row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Scooter ID</label>
          <input
              v-model="bookingData.scooterId"
              type="number"
              placeholder="Enter scooter ID"
              class="input"
          />
          <span class="error-message" v-if="errors.scooterId">{{ errors.scooterId }}</span>
        </div>
      </div>

      <!-- Hire Type row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Hire Type</label>
          <select v-model="bookingData.hireType" class="input">
            <option value="HOUR">Hour</option>
            <option value="FOUR_HOURS">4 Hours</option>
            <option value="DAY">Day</option>
            <option value="WEEK">Week</option>
          </select>
          <span class="error-message" v-if="errors.hireType">{{ errors.hireType }}</span>
        </div>
      </div>

      <!-- Start Time row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Start Time</label>
          <input
              v-model="bookingData.startTime"
              type="datetime-local"
              class="input"
          />
          <span class="error-message" v-if="errors.startTime">{{ errors.startTime }}</span>
        </div>
      </div>

      <!-- Buttons row -->
      <div class="form-row">
        <div class="button-group">
          <button class="button button-reset" @click="resetForm">Reset</button>
          <button class="button button-submit" @click="createBooking">Create Order</button>
        </div>
      </div>

      <!-- Success message -->
<!--      <div class="success-message" v-if="successMessage">-->
<!--        {{ successMessage }}-->
<!--        <div v-if="bookingResult" class="booking-details">-->
<!--          <p>Order ID: {{ bookingResult.orderId }}</p>-->
<!--          <p>Original Price: ${{ bookingResult.priceBeforeDiscount.toFixed(2) }}</p>-->
<!--          <p>Final Price: ${{ bookingResult.price.toFixed(2) }}</p>-->
<!--          <p>Start Time: {{ formatDateTime(bookingResult.startTime) }}</p>-->
<!--          <p>End Time: {{ formatDateTime(bookingResult.endTime) }}</p>-->
<!--        </div>-->
<!--      </div>-->


    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      bookingData: {
        userId: '',
        scooterId: '',
        hireType: 'HOUR',
        startTime: '',
      },
      errors: {},
      successMessage: '',
      bookingResult: null
    };
  },
  methods: {
    resetForm() {
      this.bookingData = {
        userId: '',
        scooterId: '',
        hireType: 'HOUR',
        startTime: '',
      };
    },
    formatDateTime(dateTimeString) {
      if (!dateTimeString) return '';
      const date = new Date(dateTimeString);
      return date.toLocaleString();
    },
    async createBooking() {
      // 重置错误和消息
      this.errors = {};
      this.successMessage = '';
      this.bookingResult = null;

      // 基本验证
      if (!this.bookingData.userId) {
        this.errors.userId = 'User ID is required';
      }
      if (!this.bookingData.scooterId) {
        this.errors.scooterId = 'Scooter ID is required';
      }
      if (!this.bookingData.hireType) {
        this.errors.hireType = 'Hire type is required';
      }
      if (!this.bookingData.startTime) {
        this.errors.startTime = 'Start time is required';
      }

      if (Object.keys(this.errors).length > 0) {
        return;
      }

      try {
        // 从localStorage或sessionStorage获取token
        const token = localStorage.getItem('token') || sessionStorage.getItem('token');
        const admin = localStorage.getItem('admin');
        if (!token) {
          alert('Please login first');
          this.$router.push('/login'); // 重定向到登录页
          return;
        }

        if (!admin) {
          alert('User information not available. Please login again.');
          return;
        }

        // 格式化开始时间
        // const formattedStartTime = new Date(this.bookingData.startTime).replace('T', ' ')+":00"
        const date = new Date(this.bookingData.startTime);
        const formattedStartTime = date.toLocaleString('zh-CN', { timeZone: 'Asia/Shanghai' }).replace(/\//g, '-').replace(',', '') + ":00";


        // 准备请求数据
        const payload = {
          userId: this.bookingData.userId,
          scooterId: this.bookingData.scooterId,
          hireType: this.bookingData.hireType,
          startTime: formattedStartTime,
          staffId: admin
        };

        // 发送请求
        const res = await axios.post('http://localhost:8080/api/bookings/forUnregistered', payload, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (res.status === 200) {
          this.successMessage = 'Booking created successfully';
          this.bookingResult = res.data;
          // 重置表单
          this.bookingData = {
            userId: '',
            scooterId: '',
            hireType: 'HOUR',
            startTime: ''
          };
          alert("Add Order successfully");
        }
      } catch (error) {
        if (error.response) {
          // 处理不同的错误状态码
          switch(error.response.status) {
            case 400:
              this.errors = error.response.data || {};
              console.log("bookingdata is", this.bookingData);
              console.error('Error response:', error.response.data);
              alert('Validation error: Please check your input');
              break;
            case 401:
              // token可能已过期，清除并重定向
              localStorage.removeItem('token');
              sessionStorage.removeItem('token');
              this.$store.commit('logout');
              alert('Session expired. Please login again.');
              this.$router.push('/login');
              break;
            case 403:
              alert('Forbidden: You do not have permission to perform this action');
              break;
            default:
              alert(`Error: ${error.response.data?.message || 'Unknown error occurred'}`);
          }
        } else {
          alert('Network error: Please check your connection');
        }
        console.error('Booking error:', error);
      }
    }
  },
  mounted() {
    // 组件加载时检查登录状态
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
      alert('Please login first');
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
.title {
  font-size: 28px;
  font-weight: bold;
  padding-left: 20px;
  padding-bottom: 20px;
  padding-top: 20px;
  border-bottom: 2px solid #58c4c9;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 20px 40px;
  border-radius: 12px;
  max-width: 100%;
}

.form-row {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  width: 100%;
}

.form-group {
  display: flex;
  flex-direction: column;
  flex: 1;
  margin-bottom: 10px;
}

label {
  margin-left: 5px;
  font-size: 22px;
  margin-bottom: 6px;
  color: #083435;
  font-weight: bold;
}

.input {
  height: 44px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 14px;
  margin-right: 1%;
}

.input:focus {
  outline: none;
  border-color: #58c4c9;
  box-shadow: 0 0 1px #58c4c9;
}

.button-group {
  display: flex;
  gap: 16px;
  margin-top: 10px;
}

.button {
  padding: 12px;
  border: none;
  font-size: 18px;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: bold;
  width: 150px;
}

.button-submit {
  background: #58c4c9;
  color: white;
}

.button-submit:hover {
  background: #3bb7bd;
}

.button-reset {
  background: white;
  color: #58c4c9;
  font-weight: bold;
  border: 3px solid #58c4c9;
}

.button-reset:hover {
  background: #f4f4f4;
}

.error-message {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 4px;
}

.success-message {
  color: #67c23a;
  font-weight: bold;
  margin-top: 16px;
  padding: 12px;
  border-radius: 8px;
  background-color: #f0f9eb;
}

.booking-details {
  margin-top: 10px;
  font-weight: normal;
  color: #333;
}

.booking-details p {
  margin: 5px 0;
}

@media (max-width: 767px) {
  .form-row {
    flex-direction: column;
  }

  .form-group {
    width: 100%;
  }
}

@media (max-width: 767px) {
  .button-group {
    flex-direction: column;
  }

  .button {
    width: 100%;
  }
}
</style>