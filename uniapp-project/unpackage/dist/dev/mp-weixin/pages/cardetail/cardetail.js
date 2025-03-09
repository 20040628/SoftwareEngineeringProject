"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
const _sfc_main = {
  data() {
    return {
      scooter: null,
      scooterID: null,
      selectedDuration: 0,
      // 默认选择第一个选项
      durationOptions: [],
      message: "",
      messageType: "",
      bookingForm: {
        hireType: "",
        startDate: "",
        startTime: ""
      },
      timeline: [],
      loading: false,
      currentUser: {
        userId: common_vendor.index.getStorageSync("userId") || null
      }
    };
  },
  async onLoad(options) {
    this.scooterID = options.id;
    await this.loadScooterDetail();
    this.refreshTimeline();
    this.bookingForm.startTime = (/* @__PURE__ */ new Date()).toISOString();
  },
  computed: {
    // 计算总价格
    calculatedPrice() {
      if (!this.durationOptions.length)
        return 0;
      const option = this.durationOptions[this.selectedDuration];
      return ((option == null ? void 0 : option.price) || 0).toFixed(2);
    }
  },
  // // 车辆状态处理
  // statusText() {
  //   const statusMap = {
  //     available: '可租用',
  //     rented: '使用中',
  //     low_battery: '低电量',
  //     maintenance: '维护中'
  //   }
  //   return statusMap[this.scooter.status]
  // },
  // statusClass() {
  //   return `status-${this.scooter.status}`
  // },
  //   // 是否可租用
  //   canRent() {
  //     return this.scooter.status === 'available' || 
  //           this.scooter.status === 'low_battery'
  //   },
  //   // 租车按钮文字
  //   rentButtonText() {
  //     if (this.scooter.status === 'low_battery') return '低电量可租用'
  //     if (this.scooter.status === 'available') return '立即租用'
  //     return '暂时不可用'
  //   }
  methods: {
    async loadScooterDetail() {
      try {
        const res = await common_vendor.index.request({
          url: `http://localhost:8080/api/scooters/${this.scooterID}`,
          method: "GET"
        });
        if (res.statusCode === 200) {
          this.scooter = res.data;
          this.durationOptions = [
            { label: "1小时", value: 1, type: "hour", price: this.scooter.priceHour },
            { label: "4小时", value: 4, type: "hour", price: this.scooter.priceHour * 4 },
            { label: "1天", value: 1, type: "day", price: this.scooter.priceDay },
            { label: "1周", value: 1, type: "week", price: this.scooter.priceWeek }
          ];
        } else {
          common_vendor.index.showToast({ title: "车辆信息获取失败", icon: "none" });
        }
      } catch (err) {
        common_vendor.index.showToast({ title: "网络错误", icon: "none" });
      } finally {
        this.isLoading = false;
      }
    },
    retryLoad() {
      this.isLoading = true;
      this.loadScooterDetail();
    },
    selectDuration(index) {
      this.selectedDuration = index;
    },
    onDateChange(event) {
      this.bookingForm.startDate = event.detail.value;
      this.updateStartTime();
    },
    // 时间选择改变
    onTimeChange(event) {
      this.bookingForm.startTime = event.detail.value;
      this.updateStartTime();
    },
    // 更新完整的日期时间
    updateStartTime() {
      if (this.bookingForm.startDate && this.bookingForm.startTime) {
        const startDateTime = `${this.bookingForm.startDate} ${this.bookingForm.startTime}`;
        common_vendor.index.__f__("log", "at pages/cardetail/cardetail.vue:209", "选中的开始时间:", startDateTime);
      }
    },
    // 处理租赁类型选择
    onHireTypeChange(e) {
      this.bookingForm.hireType = this.durationOptions[this.selectedDuration];
    },
    // 提交预订
    async submitBooking() {
      if (!this.bookingForm.startTime) {
        this.showMessage("请选择开始时间", "error");
        return;
      }
      if (!this.bookingForm.hireType) {
        this.showMessage("请选择租赁类型", "error");
        return;
      }
      this.loading = true;
      common_vendor.index.showLoading({ title: "提交中..." });
      try {
        const startTime = this.bookingForm.startTime.replace("T", " ") + ":00";
        const bookingData = {
          userId: this.currentUser.userId,
          scooterId: this.selectedScooter.id,
          hireType: this.bookingForm.hireType,
          startTime
        };
        const res = await new Promise((resolve, reject) => {
          common_vendor.index.request({
            url: "http://localhost:8080/api/bookings",
            method: "POST",
            data: bookingData,
            header: { "Content-Type": "application/json" },
            success: (response) => resolve(response),
            fail: (error) => reject(error)
          });
        });
        if (res.statusCode === 200) {
          this.showMessage("预订成功", "success");
          await this.refreshTimeline();
        } else {
          this.showMessage(res.data.message || "预订失败", "error");
        }
      } catch (error) {
        this.showMessage("请求失败，请重试", "error");
      } finally {
        this.loading = false;
        common_vendor.index.hideLoading();
      }
    },
    // 显示提示信息
    showMessage(text, type) {
      this.message = text;
      this.messageType = type;
      setTimeout(() => {
        this.message = "";
      }, 3e3);
    }
  },
  // 添加刷新时间轴的方法
  async refreshTimeline() {
    if (this.scooter) {
      try {
        common_vendor.index.request({
          url: `http://localhost:8080/api/bookings/timeline/${this.scooterID}`,
          method: "GET",
          success: (res) => {
            this.timeline = res.data;
          },
          fail: (err) => {
            common_vendor.index.__f__("error", "at pages/cardetail/cardetail.vue:287", "刷新时间轴失败:", err);
          }
        });
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/cardetail/cardetail.vue:291", "刷新时间轴出错:", error);
      }
    }
  },
  calculateSlotWidth(slot) {
    const start = new Date(slot.startTime).getTime();
    const end = new Date(slot.endTime).getTime();
    const duration = end - start;
    const totalDuration = 7 * 24 * 60 * 60 * 1e3;
    return duration / totalDuration * 100;
  },
  formatTimeSlot(slot) {
    const start = new Date(slot.startTime);
    const end = new Date(slot.endTime);
    if (slot.status === "booked") {
      let hirePeriodText;
      switch (slot.hirePeriod) {
        case "HOUR":
          hirePeriodText = "1小时";
          break;
        case "FOUR_HOURS":
          hirePeriodText = "4小时";
          break;
        case "DAY":
          hirePeriodText = "1天";
          break;
        case "WEEK":
          hirePeriodText = "1周";
          break;
        default:
          hirePeriodText = slot.hirePeriod;
      }
      return `${this.formatTime(start)} - ${this.formatTime(end)} (${hirePeriodText})`;
    }
    return "可预订";
  },
  formatTime(date) {
    return `${date.getMonth() + 1}/${date.getDate()} ${String(date.getHours()).padStart(2, "0")}:${String(date.getMinutes()).padStart(2, "0")}`;
  },
  formatDate(date) {
    return `${date.getMonth() + 1}月${date.getDate()}日`;
  },
  addDays(date, days) {
    const result = new Date(date);
    result.setDate(result.getDate() + days);
    return result;
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: common_vendor.t($data.scooter.id),
    b: common_vendor.t($options.calculatedPrice),
    c: $data.durationOptions.length
  }, $data.durationOptions.length ? {
    d: common_vendor.t($data.durationOptions[$data.selectedDuration].label)
  } : {}, {
    e: $data.scooter
  }, $data.scooter ? {
    f: common_assets._imports_0,
    g: common_vendor.t($data.scooter.location || "未知地址"),
    h: common_assets._imports_1$1,
    i: common_vendor.o((...args) => _ctx.openMap && _ctx.openMap(...args))
  } : {}, {
    j: common_vendor.f(7, (day, k0, i0) => {
      return {
        a: common_vendor.t(_ctx.formatDate(_ctx.addDays(/* @__PURE__ */ new Date(), day - 1))),
        b: day
      };
    }),
    k: common_vendor.f($data.timeline, (slot, index, i0) => {
      return {
        a: common_vendor.t(_ctx.formatTimeSlot(slot)),
        b: index,
        c: common_vendor.n(slot.status),
        d: _ctx.calculateSlotWidth(slot) + "%"
      };
    }),
    l: common_vendor.f($data.durationOptions, (option, index, i0) => {
      return {
        a: common_vendor.t(option.label),
        b: common_vendor.t(option.subLabel),
        c: index,
        d: $data.selectedDuration === index ? 1 : "",
        e: common_vendor.o(($event) => $options.selectDuration(index), index)
      };
    }),
    m: common_vendor.t($data.bookingForm.startDate || "请选择开始日期"),
    n: $data.bookingForm.startDate,
    o: common_vendor.o((...args) => $options.onDateChange && $options.onDateChange(...args)),
    p: common_vendor.t($data.bookingForm.startTime || "请选择开始时间"),
    q: $data.bookingForm.startTime,
    r: common_vendor.o((...args) => $options.onTimeChange && $options.onTimeChange(...args)),
    s: common_vendor.t($data.loading ? "处理中..." : "确认预订"),
    t: common_vendor.o((...args) => $options.submitBooking && $options.submitBooking(...args)),
    v: $data.loading,
    w: common_vendor.o((...args) => _ctx.closeModal && _ctx.closeModal(...args)),
    x: $data.message
  }, $data.message ? {
    y: common_vendor.t($data.message),
    z: common_vendor.n($data.messageType === "success" ? "success-msg" : "error-msg")
  } : {});
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-a91970b9"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/cardetail/cardetail.js.map
