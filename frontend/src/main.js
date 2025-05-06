import { createApp } from 'vue'; // 使用 Vue 3
import App from './App.vue';
import router from './router';

import ElementPlus from 'element-plus'; // 引入 element-plus
import 'element-plus/dist/index.css'; // 引入 element-plus 样式

import axios from 'axios';

const app = createApp(App);

app.config.globalProperties.$axios = axios; // 将 axios 挂载到全局属性

app.use(router); // 使用路由
app.use(ElementPlus); // 使用 element-plus

app.mount('#app'); // 挂载到 #app