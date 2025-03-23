"use strict";
const common_vendor = require("../../common/vendor.js");
const _sfc_main = {
  name: "UserRegister",
  data() {
    return {
      form: {
        username: "",
        password: "",
        email: "",
        mobile: "",
        birthday: "",
        userType: 0
        // default: normal user
      },
      loading: false,
      message: "",
      messageType: "success",
      registeredUser: null,
      errors: {}
    };
  },
  methods: {
    getBirthday(e) {
      this.form.birthday = e.detail.value.split("T")[0];
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
        const [err, res] = await common_vendor.index.request({
          url: "http://localhost:8080/api/auth/register",
          // 后端地址
          method: "POST",
          data: this.form,
          header: { "Content-Type": "application/json" }
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
          common_vendor.index.showToast({
            title: this.message,
            icon: "none",
            duration: 2e3
          });
          throw new Error(errorMessage);
        }
        common_vendor.index.showToast({
          title: "Registration successful!",
          icon: "success",
          duration: 2e3
        });
        this.form = {
          username: "",
          password: "",
          email: "",
          mobile: "",
          birthday: "",
          userType: 0
        };
        setTimeout(() => {
          this.goToLogin();
        }, 2e3);
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/UserRegister/UserRegister.vue:209", "Registration error:", error);
      } finally {
        this.loading = false;
      }
    },
    goToLogin() {
      common_vendor.index.navigateTo({
        url: "/pages/UserLogin/UserLogin"
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $data.form.username,
    b: common_vendor.o(($event) => $data.form.username = $event.detail.value),
    c: $data.errors.username
  }, $data.errors.username ? {
    d: common_vendor.t($data.errors.username)
  } : {}, {
    e: $data.form.password,
    f: common_vendor.o(($event) => $data.form.password = $event.detail.value),
    g: $data.errors.password
  }, $data.errors.password ? {
    h: common_vendor.t($data.errors.password)
  } : {}, {
    i: $data.form.email,
    j: common_vendor.o(($event) => $data.form.email = $event.detail.value),
    k: $data.errors.email
  }, $data.errors.email ? {
    l: common_vendor.t($data.errors.email)
  } : {}, {
    m: $data.form.mobile,
    n: common_vendor.o(($event) => $data.form.mobile = $event.detail.value),
    o: $data.errors.mobile
  }, $data.errors.mobile ? {
    p: common_vendor.t($data.errors.mobile)
  } : {}, {
    q: common_vendor.t($data.form.birthday || "Please select a start date"),
    r: $data.form.birthday,
    s: common_vendor.o((...args) => $options.getBirthday && $options.getBirthday(...args)),
    t: $data.errors.birthday
  }, $data.errors.birthday ? {
    v: common_vendor.t($data.errors.birthday)
  } : {}, {
    w: common_vendor.t($data.loading ? "Registering..." : "Register"),
    x: common_vendor.o((...args) => $options.register && $options.register(...args)),
    y: $data.loading,
    z: common_vendor.o((...args) => $options.goToLogin && $options.goToLogin(...args))
  });
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render], ["__scopeId", "data-v-51e677c0"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/UserRegister/UserRegister.js.map
