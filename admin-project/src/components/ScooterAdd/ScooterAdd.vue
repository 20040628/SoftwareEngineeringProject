<template>
  <div>
    <h2 class="title">Scooter Management</h2>
    <!-- 添加滑板车表单 -->
    <div class="form">
      <input v-model="newScooter.location" placeholder="Enter location" class="input" />
      <input v-model="newScooter.priceHour" type="number" placeholder="Price per hour" class="input" />
      <input v-model="newScooter.priceFourHour" type="number" placeholder="Price for four hours" class="input" />
      <input v-model="newScooter.priceDay" type="number" placeholder="Price per day" class="input" />
      <input v-model="newScooter.priceWeek" type="number" placeholder="Price per week" class="input" />
      <input v-model="newScooter.status" type="number" placeholder="Status (0: inactive, 1: active)" class="input" />
      <input v-model="newScooter.longitude" type="number" placeholder="Longitude" class="input" />
      <input v-model="newScooter.latitude" type="number" placeholder="Latitude" class="input" />

      <button class="button" @click="addScooter">Add Scooter</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      newScooter: {
        location: '',
        priceHour: null,
        priceFourHour: null,
        priceDay: null,
        priceWeek: null,
        status: null,
        longitude: null,
        latitude: null,
      },
    };
  },
  methods: {
    async addScooter() {
      if (!this.newScooter.location || !this.newScooter.priceHour || !this.newScooter.priceDay) {
        alert('Please enter required fields');
        return;
      }
      try {
        const res = await axios.post('http://localhost:8080/api/scooters/add', this.newScooter);
        if (res.status === 200) {
          alert('Scooter Added');
          this.newScooter = {
            location: '',
            priceHour: null,
            priceFourHour: null,
            priceDay: null,
            priceWeek: null,
            status: 1,
            longitude: null,
            latitude: null,
          };
          this.$emit('scooter-added'); // 通知父组件刷新列表
        } else {
          alert(res.data.message);
        }
      } catch (error) {
        alert('Failed to add scooter');
      }
    },
  },
};
</script>

<style scoped>
.form {
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
}

.input {
  margin: 5px 0;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.button {
  background: #58c4c9;
  color: white;
  padding: 10px;
  text-align: center;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
}
</style>