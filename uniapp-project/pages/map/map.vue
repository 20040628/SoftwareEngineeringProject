<template>
	<view class="canvas-wrap">
		<map id="map" :latitude="mapCenter.latitude" :longitude="mapCenter.longitude" :markers="stores"
			:show-location="true" style="width: 100%; height: 90vh; z-index: 10;" @markertap="handleMarkerTap">
			<cover-view class="custom-controls">
				<cover-image src="/static/icons/current_location.png" @tap="centerToUser" class="control-btn" />
			</cover-view>
			<cover-view class="picker-btn" @click="open">Recommended Stores
				<!-- {{ selectedLocation }} -->
			</cover-view>
			<cover-view v-show="showDialog" class="dialog-seletion">
				<cover-view class="selection-item">Recommended Stores:</cover-view>
				<cover-view v-for="(item, index) in locationNames" class="selection-item"
					@click="selectOneLocation(item, index)">{{item}}</cover-view>
			</cover-view>
			<!-- Address information card -->
			<cover-view v-if="showSelectedStore" class="address-card">
				<cover-view class="card-content">
					<cover-view class="store-name">{{ selectedStore ? selectedStore.name : '' }}</cover-view>
					<cover-view
						class="store-location">{{ selectedStore ? selectedStore.locationName : '' }}</cover-view>
					<cover-view class="choose-btn" @tap="chooseStore">Choose</cover-view>
				</cover-view>
			</cover-view>
		</map>

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
				mapCenter: {
					latitude: 30.7656,
					longitude: 103.9799
				},
				showDialog: false,
				recommendedLocations: [],
				selectedLocation: 'Recommended Stores',
				showSelectedStore: false,
				selectedStore: null,
				locationNames: [],
			};
		},
		async mounted() {
			await this.loadScooters();
			await this.getUserLocation();
			await this.recommendStore();
			for (const store of this.stores) {
				await this.reverseGeocode(store);
			}
			this.extractLocationNames();
		},
		methods: {
			async loadScooters() {
				try {
					const token = String(uni.getStorageSync('token'));
					const res = await uni.request({
						url: `${this.$baseURL}/api/stores/getAll`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json',
							"Authorization": `Bearer ${token}`
						},
					});

					if (res.statusCode === 200) {
						this.stores = res.data.map(store => ({
							...store,
							iconPath: "/static/icons/available_scooter.png",
							width: 20,
							height: 20
						}));
					} else {
						uni.showToast({
							title: 'Data Loading Failed',
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
			open() {
				this.showDialog = true
			},
			// Get the user's location
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

			// Click on the map marker to display the address information
			handleMarkerTap(e) {
				this.showDialog = false
				this.stores = this.stores.map(marker => ({
					...marker,
					iconPath: "/static/icons/available_scooter.png"
				}));
				const markerId = e.detail.markerId;
				const store = this.stores.find(s => s.id === markerId);
				this.selectedStore = store;
				this.selectedStore.iconPath = "/static/icons/choose.png"
				this.mapCenter = {
					latitude: this.selectedStore.latitude,
					longitude: this.selectedStore.longitude
				};
				this.showSelectedStore = true
			},

			selectOneLocation(item, index) {
				this.stores = this.stores.map(marker => ({
					...marker,
					iconPath: "/static/icons/available_scooter.png"
				}));
				const selected = this.recommendedLocations[index];
				const store = this.stores.find(s => s.id === selected.store.id);
				this.selectedStore = store;
				console.log("this.selectedStore", this.selectedStore)
				store.iconPath = "/static/icons/choose.png"
				this.showDialog = false
				this.selectedLocation = selected.store.name;
				this.showSelectedStore = true
				this.mapCenter = {
					latitude: selected.store.latitude,
					longitude: selected.store.longitude
				};
			},

			// Return to the current position of the user
			centerToUser() {
				if (this.userLocation) {
					this.mapCenter = this.userLocation;
				}
			},
			async reverseGeocode(store) {
				try {
					const res = await new Promise((resolve, reject) => {
						uni.request({
							url: `https://restapi.amap.com/v3/geocode/regeo?output=json&location=${store.longitude},${store.latitude}&key=5f722ef9e435cec7d4dba6f5daba0030&radius=1000&extensions=all&language=en`,
							success: resolve,
							fail: reject,
						});
					});

					if (res.data.status === '1') {
						this.$set(store, 'locationName', res.data.regeocode.pois[1]?.name || 'No POI found');
						this.$set(store, 'locationNum', res.data.regeocode.formatted_address);
					} else {
						this.$set(store, 'locationName', 'Unable to obtain the location');
					}
				} catch (err) {
					this.$set(store, 'locationName', 'Location request failed');
					console.error('The reverse geocoding request failed:', err);
				}
			},
			chooseStore() {
				uni.setStorageSync('selectedStore', this.selectedStore);
				uni.navigateTo({
					url: "/pages/index"
				})
			},
			async recommendStore() {
				try {
					const token = String(uni.getStorageSync('token'));
					const res = await uni.request({
						url: `${this.$baseURL}/api/stores/nearby?longitude=${this.userLocation.longitude}&latitude=${this.userLocation.latitude}`,
						method: 'GET',
						header: {
							'Content-Type': 'application/json',
							"Authorization": `Bearer ${token}`
						},
					});

					if (res.statusCode === 200) {
						this.recommendedLocations = res.data
					} else {
						uni.showToast({
							title: 'Data Loading Failed',
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
			extractLocationNames() {
				this.locationNames = this.recommendedLocations.map(item => {
					return `${item.store.name} : ${item.distance.toFixed(2)} km`;
				});
			}
		},
	};
</script>

<style scoped>
	.picker-btn {
		position: absolute;
		top: 50rpx;
		left: 50%;
		transform: translateX(-50%);
		width: 85%;
		background-color: rgba(255, 255, 255, 0.9);
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
		text-align: center;
		border: 8rpx solid #2c3e50;
		line-height: 60rpx;
		font-weight: bold;
		font-size: 40rpx;
		align-items: center;
	}

	.dialog-seletion {
		position: absolute;
		left: 50%;
		top: 40%;
		transform: translate(-50%, -50%);
		background-color: white;
		padding: 30rpx;
		width: 80%;
		border-radius: 30rpx;
		box-shadow: 0 0 10rpx rgba(0, 0, 0, 0.1);
		align-items: center;
	}

	.selection-item {
		display: block;
		width: 100%;
		margin: 10rpx 0;
		padding: 20rpx 0;
		background-color: #2c3e50;
		border-radius: 10rpx;
		color: #ffffff;
		font-size: 28rpx;
		text-align: center;
		box-shadow: 0 0 5rpx rgba(0, 0, 0, 0.1);

	}

	.selection-item:first-child {
		font-weight: bold;
		font-size: 32rpx;
		background-color: transparent;
		box-shadow: none;
		color: black;
	}

	.address-card {
		position: absolute;
		bottom: 100rpx;
		left: 50rpx;
		right: 50rpx;
		background-color: rgba(255, 255, 255, 0.9);
		padding: 30rpx;
		border-radius: 10rpx;
		box-shadow: 0 0 10rpx rgba(0, 0, 0, 0.1);
	}

	.card-content {
		font-size: 16px;
		color: #333;
		text-align: left;
	}

	.store-name {
		font-weight: bold;
		font-size: 18px;
		display: block;
	}

	.store-location {
		font-size: 14px;
		color: #777;
		margin-top: 5rpx;
		line-height: normal;
		white-space: normal;
	}

	.choose-btn {
		text-align: center;
		margin-top: 15rpx;
		line-height: 60rpx;
		background-color: #2c3e50;
		color: white;
		border: none;
		border-radius: 5rpx;
		font-size: 16px;
	}

	.custom-controls {
		position: absolute;
		right: 30rpx;
		bottom: 10rpx;
	}

	.control-btn {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.2);
	}
</style>