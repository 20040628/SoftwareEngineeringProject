<template>
  <div>
    <h2 class="title">Logging Out...</h2>
  </div>
</template>

<script>
export default {
  mounted() {
    this.logout();
    this.$nextTick(() => {
      this.$router.push('/login');
    });
  },
  methods: {
    logout() {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    },
  },
};
</script>


<style scoped>
.title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 15px;
}
</style>