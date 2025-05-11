<template>
  <div class="payment-container">
    <div class="payment">
      <h2 class="title">Select Payment Method</h2>
      <div class="payment-options">
        <div
            class="payment-option"
            :class="{ selected: selectedMethod === 'Card' }"
            @click="handlePayment('Card')"
        >
          <div class="payment-icon">
            <img src="/static/center/card.png" alt="Card" />
          </div>
          <span class="payment-label">Default bank card</span>
        </div>

        <div
            class="payment-option"
            :class="{ selected: selectedMethod === 'Alipay' }"
            @click="handlePayment('Alipay')"
        >
          <div class="payment-icon">
            <img src="/static/center/alipay.png" alt="Alipay" />
          </div>
          <span class="payment-label">Alipay</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { ElMessage } from 'element-plus';

export default {
  data() {
    return {
      selectedMethod: null,
      hasError: false,
      errorMessage: ''
    };
  },
  methods: {
    async handlePayment(method) {
      this.selectedMethod = method;
      this.hasError = false;

      const token = localStorage.getItem('token') || sessionStorage.getItem('token');
      const { orderId, userId } = this.$route.params;

      if (method === 'Alipay') {
        try {
          const response = await axios.get(
              `http://localhost:8080/alipay/pay/${orderId}`,
              { headers: { Authorization: `Bearer ${token}` } }
          );
          const newWindow = window.open();
          newWindow.document.write(response.data);
        } catch (error) {
          console.error("Alipay payment error:", error);
          this.hasError = true;
          this.errorMessage = 'Failed to initiate Alipay payment';
        }
      } else {
        this.$router.push({
          name: 'Payment',
          params: { orderId, userId }
        });
      }
    }
  }
}
</script>

<style scoped>
.payment-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f3ffff;
}

.payment {
  width: 40vw;
  min-height: 50vh;
  background-color: white;
  border: 2px solid #ccc;
  border-radius: 12px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.title {
  font-size: 28px;
  font-weight: bold;
  padding-left: 20px;
  padding-bottom: 20px;
  padding-top: 20px;
  border-bottom: 2px solid #003c51;
  text-align: center;
}

.payment-options {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 20px 40px;
  border-radius: 12px;
  max-width: 100%;
}

.payment-option {
  width: 100%;
  display: flex;
  flex-direction: row; /* 改为水平排列 */
  align-items: center;
  padding: 15px 25px;
  border: 1px solid #ccc;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.payment-icon {
  width: 50px;
  height: 50px;
  margin-right: 10px; /* 添加右边距，使图片和文字有间距 */
}

.payment-label {
  font-size: 20px;
  font-weight: bold;
  color: #202b2c;
  margin-left: 3%;
}

.payment-option:hover {
  border-color: #409eff;
  box-shadow: 0 2px 12px 0 rgba(64, 158, 255, 0.1);
}

.payment-icon img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

/* 选中状态 */
.payment-option.selected {
  border-color: #409eff;
  background-color: #ecf5ff;
}
</style>
