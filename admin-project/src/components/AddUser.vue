<template>
  <div>
    <h2 class="title">Register New User</h2>
    <div class="form">
      <!-- Username row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Username</label>
          <input
              v-model="newUser.username"
              minlength="3"
              maxlength="20"
              placeholder="3-20 characters (letters, numbers, underscore)"
              class="input"
          />
        </div>
      </div>

      <!-- Password row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Password</label>
          <input
              v-model="newUser.password"
              type="password"
              minlength="8"
              placeholder="Min 8 chars (uppercase, lowercase, number)"
              class="input"
          />
        </div>
      </div>

      <!-- Email row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Email</label>
          <input
              v-model="newUser.email"
              type="email"
              placeholder="Valid email format"
              class="input"
          />
        </div>
      </div>

      <!-- Mobile row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Mobile Number</label>
          <input
              v-model="newUser.mobile"
              type="tel"
              minlength="10"
              maxlength="13"
              placeholder="10-13 digits"
              class="input"
          />
        </div>
      </div>

      <!-- Birthday row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Birthday</label>
          <input
              v-model="newUser.birthday"
              type="date"
              class="input"
              max="2023-12-31"
          />
        </div>
      </div>

      <!-- Buttons row -->
      <div class="form-row">
        <div class="button-group">
          <button class="button button-reset" @click="resetForm">Reset</button>
          <button class="button button-submit" @click="registerUser">Add User</button>
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
      newUser: {
        username: '',
        password: '',
        email: '',
        mobile: '',
        birthday: ''
      },
      successMessage: '',
      registrationResult: null
    };
  },
  methods: {
    resetForm() {
      this.newUser = {
        username: '',
        password: '',
        email: '',
        mobile: '',
        birthday: ''
      };
    },
    async registerUser() {
      // Reset previous messages
      this.successMessage = '';
      this.registrationResult = null;

      // Collect validation errors
      const errors = [];

      if (!this.newUser.username) {
        errors.push('Username is required');
      }
      if (!this.newUser.password) {
        errors.push('Password is required');
      }
      if (!this.newUser.email) {
        errors.push('Email is required');
      }
      if (!this.newUser.mobile) {
        errors.push('Mobile number is required');
      }
      if (!this.newUser.birthday) {
        errors.push('Birthday is required');
      }

      // Show all errors in one alert
      if (errors.length > 0) {
        alert("Please fix the following errors:\n\n" + errors.join('\n'));
        return;
      }

      try {
        const token = localStorage.getItem('token') || sessionStorage.getItem('token');
        if (!token) {
          alert('Please login as admin first');
          return;
        }

        const res = await axios.post('http://localhost:8080/api/auth/register', this.newUser, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (res.status === 200) {
          this.successMessage = 'User registered successfully';
          this.registrationResult = res.data;
          this.newUser = {
            username: '',
            password: '',
            email: '',
            mobile: '',
            birthday: ''
          };
          alert("Add User successfully");
        }
      } catch (error) {
        if (error.response && error.response.status === 400) {
          // Handle validation errors from server
          const serverErrors = error.response.data;
          const errorMessages = Object.values(serverErrors).flat();
          alert("Validation errors:\n" + errorMessages.join(''));
        } else if (error.response && error.response.status === 401) {
          alert('Unauthorized: Only admins can register users');
        } else {
          alert('Failed to register user: ' + (error.response?.data?.message || error.message));
        }
      }
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

.success-message {
  color: #67c23a;
  font-weight: bold;
  margin-top: 16px;
  padding: 12px;
  border-radius: 8px;
  background-color: #f0f9eb;
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