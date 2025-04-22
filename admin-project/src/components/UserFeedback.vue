<template>
  <div>
    <h2 class="title">User Feedback</h2>
    <div class="filter-container">
      <div class="status-filter">
        <label for="status-select">Filter by Status:</label>
        <select id="status-select" v-model="selectedStatus" @change="filterFeedbacks">
          <option value="">All</option>
          <option v-for="(status, key) in statusOptions"
                  :key="key"
                  :value="key">
            {{ status.label }}
          </option>
        </select>
      </div>
      <div class="search-box">
        <input
            type="text"
            v-model="searchQuery"
            placeholder="Search feedback..."
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
      <table class="feedback-table">
        <thead>
        <tr>
          <th>ID</th>
          <th>User ID</th>
          <th>Content</th>
          <th>Create Time</th>
          <th>Status</th>
          <th>Priority</th>
          <th>Admin Response</th>
          <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="feedback in filteredPaginatedFeedbacks" :key="feedback.id">
          <td>{{ feedback.id }}</td>
          <td>{{ feedback.userId }}</td>
          <td>{{ feedback.content }}</td>
          <td style="white-space: pre-line">{{ formatDate(feedback.createTime) }}</td>
          <td>
            <div class="status-group">
              <button
                  v-for="(status, key) in statusOptions"
                  :key="key"
                  @click="feedback.status = key"
                  class="status-button"
                  :class="{ 'active': feedback.status === key }"
              >
                <span class="color-dot" :style="{ backgroundColor: status.color }"></span>
                {{ status.label }}
              </button>
            </div>
          </td>
          <td>
            <div class="priority-group">
              <button
                  v-for="(priority, key) in priorityOptions"
                  :key="key"
                  @click="feedback.priority = Number(key)"
                  class="priority-button"
                  :class="{ 'active': feedback.priority === Number(key) }"
              >
                <span class="color-dot" :style="{ backgroundColor: priority.color }"></span>
                {{ priority.label }}
              </button>
            </div>
          </td>
          <td>
              <textarea
                  v-model="feedback.adminResponse"
                  class="admin-response"
                  placeholder="输入回复..."
              ></textarea>
          </td>
          <td>
            <button @click="updateFeedback(feedback)" class="update-button">Update</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination -->
    <div class="pagination-container">
      <span class="pagination-info">Total {{ filteredFeedbacks.length }} Feedbacks</span>

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
      feedbacks: [],
      filteredFeedbacks: [],
      currentPage: 1,
      itemsPerPage: 10,
      inputPage: 1,
      searchQuery: '',
      statusOptions: {
        pending: { label: 'Pending', color: '#dc3545' },
        processing: { label: 'Processing', color: '#ffc107' },
        resolved: { label: 'Resolved', color: '#28a745' }
      },
      selectedStatus: '',
      priorityOptions: {
        2: { label: 'High', color: '#dc3545' },
        1: { label: 'Medium', color: '#ffc107' },
        0: { label: 'Low', color: '#28a745' }
      }
    };
  },
  computed: {
    filteredFeedbacks() {
      if (!this.selectedStatus) return this.feedbacks;
      return this.feedbacks.filter(f => f.status === this.selectedStatus);
    },
    totalPages() {
      return Math.ceil(this.filteredFeedbacks.length / this.itemsPerPage);
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
    filteredPaginatedFeedbacks() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredFeedbacks.slice(start, end);
    }
  },
  mounted() {
    this.fetchFeedbacks();
  },
  methods: {
    resetSearch() {
      this.searchQuery = '';
      this.selectedStatus = '';
      this.filterFeedbacks();
    },
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      return `${year}-${month}-${day}\n${hours}:${minutes}`;
    },
    async fetchFeedbacks() {
      try {
        const res = await axios.get('http://localhost:8080/api/feedback/all');
        if (res.status === 200) {
          this.feedbacks = res.data.map(item => {
            const processedItem = { ...item };
            if (typeof processedItem.priority === 'string') {
              processedItem.priority = Number(processedItem.priority);
              if (isNaN(processedItem.priority)) {
                processedItem.priority = 0;
              }
            }
            if (typeof processedItem.status === 'string') {
              processedItem.status = processedItem.status.toLowerCase();
              const validStatus = ['pending', 'processing', 'resolved'];
              if (!validStatus.includes(processedItem.status)) {
                processedItem.status = 'pending';
              }
            }
            return processedItem;
          });
          this.filteredFeedbacks = [...this.feedbacks];
        }
      } catch (error) {
        console.error('Fetch feedbacks error:', error);
        const errorMsg = error.response
            ? `Server error: ${error.response.data.message || 'Unknown error'}`
            : `Network error: ${error.message}`;
        alert(`Failed to load feedbacks: ${errorMsg}`);
      }
    },
    handleSearch() {
      this.filterFeedbacks();
    },
    filterFeedbacks() {
      const query = this.searchQuery.toLowerCase().trim();
      const statusFilter = this.selectedStatus;

      this.filteredFeedbacks = this.feedbacks.filter(feedback => {
        // Status filter
        if (statusFilter && feedback.status !== statusFilter) {
          return false;
        }

        // If no search query, return all matching status
        if (!query) return true;

        // Search in all relevant fields
        return (
            feedback.id.toString().includes(query) ||
            feedback.userId.toString().includes(query) ||
            feedback.content.toLowerCase().includes(query) ||
            this.formatDate(feedback.createTime).toLowerCase().includes(query)
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
    async updateFeedback(feedback) {
      try {
        const res = await axios.put(`http://localhost:8080/api/feedback/${feedback.id}`, {
          priority: feedback.priority,
          status: feedback.status,
          adminResponse: feedback.adminResponse,
        });
        if (res.status === 200) {
          alert('Feedback updated successfully');
          this.fetchFeedbacks();
        } else {
          alert('Failed to update feedback');
        }
      } catch (error) {
        console.error('Error updating feedback:', error.response);
        alert(`Failed to update feedback: ${error.response?.data?.message || error.message}`);
      }
    },
  },
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

.feedback-table {
  width: 100%;
  border-collapse: collapse;
  background-color: white;
}

.feedback-table th,
.feedback-table td {
  padding: 18px 15px;
  text-align: center;
  vertical-align: middle;
  border-bottom: 1px solid #e0e0e0;
}

.feedback-table th {
  background-color: #f5f5f5;
  font-weight: bold;
  color: #444;
  border-top: 1px solid #e0e0e0;
}

.feedback-table tr:hover {
  background-color: #f9f9f9;
}

.feedback-table td {
  max-width: 250px;
  color: #666;
}

/* Status and Priority Button Groups */
.status-group, .priority-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
  align-items: center;
}

.status-button, .priority-button {
  display: flex;
  align-items: center;
  padding: 4px 8px;
  border: 1px solid #e9ecef;
  border-radius: 15px;
  background: white;
  cursor: pointer;
  transition: all 0.2s;
  width: 80%;
  justify-content: center;
}

.status-button:hover, .priority-button:hover {
  background: #f8f9fa;
}

.status-button.active, .priority-button.active {
  border-color: #007bff;
  background: #e7f1ff;
}

.color-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  margin-right: 8px;
}

/* Priority button order */
.priority-group button:nth-child(1) { order: 3; } /* High */
.priority-group button:nth-child(2) { order: 2; } /* Medium */
.priority-group button:nth-child(3) { order: 1; } /* Low */

/* Admin Response Textarea */
.admin-response {
  padding: 12px 16px;
  border: 2px solid #e0e3e7;
  border-radius: 8px;
  font-size: 14px; /* Slightly increased font size */
  color: #495057;
  line-height: 1.5;
  min-height: 100px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: #ffffff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* Slightly darker shadow */
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
  resize: vertical;

}

.admin-response:focus {
  border-color: #58c4c9; /* Change border color on focus */
  box-shadow: 0 0 5px rgba(88, 196, 201, 0.5); /* Add focus shadow */
  outline: none; /* Remove default outline */
}


.admin-response::placeholder {
  color: #9aa1a9;
  font-weight: 400;
  letter-spacing: 0.2px;
}

/* Update Button */
.update-button {
  background: #58c4c9;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.2s;
}

.update-button:hover {
  background: #4ab0b5;
  transform: translateY(-1px);
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
    gap: 15px;
    align-items: flex-start;
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

  .status-group, .priority-group {
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: center;
  }

  .status-button, .priority-button {
    width: auto;
    min-width: 80px;
  }
}
</style>