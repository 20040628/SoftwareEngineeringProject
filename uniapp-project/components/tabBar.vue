<template>
	<view class="tabbar-box ipx-footer-pd" :style="{ height: nowTabPaddingBottom }">
		<!-- 首页 -->
		<view class="tabbar-item" @click="tabBar(0)">
			<image :src="nowBar === 0 ? activeIcons.home : defaultIcons.home" />
			<view :class="{ 'tabbar-active': nowBar === 0 }">Home</view>
		</view>

		<!-- 地图 -->
		<view class="tabbar-item" @click="tabBar(1)">
			<image :src="nowBar === 1 ? activeIcons.map : defaultIcons.map" />
			<view :class="{ 'tabbar-active': nowBar === 1 }">Map</view>
		</view>

		<!-- 中间按钮（扫码） -->
		<view class="tabbar-center" @click="tabBar(2)">
			<view class="tabbar-cen-box">
				<image :src="scanCodeImg" />
			</view>
		</view>

		<!-- 发现 -->
		<view class="tabbar-item" @click="tabBar(3)">
			<image :src="nowBar === 3 ? activeIcons.found : defaultIcons.found" />
			<view :class="{ 'tabbar-active': nowBar === 3 }">Now</view>
		</view>

		<!-- 我的 -->
		<view class="tabbar-item" @click="tabBar(4)">
			<image :src="nowBar === 4 ? activeIcons.my : defaultIcons.my" />
			<view :class="{ 'tabbar-active': nowBar === 4 }">My</view>
		</view>
	</view>
</template>

<script>
export default {
	props: {
	    currentIndex: Number // 接收当前选中的页面索引
	},
	data() {
		return {
			nowTabPaddingBottom: this.getTabPaddingBottom()|| '56px', // 防止 undefined
			nowBar: this.currentIndex || 0,
			scanCodeImg: '../static/tabbar/scanCode.png', // 扫码按钮图片
			defaultIcons: {
				home: '../static/tabbar/index.png',
				map: '../static/tabbar/map.png',
				found: '../static/tabbar/found.png',
				my: '../static/tabbar/my.png'
			},
			activeIcons: {
				home: '../static/tabbar/index-active.png',
				map: '../static/tabbar/map-active.png',
				found: '../static/tabbar/found-active.png',
				my: '../static/tabbar/my-active.png'
			}
		};
	},
	methods: {
		// 切换 tab，更新当前选中的 tab
		tabBar(index) {console.log('11111111111111111111')
			// 当选中的 tab 和当前 tab 不同，更新 nowBar 并通知父组件
			if (this.nowBar !== index) {
				this.nowBar = index;
				this.$emit('nowBar', index); // 通过事件向父组件传递更新的索引
			}
		},
		getTabPaddingBottom() {
		    let padding = uni.getStorageSync('nowTabPaddingBottom');
		    if (padding && typeof padding === 'string') {
		        return padding;
		    }
		    return null;  // 或者返回 undefined, 或者其他默认值
		}
	},
	watch: {
		// 监听外部的 currentIndex 变化，更新 nowBar
		currentIndex(newVal) {
		this.nowBar = newVal;
		}
	}
	
};
</script>

<style lang="scss" scoped>
.tabbar-box {
	background: #fff;
	position: fixed;
	// z-index: 900;
	bottom: 0;
	left: 0;
	width: 100%;
	box-shadow: 0px -1px 10px #eee;
	display: flex;
	align-items: center;
	justify-content: space-around;
	padding-bottom: env(safe-area-inset-bottom);

	.tabbar-item {
		color: #9698A1;
		text-align: center;
		font-size: 20rpx;
		// flex: 1;
		cursor: pointer;

		image {
			margin: 0 auto 4rpx;
			display: block;
			width: 44rpx;
			height: 44rpx;
		}

		.tabbar-active {
			color: #473958;
		}
	}

	// 中间按钮
	.tabbar-center {
		position: relative;
		top: -20rpx;

		.tabbar-cen-box {
			z-index: 1;
			position: relative;
			border-radius: 50%;
			background-image: linear-gradient(50deg, #745994, #A289C1);
			box-shadow: 0px 5px 10px 1px #745994;
			width: 120rpx;
			height: 120rpx;
			display: flex;
			align-items: center;
			justify-content: center;

			image {
				width: 48rpx;
				height: 48rpx;
			}
		}
	}
}
</style>
