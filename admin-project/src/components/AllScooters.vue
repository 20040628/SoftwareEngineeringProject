<template>
  <div>
    <h2 class="title">Scooter Management</h2>
    <div class="filter-container">
      <div class="store-filter">
        <label for="store-select">Filter by Store:</label>
        <select id="store-select" v-model="storeFilter" @change="filterScooters">
          <option value="all">All Stores</option>
          <option v-for="store in stores" :key="store.id" :value="store.id">
            {{ store.id }}: {{ store.name }} ({{ store.latitude }}, {{ store.longitude }})
          </option>
        </select>
      </div>

      <div class="status-filter">
        <label for="status-select">Filter by Status:</label>
        <select id="status-select" v-model="statusFilter" @change="filterScooters">
          <option value="all">All</option>
          <option value="1">Available</option>
          <option value="0">Unavailable</option>
        </select>
      </div>

      <div class="search-box">
        <input
            type="text"
            v-model="searchQuery"
            placeholder="Search scooters..."
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

    <div class="table-container">
      <table class="scooter-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>Status</th>
          <th>Pricing</th>
          <th>Battery</th>
          <th>Speed</th>
          <th>Store ID</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="scooter in filteredPaginatedScooters" :key="scooter.id">
          <td>{{ scooter.id }}</td>
          <td>
              <span :class="['status-label', getStatusClass(scooter.status)]">
                {{ getStatusText(scooter.status) }}
              </span>
          </td>
          <td>
            <div><strong>Hour:</strong> £{{ scooter.priceHour }}</div>
            <div><strong>Day:</strong> £{{ scooter.priceDay }}</div>
            <div><strong>Week:</strong> £{{ scooter.priceWeek }}</div>
          </td>
          <td>{{ scooter.battery }}</td>
          <td>{{ scooter.speed }}</td>
          <td>{{ scooter.store.id }}</td>
          <td>
            <button class="status-btn" @click="changeScooterStatus(scooter.id)">
              Change Status
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="pagination-container">
      <span class="pagination-info">Total {{ filteredScooters.length }} Scooters</span>

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
      scooters: [],
      stores: [],
      filteredScooters: [],
      currentPage: 1,
      itemsPerPage: 10,
      inputPage: 1,
      searchQuery: '',
      statusFilter: 'all',
      storeFilter: 'all',
      selectedStore: null
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.filteredScooters.length / this.itemsPerPage);
    },
    displayedPages() {
      const pages = [];
      const total = this.totalPages;
      const current = this.currentPage;

      // Always show first page
      pages.push(1);

      // Show pages around current page
      const start = Math.max(2, current - 2);
      const end = Math.min(total - 1, current + 2);

      // Add ellipsis if needed
      if (start > 2) pages.push('ellipsis');

      // Add middle pages
      for (let i = start; i <= end; i++) {
        pages.push(i);
      }

      // Add ellipsis if needed
      if (end < total - 1) pages.push('ellipsis');

      // Always show last page if there is more than one page
      if (total > 1) pages.push(total);

      return pages;
    },
    filteredPaginatedScooters() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredScooters.slice(start, end);
    }
  },
  async mounted() {
    await this.fetchStores();
    await this.fetchScooters();
  },
  methods: {
    resetSearch() {
      this.searchQuery = '';
      this.statusFilter = 'all';
      this.storeFilter = 'all';
      this.filterScooters();
    },
    async fetchStores() {
      try {
        const res = await axios.get('http://localhost:8080/api/stores/getAll');
        this.stores = res.data;
      } catch (error) {
        alert('Failed to load stores');
      }
    },
    async fetchScooters() {
      try {
        const res = await axios.get('http://localhost:8080/api/scooters/getAll');
        this.scooters = res.data;
        this.filteredScooters = [...res.data];
      } catch (error) {
        alert('Failed to load scooters');
      }
    },
    handleSearch() {
      this.filterScooters();
    },
    filterScooters() {
      const query = this.searchQuery.toLowerCase().trim();
      const statusFilter = this.statusFilter;
      const storeFilter = this.storeFilter;

      // Update selected store
      if (storeFilter === 'all') {
        this.selectedStore = null;
      } else {
        this.selectedStore = this.stores.find(store => store.id.toString() === storeFilter.toString());
      }

      this.filteredScooters = this.scooters.filter(scooter => {
        // Status filter
        if (statusFilter !== 'all' && scooter.status.toString() !== statusFilter) {
          return false;
        }

        // Store filter
        if (storeFilter !== 'all' && scooter.store.id.toString() !== storeFilter.toString()) {
          return false;
        }

        // If no search query, return all matching status and store
        if (!query) return true;

        // Search in all relevant fields
        return (
            scooter.id.toString().includes(query) ||
            scooter.priceHour.toString().includes(query) ||
            scooter.priceDay.toString().includes(query) ||
            scooter.priceWeek.toString().includes(query) ||
            scooter.battery.toString().includes(query) ||
            scooter.speed.toString().includes(query) ||
            scooter.store.id.toString().includes(query)
        );
      });

      this.resetPagination();
    },
    resetPagination() {
      this.currentPage = 1;
      this.inputPage = 1;
    },
    goToPage(page) {
      page = parseInt(page);
      if (page < 1) page = 1;
      if (page > this.totalPages) page = this.totalPages;

      this.currentPage = page;
      this.inputPage = page;
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },
    getStatusClass(status) {
      const statusClassMap = {
        0: 'status-unavailable',
        1: 'status-available',
      };
      return statusClassMap[status] || 'status-unknown';
    },
    getStatusText(status) {
      const textMap = {
        0: 'Unavailable',
        1: 'Available',
      };
      return textMap[status] || 'Unknown';
    },
    async changeScooterStatus(id) {
      try {
        const res = await axios.get(`http://localhost:8080/api/scooters/changeStatus/${id}`);
        if (res.status === 200) {
          this.fetchScooters();
          alert('Status updated successfully');
        } else {
          alert('Failed to update status');
        }
      } catch (error) {
        alert('Network error');
      }
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

.store-filter, .status-filter {
  display: flex;
  align-items: center;
  gap: 10px;
}

.store-filter label, .status-filter label {
  font-weight: 500;
  color: #444;
}

.store-filter select, .status-filter select {
  padding: 8px 12px;
  border: 2px solid #ddd;
  border-radius: 4px;
  background-color: white;
  cursor: pointer;
  min-width: 200px;
}

.store-filter select:focus, .status-filter select:focus {
  outline: none;
  border-color: #58c4c9;
}

/* Store information display */
.store-info {
  margin: 20px;
  padding: 5px;
  background-color: #f5f5f5;
  border-radius: 8px;
}

.store-name {
  font-size: 18px;
  font-weight: bold;
  padding: 6px 26px;
  border: 3px solid #008187;
  border-radius: 30px;
}

.store-location {

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

/* Table Styles */
.table-container {
  margin-top: 20px;
  overflow-x: auto;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.scooter-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
}

.scooter-table th,
.scooter-table td {
  padding: 18px 15px;
  text-align: center;
  vertical-align: middle;
  border-bottom: 1px solid #e0e0e0;
  height: 60px;
}

.scooter-table th {
  background-color: #f5f5f5;
  font-weight: bold;
  color: #444;
  border-top: 1px solid #e0e0e0;
}

.scooter-table tr:hover {
  background-color: #f9f9f9;
}

.scooter-table td {
  color: #666;
}

/* Status Label Styles */
.status-label {
  display: inline-block;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: bold;
  text-transform: uppercase;
}

.status-available {
  background-color: #67c23a;
  color: white;
}

.status-unavailable {
  background-color: #f56c6c;
  color: white;
}

/* Action Button Styles */
.status-btn {
  background-color: #58c4c9;
  color: white;
  padding: 8px 14px;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  transition: background 0.2s ease-in-out;
  font-size: 16px;
  font-weight: bold;
}

/* Pagination Styles */
.pagination-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 30px;
  font-size: 16px;
  flex-wrap: wrap;
  gap: 15px;
  padding: 0 2%;
}

.pagination-info {
  font-size: 18px;
  font-weight: bold;
  color: #444444;
  padding: 1px 10px;
  border: 3px solid #58c4c9;
  border-radius: 20px;
  box-shadow:  1px 1px 2px #58c4c9;
}

.pagination-buttons {
  display: flex;
  align-items: center;
  gap: 5px;
}

.pagination-prev,
.pagination-next,
.pagination-page {
  min-width: 36px;
  height: 36px;
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
  min-width: 36px;
  height: 36px;
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
  height: 36px;
  border: 2px solid #d9d9d9;
  border-radius: 4px;
  text-align: center;
  padding: 0 5px;
}

.pagination-jump input:focus {
  outline: none;
  border-color: #58c4c9;
}

.pagination-jump button {
  height: 36px;
  padding: 0 15px;
  margin-left: 10%;
  border-radius: 20px;
  background-color: #58c4c9;
  color: white;
  font-weight: bold;
  border: 1px solid #d9d9d9;
  cursor: pointer;
  transition: all 0.3s;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .filter-container {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .store-filter, .status-filter {
    width: 100%;
  }

  .store-filter select, .status-filter select {
    width: 100%;
  }

  .search-box {
    width: 100%;
  }

  .search-box input {
    width: 100%;
  }

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