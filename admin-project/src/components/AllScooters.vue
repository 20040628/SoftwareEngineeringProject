<template>
  <div>
    <div class="header-container">
      <h2 class="title">Scooter Management</h2>
      <div class="filter-container">
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
        <div class="status-filter">
          <label for="status-select">Filter by Status:</label>
          <select id="status-select" v-model="statusFilter" @change="filterScooters">
            <option value="all">All</option>
            <option value="1">Available</option>
            <option value="0">Unavailable</option>
          </select>
        </div>
      </div>
    </div>

    <div class="table-container">
      <table class="scooter-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>Location</th>
          <th>Status</th>
          <th>Pricing</th>
          <th>Coordinates</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="scooter in filteredPaginatedScooters" :key="scooter.id">
          <td>{{ scooter.id }}</td>
          <td>{{ scooter.location }}</td>
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
          <td>({{ scooter.longitude }}, {{ scooter.latitude }})</td>
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
      filteredScooters: [],
      currentPage: 1,
      itemsPerPage: 10,
      inputPage: 1,
      searchQuery: '',
      statusFilter: 'all'
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
  mounted() {
    this.fetchScooters();
  },
  methods: {
    resetSearch() {
      this.searchQuery = '';
      this.statusFilter = 'all';
      this.filterScooters();
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

      this.filteredScooters = this.scooters.filter(scooter => {
        // Status filter
        if (statusFilter !== 'all' && scooter.status.toString() !== statusFilter) {
          return false;
        }

        // If no search query, return all matching status
        if (!query) return true;

        // Search in all relevant fields
        return (
            scooter.id.toString().includes(query) ||
            scooter.location.toLowerCase().includes(query) ||
            scooter.priceHour.toString().includes(query) ||
            scooter.priceDay.toString().includes(query) ||
            scooter.priceWeek.toString().includes(query) ||
            scooter.longitude.toString().includes(query) ||
            scooter.latitude.toString().includes(query)
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
/* Header and Filter Container Styles */
.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.filter-container {
  display: flex;
  gap: 20px;
  align-items: center;
  flex-wrap: wrap;
}

/* Search Box Styles */
.search-box {
  position: relative;
  display: flex;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 20px;
  overflow: hidden;
  background: white;
}

.search-box input {
  padding: 10px 15px;
  border: none;
  font-size: 14px;
  width: 250px;
  outline: none;
}

.search-button {
  background: white;
  border: none;
  padding: 0 5px;
  height: 100%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s;
  border-left: 1px solid #ddd;
}

.reset-button {
  background: white;
  border: none;
  padding: 0 15px;
  height: 100%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s;
  border-left: 1px solid #ddd;
  font-size: 14px;
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
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: white;
  cursor: pointer;
}

.status-filter select:focus {
  outline: none;
  border-color: #409eff;
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
  border-radius: 4px;
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
  border-radius: 4px;
  cursor: pointer;
  transition: background 0.2s ease-in-out;
  font-size: 16px;
  font-weight: bold;
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

/* Responsive adjustments */
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

  .header-container {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .filter-container {
    width: 100%;
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .search-box {
    width: 100%;
  }

  .search-box input {
    width: 100%;
  }
}
</style>