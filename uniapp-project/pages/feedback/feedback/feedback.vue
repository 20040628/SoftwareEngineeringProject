<template>
	<view class="feedback-body">

		<text class="text-black">Feedback
			<text style="color: red;">*</text></text>
		<textarea placeholder="Please describe a problem you have encountered or a recommendation for this product...":placeholder-style="placeholderStyle" v-model="sendData.feedbackContent" class="feedback-textare" maxlength="-1"/>
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
	        feedbackContent: '', 
	      },
	      loading: false,  
	      message: '',
	      messageType: '',
		  placeholderStyle: 'color: #2c3e50; font-size: 14px;'
	    };
	  },
	  methods: {
	    async submitFeedback() {
	      this.loading = true;
		  const token = String(uni.getStorageSync('token'));
		  try{
			  const [err, res] = await uni.request({
				url: `${this.$baseURL}/api/feedback`,
				method: 'POST',
				data: { content: this.sendData.feedbackContent },
				header: { 'Content-Type': 'application/json',
						   "Authorization": `Bearer ${token}`}
			  }).then(res => [null, res]).catch(err => [err, null]);
				 
	       if (err || res.statusCode !== 200) {
	            let errorMessage = 'Submition failed';
	            
	            if (res?.data) {
	      				if (typeof res.data === 'string') {
	      				  errorMessage = res.data;
	      				} else if (typeof res.data === 'object' && res.data.message) {
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
				 console.log('提交成功:', res.data); 
	          uni.showToast({
	            title: 'Submition successful!',
	            icon: 'none',
	            duration: 2000
	          });

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
