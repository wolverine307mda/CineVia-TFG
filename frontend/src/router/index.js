import { createRouter, createWebHistory } from 'vue-router'
import axios from '@/axios'
import { useAuthStore } from '@/stores/auth'

import DefaultLayout from '@/layouts/DefaultLayout.vue'
import HomePage from '@/pages/Home.vue'
import Login from '@/pages/auth/login.vue'
import Register from '@/pages/auth/register.vue'
import ForgotPassword from '@/pages/auth/forgotPassword.vue'
import Profile from '@/pages/user/Profile.vue'
import MyProfile from '@/pages/user/MyProfile.vue'
import Produccion from '@/pages/general/unicas/Produccion.vue'
import Saga from '@/pages/general/unicas/Saga.vue'
import Profesional from '@/pages/general/unicas/Profesional.vue'
import General from '@/pages/general/General.vue'
import Producciones from '@/pages/general/agrupadas/ProduccionesPage.vue'
import Profesionales from '@/pages/general/agrupadas/ProfesionalesPage.vue'
import Sagas from '@/pages/general/agrupadas/SagasPage.vue'
import AdminLayout from "@/layouts/AdminLayout.vue"
import Dashboard from "@/pages/admin/Dashboard.vue"
import UbicacionesAdminPage from "@/pages/admin/UbicacionesAdminPage.vue"
import ProduccionesAdminPage from "@/pages/admin/ProduccionesAdminPage.vue"
import NotFound from "@/pages/errors/NotFound.vue"
import SagasAdminPages from "@/pages/admin/SagasAdminPages.vue"
import UsuariosAdminPages from "@/pages/admin/UsuariosAdmin.vue"
import ProfesionalesAdminPage from "@/pages/admin/ProfesionalesAdminPage.vue"
import NotAutoriced from "@/pages/errors/NotAutoriced.vue";

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
        path: '/dashboard',
            redirect: { name: 'MyProfile' }
    },
    {
        path: '/profile/:id?',
        name: 'Profile',
        component: Profile,
        meta: { public: true }
    },
    {
        path: '/produccion/:id',
        name: 'Production',
        component: Produccion,
        meta: { public: true }
    },
    {
        path: '/saga/:id',
        name: 'Saga',
        component: Saga,
        meta: { public: true }
    },
    {
        path: '/profesional/:id',
        name: 'Profesional',
        component: Profesional,
        meta: { public: true }
    },
    {
        path: '/admin',
        component: AdminLayout,
        meta: { requiresAuth: true, requiresAdmin: true },
        children: [
            {
                path: '',
                name: 'AdminDashboardDefault',
                component: Dashboard,
                meta: { title: 'Dashboard' }
            },
            {
                path: 'ubicaciones',
                name: 'AdminUbicaciones',
                component: UbicacionesAdminPage,
                meta: { title: 'Gestión de Ubicaciones' }
            },
            {
                path: 'producciones',
                name: 'AdminProducciones',
                component: ProduccionesAdminPage,
                meta: { title: 'Gestión de Producciones' }
            },
            {
                path: 'sagas',
                name: 'AdminSagas',
                component: SagasAdminPages,
                meta: { title: 'Gestión de Sagas' }
            },
            {
                path: 'usuarios',
                name: 'AdminUsuarios',
                component: UsuariosAdminPages,
                meta: { title: 'Gestión de Usuarios' }
            },
            {
                path: 'profesionales',
                name: 'AdminProfesionales',
                component: ProfesionalesAdminPage,
                meta: { title: 'Gestión de Profesionales' }
            }
        ]
    },
    {
        path: '/profesionales',
        name: 'Profesionales',
        component: Profesionales,
        meta: { public: true }
    },
    {
        path: '/not-authorized',
        name: 'NotAuthorized',
        component: NotAutoriced,
        meta: { public: true }
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
    routes,
    scrollBehavior(to, from, savedPosition) {
        return { top: 0 }
    }
})

router.beforeEach(async (to, from, next) => {
    const authStore = useAuthStore()
    const token = localStorage.getItem('token')

    // Configurar token en axios si existe
    if (token) {
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
    }

    // Intentar cargar el usuario si hay token pero no está cargado
    if (token && !authStore.user) {
        try {
            await authStore.checkAuth()
        } catch (error) {
            console.error('Error al verificar autenticación:', error)
            localStorage.removeItem('token')
        }
    }

    // Redirigir usuarios autenticados que intentan acceder a rutas onlyGuest
    if (to.meta.onlyGuest && authStore.isAuthenticated) {
        // Cambio aquí: redirigir a /admin si es administrador
        return next(authStore.isAdmin ? '/admin' : '/myprofile')
    }

    // Rutas que requieren autenticación
    if (to.meta.requiresAuth && !authStore.isAuthenticated) {
        return next('/auth/login')
    }

    // Rutas que requieren ser admin
    if (to.meta.requiresAdmin && !authStore.isAdmin) {
        return next('/not-authorized')
    }

    next()
})

export default router