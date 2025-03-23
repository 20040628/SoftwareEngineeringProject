"use strict";
const common_vendor = require("../../../common/vendor.js");
const common_assets = require("../../../common/assets.js");
const _sfc_main = {
  data() {
    return {
      problemBox: [
        {
          problemTitle: "小程序",
          problemList: [
            {
              title: "为什么提示不支持打开非业务域名",
              content: "微信小程序内部对网页链接进行了限制，遇到此情况可复制链接至手机浏览器中打开。"
            },
            {
              title: "文件类课件如何保存",
              content: "对于文件类的课件，请点击[在线预览]，稍等片刻之后程序会跳转到对应的预览页面。点击右上角三个点按钮，在下方弹窗中选择[保存到手机]即可。也可对课件进行[转发][收藏]等操作"
            }
          ]
        },
        {
          problemTitle: "活动报名",
          problemList: [
            {
              title: "已经报名的活动可以取消吗",
              content: "在报名截止日期之前均可以取消。点击活动报名详情下方的[取消报名]按钮即可。"
            }
          ]
        },
        {
          problemTitle: "调查问卷",
          problemList: [
            {
              title: "调查问卷是否可以多次填写",
              content: "不可以。完成的调查问卷会处于不可点击状态，所以请您认真对待每一次问卷的填写工作。感谢您的支持。"
            }
          ]
        },
        {
          problemTitle: "其他",
          problemList: [
            {
              title: "如何修改用户头像",
              content: "请点击[我的] - [账号管理] - [头像]即可更换用户头像"
            },
            {
              title: "如何修改登录密码",
              content: "请点击[我的] - [账号管理] - [修改密码]即可修改登录密码"
            },
            {
              title: "如何退出登录",
              content: "请点击[我的] - [账号管理] - [退出登录]即可退出登录"
            },
            {
              title: "系统反应迟钝",
              content: "如果出现系统反应迟钝、响应时间过长的问题，可查看本机的网络状态，或重启程序后再次尝试。"
            }
          ]
        }
      ]
    };
  },
  methods: {
    onClick(item) {
      common_vendor.index.navigateTo({
        url: "/pages/feedback/helpDetail/helpDetail?title=" + item.title + "&content=" + item.content,
        success: (res) => {
        },
        fail: () => {
        },
        complete: () => {
        }
      });
    },
    toFeedbackList() {
      common_vendor.index.navigateTo({
        url: "/pages/feedback/feedbackList/feedbackList",
        success: (res) => {
        },
        fail: () => {
        },
        complete: () => {
        }
      });
    },
    toFeedback() {
      common_vendor.index.navigateTo({
        url: "/pages/feedback/feedback/feedback",
        success: (res) => {
        },
        fail: () => {
        },
        complete: () => {
        }
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_vendor.f($data.problemBox, (item, index, i0) => {
      return {
        a: common_vendor.t(item.problemTitle),
        b: common_vendor.f(item.problemList, (problemItem, problemIndex, i1) => {
          return common_vendor.e({
            a: common_vendor.t(problemItem.title),
            b: problemIndex != item.problemList.length - 1
          }, problemIndex != item.problemList.length - 1 ? {} : {}, {
            c: problemIndex,
            d: common_vendor.o(($event) => $options.onClick(problemItem), problemIndex)
          });
        }),
        c: index,
        d: item.problemTitle
      };
    }),
    b: common_assets._imports_0$1,
    c: common_vendor.o((...args) => $options.toFeedbackList && $options.toFeedbackList(...args)),
    d: common_assets._imports_1$1,
    e: common_vendor.o((...args) => $options.toFeedback && $options.toFeedback(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../../.sourcemap/mp-weixin/pages/feedback/feedbackIndex/feedbackIndex.js.map
