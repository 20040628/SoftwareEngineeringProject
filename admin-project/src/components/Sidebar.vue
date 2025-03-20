<template>
  <div class="sidebar">
    <div class="user">
      <img src="/static/center/avatar.png" alt="avatar" />
      <span>Administrator</span>
    </div>
    <div class="menu">
      <div
          class="menu-item"
          v-for="v in menus"
          :key="v.id"
          :class="{ active: activeMenu === v.id }"
          @click="selectMenu(v.id)"
      >
        <img :src="v.icon" alt="menu-icon" />
        <span>{{ v.name }}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    activeMenu: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      menus: [
        { id: 1, name: 'Scooter Management', icon: '/static/center/book.png' },
        { id: 2, name: 'User Feedback', icon: '/static/center/comment.png' },
        { id: 3, name: 'Show all scooters', icon: '/static/center/share.png' },
        { id: 4, name: 'Add to desktop', icon: '/static/center/appstore-add.png' },
        { id: 5, name: 'Logout', icon: '/static/center/poweroff.png' },
      ],
    };
  },
  methods: {
    selectMenu(id) {
      this.$emit('select-menu', id); // 触发父组件的事件
    },
  },
};
</script>

<style scoped>
.sidebar {
  width: 250px;
  background: #58c4c9;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
  color: #fff;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
}

.menu-item img {
  width: 28px;
  height: 28px;
  margin-right: 10px;
  object-fit: contain;
}
</style>