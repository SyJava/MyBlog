import Vue from 'vue'
import App from './App.vue'
import router from './router'
import VueRouter from 'vue-router'

import Element from "element-ui"
import "element-ui/lib/theme-chalk/index.css"
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
import axios from './axios'

// 解决ElementUI导航栏中的vue-router在3.0版本以上重复点菜单报错问题
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

Vue.prototype.$axios = axios //
Vue.config.productionTip = false


Vue.use(Element)

// 使用 mavonEditor
Vue.use(mavonEditor)

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
