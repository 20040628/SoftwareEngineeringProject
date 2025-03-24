<template>
  <div>
    <h2 class="title">User Feedback</h2>
    <div class="feedback-list">
      <div class="table-header">
        <div class="table-cell">ID</div>
        <div class="table-cell">User ID</div>
        <div class="table-cell">Content</div>
        <div class="table-cell">Create Time</div>
        <div class="table-cell">Status</div>
        <div class="table-cell">Priority</div>
        <div class="table-cell">Admin Response</div>
        <div class="table-cell">Action</div>
      </div>
      <div class="feedback-item" v-for="feedback in feedbacks" :key="feedback.id">
        <div class="table-cell">{{ feedback.id }}</div>
        <div class="table-cell">{{ feedback.userId }}</div>
        <div class="table-cell">{{ feedback.content }}</div>
        <div class="table-cell">{{ feedback.createTime }}</div>
        <!-- <div class="table-cell">
          <select v-model="feedback.status" class="status-dropdown">
            <option value="pending">Pending</option>
            <option value="processing">Processing</option>
            <option value="resolved">Resolved</option>
          </select>
        </div> -->
        <!-- 状态选择 -->
        <div class="table-cell">
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
        </div>
        <!-- <div class="table-cell">
          <select v-model="feedback.priority" class="priority-dropdown">
            <option :value="0">Low</option>
            <option :value="1">Medium</option>
            <option :value="2">High</option>
          </select>
        </div> -->
        <!-- 优先级选择 -->
        <div class="table-cell">
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
        </div>
        <div class="table-cell">
            <textarea
                v-model="feedback.adminResponse"
                class="admin-response"
                placeholder="输入回复..."
            ></textarea>
        </div>
        <div class="table-cell">
          <button @click="updateFeedback(feedback)" class="update-button">Update</button>
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
      feedbacks: [],
      statusOptions: {
        pending: { label: 'Pending', color: '#dc3545' },
        processing: { label: 'Processing', color: '#ffc107' },
        resolved: { label: 'Resolved', color: '#28a745' }
      },
      priorityOptions: {
        2: { label: 'High', color: '#dc3545' },
        1: { label: 'Medium', color: '#ffc107' },
        0: { label: 'Low', color: '#28a745' }
      }
    };
  },
  mounted() {
    this.fetchFeedbacks();
  },
  methods: {
    // async fetchFeedbacks() {
    //   try {
    //     const res = await axios.get('http://localhost:8080/api/feedback/all');
    //     if (res.status === 200) {
    //       this.feedbacks = res.data;
    //     } else {
    //       alert('Failed to load feedbacks');
    //     }
    //   } catch (error) {
    //     alert('Failed to load feedbacks');
    //   }
    // },
    async fetchFeedbacks() {
      try {
        // 发送GET请求获取反馈数据
        const res = await axios.get('http://localhost:8080/api/feedback/all');

        // 检查HTTP状态码是否为200
        if (res.status === 200) {
          // 对返回数据进行标准化处理
          this.feedbacks = res.data.map(item => {
            // 创建新对象保留原始数据
            const processedItem = { ...item };

            // 处理priority字段（确保转为数字）
            if (typeof processedItem.priority === 'string') {
              // 显式转换为数字类型
              processedItem.priority = Number(processedItem.priority);

              // 额外验证：如果转换失败（得到NaN）则重置为默认值0
              if (isNaN(processedItem.priority)) {
                console.warn(`Invalid priority value: ${item.priority}, reset to 0`);
                processedItem.priority = 0;
              }
            }

            // 处理status字段（统一为小写格式）
            if (typeof processedItem.status === 'string') {
              processedItem.status = processedItem.status.toLowerCase();

              // 有效性检查：如果状态不在预定范围内，设置为pending
              const validStatus = ['pending', 'processing', 'resolved'];
              if (!validStatus.includes(processedItem.status)) {
                console.warn(`Invalid status: ${processedItem.status}, reset to pending`);
                processedItem.status = 'pending';
              }
            }

            return processedItem;
          });

          console.log('Processed feedback data:', this.feedbacks);
        } else {
          // 处理非200状态码
          alert(`Failed to load feedbacks (HTTP ${res.status})`);
        }
      } catch (error) {
        // 错误处理（网络错误或服务器错误）
        console.error('Fetch feedbacks error:', error);

        // 显示具体错误信息
        const errorMsg = error.response
            ? `Server error: ${error.response.data.message || 'Unknown error'}`
            : `Network error: ${error.message}`;

        alert(`Failed to load feedbacks: ${errorMsg}`);
      }
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
        } else {
          alert('Failed to update feedback');
        }
      } catch (error) {
        alert('Failed to update feedback');
      }
    },
  },
};
</script>

<style scoped>
.title {
  font-size: 20px;
  color: #2c3e50;
  margin-bottom: 25px;
  font-weight: 600;
}

.feedback-list {
  background: white;
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  overflow: hidden;
}

.table-header {
  display: grid;
  grid-template-columns: 50px 80px minmax(150px, 1fr) 120px 120px 120px 200px 100px;
  background: #f8f9fa;
  padding: 12px 15px;
  border-bottom: 2px solid #e9ecef;
  font-size: 13px;
  color: #6c757d;
  align-items: center;
}

.feedback-item {
  display: grid;
  grid-template-columns: 50px 80px minmax(150px, 1fr) 120px 120px 120px 200px 100px;
  padding: 12px 15px;
  border-bottom: 1px solid #e9ecef;
  font-size: 13px;
  color: #495057;
  transition: background 0.2s;
}

.feedback-item:hover {
  background: #f8f9fa;
}

.table-cell {
  display: flex;
  align-items: center;
  padding: 2px 5px;
}

/* 状态按钮样式 */
.status-dropdown {
  appearance: none;
  padding: 4px 8px;
  border-radius: 12px;
  border: none;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  text-align: center;
  width: 90px;
  background-repeat: no-repeat;
  background-position: right 6px center;
  background-size: 12px;
  background-image: url("data:image/svg+xml;charset=UTF-8,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3e%3cpolyline points='6 9 12 15 18 9'%3e%3c/polyline%3e%3c/svg%3e");
}

.status-dropdown[value="pending"] { background-color: #ffe3e3; color: #dc3545; }
.status-dropdown[value="processing"] { background-color: #fff3cd; color: #ffc107; }
.status-dropdown[value="resolved"] { background-color: #d4edda; color: #28a745; }

/* 优先级按钮样式 */
.priority-dropdown {
  appearance: none;
  padding: 4px 8px;
  border-radius: 12px;
  border: none;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  width: 90px;
  background-repeat: no-repeat;
  background-position: right 6px center;
  background-size: 12px;
  background-image: url("data:image/svg+xml;charset=UTF-8,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3e%3cpolyline points='6 9 12 15 18 9'%3e%3c/polyline%3e%3c/svg%3e");
}

.priority-dropdown[value="0"] { background-color: #ffe3e3; color: #dc3545; }  /* Low */
.priority-dropdown[value="1"] { background-color: #fff3cd; color: #ffc107; }  /* Medium */
.priority-dropdown[value="2"] { background-color: #ffe3e3; color: #dc3545; }  /* High */



.update-button {
  background: #009688;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.2s;
}

.update-button:hover {
  background: #0056b3;
  transform: translateY(-1px);
}

.status-group, .priority-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
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
  width: 100%;
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

.priority-button.active {
  border-color: #007bff !important;
  background: #e7f1ff !important;
}

.priority-button[data-priority="0"] .color-dot { background-color: #28a745 !important; }
.priority-button[data-priority="1"] .color-dot { background-color: #ffc107 !important; }
.priority-button[data-priority="2"] .color-dot { background-color: #dc3545 !important; }


/* 优先级按钮排序调整 */
.priority-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

/* 确保按High(2)->Medium(1)->Low(0)顺序显示 */
.priority-group button:nth-child(1) { order: 3; } /* High */
.priority-group button:nth-child(2) { order: 2; } /* Medium */
.priority-group button:nth-child(3) { order: 1; } /* Low */

/* 颜色点保持功能不变 */
.priority-button[data-priority="2"] .color-dot { background-color: #dc3545 !important; }
.priority-button[data-priority="1"] .color-dot { background-color: #ffc107 !important; }
.priority-button[data-priority="0"] .color-dot { background-color: #28a745 !important; }

.admin-response {
  width: 100%;
  padding: 12px 16px;
  border: 2px solid #e0e3e7;
  border-radius: 8px;
  font-size: 13px;
  color: #495057;
  line-height: 1.5;
  min-height: 100px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  background: #ffffff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
  resize: vertical;
}

.admin-response:focus {
  outline: none;
  border-color: #4d90fe;
  box-shadow: 0 0 0 3px rgba(77, 144, 254, 0.15);
}

.admin-response::placeholder {
  color: #9aa1a9;
  font-weight: 400;
  letter-spacing: 0.2px;
}

/* 添加输入动画效果 */
.admin-response:focus {
  transform: translateY(-1px);
}
</style>