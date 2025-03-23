"use strict";
const common_vendor = require("./common/vendor.js");
const common_assets = require("./common/assets.js");
const _sfc_main = {
  data() {
    return {
      username: "",
      // 用于存储用户名
      menus: [
        [
          { name: "My Account", icon: "/static/center/self.png", arrow: true },
          { name: "My Order", icon: "/static/center/heart.png", arrow: true },
          { name: "Contact customer service", icon: "/static/center/kefu.png", arrow: true },
          { name: "Feedback", icon: "/static/center/share.png", arrow: true },
          { name: "Logout", icon: "/static/center/out.png", arrow: false }
          // 添加 "Logout" 菜单项
        ]
      ]
    };
  },
  onLoad() {
    const user = common_vendor.index.getStorageSync("userInfo");
    if (user) {
      this.username = user.username;
    } else {
      this.username = "Unknown User";
    }
  },
  methods: {
    menuClick(menuItem) {
      if (menuItem.name === "Logout") {
        this.logout();
      } else if (menuItem.name === "Feedback") {
        common_vendor.index.navigateTo({
          url: "/pages/feedback/feedbackIndex/feedbackIndex"
        });
      } else {
        common_vendor.index.__f__("log", "at pages/UserCenter/UserCenter.vue:60", menuItem);
      }
    },
    load: function(e) {
      common_vendor.index.__f__("log", "at pages/UserCenter/UserCenter.vue:64", "load");
    },
    logout() {
      common_vendor.index.removeStorageSync("userInfo");
      common_vendor.index.removeStorageSync("token");
      common_vendor.index.reLaunch({
        url: "/pages/UserLogin/UserLogin"
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_assets._imports_0$4,
    b: common_assets._imports_1$3,
    c: common_assets._imports_0$3,
    d: common_vendor.t($data.username),
    e: common_assets._imports_3$1,
    f: common_vendor.f($data.menus, (v, k, i0) => {
      return {
        a: common_vendor.f(v, (v_, k_, i1) => {
          return common_vendor.e({
            a: v_.icon,
            b: common_vendor.t(v_.name),
            c: v_.arrow
          }, v_.arrow ? {
            d: common_assets._imports_3$1
          } : {}, {
            e: k_,
            f: common_vendor.o(($event) => $options.menuClick(v_), k_)
          });
        }),
        b: k
      };
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
exports.MiniProgramPage = MiniProgramPage;
//# sourceMappingURL=../.sourcemap/mp-weixin/UserCenter.js.map
