<template>
  <view>
    <!-- 用于自动提交支付表单的 iframe，隐藏 -->
    <iframe v-if="htmlContent" :srcdoc="htmlContent" style="display:none;"></iframe>
  </view>
</template>

<script>
export default {
  data() {
    return {
      htmlContent: '',  // 存储从后端返回的支付表单 HTML
      orderId: null     // 存储订单 ID
    };
  },
  onLoad(item) {
    this.orderId = item.id;  // 获取传递的 orderId
    console.log("Received orderId:", this.orderId);
    this.fetchPayForm();  // 获取支付表单 HTML 内容
  },
  methods: {
    async fetchPayForm() {
      const token = String(uni.getStorageSync('token'));
      try {
        // 从后端获取支付表单的 HTML 内容
        const res = await uni.request({
          url: `${this.$baseURL}/alipay/pay/${this.orderId}`,
          header: {
            'Authorization': `Bearer ${token}`,
          },
        });

        // 假设返回的 HTML 内容是完整的支付表单
        if (res.statusCode === 200 && res.data) {
          this.htmlContent = res.data;  // 将 HTML 内容保存到数据中
          this.submitForm();  // 自动提交表单
        } else {
          console.error('Failed to fetch payment form');
        }
      } catch (error) {
        console.error('Error fetching payment form:', error);
      }
    },

    submitForm() {
      // 动态解析表单内容
      const iframe = document.createElement('iframe');
      iframe.style.display = 'none';  // 隐藏 iframe
      iframe.srcdoc = this.htmlContent;  // 将表单 HTML 内容插入 iframe
      document.body.appendChild(iframe);  // 将 iframe 添加到页面

      // 获取 iframe 中的 form 元素
      const iframeDoc = iframe.contentWindow.document;
      const form = iframeDoc.querySelector('form');
      if (form) {
        // 提取 form 的 action URL 和所有字段
        const formAction = form.action;

        // 如果表单中没有 action，或者它为空，我们可以从后端返回的数据中提取 URL
        if (!formAction || formAction.trim() === '') {
          console.error('Form action URL is missing or empty');
          return;
        }

        // 创建一个新的 form 元素并将字段添加进去
        const newForm = document.createElement('form');
        newForm.action = formAction;  // 设置 form 的 action 属性
        newForm.method = 'POST';  // 设置 form 的提交方法
		newForm.target = '_blank';  // 使用 _blank 打开支付页面的新标签页

        // 将从 iframe 表单中提取的所有字段添加到新表单中
        const inputs = form.querySelectorAll('input');
        inputs.forEach(input => {
          const newInput = document.createElement('input');
          newInput.type = input.type;
          newInput.name = input.name;
          newInput.value = input.value;
          newForm.appendChild(newInput);
        });

        // 将新表单添加到页面并提交
        document.body.appendChild(newForm);
        newForm.submit();  // 提交表单，跳转到支付宝支付页面
      }
    }
  }
};
</script>
