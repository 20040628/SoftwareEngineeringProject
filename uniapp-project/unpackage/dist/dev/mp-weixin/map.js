"use strict";
const common_vendor = require("./common/vendor.js");
const common_assets = require("./common/assets.js");
const _sfc_main = {
  data() {
    return {
      scooters: [],
      userLocation: null,
      mapCenter: {
        latitude: 30.7656,
        longitude: 103.9799
      }
    };
  },
  async mounted() {
    await this.loadScooters();
  },
  methods: {
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
          this.scooters = res.data.map((scooter) => ({
            ...scooter,
            // 保留原始数据
            iconPath: scooter.status === 1 ? "/static/icons/available_scooter.png" : "/static/icons/in_use_scooter.png",
            width: 40,
            // 确保地图标记点有默认尺寸
            height: 40
          }));
        } else {
          common_vendor.index.showToast({ title: "数据加载失败", icon: "none" });
        }
      } catch (err) {
        common_vendor.index.showToast({ title: "网络错误", icon: "none" });
      } finally {
        this.isLoading = false;
      }
    },
    load: function(e) {
      common_vendor.index.__f__("log", "at pages/map/map.vue:71", "load");
    },
    // 回到当前位置
    centerToUser() {
      if (this.userLocation) {
        this.mapCenter = this.userLocation;
      }
    },
    // 地图标记点击
    handleMarkerTap(e) {
      const scooterId = e.detail.markerId;
      const scooter = this.scooters.find((s) => s.id === scooterId);
      this.showScooterDetail(scooter);
    },
    // 获取用户位置
    async getUserLocation() {
      try {
        const res = await common_vendor.index.getLocation({ type: "gcj02" });
        this.userLocation = res;
        this.mapCenter = res;
      } catch (err) {
        common_vendor.index.__f__("error", "at pages/map/map.vue:92", "获取定位失败:", err);
      }
    },
    showScooterDetail(scooter) {
      common_vendor.index.navigateTo({
        url: `/pages/cardetail/cardetail?id=${scooter.id}`
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_assets._imports_0$5,
    b: common_vendor.o((...args) => $options.centerToUser && $options.centerToUser(...args)),
    c: $data.mapCenter.latitude,
    d: $data.mapCenter.longitude,
    e: $data.scooters,
    f: common_vendor.o((...args) => $options.handleMarkerTap && $options.handleMarkerTap(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
exports.MiniProgramPage = MiniProgramPage;
//# sourceMappingURL=../.sourcemap/mp-weixin/map.js.map
