"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
const _sfc_main = {
  data() {
    return {
      mapCenter: {
        latitude: 39.90469,
        // 默认北京坐标
        longitude: 116.40717
      },
      userLocation: null,
      searchKeyword: "",
      scooters: [],
      isLoading: true,
      listHeight: 300,
      maxListHeight: 900,
      // 列表最大高度（单位 px）
      minListHeight: 300
    };
  },
  // computed: {
  //   filteredScooters() {
  //     return this.scooters.filter(scooter => 
  //       scooter.name.includes(this.searchKeyword)
  //     ).map(scooter => ({
  //       ...scooter,
  //       iconPath: '/static/marker.png', // 地图标记图标
  //       width: 30,
  //       height: 40
  //     }))
  //   }
  // },
  async mounted() {
    this.getUserLocation();
    this.calcListHeight();
    await this.loadScooters();
  },
  methods: {
    // 获取车辆列表
    async loadScooters() {
      try {
        const res = await common_vendor.index.request({
          url: "http://localhost:8080/api/scooters/getAll",
          // 后端接口地址
          method: "GET"
          // 请求方法为 GET
        });
        if (res.statusCode === 200) {
          this.scooters = res.data;
        } else {
          common_vendor.index.showToast({ title: "数据加载失败", icon: "none" });
        }
      } catch (err) {
        common_vendor.index.showToast({ title: "网络错误", icon: "none" });
      } finally {
        this.isLoading = false;
      }
    },
    // 跳转到详情页
    showScooterDetail(scooter) {
      common_vendor.index.navigateTo({
        url: `/pages/cardetail/cardetail?id=${scooter.id}`
      });
    },
    //获取用户位置
    async getUserLocation() {
      try {
        const res = await common_vendor.index.getLocation({ type: "gcj02" });
        this.userLocation = res;
        this.mapCenter = res;
      } catch (err) {
        common_vendor.index.__f__("error", "at pages/index/index.vue:141", "获取定位失败:", err);
      }
    },
    // 回到当前位置
    centerToUser() {
      if (this.userLocation) {
        this.mapCenter = this.userLocation;
      }
    },
    adjustListHeight(event) {
      const scrollTop = event.detail.scrollTop;
      const newHeight = Math.min(this.maxListHeight, Math.max(this.minListHeight, this.minListHeight + scrollTop / 3));
      this.listHeight = newHeight;
    },
    // 计算列表高度
    calcListHeight() {
      common_vendor.index.getSystemInfo({
        success: (res) => {
          this.listHeight = res.windowHeight * 0.3;
        }
      });
    },
    // 地图标记点击
    handleMarkerTap(e) {
      const scooterId = e.markerId;
      const scooter = this.scooters.find((s) => s.id === scooterId);
      this.showScooterDetail(scooter);
    }
    // 	// 搜索处理
    // handleSearch() {
    // 	  // 实际开发中可调用后端搜索接口
    // 	  uni.__f__('log','at pages/index/index.vue:176','搜索关键词:', this.searchKeyword)
    // 	}
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.o((...args) => _ctx.handleSearch && _ctx.handleSearch(...args)),
    b: $data.searchKeyword,
    c: common_vendor.o(($event) => $data.searchKeyword = $event.detail.value),
    d: common_assets._imports_0,
    e: common_vendor.o((...args) => $options.centerToUser && $options.centerToUser(...args)),
    f: common_assets._imports_1,
    g: common_vendor.o((...args) => $options.centerToUser && $options.centerToUser(...args)),
    h: $data.mapCenter.latitude,
    i: $data.mapCenter.longitude,
    j: $data.scooters,
    k: common_vendor.o((...args) => $options.handleMarkerTap && $options.handleMarkerTap(...args)),
    l: common_vendor.f($data.scooters, (scooter, k0, i0) => {
      return {
        a: common_vendor.t(scooter.id),
        b: common_vendor.t(scooter.location),
        c: scooter.id,
        d: common_vendor.o(($event) => $options.showScooterDetail(scooter), scooter.id)
      };
    }),
    m: common_vendor.o((...args) => $options.adjustListHeight && $options.adjustListHeight(...args)),
    n: $data.listHeight + "px"
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-1cf27b2a"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/index/index.js.map
