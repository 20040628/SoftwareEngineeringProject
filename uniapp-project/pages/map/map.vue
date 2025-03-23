<template>
	<!-- 地图容器 -->
	<map 
	  id="map"
	  :latitude="mapCenter.latitude"
	  :longitude="mapCenter.longitude"
	  :markers="scooters"
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
</template>

<script>
	export default {
	  data() {
	    return {
	     scooters:[],
		 userLocation: null,
		 mapCenter: {
		          latitude:  30.7656,  
		          longitude: 103.9799
		    },
	    }
	  },
	  async mounted() {
	       await this.loadScooters();
	},
	methods:{
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
		load: function(e) {
				console.log("load")
		},
		// 回到当前位置
		centerToUser() {
		  if (this.userLocation) {
		    this.mapCenter = this.userLocation
		  }
		},
		// 地图标记点击
		handleMarkerTap(e) {
			  const scooterId = e.detail.markerId
			  const scooter = this.scooters.find(s => s.id === scooterId)
			  this.showScooterDetail(scooter)
		},
		// 获取用户位置
		async getUserLocation() {
			  try {
			    const res = await uni.getLocation({ type: 'gcj02' })
			    this.userLocation = res
			    this.mapCenter = res
			  } catch (err) {
			    console.error('获取定位失败:', err)
			  }
		},
		showScooterDetail(scooter) {
			  // 跳转到详情页，传递 scooter.id 作为参数
			  uni.navigateTo({
			    url: `/pages/cardetail/cardetail?id=${scooter.id}`
			  })
			}
	}
}
</script>

<style>
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
</style>