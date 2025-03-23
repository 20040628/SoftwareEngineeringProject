<template>
  <view class="page-container">
    <view class="content">
      <view  v-show="nowBarIndex === 0">
      	<indexPage ref='indexPage' @stopDown="stopDown"></indexPage>
      </view>
      <view  v-show="nowBarIndex === 1">
      	<mapPage :nowTabHeight="nowTabHeight" :paddingBottom="nowTabPaddingBottom" ref='mapPage'></mapPage>
      </view>
      <view  v-show="nowBarIndex === 3">
      	<foundPgae ref='foundPgae' @stopDown="stopDown"></foundPgae>
      </view>
      <view  v-show="nowBarIndex === 4"> 
      	<myPage ref='myPage'></myPage>
      </view>
    </view>
    <tabBar @nowBar="nowBar"></tabBar>
  </view>
</template>

<script>
import indexPage from './index/index.vue'
import foundPage from './found/found.vue'
import myPage from './UserCenter/UserCenter.vue'
import mapPage from './map/map.vue'
import tabBar from '../components/tabBar.vue'
// import topBar from '../components/topBar.vue'

export default {
  components: {
    indexPage,
    foundPage,
    myPage,
    mapPage,
    tabBar
  },
  data() {
    return {
      nowBarIndex: 0,
      pageTitles: ['首页', '地图', '发现', '我的']
    };
  },
  methods: {
    // 切换bar
    nowBar: function(e) {
      let that = this
      // 仅一次点击有效
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
            // that.topBarTitle = '小维汽车服务'
            // that.topBarType = 1
            break
          case 1:
            that.$refs.mapPage.load()
            // that.topBarTitle = '充电地图'
            // that.topBarType = 1
            break
          case 3:
            that.$refs.foundPage.load()
            // that.topBarTitle = ['商城', '周边']
            // that.topBarType = 3
            // that.$refs.topBar.carNum()
            break
          case 4:
            that.$refs.myPage.load()
            break
        }
      })
    },
    // 停止下拉刷新
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
  /* overflow: auto; */
  transition: transform 0.3s ease-in-out;
  padding-bottom: 50px; 
  /* z-index: 999; */
}
</style>
