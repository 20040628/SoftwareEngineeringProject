"use strict";
const common_vendor = require("../../../common/vendor.js");
const common_assets = require("../../../common/assets.js");
const _sfc_main = {
  data() {
    return {
      feedback: {},
      isResolve: false,
      isShow: false
    };
  },
  onLoad(options) {
    this.feedback = JSON.parse(options.feedback);
  },
  methods: {
    resolve(isResolve) {
      if (!this.isShow) {
        this.isResolve = isResolve;
        this.isShow = true;
      }
    }
    // catImage(index) {
    // 	uni.navigateTo({
    // 		url: '/pages/feedback/showImage/showImage?imgs=' + JSON.stringify(this.feedback.images) + '&current=' +
    // 			index,
    // 		animationType: "fade-in",
    // 	})
    // }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: common_vendor.t($data.feedback.content),
    b: common_vendor.t($data.feedback.createTime.replace("T", " ")),
    c: common_vendor.t($data.feedback.createTime.replace("T", " ")),
    d: $data.feedback.adminResponse
  }, $data.feedback.adminResponse ? common_vendor.e({
    e: common_vendor.t($data.feedback.adminResponse),
    f: $data.isResolve && $data.isShow
  }, $data.isResolve && $data.isShow ? {
    g: common_assets._imports_0$2
  } : {
    h: common_assets._imports_1$2
  }, {
    i: common_vendor.o(($event) => $options.resolve(true)),
    j: common_vendor.n($data.isResolve && $data.isShow ? "btn-resolve" : "btn-normal"),
    k: !$data.isResolve && $data.isShow
  }, !$data.isResolve && $data.isShow ? {
    l: common_assets._imports_2
  } : {
    m: common_assets._imports_3
  }, {
    n: common_vendor.o(($event) => $options.resolve(false)),
    o: common_vendor.n(!$data.isResolve && $data.isShow ? "btn-unResolve" : "btn-normal"),
    p: common_vendor.t($data.feedback.responseTime.replace("T", " "))
  }) : {}, {
    q: $data.feedback.adminResponse && $data.isResolve && $data.isShow
  }, $data.feedback.adminResponse && $data.isResolve && $data.isShow ? {} : {}, {
    r: $data.feedback.adminResponse && !$data.isResolve && $data.isShow
  }, $data.feedback.adminResponse && !$data.isResolve && $data.isShow ? {} : {});
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/feedback/feedbackDetail/feedbackDetail.js.map
