// src/axios.js
import axios from 'axios';
import router from "@/router/index.js";
import {useAuthStore} from "@/stores/auth.js";

const api = axios.create({
    baseURL: `${import.meta.env.VITE_API_URL}/api/v1`,
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
});

api.interceptors.request.use(config => {
    const token = localStorage.getItem('token'); // Usar el mismo nombre de clave
    if (token && !config.url.includes('/auth/')) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// Reemplaza tu interceptor actual con este:
api.interceptors.response.use(response => response, error => {
    if (error.response?.status === 401 || error.response?.status === 403) {
        const authStore = useAuthStore();
        authStore.logout();

        // Evitar redirección infinita si ya está en login
        if (!router.currentRoute.value.meta.public) {
            router.push('/auth/login');
        }
    }
    return Promise.reject(error);
});

export default api;
