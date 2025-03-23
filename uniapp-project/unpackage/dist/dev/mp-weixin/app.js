"use strict";
Object.defineProperty(exports, Symbol.toStringTag, { value: "Module" });
const common_vendor = require("./common/vendor.js");
if (!Math) {
  "./pages/index.js";
  "./pages/UserLogin/UserLogin.js";
  "./pages/UserRegister/UserRegister.js";
  "./pages/index/index.js";
  "./pages/map/map.js";
  "./pages/found/found.js";
  "./pages/cardetail/cardetail.js";
  "./pages/UserCenter/UserCenter.js";
  "./pages/feedback/feedbackIndex/feedbackIndex.js";
  "./pages/feedback/feedbackList/feedbackList.js";
  "./pages/feedback/feedback/feedback.js";
  "./pages/feedback/helpDetail/helpDetail.js";
  "./pages/feedback/feedbackDetail/feedbackDetail.js";
  "./pages/feedback/showImage/showImage.js";
  "./pages/admin/adminDashboard.js";
}
const _sfc_main = {
  onLaunch: function() {
    common_vendor.index.__f__("log", "at App.vue:4", "App Launch");
    this.checkAuth();
  },
  onShow: function() {
    common_vendor.index.__f__("log", "at App.vue:8", "App Show");
  },
  onHide: function() {
    common_vendor.index.__f__("log", "at App.vue:11", "App Hide");
  },
  methods: {
    checkAuth() {
      const token = common_vendor.index.getStorageSync("token");
      if (!token) {
        common_vendor.index.__f__("log", "at App.vue:18", "未找到 Token，跳转到登录页...");
        common_vendor.index.redirectTo({ url: "/pages/UserLogin/UserLogin" });
        return;
      }
      common_vendor.index.__f__("log", "at App.vue:23", "检测 Token 是否有效...");
      common_vendor.index.request({
        url: " http://localhost:8080/api/auth/validate",
        // 替换成你的 API 地址
        method: "GET",
        header: { "Authorization": "Bearer " + token },
        success: (res) => {
          if (res.statusCode === 200 && res.data.valid) {
            common_vendor.index.__f__("log", "at App.vue:30", "Token 验证通过，用户 ID:", res.data.userId);
            common_vendor.index.setStorageSync("userInfo", res.data);
          } else {
            common_vendor.index.__f__("log", "at App.vue:33", "Token 无效，清除并跳转到登录页...");
            common_vendor.index.removeStorageSync("token");
            common_vendor.index.redirectTo({ url: "/pages/UserLogin/UserLogin" });
          }
        },
        fail: (err) => {
          common_vendor.index.__f__("error", "at App.vue:39", "Token 验证请求失败:", err);
          common_vendor.index.showToast({ title: "网络错误", icon: "none" });
          common_vendor.index.redirectTo({ url: "/pages/UserLogin/UserLogin" });
        }
      });
    }
  }
};
function createApp() {
  const app = common_vendor.createSSRApp(_sfc_main);
  return {
    app
  };
}
createApp().app.mount("#app");
exports.createApp = createApp;
//# sourceMappingURL=../.sourcemap/mp-weixin/app.js.map
