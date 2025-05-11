<template>
	<view>
		<view class="white">
			<view class="text-black">{{feedback.content}}</view>
			<view class="text-time">{{feedback.createTime.replace('T', ' ')}} submitted</view>

		</view>
		<view class="box">
			<view class="text-black">Admin Response:</view>
			<view class="text-grey">We have received your feedback, we will process and reply to you as soon as possible, please understand the inconvenience caused to you. Thank you for your feedback and support!</view>
			<view class="text-time">{{feedback.createTime.replace('T', ' ')}}</view>
		</view>

		<view class="box" v-if="feedback.adminResponse">
			<view class="text-black">Admin Response:</view>
			<view class="text-grey">{{feedback.adminResponse}}</view>

			<view class="text-grey" style="margin-top: 20rpx;">Whether it solved your problem</view>
			<view class="flex-row">
				<view @click="resolve(true)" :class="isResolve&&isShow?'btn-resolve':'btn-normal'" class="btn-normal">
					<image src="../../../static/common/icon_solved_sel.png" class="icon-normal"
						v-if="isResolve&&isShow"></image>
					<image src="../../../static/common/icon_solved_nor.png" class="icon-normal" v-else></image>
					resolved
				</view>
				<view @click="resolve(false)" :class="!isResolve&&isShow?'btn-unResolve':'btn-normal'"
					class="btn-normal">
					<image src="../../../static/common/icon_notsolved_sel.png" class="icon-normal"
						v-if="!isResolve&&isShow"></image>
					<image src="../../../static/common/icon_notsolved_nor.png" class="icon-normal" v-else></image>
					unsolved

				</view>
			</view>
			<view class="text-time">{{feedback.responseTime.replace('T', ' ')}}</view>
		</view>

		<view class="box" v-if="feedback.adminResponse&&isResolve&&isShow">
			<view class="text-black">Admin Response</view>
			<view class="text-grey">Dear users, we are glad that our answers can help you. Thank you for your support!</view>
		</view>

		<view class="box" v-if="feedback.adminResponse&&!isResolve&&isShow">
			<view class="text-black">Admin Response</view>
			<view class="text-grey">Dear users, we are very sorry that our answer did not help you.
			 It is recommended that you describe your problems in detail and re-submit feedback or call customer service to continue consulting, 
			 thank you for your support!
			</view>
		</view>


		<view></view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				feedback: {},
				isResolve: false,
				isShow: false

			}
		},
		onLoad(options) {
			this.feedback = JSON.parse(options.feedback);
		},

		methods: {


			resolve(isResolve) {

				if (!this.isShow) {
					this.isResolve = isResolve
					this.isShow = true;
				}


			}

		}
	}
</script>

<style>
	page {
		background-color: #F8F8F8;
		padding-bottom: 30rpx;
	}

	.white {
		background-color: #FFFFFF;
		padding: 45rpx 40rpx;
		box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
	}

	.text-grey {
		color: #606266;
		font-size: 32rpx;
		line-height: 1.5em;
	}

	.text-time {
		color: #014d87;
		font-size: 24rpx;
		text-align: end;
		margin-top: 30rpx;
	}

	.text-black {
		color: #303133;
		font-size: 32rpx;
		margin-bottom: 30rpx;
		font-weight: bold;
	}

	.box {
		background-color: #FFFFFF;
		border-radius: 30rpx;
		margin: 50rpx 30rpx;
		padding: 30rpx 30rpx;
	}

	.icon-normal {
		width: 25rpx;
		height: 25rpx;
		margin-right: 20rpx;
	}


	.flex-row {
		display: flex;
		flex-direction: row;
		justify-content: space-around;
		align-items: center;
		margin-top: 30rpx;
	}

	.btn-normal {
		border-radius: 40rpx;
		border: solid 1rpx #D2D2D2;
		color: #999999;
		padding: 20rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 28rpx;
		width: 32%;
	}

	.btn-resolve {
		background-color: #E1FFFB;
		border: solid 1rpx #19CFB7;
		color: #19CFB7;

	}

	.btn-unResolve {
		border: solid 1rpx #FF5F5F;
		background-color: #FFF2F2;
		color: #FF5F5F;

	}



	.image {
		width: 200rpx;
		height: 200rpx;
		border-radius: 20rpx;
		margin: 10rpx;
	}

	.img-row {
		display: flex;
		flex-direction: row;

		flex-wrap: wrap;
		justify-content: flex-start;
		align-items: center;

	}
</style>
