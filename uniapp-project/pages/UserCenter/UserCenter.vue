<template>
	<view class="page">
		<view class="action">
			<view><image src="/static/center/scan.png"></image></view>
			<view><image src="/static/center/setting.png"></image></view>
		</view>
		<view class="user">
			<image class="avatar" src="/static/center/avatar.png"></image>
			<view class="name">
				<text>{{ username }}</text> <!-- 显示从后端获取的用户名 -->
			</view>
			<image class="right" src="/static/center/right.png"></image>
		</view>
		<view class="card" v-for="(v, k) in menus" :key="k">
			<view class="menu">
				<view class="item" v-for="(v_, k_) in v" :key="k_" @click="menuClick(v_)">
					<image :src="v_.icon"></image>
					<view>{{v_.name}}</view>
					<image class="arrow" v-if="v_.arrow" src="/static/center/right.png"></image>
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
      menus: [
        [
          { name: 'My Account', icon: '/static/center/self.png', arrow: true },
          { name: 'My Order', icon: '/static/center/heart.png', arrow: true },
          { name: 'Contact customer service', icon: '/static/center/kefu.png', arrow: true },
          { name: 'Share APP', icon: '/static/center/share.png', arrow: false },
          { name: 'Logout', icon: '/static/center/out.png', arrow: false },  // 添加 "Logout" 菜单项
        ]
      ]
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
    menuClick(menuItem) {
      if (menuItem.name === 'Logout') {
        this.logout();  // 如果点击了 "Logout"，执行退出登录方法
      } else {
        console.log(menuItem);
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
    }
  }
};
</script>
<style lang="scss">
	@import "./usercenter.scss";
</style>
