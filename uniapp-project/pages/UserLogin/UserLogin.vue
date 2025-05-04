<template>
	<view class="page">
		<view class="header">
			<text>Welcome to E-Scooter Rental System</text>
		</view>
		<view class="container">
			<view class="title">
				<text>Login</text>
			</view>
			<form>
				<view class="form-group">
					<input type="tel" v-model="username" placeholder="Please enter your username" class="input-field" />
				</view>
				<view class="form-group">
					<input type="password" v-model="password" placeholder="Please enter your password" class="input-field" />
				</view>
				<!-- 验证码区域 -->
				<view class="form-group captcha-group">
					<input type="text" v-model="captcha" placeholder="Enter captcha" class="input-field captcha-input" />
					<view class="captcha-image" @click="refreshCaptcha">
						<image v-if="captchaImage" :src="captchaImage" mode="aspectFit"></image>
						<view v-else class="loading">Loading...</view>
					</view>
				</view>
				<view class="login-btn" @click="handleSubmit()">Login</view>
			</form>
			<view class="form-group terms">
			    <checkbox-group @change="checkboxChange">
					<checkbox value="cb" color="#000000" style="transform:scale(0.8)">I have read and agreed &nbsp;
						<a href="javascript:void(0);" @click="goToTerms">Terms</a>
					</checkbox>
			    </checkbox-group>
			</view>
			<view class="form-group terms">
				Click here to <text @click="goToRegister()">Register</text>
			</view>
			<view class="form-group terms">
				Forget the password? Click here to <text @click="find()">find password</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      password: '',
      captcha: '',
      captchaKey: '',
      captchaImage: '',
      token: null, 
      errorMessage: '',
	  agreementChecked: false
    };
  },
  onLoad() {
    // 页面加载时获取验证码
    this.getCaptcha();
  },

  methods: {
    // 获取验证码
    async getCaptcha() {
      try {
        const [err, res] = await uni.request({
          url: `${this.$baseURL}/api/auth/captcha`,
          method: 'GET',
          header: { 'Content-Type': 'application/json' }
        }).then(res => [null, res]).catch(err => [err, null]);
        
        if (err || res.statusCode !== 200) {
          uni.showToast({
            title: 'Failed to get captcha',
            icon: 'none',
            duration: 2000
          });
          return;
        }
        
        this.captchaKey = res.data.captchaKey;
        this.captchaImage = res.data.captchaImageBase64;
      } catch (error) {
        console.error('Error getting captcha:', error);
      }
    },
    
    // 刷新验证码
    refreshCaptcha() {
      this.captchaImage = '';
      this.captcha = '';
      this.getCaptcha();
    },
    
    handleSubmit() {
      if (!this.username) {
        this.toast('Please enter your username');
        return;
      }
      if (!this.password) {
        this.toast('Please enter your password');
        return;
      }
      if (!this.captcha) {
		this.toast('Please enter captcha');
        return;
      }
	  if(!this.agreementChecked){
		  this.toast('Please agree first');
		  return;
	  }
      // 密码登录
      this.loginWithPassword();
    },
    
    toast(message) {
      uni.showToast({
        title: message,
        icon: 'none',
        duration: 2000
      });
    },
	checkboxChange(e){
		this.agreementChecked = e.detail.value.includes('cb');
	},
    
    async loginWithPassword() {
      this.loading = true;
      this.message = '';
    
      try {
        // 发送登录请求
        const [err, res] = await uni.request({
          url: `${this.$baseURL}/api/auth/login`, // 后端登录接口
          method: 'POST',
          data: {
            username: this.username,
            password: this.password,
            captcha: this.captcha,
            captchaKey: this.captchaKey
          },
          header: { 'Content-Type': 'application/json' }
        }).then(res => [null, res]).catch(err => [err, null]);
    
        // 无论成功失败，都刷新验证码
        this.refreshCaptcha();
        if (err || res.statusCode !== 200) {
          // 处理后端错误
          let errorMessage = 'Login failed';
    
          if (res?.data) {
            if (typeof res.data === 'string') {
              // 后端直接返回字符串
              errorMessage = res.data;
            } else if (typeof res.data === 'object') {
              // 处理验证码错误
              if (res.data.captcha) {
                errorMessage = res.data.captcha;
              } else if (res.data.message) {
                errorMessage = res.data.message;
              }
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
    
        // 处理成功逻辑
        this.token = res.data.token; // 保存 token
        uni.setStorageSync('token', this.token); // 本地存储 token
    
        uni.showToast({
          title: 'Login successful!',
          duration: 2000
        });
		// this.$refs.toast.showToast('Login successful!');
    
        // 延迟跳转到主页
        setTimeout(() => {
          uni.navigateTo({
            url: '/pages/index'
          });
        }, 2000);
    
      } catch (error) {
        console.error('Login error:', error);
      } finally {
        this.loading = false;
      }
    },
    
    goToRegister() {
      uni.navigateTo({
        url: '/pages/UserRegister/UserRegister' // 跳转到注册页面
      });
    },
	find(){
	 uni.navigateTo({
	   url: '/pages/information/findPassword/findPassword' // 跳转到注册页面
	 });
	},
	goToTerms() {
		uni.navigateTo({
			url: '/pages/information/terms/terms' 
	    });
	}
  }
};
</script>

<style scoped lang="scss">
	@import '../index.scss';
	
	.captcha-group {
		display: flex;
		align-items: center;
	}
	
	.captcha-input {
		flex: 1;
		margin-right: 10px;
	}
	
	.captcha-image {
		width: 130px;
		height: 48px;
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: #f5f5f5;
		border-radius: 4px;
	}
	
	.captcha-image image {
		width: 100%;
		height: 100%;
	}
	
	.loading {
		font-size: 12px;
		color: #999;
	}
</style>