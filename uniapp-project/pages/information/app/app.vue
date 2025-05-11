<template>
	<view class="container">
		<view class="card">
			<view class="title">About This App</view>
			<view class="content">
				This application is designed to provide users with a clean, efficient, and reliable experience.
				Built with UniApp, it ensures cross-platform compatibility and seamless performance.
				We focus on simplicity, functionality, and continuous improvement to support your daily needs.
			</view>
			<view class="info">
				<view>Version: v1.0.0</view>
				<view>Release Date: May 2025</view>
				<view>Developer: ScootGo Team</view>
				<!-- <view>Contact: support@example.com</view> -->
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		onLoad() {
			uni.setNavigationBarTitle({
				title: 'About APP'
			});
		}
	}
</script>

<style scoped>
	page {
		/* background-color: #f5f7fa; */
	}

	.container {
		padding: 40rpx 30rpx;
	}

	.card {
		background-color: #ffffff;
		border-radius: 16rpx;
		padding: 36rpx 28rpx;
	}

	.title {
		font-size: 42rpx;
		font-weight: 600;
		color: #1f2d3d;
		margin-bottom: 24rpx;
	}

	.content {
		font-size: 30rpx;
		color: #2c3e50;
		line-height: 1.8;
		margin-bottom: 36rpx;
	}

	.info view {
		font-size: 28rpx;
		color: #2c3e50;
		margin-bottom: 12rpx;
	}
</style>
