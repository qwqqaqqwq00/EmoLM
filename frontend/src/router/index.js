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
// router.beforeEach((to, from, next) => {
//     const token = localStorage.getItem('token');
//     axios.get('/api/isAuthenticated',  {
//         headers: {
//             Authorization: `Bearer ${token}`,
//         },
//     })
//     .then(response => {
//         const isAuthenticated = response.data.success;
//         const isPublicPage = ['/login', '/register', '/'].includes(to.path);
//
//         if (!isAuthenticated && !isPublicPage) {
//             console.log("redirect:/login");
//             next('/login');
//         } else {
//             next();
//         }
//     })
//     .catch(() => {
//         if(to.path === '/login') {
//             next();
//         }else{
//             next('/login');
//         }
//     });
// });

export default router;