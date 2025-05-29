import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import './axios'; // Si esta inicializa algo, se mantiene
import VueToast from 'vue-toast-notification';
// import 'vue-toast-notification/dist/theme-sugar.css';
// import 'vue-toast-notification/dist/theme-bootstrap.css';
import 'vue-toast-notification/dist/theme-default.css';

// Estilos globales
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import '@fortawesome/fontawesome-free/css/all.css';

// FontAwesome setup
import { library } from '@fortawesome/fontawesome-svg-core';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
library.add(fas);

import api from '@/services/api.js';
import { setupInterceptors } from '@/services/interceptors.js';

setupInterceptors(api);

import axios from 'axios';
axios.defaults.baseURL = import.meta.env.VITE_API_URL;
axios.defaults.withCredentials = true;

axios.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    error => Promise.reject(error)
);

axios.interceptors.response.use(
    response => response,
    error => {
        const status = error.response?.status;
        const path = router.currentRoute.value.path;

        if (status === 401 && error.response?.data?.message?.includes('Token expirado')) {
            localStorage.removeItem('token');
            localStorage.removeItem('userRole');
            localStorage.removeItem('userId');

            if (router.currentRoute.value.meta.requiresAuth) {
                router.push('/auth/login');
            }
        }

        else if (status === 403) {
            const message = error.response?.data?.message || 'No tienes permisos para esta acción';
            const toast = app.config.globalProperties.$toast;
            toast && toast.warning(message);
        }

        return Promise.reject(error);
    }
);

// Crear y montar la app
const app = createApp(App);
app.component('font-awesome-icon', FontAwesomeIcon);
app.use(createPinia());
app.use(VueToast, {
    duration: 4500,
    dismissible: true,
    pauseOnHover: true,
    maxToasts: 5,
    queue: true,
    transition: 'Vue-Toastification__bounce', // transición suave y profesional
});
app.use(router);
app.mount('#app');
