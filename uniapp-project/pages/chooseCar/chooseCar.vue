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
		 <!-- 排序选择下拉框 -->
		<!-- <view class="sort-select">
		  <picker mode="selector" :range="sortOptions" :value="selectedSortIndex" @change="onSortChange">
		    <view class="sort-dropdown">
		      sort:{{ sortOptions[selectedSortIndex] }}
		      <image src="/static/icons/arrow-right.png" class="arrow-icon"/>
		    </view>
		  </picker>
		</view> -->
		<view v-for="scooter in scooters" :key="scooter.id" class="scooter-item ">
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
								<text class="price-value">{{ getPrice(scooter) || '0.00' }} £/{{ getPriceLabel()  }}</text>
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
	</view>
</template>

<script>
	export default {
	  data() {
	    return {
	    scooters:[],
		isLoading:true,
		carImg:"/static/images/scooter.png",
		speedImg:"/static/icons/speed.png",
		batteryImg:"/static/icons/battery.png",
		scooterImg:"/static/icons/scooter.png",
		startTime: '',  
		endTime:'',
		selectedSite: null,
		storeID:null,
		hireType:null,
		currentUser:{
			userId:null
		},
		user:null,
		 sortOptions: ['按电量排序', '按速度排序'], 
	    }
	  },
	  computed: {
	  	
	     sortedVehicles() {
	       if (this.selectedSortIndex === 0) {
	         return this.scooters.sort((a, b) => b.battery - a.battery); // 按电量降序排序
	       } else if (this.selectedSortIndex === 1) {
	         return this.scooters.sort((a, b) => b.speed - a.speed); // 按速度降序排序
	       } 
	       return this.scooters;
	     }
	   },
	  async onLoad(){
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
		
		    // // 确保 startTime 的格式正确
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
		
		    if (res.statusCode === 200) {
		      this.scooters = res.data;
		    } else {
		      uni.showToast({ title: `${res.data}`, icon: 'none' });
		    }
		  } catch (err) {
		    // 处理请求错误
		    console.error('Network error:', err);
		    uni.showToast({ title: '网络错误', icon: 'none' });
		  } finally {
		    this.isLoading = false;
		  }
		},
	  
	  	// 跳转到详情页
	  	showScooterDetail(scooter) {
	  	  // 跳转到详情页，传递 scooter.id 作为参数
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
		  
		  // 返回价格的单位标签
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

<style  scoped>
	.information {
	  display: flex;
	  flex-direction: column;
	  justify-content: center;
	  align-items: center;
	  padding: 20rpx;
	  border: 3px solid #aaaaff; /* 细边框 */
	  border-radius: 15rpx; /* 圆角效果，稍微圆润一点 */
	  background-color: #ffffff; /* 背景色 */
	  margin: 20rpx 5rpx; /* 上下间距 */
	  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1); /* 阴影效果 */
	  transition: transform 0.2s ease; /* 动画效果 */
	}
	
	
	.store {
	  font-size: 32rpx;
	  font-weight: bold;
	  color: #333;
	  margin-bottom: 12rpx;
	  text-align: center; /* 确保店铺名称居中 */
	}
	
	.time {
	  font-size: 28rpx;
	  color: #666;
	  text-align: center; /* 确保时间段居中 */
	}
	
	/* 可选：为时间添加更多的层次感 */
	.time::before {
	  content: "Time: ";
	  font-weight: 600;
	  color: #555;
	}

	/* 排序下拉框样式 */
	/* .sort-select {
	  display: flex;
	  justify-content: flex-end; /* 使内容靠左 */
/* 	  align-items: center;
	  margin-bottom: 20rpx;
	  width: auto; /* 让容器不铺满一行 */
	/* } */

	/* .sort-dropdown {
	  display: flex;
	  align-items: center;
	  font-size: 16rpx;
	  background-color: #f1f1f1;
	  border-radius: 5rpx;
	  padding: 10rpx 15rpx; /* 控制内边距 */
	/*  margin-right: 10rpx;
	  color: #333;
	} */

	.arrow-icon {
	  width: 16rpx;
	  height: 16rpx;
	  margin-left: 10rpx; /* 调整箭头和文字之间的间距 */
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
    background-color: #aaaaff;
    color: white;
    padding: 5px ;  /* 调整按钮的内边距 */
    border: none;
    border-radius: 4px;  /* 方形按钮 */
    font-size: 15px;
    cursor: pointer;
    width: 100%; /* 使按钮宽度占满容器 */
    transition: background-color 0.3s ease;
	margin-top: 25rpx;
}

.reserve-btn:hover {
  background-color: #aa55ff;
}

.listA-content {
  background-color: #fff;
  padding: 25rpx 20rpx;
  margin-bottom: 20rpx;
  border-radius: 8rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1); /* 添加阴影提升质感 */
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
.info{
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

.speed-text, .battery-text {
  font-size: 28rpx;
}

.speed-text {
  color: #7F669D;
}

.battery-text {
  color: #7F669D;
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
  box-shadow: 0 4rpx 8rpx rgba(0, 0, 0, 0.1); /* 给图片加点阴影提升效果 */
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


</style>