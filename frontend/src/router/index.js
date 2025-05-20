import { createRouter, createWebHistory } from 'vue-router';
import Login from '@/components/Login.vue';
import Register from "@/components/Register.vue";
import Dashboard from "@/components/Dashboard.vue";
import ChatDashboard from "@/components/ChatBoard.vue";
import UploadCard from "@/components/UploadCard.vue";
import CodeDriveAnimTitle from "@/components/CodeDriveAnimTitle.vue";

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
    },
    {
        path: '/animTitle',
        name: 'animTitle',
        component: CodeDriveAnimTitle,
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// 添加全局导航守卫
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token');
    const isAuthenticated = !!token; // 判断用户是否已登录
    const isPublicPage = ['/login', '/'].includes(to.path); // 公开页面无需登录

    if (!isAuthenticated && !isPublicPage) {
        // 如果用户未登录且访问非公开页面，则重定向到登录页面
        next('/login');
    } else {
        next(); // 否则继续导航
    }
});

export default router;