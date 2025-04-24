<template>
  <div>

    <h2 class="title">All Users</h2>
    <div class="filter-container">
      <div class="role-filter">
        <label for="role-select">Filter by Role:</label>
        <select id="role-select" v-model="roleFilter" @change="filterUsers">
          <option value="0">Admin</option>
          <option value="1">User</option>
        </select>
      </div>
      <div class="status-filter" v-if="roleFilter === '1'">
        <label for="status-select">Filter by Status:</label>
        <select id="status-select" v-model="statusFilter" @change="filterUsers">
          <option value="1">Active</option>
          <option value="0">Inactive</option>
        </select>
      </div>
      <div class="search-box">
        <input
            type="text"
            v-model="searchQuery"
            placeholder="Search users..."
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
      <table class="user-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>Role</th>
          <th>Username</th>
          <th>Email</th>
          <th>Mobile</th>
          <th>Birthday</th>
          <th>Payment Method</th>
          <th>Frequent User</th>
          <th>Status</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="user in filteredPaginatedUsers" :key="user.id">
          <td>{{ user.id }}</td>
          <td>
            <span :class="['role-label', getRoleClass(user.role)]">
              {{ getRoleLabel(user.role) }}
            </span>
          </td>
          <td>{{ user.username }}</td>
          <td>{{ user.email }}</td>
          <td>{{ user.mobile }}</td>
          <td>{{ formatDate(user.birthday) }}</td>
          <td>{{ user.paymentMethod }}</td>
          <td>
            <span :class="['frequent-label', user.isFrequentUser ? 'frequent-yes' : 'frequent-no']">
              {{ user.isFrequentUser ? 'Yes' : 'No' }}
            </span>
          </td>
          <td>
            <span :class="['status-label', getStatusClass(user.status)]">
              {{ getStatusLabel(user.status) }}
            </span>
          </td>
          <td>
            <button class="action-btn delete-btn" @click="disableUser(user.id)">
              <img :src="user.status ? '/static/center/disable.png' : '/static/center/enable.png'"
                   :alt="user.status ? 'Disable' : 'Enable'"
                   class="action-icon">
            </button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="pagination-container">
      <span class="pagination-info">Total {{ filteredUsers.length }} Users</span>

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
      users: [],
      filteredUsers: [],
      currentPage: 1,
      itemsPerPage: 10,
      inputPage: 1,
      searchQuery: '',
      statusFilter: '1',
      roleFilter: '1' // Default to showing users
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.filteredUsers.length / this.itemsPerPage);
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
    filteredPaginatedUsers() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredUsers.slice(start, end);
    }
  },
  mounted() {
    this.fetchUsers();
  },
  methods: {
    resetSearch() {
      this.searchQuery = '';
      this.statusFilter = '1';
      this.roleFilter = '1';
      this.filterUsers();
    },
    formatDate(dateString) {
      if (!dateString) return 'N/A';
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    async fetchUsers() {
      try {
        const res = await axios.get('http://localhost:8080/api/users/getAll');
        if (res.status === 200) {
          this.users = res.data;
          this.filteredUsers = [...res.data];
          this.filterUsers();
        }
      } catch (error) {
        console.error('Failed to load users:', error);
        alert('Failed to load users');
      }
    },
    async disableUser(userId) {
      if (!confirm('Are you sure you want to change this user\'s status?')) return;

      try {
        const token = localStorage.getItem('token') || sessionStorage.getItem('token');

        // 检查token是否存在
        if (!token) {
          alert('Please login first');
          this.$router.push('/login');
          return;
        }

        const res = await axios.get(
            `http://localhost:8080/api/users/changeStatus/${userId}`,
            {
              headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
              }
            }
        );

        if (res.status === 200) {
          alert('User status changed successfully');
          this.fetchUsers();
        }
      } catch (error) {
        console.error('Error changing user status:', error.response);
        if (error.response?.status === 401) {
          alert('Session expired, please login again');
          this.$router.push('/login');
        } else {
          alert(`Failed to change user status: ${error.response?.data?.message || error.message}`);
        }
      }
    },
    handleSearch() {
      this.filterUsers();
    },
    filterUsers() {
      const query = this.searchQuery.toLowerCase().trim();
      const statusFilter = this.statusFilter;
      const roleFilter = this.roleFilter;

      this.filteredUsers = this.users.filter(user => {
        // Role filter
        if (user.role.toString() !== roleFilter) {
          return false;
        }

        // Status filter only applies to users (role=1)
        if (roleFilter === '1' && statusFilter !== 'all' && user.status.toString() !== statusFilter) {
          return false;
        }

        // If no search query, return all matching status and role
        if (!query) return true;

        // Search in all relevant fields
        return (
            user.id.toString().includes(query) ||
            user.username.toLowerCase().includes(query) ||
            user.email.toLowerCase().includes(query) ||
            user.mobile.toLowerCase().includes(query) ||
            this.formatDate(user.birthday).toLowerCase().includes(query) ||
            user.paymentMethod.toLowerCase().includes(query)
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

    getStatusLabel(status) {
      return status ? 'Active' : 'Inactive';
    },
    getStatusClass(status) {
      return status ? 'status-active' : 'status-inactive';
    },
    getRoleLabel(role) {
      return role === 0 ? 'Admin' : 'User';
    },
    getRoleClass(role) {
      return role === 0 ? 'role-admin' : 'role-user';
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

.reset-icon {
  width: 20px;
  height: 20px;
}

/* Status and Role Filter Styles */
.status-filter, .role-filter {
  display: flex;
  align-items: center;
  gap: 10px;
}

.status-filter label, .role-filter label {
  font-weight: 500;
  color: #444;
}

.status-filter select, .role-filter select {
  padding: 8px 12px;
  border: 2px solid #ddd;
  border-radius: 4px;
  background-color: white;
  cursor: pointer;
}

.status-filter select:focus, .role-filter select:focus {
  outline: none;
  border-color: #58c4c9;
}

/* Table Styles */
.table-container {
  margin-top: 20px;
  overflow-x: auto;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.user-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
}

.user-table th,
.user-table td {
  padding: 18px 15px;
  text-align: center;
  vertical-align: middle;
  border-bottom: 1px solid #e0e0e0;
  height: 60px;
}

.user-table th {
  background-color: #f5f5f5;
  font-weight: bold;
  color: #444;
  border-top: 1px solid #e0e0e0;
}

.user-table tr:hover {
  background-color: #f9f9f9;
}

.user-table td {
  color: #666;
  font-size: 16px;
}

/* Status Label Styles */
.status-label {
  display: inline-block;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 16px;
  font-weight: bold;
  text-transform: uppercase;
}

.status-active {
  background-color: #67c23a;
  color: white;
}

.status-inactive {
  background-color: #f56c6c;
  color: white;
}

/* Role Label Styles */
.role-label {
  display: inline-block;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 16px;
  font-weight: bold;
}

.role-admin {
  background-color: #e6a23c;
  color: white;
}

.role-user {
  background-color: #409eff;
  color: white;
}

/* Frequent User Label */
.frequent-label {
  display: inline-block;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 16px;
}

.frequent-yes {
  background-color: #ef504c;
  color: white;
}

.frequent-no {
  background-color: #909399;
  color: white;
}

/* Action Buttons */
.action-btn {
  background-color: transparent;
  border: none;
  cursor: pointer;
  margin: 0 5px;
  padding: 5px;
  border-radius: 20px;
  transition: background-color 0.2s;
}


.action-icon {
  width: 30px;
  height: 30px;
}

.edit-btn:hover {
  background-color: #e6f7ff;
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

/* Responsive Adjustments */
@media (max-width: 768px) {
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

  .user-table {
    font-size: 14px;
  }

  .user-table th,
  .user-table td {
    padding: 8px 10px;
  }
}
</style>