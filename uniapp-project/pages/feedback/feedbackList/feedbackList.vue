<template>
	<view class="page">
		<view v-if="feedbacks.length==0" class="no-feedback">
			 <text>No feedback</text>
		</view>
		<view v-else>
			<view v-for="(item,index) in feedbacks" :key="index" @click="feedbackClick(item)">
				<view class="list-box">
					<view class="text-title">{{item.content}}</view>
					<view v-if="item.status === 'pending'" class="text-tips">
						We have received your feedback and will process it and get back to you as soon as possible. Thank you for your support.
					</view>
					<view v-if="item.status === 'processing'" class="text-tips">
						We are processing your feedback, please wait patiently.
					</view>
					<view v-if="item.status === 'resolved'" class="text-tips">
						{{ item.adminResponse }}
					</view>
					<view class="text-time">{{ item.createTime.replace('T', ' ') }}</view>
					<view :class="statusClass(item.status)">{{ statusText(item.status) }}</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				feedbacks: [],
				isLoading:true
			}
		},
		onLoad() {
			this.getUserFeedback();
		},
		methods: {
			//获取用户意见反馈列表
			async getUserFeedback() {
				try{
					const token = String(uni.getStorageSync('token'));
					const res = await uni.request({
					url: 'http://localhost:8080/api/feedback/user', 
					method: 'GET',
					header: { 'Content-Type': 'application/json',
							   "Authorization": `Bearer ${token}`}
					});
					if (res.statusCode === 200) {
					    this.feedbacks = res.data; 
					  } else {
					    uni.showToast({ title: '数据加载失败', icon: 'none' });
					  }
				} catch (err) {
					  uni.showToast({ title: '网络错误', icon: 'none' });
				} finally {
					  this.isLoading = false;
				}
			},
			feedbackClick(item) {
				uni.navigateTo({
					url: '/pages/feedback/feedbackDetail/feedbackDetail?' + 'feedback=' + JSON.stringify(item),
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			},
			statusText(status) {
			        const statusMap = {
			            pending: "pending",
			            processing: "processing",
			            resolved: "resolved"
			        };
			        return statusMap[status] || "unknown state";
			    },
			statusClass(status) {
			        const classMap = {
			            pending: "state-grey",
			            processing: "state-grey",
			            resolved: "state-green"
			        };
			        return classMap[status] || "state-grey";
			}


		}
	}
</script>

<style lang="scss">
	page {
		background-color: #F8F8F8;
	}

	.text-title {
		color: #303133;
		font-size: 30rpx;
		font-weight: bold;
		margin-right: 100rpx;
	}

	.text-time {
		color: #909193;
		font-size: 24rpx;
		margin-top: 24rpx;
	}

	.text-tips {
		color: #bcbcbc;
		font-size: 24rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		margin-top: 22rpx;
	}

	.state-green {
		position: absolute;
		right: 0;
		top: 0;
		color: #07C160;
		font-size: 28rpx;
		padding: 30rpx;
	}

	.state-grey {
		position: absolute;
		right: 0;
		padding: 30rpx;
		top: 0;
		color: #606266;
		font-size: 28rpx;
	}



	.list-box {
		position: relative;
		background-color: #FFFFFF;
		border-radius: 10rpx;
		margin-top: 30rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
		padding: 30rpx;
	}



	.flex-col {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: space-between;
	}
	.no-feedback {
	    text-align: center;
	    color: #888;
	    padding: 20px;
	    font-size: 16px;
	}

</style>
