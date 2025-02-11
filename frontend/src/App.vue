<script>
import TestConnection from './components/TestConnection.vue'

export default {
  name: 'App',
  components: {
    TestConnection
  }
}
</script>

<template>
  <div id="app">
    <header>
      <h1>Vue + Spring Boot 测试</h1>
    </header>
    
    <main>
      <TestConnection />
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
}

h1 {
  color: #34495e;
}
</style>
