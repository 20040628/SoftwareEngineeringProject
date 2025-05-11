<template>
	<view class="container">
		<image class="logo" src="/static/images/logo.png" mode="widthFix" />
		<text class="title">ScootGo</text>
		<text class="loading-text">Loading...</text>
	</view>
</template>

<script>
	export default {
		created() {
			setTimeout(() => {
				const token = uni.getStorageSync('token');
				if (!token) {
					console.log("The Token was not found. Redirect to the login page...");
					uni.redirectTo({
						url: '/pages/UserLogin/UserLogin'
					});
					return;
				}

				uni.request({
					url: `${this.$baseURL}/api/auth/validate`,
					method: 'GET',
					header: {
						'Authorization': 'Bearer ' + token
					},
					success: (res) => {
						if (res.statusCode === 200 && res.data.valid) {
							console.log("Token verification passed, user ID:", res.data.userId);
							uni.setStorageSync('userInfo', res.data);
							uni.redirectTo({
								url: '/pages/index'
							});
						} else {
							uni.removeStorageSync('token');
							uni.redirectTo({
								url: '/pages/UserLogin/UserLogin'
							});
						}
					},
					fail: (err) => {
						console.error("Token verification request failed:", err);
						uni.showToast({
							title: 'Network Error',
							icon: 'none'
						});
						uni.redirectTo({
							url: '/pages/UserLogin/UserLogin'
						});
					}
				});
			}, 2000);
		}
	}
</script>

<style>
	.container {
		flex-direction: column;
		justify-content: center;
		align-items: center;
		display: flex;
		height: 100vh;
		background-color: #ffffff;
	}

	.logo {
		width: 150rpx;
		height: 150rpx;
		margin-bottom: 30rpx;
	}

	.title {
		font-size: 48rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 20rpx;
	}

	.loading-text {
		font-size: 28rpx;
		color: #000000;
	}
</style>