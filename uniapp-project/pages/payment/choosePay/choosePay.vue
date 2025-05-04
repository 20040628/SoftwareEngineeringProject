<template>
	<view class="payment-container">
	  <view class="payment-header">
	    <text class="title">Select Payment Method</text>
	  </view>
	
	    <!-- 支付方式选择 -->
	    <view class="payment-method-section">
	      <text class="title">PAYMENT METHOD</text>
	      <view class="payment-options">
	        <view class="payment-option":class="{ selected: selectedPaymentMethod === 'card' }" @click="selectPaymentMethod('card')">
			  <view class="left">
			  	<image src="/static/icons/card.png" class="payment-icon" mode="aspectFill"></image>
			  	<text>Card</text>
			  </view>
	          <view class="check-icon" v-if="selectedPaymentMethod === 'card'">✔</view>
	        </view>
	        <view class="payment-option":class="{ selected: selectedPaymentMethod === 'Alipay' }" @click="selectPaymentMethod('Alipay')">
			  <view class="left">
				  <image src="/static/icons/alipay.png" class="payment-icon" mode="aspectFill"></image>
				  <text>Alipay</text>
			  </view>
	          <view class="check-icon" v-if="selectedPaymentMethod === 'Alipay'">✔</view>
	        </view>
			
	      </view>
		  <view class="payment-btn">
		      <button @click="submitPayment">Payment</button>
		  </view>
	  </view>
	</view>
	<!-- 卡号选择框弹窗 -->
	<view class="card-select-modal" v-if="showCardSelectModal">
	  <view class="card-options">
	    <view class="card-option"  @click="selectCard(card)">
	      <text>{{ bankCards.maskedCard }}</text>
	    </view>
		<button @click="bindNewCard">Bind New Card</button>
		<button @click="closeCardSelectModal">Close</button>
	  </view>
	  
	</view>
	<!-- 底部弹出支付密码输入框 -->
	<!-- <view class="payment-password-modal" v-if="showPasswordModal"> -->
	  <!-- <view class="password-container">
	    <text>Please enter your payment password</text>
	    <input type="password" v-model="password" placeholder="Enter password" />
	    <button @click="submitPaymentPassword">Submit Payment</button>
	    <button @click="cancelPayment">Cancel</button>
	  </view> -->
	  <pay-keyboard :show_key="show_key" @hideFun="hideFun" @getPassword="getPassword"></pay-keyboard>
	<!-- </view> -->

</template>

<script>
	import payKeyboard from '../../../components/keyboard.vue'
	export default {
		components:{
			payKeyboard
		},
	  data() {
	    return {
	      selectedPaymentMethod: null,
	      paymentSuccessful: false,
		  bankCards: null,                   // 模拟的银行卡数据
		        showCardSelectModal: false,  // 控制银行卡选择弹窗显示
		        showPasswordModal: false,   // 控制支付密码输入框显示
		        password: '',
				orderId:null,
				show_key:false
	    };
	  },
	  async onLoad(options){
	  	console.log('Query params:', options);
	    this.orderId = options.id;
		this.getCard();
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
			    this.bankCards = res.data
			} catch (err) {
			    uni.showToast({ title: '网络错误', icon: 'none' })
			} finally {
				uni.hideLoading();
			    this.isLoading = false
			}
		},
	    selectPaymentMethod(method) {
	      this.selectedPaymentMethod = method;
	    },
	    submitPayment() {
	      if (this.selectedPaymentMethod) {
				this.paymentSuccessful = true;
		    if (this.selectedPaymentMethod === 'card') {
				this.showCardSelectModal = true;
			} 
			if(this.selectedPaymentMethod === 'Alipay'){
				const targetUrl = `/pages/webview/webview?id=${this.orderId}`;
				uni.navigateTo({
					url:targetUrl
				})
			}
	      } else {
	        alert('Please select a payment method');
	      }
	    },
	    continueShopping() {
	      // Logic to continue shopping
	      this.paymentSuccessful = false;
	    },
		selectCard(card) {
		      this.selectedCard = card;
		      this.showCardSelectModal = false;
		      this.show_key=true;
		    },
		
		    // 绑定新银行卡
		    bindNewCard() {
		      // 模拟绑定新卡
		      this.bankCards.push({ cardNumber: '1111 2222 3333 4444' });
		      this.showCardSelectModal = false;
		    },
		
		    // 关闭银行卡选择框
		    closeCardSelectModal() {
		      this.showCardSelectModal = false;
		    },
		
		    // 提交支付密码
		    submitPaymentPassword() {
		      if (this.password) {
		        // 处理支付逻辑
		        alert('Payment Successful');
		        this.showPasswordModal = false;
		      } else {
		        alert('Please enter a valid password');
		      }
		    },
		
		    // 取消支付
		    cancelPayment() {
		      this.showPasswordModal = false;
		    },
			hideFun(){
				this.show_key = false
			},
			async getPassword(n){
				console.log("用户输入的密码",n.password)
				const token = String(uni.getStorageSync('token'));
				this.user = uni.getStorageSync('userInfo');
				try {
					uni.showLoading({ title: "加载中...", mask: true });
				    const res = await uni.request({
				        url: `${this.$baseURL}/api/bank-payment/${orderId}`,
				        method: 'POST',
						data:{
							securityCode:n.password
						},
						header: {
						  'Content-Type': 'application/json',
						  "Authorization": `Bearer ${token}`
						},
						
				    });
					
				    if (res.statusCode === 200) {
				      uni.showToast({
				        title: res.data.message,
				        icon: 'success',
				      });
				    } else {
				      uni.showToast({
				        title: res.data.message || 'Update failed',
				        icon: 'none',
				      });
				    }
				} catch (err) {
				    uni.showToast({ title: '网络错误', icon: 'none' })
				} finally {
					uni.hideLoading();
				    this.isLoading = false
				}
			}
	  }
	};

</script>

<style>
	.payment-container {
	   padding: 20px;
		background: #fff;
	    border-radius: 8px;
	    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
		margin: 30rpx 20rpx;
	}
	.payment-method-section {
	  margin-bottom: 20px;
	}
	
	.payment-header .title {
	  font-size: 18px;
	  font-weight: bold;
	  margin-bottom: 10rpx;
	}
	
	.payment-option {
	  display: flex;
	  align-items: center;
	  justify-content: space-between;
	  padding: 12px;
	  border: 1px solid #ddd;
	  border-radius: 8px;
	  margin-top: 10px;
	  cursor: pointer;
	}
	.left{
		display: flex;
		align-items: center;
		justify-content: flex-start;
	}
	.payment-option.selected {
	  font-weight: bold; /* 选中时加粗文本 */
	  border: #aaaaff solid 2rpx;
	}
	.check-icon {
	  font-size: 18px;
	  color: #0095FF; /* 勾选标记的颜色 */
	  margin-left: 10px; /* 勾选标记和文本之间的间距 */
	}
	
	.payment-option:hover {
	 border: #aaaaff solid 2rpx; 
	}
	
	.payment-icon {
	  width: 24px; /* 设置图标的宽度 */
	  height: 24px; /* 设置图标的高度 */
	  margin-right: 10px; /* 图标和文本之间的间距 */
	}
	
	.payment-text {
	  font-size: 16px;
	  color: #333;
	} 
	
	.payment-btn button {
	  width: 100%;
	  padding: 12px;
	  background-color: #aaaaff;
	  color: white;
	  font-size: 16px;
	  border: none;
	  border-radius: 8px;
	  cursor: pointer;
	  margin: 10rpx;
	}

	
	.card-select-modal {
	  position: fixed;
	  bottom: 0;
	  left: 0;
	  width: 100%;
	  background-color: rgba(0, 0, 0, 0.5);
	  display: flex;
	  justify-content: center;
	  align-items: flex-end; /* 弹窗固定在底部 */
	  padding: 20px;
	  box-sizing: border-box;
	}
	
	.card-options {
	  background-color: #fff;
	  padding: 20px;
	  border-radius: 8px;
	  width: 80%; /* 弹窗宽度 */
	  max-width: 400px; /* 最大宽度 */
	  max-height: 400px; /* 最大高度 */
	  overflow-y: auto; /* 超过最大高度时出现滚动条 */
	  display: flex;
	  flex-direction: column; /* 垂直排列卡号选项 */
	}
	
	.card-option {
	  display: flex;
	  justify-content: space-between; /* 水平方向上分布内容 */
	  align-items: center;
	  padding: 12px;
	  border: 1px solid #ddd;
	  border-radius: 8px;
	  margin-bottom: 12px; /* 卡号项之间的间距 */
	  cursor: pointer;
	  transition: background-color 0.3s ease;
	}
	
	.card-option:hover {
	  background-color: #f0f0f0;
	}
	
	.card-option:last-child {
	  margin-bottom: 0; /* 去掉最后一项的下边距 */
	}
	
	.card-options button {
	  background-color: #aaaaff;
	  color: #fff;
	  padding: 12px;
	  width: 100%;
	  border-radius: 5px;
	  margin-top: 15px;
	  cursor: pointer;
	  font-size: 10px;
	  transition: background-color 0.3s ease;
	}


</style>