import { createRouter, createWebHistory } from 'vue-router';
import Login from '@/components/Login.vue';
import Register from "@/components/Register.vue";
import Dashboard from "@/components/Dashboard.vue";
import ChatDashboard from "@/components/ChatBoard.vue";
import UploadCard from "@/components/UploadCard.vue";

const routes = [
    {
        path: '/',
        redirect: '/login',
    },
    {
        path: '/login',
        name: 'login',
        component: Login,
    },
    {
        path: '/register',
        name: 'register',
        component: Register,
    },
    {
        path: '/dashboard',
        name: 'dashboard',
        component: Dashboard,
    },
    {
        path: '/chat',
        name: 'chat',
        component: ChatDashboard,
    },
    {
        path: '/card',
        name: 'uploader',
        component: UploadCard,
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// routes.beforeEach((to, from, next) => {
//     const token = localStorage.getItem('token');
//     if(to.path !== '/login' && this.$axios.post('api/isAuthenticated', new URLSearchParams({'token': token})) === true){
//         next('/login');
//     } else {
//         next();
//     }
// });

export default router;