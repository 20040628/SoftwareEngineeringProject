<template>
  <div class="page d-flex">
    <!-- Side Navbar for large screens (d-lg-block to show only on large screens) -->
    <div class="sidebar bg-info d-flex flex-column p-3 text-white shadow-sm d-none d-lg-block">
      <div class="user mb-4">
        <img src="/static/center/logo.png" alt="avatar" class="img-fluid rounded-circle mb-2" style="width: 60px; height: 60px;" />
        <span>ScootGo Admin</span>
      </div>
      <div class="menu w-100">
        <div
            class="menu-item d-flex align-items-center py-2 px-3 cursor-pointer transition-all"
            v-for="v in menus"
            :key="v.id"
            :class="{ active: activeMenu === v.id }"
            @click="selectMenu(v.id)"
        >
          <img :src="v.icon" alt="menu-icon" class="me-2" style="width: 28px; height: 28px;" />
          <span>{{ v.name }}</span>
        </div>
      </div>
    </div>
    <!-- Bootstrap Navbar for small screens (d-lg-none to show only on small screens) -->
    <nav class="navbar navbar-expand-lg navbar-light bg-info d-lg-none w-100">
      <div class="container-fluid">
        <div class="user d-flex align-items-center">
          <img src="/static/center/logo.png" alt="avatar" class="img-fluid rounded-circle" style="width: 40px; height: 40px;" />
          <span class="ms-2">ScootGo Admin</span>
        </div>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav">
            <li class="nav-item" v-for="v in menus" :key="v.id">
              <a class="nav-link" :class="{ active: activeMenu === v.id }" @click="selectMenu(v.id)">
                <img :src="v.icon" alt="menu-icon" class="me-2" style="width: 28px; height: 28px;" />
                {{ v.name }}
              </a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <!-- Content Section -->
    <div class="content flex-grow-1">
      <router-view />
    </div>


  </div>
</template>

<script>
import AddScooter from './ScooterAdd.vue';
import AddUser from './AddUser.vue';
import AddOrder from './AddOrder.vue';
import UserFeedback from './UserFeedback.vue';
import AllScooters from './AllScooters.vue';
import DataAnalysis from './DataAnalysis.vue';
import BookingManagement from './BookingManagement.vue';
import UserManagement from './UserManagement.vue';
import Logout from './Logout.vue';
import AddStore from "@/components/AddStore.vue";

export default {
  components: {
    AddScooter,
    AddStore,
    AddUser,
    AddOrder,
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
      menus: [
        { id: 1, name: 'Add Scooter', icon: '/static/center/book.png' },
        { id: 2, name: 'Add Store', icon: '/static/center/store.svg' },
        { id: 3, name: 'Add User', icon: '/static/center/adduser.png' },
        { id: 4, name: 'Add Order', icon: '/static/center/addorder.png' },
        { id: 5, name: 'User Feedback', icon: '/static/center/comment.png' },
        { id: 6, name: 'Data Analysis', icon: '/static/center/data_analysis.png' },
        { id: 7, name: 'Scooter Management', icon: '/static/center/share.png' },
        { id: 8, name: 'Order Management', icon: '/static/center/order.png' },
        { id: 9, name: 'User Management', icon: '/static/center/user.png' },
        { id: 10, name: 'Logout', icon: '/static/center/poweroff.png' },
      ],
    };
  },
  computed: {
    currentComponent() {
      const componentMap = {
        1: 'AddScooter',
        2: 'AddStore',
        3: 'AddUser',
        4: 'AddOrder',
        5: 'UserFeedback',
        6: 'DataAnalysis',
        7: 'AllScooters',
        8: 'BookingManagement',
        9: 'UserManagement',
        10: 'Logout',
      };
      return componentMap[this.activeMenu] || 'AddScooter'; // 默认返回 AddScooter
    },
  },
  created() {
    this.setActiveMenuFromRoute();
  },

  methods: {
    setActiveMenuFromRoute() {
      const routeToIdMap = {
        '/add_scooter': 1,
        '/add_store': 2,
        '/add_user': 3,
        '/add_order':4,
        '/user-feedback': 5,
        '/data-analysis': 6,
        '/all-scooters': 7,
        '/order-management': 8,
        '/user-management': 9,
        '/logout': 10,
      };

      const currentRoute = this.$route.path;
      this.activeMenu = routeToIdMap[currentRoute] || 1; // Default to 1 if route not found
    },

    selectMenu(id) {
      this.activeMenu = id;
      const routeMap = {
        1: '/add_scooter',
        2: '/add_store',
        3: '/add_user',
        4: '/add_order',
        5: '/user-feedback',
        6: '/data-analysis',
        7: '/all-scooters',
        8: '/order-management',
        9: '/user-management',
        10: '/logout',
      };
      const route = routeMap[id];
      if (route) {
        this.$router.push(route);
      }
    }
  },

  watch: {
    '$route'(to) {
      this.setActiveMenuFromRoute();
    }
  }
};
</script>

<style scoped>
@import "./index.scss";
.page {
  display: flex;
  /* flex-direction: row;  */
  min-height: 100vh;
  background: #f3ffff;
}

.navbar {
  background: #003c51 !important;
}

.navbar-nav {
  margin-left: auto;
}

.nav-item {
  padding: 10px;
}

.nav-link {
  display: flex;
  align-items: center;
  color: #fff;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.2s ease-in-out;
}

.nav-link:hover {
  background: rgba(255, 255, 255, 0.1);
}

.nav-link.active {
  background: rgba(255, 255, 255, 0.2);
}

.nav-item img {
  width: 28px;
  height: 28px;
  margin-right: 10px;
  object-fit: contain;
}

.sidebar {
  width: 250px;
  background-color: #003c51 !important;
}

.content {
  flex: 1;
  padding: 20px;
  background: #fff;
  border-radius: 3%;
  margin: 20px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
}
</style>