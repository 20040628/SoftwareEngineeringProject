import { createStore } from 'vuex';
import axios from 'axios';
import createPersistedState from 'vuex-persistedstate';

export default createStore({
  plugins: [
    createPersistedState({
      key: 'e-scooter-auth',
      paths: ['auth']
    })
  ],
  state: {
    auth: {
      token: null,
      user: null,
      isAuthenticated: false
    }
  },
  getters: {
    isAuthenticated: state => state.auth.isAuthenticated,
    user: state => state.auth.user,
    token: state => state.auth.token
  },
  mutations: {
    SET_AUTH(state, { token, user }) {
      state.auth.token = token;
      state.auth.user = user;
      state.auth.isAuthenticated = !!token;
    },
    CLEAR_AUTH(state) {
      state.auth.token = null;
      state.auth.user = null;
      state.auth.isAuthenticated = false;
    }
  },
  actions: {
    login({ commit }, { token, user }) {
      // Set auth token for all future requests
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      
      // Commit the auth data to store
      commit('SET_AUTH', { token, user });
    },
    logout({ commit }) {
      // Remove auth token
      delete axios.defaults.headers.common['Authorization'];
      
      // Clear auth data
      commit('CLEAR_AUTH');
    },
    async validateToken({ commit, state, dispatch }) {
      if (!state.auth.token) return false;
      
      try {
        axios.defaults.headers.common['Authorization'] = `Bearer ${state.auth.token}`;
        const response = await axios.get('http://localhost:8080/api/auth/validate');
        
        if (response.data.valid) {
          // Update user data in case it changed
          const user = {
            userId: response.data.userId,
            username: response.data.username,
            userType: response.data.userType
          };
          
          commit('SET_AUTH', { token: state.auth.token, user });
          return true;
        } else {
          // Token invalid, logout
          dispatch('logout');
          return false;
        }
      } catch (error) {
        // Token validation failed, logout
        dispatch('logout');
        return false;
      }
    }
  }
}); 