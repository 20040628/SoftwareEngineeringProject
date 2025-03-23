"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      username: "",
      password: "",
      code: "",
      token: null,
      // 存储登录后获取的 token
      errorMessage: ""
    };
  },
  methods: {
    handleSubmit() {
      if (!this.username) {
        this.toast("Please enter your username");
        return;
      }
      if (!this.isCodeLogin && !this.password) {
        this.toast("Please enter your password");
        return;
      }
      this.loginWithPassword();
    },
    async loginWithPassword() {
      this.loading = true;
      this.message = "";
      try {
        const [err, res] = await common_vendor.index.request({
          url: "http://localhost:8080/api/auth/login",
          // 后端登录接口
          method: "POST",
          data: {
            username: this.username,
            password: this.password
          },
          header: { "Content-Type": "application/json" }
        }).then((res2) => [null, res2]).catch((err2) => [err2, null]);
        if (err || res.statusCode !== 200) {
          let errorMessage = "Login failed";
          if (res == null ? void 0 : res.data) {
            if (typeof res.data === "string") {
              errorMessage = res.data;
            } else if (typeof res.data === "object" && res.data.message) {
              errorMessage = res.data.message;
            }
          } else if (err) {
            errorMessage = err.message || "Network error, please try again";
          }
          this.message = errorMessage;
          this.messageType = "error";
          common_vendor.index.showToast({
            title: this.message,
            icon: "none",
            duration: 2e3
          });
          throw new Error(errorMessage);
        }
        this.token = res.data.token;
        common_vendor.index.setStorageSync("token", this.token);
        common_vendor.index.showToast({
          title: "Login successful!",
          icon: "success",
          duration: 2e3
        });
        setTimeout(() => {
          common_vendor.index.navigateTo({
            url: "/pages/index"
          });
        }, 2e3);
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/UserLogin/UserLogin.vue:112", "Login error:", error);
      } finally {
        this.loading = false;
      }
    },
    goToRegister() {
      common_vendor.index.navigateTo({
        url: "/pages/UserRegister/UserRegister"
        // 跳转到注册页面
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.username,
    b: common_vendor.o(($event) => $data.username = $event.detail.value),
    c: $data.password,
    d: common_vendor.o(($event) => $data.password = $event.detail.value),
    e: common_vendor.o(($event) => $options.handleSubmit()),
    f: common_vendor.o((...args) => $options.goToRegister && $options.goToRegister(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-aba76e30"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/UserLogin/UserLogin.js.map
