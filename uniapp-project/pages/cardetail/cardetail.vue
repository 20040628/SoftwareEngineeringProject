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
        <text class="price">£{{ getPrice(scooter) }}</text>
		<text class="original-price" v-if="hasDiscount">£{{ getOriginalPrice(scooter) }}</text>
        <text class="unit">/ {{ getPriceLabel() }}</text>
      </view> 

      <!-- 地址时间信息 -->
      <view class="address-section" v-if="scooter">
		  <view class="notification-wrapper">
		    <uni-icons type="notification" size="20" color="#ff4d4f"></uni-icons>
		    <text class="note">Please carefully check the rental period and the information of the return service point</text>
		  </view>
        <view class="information">
            <view class="label">
              <text class="label-text">Pick and Return</text>
              <text class="store-name">{{ selectedSite.name }}</text>
            </view>
            
            <view class="label">
              <text class="label-text">Location:</text>
              <text class="store-location">{{ selectedSite.locationName }}</text>
            </view>
            
            <view class="label">
              <text class="label-text">Time:</text>
              <text class="time-range">{{ startTime }} → {{ endTime }}</text>
            </view>
          </view>
      </view>

</view>
    <view class="action-bar">
    	<button @click="submitBooking" :disabled="isLoading">
    	    {{ isLoading ? 'In process...' : 'Confirming a Reservation' }}
    	</button>
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
	  hireType: '',
	    startTime: '',
		selectedSite:'',
	  isLoading:true,
	  currentUser:{
		userId:null
	  },
	  paymentUrl:'',
	  hasDiscount:false,
    }
  },
  async onLoad(options){
	  this.hireType = uni.getStorageSync('hireType');
	  this.startTime = uni.getStorageSync('startTime');
	  this.endTime = uni.getStorageSync('endTime');
	  this.selectedSite = uni.getStorageSync('selectedStore');
	  this.reverseGeocode(this.selectedSite)
    	this.scooterID = options.id;
		const userInfo = uni.getStorageSync('userInfo');
		  if (userInfo) {
		    this.currentUser.userId = userInfo.userId || null;
		}
		await this.loadScooterDetail();
  },
  computed: {
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
   
  
  methods: {
	  getPrice(scooter) {
	     if (this.hireType === 'HOUR') {
	       return scooter.discountedPriceHour;
	     } else if (this.hireType === 'FOUR_HOURS') {
	       return scooter.discountedPriceFourHour;
	     } else if (this.hireType === 'DAY') {
	       return scooter.discountedPriceDay;
	     } else if (this.hireType === 'WEEK') {
	       return scooter.discountedPriceWeek;
	     }
	     return 0;
	   },
	   getOriginalPrice(scooter){
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
	
	async loadScooterDetail() {
	  const token = String(uni.getStorageSync('token'));
	  try {
	    uni.showLoading({ title: "加载中...", mask: true });
	
	    // Make the API request to get the scooter details
	    const res = await uni.request({
	      url: `${this.$baseURL}/api/scooters/${this.scooterID}?userId=${this.currentUser.userId}`,
	      method: 'POST',
	      header: {
	        'Content-Type': 'application/json',
	        "Authorization": `Bearer ${token}`,
	      },
	    });
	
	    // Check if the response is successful
	    if (res.statusCode === 200) {
	      const data = res.data;
	
	      // Logging the response data (for debugging purposes)
	      console.log(data);
	
	      // Update scooter details
	      this.scooter=data
		  this.hasDiscount = this.scooter.hasDiscount
	
	    } else {
	      uni.showToast({ title: '加载失败，请稍后再试', icon: 'none' });
	    }
	
	  } catch (err) {
	    uni.showToast({ title: '网络错误', icon: 'none' });
	  } finally {
	    // Hide loading spinner and update loading state
	    uni.hideLoading();
	    this.isLoading = false;
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
	// 提交预订
	async submitBooking() {
		const token = String(uni.getStorageSync('token'));
		if (!this.startTime) {
			this.showMessage('请选择开始时间', 'error');
			return;
		}
		if (!this.hireType) {
			this.showMessage('请选择租赁类型', 'error');
			return;
		}

		this.isLoading = true;
		uni.showLoading({ title: 'Under submission...' });

		try {
				const bookingData = {
					userId: this.currentUser.userId,
					scooterId: this.scooter.id,
					hireType: this.hireType,
					startTime: this.startTime
				};
				

				// 发送请求
				const res = await new Promise((resolve, reject) => {
					uni.request({
						url: `${this.$baseURL}/api/bookings`,
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

				if (res.statusCode === 200) {
					this.showMessage('book successfully', 'success');
					uni.showToast({
						title: 'Book successful!',
					    icon: 'none',
					    duration: 2000  // 2秒后自动关闭
					});
					await this.handlePay(res.data);
				} else {
					uni.showToast({
					  title: res.data|| 'Scheduled failure',
					  icon: 'none',
					  duration: 2000
					});
				}
			} catch (error) {
				uni.showToast({
				      title: error.message || 'Scheduled failure',
				      icon: 'none',
				      duration: 2000
				    });
			} finally {
				this.isLoading = false;
				uni.hideLoading();
			}
	},
	async handlePay(order) {
		uni.navigateTo({
			url: `/pages/payment/payh5?id=${order.orderId}`
		})
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
	
	
  }
 

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
	.original-price{
		font-size: 28rpx;
		  color: #999;
		  margin-left: 20rpx;
		  text-decoration: line-through;  
	}
	  .unit {
		color: #666;
		margin-left: 20rpx;
		font-size: 28rpx;
	  }
}

/* address-section样式 */
.address-section {
  padding: 20px 15px;
}

.notification-wrapper {
  display: flex;
  align-items: center; /* 垂直居中图标和文字 */
  background-color: #fff3f0; /* 半弧方框的背景色 */
  padding: 12px 20px;
  border-radius: 25px 25px 0 0; /* 上半部分为圆弧形 */
  border: 1px solid #ffccc7; /* 边框颜色 */
}

/* note 样式 */
.note {
  font-size: 10px;
  color: #ff4d4f;
  margin-left: 10px; /* 图标和文字之间的间距 */
  line-height: 1.5;
}
/* information样式 */
.information {
	border-radius: 15px;
	box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
	margin-bottom: 20px;
	background-color: #ffffff;
  font-size: 16px;
  color: #333;
  line-height: 1.6;
  padding: 20px 15px;
}

/* label样式 */
.label {
  margin-bottom: 15px;
}

/* label-text样式 */
.label-text {
  font-size: 14px;
  color: #555;
  font-weight: bold;
  margin-bottom: 5px;
  display: block;
}

/* store-name样式 */
.store-name {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin-top: 5px;
}

/* store-location样式 */
.store-location {
  font-size: 14px;
  color: #888;
  line-height: 1.4;
}

/* time-range样式 */
.time-range {
  font-size: 12px;
  font-weight: 500;
  color: #333;
}

.action-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx;
  button{
	  background-color: #2c3e50;
	  color: white;
  }
}

</style>