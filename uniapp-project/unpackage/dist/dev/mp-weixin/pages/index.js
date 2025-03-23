"use strict";
const common_vendor = require("../common/vendor.js");
const indexPage = () => "./index/index2.js";
const foundPage = () => "./found/found2.js";
const myPage = () => "./UserCenter/UserCenter2.js";
const mapPage = () => "./map/map2.js";
const tabBar = () => "../components/tabBar.js";
const _sfc_main = {
  components: {
    indexPage,
    foundPage,
    myPage,
    mapPage,
    tabBar
  },
  data() {
    return {
      nowBarIndex: 0,
      pageTitles: ["首页", "地图", "发现", "我的"]
    };
  },
  methods: {
    // 切换bar
    nowBar: function(e) {
      let that = this;
      if (e == that.nowBarIndex) {
        return;
      }
      that.nowBarIndex = e;
      that.tabPage(e);
    },
    tabPage: function(e) {
      let that = this;
      this.$nextTick(() => {
        switch (Number(e)) {
          case 0:
            that.$refs.indexPage.load(1);
            break;
          case 1:
            that.$refs.mapPage.load();
            break;
          case 3:
            that.$refs.foundPage.load();
            break;
          case 4:
            that.$refs.myPage.load();
            break;
        }
      });
    },
    // 停止下拉刷新
    stopDown: function() {
      common_vendor.index.stopPullDownRefresh();
    }
  }
};
if (!Array) {
  const _component_indexPage = common_vendor.resolveComponent("indexPage");
  const _component_mapPage = common_vendor.resolveComponent("mapPage");
  const _component_foundPgae = common_vendor.resolveComponent("foundPgae");
  const _component_myPage = common_vendor.resolveComponent("myPage");
  const _component_tabBar = common_vendor.resolveComponent("tabBar");
  (_component_indexPage + _component_mapPage + _component_foundPgae + _component_myPage + _component_tabBar)();
}
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.sr("indexPage", "02281a80-0"),
    b: common_vendor.o($options.stopDown),
    c: $data.nowBarIndex === 0,
    d: common_vendor.sr("mapPage", "02281a80-1"),
    e: common_vendor.p({
      nowTabHeight: _ctx.nowTabHeight,
      paddingBottom: _ctx.nowTabPaddingBottom
    }),
    f: $data.nowBarIndex === 1,
    g: common_vendor.sr("foundPgae", "02281a80-2"),
    h: common_vendor.o($options.stopDown),
    i: $data.nowBarIndex === 3,
    j: common_vendor.sr("myPage", "02281a80-3"),
    k: $data.nowBarIndex === 4,
    l: common_vendor.o($options.nowBar)
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-02281a80"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../.sourcemap/mp-weixin/pages/index.js.map
