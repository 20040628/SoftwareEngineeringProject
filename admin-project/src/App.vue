<script>
import { computed } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
// import TestConnection from './components/TestConnection.vue'
// import UserRegister from './components/UserRegister.vue'
import UserLogin from './views/login/index.vue'
// import ScooterAdd from "@/components/ScooterAdd.vue";
// import ScooterAll from "@/components/ScooterAll.vue";

export default {
  name: 'App',
  components: {
    // ScooterAll,
    // TestConnection,
    // UserRegister,
    UserLogin,
    // ScooterAdd
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
    <main>
      <router-view />
    </main>
</template>

<style>
  main {
    width: 100vw;
    height: 100vh;
  }
</style>