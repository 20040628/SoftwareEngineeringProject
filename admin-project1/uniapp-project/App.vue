<script>
	export default {
		onLaunch: function() {
			console.log('App Launch');
			this.checkAuth();
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		},
		methods: {
		  checkAuth() {
		    const token = uni.getStorageSync('token'); // 获取本地存储的 Token
		
		    if (!token) {
		      console.log("未找到 Token，跳转到登录页...");
		      uni.redirectTo({ url: '/pages/UserLogin/UserLogin' }); // 未登录时跳转
		      return;
		    }
		
		    console.log("检测 Token 是否有效...");
		    uni.request({
		      url: ' http://localhost:8080/api/auth/validate', // 替换成你的 API 地址
		      method: 'GET',
		      header: { 'Authorization': 'Bearer ' + token },
		      success: (res) => {
		        if (res.statusCode === 200 && res.data.valid) {
		          console.log("Token 验证通过，用户 ID:", res.data.userId);
		          uni.setStorageSync('userInfo', res.data); // 存储用户信息
		        } else {
		          console.log("Token 无效，清除并跳转到登录页...");
		          uni.removeStorageSync('token');
		          uni.redirectTo({ url: '/pages/UserLogin/UserLogin' });
		        }
		      },
		      fail: (err) => {
		        console.error("Token 验证请求失败:", err);
		        uni.showToast({ title: '网络错误', icon: 'none' });
		        uni.redirectTo({ url: '/pages/UserLogin/UserLogin' });
		      }
		    });
		  }
		}
	}
</script>

<style>
	/*每个页面公共css */
</style>
