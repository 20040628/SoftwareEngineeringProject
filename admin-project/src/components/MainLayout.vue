<template>
  <div class="page">
    <!-- 侧边栏 -->
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

    <div class="content">
      <router-view />
    </div>
  </div>
</template>

<script>
import ScooterManagement from './ScooterAdd/ScooterAdd.vue';
import UserFeedback from './UserFeedback.vue';
import AllScooters from './AllScooters.vue';
import DataAnalysis from './DataAnalysis.vue';
import BookingManagement from './BookingManagement.vue';
import UserManagement from './UserManagement.vue';
import Logout from './Logout.vue';

export default {
  components: {
    ScooterManagement,
    UserFeedback,
    AllScooters,
    DataAnalysis,
    BookingManagement,
    UserManagement,
    Logout,
  },
  data() {
    return {
      activeMenu: 1, // 默认选中的菜单项
      menus: [ // 菜单数据
        { id: 1, name: 'Scooter Management', icon: '/static/center/book.png' },
        { id: 2, name: 'User Feedback', icon: '/static/center/comment.png' },
        { id: 3, name: 'Show all scooters', icon: '/static/center/share.png' },
        { id: 4, name: 'Data Analysis', icon: '/static/center/appstore-add.png' },
        { id: 5, name: 'Order Management', icon: '/static/center/poweroff.png' },
        { id: 6, name: 'User Management', icon: '/static/center/poweroff.png' },
        { id: 7, name: 'Logout', icon: '/static/center/poweroff.png' },
      ],
    };
  },
  computed: {
    currentComponent() {
      const componentMap = {
        1: 'ScooterManagement',
        2: 'UserFeedback',
        3: 'AllScooters',
        4: 'DataAnalysis',
        5: 'BookingManagement',
        6: 'UserManagement',
        7: 'Logout'
      };
      return componentMap[this.activeMenu] || 'ScooterManagement'; // 默认返回 ScooterManagement
    },
  },
  methods: {
    selectMenu(id) {
      this.activeMenu = id; // 更新选中的菜单项
      const routeMap = {
        1: '/add_scooter',
        2: '/user-feedback',
        3: '/all-scooters',
        4: '/data-analysis',
        5: '/order-management',
        6: '/user-management',
        7: '/logout'
      };
      const route = routeMap[id];
      if (route) {
        this.$router.push(route); // 导航到相应的路由
      }
    },
  },
};
</script>

<style scoped>
@import "./index.scss";
.page {
  display: flex;
  min-height: 100vh;
  background: #f5f5f5;
}

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

.content {
  flex: 1;
  padding: 20px;
  background: #fff;
  border-radius: 10px;
  margin: 20px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 10px;
  cursor: pointer;
  transition: background 0.2s ease-in-out;
}

.menu-item:hover {
  background: rgba(255, 255, 255, 0.1);
}

.menu-item.active {
  background: rgba(255, 255, 255, 0.2);
}

.menu-item img {
  width: 28px;
  height: 28px;
  margin-right: 10px;
  object-fit: contain;
}

.user {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.user img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 10px;
}

.user span {
  font-size: 16px;
  font-weight: bold;
}
</style>