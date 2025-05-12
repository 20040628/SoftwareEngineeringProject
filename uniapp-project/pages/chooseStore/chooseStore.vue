<template>
  <view class="container">
    <view class="title">Please Select a Store</view>

    <view v-if="stores.length > 0" class="store-list">
      <view 
        v-for="(store, index) in stores" 
        :key="store.id" 
        :class="['store-item', selectedStore?.id === store.id ? 'selected' : '']"
        @click="selectStore(store)"
      >
        <image src='/static/icons/store.png' class="icon" />
        <view class="store-info">
          <view class="store-name">{{ store.name }}</view>
          <view class="store-address">{{ store.locationName }}</view>
        </view>
      </view>
    </view>

    <view v-else class="loading">Loading stores...</view>

    <button type="primary" class="confirm-btn" @click="confirmSelection" :disabled="!selectedStore">
      Confirm
    </button>
  </view>
</template>

<script>
export default {
  data() {
    return {
      stores: [],
      selectedStore: null,
    };
  },
  async onLoad() {
    await this.loadStores();
	for (const store of this.stores) {
		await this.reverseGeocode(store);
	}
  },
  methods: {
    async loadStores() {
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
          this.stores=res.data
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
    selectStore(store) {
      this.selectedStore = store;
    },
    confirmSelection() {
      uni.setStorageSync('selectedStore', this.selectedStore);
      uni.navigateBack();
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
  },
};
</script>

<style>
.container {
  padding: 20rpx;
}
.title {
  font-size: 36rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
}
.store-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}
.store-item {
  display: flex;
  align-items: center;
  padding: 20rpx;
  border: 1rpx solid #ccc;
  border-radius: 16rpx;
}
.store-item.selected {
  border-color: #007aff;
  background-color: #e6f7ff;
}
.icon {
  width: 60rpx;
  height: 60rpx;
  margin-right: 20rpx;
}
.store-info {
  flex: 1;
}
.store-name {
  font-size: 28rpx;
  font-weight: 600;
}
.store-address {
  font-size: 24rpx;
  color: #666;
}
.confirm-btn {
  margin-top: 40rpx;
}
.confirm-btn {
  background-color: #2c3e50 !important;
  color: #fff;
  border: none;
}

.confirm-btn:disabled {
  background-color: #ccc !important;
  color: #999;
}

.loading {
  text-align: center;
  color: #999;
}
</style>
