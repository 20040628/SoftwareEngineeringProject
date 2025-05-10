<template>
	<view>
		<view class="text-top">Common Problem</view>
		<view class="bg-list">
			<view v-for="(item,index) in problemBox" :key="index" :title="item.problemTitle" class="list-title">
				<view class="text-title">{{item.problemTitle}}</view>
				<view v-for="(problemItem,problemIndex) in item.problemList" :key="problemIndex" class="list-question"
					hover-class="hover" @click="onClick(problemItem)">
					<view class="text-item">{{problemItem.title}}</view>
					<view class="line" v-if="problemIndex!=item.problemList.length-1"></view>
				</view>

			</view>
		</view>
		<view class="bg-box">
			<view class="bg-white-box">
				<image src="../../../static/common/icon-letter.png" class="image"></image>
				<view class="text-feedback" hover-class="hover" @click="toFeedbackList">My Feedback</view>

				<view class="vertical-line"></view>

				<image src="../../../static/common/icon-edit.png" class="image"></image>
				<view class="text-feedback" hover-class="hover" @click="toFeedback">Submit Feedback</view>
			</view>

		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				problemBox: [{
						problemTitle: 'Scooter Rental',
						problemList: [{
								title: 'How do I book a scooter?',
								content: 'You can click [Instruction on Use] on the home page.'
							},
							{
								title: 'Can I cancel my scooter reservation?',
								content: 'Unpaid orders can be cancelled within [All Orders], while paid orders cannot be cancelled.'
							}

						]
					},
					{
						problemTitle: 'Payment Issues',
						problemList: [{
								title: 'Why am I being charged incorrectly for my scooter rental?',
								content: 'When you return, the power connection is too low or the return is overdue.'
							}

						]
					},
					{
						problemTitle: 'Scooter Usage',
						problemList: [{
								title: 'How do I unlock a scooter?',
								content: 'Click "Return" on the [now] page.'
							}

						]
					}, {
						problemTitle: 'Other',
						problemList: [{
								title: 'How to change my user profile picture?',
								content: 'Please click on [My], and click on Avatar to change the your avatar.',
							},
							{
								title: 'How to change my login password?',
								content: 'Please click on [My]-[Set]-[Account and Security]to change the your password.',
							},
							{
								title: 'How to log out?',
								content: 'Please click on [My]-[Log Out] to log out.',
							},
							{
								title: 'What if the system is slow or unresponsive?',
								content: 'If there are problems such as slow system response or excessively long response time, you can check the network status of this machine or restart the program and try again.'
							}
						]
					}
				]
			}
		},
		methods: {
			onClick(item) {
				uni.navigateTo({
					url: '/pages/feedback/helpDetail/helpDetail?title=' + item.title + '&content=' + item.content,
					success: res => {},
					fail: () => {},
					complete: () => {}
				});

			},
			toFeedbackList() {
				uni.navigateTo({
					url: '/pages/feedback/feedbackList/feedbackList',
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},
			toFeedback() {
				uni.navigateTo({
					url: '/pages/feedback/feedback/feedback',
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			}
		}
	}
</script>

<style>
	page {
		background-color: #F8F8F8;
		height: 100%;
	}

	.bg-box {
		background-color: #F8F8F8;
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
	}

	.bg-list {
		margin-bottom: 100rpx;
		background-color: #FFFFFF;
		padding: 30rpx
	}

	.bg-white-box {
		background-color: #FFFFFF;
		margin: 30rpx;
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
		padding: 10rpx;
		border-radius: 20rpx;
		color: #303133;
		font-size: 26rpx;
	}

	.vertical-line {
		height: 20rpx;
		background-color: #cecece;
		width: 2rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
	}

	.line {
		width: 100%;
		height: 1rpx;
		background-color: #F5F5F5;
	}

	.text-title {
		color: #303133;
		font-size: 32rpx;
		font-weight: bold;
	}

	.text-item {
		color: #2c3e50;
		font-size: 28rpx;
		padding: 24rpx;
	}

	.list-title {
		margin-bottom: 30rpx;
	}

	.list-question {
		color: #606266;
		font-size: 28rpx;
	}

	.hover {
		background-color: #eee;
		opacity: 0.6;
	}

	.image {
		width: 40rpx;
		height: 40rpx;
		margin-left: 20rpx;
	}

	.text-feedback {
		padding: 20rpx;
	}

	.text-top {
		margin: 30rpx;
		color: #014d87;
		font-size: 30rpx;
		font-weight: bold;
	}
</style>