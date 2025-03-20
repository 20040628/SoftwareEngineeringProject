<template>
<div class="scooter-list">
    <h2>Orders</h2>
    <div v-if="orders.length > 0" class="scooter-grid">
      <div v-for="order in orders" :key="orders.id" class="scooter-card">
        <h3>id: {{ order.id }}</h3>
        <div class="price-info">
          <p>order_time:{{ order.orderTime }}</p>
          <p>price:{{ order.price }}</p>
          <p>status:{{ order.status }}</p>
          <p>user_id:{{ order.user.id }}</p>
          <p>scooter_id:{{ order.scooter.id }}</p>
        </div>
        <button @click="handlePay(order.id)" :disabled="!isAuthenticated">
          {{ isAuthenticated ? 'Pay Now' : 'Login to Pay' }}
        </button>
      </div>
    </div>
    <div v-else>
      <p>No orders available.</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { computed } from 'vue';
import { useStore } from 'vuex';

export default {
  name: 'OrderAll',
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
      orders: [],
      showModal: false,
      loading: false,
      message: '',
      messageType: 'success',
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
        const response = await axios.get('http://localhost:8080/api/bookings/getAll');
        this.orders = response.data;
      } catch (error) {
        console.error('Error fetching orders:', error);
      }
    },
    async handlePay(orderId) {
      try {
        const response = await axios.get(`http://localhost:8080/alipay/pay/${orderId}`);
        const newWindow = window.open();
        newWindow.document.write(response.data);
      } catch (error) {
        console.error("Error processing payment:", error);
      }
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