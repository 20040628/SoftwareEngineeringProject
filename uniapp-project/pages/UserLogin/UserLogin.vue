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
				<view class="login-btn" @click="handleSubmit()">Login</view>
			</form>
			<view class="form-group terms">
				Click here to <text @click="goToRegister">Register</text>
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
      code: '',
      token: null, // 存储登录后获取的 token
      errorMessage: ''
    };
  },
  methods: {
    handleSubmit() {
      if (!this.username) {
        this.toast('Please enter your username');
        return;
      }
      if (!this.isCodeLogin && !this.password) {
        this.toast('Please enter your password');
        return;
      }
      // 密码登录
      this.loginWithPassword();
    },
    async loginWithPassword() {
      this.loading = true;
      this.message = '';
    
      try {
        // 发送登录请求
        const [err, res] = await uni.request({
          url: 'http://localhost:8080/api/auth/login', // 后端登录接口
          method: 'POST',
          data: {
            username: this.username,
            password: this.password
          },
          header: { 'Content-Type': 'application/json' }
        }).then(res => [null, res]).catch(err => [err, null]); // 兼容 `try-catch`
    
        if (err || res.statusCode !== 200) {
          // 处理后端错误
          let errorMessage = 'Login failed';
    
          if (res?.data) {
            if (typeof res.data === 'string') {
              // 后端直接返回字符串
              errorMessage = res.data;
            } else if (typeof res.data === 'object' && res.data.message) {
              // 后端返回 JSON 对象
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
    
        // 处理成功逻辑
        this.token = res.data.token; // 保存 token
        uni.setStorageSync('token', this.token); // 本地存储 token
    
        uni.showToast({
          title: 'Login successful!',
          icon: 'success',
          duration: 2000
        });
    
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
    }
  }
};
</script>




<style scoped lang="scss">
	@import '../index.scss';
</style>