<template>
	<view class="page">
		<view class="header">
			<text>Welcome to ScootGo</text>
		</view>
		<view class="container">
			<view class="title">
				<text>Find your Password</text>
			</view>
			<view v-if="!codeSent">
				<form>
					<view class="form-group">
						<input type="tel" v-model="email" placeholder="Please enter your email"
							:placeholder-style="placeholderStyle" class="input-field" />
					</view>
					<view class="login-btn" @click="sendCode()">Send</view>
				</form>
			</view>
			<view v-if="codeSent" class="reset-section">
				<view class="form-group">
					<input type="tel" v-model="resetCode" placeholder="Please enter the verification code received"
						:placeholder-style="placeholderStyle" class="input-field" />
				</view>
				<view class="form-group">
					<input type="password" v-model="newPassword" placeholder="Please set a new password"
						:placeholder-style="placeholderStyle" class="input-field" />
				</view>
				<view class="form-group">
					<input type="password" v-model="confirmPassword" placeholder="Please enter the new password again"
						:placeholder-style="placeholderStyle" class="input-field" />
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
				email: '',
				codeSent: false,
				resetLoading: false,
				placeholderStyle: 'color: #2c3e50; font-size: 14px;'
			};
		},
		methods: {
			async sendCode() {
				const token = String(uni.getStorageSync('token'));
				try {
					uni.showLoading({
						title: "Loading...",
						mask: true
					});
					const res = await uni.request({
						url: `${this.$baseURL}/api/auth/forgot-password`,
						method: 'POST',
						data: {
							email: this.email
						},
						header: {
							'Content-Type': 'application/json'
						}
					});
					if (res.statusCode === 200) {
						this.codeSent = true;
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
					uni.showToast({
						title: 'Network Error',
						icon: 'none'
					})
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
						header: {
							'Content-Type': 'application/json'
						}
					}).then(res => [null, res]).catch(err => [err, null])

					if (err || res.statusCode !== 200) {
						let errorMessage = 'Password reset failed. Please try again later'
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
							errorMessage = err.message || 'Network Error'
						}
						uni.showToast({
							title: errorMessage,
							icon: 'none',
							duration: 2000
						})

						throw new Error(errorMessage)
					}

					this.successMessage = res.data.message || 'Password reset successfully!'

					uni.showToast({
						title: this.successMessage,
						icon: 'none',
						duration: 2000
					})

					setTimeout(() => {
						uni.navigateTo({
							url: '/pages/UserLogin/UserLogin'
						})
					}, 3000)

				} catch (error) {
					console.error('Reset password error:', error)
				} finally {
					this.resetLoading = false
				}
			}
		}
	};
</script>

<style scoped lang="scss">
	@import '../../index.scss';
</style>