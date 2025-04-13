import { createRouter, createWebHistory } from 'vue-router'

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


const routes = [
    {
        path: '/',
        component: DefaultLayout,
        children: [
            {
                path: '',
                name: 'Home',
                component: HomePage
            }
        ]
    },
    {
        path: '/auth/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/auth/register',
        name: 'Register',
        component: Register
    },
    {
        path: '/auth/forgot-password',
        name: 'ForgotPassword',
        component: ForgotPassword
    },

    {
        path: '/general',
        name: 'General',
        component: General
    },
    {
        path: '/producciones',
        name: 'Producciones',
        component: Producciones
    },
    {
        path: '/myprofile',
        name: 'My Profile',
        component: MyProfile
    },
    {
        path: '/produccion',
        name: 'Production',
        component: Produccion
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
        redirect: '/'
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
