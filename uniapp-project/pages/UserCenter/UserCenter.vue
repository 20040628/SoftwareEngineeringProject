<template>
	<view class="page">
		<view style="background-size: 100% 100%;"
			:style="{'padding-top':'10px','background-image':'linear-gradient(90deg, #F1EEF5, #EBF1F6)'}">
			<view>
				<view class="my-info-box" >
					<view>
						<view @click="informaition()" class="my-nickName overOne">{{userInfo.username||'点击授权登录'}}</view>
						<view @click="informaition()" class="my-vip-num">{{'未开通vip'}}</view>
	
						<view class="my-amount-box">
							<view>
								<view>{{balance||0}}</view>
								<view>我的钱包</view>
							</view>
							<view>
								<view>{{totalGainedBonus||0}}</view>
								<view>我的佣金</view>
							</view>
						</view>
					</view>
					<view class="my-logo-box" @click="informaition()">
						<view>
							<image class="user-logo" :src="userLogo"></image>
							<!-- <image class="user-vip" :src="vipLogo" v-if='isVipType'></image> -->
						</view>
					</view>
				</view>
			</view>
			<view class="my-vip-box" >
				<image :src="vipImg" mode='widthFix'></image>
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
						<image mode="widthFix" :src='unpaidImg'></image>
					</view>
					<view class="myPage-listTable-txtA">Unpaid</view>
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
      username: '',  // 用于存储用户名
	  userLogo:'/static/icons/user-logo.png',
	  myOrderImg:'/static/icons/myOrder.png',
	  unpaidImg:'/static/icons/unpay.png',
	  unuseImg:'/static/icons/unuse.png',
	  doneImg:'/static/icons/done.png',
	  paymentImg:'/static/icons/payment.png',
	  setImg:'/static/icons/setup.png',
	  myFeedbackImg:'/static/icons/feedback.png',
	  logoutImg:'/static/icons/logout.png',
	  vipImg:'/static/icons/VIP.png',
	  userInfo: uni.getStorageSync('userInfo'),
	  balance: '',
	  totalGainedBonus: '',
    };
  },
  onLoad() {
    const user = uni.getStorageSync('userInfo');
    if (user) {
      this.username = user.username;
    } else {
      this.username = 'Unknown User';  // 如果没有找到token，则显示为未知用户
    }
  },
  methods: {
	load: function(e) {
			console.log("load")
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
	goPage(e){
		switch (e) {
			case 1:
				uni.navigateTo({
					url: './myorder/myorder'
				})
				break;
			case 2:
				uni.navigateTo({
					url: './my/moreService/coupon/coupon'
				})
				break;
			case 3:
				uni.navigateTo({
					url: './my/moreService/turnover/turnover'
				})
				break;
			case 4:
				uni.navigateTo({
					url: './my/moreService/certification/certification'
				})
				break;
			case 5:
				uni.navigateTo({
					url: './my/moreService/collection/collection'
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
					url: './my/service/service'
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
		width: 670rpx;
		margin: 0 auto;
		height: 100rpx;
		margin-top: 60rpx;

		image {
			width: 10%;
		}
	}

	.my-info-box {
		display: flex;
		justify-content: space-between;
		padding: 0 40rpx;

		.my-logo-box {
			> view {
				position: relative;
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

