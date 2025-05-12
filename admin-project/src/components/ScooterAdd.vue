<template>
  <div>
    <h2 class="title">Add Scooter</h2>
    <div class="form">
      <!-- Store ID row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Store</label>
          <select v-model.number="newScooter.storeId" class="input custom-select">
            <option v-for="store in stores" :key="store.id" :value="store.id">{{ store.name }}</option>
          </select>
          <el-alert
              v-if="errors.storeId"
              :title="errors.storeId"
              type="error"
              :closable="false"
              show-icon
              class="custom-alert"
          />
        </div>
      </div>

      <!-- Price row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Price per Hour</label>
          <input v-model.number="newScooter.priceHour" type="number" step="0.01" min="0" placeholder="Enter the price per hour (price > 0)" class="input" />
          <el-alert
              v-if="errors.priceHour"
              :title="errors.priceHour"
              type="error"
              :closable="false"
              show-icon
              class="custom-alert"
          />
        </div>
        <div class="form-group col-12 col-md-6">
          <label>Price for Four Hours</label>
          <input v-model.number="newScooter.priceFourHour" type="number" step="0.01" min="0" placeholder="Enter the price for every 4 hours (price > 0)" class="input" />
          <el-alert
              v-if="errors.priceFourHour"
              :title="errors.priceFourHour"
              type="error"
              :closable="false"
              show-icon
              class="custom-alert"
          />
        </div>
      </div>

      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Price per Day</label>
          <input v-model.number="newScooter.priceDay" type="number" step="0.01" min="0" placeholder="Enter the daily price (price > 0)" class="input" />
          <el-alert
              v-if="errors.priceDay"
              :title="errors.priceDay"
              type="error"
              :closable="false"
              show-icon
              class="custom-alert"
          />
        </div>
        <div class="form-group col-12 col-md-6">
          <label>Price per Week</label>
          <input v-model.number="newScooter.priceWeek" type="number" step="0.01" min="0" placeholder="Enter the weekly price (price > 0)" class="input" />
          <el-alert
              v-if="errors.priceWeek"
              :title="errors.priceWeek"
              type="error"
              :closable="false"
              show-icon
              class="custom-alert"
          />
        </div>
      </div>

      <!-- Battery row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Battery</label>
          <input v-model.number="newScooter.battery" type="number" step="0.01" min="0" max="100" placeholder="Enter battery level (From 0 to 100)" class="input" />
          <el-alert
              v-if="errors.battery"
              :title="errors.battery"
              type="error"
              :closable="false"
              show-icon
              class="custom-alert"
          />
        </div>
      </div>

      <!-- Speed row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Speed</label>
          <input v-model.number="newScooter.speed" type="number" step="0.01" min="0" placeholder="Enter speed (speed > 0)" class="input" />
          <el-alert
              v-if="errors.speed"
              :title="errors.speed"
              type="error"
              :closable="false"
              show-icon
              class="custom-alert"
          />
        </div>
      </div>

      <!-- Error message -->
      <div class="form-row" v-if="hasError && errorMessage">
        <el-alert
            :title="errorMessage"
            type="error"
            :closable="false"
            show-icon
        />
      </div>

      <!-- Buttons row -->
      <div class="form-row">
        <div class="button-group">
          <button class="button button-reset" @click="resetForm">Reset</button>
          <button class="button button-submit" @click="addScooter">Add Scooter</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import {ElNotification} from "element-plus";

export default {
  data() {
    return {
      newScooter: {
        priceHour: null,
        priceFourHour: null,
        priceDay: null,
        priceWeek: null,
        battery: null,
        speed: null,
        storeId: null,
      },
      stores: [],
      errors: {},
      hasError: false,
      errorMessage: ''
    };
  },
  methods: {
    resetForm() {
      this.newScooter = {
        priceHour: null,
        priceFourHour: null,
        priceDay: null,
        priceWeek: null,
        battery: null,
        speed: null,
        storeId: null,
      };
      this.errors = {};
      this.hasError = false;
      this.errorMessage = '';
    },
    async fetchStores() {
      try {
        const res = await axios.get('http://localhost:8080/api/stores/getAll');
        this.stores = res.data;
        console.log(this.stores);
      } catch (error) {
        ElNotification({
          title: "Error",
          message: 'Failed to fetch stores',
          type: "error"
        });
      }
    },
    validateForm() {
      this.errors = {};
      this.hasError = false;
      this.errorMessage = '';

      // Validate required fields
      if (!this.newScooter.storeId) {
        this.errors.storeId = 'Store is required';
      }
      if (this.newScooter.priceHour === null || this.newScooter.priceHour === '') {
        this.errors.priceHour = 'Price per hour is required';
      } else if (this.newScooter.priceHour <= 0) {
        this.errors.priceHour = 'Price must be greater than 0';
      }

      if (this.newScooter.priceFourHour === null || this.newScooter.priceFourHour === '') {
        this.errors.priceFourHour = 'Price must be greater than 0';
      } else if (this.newScooter.priceFourHour <= 0) {
        this.errors.priceFourHour = 'Price must be greater than 0';
      }

      if (this.newScooter.priceDay === null || this.newScooter.priceDay === '') {
        this.errors.priceDay = 'Daily price is required';
      } else if (this.newScooter.priceDay <= 0) {
        this.errors.priceDay = 'Price must be greater than 0';
      }

      if (this.newScooter.priceWeek === null && this.newScooter.priceWeek === '') {
        this.errors.priceWeek = 'Price must be greater than 0';
      } else if (this.newScooter.priceWeek <= 0){
        this.errors.priceWeek = 'Price must be greater than 0';
      }

      if (this.newScooter.battery === null || this.newScooter.battery === '') {
        this.errors.battery = 'Battery level is required';
      } else if (this.newScooter.battery < 0 || this.newScooter.battery > 100) {
        this.errors.battery = 'Battery must be between 0 and 100';
      }

      if (this.newScooter.speed === null || this.newScooter.speed === '') {
        this.errors.speed = 'Speed is required';
      } else if (this.newScooter.speed <= 0) {
        this.errors.speed = 'Speed must be greater than 0';
      }

      this.hasError = Object.keys(this.errors).length > 0;
      if (this.hasError) {
        ElNotification({
          title: "Input Error",
          message: `Please fix the errors above`,
          type: "error"
        });
      }

      return !this.hasError;
    },
    async addScooter() {
      if (!this.validateForm()) {
        return;
      }

      try {
        const token = localStorage.getItem('token') || sessionStorage.getItem('token');
        if (!token) {
          this.errorMessage = 'Please login first';
          this.$router.push('/login');
          return;
        }

        const res = await axios.post('http://localhost:8080/api/scooters/add', this.newScooter, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (res.status === 200) {
          ElNotification({
            title: "Add Scooter Successfully",
            message: `New Scooter ID: ${res.data.scooterId}`,
            type: "success"
          });
          this.resetForm();
          this.$emit('scooter-added');
        }
      } catch (error) {
        if (error.response) {
          switch(error.response.status) {
            case 400:
              this.errors = error.response.data || {};
              if (error.response.data.message) {
                this.errorMessage = 'Error: ' + error.response.data.message;
              } else {
                this.errorMessage = this.errors;
              }
              break;
            case 401:
              localStorage.removeItem('token');
              sessionStorage.removeItem('token');
              this.errorMessage = 'Session expired. Please login again.';
              this.$router.push('/login');
              break;
            case 403:
              this.errorMessage = 'Forbidden: You do not have permission to perform this action';
              break;
            default:
              this.errorMessage = `Error: ${error.response.data?.message || 'Unknown error occurred'}`;
          }
        } else {
          this.errorMessage = 'Network error: Please check your connection';
        }
        this.hasError = true;
        console.error('Add scooter error:', error);
      }
    }
  },
  mounted() {
    this.fetchStores();
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
      this.errorMessage = 'Please login first';
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
.custom-select {
  display: flex;
  flex-wrap: wrap;
  height: auto;
}

.custom-select option {
  flex: 1 1 30%; /* Adjust the percentage based on your layout needs */
  margin: 5px;
}

.title {
  font-size: 28px;
  font-weight: bold;
  padding-left: 20px;
  padding-bottom: 20px;
  padding-top: 20px;
  border-bottom: 2px solid #003c51;
}
.form {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 20px 40px;
  border-radius: 12px;
  max-width: 100%;
}

.form-row {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  width: 100%;
}

.form-group {
  display: flex;
  flex-direction: column;
  flex: 1;
  margin-bottom: 10px;
}

label {
  margin-left: 5px;
  font-size: 22px;
  margin-bottom: 6px;
  color: #083435;
  font-weight: bold;
}

.input {
  height: 44px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 14px;
  margin-right: 1%;
}

.input:focus {
  outline: none;
  border-color: #003c51;
  box-shadow: 0 0 1px #003c51;
}

.button-group {
  display: flex;
  gap: 16px;
  margin-top: 10px;
}

.button {
  padding: 12px;
  border: none;
  font-size: 18px;
  border-radius: 25px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: bold;
  width: 150px;
}

.button-submit {
  background: #003c51;
  color: white;
}

.button-reset {
  background: white;
  color: #003c51;
  font-weight: bold;
  border: 3px solid #003c51;
}

.button-reset:hover {
  background: #f4f4f4;
}

.checkbox-options {
  display: flex;
  gap: 40px; /* 控制两个选项之间的距离 */
  align-items: center; /* 垂直居中 */
  margin-top: 8px;
}

.checkbox-option {
  display: flex;
  align-items: center; /* 垂直居中 */
  gap: 4px; /* 勾选框和文字的间距 */
  cursor: pointer;
  font-size: 18px;
  position: relative;
}

.checkbox-input {
  position: absolute;
  opacity: 0;
  cursor: pointer;
  height: 0;
  width: 0;
}

.checkmark {
  display: inline-block;
  height: 20px;
  width: 20px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  flex-shrink: 0;
  position: relative;
}

.checkbox-option:hover .checkbox-input ~ .checkmark {
  background-color: #f5f5f5;
}

.checkbox-option .checkbox-input:checked ~ .checkmark {
  background-color: #003c51;
  border-color: #003c51;
}

.checkmark:after {
  content: "";
  position: absolute;
  display: none;
}

.checkbox-option .checkbox-input:checked ~ .checkmark:after {
  display: block;
}

.checkbox-option .checkmark:after {
  left: 7px;
  top: 3px;
  width: 5px;
  height: 10px;
  border: solid white;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
}

.checkbox-label {
  margin-left: 8px;
}

.custom-alert {
  font-size: 16px;
  width: 99%;
  margin-top: 3px;
  height: 30px;
}

@media (max-width: 767px) {
  .form-row {
    flex-direction: column;
  }

  .form-group {
    width: 100%;
  }
}

@media (max-width: 767px) {
  .button-group {
    flex-direction: column;
  }

  .button {
    width: 100%;
  }
}
</style>