"use strict";
const common_vendor = require("../../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      resultData: {
        title: "",
        content: ""
      }
    };
  },
  onLoad(options) {
    this.resultData.title = options.title;
    this.resultData.content = options.content;
    common_vendor.index.setNavigationBarTitle({
      title: options.title
    });
  },
  methods: {}
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.t($data.resultData.title),
    b: common_vendor.t($data.resultData.content)
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/feedback/helpDetail/helpDetail.js.map
