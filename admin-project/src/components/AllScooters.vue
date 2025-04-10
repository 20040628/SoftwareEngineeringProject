<template>
  <div>
    <h2 class="title">Show all scooters</h2>
    <div class="scooter-list">
      <div class="scooter-item" v-for="scooter in scooters" :key="scooter.id">
        <div class="scooter-info">
          <div class="info-row">
            <span class="label">ID:</span> <span class="value">{{ scooter.id }}</span>
          </div>
          <div class="info-row">
            <span class="label">Location:</span> <span class="value">{{ scooter.location }}</span>
          </div>
          <div class="info-row">
            <span class="label">Price:</span>
            <span class="value">
              ${{ scooter.priceHour }}/hr | ${{ scooter.priceDay }}/day | ${{ scooter.priceWeek }}/week
            </span>
          </div>
          <div class="info-row">
            <span class="label">Coordinates:</span>
            <span class="value">({{ scooter.longitude }}, {{ scooter.latitude }})</span>
          </div>
          <div class="status-tag" :class="getStatusClass(scooter.status)">
            {{ getStatusText(scooter.status) }}
          </div>
        </div>
        <!-- 按钮 -->
        <button class="status-btn" @click="changeScooterStatus(scooter.id)">
          <span>Change Status</span>
        </button>
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
    };
  },
  mounted() {
    this.fetchScooters();
  },
  methods: {
    async fetchScooters() {
      try {
        const res = await axios.get('http://localhost:8080/api/scooters/getAll');
        this.scooters = res.data;
      } catch (error) {
        alert('Failed to load scooters');
      }
    },
    getStatusClass(status) {
      const statusMap = {
        0: 'unavailable',
        1: 'available',
        2: 'maintenance',
      };
      return statusMap[status] || 'unknown';
    },
    getStatusText(status) {
      const textMap = {
        0: 'Unavailable',
        1: 'Available',
        2: 'Maintenance',
      };
      return textMap[status] || 'Unknown';
    },
    async changeScooterStatus(id) {
      try {
        const res = await axios.get(`http://localhost:8080/api/scooters/changeStatus/${id}`);
        if (res.status === 200) {
          this.fetchScooters();
          alert('状态更新成功');
        } else {
          alert('状态更新失败');
        }
      } catch (error) {
        alert('网络错误');
      }
    },
  },
};
</script>

<style scoped>
.scooter-list {
  margin-top: 20px;
}

.scooter-item {
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

.status-tag {
  padding: 6px 12px;
  border-radius: 5px;
  font-size: 14px;
  font-weight: bold;
  text-align: center;
  display: inline-block;
  margin-top: 8px;
  width: fit-content;
}

.available {
  background: #e8f5e9;
  color: #2e7d32;
}

.unavailable {
  background: #ffebee;
  color: #c62828;
}

.maintenance {
  background: #fff3e0;
  color: #ff9800;
}

.status-btn {
  margin-top: 12px;
  background-color: #58c4c9;
  color: white;
  padding: 8px 14px;
  border-radius: 5px;
  text-align: center;
  transition: background 0.2s ease-in-out;
}

.status-btn:hover {
  background-color: #46a3a8;
}
</style>