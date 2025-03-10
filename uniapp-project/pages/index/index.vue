<template>
  <view class="container">
    <!-- 顶部搜索栏 -->
    <view class="search-bar">
      <input 
        class="search-input"
        placeholder="Search for nearby scooters"
        v-model="searchKeyword"
        @confirm="handleSearch"
      />
      <button class="location-btn" @click="centerToUser">
        <image src="/static/icons/location.png" class="location-icon"/>
      </button>
    </view>

    <!-- 地图容器 -->
    <map 
      id="map"
      :latitude="mapCenter.latitude"
      :longitude="mapCenter.longitude"
      :markers="scooters"
      :show-location="true"
      style="width: 100%; height: 70vh;"
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

    <!-- 底部车辆列表 -->
    <scroll-view 
      class="scooter-list"
      scroll-y
	  @scroll="adjustListHeight"
      :style="{ height: listHeight + 'px' }"
    >

      <view 
        v-for="scooter in scooters"
        :key="scooter.id"
        class="scooter-item"
		@click="showScooterDetail(scooter)"
      >
        <!-- <image :src="scooter.image" class="scooter-image"/> -->
        <view class="scooter-info">
          <text class="scooter-name">{{ scooter.id }}</text>
          <view class="details">
            <text class="distance">{{ scooter.location }}</text>
            <view class="battery">
              <!-- <image src="/static/icons/battery.png" class="battery-icon"/>
              <text>{{ scooter.battery }}%</text> -->
            </view>
          </view>
        </view>
		<button @click="changeScooterStatus(scooter.id)">
		  <text>change status</text>
		</button>
      </view>
    </scroll-view>
  </view>
</template>
<script>
export default {
  data() {
    return {
      mapCenter: {
        latitude: 39.9042,  
        longitude: 116.4074
      },
      userLocation: null,
      searchKeyword: '',
      scooters:[],
	  isLoading:true,
      listHeight: 300,
	  maxListHeight: 900, // 列表最大高度（单位 px）
	  minListHeight: 300
    }
  },
  // computed: {
  //   filteredScooters() {
  //     return this.scooters.filter(scooter => 
  //       scooter.name.includes(this.searchKeyword)
  //     ).map(scooter => ({
  //       ...scooter,
  //       iconPath: '/static/marker.png', // 地图标记图标
  //       width: 30,
  //       height: 40
  //     }))
  //   }
  // },
  async mounted() {
     // this.getUserLocation();
     this.calcListHeight();
     await this.loadScooters();

  },
  methods: {
	// 获取车辆列表
	async loadScooters() {
	  try {
	    // 发起 GET 请求来获取所有滑板车的列表数据
	    const res = await uni.request({
	      url: 'http://localhost:8080/api/scooters/getAll',  // 后端接口地址
	      method: 'GET'  // 请求方法为 GET
	    });
	
	    // 如果响应状态码为 200（请求成功）
	    if (res.statusCode === 200) {
	      this.scooters = res.data;  // 将后端返回的滑板车数据赋值给 scooters
		  this.scooters = res.data.map(scooter => ({
		        ...scooter, // 保留原始数据
		        iconPath: scooter.status === 1 
		        ? "/static/icons/available_scooter.png" 
		        : "/static/icons/in_use_scooter.png",
		        width: 40,  // 确保地图标记点有默认尺寸
		        height: 40
			}));
	    } else {
	      // 如果状态码不是 200，则显示加载失败的提示
	      uni.showToast({ title: '数据加载失败', icon: 'none' });
	    }
	  } catch (err) {
	    // 捕获并处理网络错误，显示提示
	    uni.showToast({ title: '网络错误', icon: 'none' });
	  } finally {
	    // 请求完成后，无论成功还是失败，都将 isLoading 设置为 false
	    this.isLoading = false;
	  }
	},
	
	async changeScooterStatus(id) {
	  try {
	    const res = await uni.request({
	      url: `http://localhost:8080/api/scooters/changeStatus/${id}`,
	      method: 'GET'
	    });
	    if (res.statusCode === 200) {
	      // 更新本地数据中的滑板车状态
	      const updatedScooter = res.data;
	      const index = this.scooters.findIndex(scooter => scooter.id === id);
	      if (index!== -1) {
	        this.scooters[index] = {
	         ...updatedScooter,
	          iconPath: updatedScooter.status === 1 
	           ? "/static/icons/available_scooter.png" 
	            : "/static/icons/in_use_scooter.png"
	        };
	      }
	      uni.showToast({ title: '状态更新成功', icon:'success' });
	    } else {
	      uni.showToast({ title: '状态更新失败', icon: 'none' });
	    }
	  } catch (err) {
	    uni.showToast({ title: '网络错误', icon: 'none' });
	  }
	},
	
	// 跳转到详情页
	showScooterDetail(scooter) {
	  // 跳转到详情页，传递 scooter.id 作为参数
	  uni.navigateTo({
	    url: `/pages/cardetail/cardetail?id=${scooter.id}`
	  })
	},


    //获取用户位置
    // async getUserLocation() {
    //   try {
    //     const res = await uni.getLocation({ type: 'gcj02' })
    //     this.userLocation = res
    //     this.mapCenter = res
    //   } catch (err) {
    //     console.error('获取定位失败:', err)
    //   }
    // },

    // 回到当前位置
    centerToUser() {
      if (this.userLocation) {
        this.mapCenter = this.userLocation
      }
    },
	adjustListHeight(event) {
	    const scrollTop = event.detail.scrollTop;
        const newHeight = Math.min(this.maxListHeight, Math.max(this.minListHeight, this.minListHeight + scrollTop / 3));
	    this.listHeight = newHeight;
	},
	// 计算列表高度
	calcListHeight() {
		  uni.getSystemInfo({
			success: (res) => {
			  this.listHeight = res.windowHeight * 0.3
			}
		  })
		},

	// 地图标记点击
	handleMarkerTap(e) {
		  const scooterId = e.detail.markerId
		  const scooter = this.scooters.find(s => s.id === scooterId)
		  this.showScooterDetail(scooter)
		},
	
	// 	// 搜索处理
	// handleSearch() {
	// 	  // 实际开发中可调用后端搜索接口
	// 	  console.log('搜索关键词:', this.searchKeyword)
	// 	}
	
				
	}
}
</script>

<style scoped>
.container {
  position: relative;
  height: 100vh;
}

.search-bar {
  position: absolute;
  top: 20rpx;
  left: 20rpx;
  right: 20rpx;
  z-index: 999;
  display: flex;
  gap: 15rpx;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 50rpx;
  padding: 20rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
}

.search-input {
  flex: 1;
  height: 60rpx;
  padding-left: 30rpx;
}

.location-btn {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  background: #fff;
  padding: 0;
}

.location-icon {
  width: 50rpx;
  height: 50rpx;
}

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

.scooter-list {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: #fff;
  border-radius: 40rpx 40rpx 0 0;
  padding: 30rpx;
  box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.1);
  z-index: 999;
}

.scooter-item {
  display: flex;
  align-items: center;
  padding: 30rpx;
  background: #f8f8f8;
  border-radius: 20rpx;
  margin-bottom: 20rpx;
}

.scooter-image {
  width: 200rpx;
  height: 160rpx;
  margin-right: 30rpx;
}

.scooter-info {
  flex: 1;
}

.scooter-name {
  font-size: 34rpx;
  font-weight: bold;
  color: #333;
}

.details {
  display: flex;
  justify-content: space-between;
  margin-top: 15rpx;
}

.distance {
  color: #666;
  font-size: 28rpx;
}

.battery {
  display: flex;
  align-items: center;
  color: #4CAF50;
}

.battery-icon {
  width: 30rpx;
  height: 30rpx;
  margin-right: 10rpx;
}
</style>