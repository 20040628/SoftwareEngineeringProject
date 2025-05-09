<template>
	<view class="container">
		<!-- basic information -->
		<view class="info-section card">
			<view v-if="scooter" class="header">
				<text class="scooter-id">#{{ scooter.id }}</text>
				<view class="status-tag" :class="statusClass">
					{{ statusText }}
				</view>
			</view>

			<!-- price -->
			<view class="price-section" v-if="scooter">
				<text class="price">£{{ getPrice(scooter) }}</text>
				<text class="original-price" v-if="hasDiscount">£{{ getOriginalPrice(scooter) }}</text>
				<text class="unit">/ {{ getPriceLabel() }}</text>
			</view>

			<!-- address and time information -->
			<view class="address-section" v-if="scooter">
				<view class="notification-wrapper">
					<uni-icons type="notification" size="20" color="#ff4d4f"></uni-icons>
					<text class="note">Please carefully check the rental period and the information of the return
						service point</text>
				</view>
				<view class="information">
					<view class="label">
						<text class="label-text">Pick and Return</text>
						<text class="store-name">{{ selectedSite.name }}</text>
					</view>

					<view class="label">
						<text class="label-text">Location:</text>
						<text class="store-location">{{ selectedSite.locationName }}</text>
					</view>

					<view class="label">
						<text class="label-text">Time:</text>
						<text class="time-range">{{ startTime }} → {{ endTime }}</text>
					</view>
				</view>
			</view>

		</view>
		<view class="action-bar">
			<button @click="submitBooking" :disabled="isLoading">
				{{ isLoading ? 'In process...' : 'Confirming a Reservation' }}
			</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				scooter: null,
				scooterID: null,
				selectedDuration: 0,
				durationOptions: [],
				message: '',
				messageType: '',
				hireType: '',
				startTime: '',
				selectedSite: '',
				isLoading: true,
				currentUser: {
					userId: null
				},
				paymentUrl: '',
				hasDiscount: false,
			}
		},
		async onLoad(options) {
			this.hireType = uni.getStorageSync('hireType');
			this.startTime = uni.getStorageSync('startTime');
			this.endTime = uni.getStorageSync('endTime');
			this.selectedSite = uni.getStorageSync('selectedStore');
			this.reverseGeocode(this.selectedSite)
			this.scooterID = options.id;
			const userInfo = uni.getStorageSync('userInfo');
			if (userInfo) {
				this.currentUser.userId = userInfo.userId || null;
			}
			await this.loadScooterDetail();
		},
		computed: {
			statusClass() {
				const statusMap = {
					0: "unavailable",
					1: "available",
					2: "maintenance",
				};
				return statusMap[this.scooter.status] || "unknown"; // 默认值
			},

			statusText() {
				const textMap = {
					0: "unavailable",
					1: "available",
					2: "maintenance",
				};
				return textMap[this.scooter.status] || "unknown";
			}
		},


		methods: {
			getPrice(scooter) {
				if (this.hireType === 'HOUR') {
					return scooter.discountedPriceHour;
				} else if (this.hireType === 'FOUR_HOURS') {
					return scooter.discountedPriceFourHour;
				} else if (this.hireType === 'DAY') {
					return scooter.discountedPriceDay;
				} else if (this.hireType === 'WEEK') {
					return scooter.discountedPriceWeek;
				}
				return 0;
			},
			getOriginalPrice(scooter) {
				if (this.hireType === 'HOUR') {
					return scooter.priceHour;
				} else if (this.hireType === 'FOUR_HOURS') {
					return scooter.priceFourHour;
				} else if (this.hireType === 'DAY') {
					return scooter.priceDay;
				} else if (this.hireType === 'WEEK') {
					return scooter.priceWeek;
				}
				return 0;
			},

			// 返回价格的单位标签
			getPriceLabel() {
				switch (this.hireType) {
					case 'HOUR':
						return 'h';
					case 'FOUR_HOURS':
						return '4h';
					case 'DAY':
						return 'day';
					case 'WEEK':
						return 'week';
					default:
						return '';
				}
			},

			async loadScooterDetail() {
				const token = String(uni.getStorageSync('token'));
				try {
					uni.showLoading({
						title: "Loading...",
						mask: true
					});

					// Make the API request to get the scooter details
					const res = await uni.request({
						url: `${this.$baseURL}/api/scooters/${this.scooterID}?userId=${this.currentUser.userId}`,
						method: 'POST',
						header: {
							'Content-Type': 'application/json',
							"Authorization": `Bearer ${token}`,
						},
					});

					// Check if the response is successful
					if (res.statusCode === 200) {
						const data = res.data;
						// Update scooter details
						this.scooter = data
						this.hasDiscount = this.scooter.hasDiscount

					} else {
						uni.showToast({
							title: 'Loading failed. Please try again later',
							icon: 'none'
						});
					}

				} catch (err) {
					uni.showToast({
						title: 'Network Error',
						icon: 'none'
					});
				} finally {
					// Hide loading spinner and update loading state
					uni.hideLoading();
					this.isLoading = false;
				}
			},
			async reverseGeocode(store) {
				uni.request({
					url: `https://restapi.amap.com/v3/geocode/regeo?output=json&location=${store.longitude},${store.latitude}&key=5f722ef9e435cec7d4dba6f5daba0030&radius=1000&extensions=all&language=en`,
					success: (res) => {
						if (res.data.status === '1') {
							this.$set(store, 'locationName', res.data.regeocode.pois[1].name);
							this.$set(store, 'locationNum', res.data.regeocode.formatted_address);
						} else {
							this.$set(store, 'locationName', 'Unable to obtain the location');
							console.error('Unable to obtain the location:', res.data);
						}
					},
					fail: (err) => {
						this.$set(store, 'locationName', 'Location request failed');
						console.error('Reverse geocoding request failed:', err);
					}
				});
			},

			async submitBooking() {
				const token = String(uni.getStorageSync('token'));
				if (!this.startTime) {
					this.showMessage('Please select the start time', 'error');
					return;
				}
				if (!this.hireType) {
					this.showMessage('Please select the rental type', 'error');
					return;
				}

				this.isLoading = true;
				uni.showLoading({
					title: 'Under submission...'
				});

				try {
					const bookingData = {
						userId: this.currentUser.userId,
						scooterId: this.scooter.id,
						hireType: this.hireType,
						startTime: this.startTime
					};


					const res = await new Promise((resolve, reject) => {
						uni.request({
							url: `${this.$baseURL}/api/bookings`,
							method: 'POST',
							data: bookingData,
							header: {
								'Content-Type': 'application/json',
								"Authorization": `Bearer ${token}`
							},
							success: (response) => resolve(response),
							fail: (error) => reject(error)
						});
					});

					if (res.statusCode === 200) {
						this.showMessage('book successfully', 'success');
						uni.showToast({
							title: 'Book successful!',
							icon: 'none',
							duration: 2000 
						});
						await this.handlePay(res.data);
					} else {
						uni.showToast({
							title: res.data || 'Scheduled failure',
							icon: 'none',
							duration: 2000
						});
					}
				} catch (error) {
					uni.showToast({
						title: error.message || 'Scheduled failure',
						icon: 'none',
						duration: 2000
					});
				} finally {
					this.isLoading = false;
					uni.hideLoading();
				}
			},
			async handlePay(order) {
				uni.navigateTo({
					url: `/pages/payment/payh5?id=${order.orderId}`
				})
			},

			showMessage(text, type) {
				this.message = text;
				this.messageType = type;
				setTimeout(() => {
					this.message = '';
				}, 3000);
			}
		}


	}
</script>

<style scoped lang="scss">
	.container {
		padding: 24rpx;
		background: #f8f9fa;
	}

	.card {
		background: #fff;
		border-radius: 24rpx;
		padding: 32rpx;
		margin-bottom: 24rpx;
	}

	.info-section {
		padding: 30rpx;
	}

	.header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
	}

	.scooter-id {
		font-size: 40rpx;
		font-weight: 500;
		color: #666;
	}

	.status-tag {
		padding: 8rpx 20rpx;
		border-radius: 40rpx;
		font-size: 24rpx;

		&.available {
			background: #e8f5e9;
			color: #4CAF50;
		}

		&.unavailable {
			background: #fff3e0;
			color: #FF9800;
		}

	}

	.price-section {
		display: flex;
		align-items: baseline;
		margin: 40rpx 0;

		.price {
			font-size: 64rpx;
			color: #FF5722;
			font-weight: bold;
		}

		.original-price {
			font-size: 28rpx;
			color: #999;
			margin-left: 20rpx;
			text-decoration: line-through;
		}

		.unit {
			color: #666;
			margin-left: 20rpx;
			font-size: 28rpx;
		}
	}

	
	.address-section {
		padding: 20px 15px;
	}

	.notification-wrapper {
		display: flex;
		align-items: center;
		background-color: #fff3f0;
		padding: 12px 20px;
		border-radius: 25px 25px 0 0;
		border: 1px solid #ffccc7;
	}

	.note {
		font-size: 10px;
		color: #ff4d4f;
		margin-left: 10px;
		line-height: 1.5;
	}

	.information {
		border-radius: 15px;
		box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
		margin-bottom: 20px;
		background-color: #ffffff;
		font-size: 16px;
		color: #333;
		line-height: 1.6;
		padding: 20px 15px;
	}

	.label {
		margin-bottom: 15px;
	}

	.label-text {
		font-size: 14px;
		color: #555;
		font-weight: bold;
		margin-bottom: 5px;
		display: block;
	}

	.store-name {
		font-size: 18px;
		font-weight: bold;
		color: #333;
		margin-top: 5px;
	}

	.store-location {
		font-size: 14px;
		color: #888;
		line-height: 1.4;
	}

	.time-range {
		font-size: 12px;
		font-weight: 500;
		color: #333;
	}

	.action-bar {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		padding: 20rpx;

		button {
			background-color: #2c3e50;
			color: white;
		}
	}
</style>