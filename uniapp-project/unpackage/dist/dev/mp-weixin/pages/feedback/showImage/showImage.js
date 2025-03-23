"use strict";
const common_vendor = require("../../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      imgs: [],
      currentImg: 0
    };
  },
  onLoad(options) {
    let {
      imgs,
      current
    } = options;
    this.imgs = JSON.parse(imgs);
    this.currentImg = current;
  },
  methods: {
    changeSwiper(e) {
      this.currentImg = e.detail.current;
    },
    toImg(index) {
      this.currentImg = index;
    },
    back() {
      if (this.isPop) {
        this.isPop = false;
        return;
      }
      try {
        this.$Router.back(1);
      } catch (e) {
        common_vendor.index.navigateBack();
      }
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: common_vendor.f($data.imgs, (item, index, i0) => {
      return {
        a: item.url,
        b: index
      };
    }),
    b: $data.currentImg,
    c: common_vendor.o((...args) => $options.changeSwiper && $options.changeSwiper(...args)),
    d: $data.imgs.length > 1
  }, $data.imgs.length > 1 ? {
    e: common_vendor.f($data.imgs, (item, index, i0) => {
      return {
        a: item.url,
        b: common_vendor.n($data.currentImg == index ? "img-page-checked" : ""),
        c: index,
        d: common_vendor.o(($event) => $options.toImg(index), index)
      };
    })
  } : {}, {
    f: common_vendor.o((...args) => $options.back && $options.back(...args))
  });
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-5a808144"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/feedback/showImage/showImage.js.map
