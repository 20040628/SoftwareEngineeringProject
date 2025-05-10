<template>
	<view class="payment-container">
		<view class="payment-header">
			<text class="title">Select Payment Method</text>
		</view>

		<!-- Payment method selection -->
		<view class="payment-method-section">
			<view class="payment-options">
				<view class="payment-option" :class="{ selected: selectedPaymentMethod === 'card' }"
					@click="selectPaymentMethod('card')">
					<view class="left">
						<image src="/static/icons/quick.png" class="payment-icon" mode="aspectFill"></image>
						<text>Quick Payment</text>
					</view>
					<view class="check-icon" v-if="selectedPaymentMethod === 'card'">✔</view>
				</view>
				<view class="payment-option" :class="{ selected: selectedPaymentMethod === 'Alipay' }"
					@click="selectPaymentMethod('Alipay')">
					<view class="left">
						<image src="/static/icons/card.png" class="payment-icon" mode="aspectFill"></image>
						<text>New Card Payment</text>
					</view>
					<view class="check-icon" v-if="selectedPaymentMethod === 'Alipay'">✔</view>
				</view>

			</view>
			<view class="payment-btn">
				<button @click="submitPayment">Payment</button>
			</view>
		</view>
	</view>
	<view class="card-select-modal" v-if="showCardSelectModal">
		<view class="card-options">
			<view class="form-group" v-for="(item, index) in fields" :key="index">
				<text class="label">{{ item.label }} <text class="required">*</text></text>
				<input class="input-field" :placeholder="item.placeholder" v-model="form[item.key]" />
			</view>
			<view class="form-group">
				<text class="label">Start Date<text class="required">*</text></text>
				<picker mode="date" fields="month" @change="onStartChange">
					<view class="picker">{{ startDate || 'Select MM/YYYY' }}</view>
				</picker>
			</view>
			<view class="form-group">
				<text class="label">Expiry Date <text class="required">*</text></text>
				<picker mode="date" fields="month" @change="onExpiryChange">
					<view class="picker">{{ expiryDate || 'Select MM/YYYY' }}</view>
				</picker>
			</view>
			<button @click="submitNew()">submit</button>
			<button @click="closeCardSelectModal">Close</button>
		</view>
	</view>
	<view class="card-select-modal" v-if="showQuickCardModal">
		<view class="card-options">
			<view class="form-group">
				<text class="label">Card Number<text class="required">*</text></text>
				<input class="input-field" placeholder="Enter card number" v-model="quickCard" />
			</view>
			<button @click="submit()">submit</button>
			<button @click="closeQuickCardModal">Close</button>
		</view>
	</view>

</template>

<script>
	export default {
		data() {
			return {
				selectedPaymentMethod: null,
				paymentSuccessful: false,
				bankCards: null,
				cardNumber: null,
				showCardSelectModal: false,
				showQuickCardModal:false,
				orderId: null,
				quickCard:'',
				form: {
					number: '',
					code: ''
				},
				expiryDate: '',
				startDate: '',
				fields: [{
						key: 'number',
						label: 'Card Number',
						placeholder: 'Enter card number'
					},
					{
						key: 'code',
						label: 'Card Security Code',
						placeholder: 'CVV'
					}
				]
			};
		},
		async onLoad(options) {
			this.orderId = options.id;
			this.getCard();
		},
		methods: {
			async getCard() {
				const token = String(uni.getStorageSync('token'));
				this.user = uni.getStorageSync('userInfo');
				try {
					uni.showLoading({
						title: "Loadinh...",
						mask: true
					});
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
					uni.showToast({
						title: 'Network Error',
						icon: 'none'
					})
					uni.hideLoading();
				} finally {
					uni.hideLoading();
				}
			},
			selectPaymentMethod(method) {
				this.selectedPaymentMethod = method;
			},
			submitPayment() {
				if (this.selectedPaymentMethod) {
					this.paymentSuccessful = true;
					if (this.selectedPaymentMethod === 'card') {
						this.showQuickCardModal = true;
					}
					if (this.selectedPaymentMethod === 'Alipay') {
						this.showCardSelectModal = true;
					}
				} else {
					alert('Please select a payment method');
				}
			},

			onExpiryChange(e) {
				this.expiryDate = e.detail.value;
			},
			onStartChange(e) {
				this.startDate = e.detail.value;
			},
			async submitNew(){
				const cardRegex = /^[0-9]{13,16}$/;
				if (!this.form.number) {
					uni.showToast({
						title: 'Card number is required',
						icon: 'none'
					});
					return;
				} else if (!cardRegex.test(this.form.number)) {
					uni.showToast({
						title: 'Card number must be between 13-16 digits',
						icon: 'none'
					});
					return;
				}
				if (!/^\d{3}$/.test(this.form.code)) {
					uni.showToast({
						title: 'The security code should be three digits',
						icon: 'none'
					});
					return;
				}
				
				if (!this.startDate || !this.expiryDate) {
					uni.showToast({
						title: 'Please select the date.',
						icon: 'none'
					});
					return;
				}
				const now = new Date();
				const start = new Date(this.startDate + '-01');
				const expiry = new Date(this.expiryDate + '-01');
				if (start >= expiry) {
					uni.showToast({
						title: 'The start date cannot be later than or equal to the end date',
						icon: 'none'
					});
					return;
				}
				if (expiry < new Date(now.getFullYear(), now.getMonth(), 1)) {
					uni.showToast({
						title: 'The start date cannot be earlier than the current month',
						icon: 'none'
					});
					return;
				}
				const token = String(uni.getStorageSync('token'));
				this.user = uni.getStorageSync('userInfo');
				try {
					const res = await uni.request({
						url: `${this.$baseURL}/api/bank-payment/newCard/${this.orderId}`,
						method: 'POST',
						data: {
							bankCard: this.form.number
						},
						header: {
							'Content-Type': 'application/json',
							"Authorization": `Bearer ${token}`
						},
					});
					if (res.statusCode === 200) {
						uni.showToast({
							title: 'Payment successful',
							icon: 'none',
						});
						this.closeCardSelectModal();
						uni.navigateTo({
							url: "/pages/myorder/orderlist/orderlist"
						})
					} else {
						uni.showToast({
							title: res.data || 'Update failed',
							icon: 'none',
						});
					}
				} catch (err) {
					uni.showToast({
						title: 'Network Error',
						icon: 'none'
					})
				} finally {}
				
			},
			async submit() {
				const token = String(uni.getStorageSync('token'));
				this.user = uni.getStorageSync('userInfo');
				try {
					const res = await uni.request({
						url: `${this.$baseURL}/api/bank-payment/${this.orderId}`,
						method: 'POST',
						data: {
							bankCard: this.quickCard
						},
						header: {
							'Content-Type': 'application/json',
							"Authorization": `Bearer ${token}`
						},
					});
					if (res.statusCode === 200) {
						uni.showToast({
							title: 'Payment successful',
							icon: 'none',
						});
						this.closeCardSelectModal();
						uni.navigateTo({
							url: "/pages/myorder/orderlist/orderlist"
						})
					} else {
						uni.showToast({
							title: res.data || 'Update failed',
							icon: 'none',
						});
					}
				} catch (err) {
					uni.showToast({
						title: 'Network Error',
						icon: 'none'
					})
				} finally {}


			},
			closeCardSelectModal() {
				this.showCardSelectModal = false;
			},
			closeQuickCardModal(){
				this.showQuickCardModal = false;
			}

		}
	};
</script>

<style>
	.payment-container {
		padding: 40rpx 30rpx;
		background: #fff;
		border-radius: 20rpx;
		box-shadow: 0 6rpx 16rpx rgba(0, 0, 0, 0.08);
		margin: 30rpx 20rpx;
	}

	.payment-header .title {
		font-size: 36rpx;
		font-weight: 600;
		margin-bottom: 20rpx;
		color: #2c3e50;
		text-align: center;
	}

	.payment-method-section {
		margin-top: 20rpx;
	}

	.payment-options {
		display: flex;
		flex-direction: column;
		gap: 20rpx;
	}

	.payment-option {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 30rpx;
		border: 2rpx solid #e0e0e0;
		border-radius: 20rpx;
		background-color: #f9f9f9;
		transition: all 0.3s ease;
		margin-bottom: 10rpx;
	}

	.payment-option:hover {
		border-color: #3498db;
		background-color: #f0f8ff;
	}

	.payment-option.selected {
		border-color: #3498db;
		background-color: #e6f2fc;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.06);
	}

	.left {
		display: flex;
		align-items: center;
	}

	.payment-icon {
		width: 40rpx;
		height: 40rpx;
		margin-right: 20rpx;
	}

	.check-icon {
		font-size: 28rpx;
		color: #3498db;
		font-weight: bold;
	}

	.payment-btn button {
		width: 100%;
		padding: 30rpx;
		background-color: #2c3e50;
		color: white;
		font-size: 32rpx;
		font-weight: bold;
		border: none;
		border-radius: 20rpx;
		margin-top: 40rpx;
		transition: background-color 0.3s ease;
	}

	.card-select-modal {
		position: fixed;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-color: rgba(0, 0, 0, 0.5);
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.card-options {
		background: #fff;
		padding: 40rpx 30rpx;
		border-radius: 24rpx;
		width: 80%;
		box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.15);
	}

	.form-group {
		margin-bottom: 30rpx;
	}

	.label {
		display: block;
		margin-bottom: 10rpx;
		font-size: 28rpx;
		font-weight: bold;
		color: #2c3e50;
	}

	.required {
		color: red;
		font-weight: normal;
		margin-left: 6rpx;
	}

	.input-field {
		box-sizing: border-box;
		width: 100%;
		padding: 20rpx;
		height: 80rpx;
		border: 1px solid #ccc;
		border-radius: 10rpx;
		font-size: 28rpx;
	}

	.picker {
		padding: 20rpx;
		border: 1px solid #ccc;
		border-radius: 16rpx;
		background: #f5f5f5;
		font-size: 28rpx;
		color: #333;
	}

	.button-group,
	.card-options button {
		margin-top: 20rpx;
	}

	.card-options button {
		background-color: #2c3e50;
		color: #fff;
		padding: 24rpx;
		width: 100%;
		border: none;
		border-radius: 16rpx;
		font-size: 30rpx;
		cursor: pointer;
		transition: opacity 0.3s ease;
	}
</style>