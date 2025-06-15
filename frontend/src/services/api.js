import axios from 'axios';
import { useAuthStore } from '@/stores/auth';
import { jwtDecode } from 'jwt-decode';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL,
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
});

// Interceptor de petición: añade token si existe
api.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, error => {
    return Promise.reject(error);
});

// Interceptor de respuesta: gestiona errores globales
api.interceptors.response.use(
    response => response,
    async error => {
        const authStore = useAuthStore();

        // Error de respuesta HTTP
        const status = error.response?.status;

        if (status === 401) {
            // Token inválido o expirado
            console.warn('Error 401: no autorizado');

            // Verificar si el token está expirado
            const token = localStorage.getItem('token');
            if (token) {
                try {
                    const decoded = jwtDecode(token);
                    if (decoded.exp * 1000 < Date.now()) {
                        console.warn('Token expirado, cerrando sesión');
                        authStore.logout();
                        window.location.href = '/login'; // O redirige donde desees
                    }
                } catch (e) {
                    authStore.logout();
                    window.location.href = '/login';
                }
            }
        } else if (status === 403) {
            console.warn('Error 403: acceso denegado');
            // Aquí podrías mostrar un toast sin cerrar sesión
        } else if (status >= 500) {
            console.error('Error del servidor');
            // Puedes notificar al usuario si lo deseas
        }

        return Promise.reject(error);
    }
);

export default api;
