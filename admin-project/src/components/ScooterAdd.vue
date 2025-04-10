<template>
  <div>
    <h2 class="title">Scooter Management</h2>
    <div class="form">
      <!-- Location row -->
      <div class="form-row">
        <div class="form-group full-width">
          <label>Location</label>
          <input v-model="newScooter.location" placeholder="Enter location" class="input" />
        </div>
      </div>

      <!-- Price row -->
      <div class="form-row">
        <div class="form-group">
          <label>Price per Hour</label>
          <input v-model.number="newScooter.priceHour" type="number" step="0.01" placeholder="e.g., 2.50" class="input" />
        </div>
        <div class="form-group">
          <label>Price for Four Hours</label>
          <input v-model.number="newScooter.priceFourHour" type="number" step="0.01" placeholder="e.g., 8.00" class="input" />
        </div>
      </div>

      <div class="form-row">
        <div class="form-group">
          <label>Price per Day</label>
          <input v-model.number="newScooter.priceDay" type="number" step="0.01" placeholder="e.g., 15.00" class="input" />
        </div>
        <div class="form-group">
          <label>Price per Week</label>
          <input v-model.number="newScooter.priceWeek" type="number" step="0.01" placeholder="e.g., 70.00" class="input" />
        </div>
      </div>

      <!-- Status row -->
      <div class="form-row">
        <div class="form-group medium-width">
          <label>Status</label>
          <select v-model="newScooter.status" class="input">
            <option :value="1">Active</option>
            <option :value="0">Inactive</option>
          </select>
        </div>
      </div>

      <!-- Coordinates row -->
      <div class="form-row">
        <div class="form-group">
          <label>Longitude</label>
          <input v-model.number="newScooter.longitude" type="number" step="0.000001" placeholder="e.g., 103.851959" class="input" />
        </div>
        <div class="form-group">
          <label>Latitude</label>
          <input v-model.number="newScooter.latitude" type="number" step="0.000001" placeholder="e.g., 1.290270" class="input" />
        </div>
      </div>

      <!-- Buttons row -->
      <div class="form-row">
        <button class="button" @click="addScooter">Add Scooter</button>
      </div>
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
        status: 1,
        longitude: null,
        latitude: null,
      },
    };
  },
  methods: {
    async addScooter() {
      if (!this.newScooter.location || !this.newScooter.priceHour || !this.newScooter.priceDay) {
        alert('Please enter required fields (location, price per hour and day)');
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
          this.$emit('scooter-added');
        } else {
          alert(res.data.message || 'Something went wrong');
        }
      } catch (error) {
        alert('Failed to add scooter');
      }
    },
  },
};
</script>

<style scoped>
.title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
}

.form {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 20px;
  border-radius: 12px;
  max-width: 99%;
}

.form-row {
  display: flex;
  gap: 16px;
  width: 100%;
}

.form-group {
  display: flex;
  flex-direction: column;
  flex: 1;
}

.form-group.medium-width {
  width: 48%;
}

.form-group.full-width {
  width: 100%;
}

label {
  font-size: 16px;
  margin-bottom: 6px;
  color: #333;
  font-weight: bold;
}

.input {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 14px;
  margin-right: 1%;
}

.input:focus {
  outline: none;
  border-color: #58c4c9;
  box-shadow: 0 0 1px #58c4c9;
}

.button {
  background: #58c4c9;
  color: white;
  padding: 12px;
  border: none;
  font-size: 16px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.3s ease;
  width: 200px;
  margin-top: 10px;
}

.button:hover {
  background: #3bb7bd;
}
</style>
