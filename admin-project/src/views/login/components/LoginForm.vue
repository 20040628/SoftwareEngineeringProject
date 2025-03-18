<script setup>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import axios from "axios";

// 路由和状态管理
const router = useRouter();
const store = useStore();

// 表单数据
const loginForm = reactive({
  username: "",
  password: "",
});

// 错误消息
const errorMessage = ref("");

// 加载状态
const loading = ref(false);

// 登录函数
const login = async () => {
  // 简单的前端验证
  if (!loginForm.username || !loginForm.password) {
    errorMessage.value = "用户名和密码不能为空";
    return;
  }

  loading.value = true;
  errorMessage.value = "";

  try {
    // 调用登录接口
    const response = await axios.post("http://localhost:8080/api/auth/login", loginForm);

    // 登录成功处理
    if (response.data && response.data.token) {
      store.dispatch("login", {
        token: response.data.token,
        user: {
          userId: response.data.userId,
          username: response.data.username,
          userType: response.data.userType,
          role: response.data.role
        },
      });

      // 根据用户类型跳转
      if (response.data.userType === "ADMIN") {
        router.push("/admin/dashboard");
      } else {
        router.push("/scooters");
      }

      // 显示成功消息（使用浏览器原生 alert）
      alert(`登录成功，欢迎回来，${response.data.username}`);
    }
  } catch (error) {
    // 错误处理
    if (error.response) {
      errorMessage.value = error.response.data.message || "用户名或密码错误";
    } else {
      errorMessage.value = "无法连接到服务器，请稍后再试";
    }
  } finally {
    loading.value = false;
  }
};

// 重置表单
const resetForm = () => {
  loginForm.username = "";
  loginForm.password = "";
  errorMessage.value = "";
};
</script>

<template>
  <div class="login-container">
    <div class="login-form">
      <h2>用户登录</h2>

      <!-- 错误提示 -->
      <div v-if="errorMessage" class="error-message">
        {{ errorMessage }}
      </div>

      <!-- 登录表单 -->
      <form @submit.prevent="login">
        <div class="form-group">
          <label for="username">用户名</label>
          <input
              type="text"
              id="username"
              v-model="loginForm.username"
              placeholder="请输入用户名"
              required
          />
        </div>

        <div class="form-group">
          <label for="password">密码</label>
          <input
              type="password"
              id="password"
              v-model="loginForm.password"
              placeholder="请输入密码"
              required
          />
        </div>

        <div class="form-actions">
          <button type="submit" :disabled="loading">
            {{ loading ? "登录中..." : "登录" }}
          </button>
          <button type="button" @click="resetForm">重置</button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}

.login-form {
  width: 100%;
  max-width: 400px;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #555;
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

input:focus {
  border-color: #4a90e2;
  outline: none;
  box-shadow: 0 0 0 2px rgba(74, 144, 226, 0.2);
}

.error-message {
  padding: 10px;
  margin-bottom: 15px;
  background-color: #ffebee;
  color: #d32f2f;
  border: 1px solid #ffcdd2;
  border-radius: 4px;
  text-align: center;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button[type="submit"] {
  background-color: #4a90e2;
  color: white;
}

button[type="submit"]:hover {
  background-color: #3a7fcb;
}

button[type="button"] {
  background-color: #f5f5f5;
  color: #333;
}

button[type="button"]:hover {
  background-color: #ddd;
}
</style>