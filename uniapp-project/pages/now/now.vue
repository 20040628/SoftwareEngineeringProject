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
				<view v-if='order'>
					<view class="item-left">
						<image :src="picUrl" alt="Order Image" style="width: 70px; height: 70px; border-radius: 5px;">
						</image>
					</view>
					<!-- 车的详情 -->
					<view class="item-right">
						<view class="car-info">
							<view class="car-battery">battery: {{ order.scooter?.battery || 0 }}%</view>
							<view class="car-speed">speed: {{ order.scooter?.speed || 0 }} km/h</view>
						</view>
						<view class="time-info">
							<view class="start-time">start: {{ formatTime(order.startTime) }}</view>
							<view class="end-time">end: {{ formatTime(order.endTime) }}</view>
						</view>
						<view v-if="isOvertime" class="overtime-info">Timeout!</view>
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
				selectedStore: null, // 用于显示地址信息卡片
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
			// 判断是否超时
			isOvertime() {
				return this.currentTime > this.endTime;
			}
		},
		methods: {
			load() {
				console.log("Load method is called");
				// this.getUserLocation();
				this.loadScooters();
				console.log('hasOngoingOrder', this.hasOngoingOrder);
				console.log('order', this.order);
				if (this.order.scooter?.endTime) {
					this.endTime = new Date(this.order.scooter.endTime);
				}

			},
			// 加载滑板车信息
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
					console.log("res.data.", res.data)
					if (res.statusCode === 200) {
						if (res.data.length > 0) {
							this.order = res.data[0]
						} else {
							this.order = null
						}
						console.log("order", this.order)
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
							title: '数据加载失败',
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

			//获取用户位置
			async getUserLocation() {
				try {
					const res = await uni.getLocation({
						type: 'gcj02'
					});
					this.userLocation = res;
					this.mapCenter = res;
				} catch (err) {
					console.error('获取定位失败:', err);
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
	  overflow: auto;
	  height: 30vh;
	}

	.car-card {
		position: absolute;
		bottom: 190rpx;
		left: 50%;
		transform: translateX(-50%);
		/* background-color: rgba(255, 255, 255, 0.9); */
		padding: 30rpx;
		width: 80%;
		border-radius: 10rpx;
		/* box-shadow: 0 0 10rpx rgba(0, 0, 0, 0.1); */
	}

	.card-content {
		position: absolute;
		bottom: 190rpx;
		left: 50%;
		transform: translateX(-50%);
		background-color: rgba(255, 255, 255, 0.9);
		padding: 30rpx;
		width: 80%;
		/* 调整宽度为80% */
		border-radius: 10rpx;
		box-shadow: 0 0 10rpx rgba(0, 0, 0, 0.1);
		display: flex;
		justify-content: space-between;
		align-items: center;
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

	.item-left {
		width: 70px;
		height: 70px;
	}

	.item-right {
		flex-grow: 1;
		margin-left: 20rpx;
	}

	.car-info {
		display: flex;
		justify-content: space-between;
		font-size: 28rpx;
		color: #333;
	}

	.time-info {
		font-size: 24rpx;
		color: #666;
		margin-top: 10rpx;
	}

	.overtime-info {
		margin-top: 10rpx;
		color: red;
		font-size: 26rpx;
	}

	.choose-btn {
		margin-top: 20rpx;
		width: 100%;
		background-color: #014d87;
		color: white;
		font-size: 16px;
		border: none;
		border-radius: 8rpx;
		text-align: center;
		line-height: 60rpx;
	}

	.blurred {
		filter: blur(5px);
		pointer-events: none;
	}
</style>