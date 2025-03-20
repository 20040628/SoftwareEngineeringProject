<script setup>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { ElNotification } from "element-plus";
import { User, Lock } from "@element-plus/icons-vue";
import axios from "axios";

const router = useRouter();
const store = useStore();

const loginForm = reactive({
  username: "",
  password: "",
});

// 表单校验规则
const loginRules = reactive({
  username: [{ required: true, message: "Please enter username", trigger: "blur" }],
  password: [{ required: true, message: "Please enter password", trigger: "blur" }],
});

const loginFormRef = ref();
const errorMessage = ref("");
const loading = ref(false);

const login = async () => {
  if (!loginFormRef.value) return;

  loginFormRef.value.validate(async (valid) => {
    if (!valid) return;

    loading.value = true;
    errorMessage.value = "";

    try {
      const response = await axios.post("http://localhost:8080/api/auth/login", loginForm);
      if (response.data && response.data.token) {
        await store.dispatch("login", {
          token: response.data.token,
          user: {
            userId: response.data.userId,
            username: response.data.username,
            userType: response.data.userType,
            role: response.data.role,
          },
        });

        router.push(response.data.role === 0 ? "/admin/ScooterAdd" : "/login");
        ElNotification({ title: "登录成功", message: `欢迎回来，${response.data.username}`, type: "success" });
      }
    } catch (error) {
      errorMessage.value = error.response?.data?.message || "用户名或密码错误";
    } finally {
      loading.value = false;
    }
  });
};
</script>


<template>
  <div v-if="errorMessage" class="alert alert-danger">
    {{ errorMessage }}
  </div>

  <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef">
    <!-- 用户名 -->
    <el-form-item prop="username">
      <el-input v-model.trim="loginForm.username" placeholder="Username" class="custom-input" size="large">
        <template #prefix>
          <el-icon><User /></el-icon>
        </template>
      </el-input>
    </el-form-item>

    <!-- 密码 -->
    <el-form-item prop="password">
      <el-input v-model.trim="loginForm.password" type="password" placeholder="Password" class="custom-input" show-password size="large">
        <template #prefix>
          <el-icon><Lock /></el-icon>
        </template>
      </el-input>
    </el-form-item>

    <!-- 按钮 -->
    <el-form-item>
      <el-button type="primary" :loading="loading" native-type="submit" @click="login">Login</el-button>
      <router-link to="/register" class="btn-link">Register</router-link>
    </el-form-item>
  </el-form>
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

/* 自定义 Element Plus 的 el-input 样式 */
.custom-input {
  --el-input-border-color: #dcdfe6; /* 统一边框色 */
  --el-input-hover-border-color: #409eff; /* 悬停变色 */
  --el-input-focus-border-color: #409eff;
  --el-input-placeholder-color: #909399;
}

/* 调整 el-input 里面的图标 */
.el-input__prefix {
  color: #c0c4cc;
  font-size: 16px;
}

/* 表单按钮样式 */
.form-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 25px;
}

/* Element Plus 按钮调整 */
.el-button {
  width: 100%;
  font-size: 16px;
  padding: 12px 0;
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
  border: 1px solid #dcdfe6; /* Element Plus 主题默认边框色 */
  border-radius: 4px;
  font-size: 16px;
  transition: border-color 0.3s ease;
  background-color: #fff;
  color: #606266;
}

.form-control:focus {
  border-color: #009688; /* Element Plus 主题的高亮色 */
  outline: none;
  box-shadow: #009688;
}

.input-wrapper {
  display: flex;
  align-items: center;
  position: relative;
}

.input-wrapper .icon {
  position: absolute;
  left: 10px;
  color: #c0c4cc;
  font-size: 16px;
}

.input-wrapper input {
  padding-left: 35px; /* 预留左侧空间给图标 */
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

.el-button {
  padding: 10px 20px !important; /* 这里必须加 !important 强制覆盖 */
  border-radius: 4px !important;
  font-size: 16px !important;
  cursor: pointer !important;
  transition: all 0.3s ease !important;
}


.el-button--primary {
  background-color: #009688 !important;
  border: none !important;
  color: white !important;
}

.el-button--primary:hover {
  background-color: #00796b !important; /* 让 hover 颜色稍微不同 */
}

.btn-link {
  color: #009688;
  text-decoration: none;
}

.btn-link:hover {
  text-decoration: underline;
}
</style>