<template>
	<view class="page">
		<view class="header">
			<text>E-scooter management system</text>
		</view>
		<view class="container">
			<view class="title">
				<text v-if="!isCodeLogin">Administrator Login</text>
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
		</view>
	</view>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      password: '',
      isCodeLogin: false,
      send_count_down: 0, // 验证码重新发送等待倒计时
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
            this.token = res.data.token; 
            uni.setStorageSync('user', {
              username: res.data.username, 
              token: this.token 
            });

            this.toast('Successfully logged in');

            // 判断是否为管理员
            if (this.username === 'admin' && this.password === 'Admin123') {
              // 如果是管理员，跳转到管理员界面
              uni.navigateTo({
                url: '/pages/admin/adminDashboard' // 假设管理员界面的路径是 /pages/admin/adminDashboard
              });
            } else {
              // 普通用户跳转到用户个人中心
              uni.navigateTo({
                url: '/pages/userindex/userIndex' // 普通用户个人中心路径
              });
            }
          } else {
            this.toast('Fail to login: ' + res.data.message);
          }
        },
        fail: (err) => {
          this.toast('Login request failed');
        }
      });
    },
    toast(msg) {
      uni.showToast({
        icon: 'none',
        title: msg
      });
    }
  }
};
</script>

<style scoped lang="scss">
	@import 'adminLogin.scss';
</style>
