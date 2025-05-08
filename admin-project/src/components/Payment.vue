<template>
  <div class="payment-container">
    <div class="payment">
      <h2 class="title">Payment</h2>
      <div class="form">
        <!-- Card CVC (last 3 digits) -->
        <div class="form-row">
          <div class="form-group col-12 col-md-6">
            <label>Card Verification Code (CVC)</label>
            <input
                v-model="paymentData.cvc"
                type="text"
                maxlength="3"
                placeholder="Last 3 digits on back of card"
                class="input"
                @input="formatCVC"
            />
            <el-alert
                v-if="errors.cvc"
                :title="errors.cvc"
                type="error"
                :closable="false"
                show-icon
                class="custom-alert"
            />
          </div>
        </div>

        <!-- Card Expiration Date -->
        <div class="form-row">
          <div class="form-group col-12 col-md-6">
            <label>Expiration Date</label>
            <input
                v-model="paymentData.expiryDate"
                type="date"
                placeholder="Select date"
                class="input"
            />
            <el-alert
                v-if="errors.expiryDate"
                :title="errors.expiryDate"
                type="error"
                :closable="false"
                show-icon
                class="custom-alert"
            />
          </div>
        </div>

        <!-- Error message -->
        <div class="form-row" v-if="hasError && errorMessage">
          <el-alert
              :title="errorMessage"
              type="error"
              :closable="false"
              show-icon
          />
        </div>

        <!-- Buttons row -->
        <div class="form-row">
          <div class="form-group col-12 col-md-6">
            <button class="button button-submit" @click="submitPayment">Submit Payment</button>
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
        cvc: '',
        expiryDate: ''
      },
      errors: {},
      hasError: false,
      errorMessage: '',
      expiryDateOptions: {
        disabledDate(date) {
          const today = new Date();
          today.setHours(0, 0, 0, 0);
          return date < today;
        }
      }
    };
  },
  methods: {
    formatCVC() {
      // Remove non-numeric characters
      this.paymentData.cvc = this.paymentData.cvc.replace(/\D/g, '');
    },
    validateForm() {
      this.errors = {};
      this.hasError = false;
      this.errorMessage = '';

      // CVC validation
      if (!this.paymentData.cvc) {
        this.errors.cvc = 'CVC is required';
      } else if (this.paymentData.cvc.length !== 3) {
        this.errors.cvc = 'CVC must be 3 digits';
      } else if (!/^\d{3}$/.test(this.paymentData.cvc)) {
        this.errors.cvc = 'CVC must be numeric';
      }

      // Expiry date validation
      if (!this.paymentData.expiryDate) {
        this.errors.expiryDate = 'Expiration date is required';
      } else {
        const today = new Date();
        const expiryDate = new Date(this.paymentData.expiryDate);
        if (expiryDate < today) {
          this.errors.expiryDate = 'Expiration date cannot be before today';
        }
      }

      this.hasError = Object.keys(this.errors).length > 0;
      if (this.hasError) {
        this.errorMessage = 'Please fix the errors above';
      }

      return !this.hasError;
    },
    async submitPayment() {
      if (!this.validateForm()) {
        return;
      }

      try {
        const token = localStorage.getItem('token') || sessionStorage.getItem('token');
        const orderId = this.$route.params.orderId;

        if (!token) {
          this.errorMessage = 'Please login first';
          this.$router.push('/add_order');
          return;
        }

        const payload = {
          securityCode: 123456
        };

        console.log('Request Payload:', payload);

        const res = await axios.post(`http://localhost:8080/api/bank-payment/${orderId}`, payload, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        });

        if (res.status === 200) {
          ElNotification({
            title: "Payment Successful",
            message: 'Your payment has been processed',
            type: "success"
          });
          this.$router.push('/add_order');
        }

      } catch (error) {
        console.error('Payment error:', error);
        if (error.response) {
          console.error('Server Response:', error.response.data);
          switch (error.response.status) {
            case 400:
              this.errors = error.response.data || {};
              if (error.response.data.message) {
                this.errorMessage = 'Error: ' + error.response.data.message;
              } else {
                this.errorMessage = 'Invalid payment details';
              }
              break;
            case 401:
              localStorage.removeItem('token');
              sessionStorage.removeItem('token');
              this.errorMessage = 'Session expired. Please login again.';
              this.$router.push('/add_order');
              break;
            case 403:
              this.errorMessage = 'Forbidden: You do not have permission to perform this action';
              break;
            default:
              this.errorMessage = `Error: ${error.response.data?.message || 'Payment failed'}`;
          }
        } else {
          this.errorMessage = 'Network error: Please check your connection';
        }
        this.hasError = true;
      }
    }

  },
  mounted() {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
      this.errorMessage = 'Please login first';
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
  margin-bottom: 20px;
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
  width: 100%;
}

.button-submit {
  background: #003c51;
  color: white;
}

.custom-alert {
  font-size: 16px;
  width: 99%;
  margin-top: 3px;
  height: 30px;
}

.date-picker {
  width: 550px;
  height: 56px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 14px;
  margin-right: 1%;
}

:deep(.el-input__inner) {
  height: 56px;
  padding: 10px;
  border-radius: 8px;
}
</style>
