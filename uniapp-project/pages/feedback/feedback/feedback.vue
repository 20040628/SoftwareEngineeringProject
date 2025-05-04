<template>
	<view class="feedback-body">

		<text class="text-black">Feedback
			<text style="color: red;">*</text></text>
		<textarea placeholder="Please describe a problem you have encountered or a recommendation for this product..." v-model="sendData.feedbackContent" class="feedback-textare" maxlength="-1"/>

		<view class="image-title">
			<!-- <text class="text-black">Upload problem screenshot<text class="text-grey"> (Optional, up to 6 photos can be uploaded)
				</text>
			</text> -->
			<!-- <view class="text-grey">{{ sendData.imgs.length }}/6</view> -->
		</view>
		<!-- <view class="filepicker">
			<uni-file-picker file-mediatype="image" :limit="6" return-type="array" v-model="sendData.imgs">
			</uni-file-picker>
		</view> -->

		<!-- <text class="text-black">联系方式<text class="text-grey">(选填)</text> </text>
		<input class="feedback-input" v-model="sendData.mobile" placeholder="请输入您的手机号" /> -->
		<view class="btn">
			<button :disabled="!sendData.feedbackContent" type="primary" @click="submitFeedback">
				Submit
			</button>
		</view>

	</view>
</template>

<script>
	export default {
	  data() {
	    return {
	      sendData: {
	        feedbackContent: '', // 反馈内容
	      },
	      loading: false,  // 按钮加载状态
	      message: '',
	      messageType: '',
	    };
	  },
	  methods: {
	    async submitFeedback() {
	      this.loading = true; // 开启加载状态
		  const token = String(uni.getStorageSync('token'));
		  try{
			  const [err, res] = await uni.request({
				url: `${this.$baseURL}/api/feedback`, // 后端地址
				method: 'POST',
				data: { content: this.sendData.feedbackContent },
				header: { 'Content-Type': 'application/json',
						   "Authorization": `Bearer ${token}`}
			  }).then(res => [null, res]).catch(err => [err, null]); // 兼容 `try-catch` 方式
				 
	       if (err || res.statusCode !== 200) {
	            // 这里处理后端返回的错误信息
	            let errorMessage = 'Submition failed';
	            
	            if (res?.data) {
	      				if (typeof res.data === 'string') {
	      				  // 如果后端直接返回字符串
	      				  errorMessage = res.data;
	      				} else if (typeof res.data === 'object' && res.data.message) {
	      				  // 如果后端返回 JSON 对象
	      				  errorMessage = res.data.message;
	      				}
	      			  } else if (err) {
	      				errorMessage = err.message || 'Network error, please try again';
	      			  }
	      	
	            this.message = errorMessage;
	            this.messageType = 'error';
	      
	            uni.showToast({
	              title: this.message,
	              icon: 'none',
	              duration: 2000
	            });
	      
	            throw new Error(errorMessage);
	          }
				 console.log('提交成功:', res.data);  // ✅ 确保数据正确返回
				
				//         // **存储数据到本地存储**
				// uni.setStorageSync('feedbackData', res.data);  

	          // 显示成功提示
	          uni.showToast({
	            title: 'Submition successful!',
	            icon: 'success',
	            duration: 2000
	          });

			// 注册成功后清空表单
			this.sendData = {
			  feedbackContent: '',
			};
	        } catch (error) {
	          console.error('Submition error:', error);
	        } finally {
	          this.loading = false;
	        }
	    }
		}
	};

</script>

<style>
	.text-black {
		color: #303133;
		font-size: 32rpx;
	}

	.text-grey {
		color: #BCBCBC;
		font-size: 24rpx;
		margin-left: 15rpx;
	}

	.feedback-quick {
		padding-right: 10rpx;
		color: #606266;
		font-size: 32rpx;
	}

	.feedback-body {
		padding: 30rpx;
	}

	.feedback-textare {
		margin-top: 30rpx;
		margin-bottom: 30rpx;
		height: 266rpx;
		color: #303133;
		font-size: 28rpx;
		line-height: 2em;
		width: 100%;
		box-sizing: border-box;
		padding: 20rpx 30rpx;
		border-radius: 20rpx;
		background-color: #F5F6F8;
	}

	.feedback-input {
		font-size: 28rpx;
		color: #303133;
		background-color: #F5F6F8;
		border-radius: 20rpx;
		height: 100rpx;
		min-height: 100rpx;
		padding: 0 30rpx;
		margin-top: 30rpx;
		margin-bottom: 60rpx;
	}



	.btn-submit {
		border-radius: 20rpx;
		color: #FFFFFF;
		margin-top: 100rpx;
		background-color: #007AFF;
		margin-bottom: 70rpx;
	}

	.image-title {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		color: #606266;

	}

	.filepicker {
		margin-top: 30rpx;
		margin-bottom: 30rpx;
	}

	.btn {
		position: fixed;
		bottom: 0;
		left: 0 ;
		right: 0;
		margin: 30rpx 30rpx 60rpx 30rpx;
	}
</style>
