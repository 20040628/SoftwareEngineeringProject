<template>
  <div>
    <h2 class="title">All Bookings</h2>
    <div class="booking-list">
      <div class="booking-item" v-for="booking in bookings" :key="booking.id">
        <div class="booking-info">
          <div class="info-row">
            <span class="label">Order ID:</span> <span class="value">{{ booking.id }}</span>
          </div>
          <div class="info-row">
            <span class="label">Order Time:</span> <span class="value">{{ booking.orderTime }}</span>
          </div>
          <div class="info-row">
            <span class="label">Status:</span> <span class="value">{{ getStatusLabel(booking.status) }}</span>
          </div>
          <div class="info-row">
            <span class="label">Hire Period:</span> <span class="value">{{ booking.hirePeriod }}</span>
          </div>
          <div class="info-row">
            <span class="label">Price:</span> <span class="value">${{ booking.price.toFixed(2) }}</span>
          </div>

          <details>
            <summary>User Info</summary>
            <div class="info-row">
              <span class="label">Username:</span> <span class="value">{{ booking.user.username }}</span>
            </div>
            <div class="info-row">
              <span class="label">Email:</span> <span class="value">{{ booking.user.email }}</span>
            </div>
          </details>

          <details>
            <summary>Scooter Info</summary>
            <div class="info-row">
              <span class="label">Location:</span> <span class="value">{{ booking.scooter.location }}</span>
            </div>
            <div class="info-row">
              <span class="label">Hourly Price:</span> <span class="value">${{ booking.scooter.priceHour.toFixed(2) }}</span>
            </div>
          </details>

          <button class="cancel-btn" @click="cancelBooking(booking.id)">Cancel Booking</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      bookings: [],
    };
  },
  mounted() {
    this.fetchBookings();
  },
  methods: {
    async fetchBookings() {
      try {
        const res = await axios.get('http://localhost:8080/api/bookings/getAll');
        if (res.status === 200) {
          this.bookings = res.data;
        } else {
          alert('Failed to load bookings');
        }
      } catch (error) {
        alert('Failed to load bookings');
      }
    },
    async cancelBooking(orderId) {
      if (!confirm('Are you sure you want to cancel this booking?')) return;
      try {
        const res = await axios.post(`http://localhost:8080/api/bookings/cancel/${orderId}`);
        if (res.status === 200) {
          alert('Booking cancelled successfully');
          this.fetchBookings();
        }
      } catch (error) {
        alert(error.response?.data?.message || 'Failed to cancel booking');
      }
    },
    getStatusLabel(status) {
      const statusMap = { 1: 'Active', 2: 'Completed', 3: 'Cancelled' };
      return statusMap[status] || 'Unknown';
    }
  }
};
</script>

<style scoped>
.booking-list {
  margin-top: 20px;
}

.booking-item {
  background-color: white;
  padding: 16px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 16px;
}

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

.cancel-btn {
  background-color: red;
  color: white;
  padding: 8px 12px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
}

.cancel-btn:hover {
  background-color: darkred;
}
</style>
