<template>
  <view>
    <!-- 地图容器 -->
    <map 
      id="map"
      :latitude="mapCenter.latitude"
      :longitude="mapCenter.longitude"
      :markers="stores"
      :show-location="true"
      style="width: 100%; height: 90vh;"
      @markertap="handleMarkerTap"
    > 
      <!-- 自定义定位按钮 -->
      <cover-view class="custom-controls">
        <cover-image 
          src="/static/icons/current_location.png"
          @tap="centerToUser"
          class="control-btn"
        />
      </cover-view> 
    </map>

    <!-- 推荐地点的下拉框 -->
    <picker 
          mode="selector" 
          :range="locationNames" 
          @change="onLocationChange"
          class="location-picker"
        >
          <view class="picker-btn">
            <text>{{ selectedLocation }}</text>
          </view>
    </picker>
    <!-- 地址信息卡片 -->
    <cover-view v-if="selectedStore" class="address-card">
      <view class="card-content">
        <text class="store-name">{{ selectedStore.name }}</text>
        <text class="store-location">{{ selectedStore.locationName }}</text>
        <button class="choose-btn" @tap="chooseStore">Choose</button>
      </view>
    </cover-view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      stores: [],
      userLocation: null,
      mapCenter: {
        latitude: 30.7656,  
        longitude: 103.9799
      },
      recommendedLocations: [],
      selectedLocation: 'near stores',
      selectedStore: null, // 用于显示地址信息卡片
	   locationNames: [] ,
    };
  },
  async mounted() {
    await this.loadScooters();
    await this.getUserLocation();
	await this.recommendStore();
	this.stores.forEach(store => {
	    this.reverseGeocode(store);
	});
	this.extractLocationNames();
  },
  methods: {
    // 加载滑板车信息
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
            width: 40,
            height: 40
          }));
        } else {
          uni.showToast({ title: '数据加载失败', icon: 'none' });
        }
      } catch (err) {
        uni.showToast({ title: '网络错误', icon: 'none' });
      }
    },

    // 获取用户位置
    async getUserLocation() {
      try {
        const res = await uni.getLocation({ type: 'gcj02' });
        this.userLocation = res;
        this.mapCenter = res;
      } catch (err) {
        console.error('获取定位失败:', err);
      }
    },

    // 点击地图标记，显示地址信息
    handleMarkerTap(e) {
      const markerId = e.detail.markerId;
      const store = this.stores.find(s => s.id === markerId);
      this.selectedStore = store;
    },

    // 选择推荐地点时更新地图位置
    onLocationChange(e) {
      const index = e.detail.value;
      const selected = this.recommendedLocations[index];
	  this.selectedStore = selected.store;
	  this.reverseGeocode(this.selectedStore)
      this.selectedLocation = selected.store.name;
      this.mapCenter = {
        latitude: selected.store.latitude,
        longitude: selected.store.longitude
      };
    },

    // 回到用户当前位置
    centerToUser() {
      if (this.userLocation) {
        this.mapCenter = this.userLocation;
      }
    },
	async reverseGeocode(store){
		 uni.request({
		    url: `https://restapi.amap.com/v3/geocode/regeo?output=json&location=${store.longitude},${store.latitude}&key=5f722ef9e435cec7d4dba6f5daba0030&radius=1000&extensions=all&language=en`,
		    success: (res) => {
		      // console.log('高德逆地理编码返回数据：', res.data);
		      if (res.data.status === '1') {
		        // 动态为 scooter 对象添加 locationName 属性
		        this.$set(store, 'locationName', res.data.regeocode.pois[1].name);
				this.$set(store, 'locationNum', res.data.regeocode.formatted_address);
		      } else {
		        this.$set(store, 'locationName', '无法获取位置');
		        console.error('无法获取位置，返回数据:', res.data);
		      }
		    },
		    fail: (err) => {
		      this.$set(store, 'locationName', '位置请求失败');
		      console.error('逆地理编码请求失败:', err);
		    }
		  });
		
	},
	 chooseStore() {
	      uni.setStorageSync('selectedStore', this.selectedStore); // 保存选中的地址
	      uni.navigateBack(); // 返回主页
	    },
	async recommendStore(){
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
			      this.recommendedLocations=res.data
			    } else {
			      uni.showToast({ title: '数据加载失败', icon: 'none' });
			    }
			  } catch (err) {
			    uni.showToast({ title: '网络错误', icon: 'none' });
			  }
		},
		 extractLocationNames() {
		      this.locationNames = this.recommendedLocations.map(item => {
		        return `${item.store.name} : ${item.distance.toFixed(2)} km`; // 拼接名称和距离
		      });
		      this.selectedLocation = this.locationNames[0]; // 默认选择第一个地点
		    },
  },
};
</script>

<style scoped>
.custom-controls {
  position: absolute;
  right: 30rpx;
  bottom: 200rpx;
}

.control-btn {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.2);
}

.location-picker {
  position: absolute;
  top: 50rpx;  /* 调整位置，使其从顶部下移 */
  left: 50%;
  transform: translateX(-50%);
  z-index: 1000;
  width: 85%;  /* 调整宽度，确保适应不同屏幕 */
  background-color: rgba(255, 255, 255, 0.9);  /* 提高透明度，使背景色稍微深一点 */
  padding: 20rpx 10rpx;  /* 添加上下内边距，增加点击区域 */
  border-radius: 10rpx;  /* 增加圆角 */
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);  /* 添加阴影效果 */
}

.picker-btn {
   font-size: 18px;  /* 增大字体 */
    color: #333;  /* 保持文字颜色为深色 */
    text-align: center;
    padding: 12rpx;  /* 增加上下内边距，增大按钮点击区域 */
    border-radius: 8rpx;  /* 圆角按钮 */
  }
  
  .picker-btn text {
    font-weight: 500;  /* 加粗文字 */
  }


.address-card {
  position: absolute;
    bottom: 120rpx;
    left: 50%;
    transform: translateX(-50%);
    background-color: rgba(255, 255, 255, 0.9);
    padding: 30rpx;
    width: 80%; /* 调整宽度为80% */
    border-radius: 10rpx;
    box-shadow: 0 0 10rpx rgba(0, 0, 0, 0.1);
}

.card-content {
  font-size: 16px;
  color: #333;
  text-align: left;
  display: block; 
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
}

.choose-btn {
  margin-top: 15rpx;
  padding: 10rpx 20rpx;
  background-color: #aaaaff;
  color: white;
  border: none;
  border-radius: 5rpx;
  font-size: 16px;
}
</style>
