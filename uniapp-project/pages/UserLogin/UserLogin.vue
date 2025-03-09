<template>
	<view class="page">
		<view class="header">
			<text>Welcome to E-Scooter Rental System</text>
		</view>
		<view class="container">
			<view class="title">
				<text v-if="!isCodeLogin">Password Login</text>
			</view>
			<form>
				<view class="form-group">
					<input type="tel" v-model="username" placeholder="Please enter your username" class="input-field" />
				</view>
				<view v-if="!isCodeLogin" class="form-group">
					<input type="password" v-model="password" placeholder="Please enter your password" class="input-field" />
				</view>
				<view class="login-btn" @click="handleSubmit()">Login</view>
			</form>
			<view v-if="!isCodeLogin" class="form-group terms">
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
      isCodeLogin: false,
      send_count_down: 0, // 验证码重新发送等待倒计时
      token: null, // 存储登录后获取的 token
      errorMessage: ''
    };
  },
  methods: {
    handleSubmit() {
      if (!this.username) {
        this.toast('请输入手机号');
        return;
      }
      if (!this.isCodeLogin && !this.password) {
        this.toast('请输入密码');
        return;
      }
      if (this.isCodeLogin && !this.code) {
        this.toast('请输入验证码');
        return;
      }

      // 如果是验证码登录
      if (this.isCodeLogin) {
        // 这里你可以根据需求补充验证码登录的逻辑
        this.toast('验证码登录暂未实现');
        return;
      }

      // 密码登录
      this.loginWithPassword();
    },
    loginWithPassword() {
      uni.request({
        url: 'http://localhost:8080/api/auth/login', // 后端登录接口
        method: 'POST',
        data: {
          username: this.username,
          password: this.password
        },
        success: (res) => {
          if (res.statusCode === 200) {
            this.token = res.data.token; // 保存 token
            uni.setStorageSync('token', this.token); // 本地存储 token
            this.toast('login successfully');
            uni.switchTab({
              url: '/pages/index/index' // 跳转到主页
            });
          } else {
            this.toast('登录失败: ' + res.data.message);
          }
        },
        fail: (err) => {
          this.toast('登录请求失败');
        }
      });
    },
    sendCode() {
      if (this.send_count_down === 0) {
        let countDown = 60;
        this.send_count_down = countDown;
        let timer = setInterval(() => {
          countDown--;
          this.send_count_down = countDown;
          if (countDown === 0) {
            clearInterval(timer);
          }
        }, 1000);
      }
    },
    toast(msg) {
      uni.showToast({
        icon: 'none',
        title: msg
      });
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