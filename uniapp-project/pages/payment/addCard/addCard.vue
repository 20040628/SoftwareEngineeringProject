<template>
  <view class="container">
    <view class="form-item">
      <text class="label">Card Number</text>
      <input class="input" v-model="card.cardNumber" type="number" placeholder="Please enter the number(13-19)" />
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
      isLoading: false,  // 处理加载状态
    };
  },
  methods: {
    // 提交银行卡信息
    async submitCard() {
      // 校验银行卡号是否填写完整
      if (!this.card.cardNumber) {
        uni.showToast({
          title: 'Please complete the bank card information',
          icon: 'none',
        });
        return;
      }

      // 获取 token 和用户信息
      const token = uni.getStorageSync('token');
      this.user = uni.getStorageSync('userInfo');

      if (!token || !this.user) {
        uni.showToast({
          title: 'User not logged in',
          icon: 'none',
        });
        return;
      }

      // 开始请求
      try {
        this.isLoading = true; // 设置加载状态
        uni.showLoading({ title: "Loading...", mask: true });

        const res = await uni.request({
          url: `${this.$baseURL}/api/users/updateBankCard/${this.user.userId}`,
          method: 'POST',
          data: {
            bankCard: this.card.cardNumber, // 使用正确的 cardNumber
          },
          header: {
            'Content-Type': 'application/json',
            "Authorization": `Bearer ${token}`,
          },
        });

        // 假设接口返回的数据为 res.data
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
        console.error(err); // 打印错误，便于调试
        uni.showToast({
          title: 'Network error',
          icon: 'none',
        });
      } finally {
        uni.hideLoading();
        this.isLoading = false;
      }

      console.log('Submitted bank card information:', this.card);

      // 模拟提交成功后返回上一页
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
