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
            <span class="label">Status:</span> <span class="value">{{ feedback.status }}</span>
          </div>
          <div class="info-row">
            <span class="label">Priority:</span> <span class="value">{{ feedback.priority }}</span>
          </div>
          <div class="info-row">
            <span class="label">Admin Response:</span> <span class="value">{{ feedback.adminResponse }}</span>
          </div>
          <div class="info-row">
            <span class="label">Response Time:</span> <span class="value">{{ feedback.responseTime }}</span>
          </div>
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
</style>