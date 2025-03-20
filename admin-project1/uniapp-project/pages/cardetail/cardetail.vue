<template>
  <view class="container">
    <!-- 基本信息 -->
    <view class="info-section card">
      <view v-if="scooter" class="header">
        <text class="scooter-id">#{{ scooter.id }}</text>
        <view class="status-tag" :class="statusClass">
			{{ statusText }}
		</view>
      </view>
	  
      <!-- 价格显示 --> 
      <view class="price-section">
        <text class="price">${{ calculatedPrice }}</text>
        <text class="unit" v-if="durationOptions.length">/ {{ durationOptions[selectedDuration].label }}</text>
      </view> 

      <!-- 地址信息 -->
      <view class="address-section" v-if="scooter">
        <image src="/static/icons/location.png" class="icon"/>
        <view class="address">
          <text class="title">Pick-up Position</text>
          <text class="detail">{{ scooter.location || 'Getting location information' }}</text>
        </view>
        <image src="/static/icons/arrow_right.png" class="arrow"/>
      </view>
    </view>

	<view class="timeline-container">
	    <h4>Booking Timeline</h4>
	    <!-- 添加时间刻度 -->
	    <view class="timeline-scale">
	        <view v-for="day in 7" :key="day" class="timeline-day">
	            {{ formatDate(addDays(new Date(), day-1)) }}
	        </view>
	    </view>
	    <view class="timeline">
			<view v-for="(slot, index) in timeline" :key="index" 
				:class="['time-slot', slot.status]"
				:style="{ width: calculateSlotWidth(slot) + '%' }">
				<view class="slot-info">
					{{ formatTimeSlot(slot) }}
				</view>
			</view>
	    </view>
	</view>
	
    <!-- 租车时长选择 -->
    <view class="duration-section">
      <view class="section-title">Select Duration</view>
      <view class="duration-grid">
        <view 
          v-for="(option, index) in durationOptions"
          :key="index"
          class="duration-item"
          :class="{ active: selectedDuration === index }"
          @click="selectDuration(index)"
        >
          <text class="label">{{ option.label }}</text>
          <text class="sub-label">{{ option.subLabel }}</text>
        </view>
      </view>
    </view>
	
	<!-- 选择开始时间 -->
	<view class="form-group">
		<label>start date:</label>
		<!-- 日期选择器 -->
		<picker mode="date" :value="bookingForm.startDate" @change="onDateChange">
			<view class="picker">{{ bookingForm.startDate || 'Please select a start date' }}</view>
		</picker>
		<label>start time:</label>
		<!-- 时间选择器 -->
		<picker mode="time" :value="bookingForm.startTime" @change="onTimeChange">
			<view class="picker">{{bookingForm.startTime || 'Please select a start time' }}</view>
		</picker>

	</view>

    <view class="action-bar">
    	<button @click="submitBooking" :disabled="isLoading">
    	    {{ isLoading ? 'In process...' : 'Confirming a Reservation' }}
    	</button>
    </view>
	<view v-if="message" :class="messageType === 'success' ? 'success-msg' : 'error-msg'">
		{{ message }}
	</view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      scooter:null,
	  scooterID:null,
      selectedDuration: 0, // 默认选择第一个选项
      durationOptions: [],
	  message: '',
	  messageType: '',
	  bookingForm: {
	    hireType: '',
	    startDate: '', 
	    startTime: '',
		startDateTime:''
	  },
	  timeline: [],
	  isLoading:true,
	  currentUser:{
		  userId:null
	  }
    }
  },
  async onLoad(options){
    	this.scooterID = options.id;
    	await this.loadScooterDetail();
    	await this.refreshTimeline(); 
		const userInfo = uni.getStorageSync('userInfo');
		  if (userInfo) {
		    this.currentUser.userId = userInfo.userId || null;
		}
  },
  computed: {
  	    // 计算总价格
  	    calculatedPrice() {
  	      if (!this.durationOptions.length) return 0; // 确保 options 已加载
  	
  	      const option = this.durationOptions[this.selectedDuration];
  	      return (option?.price || 0).toFixed(2)
  	    },
		statusClass() {
			const statusMap = {
				0: "unavailable",
			    1: "available",
			    2: "maintenance",
			};
			return statusMap[this.scooter.status] || "unknown"; // 默认值
		},
		
		statusText() {
		    const textMap = {
				0: "unavailable",
				1: "available",
				2: "maintenance",
		    };
		    return textMap[this.scooter.status] || "unknown";
		 }
   },
   

 // 是否可租用
  //   canRent() {
  //     return this.scooter.status === 'available' || 
  //           this.scooter.status === 'low_battery'
  //   },

  //   // 租车按钮文字
  //   rentButtonText() {
  //     if (this.scooter.status === 'low_battery') return '低电量可租用'
  //     if (this.scooter.status === 'available') return '立即租用'
  //     return '暂时不可用'
  //   }
  
  methods: {
	  // 封装 uni.request 成 Promise 以支持 async/await
	request(options) {
	    return new Promise((resolve, reject) => {
	        uni.request({
				...options,
	            success: (res) => {
	              if (res.statusCode === 200) {
	                resolve(res.data);
	              } else {
	                reject(new Error(`请求失败: ${res.statusCode}`));
	              }
	            },
	            fail: (err) => reject(err)
	        });
		});
	},
	
	async loadScooterDetail() {
	    try {
			uni.showLoading({ title: "加载中...", mask: true });
	        const data = await this.request({
	            url: `http://localhost:8080/api/scooters/${this.scooterID}`,
	            method: 'GET'
	        });
	        this.scooter = data
			this.durationOptions = [
			        { label: '1 Hour', value: 1, type: 'hour', price: this.scooter.priceHour },
			        { label: '4 Hours', value: 4, type: 'hour', price: this.scooter.priceHour * 4 }, 
			        { label: '1 Day', value: 1, type: 'day', price: this.scooter.priceDay }, 
			        { label: '1 Week', value: 1, type: 'week', price: this.scooter.priceWeek } 
			];
	    } catch (err) {
	        uni.showToast({ title: '网络错误', icon: 'none' })
	    } finally {
			uni.hideLoading();
	        this.isLoading = false
	    }
	},
	
	retryLoad() {
	      this.isLoading = true
	      this.loadScooterDetail()
	},
	
	addDays(date, days) {
	      return new Date(date.getTime() + days * 86400000); 
	},
	
	formatDate(date) {
	      return `${date.getMonth() + 1}.${date.getDate()}`;
	},
	// 添加刷新时间轴的方法
	async refreshTimeline() {
	      if (!this.scooterID) return; // 确保有 scooterID
	      try {
	        this.timeline = await this.request({
	          url: `http://localhost:8080/api/bookings/timeline/${this.scooterID}`,
	          method: 'GET'
	        });
	      } catch (err) {
	        console.error('刷新时间轴失败:', err);
	      }
	},
	
	calculateSlotWidth(slot) {
		const start = new Date(slot.startTime).getTime();
		const end = new Date(slot.endTime).getTime();
		const duration = end - start;
		console.log(slot.startTime, new Date(slot.startTime))
		      
		// 计算时间段占总时间范围的百分比
		const totalDuration = 7 * 24 * 60 * 60 * 1000; // 7天的毫秒数
		return (duration / totalDuration) * 100;
	},
		    
	formatTimeSlot(slot) {
		const start = new Date(slot.startTime);
		const end = new Date(slot.endTime);
		if (slot.status === 'booked') {
		    let hirePeriodText;
		    switch (slot.hirePeriod) {
		        case 'HOUR':
		        hirePeriodText = '1hour';
		        break;
		        case 'FOUR_HOURS':
		        hirePeriodText = '4hours';
		        break;
		        case 'DAY':
		        hirePeriodText = '1day';
		        break;
		        case 'WEEK':
		        hirePeriodText = '1week';
		        break;
		        default:
		        hirePeriodText = slot.hirePeriod;
		    }
		    return `${this.formatTime(start)} - ${this.formatTime(end)} (${hirePeriodText})`;
		    }
		return 'Available';
		},
		
	
	
	formatTime(date) {
		return `${date.getMonth() + 1}/${date.getDate()} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
	},
	
	selectDuration(index) {
	  this.selectedDuration = index
	},
	
	onDateChange(event) {
	    this.bookingForm.startDate = event.detail.value;
	    this.updateStartTime();
	},
	    // 时间选择改变
	onTimeChange(event) {
	    this.bookingForm.startTime = event.detail.value;
	    this.updateStartTime();
	},
	
	    // 更新完整的日期时间
	updateStartTime() {
	    if (this.bookingForm.startDate && this.bookingForm.startTime) {
	    this.bookingForm.startDateTime = `${this.bookingForm.startDate} ${this.bookingForm.startTime}`;
	    }
	},

	// 提交预订
	async submitBooking() {
		this.bookingForm.hireType = this.durationOptions[this.selectedDuration].type.toUpperCase();
		const token = String(uni.getStorageSync('token'));
		if (!this.bookingForm.startTime) {
			this.showMessage('请选择开始时间', 'error');
			return;
		}
		if (!this.bookingForm.hireType) {
			this.showMessage('请选择租赁类型', 'error');
			return;
		}

		this.isLoading = true;
		uni.showLoading({ title: 'Under submission...' });

		try {
				const startTime = this.bookingForm.startDateTime.replace('T', ' ') + ':00';
				console.log('startDateTime:', startTime);
				console.log('User ID:', this.currentUser?.userId);
				console.log('Scooter ID:', this.scooter?.id);
				console.log("Token:", token);
				const payloadBase64 = token.split('.')[1]; // JWT 的 payload 部分
				const payload = JSON.parse(atob(payloadBase64)); 
				console.log("Token Payload:", payload);

				const bookingData = {
					userId: this.currentUser.userId,
					scooterId: this.scooter.id,
					hireType: this.bookingForm.hireType,
					startTime: startTime
				};
				console.log("Booking Data:", bookingData);

				// 发送请求
				const res = await new Promise((resolve, reject) => {
					uni.request({
						url: 'http://localhost:8080/api/bookings',
						method: 'POST',
						data: bookingData,
						header: { 
							'Content-Type': 'application/json',
							"Authorization": `Bearer ${token}`
							},
						success: (response) => resolve(response),
						fail: (error) => reject(error)
					});
				});
				console.log("API Response:", res);

				if (res.statusCode === 200) {
					this.showMessage('book successfully', 'success');
					await this.refreshTimeline();
				} else {
					console.log('Error message:', res.data.message);
					this.showMessage(res.data.message || '预订失败', 'error');
				}
			} catch (error) {
				this.showMessage('请求失败，请重试', 'error');
			} finally {
				this.isLoading = false;
				uni.hideLoading();
			}
	},

	// 显示提示信息
	showMessage(text, type) {
			this.message = text;
			this.messageType = type;
			setTimeout(() => {
				this.message = '';
			}, 3000);
		}
	}
	
	// openMap() {
	//   uni.openLocation({
	//     latitude: this.scooter.latitude,
	//     longitude: this.scooter.longitude,
	//     name: this.scooter.location
	//   });
	// }
	
  }
    // handleRent() {
    //   if (!this.canRent) return
      
    //   uni.showLoading({ title: '正在创建订单' })
      
    //   // 调用租车接口
    //   const orderData = {
    //     scooterId: this.scooter.id,
    //     duration: this.durationOptions[this.selectedDuration]
    //   }
      
    //   // 模拟API调用
    //   setTimeout(() => {
    //     uni.hideLoading()
    //     uni.navigateTo({
    //       url: `/pages/payment/payment?orderId=${Date.now()}`
    //     })
    //   }, 1000)
    // }
 

</script>

<style scoped lang="scss">
.container {
  padding: 24rpx;
  background: #f8f9fa;
}
.card{
	background: #fff;
	border-radius: 24rpx;
	padding: 32rpx;
	margin-bottom: 24rpx;
}

.info-section {
  padding: 30rpx;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.scooter-id {
  font-size: 40rpx;
  font-weight: 500;
  color:#666 ;
}

.status-tag {
  padding: 8rpx 20rpx;
  border-radius: 40rpx;
  font-size: 24rpx;
  
  &.available {
    background: #e8f5e9;
    color: #4CAF50;
}
  &.unavailable {
    background: #fff3e0;
    color: #FF9800;
}

}
  
.price-section {
	display: flex;
	align-items: baseline;
	margin: 40rpx 0;
	.price {
		font-size: 64rpx;
		color: #FF5722;
		font-weight: bold;
	  }
	  .unit {
		color: #666;
		margin-left: 20rpx;
		font-size: 28rpx;
	  }
}

.address-section {
  display: flex;
  align-items: center;
  padding: 30rpx;
  background: #f5f5f5;
  border-radius: 16rpx;
  .icon {
    width: 48rpx;
    height: 48rpx;
    margin-right: 20rpx;
  }
  .address {
    flex: 1;
    .title {
      display: block;
      color: #999;
      font-size: 24rpx;
    }
    .detail {
      font-size: 28rpx;
    }
  }
  .arrow {
    width: 30rpx;
    height: 30rpx;
    margin-left: 20rpx;
  }
}

.timeline-container {
  margin: 20px 0;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}
.timeline-container h4{
	margin-bottom: 10rpx;
}

.timeline-scale {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
  padding: 0 5px;
}
.timeline-day {
  font-size: 12px;
  color: #666;
  flex: 1;
  text-align: center;
  border-right: 1px solid #ddd;
  padding: 2px 0;
}

.timeline {
  display: flex;
  height: 60px;
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow: hidden;
  margin: 5px 0;
}

.time-slot {
  position: relative;
  height: 100%;
  min-width: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  transition: all 0.3s ease;
  padding: 0 5px;
}

.time-slot.available {
  background: #E8F5E9;
  color: #2E7D32;
  border-right: 1px dashed #A5D6A7;
}

.time-slot.booked {
  background: #FFEBEE;
  color: #C62828;
  border-right: 1px solid #FFCDD2;
}

.slot-info {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: center;
  line-height: 1.2;
  width: 100%;
}


.duration-section {
  padding: 30rpx;
  .section-title {
    font-size: 25rpx;
    font-weight: bold;
    margin-bottom: 10rpx;
  }
}

.duration-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
}

.duration-item {
  padding: 30rpx;
  border: 2rpx solid #eee;
  border-radius: 16rpx;
  text-align: center;
  transition: all 0.3s;
  
  &.active {
    border-color: #4CAF50;
    background: #f0f9eb;
  }
  
  .label {
    display: block;
    font-size: 28rpx;
    color: #333;
  }
}

.form-group {
  margin-bottom: 15px;
  padding-bottom: 80rpx
}

.form-group label {
  display: block;
  margin-bottom: 5px;
}

.picker {
	padding: 10px;
	background-color: #f5f5f5;
	border-radius: 5px;
	text-align: center;
	margin-bottom: 10px;
}

.action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx;
  button{
	  background-color: #2E7D32;
	  color: white;
  }
}

</style>