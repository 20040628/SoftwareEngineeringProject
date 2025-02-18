<template>
  <div class="scooter-list">
    <h2>Available Scooters</h2>
    <div v-if="scooters.length > 0" class="scooter-grid">
      <div v-for="scooter in scooters" :key="scooter.id" class="scooter-card">
        <h3>Location: {{ scooter.location }}</h3>
        <div class="price-info">
          <p>Hourly Rate: ${{ scooter.priceHour }}</p>
          <p>4-Hour Rate: ${{ scooter.priceFourHour }}</p>
          <p>Daily Rate: ${{ scooter.priceDay }}</p>
          <p>Weekly Rate: ${{ scooter.priceWeek }}</p>
        </div>
        <button @click="showBookingModal(scooter)" :disabled="!currentUser">
          {{ currentUser ? 'Book Now' : 'Login to Book' }}
        </button>
      </div>
    </div>
    <div v-else>
      <p>No scooters available.</p>
    </div>

    <!-- Booking Modal -->
    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <h3>Book Scooter</h3>
        
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
            <label>Hire Period:</label>
            <select v-model="bookingForm.hireType">
              <option value="HOUR">1 Hour (${{ selectedScooter?.priceHour }})</option>
              <option value="FOUR_HOURS">4 Hours (${{ selectedScooter?.priceFourHour }})</option>
              <option value="DAY">1 Day (${{ selectedScooter?.priceDay }})</option>
              <option value="WEEK">1 Week (${{ selectedScooter?.priceWeek }})</option>
            </select>
          </div>
          
          <div class="form-group">
            <label>Start Time:</label>
            <input type="datetime-local" v-model="bookingForm.startTime">
          </div>

          <div class="modal-buttons">
            <button @click="submitBooking" :disabled="loading">
              {{ loading ? 'Processing...' : 'Confirm Booking' }}
            </button>
            <button @click="closeModal" class="cancel-button">Cancel</button>
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

export default {
  name: 'ScooterAll',
  props: {
    currentUser: {
      type: Object,
      default: null
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
        const response = await axios.get('http://localhost:8080/api/scooters/getAll');
        this.scooters = response.data;
      } catch (error) {
        console.error('Error fetching scooters:', error);
      }
    },
    
    async showBookingModal(scooter) {
      this.selectedScooter = scooter;
      this.showModal = true;
      
      // 获取时间轴数据
      try {
        const response = await axios.get(`http://localhost:8080/api/bookings/timeline/${scooter.id}`);
        this.timeline = response.data;
      } catch (error) {
        console.error('Error fetching timeline:', error);
      }
      
      // Set default start time to current time
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
</style>