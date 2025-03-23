"use strict";
const common_vendor = require("../../../common/vendor.js");
const _sfc_main = {
  data() {
    return {
      feedbacks: [],
      isLoading: true
    };
  },
  onLoad() {
    this.getUserFeedback();
  },
  methods: {
    //获取用户意见反馈列表
    async getUserFeedback() {
      try {
        const token = String(common_vendor.index.getStorageSync("token"));
        const res = await common_vendor.index.request({
          url: "http://localhost:8080/api/feedback/user",
          method: "GET",
          header: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          }
        });
        if (res.statusCode === 200) {
          this.feedbacks = res.data;
        } else {
          common_vendor.index.showToast({ title: "数据加载失败", icon: "none" });
        }
      } catch (err) {
        common_vendor.index.showToast({ title: "网络错误", icon: "none" });
      } finally {
        this.isLoading = false;
      }
    },
    feedbackClick(item) {
      common_vendor.index.navigateTo({
        url: "/pages/feedback/feedbackDetail/feedbackDetail?feedback=" + JSON.stringify(item),
        success: (res) => {
        },
        fail: () => {
        },
        complete: () => {
        }
      });
    },
    statusText(status) {
      const statusMap = {
        pending: "pending",
        processing: "processing",
        resolved: "resolved"
      };
      return statusMap[status] || "unknown state";
    },
    statusClass(status) {
      const classMap = {
        pending: "state-grey",
        processing: "state-grey",
        resolved: "state-green"
      };
      return classMap[status] || "state-grey";
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $data.feedbacks.length == 0
  }, $data.feedbacks.length == 0 ? {} : {
    b: common_vendor.f($data.feedbacks, (item, index, i0) => {
      return common_vendor.e({
        a: common_vendor.t(item.content),
        b: item.status === "pending"
      }, item.status === "pending" ? {} : {}, {
        c: item.status === "processing"
      }, item.status === "processing" ? {} : {}, {
        d: item.status === "resolved"
      }, item.status === "resolved" ? {
        e: common_vendor.t(item.adminResponse)
      } : {}, {
        f: common_vendor.t(item.createTime.replace("T", " ")),
        g: common_vendor.t($options.statusText(item.status)),
        h: common_vendor.n($options.statusClass(item.status)),
        i: index,
        j: common_vendor.o(($event) => $options.feedbackClick(item), index)
      });
    })
  });
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/feedback/feedbackList/feedbackList.js.map
