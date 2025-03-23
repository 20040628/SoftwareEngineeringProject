"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
const _sfc_main = {
  data() {
    return {
      activeMenu: 1,
      // 默认选中的菜单项
      menus: [
        { id: 1, name: "Scooter Management", icon: "/static/center/book.png" },
        { id: 2, name: "User Feedback", icon: "/static/center/comment.png" },
        { id: 3, name: "Show all scooters", icon: "/static/center/share.png" },
        { id: 4, name: "Add to desktop", icon: "/static/center/appstore-add.png" },
        { id: 5, name: "Logout", icon: "/static/center/poweroff.png" }
      ],
      scooters: [],
      // 滑板车列表
      newScooter: {
        location: "",
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
      common_vendor.index.request({
        url: "http://localhost:8080/api/scooters/getAll",
        method: "GET",
        success: (res) => {
          this.scooters = res.data;
        },
        fail: () => {
          common_vendor.index.showToast({ title: "Failed to load scooters", icon: "none" });
        }
      });
    },
    // 添加新滑板车
    addScooter() {
      if (!this.newScooter.location || !this.newScooter.priceHour || !this.newScooter.priceDay) {
        common_vendor.index.showToast({ title: "Please enter required fields", icon: "none" });
        return;
      }
      common_vendor.index.request({
        url: "http://localhost:8080/api/scooters/add",
        method: "POST",
        data: this.newScooter,
        success: (res) => {
          if (res.statusCode === 200) {
            common_vendor.index.showToast({ title: "Scooter Added", icon: "success" });
            this.newScooter = { location: "", priceHour: null, priceFourHour: null, priceDay: null, priceWeek: null, status: 1, longitude: null, latitude: null };
            this.fetchScooters();
          } else {
            common_vendor.index.showToast({ title: res.data.message, icon: "none" });
          }
        },
        fail: () => {
          common_vendor.index.showToast({ title: "Failed to add scooter", icon: "none" });
        }
      });
    },
    getStatusClass(status) {
      const statusMap = {
        0: "unavailable",
        1: "available",
        2: "maintenance"
      };
      return statusMap[status] || "unknown";
    },
    getStatusText(status) {
      const textMap = {
        0: "Unavailable",
        1: "Available",
        2: "Maintenance"
      };
      return textMap[status] || "Unknown";
    },
    async changeScooterStatus(id) {
      try {
        const res = await common_vendor.index.request({
          url: `http://localhost:8080/api/scooters/changeStatus/${id}`,
          method: "GET"
        });
        if (res.statusCode === 200) {
          this.fetchScooters();
          common_vendor.index.showToast({ title: "状态更新成功", icon: "success" });
        } else {
          common_vendor.index.showToast({ title: "状态更新失败", icon: "none" });
        }
      } catch (err) {
        common_vendor.index.showToast({ title: "网络错误", icon: "none" });
      }
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: common_assets._imports_0$3,
    b: common_vendor.f($data.menus, (v, k0, i0) => {
      return {
        a: v.icon,
        b: common_vendor.t(v.name),
        c: v.id,
        d: $data.activeMenu === v.id ? 1 : "",
        e: common_vendor.o(($event) => $options.selectMenu(v.id), v.id)
      };
    }),
    c: $data.activeMenu === 1
  }, $data.activeMenu === 1 ? {
    d: $data.newScooter.location,
    e: common_vendor.o(($event) => $data.newScooter.location = $event.detail.value),
    f: $data.newScooter.priceHour,
    g: common_vendor.o(($event) => $data.newScooter.priceHour = $event.detail.value),
    h: $data.newScooter.priceFourHour,
    i: common_vendor.o(($event) => $data.newScooter.priceFourHour = $event.detail.value),
    j: $data.newScooter.priceDay,
    k: common_vendor.o(($event) => $data.newScooter.priceDay = $event.detail.value),
    l: $data.newScooter.priceWeek,
    m: common_vendor.o(($event) => $data.newScooter.priceWeek = $event.detail.value),
    n: $data.newScooter.status,
    o: common_vendor.o(($event) => $data.newScooter.status = $event.detail.value),
    p: $data.newScooter.longitude,
    q: common_vendor.o(($event) => $data.newScooter.longitude = $event.detail.value),
    r: $data.newScooter.latitude,
    s: common_vendor.o(($event) => $data.newScooter.latitude = $event.detail.value),
    t: common_vendor.o((...args) => $options.addScooter && $options.addScooter(...args))
  } : {}, {
    v: $data.activeMenu === 2
  }, $data.activeMenu === 2 ? {} : {}, {
    w: $data.activeMenu === 3
  }, $data.activeMenu === 3 ? {
    x: common_vendor.f($data.scooters, (scooter, k0, i0) => {
      return {
        a: common_vendor.t(scooter.id),
        b: common_vendor.t(scooter.location),
        c: common_vendor.t(scooter.priceHour),
        d: common_vendor.t(scooter.priceDay),
        e: common_vendor.t(scooter.priceWeek),
        f: common_vendor.t(scooter.longitude),
        g: common_vendor.t(scooter.latitude),
        h: common_vendor.t($options.getStatusText(scooter.status)),
        i: common_vendor.n($options.getStatusClass(scooter.status)),
        j: common_vendor.o(($event) => $options.changeScooterStatus(scooter.id), scooter.id),
        k: scooter.id
      };
    })
  } : {}, {
    y: $data.activeMenu === 4
  }, $data.activeMenu === 4 ? {} : {}, {
    z: $data.activeMenu === 5
  }, $data.activeMenu === 5 ? {} : {});
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/admin/adminDashboard.js.map
