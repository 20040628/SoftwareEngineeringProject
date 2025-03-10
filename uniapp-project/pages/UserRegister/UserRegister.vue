<template>
	<view class="page">
		<view class="header">
			<text>Welcome to E-Scooter Rental System</text>
		</view>
		<view class="container">
			<view class="title">
				<text v-if="!isCodeLogin">Register</text>
			</view>
			<form>
				<view class="form-group">
				  <label>Username</label>
				  <input v-model="form.username" type="text" placeholder="Enter username" class="input-field" />
				  <span v-if="errors.username" class="error-message">{{ errors.username }}</span>
				</view>
				
				<view class="form-group">
				  <label>Password</label>
				  <input v-model="form.password" type="password" placeholder="Enter password" class="input-field"/>
				  <span v-if="errors.password" class="error-message">{{ errors.password }}</span>
				</view>
				
				<view class="form-group">
				  <label>Email</label>
				  <input v-model="form.email" type="email" placeholder="Enter email" class="input-field"/>
				  <span v-if="errors.email" class="error-message">{{ errors.email }}</span>
				</view>
				
				<view class="form-group">
				  <label>Mobile</label>
				  <input v-model="form.mobile" type="text" placeholder="Enter mobile number" class="input-field"/>
				  <span v-if="errors.mobile" class="error-message">{{ errors.mobile }}</span>
				</view>
				
				<view class="form-group">
				  <label>Birthday</label>
				  <picker mode="date" :value="form.birthday" @change="getBirthday">
				  	 <view class="picker">{{ form.birthday || 'Please select a start date' }}</view>
				  </picker>
				  <span v-if="errors.birthday" class="error-message">{{ errors.birthday }}</span>
				</view>
				<!-- <view class="form-group">
					<input type="tel" v-model="username" placeholder="Please enter your username" class="input-field" />
				</view> -->
			</form>
			<div class="form-group">
			  <button @click="register" :disabled="loading" class="login-btn">
			    {{ loading ? 'Registering...' : 'Register' }}
			  </button>
			</div>
			
			<div v-if="message" :class="['message', messageType]">
			  <p>{{ message }}</p>
			  <template v-if="registeredUser">
			    <p>User ID: {{ registeredUser.userId }}</p>
			    <p>Username: {{ registeredUser.username }}</p>
			  </template>
			</div>
			
			<div class="form-group">
			  <button @click="goToLogin" class="login-btn">
			    Go to Login
			  </button>
			</div>
		</view>
	</view>
</template>





<script>
export default {
  name: 'UserRegister',
  data() {
    return {
      form: {
        username: '',
        password: '',
        email: '',
        mobile: '',
        birthday: '',
        userType: 0,  // default: normal user
      },
      loading: false,
      message: '',
      messageType: 'success',
      registeredUser: null,
      errors: {}
    };
  },
  methods: {
    validate() {
      this.errors = {};

      // Username validation: 3-20 characters, letters, numbers, and underscores only
      const usernameRegex = /^[a-zA-Z0-9_]{3,20}$/;
      if (!this.form.username) {
        this.errors.username = 'Username is required';
      } else if (!usernameRegex.test(this.form.username)) {
        this.errors.username = 'Username must be 3-20 characters and can only contain letters, numbers, and underscores';
      }

      // Password validation: at least 8 characters, containing uppercase, lowercase, and number
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
      if (!this.form.password) {
        this.errors.password = 'Password is required';
      } else if (!passwordRegex.test(this.form.password)) {
        this.errors.password = 'Password must be at least 8 characters and include at least one uppercase letter, one lowercase letter, and one number';
      }

      // Email validation: valid email format
      const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      if (!this.form.email) {
        this.errors.email = 'Email is required';
      } else if (!emailRegex.test(this.form.email)) {
        this.errors.email = 'Please enter a valid email address';
      }

      // Mobile number validation: 10-13 digits
      const mobileRegex = /^[0-9]{10,13}$/;
      if (!this.form.mobile) {
        this.errors.mobile = 'Mobile number is required';
      } else if (!mobileRegex.test(this.form.mobile)) {
        this.errors.mobile = 'Mobile number must be between 10-13 digits';
      }

      // Birthday validation: valid date format and must be in the past
      const today = new Date();
      const birthdayDate = new Date(this.form.birthday);
      if (!this.form.birthday) {
        this.errors.birthday = 'Birthday is required';
      } else if (birthdayDate >= today) {
        this.errors.birthday = 'Birthday must be in the past';
      }

      return Object.keys(this.errors).length === 0;
    },
    async register() {
      this.loading = true;
      this.message = '';
      this.registeredUser = null;

      // Validate form before submission
      if (!this.validate()) {
        this.message = 'Please correct the errors below';
        this.messageType = 'error';
        this.loading = false;
        return;
      }

      try {
        // Use uni.request instead of axios
        const response = await new Promise((resolve, reject) => {
          uni.request({
            url: 'http://localhost:8080/api/auth/register', // replace with your actual URL
            method: 'POST',
            data: {
              ...this.form,
              birthday: new Date(this.form.birthday)
            },
            success: (res) => {
              if (res.statusCode === 200) {
                resolve(res.data);  // Resolving with response data
              } else {
                reject(new Error('Request failed with status ' + res.statusCode));
              }
            },
            fail: (err) => {
              reject(err);  // Rejecting on failure
            }
          });
        });

        // Handle successful registration
        this.message = response.message || 'Registration successful';
        this.messageType = 'success';
        this.registeredUser = {
          userId: response.userId,
          username: response.username
        };

        // Clear form
        this.form = {
          username: '',
          password: '',
          email: '',
          mobile: '',
          birthday: '',
          userType: 0,
        };
      } catch (error) {
        // Handle errors
        if (error.response?.data && typeof error.response.data === 'object') {
          // Handle validation errors
          this.errors = error.response.data;
          this.message = 'Please correct the errors below';
        } else {
          // Handle other errors
          this.message = error.message || 'Registration failed';
        }
        this.messageType = 'error';
      } finally {
        this.loading = false;
      }
    },
    goToLogin() {
      uni.navigateTo({
        url: '/pages/index/index' 
      });
    }
  }
};
</script>

<style scoped lang="scss">
	@import '../index.scss';
</style>
<style scoped lang="scss">
	.error-message{
		color:red;
	}
	.picker {
		padding: 10rpx;
		border: 2rpx solid darkgrey;
		border-radius: 10rpx;
		text-align: center;
		margin-bottom: 10rpx;
		}
</style>
