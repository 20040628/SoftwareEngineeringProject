<template>
	<view class="page">
		<view style="background-size: 100% 100%;"
			:style="{'padding-top':'10px','background-image':'linear-gradient(90deg, #F1EEF5, #EBF1F6)','padding-bottom':'10px'}">
			<view>
				<view class="my-info-box" >
					<view>
						<view @click="informaition()" class="my-nickName overOne">{{userInfo.username||'点击授权登录'}}</view>
	
						<view class="my-amount-box">
							<view>
								<view>{{balance}}</view>
								<view>wallet</view>
							</view>
							<view>
								<view>{{totalOrders}}</view>
								<view>scooters</view>
							</view>
						</view>
					</view>
					<view class="my-logo-box" @click="informaition()">
						<view>
							<image 
									class="user-logo" 
									:src="userLogo"
									@click="chooseImg"
							></image>
							<image class="user-vip" :src="vipLogo" v-if='status>0'></image>
						</view>
					</view>
				</view>
			</view>
			<view class="my-vip-box" >
				<image :src="vipImg" mode='widthFix'></image>
				<view :class="statusClass">{{ statusClass }}</view>
			</view>
			
		</view>
		<view class="myPage-listTable">
			<view class="myPage-listTable-box">
				<view @click="goPage(1)">
					<view>
						<image mode="widthFix" :src='myOrderImg'></image>
					</view>
					<view class="myPage-listTable-txtA">All Order</view>
				</view>
				<view @click="goPage(2)">
					<view>
						<image mode="widthFix" :src='doingImg'></image>
					</view>
					<view class="myPage-listTable-txtA">Doing</view>
				</view>
				<view @click="goPage(3)">
					<view>
						<image mode="widthFix" :src='unuseImg'></image>
					</view>
					<view class="myPage-listTable-txtA">Unuse</view>
				</view>
				<view @click="goPage(4)">
					<view>
						<image mode="widthFix" :src='doneImg'></image>
					</view>
					<view class="myPage-listTable-txtA">Done</view>
				</view>
			</view>
			<view class="my-more_box">
				<view class="myPage-listTable-title">More services</view>
				<view class="myPage-listTable-box">
					<view @click="goPage(5)">
						<view class="s">
							<image mode="widthFix" :src='paymentImg'></image>
						</view>
						<view class="myPage-listTable-txtB">Payment</view>
					</view>
					<view @click="goPage(7)">
						<view class="s">
							<image mode="widthFix" :src='myFeedbackImg'></image>
						</view>
						<view class="myPage-listTable-txtB">Feedback</view>
					</view>
					<view @click="goPage(9)">
						<view class="s">
							<image mode="widthFix" :src='setImg'></image>
						</view>
						<view class="myPage-listTable-txtB">Set</view>
					</view>
					<view @click="logout()">
						<view class="s">
							<image mode="widthFix" :src='logoutImg'></image>
						</view>
						<view class="myPage-listTable-txtB">Logout</view>
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
      username: '',
	  userid: null,
	  userLogo:'/static/icons/user-logo.png',
	  myOrderImg:'/static/icons/myOrder.png',
	  unpaidImg:'/static/icons/unpay.png',
	  unuseImg:'/static/icons/unuse.png',
	  doneImg:'/static/icons/done.png',
	  doingImg:'/static/icons/doing.png',
	  paymentImg:'/static/icons/payment.png',
	  setImg:'/static/icons/setup.png',
	  myFeedbackImg:'/static/icons/feedback.png',
	  logoutImg:'/static/icons/logout.png',
	  vipImg:'/static/icons/name.png',
	  vipLogo:'/static/icons/VIP.png',
	  userInfo: uni.getStorageSync('userInfo'),
	  balance: 0,
	  totalOrders: 0,
	  status: 0,
	  isLoading:true,
	  user:[]
    };
  },
  async mounted() {
    this.user = uni.getStorageSync('userInfo');
    if (this.user) {
      this.username = this.user.username;
	  this.userid = this.user.userId;
	  await this.getuser();
    } else {
      this.username = 'Unknown User'; 
    }
	console.log(this.user)
	
	console.log(this.userLogo)
	await this.getOrders();
	await this.getCard()
  },
   computed: {
      statusClass() {
        switch (this.status) {
          case 1:
            return 'Student';  
          case 2:
            return 'Senior';   
          case 3:
            return 'Frequent'; 
          case 4:
            return 'Frequent_student'; 
		  case 5:
			return 'Frequent_senior';
          default:
            return 'Ordinary User';
        }
      },
    },
  methods: {
	load: function(e) {
			console.log("load")
	},
	chooseImg() {
	  uni.chooseImage({
	    count: 1,  // 选择的图片数量
	    sourceType: ['album'],  // 只从相册中选择
	    sizeType: ['original', 'compressed'],  // 可以选择原图或压缩图
	    success: (res) => {
	      this.userLogo = res.tempFilePaths;  // 获取选中的图片路径
	    }
	  })
	},
	async getprofile(){
		const token = String(uni.getStorageSync('token'));
		try {
			uni.showLoading({ title: "Loading", mask: true });
		    const res = await uni.request({
		      url: `${this.$baseURL}/api/users/profile/${this.userid}`,
		      method: 'GET',
			  header: {
					'Authorization': `Bearer ${token}`,
					'Content-Type': 'application/json'
			   },
			});
			if (res.statusCode === 200) {
				this.userLogo = `../../../backend/uploads/avatars/${res.data.avatar}`
			} else {
				uni.showToast({
				   title: res.data.message,
				   icon: 'none',
				   duration: 2000
				 });
			} 	
		} catch (err) {
		    uni.showToast({ title: '网络错误', icon: 'none' })
		} finally {
		  uni.hideLoading();
		  this.isLoading = false
		}
	},
	async getuser(){
		const token = String(uni.getStorageSync('token'));
		try {
			uni.showLoading({ title: "Loading", mask: true });
		    const res = await uni.request({
		      url: `${this.$baseURL}/api/users/${this.userid}`,
		      method: 'PUT',
			  data: {
				email:this.user.email ,
				mobile:this.user.mobile,
				birthDate: this.user.birthDate   
			   },
			  header: {
					'Authorization': `Bearer ${token}`,
					'Content-Type': 'application/json'
			   },
			});
			if (res.statusCode === 200) {
				if(res.data.isStudent==1){
					this.status=1;
				}else if(res.data.isSenior==1){
					this.status=2
				}else if(res.data.isFrequentUser==1){
					this.status=3
				} else if(res.data.isStudent==1 && res.data.isFrequentUser==1){
					this.status=4
				} else if(res.data.isSenior==1 && res.data.isFrequentUser==1){
					this.status=5
				}
			} else {
				uni.showToast({
				   title: res.data.message,
				   icon: 'none',
				   duration: 2000
				 });
			} 	
		} catch (err) {
		    uni.showToast({ title: '网络错误', icon: 'none' })
		} finally {
		  uni.hideLoading();
		  this.isLoading = false
		}
	},
	async getCard(){
		const token = String(uni.getStorageSync('token'));
		this.user = uni.getStorageSync('userInfo');
		try {
			uni.showLoading({ title: "加载中...", mask: true });
		    const res = await uni.request({
		        url: `${this.$baseURL}/api/bank-payment/check-card/${this.user.userId}`,
		        method: 'GET',
				header: {
				  'Content-Type': 'application/json',
				  "Authorization": `Bearer ${token}`
				},
				
		    });
		    this.balance = res.data.bankBalance
		} catch (err) {
		    uni.showToast({ title: '网络错误', icon: 'none' })
		} finally {
			uni.hideLoading();
		    this.isLoading = false
		}
	},
	logout() {
	  // 清除本地存储中的用户信息
	  uni.removeStorageSync('userInfo');
	  uni.removeStorageSync('token');
	
	  // 跳转到登录页面
	  uni.reLaunch({
	    url: '/pages/UserLogin/UserLogin'  
	  });
	},
	async getOrders(){
		const token = String(uni.getStorageSync('token'));
		try {
		    const res = await uni.request({
		      url: `${this.$baseURL}/api/bookings/getAllFinished/${this.userid}`,
		      method: 'GET',
			  header: {
					'Authorization': `Bearer ${token}`,
					'Content-Type': 'application/json'
			   },
			});
			if (res.statusCode === 200) {
				if (Array.isArray(res.data)) {
				    console.log("订单总数:", res.data.length);
				    this.totalOrders = res.data.length;
				} else {
				    console.error("返回的数据格式不正确", res.data);
				    uni.showToast({
				        title: '数据格式错误',
				        icon: 'none',
				        duration: 2000
				    });
				}
			} else {
				uni.showToast({
				   title: res.data.message,
				   icon: 'none',
				   duration: 2000
				 });
			} 	
		} catch (err) {
		    uni.showToast({ title: '网络错误', icon: 'none' })
		}
	},
	goPage(e){
		switch (e) {
			case 1:
				uni.navigateTo({
					url: './myorder/orderlist/orderlist'
				})
				break;
			case 2:
				uni.navigateTo({
					url: './myorder/doing/doing'
				})
				break;
			case 3:
				uni.navigateTo({
					url: './myorder/unuse/unuse'
				})
				break;
			case 4:
				uni.navigateTo({
					url: './myorder/done/done'
				})
				break;
			case 5:
				uni.navigateTo({
					url: './payment/card/card'
				})
				break;
			case 6:
				uni.navigateTo({
					url: './my/moreService/collectionGoods/collectionGoods'
				})
				break;
			case 7:
				uni.navigateTo({
					url: './feedback/feedbackIndex/feedbackIndex'
				})
				break;
			case 9:
				uni.navigateTo({
					url: './information/set/set'
				})
				break;
			case 10:
				uni.navigateTo({
					url: './my/moreService/invoice/invoice'
				})
				break;
			case 11:
				uni.navigateTo({
					url: './my/bluetooth/bluetooth'
				})
				break;
			case 12:
				uni.navigateTo({
					url: '/pages/my/car/carAdd'
				})
				break;
			case 13:
				that.logout();
				break;
			case 15:
				uni.navigateTo({
					url: '/pages/my/login/getInfo'
				})
				break;
			}
	}
  }
};
</script>
<style lang="scss" scoped>
	.page {
		background: #f5f5f5;
		min-height: 100vh;
	}
	.my-vip-box {
		display: flex;
		width: 670rpx;
		margin: 0 auto;
		height: 100rpx;
		margin-top: 60rpx;
		justify-content: flex-start;
		align-items: center;

		image {
			margin: 10rpx;
			width: 10%;
		}
		
	}

	.my-info-box {
		display: flex;
		justify-content: space-between;
		padding: 0 40rpx;

		.my-logo-box {
			display: flex;
			> view {
				display: flex;
				position: relative;
				justify-content: flex-start; 
				width: 180rpx;
				height: 180rpx;

				.user-logo {
					width: 180rpx;
					height: 180rpx;
					border-radius: 50%;
				}

				.user-vip {
					position: absolute;
					bottom: 0;
					right: 0;
					width: 40rpx;
					height: 40rpx;
				}
			}
		}

		.my-nickName {
			color: #333;
			font-size: 36rpx;
			max-width: 400rpx;
			font-weight: bold;
			margin-bottom: 6rpx;
		}
		.Student {
		  background-color: lightblue;
		  color: black;
		}
		.Senior {
		  background-color: lightgreen;
		  color: darkgreen;
		}
		.Frequent {
		  background-color: lightyellow;
		  color: darkorange;
		}
		.Frequent_student .Frequent_senior {
		  background-color: lightcoral;
		  color: white;
		}

		.my-vip-num {
			color: #999;
			font-size: 24rpx;
		}

		.my-amount-box {
			margin-top: 50rpx;
			overflow: hidden;

			> view {
				float: left;
				margin-right: 80rpx;

				> view:first-child {
					color: #333;
					font-size: 48rpx;
					margin-bottom: 14rpx;
				}

				> view:last-child {
					font-size: 24rpx;
					color: #666;
				}
			}
		}
	}

	.myPage-listTable {
		padding: 80rpx 0 80rpx 0;
		background-color: #FAFAFA;

		.myPage-listTable-box {
			display: flex;
			text-align: center;
			flex-wrap: wrap;

			> view {
				width: 25%;
				margin-bottom: 30rpx;

				> view {
					font-size: 24rpx;

					image {
						margin-bottom: 20rpx;
						width: 64rpx;
						height: 64rpx;
					}
				}

				.s {
					image {
						width: 48rpx;
						height: 48rpx;
					}
				}

				.myPage-listTable-txtA {
					color: #333;
				}

				.myPage-listTable-txtB {
					color: #666;
				}
			}
		}

		.myPage-listTable-title {
			font-weight: bold;
			color: #333;
			font-size: 28rpx;
			margin-bottom: 40rpx;
			padding: 20rpx 0 0 20rpx;
		}
	}

	// 我的爱车
	.my-car_add,
	.my-car_model {
		margin: 30rpx 40rpx 20rpx;
		border-radius: 12rpx;
		background-color: #FFFFFF;
	}

	.my-car_add {
		padding: 20rpx;
	}

	.car-add_label {
		font-size: 28rpx;
		color: #333333;
		font-weight: bold;
		line-height: 40rpx;
	}

	.car-add_text,
	.info-text {
		color: #666666;
	}

	.car-add_text:active {
		color: #333;
	}

	.car-add_img {
		display: block;
		width: 16rpx;
		height: 16rpx;
		margin-left: 8rpx;
	}

	.my-car_model {
		padding: 16rpx 20rpx;
	}

	.info-title {
		margin-bottom: 6rpx;
		font-size: 32rpx;
		color: #333333;
		line-height: 44rpx;
		font-weight: bold;
	}

	.info-title_subtitle,
	.car-add_text,
	.info-text {
		font-size: 24rpx;
		line-height: 34rpx;
	}

	.info-img {
		display: block;
		width: 200rpx;
		height: 112rpx;
	}

	.my-more_box {
		margin: 0 40rpx;
		border-radius: 12rpx;
		background-color: #FFFFFF;
	}
</style>

