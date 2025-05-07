import { createRouter, createWebHistory } from 'vue-router';
import Login from '@/components/Login.vue';
import Register from "@/components/Register.vue";
import Dashboard from "@/components/Dashboard.vue";
import Chat from "@/components/Chat.vue";

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
        component: Chat,
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;