<template>
  <div>
    <h2 class="title">Add Scooter</h2>
    <div class="form">
      <!-- Location row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Location</label>
          <input v-model="newScooter.location" placeholder="Enter location" class="input" />
        </div>
      </div>

      <!-- Price row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Price per Hour</label>
          <input v-model.number="newScooter.priceHour" type="number" step="0.01" placeholder="Enter the price per hour" class="input" />
        </div>
        <div class="form-group col-12 col-md-6">
          <label>Price for Four Hours</label>
          <input v-model.number="newScooter.priceFourHour" type="number" step="0.01" placeholder="Enter the price for every 4 hours" class="input" />
        </div>
      </div>

      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Price per Day</label>
          <input v-model.number="newScooter.priceDay" type="number" step="0.01" placeholder="Enter the daily price" class="input" />
        </div>
        <div class="form-group col-12 col-md-6">
          <label>Price per Week</label>
          <input v-model.number="newScooter.priceWeek" type="number" step="0.01" placeholder="Enter the weekly price" class="input" />
        </div>
      </div>

      <!-- Coordinates row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Longitude</label>
          <input v-model.number="newScooter.longitude" type="number" step="0.000001" placeholder="Enter the scooter longitude" class="input" />
        </div>
        <div class="form-group col-12 col-md-6">
          <label>Latitude</label>
          <input v-model.number="newScooter.latitude" type="number" step="0.000001" placeholder="Enter the scooter latitude" class="input" />
        </div>
      </div>

      <!-- Status row -->
      <div class="form-row">
        <div class="form-group col-6 col-md-6">
          <label>Status</label>
          <div class="checkbox-options">
            <label class="checkbox-option">
              <input
                  type="checkbox"
                  v-model="newScooter.status"
                  :true-value="1"
                  :false-value="0"
                  class="checkbox-input"
              >
              <span class="checkmark"></span>
              <span class="checkbox-label">Active</span>
            </label>
            <label class="checkbox-option">
              <input
                  type="checkbox"
                  v-model="newScooter.status"
                  :true-value="0"
                  :false-value="1"
                  class="checkbox-input"
              >
              <span class="checkmark"></span>
              <span class="checkbox-label">Inactive</span>
            </label>
          </div>
        </div>
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
    resetForm() {
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
    },
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
  font-size: 28px;
  font-weight: bold;
  padding-left: 20px;
  padding-bottom: 20px;
  padding-top: 20px;
  border-bottom: 2px solid #58c4c9;
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
  border-color: #58c4c9;
  box-shadow: 0 0 1px #58c4c9;
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
  background: #58c4c9;
  color: white;
}

.button-submit:hover {
  background: #3bb7bd;
}

.button-reset {
  background: white;
  color: #58c4c9;
  font-weight: bold;
  border: 3px solid #58c4c9;
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
  background-color: #58c4c9;
  border-color: #58c4c9;
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
