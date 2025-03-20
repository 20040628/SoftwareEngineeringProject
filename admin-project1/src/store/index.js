import { createStore } from 'vuex';
import axios from 'axios';

export default createStore({
  state: {
    auth: {
      token: localStorage.getItem('token') || null,
      user: JSON.parse(localStorage.getItem('user')) || null,
      isAuthenticated: !!localStorage.getItem('token')
    }
  },
  getters: {
    isAuthenticated: state => state.auth.isAuthenticated,
    user: state => state.auth.user,
    token: state => state.auth.token
  },
  mutations: {
    SET_AUTH(state, { token, user }) {
      console.log('SET_AUTH mutation called with token:', token);
      state.auth.token = token;
      state.auth.user = user;
      state.auth.isAuthenticated = !!token;

      // 存储到 localStorage
      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(user));

      console.log('State after SET_AUTH:', state.auth);
    },
    CLEAR_AUTH(state) {
      state.auth.token = null;
      state.auth.user = null;
      state.auth.isAuthenticated = false;

      // 清除 localStorage
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    }
  },
  actions: {
    login({ commit }, { token, user }) {
      console.log('Dispatching login action with token:', token);

      // 设置 axios 头部
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

      commit('SET_AUTH', { token, user });
      console.log('State after login action:', this.state.auth);
    },
    logout({ commit }) {
      delete axios.defaults.headers.common['Authorization'];
      commit('CLEAR_AUTH');
    },
    async validateToken({ commit, state, dispatch }) {
      if (!state.auth.token) return false;

      try {
        axios.defaults.headers.common['Authorization'] = `Bearer ${state.auth.token}`;
        const response = await axios.get('http://localhost:8080/api/auth/validate');

        if (response.data.valid) {
          const user = {
            userId: response.data.userId,
            username: response.data.username,
            userType: response.data.userType
          };
          commit('SET_AUTH', { token: state.auth.token, user });
          return true;
        } else {
          dispatch('logout');
          return false;
        }
      } catch (error) {
        dispatch('logout');
        return false;
      }
    }
  }
});
