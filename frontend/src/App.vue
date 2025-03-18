<script>
import { computed } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import TestConnection from './components/TestConnection.vue'
import UserRegister from './components/UserRegister.vue'
import UserLogin from './components/UserLogin.vue'
import ScooterAdd from "@/components/ScooterAdd.vue";
import ScooterAll from "@/components/ScooterAll.vue";

export default {
  name: 'App',
  components: {
    ScooterAll,
    TestConnection,
    UserRegister,
    UserLogin,
    ScooterAdd
  },
  setup() {
    const store = useStore();
    const router = useRouter();
    
    // 使用计算属性获取认证状态和用户信息
    const isAuthenticated = computed(() => store.getters.isAuthenticated);
    const currentUser = computed(() => store.getters.user);
    
    // 登出方法
    const logout = () => {
      store.dispatch('logout');
      router.push('/login');
    };
    
    return {
      isAuthenticated,
      currentUser,
      logout
    };
  }
}
</script>

<template>
  <div id="app">
    <header>
      <div class="header-content">
        <h1>电动滑板车租赁系统</h1>
        
        <nav v-if="isAuthenticated" class="main-nav">
          <router-link to="/scooters" class="nav-link">浏览滑板车</router-link>
          <router-link v-if="currentUser && currentUser.role === 0" to="/admin/dashboard" class="nav-link">管理面板</router-link>
          <router-link v-if="currentUser && currentUser.role === 0" to="/admin/weekly-revenue" class="nav-link">周收入统计</router-link>
          <router-link to="/my-bookings" class="nav-link">我的预订</router-link>
        </nav>
        
        <div class="user-area">
          <template v-if="isAuthenticated">
            <span class="welcome-text">欢迎, {{ currentUser ? currentUser.username : '' }}!</span>
            <button @click="logout" class="logout-btn">登出</button>
          </template>
          <template v-else>
            <router-link to="/login" class="auth-link">登录</router-link>
            <router-link to="/register" class="auth-link">注册</router-link>
          </template>
        </div>
      </div>
    </header>
    
    <main>
      <router-view />
    </main>
    
    <footer>
      <p>© 2023 电动滑板车租赁系统 - 版权所有</p>
    </footer>
  </div>
</template>

<style>
* {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

body {
  font-family: 'PingFang SC', 'Microsoft YaHei', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  background-color: #f8f9fa;
  line-height: 1.6;
}

#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

header {
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 15px 0;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

h1 {
  color: #4a90e2;
  font-size: 24px;
  font-weight: 500;
}

.main-nav {
  display: flex;
  gap: 20px;
}

.nav-link, .auth-link {
  text-decoration: none;
  color: #555;
  padding: 5px 10px;
  transition: all 0.3s ease;
}

.nav-link:hover, .auth-link:hover {
  color: #4a90e2;
}

.user-area {
  display: flex;
  align-items: center;
  gap: 15px;
}

.welcome-text {
  font-size: 14px;
  color: #666;
}

.logout-btn {
  background-color: transparent;
  border: 1px solid #e0e0e0;
  color: #666;
  padding: 5px 10px;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.logout-btn:hover {
  background-color: #f5f5f5;
  color: #d32f2f;
}

main {
  flex: 1;
  padding: 30px 20px;
  max-width: 1200px;
  margin: 0 auto;
  width: 100%;
}

footer {
  background-color: #fff;
  border-top: 1px solid #e0e0e0;
  padding: 20px;
  text-align: center;
  color: #666;
  font-size: 14px;
}

/* 全局样式重置和通用样式 */
button {
  font-family: inherit;
}

.auth-link {
  display: inline-block;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 15px;
  }
  
  .main-nav, .user-area {
    width: 100%;
    justify-content: center;
  }
}
</style>
