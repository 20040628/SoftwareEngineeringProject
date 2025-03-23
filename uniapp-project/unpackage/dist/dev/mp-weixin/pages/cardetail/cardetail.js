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
        startTime: "",
        startDateTime: ""
      },
      timeline: [],
      isLoading: true,
      currentUser: {
        userId: null
      }
    };
  },
  async onLoad(options) {
    this.scooterID = options.id;
    await this.loadScooterDetail();
    await this.refreshTimeline();
    const userInfo = common_vendor.index.getStorageSync("userInfo");
    if (userInfo) {
      this.currentUser.userId = userInfo.userId || null;
    }
  },
  computed: {
    // 计算总价格
    calculatedPrice() {
      if (!this.durationOptions.length)
        return 0;
      const option = this.durationOptions[this.selectedDuration];
      return ((option == null ? void 0 : option.price) || 0).toFixed(2);
    },
    statusClass() {
      const statusMap = {
        0: "unavailable",
        1: "available",
        2: "maintenance"
      };
      return statusMap[this.scooter.status] || "unknown";
    },
    statusText() {
      const textMap = {
        0: "unavailable",
        1: "available",
        2: "maintenance"
      };
      return textMap[this.scooter.status] || "unknown";
    }
  },
  // 是否可租用
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
    // 封装 uni.request 成 Promise 以支持 async/await
    request(options) {
      return new Promise((resolve, reject) => {
        common_vendor.index.request({
          ...options,
          success: (res) => {
            if (res.statusCode === 200) {
              resolve(res.data);
            } else {
              reject(new Error(`请求失败: ${res.statusCode}`));
            }
          },
          fail: (err) => reject(err)
        });
      });
    },
    async loadScooterDetail() {
      try {
        common_vendor.index.showLoading({ title: "加载中...", mask: true });
        const data = await this.request({
          url: `http://localhost:8080/api/scooters/${this.scooterID}`,
          method: "GET"
        });
        this.scooter = data;
        this.durationOptions = [
          { label: "1 Hour", value: 1, type: "hour", price: this.scooter.priceHour },
          { label: "4 Hours", value: 4, type: "hour", price: this.scooter.priceHour * 4 },
          { label: "1 Day", value: 1, type: "day", price: this.scooter.priceDay },
          { label: "1 Week", value: 1, type: "week", price: this.scooter.priceWeek }
        ];
      } catch (err) {
        common_vendor.index.showToast({ title: "网络错误", icon: "none" });
      } finally {
        common_vendor.index.hideLoading();
        this.isLoading = false;
      }
    },
    retryLoad() {
      this.isLoading = true;
      this.loadScooterDetail();
    },
    addDays(date, days) {
      return new Date(date.getTime() + days * 864e5);
    },
    formatDate(date) {
      return `${date.getMonth() + 1}.${date.getDate()}`;
    },
    // 添加刷新时间轴的方法
    async refreshTimeline() {
      const token = String(common_vendor.index.getStorageSync("token"));
      if (!this.scooterID)
        return;
      common_vendor.index.__f__("log", "at pages/cardetail/cardetail.vue:220", this.scooterID);
      try {
        this.timeline = await this.request({
          url: `http://localhost:8080/api/bookings/timeline/${this.scooterID}`,
          method: "GET",
          header: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          }
        });
      } catch (err) {
        common_vendor.index.__f__("error", "at pages/cardetail/cardetail.vue:231", "刷新时间轴失败:", err);
      }
      common_vendor.index.__f__("log", "at pages/cardetail/cardetail.vue:233", this.timeline);
    },
    calculateSlotWidth(slot) {
      const start = new Date(slot.startTime).getTime();
      const end = new Date(slot.endTime).getTime();
      const duration = end - start;
      common_vendor.index.__f__("log", "at pages/cardetail/cardetail.vue:240", slot.startTime, new Date(slot.startTime));
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
            hirePeriodText = "1hour";
            break;
          case "FOUR_HOURS":
            hirePeriodText = "4hours";
            break;
          case "DAY":
            hirePeriodText = "1day";
            break;
          case "WEEK":
            hirePeriodText = "1week";
            break;
          default:
            hirePeriodText = slot.hirePeriod;
        }
        return `${this.formatTime(start)} - ${this.formatTime(end)} (${hirePeriodText})`;
      }
      return "Available";
    },
    formatTime(date) {
      return `${date.getMonth() + 1}/${date.getDate()} ${String(date.getHours()).padStart(2, "0")}:${String(date.getMinutes()).padStart(2, "0")}`;
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
        this.bookingForm.startDateTime = `${this.bookingForm.startDate} ${this.bookingForm.startTime}`;
      }
    },
    // 提交预订
    async submitBooking() {
      var _a, _b;
      this.bookingForm.hireType = this.durationOptions[this.selectedDuration].type.toUpperCase();
      const token = String(common_vendor.index.getStorageSync("token"));
      if (!this.bookingForm.startTime) {
        this.showMessage("请选择开始时间", "error");
        return;
      }
      if (!this.bookingForm.hireType) {
        this.showMessage("请选择租赁类型", "error");
        return;
      }
      this.isLoading = true;
      common_vendor.index.showLoading({ title: "Under submission..." });
      try {
        const startTime = this.bookingForm.startDateTime.replace("T", " ") + ":00";
        common_vendor.index.__f__("log", "at pages/cardetail/cardetail.vue:318", "startDateTime:", startTime);
        common_vendor.index.__f__("log", "at pages/cardetail/cardetail.vue:319", "User ID:", (_a = this.currentUser) == null ? void 0 : _a.userId);
        common_vendor.index.__f__("log", "at pages/cardetail/cardetail.vue:320", "Scooter ID:", (_b = this.scooter) == null ? void 0 : _b.id);
        common_vendor.index.__f__("log", "at pages/cardetail/cardetail.vue:321", "Token:", token);
        const payloadBase64 = token.split(".")[1];
        const payload = JSON.parse(atob(payloadBase64));
        common_vendor.index.__f__("log", "at pages/cardetail/cardetail.vue:324", "Token Payload:", payload);
        const bookingData = {
          userId: this.currentUser.userId,
          scooterId: this.scooter.id,
          hireType: this.bookingForm.hireType,
          startTime
        };
        common_vendor.index.__f__("log", "at pages/cardetail/cardetail.vue:332", "Booking Data:", bookingData);
        const res = await new Promise((resolve, reject) => {
          common_vendor.index.request({
            url: "http://localhost:8080/api/bookings",
            method: "POST",
            data: bookingData,
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            },
            success: (response) => resolve(response),
            fail: (error) => reject(error)
          });
        });
        common_vendor.index.__f__("log", "at pages/cardetail/cardetail.vue:348", "API Response:", res);
        if (res.statusCode === 200) {
          this.showMessage("book successfully", "success");
          common_vendor.index.showToast({
            title: "Book successful!",
            icon: "success",
            duration: 2e3
            // 2秒后自动关闭
          });
          await this.refreshTimeline();
        } else {
          common_vendor.index.__f__("log", "at pages/cardetail/cardetail.vue:359", "Error message:", res.data.message);
          this.showMessage(res.data.message || "预订失败", "error");
        }
      } catch (error) {
        this.showMessage("请求失败，请重试", "error");
      } finally {
        this.isLoading = false;
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
  }
  // openMap() {
  //   uni.openLocation({
  //     latitude: this.scooter.latitude,
  //     longitude: this.scooter.longitude,
  //     name: this.scooter.location
  //   });
  // }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $data.scooter
  }, $data.scooter ? {
    b: common_vendor.t($data.scooter.id),
    c: common_vendor.t($options.statusText),
    d: common_vendor.n($options.statusClass)
  } : {}, {
    e: common_vendor.t($options.calculatedPrice),
    f: $data.durationOptions.length
  }, $data.durationOptions.length ? {
    g: common_vendor.t($data.durationOptions[$data.selectedDuration].label)
  } : {}, {
    h: $data.scooter
  }, $data.scooter ? {
    i: common_assets._imports_0,
    j: common_vendor.t($data.scooter.location || "Getting location information"),
    k: common_assets._imports_1
  } : {}, {
    l: common_vendor.f(7, (day, k0, i0) => {
      return {
        a: common_vendor.t($options.formatDate($options.addDays(/* @__PURE__ */ new Date(), day - 1))),
        b: day
      };
    }),
    m: common_vendor.f($data.timeline, (slot, index, i0) => {
      return {
        a: common_vendor.t($options.formatTimeSlot(slot)),
        b: index,
        c: common_vendor.n(slot.status),
        d: $options.calculateSlotWidth(slot) + "%"
      };
    }),
    n: common_vendor.f($data.durationOptions, (option, index, i0) => {
      return {
        a: common_vendor.t(option.label),
        b: common_vendor.t(option.subLabel),
        c: index,
        d: $data.selectedDuration === index ? 1 : "",
        e: common_vendor.o(($event) => $options.selectDuration(index), index)
      };
    }),
    o: common_vendor.t($data.bookingForm.startDate || "Please select a start date"),
    p: $data.bookingForm.startDate,
    q: common_vendor.o((...args) => $options.onDateChange && $options.onDateChange(...args)),
    r: common_vendor.t($data.bookingForm.startTime || "Please select a start time"),
    s: $data.bookingForm.startTime,
    t: common_vendor.o((...args) => $options.onTimeChange && $options.onTimeChange(...args)),
    v: common_vendor.t($data.isLoading ? "In process..." : "Confirming a Reservation"),
    w: common_vendor.o((...args) => $options.submitBooking && $options.submitBooking(...args)),
    x: $data.isLoading,
    y: $data.message
  }, $data.message ? {
    z: common_vendor.t($data.message),
    A: common_vendor.n($data.messageType === "success" ? "success-msg" : "error-msg")
  } : {});
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-a91970b9"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/cardetail/cardetail.js.map
