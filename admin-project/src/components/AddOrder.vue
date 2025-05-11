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
              min="1"
              placeholder="Enter user ID"
              class="input"
          />
          <el-alert
              v-if="errors.userId"
              :title="errors.userId"
              type="error"
              :closable="false"
              show-icon
              class="custom-alert"
          />
        </div>
      </div>

      <!-- Scooter ID row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Scooter ID</label>
          <input
              v-model="bookingData.scooterId"
              type="number"
              min="1"
              placeholder="Enter scooter ID"
              class="input"
          />
          <el-alert
              v-if="errors.scooterId"
              :title="errors.scooterId"
              type="error"
              :closable="false"
              show-icon
              class="custom-alert"
          />
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
          <el-alert
              v-if="errors.hireType"
              :title="errors.hireType"
              type="error"
              :closable="false"
              show-icon
              class="custom-alert"
          />
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
          <el-alert
              v-if="errors.startTime"
              :title="errors.startTime"
              type="error"
              :closable="false"
              show-icon
              class="custom-alert"
          />
        </div>
      </div>

      <!-- Error message -->
      <div class="form-row" v-if="hasError && errorMessage">
        <el-alert
            :title="errorMessage"
            type="error"
            :closable="false"
            show-icon
        />
      </div>

      <!-- Buttons row -->
      <div class="form-row">
        <div class="button-group">
          <button class="button button-reset" @click="resetForm">Reset</button>
          <button class="button button-submit" @click="createBooking">Create Order</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import {ElNotification} from "element-plus";

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
      hasError: false,
      errorMessage: '',
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
      this.errors = {};
      this.hasError = false;
      this.errorMessage = '';
      this.bookingResult = null;
    },
    validateForm() {
      this.errors = {};
      this.hasError = false;
      this.errorMessage = '';

      if (!this.bookingData.userId) {
        this.errors.userId = 'User ID is required';
      } else if (this.bookingData.userId <= 0) {
        this.errors.userId = 'User ID must be greater than 0';
      }

      if (!this.bookingData.scooterId) {
        this.errors.scooterId = 'Scooter ID is required';
      } else if (this.bookingData.scooterId <= 0) {
        this.errors.scooterId = 'Scooter ID must be greater than 0';
      }

      if (!this.bookingData.hireType) {
        this.errors.hireType = 'Hire type is required';
      }

      if (!this.bookingData.startTime) {
        this.errors.startTime = 'Start time is required';
      }

      this.hasError = Object.keys(this.errors).length > 0;
      if (this.hasError) {
        ElNotification({
          title: "Input Error",
          message: `Please fix the errors above`,
          type: "error"
        });
      }

      return !this.hasError;
    },
    async createBooking() {
      if (!this.validateForm()) {
        return;
      }

      try {
        // 获取 token 和 admin
        const token = localStorage.getItem('token') || sessionStorage.getItem('token');
        const admin = localStorage.getItem('admin');

        if (!token) {
          this.errorMessage = 'Please login first';
          this.$router.push('/login');
          return;
        }

        if (!admin) {
          this.errorMessage = 'User information not available. Please login again.';
          this.hasError = true;
          return;
        }

        // 格式化时间
        const date = new Date(this.bookingData.startTime);
        const formattedStartTime = date.toLocaleString('zh-CN', { timeZone: 'Asia/Shanghai' }).replace(/\//g, '-').replace(',', '') + ":00";

        // 构造请求体
        const payload = {
          userId: this.bookingData.userId,
          scooterId: this.bookingData.scooterId,
          hireType: this.bookingData.hireType,
          startTime: formattedStartTime,
          staffId: admin
        };

        // 创建订单
        const res = await axios.post('http://localhost:8080/api/bookings/forUnregistered', payload, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (res.status === 200) {
          const orderId = res.data.orderId;
          const userId = this.bookingData.userId;
          this.resetForm();

          console.log('userid',userId);
          // 跳转到支付页面
          this.$router.push({ name: 'ChoosePayment', params: { orderId, userId} });
        }

      } catch (error) {
        if (error.response) {
          switch (error.response.status) {
            case 400:
              this.errors = error.response.data || {};
              if (error.response.data.message) {
                this.errorMessage = 'Error: ' + error.response.data.message;
              } else {
                this.errorMessage = this.errors;
              }
              break;
            case 401:
              localStorage.removeItem('token');
              sessionStorage.removeItem('token');
              this.errorMessage = 'Session expired. Please login again.';
              this.$router.push('/login');
              break;
            case 403:
              this.errorMessage = 'Forbidden: You do not have permission to perform this action';
              break;
            default:
              this.errorMessage = `Error: ${error.response.data?.message || 'Unknown error occurred'}`;
          }
        } else {
          this.errorMessage = 'Network error: Please check your connection';
        }
        this.hasError = true;
        console.error('Booking error:', error);
      }
    }
  },
  mounted() {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
      this.errorMessage = 'Please login first';
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
  border-bottom: 2px solid #003c51;
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
  border-color: #003c51;
  box-shadow: 0 0 1px #003c51;
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
  background: #003c51;
  color: white;
}

.button-submit:hover {
  background: #003c51;
}

.button-reset {
  background: white;
  color: #003c51;
  font-weight: bold;
  border: 3px solid #003c51;
}

.button-reset:hover {
  background: #f4f4f4;
}

.custom-alert {
  font-size: 16px;
  width: 99%;
  margin-top: 3px;
  height: 30px;
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