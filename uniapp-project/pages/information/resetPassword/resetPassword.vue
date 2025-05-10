<template>
  <view class="container">
	<form>
		<view class="form-group">
		  <label>old password:</label>
		  <input type="password" v-model="oldPassword"placeholder="Please enter your old password" class="input-field"/>
		</view>
		<view class="form-group">
		  <label>new password:</label>
		  <input type="password" v-model="newPassword"placeholder="Please enter your new password" class="input-field"/>
		</view>
		<view class="form-group">
		  <label>Confirm the new password:</label>
		  <input type="password" v-model="confirmPassword"placeholder="Please confirm the new password" class="input-field" />
		</view>
	</form>
    <button @click="changePassword">change password</button>
  </view>
</template>

<script>
export default {
  data() {
    return {
      oldPassword: '',
      newPassword: '',
      confirmPassword: '',
      userId: '',
	  user:[]
    };
  },
  onLoad(){
	  this.user = uni.getStorageSync('userInfo');
	  if (this.user) {
	    this.userId = this.user.userId;
	  } else {
	    this.userId = null; 
	  }
  },
  methods: {
    async changePassword() {
      if (this.newPassword!== this.confirmPassword) {
        uni.showToast({
          title: 'The new password does not match the confirmed password',
          icon: 'none'
        });
        return;
      }

      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
      if (!passwordRegex.test(this.newPassword)) {
        uni.showToast({
          title: 'The new password must be at least 8 characters long, including at least one capital letter, one lowercase letter and one number',
          icon: 'none'
        });
        return;
      }

      try {
		  const token = String(uni.getStorageSync('token'));
        const response = await uni.request({
          url: `${this.$baseURL}/api/users/changePassword/${this.userId}`,
          method: 'POST',
          data: {
            oldPassword: this.oldPassword,
            newPassword: this.newPassword,
            confirmPassword: this.confirmPassword
          },
		  header: {
		    'Content-Type': 'application/json',
		    "Authorization": `Bearer ${token}`
		  },
        });

        if (response.statusCode === 200) {
          uni.showToast({
            title: response.data.message,
            icon: 'none',
          });
		  this.oldPassword = '';
		this.newPassword = '';
		this.confirmPassword = '';
        } else if (response.statusCode === 400) {
          uni.showToast({
            title: response.data,
            icon: 'none'
          });
        }
      } catch (e) {
        uni.showToast({
          title: `Password modification failed: ${e.message}`,
          icon: 'none'
        });
      }
    }
  }
};
</script>

<style scoped>

	.container{
		height: 100%;
		width: 750rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		box-sizing: border-box;
		background-color: #fff;
		border-radius: 60rpx 60rpx 0 0;
	}
	.form-group {
		width: 660rpx;
		margin-bottom: 60rpx;
		position: relative;
	}
	.input-field {
		box-sizing: border-box;
		width: 660rpx;
		height: 90rpx;
		border-bottom: 1rpx solid #333;
		font-size: 14px;
		background-color: #fff;
	}
	
	
.container {
  padding: 20px;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #2c3e50;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>