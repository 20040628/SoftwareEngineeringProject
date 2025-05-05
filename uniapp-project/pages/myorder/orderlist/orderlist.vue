<template>
	<view class="content">
		<view class="none" v-if="!data">
			There has no order!
		</view>
		<view class="me-head">
		      <view class="tabs-container">
		        <view class="tabs">
		          <view v-for="(item, index) in allList" :key="index" 
		            :class="['tab', { active: tabsIndex === item.tabId }]" 
		            @click="tabClick(item)">
		            <text>{{ item.name }}</text>
		          </view>
		        </view>
		      </view>
		</view>
		<view class="me-center">
			<view class="list-tab">
				<ul class="item-ul" v-for="(item, index) in orderList" :key="index">
					<li v-if="item.status == tabsIndex || tabsIndex == 0">
						<view  class="item-top">
							<view>
								<text>OrderID:</text>
								<text>{{item.id}}</text>
							</view>
							<view class="pay-type" :class="getStatusClass(item.status)">
								<text>{{allList[item.status].name}}</text>
							</view>
						</view>
						<view class="dashed-line"></view>
						<view class="item-content">
							<view  class="item-left">
								 <image src="@/static/images/car.jpg" alt="Order Image" style="width: 70px; height: 70px; border-radius: 5px;"></image>
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
						<view class="item-btom" v-if="item.status == 1">
							<view @click="cancelClick(item)">
								<view class="item-btom-btn">cancel</view>
							</view>
							<view  @click="payClick(item)">
								<view class="item-btom-btn pay">pay</view>
							</view>
						</view>
						
						<!-- <view class="item-btom" v-if="item.status == 3">
							<view class="item-btom-btn" @click="evaluateClick(item)">Comment</view>
						</view> -->
					</li>
				</ul>
			</view>
		</view>
	</view>
</template>
<script>
	export default {
		onBackPress(e) {
			uni.navigateTo({
			  url: '/pages/index'
			});
			return true;
		},
		data() {
			return {
				tabsIndex: 0,
				allList: [
					{tabId: 0,name: 'All'},
					{tabId: 1,name: 'Unpaid'},
					{tabId: 2,name: 'Paid'},
					{tabId: 3,name: 'Canceled'},
				],
				orderList: [],
				userInfo: uni.getStorageSync('userInfo'),
				isLoading:true,
				data:false,
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
			      url: `${this.$baseURL}/api/users/orders/${user.userId}`, 
			      method: 'GET',
				  header: {
				  	'Content-Type': 'application/json',
				  	"Authorization": `Bearer ${token}`
				  	},
			    });
			
			    if (res.statusCode === 200) {
			      this.orderList = res.data; 
				  if(this.orderList.length>0){
					  this.data= true
				  }
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
			evaluateClick(item){
				uni.navigateTo({
					url: '../order/OrderEvaluate?detailDate=' + encodeURIComponent(JSON.stringify(item))
				});
			},
			
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
    color:  #2c3e50;
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
					background-color:  #2c3e50;
					border: 1px solid  #2c3e50;
					color: #ffffff;
				}
			}
		}
	}
</style>
