<template>
	<view class="container">
		<view class="information">
			<view class="store">
				{{selectedSite.name}}
			</view>
			<view class="time">
				{{startTime}} → {{endTime}}
			</view>
		</view>

		<view v-if="scooters.length>0" v-for="scooter in scooters" :key="scooter.id" class="scooter-item ">
			<view class="listA-content">
				<view class="listA-boxC">
					<view class="box-left">
						<view class="info">
							<view class="chargin-type">
								<image mode="widthFix" :src="speedImg" class="icon"></image>
								<text class="speed-text">{{scooter.speed || '0.00'}} m/s</text>
							</view>
							<view class="chargin-type">
								<image mode="widthFix" :src="batteryImg" class="icon"></image>
								<text class="battery-text">{{scooter.battery || '0.00'}} %</text>
							</view>
						</view>
						<view class="price">
							<view class="price-item">
								<text class="price-value">{{ getPrice(scooter) || '0.00' }}
									£/{{ getPriceLabel()  }}</text>
							</view>
							<button class="reserve-btn" @click="showScooterDetail(scooter)">Reserve</button>
						</view>
					</view>
					<view class="site-image">
						<image mode="heightFix" :src="carImg" class="car-img"></image>
					</view>
				</view>
				<view class="listA-boxB">
					<image mode="widthFix" :src="scooterImg" class="car-img"></image>
					<text class="scooter-id">{{scooter.id}}</text>
				</view>
			</view>
		</view>
		<view v-else-if="error" class="none">
			{{error}}
		</view>
		<view class="none" v-else>
			NO Scooters to user, please query again
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				scooters: [],
				isLoading: true,
				carImg: "/static/images/scooter.png",
				speedImg: "/static/icons/speed.png",
				batteryImg: "/static/icons/battery.png",
				scooterImg: "/static/icons/scooter.png",
				startTime: '',
				endTime: '',
				selectedSite: null,
				storeID: null,
				hireType: null,
				currentUser: {
					userId: null
				},
				user: null,
				error: null
			}
		},
		async onLoad() {
			this.hireType = uni.getStorageSync('hireType');
			this.startTime = uni.getStorageSync('startTime');
			this.endTime = uni.getStorageSync('endTime');
			this.selectedSite = uni.getStorageSync('selectedStore');
			const userInfo = uni.getStorageSync('userInfo');
			if (userInfo) {
				this.currentUser.userId = userInfo.userId || null;
			}
			await this.queryAvailableScooters();
		},

		methods: {
			async queryAvailableScooters() {
				try {
					const token = String(uni.getStorageSync('token'));
					this.user = uni.getStorageSync('userInfo');
					let startTime = '';
					if (this.startDate && this.startTime) {
						const startDateTime = `${this.startDate} ${this.startTime}`;
						startTime = startDateTime.replace('T', ' ') + ':00';
					}

					const res = await uni.request({
						url: `${this.$baseURL}/api/scooters/getScootersAvailable/${this.user.userId}/${this.selectedSite.id}`,
						method: 'POST',
						data: {
							hireType: this.hireType,
							startTime: this.startTime
						},
						header: {
							'Content-Type': 'application/json',
							"Authorization": `Bearer ${token}`
						},
					});
					console.log("scooter", res.data)

					if (res.statusCode === 200) {
						this.scooters = res.data;
					} else {
						this.error = res.data
						console.log("this.error", this.error)
						uni.showToast({
							title: `${res.data}`,
							icon: 'none'
						});
					}
				} catch (err) {
					console.error('Network error:', err);
					uni.showToast({
						title: 'Network Error',
						icon: 'none'
					});
				} finally {
					this.isLoading = false;
				}
			},

			showScooterDetail(scooter) {
				uni.navigateTo({
					url: `/pages/cardetail/cardetail?id=${scooter.id}`
				})
			},
			getPrice(scooter) {
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



		}
	}
</script>

<style scoped>
	.information {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		padding: 20rpx;
		margin: 20rpx 5rpx;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
		transition: transform 0.2s ease;
	}


	.store {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 12rpx;
		text-align: center;
	}

	.time {
		font-size: 28rpx;
		color: #666;
		text-align: center;
	}

	.time::before {
		content: "Time: ";
		font-weight: 600;
		color: #555;
	}
	
	.arrow-icon {
		width: 16rpx;
		height: 16rpx;
		margin-left: 10rpx;
	}

	.scooter-item {
		display: flex;
		align-items: center;
		flex-direction: row;
		padding: 30rpx;
		background: #f8f8f8;
		border-radius: 20rpx;
		margin-bottom: 20rpx;
	}

	.scooter-info {
		display: flex;
		justify-content: space-between;
		flex-direction: column;
		width: 100%;
		padding: 0 10px;
		flex-grow: 1;
		align-items: flex-start;
	}

	.reserve-btn {
		background-color: #014d87;
		color: white;
		padding: 5px;
		border: none;
		border-radius: 4px;
		font-size: 15px;
		cursor: pointer;
		width: 100%;
		transition: background-color 0.3s ease;
		margin-top: 25rpx;
	}

	.reserve-btn:hover {
		background-color: #014d87;
	}

	.listA-content {
		background-color: #fff;
		padding: 25rpx 20rpx;
		margin-bottom: 20rpx;
		border-radius: 8rpx;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
		transition: all 0.3s ease;
		display: flex;
		justify-content: space-between;
		flex-direction: column;
		width: 100%;
		padding: 0 10px;
		flex-grow: 1;
	}

	.listA-boxC {
		display: flex;
		justify-content: space-between;
		margin: 15rpx 0;
		flex-wrap: nowrap;
		flex-direction: row-reverse
	}

	.box-left {
		width: 70%;
	}

	.info {
		display: flex;
		align-items: center;
		margin-bottom: 10rpx;
		justify-content: space-between;
	}

	.chargin-type {
		display: flex;
		align-items: center;
		margin-bottom: 10rpx;
	}

	.icon {
		width: 32rpx;
		height: 32rpx;
		margin-right: 10rpx;
	}

	.speed-text,
	.battery-text {
		font-size: 28rpx;
	}

	.speed-text {
		color: #1c315e;
	}

	.battery-text {
		color: #1c315e;
	}

	.price-item {
		display: flex;
		justify-content: space-between;
		margin-bottom: 10rpx;
	}

	.price-label {
		font-size: 24rpx;
		color: #666;
	}

	.price-value {
		font-size: 28rpx;
		color: #333;
		font-weight: bold;
	}


	.site-image {
		width: 45%;
		height: 170rpx;
	}

	.car-img {
		width: 100%;
		height: 100%;
		object-fit: cover;
		border-radius: 12rpx;
		box-shadow: 0 4rpx 8rpx rgba(0, 0, 0, 0.1);
	}

	.listA-boxB {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 15rpx;
		color: #999;
	}

	.listA-boxB image {
		width: 32rpx;
		height: 32rpx;
		margin-right: 10rpx;
	}

	.scooter-id {
		font-size: 24rpx;
		color: #333;
	}

	.none {
		color: #B9B9B9;
		text-align: center;
	}
</style>