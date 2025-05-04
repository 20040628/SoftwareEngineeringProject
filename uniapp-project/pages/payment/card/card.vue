<template>
  <view class="container">
    <!-- 银行卡列表 -->
    <view class="card-list">
      <view class="card">
        <view class="card-info">
          <text class="card-number">
            {{ card.maskedCard}}
          </text>
        </view>
      </view>
    </view>

    <!-- 添加银行卡按钮 -->
    <button class="add-card-btn" @click="addCard">Update a bank card</button>
  </view>
</template>

<script>
export default {
  data() {
    return {
      card: null
    };
  },
  onLoad(){
	  this.getCard()
  },
  methods: {
     async getCard(){
		const token = String(uni.getStorageSync('token'));
		this.user = uni.getStorageSync('userInfo');
		try {
			uni.showLoading({ title: "加载中...", mask: true });
		    const res = await uni.request({
		        url: `${this.$baseURL}/api/bank-payment/check-card/${this.user.userId}`,
		        method: 'GET',
				header: {
				  'Content-Type': 'application/json',
				  "Authorization": `Bearer ${token}`
				},
				
		    });
		    this.card = res.data
		} catch (err) {
		    uni.showToast({ title: '网络错误', icon: 'none' })
		} finally {
			uni.hideLoading();
		    this.isLoading = false
		}
	},
    addCard() {
	  uni.navigateTo({
	  	url: '../addCard/addCard'
	  })
    },
    // 隐藏卡号的部分数字，显示星号
    maskCardNumber(cardNumber) {
      return cardNumber.slice(0, 4) + ' **** **** ' + cardNumber.slice(-4);
    }
  }
};
</script>

<style scoped>
.container {
  padding: 20px;
}
.card-list {
  margin-bottom: 30px;
}
.card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  margin-bottom: 10px;
  background-color: #f8f8f8;
  border-radius: 5px;
}
.card-info {
  font-size: 16px;
}
.card-number {
  color: #333;
}
.card-actions {
  display: flex;
  align-items: center;
}
.default-switch {
  margin-right: 10px;
}
.default-text {
  color: #1E90FF;
  font-size: 14px;
}
.add-card-btn {
  width: 100%;
  padding: 15px;
  background-color: #1E90FF;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
}
</style>
