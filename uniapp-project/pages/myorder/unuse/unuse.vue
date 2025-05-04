<template>
	<view class="content">
		<view class="me-center">
			<view class="list-tab">
				<ul class="item-ul" v-for="(item, index) in orderList" :key="index">
					<li v-if="item.status == tabsIndex || tabsIndex == 0">
						<view  class="item-top">
							<view>
								<text>OrderID:</text>
								<text>{{item.id}}</text>
							</view>
						</view>
						<view class="dashed-line"></view>
						<view class="item-content">
							<view  class="item-left">
								 <img :src="picUrl" alt="Order Image" style="width: 70px; height: 70px; border-radius: 5px;">
							</view>
							<view class="item-right"  @click="goDetail(item)">
								<!-- <view class="item-right-v1">
									<text class="item-title">{{item.odName}}</text>
								</view> -->
								<view class="item-right-v1 induce">
									<text class="rate-text1">orderTime:{{formatDate(item.startTime)}}</text>
								</view>
								<view class="item-right-v1 induce">
									<text class="rate-text1">startTime:{{formatDate(item.startTime)}}</text>
								</view>
								<view class="item-right-v1 induce">
									<text class="rate-text1">hirePeriod:{{item.hirePeriod}}</text>
								</view>
								<view class="item-right-v2">
									<view class="v2-fh">£<text class="v2-price">{{item.price}}</text></view>
								</view>
							</view>
						</view>
						
						<view class="dashed-line"></view>
						<!-- <view class="item-btom" v-if="item.status == 1">
							<view @click="cancelClick(item)">
								<view class="item-btom-btn">cancel</view>
							</view>
							<view  @click="payClick(item)">
								<view class="item-btom-btn pay">pay</view>
							</view>
						</view> -->
						
						<view class="item-btom">
							<button class="item-btom-btn" @click="item.showModal = true">Extend</button>
							<view class="modal" v-if="item.showModal">
								<view class="modal-content">
									<view>Extend the order</view>
									<view class="section-title">Select Duration</view>
									<view class="duration-section">
										  <view class="duration-grid">
											<view 
											  v-for="(option, index) in durationOptions"
											  :key="index"
											  class="duration-item"
											  :class="{ active: selectedDuration === index }"
											  @click="selectDuration(index)"
											>
											  <text class="label">{{ option.label }}</text>
											</view>
										  </view>
										</view>
									<view class="modal-buttons">
									<button @click="handleConfirm(item)">Confirm</button>
									<button @click="handleCancel(item)">Cancel</button>
									</view>
								</view>
							</view>
						</view>
					</li>
				</ul>
			</view>
		</view>
	</view>
</template>
<script>
	export default {
		data() {
			return {
				tabsIndex: 0,
				orderList: [],
				userInfo: uni.getStorageSync('userInfo'),
				isLoading:true,
				picUrl:"../../../static/images/car.jpg",
				selectedDuration: 0, 
				durationOptions: [
					{ label: '1 Hour', value:"HOUR"},
			        { label: '4 Hours', value:"FOUR_HOURS"}, 
			        { label: '1 Day', value: "DAY"}, 
			        { label: '1 Week', value: "WEEK"} ,
				],
				showModal: false,
			}
		},
		async mounted() {
		   await this.loadBookings();			 
		},
		onLoad() {
		},

		methods: {
			async loadBookings() {
			  const user = uni.getStorageSync('userInfo');
			  const token = String(uni.getStorageSync('token'));
			  try {
			    const res = await uni.request({
			      url: `${this.$baseURL}/api/bookings/getAllUndo/${user.userId}`, 
			      method: 'GET',
				  header: {
				  	'Content-Type': 'application/json',
				  	"Authorization": `Bearer ${token}`
				  	},
			    });
			
			    if (res.statusCode === 200) {
			     this.orderList = res.data.map(order => ({
			           ...order,
			           showModal: false, // 为每个订单项添加showModal字段
			    }));
			    } else {
			      uni.showToast({ title: '数据加载失败', icon: 'none' });
			    }
			  } catch (err) {
			    uni.showToast({ title: '网络错误', icon: 'none' });
			  } finally {
			    this.isLoading = false;
			  }
			},
			 formatDate(dateString) {
			    const date = new Date(dateString);
			    const year = date.getFullYear();
			    const month = String(date.getMonth() + 1).padStart(2, '0');
			    const day = String(date.getDate()).padStart(2, '0');
			    const hours = String(date.getHours()).padStart(2, '0');
			    const minutes = String(date.getMinutes()).padStart(2, '0');
			    return ` ${day}/${month}/${year} ${hours}:${minutes}`;
			  },
			  
			tabClick(item){
				this.tabsIndex = item.tabId;
			},
			getStatusClass(status) {
			    return {
			        'status-red': status == 1,
					'status-2': status == 2,
			        'status-green': status == 3,
			    };
			},
				
			// 取消
			async cancelClick(item) {
			    uni.showModal({
			        title: 'Tips',
			        content: 'Are you sure you want to cancel this order?',
			        cancelText: 'cancel',
			        confirmText: 'confirm',
			        success: async (res) => {  // 注意这里的 success 函数也需要用 async
			            if (res.confirm) {
			                const token = String(uni.getStorageSync('token'));
			                try {
			                    uni.showLoading({ title: "loading...", mask: true });
			                    const res = await uni.request({
			                        url: `${this.$baseURL}/api/bookings/cancel/${item.id}`,
			                        method: 'POST',
			                        header: {
			                            'Authorization': `Bearer ${token}`,
			                            'Content-Type': 'application/json'
			                        },
			                    });
			
			                    if (res.statusCode === 200) {
			                        uni.showToast({
			                            title: res.data.message,
			                            icon: 'none',
			                            duration: 2000
			                        });
			                        this.loadBookings();
			                    } else {
			                        uni.showToast({
			                            title: res.data.message,
			                            icon: 'none',
			                            duration: 2000
			                        });
			                    }
			                } catch (err) {
			                    uni.showToast({ title: '网络错误', icon: 'none' });
			                } finally {
			                    uni.hideLoading();
			                    this.isLoading = false;
			                }
			            }
			        }
			    });
			},

			
			payClick(item){
				uni.navigateTo({
					url: `/pages/payment/payh5?id=${item.id}`
				})
			},
			selectDuration(index) {
			  this.selectedDuration = index
			},
			async handleConfirm(item) {
				console.log(item.id)
				const user = uni.getStorageSync('userInfo');
				const token = String(uni.getStorageSync('token'));
				
				try {
				  const res = await uni.request({
				    url: `${this.$baseURL}/api/bookings/extend/${item.id}`, 
				    method: 'POST',
					data:{
						hireType: this.durationOptions[this.selectedDuration].value
					},
					header: {
						'Content-Type': 'application/json',
						"Authorization": `Bearer ${token}`
					},
				  });
							
				  if (res.statusCode === 200) {
				    uni.showToast({
				        title: res.data.message,
				        icon: 'none',
				        duration: 2000
				    });
					uni.navigateTo({
						url: `/pages/payment/payh5?id=${item.id}`
					})
				  } else {
				    uni.showToast({
				        title: res.data,
				        icon: 'none',
				        duration: 2000
				    });
				  }
				} catch (err) {
				  uni.showToast({ title: '网络错误', icon: 'none' });
				} finally {
				  this.isLoading = false;
				}
				item.showModal = false; // 关闭模态框
			},
						
			handleCancel(item) {
			item.showModal = false; 
			}

		}
	};
</script>
<style scoped lang="scss">
	page {
		text-align: center;
		background-color: #F7F8FA;
	}
	
	.content{
		display: flex;
		flex-direction: column;
		height: 100%;
	}
	  .me-head {
    width: 100%;
    height: 80upx;
    padding-bottom: 20upx;
  }

  .tabs-container {
    padding: 10rpx;
    background-color: #fff;
  }

  .tabs {
    display: flex;
    justify-content: space-around;
  }

  .tab {
    padding: 10rpx 20rpx;
    font-size: 28rpx;
    cursor: pointer;
  }

  .tab.active {
    color: #aaaaff;
    font-weight: bold;
    transform: scale(1.05);
  }

  .dashed-line {
    border-top: 1px dashed #ccc;
    margin: 10rpx 0;
  }
	
	.me-center{
		flex: 1;
		overflow-y: auto;
		width: 100%;
		margin-top: 20upx;
		
		.pullScrollView {
			display: flex;
			flex-direction: column;
		}
		
		.list-tab{
			width: 94%;
			margin: 0 auto;

			.item-ul{
				margin-bottom: 20upx;
				list-style-type: none;
				padding-left: 0; 
			}
			.item-ul li{
				background-color: #ffffff;
				border-radius: 15upx;
				box-shadow: 0 0upx 6upx 0upx rgba(0, 0, 150, .2);
			}
			
			.item-top{
				display: flex;
				justify-content: space-between;
				padding: 20rpx;
				font-size: 28rpx;
				
				.pay-type{
					font-weight: bold;
				}
				.status-red{
					color: #ff5500;
				}
				.status-2{
					color: #4891d9;
				}
				.status-green{
					color: #00d500;
				}
			}
			.item-content{
				display: flex;
				justify-content: center;
				align-items: center;
				padding: 10upx 20upx;
			}
			
			.item-right{
				flex: 1;
				margin-left: 20upx;
				
				.item-right-v1{
					width: 100%;
					padding: 8rpx 0;

					.item-title{
						font-weight: bold;
						display: -webkit-box;
					}
				}
				
				.induce{
					display: flex;
					align-items: center;
					
					.rate-text1{
						font-size: 26rpx;
					}
					.rate-text2{
						margin-left: 20rpx;
						font-size: 26rpx;
						color: #B9B9B9;
					}
				}
		
				.item-right-v2{
					padding: 8rpx 0;
					display: flex;
					justify-content: space-between;
					color: #a3a3a3;
					font-size: 26upx;
					
					.v2-fh{
						color: #ff5500;
						font-size: 22upx;
					}
					.v2-price{
						color: #ff5500;
						font-weight: bold;
						font-size: 32upx;
					}
				}
			}	
			
			.item-btom{
				display: flex;
				justify-content: flex-end;
				padding: 20rpx 10upx;
				font-size: 28upx;
				
				.item-btom-btn{
					padding: 6upx 40upx ;
					border: 1px solid #d2d3d4;
					border-radius: 40upx;
					margin-right: 20upx;
					align-items: center;
				}
				.pay{
					background-color: #aaaaff;
					border: 1px solid #aaaaff;
					color: #ffffff;
				}
			}
		}
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
	  gap: 10rpx;
	}
	
	.duration-item {
	  padding: 20rpx;
	  border: 2rpx solid #f0eaff;
	  border-radius: 16rpx;
	  text-align: center;
	  transition: all 0.3s;
	  box-sizing: border-box;
	  display: flex;         
	  justify-content: center; 
	  align-items: center;    
	  height: 100rpx;
	  
	  &.active {
	    // border-color: #aa55ff;
	    background: #e3e2ff;
	  }
	  
	  .label {
	    display: block;
	    font-size: 28rpx;
	    color: #333;
	  }
	}
	.modal {
			position: fixed;
			top: 0;
			left: 0;
			width: 100%;
			height: 100%;
			background-color: rgba(0, 0, 0, 0.5);
			display: flex;
			justify-content: center;
			align-items: center;
			z-index: 1000; 
		}
		/* 窗口 */
	.modal-content {
			background-color: white;
			/* padding: 20px; */
			width: 600rpx;
			height: 800rpx;
			border-radius: 8rpx;
			position: relative;
			//modal-content下的第一个view
			view:first-child{
				padding:20rpx;
				font-size:60rpx;
				font-weight:bold;
			}
			//modal-content下的第二个view
			view:nth-child(2){
				padding:20rpx;
				font-size:30rpx;
				color: #4891d9;
			}
		}
		/* 按钮 */
	.modal-buttons {
			width: 100%;
			display: flex;
			bottom: 0;
			position: absolute;
		}
	.modal-buttons button {
			width: 100%;
			border: none;
		}
	.modal-buttons button:first-child {
			background-color: #74bfe7;
			color: #fff;
			border-radius: 0;
		}
	.modal-buttons button:last-child {
			width: 100%;
			border: 2rpx solid #74bfe7;
			border-radius: 0px;
			background-color: #fff;
			color: #74bfe7;
	}
	
</style>
