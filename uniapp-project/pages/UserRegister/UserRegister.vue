<template>
	<view class="page">
		<view class="header">
			<text>Welcome to E-Scooter Rental System</text>
		</view>
		<view class="container">
			<view class="title">
				<text>Register</text>
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
				  <picker mode="date":value="form.birthday" @change="getBirthday">
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
	  
	getBirthday(e){
		this.form.birthday=e.detail.value.split("T")[0];
	},
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
   
     // 验证表单
     if (!this.validate()) {
       this.message = 'Please correct the errors below';
       this.messageType = 'error';
       this.loading = false;
       return;
     }
   
     try {
       // 发送注册请求
       const [err, res] = await uni.request({
         url: 'http://localhost:8080/api/auth/register', // 后端地址
         method: 'POST',
         data: this.form,
         header: { 'Content-Type': 'application/json' }
       }).then(res => [null, res]).catch(err => [err, null]); // 兼容 `try-catch` 方式
   
        if (err || res.statusCode !== 200) {
             // 这里处理后端返回的错误信息
             let errorMessage = 'Registration failed';
             
             if (res?.data) {
				if (typeof res.data === 'string') {
				  // 如果后端直接返回字符串
				  errorMessage = res.data;
				} else if (typeof res.data === 'object' && res.data.message) {
				  // 如果后端返回 JSON 对象
				  errorMessage = res.data.message;
				}
			  } else if (err) {
				errorMessage = err.message || 'Network error, please try again';
			  }
	
             this.message = errorMessage;
             this.messageType = 'error';
       
             uni.showToast({
               title: this.message,
               icon: 'none',
               duration: 2000
             });
       
             throw new Error(errorMessage);
           }
       
           // 显示成功提示
           uni.showToast({
             title: 'Registration successful!',
             icon: 'success',
             duration: 2000
           });
       
           // 注册成功后清空表单
           this.form = {
             username: '',
             password: '',
             email: '',
             mobile: '',
             birthday: '',
             userType: 0,
           };
       
           // 延迟跳转到登录页面
           setTimeout(() => {
             this.goToLogin();
           }, 2000);
       
         } catch (error) {
           console.error('Registration error:', error);
         } finally {
           this.loading = false;
         }
       },
	   
	   goToLogin() {
		 uni.navigateTo({
		   url: '/pages/UserLogin/UserLogin'
		 });
	   }
   }
}
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
