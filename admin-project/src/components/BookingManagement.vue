<template>
  <div>
    <h2 class="title">All Bookings</h2>
    <div class="filter-container">
      <div class="status-filter">
        <label for="status-select">Filter by Status:</label>
        <select id="status-select" v-model="statusFilter" @change="filterBookings">
          <option value="1">Active</option>
          <option value="2">Completed</option>
          <option value="3">Cancelled</option>
        </select>
      </div>
      <div class="search-box">
        <input
            type="text"
            v-model="searchQuery"
            placeholder="Search bookings..."
            @keyup.enter="handleSearch"
        >
        <button class="reset-button" @click="resetSearch">
          <img src="/static/center/reset.png" alt="Reset" class="reset-icon">
        </button>
        <button class="search-button" @click="handleSearch">
          <img src="/static/center/search.svg" alt="Search" class="search-icon">
        </button>
      </div>
    </div>

    <div v-if="bookings.length === 0" style="color: red; padding: 10px;">
      <p>Debug Info:</p>
      <p>isAdmin: {{ isAdmin }}</p>
      <p>Token: {{ $store.getters.token ? 'Exists' : 'Missing' }}</p>
      <p>Bookings count: {{ bookings.length }}</p>
      <p>Error: {{ error || 'None' }}</p>
    </div>
    <div class="table-container">
      <table class="booking-table">
        <thead>
        <tr>
          <th>Order ID</th>
          <th>Order Time</th>
          <th>Status</th>
          <th>Hire Period</th>
          <th>Start Time</th>
          <th>End Time</th>
          <th>Price</th>
          <th>User Info</th>
          <th>Scooter Info</th>
          <th v-if="showActionsColumn">Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="booking in filteredPaginatedBookings" :key="booking.id">
          <td>{{ booking.id }}</td>
          <td style="white-space: pre-line">{{ formatDate(booking.orderTime) }}</td>
          <td>
              <span :class="['status-label', getStatusClass(booking.status)]">
                {{ getStatusLabel(booking.status) }}
              </span>
          </td>
          <td>{{ booking.hirePeriod }}</td>
          <td style="white-space: pre-line">{{ formatDate(booking.startTime) }}</td>
          <td style="white-space: pre-line">{{ formatDate(booking.endTime) }}</td>
          <td>£{{ booking.price.toFixed(2) }}</td>
          <td>
            <div><strong>Username:</strong> {{ booking.user.username }}</div>
            <div><strong>Email:</strong> {{ booking.user.email }}</div>
          </td>
          <td>
            <div><strong>Location:</strong> {{ booking.scooter.location }}</div>
            <div><strong>Price/Hour:</strong> £{{ booking.scooter.priceHour.toFixed(2) }}</div>
          </td>
          <td v-if="booking.status === 1">
            <button class="delete-btn" @click="cancelBooking(booking.id)">
              <img src="/static/center/cancel.webp" alt="Delete" style="width: 30px; height: 30px;">
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="pagination-container">
      <span class="pagination-info">Total {{ filteredBookings.length }} Bookings</span>

      <div class="pagination-buttons">
        <button
            class="pagination-prev"
            :disabled="currentPage === 1"
            @click="goToPage(currentPage - 1)"
        >
          &lt;
        </button>

        <template v-for="page in displayedPages" :key="page">
          <button
              v-if="page === 'ellipsis'"
              class="pagination-ellipsis"
              disabled
          >
            ⋯
          </button>
          <button
              v-else
              class="pagination-page"
              :class="{ 'pagination-active': page === currentPage }"
              @click="goToPage(page)"
          >
            {{ page }}
          </button>
        </template>

        <button
            class="pagination-next"
            :disabled="currentPage === totalPages"
            @click="goToPage(currentPage + 1)"
        >
          &gt;
        </button>
      </div>

      <div class="pagination-jump">
        <input
            type="number"
            v-model.number="inputPage"
            min="1"
            :max="totalPages"
            @keyup.enter="goToPage(inputPage)"
        />
        <span>Page</span>
        <button @click="goToPage(inputPage)">Go</button>
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
      filteredBookings: [],
      currentPage: 1,
      itemsPerPage: 10,
      inputPage: 1,
      searchQuery: '',
      statusFilter: '1',
      showActionsColumn: true,
      error: null
    };
  },

  computed: {
    isAdmin() {
      return this.$store.getters.isAdmin;
    },
    totalPages() {
      return Math.ceil(this.filteredBookings.length / this.itemsPerPage);
    },
    displayedPages() {
      const pages = [];
      const total = this.totalPages;
      const current = this.currentPage;

      pages.push(1);
      const start = Math.max(2, current - 2);
      const end = Math.min(total - 1, current + 2);

      if (start > 2) pages.push('ellipsis');
      for (let i = start; i <= end; i++) pages.push(i);
      if (end < total - 1) pages.push('ellipsis');
      if (total > 1) pages.push(total);

      return pages;
    },
    filteredPaginatedBookings() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredBookings.slice(start, end);
    }
  },

  mounted() {
    console.log("Current token:", this.$store.getters.token);
    console.log("Current user:", this.$store.getters.user);
    this.fetchBookings();
  },

  methods: {
    resetSearch() {
      this.searchQuery = '';
      this.statusFilter = '1';
      this.filterBookings();
    },

    formatDate(dateString) {
      console.log(dateString);
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      return `${year}-${month}-${day}\n${hours}:${minutes}`;
    },
    async fetchBookings() {
      if (!this.isAdmin) {
        this.error = 'Only Administrator can get all bookings';
        alert(this.error);
        return;
      }

      try {
        const token = this.$store.getters.token;
        console.log("Token is :", token); // Log the token for debugging
        if (!token) {
          this.error = 'Not authenticated';
          alert(this.error);
          this.$router.push('/add_scooter'); // Redirect to login page
          return;
        }

        const config = {
          headers: { Authorization: `Bearer ${token}` }
        };

        console.log("Sending request to /api/bookings/getAll..."); // Request start
        const res = await axios.get('http://localhost:8080/api/bookings/getAll', config);
        console.log("API response:", res); // Full response object

        // Print key data
        console.log("Response data:", res.data); // Actual returned data
        if (Array.isArray(res.data)) {
          this.bookings = res.data;
        } else {
          console.warn('Unexpected data format:', res.data); // Format warning
          this.bookings = [];
        }
        this.filteredBookings = [...this.bookings];
        this.error = null;

        // Print processed datastatus-filter
        console.log("Processed bookings:", this.bookings);

        // Default disable filter
        this.statusFilter = '1';
        this.searchQuery = '';
        this.filterBookings();
      } catch (error) {
        // More comprehensive error handling
        if (error.response) {
          // Server returned an error response
          console.error('Server responded with an error:', error.response);
          this.error = error.response.data?.message || `Error: ${error.response.status}`;
        } else if (error.request) {
          // Request sent but no response received
          console.error('No response received:', error.request);
          this.error = 'No response received from server';
        } else {
          // Other errors during request creation
          console.error('Error creating request:', error.message);
          this.error = `Request error: ${error.message}`;
        }
        alert(this.error); // Notify user of error
      }
    },

    handleSearch() {
      this.filterBookings();
    },

    filterBookings() {
      const query = this.searchQuery.toLowerCase().trim();
      const statusFilter = this.statusFilter;

      this.filteredBookings = this.bookings.filter(booking => {
        if (statusFilter !== 'all' && booking.status.toString() !== statusFilter) {
          return false;
        }

        if (!query) return true;

        return (
            booking.id.toString().includes(query) ||
            this.formatDateForSearch(booking.orderTime).toLowerCase().includes(query) ||
            booking.hirePeriod.toLowerCase().includes(query) ||
            this.formatDateForSearch(booking.startTime).toLowerCase().includes(query) ||
            this.formatDateForSearch(booking.endTime).toLowerCase().includes(query) ||
            booking.price.toString().includes(query) ||
            booking.user.username.toLowerCase().includes(query) ||
            booking.user.email.toLowerCase().includes(query) ||
            booking.scooter.location.toLowerCase().includes(query) ||
            booking.scooter.priceHour.toString().includes(query)
        );
      });

      this.resetPagination();
    },

    formatDateForSearch(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}-${String(date.getDate()).padStart(2,'0')} ${String(date.getHours()).padStart(2,'0')}:${String(date.getMinutes()).padStart(2,'0')}`;
    },

    resetPagination() {
      this.currentPage = 1;
      this.inputPage = 1;
      this.updateActionsColumnVisibility();
    },

    updateActionsColumnVisibility() {
      this.showActionsColumn = this.filteredBookings.some(booking => booking.status === 1);
    },

    goToPage(page) {
      page = parseInt(page);
      if (page < 1) page = 1;
      if (page > this.totalPages) page = this.totalPages;

      this.currentPage = page;
      this.inputPage = page;
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },

    async cancelBooking(orderId) {
      if (!confirm('Are you sure you want to cancel this booking?')) return;
      try {
        const token = this.$store.getters.token;
        const res = await axios.post(
            `http://localhost:8080/api/bookings/cancel/${orderId}`,
            {},
            {
              headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
              }
            }
        );
        if (res.status === 200) {
          alert('Booking cancelled successfully');
          this.fetchBookings();
        }
      } catch (error) {
        console.error('Error cancelling booking:', error.response);
        alert(`Failed to cancel booking: ${error.response?.data?.message || error.message}`);
      }
    },

    getStatusLabel(status) {
      const statusMap = { 1: 'Active', 2: 'Completed', 3: 'Cancelled' };
      return statusMap[status] || 'Unknown';
    },

    getStatusClass(status) {
      const statusClassMap = {
        1: 'status-active',
        2: 'status-completed',
        3: 'status-cancelled'
      };
      return statusClassMap[status] || 'status-unknown';
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
/* Header and Filter Container Styles */
.filter-container {
  display: flex;
  gap: 20px;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  margin-top: 20px;
  padding-left: 20px;
  padding-right: 20px;
}

/* Search Box Styles */
.search-box {
  position: relative;
  display: flex;
  align-items: center;
  border: 2px solid #ddd;
  border-radius: 20px;
  overflow: hidden;
  background: white;
}

.search-box input {
  padding: 10px 15px;
  border: none;
  font-size: 16px;
  width: 250px;
  outline: none;
}

.search-button {
  background: white;
  border: none;
  height: 100%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s;
  padding-left: 5px;
  padding-right: 10px;
}

.reset-button {
  background: white;
  border: none;
  padding: 0 10px;
  height: 100%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s;
  border-left: 2px solid #ddd;
  border-right: 2px solid #ddd;
  color: #666;
}

.search-button:hover, .reset-button:hover {
  background: #e0e0e0;
}

.search-icon {
  width: 30px;
  height: 30px;
}

.reset-icon{
  width: 20px;
  height: 20px;
}

/* Status Filter Styles */
.status-filter {
  display: flex;
  align-items: center;
  gap: 10px;
}

.status-filter label {
  font-weight: 500;
  color: #444;
}

.status-filter select {
  padding: 8px 12px;
  border: 2px solid #ddd;
  border-radius: 4px;
  background-color: white;
  cursor: pointer;
}

.status-filter select:focus {
  outline: none;
  border-color: #58c4c9;
}

/* Table Styles */
.table-container {
  margin-top: 20px;
  overflow-x: auto;
  border: 1px solid #e0e0e0; /* Added border */
  border-radius: 8px; /* Added border radius */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); /* Added shadow */
}

.booking-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
}

.booking-table th,
.booking-table td {
  padding: 18px 15px;
  text-align: center;
  vertical-align: middle;
  border-bottom: 1px solid #e0e0e0;
  height: 60px;
}

.booking-table th {
  background-color: #f5f5f5;
  font-weight: bold;
  color: #444;
  border-top: 1px solid #e0e0e0; /* Added top border */
}

.booking-table tr:hover {
  background-color: #f9f9f9;
}

.booking-table td {
  color: #666;
}

/* Status Label Styles */
.status-label {
  display: inline-block;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 14px;
  font-weight: bold;
  text-transform: uppercase;
}

.status-active {
  background-color: #67c23a;
  color: white;
}

.status-completed {
  background-color: #409eff;
  color: white;
}

.status-cancelled {
  background-color: #f56c6c;
  color: white;
}

/* Delete Button Styles */
.delete-btn {
  background-color: transparent;
  border: none;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}

.delete-btn img {
  width: 20px;
  height: 20px;
}

.delete-btn:hover img {
  filter: brightness(0.8);
}

/* Pagination Styles */
.pagination-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 30px;
  font-size: 14px;
  flex-wrap: wrap;
  gap: 15px;
}

.pagination-info {
  margin-right: 15px;
  color: #666;
}

.pagination-buttons {
  display: flex;
  align-items: center;
  gap: 5px;
}

.pagination-prev,
.pagination-next,
.pagination-page {
  min-width: 32px;
  height: 32px;
  padding: 0 8px;
  border: 1px solid #d9d9d9;
  background-color: #fff;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s;
  color: rgba(0, 0, 0, 0.65);
}

.pagination-prev:hover:not(:disabled),
.pagination-next:hover:not(:disabled),
.pagination-page:hover:not(.pagination-active) {
  color: #58c4c9;
  border-color: #58c4c9;
}

.pagination-prev:disabled,
.pagination-next:disabled {
  color: rgba(0, 0, 0, 0.25);
  background-color: #f5f5f5;
  border-color: #d9d9d9;
  cursor: not-allowed;
}

.pagination-active {
  background-color: #58c4c9;
  color: #fff !important;
  border-color: #58c4c9 !important;
  font-weight: 500;
}

.pagination-ellipsis {
  min-width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: rgba(0, 0, 0, 0.25);
}

.pagination-jump {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-left: 8px;
}

.pagination-jump input {
  width: 50px;
  height: 32px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  text-align: center;
  padding: 0 5px;
}

.pagination-jump input:focus {
  outline: none;
  border-color: #58c4c9;
}

.pagination-jump button {
  height: 32px;
  padding: 0 15px;
  background-color: #f5f5f5;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.pagination-jump button:hover {
  background-color: #e6e6e6;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .pagination-container {
    flex-direction: column;
    gap: 10px;
  }

  .pagination-info {
    margin-right: 0;
    margin-bottom: 5px;
  }

  .pagination-jump {
    margin-left: 0;
    margin-top: 10px;
  }
}
</style>