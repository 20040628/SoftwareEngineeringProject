<template>
	<view class="page">
		<view class="header">
			<text>Welcome to ScootGo</text>
		</view>
		<view class="container">
			<view class="title">
				<text>Find your Password</text>
			</view>
			<view v-if="!codeSent" >
			<form>
				<view class="form-group">
					<input type="tel" v-model="email" placeholder="Please enter your email" class="input-field" />
				</view>
				<view class="login-btn" @click="sendCode()">Send</view>
			</form>
			</view>
			<view v-if="codeSent" class="reset-section">
				<view class="form-group">
					<input type="tel" v-model="resetCode" placeholder="Please enter the verification code received" class="input-field" />
				</view>
				<view class="form-group">
					<input type="password" v-model="newPassword" placeholder="Please set a new password" class="input-field" />
				</view>
				<view class="form-group">
					<input type="password" v-model="confirmPassword" placeholder="Please enter the new password again" class="input-field" />
				</view>
				<view class="login-btn" @click="resetPassword()">Reset</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
	  data() {
	    return {
			email:'',
			codeSent:false,
			resetLoading:false
	    };
	  },
	  methods: {
		async sendCode(){
					const token = String(uni.getStorageSync('token'));
					try {
						uni.showLoading({ title: "Loading...", mask: true });
						const res = await uni.request({
					      url: `${this.$baseURL}/api/auth/forgot-password`,
					      method: 'POST',
						  data:{
							  email:this.email
						  },
						  header: { 'Content-Type': 'application/json' }
					  });
						if (res.statusCode === 200) {
							this.codeSent=true;
							uni.showToast({
							  title: res.data.message,
							  icon: 'none',
							  duration: 2000
							});
						} else {
							uni.showToast({
							  title: res.data.error || res.data.email,
							  icon: 'none',
							  duration: 2000
							});
						}
					} catch (err) {
					    uni.showToast({ title: 'Network Error', icon: 'none' })
					} finally {
						uni.hideLoading();
					} 
		},
		async resetPassword() {
		  if (this.newPassword !== this.confirmPassword) {
		    uni.showToast({
		      title: 'The passwords entered twice are inconsistent',
		      icon: 'none',
		      duration: 2000
		    })
		    return
		  }
		  this.resetLoading = true
		   try {
		      const [err, res] = await uni.request({
		        url: `${this.$baseURL}/api/auth/reset-password`,
		        method: 'POST',
		        data: {
		          email: this.email,
		          resetCode: this.resetCode,
		          newPassword: this.newPassword
		        },
		        header: { 'Content-Type': 'application/json' }
		      }).then(res => [null, res]).catch(err => [err, null])
		  
		      if (err || res.statusCode !== 200) {
		        let errorMessage = '重置密码失败，请稍后重试'
			  if (res?.data) {
				  if (res.data.error) {
						errorMessage = res.data.error
				  } else if (res.data.email) {
						errorMessage = res.data.email
					} else if (res.data.resetCode) {
						errorMessage = res.data.resetCode
					} else if (res.data.newPassword) {
						errorMessage = res.data.newPassword
					}
				} else if (err) {
					  errorMessage = err.message || 'Network Error，请稍后重试'
				}
		
		        // 显示错误信息
		        uni.showToast({
		          title: errorMessage,
		          icon: 'none',
		          duration: 2000
		        })
		  
		        throw new Error(errorMessage)
		      }
		  
		      // 处理成功逻辑
		      this.successMessage = res.data.message || '密码重置成功！'
		  
		      // 显示成功信息
		      uni.showToast({
		        title: this.successMessage,
		        icon: 'success',
		        duration: 2000
		      })
		  
		      // 3秒后跳转到登录页
		      setTimeout(() => {
		        uni.navigateTo({
		          url: '/pages/UserLogin/UserLogin' // 登录页路径
		        })
		      }, 3000)
		  
		    } catch (error) {
		      console.error('Reset password error:', error)
		    } finally {
		      // 关闭加载状态
		      this.resetLoading = false
		    }
		}
	  }
	};
	
</script>

<style scoped lang="scss">
	@import '../../index.scss';
</style>