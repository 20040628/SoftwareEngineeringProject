<template>
	<view class="container">
		<view class="content-box">
			<view class="helpTitle">{{ resultData.title }}</view>
			<view class="helpCon">{{ resultData.content }}</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				resultData: {
					title: '',
					content: ''
				}
			};
		},
		onLoad(options) {
			this.resultData.title = options.title;
			this.resultData.content = options.content;
			uni.setNavigationBarTitle({
				title: options.title
			});
		}
	};
</script>

<style>
	page {
		background-color: #f9f9f9;
	}

	.container {
		padding: 40rpx 30rpx;
	}

	.content-box {
		background-color: #ffffff;
		border-radius: 16rpx;
		padding: 36rpx 28rpx;
	}

	.helpTitle {
		font-size: 42rpx;
		font-weight: 500;
		color: #1f2d3d;
		margin-bottom: 24rpx;
		line-height: 1.4;
	}

	.helpCon {
		font-size: 30rpx;
		color: #4f5e6e;
		line-height: 1.8;
	}
</style>
