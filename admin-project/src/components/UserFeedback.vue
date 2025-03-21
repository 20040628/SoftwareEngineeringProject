<template>
  <div>
    <h2 class="title">User Feedback</h2>
    <div class="feedback-list">
      <div class="feedback-item" v-for="feedback in feedbacks" :key="feedback.id">
        <div class="feedback-info">
          <div class="info-row">
            <span class="label">ID:</span> <span class="value">{{ feedback.id }}</span>
          </div>
          <div class="info-row">
            <span class="label">User ID:</span> <span class="value">{{ feedback.userId }}</span>
          </div>
          <div class="info-row">
            <span class="label">Content:</span> <span class="value">{{ feedback.content }}</span>
          </div>
          <div class="info-row">
            <span class="label">Create Time:</span> <span class="value">{{ feedback.createTime }}</span>
          </div>

          <div class="info-row">
            <span class="label">Status:</span>
            <select v-model="feedback.status">
              <option value="pending">Pending</option>
              <option value="processing">Processing</option>
              <option value="resolved">Resolved</option>
            </select>
          </div>

          <div class="info-row">
            <span class="label">Priority:</span>
            <select v-model="feedback.priority">
              <option :value="0">Low</option>
              <option :value="1">Medium</option>
              <option :value="2">High</option>
            </select>
          </div>

          <div class="info-row">
            <span class="label">Admin Response:</span>
            <textarea v-model="feedback.adminResponse" placeholder="Enter response..."></textarea>
          </div>

          <button @click="updateFeedback(feedback)">Update</button>
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
    };
  },
  mounted() {
    this.fetchFeedbacks();
  },
  methods: {
    async fetchFeedbacks() {
      try {
        const res = await axios.get('http://localhost:8080/api/feedback/all');
        if (res.status === 200) {
          this.feedbacks = res.data;
        } else {
          alert('Failed to load feedbacks');
        }
      } catch (error) {
        alert('Failed to load feedbacks');
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
.feedback-list {
  margin-top: 20px;
}

.feedback-item {
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

textarea {
  width: 100%;
  min-height: 50px;
}

button {
  margin-top: 10px;
  padding: 8px 12px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>
