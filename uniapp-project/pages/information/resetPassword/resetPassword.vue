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
      // 检查新密码和确认密码是否一致
      if (this.newPassword!== this.confirmPassword) {
        uni.showToast({
          title: 'The new password does not match the confirmed password',
          icon: 'none'
        });
        return;
      }

      // 检查新密码是否符合要求
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
      if (!passwordRegex.test(this.newPassword)) {
        uni.showToast({
          title: '新密码必须至少8个字符，包括至少一个大写字母、一个小写字母和一个数字',
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
            icon:'success'
          });
		  this.oldPassword = '';
		this.newPassword = '';
		this.confirmPassword = '';
        } else if (response.statusCode === 400) {
          uni.showToast({
            title: response.data.message,
            icon: 'none'
          });
        }
      } catch (e) {
        uni.showToast({
          title: `密码修改失败: ${e.message}`,
          icon: 'none'
        });
      }
    }
  }
};
</script>

<style>
	@import '../../index.scss';
.container {
  padding: 20px;
}



button {
  width: 100%;
  padding: 10px;
  background-color: #aaaaff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}
</style>