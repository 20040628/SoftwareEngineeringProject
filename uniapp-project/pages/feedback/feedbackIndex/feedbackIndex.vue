<!-- 帮助反馈页面 -->
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
								content: '微信小程序内部对网页链接进行了限制，遇到此情况可复制链接至手机浏览器中打开。'
							},
							{
								title: 'Can I cancel my scooter reservation?',
								content: '对于文件类的课件，请点击[在线预览]，稍等片刻之后程序会跳转到对应的预览页面。点击右上角三个点按钮，在下方弹窗中选择[保存到手机]即可。也可对课件进行[转发][收藏]等操作'
							}

						]
					},
					{
						problemTitle: 'Payment Issues',
						problemList: [{
								title: 'Why am I being charged incorrectly for my scooter rental?',
								content: '在报名截止日期之前均可以取消。点击活动报名详情下方的[取消报名]按钮即可。'
							}

						]
					},
					{
						problemTitle: 'Scooter Usage',
						problemList: [{
								title: 'How do I unlock a scooter?',
								content: '不可以。完成的调查问卷会处于不可点击状态，所以请您认真对待每一次问卷的填写工作。感谢您的支持。'
							}

						]
					}, {
						problemTitle: 'Other',
						problemList: [{
								title: 'How to change my user profile picture?',
								content: '请点击[我的] - [账号管理] - [头像]即可更换用户头像',
							},
							{
								title: 'How to change my login password?',
								content: '请点击[我的] - [账号管理] - [修改密码]即可修改登录密码',
							},
							{
								title: 'How to log out?',
								content: '请点击[我的] - [账号管理] - [退出登录]即可退出登录',
							},
							{
								title: 'What if the system is slow or unresponsive?',
								content: '如果出现系统反应迟钝、响应时间过长的问题，可查看本机的网络状态，或重启程序后再次尝试。'
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
		color: #999999;
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
		color: #999999;
		font-size: 30rpx;
	}
</style>
