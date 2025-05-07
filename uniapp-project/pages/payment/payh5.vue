<template>
	<view class="page-order-det">
		<view class="top"></view>
		<view class="body">
			<view class="status">
				<image class="s-img" src="/static/images/car.jpg" mode="aspectFill" />
				<view class="main">
					<text class="status-name">Successful Booking</text>
					<text class="tips">Please pay as soon as possible</text>
				</view>
			</view>
			<view v-if="order.scooter" class="goods-info">
				<view class="title"> Scooter Information </view>
				<view class="list">
					<view class="item">
						<image src="/static/images/car.jpg" mode="aspectFill" class="preview" />
						<view class="main">
							<view class="info">
								<text class="name">ID: {{ order.scooter.id }}</text>
								<text class="size">Location:{{ order.scooter.location }}</text>
							</view>
							<view class="price">
								<text class="unit">£</text>
								<text class="num">{{ order.priceBeforeDiscount }}</text>
							</view>
						</view>
					</view>
				</view>
				<view class="discount">
					<text class="name">Discount Amount</text>
					<view class="price">£<text>{{ discount }}</text> </view>
				</view>
				<view class="pay">
					<text class="name">Actual Payment</text>
					<text class="price">£{{ order.price }}</text>
				</view>
			</view>
			<view class="order-info">
				<view class="title">Order Information</view>
				<view class="list">
					<view class="item">
						<text class="label">Order Reference</text>
						<text class="value">{{order.id}}</text>
					</view>
					<view class="item">
						<text class="label">Mode of Payment</text>
						<text class="value">Online Payment</text>
					</view>
					<view class="item">
						<text class="label">Order Time</text>
						<text class="value">{{formatDate(order.orderTime)}}</text>
					</view>
					<view class="item">
						<text class="label">Start Time</text>
						<text class="value">{{formatDate(order.startTime)}}</text>
					</view>
					<view class="item">
						<text class="label">End Time</text>
						<text class="value">{{formatDate(order.endTime)}}</text>
					</view>
				</view>
			</view>
			<view class="order-pay">
				<view class="total">
					<text class="name"></text>
					<text value="95.00" type="price" color="red" :size="30"></text>
				</view>
				<view class="btn">
					<view class="cancel" @click="cancel()"><button size="mini">Cancel Order</button></view>
					<view class="go-pay"  @click="pay()"><button size="mini">Pay</button></view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
export default {
  data() {
    return {
      orderInfo: {},
	  orderId : null,
	  order:[],
	  payFormHtml: '',
	  discount: 0,
    }
  },
  async onLoad(options){
    this.orderId = options.id;
	await this.loadOrderDetail();
  },

  methods:{
	  async pay() {
		  uni.navigateTo({
		  	url:`/pages/payment/choosePay/choosePay?id=${this.orderId}`
		  })
	  },

		async loadOrderDetail() {
			  const token = String(uni.getStorageSync('token'));
		      try {
		  		uni.showLoading({ title: "Loading...", mask: true });
		        const res = await uni.request({
		            url: `${this.$baseURL}/api/bookings/${this.orderId}`,
		            method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`,
						'Content-Type': 'application/json'
					},
		        });
				if (res.statusCode === 200) {
				  this.order = res.data 
				  console.log(this.order.priceBeforeDiscount)
				  this.discount= this.order.priceBeforeDiscount - this.order.price
				} else {
				  uni.showToast({ title: '数据加载失败', icon: 'none' });
				}
				
		      } catch (err) {
				   console.error('请求失败:', err);
		          uni.showToast({ title: '网络错误', icon: 'none' })
		      } finally {
		  		uni.hideLoading();
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
		async cancel(){
			const token = String(uni.getStorageSync('token'));
			try {
				uni.showLoading({ title: "Loading...", mask: true });
			    const res = await uni.request({
			      url: `${this.$baseURL}/api/bookings/cancel/${this.orderId}`,
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
				} else {
					uni.showToast({
					   title: res.data.message,
					   icon: 'none',
					   duration: 2000
					 });
				}
				uni.reLaunch({
				    url: '/pages/myorder/orderlist'  // 确保路径正确
				}); 	
			} catch (err) {
			    uni.showToast({ title: '网络错误', icon: 'none' })
			} finally {
			  uni.hideLoading();
			}
		}
  }
  
};
</script>

<style lang="scss">
	page {
		background-color: #f7f7f7;
	}

	.page-order-det {
		position: relative;

		.top {
			width: 100%;
			height: 548rpx;
			margin-top: -44px;
			background: linear-gradient(180deg, #2c3e50 80%, #f7f7f7 100%);
		}

		.body {
			position: absolute;
			top: 150rpx;
			width: 100%;
			padding-bottom: 140rpx;

			.status {
				display: flex;
				align-items: center;
				width: calc(100% - 64rpx);
				margin: 0 32rpx 50rpx 32rpx;
				padding: 52rpx 30rpx;
				box-sizing: border-box;
				background-color: #ffffff;
				border-radius: 8rpx;

				.s-img {
					display: block;
					width: 58rpx;
					height: 58rpx;
					margin-right: 24rpx;
				}

				.status-name {
					display: block;
					font-size: 40rpx;
					font-weight: 500;
					color: #565656;
				}

				.tips {
					font-size: 24rpx;
					font-weight: 400;
					color: #ababab;
				}
			}

			.goods-info {
				width: calc(100% - 64rpx);
				margin: 0 32rpx 20rpx 32rpx;
				padding: 52rpx 30rpx 36rpx 30rpx;
				box-sizing: border-box;
				background-color: #ffffff;
				border-radius: 8rpx;

				.title {
					padding-bottom: 20rpx;
					border-bottom: solid 2rpx #e8e8e8;
					font-size: 36rpx;
					font-weight: 500;
					color: #333333;
				}

				.list {
					margin-bottom: 36rpx;
					border-bottom: solid 2rpx #e8e8e8;

					.item {
						display: flex;
						position: relative;
						padding: 30rpx 0;

						.preview {
							display: block;
							width: 188rpx;
							height: 188rpx;
							margin-right: 20rpx;
						}

						.main {
							display: flex;
							flex-direction: column;
							justify-content: space-between;

							.info {
								.name {
									display: block;
									font-size: 32rpx;
									font-weight: 400;
									color: #333333;
								}

								.size {
									font-size: 24rpx;
									font-weight: 400;
									color: #8e8e8e;
								}
							}

							.price {
								font-size: 32rpx;
								font-weight: bold;
								color: #343434;
							}
						}

						.buy-num {
							position: absolute;
							top: 0;
							right: 0;
							font-size: 28rpx;
							font-weight: 400;
							color: #8e8e8e;
						}
					}
				}

				.delivery {
					display: flex;
					align-items: center;
					margin-bottom: 30rpx;

					.label {
						display: block;
						flex: 1;
						font-size: 28rpx;
						font-weight: 400;
						color: #333333;
					}

					.value {
						font-size: 28rpx;
						font-weight: 400;
						color: #8e8e8e;
					}
				}

				.discount {
					display: flex;
					align-items: center;
					margin-bottom: 38rpx;

					.name {
						flex: 1;
						font-size: 28rpx;
						font-weight: 400;
						color: #333333;
					}

					.price {
						font-size: 28rpx;
						font-weight: 400;
						color: red;
					}
				}

				.pay {
					display: flex;
					justify-content: center;
		

					.name {
						display: block;
						flex: 1;
						margin-right: 10rpx;
						font-size: 28rpx;
						font-weight: 400;
						color: #343434;
					}

					.price {
						font-size: 36rpx;
						font-weight: 500;
						color: red;
					}
				}
			}

			.order-info {
				width: calc(100% - 64rpx);
				margin: 0 32rpx 20rpx 32rpx;
				padding: 52rpx 30rpx 1rpx 30rpx;
				box-sizing: border-box;
				background-color: #ffffff;
				border-radius: 8rpx;

				.title {
					margin-bottom: 36rpx;
					padding-bottom: 20rpx;
					border-bottom: solid 2rpx #e8e8e8;
					font-size: 36rpx;
					font-weight: 500;
					color: #333333;
				}

				.list {
					.item {
						display: flex;
						justify-content: space-between;
						align-items: center;
						margin-bottom: 40rpx;

						.label {
							font-size: 28rpx;
							font-weight: 400;
							color: #333333;
						}

						.value {
							font-size: 28rpx;
							font-weight: 400;
							color: #8e8e8e;
						}
					}
				}
			}

			.order-pay {
				display: flex;
				align-items: center;
				position: fixed;
				left: 0;
				bottom: calc(1rpx + env(safe-area-inset-bottom));
				width: 100%;
				height: 50px;
				padding: 0 32rpx;
				background-color: #ffffff;
				box-sizing: border-box;

				.total {
					display: flex;
					align-items: center;
					flex: 1;

					.name {
						font-size: 24rpx;
						font-weight: 400;
						color: #333333;
					}
				}

				.btn {
					display: flex;

					.cancel {
						margin-right: 16rpx;
						button {
							background-color: #343434;
							color: #ffffff;
						}
					}

					.go-pay {
						button {
							background-color: #343434;
							color: #ffffff;
						}
					}
				}
			}
		}
	}
</style>