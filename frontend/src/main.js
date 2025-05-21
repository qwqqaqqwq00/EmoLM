import { createApp } from 'vue'; // Use Vue 3
import App from './App.vue';
import router from './router';

import ElementPlus from 'element-plus'; // Import element-plus
import 'element-plus/dist/index.css'; // Import element-plus styles

import axios from 'axios';

const app = createApp(App);

app.config.globalProperties.$axios = axios; // Mount axios to global property

app.use(router); // Use router
app.use(ElementPlus); // Use element-plus

app.mount('#app'); // Mount to #app