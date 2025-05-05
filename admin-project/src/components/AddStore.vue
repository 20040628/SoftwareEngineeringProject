<template>
  <div>
    <h2 class="title">Add New Store</h2>
    <div class="form">
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Store Name</label>
          <input
              v-model="storeData.name"
              type="text"
              placeholder="Enter store name"
              class="input"
          />
          <span class="error-message" v-if="errors.name">{{ errors.name }}</span>
        </div>
      </div>
      <!-- Latitude row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Latitude</label>
          <input
              v-model="storeData.latitude"
              type="number"
              step="0.000001"
              placeholder="Enter latitude"
              class="input"
          />
          <span class="error-message" v-if="errors.latitude">{{ errors.latitude }}</span>
        </div>
      </div>

      <!-- Longitude row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Longitude</label>
          <input
              v-model="storeData.longitude"
              type="number"
              step="0.000001"
              placeholder="Enter longitude"
              class="input"
          />
          <span class="error-message" v-if="errors.longitude">{{ errors.longitude }}</span>
        </div>
      </div>

      <!-- Buttons row -->
      <div class="form-row">
        <div class="button-group">
          <button class="button button-reset" @click="resetForm">Reset</button>
          <button class="button button-submit" @click="addStore">Add Store</button>
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
      storeData: {
        name: '',
        latitude: '',
        longitude: '',
      },
      errors: {},
    };
  },
  methods: {
    resetForm() {
      this.storeData = {
        name: '',
        latitude: '',
        longitude: '',
      };
      this.errors = {};
    },
    async addStore() {
      // Reset errors
      this.errors = {};

      if (!this.storeData.name) {
        this.errors.name = 'Store name is required';
      }
      // Basic validation
      if (!this.storeData.latitude) {
        this.errors.latitude = 'Latitude is required';
      }
      if (!this.storeData.longitude) {
        this.errors.longitude = 'Longitude is required';
      }

      if (Object.keys(this.errors).length > 0) {
        alert('Please fill in all required fields');
        return;
      }

      try {
        // Get token from localStorage or sessionStorage
        const token = localStorage.getItem('token') || sessionStorage.getItem('token');
        if (!token) {
          alert('Please login first');
          this.$router.push('/login');
          return;
        }

        // Prepare request data
        const payload = {
          name: this.storeData.name,
          latitude: parseFloat(this.storeData.latitude),
          longitude: parseFloat(this.storeData.longitude)
        };

        // Send request
        const res = await axios.post('/api/stores/add', payload, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (res.status === 200) {
          alert('Store added successfully!\nStore ID: ' + res.data.storeId);
          this.resetForm();
        }
      } catch (error) {
        if (error.response) {
          // Handle different error status codes
          switch(error.response.status) {
            case 400:
              this.errors = error.response.data || {};
              if (error.response.data.message) {
                alert('Error: ' + error.response.data.message);
              } else if (error.response.data.name) {
                alert('Error: ' + error.response.data.name);
              } else if (error.response.data.latitude) {
                alert('Error: ' + error.response.data.latitude);
              } else if (error.response.data.longitude) {
                alert('Error: ' + error.response.data.longitude);
              } else {
                alert('Invalid data provided');
              }
              break;
            case 401:
              localStorage.removeItem('token');
              sessionStorage.removeItem('token');
              this.$store.commit('logout');
              alert('Session expired. Please login again.');
              this.$router.push('/login');
              break;
            case 403:
              alert('Forbidden: You do not have permission to perform this action');
              break;
            default:
              alert(`Error: ${error.response.data?.message || 'Unknown error occurred'}`);
          }
        } else {
          alert('Network error: Please check your connection');
        }
        console.error('Add store error:', error);
      }
    }
  },
  mounted() {
    // Check login status when component loads
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
      alert('Please login first');
      this.$router.push('/login');
    }
  }
};
</script>

<style scoped>
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

.button-submit:hover {
  background: #003c51;
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

.error-message {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 4px;
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