<template>
	<view class="container">
		<swiper class="swiper" indicator-dots="true" indicator-color="#ccc" indicator-active-color="red" autoplay="true"
			interval="3000" duration="500" circular="true">
			<swiper-item v-for="(item, index) in bannerList" :key="index">
				<image :src="item.url" lazy-load="true" alt="show the scooters"></image>
			</swiper-item>
		</swiper>
		<view class="scooter-list">
			<view class="container-time">
				<view class="example-body">
					<uni-datetime-picker type="datetime" :hide-second="true" v-model="datetimesingle"
						@change="changeLog" />
				</view>
				<view class="address-section" @click="goToSelectPage()">
					<image src="/static/icons/location.png" class="icon" alt="Location icon" />
					<view class="address">
						<text class="title">Pick-up Position</text>
						<text class="detail" v-if="selectedSite">{{ selectedSite.name || 'Please choose' }}</text>
					</view>
					<image src="/static/icons/arrow_right.png" class="arrow" alt="arrow icon" />
				</view>
				<!-- Reservation duration selector -->
				<view class="duration-picker">
					<text>Select Scheduled Duration:</text>
					<view class="duration-options">
						<view class="duration-option" :class="{ selected: selectedDuration === 0 }"
							@click="onDurationSelect(0)">
							1 Hour
						</view>
						<view class="duration-option" :class="{ selected: selectedDuration === 1 }"
							@click="onDurationSelect(1)">
							4 Hours
						</view>
						<view class="duration-option" :class="{ selected: selectedDuration === 2 }"
							@click="onDurationSelect(2)">
							1 Day
						</view>
						<view class="duration-option" :class="{ selected: selectedDuration === 3 }"
							@click="onDurationSelect(3)">
							1 Week
						</view>
					</view>
				</view>
				<!-- Query button -->
				<view class="query-btn">
					<button @click="queryAvailableScooters">Query</button>
				</view>
			</view>
			<view class="instruction" @click="instruction()">
				<view class="text">
					Instruction To Use
				</view>
				<uni-icons type="right" size="20" color="white" class="arrow"></uni-icons>
			</view>
			<view class="discount-container">
				<view class="discount-title">Discount Policies</view>

				<view class="table">
					<view class="table-header">
						<view class="header-item">Discount Type</view>
						<view class="header-item">Details</view>
					</view>

					<view class="table-row">
						<view class="row-item">Student (15%)</view>
						<view class="row-item">
							For users aged 18-25
						</view>
					</view>

					<view class="table-row">
						<view class="row-item">Senior Citizen (20%)</view>
						<view class="row-item">
							For users aged 60+
						</view>
					</view>

					<view class="table-row">
						<view class="row-item">Frequent User (10%)</view>
						<view class="row-item">
							For users renting scooters 8+ hours/week
						</view>
					</view>

					<view class="note">
						*Discount Stacking: Multiple discounts can be combined
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
				scooters: [],
				bannerList: [{
						url: '/static/images/banner1.png'
					},
					{
						url: '/static/images/banner2.png'
					},
					{
						url: '/static/images/banner3.png'
					}
				],
				carImg: "/static/images/scooter.png",
				speedImg: "/static/icons/speed.png",
				batteryImg: "/static/icons/battery.png",
				scooterImg: "/static/icons/scooter.png",
				startTime: '',
				startDate: '',
				selectedDuration: 0,
				durationOptions: [{
						label: '1 Hour',
						type: 'hour'
					},
					{
						label: '4 Hours',
						type: 'FOUR_HOURS'
					},
					{
						label: '1 Day',
						type: 'day'
					},
					{
						label: '1 Week',
						type: 'week'
					}
				],
				datetimesingle: Date.now(),
				selectedSite: null,
				cates: [],
				active: 0,
				secondData: []

			}
		},
		watch: {
			datetimesingle(newval) {
				console.log("load");
				uni.setStorageSync("datetimesingle", this.datetimesingle)
			},
		},
		mounted() {
			this.loadSelectedSite();
			if (uni.getStorageSync('datetimesingle')) {
				this.datetimesingle = uni.getStorageSync('datetimesingle');
			}
		},
		onLoad() {
			uni.setLocale('en');
		},

		methods: {
			changeLog(e) {
				console.log('change:', e);
			},
			loadSelectedSite() {
				const selectedSite = uni.getStorageSync('selectedStore');
				if (selectedSite) {
					this.selectedSite = selectedSite;
				} else {
					this.selectedSite = null;
				}
			},
			onDurationSelect(index) {
				this.selectedDuration = index;
			},

			goToSelectPage(e) {
				uni.navigateTo({
					url: '/pages/chooseStore/chooseStore'
				});
			},
			queryAvailableScooters() {
				const hireType = this.durationOptions[this.selectedDuration].type.toUpperCase();
				let startTimeStr = this.datetimesingle + ':00';
				let startDate = new Date(startTimeStr);
				let endDate;
				switch (hireType) {
					case 'HOUR':
						endDate = new Date(startDate.getTime() + 1 * 60 * 60 * 1000);
						break;
					case 'FOUR_HOURS':
						endDate = new Date(startDate.getTime() + 4 * 60 * 60 * 1000);
						break;
					case 'DAY':
						endDate = new Date(startDate.getTime() + 1 * 24 * 60 * 60 * 1000);
						break;
					case 'WEEK':
						endDate = new Date(startDate.getTime() + 7 * 24 * 60 * 60 * 1000);
						break;
					default:
						endDate = startDate;
				}
				let formattedStartDate = startDate.getFullYear() + '-' +
					(startDate.getMonth() + 1).toString().padStart(2, '0') + '-' +
					startDate.getDate().toString().padStart(2, '0') + ' ' +
					startDate.getHours().toString().padStart(2, '0') + ':' +
					startDate.getMinutes().toString().padStart(2, '0') + ':' +
					startDate.getSeconds().toString().padStart(2, '0');
				let formattedDate = endDate.getFullYear() + '-' +
					(endDate.getMonth() + 1).toString().padStart(2, '0') + '-' +
					endDate.getDate().toString().padStart(2, '0') + ' ' +
					endDate.getHours().toString().padStart(2, '0') + ':' +
					endDate.getMinutes().toString().padStart(2, '0') + ':' +
					endDate.getSeconds().toString().padStart(2, '0');
				uni.setStorageSync('hireType', hireType);
				uni.setStorageSync('startTime', formattedStartDate);
				uni.setStorageSync('endTime', formattedDate);
				if(formattedStartDate && this.selectedSite){
					if(new Date(formattedStartDate).getTime() > Date.now()){
						uni.navigateTo({
							url: '/pages/chooseCar/chooseCar'
						});
					}else{
						uni.showToast({
							title: 'The start time can not be earlier than current time',
							icon: 'none'
						})
					}
				}else{
					uni.showToast({
						title: 'Please select both date and store',
						icon: 'none'
					})
				}
			},
			instruction(e) {
				uni.navigateTo({
					url: '/pages/information/instruction/instruction'
				});
			}

		}
	}
</script>

<style scoped lang="scss">
	.container {
		position: relative;
		box-sizing: border-box;
	}

	.swiper {
		width: 100%;
		height: 180px;
	}

	.swiper image {
		width: 100%;
		height: 100%;
		object-fit: cover;
	}


	.container-time {
		padding: 20px;
		margin: 5rpx;
		border-radius: 10rpx;
		border: 1.5px solid var(--bg-color);
		border-radius: 16rpx;
		margin: 20rpx;
		box-shadow: 0 0 5px .5px rgba(127, 102, 157, .2);
	}

	.time-picker {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		margin-bottom: 20rpx;
		padding: 20rpx;
		background: #fff;
		border-radius: 12rpx;
		box-shadow: 0 2rpx 6rpx rgba(0, 0, 0, 0.1);
	}

	.time-picker-title {
		font-size: 28rpx;
		color: #333;
		margin-bottom: 15rpx;
	}

	.picker-container {
		display: flex;
		justify-content: space-between;
		width: 100%;
	}

	.picker {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 15rpx;
		background: #f7f7f7;
		border-radius: 8rpx;
		margin-right: 10rpx;
		width: 100%;
		box-sizing: border-box;
	}

	.picker text {
		font-size: 30rpx;
		color: #666;
	}

	.picker:hover {
		background: #e8e8e8;
		cursor: pointer;
	}

	.picker:active {
		background: #d0d0d0;
	}

	.picker:last-child {
		margin-right: 0;
	}

	.duration-picker {
		margin-bottom: 20px;
	}

	.duration-options {
		display: flex;
		justify-content: space-between;
		flex-wrap: wrap;
	}

	.duration-option {
		padding: 10px;
		border: 2px solid #ddd;
		border-radius: 5px;
		text-align: center;
		width: 30%;
		cursor: pointer;
		transition: background-color 0.3s ease;
		margin: 5px;
		;
	}

	.duration-option.selected {
		border-color: var(--button-bg-color);
	}

	.query-btn button {
		padding: 5px 10px;
		background-color: #34495e;
		color: white;
		border: none;
		border-radius: 30rpx;
		width: 100%;
	}

	.scooter-list {
		margin: 10rpx;
	}

	.address-section {
		display: flex;
		align-items: center;
		padding: 30rpx;
		background: #f5f5f5;
		border-radius: 16rpx;
		margin: 20rpx;

		.icon {
			width: 48rpx;
			height: 48rpx;
			margin-right: 20rpx;
		}

		.address {
			flex: 1;

			.title {
				display: block;
				color: #000000;
				font-size: 24rpx;
			}

			.detail {
				font-size: 28rpx;
			}
		}
	}

	.arrow {
		width: 30rpx;
		height: 30rpx;
		margin-left: 20rpx;
	}

	.instruction {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin: 30rpx;
		background: linear-gradient(135deg, #2c3e50, #34495e);
		border-radius: 20rpx;
		padding: 24rpx 36rpx;
		box-shadow: 0 6rpx 16rpx rgba(0, 0, 0, 0.15);
		transition: transform 0.2s ease, box-shadow 0.2s ease;
	}

	.instruction:hover {
		transform: scale(1.04);
		box-shadow: 0 10rpx 24rpx rgba(0, 0, 0, 0.2);
	}

	.text {
		font-size: 34rpx;
		font-weight: 600;
		color: #fff;
		margin-right: 16rpx;
		letter-spacing: 1rpx;
	}

	.arrow {
		transform: translateY(-2rpx);
	}

	.discount-container {
		margin: 30rpx;
		padding: 32rpx 24rpx;
		background-color: #ffffff;
		border-radius: 20rpx;
		box-shadow: 0 6rpx 18rpx rgba(0, 0, 0, 0.08);
	}

	.discount-title {
		font-size: 36rpx;
		font-weight: 700;
		color: #2c3e50;
		margin-bottom: 28rpx;
		text-align: center;
	}

	.table {
		width: 100%;
	}

	.table-header {
		display: flex;
		background-color: #2c3e50;
		color: #ffffff;
		padding: 20rpx;
		border-radius: 12rpx 12rpx 0 0;
		font-size: 28rpx;
		font-weight: bold;
	}

	.header-item {
		flex: 1;
		text-align: center;
	}

	.table-row {
		display: flex;
		background-color: #f9f9f9;
		border-bottom: 1rpx solid #e0e0e0;
		padding: 18rpx;
		font-size: 26rpx;
		color: black;
	}

	.row-item {
		flex: 1;
		text-align: left;
		line-height: 1.6;
	}

	.note {
		font-size: 26rpx;
		color: black;
		margin-top: 20rpx;
		text-align: center;
		font-style: italic;
	}
</style>