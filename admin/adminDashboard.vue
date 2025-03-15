<template>
	<view class="page">
		<!-- 侧边栏 -->
		<view class="sidebar">
			<view class="user">
				<image src="/static/center/avatar.png"></image>
				<text>Administrator</text>
			</view>
			<view class="menu">
				<view class="menu-item" 
				      v-for="v in menus" 
				      :key="v.id" 
				      :class="{ active: activeMenu === v.id }"
				      @click="selectMenu(v.id)">
					<image :src="v.icon"></image>
					<text>{{ v.name }}</text>
				</view>
			</view>
		</view>

		<!-- 主要内容区 -->
		<view class="content">
			<!-- 滑板车管理 -->
			<view v-if="activeMenu === 1">
				<text class="title">Scooter Management</text>
				<!-- 添加滑板车表单 -->
				<view class="form">
					<input v-model="newScooter.location" placeholder="Enter location" class="input" />
					<input v-model="newScooter.priceHour" type="number" placeholder="Price per hour" class="input" />
					<input v-model="newScooter.priceFourHour" type="number" placeholder="Price for four hours" class="input" />
					<input v-model="newScooter.priceDay" type="number" placeholder="Price per day" class="input" />
					<input v-model="newScooter.priceWeek" type="number" placeholder="Price per week" class="input" />
					<input v-model="newScooter.status" type="number" placeholder="Status (0: inactive, 1: active)" class="input" />
					<input v-model="newScooter.longitude" type="number" placeholder="Longitude" class="input" />
					<input v-model="newScooter.latitude" type="number" placeholder="Latitude" class="input" />

					<view class="button" @click="addScooter">Add Scooter</view>
				</view>

				
			</view>


			<view v-if="activeMenu === 2">
				<text class="title">User Feedback</text>
				<!-- 用户反馈管理 -->
							<view v-if="activeMenu === 2">
								<text class="title">User Feedback</text>
								<view class="feedback-list">
									<view class="feedback-item" v-for="feedback in feedbacks" :key="feedback.id">
										<view class="feedback-info">
											<view class="info-row">
												<text class="label">ID:</text> <text class="value">{{ feedback.id }}</text>
											</view>
											<view class="info-row">
												<text class="label">User ID:</text> <text class="value">{{ feedback.userId }}</text>
											</view>
											<view class="info-row">
												<text class="label">Content:</text> <text class="value">{{ feedback.content }}</text>
											</view>
											<view class="info-row">
												<text class="label">Create Time:</text> <text class="value">{{ feedback.createTime }}</text>
											</view>
											<view class="info-row">
												<text class="label">Status:</text> <text class="value">{{ feedback.status }}</text>
											</view>
											<view class="info-row">
												<text class="label">Priority:</text> <text class="value">{{ feedback.priority }}</text>
											</view>
											<view class="info-row">
												<text class="label">Admin Response:</text> <text class="value">{{ feedback.adminResponse }}</text>
											</view>
											<view class="info-row">
												<text class="label">Response Time:</text> <text class="value">{{ feedback.responseTime }}</text>
											</view>
										</view>
									</view>
								</view>
							</view>
			</view>
			
			
			
			<view v-if="activeMenu === 3">
			<view class="page">
			  <text class="title">Show all scooters</text>
			    
			  <!-- 滑板车列表 -->
			  <view class="scooter-list">
				<view class="scooter-item" v-for="scooter in scooters" :key="scooter.id">
				  <view class="scooter-info">
					<view class="info-row">
					  <text class="label">ID:</text> <text class="value">{{ scooter.id }}</text>
					</view>
					<view class="info-row">
					  <text class="label">Location:</text> <text class="value">{{ scooter.location }}</text>
					</view>
					<view class="info-row">
					  <text class="label">Price:</text>
					  <text class="value">
						${{ scooter.priceHour }}/hr | ${{ scooter.priceDay }}/day | ${{ scooter.priceWeek }}/week
					  </text>
					</view>
					<view class="info-row">
					  <text class="label">Coordinates:</text>
					  <text class="value">({{ scooter.longitude }}, {{ scooter.latitude }})</text>
					</view>
					<view class="status-tag" :class="getStatusClass(scooter.status)">
					  {{ getStatusText(scooter.status) }}
					</view>
				  </view>
				  <!-- 按钮 -->
				  <button class="status-btn" @click="changeScooterStatus(scooter.id)">
					<text>Change Status</text>
				  </button>
				</view>
			  </view>
			</view>
	
				
			</view>
			<view v-if="activeMenu === 4">
				<text class="title">Add to Desktop</text>
			</view>
			<view v-if="activeMenu === 5">
				<text class="title">Logging Out...</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			activeMenu: 1, // 默认选中的菜单项
			menus: [
				{ id: 1, name: 'Scooter Management', icon: '/static/center/book.png' },
				{ id: 2, name: 'User Feedback', icon: '/static/center/comment.png' },
				{ id: 3, name: 'Show all scooters', icon: '/static/center/share.png' },
				{ id: 4, name: 'Add to desktop', icon: '/static/center/appstore-add.png' },
				{ id: 5, name: 'Logout', icon: '/static/center/poweroff.png' },
			],
			scooters: [], // 滑板车列表
			feedbacks: [], // 用户反馈列表
			newScooter: {
				location: '',
				priceHour: null,
				priceFourHour: null,
				priceDay: null,
				priceWeek: null,
				status: null,
				longitude: null,
				latitude: null
			}
		};
	},
	mounted() {
		this.fetchScooters();
	},
	
	methods: {
		selectMenu(id) {
			this.activeMenu = id;
			if (id === 1) {
				this.fetchScooters();
			}
		},
		// 获取滑板车列表
		fetchScooters() {
			uni.request({
				url: 'http://localhost:8080/api/scooters/getAll',
				method: 'GET',
				success: (res) => {
					this.scooters = res.data;
				},
				fail: () => {
					uni.showToast({ title: 'Failed to load scooters', icon: 'none' });
				}
			});
		},
		// 添加新滑板车
		addScooter() {
			if (!this.newScooter.location || !this.newScooter.priceHour || !this.newScooter.priceDay) {
				uni.showToast({ title: 'Please enter required fields', icon: 'none' });
				return;
			}
			uni.request({
				url: 'http://localhost:8080/api/scooters/add',
				method: 'POST',
				data: this.newScooter,
				success: (res) => {
					if (res.statusCode === 200) {
						uni.showToast({ title: 'Scooter Added', icon: 'success' });
						this.newScooter = { location: '', priceHour: null, priceFourHour: null, priceDay: null, priceWeek: null, status: 1, longitude: null, latitude: null };
						this.fetchScooters();
					} else {
						uni.showToast({ title: res.data.message, icon: 'none' });
					}
				},
				fail: () => {
					uni.showToast({ title: 'Failed to add scooter', icon: 'none' });
				}
			});
		},
		// 获取用户反馈数据
				fetchFeedbacks() {
					uni.request({
						url: 'http://localhost:8080/api/feedback/all', // 确保使用正确的API地址
						method: 'GET',
						header: {
							Authorization: `Bearer ${token}`
						},
						success: (res) => {
							if (res.statusCode === 200) {
								this.feedbacks = res.data; // 将返回的数据赋值给反馈列表
							} else {
								uni.showToast({ title: 'Failed to load feedbacks', icon: 'none' });
							}
						},
						fail: () => {
							uni.showToast({ title: 'Failed to load feedbacks', icon: 'none' });
						}
					});
				},
		getStatusClass(status) {
		    const statusMap = {
		      0: "unavailable",
		      1: "available",
		      2: "maintenance",
		    };
		    return statusMap[status] || "unknown"; // 默认值
		  },
		
		  getStatusText(status) {
		    const textMap = {
		      0: "Unavailable",
		      1: "Available",
		      2: "Maintenance",
		    };
		    return textMap[status] || "Unknown";
		  },
		async changeScooterStatus(id) {
		  try {
		    const res = await uni.request({
		      url: `http://localhost:8080/api/scooters/changeStatus/${id}`,
		      method: 'GET'
		    });
		    if (res.statusCode === 200) {
		      this.fetchScooters();
		      uni.showToast({ title: '状态更新成功', icon:'success' });
		    } else {
		      uni.showToast({ title: '状态更新失败', icon: 'none' });
		    }
		  } catch (err) {
		    uni.showToast({ title: '网络错误', icon: 'none' });
		  }
		},
	}
};
</script>



<style lang="scss">
@import "./adminDashboard.scss";

.page {
	display: flex;
	min-height: 100vh;
	background: #f5f5f5;
}

.sidebar {
	width: 250px;
	background: #58C4C9;
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 20px 0;
	color: #fff;
	box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
}

.content {
	flex: 1;
	padding: 20px;
	background: #fff;
	border-radius: 10px;
	margin: 20px;
	box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}

.form {
	display: flex;
	flex-direction: column;
	margin-bottom: 20px;

	.input {
		margin: 5px 0;
		padding: 10px;
		border: 1px solid #ccc;
		border-radius: 5px;
	}

	.button {
		background: #58C4C9;
		color: white;
		padding: 10px;
		text-align: center;
		border-radius: 5px;
		cursor: pointer;
		margin-top: 10px;
	}
}

/* 标题 */
.title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 15px;
}

/* 滑板车列表 */
.scooter-list {
  margin-top: 90rpx;
  display: flex;
  flex-direction: column;
  gap: 16px;
  width: 100%;
  max-width: 800px;
}

/* 滑板车卡片 */
.scooter-item {
  display: flex;
  flex-direction: column;
  background-color: white;
  padding: 16px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s ease-in-out;
}

.scooter-item:hover {
  transform: translateY(-3px);
}

/* 统一信息行样式 */
.info-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6px;
}

.label {
  font-weight: bold;
  color: #444;
}

.value {
  color: #666;
}

/* 状态标签 */
.status-tag {
  padding: 6px 12px;
  border-radius: 5px;
  font-size: 14px;
  font-weight: bold;
  text-align: center;
  display: inline-block;
  margin-top: 8px;
  width: fit-content;
}

/* 状态颜色 */
.available {
  background: #e8f5e9;
  color: #2e7d32;
}

.unavailable {
  background: #ffebee;
  color: #c62828;
}

.maintenance {
  background: #fff3e0;
  color: #ff9800;
}

/* 按钮样式 */
.status-btn {
  margin-top: 12px;
  background-color: #58c4c9;
  color: white;
  padding: 8px 14px;
  border-radius: 5px;
  text-align: center;
  transition: background 0.2s ease-in-out;
}

.status-btn:hover {
  background-color: #46a3a8;
}


</style>
