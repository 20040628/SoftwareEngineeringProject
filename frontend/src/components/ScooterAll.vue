<template>
  <div class="scooter-list">
    <h2>可用滑板车</h2>
    <div v-if="scooters.length > 0" class="scooter-grid">
      <div v-for="scooter in scooters" :key="scooter.id" class="scooter-card">
        <h3>位置: {{ scooter.location }}</h3>
        <div class="price-info">
          <template v-if="scooter.hasDiscount">
            <p>
              小时费率: <span class="original-price">${{ scooter.priceHour }}</span> 
              <span class="discount-price">${{ scooter.discountedPriceHour }}</span>
            </p>
            <p>
              4小时费率: <span class="original-price">${{ scooter.priceFourHour }}</span> 
              <span class="discount-price">${{ scooter.discountedPriceFourHour }}</span>
            </p>
            <p>
              日费率: <span class="original-price">${{ scooter.priceDay }}</span> 
              <span class="discount-price">${{ scooter.discountedPriceDay }}</span>
            </p>
            <p>
              周费率: <span class="original-price">${{ scooter.priceWeek }}</span> 
              <span class="discount-price">${{ scooter.discountedPriceWeek }}</span>
            </p>
            <p class="discount-badge">已应用折扣!</p>
            <p class="debug-info" v-if="isAuthenticated">折扣比率: {{ 
              ((1 - (scooter.discountedPriceHour / scooter.priceHour)) * 100).toFixed(0) 
            }}%</p>
          </template>
          <template v-else>
            <p>小时费率: ${{ scooter.priceHour }}</p>
            <p>4小时费率: ${{ scooter.priceFourHour }}</p>
            <p>日费率: ${{ scooter.priceDay }}</p>
            <p>周费率: ${{ scooter.priceWeek }}</p>
          </template>
        </div>
        <button @click="showBookingModal(scooter)" :disabled="!isAuthenticated">
          {{ isAuthenticated ? '立即预订' : '登录后预订' }}
        </button>
      </div>
    </div>
    <div v-else>
      <p>暂无可用滑板车。</p>
    </div>

    <!-- Booking Modal -->
    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <h3>预订滑板车</h3>
        
        <!-- 添加时间轴显示 -->
        <div class="timeline-container">
          <h4>预订时间轴</h4>
          <!-- 添加时间刻度 -->
          <div class="timeline-scale">
            <div v-for="day in 7" :key="day" class="timeline-day">
              {{ formatDate(addDays(new Date(), day-1)) }}
            </div>
          </div>
          <div class="timeline">
            <div v-for="(slot, index) in timeline" :key="index" 
                 :class="['time-slot', slot.status]"
                 :style="{ width: calculateSlotWidth(slot) + '%' }">
              <div class="slot-info">
                {{ formatTimeSlot(slot) }}
              </div>
            </div>
          </div>
        </div>

        <div class="booking-form">
          <div class="form-group">
            <label>租赁时长:</label>
            <select v-model="bookingForm.hireType">
              <option value="HOUR">
                1 小时 
                <template v-if="selectedScooter?.hasDiscount">
                  <span class="original-price">${{ selectedScooter?.priceHour }}</span> 
                  ${{ selectedScooter?.discountedPriceHour }}
                </template>
                <template v-else>
                  ${{ selectedScooter?.priceHour }}
                </template>
              </option>
              <option value="FOUR_HOURS">
                4 小时 
                <template v-if="selectedScooter?.hasDiscount">
                  <span class="original-price">${{ selectedScooter?.priceFourHour }}</span> 
                  ${{ selectedScooter?.discountedPriceFourHour }}
                </template>
                <template v-else>
                  ${{ selectedScooter?.priceFourHour }}
                </template>
              </option>
              <option value="DAY">
                1 天 
                <template v-if="selectedScooter?.hasDiscount">
                  <span class="original-price">${{ selectedScooter?.priceDay }}</span> 
                  ${{ selectedScooter?.discountedPriceDay }}
                </template>
                <template v-else>
                  ${{ selectedScooter?.priceDay }}
                </template>
              </option>
              <option value="WEEK">
                1 周 
                <template v-if="selectedScooter?.hasDiscount">
                  <span class="original-price">${{ selectedScooter?.priceWeek }}</span> 
                  ${{ selectedScooter?.discountedPriceWeek }}
                </template>
                <template v-else>
                  ${{ selectedScooter?.priceWeek }}
                </template>
              </option>
            </select>
          </div>
          
          <div class="form-group">
            <label>开始时间:</label>
            <input type="datetime-local" v-model="bookingForm.startTime">
          </div>

          <div class="modal-buttons">
            <button @click="submitBooking" :disabled="loading">
              {{ loading ? '处理中...' : '确认预订' }}
            </button>
            <button @click="closeModal" class="cancel-button">取消</button>
          </div>
        </div>

        <div v-if="message" :class="['message', messageType]">
          {{ message }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { computed } from 'vue';
import { useStore } from 'vuex';

export default {
  name: 'ScooterAll',
  props: {
    currentUser: {
      type: Object,
      default: null
    }
  },
  setup() {
    const store = useStore();
    
    // 从Vuex获取用户认证状态和用户信息
    const isAuthenticated = computed(() => store.getters.isAuthenticated);
    const currentUser = computed(() => store.getters.user);
    
    return {
      isAuthenticated,
      currentUser
    }
  },
  data() {
    return {
      scooters: [],
      showModal: false,
      selectedScooter: null,
      loading: false,
      message: '',
      messageType: 'success',
      bookingForm: {
        hireType: 'HOUR',
        startTime: ''
      },
      timeline: [],
      tooltipContent: '',
      tooltipStyle: {
        left: '0px',
        top: '0px'
      }
    };
  },
  mounted() {
    this.fetchScooters();
  },
  methods: {
    async fetchScooters() {
      try {
        // 检查是否已登录，如果登录则获取折扣价格
        if (this.isAuthenticated && this.currentUser?.userId) {
          const response = await axios.get(`http://localhost:8080/api/scooters/getAll?userId=${this.currentUser.userId}`);
          this.scooters = response.data;
        } else {
          const response = await axios.get('http://localhost:8080/api/scooters/getAll');
          this.scooters = response.data;
        }
      } catch (error) {
        console.error('获取滑板车列表失败:', error);
      }
    },
    
    async showBookingModal(scooter) {
      // 如果已有折扣信息则使用，否则尝试获取
      if (!scooter.hasDiscount && this.isAuthenticated && this.currentUser?.userId) {
        try {
          const response = await axios.get(`http://localhost:8080/api/scooters/${scooter.id}?userId=${this.currentUser.userId}`);
          this.selectedScooter = response.data;
        } catch (error) {
          console.error('获取滑板车折扣信息失败:', error);
          this.selectedScooter = scooter;
        }
      } else {
        this.selectedScooter = scooter;
      }
      
      this.showModal = true;
      
      // 获取时间轴数据
      try {
        const response = await axios.get(`http://localhost:8080/api/bookings/timeline/${scooter.id}`);
        this.timeline = response.data;
      } catch (error) {
        console.error('获取时间轴失败:', error);
      }
      
      // 设置默认开始时间为当前时间
      const now = new Date();
      now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
      this.bookingForm.startTime = now.toISOString().slice(0, 16);
    },
    
    closeModal() {
      this.showModal = false;
      this.selectedScooter = null;
      this.message = '';
      this.bookingForm = {
        hireType: 'HOUR',
        startTime: ''
      };
    },
    
    async submitBooking() {
      if (!this.bookingForm.startTime) {
        this.message = '请选择开始时间';
        this.messageType = 'error';
        return;
      }

      this.loading = true;
      this.message = '';
      
      try {
        // 直接使用用户选择的本地时间，格式化为 MySQL datetime 格式
        const startTime = this.bookingForm.startTime.replace('T', ' ') + ':00';
        
        const bookingData = {
          userId: this.currentUser.userId,
          scooterId: this.selectedScooter.id,
          hireType: this.bookingForm.hireType,
          startTime: startTime
        };
        
        const response = await axios.post('http://localhost:8080/api/bookings', bookingData);
        
        this.message = response.data.message;
        this.messageType = 'success';
        
        // 刷新时间轴数据
        await this.refreshTimeline();
        
        // 3秒后关闭模态框
        setTimeout(() => {
          this.closeModal();
        }, 3000);
      } catch (error) {
        this.message = error.response?.data || '预订失败';
        this.messageType = 'error';
      } finally {
        this.loading = false;
      }
    },
    
    // 添加刷新时间轴的方法
    async refreshTimeline() {
      if (this.selectedScooter) {
        try {
          const response = await axios.get(`http://localhost:8080/api/bookings/timeline/${this.selectedScooter.id}`);
          this.timeline = response.data;
        } catch (error) {
          console.error('刷新时间轴失败:', error);
        }
      }
    },
    
    calculateSlotWidth(slot) {
      const start = new Date(slot.startTime).getTime();
      const end = new Date(slot.endTime).getTime();
      const duration = end - start;
      
      // 计算时间段占总时间范围的百分比
      const totalDuration = 7 * 24 * 60 * 60 * 1000; // 7天的毫秒数
      return (duration / totalDuration) * 100;
    },
    
    formatTimeSlot(slot) {
      const start = new Date(slot.startTime);
      const end = new Date(slot.endTime);
      if (slot.status === 'booked') {
        let hirePeriodText;
        switch (slot.hirePeriod) {
          case 'HOUR':
            hirePeriodText = '1小时';
            break;
          case 'FOUR_HOURS':
            hirePeriodText = '4小时';
            break;
          case 'DAY':
            hirePeriodText = '1天';
            break;
          case 'WEEK':
            hirePeriodText = '1周';
            break;
          default:
            hirePeriodText = slot.hirePeriod;
        }
        return `${this.formatTime(start)} - ${this.formatTime(end)} (${hirePeriodText})`;
      }
      return '可预订';
    },

    formatTime(date) {
      return `${date.getMonth() + 1}/${date.getDate()} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
    },

    formatDate(date) {
      return `${date.getMonth() + 1}月${date.getDate()}日`;
    },

    addDays(date, days) {
      const result = new Date(date);
      result.setDate(result.getDate() + days);
      return result;
    }
  }
};
</script>

<style scoped>
.scooter-list {
  padding: 20px;
}

.scooter-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.scooter-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 15px;
  background: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.price-info {
  margin: 10px 0;
}

.price-info p {
  margin: 5px 0;
  color: #666;
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
  cursor: not-allowed;
}

.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
}

.booking-form {
  margin-top: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.form-group select,
.form-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.modal-buttons {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.cancel-button {
  background: #f44336;
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

.timeline-container {
  margin: 20px 0;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.timeline-scale {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
  padding: 0 5px;
}

.timeline-day {
  font-size: 12px;
  color: #666;
  flex: 1;
  text-align: center;
  border-right: 1px solid #ddd;
  padding: 2px 0;
}

.timeline {
  display: flex;
  height: 60px;
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden;
  margin: 5px 0;
}

.time-slot {
  position: relative;
  height: 100%;
  min-width: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  transition: all 0.3s ease;
  padding: 0 5px;
}

.time-slot.available {
  background: #E8F5E9;
  color: #2E7D32;
  border-right: 1px dashed #A5D6A7;
}

.time-slot.booked {
  background: #FFEBEE;
  color: #C62828;
  border-right: 1px solid #FFCDD2;
}

.slot-info {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: center;
  line-height: 1.2;
  width: 100%;
}

/* 新增样式 - 折扣价格 */
.original-price {
  text-decoration: line-through;
  color: #999;
  margin-right: 5px;
  font-size: 0.9em;
}

.discount-price {
  color: #e44d26;
  font-weight: bold;
}

.discount-badge {
  background-color: #e44d26;
  color: white;
  padding: 3px 8px;
  border-radius: 10px;
  font-size: 0.8em;
  display: inline-block;
  margin-top: 5px;
}

.debug-info {
  font-size: 0.8em;
  color: #666;
  margin-top: 5px;
}
</style>