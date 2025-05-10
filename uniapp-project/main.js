import App from './App'

// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
Vue.config.productionTip = false
Vue.prototype.$baseURL = 'http://192.168.1.11:8080';
App.mpType = 'app'
const app = new Vue({
  ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  app.config.globalProperties.$baseURL = 'http://192.168.1.11:8080';
  // http://118.24.22.77
  // http://192.168.1.11:8080
  return {
    app
  }
}
// #endif