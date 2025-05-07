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
			  <image :src="item.url" lazy-load="true" alt="show the scooters"></image>
			</swiper-item>
		</swiper>
		<view class="scooter-list">
			<view class="container-time">
				<view class="example-body">
					<uni-datetime-picker type="datetime" :start="start" :end="end" v-model="datetimeString" @change="change" />
				</view>
				<view class="address-section" @click="goToSelectPage">
					 <image src="/static/icons/location.png" class="icon" alt="Location icon"/>
					    <view class="address">
					        <text class="title">Pick-up Position</text>
					        <text class="detail" v-if="selectedSite">{{ selectedSite.name || 'Please choose' }}</text>
					    </view>
					<image src="/static/icons/arrow_right.png" class="arrow" alt="arrow icon"/>
				</view>
				<!-- 预定时长选择器 -->
				<view class="duration-picker">
					<text>Select Scheduled Duration:</text>
					<view class="duration-options">
						<view
							class="duration-option" 
							:class="{ selected: selectedDuration === 0 }" 
							@click="onDurationSelect(0)">
							1 Hour
						</view>
						<view 
							class="duration-option" 
							:class="{ selected: selectedDuration === 1 }" 
							@click="onDurationSelect(1)">
							4 Hours
						</view>
						<view 
							class="duration-option" 
							:class="{ selected: selectedDuration === 2 }" 
							@click="onDurationSelect(2)">
							1 Day
						</view>
						<view 
							class="duration-option" 
							:class="{ selected: selectedDuration === 3 }" 
							@click="onDurationSelect(3)">
							1 Week
						</view>
					</view>
				</view>
				<!-- 查询按钮 -->
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
      scooters:[],
	  isLoading:true,
	  selectedIndex: 0, // 默认选择索引
	  bannerList: [
	          { url: '/static/images/banner1.png'},
	          { url: '/static/images/banner2.png'},
	          { url: '/static/images/banner3.png'}
	        ],
	carImg:"/static/images/scooter.png",
	speedImg:"/static/icons/speed.png",
	batteryImg:"/static/icons/battery.png",
	scooterImg:"/static/icons/scooter.png",
	startTime: '',  // 开始时间
	startDate:'',
	selectedDuration: 0,  // 默认选择第一个选项
	durationOptions:[
		{ label: '1 Hour', type: 'hour' },
		{ label: '4 Hours', type: 'FOUR_HOURS' }, 
		{ label: '1 Day', type: 'day' }, 
		{ label: '1 Week', type: 'week'} ],
	datetimeString: this.getDateTime(new Date(), false),
	selectedSite: null,
	cates: [],
	active: 0,
	secondData: []
	
    }
  },
  watch: {
  	datetimeString() {
  		console.log('日期时间单选:', this.datetimeString);
  	}
   },
  async mounted() {
	 // const selectedSite = uni.getStorageSync('selectedStore');
	 //  if (selectedSite) {
	 //    this.selectedSite = selectedSite;
	 //  }
	 this.loadSelectedSite();
	 
  },
  onLoad() {
  	uni.setLocale('en');
  },
  
  methods: {
	  // 页面加载时读取选中的位置
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
	getDateTime(date, addZero = true){
	  return `${this.getDate(date, addZero)} ${this.getTime(date, addZero)}`
	},
	getDate(date, addZero = true){
	  date = new Date(date)
	  const year = date.getFullYear()
	  const month = date.getMonth()+1
	  const day = date.getDate()
	  return `${year}-${addZero ? this.addZero(month) : month}-${addZero ? this.addZero(day) : day}`
	},
	getTime(date, addZero = true){
	  date = new Date(date)
	  const hour = date.getHours()
	  const minute = date.getMinutes()
	  const second = date.getSeconds()
	 
	return `${addZero ? this.addZero(hour) : hour}:${addZero ? this.addZero(minute) : minute}:${addZero ? this.addZero(second) : second}`;
	    
	},
	addZero(num) {
		if (num < 10) {
			num = `0${num}`
		}
		return num
	},
	
	goToSelectPage(){
		uni.navigateTo({
		    url: '/pages/map/map'
		 });
	},
	queryAvailableScooters(){
		const hireType = this.durationOptions[this.selectedDuration].type.toUpperCase();
		console.log(this.datetimeString)
		let startDate = new Date(this.datetimeString);
		let endDate;
		switch (hireType) {
		  case 'HOUR':
		    endDate = new Date(startDate.getTime() + 1 * 60 * 60 * 1000);  // 加 1 小时
		    break;
		  case 'FOUR_HOURS':
		    endDate = new Date(startDate.getTime() + 4 * 60 * 60 * 1000);  // 加 4 小时
		    break;
		  case 'DAY':
		    endDate = new Date(startDate.getTime() + 1 * 24 * 60 * 60 * 1000);  // 加 1 天
		    break;
		  case 'WEEK':
		    endDate = new Date(startDate.getTime() + 7 * 24 * 60 * 60 * 1000);  // 加 1 周
		    break;
		  default:
		    endDate = startDate;  // 默认情况下，不改变时间
		}
		let formattedDate = endDate.getFullYear() + '-' + 
		                    (endDate.getMonth() + 1).toString().padStart(2, '0') + '-' + 
		                    endDate.getDate().toString().padStart(2, '0') + ' ' + 
		                    endDate.getHours().toString().padStart(2, '0') + ':' + 
		                    endDate.getMinutes().toString().padStart(2, '0') + ':' + 
		                    endDate.getSeconds().toString().padStart(2, '0'); 
		uni.setStorageSync('hireType', hireType); 
		uni.setStorageSync('startTime', this.datetimeString); 
		uni.setStorageSync('endTime', formattedDate); 
		uni.navigateTo({
		  url: '/pages/chooseCar/chooseCar'
		});

	},
	instruction(e){
		uni.navigateTo({
		  url: '/pages/information/instruction/instruction'
		});
	}
				
  }
}
</script>

<style  scoped lang="scss">
	
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
  // background: rgba(252, 250, 255, 1);
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
  box-shadow: 0 2rpx 6rpx rgba(0, 0, 0, 0.1); /* 轻微阴影 */
}

.time-picker-title {
  font-size: 28rpx;
  color: #333;
  margin-bottom: 15rpx;
}

.picker-container {
  display: flex; /* 水平排列 */
  justify-content: space-between; /* 在水平方向上分布 */
  width: 100%; /* 宽度占满父容器 */
}

.picker {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 15rpx;
  background: #f7f7f7;
  border-radius: 8rpx;
  margin-right: 10rpx; /* 选择器之间有间隔 */
  width: 100%; /* 每个选择框占一半宽度 */
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
  background: #d0d0d0; /* 点击时稍微变色 */
}

.picker:last-child {
  margin-right: 0; /* 最后一个选择框不需要右间隔 */
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
  width: 30%;  /* 每个选项宽度占 1/4 */
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin: 5px;;
}

.duration-option.selected {
  border-color:var(--button-bg-color);
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
  // background: #fff;
  // border-radius: 40rpx 40rpx 0 0;
  // padding: 30rpx;
  // box-shadow: 0 -4rpx 12rpx rgba(0, 0, 0, 0.1);
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
    background: #34495e; /* 渐变背景 */
    border-radius: 30rpx; /* 更圆润的圆角 */
    padding: 20rpx 30rpx; /* 调整内边距 */
    box-shadow: 0 6rpx 15rpx rgba(0, 0, 0, 0.1); /* 柔和的阴影效果 */
    transition: all 0.3s ease; /* 过渡效果 */
  }
  
  .instruction:hover {
    transform: scale(1.05); /* 鼠标悬停时放大 */
    box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.15); /* 鼠标悬停时加强阴影 */
  }
  
  .text {
    font-size: 16px;
    font-weight: 500;
    color: white;
    margin-right: 10rpx; /* 与箭头之间的间距 */
  }
  
  .discount-container {
	margin: 30rpx;
    padding: 20rpx;
    // border: 1.5px solid var(--bg-color);
    border-radius: 10rpx;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
  }
  .discount-title {
    font-size: 22px;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
	align-items: center;
  }
  
  .table {
    width: 100%;
    border-collapse: collapse;
  }
  
  .table-header {
    display: flex;
    background-color: #34495e;
    padding: 12rpx 20rpx;
    font-size: 16px;
    font-weight: bold;
    border-radius: 8rpx;
	border: 2rpx solid var(--bg-color);
	color: white;
  }
  
  .header-item {
    flex: 1;
    text-align: center;
  }
  
  .table-row {
    display: flex;
    padding: 12rpx 20rpx;
    border-bottom: 2rpx solid var(--bg-color);
    font-size: 12px;
    color: black;
  }
  
  .row-item {
    flex: 1;
    text-align: left;
    word-wrap: break-word;
  }
  
  .row-item:last-child {
    text-align: left;
  }
  .note {
    font-size: 14px;
    color: #000000;
    margin-top: 15rpx;
    padding: 5rpx;
    font-weight: bold;
    border-radius: 5rpx;
	margin: 10rpx;
	margin-bottom: 20rpx;
  }


</style>