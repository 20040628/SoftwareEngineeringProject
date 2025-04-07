<template>
	<view class="container">
		<swiper 
			class="swiper" 
			indicator-dots="true" 
			indicator-color="#ccc" 
			indicator-active-color="red" 
			autoplay="true"
			interval="3000" 
			duration="500" 
			circular="true">
			<swiper-item 
			  v-for="(item, index) in bannerList" 
			  :key="index" 
			>
			  <image :src="item.url" lazy-load="true"></image>
			</swiper-item>
		</swiper>
		<!-- 顶部搜索栏 -->
		<view class="soso-box">
			<view class="soso-input-box">
			  <view class="soso-icon">
				<image mode="widthFix" :src="sosoImg"></image>
			  </view>
			  <input
				type="text"
				v-model="sosoTxt"
				@confirm="getList"
				placeholder="Search nearby scooters"
			  />
			</view>
		</view>
		<!-- 底部车辆列表 -->
		<view class="scooter-list">
		 <!-- 选择器 -->
			<view class="screening-boxA">
				<view v-for="(item, index) in screeningAList" :key="index" @click="screeningA(index)" class="screeningA-con"
					:class="item.type?'screeningA-con-active':''">{{item.name}}
				</view>
			</view>
			<view class="pricing-table">
			   <view 
				 v-for="(option, index) in pricingOptions" 
				 :key="index" 
				 class="pricing-card"
			   >
				 <text class="plan-price">{{ option.price }}</text>
				 <text class="plan-duration">/{{ option.duration }}</text>
			   </view>
			</view>
			<view class="container-time">
				<!-- 开始时间选择器 -->
				<view class="time-picker">
				  <text>Select start time:</text>
				  <picker mode="date" value="{{ startTime }}" @change="onStartTimeChange">
					<view class="picker">
					  <text>{{ startTime }}</text>
					</view>
				  </picker>
				</view>
				<!-- 预定时长选择器 -->
				<view class="duration-picker">
					<text>Select scheduled duration:</text>
					<view class="duration-options">
						<view
							class="duration-option" 
							:class="{ selected: selectedDuration === 0 }" 
							@click="onDurationSelect(0)">
							1hour
						</view>
						<view 
							class="duration-option" 
							:class="{ selected: selectedDuration === 1 }" 
							@click="onDurationSelect(1)">
							4hours
						</view>
						<view 
							class="duration-option" 
							:class="{ selected: selectedDuration === 2 }" 
							@click="onDurationSelect(2)">
							1day
						</view>
						<view 
							class="duration-option" 
							:class="{ selected: selectedDuration === 3 }" 
							@click="onDurationSelect(3)">
							1week
						</view>
					</view>
				</view>
				<!-- 查询按钮 -->
				<view class="query-btn">
				  <button @click="queryAvailableScooters">Query</button>
				</view>
			</view>
			<view v-for="scooter in filteredScooters" :key="scooter.id" class="scooter-item">
				<!-- <image :src="scooter.image" class="scooter-image"/> -->
				<view class="scooter-info">
				  <text class="location">{{ scooter.locationName }}</text>
				  <view class="location-name">
						<image src="/static/icons/location_name.png" class="location-icon" />
						<text>{{ scooter.locationNum }}</text>
				  </view>
				  <button class="reserve-btn" @click="showScooterDetail(scooter)">Reserve</button>
				</view>
			</view>
		</view>
	</view>
</template>
<script>
export default {
  data() {
    return {
      searchKeyword: '',
      scooters:[],
	  isLoading:true,
	  options: ['all', 'available'], // 选项列表
	  selectedIndex: 0, // 默认选择索引
	  selectedOption: 'all', // 默认选择的值
	  bannerList: [
	          { url: '/static/images/banner1.png'},
	          { url: '/static/images/banner2.png'},
	          { url: '/static/images/banner3.png'}
	        ],
	 screeningAList: [
				{
					'id': 0,
					'name': 'All',
					'type': true
				},
				{
					id: 1,
					'name': 'Available',
					'type': false
				}
		],
	pricingOptions: [
			      { duration: 'hour', price: '$5.00' },
			      { duration: 'day', price: '$20.00' },
			      { duration: 'week', price: '$100.00' }
		],
	sosoTxt: "", // 搜索内容
	sosoImg: "/static/icons/search.png", // 搜索图标路径	
	startTime: '',  // 开始时间
	selectedDuration: 0,  // 默认选择第一个选项
    }
  },
  computed: {
    filteredScooters() {
     return this.selectedOption === 'all'
        ? this.scooters
        : this.scooters.filter(scooter => scooter.status === 1);
    }
  },
  async mounted() {
     await this.loadScooters();
	 this.scooters.forEach(scooter => {
	     this.reverseGeocode(scooter);
	 });
	 
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

	// 跳转到详情页
	showScooterDetail(scooter) {
	  // 跳转到详情页，传递 scooter.id 作为参数
	  uni.navigateTo({
	    url: `/pages/cardetail/cardetail?id=${scooter.id}`
	  })
	},
	screeningA(index) {
		this.screeningAList.forEach((item, i) => {
			item.type = (i === index); // 只有被点击的索引变成 true
		});
		this.selectedOption = this.options[index]
	},
	async reverseGeocode(scooter){
		 uni.request({
		    url: `https://restapi.amap.com/v3/geocode/regeo?output=json&location=${scooter.longitude},${scooter.latitude}&key=5f722ef9e435cec7d4dba6f5daba0030&radius=1000&extensions=all&language=en`,
		    success: (res) => {
		      // console.log('高德逆地理编码返回数据：', res.data);
			

		      if (res.data.status === '1') {
		        // 动态为 scooter 对象添加 locationName 属性
		        this.$set(scooter, 'locationName', res.data.regeocode.pois[1].name);
				this.$set(scooter, 'locationNum', res.data.regeocode.formatted_address);
		      } else {
		        this.$set(scooter, 'locationName', '无法获取位置');
		        console.error('无法获取位置，返回数据:', res.data);
		      }
		    },
		    fail: (err) => {
		      this.$set(scooter, 'locationName', '位置请求失败');
		      console.error('逆地理编码请求失败:', err);
		    }
		  });
		
	},
	 // 处理开始时间选择变化
	onStartTimeChange(e) {
	    this.startTime = e.detail.value;  // 获取选择的开始时间
	},
	onDurationSelect(index) {
	      this.selectedDuration = index;  // 更新选中的预定时长
	}
	
  }
}
</script>

<style scoped>
.container {
  position: relative;
  height: 100vh;
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
.soso-box {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20rpx;
  margin: 10rpx;
}

.soso-input-box {
  display: flex;
  align-items: center;
  border: 2px solid #7f669d;
  border-radius: 50rpx;
  height: 60rpx;
  width: 600rpx;
  padding: 0 20rpx;
  background: #fff;
}

.soso-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 10rpx;

  image {
    width: 32rpx;
    height: 32rpx;
  }
}

input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 28rpx;
  height: 72rpx;
  line-height: 72rpx;
  background: transparent;
}

.screening-boxA {
		display: flex;
		justify-content: space-between;
		background: #fff;
		padding: 20rpx;

		.screeningA-con {
			width: 120rpx;
			border-radius: 50rpx;
			color: #999;
			font-size: 24rpx;
			text-align: center;
			height: 48rpx;
			line-height: 48rpx;
		}

		.screeningA-con-active {
			background-image: linear-gradient(to right, #A289C1, #745994);
			color: #fff;
			font-size: 20rpx;
		}
	}
.container-time {
  padding: 20px;
  /* border: 1px solid black;
  margin: 5rpx;
  border-radius: 10rpx; */
  border: 1px solid rgba(127, 102, 157, .5);
  border-radius: 16rpx;
  background: rgba(252, 250, 255, 1);
  margin: 20rpx;
  box-shadow: 0 0 5px .5px rgba(127, 102, 157, .2);
}

.time-picker{
  margin-bottom: 20px;
}

.picker {
  padding: 10px;
  border: 1px solid #ddd;
  display: flex;
  justify-content: space-between;
}
.duration-picker {
  margin-bottom: 20px;
}

.duration-options {
  display: flex;
  justify-content: space-between;
}

.duration-option {
  padding: 10px;
  border: 2px solid #ddd;
  border-radius: 5px;
  text-align: center;
  width: 22%;  /* 每个选项宽度占 1/4 */
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin: 5px;;
}

.duration-option.selected {
  border-color:#aaaaff;
}

.query-btn button {
  padding: 5px 10px;
  background-color: #aa55ff;
  color: white;
  border: none;
  border-radius: 5px;
  width: 100%;
}	
.pricing-table {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  padding: 10px;
}

.pricing-card {
  flex: 1;
  background: #fff;
  border-radius: 10px;
  padding: 10px;
  text-align: center;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  margin: 5px;
}

.plan-duration {
  font-weight: bold;
  font-size: 16px;
  color: #333;
}

.plan-price {
  font-size: 18px;
  color: #ff5722;
  font-weight: bold;
  margin-top: 5px;
}
.scooter-list {
  margin: 10rpx;
  background: #fff;
  border-radius: 40rpx 40rpx 0 0;
  padding: 30rpx;
  box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.1);
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
.location {
  font-size: 20px;
  color: #333;
  font-weight: bold;
  margin-bottom: 9px;
}

.location-name {
   display: flex;
   font-size: 13px;
   color: #666;
   margin-bottom: 8px;
}

.location-icon {
  width: 18px;
  height: 18px;
  margin-right: 5px;
 flex-shrink: 0; 
}

.reserve-btn {
    background-color: #aaaaff;
    color: white;
    padding: 5px 20px;  /* 调整按钮的内边距 */
    border: none;
    border-radius: 4px;  /* 方形按钮 */
    font-size: 14px;
    cursor: pointer;
    width: 100%; /* 使按钮宽度占满容器 */
    transition: background-color 0.3s ease;
}

.reserve-btn:hover {
  background-color: #aa55ff;
}

</style>