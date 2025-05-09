<!-- <template>
  <view>
<<<<<<< HEAD
    <text>The Alipay payment is being invoked....</text>
=======
    <iframe v-if="htmlContent" :srcdoc="htmlContent" style="display:none;"></iframe>
>>>>>>> a8214d7d8a67dbf8b2128232738e272ad97d0500
  </view>
</template> -->

<!-- <script>
export default {
  data() {
    return {
      orderId: null
    };
  },
  onLoad(options) {
    this.orderId = options.id; 
    console.log("Received orderId:", this.orderId);
    this.startAlipay(); 
  },
  methods: {
    async startAlipay() {
      const token = uni.getStorageSync('token');
      try {
        const res = await uni.request({
          url: `${this.$baseURL}/alipay/appPay/${this.orderId}`,
          method: 'GET',
          header: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (res.statusCode === 200 && res.data.orderString) {
          const orderStr = res.data.orderString;

          // 调用原生支付宝支付
          uni.requestPayment({
            provider: 'alipay',
            orderInfo: orderStr, // 后端返回的支付宝 orderString
            success: function (payRes) {
              console.log('Pay Successfully:', payRes);
              uni.showToast({
                title: 'Pay Successfully',
                icon: 'success'
              });
              uni.redirectTo({
                url: '/pages/my-bookings/my-bookings'
              });
            },
            fail: function (err) {
              console.error('Failed to pay:', err);
              uni.showToast({
                title: 'Payment is cancelled',
                icon: 'none'
              });
              // 可选：返回订单页或其他处理
              uni.redirectTo({
                url: '/pages/my-bookings/my-bookings'
              });
            }
          });
        } else {
          console.error('The back end did not return orderString');
          uni.showToast({
            title: 'Payment request failed.',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('Network Error:', error);
        uni.showToast({
          title: 'Network Error',
          icon: 'none'
        });
      }
    }
  }
};
</script> -->
<template>
  <view>
    <text>The Alipay payment is being invoked....</text>
  </view>
</template>

<script>
export default {
  data() {
    return {
      orderId: null
    };
  },
  onLoad(options) {
    this.orderId = options.id; // 从页面参数中获取订单 ID
    console.log("Received orderId:", this.orderId);
    this.startAlipay(); // 页面加载时发起支付
  },
  methods: {
    async startAlipay() {
      const token = uni.getStorageSync('token');
      try {
        const res = await uni.request({
          url: `${this.$baseURL}/alipay/appPay/${this.orderId}`,
          method: 'GET',
          header: {
            'Authorization': `Bearer ${token}`
          }
        });

        if (res.statusCode === 200 && res.data.orderString) {
          const orderStr = res.data.orderString;

          // 调用原生支付宝支付
          uni.requestPayment({
            provider: 'alipay',
            orderInfo: orderStr, // 后端返回的支付宝 orderString
            success: function (payRes) {
              console.log('Pay Successfully:', payRes);
              uni.showToast({
                title: 'Pay Successfully',
                icon: 'success'
              });
              uni.redirectTo({
                url: '/pages/my-bookings/my-bookings'
              });
            },
            fail: function (err) {
              console.error('Failed to pay:', err);
              uni.showToast({
                title: 'Payment is cancelled',
                icon: 'none'
              });
              // 可选：返回订单页或其他处理
              uni.redirectTo({
                url: '/pages/my-bookings/my-bookings'
              });
            }
          });
        } else {
          console.error('The back end did not return orderString');
          uni.showToast({
            title: 'Payment request failed.',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('Network Error:', error);
        uni.showToast({
          title: 'Network Error',
          icon: 'none'
        });
      }
    }
  }
};
</script>

<style>
/* 可自定义样式 */
</style>
