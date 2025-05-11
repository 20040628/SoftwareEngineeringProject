<template>
  <view class="container">
    <view class="form-item">
      <text class="label">Card Number</text>
      <input class="input" v-model="card.cardNumber" type="number" placeholder="Please enter the number(13-19)":placeholder-style="placeholderStyle" />
    </view>
    <button class="submit-btn" @click="submitCard">Submit</button>
  </view>
</template>

<script>
export default {
  data() {
    return {
      card: {
        cardNumber: '',
      },
      user: null,
      isLoading: false, 
	  placeholderStyle: 'color: #2c3e50; font-size: 14px;'
    };
  },
  methods: {
    async submitCard() {
      if (!this.card.cardNumber) {
        uni.showToast({
          title: 'Please complete the bank card information',
          icon: 'none',
        });
        return;
      }
	  const cardRegex = /^[0-9]{13,16}$/;
	  if (!cardRegex.test(this.card.cardNumber)) {
		  uni.showToast({
		    title: 'Card number must be between 13-16 digits',
		    icon: 'none',
		  });
		  return;
	  }
      const token = uni.getStorageSync('token');
      this.user = uni.getStorageSync('userInfo');

      if (!token || !this.user) {
        uni.showToast({
          title: 'User not logged in',
          icon: 'none',
        });
        return;
      }

      try {
        this.isLoading = true;
        uni.showLoading({ title: "Loading...", mask: true });

        const res = await uni.request({
          url: `${this.$baseURL}/api/users/updateBankCard/${this.user.userId}`,
          method: 'POST',
          data: {
            bankCard: this.card.cardNumber,
          },
          header: {
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${token}`,
          },
        });

        if (res.data.success) {
          uni.showToast({
            title: 'Bank card updated successfully',
            icon: 'none',
          });
        } else {
          uni.showToast({
            title: res.data.message || 'Update failed',
            icon: 'none',
          });
        }
      } catch (err) {
        console.error(err); 
        uni.showToast({
          title: 'Network error',
          icon: 'none',
        });
      } finally {
        uni.hideLoading();
        this.isLoading = false;
      }
      uni.navigateBack();
    },
  },
};
</script>

<style scoped>
.container {
  padding: 20px;
}
.form-item {
  margin-bottom: 15px;
}
.label {
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
}
.input {
  width: 90%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  font-size: 16px;
}
.submit-btn {
  width: 100%;
  padding: 10px;
  background-color: #2c3e50;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
}
</style>
