import { createRouter, createWebHistory } from 'vue-router'
import axios from '@/axios' // Importa tu configuración de axios

import DefaultLayout from '@/layouts/DefaultLayout.vue'
import HomePage from '@/pages/Home.vue'
import Login from '@/pages/auth/login.vue'
import Register from '@/pages/auth/register.vue'
import ForgotPassword from '@/pages/auth/forgotPassword.vue'
import Profile from '@/pages/user/Profile.vue'
import MyProfile from '@/pages/user/MyProfile.vue'
import Produccion from '@/pages/general/Produccion.vue'
import General from '@/pages/general/General.vue'
import Producciones from '@/pages/general/ProduccionesPage.vue'
import Profesionales from '@/pages/general/ProfesionalPage.vue'
import Sagas from '@/pages/general/SagasPage.vue'
import AdminLayout from "@/layouts/AdminLayout.vue";
import Dashboard from "@/pages/admin/Dashboard.vue";
import UbicacionesAdminPage from "@/pages/admin/UbicacionesAdminPage.vue";
import ProduccionesAdminPage from "@/pages/admin/ProduccionesAdminPage.vue";
import NotFound from "@/pages/NotFound.vue";
import SagasAdminPages from "@/pages/admin/SagasAdminPages.vue";
import ProfesionalesAdminPage from "@/pages/admin/ProfesionalesAdminPage.vue";

const routes = [
    {
        path: '/',
        component: DefaultLayout,
        children: [
            {
                path: '',
                name: 'Home',
                component: HomePage,
                meta: { public: true }
            }
        ]
    },
    {
        path: '/auth/login',
        name: 'Login',
        component: Login,
        meta: { public: true, onlyGuest: true }
    },
    {
        path: '/auth/register',
        name: 'Register',
        component: Register,
        meta: { public: true, onlyGuest: true }
    },
    {
        path: '/auth/forgot-password',
        name: 'ForgotPassword',
        component: ForgotPassword,
        meta: { public: true }
    },
    {
        path: '/general',
        name: 'General',
        component: General,
        meta: { public: true }
    },
    {
        path: '/producciones',
        name: 'Producciones',
        component: Producciones,
        meta: { public: true }
    },
    {
        path: '/sagas',
        name: 'Sagas',
        component: Sagas,
        meta: { public: true }
    },
    {
        path: '/myprofile',
        name: 'MyProfile',
        component: MyProfile,
        meta: { requiresAuth: true }
    },
    {
        path: '/produccion',
        name: 'Production',
        component: Produccion
    },
    {
        path: '/admin',
        component: AdminLayout,
        children: [
            {
                path: '',
                name: 'AdminDashboard',
                components: { default: Dashboard },
                meta: { title: 'Dashboard de Administración' }
            },
            {
                path: 'ubicaciones',
                name: 'AdminUbicaciones',
                components: { default: UbicacionesAdminPage },
                meta: { title: 'Administración de Lugares' }
            },
            {
                path: 'producciones',
                name: 'AdminProducciones',
                components: { default: ProduccionesAdminPage },
                meta: { title: 'Administración de Producciones' }
            },
            {
                path: 'sagas',
                name: 'AdminSagas',
                components: { default: SagasAdminPages },
                meta: { title: 'Administración de Sagas' }
            },
            {
                path: 'profesionales',
                name: 'AdminProfesionales',
                components: { default: ProfesionalesAdminPage },
                meta: { title: 'Administración de Profesionales' }
            }
        ]
    },
    {
        path: '/profesionales',
        name: 'Profesionales',
        component: Profesionales
    },
    {
        path: '/profile',
        name: 'Profile',
        component: Profile
    },
    {
        path: '/:pathMatch(.*)*',
        name: 'NotFound',
        component: NotFound,
        meta: { public: true }
    }

]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach(async (to, from, next) => {
    const token = localStorage.getItem('token')
    const isAuthenticated = !!token

    // Configura el token en axios si existe
    if (token) {
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
    }

    if (to.meta.onlyGuest && isAuthenticated) {
        return next('/myprofile')
    }

    // Rutas que requieren autenticación
    if (to.meta.requiresAuth && !isAuthenticated) {
        return next('/auth/login') // Redirige a login si no está autenticado
    }

    next()
})

export default router