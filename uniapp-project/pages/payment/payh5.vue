<template>
	<view class="page-order-det">
		<view class="top"></view>
		<view class="body">
			<view class="status">
				<image class="s-img" src="/static/images/car.jpg" mode="aspectFill" />
				<view class="main">
					<text class="status-name">successful booking</text>
					<text class="tips">Please pay as soon as possible</text>
				</view>
			</view>
			<view class="goods-info">
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
								<text class="unit">￥</text>
								<text class="num">{{ order.price }}</text>
							</view>
						</view>
						<view class="buy-num">x{{ item.num }}</view>
					</view>
				</view>
				<!-- <view class="delivery">
					<text class="label">配送方式</text>
					<text class="value">快递</text>
				</view> -->
				<view class="discount">
					<text class="name">Discount Amount</text>
					<view class="price"> ￥<text>0.00</text> </view>
				</view>
				<view class="pay">
					<text class="name">Actual Payment</text>
					<text class="price">￥{{ order.price }}</text>
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
	  isLoading:true,
	  payFormHtml: ''
    }
  },
  async onLoad(options){
	console.log('Query params:', options);
    this.orderId = options.id;
	await this.loadOrderDetail();
	// 设置沙箱环境
	// if (plus && plus.android) {
	//     var EnvUtils = plus.android.importClass('com.alipay.sdk.app.EnvUtils');
	//     EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX); // 设置为沙箱环境
	// }
  },

  methods:{
	  async pay() {
		  const targetUrl = `/pages/webview/webview?id=${this.orderId}`;
		  console.log(targetUrl);  // 打印 URL 以便调试
		  uni.navigateTo({
		  	url:targetUrl
		  })
	    // try {
	    //   const res = await uni.request({
	    //     url: `http://172.20.10.6:8080/alipay/pay/${this.orderId}`, 
	    //     method: 'GET',
	    //     success: (response) => {
			  //  this.payFormHtml = res.data;
	    //     }, 
	    //     fail: (error) => {
	    //       console.error("Error processing payment:", error);
	    //     }
	    //   });
	    // } catch (error) {
	    //   console.error("Error processing payment:", error);
	    // }
	  },

	  //  async pay() {
	  //      try {
	  //        const token = String(uni.getStorageSync('token'));
	  //        const res = await new Promise((resolve, reject) => {
	  //          uni.request({
	  //            // url: `http://192.168.101.8:8080/alipay/appPay/${this.orderId}`, 
			// 	 url:`http://192.168.101.8:8080/alipay/pay/${this.orderId}`,
	  //            method: 'GET',  
	  //            header: {
	  //              'Authorization': `Bearer ${token}` 
	  //            },
	  //            success: (res) => resolve(res),
	  //            fail: (error) => reject(error)
	  //          });
	  //        });
	   
	  //        if (res.statusCode === 200) {
	  //          // 获取支付订单信息
	  //          this.orderInfo = res.data; 
	  //          console.log('Order Info:', this.orderInfo);  // 打印订单信息，确保正确
	   
	  //          // 检查是否有有效的订单信息
	  //          if (!this.orderInfo) {
	  //            uni.showToast({
	  //              title: '订单信息无效',
	  //              icon: 'none'
	  //            });
	  //            return;
	  //          }
			// console.log(this.orderInfo)
	  //       // 调用支付功能
	  //       uni.getProvider({
	  //           service: 'payment',
	  //           success: (res) => {
	  //           console.log(res.provider);
	  //           if (~res.provider.indexOf('alipay')) {
	  //               uni.requestPayment({
	  //               provider: 'alipay',  // 固定值为"alipay"
	  //               orderInfo: this.orderInfo, // 订单信息字符串
	  //               success: (res) => {
	  //                   const rawdata = JSON.parse(res.rawdata);
	  //                   console.log("支付成功");
	  //                   this.paymentStatus = '支付成功！';
	  //               },
	  //               fail: (err) => {
	  //                   console.log('支付失败:', JSON.stringify(err));
	  //                   this.paymentStatus = '支付失败，请重试！';
	  //               }
	  //               });
	  //           }
	  //           },
	  //           fail: (err) => {
			// 		console.log('支付服务获取失败：' + JSON.stringify(err));
	  //               this.paymentStatus = '支付服务不可用';
	  //               }
	  //           });
	        
	  //           } else {
	  //               // 如果状态码不是 200，则显示加载失败的提示
	  //              uni.showToast({ title: '支付请求失败', icon: 'none' });
	  //           }
	              
	  //           } catch (error) {
	  //             console.error('支付请求失败:', error);
	  //             uni.showToast({
	  //               title: '支付请求失败，请重试',
	  //               icon: 'none',
	  //               duration: 2000
	  //             });
	  //           }
	  //   },
		async loadOrderDetail() {
			  const token = String(uni.getStorageSync('token'));
		      try {
		  		uni.showLoading({ title: "加载中...", mask: true });
		        const res = await uni.request({
		            url: `http://localhost:8080/api/bookings/${this.orderId}`,
		            method: 'GET',
					header: {
						'Authorization': `Bearer ${token}`,
						'Content-Type': 'application/json'
					},
		        });
				if (res.statusCode === 200) {
				  this.order = res.data 
				} else {
				  uni.showToast({ title: '数据加载失败', icon: 'none' });
				}
				
		      } catch (err) {
		          uni.showToast({ title: '网络错误', icon: 'none' })
		      } finally {
		  		uni.hideLoading();
		        this.isLoading = false
		      }
		},
		formatDate(dateString) {
		     const date = new Date(dateString);
		     const year = date.getFullYear();
		     const month = String(date.getMonth() + 1).padStart(2, '0');
		     const day = String(date.getDate()).padStart(2, '0');
		     const hours = String(date.getHours()).padStart(2, '0');
		     const minutes = String(date.getMinutes()).padStart(2, '0');
		     return `${year}-${month}-${day} ${hours}:${minutes}`;
		},
		async cancel(){
			const token = String(uni.getStorageSync('token'));
			try {
				uni.showLoading({ title: "加载中...", mask: true });
			    const res = await uni.request({
			      url: `http://localhost:8080/api/bookings/cancel/${this.orderId}`,
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
			  this.isLoading = false
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
			background: linear-gradient(180deg, #D3A7FF 80%, #f7f7f7 100%);
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
					justify-content: flex-end;
					align-items: flex-end;

					.name {
						display: block;
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