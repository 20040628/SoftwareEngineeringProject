"use strict";
const common_vendor = require("./common/vendor.js");
const _sfc_main = {
  data() {
    return {
      searchKeyword: "",
      scooters: [],
      isLoading: true,
      options: ["all", "available"],
      // 选项列表
      selectedIndex: 0,
      // 默认选择索引
      selectedOption: "all",
      // 默认选择的值
      bannerList: [
        { url: "/static/images/banner1.png" },
        { url: "/static/images/banner2.png" },
        { url: "/static/images/banner3.png" }
      ],
      screeningAList: [
        {
          "id": 0,
          "name": "All",
          "type": true
        },
        {
          id: 1,
          "name": "Available",
          "type": false
        }
      ],
      pricingOptions: [
        { duration: "hour", price: "$5.00" },
        { duration: "day", price: "$20.00" },
        { duration: "week", price: "$100.00" }
      ],
      sosoTxt: "",
      // 搜索内容
      sosoImg: "/static/icons/search.png"
      // 搜索图标路径	
    };
  },
  computed: {
    filteredScooters() {
      return this.selectedOption === "all" ? this.scooters : this.scooters.filter((scooter) => scooter.status === 1);
    }
  },
  async mounted() {
    await this.loadScooters();
  },
  methods: {
    // 获取车辆列表
    async loadScooters() {
      try {
        const res = await common_vendor.index.request({
          url: "http://localhost:8080/api/scooters/getAll",
          // 后端接口地址
          method: "GET"
          // 请求方法为 GET
        });
        if (res.statusCode === 200) {
          this.scooters = res.data;
        } else {
          common_vendor.index.showToast({ title: "数据加载失败", icon: "none" });
        }
      } catch (err) {
        common_vendor.index.showToast({ title: "网络错误", icon: "none" });
      } finally {
        this.isLoading = false;
      }
    },
    load: function(e) {
      common_vendor.index.__f__("log", "at pages/index/index.vue:133", "load");
    },
    // 跳转到详情页
    showScooterDetail(scooter) {
      common_vendor.index.navigateTo({
        url: `/pages/cardetail/cardetail?id=${scooter.id}`
      });
    },
    screeningA(index) {
      this.screeningAList.forEach((item, i) => {
        item.type = i === index;
      });
      this.selectedOption = this.options[index];
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.f($data.bannerList, (item, index, i0) => {
      return {
        a: item.url,
        b: index
      };
    }),
    b: $data.sosoImg,
    c: common_vendor.o((...args) => _ctx.getList && _ctx.getList(...args)),
    d: $data.sosoTxt,
    e: common_vendor.o(($event) => $data.sosoTxt = $event.detail.value),
    f: common_vendor.f($data.screeningAList, (item, index, i0) => {
      return {
        a: common_vendor.t(item.name),
        b: index,
        c: common_vendor.o(($event) => $options.screeningA(index), index),
        d: common_vendor.n(item.type ? "screeningA-con-active" : "")
      };
    }),
    g: common_vendor.f($data.pricingOptions, (option, index, i0) => {
      return {
        a: common_vendor.t(option.price),
        b: common_vendor.t(option.duration),
        c: index
      };
    }),
    h: common_vendor.f($options.filteredScooters, (scooter, k0, i0) => {
      return {
        a: common_vendor.t(scooter.location),
        b: common_vendor.o(($event) => $options.showScooterDetail(scooter), scooter.id),
        c: scooter.id
      };
    })
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-1cf27b2a"]]);
exports.MiniProgramPage = MiniProgramPage;
//# sourceMappingURL=../.sourcemap/mp-weixin/index.js.map
