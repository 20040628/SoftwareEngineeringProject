<script>
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
  data() {
    return {
      currentUser: null
    }
  },
  methods: {
    handleLoginSuccess(user) {
      this.currentUser = user
    }
  }
}
</script>

<template>
  <div id="app">
    <header>
      <h1>E-Scooter Rental System</h1>
      <div v-if="currentUser" class="user-info">
        Welcome, {{ currentUser.username }}!
      </div>
    </header>
    
    <main>
      <div v-if="!currentUser">
        <div class="auth-container">
          <UserLogin @login-success="handleLoginSuccess" />
          <div class="divider">
            <span>Or</span>
          </div>
          <UserRegister />
        </div>
      </div>
      <div v-else>
        <!-- Add your authenticated content here -->
        <p>You are logged in!</p>
        <div v-if="currentUser.userType === 0">
          <ScooterAdd />
        </div>
      </div>

      <div>
        <ScooterAll :current-user="currentUser"/>
      </div>
    </main>
  </div>
</template>

<style>
#app {
  font-family: Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  margin: 0;
  padding: 20px;
}

header {
  text-align: center;
  margin-bottom: 30px;
  position: relative;
}

.user-info {
  position: absolute;
  top: 0;
  right: 0;
  padding: 10px;
  font-size: 0.9em;
  color: #666;
}

h1 {
  color: #34495e;
}

.auth-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.divider {
  text-align: center;
  margin: 20px 0;
  position: relative;
}

.divider::before,
.divider::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 45%;
  height: 1px;
  background-color: #ddd;
}

.divider::before {
  left: 0;
}

.divider::after {
  right: 0;
}

.divider span {
  background-color: white;
  padding: 0 10px;
  color: #666;
  font-size: 0.9em;
}
</style>
