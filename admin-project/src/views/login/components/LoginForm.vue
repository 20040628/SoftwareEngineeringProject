<script setup>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import axios from "axios";

const router = useRouter();
const store = useStore();

const loginForm = reactive({
  username: "",
  password: "",
});

const errorMessage = ref("");
const loading = ref(false);

const login = async () => {
  if (!loginForm.username || !loginForm.password) {
    errorMessage.value = "用户名和密码不能为空";
    return;
  }

  loading.value = true;
  errorMessage.value = "";

  try {
    const response = await axios.post("http://localhost:8080/api/auth/login", loginForm);

    if (response.data && response.data.token) {
      console.log('Login response:', response.data);
      await store.dispatch("login", {
        token: response.data.token,
        user: {
          userId: response.data.userId,
          username: response.data.username,
          userType: response.data.userType,
          role: response.data.role,
        },
      });

      console.log('Vuex Store 中的 token：', store.state.auth.token);
      console.log('Vuex Store 中的 user：', store.state.auth.user);

      if (response.data.role === 0) {
        console.log('Vuex Store 中的 token：', store.state.auth.token);
        console.log('Vuex Store 中的 user：', store.state.auth.user);
        router.push("/admin/ScooterAdd");
      } else {
        console.log('Vuex Store 中的 token：', store.state.auth.token);
        console.log('Vuex Store 中的 user：', store.state.auth.user);
        router.push("/login");
      }

      alert(`登录成功，欢迎回来，${response.data.username}`);
    }
  } catch (error) {
    if (error.response) {
      errorMessage.value = error.response.data.message || "用户名或密码错误";
    } else {
      errorMessage.value = "无法连接到服务器，请稍后再试";
    }
  } finally {
    loading.value = false;
  }
};

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

      <div v-if="errorMessage" class="alert alert-danger">
        {{ errorMessage }}
      </div>

      <form @submit.prevent="login">
        <div class="form-group">
          <label for="username">用户名</label>
          <input
              type="text"
              id="username"
              v-model="loginForm.username"
              class="form-control"
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
              class="form-control"
              placeholder="请输入密码"
              required
          />
        </div>

        <div class="form-actions">
          <button type="submit" class="btn btn-primary" :disabled="loading">
            {{ loading ? "登录中..." : "登录" }}
          </button>
          <router-link to="/register" class="btn btn-link">注册新账号</router-link>
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
  min-height: 80vh;
  background-color: #f5f5f5;
}

.login-form {
  width: 100%;
  max-width: 400px;
  padding: 30px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

h2 {
  text-align: center;
  margin-bottom: 25px;
  color: #333;
  font-weight: 500;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #555;
}

.form-control {
  width: 100%;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
}

.form-control:focus {
  border-color: #4a90e2;
  outline: none;
  box-shadow: 0 0 0 2px rgba(74, 144, 226, 0.2);
}

.alert {
  padding: 12px 15px;
  margin-bottom: 20px;
  border-radius: 4px;
}

.alert-danger {
  background-color: #ffebee;
  color: #d32f2f;
  border: 1px solid #ffcdd2;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 25px;
}

.btn {
  padding: 10px 20px;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary {
  background-color: #4a90e2;
  color: white;
  border: none;
}

.btn-primary:hover {
  background-color: #3a7fcb;
}

.btn-link {
  color: #4a90e2;
  text-decoration: none;
}

.btn-link:hover {
  text-decoration: underline;
}
</style>