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

// Add global navigation guard
router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token');
    const isAuthenticated = !!token; // Check if user is authenticated
    const isPublicPage = ['/login', '/'].includes(to.path); // Public pages do not require login

    if (!isAuthenticated && !isPublicPage) {
        // If user is not authenticated and accessing non-public pages, redirect to login page
        next('/login');
    } else {
        next(); // Otherwise continue navigation
    }
});

export default router;