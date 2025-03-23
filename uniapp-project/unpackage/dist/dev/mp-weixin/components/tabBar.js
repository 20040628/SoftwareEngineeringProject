"use strict";
const common_vendor = require("../common/vendor.js");
const _sfc_main = {
  props: {
    currentIndex: Number
    // 接收当前选中的页面索引
  },
  data() {
    return {
      nowTabPaddingBottom: this.getTabPaddingBottom() || "56px",
      // 防止 undefined
      nowBar: this.currentIndex || 0,
      scanCodeImg: "../static/tabbar/scanCode.png",
      // 扫码按钮图片
      defaultIcons: {
        home: "../static/tabbar/index.png",
        map: "../static/tabbar/map.png",
        found: "../static/tabbar/found.png",
        my: "../static/tabbar/my.png"
      },
      activeIcons: {
        home: "../static/tabbar/index-active.png",
        map: "../static/tabbar/map-active.png",
        found: "../static/tabbar/found-active.png",
        my: "../static/tabbar/my-active.png"
      }
    };
  },
  methods: {
    // 切换 tab，更新当前选中的 tab
    tabBar(index) {
      common_vendor.index.__f__("log", "at components/tabBar.vue:62", "11111111111111111111");
      if (this.nowBar !== index) {
        this.nowBar = index;
        this.$emit("nowBar", index);
      }
    },
    getTabPaddingBottom() {
      let padding = common_vendor.index.getStorageSync("nowTabPaddingBottom");
      if (padding && typeof padding === "string") {
        return padding;
      }
      return null;
    }
  },
  watch: {
    // 监听外部的 currentIndex 变化，更新 nowBar
    currentIndex(newVal) {
      this.nowBar = newVal;
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.nowBar === 0 ? $data.activeIcons.home : $data.defaultIcons.home,
    b: $data.nowBar === 0 ? 1 : "",
    c: common_vendor.o(($event) => $options.tabBar(0)),
    d: $data.nowBar === 1 ? $data.activeIcons.map : $data.defaultIcons.map,
    e: $data.nowBar === 1 ? 1 : "",
    f: common_vendor.o(($event) => $options.tabBar(1)),
    g: $data.scanCodeImg,
    h: common_vendor.o(($event) => $options.tabBar(2)),
    i: $data.nowBar === 3 ? $data.activeIcons.found : $data.defaultIcons.found,
    j: $data.nowBar === 3 ? 1 : "",
    k: common_vendor.o(($event) => $options.tabBar(3)),
    l: $data.nowBar === 4 ? $data.activeIcons.my : $data.defaultIcons.my,
    m: $data.nowBar === 4 ? 1 : "",
    n: common_vendor.o(($event) => $options.tabBar(4)),
    o: $data.nowTabPaddingBottom
  };
}
const Component = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-22586f07"]]);
wx.createComponent(Component);
//# sourceMappingURL=../../.sourcemap/mp-weixin/components/tabBar.js.map
