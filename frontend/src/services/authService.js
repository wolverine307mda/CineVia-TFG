// Auth service
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

// Configuración de Axios
const api = axios.create({
    baseURL: 'http://localhost:8080',
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
});

// Interceptor para CORS
api.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
}, error => {
    return Promise.reject(error)
})

// Interceptor para añadir token
api.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token && !config.url.includes('/auth/')) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})

// Interceptor para manejar errores
api.interceptors.response.use(response => response, error => {
    if (error.response?.status === 401 || error.response?.status === 403) {
        localStorage.removeItem('token')
        localStorage.removeItem('userRole')
        window.location.href = '/auth/login'
    }
    return Promise.reject(error)
})

export async function signin(email, password) {
    try {
        const response = await api.post('/api/v1/auth/signin', { email, password })
        const token = response.data.token
        const role = response.data.role

        if (!token) {
            throw new Error('No se recibió token')
        }

        const decoded = jwtDecode(token)
        localStorage.setItem('token', token)
        localStorage.setItem('userRole', role)

        return {
            token,
            role
        }
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Error en el inicio de sesión')
    }
}

export async function fetchCurrentUser() {
    try {
        const response = await api.get('/api/users/me');
        return response.data;
    } catch (error) {
        if (error.response?.status === 401 || error.response?.status === 403) {
            logout();
        }
        throw error;
    }
}

export async function signup(userData) {
    const response = await api.post('/signup', userData);
    const token = response.data.token;
    const role = response.data.role;

    localStorage.setItem('token', token);
    localStorage.setItem('userRole', role);

    return {
        token,
        role
    };
}

export function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userRole');
}

export function isAuthenticated() {
    const token = localStorage.getItem('token');
    if (!token) return false;

    try {
        const decoded = jwtDecode(token);
        return decoded.exp * 1000 > Date.now();
    } catch {
        return false;
    }
}
