if (typeof Promise !== "undefined" && !Promise.prototype.finally) {
  Promise.prototype.finally = function(callback) {
    const promise = this.constructor;
    return this.then(
      (value) => promise.resolve(callback()).then(() => value),
      (reason) => promise.resolve(callback()).then(() => {
        throw reason;
      })
    );
  };
}
;
if (typeof uni !== "undefined" && uni && uni.requireGlobal) {
  const global2 = uni.requireGlobal();
  ArrayBuffer = global2.ArrayBuffer;
  Int8Array = global2.Int8Array;
  Uint8Array = global2.Uint8Array;
  Uint8ClampedArray = global2.Uint8ClampedArray;
  Int16Array = global2.Int16Array;
  Uint16Array = global2.Uint16Array;
  Int32Array = global2.Int32Array;
  Uint32Array = global2.Uint32Array;
  Float32Array = global2.Float32Array;
  Float64Array = global2.Float64Array;
  BigInt64Array = global2.BigInt64Array;
  BigUint64Array = global2.BigUint64Array;
}
;
if (uni.restoreGlobal) {
  uni.restoreGlobal(Vue, weex, plus, setTimeout, clearTimeout, setInterval, clearInterval);
}
(function(vue) {
  "use strict";
  function formatAppLog(type, filename, ...args) {
    if (uni.__log__) {
      uni.__log__(type, filename, ...args);
    } else {
      console[type].apply(console, [...args, filename]);
    }
  }
  function resolveEasycom(component, easycom) {
    return typeof component === "string" ? easycom : component;
  }
  const _imports_0$7 = "/static/images/logo.png";
  const _export_sfc = (sfc, props) => {
    const target = sfc.__vccOpts || sfc;
    for (const [key, val] of props) {
      target[key] = val;
    }
    return target;
  };
  const _sfc_main$D = {
    created() {
      setTimeout(() => {
        const token = uni.getStorageSync("token");
        if (!token) {
          formatAppLog("log", "at pages/JudeLoginPage/JudeLoginPage.vue:15", "The Token was not found. Redirect to the login page...");
          uni.redirectTo({
            url: "/pages/UserLogin/UserLogin"
          });
          return;
        }
        uni.request({
          url: `${this.$baseURL}/api/auth/validate`,
          method: "GET",
          header: {
            "Authorization": "Bearer " + token
          },
          success: (res) => {
            if (res.statusCode === 200 && res.data.valid) {
              formatAppLog("log", "at pages/JudeLoginPage/JudeLoginPage.vue:30", "Token verification passed, user ID:", res.data.userId);
              uni.setStorageSync("userInfo", res.data);
              uni.redirectTo({
                url: "/pages/index"
              });
            } else {
              uni.removeStorageSync("token");
              uni.redirectTo({
                url: "/pages/UserLogin/UserLogin"
              });
            }
          },
          fail: (err) => {
            formatAppLog("error", "at pages/JudeLoginPage/JudeLoginPage.vue:43", "Token verification request failed:", err);
            uni.showToast({
              title: "Network Error",
              icon: "none"
            });
            uni.redirectTo({
              url: "/pages/UserLogin/UserLogin"
            });
          }
        });
      }, 2e3);
    }
  };
  function _sfc_render$C(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "container" }, [
      vue.createElementVNode("image", {
        class: "logo",
        src: _imports_0$7,
        mode: "widthFix"
      }),
      vue.createElementVNode("text", { class: "title" }, "ScootGo"),
      vue.createElementVNode("text", { class: "loading-text" }, "Loading...")
    ]);
  }
  const PagesJudeLoginPageJudeLoginPage = /* @__PURE__ */ _export_sfc(_sfc_main$D, [["render", _sfc_render$C], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/JudeLoginPage/JudeLoginPage.vue"]]);
  const fontData = [
    {
      "font_class": "arrow-down",
      "unicode": ""
    },
    {
      "font_class": "arrow-left",
      "unicode": ""
    },
    {
      "font_class": "arrow-right",
      "unicode": ""
    },
    {
      "font_class": "arrow-up",
      "unicode": ""
    },
    {
      "font_class": "auth",
      "unicode": ""
    },
    {
      "font_class": "auth-filled",
      "unicode": ""
    },
    {
      "font_class": "back",
      "unicode": ""
    },
    {
      "font_class": "bars",
      "unicode": ""
    },
    {
      "font_class": "calendar",
      "unicode": ""
    },
    {
      "font_class": "calendar-filled",
      "unicode": ""
    },
    {
      "font_class": "camera",
      "unicode": ""
    },
    {
      "font_class": "camera-filled",
      "unicode": ""
    },
    {
      "font_class": "cart",
      "unicode": ""
    },
    {
      "font_class": "cart-filled",
      "unicode": ""
    },
    {
      "font_class": "chat",
      "unicode": ""
    },
    {
      "font_class": "chat-filled",
      "unicode": ""
    },
    {
      "font_class": "chatboxes",
      "unicode": ""
    },
    {
      "font_class": "chatboxes-filled",
      "unicode": ""
    },
    {
      "font_class": "chatbubble",
      "unicode": ""
    },
    {
      "font_class": "chatbubble-filled",
      "unicode": ""
    },
    {
      "font_class": "checkbox",
      "unicode": ""
    },
    {
      "font_class": "checkbox-filled",
      "unicode": ""
    },
    {
      "font_class": "checkmarkempty",
      "unicode": ""
    },
    {
      "font_class": "circle",
      "unicode": ""
    },
    {
      "font_class": "circle-filled",
      "unicode": ""
    },
    {
      "font_class": "clear",
      "unicode": ""
    },
    {
      "font_class": "close",
      "unicode": ""
    },
    {
      "font_class": "closeempty",
      "unicode": ""
    },
    {
      "font_class": "cloud-download",
      "unicode": ""
    },
    {
      "font_class": "cloud-download-filled",
      "unicode": ""
    },
    {
      "font_class": "cloud-upload",
      "unicode": ""
    },
    {
      "font_class": "cloud-upload-filled",
      "unicode": ""
    },
    {
      "font_class": "color",
      "unicode": ""
    },
    {
      "font_class": "color-filled",
      "unicode": ""
    },
    {
      "font_class": "compose",
      "unicode": ""
    },
    {
      "font_class": "contact",
      "unicode": ""
    },
    {
      "font_class": "contact-filled",
      "unicode": ""
    },
    {
      "font_class": "down",
      "unicode": ""
    },
    {
      "font_class": "bottom",
      "unicode": ""
    },
    {
      "font_class": "download",
      "unicode": ""
    },
    {
      "font_class": "download-filled",
      "unicode": ""
    },
    {
      "font_class": "email",
      "unicode": ""
    },
    {
      "font_class": "email-filled",
      "unicode": ""
    },
    {
      "font_class": "eye",
      "unicode": ""
    },
    {
      "font_class": "eye-filled",
      "unicode": ""
    },
    {
      "font_class": "eye-slash",
      "unicode": ""
    },
    {
      "font_class": "eye-slash-filled",
      "unicode": ""
    },
    {
      "font_class": "fire",
      "unicode": ""
    },
    {
      "font_class": "fire-filled",
      "unicode": ""
    },
    {
      "font_class": "flag",
      "unicode": ""
    },
    {
      "font_class": "flag-filled",
      "unicode": ""
    },
    {
      "font_class": "folder-add",
      "unicode": ""
    },
    {
      "font_class": "folder-add-filled",
      "unicode": ""
    },
    {
      "font_class": "font",
      "unicode": ""
    },
    {
      "font_class": "forward",
      "unicode": ""
    },
    {
      "font_class": "gear",
      "unicode": ""
    },
    {
      "font_class": "gear-filled",
      "unicode": ""
    },
    {
      "font_class": "gift",
      "unicode": ""
    },
    {
      "font_class": "gift-filled",
      "unicode": ""
    },
    {
      "font_class": "hand-down",
      "unicode": ""
    },
    {
      "font_class": "hand-down-filled",
      "unicode": ""
    },
    {
      "font_class": "hand-up",
      "unicode": ""
    },
    {
      "font_class": "hand-up-filled",
      "unicode": ""
    },
    {
      "font_class": "headphones",
      "unicode": ""
    },
    {
      "font_class": "heart",
      "unicode": ""
    },
    {
      "font_class": "heart-filled",
      "unicode": ""
    },
    {
      "font_class": "help",
      "unicode": ""
    },
    {
      "font_class": "help-filled",
      "unicode": ""
    },
    {
      "font_class": "home",
      "unicode": ""
    },
    {
      "font_class": "home-filled",
      "unicode": ""
    },
    {
      "font_class": "image",
      "unicode": ""
    },
    {
      "font_class": "image-filled",
      "unicode": ""
    },
    {
      "font_class": "images",
      "unicode": ""
    },
    {
      "font_class": "images-filled",
      "unicode": ""
    },
    {
      "font_class": "info",
      "unicode": ""
    },
    {
      "font_class": "info-filled",
      "unicode": ""
    },
    {
      "font_class": "left",
      "unicode": ""
    },
    {
      "font_class": "link",
      "unicode": ""
    },
    {
      "font_class": "list",
      "unicode": ""
    },
    {
      "font_class": "location",
      "unicode": ""
    },
    {
      "font_class": "location-filled",
      "unicode": ""
    },
    {
      "font_class": "locked",
      "unicode": ""
    },
    {
      "font_class": "locked-filled",
      "unicode": ""
    },
    {
      "font_class": "loop",
      "unicode": ""
    },
    {
      "font_class": "mail-open",
      "unicode": ""
    },
    {
      "font_class": "mail-open-filled",
      "unicode": ""
    },
    {
      "font_class": "map",
      "unicode": ""
    },
    {
      "font_class": "map-filled",
      "unicode": ""
    },
    {
      "font_class": "map-pin",
      "unicode": ""
    },
    {
      "font_class": "map-pin-ellipse",
      "unicode": ""
    },
    {
      "font_class": "medal",
      "unicode": ""
    },
    {
      "font_class": "medal-filled",
      "unicode": ""
    },
    {
      "font_class": "mic",
      "unicode": ""
    },
    {
      "font_class": "mic-filled",
      "unicode": ""
    },
    {
      "font_class": "micoff",
      "unicode": ""
    },
    {
      "font_class": "micoff-filled",
      "unicode": ""
    },
    {
      "font_class": "minus",
      "unicode": ""
    },
    {
      "font_class": "minus-filled",
      "unicode": ""
    },
    {
      "font_class": "more",
      "unicode": ""
    },
    {
      "font_class": "more-filled",
      "unicode": ""
    },
    {
      "font_class": "navigate",
      "unicode": ""
    },
    {
      "font_class": "navigate-filled",
      "unicode": ""
    },
    {
      "font_class": "notification",
      "unicode": ""
    },
    {
      "font_class": "notification-filled",
      "unicode": ""
    },
    {
      "font_class": "paperclip",
      "unicode": ""
    },
    {
      "font_class": "paperplane",
      "unicode": ""
    },
    {
      "font_class": "paperplane-filled",
      "unicode": ""
    },
    {
      "font_class": "person",
      "unicode": ""
    },
    {
      "font_class": "person-filled",
      "unicode": ""
    },
    {
      "font_class": "personadd",
      "unicode": ""
    },
    {
      "font_class": "personadd-filled",
      "unicode": ""
    },
    {
      "font_class": "personadd-filled-copy",
      "unicode": ""
    },
    {
      "font_class": "phone",
      "unicode": ""
    },
    {
      "font_class": "phone-filled",
      "unicode": ""
    },
    {
      "font_class": "plus",
      "unicode": ""
    },
    {
      "font_class": "plus-filled",
      "unicode": ""
    },
    {
      "font_class": "plusempty",
      "unicode": ""
    },
    {
      "font_class": "pulldown",
      "unicode": ""
    },
    {
      "font_class": "pyq",
      "unicode": ""
    },
    {
      "font_class": "qq",
      "unicode": ""
    },
    {
      "font_class": "redo",
      "unicode": ""
    },
    {
      "font_class": "redo-filled",
      "unicode": ""
    },
    {
      "font_class": "refresh",
      "unicode": ""
    },
    {
      "font_class": "refresh-filled",
      "unicode": ""
    },
    {
      "font_class": "refreshempty",
      "unicode": ""
    },
    {
      "font_class": "reload",
      "unicode": ""
    },
    {
      "font_class": "right",
      "unicode": ""
    },
    {
      "font_class": "scan",
      "unicode": ""
    },
    {
      "font_class": "search",
      "unicode": ""
    },
    {
      "font_class": "settings",
      "unicode": ""
    },
    {
      "font_class": "settings-filled",
      "unicode": ""
    },
    {
      "font_class": "shop",
      "unicode": ""
    },
    {
      "font_class": "shop-filled",
      "unicode": ""
    },
    {
      "font_class": "smallcircle",
      "unicode": ""
    },
    {
      "font_class": "smallcircle-filled",
      "unicode": ""
    },
    {
      "font_class": "sound",
      "unicode": ""
    },
    {
      "font_class": "sound-filled",
      "unicode": ""
    },
    {
      "font_class": "spinner-cycle",
      "unicode": ""
    },
    {
      "font_class": "staff",
      "unicode": ""
    },
    {
      "font_class": "staff-filled",
      "unicode": ""
    },
    {
      "font_class": "star",
      "unicode": ""
    },
    {
      "font_class": "star-filled",
      "unicode": ""
    },
    {
      "font_class": "starhalf",
      "unicode": ""
    },
    {
      "font_class": "trash",
      "unicode": ""
    },
    {
      "font_class": "trash-filled",
      "unicode": ""
    },
    {
      "font_class": "tune",
      "unicode": ""
    },
    {
      "font_class": "tune-filled",
      "unicode": ""
    },
    {
      "font_class": "undo",
      "unicode": ""
    },
    {
      "font_class": "undo-filled",
      "unicode": ""
    },
    {
      "font_class": "up",
      "unicode": ""
    },
    {
      "font_class": "top",
      "unicode": ""
    },
    {
      "font_class": "upload",
      "unicode": ""
    },
    {
      "font_class": "upload-filled",
      "unicode": ""
    },
    {
      "font_class": "videocam",
      "unicode": ""
    },
    {
      "font_class": "videocam-filled",
      "unicode": ""
    },
    {
      "font_class": "vip",
      "unicode": ""
    },
    {
      "font_class": "vip-filled",
      "unicode": ""
    },
    {
      "font_class": "wallet",
      "unicode": ""
    },
    {
      "font_class": "wallet-filled",
      "unicode": ""
    },
    {
      "font_class": "weibo",
      "unicode": ""
    },
    {
      "font_class": "weixin",
      "unicode": ""
    }
  ];
  const getVal = (val) => {
    const reg = /^[0-9]*$/g;
    return typeof val === "number" || reg.test(val) ? val + "px" : val;
  };
  const _sfc_main$C = {
    name: "UniIcons",
    emits: ["click"],
    props: {
      type: {
        type: String,
        default: ""
      },
      color: {
        type: String,
        default: "#333333"
      },
      size: {
        type: [Number, String],
        default: 16
      },
      customPrefix: {
        type: String,
        default: ""
      },
      fontFamily: {
        type: String,
        default: ""
      }
    },
    data() {
      return {
        icons: fontData
      };
    },
    computed: {
      unicode() {
        let code = this.icons.find((v) => v.font_class === this.type);
        if (code) {
          return code.unicode;
        }
        return "";
      },
      iconSize() {
        return getVal(this.size);
      },
      styleObj() {
        if (this.fontFamily !== "") {
          return `color: ${this.color}; font-size: ${this.iconSize}; font-family: ${this.fontFamily};`;
        }
        return `color: ${this.color}; font-size: ${this.iconSize};`;
      }
    },
    methods: {
      _onClick() {
        this.$emit("click");
      }
    }
  };
  function _sfc_render$B(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock(
      "text",
      {
        style: vue.normalizeStyle($options.styleObj),
        class: vue.normalizeClass(["uni-icons", ["uniui-" + $props.type, $props.customPrefix, $props.customPrefix ? $props.type : ""]]),
        onClick: _cache[0] || (_cache[0] = (...args) => $options._onClick && $options._onClick(...args))
      },
      [
        vue.renderSlot(_ctx.$slots, "default", {}, void 0, true)
      ],
      6
      /* CLASS, STYLE */
    );
  }
  const __easycom_0$2 = /* @__PURE__ */ _export_sfc(_sfc_main$C, [["render", _sfc_render$B], ["__scopeId", "data-v-d31e1c47"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/uni_modules/uni-icons/components/uni-icons/uni-icons.vue"]]);
  let Calendar$1 = class Calendar {
    constructor({
      selected,
      startDate,
      endDate,
      range
    } = {}) {
      this.date = this.getDateObj(/* @__PURE__ */ new Date());
      this.selected = selected || [];
      this.startDate = startDate;
      this.endDate = endDate;
      this.range = range;
      this.cleanMultipleStatus();
      this.weeks = {};
      this.lastHover = false;
    }
    /**
     * 设置日期
     * @param {Object} date
     */
    setDate(date) {
      const selectDate = this.getDateObj(date);
      this.getWeeks(selectDate.fullDate);
    }
    /**
     * 清理多选状态
     */
    cleanMultipleStatus() {
      this.multipleStatus = {
        before: "",
        after: "",
        data: []
      };
    }
    setStartDate(startDate) {
      this.startDate = startDate;
    }
    setEndDate(endDate) {
      this.endDate = endDate;
    }
    getPreMonthObj(date) {
      date = fixIosDateFormat(date);
      date = new Date(date);
      const oldMonth = date.getMonth();
      date.setMonth(oldMonth - 1);
      const newMonth = date.getMonth();
      if (oldMonth !== 0 && newMonth - oldMonth === 0) {
        date.setMonth(newMonth - 1);
      }
      return this.getDateObj(date);
    }
    getNextMonthObj(date) {
      date = fixIosDateFormat(date);
      date = new Date(date);
      const oldMonth = date.getMonth();
      date.setMonth(oldMonth + 1);
      const newMonth = date.getMonth();
      if (newMonth - oldMonth > 1) {
        date.setMonth(newMonth - 1);
      }
      return this.getDateObj(date);
    }
    /**
     * 获取指定格式Date对象
     */
    getDateObj(date) {
      date = fixIosDateFormat(date);
      date = new Date(date);
      return {
        fullDate: getDate(date),
        year: date.getFullYear(),
        month: addZero(date.getMonth() + 1),
        date: addZero(date.getDate()),
        day: date.getDay()
      };
    }
    /**
     * 获取上一个月日期集合
     */
    getPreMonthDays(amount, dateObj) {
      const result = [];
      for (let i = amount - 1; i >= 0; i--) {
        const month = dateObj.month - 1;
        result.push({
          date: new Date(dateObj.year, month, -i).getDate(),
          month,
          disable: true
        });
      }
      return result;
    }
    /**
     * 获取本月日期集合
     */
    getCurrentMonthDays(amount, dateObj) {
      const result = [];
      const fullDate = this.date.fullDate;
      for (let i = 1; i <= amount; i++) {
        const currentDate = `${dateObj.year}-${dateObj.month}-${addZero(i)}`;
        const isToday = fullDate === currentDate;
        const info = this.selected && this.selected.find((item) => {
          if (this.dateEqual(currentDate, item.date)) {
            return item;
          }
        });
        if (this.startDate) {
          dateCompare(this.startDate, currentDate);
        }
        if (this.endDate) {
          dateCompare(currentDate, this.endDate);
        }
        let multiples = this.multipleStatus.data;
        let multiplesStatus = -1;
        if (this.range && multiples) {
          multiplesStatus = multiples.findIndex((item) => {
            return this.dateEqual(item, currentDate);
          });
        }
        const checked = multiplesStatus !== -1;
        result.push({
          fullDate: currentDate,
          year: dateObj.year,
          date: i,
          multiple: this.range ? checked : false,
          beforeMultiple: this.isLogicBefore(currentDate, this.multipleStatus.before, this.multipleStatus.after),
          afterMultiple: this.isLogicAfter(currentDate, this.multipleStatus.before, this.multipleStatus.after),
          month: dateObj.month,
          disable: this.startDate && !dateCompare(this.startDate, currentDate) || this.endDate && !dateCompare(
            currentDate,
            this.endDate
          ),
          isToday,
          userChecked: false,
          extraInfo: info
        });
      }
      return result;
    }
    /**
     * 获取下一个月日期集合
     */
    _getNextMonthDays(amount, dateObj) {
      const result = [];
      const month = dateObj.month + 1;
      for (let i = 1; i <= amount; i++) {
        result.push({
          date: i,
          month,
          disable: true
        });
      }
      return result;
    }
    /**
     * 获取当前日期详情
     * @param {Object} date
     */
    getInfo(date) {
      if (!date) {
        date = /* @__PURE__ */ new Date();
      }
      const res = this.calendar.find((item) => item.fullDate === this.getDateObj(date).fullDate);
      return res ? res : this.getDateObj(date);
    }
    /**
     * 比较时间是否相等
     */
    dateEqual(before, after) {
      before = new Date(fixIosDateFormat(before));
      after = new Date(fixIosDateFormat(after));
      return before.valueOf() === after.valueOf();
    }
    /**
     *  比较真实起始日期
     */
    isLogicBefore(currentDate, before, after) {
      let logicBefore = before;
      if (before && after) {
        logicBefore = dateCompare(before, after) ? before : after;
      }
      return this.dateEqual(logicBefore, currentDate);
    }
    isLogicAfter(currentDate, before, after) {
      let logicAfter = after;
      if (before && after) {
        logicAfter = dateCompare(before, after) ? after : before;
      }
      return this.dateEqual(logicAfter, currentDate);
    }
    /**
     * 获取日期范围内所有日期
     * @param {Object} begin
     * @param {Object} end
     */
    geDateAll(begin, end) {
      var arr = [];
      var ab = begin.split("-");
      var ae = end.split("-");
      var db = /* @__PURE__ */ new Date();
      db.setFullYear(ab[0], ab[1] - 1, ab[2]);
      var de = /* @__PURE__ */ new Date();
      de.setFullYear(ae[0], ae[1] - 1, ae[2]);
      var unixDb = db.getTime() - 24 * 60 * 60 * 1e3;
      var unixDe = de.getTime() - 24 * 60 * 60 * 1e3;
      for (var k = unixDb; k <= unixDe; ) {
        k = k + 24 * 60 * 60 * 1e3;
        arr.push(this.getDateObj(new Date(parseInt(k))).fullDate);
      }
      return arr;
    }
    /**
     *  获取多选状态
     */
    setMultiple(fullDate) {
      if (!this.range)
        return;
      let {
        before,
        after
      } = this.multipleStatus;
      if (before && after) {
        if (!this.lastHover) {
          this.lastHover = true;
          return;
        }
        this.multipleStatus.before = fullDate;
        this.multipleStatus.after = "";
        this.multipleStatus.data = [];
        this.multipleStatus.fulldate = "";
        this.lastHover = false;
      } else {
        if (!before) {
          this.multipleStatus.before = fullDate;
          this.multipleStatus.after = void 0;
          this.lastHover = false;
        } else {
          this.multipleStatus.after = fullDate;
          if (dateCompare(this.multipleStatus.before, this.multipleStatus.after)) {
            this.multipleStatus.data = this.geDateAll(this.multipleStatus.before, this.multipleStatus.after);
          } else {
            this.multipleStatus.data = this.geDateAll(this.multipleStatus.after, this.multipleStatus.before);
          }
          this.lastHover = true;
        }
      }
      this.getWeeks(fullDate);
    }
    /**
     *  鼠标 hover 更新多选状态
     */
    setHoverMultiple(fullDate) {
      if (!this.range || this.lastHover)
        return;
      const {
        before
      } = this.multipleStatus;
      if (!before) {
        this.multipleStatus.before = fullDate;
      } else {
        this.multipleStatus.after = fullDate;
        if (dateCompare(this.multipleStatus.before, this.multipleStatus.after)) {
          this.multipleStatus.data = this.geDateAll(this.multipleStatus.before, this.multipleStatus.after);
        } else {
          this.multipleStatus.data = this.geDateAll(this.multipleStatus.after, this.multipleStatus.before);
        }
      }
      this.getWeeks(fullDate);
    }
    /**
     * 更新默认值多选状态
     */
    setDefaultMultiple(before, after) {
      this.multipleStatus.before = before;
      this.multipleStatus.after = after;
      if (before && after) {
        if (dateCompare(before, after)) {
          this.multipleStatus.data = this.geDateAll(before, after);
          this.getWeeks(after);
        } else {
          this.multipleStatus.data = this.geDateAll(after, before);
          this.getWeeks(before);
        }
      }
    }
    /**
     * 获取每周数据
     * @param {Object} dateData
     */
    getWeeks(dateData) {
      const {
        year,
        month
      } = this.getDateObj(dateData);
      const preMonthDayAmount = new Date(year, month - 1, 1).getDay();
      const preMonthDays = this.getPreMonthDays(preMonthDayAmount, this.getDateObj(dateData));
      const currentMonthDayAmount = new Date(year, month, 0).getDate();
      const currentMonthDays = this.getCurrentMonthDays(currentMonthDayAmount, this.getDateObj(dateData));
      const nextMonthDayAmount = 42 - preMonthDayAmount - currentMonthDayAmount;
      const nextMonthDays = this._getNextMonthDays(nextMonthDayAmount, this.getDateObj(dateData));
      const calendarDays = [...preMonthDays, ...currentMonthDays, ...nextMonthDays];
      const weeks = new Array(6);
      for (let i = 0; i < calendarDays.length; i++) {
        const index = Math.floor(i / 7);
        if (!weeks[index]) {
          weeks[index] = new Array(7);
        }
        weeks[index][i % 7] = calendarDays[i];
      }
      this.calendar = calendarDays;
      this.weeks = weeks;
    }
  };
  function getDateTime(date, hideSecond) {
    return `${getDate(date)} ${getTime(date, hideSecond)}`;
  }
  function getDate(date) {
    date = fixIosDateFormat(date);
    date = new Date(date);
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();
    return `${year}-${addZero(month)}-${addZero(day)}`;
  }
  function getTime(date, hideSecond) {
    date = fixIosDateFormat(date);
    date = new Date(date);
    const hour = date.getHours();
    const minute = date.getMinutes();
    const second = date.getSeconds();
    return hideSecond ? `${addZero(hour)}:${addZero(minute)}` : `${addZero(hour)}:${addZero(minute)}:${addZero(second)}`;
  }
  function addZero(num) {
    if (num < 10) {
      num = `0${num}`;
    }
    return num;
  }
  function getDefaultSecond(hideSecond) {
    return hideSecond ? "00:00" : "00:00:00";
  }
  function dateCompare(startDate, endDate) {
    startDate = new Date(fixIosDateFormat(startDate));
    endDate = new Date(fixIosDateFormat(endDate));
    return startDate <= endDate;
  }
  function checkDate(date) {
    const dateReg = /((19|20)\d{2})(-|\/)\d{1,2}(-|\/)\d{1,2}/g;
    return date.match(dateReg);
  }
  const dateTimeReg = /^\d{4}-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])( [0-5]?[0-9]:[0-5]?[0-9](:[0-5]?[0-9])?)?$/;
  function fixIosDateFormat(value) {
    if (typeof value === "string" && dateTimeReg.test(value)) {
      value = value.replace(/-/g, "/");
    }
    return value;
  }
  const _sfc_main$B = {
    props: {
      weeks: {
        type: Object,
        default() {
          return {};
        }
      },
      calendar: {
        type: Object,
        default: () => {
          return {};
        }
      },
      selected: {
        type: Array,
        default: () => {
          return [];
        }
      },
      checkHover: {
        type: Boolean,
        default: false
      }
    },
    methods: {
      choiceDate(weeks) {
        this.$emit("change", weeks);
      },
      handleMousemove(weeks) {
        this.$emit("handleMouse", weeks);
      }
    }
  };
  function _sfc_render$A(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock(
      "view",
      {
        class: vue.normalizeClass(["uni-calendar-item__weeks-box", {
          "uni-calendar-item--disable": $props.weeks.disable,
          "uni-calendar-item--before-checked-x": $props.weeks.beforeMultiple,
          "uni-calendar-item--multiple": $props.weeks.multiple,
          "uni-calendar-item--after-checked-x": $props.weeks.afterMultiple
        }]),
        onClick: _cache[0] || (_cache[0] = ($event) => $options.choiceDate($props.weeks)),
        onMouseenter: _cache[1] || (_cache[1] = ($event) => $options.handleMousemove($props.weeks))
      },
      [
        vue.createElementVNode(
          "view",
          {
            class: vue.normalizeClass(["uni-calendar-item__weeks-box-item", {
              "uni-calendar-item--checked": $props.calendar.fullDate === $props.weeks.fullDate && ($props.calendar.userChecked || !$props.checkHover),
              "uni-calendar-item--checked-range-text": $props.checkHover,
              "uni-calendar-item--before-checked": $props.weeks.beforeMultiple,
              "uni-calendar-item--multiple": $props.weeks.multiple,
              "uni-calendar-item--after-checked": $props.weeks.afterMultiple,
              "uni-calendar-item--disable": $props.weeks.disable
            }])
          },
          [
            $props.selected && $props.weeks.extraInfo ? (vue.openBlock(), vue.createElementBlock("text", {
              key: 0,
              class: "uni-calendar-item__weeks-box-circle"
            })) : vue.createCommentVNode("v-if", true),
            vue.createElementVNode(
              "text",
              { class: "uni-calendar-item__weeks-box-text uni-calendar-item__weeks-box-text-disable uni-calendar-item--checked-text" },
              vue.toDisplayString($props.weeks.date),
              1
              /* TEXT */
            )
          ],
          2
          /* CLASS */
        ),
        vue.createElementVNode(
          "view",
          {
            class: vue.normalizeClass({ "uni-calendar-item--today": $props.weeks.isToday })
          },
          null,
          2
          /* CLASS */
        )
      ],
      34
      /* CLASS, NEED_HYDRATION */
    );
  }
  const calendarItem = /* @__PURE__ */ _export_sfc(_sfc_main$B, [["render", _sfc_render$A], ["__scopeId", "data-v-3c762a01"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/uni_modules/uni-datetime-picker/components/uni-datetime-picker/calendar-item.vue"]]);
  const isObject = (val) => val !== null && typeof val === "object";
  const defaultDelimiters = ["{", "}"];
  class BaseFormatter {
    constructor() {
      this._caches = /* @__PURE__ */ Object.create(null);
    }
    interpolate(message, values, delimiters = defaultDelimiters) {
      if (!values) {
        return [message];
      }
      let tokens = this._caches[message];
      if (!tokens) {
        tokens = parse(message, delimiters);
        this._caches[message] = tokens;
      }
      return compile(tokens, values);
    }
  }
  const RE_TOKEN_LIST_VALUE = /^(?:\d)+/;
  const RE_TOKEN_NAMED_VALUE = /^(?:\w)+/;
  function parse(format, [startDelimiter, endDelimiter]) {
    const tokens = [];
    let position = 0;
    let text = "";
    while (position < format.length) {
      let char = format[position++];
      if (char === startDelimiter) {
        if (text) {
          tokens.push({ type: "text", value: text });
        }
        text = "";
        let sub = "";
        char = format[position++];
        while (char !== void 0 && char !== endDelimiter) {
          sub += char;
          char = format[position++];
        }
        const isClosed = char === endDelimiter;
        const type = RE_TOKEN_LIST_VALUE.test(sub) ? "list" : isClosed && RE_TOKEN_NAMED_VALUE.test(sub) ? "named" : "unknown";
        tokens.push({ value: sub, type });
      } else {
        text += char;
      }
    }
    text && tokens.push({ type: "text", value: text });
    return tokens;
  }
  function compile(tokens, values) {
    const compiled = [];
    let index = 0;
    const mode = Array.isArray(values) ? "list" : isObject(values) ? "named" : "unknown";
    if (mode === "unknown") {
      return compiled;
    }
    while (index < tokens.length) {
      const token = tokens[index];
      switch (token.type) {
        case "text":
          compiled.push(token.value);
          break;
        case "list":
          compiled.push(values[parseInt(token.value, 10)]);
          break;
        case "named":
          if (mode === "named") {
            compiled.push(values[token.value]);
          } else {
            {
              console.warn(`Type of token '${token.type}' and format of value '${mode}' don't match!`);
            }
          }
          break;
        case "unknown":
          {
            console.warn(`Detect 'unknown' type of token!`);
          }
          break;
      }
      index++;
    }
    return compiled;
  }
  const LOCALE_ZH_HANS = "zh-Hans";
  const LOCALE_ZH_HANT = "zh-Hant";
  const LOCALE_EN = "en";
  const LOCALE_FR = "fr";
  const LOCALE_ES = "es";
  const hasOwnProperty = Object.prototype.hasOwnProperty;
  const hasOwn = (val, key) => hasOwnProperty.call(val, key);
  const defaultFormatter = new BaseFormatter();
  function include(str, parts) {
    return !!parts.find((part) => str.indexOf(part) !== -1);
  }
  function startsWith(str, parts) {
    return parts.find((part) => str.indexOf(part) === 0);
  }
  function normalizeLocale(locale, messages) {
    if (!locale) {
      return;
    }
    locale = locale.trim().replace(/_/g, "-");
    if (messages && messages[locale]) {
      return locale;
    }
    locale = locale.toLowerCase();
    if (locale === "chinese") {
      return LOCALE_ZH_HANS;
    }
    if (locale.indexOf("zh") === 0) {
      if (locale.indexOf("-hans") > -1) {
        return LOCALE_ZH_HANS;
      }
      if (locale.indexOf("-hant") > -1) {
        return LOCALE_ZH_HANT;
      }
      if (include(locale, ["-tw", "-hk", "-mo", "-cht"])) {
        return LOCALE_ZH_HANT;
      }
      return LOCALE_ZH_HANS;
    }
    let locales = [LOCALE_EN, LOCALE_FR, LOCALE_ES];
    if (messages && Object.keys(messages).length > 0) {
      locales = Object.keys(messages);
    }
    const lang = startsWith(locale, locales);
    if (lang) {
      return lang;
    }
  }
  class I18n {
    constructor({ locale, fallbackLocale, messages, watcher, formater: formater2 }) {
      this.locale = LOCALE_EN;
      this.fallbackLocale = LOCALE_EN;
      this.message = {};
      this.messages = {};
      this.watchers = [];
      if (fallbackLocale) {
        this.fallbackLocale = fallbackLocale;
      }
      this.formater = formater2 || defaultFormatter;
      this.messages = messages || {};
      this.setLocale(locale || LOCALE_EN);
      if (watcher) {
        this.watchLocale(watcher);
      }
    }
    setLocale(locale) {
      const oldLocale = this.locale;
      this.locale = normalizeLocale(locale, this.messages) || this.fallbackLocale;
      if (!this.messages[this.locale]) {
        this.messages[this.locale] = {};
      }
      this.message = this.messages[this.locale];
      if (oldLocale !== this.locale) {
        this.watchers.forEach((watcher) => {
          watcher(this.locale, oldLocale);
        });
      }
    }
    getLocale() {
      return this.locale;
    }
    watchLocale(fn) {
      const index = this.watchers.push(fn) - 1;
      return () => {
        this.watchers.splice(index, 1);
      };
    }
    add(locale, message, override = true) {
      const curMessages = this.messages[locale];
      if (curMessages) {
        if (override) {
          Object.assign(curMessages, message);
        } else {
          Object.keys(message).forEach((key) => {
            if (!hasOwn(curMessages, key)) {
              curMessages[key] = message[key];
            }
          });
        }
      } else {
        this.messages[locale] = message;
      }
    }
    f(message, values, delimiters) {
      return this.formater.interpolate(message, values, delimiters).join("");
    }
    t(key, locale, values) {
      let message = this.message;
      if (typeof locale === "string") {
        locale = normalizeLocale(locale, this.messages);
        locale && (message = this.messages[locale]);
      } else {
        values = locale;
      }
      if (!hasOwn(message, key)) {
        console.warn(`Cannot translate the value of keypath ${key}. Use the value of keypath as default.`);
        return key;
      }
      return this.formater.interpolate(message[key], values).join("");
    }
  }
  function watchAppLocale(appVm, i18n) {
    if (appVm.$watchLocale) {
      appVm.$watchLocale((newLocale) => {
        i18n.setLocale(newLocale);
      });
    } else {
      appVm.$watch(() => appVm.$locale, (newLocale) => {
        i18n.setLocale(newLocale);
      });
    }
  }
  function getDefaultLocale() {
    if (typeof uni !== "undefined" && uni.getLocale) {
      return uni.getLocale();
    }
    if (typeof global !== "undefined" && global.getLocale) {
      return global.getLocale();
    }
    return LOCALE_EN;
  }
  function initVueI18n(locale, messages = {}, fallbackLocale, watcher) {
    if (typeof locale !== "string") {
      const options = [
        messages,
        locale
      ];
      locale = options[0];
      messages = options[1];
    }
    if (typeof locale !== "string") {
      locale = getDefaultLocale();
    }
    if (typeof fallbackLocale !== "string") {
      fallbackLocale = typeof __uniConfig !== "undefined" && __uniConfig.fallbackLocale || LOCALE_EN;
    }
    const i18n = new I18n({
      locale,
      fallbackLocale,
      messages,
      watcher
    });
    let t2 = (key, values) => {
      if (typeof getApp !== "function") {
        t2 = function(key2, values2) {
          return i18n.t(key2, values2);
        };
      } else {
        let isWatchedAppLocale = false;
        t2 = function(key2, values2) {
          const appVm = getApp().$vm;
          if (appVm) {
            appVm.$locale;
            if (!isWatchedAppLocale) {
              isWatchedAppLocale = true;
              watchAppLocale(appVm, i18n);
            }
          }
          return i18n.t(key2, values2);
        };
      }
      return t2(key, values);
    };
    return {
      i18n,
      f(message, values, delimiters) {
        return i18n.f(message, values, delimiters);
      },
      t(key, values) {
        return t2(key, values);
      },
      add(locale2, message, override = true) {
        return i18n.add(locale2, message, override);
      },
      watch(fn) {
        return i18n.watchLocale(fn);
      },
      getLocale() {
        return i18n.getLocale();
      },
      setLocale(newLocale) {
        return i18n.setLocale(newLocale);
      }
    };
  }
  const en = {
    "uni-datetime-picker.selectDate": "select date",
    "uni-datetime-picker.selectTime": "select time",
    "uni-datetime-picker.selectDateTime": "select date and time",
    "uni-datetime-picker.startDate": "start date",
    "uni-datetime-picker.endDate": "end date",
    "uni-datetime-picker.startTime": "start time",
    "uni-datetime-picker.endTime": "end time",
    "uni-datetime-picker.ok": "ok",
    "uni-datetime-picker.clear": "clear",
    "uni-datetime-picker.cancel": "cancel",
    "uni-datetime-picker.year": "-",
    "uni-datetime-picker.month": "",
    "uni-calender.MON": "MON",
    "uni-calender.TUE": "TUE",
    "uni-calender.WED": "WED",
    "uni-calender.THU": "THU",
    "uni-calender.FRI": "FRI",
    "uni-calender.SAT": "SAT",
    "uni-calender.SUN": "SUN",
    "uni-calender.confirm": "confirm"
  };
  const zhHans = {
    "uni-datetime-picker.selectDate": "选择日期",
    "uni-datetime-picker.selectTime": "选择时间",
    "uni-datetime-picker.selectDateTime": "选择日期时间",
    "uni-datetime-picker.startDate": "开始日期",
    "uni-datetime-picker.endDate": "结束日期",
    "uni-datetime-picker.startTime": "开始时间",
    "uni-datetime-picker.endTime": "结束时间",
    "uni-datetime-picker.ok": "确定",
    "uni-datetime-picker.clear": "清除",
    "uni-datetime-picker.cancel": "取消",
    "uni-datetime-picker.year": "年",
    "uni-datetime-picker.month": "月",
    "uni-calender.SUN": "日",
    "uni-calender.MON": "一",
    "uni-calender.TUE": "二",
    "uni-calender.WED": "三",
    "uni-calender.THU": "四",
    "uni-calender.FRI": "五",
    "uni-calender.SAT": "六",
    "uni-calender.confirm": "确认"
  };
  const zhHant = {
    "uni-datetime-picker.selectDate": "選擇日期",
    "uni-datetime-picker.selectTime": "選擇時間",
    "uni-datetime-picker.selectDateTime": "選擇日期時間",
    "uni-datetime-picker.startDate": "開始日期",
    "uni-datetime-picker.endDate": "結束日期",
    "uni-datetime-picker.startTime": "開始时间",
    "uni-datetime-picker.endTime": "結束时间",
    "uni-datetime-picker.ok": "確定",
    "uni-datetime-picker.clear": "清除",
    "uni-datetime-picker.cancel": "取消",
    "uni-datetime-picker.year": "年",
    "uni-datetime-picker.month": "月",
    "uni-calender.SUN": "日",
    "uni-calender.MON": "一",
    "uni-calender.TUE": "二",
    "uni-calender.WED": "三",
    "uni-calender.THU": "四",
    "uni-calender.FRI": "五",
    "uni-calender.SAT": "六",
    "uni-calender.confirm": "確認"
  };
  const i18nMessages = {
    en,
    "zh-Hans": zhHans,
    "zh-Hant": zhHant
  };
  const {
    t: t$1
  } = initVueI18n(i18nMessages);
  const _sfc_main$A = {
    name: "UniDatetimePicker",
    data() {
      return {
        indicatorStyle: `height: 50px;`,
        visible: false,
        fixNvueBug: {},
        dateShow: true,
        timeShow: true,
        title: "日期和时间",
        // 输入框当前时间
        time: "",
        // 当前的年月日时分秒
        year: 1920,
        month: 0,
        day: 0,
        hour: 0,
        minute: 0,
        second: 0,
        // 起始时间
        startYear: 1920,
        startMonth: 1,
        startDay: 1,
        startHour: 0,
        startMinute: 0,
        startSecond: 0,
        // 结束时间
        endYear: 2120,
        endMonth: 12,
        endDay: 31,
        endHour: 23,
        endMinute: 59,
        endSecond: 59
      };
    },
    options: {
      virtualHost: true
    },
    props: {
      type: {
        type: String,
        default: "datetime"
      },
      value: {
        type: [String, Number],
        default: ""
      },
      modelValue: {
        type: [String, Number],
        default: ""
      },
      start: {
        type: [Number, String],
        default: ""
      },
      end: {
        type: [Number, String],
        default: ""
      },
      returnType: {
        type: String,
        default: "string"
      },
      disabled: {
        type: [Boolean, String],
        default: false
      },
      border: {
        type: [Boolean, String],
        default: true
      },
      hideSecond: {
        type: [Boolean, String],
        default: false
      }
    },
    watch: {
      modelValue: {
        handler(newVal) {
          if (newVal) {
            this.parseValue(fixIosDateFormat(newVal));
            this.initTime(false);
          } else {
            this.time = "";
            this.parseValue(Date.now());
          }
        },
        immediate: true
      },
      type: {
        handler(newValue) {
          if (newValue === "date") {
            this.dateShow = true;
            this.timeShow = false;
            this.title = "日期";
          } else if (newValue === "time") {
            this.dateShow = false;
            this.timeShow = true;
            this.title = "时间";
          } else {
            this.dateShow = true;
            this.timeShow = true;
            this.title = "日期和时间";
          }
        },
        immediate: true
      },
      start: {
        handler(newVal) {
          this.parseDatetimeRange(fixIosDateFormat(newVal), "start");
        },
        immediate: true
      },
      end: {
        handler(newVal) {
          this.parseDatetimeRange(fixIosDateFormat(newVal), "end");
        },
        immediate: true
      },
      // 月、日、时、分、秒可选范围变化后，检查当前值是否在范围内，不在则当前值重置为可选范围第一项
      months(newVal) {
        this.checkValue("month", this.month, newVal);
      },
      days(newVal) {
        this.checkValue("day", this.day, newVal);
      },
      hours(newVal) {
        this.checkValue("hour", this.hour, newVal);
      },
      minutes(newVal) {
        this.checkValue("minute", this.minute, newVal);
      },
      seconds(newVal) {
        this.checkValue("second", this.second, newVal);
      }
    },
    computed: {
      // 当前年、月、日、时、分、秒选择范围
      years() {
        return this.getCurrentRange("year");
      },
      months() {
        return this.getCurrentRange("month");
      },
      days() {
        return this.getCurrentRange("day");
      },
      hours() {
        return this.getCurrentRange("hour");
      },
      minutes() {
        return this.getCurrentRange("minute");
      },
      seconds() {
        return this.getCurrentRange("second");
      },
      // picker 当前值数组
      ymd() {
        return [this.year - this.minYear, this.month - this.minMonth, this.day - this.minDay];
      },
      hms() {
        return [this.hour - this.minHour, this.minute - this.minMinute, this.second - this.minSecond];
      },
      // 当前 date 是 start
      currentDateIsStart() {
        return this.year === this.startYear && this.month === this.startMonth && this.day === this.startDay;
      },
      // 当前 date 是 end
      currentDateIsEnd() {
        return this.year === this.endYear && this.month === this.endMonth && this.day === this.endDay;
      },
      // 当前年、月、日、时、分、秒的最小值和最大值
      minYear() {
        return this.startYear;
      },
      maxYear() {
        return this.endYear;
      },
      minMonth() {
        if (this.year === this.startYear) {
          return this.startMonth;
        } else {
          return 1;
        }
      },
      maxMonth() {
        if (this.year === this.endYear) {
          return this.endMonth;
        } else {
          return 12;
        }
      },
      minDay() {
        if (this.year === this.startYear && this.month === this.startMonth) {
          return this.startDay;
        } else {
          return 1;
        }
      },
      maxDay() {
        if (this.year === this.endYear && this.month === this.endMonth) {
          return this.endDay;
        } else {
          return this.daysInMonth(this.year, this.month);
        }
      },
      minHour() {
        if (this.type === "datetime") {
          if (this.currentDateIsStart) {
            return this.startHour;
          } else {
            return 0;
          }
        }
        if (this.type === "time") {
          return this.startHour;
        }
      },
      maxHour() {
        if (this.type === "datetime") {
          if (this.currentDateIsEnd) {
            return this.endHour;
          } else {
            return 23;
          }
        }
        if (this.type === "time") {
          return this.endHour;
        }
      },
      minMinute() {
        if (this.type === "datetime") {
          if (this.currentDateIsStart && this.hour === this.startHour) {
            return this.startMinute;
          } else {
            return 0;
          }
        }
        if (this.type === "time") {
          if (this.hour === this.startHour) {
            return this.startMinute;
          } else {
            return 0;
          }
        }
      },
      maxMinute() {
        if (this.type === "datetime") {
          if (this.currentDateIsEnd && this.hour === this.endHour) {
            return this.endMinute;
          } else {
            return 59;
          }
        }
        if (this.type === "time") {
          if (this.hour === this.endHour) {
            return this.endMinute;
          } else {
            return 59;
          }
        }
      },
      minSecond() {
        if (this.type === "datetime") {
          if (this.currentDateIsStart && this.hour === this.startHour && this.minute === this.startMinute) {
            return this.startSecond;
          } else {
            return 0;
          }
        }
        if (this.type === "time") {
          if (this.hour === this.startHour && this.minute === this.startMinute) {
            return this.startSecond;
          } else {
            return 0;
          }
        }
      },
      maxSecond() {
        if (this.type === "datetime") {
          if (this.currentDateIsEnd && this.hour === this.endHour && this.minute === this.endMinute) {
            return this.endSecond;
          } else {
            return 59;
          }
        }
        if (this.type === "time") {
          if (this.hour === this.endHour && this.minute === this.endMinute) {
            return this.endSecond;
          } else {
            return 59;
          }
        }
      },
      /**
       * for i18n
       */
      selectTimeText() {
        return t$1("uni-datetime-picker.selectTime");
      },
      okText() {
        return t$1("uni-datetime-picker.ok");
      },
      clearText() {
        return t$1("uni-datetime-picker.clear");
      },
      cancelText() {
        return t$1("uni-datetime-picker.cancel");
      }
    },
    mounted() {
    },
    methods: {
      /**
       * @param {Object} item
       * 小于 10 在前面加个 0
       */
      lessThanTen(item) {
        return item < 10 ? "0" + item : item;
      },
      /**
       * 解析时分秒字符串，例如：00:00:00
       * @param {String} timeString
       */
      parseTimeType(timeString) {
        if (timeString) {
          let timeArr = timeString.split(":");
          this.hour = Number(timeArr[0]);
          this.minute = Number(timeArr[1]);
          this.second = Number(timeArr[2]);
        }
      },
      /**
       * 解析选择器初始值，类型可以是字符串、时间戳，例如：2000-10-02、'08:30:00'、 1610695109000
       * @param {String | Number} datetime
       */
      initPickerValue(datetime) {
        let defaultValue = null;
        if (datetime) {
          defaultValue = this.compareValueWithStartAndEnd(datetime, this.start, this.end);
        } else {
          defaultValue = Date.now();
          defaultValue = this.compareValueWithStartAndEnd(defaultValue, this.start, this.end);
        }
        this.parseValue(defaultValue);
      },
      /**
       * 初始值规则：
       * - 用户设置初始值 value
       * 	- 设置了起始时间 start、终止时间 end，并 start < value < end，初始值为 value， 否则初始值为 start
       * 	- 只设置了起始时间 start，并 start < value，初始值为 value，否则初始值为 start
       * 	- 只设置了终止时间 end，并 value < end，初始值为 value，否则初始值为 end
       * 	- 无起始终止时间，则初始值为 value
       * - 无初始值 value，则初始值为当前本地时间 Date.now()
       * @param {Object} value
       * @param {Object} dateBase
       */
      compareValueWithStartAndEnd(value, start, end) {
        let winner = null;
        value = this.superTimeStamp(value);
        start = this.superTimeStamp(start);
        end = this.superTimeStamp(end);
        if (start && end) {
          if (value < start) {
            winner = new Date(start);
          } else if (value > end) {
            winner = new Date(end);
          } else {
            winner = new Date(value);
          }
        } else if (start && !end) {
          winner = start <= value ? new Date(value) : new Date(start);
        } else if (!start && end) {
          winner = value <= end ? new Date(value) : new Date(end);
        } else {
          winner = new Date(value);
        }
        return winner;
      },
      /**
       * 转换为可比较的时间戳，接受日期、时分秒、时间戳
       * @param {Object} value
       */
      superTimeStamp(value) {
        let dateBase = "";
        if (this.type === "time" && value && typeof value === "string") {
          const now = /* @__PURE__ */ new Date();
          const year = now.getFullYear();
          const month = now.getMonth() + 1;
          const day = now.getDate();
          dateBase = year + "/" + month + "/" + day + " ";
        }
        if (Number(value)) {
          value = parseInt(value);
          dateBase = 0;
        }
        return this.createTimeStamp(dateBase + value);
      },
      /**
       * 解析默认值 value，字符串、时间戳
       * @param {Object} defaultTime
       */
      parseValue(value) {
        if (!value) {
          return;
        }
        if (this.type === "time" && typeof value === "string") {
          this.parseTimeType(value);
        } else {
          let defaultDate = null;
          defaultDate = new Date(value);
          if (this.type !== "time") {
            this.year = defaultDate.getFullYear();
            this.month = defaultDate.getMonth() + 1;
            this.day = defaultDate.getDate();
          }
          if (this.type !== "date") {
            this.hour = defaultDate.getHours();
            this.minute = defaultDate.getMinutes();
            this.second = defaultDate.getSeconds();
          }
        }
        if (this.hideSecond) {
          this.second = 0;
        }
      },
      /**
       * 解析可选择时间范围 start、end，年月日字符串、时间戳
       * @param {Object} defaultTime
       */
      parseDatetimeRange(point, pointType) {
        if (!point) {
          if (pointType === "start") {
            this.startYear = 1920;
            this.startMonth = 1;
            this.startDay = 1;
            this.startHour = 0;
            this.startMinute = 0;
            this.startSecond = 0;
          }
          if (pointType === "end") {
            this.endYear = 2120;
            this.endMonth = 12;
            this.endDay = 31;
            this.endHour = 23;
            this.endMinute = 59;
            this.endSecond = 59;
          }
          return;
        }
        if (this.type === "time") {
          const pointArr = point.split(":");
          this[pointType + "Hour"] = Number(pointArr[0]);
          this[pointType + "Minute"] = Number(pointArr[1]);
          this[pointType + "Second"] = Number(pointArr[2]);
        } else {
          if (!point) {
            pointType === "start" ? this.startYear = this.year - 60 : this.endYear = this.year + 60;
            return;
          }
          if (Number(point)) {
            point = parseInt(point);
          }
          const hasTime = /[0-9]:[0-9]/;
          if (this.type === "datetime" && pointType === "end" && typeof point === "string" && !hasTime.test(
            point
          )) {
            point = point + " 23:59:59";
          }
          const pointDate = new Date(point);
          this[pointType + "Year"] = pointDate.getFullYear();
          this[pointType + "Month"] = pointDate.getMonth() + 1;
          this[pointType + "Day"] = pointDate.getDate();
          if (this.type === "datetime") {
            this[pointType + "Hour"] = pointDate.getHours();
            this[pointType + "Minute"] = pointDate.getMinutes();
            this[pointType + "Second"] = pointDate.getSeconds();
          }
        }
      },
      // 获取 年、月、日、时、分、秒 当前可选范围
      getCurrentRange(value) {
        const range = [];
        for (let i = this["min" + this.capitalize(value)]; i <= this["max" + this.capitalize(value)]; i++) {
          range.push(i);
        }
        return range;
      },
      // 字符串首字母大写
      capitalize(str) {
        return str.charAt(0).toUpperCase() + str.slice(1);
      },
      // 检查当前值是否在范围内，不在则当前值重置为可选范围第一项
      checkValue(name, value, values) {
        if (values.indexOf(value) === -1) {
          this[name] = values[0];
        }
      },
      // 每个月的实际天数
      daysInMonth(year, month) {
        return new Date(year, month, 0).getDate();
      },
      /**
       * 生成时间戳
       * @param {Object} time
       */
      createTimeStamp(time) {
        if (!time)
          return;
        if (typeof time === "number") {
          return time;
        } else {
          time = time.replace(/-/g, "/");
          if (this.type === "date") {
            time = time + " 00:00:00";
          }
          return Date.parse(time);
        }
      },
      /**
       * 生成日期或时间的字符串
       */
      createDomSting() {
        const yymmdd = this.year + "-" + this.lessThanTen(this.month) + "-" + this.lessThanTen(this.day);
        let hhmmss = this.lessThanTen(this.hour) + ":" + this.lessThanTen(this.minute);
        if (!this.hideSecond) {
          hhmmss = hhmmss + ":" + this.lessThanTen(this.second);
        }
        if (this.type === "date") {
          return yymmdd;
        } else if (this.type === "time") {
          return hhmmss;
        } else {
          return yymmdd + " " + hhmmss;
        }
      },
      /**
       * 初始化返回值，并抛出 change 事件
       */
      initTime(emit = true) {
        this.time = this.createDomSting();
        if (!emit)
          return;
        if (this.returnType === "timestamp" && this.type !== "time") {
          this.$emit("change", this.createTimeStamp(this.time));
          this.$emit("input", this.createTimeStamp(this.time));
          this.$emit("update:modelValue", this.createTimeStamp(this.time));
        } else {
          this.$emit("change", this.time);
          this.$emit("input", this.time);
          this.$emit("update:modelValue", this.time);
        }
      },
      /**
       * 用户选择日期或时间更新 data
       * @param {Object} e
       */
      bindDateChange(e) {
        const val = e.detail.value;
        this.year = this.years[val[0]];
        this.month = this.months[val[1]];
        this.day = this.days[val[2]];
      },
      bindTimeChange(e) {
        const val = e.detail.value;
        this.hour = this.hours[val[0]];
        this.minute = this.minutes[val[1]];
        this.second = this.seconds[val[2]];
      },
      /**
       * 初始化弹出层
       */
      initTimePicker() {
        if (this.disabled)
          return;
        const value = fixIosDateFormat(this.time);
        this.initPickerValue(value);
        this.visible = !this.visible;
      },
      /**
       * 触发或关闭弹框
       */
      tiggerTimePicker(e) {
        this.visible = !this.visible;
      },
      /**
       * 用户点击“清空”按钮，清空当前值
       */
      clearTime() {
        this.time = "";
        this.$emit("change", this.time);
        this.$emit("input", this.time);
        this.$emit("update:modelValue", this.time);
        this.tiggerTimePicker();
      },
      /**
       * 用户点击“确定”按钮
       */
      setTime() {
        this.initTime();
        this.tiggerTimePicker();
      }
    }
  };
  function _sfc_render$z(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "uni-datetime-picker" }, [
      vue.createElementVNode("view", {
        onClick: _cache[0] || (_cache[0] = (...args) => $options.initTimePicker && $options.initTimePicker(...args))
      }, [
        vue.renderSlot(_ctx.$slots, "default", {}, () => [
          vue.createElementVNode(
            "view",
            {
              class: vue.normalizeClass(["uni-datetime-picker-timebox-pointer", { "uni-datetime-picker-disabled": $props.disabled, "uni-datetime-picker-timebox": $props.border }])
            },
            [
              vue.createElementVNode(
                "text",
                { class: "uni-datetime-picker-text" },
                vue.toDisplayString($data.time),
                1
                /* TEXT */
              ),
              !$data.time ? (vue.openBlock(), vue.createElementBlock("view", {
                key: 0,
                class: "uni-datetime-picker-time"
              }, [
                vue.createElementVNode(
                  "text",
                  { class: "uni-datetime-picker-text" },
                  vue.toDisplayString($options.selectTimeText),
                  1
                  /* TEXT */
                )
              ])) : vue.createCommentVNode("v-if", true)
            ],
            2
            /* CLASS */
          )
        ], true)
      ]),
      $data.visible ? (vue.openBlock(), vue.createElementBlock("view", {
        key: 0,
        id: "mask",
        class: "uni-datetime-picker-mask",
        onClick: _cache[1] || (_cache[1] = (...args) => $options.tiggerTimePicker && $options.tiggerTimePicker(...args))
      })) : vue.createCommentVNode("v-if", true),
      $data.visible ? (vue.openBlock(), vue.createElementBlock(
        "view",
        {
          key: 1,
          class: vue.normalizeClass(["uni-datetime-picker-popup", [$data.dateShow && $data.timeShow ? "" : "fix-nvue-height"]]),
          style: vue.normalizeStyle($data.fixNvueBug)
        },
        [
          vue.createElementVNode("view", { class: "uni-title" }, [
            vue.createElementVNode(
              "text",
              { class: "uni-datetime-picker-text" },
              vue.toDisplayString($options.selectTimeText),
              1
              /* TEXT */
            )
          ]),
          $data.dateShow ? (vue.openBlock(), vue.createElementBlock("view", {
            key: 0,
            class: "uni-datetime-picker__container-box"
          }, [
            vue.createElementVNode("picker-view", {
              class: "uni-datetime-picker-view",
              "indicator-style": $data.indicatorStyle,
              value: $options.ymd,
              onChange: _cache[2] || (_cache[2] = (...args) => $options.bindDateChange && $options.bindDateChange(...args))
            }, [
              vue.createElementVNode("picker-view-column", null, [
                (vue.openBlock(true), vue.createElementBlock(
                  vue.Fragment,
                  null,
                  vue.renderList($options.years, (item, index) => {
                    return vue.openBlock(), vue.createElementBlock("view", {
                      class: "uni-datetime-picker-item",
                      key: index
                    }, [
                      vue.createElementVNode(
                        "text",
                        { class: "uni-datetime-picker-item" },
                        vue.toDisplayString($options.lessThanTen(item)),
                        1
                        /* TEXT */
                      )
                    ]);
                  }),
                  128
                  /* KEYED_FRAGMENT */
                ))
              ]),
              vue.createElementVNode("picker-view-column", null, [
                (vue.openBlock(true), vue.createElementBlock(
                  vue.Fragment,
                  null,
                  vue.renderList($options.months, (item, index) => {
                    return vue.openBlock(), vue.createElementBlock("view", {
                      class: "uni-datetime-picker-item",
                      key: index
                    }, [
                      vue.createElementVNode(
                        "text",
                        { class: "uni-datetime-picker-item" },
                        vue.toDisplayString($options.lessThanTen(item)),
                        1
                        /* TEXT */
                      )
                    ]);
                  }),
                  128
                  /* KEYED_FRAGMENT */
                ))
              ]),
              vue.createElementVNode("picker-view-column", null, [
                (vue.openBlock(true), vue.createElementBlock(
                  vue.Fragment,
                  null,
                  vue.renderList($options.days, (item, index) => {
                    return vue.openBlock(), vue.createElementBlock("view", {
                      class: "uni-datetime-picker-item",
                      key: index
                    }, [
                      vue.createElementVNode(
                        "text",
                        { class: "uni-datetime-picker-item" },
                        vue.toDisplayString($options.lessThanTen(item)),
                        1
                        /* TEXT */
                      )
                    ]);
                  }),
                  128
                  /* KEYED_FRAGMENT */
                ))
              ])
            ], 40, ["indicator-style", "value"]),
            vue.createCommentVNode(" 兼容 nvue 不支持伪类 "),
            vue.createElementVNode("text", { class: "uni-datetime-picker-sign sign-left" }, "-"),
            vue.createElementVNode("text", { class: "uni-datetime-picker-sign sign-right" }, "-")
          ])) : vue.createCommentVNode("v-if", true),
          $data.timeShow ? (vue.openBlock(), vue.createElementBlock("view", {
            key: 1,
            class: "uni-datetime-picker__container-box"
          }, [
            vue.createElementVNode("picker-view", {
              class: vue.normalizeClass(["uni-datetime-picker-view", [$props.hideSecond ? "time-hide-second" : ""]]),
              "indicator-style": $data.indicatorStyle,
              value: $options.hms,
              onChange: _cache[3] || (_cache[3] = (...args) => $options.bindTimeChange && $options.bindTimeChange(...args))
            }, [
              vue.createElementVNode("picker-view-column", null, [
                (vue.openBlock(true), vue.createElementBlock(
                  vue.Fragment,
                  null,
                  vue.renderList($options.hours, (item, index) => {
                    return vue.openBlock(), vue.createElementBlock("view", {
                      class: "uni-datetime-picker-item",
                      key: index
                    }, [
                      vue.createElementVNode(
                        "text",
                        { class: "uni-datetime-picker-item" },
                        vue.toDisplayString($options.lessThanTen(item)),
                        1
                        /* TEXT */
                      )
                    ]);
                  }),
                  128
                  /* KEYED_FRAGMENT */
                ))
              ]),
              vue.createElementVNode("picker-view-column", null, [
                (vue.openBlock(true), vue.createElementBlock(
                  vue.Fragment,
                  null,
                  vue.renderList($options.minutes, (item, index) => {
                    return vue.openBlock(), vue.createElementBlock("view", {
                      class: "uni-datetime-picker-item",
                      key: index
                    }, [
                      vue.createElementVNode(
                        "text",
                        { class: "uni-datetime-picker-item" },
                        vue.toDisplayString($options.lessThanTen(item)),
                        1
                        /* TEXT */
                      )
                    ]);
                  }),
                  128
                  /* KEYED_FRAGMENT */
                ))
              ]),
              !$props.hideSecond ? (vue.openBlock(), vue.createElementBlock("picker-view-column", { key: 0 }, [
                (vue.openBlock(true), vue.createElementBlock(
                  vue.Fragment,
                  null,
                  vue.renderList($options.seconds, (item, index) => {
                    return vue.openBlock(), vue.createElementBlock("view", {
                      class: "uni-datetime-picker-item",
                      key: index
                    }, [
                      vue.createElementVNode(
                        "text",
                        { class: "uni-datetime-picker-item" },
                        vue.toDisplayString($options.lessThanTen(item)),
                        1
                        /* TEXT */
                      )
                    ]);
                  }),
                  128
                  /* KEYED_FRAGMENT */
                ))
              ])) : vue.createCommentVNode("v-if", true)
            ], 42, ["indicator-style", "value"]),
            vue.createCommentVNode(" 兼容 nvue 不支持伪类 "),
            vue.createElementVNode(
              "text",
              {
                class: vue.normalizeClass(["uni-datetime-picker-sign", [$props.hideSecond ? "sign-center" : "sign-left"]])
              },
              ":",
              2
              /* CLASS */
            ),
            !$props.hideSecond ? (vue.openBlock(), vue.createElementBlock("text", {
              key: 0,
              class: "uni-datetime-picker-sign sign-right"
            }, ":")) : vue.createCommentVNode("v-if", true)
          ])) : vue.createCommentVNode("v-if", true),
          vue.createElementVNode("view", { class: "uni-datetime-picker-btn" }, [
            vue.createElementVNode("view", {
              onClick: _cache[4] || (_cache[4] = (...args) => $options.clearTime && $options.clearTime(...args))
            }, [
              vue.createElementVNode(
                "text",
                { class: "uni-datetime-picker-btn-text" },
                vue.toDisplayString($options.clearText),
                1
                /* TEXT */
              )
            ]),
            vue.createElementVNode("view", { class: "uni-datetime-picker-btn-group" }, [
              vue.createElementVNode("view", {
                class: "uni-datetime-picker-cancel",
                onClick: _cache[5] || (_cache[5] = (...args) => $options.tiggerTimePicker && $options.tiggerTimePicker(...args))
              }, [
                vue.createElementVNode(
                  "text",
                  { class: "uni-datetime-picker-btn-text" },
                  vue.toDisplayString($options.cancelText),
                  1
                  /* TEXT */
                )
              ]),
              vue.createElementVNode("view", {
                onClick: _cache[6] || (_cache[6] = (...args) => $options.setTime && $options.setTime(...args))
              }, [
                vue.createElementVNode(
                  "text",
                  { class: "uni-datetime-picker-btn-text" },
                  vue.toDisplayString($options.okText),
                  1
                  /* TEXT */
                )
              ])
            ])
          ])
        ],
        6
        /* CLASS, STYLE */
      )) : vue.createCommentVNode("v-if", true)
    ]);
  }
  const TimePicker = /* @__PURE__ */ _export_sfc(_sfc_main$A, [["render", _sfc_render$z], ["__scopeId", "data-v-1d532b70"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/uni_modules/uni-datetime-picker/components/uni-datetime-picker/time-picker.vue"]]);
  const {
    t
  } = initVueI18n(i18nMessages);
  const _sfc_main$z = {
    components: {
      calendarItem,
      timePicker: TimePicker
    },
    options: {
      virtualHost: true
    },
    props: {
      date: {
        type: String,
        default: ""
      },
      defTime: {
        type: [String, Object],
        default: ""
      },
      selectableTimes: {
        type: [Object],
        default() {
          return {};
        }
      },
      selected: {
        type: Array,
        default() {
          return [];
        }
      },
      startDate: {
        type: String,
        default: ""
      },
      endDate: {
        type: String,
        default: ""
      },
      startPlaceholder: {
        type: String,
        default: ""
      },
      endPlaceholder: {
        type: String,
        default: ""
      },
      range: {
        type: Boolean,
        default: false
      },
      hasTime: {
        type: Boolean,
        default: false
      },
      insert: {
        type: Boolean,
        default: true
      },
      showMonth: {
        type: Boolean,
        default: true
      },
      clearDate: {
        type: Boolean,
        default: true
      },
      checkHover: {
        type: Boolean,
        default: true
      },
      hideSecond: {
        type: [Boolean],
        default: false
      },
      pleStatus: {
        type: Object,
        default() {
          return {
            before: "",
            after: "",
            data: [],
            fulldate: ""
          };
        }
      },
      defaultValue: {
        type: [String, Object, Array],
        default: ""
      }
    },
    data() {
      return {
        show: false,
        weeks: [],
        calendar: {},
        nowDate: {},
        aniMaskShow: false,
        firstEnter: true,
        time: "",
        timeRange: {
          startTime: "",
          endTime: ""
        },
        tempSingleDate: "",
        tempRange: {
          before: "",
          after: ""
        }
      };
    },
    watch: {
      date: {
        immediate: true,
        handler(newVal) {
          if (!this.range) {
            this.tempSingleDate = newVal;
            setTimeout(() => {
              this.init(newVal);
            }, 100);
          }
        }
      },
      defTime: {
        immediate: true,
        handler(newVal) {
          if (!this.range) {
            this.time = newVal;
          } else {
            this.timeRange.startTime = newVal.start;
            this.timeRange.endTime = newVal.end;
          }
        }
      },
      startDate(val) {
        if (!this.cale) {
          return;
        }
        this.cale.setStartDate(val);
        this.cale.setDate(this.nowDate.fullDate);
        this.weeks = this.cale.weeks;
      },
      endDate(val) {
        if (!this.cale) {
          return;
        }
        this.cale.setEndDate(val);
        this.cale.setDate(this.nowDate.fullDate);
        this.weeks = this.cale.weeks;
      },
      selected(newVal) {
        if (!this.cale) {
          return;
        }
        this.cale.setSelectInfo(this.nowDate.fullDate, newVal);
        this.weeks = this.cale.weeks;
      },
      pleStatus: {
        immediate: true,
        handler(newVal) {
          const {
            before,
            after,
            fulldate,
            which
          } = newVal;
          this.tempRange.before = before;
          this.tempRange.after = after;
          setTimeout(() => {
            if (fulldate) {
              this.cale.setHoverMultiple(fulldate);
              if (before && after) {
                this.cale.lastHover = true;
                if (this.rangeWithinMonth(after, before))
                  return;
                this.setDate(before);
              } else {
                this.cale.setMultiple(fulldate);
                this.setDate(this.nowDate.fullDate);
                this.calendar.fullDate = "";
                this.cale.lastHover = false;
              }
            } else {
              if (!this.cale) {
                return;
              }
              this.cale.setDefaultMultiple(before, after);
              if (which === "left" && before) {
                this.setDate(before);
                this.weeks = this.cale.weeks;
              } else if (after) {
                this.setDate(after);
                this.weeks = this.cale.weeks;
              }
              this.cale.lastHover = true;
            }
          }, 16);
        }
      }
    },
    computed: {
      timepickerStartTime() {
        const activeDate = this.range ? this.tempRange.before : this.calendar.fullDate;
        return activeDate === this.startDate ? this.selectableTimes.start : "";
      },
      timepickerEndTime() {
        const activeDate = this.range ? this.tempRange.after : this.calendar.fullDate;
        return activeDate === this.endDate ? this.selectableTimes.end : "";
      },
      /**
       * for i18n
       */
      selectDateText() {
        return t("uni-datetime-picker.selectDate");
      },
      startDateText() {
        return this.startPlaceholder || t("uni-datetime-picker.startDate");
      },
      endDateText() {
        return this.endPlaceholder || t("uni-datetime-picker.endDate");
      },
      okText() {
        return t("uni-datetime-picker.ok");
      },
      yearText() {
        return t("uni-datetime-picker.year");
      },
      monthText() {
        return t("uni-datetime-picker.month");
      },
      MONText() {
        return t("uni-calender.MON");
      },
      TUEText() {
        return t("uni-calender.TUE");
      },
      WEDText() {
        return t("uni-calender.WED");
      },
      THUText() {
        return t("uni-calender.THU");
      },
      FRIText() {
        return t("uni-calender.FRI");
      },
      SATText() {
        return t("uni-calender.SAT");
      },
      SUNText() {
        return t("uni-calender.SUN");
      },
      confirmText() {
        return t("uni-calender.confirm");
      }
    },
    created() {
      this.cale = new Calendar$1({
        selected: this.selected,
        startDate: this.startDate,
        endDate: this.endDate,
        range: this.range
      });
      this.init(this.date);
    },
    methods: {
      leaveCale() {
        this.firstEnter = true;
      },
      handleMouse(weeks) {
        if (weeks.disable)
          return;
        if (this.cale.lastHover)
          return;
        let {
          before,
          after
        } = this.cale.multipleStatus;
        if (!before)
          return;
        this.calendar = weeks;
        this.cale.setHoverMultiple(this.calendar.fullDate);
        this.weeks = this.cale.weeks;
        if (this.firstEnter) {
          this.$emit("firstEnterCale", this.cale.multipleStatus);
          this.firstEnter = false;
        }
      },
      rangeWithinMonth(A, B) {
        const [yearA, monthA] = A.split("-");
        const [yearB, monthB] = B.split("-");
        return yearA === yearB && monthA === monthB;
      },
      // 蒙版点击事件
      maskClick() {
        this.close();
        this.$emit("maskClose");
      },
      clearCalender() {
        if (this.range) {
          this.timeRange.startTime = "";
          this.timeRange.endTime = "";
          this.tempRange.before = "";
          this.tempRange.after = "";
          this.cale.multipleStatus.before = "";
          this.cale.multipleStatus.after = "";
          this.cale.multipleStatus.data = [];
          this.cale.lastHover = false;
        } else {
          this.time = "";
          this.tempSingleDate = "";
        }
        this.calendar.fullDate = "";
        this.setDate(/* @__PURE__ */ new Date());
      },
      bindDateChange(e) {
        const value = e.detail.value + "-1";
        this.setDate(value);
      },
      /**
       * 初始化日期显示
       * @param {Object} date
       */
      init(date) {
        if (!this.cale) {
          return;
        }
        this.cale.setDate(date || /* @__PURE__ */ new Date());
        this.weeks = this.cale.weeks;
        this.nowDate = this.cale.getInfo(date);
        this.calendar = {
          ...this.nowDate
        };
        if (!date) {
          this.calendar.fullDate = "";
          if (this.defaultValue && !this.range) {
            const defaultDate = new Date(this.defaultValue);
            const fullDate = getDate(defaultDate);
            const year = defaultDate.getFullYear();
            const month = defaultDate.getMonth() + 1;
            const date2 = defaultDate.getDate();
            const day = defaultDate.getDay();
            this.calendar = {
              fullDate,
              year,
              month,
              date: date2,
              day
            }, this.tempSingleDate = fullDate;
            this.time = getTime(defaultDate, this.hideSecond);
          }
        }
      },
      /**
       * 打开日历弹窗
       */
      open() {
        if (this.clearDate && !this.insert) {
          this.cale.cleanMultipleStatus();
          this.init(this.date);
        }
        this.show = true;
        this.$nextTick(() => {
          setTimeout(() => {
            this.aniMaskShow = true;
          }, 50);
        });
      },
      /**
       * 关闭日历弹窗
       */
      close() {
        this.aniMaskShow = false;
        this.$nextTick(() => {
          setTimeout(() => {
            this.show = false;
            this.$emit("close");
          }, 300);
        });
      },
      /**
       * 确认按钮
       */
      confirm() {
        this.setEmit("confirm");
        this.close();
      },
      /**
       * 变化触发
       */
      change(isSingleChange) {
        if (!this.insert && !isSingleChange)
          return;
        this.setEmit("change");
      },
      /**
       * 选择月份触发
       */
      monthSwitch() {
        let {
          year,
          month
        } = this.nowDate;
        this.$emit("monthSwitch", {
          year,
          month: Number(month)
        });
      },
      /**
       * 派发事件
       * @param {Object} name
       */
      setEmit(name) {
        if (!this.range) {
          if (!this.calendar.fullDate) {
            this.calendar = this.cale.getInfo(/* @__PURE__ */ new Date());
            this.tempSingleDate = this.calendar.fullDate;
          }
          if (this.hasTime && !this.time) {
            this.time = getTime(/* @__PURE__ */ new Date(), this.hideSecond);
          }
        }
        let {
          year,
          month,
          date,
          fullDate,
          extraInfo
        } = this.calendar;
        this.$emit(name, {
          range: this.cale.multipleStatus,
          year,
          month,
          date,
          time: this.time,
          timeRange: this.timeRange,
          fulldate: fullDate,
          extraInfo: extraInfo || {}
        });
      },
      /**
       * 选择天触发
       * @param {Object} weeks
       */
      choiceDate(weeks) {
        if (weeks.disable)
          return;
        this.calendar = weeks;
        this.calendar.userChecked = true;
        this.cale.setMultiple(this.calendar.fullDate, true);
        this.weeks = this.cale.weeks;
        this.tempSingleDate = this.calendar.fullDate;
        const beforeDate = new Date(this.cale.multipleStatus.before).getTime();
        const afterDate = new Date(this.cale.multipleStatus.after).getTime();
        if (beforeDate > afterDate && afterDate) {
          this.tempRange.before = this.cale.multipleStatus.after;
          this.tempRange.after = this.cale.multipleStatus.before;
        } else {
          this.tempRange.before = this.cale.multipleStatus.before;
          this.tempRange.after = this.cale.multipleStatus.after;
        }
        this.change(true);
      },
      changeMonth(type) {
        let newDate;
        if (type === "pre") {
          newDate = this.cale.getPreMonthObj(this.nowDate.fullDate).fullDate;
        } else if (type === "next") {
          newDate = this.cale.getNextMonthObj(this.nowDate.fullDate).fullDate;
        }
        this.setDate(newDate);
        this.monthSwitch();
      },
      /**
       * 设置日期
       * @param {Object} date
       */
      setDate(date) {
        this.cale.setDate(date);
        this.weeks = this.cale.weeks;
        this.nowDate = this.cale.getInfo(date);
      }
    }
  };
  function _sfc_render$y(_ctx, _cache, $props, $setup, $data, $options) {
    const _component_calendar_item = vue.resolveComponent("calendar-item");
    const _component_time_picker = vue.resolveComponent("time-picker");
    const _component_uni_icons = resolveEasycom(vue.resolveDynamicComponent("uni-icons"), __easycom_0$2);
    return vue.openBlock(), vue.createElementBlock(
      "view",
      {
        class: "uni-calendar",
        onMouseleave: _cache[9] || (_cache[9] = (...args) => $options.leaveCale && $options.leaveCale(...args))
      },
      [
        !$props.insert && $data.show ? (vue.openBlock(), vue.createElementBlock(
          "view",
          {
            key: 0,
            class: vue.normalizeClass(["uni-calendar__mask", { "uni-calendar--mask-show": $data.aniMaskShow }]),
            onClick: _cache[0] || (_cache[0] = (...args) => $options.maskClick && $options.maskClick(...args))
          },
          null,
          2
          /* CLASS */
        )) : vue.createCommentVNode("v-if", true),
        $props.insert || $data.show ? (vue.openBlock(), vue.createElementBlock(
          "view",
          {
            key: 1,
            class: vue.normalizeClass(["uni-calendar__content", { "uni-calendar--fixed": !$props.insert, "uni-calendar--ani-show": $data.aniMaskShow, "uni-calendar__content-mobile": $data.aniMaskShow }])
          },
          [
            vue.createElementVNode(
              "view",
              {
                class: vue.normalizeClass(["uni-calendar__header", { "uni-calendar__header-mobile": !$props.insert }])
              },
              [
                vue.createElementVNode("view", {
                  class: "uni-calendar__header-btn-box",
                  onClick: _cache[1] || (_cache[1] = vue.withModifiers(($event) => $options.changeMonth("pre"), ["stop"]))
                }, [
                  vue.createElementVNode("view", { class: "uni-calendar__header-btn uni-calendar--left" })
                ]),
                vue.createElementVNode("picker", {
                  mode: "date",
                  value: $props.date,
                  fields: "month",
                  onChange: _cache[2] || (_cache[2] = (...args) => $options.bindDateChange && $options.bindDateChange(...args))
                }, [
                  vue.createElementVNode(
                    "text",
                    { class: "uni-calendar__header-text" },
                    vue.toDisplayString(($data.nowDate.year || "") + $options.yearText + ($data.nowDate.month || "") + $options.monthText),
                    1
                    /* TEXT */
                  )
                ], 40, ["value"]),
                vue.createElementVNode("view", {
                  class: "uni-calendar__header-btn-box",
                  onClick: _cache[3] || (_cache[3] = vue.withModifiers(($event) => $options.changeMonth("next"), ["stop"]))
                }, [
                  vue.createElementVNode("view", { class: "uni-calendar__header-btn uni-calendar--right" })
                ]),
                !$props.insert ? (vue.openBlock(), vue.createElementBlock("view", {
                  key: 0,
                  class: "dialog-close",
                  onClick: _cache[4] || (_cache[4] = (...args) => $options.maskClick && $options.maskClick(...args))
                }, [
                  vue.createElementVNode("view", {
                    class: "dialog-close-plus",
                    "data-id": "close"
                  }),
                  vue.createElementVNode("view", {
                    class: "dialog-close-plus dialog-close-rotate",
                    "data-id": "close"
                  })
                ])) : vue.createCommentVNode("v-if", true)
              ],
              2
              /* CLASS */
            ),
            vue.createElementVNode("view", { class: "uni-calendar__box" }, [
              $props.showMonth ? (vue.openBlock(), vue.createElementBlock("view", {
                key: 0,
                class: "uni-calendar__box-bg"
              }, [
                vue.createElementVNode(
                  "text",
                  { class: "uni-calendar__box-bg-text" },
                  vue.toDisplayString($data.nowDate.month),
                  1
                  /* TEXT */
                )
              ])) : vue.createCommentVNode("v-if", true),
              vue.createElementVNode("view", {
                class: "uni-calendar__weeks",
                style: { "padding-bottom": "7px" }
              }, [
                vue.createElementVNode("view", { class: "uni-calendar__weeks-day" }, [
                  vue.createElementVNode(
                    "text",
                    { class: "uni-calendar__weeks-day-text" },
                    vue.toDisplayString($options.SUNText),
                    1
                    /* TEXT */
                  )
                ]),
                vue.createElementVNode("view", { class: "uni-calendar__weeks-day" }, [
                  vue.createElementVNode(
                    "text",
                    { class: "uni-calendar__weeks-day-text" },
                    vue.toDisplayString($options.MONText),
                    1
                    /* TEXT */
                  )
                ]),
                vue.createElementVNode("view", { class: "uni-calendar__weeks-day" }, [
                  vue.createElementVNode(
                    "text",
                    { class: "uni-calendar__weeks-day-text" },
                    vue.toDisplayString($options.TUEText),
                    1
                    /* TEXT */
                  )
                ]),
                vue.createElementVNode("view", { class: "uni-calendar__weeks-day" }, [
                  vue.createElementVNode(
                    "text",
                    { class: "uni-calendar__weeks-day-text" },
                    vue.toDisplayString($options.WEDText),
                    1
                    /* TEXT */
                  )
                ]),
                vue.createElementVNode("view", { class: "uni-calendar__weeks-day" }, [
                  vue.createElementVNode(
                    "text",
                    { class: "uni-calendar__weeks-day-text" },
                    vue.toDisplayString($options.THUText),
                    1
                    /* TEXT */
                  )
                ]),
                vue.createElementVNode("view", { class: "uni-calendar__weeks-day" }, [
                  vue.createElementVNode(
                    "text",
                    { class: "uni-calendar__weeks-day-text" },
                    vue.toDisplayString($options.FRIText),
                    1
                    /* TEXT */
                  )
                ]),
                vue.createElementVNode("view", { class: "uni-calendar__weeks-day" }, [
                  vue.createElementVNode(
                    "text",
                    { class: "uni-calendar__weeks-day-text" },
                    vue.toDisplayString($options.SATText),
                    1
                    /* TEXT */
                  )
                ])
              ]),
              (vue.openBlock(true), vue.createElementBlock(
                vue.Fragment,
                null,
                vue.renderList($data.weeks, (item, weekIndex) => {
                  return vue.openBlock(), vue.createElementBlock("view", {
                    class: "uni-calendar__weeks",
                    key: weekIndex
                  }, [
                    (vue.openBlock(true), vue.createElementBlock(
                      vue.Fragment,
                      null,
                      vue.renderList(item, (weeks, weeksIndex) => {
                        return vue.openBlock(), vue.createElementBlock("view", {
                          class: "uni-calendar__weeks-item",
                          key: weeksIndex
                        }, [
                          vue.createVNode(_component_calendar_item, {
                            class: "uni-calendar-item--hook",
                            weeks,
                            calendar: $data.calendar,
                            selected: $props.selected,
                            checkHover: $props.range,
                            onChange: $options.choiceDate,
                            onHandleMouse: $options.handleMouse
                          }, null, 8, ["weeks", "calendar", "selected", "checkHover", "onChange", "onHandleMouse"])
                        ]);
                      }),
                      128
                      /* KEYED_FRAGMENT */
                    ))
                  ]);
                }),
                128
                /* KEYED_FRAGMENT */
              ))
            ]),
            !$props.insert && !$props.range && $props.hasTime ? (vue.openBlock(), vue.createElementBlock("view", {
              key: 0,
              class: "uni-date-changed uni-calendar--fixed-top",
              style: { "padding": "0 80px" }
            }, [
              vue.createElementVNode(
                "view",
                { class: "uni-date-changed--time-date" },
                vue.toDisplayString($data.tempSingleDate ? $data.tempSingleDate : $options.selectDateText),
                1
                /* TEXT */
              ),
              vue.createVNode(_component_time_picker, {
                type: "time",
                start: $options.timepickerStartTime,
                end: $options.timepickerEndTime,
                modelValue: $data.time,
                "onUpdate:modelValue": _cache[5] || (_cache[5] = ($event) => $data.time = $event),
                disabled: !$data.tempSingleDate,
                border: false,
                "hide-second": $props.hideSecond,
                class: "time-picker-style"
              }, null, 8, ["start", "end", "modelValue", "disabled", "hide-second"])
            ])) : vue.createCommentVNode("v-if", true),
            !$props.insert && $props.range && $props.hasTime ? (vue.openBlock(), vue.createElementBlock("view", {
              key: 1,
              class: "uni-date-changed uni-calendar--fixed-top"
            }, [
              vue.createElementVNode("view", { class: "uni-date-changed--time-start" }, [
                vue.createElementVNode(
                  "view",
                  { class: "uni-date-changed--time-date" },
                  vue.toDisplayString($data.tempRange.before ? $data.tempRange.before : $options.startDateText),
                  1
                  /* TEXT */
                ),
                vue.createVNode(_component_time_picker, {
                  type: "time",
                  start: $options.timepickerStartTime,
                  modelValue: $data.timeRange.startTime,
                  "onUpdate:modelValue": _cache[6] || (_cache[6] = ($event) => $data.timeRange.startTime = $event),
                  border: false,
                  "hide-second": $props.hideSecond,
                  disabled: !$data.tempRange.before,
                  class: "time-picker-style"
                }, null, 8, ["start", "modelValue", "hide-second", "disabled"])
              ]),
              vue.createElementVNode("view", { style: { "line-height": "50px" } }, [
                vue.createVNode(_component_uni_icons, {
                  type: "arrowthinright",
                  color: "#999"
                })
              ]),
              vue.createElementVNode("view", { class: "uni-date-changed--time-end" }, [
                vue.createElementVNode(
                  "view",
                  { class: "uni-date-changed--time-date" },
                  vue.toDisplayString($data.tempRange.after ? $data.tempRange.after : $options.endDateText),
                  1
                  /* TEXT */
                ),
                vue.createVNode(_component_time_picker, {
                  type: "time",
                  end: $options.timepickerEndTime,
                  modelValue: $data.timeRange.endTime,
                  "onUpdate:modelValue": _cache[7] || (_cache[7] = ($event) => $data.timeRange.endTime = $event),
                  border: false,
                  "hide-second": $props.hideSecond,
                  disabled: !$data.tempRange.after,
                  class: "time-picker-style"
                }, null, 8, ["end", "modelValue", "hide-second", "disabled"])
              ])
            ])) : vue.createCommentVNode("v-if", true),
            !$props.insert ? (vue.openBlock(), vue.createElementBlock("view", {
              key: 2,
              class: "uni-date-changed uni-date-btn--ok"
            }, [
              vue.createElementVNode(
                "view",
                {
                  class: "uni-datetime-picker--btn",
                  onClick: _cache[8] || (_cache[8] = (...args) => $options.confirm && $options.confirm(...args))
                },
                vue.toDisplayString($options.confirmText),
                1
                /* TEXT */
              )
            ])) : vue.createCommentVNode("v-if", true)
          ],
          2
          /* CLASS */
        )) : vue.createCommentVNode("v-if", true)
      ],
      32
      /* NEED_HYDRATION */
    );
  }
  const Calendar = /* @__PURE__ */ _export_sfc(_sfc_main$z, [["render", _sfc_render$y], ["__scopeId", "data-v-1d379219"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/uni_modules/uni-datetime-picker/components/uni-datetime-picker/calendar.vue"]]);
  const _sfc_main$y = {
    name: "UniDatetimePicker",
    options: {
      virtualHost: true
    },
    components: {
      Calendar,
      TimePicker
    },
    data() {
      return {
        isRange: false,
        hasTime: false,
        displayValue: "",
        inputDate: "",
        calendarDate: "",
        pickerTime: "",
        calendarRange: {
          startDate: "",
          startTime: "",
          endDate: "",
          endTime: ""
        },
        displayRangeValue: {
          startDate: "",
          endDate: ""
        },
        tempRange: {
          startDate: "",
          startTime: "",
          endDate: "",
          endTime: ""
        },
        // 左右日历同步数据
        startMultipleStatus: {
          before: "",
          after: "",
          data: [],
          fulldate: ""
        },
        endMultipleStatus: {
          before: "",
          after: "",
          data: [],
          fulldate: ""
        },
        pickerVisible: false,
        pickerPositionStyle: null,
        isEmitValue: false,
        isPhone: false,
        isFirstShow: true,
        i18nT: () => {
        }
      };
    },
    props: {
      type: {
        type: String,
        default: "datetime"
      },
      value: {
        type: [String, Number, Array, Date],
        default: ""
      },
      modelValue: {
        type: [String, Number, Array, Date],
        default: ""
      },
      start: {
        type: [Number, String],
        default: ""
      },
      end: {
        type: [Number, String],
        default: ""
      },
      returnType: {
        type: String,
        default: "string"
      },
      placeholder: {
        type: String,
        default: ""
      },
      startPlaceholder: {
        type: String,
        default: ""
      },
      endPlaceholder: {
        type: String,
        default: ""
      },
      rangeSeparator: {
        type: String,
        default: "-"
      },
      border: {
        type: [Boolean],
        default: true
      },
      disabled: {
        type: [Boolean],
        default: false
      },
      clearIcon: {
        type: [Boolean],
        default: true
      },
      hideSecond: {
        type: [Boolean],
        default: false
      },
      defaultValue: {
        type: [String, Object, Array],
        default: ""
      }
    },
    watch: {
      type: {
        immediate: true,
        handler(newVal) {
          this.hasTime = newVal.indexOf("time") !== -1;
          this.isRange = newVal.indexOf("range") !== -1;
        }
      },
      modelValue: {
        immediate: true,
        handler(newVal) {
          if (this.isEmitValue) {
            this.isEmitValue = false;
            return;
          }
          this.initPicker(newVal);
        }
      },
      start: {
        immediate: true,
        handler(newVal) {
          if (!newVal)
            return;
          this.calendarRange.startDate = getDate(newVal);
          if (this.hasTime) {
            this.calendarRange.startTime = getTime(newVal);
          }
        }
      },
      end: {
        immediate: true,
        handler(newVal) {
          if (!newVal)
            return;
          this.calendarRange.endDate = getDate(newVal);
          if (this.hasTime) {
            this.calendarRange.endTime = getTime(newVal, this.hideSecond);
          }
        }
      }
    },
    computed: {
      timepickerStartTime() {
        const activeDate = this.isRange ? this.tempRange.startDate : this.inputDate;
        return activeDate === this.calendarRange.startDate ? this.calendarRange.startTime : "";
      },
      timepickerEndTime() {
        const activeDate = this.isRange ? this.tempRange.endDate : this.inputDate;
        return activeDate === this.calendarRange.endDate ? this.calendarRange.endTime : "";
      },
      mobileCalendarTime() {
        const timeRange = {
          start: this.tempRange.startTime,
          end: this.tempRange.endTime
        };
        return this.isRange ? timeRange : this.pickerTime;
      },
      mobSelectableTime() {
        return {
          start: this.calendarRange.startTime,
          end: this.calendarRange.endTime
        };
      },
      datePopupWidth() {
        return this.isRange ? 653 : 301;
      },
      /**
       * for i18n
       */
      singlePlaceholderText() {
        return this.placeholder || (this.type === "date" ? this.selectDateText : this.selectDateTimeText);
      },
      startPlaceholderText() {
        return this.startPlaceholder || this.startDateText;
      },
      endPlaceholderText() {
        return this.endPlaceholder || this.endDateText;
      },
      selectDateText() {
        return this.i18nT("uni-datetime-picker.selectDate");
      },
      selectDateTimeText() {
        return this.i18nT("uni-datetime-picker.selectDateTime");
      },
      selectTimeText() {
        return this.i18nT("uni-datetime-picker.selectTime");
      },
      startDateText() {
        return this.startPlaceholder || this.i18nT("uni-datetime-picker.startDate");
      },
      startTimeText() {
        return this.i18nT("uni-datetime-picker.startTime");
      },
      endDateText() {
        return this.endPlaceholder || this.i18nT("uni-datetime-picker.endDate");
      },
      endTimeText() {
        return this.i18nT("uni-datetime-picker.endTime");
      },
      okText() {
        return this.i18nT("uni-datetime-picker.ok");
      },
      clearText() {
        return this.i18nT("uni-datetime-picker.clear");
      },
      showClearIcon() {
        return this.clearIcon && !this.disabled && (this.displayValue || this.displayRangeValue.startDate && this.displayRangeValue.endDate);
      }
    },
    created() {
      this.initI18nT();
      this.platform();
    },
    methods: {
      initI18nT() {
        const vueI18n = initVueI18n(i18nMessages);
        this.i18nT = vueI18n.t;
      },
      initPicker(newVal) {
        if (!newVal && !this.defaultValue || Array.isArray(newVal) && !newVal.length) {
          this.$nextTick(() => {
            this.clear(false);
          });
          return;
        }
        if (!Array.isArray(newVal) && !this.isRange) {
          if (newVal) {
            this.displayValue = this.inputDate = this.calendarDate = getDate(newVal);
            if (this.hasTime) {
              this.pickerTime = getTime(newVal, this.hideSecond);
              this.displayValue = `${this.displayValue} ${this.pickerTime}`;
            }
          } else if (this.defaultValue) {
            this.inputDate = this.calendarDate = getDate(this.defaultValue);
            if (this.hasTime) {
              this.pickerTime = getTime(this.defaultValue, this.hideSecond);
            }
          }
        } else {
          const [before, after] = newVal;
          if (!before && !after)
            return;
          const beforeDate = getDate(before);
          const beforeTime = getTime(before, this.hideSecond);
          const afterDate = getDate(after);
          const afterTime = getTime(after, this.hideSecond);
          const startDate = beforeDate;
          const endDate = afterDate;
          this.displayRangeValue.startDate = this.tempRange.startDate = startDate;
          this.displayRangeValue.endDate = this.tempRange.endDate = endDate;
          if (this.hasTime) {
            this.displayRangeValue.startDate = `${beforeDate} ${beforeTime}`;
            this.displayRangeValue.endDate = `${afterDate} ${afterTime}`;
            this.tempRange.startTime = beforeTime;
            this.tempRange.endTime = afterTime;
          }
          const defaultRange = {
            before: beforeDate,
            after: afterDate
          };
          this.startMultipleStatus = Object.assign({}, this.startMultipleStatus, defaultRange, {
            which: "right"
          });
          this.endMultipleStatus = Object.assign({}, this.endMultipleStatus, defaultRange, {
            which: "left"
          });
        }
      },
      updateLeftCale(e) {
        const left = this.$refs.left;
        left.cale.setHoverMultiple(e.after);
        left.setDate(this.$refs.left.nowDate.fullDate);
      },
      updateRightCale(e) {
        const right = this.$refs.right;
        right.cale.setHoverMultiple(e.after);
        right.setDate(this.$refs.right.nowDate.fullDate);
      },
      platform() {
        if (typeof navigator !== "undefined") {
          this.isPhone = navigator.userAgent.toLowerCase().indexOf("mobile") !== -1;
          return;
        }
        const {
          windowWidth
        } = uni.getSystemInfoSync();
        this.isPhone = windowWidth <= 500;
        this.windowWidth = windowWidth;
      },
      show() {
        this.$emit("show");
        if (this.disabled) {
          return;
        }
        this.platform();
        if (this.isPhone) {
          setTimeout(() => {
            this.$refs.mobile.open();
          }, 0);
          return;
        }
        this.pickerPositionStyle = {
          top: "10px"
        };
        const dateEditor = uni.createSelectorQuery().in(this).select(".uni-date-editor");
        dateEditor.boundingClientRect((rect) => {
          if (this.windowWidth - rect.left < this.datePopupWidth) {
            this.pickerPositionStyle.right = 0;
          }
        }).exec();
        setTimeout(() => {
          this.pickerVisible = !this.pickerVisible;
          if (!this.isPhone && this.isRange && this.isFirstShow) {
            this.isFirstShow = false;
            const {
              startDate,
              endDate
            } = this.calendarRange;
            if (startDate && endDate) {
              if (this.diffDate(startDate, endDate) < 30) {
                this.$refs.right.changeMonth("pre");
              }
            } else {
              if (this.isPhone) {
                this.$refs.right.cale.lastHover = false;
              }
            }
          }
        }, 50);
      },
      close() {
        setTimeout(() => {
          this.pickerVisible = false;
          this.$emit("maskClick", this.value);
          this.$refs.mobile && this.$refs.mobile.close();
        }, 20);
      },
      setEmit(value) {
        if (this.returnType === "timestamp" || this.returnType === "date") {
          if (!Array.isArray(value)) {
            if (!this.hasTime) {
              value = value + " 00:00:00";
            }
            value = this.createTimestamp(value);
            if (this.returnType === "date") {
              value = new Date(value);
            }
          } else {
            if (!this.hasTime) {
              value[0] = value[0] + " 00:00:00";
              value[1] = value[1] + " 00:00:00";
            }
            value[0] = this.createTimestamp(value[0]);
            value[1] = this.createTimestamp(value[1]);
            if (this.returnType === "date") {
              value[0] = new Date(value[0]);
              value[1] = new Date(value[1]);
            }
          }
        }
        this.$emit("update:modelValue", value);
        this.$emit("input", value);
        this.$emit("change", value);
        this.isEmitValue = true;
      },
      createTimestamp(date) {
        date = fixIosDateFormat(date);
        return Date.parse(new Date(date));
      },
      singleChange(e) {
        this.calendarDate = this.inputDate = e.fulldate;
        if (this.hasTime)
          return;
        this.confirmSingleChange();
      },
      confirmSingleChange() {
        if (!checkDate(this.inputDate)) {
          const now = /* @__PURE__ */ new Date();
          this.calendarDate = this.inputDate = getDate(now);
          this.pickerTime = getTime(now, this.hideSecond);
        }
        let startLaterInputDate = false;
        let startDate, startTime;
        if (this.start) {
          let startString = this.start;
          if (typeof this.start === "number") {
            startString = getDateTime(this.start, this.hideSecond);
          }
          [startDate, startTime] = startString.split(" ");
          if (this.start && !dateCompare(startDate, this.inputDate)) {
            startLaterInputDate = true;
            this.inputDate = startDate;
          }
        }
        let endEarlierInputDate = false;
        let endDate, endTime;
        if (this.end) {
          let endString = this.end;
          if (typeof this.end === "number") {
            endString = getDateTime(this.end, this.hideSecond);
          }
          [endDate, endTime] = endString.split(" ");
          if (this.end && !dateCompare(this.inputDate, endDate)) {
            endEarlierInputDate = true;
            this.inputDate = endDate;
          }
        }
        if (this.hasTime) {
          if (startLaterInputDate) {
            this.pickerTime = startTime || getDefaultSecond(this.hideSecond);
          }
          if (endEarlierInputDate) {
            this.pickerTime = endTime || getDefaultSecond(this.hideSecond);
          }
          if (!this.pickerTime) {
            this.pickerTime = getTime(Date.now(), this.hideSecond);
          }
          this.displayValue = `${this.inputDate} ${this.pickerTime}`;
        } else {
          this.displayValue = this.inputDate;
        }
        this.setEmit(this.displayValue);
        this.pickerVisible = false;
      },
      leftChange(e) {
        const {
          before,
          after
        } = e.range;
        this.rangeChange(before, after);
        const obj = {
          before: e.range.before,
          after: e.range.after,
          data: e.range.data,
          fulldate: e.fulldate
        };
        this.startMultipleStatus = Object.assign({}, this.startMultipleStatus, obj);
        this.$emit("calendarClick", e);
      },
      rightChange(e) {
        const {
          before,
          after
        } = e.range;
        this.rangeChange(before, after);
        const obj = {
          before: e.range.before,
          after: e.range.after,
          data: e.range.data,
          fulldate: e.fulldate
        };
        this.endMultipleStatus = Object.assign({}, this.endMultipleStatus, obj);
        this.$emit("calendarClick", e);
      },
      mobileChange(e) {
        if (this.isRange) {
          const {
            before,
            after
          } = e.range;
          if (!before) {
            return;
          }
          this.handleStartAndEnd(before, after, true);
          if (this.hasTime) {
            const {
              startTime,
              endTime
            } = e.timeRange;
            this.tempRange.startTime = startTime;
            this.tempRange.endTime = endTime;
          }
          this.confirmRangeChange();
        } else {
          if (this.hasTime) {
            this.displayValue = e.fulldate + " " + e.time;
          } else {
            this.displayValue = e.fulldate;
          }
          this.setEmit(this.displayValue);
          this.calendarDate = this.displayValue;
        }
        this.$refs.mobile.close();
      },
      rangeChange(before, after) {
        if (!(before && after))
          return;
        this.handleStartAndEnd(before, after, true);
        if (this.hasTime)
          return;
        this.confirmRangeChange();
      },
      confirmRangeChange() {
        if (!this.tempRange.startDate || !this.tempRange.endDate) {
          this.pickerVisible = false;
          return;
        }
        if (!checkDate(this.tempRange.startDate)) {
          this.tempRange.startDate = getDate(Date.now());
        }
        if (!checkDate(this.tempRange.endDate)) {
          this.tempRange.endDate = getDate(Date.now());
        }
        let start, end;
        let startDateLaterRangeStartDate = false;
        let startDateLaterRangeEndDate = false;
        let startDate, startTime;
        let compareStartDateString = this.tempRange.startDate;
        let compareEndDateString = this.tempRange.endDate;
        if (this.hasTime) {
          compareStartDateString = `${this.tempRange.startDate} ${this.tempRange.startTime}`;
          compareEndDateString = `${this.tempRange.endDate} ${this.tempRange.endTime}`;
        }
        if (this.start) {
          let startString = this.start;
          if (typeof this.start === "number") {
            startString = getDateTime(this.start, this.hideSecond);
          }
          [startDate, startTime] = startString.split(" ");
          if (this.start && !dateCompare(this.start, compareStartDateString)) {
            startDateLaterRangeStartDate = true;
            this.tempRange.startDate = startDate;
          }
          if (this.start && !dateCompare(this.start, compareEndDateString)) {
            startDateLaterRangeEndDate = true;
            this.tempRange.endDate = startDate;
          }
        }
        let endDateEarlierRangeStartDate = false;
        let endDateEarlierRangeEndDate = false;
        let endDate, endTime;
        if (this.end) {
          let endString = this.end;
          if (typeof this.end === "number") {
            endString = getDateTime(this.end, this.hideSecond);
          }
          [endDate, endTime] = endString.split(" ");
          if (this.end && !dateCompare(compareStartDateString, this.end)) {
            endDateEarlierRangeStartDate = true;
            this.tempRange.startDate = endDate;
          }
          if (this.end && !dateCompare(compareEndDateString, this.end)) {
            endDateEarlierRangeEndDate = true;
            this.tempRange.endDate = endDate;
          }
        }
        if (!this.hasTime) {
          start = this.displayRangeValue.startDate = this.tempRange.startDate;
          end = this.displayRangeValue.endDate = this.tempRange.endDate;
        } else {
          if (startDateLaterRangeStartDate) {
            this.tempRange.startTime = startTime || getDefaultSecond(this.hideSecond);
          } else if (endDateEarlierRangeStartDate) {
            this.tempRange.startTime = endTime || getDefaultSecond(this.hideSecond);
          }
          if (!this.tempRange.startTime) {
            this.tempRange.startTime = getTime(Date.now(), this.hideSecond);
          }
          if (startDateLaterRangeEndDate) {
            this.tempRange.endTime = startTime || getDefaultSecond(this.hideSecond);
          } else if (endDateEarlierRangeEndDate) {
            this.tempRange.endTime = endTime || getDefaultSecond(this.hideSecond);
          }
          if (!this.tempRange.endTime) {
            this.tempRange.endTime = getTime(Date.now(), this.hideSecond);
          }
          start = this.displayRangeValue.startDate = `${this.tempRange.startDate} ${this.tempRange.startTime}`;
          end = this.displayRangeValue.endDate = `${this.tempRange.endDate} ${this.tempRange.endTime}`;
        }
        if (!dateCompare(start, end)) {
          [start, end] = [end, start];
        }
        this.displayRangeValue.startDate = start;
        this.displayRangeValue.endDate = end;
        const displayRange = [start, end];
        this.setEmit(displayRange);
        this.pickerVisible = false;
      },
      handleStartAndEnd(before, after, temp = false) {
        if (!before)
          return;
        if (!after)
          after = before;
        const type = temp ? "tempRange" : "range";
        const isStartEarlierEnd = dateCompare(before, after);
        this[type].startDate = isStartEarlierEnd ? before : after;
        this[type].endDate = isStartEarlierEnd ? after : before;
      },
      /**
       * 比较时间大小
       */
      dateCompare(startDate, endDate) {
        startDate = new Date(startDate.replace("-", "/").replace("-", "/"));
        endDate = new Date(endDate.replace("-", "/").replace("-", "/"));
        return startDate <= endDate;
      },
      /**
       * 比较时间差
       */
      diffDate(startDate, endDate) {
        startDate = new Date(startDate.replace("-", "/").replace("-", "/"));
        endDate = new Date(endDate.replace("-", "/").replace("-", "/"));
        const diff = (endDate - startDate) / (24 * 60 * 60 * 1e3);
        return Math.abs(diff);
      },
      clear(needEmit = true) {
        if (!this.isRange) {
          this.displayValue = "";
          this.inputDate = "";
          this.pickerTime = "";
          if (this.isPhone) {
            this.$refs.mobile && this.$refs.mobile.clearCalender();
          } else {
            this.$refs.pcSingle && this.$refs.pcSingle.clearCalender();
          }
          if (needEmit) {
            this.$emit("change", "");
            this.$emit("input", "");
            this.$emit("update:modelValue", "");
          }
        } else {
          this.displayRangeValue.startDate = "";
          this.displayRangeValue.endDate = "";
          this.tempRange.startDate = "";
          this.tempRange.startTime = "";
          this.tempRange.endDate = "";
          this.tempRange.endTime = "";
          if (this.isPhone) {
            this.$refs.mobile && this.$refs.mobile.clearCalender();
          } else {
            this.$refs.left && this.$refs.left.clearCalender();
            this.$refs.right && this.$refs.right.clearCalender();
            this.$refs.right && this.$refs.right.changeMonth("next");
          }
          if (needEmit) {
            this.$emit("change", []);
            this.$emit("input", []);
            this.$emit("update:modelValue", []);
          }
        }
      },
      calendarClick(e) {
        this.$emit("calendarClick", e);
      }
    }
  };
  function _sfc_render$x(_ctx, _cache, $props, $setup, $data, $options) {
    const _component_uni_icons = resolveEasycom(vue.resolveDynamicComponent("uni-icons"), __easycom_0$2);
    const _component_time_picker = vue.resolveComponent("time-picker");
    const _component_Calendar = vue.resolveComponent("Calendar");
    return vue.openBlock(), vue.createElementBlock("view", { class: "uni-date" }, [
      vue.createElementVNode("view", {
        class: "uni-date-editor",
        onClick: _cache[1] || (_cache[1] = (...args) => $options.show && $options.show(...args))
      }, [
        vue.renderSlot(_ctx.$slots, "default", {}, () => [
          vue.createElementVNode(
            "view",
            {
              class: vue.normalizeClass(["uni-date-editor--x", { "uni-date-editor--x__disabled": $props.disabled, "uni-date-x--border": $props.border }])
            },
            [
              !$data.isRange ? (vue.openBlock(), vue.createElementBlock("view", {
                key: 0,
                class: "uni-date-x uni-date-single"
              }, [
                vue.createVNode(_component_uni_icons, {
                  class: "icon-calendar",
                  type: "calendar",
                  color: "#c0c4cc",
                  size: "22"
                }),
                vue.createElementVNode(
                  "view",
                  { class: "uni-date__x-input" },
                  vue.toDisplayString($data.displayValue || $options.singlePlaceholderText),
                  1
                  /* TEXT */
                )
              ])) : (vue.openBlock(), vue.createElementBlock("view", {
                key: 1,
                class: "uni-date-x uni-date-range"
              }, [
                vue.createVNode(_component_uni_icons, {
                  class: "icon-calendar",
                  type: "calendar",
                  color: "#c0c4cc",
                  size: "22"
                }),
                vue.createElementVNode(
                  "view",
                  { class: "uni-date__x-input text-center" },
                  vue.toDisplayString($data.displayRangeValue.startDate || $options.startPlaceholderText),
                  1
                  /* TEXT */
                ),
                vue.createElementVNode(
                  "view",
                  { class: "range-separator" },
                  vue.toDisplayString($props.rangeSeparator),
                  1
                  /* TEXT */
                ),
                vue.createElementVNode(
                  "view",
                  { class: "uni-date__x-input text-center" },
                  vue.toDisplayString($data.displayRangeValue.endDate || $options.endPlaceholderText),
                  1
                  /* TEXT */
                )
              ])),
              $options.showClearIcon ? (vue.openBlock(), vue.createElementBlock("view", {
                key: 2,
                class: "uni-date__icon-clear",
                onClick: _cache[0] || (_cache[0] = vue.withModifiers((...args) => $options.clear && $options.clear(...args), ["stop"]))
              }, [
                vue.createVNode(_component_uni_icons, {
                  type: "clear",
                  color: "#c0c4cc",
                  size: "22"
                })
              ])) : vue.createCommentVNode("v-if", true)
            ],
            2
            /* CLASS */
          )
        ], true)
      ]),
      vue.withDirectives(vue.createElementVNode(
        "view",
        {
          class: "uni-date-mask--pc",
          onClick: _cache[2] || (_cache[2] = (...args) => $options.close && $options.close(...args))
        },
        null,
        512
        /* NEED_PATCH */
      ), [
        [vue.vShow, $data.pickerVisible]
      ]),
      !$data.isPhone ? vue.withDirectives((vue.openBlock(), vue.createElementBlock(
        "view",
        {
          key: 0,
          ref: "datePicker",
          class: "uni-date-picker__container"
        },
        [
          !$data.isRange ? (vue.openBlock(), vue.createElementBlock(
            "view",
            {
              key: 0,
              class: "uni-date-single--x",
              style: vue.normalizeStyle($data.pickerPositionStyle)
            },
            [
              vue.createElementVNode("view", { class: "uni-popper__arrow" }),
              $data.hasTime ? (vue.openBlock(), vue.createElementBlock("view", {
                key: 0,
                class: "uni-date-changed popup-x-header"
              }, [
                vue.withDirectives(vue.createElementVNode("input", {
                  class: "uni-date__input text-center",
                  type: "text",
                  "onUpdate:modelValue": _cache[3] || (_cache[3] = ($event) => $data.inputDate = $event),
                  placeholder: $options.selectDateText
                }, null, 8, ["placeholder"]), [
                  [vue.vModelText, $data.inputDate]
                ]),
                vue.createVNode(_component_time_picker, {
                  type: "time",
                  modelValue: $data.pickerTime,
                  "onUpdate:modelValue": _cache[5] || (_cache[5] = ($event) => $data.pickerTime = $event),
                  border: false,
                  disabled: !$data.inputDate,
                  start: $options.timepickerStartTime,
                  end: $options.timepickerEndTime,
                  hideSecond: $props.hideSecond,
                  style: { "width": "100%" }
                }, {
                  default: vue.withCtx(() => [
                    vue.withDirectives(vue.createElementVNode("input", {
                      class: "uni-date__input text-center",
                      type: "text",
                      "onUpdate:modelValue": _cache[4] || (_cache[4] = ($event) => $data.pickerTime = $event),
                      placeholder: $options.selectTimeText,
                      disabled: !$data.inputDate
                    }, null, 8, ["placeholder", "disabled"]), [
                      [vue.vModelText, $data.pickerTime]
                    ])
                  ]),
                  _: 1
                  /* STABLE */
                }, 8, ["modelValue", "disabled", "start", "end", "hideSecond"])
              ])) : vue.createCommentVNode("v-if", true),
              vue.createVNode(_component_Calendar, {
                ref: "pcSingle",
                showMonth: false,
                "start-date": $data.calendarRange.startDate,
                "end-date": $data.calendarRange.endDate,
                date: $data.calendarDate,
                onChange: $options.singleChange,
                "default-value": $props.defaultValue,
                style: { "padding": "0 8px" }
              }, null, 8, ["start-date", "end-date", "date", "onChange", "default-value"]),
              $data.hasTime ? (vue.openBlock(), vue.createElementBlock("view", {
                key: 1,
                class: "popup-x-footer"
              }, [
                vue.createElementVNode(
                  "text",
                  {
                    class: "confirm-text",
                    onClick: _cache[6] || (_cache[6] = (...args) => $options.confirmSingleChange && $options.confirmSingleChange(...args))
                  },
                  vue.toDisplayString($options.okText),
                  1
                  /* TEXT */
                )
              ])) : vue.createCommentVNode("v-if", true)
            ],
            4
            /* STYLE */
          )) : (vue.openBlock(), vue.createElementBlock(
            "view",
            {
              key: 1,
              class: "uni-date-range--x",
              style: vue.normalizeStyle($data.pickerPositionStyle)
            },
            [
              vue.createElementVNode("view", { class: "uni-popper__arrow" }),
              $data.hasTime ? (vue.openBlock(), vue.createElementBlock("view", {
                key: 0,
                class: "popup-x-header uni-date-changed"
              }, [
                vue.createElementVNode("view", { class: "popup-x-header--datetime" }, [
                  vue.withDirectives(vue.createElementVNode("input", {
                    class: "uni-date__input uni-date-range__input",
                    type: "text",
                    "onUpdate:modelValue": _cache[7] || (_cache[7] = ($event) => $data.tempRange.startDate = $event),
                    placeholder: $options.startDateText
                  }, null, 8, ["placeholder"]), [
                    [vue.vModelText, $data.tempRange.startDate]
                  ]),
                  vue.createVNode(_component_time_picker, {
                    type: "time",
                    modelValue: $data.tempRange.startTime,
                    "onUpdate:modelValue": _cache[9] || (_cache[9] = ($event) => $data.tempRange.startTime = $event),
                    start: $options.timepickerStartTime,
                    border: false,
                    disabled: !$data.tempRange.startDate,
                    hideSecond: $props.hideSecond
                  }, {
                    default: vue.withCtx(() => [
                      vue.withDirectives(vue.createElementVNode("input", {
                        class: "uni-date__input uni-date-range__input",
                        type: "text",
                        "onUpdate:modelValue": _cache[8] || (_cache[8] = ($event) => $data.tempRange.startTime = $event),
                        placeholder: $options.startTimeText,
                        disabled: !$data.tempRange.startDate
                      }, null, 8, ["placeholder", "disabled"]), [
                        [vue.vModelText, $data.tempRange.startTime]
                      ])
                    ]),
                    _: 1
                    /* STABLE */
                  }, 8, ["modelValue", "start", "disabled", "hideSecond"])
                ]),
                vue.createVNode(_component_uni_icons, {
                  type: "arrowthinright",
                  color: "#999",
                  style: { "line-height": "40px" }
                }),
                vue.createElementVNode("view", { class: "popup-x-header--datetime" }, [
                  vue.withDirectives(vue.createElementVNode("input", {
                    class: "uni-date__input uni-date-range__input",
                    type: "text",
                    "onUpdate:modelValue": _cache[10] || (_cache[10] = ($event) => $data.tempRange.endDate = $event),
                    placeholder: $options.endDateText
                  }, null, 8, ["placeholder"]), [
                    [vue.vModelText, $data.tempRange.endDate]
                  ]),
                  vue.createVNode(_component_time_picker, {
                    type: "time",
                    modelValue: $data.tempRange.endTime,
                    "onUpdate:modelValue": _cache[12] || (_cache[12] = ($event) => $data.tempRange.endTime = $event),
                    end: $options.timepickerEndTime,
                    border: false,
                    disabled: !$data.tempRange.endDate,
                    hideSecond: $props.hideSecond
                  }, {
                    default: vue.withCtx(() => [
                      vue.withDirectives(vue.createElementVNode("input", {
                        class: "uni-date__input uni-date-range__input",
                        type: "text",
                        "onUpdate:modelValue": _cache[11] || (_cache[11] = ($event) => $data.tempRange.endTime = $event),
                        placeholder: $options.endTimeText,
                        disabled: !$data.tempRange.endDate
                      }, null, 8, ["placeholder", "disabled"]), [
                        [vue.vModelText, $data.tempRange.endTime]
                      ])
                    ]),
                    _: 1
                    /* STABLE */
                  }, 8, ["modelValue", "end", "disabled", "hideSecond"])
                ])
              ])) : vue.createCommentVNode("v-if", true),
              vue.createElementVNode("view", { class: "popup-x-body" }, [
                vue.createVNode(_component_Calendar, {
                  ref: "left",
                  showMonth: false,
                  "start-date": $data.calendarRange.startDate,
                  "end-date": $data.calendarRange.endDate,
                  range: true,
                  pleStatus: $data.endMultipleStatus,
                  onChange: $options.leftChange,
                  onFirstEnterCale: $options.updateRightCale,
                  style: { "padding": "0 8px" }
                }, null, 8, ["start-date", "end-date", "pleStatus", "onChange", "onFirstEnterCale"]),
                vue.createVNode(_component_Calendar, {
                  ref: "right",
                  showMonth: false,
                  "start-date": $data.calendarRange.startDate,
                  "end-date": $data.calendarRange.endDate,
                  range: true,
                  onChange: $options.rightChange,
                  pleStatus: $data.startMultipleStatus,
                  onFirstEnterCale: $options.updateLeftCale,
                  style: { "padding": "0 8px", "border-left": "1px solid #F1F1F1" }
                }, null, 8, ["start-date", "end-date", "onChange", "pleStatus", "onFirstEnterCale"])
              ]),
              $data.hasTime ? (vue.openBlock(), vue.createElementBlock("view", {
                key: 1,
                class: "popup-x-footer"
              }, [
                vue.createElementVNode(
                  "text",
                  {
                    onClick: _cache[13] || (_cache[13] = (...args) => $options.clear && $options.clear(...args))
                  },
                  vue.toDisplayString($options.clearText),
                  1
                  /* TEXT */
                ),
                vue.createElementVNode(
                  "text",
                  {
                    class: "confirm-text",
                    onClick: _cache[14] || (_cache[14] = (...args) => $options.confirmRangeChange && $options.confirmRangeChange(...args))
                  },
                  vue.toDisplayString($options.okText),
                  1
                  /* TEXT */
                )
              ])) : vue.createCommentVNode("v-if", true)
            ],
            4
            /* STYLE */
          ))
        ],
        512
        /* NEED_PATCH */
      )), [
        [vue.vShow, $data.pickerVisible]
      ]) : vue.createCommentVNode("v-if", true),
      $data.isPhone ? (vue.openBlock(), vue.createBlock(_component_Calendar, {
        key: 1,
        ref: "mobile",
        clearDate: false,
        date: $data.calendarDate,
        defTime: $options.mobileCalendarTime,
        "start-date": $data.calendarRange.startDate,
        "end-date": $data.calendarRange.endDate,
        selectableTimes: $options.mobSelectableTime,
        startPlaceholder: $props.startPlaceholder,
        endPlaceholder: $props.endPlaceholder,
        "default-value": $props.defaultValue,
        pleStatus: $data.endMultipleStatus,
        showMonth: false,
        range: $data.isRange,
        hasTime: $data.hasTime,
        insert: false,
        hideSecond: $props.hideSecond,
        onConfirm: $options.mobileChange,
        onMaskClose: $options.close,
        onChange: $options.calendarClick
      }, null, 8, ["date", "defTime", "start-date", "end-date", "selectableTimes", "startPlaceholder", "endPlaceholder", "default-value", "pleStatus", "range", "hasTime", "hideSecond", "onConfirm", "onMaskClose", "onChange"])) : vue.createCommentVNode("v-if", true)
    ]);
  }
  const __easycom_0$1 = /* @__PURE__ */ _export_sfc(_sfc_main$y, [["render", _sfc_render$x], ["__scopeId", "data-v-9802168a"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/uni_modules/uni-datetime-picker/components/uni-datetime-picker/uni-datetime-picker.vue"]]);
  const _imports_0$6 = "/static/icons/location.png";
  const _imports_1$3 = "/static/icons/arrow_right.png";
  const _sfc_main$x = {
    data() {
      return {
        scooters: [],
        bannerList: [
          {
            url: "/static/images/banner1.png"
          },
          {
            url: "/static/images/banner2.png"
          },
          {
            url: "/static/images/banner3.png"
          }
        ],
        carImg: "/static/images/scooter.png",
        speedImg: "/static/icons/speed.png",
        batteryImg: "/static/icons/battery.png",
        scooterImg: "/static/icons/scooter.png",
        startTime: "",
        startDate: "",
        selectedDuration: 0,
        durationOptions: [
          {
            label: "1 Hour",
            type: "hour"
          },
          {
            label: "4 Hours",
            type: "FOUR_HOURS"
          },
          {
            label: "1 Day",
            type: "day"
          },
          {
            label: "1 Week",
            type: "week"
          }
        ],
        datetimesingle: Date.now(),
        selectedSite: null,
        cates: [],
        active: 0,
        secondData: []
      };
    },
    watch: {
      datetimesingle(newval) {
        formatAppLog("log", "at pages/index/index.vue:144", "load");
        uni.setStorageSync("datetimesingle", this.datetimesingle);
      }
    },
    mounted() {
      this.loadSelectedSite();
      if (uni.getStorageSync("datetimesingle")) {
        this.datetimesingle = uni.getStorageSync("datetimesingle");
      }
    },
    onLoad() {
      uni.setLocale("en");
    },
    methods: {
      changeLog(e) {
        formatAppLog("log", "at pages/index/index.vue:160", "change:", e);
      },
      loadSelectedSite() {
        const selectedSite = uni.getStorageSync("selectedStore");
        if (selectedSite) {
          this.selectedSite = selectedSite;
        } else {
          this.selectedSite = null;
        }
      },
      onDurationSelect(index) {
        this.selectedDuration = index;
      },
      goToSelectPage(e) {
        uni.navigateTo({
          url: "/pages/chooseStore/chooseStore"
        });
      },
      queryAvailableScooters() {
        const hireType = this.durationOptions[this.selectedDuration].type.toUpperCase();
        let startTimeStr = this.datetimesingle + ":00";
        let startDate = new Date(startTimeStr);
        let endDate;
        switch (hireType) {
          case "HOUR":
            endDate = new Date(startDate.getTime() + 1 * 60 * 60 * 1e3);
            break;
          case "FOUR_HOURS":
            endDate = new Date(startDate.getTime() + 4 * 60 * 60 * 1e3);
            break;
          case "DAY":
            endDate = new Date(startDate.getTime() + 1 * 24 * 60 * 60 * 1e3);
            break;
          case "WEEK":
            endDate = new Date(startDate.getTime() + 7 * 24 * 60 * 60 * 1e3);
            break;
          default:
            endDate = startDate;
        }
        let formattedStartDate = startDate.getFullYear() + "-" + (startDate.getMonth() + 1).toString().padStart(2, "0") + "-" + startDate.getDate().toString().padStart(2, "0") + " " + startDate.getHours().toString().padStart(2, "0") + ":" + startDate.getMinutes().toString().padStart(2, "0") + ":" + startDate.getSeconds().toString().padStart(2, "0");
        let formattedDate = endDate.getFullYear() + "-" + (endDate.getMonth() + 1).toString().padStart(2, "0") + "-" + endDate.getDate().toString().padStart(2, "0") + " " + endDate.getHours().toString().padStart(2, "0") + ":" + endDate.getMinutes().toString().padStart(2, "0") + ":" + endDate.getSeconds().toString().padStart(2, "0");
        uni.setStorageSync("hireType", hireType);
        uni.setStorageSync("startTime", formattedStartDate);
        uni.setStorageSync("endTime", formattedDate);
        if (formattedStartDate && this.selectedSite) {
          if (new Date(formattedStartDate).getTime() > Date.now()) {
            uni.navigateTo({
              url: "/pages/chooseCar/chooseCar"
            });
          } else {
            uni.showToast({
              title: "The start time can not be earlier than current time",
              icon: "none"
            });
          }
        } else {
          uni.showToast({
            title: "Please select both date and store",
            icon: "none"
          });
        }
      },
      instruction(e) {
        uni.navigateTo({
          url: "/pages/information/instruction/instruction"
        });
      }
    }
  };
  function _sfc_render$w(_ctx, _cache, $props, $setup, $data, $options) {
    const _component_uni_datetime_picker = resolveEasycom(vue.resolveDynamicComponent("uni-datetime-picker"), __easycom_0$1);
    const _component_uni_icons = resolveEasycom(vue.resolveDynamicComponent("uni-icons"), __easycom_0$2);
    return vue.openBlock(), vue.createElementBlock("view", { class: "container" }, [
      vue.createElementVNode("swiper", {
        class: "swiper",
        "indicator-dots": "true",
        "indicator-color": "#ccc",
        "indicator-active-color": "red",
        autoplay: "true",
        interval: "3000",
        duration: "500",
        circular: "true"
      }, [
        (vue.openBlock(true), vue.createElementBlock(
          vue.Fragment,
          null,
          vue.renderList($data.bannerList, (item, index) => {
            return vue.openBlock(), vue.createElementBlock("swiper-item", { key: index }, [
              vue.createElementVNode("image", {
                src: item.url,
                "lazy-load": "true",
                alt: "show the scooters"
              }, null, 8, ["src"])
            ]);
          }),
          128
          /* KEYED_FRAGMENT */
        ))
      ]),
      vue.createElementVNode("view", { class: "scooter-list" }, [
        vue.createElementVNode("view", { class: "container-time" }, [
          vue.createElementVNode("view", { class: "example-body" }, [
            vue.createVNode(_component_uni_datetime_picker, {
              type: "datetime",
              "hide-second": true,
              modelValue: $data.datetimesingle,
              "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => $data.datetimesingle = $event),
              onChange: $options.changeLog
            }, null, 8, ["modelValue", "onChange"])
          ]),
          vue.createElementVNode("view", {
            class: "address-section",
            onClick: _cache[1] || (_cache[1] = ($event) => $options.goToSelectPage())
          }, [
            vue.createElementVNode("image", {
              src: _imports_0$6,
              class: "icon",
              alt: "Location icon"
            }),
            vue.createElementVNode("view", { class: "address" }, [
              vue.createElementVNode("text", { class: "title" }, "Pick-up Position"),
              $data.selectedSite ? (vue.openBlock(), vue.createElementBlock(
                "text",
                {
                  key: 0,
                  class: "detail"
                },
                vue.toDisplayString($data.selectedSite.name || "Please choose"),
                1
                /* TEXT */
              )) : vue.createCommentVNode("v-if", true)
            ]),
            vue.createElementVNode("image", {
              src: _imports_1$3,
              class: "arrow",
              alt: "arrow icon"
            })
          ]),
          vue.createCommentVNode(" Reservation duration selector "),
          vue.createElementVNode("view", { class: "duration-picker" }, [
            vue.createElementVNode("text", null, "Select Scheduled Duration:"),
            vue.createElementVNode("view", { class: "duration-options" }, [
              vue.createElementVNode(
                "view",
                {
                  class: vue.normalizeClass(["duration-option", { selected: $data.selectedDuration === 0 }]),
                  onClick: _cache[2] || (_cache[2] = ($event) => $options.onDurationSelect(0))
                },
                " 1 Hour ",
                2
                /* CLASS */
              ),
              vue.createElementVNode(
                "view",
                {
                  class: vue.normalizeClass(["duration-option", { selected: $data.selectedDuration === 1 }]),
                  onClick: _cache[3] || (_cache[3] = ($event) => $options.onDurationSelect(1))
                },
                " 4 Hours ",
                2
                /* CLASS */
              ),
              vue.createElementVNode(
                "view",
                {
                  class: vue.normalizeClass(["duration-option", { selected: $data.selectedDuration === 2 }]),
                  onClick: _cache[4] || (_cache[4] = ($event) => $options.onDurationSelect(2))
                },
                " 1 Day ",
                2
                /* CLASS */
              ),
              vue.createElementVNode(
                "view",
                {
                  class: vue.normalizeClass(["duration-option", { selected: $data.selectedDuration === 3 }]),
                  onClick: _cache[5] || (_cache[5] = ($event) => $options.onDurationSelect(3))
                },
                " 1 Week ",
                2
                /* CLASS */
              )
            ])
          ]),
          vue.createCommentVNode(" Query button "),
          vue.createElementVNode("view", { class: "query-btn" }, [
            vue.createElementVNode("button", {
              onClick: _cache[6] || (_cache[6] = (...args) => $options.queryAvailableScooters && $options.queryAvailableScooters(...args))
            }, "Query")
          ])
        ]),
        vue.createElementVNode("view", {
          class: "instruction",
          onClick: _cache[7] || (_cache[7] = ($event) => $options.instruction())
        }, [
          vue.createElementVNode("view", { class: "text" }, " Instruction To Use "),
          vue.createVNode(_component_uni_icons, {
            type: "right",
            size: "20",
            color: "white",
            class: "arrow"
          })
        ]),
        vue.createElementVNode("view", { class: "discount-container" }, [
          vue.createElementVNode("view", { class: "discount-title" }, "Discount Policies"),
          vue.createElementVNode("view", { class: "table" }, [
            vue.createElementVNode("view", { class: "table-header" }, [
              vue.createElementVNode("view", { class: "header-item" }, "Discount Type"),
              vue.createElementVNode("view", { class: "header-item" }, "Details")
            ]),
            vue.createElementVNode("view", { class: "table-row" }, [
              vue.createElementVNode("view", { class: "row-item" }, "Student (15%)"),
              vue.createElementVNode("view", { class: "row-item" }, " For users aged 18-25 ")
            ]),
            vue.createElementVNode("view", { class: "table-row" }, [
              vue.createElementVNode("view", { class: "row-item" }, "Senior Citizen (20%)"),
              vue.createElementVNode("view", { class: "row-item" }, " For users aged 60+ ")
            ]),
            vue.createElementVNode("view", { class: "table-row" }, [
              vue.createElementVNode("view", { class: "row-item" }, "Frequent User (10%)"),
              vue.createElementVNode("view", { class: "row-item" }, " For users renting scooters 8+ hours/week ")
            ]),
            vue.createElementVNode("view", { class: "note" }, " *Discount Stacking: Multiple discounts can be combined ")
          ])
        ])
      ])
    ]);
  }
  const PagesIndexIndex = /* @__PURE__ */ _export_sfc(_sfc_main$x, [["render", _sfc_render$w], ["__scopeId", "data-v-1cf27b2a"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/index/index.vue"]]);
  const _sfc_main$w = {
    data() {
      return {
        stores: [],
        userLocation: {
          latitude: 30.7656,
          longitude: 103.9799
        },
        recommendedLocations: [],
        selectedLocation: "near stores",
        selectedStore: null,
        locationNames: [],
        order: null,
        picUrl: "../../../static/images/car.jpg",
        currentTime: /* @__PURE__ */ new Date(),
        endTime: /* @__PURE__ */ new Date(),
        hasOngoingOrder: false
      };
    },
    async onLoad() {
    },
    computed: {
      isOvertime() {
        return this.currentTime > this.endTime;
      }
    },
    methods: {
      load() {
        var _a;
        formatAppLog("log", "at pages/now/now.vue:79", "Load method is called");
        this.loadScooters();
        if ((_a = this.order.scooter) == null ? void 0 : _a.endTime) {
          this.endTime = new Date(this.order.scooter.endTime);
        }
      },
      async loadScooters() {
        const user = uni.getStorageSync("userInfo");
        const token = String(uni.getStorageSync("token"));
        try {
          const res = await uni.request({
            url: `${this.$baseURL}/api/bookings/getAllOngoing/${user.userId}`,
            method: "GET",
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          if (res.statusCode === 200) {
            if (res.data.length > 0) {
              this.order = res.data[0];
            } else {
              this.order = null;
            }
            if (this.order) {
              this.hasOngoingOrder = true;
              const store = this.order.scooter.store;
              this.stores = [{
                ...store,
                iconPath: "/static/icons/available_scooter.png",
                width: 40,
                height: 40
              }];
            } else {
              this.hasOngoingOrder = false;
            }
          } else {
            uni.showToast({
              title: "Data loading failed",
              icon: "none"
            });
          }
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        }
      },
      appointment() {
        uni.navigateTo({
          url: "/pages/index"
        });
      },
      handleMarkerTap(e) {
        const markerId = e.detail.markerId;
        const store = this.stores.find((s) => s.id === markerId);
        this.selectedStore = store;
      },
      async getUserLocation() {
        try {
          const res = await uni.getLocation({
            type: "gcj02"
          });
          this.userLocation = res;
          this.mapCenter = res;
        } catch (err) {
          formatAppLog("error", "at pages/now/now.vue:149", "Failed to obtain the positioning:", err);
        }
      },
      formatTime(time) {
        const date = new Date(time);
        return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, "0")}-${date.getDate().toString().padStart(2, "0")} ${date.getHours().toString().padStart(2, "0")}:${date.getMinutes().toString().padStart(2, "0")}`;
      },
      async returnCar() {
        uni.getStorageSync("userInfo");
        const token = String(uni.getStorageSync("token"));
        formatAppLog("log", "at pages/now/now.vue:161", "order", this.order);
        try {
          const res = await uni.request({
            url: `${this.$baseURL}/api/bookings/return`,
            method: "POST",
            data: {
              orderId: this.order.id
            },
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          formatAppLog("log", "at pages/now/now.vue:174", "res", res.data);
          if (res.statusCode === 200) {
            uni.showToast({
              title: res.data.message,
              icon: "none"
            });
            this.load();
          } else {
            uni.showToast({
              title: res.data.message || "Update failed",
              icon: "none"
            });
          }
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        }
      }
    }
  };
  function _sfc_render$v(_ctx, _cache, $props, $setup, $data, $options) {
    var _a, _b;
    const _component_uni_icons = resolveEasycom(vue.resolveDynamicComponent("uni-icons"), __easycom_0$2);
    return vue.openBlock(), vue.createElementBlock("view", { class: "container" }, [
      vue.createElementVNode("map", {
        id: "map",
        latitude: $data.userLocation.latitude,
        longitude: $data.userLocation.longitude,
        markers: $data.stores,
        "show-location": true,
        style: { "width": "100%", "height": "70vh" },
        onMarkertap: _cache[0] || (_cache[0] = (...args) => $options.handleMarkerTap && $options.handleMarkerTap(...args))
      }, null, 40, ["latitude", "longitude", "markers"]),
      vue.createElementVNode("view", { class: "bottom" }, [
        vue.withDirectives(vue.createElementVNode(
          "view",
          { class: "car-card" },
          [
            vue.createElementVNode("view", null, [
              vue.createElementVNode("view", { class: "hint" }, "There are no vehicles in use yet."),
              vue.createElementVNode("view", {
                class: "choose-btn-appointment",
                onClick: _cache[1] || (_cache[1] = (...args) => $options.appointment && $options.appointment(...args))
              }, "Make An Appointment")
            ])
          ],
          512
          /* NEED_PATCH */
        ), [
          [vue.vShow, !$data.hasOngoingOrder]
        ]),
        vue.withDirectives(vue.createElementVNode(
          "view",
          { class: "card-content" },
          [
            $data.order ? (vue.openBlock(), vue.createElementBlock("view", {
              key: 0,
              class: "order-wrapper"
            }, [
              vue.createElementVNode("view", { class: "item-left" }, [
                vue.createElementVNode("image", {
                  src: $data.picUrl,
                  class: "order-img",
                  mode: "aspectFill"
                }, null, 8, ["src"])
              ]),
              vue.createElementVNode("view", { class: "item-right" }, [
                vue.createElementVNode("view", { class: "car-info" }, [
                  vue.createElementVNode(
                    "view",
                    { class: "car-battery" },
                    " 🔋" + vue.toDisplayString(((_a = $data.order.scooter) == null ? void 0 : _a.battery) || 0) + "% ",
                    1
                    /* TEXT */
                  ),
                  vue.createElementVNode(
                    "view",
                    { class: "car-speed" },
                    " ⚡ " + vue.toDisplayString(((_b = $data.order.scooter) == null ? void 0 : _b.speed) || 0) + " km/h ",
                    1
                    /* TEXT */
                  )
                ]),
                vue.createElementVNode("view", { class: "time-info" }, [
                  vue.createElementVNode("view", { class: "start-time" }, [
                    vue.createVNode(_component_uni_icons, {
                      type: "calendar",
                      size: "18",
                      color: "#666",
                      style: { "margin-right": "8rpx" }
                    }),
                    vue.createTextVNode(
                      " Start Time: " + vue.toDisplayString($options.formatTime($data.order.startTime)),
                      1
                      /* TEXT */
                    )
                  ]),
                  vue.createElementVNode("view", { class: "end-time" }, [
                    vue.createVNode(_component_uni_icons, {
                      type: "calendar",
                      size: "18",
                      color: "#666",
                      style: { "margin-right": "8rpx" }
                    }),
                    vue.createTextVNode(
                      " End Time: " + vue.toDisplayString($options.formatTime($data.order.endTime)),
                      1
                      /* TEXT */
                    )
                  ])
                ]),
                vue.createElementVNode(
                  "view",
                  {
                    class: vue.normalizeClass(["overtime-info", { visible: $options.isOvertime }])
                  },
                  "⛔ Overdue return",
                  2
                  /* CLASS */
                ),
                vue.createElementVNode("view", {
                  class: "choose-btn",
                  onClick: _cache[2] || (_cache[2] = ($event) => $options.returnCar())
                }, "Return")
              ])
            ])) : vue.createCommentVNode("v-if", true)
          ],
          512
          /* NEED_PATCH */
        ), [
          [vue.vShow, $data.hasOngoingOrder]
        ])
      ])
    ]);
  }
  const PagesNowNow = /* @__PURE__ */ _export_sfc(_sfc_main$w, [["render", _sfc_render$v], ["__scopeId", "data-v-37a58fb7"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/now/now.vue"]]);
  const _sfc_main$v = {
    data() {
      return {
        username: "",
        userid: null,
        userLogo: "/static/icons/user-logo.png",
        myOrderImg: "/static/icons/myOrder.png",
        unpaidImg: "/static/icons/unpay.png",
        unuseImg: "/static/icons/unuse.png",
        doneImg: "/static/icons/done.png",
        doingImg: "/static/icons/doing.png",
        paymentImg: "/static/icons/payment.png",
        setImg: "/static/icons/setup.png",
        myFeedbackImg: "/static/icons/feedback.png",
        logoutImg: "/static/icons/logout.png",
        vipImg: "/static/icons/name.png",
        vipLogo: "/static/icons/VIP.png",
        userInfo: uni.getStorageSync("userInfo"),
        balance: 0,
        totalOrders: 0,
        status: 0,
        // isLoading: true,
        user: []
      };
    },
    computed: {
      statusClass() {
        switch (this.status) {
          case 1:
            return "Student";
          case 2:
            return "Senior";
          case 3:
            return "Frequent";
          case 4:
            return "Frequent_student";
          case 5:
            return "Frequent_senior";
          default:
            return "Ordinary User";
        }
      }
    },
    methods: {
      load: function(e) {
      },
      chooseImg() {
        uni.chooseImage({
          count: 1,
          sourceType: ["album"],
          sizeType: ["original", "compressed"],
          success: async (res) => {
            const filePath = res.tempFilePaths[0];
            this.avatarUpload(filePath);
          }
        });
      },
      avatarUpload(filePath) {
        const token = String(uni.getStorageSync("token"));
        uni.showLoading({
          title: "Uploading...",
          mask: true
        });
        uni.uploadFile({
          url: `${this.$baseURL}/api/users/avatar/${this.userid}`,
          filePath,
          name: "file",
          header: {
            "Authorization": `Bearer ${token}`
          },
          success: (res) => {
            try {
              if (res.statusCode === 200) {
                uni.showToast({
                  title: "Upload success",
                  icon: "none"
                });
                this.getprofile();
              } else {
                uni.showToast({
                  title: res.data.message || "Upload failed",
                  icon: "none"
                });
              }
            } catch (e) {
              uni.showToast({
                title: "The server responded abnormally",
                icon: "none"
              });
            }
          },
          fail: (err) => {
            formatAppLog("error", "at pages/UserCenter/UserCenter.vue:190", "Upload failed:", err);
            uni.showToast({
              title: "Network Error",
              icon: "none"
            });
          },
          complete: () => {
            uni.hideLoading();
          }
        });
      },
      open() {
        this.$refs.popup.open();
      },
      async getprofile() {
        this.user = uni.getStorageSync("userInfo");
        if (this.user) {
          this.username = this.user.username;
          this.userid = this.user.userId;
          this.getuser();
        } else {
          this.username = "Unknown User";
        }
        const token = String(uni.getStorageSync("token"));
        try {
          const res = await uni.request({
            url: `${this.$baseURL}/api/users/profile/${this.userid}`,
            method: "GET",
            header: {
              "Authorization": `Bearer ${token}`,
              "Content-Type": "application/json"
            }
          });
          if (res.statusCode === 200) {
            this.userLogo = res.data.avatar;
          } else {
            uni.showToast({
              title: res.data.message || "Data Loading Failed",
              icon: "none",
              duration: 2e3
            });
          }
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        } finally {
        }
      },
      async getuser() {
        const token = String(uni.getStorageSync("token"));
        try {
          const res = await uni.request({
            url: `${this.$baseURL}/api/users/${this.userid}`,
            method: "PUT",
            data: {
              email: this.user.email,
              mobile: this.user.mobile,
              birthDate: this.user.birthDate
            },
            header: {
              "Authorization": `Bearer ${token}`,
              "Content-Type": "application/json"
            }
          });
          if (res.statusCode === 200) {
            if (res.data.isStudent == 1) {
              this.status = 1;
            } else if (res.data.isSenior == 1) {
              this.status = 2;
            } else if (res.data.isFrequentUser == 1) {
              this.status = 3;
            } else if (res.data.isStudent == 1 && res.data.isFrequentUser == 1) {
              this.status = 4;
            } else if (res.data.isSenior == 1 && res.data.isFrequentUser == 1) {
              this.status = 5;
            }
          } else {
            uni.showToast({
              title: res.data.message || "Data Loading Failed",
              icon: "none",
              duration: 2e3
            });
          }
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        } finally {
          uni.hideLoading();
        }
      },
      async getCard() {
        const token = String(uni.getStorageSync("token"));
        this.user = uni.getStorageSync("userInfo");
        try {
          const res = await uni.request({
            url: `${this.$baseURL}/api/bank-payment/check-card/${this.user.userId}`,
            method: "GET",
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          this.balance = res.data.bankBalance;
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        } finally {
          uni.hideLoading();
        }
      },
      login() {
        uni.reLaunch({
          url: "/pages/UserLogin/UserLogin"
        });
      },
      logout() {
        uni.removeStorageSync("userInfo");
        uni.removeStorageSync("token");
        uni.reLaunch({
          url: "/pages/UserLogin/UserLogin"
        });
      },
      async getOrders() {
        const token = String(uni.getStorageSync("token"));
        try {
          const res = await uni.request({
            url: `${this.$baseURL}/api/bookings/getAllFinished/${this.userid}`,
            method: "GET",
            header: {
              "Authorization": `Bearer ${token}`,
              "Content-Type": "application/json"
            }
          });
          if (res.statusCode === 200) {
            if (Array.isArray(res.data)) {
              this.totalOrders = res.data.length;
            } else {
              this.totalOrders = 0;
            }
          } else {
            uni.showToast({
              title: res.data.message || "Data Loading Failed",
              icon: "none",
              duration: 2e3
            });
          }
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        }
      },
      goPage(e) {
        switch (e) {
          case 1:
            uni.navigateTo({
              url: "./myorder/orderlist/orderlist"
            });
            break;
          case 2:
            uni.navigateTo({
              url: "./myorder/doing/doing"
            });
            break;
          case 3:
            uni.navigateTo({
              url: "./myorder/unuse/unuse"
            });
            break;
          case 4:
            uni.navigateTo({
              url: "./myorder/done/done"
            });
            break;
          case 5:
            uni.navigateTo({
              url: "./payment/card/card"
            });
            break;
          case 7:
            uni.navigateTo({
              url: "./feedback/feedbackIndex/feedbackIndex"
            });
            break;
          case 9:
            uni.navigateTo({
              url: "./information/set/set"
            });
            break;
          case 13:
            that.logout();
            break;
        }
      }
    }
  };
  function _sfc_render$u(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "page" }, [
      vue.createElementVNode("view", {
        style: vue.normalizeStyle([{ "background-size": "100% 100%" }, { "padding-top": "10px" }])
      }, [
        vue.createElementVNode("view", null, [
          vue.createElementVNode("view", { class: "my-info-box" }, [
            vue.createElementVNode("view", { class: "left" }, [
              vue.createElementVNode("view", { class: "my-logo-box" }, [
                vue.createElementVNode("view", null, [
                  vue.createElementVNode("image", {
                    class: "user-logo",
                    src: $data.userLogo,
                    onClick: _cache[0] || (_cache[0] = (...args) => $options.chooseImg && $options.chooseImg(...args)),
                    mode: "aspectFill"
                  }, null, 8, ["src"]),
                  $data.status > 0 ? (vue.openBlock(), vue.createElementBlock("image", {
                    key: 0,
                    class: "user-vip",
                    src: $data.vipLogo
                  }, null, 8, ["src"])) : vue.createCommentVNode("v-if", true)
                ])
              ]),
              vue.createElementVNode(
                "view",
                {
                  onClick: _cache[1] || (_cache[1] = ($event) => $options.login()),
                  class: "my-nickName"
                },
                vue.toDisplayString($data.userInfo.username || "Click Login"),
                1
                /* TEXT */
              )
            ]),
            vue.createElementVNode("view", { class: "right" }, [
              vue.createElementVNode("view", { class: "my-vip-box" }, [
                vue.createElementVNode("image", {
                  src: $data.vipImg,
                  mode: "widthFix"
                }, null, 8, ["src"]),
                vue.createElementVNode(
                  "view",
                  {
                    class: vue.normalizeClass($options.statusClass)
                  },
                  vue.toDisplayString($options.statusClass),
                  3
                  /* TEXT, CLASS */
                )
              ]),
              vue.createElementVNode("view", { class: "my-amount-box" }, [
                vue.createElementVNode("view", { class: "amount" }, [
                  vue.createElementVNode("text", null, "Balance"),
                  vue.createElementVNode(
                    "view",
                    null,
                    vue.toDisplayString($data.balance),
                    1
                    /* TEXT */
                  )
                ]),
                vue.createElementVNode("view", { class: "amount" }, [
                  vue.createElementVNode("text", null, "Orders"),
                  vue.createElementVNode(
                    "view",
                    null,
                    vue.toDisplayString($data.totalOrders),
                    1
                    /* TEXT */
                  )
                ])
              ])
            ])
          ])
        ])
      ]),
      vue.createElementVNode("view", { class: "myPage-listTable" }, [
        vue.createElementVNode("view", { class: "myPage-listTable-box" }, [
          vue.createElementVNode("view", {
            onClick: _cache[2] || (_cache[2] = ($event) => $options.goPage(1))
          }, [
            vue.createElementVNode("view", null, [
              vue.createElementVNode("image", {
                mode: "widthFix",
                src: $data.myOrderImg
              }, null, 8, ["src"])
            ]),
            vue.createElementVNode("view", { class: "myPage-listTable-txtA" }, "All Order")
          ]),
          vue.createElementVNode("view", {
            onClick: _cache[3] || (_cache[3] = ($event) => $options.goPage(2))
          }, [
            vue.createElementVNode("view", null, [
              vue.createElementVNode("image", {
                mode: "widthFix",
                src: $data.doingImg
              }, null, 8, ["src"])
            ]),
            vue.createElementVNode("view", { class: "myPage-listTable-txtA" }, "Doing")
          ]),
          vue.createElementVNode("view", {
            onClick: _cache[4] || (_cache[4] = ($event) => $options.goPage(3))
          }, [
            vue.createElementVNode("view", null, [
              vue.createElementVNode("image", {
                mode: "widthFix",
                src: $data.unuseImg
              }, null, 8, ["src"])
            ]),
            vue.createElementVNode("view", { class: "myPage-listTable-txtA" }, "Unuse")
          ]),
          vue.createElementVNode("view", {
            onClick: _cache[5] || (_cache[5] = ($event) => $options.goPage(4))
          }, [
            vue.createElementVNode("view", null, [
              vue.createElementVNode("image", {
                mode: "widthFix",
                src: $data.doneImg
              }, null, 8, ["src"])
            ]),
            vue.createElementVNode("view", { class: "myPage-listTable-txtA" }, "Done")
          ])
        ]),
        vue.createElementVNode("view", { class: "my-more_box" }, [
          vue.createElementVNode("view", { class: "myPage-listTable-title" }, "More Services"),
          vue.createElementVNode("view", { class: "myPage-listTable-box" }, [
            vue.createElementVNode("view", {
              onClick: _cache[6] || (_cache[6] = ($event) => $options.goPage(5))
            }, [
              vue.createElementVNode("view", { class: "s" }, [
                vue.createElementVNode("image", {
                  mode: "widthFix",
                  src: $data.paymentImg
                }, null, 8, ["src"])
              ]),
              vue.createElementVNode("view", { class: "myPage-listTable-txtB" }, "Payment")
            ]),
            vue.createElementVNode("view", {
              onClick: _cache[7] || (_cache[7] = ($event) => $options.goPage(7))
            }, [
              vue.createElementVNode("view", { class: "s" }, [
                vue.createElementVNode("image", {
                  mode: "widthFix",
                  src: $data.myFeedbackImg
                }, null, 8, ["src"])
              ]),
              vue.createElementVNode("view", { class: "myPage-listTable-txtB" }, "Feedback")
            ]),
            vue.createElementVNode("view", {
              onClick: _cache[8] || (_cache[8] = ($event) => $options.goPage(9))
            }, [
              vue.createElementVNode("view", { class: "s" }, [
                vue.createElementVNode("image", {
                  mode: "widthFix",
                  src: $data.setImg
                }, null, 8, ["src"])
              ]),
              vue.createElementVNode("view", { class: "myPage-listTable-txtB" }, "Set")
            ]),
            vue.createElementVNode("view", {
              onClick: _cache[9] || (_cache[9] = ($event) => $options.logout())
            }, [
              vue.createElementVNode("view", { class: "s" }, [
                vue.createElementVNode("image", {
                  mode: "widthFix",
                  src: $data.logoutImg
                }, null, 8, ["src"])
              ]),
              vue.createElementVNode("view", { class: "myPage-listTable-txtB" }, "Logout")
            ])
          ])
        ])
      ])
    ]);
  }
  const PagesUserCenterUserCenter = /* @__PURE__ */ _export_sfc(_sfc_main$v, [["render", _sfc_render$u], ["__scopeId", "data-v-4d518d9c"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/UserCenter/UserCenter.vue"]]);
  const _imports_0$5 = "/static/icons/current_location.png";
  const _sfc_main$u = {
    data() {
      return {
        stores: [],
        userLocation: {
          latitude: 30.7656,
          longitude: 103.9799
        },
        mapCenter: {
          latitude: 30.7656,
          longitude: 103.9799
        },
        showDialog: false,
        recommendedLocations: [],
        selectedLocation: "Recommended Stores",
        showSelectedStore: false,
        selectedStore: null,
        locationNames: []
      };
    },
    async mounted() {
      await this.loadScooters();
      await this.getUserLocation();
      await this.recommendStore();
      for (const store of this.stores) {
        await this.reverseGeocode(store);
      }
      this.extractLocationNames();
    },
    methods: {
      async loadScooters() {
        try {
          const token = String(uni.getStorageSync("token"));
          const res = await uni.request({
            url: `${this.$baseURL}/api/stores/getAll`,
            method: "GET",
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          if (res.statusCode === 200) {
            this.stores = res.data.map((store) => ({
              ...store,
              iconPath: "/static/icons/available_scooter.png",
              width: 20,
              height: 20
            }));
          } else {
            uni.showToast({
              title: "Data Loading Failed",
              icon: "none"
            });
          }
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        }
      },
      open() {
        this.showDialog = true;
      },
      // Get the user's location
      async getUserLocation() {
        try {
          const res = await uni.getLocation({
            type: "gcj02"
          });
          this.userLocation = res;
          this.mapCenter = res;
        } catch (err) {
          formatAppLog("error", "at pages/map/map.vue:105", "Failed to obtain the positioning:", err);
        }
      },
      // Click on the map marker to display the address information
      handleMarkerTap(e) {
        this.showDialog = false;
        this.stores = this.stores.map((marker) => ({
          ...marker,
          iconPath: "/static/icons/available_scooter.png"
        }));
        const markerId = e.detail.markerId;
        const store = this.stores.find((s) => s.id === markerId);
        this.selectedStore = store;
        formatAppLog("log", "at pages/map/map.vue:119", "this.selectedStore handleMarkerTap", this.selectedStore);
        this.selectedStore.iconPath = "/static/icons/choose.png";
        this.mapCenter = {
          latitude: this.selectedStore.latitude,
          longitude: this.selectedStore.longitude
        };
        this.showSelectedStore = true;
      },
      selectOneLocation(item, index) {
        this.stores = this.stores.map((marker) => ({
          ...marker,
          iconPath: "/static/icons/available_scooter.png"
        }));
        const selected = this.recommendedLocations[index];
        const store = this.stores.find((s) => s.id === selected.store.id);
        this.selectedStore = store;
        formatAppLog("log", "at pages/map/map.vue:136", "this.selectedStore selectOneLocation", this.selectedStore);
        store.iconPath = "/static/icons/choose.png";
        this.showDialog = false;
        this.selectedLocation = selected.store.name;
        this.showSelectedStore = true;
        this.mapCenter = {
          latitude: selected.store.latitude,
          longitude: selected.store.longitude
        };
      },
      // Return to the current position of the user
      centerToUser() {
        if (this.userLocation) {
          this.mapCenter = this.userLocation;
        }
      },
      async reverseGeocode(store) {
        var _a;
        try {
          const res = await new Promise((resolve, reject) => {
            uni.request({
              url: `https://restapi.amap.com/v3/geocode/regeo?output=json&location=${store.longitude},${store.latitude}&key=5f722ef9e435cec7d4dba6f5daba0030&radius=1000&extensions=all&language=en`,
              success: resolve,
              fail: reject
            });
          });
          if (res.data.status === "1") {
            this.$set(store, "locationName", ((_a = res.data.regeocode.pois[1]) == null ? void 0 : _a.name) || "No POI found");
            this.$set(store, "locationNum", res.data.regeocode.formatted_address);
          } else {
            this.$set(store, "locationName", "Unable to obtain the location");
          }
        } catch (err) {
          this.$set(store, "locationName", "Location request failed");
          formatAppLog("error", "at pages/map/map.vue:171", "The reverse geocoding request failed:", err);
        }
      },
      chooseStore() {
        uni.setStorageSync("selectedStore", this.selectedStore);
        uni.navigateTo({
          url: "/pages/index"
        });
      },
      async recommendStore() {
        try {
          const token = String(uni.getStorageSync("token"));
          const res = await uni.request({
            url: `${this.$baseURL}/api/stores/nearby?longitude=${this.userLocation.longitude}&latitude=${this.userLocation.latitude}`,
            method: "GET",
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          if (res.statusCode === 200) {
            this.recommendedLocations = res.data;
          } else {
            uni.showToast({
              title: "Data Loading Failed",
              icon: "none"
            });
          }
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        }
      },
      extractLocationNames() {
        this.locationNames = this.recommendedLocations.map((item) => {
          return `${item.store.name} : ${item.distance.toFixed(2)} km`;
        });
      }
    }
  };
  function _sfc_render$t(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "canvas-wrap" }, [
      vue.createElementVNode("map", {
        id: "map",
        latitude: $data.mapCenter.latitude,
        longitude: $data.mapCenter.longitude,
        markers: $data.stores,
        "show-location": true,
        style: { "width": "100%", "height": "90vh", "z-index": "10" },
        onMarkertap: _cache[3] || (_cache[3] = (...args) => $options.handleMarkerTap && $options.handleMarkerTap(...args))
      }, [
        vue.createElementVNode("cover-view", { class: "custom-controls" }, [
          vue.createElementVNode("cover-image", {
            src: _imports_0$5,
            onClick: _cache[0] || (_cache[0] = (...args) => $options.centerToUser && $options.centerToUser(...args)),
            class: "control-btn"
          })
        ]),
        vue.createElementVNode("cover-view", {
          class: "picker-btn",
          onClick: _cache[1] || (_cache[1] = (...args) => $options.open && $options.open(...args))
        }, "Recommended Stores"),
        vue.withDirectives(vue.createElementVNode(
          "cover-view",
          { class: "dialog-seletion" },
          [
            vue.createElementVNode("cover-view", { class: "selection-item" }, "Recommended Stores:"),
            (vue.openBlock(true), vue.createElementBlock(
              vue.Fragment,
              null,
              vue.renderList($data.locationNames, (item, index) => {
                return vue.openBlock(), vue.createElementBlock("cover-view", {
                  class: "selection-item",
                  onClick: ($event) => $options.selectOneLocation(item, index)
                }, vue.toDisplayString(item), 9, ["onClick"]);
              }),
              256
              /* UNKEYED_FRAGMENT */
            ))
          ],
          512
          /* NEED_PATCH */
        ), [
          [vue.vShow, $data.showDialog]
        ]),
        vue.createCommentVNode(" Address information card "),
        $data.showSelectedStore ? (vue.openBlock(), vue.createElementBlock("cover-view", {
          key: 0,
          class: "address-card"
        }, [
          vue.createElementVNode("cover-view", { class: "card-content" }, [
            vue.createElementVNode(
              "cover-view",
              { class: "store-name" },
              vue.toDisplayString($data.selectedStore ? $data.selectedStore.name : ""),
              1
              /* TEXT */
            ),
            vue.createElementVNode(
              "cover-view",
              { class: "store-location" },
              vue.toDisplayString($data.selectedStore ? $data.selectedStore.locationName : ""),
              1
              /* TEXT */
            ),
            vue.createElementVNode("cover-view", {
              class: "choose-btn",
              onClick: _cache[2] || (_cache[2] = (...args) => $options.chooseStore && $options.chooseStore(...args))
            }, "Choose")
          ])
        ])) : vue.createCommentVNode("v-if", true)
      ], 40, ["latitude", "longitude", "markers"])
    ]);
  }
  const PagesMapMap = /* @__PURE__ */ _export_sfc(_sfc_main$u, [["render", _sfc_render$t], ["__scopeId", "data-v-e06b858f"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/map/map.vue"]]);
  const _sfc_main$t = {
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
        if (this.nowBar !== index) {
          this.nowBar = index;
          this.$emit("nowBar", index);
        }
      },
      getTabPaddingBottom() {
        let padding = uni.getStorageSync("nowTabPaddingBottom");
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
  function _sfc_render$s(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock(
      "view",
      {
        class: "tabbar-box ipx-footer-pd",
        style: vue.normalizeStyle({ height: $data.nowTabPaddingBottom })
      },
      [
        vue.createCommentVNode(" 首页 "),
        vue.createElementVNode("view", {
          class: "tabbar-item",
          onClick: _cache[0] || (_cache[0] = ($event) => $options.tabBar(0))
        }, [
          vue.createElementVNode("image", {
            src: $data.nowBar === 0 ? $data.activeIcons.home : $data.defaultIcons.home
          }, null, 8, ["src"]),
          vue.createElementVNode(
            "view",
            {
              class: vue.normalizeClass({ "tabbar-active": $data.nowBar === 0 })
            },
            "Home",
            2
            /* CLASS */
          )
        ]),
        vue.createCommentVNode(" 地图 "),
        vue.createElementVNode("view", {
          class: "tabbar-item",
          onClick: _cache[1] || (_cache[1] = ($event) => $options.tabBar(1))
        }, [
          vue.createElementVNode("image", {
            src: $data.nowBar === 1 ? $data.activeIcons.map : $data.defaultIcons.map
          }, null, 8, ["src"]),
          vue.createElementVNode(
            "view",
            {
              class: vue.normalizeClass({ "tabbar-active": $data.nowBar === 1 })
            },
            "Map",
            2
            /* CLASS */
          )
        ]),
        vue.createCommentVNode(" 发现 "),
        vue.createElementVNode("view", {
          class: "tabbar-item",
          onClick: _cache[2] || (_cache[2] = ($event) => $options.tabBar(3))
        }, [
          vue.createElementVNode("image", {
            src: $data.nowBar === 3 ? $data.activeIcons.found : $data.defaultIcons.found
          }, null, 8, ["src"]),
          vue.createElementVNode(
            "view",
            {
              class: vue.normalizeClass({ "tabbar-active": $data.nowBar === 3 })
            },
            "Now",
            2
            /* CLASS */
          )
        ]),
        vue.createCommentVNode(" 我的 "),
        vue.createElementVNode("view", {
          class: "tabbar-item",
          onClick: _cache[3] || (_cache[3] = ($event) => $options.tabBar(4))
        }, [
          vue.createElementVNode("image", {
            src: $data.nowBar === 4 ? $data.activeIcons.my : $data.defaultIcons.my
          }, null, 8, ["src"]),
          vue.createElementVNode(
            "view",
            {
              class: vue.normalizeClass({ "tabbar-active": $data.nowBar === 4 })
            },
            "My",
            2
            /* CLASS */
          )
        ])
      ],
      4
      /* STYLE */
    );
  }
  const tabBar = /* @__PURE__ */ _export_sfc(_sfc_main$t, [["render", _sfc_render$s], ["__scopeId", "data-v-22586f07"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/components/tabBar.vue"]]);
  const _sfc_main$s = {
    components: {
      indexPage: PagesIndexIndex,
      nowPgae: PagesNowNow,
      myPage: PagesUserCenterUserCenter,
      mapPage: PagesMapMap,
      tabBar
    },
    data() {
      return {
        nowBarIndex: 0
      };
    },
    onShow() {
      setTimeout(() => {
        if (this.$refs["indexPage"]) {
          this.$refs["indexPage"].loadSelectedSite();
        }
        if (this.$refs["myPage"]) {
          this.$refs["myPage"].getprofile();
          this.$refs["myPage"].getOrders();
          this.$refs["myPage"].getCard();
        }
      }, 300);
    },
    methods: {
      nowBar: function(e) {
        if (e == this.nowBarIndex) {
          return;
        }
        this.nowBarIndex = e;
        this.tabPage(e);
      },
      tabPage: function(e) {
        this.$nextTick(() => {
          switch (Number(e)) {
            case 0:
              this.$refs.indexPage.load(1);
              break;
            case 1:
              this.$refs.mapPage.load();
              break;
            case 3:
              this.$refs.nowPgae.load();
              break;
            case 4:
              this.$refs.myPage.load();
              break;
          }
        });
      },
      stopDown: function() {
        uni.stopPullDownRefresh();
      }
    }
  };
  function _sfc_render$r(_ctx, _cache, $props, $setup, $data, $options) {
    const _component_indexPage = vue.resolveComponent("indexPage");
    const _component_mapPage = vue.resolveComponent("mapPage");
    const _component_nowPgae = vue.resolveComponent("nowPgae");
    const _component_myPage = vue.resolveComponent("myPage");
    const _component_tabBar = vue.resolveComponent("tabBar");
    return vue.openBlock(), vue.createElementBlock("view", { class: "page-container" }, [
      vue.createElementVNode("view", { class: "content" }, [
        vue.withDirectives(vue.createElementVNode(
          "view",
          null,
          [
            vue.createVNode(
              _component_indexPage,
              { ref: "indexPage" },
              null,
              512
              /* NEED_PATCH */
            )
          ],
          512
          /* NEED_PATCH */
        ), [
          [vue.vShow, $data.nowBarIndex === 0]
        ]),
        vue.withDirectives(vue.createElementVNode(
          "view",
          null,
          [
            vue.createVNode(
              _component_mapPage,
              { ref: "mapPage" },
              null,
              512
              /* NEED_PATCH */
            )
          ],
          512
          /* NEED_PATCH */
        ), [
          [vue.vShow, $data.nowBarIndex === 1]
        ]),
        vue.withDirectives(vue.createElementVNode(
          "view",
          null,
          [
            vue.createVNode(
              _component_nowPgae,
              { ref: "nowPgae" },
              null,
              512
              /* NEED_PATCH */
            )
          ],
          512
          /* NEED_PATCH */
        ), [
          [vue.vShow, $data.nowBarIndex === 3]
        ]),
        vue.withDirectives(vue.createElementVNode(
          "view",
          null,
          [
            vue.createVNode(
              _component_myPage,
              { ref: "myPage" },
              null,
              512
              /* NEED_PATCH */
            )
          ],
          512
          /* NEED_PATCH */
        ), [
          [vue.vShow, $data.nowBarIndex === 4]
        ])
      ]),
      vue.createVNode(_component_tabBar, { onNowBar: $options.nowBar }, null, 8, ["onNowBar"])
    ]);
  }
  const PagesIndex = /* @__PURE__ */ _export_sfc(_sfc_main$s, [["render", _sfc_render$r], ["__scopeId", "data-v-02281a80"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/index.vue"]]);
  const _sfc_main$r = {
    data() {
      return {
        username: "",
        password: "",
        captcha: "",
        captchaKey: "",
        captchaImage: "",
        token: null,
        errorMessage: "",
        agreementChecked: false,
        placeholderStyle: "color: #2c3e50; font-size: 14px;"
      };
    },
    onLoad() {
      this.getCaptcha();
    },
    onPullDownRefresh() {
      uni.stopPullDownRefresh();
    },
    methods: {
      async getCaptcha() {
        try {
          const [err, res] = await uni.request({
            url: `${this.$baseURL}/api/auth/captcha`,
            method: "GET",
            header: {
              "Content-Type": "application/json"
            }
          }).then((res2) => [null, res2]).catch((err2) => [err2, null]);
          if (err || res.statusCode !== 200) {
            uni.showToast({
              title: "Failed to get captcha",
              icon: "none",
              duration: 2e3
            });
            return;
          }
          this.captchaKey = res.data.captchaKey;
          this.captchaImage = res.data.captchaImageBase64;
        } catch (error) {
          formatAppLog("error", "at pages/UserLogin/UserLogin.vue:90", "Error getting captcha:", error);
        }
      },
      // Refresh the verification code
      refreshCaptcha() {
        this.captchaImage = "";
        this.captcha = "";
        this.getCaptcha();
      },
      handleSubmit() {
        if (!this.username) {
          this.toast("Please enter your username");
          return;
        }
        if (!this.password) {
          this.toast("Please enter your password");
          return;
        }
        if (!this.captcha) {
          this.toast("Please enter captcha");
          return;
        }
        if (!this.agreementChecked) {
          this.toast("Please agree first");
          return;
        }
        this.loginWithPassword();
      },
      toast(message) {
        uni.showToast({
          title: message,
          icon: "none",
          duration: 2e3
        });
      },
      checkboxChange(e) {
        this.agreementChecked = e.detail.value.includes("cb");
      },
      async loginWithPassword() {
        this.loading = true;
        this.message = "";
        try {
          const [err, res] = await uni.request({
            url: `${this.$baseURL}/api/auth/login`,
            method: "POST",
            data: {
              username: this.username,
              password: this.password,
              captcha: this.captcha,
              captchaKey: this.captchaKey
            },
            header: {
              "Content-Type": "application/json"
            }
          }).then((res2) => [null, res2]).catch((err2) => [err2, null]);
          this.refreshCaptcha();
          if (err || res.statusCode !== 200) {
            let errorMessage = "Login failed";
            if (res == null ? void 0 : res.data) {
              if (typeof res.data === "string") {
                errorMessage = res.data;
              } else if (typeof res.data === "object") {
                if (res.data.captcha) {
                  errorMessage = res.data.captcha;
                } else if (res.data.message) {
                  errorMessage = res.data.message;
                }
              }
            } else if (err) {
              errorMessage = err.message || "Network error, please try again";
            }
            this.message = errorMessage;
            this.messageType = "error";
            uni.showToast({
              title: this.message,
              icon: "none",
              duration: 2e3
            });
            throw new Error(errorMessage);
          }
          this.token = res.data.token;
          uni.setStorageSync("token", this.token);
          this.token = res.data.token;
          uni.setStorageSync("token", this.token);
          let userInfo = {
            username: res.data.username,
            userId: res.data.userId
          };
          uni.setStorageSync("userInfo", userInfo);
          uni.showToast({
            title: "Login successful!",
            icon: "none",
            duration: 1e3
          });
          setTimeout(() => {
            uni.navigateTo({
              url: "/pages/index"
            });
          }, 1e3);
        } catch (error) {
          formatAppLog("error", "at pages/UserLogin/UserLogin.vue:205", "Login error:", error);
        } finally {
          this.loading = false;
        }
      },
      goToRegister() {
        uni.navigateTo({
          url: "/pages/UserRegister/UserRegister"
        });
      },
      find() {
        uni.navigateTo({
          url: "/pages/information/findPassword/findPassword"
        });
      },
      goToTerms() {
        uni.navigateTo({
          url: "/pages/information/terms/terms"
        });
      }
    }
  };
  function _sfc_render$q(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "page" }, [
      vue.createElementVNode("view", { class: "header" }, [
        vue.createElementVNode("text", null, "Welcome to ScootGo")
      ]),
      vue.createElementVNode("view", { class: "container" }, [
        vue.createElementVNode("view", { class: "title" }, [
          vue.createElementVNode("text", null, "Login")
        ]),
        vue.createElementVNode("form", null, [
          vue.createElementVNode("view", { class: "form-group" }, [
            vue.withDirectives(vue.createElementVNode("input", {
              type: "tel",
              "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => $data.username = $event),
              "aria-label": "username",
              placeholder: "Please enter your username",
              "placeholder-style": $data.placeholderStyle,
              class: "input-field"
            }, null, 8, ["placeholder-style"]), [
              [vue.vModelText, $data.username]
            ])
          ]),
          vue.createElementVNode("view", { class: "form-group" }, [
            vue.withDirectives(vue.createElementVNode("input", {
              type: "password",
              "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => $data.password = $event),
              "aria-label": "password",
              placeholder: "Please enter your password",
              "placeholder-style": $data.placeholderStyle,
              class: "input-field"
            }, null, 8, ["placeholder-style"]), [
              [vue.vModelText, $data.password]
            ])
          ]),
          vue.createElementVNode("view", { class: "form-group captcha-group" }, [
            vue.withDirectives(vue.createElementVNode("input", {
              type: "text",
              "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => $data.captcha = $event),
              "aria-label": "captcha",
              placeholder: "Enter captcha",
              "placeholder-style": $data.placeholderStyle,
              class: "input-field captcha-input"
            }, null, 8, ["placeholder-style"]), [
              [vue.vModelText, $data.captcha]
            ]),
            vue.createElementVNode("view", {
              class: "captcha-image",
              onClick: _cache[3] || (_cache[3] = (...args) => $options.refreshCaptcha && $options.refreshCaptcha(...args))
            }, [
              $data.captchaImage ? (vue.openBlock(), vue.createElementBlock("image", {
                key: 0,
                src: $data.captchaImage,
                alt: "captchaImage",
                mode: "aspectFit"
              }, null, 8, ["src"])) : (vue.openBlock(), vue.createElementBlock("view", {
                key: 1,
                class: "loading"
              }, "Loading..."))
            ])
          ]),
          vue.createElementVNode("view", {
            class: "login-btn",
            onClick: _cache[4] || (_cache[4] = ($event) => $options.handleSubmit())
          }, "Login")
        ]),
        vue.createElementVNode("view", { class: "form-group terms" }, [
          vue.createElementVNode(
            "checkbox-group",
            {
              onChange: _cache[6] || (_cache[6] = (...args) => $options.checkboxChange && $options.checkboxChange(...args))
            },
            [
              vue.createElementVNode("checkbox", {
                value: "cb",
                color: "#000000",
                style: { "transform": "scale(0.8)" }
              }),
              vue.createTextVNode(" I have read and agreed to ScootGo's "),
              vue.createElementVNode("a", {
                href: "javascript:void(0);",
                onClick: _cache[5] || (_cache[5] = (...args) => $options.goToTerms && $options.goToTerms(...args))
              }, "Terms")
            ],
            32
            /* NEED_HYDRATION */
          )
        ]),
        vue.createElementVNode("view", { class: "form-group terms" }, [
          vue.createTextVNode(" Click here to "),
          vue.createElementVNode("text", {
            onClick: _cache[7] || (_cache[7] = ($event) => $options.goToRegister())
          }, "Register")
        ]),
        vue.createElementVNode("view", { class: "form-group terms" }, [
          vue.createTextVNode(" Forget the password? Click here to "),
          vue.createElementVNode("text", {
            onClick: _cache[8] || (_cache[8] = ($event) => $options.find())
          }, "find password")
        ])
      ])
    ]);
  }
  const PagesUserLoginUserLogin = /* @__PURE__ */ _export_sfc(_sfc_main$r, [["render", _sfc_render$q], ["__scopeId", "data-v-aba76e30"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/UserLogin/UserLogin.vue"]]);
  const _sfc_main$q = {
    data() {
      return {
        termsContent: `
		        <div>
		          <p>Welcome to ScootGo. These terms and conditions outline the rules and regulations for the use of the ScootGo application and services.</p>
		          <p>By accessing this application, we assume you accept these terms and conditions. Do not continue to use ScootGo if you do not agree to all of the terms and conditions stated on this page.</p>
		          <p><b>1. User Eligibility</b><br />
		          To use our services, you must be at least 16 years of age and possess a valid government-issued identification. Users under 18 must have parental or legal guardian consent.</p>
		          <p><b>2. Account Responsibility</b><br />
		          You are responsible for maintaining the confidentiality of your account credentials and are fully responsible for all activities that occur under your account.</p>
		          <ul>
		            <li>Scooters must be used responsibly and parked legally.</li>
		            <li>Helmets are recommended during all rides.</li>
		            <li>You must not ride under the influence of drugs or alcohol.</li>
		            <li>Any damage caused by reckless use will be your responsibility.</li>
		          </ul>
		          <p><b>4. Payment and Fees</b><br />
		          Usage fees are charged based on time and distance. Additional charges may apply for parking violations, late returns (including overtime returns), or damages. A penalty fee will be imposed for returning the scooter beyond the allowed time limit. All fees will be displayed before ride confirmation.</p>
		          <p><b>5. Cancellations and Refunds</b><br />
				  <ul>
					<li>Rides can be cancelled before paid.</li>
					<li>Once the ride has started, no refunds are available unless due to system failure.</li>
				  </ul>
		          <p><b>6. Data Privacy</b><br />
		          We are committed to protecting your privacy. Your personal information is only used to improve our service and will not be shared with third parties without consent. Please refer to our Privacy Policy for more details.</p>
		          <p><b>7. Termination of Use</b><br />
		          ScootGo reserves the right to suspend or terminate your account if any suspicious, illegal, or abusive behavior is detected.</p>
		          <p><b>8. Changes to Terms</b><br />
		          We may update these terms from time to time. Continued use of the app after any changes constitutes acceptance of the new terms.</p>
		          <p>If you have any questions or concerns regarding these Terms, please contact us.</p>
		          <p>Thank you for choosing ScootGo!</p>
		        </div>
		      `
      };
    },
    onLoad() {
      uni.setNavigationBarTitle({
        title: "Terms of Service"
      });
    }
  };
  function _sfc_render$p(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "terms-container" }, [
      vue.createElementVNode("text", { class: "title" }, "ScootGo Terms and Conditions"),
      vue.createElementVNode("scroll-view", {
        "scroll-y": "",
        style: { "height": "90vh" }
      }, [
        vue.createElementVNode("rich-text", {
          nodes: $data.termsContent,
          class: "terms-content"
        }, null, 8, ["nodes"])
      ])
    ]);
  }
  const PagesInformationTermsTerms = /* @__PURE__ */ _export_sfc(_sfc_main$q, [["render", _sfc_render$p], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/information/terms/terms.vue"]]);
  const _sfc_main$p = {
    name: "UserRegister",
    data() {
      return {
        form: {
          username: "",
          password: "",
          email: "",
          mobile: "",
          bankCard: "",
          birthday: "",
          userType: 0
          // default: normal user
        },
        loading: false,
        message: "",
        messageType: "success",
        registeredUser: null,
        errors: {},
        placeholderStyle: "color: #2c3e50; font-size: 14px;"
      };
    },
    methods: {
      maskClick() {
        formatAppLog("log", "at pages/UserRegister/UserRegister.vue:92", "----maskClick");
      },
      getDate(date, addZero2 = true) {
        date = new Date(date);
        const year = date.getFullYear();
        const month = date.getMonth() + 1;
        const day = date.getDate();
        return `${year}-${addZero2 ? this.addZero(month) : month}-${addZero2 ? this.addZero(day) : day}`;
      },
      getDateTime(date, addZero2 = true) {
        return `${this.getDate(date, addZero2)} ${this.getTime(date, addZero2)}`;
      },
      getTime(date, addZero2 = true) {
        date = new Date(date);
        const hour = date.getHours();
        const minute = date.getMinutes();
        const second = date.getSeconds();
        return this.hideSecond ? `${addZero2 ? this.addZero(hour) : hour}:${addZero2 ? this.addZero(minute) : minute}` : `${addZero2 ? this.addZero(hour) : hour}:${addZero2 ? this.addZero(minute) : minute}:${addZero2 ? this.addZero(second) : second}`;
      },
      addZero(num) {
        if (num < 10) {
          num = `0${num}`;
        }
        return num;
      },
      validate() {
        this.errors = {};
        const usernameRegex = /^[a-zA-Z0-9_]{3,20}$/;
        if (!this.form.username) {
          this.errors.username = "Username is required";
        } else if (!usernameRegex.test(this.form.username)) {
          this.errors.username = "Username must be 3-20 characters and can only contain letters, numbers, and underscores";
        }
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
        if (!this.form.password) {
          this.errors.password = "Password is required";
        } else if (!passwordRegex.test(this.form.password)) {
          this.errors.password = "Password must be at least 8 characters and include at least one uppercase letter, one lowercase letter, and one number";
        }
        const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!this.form.email) {
          this.errors.email = "Email is required";
        } else if (!emailRegex.test(this.form.email)) {
          this.errors.email = "Please enter a valid email address";
        }
        const mobileRegex = /^[0-9]{10,13}$/;
        if (!this.form.mobile) {
          this.errors.mobile = "Mobile number is required";
        } else if (!mobileRegex.test(this.form.mobile)) {
          this.errors.mobile = "Mobile number must be between 10-13 digits";
        }
        const cardRegex = /^[0-9]{13,16}$/;
        if (!this.form.bankCard) {
          this.errors.bankCard = "Card number is required";
        } else if (!cardRegex.test(this.form.bankCard)) {
          this.errors.bankCard = "Card number must be between 13-16 digits";
        }
        const today = /* @__PURE__ */ new Date();
        const birthdayDate = new Date(this.form.birthday);
        if (!this.form.birthday) {
          this.errors.birthday = "Birthday is required";
        } else if (birthdayDate >= today) {
          this.errors.birthday = "Birthday must be in the past";
        }
        return Object.keys(this.errors).length === 0;
      },
      async register() {
        this.loading = true;
        this.message = "";
        this.registeredUser = null;
        if (!this.validate()) {
          this.message = "Please correct the errors below";
          this.messageType = "error";
          this.loading = false;
          return;
        }
        try {
          const [err, res] = await uni.request({
            url: `${this.$baseURL}/api/auth/register`,
            method: "POST",
            data: this.form,
            header: {
              "Content-Type": "application/json"
            }
          }).then((res2) => [null, res2]).catch((err2) => [err2, null]);
          if (err || res.statusCode !== 200) {
            let errorMessage = "Registration failed";
            if (res == null ? void 0 : res.data) {
              if (typeof res.data === "string") {
                errorMessage = res.data;
              } else if (typeof res.data === "object" && res.data.message) {
                errorMessage = res.data.message;
              }
            } else if (err) {
              errorMessage = err.message || "Network error, please try again";
            }
            this.message = errorMessage;
            this.messageType = "error";
            uni.showToast({
              title: this.message,
              icon: "none",
              duration: 2e3
            });
            throw new Error(errorMessage);
          }
          uni.showToast({
            title: "Registration successful!",
            icon: "none",
            duration: 2e3
          });
          this.form = {
            username: "",
            password: "",
            email: "",
            mobile: "",
            bankCard: "",
            birthday: "",
            userType: 0
          };
          setTimeout(() => {
            this.goToLogin();
          }, 2e3);
        } catch (error) {
          formatAppLog("error", "at pages/UserRegister/UserRegister.vue:241", "Registration error:", error);
        } finally {
          this.loading = false;
        }
      },
      goToLogin() {
        uni.navigateTo({
          url: "/pages/UserLogin/UserLogin"
        });
      }
    }
  };
  function _sfc_render$o(_ctx, _cache, $props, $setup, $data, $options) {
    const _component_uni_datetime_picker = resolveEasycom(vue.resolveDynamicComponent("uni-datetime-picker"), __easycom_0$1);
    return vue.openBlock(), vue.createElementBlock("view", { class: "page" }, [
      vue.createElementVNode("view", { class: "header" }, [
        vue.createElementVNode("text", null, "Welcome to ScootGo")
      ]),
      vue.createElementVNode("view", { class: "container" }, [
        vue.createElementVNode("view", { class: "title" }, [
          vue.createElementVNode("text", null, "Register")
        ]),
        vue.createElementVNode("form", null, [
          vue.createElementVNode("view", { class: "form-group" }, [
            vue.createElementVNode("label", null, "Username"),
            vue.withDirectives(vue.createElementVNode("input", {
              "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => $data.form.username = $event),
              type: "text",
              "aria-label": "username",
              placeholder: "Enter username",
              "placeholder-style": $data.placeholderStyle,
              class: "input-field"
            }, null, 8, ["placeholder-style"]), [
              [vue.vModelText, $data.form.username]
            ]),
            $data.errors.username ? (vue.openBlock(), vue.createElementBlock(
              "span",
              {
                key: 0,
                class: "error-message"
              },
              vue.toDisplayString($data.errors.username),
              1
              /* TEXT */
            )) : vue.createCommentVNode("v-if", true)
          ]),
          vue.createElementVNode("view", { class: "form-group" }, [
            vue.createElementVNode("label", null, "Password"),
            vue.withDirectives(vue.createElementVNode("input", {
              "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => $data.form.password = $event),
              type: "password",
              "aria-label": "password",
              placeholder: "Enter password",
              "placeholder-style": $data.placeholderStyle,
              class: "input-field"
            }, null, 8, ["placeholder-style"]), [
              [vue.vModelText, $data.form.password]
            ]),
            $data.errors.password ? (vue.openBlock(), vue.createElementBlock(
              "span",
              {
                key: 0,
                class: "error-message"
              },
              vue.toDisplayString($data.errors.password),
              1
              /* TEXT */
            )) : vue.createCommentVNode("v-if", true)
          ]),
          vue.createElementVNode("view", { class: "form-group" }, [
            vue.createElementVNode("label", null, "Email"),
            vue.withDirectives(vue.createElementVNode("input", {
              "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => $data.form.email = $event),
              type: "email",
              "aria-label": "email",
              placeholder: "Enter email",
              "placeholder-style": $data.placeholderStyle,
              class: "input-field"
            }, null, 8, ["placeholder-style"]), [
              [vue.vModelText, $data.form.email]
            ]),
            $data.errors.email ? (vue.openBlock(), vue.createElementBlock(
              "span",
              {
                key: 0,
                class: "error-message"
              },
              vue.toDisplayString($data.errors.email),
              1
              /* TEXT */
            )) : vue.createCommentVNode("v-if", true)
          ]),
          vue.createElementVNode("view", { class: "form-group" }, [
            vue.createElementVNode("label", null, "Mobile"),
            vue.withDirectives(vue.createElementVNode("input", {
              "onUpdate:modelValue": _cache[3] || (_cache[3] = ($event) => $data.form.mobile = $event),
              type: "text",
              "aria-label": "mobile",
              placeholder: "Enter mobile number",
              "placeholder-style": $data.placeholderStyle,
              class: "input-field"
            }, null, 8, ["placeholder-style"]), [
              [vue.vModelText, $data.form.mobile]
            ]),
            $data.errors.mobile ? (vue.openBlock(), vue.createElementBlock(
              "span",
              {
                key: 0,
                class: "error-message"
              },
              vue.toDisplayString($data.errors.mobile),
              1
              /* TEXT */
            )) : vue.createCommentVNode("v-if", true)
          ]),
          vue.createElementVNode("view", { class: "form-group" }, [
            vue.createElementVNode("label", null, "Card"),
            vue.withDirectives(vue.createElementVNode("input", {
              "onUpdate:modelValue": _cache[4] || (_cache[4] = ($event) => $data.form.bankCard = $event),
              type: "text",
              "aria-label": "bankCard",
              placeholder: "Enter Card number",
              "placeholder-style": $data.placeholderStyle,
              class: "input-field"
            }, null, 8, ["placeholder-style"]), [
              [vue.vModelText, $data.form.bankCard]
            ]),
            $data.errors.bankCard ? (vue.openBlock(), vue.createElementBlock(
              "span",
              {
                key: 0,
                class: "error-message"
              },
              vue.toDisplayString($data.errors.bankCard),
              1
              /* TEXT */
            )) : vue.createCommentVNode("v-if", true)
          ]),
          vue.createElementVNode("view", { class: "form-group" }, [
            vue.createElementVNode("label", null, "Birthday"),
            vue.createElementVNode("view", { class: "example-body" }, [
              vue.createVNode(_component_uni_datetime_picker, {
                type: "date",
                "clear-icon": false,
                modelValue: $data.form.birthday,
                "onUpdate:modelValue": _cache[5] || (_cache[5] = ($event) => $data.form.birthday = $event),
                onMaskClick: $options.maskClick
              }, null, 8, ["modelValue", "onMaskClick"])
            ]),
            $data.errors.birthday ? (vue.openBlock(), vue.createElementBlock(
              "span",
              {
                key: 0,
                class: "error-message"
              },
              vue.toDisplayString($data.errors.birthday),
              1
              /* TEXT */
            )) : vue.createCommentVNode("v-if", true)
          ])
        ]),
        vue.createElementVNode("div", { class: "form-group" }, [
          vue.createElementVNode("button", {
            onClick: _cache[6] || (_cache[6] = (...args) => $options.register && $options.register(...args)),
            disabled: $data.loading,
            class: "login-btn"
          }, vue.toDisplayString($data.loading ? "Registering..." : "Register"), 9, ["disabled"])
        ]),
        vue.createElementVNode("div", { class: "form-group" }, [
          vue.createElementVNode("button", {
            onClick: _cache[7] || (_cache[7] = (...args) => $options.goToLogin && $options.goToLogin(...args)),
            class: "login-btn"
          }, " Go to Login ")
        ])
      ])
    ]);
  }
  const PagesUserRegisterUserRegister = /* @__PURE__ */ _export_sfc(_sfc_main$p, [["render", _sfc_render$o], ["__scopeId", "data-v-51e677c0"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/UserRegister/UserRegister.vue"]]);
  const _sfc_main$o = {
    data() {
      return {
        scooter: null,
        scooterID: null,
        selectedDuration: 0,
        durationOptions: [],
        message: "",
        messageType: "",
        hireType: "",
        startTime: "",
        selectedSite: "",
        isLoading: true,
        currentUser: {
          userId: null
        },
        paymentUrl: "",
        hasDiscount: false
      };
    },
    async onLoad(options) {
      this.hireType = uni.getStorageSync("hireType");
      this.startTime = uni.getStorageSync("startTime");
      this.endTime = uni.getStorageSync("endTime");
      this.selectedSite = uni.getStorageSync("selectedStore");
      this.reverseGeocode(this.selectedSite);
      this.scooterID = options.id;
      const userInfo = uni.getStorageSync("userInfo");
      if (userInfo) {
        this.currentUser.userId = userInfo.userId || null;
      }
      await this.loadScooterDetail();
    },
    computed: {
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
    methods: {
      getPrice(scooter) {
        if (this.hireType === "HOUR") {
          return scooter.discountedPriceHour;
        } else if (this.hireType === "FOUR_HOURS") {
          return scooter.discountedPriceFourHour;
        } else if (this.hireType === "DAY") {
          return scooter.discountedPriceDay;
        } else if (this.hireType === "WEEK") {
          return scooter.discountedPriceWeek;
        }
        return 0;
      },
      getOriginalPrice(scooter) {
        if (this.hireType === "HOUR") {
          return scooter.priceHour;
        } else if (this.hireType === "FOUR_HOURS") {
          return scooter.priceFourHour;
        } else if (this.hireType === "DAY") {
          return scooter.priceDay;
        } else if (this.hireType === "WEEK") {
          return scooter.priceWeek;
        }
        return 0;
      },
      getPriceLabel() {
        switch (this.hireType) {
          case "HOUR":
            return "h";
          case "FOUR_HOURS":
            return "4h";
          case "DAY":
            return "day";
          case "WEEK":
            return "week";
          default:
            return "";
        }
      },
      async loadScooterDetail() {
        const token = String(uni.getStorageSync("token"));
        try {
          uni.showLoading({
            title: "Loading...",
            mask: true
          });
          const res = await uni.request({
            url: `${this.$baseURL}/api/scooters/${this.scooterID}?userId=${this.currentUser.userId}`,
            method: "POST",
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          if (res.statusCode === 200) {
            const data = res.data;
            this.scooter = data;
            this.hasDiscount = this.scooter.hasDiscount;
          } else {
            uni.showToast({
              title: "Loading failed. Please try again later",
              icon: "none"
            });
          }
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        } finally {
          uni.hideLoading();
          this.isLoading = false;
        }
      },
      async reverseGeocode(store) {
        uni.request({
          url: `https://restapi.amap.com/v3/geocode/regeo?output=json&location=${store.longitude},${store.latitude}&key=5f722ef9e435cec7d4dba6f5daba0030&radius=1000&extensions=all&language=en`,
          success: (res) => {
            if (res.data.status === "1") {
              this.$set(store, "locationName", res.data.regeocode.pois[1].name);
              this.$set(store, "locationNum", res.data.regeocode.formatted_address);
            } else {
              this.$set(store, "locationName", "Unable to obtain the location");
              formatAppLog("error", "at pages/cardetail/cardetail.vue:201", "Unable to obtain the location:", res.data);
            }
          },
          fail: (err) => {
            this.$set(store, "locationName", "Location request failed");
            formatAppLog("error", "at pages/cardetail/cardetail.vue:206", "Reverse geocoding request failed:", err);
          }
        });
      },
      async submitBooking() {
        const token = String(uni.getStorageSync("token"));
        if (!this.startTime) {
          this.showMessage("Please select the start time", "error");
          return;
        }
        if (!this.hireType) {
          this.showMessage("Please select the rental type", "error");
          return;
        }
        this.isLoading = true;
        uni.showLoading({
          title: "Under submission..."
        });
        try {
          const bookingData = {
            userId: this.currentUser.userId,
            scooterId: this.scooter.id,
            hireType: this.hireType,
            startTime: this.startTime
          };
          const res = await new Promise((resolve, reject) => {
            uni.request({
              url: `${this.$baseURL}/api/bookings`,
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
          if (res.statusCode === 200) {
            this.showMessage("book successfully", "success");
            uni.showToast({
              title: "Book successful!",
              icon: "none",
              duration: 2e3
            });
            await this.handlePay(res.data);
          } else {
            uni.showToast({
              title: res.data || "Scheduled failure",
              icon: "none",
              duration: 2e3
            });
          }
        } catch (error) {
          uni.showToast({
            title: error.message || "Scheduled failure",
            icon: "none",
            duration: 2e3
          });
        } finally {
          this.isLoading = false;
          uni.hideLoading();
        }
      },
      async handlePay(order) {
        uni.navigateTo({
          url: `/pages/payment/payh5?id=${order.orderId}`
        });
      },
      showMessage(text, type) {
        this.message = text;
        this.messageType = type;
        setTimeout(() => {
          this.message = "";
        }, 3e3);
      }
    }
  };
  function _sfc_render$n(_ctx, _cache, $props, $setup, $data, $options) {
    const _component_uni_icons = resolveEasycom(vue.resolveDynamicComponent("uni-icons"), __easycom_0$2);
    return vue.openBlock(), vue.createElementBlock("view", { class: "container" }, [
      vue.createCommentVNode(" basic information "),
      vue.createElementVNode("view", { class: "info-section card" }, [
        $data.scooter ? (vue.openBlock(), vue.createElementBlock("view", {
          key: 0,
          class: "header"
        }, [
          vue.createElementVNode(
            "text",
            { class: "scooter-id" },
            "#" + vue.toDisplayString($data.scooter.id),
            1
            /* TEXT */
          ),
          vue.createElementVNode(
            "view",
            {
              class: vue.normalizeClass(["status-tag", $options.statusClass])
            },
            vue.toDisplayString($options.statusText),
            3
            /* TEXT, CLASS */
          )
        ])) : vue.createCommentVNode("v-if", true),
        vue.createCommentVNode(" price "),
        $data.scooter ? (vue.openBlock(), vue.createElementBlock("view", {
          key: 1,
          class: "price-section"
        }, [
          vue.createElementVNode(
            "text",
            { class: "price" },
            "£" + vue.toDisplayString($options.getPrice($data.scooter)),
            1
            /* TEXT */
          ),
          $data.hasDiscount ? (vue.openBlock(), vue.createElementBlock(
            "text",
            {
              key: 0,
              class: "original-price"
            },
            "£" + vue.toDisplayString($options.getOriginalPrice($data.scooter)),
            1
            /* TEXT */
          )) : vue.createCommentVNode("v-if", true),
          vue.createElementVNode(
            "text",
            { class: "unit" },
            "/ " + vue.toDisplayString($options.getPriceLabel()),
            1
            /* TEXT */
          )
        ])) : vue.createCommentVNode("v-if", true),
        vue.createCommentVNode(" address and time information "),
        $data.scooter ? (vue.openBlock(), vue.createElementBlock("view", {
          key: 2,
          class: "address-section"
        }, [
          vue.createElementVNode("view", { class: "notification-wrapper" }, [
            vue.createVNode(_component_uni_icons, {
              type: "notification",
              size: "20",
              color: "#ff4d4f"
            }),
            vue.createElementVNode("text", { class: "note" }, "Please carefully check the rental period and the information of the return service point")
          ]),
          vue.createElementVNode("view", { class: "information" }, [
            vue.createElementVNode("view", { class: "label" }, [
              vue.createElementVNode("text", { class: "label-text" }, "Pick and Return"),
              vue.createElementVNode(
                "text",
                { class: "store-name" },
                vue.toDisplayString($data.selectedSite.name),
                1
                /* TEXT */
              )
            ]),
            vue.createElementVNode("view", { class: "label" }, [
              vue.createElementVNode("text", { class: "label-text" }, "Location:"),
              vue.createElementVNode(
                "text",
                { class: "store-location" },
                vue.toDisplayString($data.selectedSite.locationName),
                1
                /* TEXT */
              )
            ]),
            vue.createElementVNode("view", { class: "label" }, [
              vue.createElementVNode("text", { class: "label-text" }, "Time:"),
              vue.createElementVNode(
                "text",
                { class: "time-range" },
                vue.toDisplayString($data.startTime) + " → " + vue.toDisplayString(_ctx.endTime),
                1
                /* TEXT */
              )
            ])
          ])
        ])) : vue.createCommentVNode("v-if", true)
      ]),
      vue.createElementVNode("view", { class: "action-bar" }, [
        vue.createElementVNode("button", {
          onClick: _cache[0] || (_cache[0] = (...args) => $options.submitBooking && $options.submitBooking(...args)),
          disabled: $data.isLoading
        }, vue.toDisplayString($data.isLoading ? "In process..." : "Confirming a Reservation"), 9, ["disabled"])
      ])
    ]);
  }
  const PagesCardetailCardetail = /* @__PURE__ */ _export_sfc(_sfc_main$o, [["render", _sfc_render$n], ["__scopeId", "data-v-a91970b9"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/cardetail/cardetail.vue"]]);
  const _imports_0$4 = "/static/common/icon-letter.png";
  const _imports_1$2 = "/static/common/icon-edit.png";
  const _sfc_main$n = {
    data() {
      return {
        problemBox: [
          {
            problemTitle: "Scooter Rental",
            problemList: [
              {
                title: "How do I book a scooter?",
                content: "You can click [Instruction on Use] on the home page."
              },
              {
                title: "Can I cancel my scooter reservation?",
                content: "Unpaid orders can be cancelled within [All Orders], while paid orders cannot be cancelled."
              }
            ]
          },
          {
            problemTitle: "Payment Issues",
            problemList: [
              {
                title: "Why am I being charged incorrectly for my scooter rental?",
                content: "When you return, the power connection is too low or the return is overdue."
              }
            ]
          },
          {
            problemTitle: "Scooter Usage",
            problemList: [
              {
                title: "How do I unlock a scooter?",
                content: 'Click "Return" on the [now] page.'
              }
            ]
          },
          {
            problemTitle: "Other",
            problemList: [
              {
                title: "How to change my user profile picture?",
                content: "Please click on [My], and click on Avatar to change the your avatar."
              },
              {
                title: "How to change my login password?",
                content: "Please click on [My]-[Set]-[Account and Security]to change the your password."
              },
              {
                title: "How to log out?",
                content: "Please click on [My]-[Log Out] to log out."
              },
              {
                title: "What if the system is slow or unresponsive?",
                content: "If there are problems such as slow system response or excessively long response time, you can check the network status of this machine or restart the program and try again."
              }
            ]
          }
        ]
      };
    },
    methods: {
      onClick(item) {
        uni.navigateTo({
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
        uni.navigateTo({
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
        uni.navigateTo({
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
  function _sfc_render$m(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", null, [
      vue.createElementVNode("view", { class: "text-top" }, "Common Problem"),
      vue.createElementVNode("view", { class: "bg-list" }, [
        (vue.openBlock(true), vue.createElementBlock(
          vue.Fragment,
          null,
          vue.renderList($data.problemBox, (item, index) => {
            return vue.openBlock(), vue.createElementBlock("view", {
              key: index,
              title: item.problemTitle,
              class: "list-title"
            }, [
              vue.createElementVNode(
                "view",
                { class: "text-title" },
                vue.toDisplayString(item.problemTitle),
                1
                /* TEXT */
              ),
              (vue.openBlock(true), vue.createElementBlock(
                vue.Fragment,
                null,
                vue.renderList(item.problemList, (problemItem, problemIndex) => {
                  return vue.openBlock(), vue.createElementBlock("view", {
                    key: problemIndex,
                    class: "list-question",
                    "hover-class": "hover",
                    onClick: ($event) => $options.onClick(problemItem)
                  }, [
                    vue.createElementVNode(
                      "view",
                      { class: "text-item" },
                      vue.toDisplayString(problemItem.title),
                      1
                      /* TEXT */
                    ),
                    problemIndex != item.problemList.length - 1 ? (vue.openBlock(), vue.createElementBlock("view", {
                      key: 0,
                      class: "line"
                    })) : vue.createCommentVNode("v-if", true)
                  ], 8, ["onClick"]);
                }),
                128
                /* KEYED_FRAGMENT */
              ))
            ], 8, ["title"]);
          }),
          128
          /* KEYED_FRAGMENT */
        ))
      ]),
      vue.createElementVNode("view", { class: "bg-box" }, [
        vue.createElementVNode("view", { class: "bg-white-box" }, [
          vue.createElementVNode("image", {
            src: _imports_0$4,
            class: "image"
          }),
          vue.createElementVNode("view", {
            class: "text-feedback",
            "hover-class": "hover",
            onClick: _cache[0] || (_cache[0] = (...args) => $options.toFeedbackList && $options.toFeedbackList(...args))
          }, "My Feedback"),
          vue.createElementVNode("view", { class: "vertical-line" }),
          vue.createElementVNode("image", {
            src: _imports_1$2,
            class: "image"
          }),
          vue.createElementVNode("view", {
            class: "text-feedback",
            "hover-class": "hover",
            onClick: _cache[1] || (_cache[1] = (...args) => $options.toFeedback && $options.toFeedback(...args))
          }, "Submit Feedback")
        ])
      ])
    ]);
  }
  const PagesFeedbackFeedbackIndexFeedbackIndex = /* @__PURE__ */ _export_sfc(_sfc_main$n, [["render", _sfc_render$m], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/feedback/feedbackIndex/feedbackIndex.vue"]]);
  const _sfc_main$m = {
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
      async getUserFeedback() {
        try {
          const token = String(uni.getStorageSync("token"));
          const res = await uni.request({
            url: `${this.$baseURL}/api/feedback/user`,
            method: "GET",
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          if (res.statusCode === 200) {
            this.feedbacks = res.data;
          } else {
            uni.showToast({ title: "Data loading failed", icon: "none" });
          }
        } catch (err) {
          uni.showToast({ title: "Network Error", icon: "none" });
        } finally {
          this.isLoading = false;
        }
      },
      feedbackClick(item) {
        uni.navigateTo({
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
  function _sfc_render$l(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "page" }, [
      $data.feedbacks.length == 0 ? (vue.openBlock(), vue.createElementBlock("view", {
        key: 0,
        class: "no-feedback"
      }, [
        vue.createElementVNode("text", null, "No feedback")
      ])) : (vue.openBlock(), vue.createElementBlock("view", { key: 1 }, [
        (vue.openBlock(true), vue.createElementBlock(
          vue.Fragment,
          null,
          vue.renderList($data.feedbacks, (item, index) => {
            return vue.openBlock(), vue.createElementBlock("view", {
              key: index,
              onClick: ($event) => $options.feedbackClick(item)
            }, [
              vue.createElementVNode("view", { class: "list-box" }, [
                vue.createElementVNode(
                  "view",
                  { class: "text-title" },
                  vue.toDisplayString(item.content),
                  1
                  /* TEXT */
                ),
                item.status === "pending" ? (vue.openBlock(), vue.createElementBlock("view", {
                  key: 0,
                  class: "text-tips"
                }, " We have received your feedback and will process it and get back to you as soon as possible. Thank you for your support. ")) : vue.createCommentVNode("v-if", true),
                item.status === "processing" ? (vue.openBlock(), vue.createElementBlock("view", {
                  key: 1,
                  class: "text-tips"
                }, " We are processing your feedback, please wait patiently. ")) : vue.createCommentVNode("v-if", true),
                item.status === "resolved" ? (vue.openBlock(), vue.createElementBlock(
                  "view",
                  {
                    key: 2,
                    class: "text-tips"
                  },
                  vue.toDisplayString(item.adminResponse),
                  1
                  /* TEXT */
                )) : vue.createCommentVNode("v-if", true),
                vue.createElementVNode(
                  "view",
                  { class: "text-time" },
                  vue.toDisplayString(item.createTime.replace("T", " ")),
                  1
                  /* TEXT */
                ),
                vue.createElementVNode(
                  "view",
                  {
                    class: vue.normalizeClass($options.statusClass(item.status))
                  },
                  vue.toDisplayString($options.statusText(item.status)),
                  3
                  /* TEXT, CLASS */
                )
              ])
            ], 8, ["onClick"]);
          }),
          128
          /* KEYED_FRAGMENT */
        ))
      ]))
    ]);
  }
  const PagesFeedbackFeedbackListFeedbackList = /* @__PURE__ */ _export_sfc(_sfc_main$m, [["render", _sfc_render$l], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/feedback/feedbackList/feedbackList.vue"]]);
  const _sfc_main$l = {
    data() {
      return {
        sendData: {
          feedbackContent: ""
        },
        loading: false,
        message: "",
        messageType: "",
        placeholderStyle: "color: #2c3e50; font-size: 14px;"
      };
    },
    methods: {
      async submitFeedback() {
        this.loading = true;
        const token = String(uni.getStorageSync("token"));
        try {
          const [err, res] = await uni.request({
            url: `${this.$baseURL}/api/feedback`,
            method: "POST",
            data: { content: this.sendData.feedbackContent },
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          }).then((res2) => [null, res2]).catch((err2) => [err2, null]);
          if (err || res.statusCode !== 200) {
            let errorMessage = "Submition failed";
            if (res == null ? void 0 : res.data) {
              if (typeof res.data === "string") {
                errorMessage = res.data;
              } else if (typeof res.data === "object" && res.data.message) {
                errorMessage = res.data.message;
              }
            } else if (err) {
              errorMessage = err.message || "Network error, please try again";
            }
            this.message = errorMessage;
            this.messageType = "error";
            uni.showToast({
              title: this.message,
              icon: "none",
              duration: 2e3
            });
            throw new Error(errorMessage);
          }
          formatAppLog("log", "at pages/feedback/feedback/feedback.vue:65", "提交成功:", res.data);
          uni.showToast({
            title: "Submition successful!",
            icon: "none",
            duration: 2e3
          });
          this.sendData = {
            feedbackContent: ""
          };
        } catch (error) {
          formatAppLog("error", "at pages/feedback/feedback/feedback.vue:76", "Submition error:", error);
        } finally {
          this.loading = false;
        }
      }
    }
  };
  function _sfc_render$k(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "feedback-body" }, [
      vue.createElementVNode("text", { class: "text-black" }, [
        vue.createTextVNode("Feedback "),
        vue.createElementVNode("text", { style: { "color": "red" } }, "*")
      ]),
      vue.withDirectives(vue.createElementVNode("textarea", {
        placeholder: "Please describe a problem you have encountered or a recommendation for this product...",
        "placeholder-style": $data.placeholderStyle,
        "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => $data.sendData.feedbackContent = $event),
        class: "feedback-textare",
        maxlength: "-1"
      }, null, 8, ["placeholder-style"]), [
        [vue.vModelText, $data.sendData.feedbackContent]
      ]),
      vue.createElementVNode("view", { class: "btn" }, [
        vue.createElementVNode("button", {
          disabled: !$data.sendData.feedbackContent,
          type: "primary",
          onClick: _cache[1] || (_cache[1] = (...args) => $options.submitFeedback && $options.submitFeedback(...args))
        }, " Submit ", 8, ["disabled"])
      ])
    ]);
  }
  const PagesFeedbackFeedbackFeedback = /* @__PURE__ */ _export_sfc(_sfc_main$l, [["render", _sfc_render$k], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/feedback/feedback/feedback.vue"]]);
  const _sfc_main$k = {
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
      uni.setNavigationBarTitle({
        title: options.title
      });
    }
  };
  function _sfc_render$j(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "container" }, [
      vue.createElementVNode("view", { class: "content-box" }, [
        vue.createElementVNode(
          "view",
          { class: "helpTitle" },
          vue.toDisplayString($data.resultData.title),
          1
          /* TEXT */
        ),
        vue.createElementVNode(
          "view",
          { class: "helpCon" },
          vue.toDisplayString($data.resultData.content),
          1
          /* TEXT */
        )
      ])
    ]);
  }
  const PagesFeedbackHelpDetailHelpDetail = /* @__PURE__ */ _export_sfc(_sfc_main$k, [["render", _sfc_render$j], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/feedback/helpDetail/helpDetail.vue"]]);
  const _imports_0$3 = "/static/common/icon_solved_sel.png";
  const _imports_1$1 = "/static/common/icon_solved_nor.png";
  const _imports_2 = "/static/common/icon_notsolved_sel.png";
  const _imports_3 = "/static/common/icon_notsolved_nor.png";
  const _sfc_main$j = {
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
    }
  };
  function _sfc_render$i(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", null, [
      vue.createElementVNode("view", { class: "white" }, [
        vue.createElementVNode(
          "view",
          { class: "text-black" },
          vue.toDisplayString($data.feedback.content),
          1
          /* TEXT */
        ),
        vue.createElementVNode(
          "view",
          { class: "text-time" },
          vue.toDisplayString($data.feedback.createTime.replace("T", " ")) + " submitted",
          1
          /* TEXT */
        )
      ]),
      vue.createElementVNode("view", { class: "box" }, [
        vue.createElementVNode("view", { class: "text-black" }, "Admin Response:"),
        vue.createElementVNode("view", { class: "text-grey" }, "We have received your feedback, we will process and reply to you as soon as possible, please understand the inconvenience caused to you. Thank you for your feedback and support!"),
        vue.createElementVNode(
          "view",
          { class: "text-time" },
          vue.toDisplayString($data.feedback.createTime.replace("T", " ")),
          1
          /* TEXT */
        )
      ]),
      $data.feedback.adminResponse ? (vue.openBlock(), vue.createElementBlock("view", {
        key: 0,
        class: "box"
      }, [
        vue.createElementVNode("view", { class: "text-black" }, "Admin Response:"),
        vue.createElementVNode(
          "view",
          { class: "text-grey" },
          vue.toDisplayString($data.feedback.adminResponse),
          1
          /* TEXT */
        ),
        vue.createElementVNode("view", {
          class: "text-grey",
          style: { "margin-top": "20rpx" }
        }, "Whether it solved your problem"),
        vue.createElementVNode("view", { class: "flex-row" }, [
          vue.createElementVNode(
            "view",
            {
              onClick: _cache[0] || (_cache[0] = ($event) => $options.resolve(true)),
              class: vue.normalizeClass([$data.isResolve && $data.isShow ? "btn-resolve" : "btn-normal", "btn-normal"])
            },
            [
              $data.isResolve && $data.isShow ? (vue.openBlock(), vue.createElementBlock("image", {
                key: 0,
                src: _imports_0$3,
                class: "icon-normal"
              })) : (vue.openBlock(), vue.createElementBlock("image", {
                key: 1,
                src: _imports_1$1,
                class: "icon-normal"
              })),
              vue.createTextVNode(" resolved ")
            ],
            2
            /* CLASS */
          ),
          vue.createElementVNode(
            "view",
            {
              onClick: _cache[1] || (_cache[1] = ($event) => $options.resolve(false)),
              class: vue.normalizeClass([!$data.isResolve && $data.isShow ? "btn-unResolve" : "btn-normal", "btn-normal"])
            },
            [
              !$data.isResolve && $data.isShow ? (vue.openBlock(), vue.createElementBlock("image", {
                key: 0,
                src: _imports_2,
                class: "icon-normal"
              })) : (vue.openBlock(), vue.createElementBlock("image", {
                key: 1,
                src: _imports_3,
                class: "icon-normal"
              })),
              vue.createTextVNode(" unsolved ")
            ],
            2
            /* CLASS */
          )
        ]),
        vue.createElementVNode(
          "view",
          { class: "text-time" },
          vue.toDisplayString($data.feedback.responseTime.replace("T", " ")),
          1
          /* TEXT */
        )
      ])) : vue.createCommentVNode("v-if", true),
      $data.feedback.adminResponse && $data.isResolve && $data.isShow ? (vue.openBlock(), vue.createElementBlock("view", {
        key: 1,
        class: "box"
      }, [
        vue.createElementVNode("view", { class: "text-black" }, "Admin Response"),
        vue.createElementVNode("view", { class: "text-grey" }, "Dear users, we are glad that our answers can help you. Thank you for your support!")
      ])) : vue.createCommentVNode("v-if", true),
      $data.feedback.adminResponse && !$data.isResolve && $data.isShow ? (vue.openBlock(), vue.createElementBlock("view", {
        key: 2,
        class: "box"
      }, [
        vue.createElementVNode("view", { class: "text-black" }, "Admin Response"),
        vue.createElementVNode("view", { class: "text-grey" }, "Dear users, we are very sorry that our answer did not help you. It is recommended that you describe your problems in detail and re-submit feedback or call customer service to continue consulting, thank you for your support! ")
      ])) : vue.createCommentVNode("v-if", true),
      vue.createElementVNode("view")
    ]);
  }
  const PagesFeedbackFeedbackDetailFeedbackDetail = /* @__PURE__ */ _export_sfc(_sfc_main$j, [["render", _sfc_render$i], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/feedback/feedbackDetail/feedbackDetail.vue"]]);
  const _imports_0$2 = "/static/images/car.jpg";
  const _sfc_main$i = {
    onBackPress(e) {
      uni.navigateTo({
        url: "/pages/index"
      });
      return true;
    },
    data() {
      return {
        tabsIndex: 0,
        allList: [
          {
            tabId: 0,
            name: "All"
          },
          {
            tabId: 1,
            name: "Unpaid"
          },
          {
            tabId: 2,
            name: "Paid"
          },
          {
            tabId: 4,
            name: "Cancelled"
          }
        ],
        list: [
          {
            name: "Unpaid"
          },
          {
            name: "Paid"
          },
          {
            name: "Completed"
          },
          {
            name: "Cancelled"
          }
        ],
        orderList: [],
        userInfo: uni.getStorageSync("userInfo"),
        isLoading: true,
        data: false
      };
    },
    async mounted() {
      await this.loadBookings();
    },
    onLoad() {
    },
    methods: {
      async loadBookings() {
        const user = uni.getStorageSync("userInfo");
        const token = String(uni.getStorageSync("token"));
        try {
          const res = await uni.request({
            url: `${this.$baseURL}/api/users/orders/${user.userId}`,
            method: "GET",
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          if (res.statusCode === 200) {
            this.orderList = res.data;
            if (this.orderList.length > 0) {
              this.data = true;
            }
          } else {
            uni.showToast({
              title: "Data loading failed",
              icon: "none"
            });
          }
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        } finally {
          this.isLoading = false;
        }
      },
      formatDate(dateString) {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        const hours = String(date.getHours()).padStart(2, "0");
        const minutes = String(date.getMinutes()).padStart(2, "0");
        return ` ${day}/${month}/${year} ${hours}:${minutes}`;
      },
      tabClick(item) {
        this.tabsIndex = item.tabId;
      },
      getStatusClass(status) {
        return {
          "status-red": status == 1,
          "status-2": status == 2,
          "status-3": status == 3,
          "status-4": status == 4,
          "status-green": status == 5
        };
      },
      // cancel
      async cancelClick(item) {
        uni.showModal({
          title: "Tips",
          content: "Are you sure you want to cancel this order?",
          cancelText: "cancel",
          confirmText: "confirm",
          success: async (res) => {
            if (res.confirm) {
              const token = String(uni.getStorageSync("token"));
              try {
                uni.showLoading({
                  title: "loading...",
                  mask: true
                });
                const res2 = await uni.request({
                  url: `${this.$baseURL}/api/bookings/cancel/${item.id}`,
                  method: "POST",
                  header: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                  }
                });
                if (res2.statusCode === 200) {
                  uni.showToast({
                    title: res2.data.message,
                    icon: "none",
                    duration: 2e3
                  });
                  this.loadBookings();
                } else {
                  uni.showToast({
                    title: res2.data.message,
                    icon: "none",
                    duration: 2e3
                  });
                }
              } catch (err) {
                uni.showToast({
                  title: "Network Error",
                  icon: "none"
                });
              } finally {
                uni.hideLoading();
                this.isLoading = false;
              }
            }
          }
        });
      },
      payClick(item) {
        uni.navigateTo({
          url: `/pages/payment/payh5?id=${item.id}`
        });
      },
      evaluateClick(item) {
        uni.navigateTo({
          url: "../order/OrderEvaluate?detailDate=" + encodeURIComponent(JSON.stringify(item))
        });
      }
    }
  };
  function _sfc_render$h(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "content" }, [
      !$data.data ? (vue.openBlock(), vue.createElementBlock("view", {
        key: 0,
        class: "none"
      }, " There has no order! ")) : vue.createCommentVNode("v-if", true),
      vue.createElementVNode("view", { class: "me-head" }, [
        vue.createElementVNode("view", { class: "tabs-container" }, [
          vue.createElementVNode("view", { class: "tabs" }, [
            (vue.openBlock(true), vue.createElementBlock(
              vue.Fragment,
              null,
              vue.renderList($data.allList, (item, index) => {
                return vue.openBlock(), vue.createElementBlock("view", {
                  key: index,
                  class: vue.normalizeClass(["tab", { active: $data.tabsIndex === item.tabId }]),
                  onClick: ($event) => $options.tabClick(item)
                }, [
                  vue.createElementVNode(
                    "text",
                    null,
                    vue.toDisplayString(item.name),
                    1
                    /* TEXT */
                  )
                ], 10, ["onClick"]);
              }),
              128
              /* KEYED_FRAGMENT */
            ))
          ])
        ])
      ]),
      vue.createElementVNode("view", { class: "me-center" }, [
        vue.createElementVNode("view", { class: "list-tab" }, [
          (vue.openBlock(true), vue.createElementBlock(
            vue.Fragment,
            null,
            vue.renderList($data.orderList, (item, index) => {
              return vue.openBlock(), vue.createElementBlock("ul", {
                class: "item-ul",
                key: index
              }, [
                item.status == $data.tabsIndex || $data.tabsIndex == 0 ? (vue.openBlock(), vue.createElementBlock("li", { key: 0 }, [
                  vue.createElementVNode("view", { class: "item-top" }, [
                    vue.createElementVNode("view", null, [
                      vue.createElementVNode("text", null, "OrderID: "),
                      vue.createElementVNode(
                        "text",
                        null,
                        vue.toDisplayString(item.id),
                        1
                        /* TEXT */
                      )
                    ]),
                    vue.createElementVNode(
                      "view",
                      {
                        class: vue.normalizeClass(["pay-type", $options.getStatusClass(item.status)])
                      },
                      [
                        vue.createElementVNode(
                          "text",
                          null,
                          vue.toDisplayString($data.list[item.status - 1].name),
                          1
                          /* TEXT */
                        )
                      ],
                      2
                      /* CLASS */
                    )
                  ]),
                  vue.createElementVNode("view", { class: "dashed-line" }),
                  vue.createElementVNode("view", { class: "item-content" }, [
                    vue.createElementVNode("view", { class: "item-left" }, [
                      vue.createElementVNode("image", {
                        src: _imports_0$2,
                        alt: "Order Image",
                        style: { "width": "70px", "height": "70px", "border-radius": "5px" }
                      })
                    ]),
                    vue.createElementVNode("view", { class: "item-right" }, [
                      vue.createElementVNode("view", { class: "item-right-v1 induce" }, [
                        vue.createElementVNode(
                          "text",
                          { class: "rate-text1" },
                          "Order Time: " + vue.toDisplayString($options.formatDate(item.startTime)),
                          1
                          /* TEXT */
                        )
                      ]),
                      vue.createElementVNode("view", { class: "item-right-v1 induce" }, [
                        vue.createElementVNode(
                          "text",
                          { class: "rate-text1" },
                          "Start Time: " + vue.toDisplayString($options.formatDate(item.startTime)),
                          1
                          /* TEXT */
                        )
                      ]),
                      vue.createElementVNode("view", { class: "item-right-v1 induce" }, [
                        vue.createElementVNode(
                          "text",
                          { class: "rate-text1" },
                          "Hire Period: " + vue.toDisplayString(item.hirePeriod),
                          1
                          /* TEXT */
                        )
                      ]),
                      vue.createElementVNode("view", { class: "item-right-v2" }, [
                        vue.createElementVNode("view", { class: "v2-fh" }, [
                          vue.createTextVNode("£ "),
                          vue.createElementVNode(
                            "text",
                            { class: "v2-price" },
                            vue.toDisplayString(item.price),
                            1
                            /* TEXT */
                          )
                        ])
                      ])
                    ])
                  ]),
                  vue.createElementVNode("view", { class: "dashed-line" }),
                  item.status == 1 ? (vue.openBlock(), vue.createElementBlock("view", {
                    key: 0,
                    class: "item-btom"
                  }, [
                    vue.createElementVNode("view", {
                      onClick: ($event) => $options.cancelClick(item)
                    }, [
                      vue.createElementVNode("view", { class: "item-btom-btn" }, "Cancel")
                    ], 8, ["onClick"]),
                    vue.createElementVNode("view", {
                      onClick: ($event) => $options.payClick(item)
                    }, [
                      vue.createElementVNode("view", { class: "item-btom-btn pay" }, "Pay")
                    ], 8, ["onClick"])
                  ])) : vue.createCommentVNode("v-if", true)
                ])) : vue.createCommentVNode("v-if", true)
              ]);
            }),
            128
            /* KEYED_FRAGMENT */
          ))
        ])
      ])
    ]);
  }
  const PagesMyorderOrderlistOrderlist = /* @__PURE__ */ _export_sfc(_sfc_main$i, [["render", _sfc_render$h], ["__scopeId", "data-v-267dc6a9"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/myorder/orderlist/orderlist.vue"]]);
  const _sfc_main$h = {
    data() {
      return {
        orderInfo: {},
        orderId: null,
        order: [],
        payFormHtml: "",
        discount: 0
      };
    },
    async onLoad(options) {
      this.orderId = options.id;
      await this.loadOrderDetail();
    },
    methods: {
      async pay() {
        uni.navigateTo({
          url: `/pages/payment/choosePay/choosePay?id=${this.orderId}`
        });
      },
      async loadOrderDetail() {
        const token = String(uni.getStorageSync("token"));
        try {
          uni.showLoading({
            title: "Loading...",
            mask: true
          });
          const res = await uni.request({
            url: `${this.$baseURL}/api/bookings/${this.orderId}`,
            method: "GET",
            header: {
              "Authorization": `Bearer ${token}`,
              "Content-Type": "application/json"
            }
          });
          if (res.statusCode === 200) {
            this.order = res.data;
            this.discount = this.order.priceBeforeDiscount - this.order.price;
          } else {
            uni.showToast({
              title: "Data loading failed",
              icon: "none"
            });
          }
        } catch (err) {
          formatAppLog("error", "at pages/payment/payh5.vue:127", "request failed:", err);
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        } finally {
          uni.hideLoading();
        }
      },
      formatDate(dateString) {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        const hours = String(date.getHours()).padStart(2, "0");
        const minutes = String(date.getMinutes()).padStart(2, "0");
        return ` ${day}/${month}/${year} ${hours}:${minutes}`;
      },
      async cancel() {
        const token = String(uni.getStorageSync("token"));
        try {
          uni.showLoading({
            title: "Loading...",
            mask: true
          });
          const res = await uni.request({
            url: `${this.$baseURL}/api/bookings/cancel/${this.orderId}`,
            method: "POST",
            header: {
              "Authorization": `Bearer ${token}`,
              "Content-Type": "application/json"
            }
          });
          if (res.statusCode === 200) {
            uni.showToast({
              title: res.data.message,
              icon: "none",
              duration: 1e3
            });
          } else {
            uni.showToast({
              title: res.data.message,
              icon: "none",
              duration: 1e3
            });
          }
          uni.navigateTo({
            url: "/pages/myorder/orderlist/orderlist"
          });
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        } finally {
          uni.hideLoading();
        }
      }
    }
  };
  function _sfc_render$g(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "page-order-det" }, [
      vue.createElementVNode("view", { class: "top" }),
      vue.createElementVNode("view", { class: "body" }, [
        vue.createElementVNode("view", { class: "status" }, [
          vue.createElementVNode("image", {
            class: "s-img",
            src: _imports_0$2,
            mode: "aspectFill"
          }),
          vue.createElementVNode("view", { class: "main" }, [
            vue.createElementVNode("text", { class: "status-name" }, "Successful Booking"),
            vue.createElementVNode("text", { class: "tips" }, "Please pay as soon as possible")
          ])
        ]),
        $data.order.scooter ? (vue.openBlock(), vue.createElementBlock("view", {
          key: 0,
          class: "goods-info"
        }, [
          vue.createElementVNode("view", { class: "title" }, " Scooter Information "),
          vue.createElementVNode("view", { class: "list" }, [
            vue.createElementVNode("view", { class: "item" }, [
              vue.createElementVNode("image", {
                src: _imports_0$2,
                mode: "aspectFill",
                class: "preview"
              }),
              vue.createElementVNode("view", { class: "main" }, [
                vue.createElementVNode("view", { class: "info" }, [
                  vue.createElementVNode(
                    "text",
                    { class: "name" },
                    "ID: " + vue.toDisplayString($data.order.scooter.id),
                    1
                    /* TEXT */
                  ),
                  vue.createElementVNode(
                    "text",
                    { class: "size" },
                    "Store: " + vue.toDisplayString($data.order.scooter.store.name),
                    1
                    /* TEXT */
                  )
                ]),
                vue.createElementVNode("view", { class: "price" }, [
                  vue.createElementVNode("text", { class: "unit" }, "£"),
                  vue.createElementVNode(
                    "text",
                    { class: "num" },
                    vue.toDisplayString($data.order.priceBeforeDiscount),
                    1
                    /* TEXT */
                  )
                ])
              ])
            ])
          ]),
          vue.createElementVNode("view", { class: "discount" }, [
            vue.createElementVNode("text", { class: "name" }, "Discount Amount"),
            vue.createElementVNode("view", { class: "price" }, [
              vue.createTextVNode("£"),
              vue.createElementVNode(
                "text",
                null,
                vue.toDisplayString($data.discount),
                1
                /* TEXT */
              )
            ])
          ]),
          vue.createElementVNode("view", { class: "pay" }, [
            vue.createElementVNode("text", { class: "name" }, "Actual Payment"),
            vue.createElementVNode(
              "text",
              { class: "price" },
              "£" + vue.toDisplayString($data.order.price),
              1
              /* TEXT */
            )
          ])
        ])) : vue.createCommentVNode("v-if", true),
        vue.createElementVNode("view", { class: "order-info" }, [
          vue.createElementVNode("view", { class: "title" }, "Order Information"),
          vue.createElementVNode("view", { class: "list" }, [
            vue.createElementVNode("view", { class: "item" }, [
              vue.createElementVNode("text", { class: "label" }, "Order Reference"),
              vue.createElementVNode(
                "text",
                { class: "value" },
                vue.toDisplayString($data.order.id),
                1
                /* TEXT */
              )
            ]),
            vue.createElementVNode("view", { class: "item" }, [
              vue.createElementVNode("text", { class: "label" }, "Mode of Payment"),
              vue.createElementVNode("text", { class: "value" }, "Online Payment")
            ]),
            vue.createElementVNode("view", { class: "item" }, [
              vue.createElementVNode("text", { class: "label" }, "Order Time"),
              vue.createElementVNode(
                "text",
                { class: "value" },
                vue.toDisplayString($options.formatDate($data.order.orderTime)),
                1
                /* TEXT */
              )
            ]),
            vue.createElementVNode("view", { class: "item" }, [
              vue.createElementVNode("text", { class: "label" }, "Start Time"),
              vue.createElementVNode(
                "text",
                { class: "value" },
                vue.toDisplayString($options.formatDate($data.order.startTime)),
                1
                /* TEXT */
              )
            ]),
            vue.createElementVNode("view", { class: "item" }, [
              vue.createElementVNode("text", { class: "label" }, "End Time"),
              vue.createElementVNode(
                "text",
                { class: "value" },
                vue.toDisplayString($options.formatDate($data.order.endTime)),
                1
                /* TEXT */
              )
            ])
          ])
        ]),
        vue.createElementVNode("view", { class: "order-pay" }, [
          vue.createElementVNode("view", { class: "total" }, [
            vue.createElementVNode("text", { class: "name" }),
            vue.createElementVNode("text", {
              value: "95.00",
              type: "price",
              color: "red",
              size: 30
            })
          ]),
          vue.createElementVNode("view", { class: "btn" }, [
            vue.createElementVNode("view", {
              class: "cancel",
              onClick: _cache[0] || (_cache[0] = ($event) => $options.cancel())
            }, [
              vue.createElementVNode("button", { size: "mini" }, "Cancel Order")
            ]),
            vue.createElementVNode("view", {
              class: "go-pay",
              onClick: _cache[1] || (_cache[1] = ($event) => $options.pay())
            }, [
              vue.createElementVNode("button", { size: "mini" }, "Pay")
            ])
          ])
        ])
      ])
    ]);
  }
  const PagesPaymentPayh5 = /* @__PURE__ */ _export_sfc(_sfc_main$h, [["render", _sfc_render$g], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/payment/payh5.vue"]]);
  const _sfc_main$g = {
    data() {
      return {
        orderId: null
      };
    },
    onLoad(options) {
      this.orderId = options.id;
      formatAppLog("log", "at pages/webview/webview.vue:97", "Received orderId:", this.orderId);
      this.startAlipay();
    },
    methods: {
      async startAlipay() {
        const token = uni.getStorageSync("token");
        try {
          const res = await uni.request({
            url: `${this.$baseURL}/alipay/appPay/${this.orderId}`,
            method: "GET",
            header: {
              "Authorization": `Bearer ${token}`
            }
          });
          if (res.statusCode === 200 && res.data.orderString) {
            const orderStr = res.data.orderString;
            uni.requestPayment({
              provider: "alipay",
              orderInfo: orderStr,
              // 后端返回的支付宝 orderString
              success: function(payRes) {
                formatAppLog("log", "at pages/webview/webview.vue:120", "Pay Successfully:", payRes);
                uni.showToast({
                  title: "Pay Successfully",
                  icon: "success"
                });
                uni.redirectTo({
                  url: "/pages/my-bookings/my-bookings"
                });
              },
              fail: function(err) {
                formatAppLog("error", "at pages/webview/webview.vue:130", "Failed to pay:", err);
                uni.showToast({
                  title: "Payment is cancelled",
                  icon: "none"
                });
                uni.redirectTo({
                  url: "/pages/my-bookings/my-bookings"
                });
              }
            });
          } else {
            formatAppLog("error", "at pages/webview/webview.vue:142", "The back end did not return orderString");
            uni.showToast({
              title: "Payment request failed.",
              icon: "none"
            });
          }
        } catch (error) {
          formatAppLog("error", "at pages/webview/webview.vue:149", "Network Error:", error);
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        }
      }
    }
  };
  function _sfc_render$f(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", null, [
      vue.createElementVNode("text", null, "The Alipay payment is being invoked....")
    ]);
  }
  const PagesWebviewWebview = /* @__PURE__ */ _export_sfc(_sfc_main$g, [["render", _sfc_render$f], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/webview/webview.vue"]]);
  const _sfc_main$f = {
    data() {
      return {
        tabsIndex: 0,
        orderList: [],
        userInfo: uni.getStorageSync("userInfo"),
        isLoading: true,
        picUrl: "../../../static/images/car.jpg",
        selectedDuration: 0,
        durationOptions: [
          {
            label: "1 Hour",
            value: "HOUR"
          },
          {
            label: "4 Hours",
            value: "FOUR_HOURS"
          },
          {
            label: "1 Day",
            value: "DAY"
          },
          {
            label: "1 Week",
            value: "WEEK"
          }
        ],
        showModal: false,
        data: false
      };
    },
    async mounted() {
      await this.loadBookings();
    },
    onLoad() {
    },
    methods: {
      async loadBookings() {
        const user = uni.getStorageSync("userInfo");
        const token = String(uni.getStorageSync("token"));
        try {
          const res = await uni.request({
            url: `${this.$baseURL}/api/bookings/getAllUndo/${user.userId}`,
            method: "GET",
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          if (res.statusCode === 200) {
            if (res.data.length > 0) {
              this.data = true;
            }
            this.orderList = res.data.map((order) => ({
              ...order,
              showModal: false
            }));
          } else {
            uni.showToast({
              title: "Data loading failed",
              icon: "none"
            });
          }
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        } finally {
          this.isLoading = false;
        }
      },
      formatDate(dateString) {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        const hours = String(date.getHours()).padStart(2, "0");
        const minutes = String(date.getMinutes()).padStart(2, "0");
        return ` ${day}/${month}/${year} ${hours}:${minutes}`;
      },
      tabClick(item) {
        this.tabsIndex = item.tabId;
      },
      getStatusClass(status) {
        return {
          "status-red": status == 1,
          "status-2": status == 2,
          "status-green": status == 3
        };
      },
      async cancelClick(item) {
        uni.showModal({
          title: "Tips",
          content: "Are you sure you want to cancel this order?",
          cancelText: "cancel",
          confirmText: "confirm",
          success: async (res) => {
            if (res.confirm) {
              const token = String(uni.getStorageSync("token"));
              try {
                uni.showLoading({
                  title: "loading...",
                  mask: true
                });
                const res2 = await uni.request({
                  url: `${this.$baseURL}/api/bookings/cancel/${item.id}`,
                  method: "POST",
                  header: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                  }
                });
                if (res2.statusCode === 200) {
                  uni.showToast({
                    title: res2.data.message,
                    icon: "none",
                    duration: 2e3
                  });
                  this.loadBookings();
                } else {
                  uni.showToast({
                    title: res2.data.message,
                    icon: "none",
                    duration: 2e3
                  });
                }
              } catch (err) {
                uni.showToast({
                  title: "Network Error",
                  icon: "none"
                });
              } finally {
                uni.hideLoading();
                this.isLoading = false;
              }
            }
          }
        });
      },
      payClick(item) {
        uni.navigateTo({
          url: `/pages/payment/payh5?id=${item.id}`
        });
      },
      selectDuration(index) {
        this.selectedDuration = index;
      },
      async handleConfirm(item) {
        uni.getStorageSync("userInfo");
        const token = String(uni.getStorageSync("token"));
        try {
          const res = await uni.request({
            url: `${this.$baseURL}/api/bookings/extend/${item.id}`,
            method: "POST",
            data: {
              hireType: this.durationOptions[this.selectedDuration].value
            },
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          if (res.statusCode === 200) {
            const extendId = res.data.orderId;
            uni.showToast({
              title: res.data.message,
              icon: "none",
              duration: 2e3
            });
            uni.navigateTo({
              url: `/pages/payment/payh5?id=${extendId}`
            });
          } else {
            uni.showToast({
              title: res.data,
              icon: "none",
              duration: 2e3
            });
          }
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        } finally {
          this.isLoading = false;
        }
        item.showModal = false;
      },
      handleCancel(item) {
        item.showModal = false;
      }
    }
  };
  function _sfc_render$e(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "content" }, [
      !$data.data ? (vue.openBlock(), vue.createElementBlock("view", {
        key: 0,
        class: "none"
      }, [
        vue.createElementVNode("text", null, "There has no order!")
      ])) : vue.createCommentVNode("v-if", true),
      vue.createElementVNode("view", { class: "me-center" }, [
        vue.createElementVNode("view", { class: "list-tab" }, [
          (vue.openBlock(true), vue.createElementBlock(
            vue.Fragment,
            null,
            vue.renderList($data.orderList, (item, index) => {
              return vue.openBlock(), vue.createElementBlock("ul", {
                class: "item-ul",
                key: index
              }, [
                item.status == $data.tabsIndex || $data.tabsIndex == 0 ? (vue.openBlock(), vue.createElementBlock("li", { key: 0 }, [
                  vue.createElementVNode("view", { class: "item-top" }, [
                    vue.createElementVNode("view", null, [
                      vue.createElementVNode("text", null, "OrderID: "),
                      vue.createElementVNode(
                        "text",
                        null,
                        vue.toDisplayString(item.id),
                        1
                        /* TEXT */
                      )
                    ])
                  ]),
                  vue.createElementVNode("view", { class: "dashed-line" }),
                  vue.createElementVNode("view", { class: "item-content" }, [
                    vue.createElementVNode("view", { class: "item-left" }, [
                      vue.createElementVNode("image", {
                        src: $data.picUrl,
                        alt: "Order Image",
                        style: { "width": "70px", "height": "70px", "border-radius": "5px" }
                      }, null, 8, ["src"])
                    ]),
                    vue.createElementVNode("view", {
                      class: "item-right",
                      onClick: ($event) => _ctx.goDetail(item)
                    }, [
                      vue.createElementVNode("view", { class: "item-right-v1 induce" }, [
                        vue.createElementVNode(
                          "text",
                          { class: "rate-text1" },
                          "Order Time: " + vue.toDisplayString($options.formatDate(item.startTime)),
                          1
                          /* TEXT */
                        )
                      ]),
                      vue.createElementVNode("view", { class: "item-right-v1 induce" }, [
                        vue.createElementVNode(
                          "text",
                          { class: "rate-text1" },
                          "Start Time: " + vue.toDisplayString($options.formatDate(item.startTime)),
                          1
                          /* TEXT */
                        )
                      ]),
                      vue.createElementVNode("view", { class: "item-right-v1 induce" }, [
                        vue.createElementVNode(
                          "text",
                          { class: "rate-text1" },
                          "Hire Period: " + vue.toDisplayString(item.hirePeriod),
                          1
                          /* TEXT */
                        )
                      ]),
                      vue.createElementVNode("view", { class: "item-right-v2" }, [
                        vue.createElementVNode("view", { class: "v2-fh" }, [
                          vue.createTextVNode("£ "),
                          vue.createElementVNode(
                            "text",
                            { class: "v2-price" },
                            vue.toDisplayString(item.price),
                            1
                            /* TEXT */
                          )
                        ])
                      ])
                    ], 8, ["onClick"])
                  ]),
                  vue.createElementVNode("view", { class: "dashed-line" }),
                  vue.createElementVNode("view", { class: "item-btom" }, [
                    vue.createElementVNode("button", {
                      class: "item-btom-btn",
                      onClick: ($event) => item.showModal = true
                    }, "Extend", 8, ["onClick"]),
                    item.showModal ? (vue.openBlock(), vue.createElementBlock("view", {
                      key: 0,
                      class: "modal"
                    }, [
                      vue.createElementVNode("view", { class: "modal-content" }, [
                        vue.createElementVNode("view", null, "Extend the Order"),
                        vue.createElementVNode("view", { class: "section-title" }, "Select Duration"),
                        vue.createElementVNode("view", { class: "duration-section" }, [
                          vue.createElementVNode("view", { class: "duration-grid" }, [
                            (vue.openBlock(true), vue.createElementBlock(
                              vue.Fragment,
                              null,
                              vue.renderList($data.durationOptions, (option, index2) => {
                                return vue.openBlock(), vue.createElementBlock("view", {
                                  key: index2,
                                  class: vue.normalizeClass(["duration-item", { active: $data.selectedDuration === index2 }]),
                                  onClick: ($event) => $options.selectDuration(index2)
                                }, [
                                  vue.createElementVNode(
                                    "text",
                                    { class: "label" },
                                    vue.toDisplayString(option.label),
                                    1
                                    /* TEXT */
                                  )
                                ], 10, ["onClick"]);
                              }),
                              128
                              /* KEYED_FRAGMENT */
                            ))
                          ])
                        ]),
                        vue.createElementVNode("view", { class: "modal-buttons" }, [
                          vue.createElementVNode("button", {
                            onClick: ($event) => $options.handleConfirm(item)
                          }, "Confirm", 8, ["onClick"]),
                          vue.createElementVNode("button", {
                            onClick: ($event) => $options.handleCancel(item)
                          }, "Cancel", 8, ["onClick"])
                        ])
                      ])
                    ])) : vue.createCommentVNode("v-if", true)
                  ])
                ])) : vue.createCommentVNode("v-if", true)
              ]);
            }),
            128
            /* KEYED_FRAGMENT */
          ))
        ])
      ])
    ]);
  }
  const PagesMyorderUnuseUnuse = /* @__PURE__ */ _export_sfc(_sfc_main$f, [["render", _sfc_render$e], ["__scopeId", "data-v-f4e2d880"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/myorder/unuse/unuse.vue"]]);
  const _sfc_main$e = {
    data() {
      return {
        tabsIndex: 0,
        orderList: [],
        userInfo: uni.getStorageSync("userInfo"),
        isLoading: true,
        picUrl: "../../../static/images/car.jpg",
        selectedDuration: 0,
        durationOptions: [
          {
            label: "1 Hour",
            value: "HOUR"
          },
          {
            label: "4 Hours",
            value: "FOUR_HOURS"
          },
          {
            label: "1 Day",
            value: "DAY"
          },
          {
            label: "1 Week",
            value: "WEEK"
          }
        ],
        showModal: false,
        data: false
      };
    },
    async mounted() {
      await this.loadBookings();
    },
    onLoad() {
    },
    methods: {
      async loadBookings() {
        const user = uni.getStorageSync("userInfo");
        const token = String(uni.getStorageSync("token"));
        try {
          const res = await uni.request({
            url: `${this.$baseURL}/api/bookings/getAllOngoing/${user.userId}`,
            method: "GET",
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          if (res.statusCode === 200) {
            if (res.data.length > 0) {
              this.data = true;
            }
            this.orderList = res.data.map((order) => ({
              ...order,
              showModal: false
            }));
          } else {
            uni.showToast({
              title: "Data loading failed",
              icon: "none"
            });
          }
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        } finally {
          this.isLoading = false;
        }
      },
      formatDate(dateString) {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        const hours = String(date.getHours()).padStart(2, "0");
        const minutes = String(date.getMinutes()).padStart(2, "0");
        return `${day}-${month}-${year} ${hours}:${minutes}`;
      },
      getStatusClass(status) {
        return {
          "status-red": status == 1,
          "status-2": status == 2,
          "status-green": status == 3
        };
      },
      async cancelClick(item) {
        uni.showModal({
          title: "Tips",
          content: "Are you sure you want to cancel this order?",
          cancelText: "cancel",
          confirmText: "confirm",
          success: async (res) => {
            if (res.confirm) {
              const token = String(uni.getStorageSync("token"));
              try {
                uni.showLoading({
                  title: "loading...",
                  mask: true
                });
                const res2 = await uni.request({
                  url: `${this.$baseURL}/api/bookings/cancel/${item.id}`,
                  method: "POST",
                  header: {
                    "Authorization": `Bearer ${token}`,
                    "Content-Type": "application/json"
                  }
                });
                if (res2.statusCode === 200) {
                  uni.showToast({
                    title: res2.data.message,
                    icon: "none",
                    duration: 2e3
                  });
                  this.loadBookings();
                } else {
                  uni.showToast({
                    title: res2.data.message,
                    icon: "none",
                    duration: 2e3
                  });
                }
              } catch (err) {
                uni.showToast({
                  title: "Network Error",
                  icon: "none"
                });
              } finally {
                uni.hideLoading();
                this.isLoading = false;
              }
            }
          }
        });
      },
      payClick(item) {
        uni.navigateTo({
          url: `/pages/payment/payh5?id=${item.id}`
        });
      },
      selectDuration(index) {
        this.selectedDuration = index;
      },
      async handleConfirm(item) {
        formatAppLog("log", "at pages/myorder/doing/doing.vue:220", item.id);
        uni.getStorageSync("userInfo");
        const token = String(uni.getStorageSync("token"));
        try {
          const res = await uni.request({
            url: `${this.$baseURL}/api/bookings/extend/${item.id}`,
            method: "POST",
            data: {
              hireType: this.durationOptions[this.selectedDuration].value
            },
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          if (res.statusCode === 200) {
            const extendId = res.data.orderId;
            uni.showToast({
              title: res.data.message,
              icon: "none",
              duration: 2e3
            });
            uni.navigateTo({
              url: `/pages/payment/payh5?id=${extendId}`
            });
          } else {
            uni.showToast({
              title: res.data,
              icon: "none",
              duration: 2e3
            });
          }
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        } finally {
          this.isLoading = false;
        }
        item.showModal = false;
      },
      handleCancel(item) {
        item.showModal = false;
      }
    }
  };
  function _sfc_render$d(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "content" }, [
      !$data.data ? (vue.openBlock(), vue.createElementBlock("view", {
        key: 0,
        class: "none"
      }, [
        vue.createElementVNode("text", null, "There has no order!")
      ])) : vue.createCommentVNode("v-if", true),
      vue.createElementVNode("view", { class: "me-center" }, [
        vue.createElementVNode("view", { class: "list-tab" }, [
          (vue.openBlock(true), vue.createElementBlock(
            vue.Fragment,
            null,
            vue.renderList($data.orderList, (item, index) => {
              return vue.openBlock(), vue.createElementBlock("ul", {
                class: "item-ul",
                key: index
              }, [
                item.status == $data.tabsIndex || $data.tabsIndex == 0 ? (vue.openBlock(), vue.createElementBlock("li", { key: 0 }, [
                  vue.createElementVNode("view", { class: "item-top" }, [
                    vue.createElementVNode("view", null, [
                      vue.createElementVNode("text", null, "OrderID: "),
                      vue.createElementVNode(
                        "text",
                        null,
                        vue.toDisplayString(item.id),
                        1
                        /* TEXT */
                      )
                    ])
                  ]),
                  vue.createElementVNode("view", { class: "dashed-line" }),
                  vue.createElementVNode("view", { class: "item-content" }, [
                    vue.createElementVNode("view", { class: "item-left" }, [
                      vue.createElementVNode("image", {
                        src: $data.picUrl,
                        alt: "Order Image",
                        style: { "width": "70px", "height": "70px", "border-radius": "5px" }
                      }, null, 8, ["src"])
                    ]),
                    vue.createElementVNode("view", {
                      class: "item-right",
                      onClick: ($event) => _ctx.goDetail(item)
                    }, [
                      vue.createElementVNode("view", { class: "item-right-v1 induce" }, [
                        vue.createElementVNode(
                          "text",
                          { class: "rate-text1" },
                          "Order Time: " + vue.toDisplayString($options.formatDate(item.startTime)),
                          1
                          /* TEXT */
                        )
                      ]),
                      vue.createElementVNode("view", { class: "item-right-v1 induce" }, [
                        vue.createElementVNode(
                          "text",
                          { class: "rate-text1" },
                          "Start Time: " + vue.toDisplayString($options.formatDate(item.startTime)),
                          1
                          /* TEXT */
                        )
                      ]),
                      vue.createElementVNode("view", { class: "item-right-v1 induce" }, [
                        vue.createElementVNode(
                          "text",
                          { class: "rate-text1" },
                          "Hire Period: " + vue.toDisplayString(item.hirePeriod),
                          1
                          /* TEXT */
                        )
                      ]),
                      vue.createElementVNode("view", { class: "item-right-v2" }, [
                        vue.createElementVNode("view", { class: "v2-fh" }, [
                          vue.createTextVNode("£ "),
                          vue.createElementVNode(
                            "text",
                            { class: "v2-price" },
                            vue.toDisplayString(item.price),
                            1
                            /* TEXT */
                          )
                        ])
                      ])
                    ], 8, ["onClick"])
                  ]),
                  vue.createElementVNode("view", { class: "dashed-line" }),
                  vue.createElementVNode("view", { class: "item-btom" }, [
                    vue.createElementVNode("button", {
                      class: "item-btom-btn",
                      onClick: ($event) => item.showModal = true
                    }, "Extend", 8, ["onClick"]),
                    item.showModal ? (vue.openBlock(), vue.createElementBlock("view", {
                      key: 0,
                      class: "modal"
                    }, [
                      vue.createElementVNode("view", { class: "modal-content" }, [
                        vue.createElementVNode("view", null, "Extend the Order"),
                        vue.createElementVNode("view", { class: "section-title" }, "Select Duration"),
                        vue.createElementVNode("view", { class: "duration-section" }, [
                          vue.createElementVNode("view", { class: "duration-grid" }, [
                            (vue.openBlock(true), vue.createElementBlock(
                              vue.Fragment,
                              null,
                              vue.renderList($data.durationOptions, (option, index2) => {
                                return vue.openBlock(), vue.createElementBlock("view", {
                                  key: index2,
                                  class: vue.normalizeClass(["duration-item", { active: $data.selectedDuration === index2 }]),
                                  onClick: ($event) => $options.selectDuration(index2)
                                }, [
                                  vue.createElementVNode(
                                    "text",
                                    { class: "label" },
                                    vue.toDisplayString(option.label),
                                    1
                                    /* TEXT */
                                  )
                                ], 10, ["onClick"]);
                              }),
                              128
                              /* KEYED_FRAGMENT */
                            ))
                          ])
                        ]),
                        vue.createElementVNode("view", { class: "modal-buttons" }, [
                          vue.createElementVNode("button", {
                            onClick: ($event) => $options.handleConfirm(item)
                          }, "Confirm", 8, ["onClick"]),
                          vue.createElementVNode("button", {
                            onClick: ($event) => $options.handleCancel(item)
                          }, "Cancel", 8, ["onClick"])
                        ])
                      ])
                    ])) : vue.createCommentVNode("v-if", true)
                  ])
                ])) : vue.createCommentVNode("v-if", true)
              ]);
            }),
            128
            /* KEYED_FRAGMENT */
          ))
        ])
      ])
    ]);
  }
  const PagesMyorderDoingDoing = /* @__PURE__ */ _export_sfc(_sfc_main$e, [["render", _sfc_render$d], ["__scopeId", "data-v-4d33d24e"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/myorder/doing/doing.vue"]]);
  const _sfc_main$d = {
    data() {
      return {
        tabsIndex: 0,
        orderList: [],
        userInfo: uni.getStorageSync("userInfo"),
        isLoading: true,
        picUrl: "../../../static/images/car.jpg",
        data: false
      };
    },
    async mounted() {
      await this.loadBookings();
    },
    onLoad() {
    },
    methods: {
      async loadBookings() {
        const user = uni.getStorageSync("userInfo");
        const token = String(uni.getStorageSync("token"));
        try {
          const res = await uni.request({
            url: `${this.$baseURL}/api/bookings/getAllFinished/${user.userId}`,
            method: "GET",
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          if (res.statusCode === 200) {
            if (res.data.length > 0) {
              this.data = true;
            }
            this.orderList = res.data.map((order) => ({
              ...order,
              showModal: false
            }));
          } else {
            uni.showToast({ title: "Data loading failed", icon: "none" });
          }
        } catch (err) {
          uni.showToast({ title: "Network Error", icon: "none" });
        } finally {
          this.isLoading = false;
        }
      },
      formatDate(dateString) {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        const hours = String(date.getHours()).padStart(2, "0");
        const minutes = String(date.getMinutes()).padStart(2, "0");
        return `${day}-${month}-${year} ${hours}:${minutes}`;
      },
      tabClick(item) {
        this.tabsIndex = item.tabId;
      },
      getStatusClass(status) {
        return {
          "status-red": status == 1,
          "status-2": status == 2,
          "status-green": status == 3
        };
      }
    }
  };
  function _sfc_render$c(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "content" }, [
      !$data.data ? (vue.openBlock(), vue.createElementBlock("view", {
        key: 0,
        class: "none"
      }, [
        vue.createElementVNode("text", null, "There has no order!")
      ])) : vue.createCommentVNode("v-if", true),
      vue.createElementVNode("view", { class: "me-center" }, [
        vue.createElementVNode("view", { class: "list-tab" }, [
          (vue.openBlock(true), vue.createElementBlock(
            vue.Fragment,
            null,
            vue.renderList($data.orderList, (item, index) => {
              return vue.openBlock(), vue.createElementBlock("ul", {
                class: "item-ul",
                key: index
              }, [
                item.status == $data.tabsIndex || $data.tabsIndex == 0 ? (vue.openBlock(), vue.createElementBlock("li", { key: 0 }, [
                  vue.createElementVNode("view", { class: "item-top" }, [
                    vue.createElementVNode("view", null, [
                      vue.createElementVNode("text", null, "OrderID: "),
                      vue.createElementVNode(
                        "text",
                        null,
                        vue.toDisplayString(item.id),
                        1
                        /* TEXT */
                      )
                    ])
                  ]),
                  vue.createElementVNode("view", { class: "dashed-line" }),
                  vue.createElementVNode("view", { class: "item-content" }, [
                    vue.createElementVNode("view", { class: "item-left" }, [
                      vue.createElementVNode("image", {
                        src: $data.picUrl,
                        alt: "Order Image",
                        style: { "width": "70px", "height": "70px", "border-radius": "5px" }
                      }, null, 8, ["src"])
                    ]),
                    vue.createElementVNode("view", {
                      class: "item-right",
                      onClick: ($event) => _ctx.goDetail(item)
                    }, [
                      vue.createElementVNode("view", { class: "item-right-v1 induce" }, [
                        vue.createElementVNode(
                          "text",
                          { class: "rate-text1" },
                          "Order Time: " + vue.toDisplayString($options.formatDate(item.startTime)),
                          1
                          /* TEXT */
                        )
                      ]),
                      vue.createElementVNode("view", { class: "item-right-v1 induce" }, [
                        vue.createElementVNode(
                          "text",
                          { class: "rate-text1" },
                          "Start Time: " + vue.toDisplayString($options.formatDate(item.startTime)),
                          1
                          /* TEXT */
                        )
                      ]),
                      vue.createElementVNode("view", { class: "item-right-v1 induce" }, [
                        vue.createElementVNode(
                          "text",
                          { class: "rate-text1" },
                          "Hire Period: " + vue.toDisplayString(item.hirePeriod),
                          1
                          /* TEXT */
                        )
                      ]),
                      vue.createElementVNode("view", { class: "item-right-v2" }, [
                        vue.createElementVNode("view", { class: "v2-fh" }, [
                          vue.createTextVNode("£ "),
                          vue.createElementVNode(
                            "text",
                            { class: "v2-price" },
                            vue.toDisplayString(item.price),
                            1
                            /* TEXT */
                          )
                        ])
                      ])
                    ], 8, ["onClick"])
                  ]),
                  vue.createElementVNode("view", { class: "dashed-line" })
                ])) : vue.createCommentVNode("v-if", true)
              ]);
            }),
            128
            /* KEYED_FRAGMENT */
          ))
        ])
      ])
    ]);
  }
  const PagesMyorderDoneDone = /* @__PURE__ */ _export_sfc(_sfc_main$d, [["render", _sfc_render$c], ["__scopeId", "data-v-4df7be5e"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/myorder/done/done.vue"]]);
  const _imports_0$1 = "/static/icons/quick.png";
  const _imports_1 = "/static/icons/card.png";
  const _sfc_main$c = {
    data() {
      return {
        selectedPaymentMethod: null,
        paymentSuccessful: false,
        bankCards: null,
        cardNumber: null,
        showCardSelectModal: false,
        showQuickCardModal: false,
        orderId: null,
        quickCard: "",
        form: {
          number: "",
          code: ""
        },
        expiryDate: "",
        startDate: "",
        fields: [
          {
            key: "number",
            label: "Card Number",
            placeholder: "Enter card number"
          },
          {
            key: "code",
            label: "Card Security Code",
            placeholder: "CVV"
          }
        ]
      };
    },
    async onLoad(options) {
      this.orderId = options.id;
      this.getCard();
    },
    methods: {
      async getCard() {
        const token = String(uni.getStorageSync("token"));
        this.user = uni.getStorageSync("userInfo");
        try {
          uni.showLoading({
            title: "Loadinh...",
            mask: true
          });
          const res = await uni.request({
            url: `${this.$baseURL}/api/bank-payment/check-card/${this.user.userId}`,
            method: "GET",
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          this.bankCards = res.data;
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
          uni.hideLoading();
        } finally {
          uni.hideLoading();
        }
      },
      selectPaymentMethod(method) {
        this.selectedPaymentMethod = method;
      },
      submitPayment() {
        if (this.selectedPaymentMethod) {
          this.paymentSuccessful = true;
          if (this.selectedPaymentMethod === "card") {
            this.submit();
          }
          if (this.selectedPaymentMethod === "Alipay") {
            this.showCardSelectModal = true;
          }
        } else {
          alert("Please select a payment method");
        }
      },
      onExpiryChange(e) {
        this.expiryDate = e.detail.value;
      },
      onStartChange(e) {
        this.startDate = e.detail.value;
      },
      async submitNew() {
        const cardRegex = /^[0-9]{13,16}$/;
        if (!this.form.number) {
          uni.showToast({
            title: "Card number is required",
            icon: "none"
          });
          return;
        } else if (!cardRegex.test(this.form.number)) {
          uni.showToast({
            title: "Card number must be between 13-16 digits",
            icon: "none"
          });
          return;
        }
        if (!/^\d{3}$/.test(this.form.code)) {
          uni.showToast({
            title: "The security code should be three digits",
            icon: "none"
          });
          return;
        }
        if (!this.startDate || !this.expiryDate) {
          uni.showToast({
            title: "Please select the date.",
            icon: "none"
          });
          return;
        }
        const now = /* @__PURE__ */ new Date();
        const start = /* @__PURE__ */ new Date(this.startDate + "-01");
        const expiry = /* @__PURE__ */ new Date(this.expiryDate + "-01");
        if (start >= expiry) {
          uni.showToast({
            title: "The start date cannot be later than or equal to the end date",
            icon: "none"
          });
          return;
        }
        if (expiry < new Date(now.getFullYear(), now.getMonth(), 1)) {
          uni.showToast({
            title: "The start date cannot be earlier than the current month",
            icon: "none"
          });
          return;
        }
        const token = String(uni.getStorageSync("token"));
        this.user = uni.getStorageSync("userInfo");
        try {
          const res = await uni.request({
            url: `${this.$baseURL}/api/bank-payment/newCard/${this.orderId}`,
            method: "POST",
            data: {
              bankCard: this.form.number
            },
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          if (res.statusCode === 200) {
            uni.showToast({
              title: "Payment successful",
              icon: "none"
            });
            this.closeCardSelectModal();
            uni.navigateTo({
              url: "/pages/myorder/orderlist/orderlist"
            });
          } else {
            uni.showToast({
              title: res.data || "Update failed",
              icon: "none"
            });
          }
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        } finally {
        }
      },
      async submit() {
        const token = String(uni.getStorageSync("token"));
        this.user = uni.getStorageSync("userInfo");
        try {
          const res = await uni.request({
            url: `${this.$baseURL}/api/bank-payment/${this.orderId}`,
            method: "POST",
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          if (res.statusCode === 200) {
            uni.showToast({
              title: "Payment successful",
              icon: "none"
            });
            this.closeCardSelectModal();
            uni.navigateTo({
              url: "/pages/myorder/orderlist/orderlist"
            });
          } else {
            uni.showToast({
              title: res.data || "Update failed",
              icon: "none"
            });
          }
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        } finally {
        }
      },
      closeCardSelectModal() {
        this.showCardSelectModal = false;
      },
      closeQuickCardModal() {
        this.showQuickCardModal = false;
      }
    }
  };
  function _sfc_render$b(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock(
      vue.Fragment,
      null,
      [
        vue.createElementVNode("view", { class: "payment-container" }, [
          vue.createElementVNode("view", { class: "payment-header" }, [
            vue.createElementVNode("text", { class: "title" }, "Select Payment Method")
          ]),
          vue.createCommentVNode(" Payment method selection "),
          vue.createElementVNode("view", { class: "payment-method-section" }, [
            vue.createElementVNode("view", { class: "payment-options" }, [
              vue.createElementVNode(
                "view",
                {
                  class: vue.normalizeClass(["payment-option", { selected: $data.selectedPaymentMethod === "card" }]),
                  onClick: _cache[0] || (_cache[0] = ($event) => $options.selectPaymentMethod("card"))
                },
                [
                  vue.createElementVNode("view", { class: "left" }, [
                    vue.createElementVNode("image", {
                      src: _imports_0$1,
                      class: "payment-icon",
                      mode: "aspectFill"
                    }),
                    vue.createElementVNode("text", null, "Quick Payment")
                  ]),
                  $data.selectedPaymentMethod === "card" ? (vue.openBlock(), vue.createElementBlock("view", {
                    key: 0,
                    class: "check-icon"
                  }, "✔")) : vue.createCommentVNode("v-if", true)
                ],
                2
                /* CLASS */
              ),
              vue.createElementVNode(
                "view",
                {
                  class: vue.normalizeClass(["payment-option", { selected: $data.selectedPaymentMethod === "Alipay" }]),
                  onClick: _cache[1] || (_cache[1] = ($event) => $options.selectPaymentMethod("Alipay"))
                },
                [
                  vue.createElementVNode("view", { class: "left" }, [
                    vue.createElementVNode("image", {
                      src: _imports_1,
                      class: "payment-icon",
                      mode: "aspectFill"
                    }),
                    vue.createElementVNode("text", null, "New Card Payment")
                  ]),
                  $data.selectedPaymentMethod === "Alipay" ? (vue.openBlock(), vue.createElementBlock("view", {
                    key: 0,
                    class: "check-icon"
                  }, "✔")) : vue.createCommentVNode("v-if", true)
                ],
                2
                /* CLASS */
              )
            ]),
            vue.createElementVNode("view", { class: "payment-btn" }, [
              vue.createElementVNode("button", {
                onClick: _cache[2] || (_cache[2] = (...args) => $options.submitPayment && $options.submitPayment(...args))
              }, "Payment")
            ])
          ])
        ]),
        $data.showCardSelectModal ? (vue.openBlock(), vue.createElementBlock("view", {
          key: 0,
          class: "card-select-modal"
        }, [
          vue.createElementVNode("view", { class: "card-options" }, [
            (vue.openBlock(true), vue.createElementBlock(
              vue.Fragment,
              null,
              vue.renderList($data.fields, (item, index) => {
                return vue.openBlock(), vue.createElementBlock("view", {
                  class: "form-group",
                  key: index
                }, [
                  vue.createElementVNode("text", { class: "label" }, [
                    vue.createTextVNode(
                      vue.toDisplayString(item.label) + " ",
                      1
                      /* TEXT */
                    ),
                    vue.createElementVNode("text", { class: "required" }, "*")
                  ]),
                  vue.withDirectives(vue.createElementVNode("input", {
                    class: "input-field",
                    placeholder: item.placeholder,
                    "onUpdate:modelValue": ($event) => $data.form[item.key] = $event
                  }, null, 8, ["placeholder", "onUpdate:modelValue"]), [
                    [vue.vModelText, $data.form[item.key]]
                  ])
                ]);
              }),
              128
              /* KEYED_FRAGMENT */
            )),
            vue.createElementVNode("view", { class: "form-group" }, [
              vue.createElementVNode("text", { class: "label" }, [
                vue.createTextVNode("Start Date"),
                vue.createElementVNode("text", { class: "required" }, "*")
              ]),
              vue.createElementVNode(
                "picker",
                {
                  mode: "date",
                  fields: "month",
                  onChange: _cache[3] || (_cache[3] = (...args) => $options.onStartChange && $options.onStartChange(...args))
                },
                [
                  vue.createElementVNode(
                    "view",
                    { class: "picker" },
                    vue.toDisplayString($data.startDate || "Select MM/YYYY"),
                    1
                    /* TEXT */
                  )
                ],
                32
                /* NEED_HYDRATION */
              )
            ]),
            vue.createElementVNode("view", { class: "form-group" }, [
              vue.createElementVNode("text", { class: "label" }, [
                vue.createTextVNode("Expiry Date "),
                vue.createElementVNode("text", { class: "required" }, "*")
              ]),
              vue.createElementVNode(
                "picker",
                {
                  mode: "date",
                  fields: "month",
                  onChange: _cache[4] || (_cache[4] = (...args) => $options.onExpiryChange && $options.onExpiryChange(...args))
                },
                [
                  vue.createElementVNode(
                    "view",
                    { class: "picker" },
                    vue.toDisplayString($data.expiryDate || "Select MM/YYYY"),
                    1
                    /* TEXT */
                  )
                ],
                32
                /* NEED_HYDRATION */
              )
            ]),
            vue.createElementVNode("button", {
              onClick: _cache[5] || (_cache[5] = ($event) => $options.submitNew())
            }, "submit"),
            vue.createElementVNode("button", {
              onClick: _cache[6] || (_cache[6] = (...args) => $options.closeCardSelectModal && $options.closeCardSelectModal(...args))
            }, "Close")
          ])
        ])) : vue.createCommentVNode("v-if", true)
      ],
      64
      /* STABLE_FRAGMENT */
    );
  }
  const PagesPaymentChoosePayChoosePay = /* @__PURE__ */ _export_sfc(_sfc_main$c, [["render", _sfc_render$b], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/payment/choosePay/choosePay.vue"]]);
  const _sfc_main$b = {
    data() {
      return {
        email: "",
        codeSent: false,
        resetLoading: false,
        placeholderStyle: "color: #2c3e50; font-size: 14px;"
      };
    },
    methods: {
      async sendCode() {
        String(uni.getStorageSync("token"));
        try {
          uni.showLoading({
            title: "Loading...",
            mask: true
          });
          const res = await uni.request({
            url: `${this.$baseURL}/api/auth/forgot-password`,
            method: "POST",
            data: {
              email: this.email
            },
            header: {
              "Content-Type": "application/json"
            }
          });
          if (res.statusCode === 200) {
            this.codeSent = true;
            uni.showToast({
              title: res.data.message,
              icon: "none",
              duration: 2e3
            });
          } else {
            uni.showToast({
              title: res.data.error || res.data.email,
              icon: "none",
              duration: 2e3
            });
          }
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        } finally {
          uni.hideLoading();
        }
      },
      async resetPassword() {
        if (this.newPassword !== this.confirmPassword) {
          uni.showToast({
            title: "The passwords entered twice are inconsistent",
            icon: "none",
            duration: 2e3
          });
          return;
        }
        this.resetLoading = true;
        try {
          const [err, res] = await uni.request({
            url: `${this.$baseURL}/api/auth/reset-password`,
            method: "POST",
            data: {
              email: this.email,
              resetCode: this.resetCode,
              newPassword: this.newPassword
            },
            header: {
              "Content-Type": "application/json"
            }
          }).then((res2) => [null, res2]).catch((err2) => [err2, null]);
          if (err || res.statusCode !== 200) {
            let errorMessage = "Password reset failed. Please try again later";
            if (res == null ? void 0 : res.data) {
              if (res.data.error) {
                errorMessage = res.data.error;
              } else if (res.data.email) {
                errorMessage = res.data.email;
              } else if (res.data.resetCode) {
                errorMessage = res.data.resetCode;
              } else if (res.data.newPassword) {
                errorMessage = res.data.newPassword;
              }
            } else if (err) {
              errorMessage = err.message || "Network Error";
            }
            uni.showToast({
              title: errorMessage,
              icon: "none",
              duration: 2e3
            });
            throw new Error(errorMessage);
          }
          this.successMessage = res.data.message || "Password reset successfully!";
          uni.showToast({
            title: this.successMessage,
            icon: "none",
            duration: 2e3
          });
          setTimeout(() => {
            uni.navigateTo({
              url: "/pages/UserLogin/UserLogin"
            });
          }, 3e3);
        } catch (error) {
          formatAppLog("error", "at pages/information/findPassword/findPassword.vue:152", "Reset password error:", error);
        } finally {
          this.resetLoading = false;
        }
      }
    }
  };
  function _sfc_render$a(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "page" }, [
      vue.createElementVNode("view", { class: "header" }, [
        vue.createElementVNode("text", null, "Welcome to ScootGo")
      ]),
      vue.createElementVNode("view", { class: "container" }, [
        vue.createElementVNode("view", { class: "title" }, [
          vue.createElementVNode("text", null, "Find your Password")
        ]),
        !$data.codeSent ? (vue.openBlock(), vue.createElementBlock("view", { key: 0 }, [
          vue.createElementVNode("form", null, [
            vue.createElementVNode("view", { class: "form-group" }, [
              vue.withDirectives(vue.createElementVNode("input", {
                type: "tel",
                "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => $data.email = $event),
                placeholder: "Please enter your email",
                "placeholder-style": $data.placeholderStyle,
                class: "input-field"
              }, null, 8, ["placeholder-style"]), [
                [vue.vModelText, $data.email]
              ])
            ]),
            vue.createElementVNode("view", {
              class: "login-btn",
              onClick: _cache[1] || (_cache[1] = ($event) => $options.sendCode())
            }, "Send")
          ])
        ])) : vue.createCommentVNode("v-if", true),
        $data.codeSent ? (vue.openBlock(), vue.createElementBlock("view", {
          key: 1,
          class: "reset-section"
        }, [
          vue.createElementVNode("view", { class: "form-group" }, [
            vue.withDirectives(vue.createElementVNode("input", {
              type: "tel",
              "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => _ctx.resetCode = $event),
              placeholder: "Please enter the verification code received",
              "placeholder-style": $data.placeholderStyle,
              class: "input-field"
            }, null, 8, ["placeholder-style"]), [
              [vue.vModelText, _ctx.resetCode]
            ])
          ]),
          vue.createElementVNode("view", { class: "form-group" }, [
            vue.withDirectives(vue.createElementVNode("input", {
              type: "password",
              "onUpdate:modelValue": _cache[3] || (_cache[3] = ($event) => _ctx.newPassword = $event),
              placeholder: "Please set a new password",
              "placeholder-style": $data.placeholderStyle,
              class: "input-field"
            }, null, 8, ["placeholder-style"]), [
              [vue.vModelText, _ctx.newPassword]
            ])
          ]),
          vue.createElementVNode("view", { class: "form-group" }, [
            vue.withDirectives(vue.createElementVNode("input", {
              type: "password",
              "onUpdate:modelValue": _cache[4] || (_cache[4] = ($event) => _ctx.confirmPassword = $event),
              placeholder: "Please enter the new password again",
              "placeholder-style": $data.placeholderStyle,
              class: "input-field"
            }, null, 8, ["placeholder-style"]), [
              [vue.vModelText, _ctx.confirmPassword]
            ])
          ]),
          vue.createElementVNode("view", {
            class: "login-btn",
            onClick: _cache[5] || (_cache[5] = ($event) => $options.resetPassword())
          }, "Reset")
        ])) : vue.createCommentVNode("v-if", true)
      ])
    ]);
  }
  const PagesInformationFindPasswordFindPassword = /* @__PURE__ */ _export_sfc(_sfc_main$b, [["render", _sfc_render$a], ["__scopeId", "data-v-98f58a0b"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/information/findPassword/findPassword.vue"]]);
  const _sfc_main$a = {
    data() {
      return {
        card: null
      };
    },
    onLoad() {
      this.getCard();
    },
    mounted() {
      this.getCard();
    },
    methods: {
      async getCard() {
        const token = String(uni.getStorageSync("token"));
        this.user = uni.getStorageSync("userInfo");
        try {
          uni.showLoading({ title: "Loading...", mask: true });
          const res = await uni.request({
            url: `${this.$baseURL}/api/bank-payment/check-card/${this.user.userId}`,
            method: "GET",
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          this.card = res.data;
        } catch (err) {
          uni.showToast({ title: "Network Error", icon: "none" });
        } finally {
          uni.hideLoading();
          this.isLoading = false;
        }
      },
      addCard() {
        uni.navigateTo({
          url: "../addCard/addCard"
        });
      }
    }
  };
  function _sfc_render$9(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "container" }, [
      vue.createElementVNode("view", { class: "card-list" }, [
        vue.createElementVNode("view", { class: "card" }, [
          $data.card ? (vue.openBlock(), vue.createElementBlock("view", {
            key: 0,
            class: "card-info"
          }, [
            vue.createElementVNode(
              "text",
              { class: "card-number" },
              vue.toDisplayString($data.card.maskedCard),
              1
              /* TEXT */
            )
          ])) : vue.createCommentVNode("v-if", true)
        ])
      ]),
      vue.createElementVNode("button", {
        class: "add-card-btn",
        onClick: _cache[0] || (_cache[0] = (...args) => $options.addCard && $options.addCard(...args))
      }, "Update the Card")
    ]);
  }
  const PagesPaymentCardCard = /* @__PURE__ */ _export_sfc(_sfc_main$a, [["render", _sfc_render$9], ["__scopeId", "data-v-90c4889f"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/payment/card/card.vue"]]);
  const _sfc_main$9 = {
    data() {
      return {
        card: {
          cardNumber: ""
        },
        user: null,
        isLoading: false,
        placeholderStyle: "color: #2c3e50; font-size: 14px;"
      };
    },
    methods: {
      async submitCard() {
        if (!this.card.cardNumber) {
          uni.showToast({
            title: "Please complete the bank card information",
            icon: "none"
          });
          return;
        }
        const cardRegex = /^[0-9]{13,16}$/;
        if (!cardRegex.test(this.card.cardNumber)) {
          uni.showToast({
            title: "Card number must be between 13-16 digits",
            icon: "none"
          });
          return;
        }
        const token = uni.getStorageSync("token");
        this.user = uni.getStorageSync("userInfo");
        if (!token || !this.user) {
          uni.showToast({
            title: "User not logged in",
            icon: "none"
          });
          return;
        }
        try {
          this.isLoading = true;
          uni.showLoading({ title: "Loading...", mask: true });
          const res = await uni.request({
            url: `${this.$baseURL}/api/users/updateBankCard/${this.user.userId}`,
            method: "POST",
            data: {
              bankCard: this.card.cardNumber
            },
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          if (res.data.success) {
            uni.showToast({
              title: "Bank card updated successfully",
              icon: "none"
            });
          } else {
            uni.showToast({
              title: res.data.message || "Update failed",
              icon: "none"
            });
          }
        } catch (err) {
          formatAppLog("error", "at pages/payment/addCard/addCard.vue:79", err);
          uni.showToast({
            title: "Network error",
            icon: "none"
          });
        } finally {
          uni.hideLoading();
          this.isLoading = false;
        }
        uni.navigateBack();
      }
    }
  };
  function _sfc_render$8(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "container" }, [
      vue.createElementVNode("view", { class: "form-item" }, [
        vue.createElementVNode("text", { class: "label" }, "Card Number"),
        vue.withDirectives(vue.createElementVNode("input", {
          class: "input",
          "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => $data.card.cardNumber = $event),
          type: "number",
          placeholder: "Please enter the number(13-19)",
          "placeholder-style": $data.placeholderStyle
        }, null, 8, ["placeholder-style"]), [
          [vue.vModelText, $data.card.cardNumber]
        ])
      ]),
      vue.createElementVNode("button", {
        class: "submit-btn",
        onClick: _cache[1] || (_cache[1] = (...args) => $options.submitCard && $options.submitCard(...args))
      }, "Submit")
    ]);
  }
  const PagesPaymentAddCardAddCard = /* @__PURE__ */ _export_sfc(_sfc_main$9, [["render", _sfc_render$8], ["__scopeId", "data-v-6185ad71"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/payment/addCard/addCard.vue"]]);
  const _sfc_main$8 = {
    data() {
      return {
        scooters: [],
        isLoading: true,
        carImg: "/static/images/scooter.png",
        speedImg: "/static/icons/speed.png",
        batteryImg: "/static/icons/battery.png",
        scooterImg: "/static/icons/scooter.png",
        startTime: "",
        endTime: "",
        selectedSite: null,
        storeID: null,
        hireType: null,
        currentUser: {
          userId: null
        },
        user: null,
        error: null
      };
    },
    async onLoad() {
      this.hireType = uni.getStorageSync("hireType");
      this.startTime = uni.getStorageSync("startTime");
      this.endTime = uni.getStorageSync("endTime");
      this.selectedSite = uni.getStorageSync("selectedStore");
      const userInfo = uni.getStorageSync("userInfo");
      if (userInfo) {
        this.currentUser.userId = userInfo.userId || null;
      }
      await this.queryAvailableScooters();
    },
    methods: {
      async queryAvailableScooters() {
        try {
          const token = String(uni.getStorageSync("token"));
          this.user = uni.getStorageSync("userInfo");
          let startTime = "";
          if (this.startDate && this.startTime) {
            const startDateTime = `${this.startDate} ${this.startTime}`;
            startTime = startDateTime.replace("T", " ") + ":00";
          }
          const res = await uni.request({
            url: `${this.$baseURL}/api/scooters/getScootersAvailable/${this.user.userId}/${this.selectedSite.id}`,
            method: "POST",
            data: {
              hireType: this.hireType,
              startTime: this.startTime
            },
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          if (res.statusCode === 200) {
            this.scooters = res.data;
          } else {
            this.error = res.data;
            uni.showToast({
              title: `${res.data}`,
              icon: "none"
            });
          }
        } catch (err) {
          formatAppLog("error", "at pages/chooseCar/chooseCar.vue:121", "Network error:", err);
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        } finally {
          this.isLoading = false;
        }
      },
      showScooterDetail(scooter) {
        uni.navigateTo({
          url: `/pages/cardetail/cardetail?id=${scooter.id}`
        });
      },
      getPrice(scooter) {
        if (this.hireType === "HOUR") {
          return scooter.priceHour;
        } else if (this.hireType === "FOUR_HOURS") {
          return scooter.priceFourHour;
        } else if (this.hireType === "DAY") {
          return scooter.priceDay;
        } else if (this.hireType === "WEEK") {
          return scooter.priceWeek;
        }
        return 0;
      },
      getPriceLabel() {
        switch (this.hireType) {
          case "HOUR":
            return "h";
          case "FOUR_HOURS":
            return "4h";
          case "DAY":
            return "day";
          case "WEEK":
            return "week";
          default:
            return "";
        }
      }
    }
  };
  function _sfc_render$7(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "container" }, [
      vue.createElementVNode("view", { class: "information" }, [
        vue.createElementVNode(
          "view",
          { class: "store" },
          vue.toDisplayString($data.selectedSite.name),
          1
          /* TEXT */
        ),
        vue.createElementVNode(
          "view",
          { class: "time" },
          vue.toDisplayString($data.startTime) + " → " + vue.toDisplayString($data.endTime),
          1
          /* TEXT */
        )
      ]),
      $data.scooters.length > 0 ? (vue.openBlock(true), vue.createElementBlock(
        vue.Fragment,
        { key: 0 },
        vue.renderList($data.scooters, (scooter) => {
          return vue.openBlock(), vue.createElementBlock("view", {
            key: scooter.id,
            class: "scooter-item"
          }, [
            vue.createElementVNode("view", { class: "listA-content" }, [
              vue.createElementVNode("view", { class: "listA-boxC" }, [
                vue.createElementVNode("view", { class: "box-left" }, [
                  vue.createElementVNode("view", { class: "info" }, [
                    vue.createElementVNode("view", { class: "chargin-type" }, [
                      vue.createElementVNode("image", {
                        mode: "widthFix",
                        src: $data.speedImg,
                        class: "icon"
                      }, null, 8, ["src"]),
                      vue.createElementVNode(
                        "text",
                        { class: "speed-text" },
                        vue.toDisplayString(scooter.speed || "0.00") + " km/h",
                        1
                        /* TEXT */
                      )
                    ]),
                    vue.createElementVNode("view", { class: "chargin-type" }, [
                      vue.createElementVNode("image", {
                        mode: "widthFix",
                        src: $data.batteryImg,
                        class: "icon"
                      }, null, 8, ["src"]),
                      vue.createElementVNode(
                        "text",
                        { class: "battery-text" },
                        vue.toDisplayString(scooter.battery || "0.00") + " %",
                        1
                        /* TEXT */
                      )
                    ])
                  ]),
                  vue.createElementVNode("view", { class: "price" }, [
                    vue.createElementVNode("view", { class: "price-item" }, [
                      vue.createElementVNode(
                        "text",
                        { class: "price-value" },
                        vue.toDisplayString($options.getPrice(scooter) || "0.00") + " £/" + vue.toDisplayString($options.getPriceLabel()),
                        1
                        /* TEXT */
                      )
                    ]),
                    vue.createElementVNode("button", {
                      class: "reserve-btn",
                      onClick: ($event) => $options.showScooterDetail(scooter)
                    }, "Reserve", 8, ["onClick"])
                  ])
                ]),
                vue.createElementVNode("view", { class: "site-image" }, [
                  vue.createElementVNode("image", {
                    mode: "heightFix",
                    src: $data.carImg,
                    class: "car-img"
                  }, null, 8, ["src"])
                ])
              ]),
              vue.createElementVNode("view", { class: "listA-boxB" }, [
                vue.createElementVNode("image", {
                  mode: "widthFix",
                  src: $data.scooterImg,
                  class: "car-img"
                }, null, 8, ["src"]),
                vue.createElementVNode(
                  "text",
                  { class: "scooter-id" },
                  vue.toDisplayString(scooter.id),
                  1
                  /* TEXT */
                )
              ])
            ])
          ]);
        }),
        128
        /* KEYED_FRAGMENT */
      )) : $data.error ? (vue.openBlock(), vue.createElementBlock(
        "view",
        {
          key: 1,
          class: "none"
        },
        vue.toDisplayString($data.error),
        1
        /* TEXT */
      )) : (vue.openBlock(), vue.createElementBlock("view", {
        key: 2,
        class: "none"
      }, " NO Scooters to user, please query again "))
    ]);
  }
  const PagesChooseCarChooseCar = /* @__PURE__ */ _export_sfc(_sfc_main$8, [["render", _sfc_render$7], ["__scopeId", "data-v-ce976c48"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/chooseCar/chooseCar.vue"]]);
  const _sfc_main$7 = {
    data() {
      return {
        instructions: [
          {
            icon: "person",
            title: "Register and Log In",
            desc: "You must have a valid card number to register. Forgot password? You can recover it. Agree to the terms and conditions."
          },
          {
            icon: "location",
            title: "Choose Rental Location and Time",
            desc: "Select the scooter station and your preferred rental time slot."
          },
          {
            icon: "compose",
            title: "Confirm Scooter Information",
            desc: "Check the scooter model and availability before confirming."
          },
          {
            icon: "wallet",
            title: "Make Payment",
            desc: "Pay the rental fee and a deposit (£50). If returned late or with battery below 90%, the deposit will be deducted."
          },
          {
            icon: "gear",
            title: "Manage Orders",
            desc: "You can extend your rental, return the scooter early, or on time. Late returns or wrong return locations will forfeit your deposit."
          }
        ]
      };
    }
  };
  function _sfc_render$6(_ctx, _cache, $props, $setup, $data, $options) {
    const _component_uni_icons = resolveEasycom(vue.resolveDynamicComponent("uni-icons"), __easycom_0$2);
    return vue.openBlock(), vue.createElementBlock("view", { class: "instruction-page" }, [
      vue.createElementVNode("view", { class: "instruction-title" }, "How to Rent a Scooter"),
      (vue.openBlock(true), vue.createElementBlock(
        vue.Fragment,
        null,
        vue.renderList($data.instructions, (step, index) => {
          return vue.openBlock(), vue.createElementBlock("view", {
            class: "instruction-step",
            key: index
          }, [
            vue.createVNode(_component_uni_icons, {
              type: step.icon,
              size: "28",
              color: "#014d87",
              class: "step-icon"
            }, null, 8, ["type"]),
            vue.createElementVNode("view", { class: "step-content" }, [
              vue.createElementVNode(
                "view",
                { class: "step-title" },
                "Step " + vue.toDisplayString(index + 1) + ": " + vue.toDisplayString(step.title),
                1
                /* TEXT */
              ),
              vue.createElementVNode(
                "view",
                { class: "step-desc" },
                vue.toDisplayString(step.desc),
                1
                /* TEXT */
              )
            ])
          ]);
        }),
        128
        /* KEYED_FRAGMENT */
      ))
    ]);
  }
  const PagesInformationInstructionInstruction = /* @__PURE__ */ _export_sfc(_sfc_main$7, [["render", _sfc_render$6], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/information/instruction/instruction.vue"]]);
  const _sfc_main$6 = {
    props: {
      list: Array
    },
    methods: {
      clickevent(item) {
        if (item.name == "清除缓存") {
          uni.showModal({
            title: "提示",
            content: "是否要清空缓存？",
            confirmText: "立刻清除",
            success: function(res) {
              if (res.confirm) {
                formatAppLog("log", "at components/set-list/set-list.vue:29", "用户点击确定");
                uni.clearStorage();
                uni.showToast({
                  icon: "none",
                  title: "清除成功"
                });
              } else if (res.cancel) {
                formatAppLog("log", "at components/set-list/set-list.vue:36", "用户点击取消");
              }
            }
          });
        }
        if (item.url) {
          uni.navigateTo({
            url: item.url
          });
        }
      }
    }
  };
  function _sfc_render$5(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "home-list" }, [
      (vue.openBlock(true), vue.createElementBlock(
        vue.Fragment,
        null,
        vue.renderList($props.list, (item, index) => {
          return vue.openBlock(), vue.createElementBlock("view", {
            key: index,
            class: "home-list-item",
            "hover-class": "home-list-hover",
            onClick: ($event) => $options.clickevent(item)
          }, [
            vue.createElementVNode("view", { class: "home-list-item-left" }, [
              vue.createElementVNode(
                "view",
                { class: "home-list-item-text" },
                vue.toDisplayString(item.name),
                1
                /* TEXT */
              )
            ]),
            vue.createElementVNode("view", { class: "home-list-item-right icon iconfont icon-icon-right" })
          ], 8, ["onClick"]);
        }),
        128
        /* KEYED_FRAGMENT */
      )),
      vue.createCommentVNode(' <button class="exit-button" type="default">退出登陆</button> ')
    ]);
  }
  const __easycom_0 = /* @__PURE__ */ _export_sfc(_sfc_main$6, [["render", _sfc_render$5], ["__scopeId", "data-v-cc79563a"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/components/set-list/set-list.vue"]]);
  const _sfc_main$5 = {
    components: {
      SetList: __easycom_0
    },
    data() {
      return {
        list: [
          {
            name: "Account and Security",
            url: "../../../pages/information/change/changeAccount"
          },
          {
            name: "About APP",
            url: "../../../pages/information/app/app"
          }
        ]
      };
    }
  };
  function _sfc_render$4(_ctx, _cache, $props, $setup, $data, $options) {
    const _component_set_list = resolveEasycom(vue.resolveDynamicComponent("set-list"), __easycom_0);
    return vue.openBlock(), vue.createBlock(_component_set_list, { list: $data.list }, null, 8, ["list"]);
  }
  const PagesInformationSetSet = /* @__PURE__ */ _export_sfc(_sfc_main$5, [["render", _sfc_render$4], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/information/set/set.vue"]]);
  const _sfc_main$4 = {
    components: {
      SetList: __easycom_0
    },
    data() {
      return {
        list: [
          {
            name: "Reset Password",
            url: "../../../pages/information/resetPassword/resetPassword"
          }
        ]
      };
    }
  };
  function _sfc_render$3(_ctx, _cache, $props, $setup, $data, $options) {
    const _component_set_list = resolveEasycom(vue.resolveDynamicComponent("set-list"), __easycom_0);
    return vue.openBlock(), vue.createBlock(_component_set_list, { list: $data.list }, null, 8, ["list"]);
  }
  const PagesInformationChangeChangeAccount = /* @__PURE__ */ _export_sfc(_sfc_main$4, [["render", _sfc_render$3], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/information/change/changeAccount.vue"]]);
  const _sfc_main$3 = {
    onLoad() {
      uni.setNavigationBarTitle({
        title: "About APP"
      });
    }
  };
  function _sfc_render$2(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "container" }, [
      vue.createElementVNode("view", { class: "card" }, [
        vue.createElementVNode("view", { class: "title" }, "About This App"),
        vue.createElementVNode("view", { class: "content" }, " This application is designed to provide users with a clean, efficient, and reliable experience. Built with UniApp, it ensures cross-platform compatibility and seamless performance. We focus on simplicity, functionality, and continuous improvement to support your daily needs. "),
        vue.createElementVNode("view", { class: "info" }, [
          vue.createElementVNode("view", null, "Version: v1.0.0"),
          vue.createElementVNode("view", null, "Release Date: May 2025"),
          vue.createElementVNode("view", null, "Developer: ScootGo Team"),
          vue.createCommentVNode(" <view>Contact: support@example.com</view> ")
        ])
      ])
    ]);
  }
  const PagesInformationAppApp = /* @__PURE__ */ _export_sfc(_sfc_main$3, [["render", _sfc_render$2], ["__scopeId", "data-v-37c6afb9"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/information/app/app.vue"]]);
  const _sfc_main$2 = {
    data() {
      return {
        oldPassword: "",
        newPassword: "",
        confirmPassword: "",
        userId: "",
        user: []
      };
    },
    onLoad() {
      this.user = uni.getStorageSync("userInfo");
      if (this.user) {
        this.userId = this.user.userId;
      } else {
        this.userId = null;
      }
    },
    methods: {
      async changePassword() {
        if (this.newPassword !== this.confirmPassword) {
          uni.showToast({
            title: "The new password does not match the confirmed password",
            icon: "none"
          });
          return;
        }
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
        if (!passwordRegex.test(this.newPassword)) {
          uni.showToast({
            title: "The new password must be at least 8 characters long, including at least one capital letter, one lowercase letter and one number",
            icon: "none"
          });
          return;
        }
        try {
          const token = String(uni.getStorageSync("token"));
          const response = await uni.request({
            url: `${this.$baseURL}/api/users/changePassword/${this.userId}`,
            method: "POST",
            data: {
              oldPassword: this.oldPassword,
              newPassword: this.newPassword,
              confirmPassword: this.confirmPassword
            },
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          if (response.statusCode === 200) {
            uni.showToast({
              title: response.data.message,
              icon: "none"
            });
            this.oldPassword = "";
            this.newPassword = "";
            this.confirmPassword = "";
          } else if (response.statusCode === 400) {
            uni.showToast({
              title: response.data,
              icon: "none"
            });
          }
        } catch (e) {
          uni.showToast({
            title: `Password modification failed: ${e.message}`,
            icon: "none"
          });
        }
      }
    }
  };
  function _sfc_render$1(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "container" }, [
      vue.createElementVNode("form", null, [
        vue.createElementVNode("view", { class: "form-group" }, [
          vue.createElementVNode("label", null, "old password:"),
          vue.withDirectives(vue.createElementVNode(
            "input",
            {
              type: "password",
              "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => $data.oldPassword = $event),
              placeholder: "Please enter your old password",
              class: "input-field"
            },
            null,
            512
            /* NEED_PATCH */
          ), [
            [vue.vModelText, $data.oldPassword]
          ])
        ]),
        vue.createElementVNode("view", { class: "form-group" }, [
          vue.createElementVNode("label", null, "new password:"),
          vue.withDirectives(vue.createElementVNode(
            "input",
            {
              type: "password",
              "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => $data.newPassword = $event),
              placeholder: "Please enter your new password",
              class: "input-field"
            },
            null,
            512
            /* NEED_PATCH */
          ), [
            [vue.vModelText, $data.newPassword]
          ])
        ]),
        vue.createElementVNode("view", { class: "form-group" }, [
          vue.createElementVNode("label", null, "Confirm the new password:"),
          vue.withDirectives(vue.createElementVNode(
            "input",
            {
              type: "password",
              "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => $data.confirmPassword = $event),
              placeholder: "Please confirm the new password",
              class: "input-field"
            },
            null,
            512
            /* NEED_PATCH */
          ), [
            [vue.vModelText, $data.confirmPassword]
          ])
        ])
      ]),
      vue.createElementVNode("button", {
        onClick: _cache[3] || (_cache[3] = (...args) => $options.changePassword && $options.changePassword(...args))
      }, "change password")
    ]);
  }
  const PagesInformationResetPasswordResetPassword = /* @__PURE__ */ _export_sfc(_sfc_main$2, [["render", _sfc_render$1], ["__scopeId", "data-v-8d816fce"], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/information/resetPassword/resetPassword.vue"]]);
  const _imports_0 = "/static/icons/store.png";
  const _sfc_main$1 = {
    data() {
      return {
        stores: [],
        selectedStore: null
      };
    },
    async onLoad() {
      await this.loadStores();
      for (const store of this.stores) {
        await this.reverseGeocode(store);
      }
    },
    methods: {
      async loadStores() {
        try {
          const token = String(uni.getStorageSync("token"));
          const res = await uni.request({
            url: `${this.$baseURL}/api/stores/getAll`,
            method: "GET",
            header: {
              "Content-Type": "application/json",
              "Authorization": `Bearer ${token}`
            }
          });
          if (res.statusCode === 200) {
            this.stores = res.data;
          } else {
            uni.showToast({
              title: "Data Loading Failed",
              icon: "none"
            });
          }
        } catch (err) {
          uni.showToast({
            title: "Network Error",
            icon: "none"
          });
        }
      },
      selectStore(store) {
        this.selectedStore = store;
      },
      confirmSelection() {
        uni.setStorageSync("selectedStore", this.selectedStore);
        uni.navigateBack();
      },
      async reverseGeocode(store) {
        var _a;
        try {
          const res = await new Promise((resolve, reject) => {
            uni.request({
              url: `https://restapi.amap.com/v3/geocode/regeo?output=json&location=${store.longitude},${store.latitude}&key=5f722ef9e435cec7d4dba6f5daba0030&radius=1000&extensions=all&language=en`,
              success: resolve,
              fail: reject
            });
          });
          if (res.data.status === "1") {
            this.$set(store, "locationName", ((_a = res.data.regeocode.pois[1]) == null ? void 0 : _a.name) || "No POI found");
            this.$set(store, "locationNum", res.data.regeocode.formatted_address);
          } else {
            this.$set(store, "locationName", "Unable to obtain the location");
          }
        } catch (err) {
          this.$set(store, "locationName", "Location request failed");
          formatAppLog("error", "at pages/chooseStore/chooseStore.vue:95", "The reverse geocoding request failed:", err);
        }
      }
    }
  };
  function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
    return vue.openBlock(), vue.createElementBlock("view", { class: "container" }, [
      vue.createElementVNode("view", { class: "title" }, "Please Select a Store"),
      $data.stores.length > 0 ? (vue.openBlock(), vue.createElementBlock("view", {
        key: 0,
        class: "store-list"
      }, [
        (vue.openBlock(true), vue.createElementBlock(
          vue.Fragment,
          null,
          vue.renderList($data.stores, (store, index) => {
            var _a;
            return vue.openBlock(), vue.createElementBlock("view", {
              key: store.id,
              class: vue.normalizeClass(["store-item", ((_a = $data.selectedStore) == null ? void 0 : _a.id) === store.id ? "selected" : ""]),
              onClick: ($event) => $options.selectStore(store)
            }, [
              vue.createElementVNode("image", {
                src: _imports_0,
                class: "icon"
              }),
              vue.createElementVNode("view", { class: "store-info" }, [
                vue.createElementVNode(
                  "view",
                  { class: "store-name" },
                  vue.toDisplayString(store.name),
                  1
                  /* TEXT */
                ),
                vue.createElementVNode(
                  "view",
                  { class: "store-address" },
                  vue.toDisplayString(store.locationName),
                  1
                  /* TEXT */
                )
              ])
            ], 10, ["onClick"]);
          }),
          128
          /* KEYED_FRAGMENT */
        ))
      ])) : (vue.openBlock(), vue.createElementBlock("view", {
        key: 1,
        class: "loading"
      }, "Loading stores...")),
      vue.createElementVNode("button", {
        type: "primary",
        class: "confirm-btn",
        onClick: _cache[0] || (_cache[0] = (...args) => $options.confirmSelection && $options.confirmSelection(...args)),
        disabled: !$data.selectedStore
      }, " Confirm ", 8, ["disabled"])
    ]);
  }
  const PagesChooseStoreChooseStore = /* @__PURE__ */ _export_sfc(_sfc_main$1, [["render", _sfc_render], ["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/pages/chooseStore/chooseStore.vue"]]);
  __definePage("pages/JudeLoginPage/JudeLoginPage", PagesJudeLoginPageJudeLoginPage);
  __definePage("pages/index", PagesIndex);
  __definePage("pages/UserLogin/UserLogin", PagesUserLoginUserLogin);
  __definePage("pages/information/terms/terms", PagesInformationTermsTerms);
  __definePage("pages/UserRegister/UserRegister", PagesUserRegisterUserRegister);
  __definePage("pages/index/index", PagesIndexIndex);
  __definePage("pages/map/map", PagesMapMap);
  __definePage("pages/now/now", PagesNowNow);
  __definePage("pages/cardetail/cardetail", PagesCardetailCardetail);
  __definePage("pages/UserCenter/UserCenter", PagesUserCenterUserCenter);
  __definePage("pages/feedback/feedbackIndex/feedbackIndex", PagesFeedbackFeedbackIndexFeedbackIndex);
  __definePage("pages/feedback/feedbackList/feedbackList", PagesFeedbackFeedbackListFeedbackList);
  __definePage("pages/feedback/feedback/feedback", PagesFeedbackFeedbackFeedback);
  __definePage("pages/feedback/helpDetail/helpDetail", PagesFeedbackHelpDetailHelpDetail);
  __definePage("pages/feedback/feedbackDetail/feedbackDetail", PagesFeedbackFeedbackDetailFeedbackDetail);
  __definePage("pages/myorder/orderlist/orderlist", PagesMyorderOrderlistOrderlist);
  __definePage("pages/payment/payh5", PagesPaymentPayh5);
  __definePage("pages/webview/webview", PagesWebviewWebview);
  __definePage("pages/myorder/unuse/unuse", PagesMyorderUnuseUnuse);
  __definePage("pages/myorder/doing/doing", PagesMyorderDoingDoing);
  __definePage("pages/myorder/done/done", PagesMyorderDoneDone);
  __definePage("pages/payment/choosePay/choosePay", PagesPaymentChoosePayChoosePay);
  __definePage("pages/information/findPassword/findPassword", PagesInformationFindPasswordFindPassword);
  __definePage("pages/payment/card/card", PagesPaymentCardCard);
  __definePage("pages/payment/addCard/addCard", PagesPaymentAddCardAddCard);
  __definePage("pages/chooseCar/chooseCar", PagesChooseCarChooseCar);
  __definePage("pages/information/instruction/instruction", PagesInformationInstructionInstruction);
  __definePage("pages/information/set/set", PagesInformationSetSet);
  __definePage("pages/information/change/changeAccount", PagesInformationChangeChangeAccount);
  __definePage("pages/information/app/app", PagesInformationAppApp);
  __definePage("pages/information/resetPassword/resetPassword", PagesInformationResetPasswordResetPassword);
  __definePage("pages/chooseStore/chooseStore", PagesChooseStoreChooseStore);
  const _sfc_main = {
    onLaunch: function() {
      formatAppLog("log", "at App.vue:4", "App Launch");
    },
    onShow: function() {
      formatAppLog("log", "at App.vue:7", "App Show");
    },
    onHide: function() {
      formatAppLog("log", "at App.vue:10", "App Hide");
    },
    methods: {}
  };
  const App = /* @__PURE__ */ _export_sfc(_sfc_main, [["__file", "D:/3/Software engineer principles/project/back/SoftwareEngineeringProject/uniapp-project/App.vue"]]);
  function createApp() {
    const app = vue.createVueApp(App);
    app.config.globalProperties.$baseURL = "http://118.24.22.77";
    return {
      app
    };
  }
  const { app: __app__, Vuex: __Vuex__, Pinia: __Pinia__ } = createApp();
  uni.Vuex = __Vuex__;
  uni.Pinia = __Pinia__;
  __app__.provide("__globalStyles", __uniConfig.styles);
  __app__._component.mpType = "app";
  __app__._component.render = () => {
  };
  __app__.mount("#app");
})(Vue);
