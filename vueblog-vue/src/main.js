import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
// 注册ElementUI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import axios from 'axios';

// 注册一个Markdown编辑器
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import "./axios"
import "./permission"


Vue.config.productionTip = false

Vue.use(ElementUI)
Vue.use(mavonEditor)

Vue.prototype.$axios = axios

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')