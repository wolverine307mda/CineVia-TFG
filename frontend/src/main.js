import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import './axios'; // Si esta inicializa algo, se mantiene

// Estilos globales
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import '@fortawesome/fontawesome-free/css/all.css';

// FontAwesome setup
import { library } from '@fortawesome/fontawesome-svg-core';
import { fas } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
library.add(fas);

// Axios config
import axios from 'axios';
axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.withCredentials = true;

// Interceptor: añadir token
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

// Interceptor: manejar errores 401/403
axios.interceptors.response.use(
    response => response,
    error => {
        if (error.response?.status === 401 || error.response?.status === 403) {
            localStorage.removeItem('token');
            localStorage.removeItem('userRole');
            localStorage.removeItem('userId');

            // Redirige solo si la ruta requiere autenticación
            if (router.currentRoute.value.meta.requiresAuth) {
                router.push('/auth/login');
            }
        }
        return Promise.reject(error);
    }
);

// Crear y montar la app
const app = createApp(App);
app.component('font-awesome-icon', FontAwesomeIcon);
app.use(createPinia());
app.use(router);
app.mount('#app');
