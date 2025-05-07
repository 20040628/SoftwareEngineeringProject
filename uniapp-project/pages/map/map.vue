<template>
  <view class="canvas-wrap">
	<map 
	      id="map"
	      :latitude="mapCenter.latitude"
	      :longitude="mapCenter.longitude"
	      :markers="stores"
	      :show-location="true"
	      style="width: 100%; height: 90vh; z-index: 10;"
	      @markertap="handleMarkerTap"
	    > 
			<cover-view class="picker-btn" @click="open">
			  {{ selectedLocation }}
			</cover-view>
			
		
			<cover-view v-show="showDialog" class="dialog-seletion">
				<cover-view class="selection-item">Recommended Stores:</cover-view>
				<cover-view v-for="(item, index) in locationNames" class="selection-item" @click="selectOneLocation(item, index)">{{item}}</cover-view>
			</cover-view>
			<!-- 地址信息卡片 -->
			  <cover-view v-if="showSelectedStore" class="address-card">
			    <cover-view class="card-content">
			      <cover-view class="store-name">{{ selectedStore ? selectedStore.name : '' }}</cover-view>
			      <cover-view class="store-location">{{ selectedStore ? selectedStore.locationName : '' }}</cover-view>
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
      selectedStore: null, // 用于显示地址信息卡片
	   locationNames: [] ,
    };
  },
  async mounted() {
    await this.loadScooters();
    // await this.getUserLocation();
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
            width: 20,
            height: 20
          }));
        } else {
          uni.showToast({ title: '数据加载失败', icon: 'none' });
        }
      } catch (err) {
        uni.showToast({ title: '网络错误', icon: 'none' });
      }
    },
	open() {
		this.showDialog = true
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
	selectOneLocation(item, index) {
		const selected = this.recommendedLocations[index];
		this.selectedStore = selected.store;
		this.reverseGeocode(this.selectedStore)
		this.showDialog = false
		this.selectedLocation = selected.store.name;
		this.showSelectedStore = true
		this.mapCenter = {
		  latitude: selected.store.latitude,
		  longitude: selected.store.longitude
		};
	},
    // 选择推荐地点时更新地图位置
    onLocationChange(e) {
      const index = e.detail.value;
      const selected = this.recommendedLocations[index];
	  this.selectedStore = selected.store;
	  this.reverseGeocode(this.selectedStore)
      this.selectedLocation = selected.store.name;
	  this.showSelectedStore = true
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
		    }
  },
};
</script>

<style scoped>
.picker-btn {
	   position: absolute;
	   top: 50rpx;  /* 调整位置，使其从顶部下移 */
	   left: 50%;
	   transform: translateX(-50%);
	   width: 85%;  /* 调整宽度，确保适应不同屏幕 */
	   background-color: rgba(255, 255, 255, 0.9);  /* 提高透明度，使背景色稍微深一点 */
	   border-radius: 10rpx;  /* 增加圆角 */
	   /* box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);  /* 添加阴影效果 */ 
		text-align: center;
		font-size: 30rpx;
		border: 8rpx solid #2c3e50;
		font-weight: bold;
}
.dialog-seletion {
	position: absolute;
	left: 50%;
	top: 50%;
	transform: translate(-50%, -50%);
	background-color: rgba(255, 255, 255, 0.9);
	padding: 30rpx;
	width: 80%; /* 调整宽度为80% */
	border-radius: 10rpx;
	box-shadow: 0 0 10rpx rgba(0, 0, 0, 0.1);
}
.selection-item {
	color: #333;
	font-size: 16px;
}
.address-card {
  position: absolute;
    bottom: 120rpx;
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
</style>
