<template>
  <div class="add-scooter-form">
    <h2>Add Scooter</h2>
    <div class="form-group">
      <label>Location:</label>
      <input v-model="form.location" type="text" placeholder="Enter location">
      <span v-if="errors.location" class="error-message">{{ errors.location }}</span>
    </div>

    <div class="form-group">
      <label>Price per hour:</label>
      <input v-model="form.priceHour" type="text" placeholder="Enter price per hour">
      <span v-if="errors.priceHour" class="error-message">{{ errors.priceHour }}</span>
    </div>

    <div class="form-group">
      <label>Price per four hours:</label>
      <input v-model="form.priceFourHour" type="text" placeholder="Enter price per four hours">
      <span v-if="errors.priceFourHour" class="error-message">{{ errors.priceFourHour }}</span>
    </div>

    <div class="form-group">
      <label>Price per day:</label>
      <input v-model="form.priceDay" type="text" placeholder="Enter price per day">
      <span v-if="errors.priceDay" class="error-message">{{ errors.priceDay }}</span>
    </div>

    <div class="form-group">
      <label>Price per week:</label>
      <input v-model="form.priceWeek" type="text" placeholder="Enter price per week">
      <span v-if="errors.priceWeek" class="error-message">{{ errors.priceWeek }}</span>
    </div>

    <div class="form-group">
      <button @click="add" :disabled="loading">
        {{ loading ? 'Adding...' : 'Add' }}
      </button>
    </div>

    <div v-if="message" :class="['message', messageType]">
      <p>{{ message }}</p>
      <template v-if="addedScooter">
        <p>Scooter ID: {{ addedScooter.scooterId }}</p>
      </template>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ScooterAdd',
  data() {
    return {
      form: {
        location: '',
        priceHour: '',
        priceFourHour: '',
        priceDay: '',
        priceWeek: '',
        status: 1
      },
      loading: false,
      message: '',
      messageType: 'success',
      addedScooter: null,
      errors: {}
    }
  },
  methods: {
    async add() {
      this.loading = true
      this.message = ''
      this.addedScooter = null
      this.errors = {}

      // Basic frontend validation
      if (!this.form.location ||!this.form.priceHour || !this.form.priceFourHour  ||!this.form.priceDay ||!this.form.priceWeek) {
        this.message = 'Please fill in all required fields'
        this.messageType = 'error'
        this.loading = false
        return
      }

      this.form.priceHour = parseFloat(this.form.priceHour);
      this.form.priceFourHour = parseFloat(this.form.priceFourHour);
      this.form.priceDay = parseFloat(this.form.priceDay);
      this.form.priceWeek = parseFloat(this.form.priceWeek);

      try {
        const response = await axios.post('http://localhost:8080/api/scooters/add', this.form)

        this.message = response.data.message
        this.messageType = 'success'
        this.addedScooter = {
          scooterId: response.data.scooterId,
        }

        // Clear form
        this.form = {
          location: '',
          priceHour: '',
          priceFourHour: '',
          priceDay: '',
          priceWeek: ''
        }
      } catch (error) {
        if (error.response?.data && typeof error.response.data === 'object') {
          // Handle validation errors
          this.errors = error.response.data
          this.message = 'Please correct the errors below'
        } else {
          // Handle other errors
          this.message = error.response?.data || 'Adding scooter failed'
        }
        this.messageType = 'error'
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.add-scooter-form {
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input, select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  width: 100%;
  padding: 10px;
  background: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:disabled {
  background: #cccccc;
}

.message {
  margin-top: 15px;
  padding: 10px;
  border-radius: 4px;
}

.success {
  background: #E8F5E9;
  color: #2E7D32;
}

.error {
  background: #FFEBEE;
  color: #C62828;
}

.error-message {
  color: #C62828;
  font-size: 0.8em;
  margin-top: 5px;
  display: block;
}
</style>