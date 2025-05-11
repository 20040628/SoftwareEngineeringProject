<template>
  <view class="container">
    <view class="card-list">
      <view class="card">
        <view class="card-info" v-if="card">
          <text class="card-number">
            {{ card.maskedCard}}
          </text>
        </view>
      </view>
    </view>

    <button class="add-card-btn" @click="addCard">Update the Card</button>
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
  mounted() {
  	 this.getCard()
  },
  methods: {
     async getCard(){
		const token = String(uni.getStorageSync('token'));
		this.user = uni.getStorageSync('userInfo');
		try {
			uni.showLoading({ title: "Loading...", mask: true });
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
		    uni.showToast({ title: 'Network Error', icon: 'none' })
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
  color: #2c3e50;
  font-size: 14px;
}
.add-card-btn {
  width: 100%;
  padding: 10px;
  background-color: #2c3e50;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
}
</style>
