"use strict";
const common_vendor = require("../../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      sendData: {
        feedbackContent: ""
        // 反馈内容
      },
      loading: false,
      // 按钮加载状态
      message: "",
      messageType: ""
    };
  },
  methods: {
    async submitFeedback() {
      this.loading = true;
      const token = String(common_vendor.index.getStorageSync("token"));
      try {
        const [err, res] = await common_vendor.index.request({
          url: "http://localhost:8080/api/feedback",
          // 后端地址
          method: "POST",
          data: { content: this.sendData.feedbackContent },
          header: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          }
        }).then((res2) => [null, res2]).catch((err2) => [err2, null]);
        if (err || res.statusCode !== 200) {
          let errorMessage = "Submition failed";
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
        common_vendor.index.__f__("log", "at pages/feedback/feedback/feedback.vue:82", "提交成功:", res.data);
        common_vendor.index.showToast({
          title: "Submition successful!",
          icon: "success",
          duration: 2e3
        });
        this.sendData = {
          feedbackContent: ""
        };
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/feedback/feedback/feedback.vue:99", "Submition error:", error);
      } finally {
        this.loading = false;
      }
    }
  }
};
if (!Array) {
  const _easycom_uni_file_picker2 = common_vendor.resolveComponent("uni-file-picker");
  _easycom_uni_file_picker2();
}
const _easycom_uni_file_picker = () => "../../../uni_modules/uni-file-picker/components/uni-file-picker/uni-file-picker.js";
if (!Math) {
  _easycom_uni_file_picker();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.sendData.feedbackContent,
    b: common_vendor.o(($event) => $data.sendData.feedbackContent = $event.detail.value),
    c: common_vendor.o(($event) => $data.sendData.imgs = $event),
    d: common_vendor.p({
      ["file-mediatype"]: "image",
      limit: 6,
      ["return-type"]: "array",
      modelValue: $data.sendData.imgs
    }),
    e: !$data.sendData.feedbackContent,
    f: common_vendor.o((...args) => $options.submitFeedback && $options.submitFeedback(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/feedback/feedback/feedback.js.map
