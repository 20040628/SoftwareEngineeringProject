<template>
	<view class="page-container">
		<view class="content">
			<view v-show="nowBarIndex === 0">
				<indexPage ref='indexPage'></indexPage>
			</view>
			<view v-show="nowBarIndex === 1">
				<mapPage ref='mapPage'></mapPage>
			</view>
			<view v-show="nowBarIndex === 3">
				<nowPgae ref='nowPgae' ></nowPgae>
			</view>
			<view v-show="nowBarIndex === 4">
				<myPage ref='myPage'></myPage>
			</view>
		</view>
		<tabBar @nowBar="nowBar"></tabBar>
	</view>
</template>

<script>
	import indexPage from './index/index.vue'
	import nowPgae from './now/now.vue'
	import myPage from './UserCenter/UserCenter.vue'
	import mapPage from './map/map.vue'
	import tabBar from '../components/tabBar.vue'

	export default {
		components: {
			indexPage,
			nowPgae,
			myPage,
			mapPage,
			tabBar
		},
		data() {
			return {
				nowBarIndex: 0,
			};
		},
		onShow() {
			setTimeout(() => {
				if (this.$refs['indexPage']) {
					this.$refs['indexPage'].loadSelectedSite()
				}
				if (this.$refs['myPage']) {
					this.$refs['myPage'].getOrders()
					this.$refs['myPage'].getCard()
					this.$refs['myPage'].getprofile()
				}
			}, 300)
		},
		methods: {
			nowBar: function(e) {
				let that = this
				if (e == that.nowBarIndex) {
					return
				}
				that.nowBarIndex = e
				that.tabPage(e)
			},
			tabPage: function(e) {
				let that = this
				this.$nextTick(() => {
					switch (Number(e)) {
						case 0:
							that.$refs.indexPage.load(1)
							break
						case 1:
							that.$refs.mapPage.load()
							break
						case 3:
							that.$refs.nowPgae.load()
							break
						case 4:
							that.$refs.myPage.load()
							break
					}
				})
			},
			stopDown: function() {
				uni.stopPullDownRefresh()
			}
		}
	};
</script>

<style scoped>
	.page-container {
		display: flex;
		flex-direction: column;
		height: 100vh;
	}
	.content {
		flex: 1;
		transition: transform 0.3s ease-in-out;
		padding-bottom: 50px;
	}
</style>