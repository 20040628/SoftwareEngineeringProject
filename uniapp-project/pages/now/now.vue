<template>
	<view class="container">
		<map id="map" :latitude="userLocation.latitude" :longitude="userLocation.longitude" :markers="stores"
			:show-location="true" style="width: 100%; height: 70vh;" @markertap="handleMarkerTap">
		</map>
		<view class="bottom">
			<view v-show="!hasOngoingOrder" class="car-card">
				<view>
					<view class="hint">There are no vehicles in use yet.</view>
					<view class="choose-btn-appointment" @tap="appointment">Make An Appointment</view>
				</view>
			</view>
			<view v-show="hasOngoingOrder" class="card-content">
				<view v-if='order' class="order-wrapper">
					<view class="item-left">
						<image :src="picUrl" class="order-img" mode="aspectFill"></image>
					</view>
					<view class="item-right">
						<view class="car-info">
							<view class="car-battery">
								🔋{{ order.scooter?.battery || 0 }}%
							</view>
							<view class="car-speed">
								⚡ {{ order.scooter?.speed || 0 }} km/h
							</view>
						</view>

						<view class="time-info">
							<view class="start-time">
								<uni-icons type="calendar" size="18" color="#666" style="margin-right: 8rpx;" />
								Start Time: {{ formatTime(order.startTime) }}
							</view>
							<view class="end-time">
								<uni-icons type="calendar" size="18" color="#666" style="margin-right: 8rpx;" />
								End Time: {{ formatTime(order.endTime) }}
							</view>
						</view>
						<view :class="['overtime-info', { visible: isOvertime }]">⛔ Overdue return</view>
						<view class="choose-btn" @tap="returnCar()">Return</view>
					</view>
				</view>
			</view>
		</view>
	</view>


</template>

<script>
	export default {
		data() {
			return {
				stores: [],
				userLocation: {
					latitude: 30.7656,
					longitude: 103.9799
				},
				recommendedLocations: [],
				selectedLocation: 'near stores',
				selectedStore: null, 
				locationNames: [],
				order: null,
				picUrl: "../../../static/images/car.jpg",
				currentTime: new Date(),
				endTime: new Date(),
				hasOngoingOrder: false
			};
		},
		async onLoad() {

		},
		computed: {
			isOvertime() {
				return this.currentTime > this.endTime;
			}
		},
		methods: {
			load() {
				console.log("Load method is called");
				this.loadScooters();
				if (this.order.scooter?.endTime) {
					this.endTime = new Date(this.order.scooter.endTime);
				}

			},

			async loadScooters() {
				const user = uni.getStorageSync('userInfo');
				const token = String(uni.getStorageSync('token'));
				try {
					const res = await uni.request({
						url: `${this.$baseURL}/api/bookings/getAllOngoing/${user.userId}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json',
							"Authorization": `Bearer ${token}`
						},
					});
					if (res.statusCode === 200) {
						if (res.data.length > 0) {
							this.order = res.data[0]
						} else {
							this.order = null
						}
						if (this.order) {
							this.hasOngoingOrder = true;
							const store = this.order.scooter.store;
							this.stores = [{
								...store,
								iconPath: "/static/icons/available_scooter.png",
								width: 40,
								height: 40
							}];
						} else {
							this.hasOngoingOrder = false;
						}
					} else {
						uni.showToast({
							title: 'Data loading failed',
							icon: 'none'
						});
					}
				} catch (err) {
					uni.showToast({
						title: 'Network Error',
						icon: 'none'
					});
				}
			},
			appointment() {
				uni.navigateTo({
					url: '/pages/index'
				})
			},
			handleMarkerTap(e) {
				const markerId = e.detail.markerId;
				const store = this.stores.find(s => s.id === markerId);
				this.selectedStore = store;
			},

			async getUserLocation() {
				try {
					const res = await uni.getLocation({
						type: 'gcj02'
					});
					this.userLocation = res;
					this.mapCenter = res;
				} catch (err) {
					console.error('Failed to obtain the positioning:', err);
				}
			},

			formatTime(time) {
				const date = new Date(time);
				return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
			},

			async returnCar() {
				const user = uni.getStorageSync('userInfo');
				const token = String(uni.getStorageSync('token'));
				console.log("order", this.order)
				try {
					const res = await uni.request({
						url: `${this.$baseURL}/api/bookings/return`,
						method: 'POST',
						data: {
							orderId: this.order.id
						},
						header: {
							'Content-Type': 'application/json',
							"Authorization": `Bearer ${token}`
						},
					});
					console.log("res", res.data)
					if (res.statusCode === 200) {
						uni.showToast({
							title: res.data.message,
							icon: 'none',
						});
						this.load();
						
					} else {
						uni.showToast({
							title: res.data.message || 'Update failed',
							icon: 'none',
						});
					}
				} catch (err) {
					uni.showToast({
						title: 'Network Error',
						icon: 'none'
					});
				}
			},
		}
	}
</script>

<style scoped>
	.container {
		display: flex;
		flex-direction: column;
		height: 100vh;
	}

	.bottom {
		flex: 1;
		height: 30vh;
	}

	.car-card {
		position: absolute;
		bottom: 190rpx;
		left: 50%;
		transform: translateX(-50%);
		padding: 30rpx;
		width: 80%;
		border-radius: 10rpx;
	}


	.hint {
		font-size: 16px;
		color: #000;
		text-align: center;
	}

	.choose-btn-appointment {
		text-align: center;
		margin-top: 15rpx;
		line-height: 60rpx;
		background-color: #014d87;
		color: white;
		border: none;
		border-radius: 5rpx;
		font-size: 16px;
	}

	.card-content {
		position: fixed;
		bottom: 100rpx;
		left: 50%;
		transform: translateX(-50%);
		background-color: #ffffffdd;
		padding: 30rpx;
		width: 90%;
		border-radius: 16rpx;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
	}

	.order-wrapper {
		display: flex;
		align-items: flex-start;
	}

	.order-img {
		width: 140rpx;
		height: 140rpx;
		border-radius: 12rpx;
		object-fit: cover;
	}

	.item-left {
		flex-shrink: 0;
	}

	.item-right {
		flex-grow: 1;
		margin-left: 24rpx;
	}

	.car-info {
		display: flex;
		justify-content: space-between;
		font-size: 26rpx;
		color: #014d87;
		margin-top: 8rpx;
	}

	.car-battery,
	.car-speed {
		display: flex;
		align-items: center;
	}

	.time-info {
		display: flex;
		flex-direction: column;
		font-size: 24rpx;
		color: #666;
		margin-top: 16rpx;
		gap: 8rpx;
	}

	.start-time,
	.end-time {
		display: flex;
		align-items: center;
	}

	.overtime-info {
	  height: 20rpx; 
	  color: red;
	  font-size: 26rpx;
	  margin-top: 10rpx;
	  opacity: 0;
	  visibility: hidden;
	  transition: opacity 0.3s;
	}
	
	.overtime-info.visible {
	  opacity: 1;
	  visibility: visible;
	}

	.choose-btn {
		margin-top: 24rpx;
		background-color: #014d87;
		color: white;
		font-size: 28rpx;
		border-radius: 12rpx;
		text-align: center;
		line-height: 72rpx;
	}
</style>