<template>
  <div class="payment-container">
    <div class="payment">
      <h2 class="title">Payment</h2>
      <div class="form">
        <div class="form-row">
          <div class="form-group col-12 col-md-6">
            <label>Password</label>
            <input
                v-model="paymentData.securityCode"
                type="password"
                maxlength="6"
                placeholder="Enter Password"
                class="input"
            />
            <span class="error-message" v-if="errors.securityCode">{{ errors.securityCode }}</span>
          </div>
        </div>

        <!-- Buttons row -->
        <div class="form-row">
          <div class="form-group col-12 col-md-6">
            <button class="button button-submit" @click="submitPayment">Submit</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import {ElNotification} from "element-plus";

export default {
  data() {
    return {
      paymentData: {
        securityCode: ''
      },
      errors: {},
      successMessage: ''
    };
  },
  methods: {
    async submitPayment() {
      // 清空错误和消息
      this.errors = {};
      this.successMessage = '';

      // 表单基本验证
      if (!this.paymentData.securityCode) {
        this.errors.securityCode = 'Password is required';
        return;
      }

      try {
        // 获取 token
        const token = localStorage.getItem('token') || sessionStorage.getItem('token');
        const orderId = this.$route.params.orderId;

        if (!token) {
          alert('Please login first');
          this.$router.push('/add_order');
          return;
        }

        // 构造请求体
        const payload = {
          securityCode: this.paymentData.securityCode
        };

        // 提交支付请求
        const res = await axios.post(`http://localhost:8080/api/bank-payment/${orderId}`, payload, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (res.status === 200) {
          ElNotification({
            title: "Successfully",
            message: 'Payment successful',
            type: "success"
          });
          this.$router.push('/add_order');
        }

      } catch (error) {
        if (error.response) {
          switch (error.response.status) {
            case 400:
              this.errors = error.response.data || {};
              alert('Error response: ' + JSON.stringify(error.response.data));
              break;
            case 401:
              localStorage.removeItem('token');
              sessionStorage.removeItem('token');
              alert('Session expired. Please login again.');
              this.$router.push('/add_order');
              break;
            case 403:
              alert('Forbidden: You do not have permission to perform this action');
              break;
            default:
              alert(`Error: ${error.response.data?.message || 'Unknown error occurred'}`);
          }
        } else {
          alert('Network error: Please check your connection');
        }
        console.error('Payment error:', error);
      }
    }
  },
  mounted() {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
      alert('Please login first');
      this.$router.push('/add_order');
    }
  }
};
</script>

<style scoped>
.payment-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.payment {
  width: 40vw;
  height: 40vh;
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

.form {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 20px 40px;
  border-radius: 12px;
  max-width: 100%;
}

.form-row {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  width: 100%;
  margin-bottom: 40px;
}

.form-group {
  display: flex;
  flex-direction: column;
  flex: 1;
  margin-bottom: 10px;
}

label {
  margin-left: 5px;
  font-size: 22px;
  margin-bottom: 6px;
  color: #083435;
  font-weight: bold;
}

.input {
  height: 56px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 14px;
  margin-right: 1%;
}

.input:focus {
  outline: none;
  border-color: #003c51;
  box-shadow: 0 0 1px #003c51;
}

.button {
  padding: 12px;
  border: none;
  font-size: 18px;
  border-radius: 25px;
  height: 56px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: bold;
}

.button-submit {
  background: #003c51;
  color: white;
}

.button-submit:hover {
  background: #003c51;
}

.button-reset {
  background: white;
  color: #003c51;
  font-weight: bold;
  border: 3px solid #003c51;
}

.button-reset:hover {
  background: #f4f4f4;
}

.error-message {
  color: #f56c6c;
  font-size: 12px;
  margin-top: 4px;
}

@media (max-width: 767px) {
  .form-row {
    flex-direction: column;
  }

  .form-group {
    width: 100%;
  }
}

@media (max-width: 767px) {
  .button-group {
    flex-direction: column;
  }

  .button {
    width: 100%;
  }
}
</style>
