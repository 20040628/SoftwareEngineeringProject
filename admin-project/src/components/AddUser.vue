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
          <el-alert
              v-if="errors.username"
              :title="errors.username"
              type="error"
              :closable="false"
              show-icon
              class="custom-alert"
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
          <el-alert
              v-if="errors.password"
              :title="errors.password"
              type="error"
              :closable="false"
              show-icon
              class="custom-alert"
          />
        </div>
      </div>

      <!-- Bank Card row -->
      <div class="form-row">
        <div class="form-group col-12 col-md-6">
          <label>Bank Card</label>
          <input
              v-model="newUser.bankCard"
              type="text"
              minlength="16"
              maxlength="19"
              placeholder="16-19 digits"
              class="input"
          />
          <el-alert
              v-if="errors.bankCard"
              :title="errors.bankCard"
              type="error"
              :closable="false"
              show-icon
              class="custom-alert"
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
          <el-alert
              v-if="errors.email"
              :title="errors.email"
              type="error"
              :closable="false"
              show-icon
              class="custom-alert"
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
          <el-alert
              v-if="errors.mobile"
              :title="errors.mobile"
              type="error"
              :closable="false"
              show-icon
              class="custom-alert"
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
          <el-alert
              v-if="errors.birthday"
              :title="errors.birthday"
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
          <button class="button button-submit" @click="registerUser">Add User</button>
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
      newUser: {
        username: '',
        password: '',
        email: '',
        mobile: '',
        birthday: '',
        bankCard: ''
      },
      errors: {},
      hasError: false,
      errorMessage: '',
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
        birthday: '',
        bankCard: ''
      };
      this.errors = {};
      this.hasError = false;
      this.errorMessage = '';
      this.successMessage = '';
    },
    async registerUser() {
      // Reset previous errors
      this.errors = {};
      this.hasError = false;
      this.errorMessage = '';
      this.successMessage = '';

      // Field validation
      if (!this.newUser.username) {
        this.errors.username = 'Username is required';
      } else if (this.newUser.username.length < 3 || this.newUser.username.length > 20) {
        this.errors.username = 'Username must be 3-20 characters';
      }

      if (!this.newUser.password) {
        this.errors.password = 'Password is required';
      } else if (this.newUser.password.length < 8) {
        this.errors.password = 'Password must be at least 8 characters';
      }

      if (!this.newUser.bankCard) {
        this.errors.bankCard = 'Bank card is required';
      } else if (!/^\d{16,19}$/.test(this.newUser.bankCard)) {
        this.errors.bankCard = 'Bank card must be 16-19 digits';
      }

      if (!this.newUser.email) {
        this.errors.email = 'Email is required';
      } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.newUser.email)) {
        this.errors.email = 'Invalid email format';
      }

      if (!this.newUser.mobile) {
        this.errors.mobile = 'Mobile number is required';
      } else if (!/^\d{10,13}$/.test(this.newUser.mobile)) {
        this.errors.mobile = 'Mobile must be 10-13 digits';
      }

      if (!this.newUser.birthday) {
        this.errors.birthday = 'Birthday is required';
      }

      if (Object.keys(this.errors).length > 0) {
        this.hasError = true;
        ElNotification({
          title: "Input Error",
          message: `Please fix the errors above`,
          type: "error"
        });
        return;
      }

      try {
        const token = localStorage.getItem('token') || sessionStorage.getItem('token');
        if (!token) {
          this.hasError = true;
          this.errorMessage = 'Please login as admin first';
          return;
        }

        const res = await axios.post('http://localhost:8080/api/auth/register', this.newUser, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (res.status === 200) {
          this.registrationResult = res.data;
          this.resetForm();
          ElNotification({
            title: "Add Successfully",
            message: `User: ${this.newUser.username}`,
            type: "success"
          });
        }
      } catch (error) {
        if (error.response) {
          switch(error.response.status) {
            case 400:
              // Handle server-side validation errors
              const serverErrors = error.response.data;
              this.errors = serverErrors;
              this.errorMessage = 'Please correct the validation errors';
              break;
            case 401:
              this.errorMessage = 'Unauthorized: Only admins can register users';
              break;
            case 403:
              this.errorMessage = 'Forbidden: You do not have permission';
              break;
            default:
              this.errorMessage = `Error: ${error.response.data?.message || 'Unknown error occurred'}`;
          }
        } else {
          this.errorMessage = 'Network error: Please check your connection';
        }
        this.hasError = true;
        console.error('Registration error:', error);
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

.custom-alert {
  font-size: 16px;
  width: 99%;
  margin-top: 3px;
  height: 30px;
}

.error-message {
  color: #f56c6c;
  font-size: 16px;
  margin-top: 10px;
  padding: 10px;
  background-color: #fef0f0;
  border-radius: 4px;
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