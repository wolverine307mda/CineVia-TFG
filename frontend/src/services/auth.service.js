// auth.service.js
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

// Interceptor para añadir token a las solicitudes
api.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token && !config.url.includes('/auth/')) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, error => {
    return Promise.reject(error);
});

// Interceptor para manejar respuestas
api.interceptors.response.use(
    response => response,
    error => {
        if (error.response?.status === 401 || error.response?.status === 403) {
            logout();
            window.location.href = '/auth/login';
        }
        return Promise.reject(error);
    }
);

/**
 * Inicia sesión con email y contraseña
 * @param {string} email
 * @param {string} password
 * @returns {Promise<{token: string, role: string}>}
 */
export async function signin(email, password) {
    try {
        const response = await api.post('/api/v1/auth/signin', { email, password });
        const { token, role } = response.data;

        if (!token) {
            throw new Error('No se recibió token en la respuesta');
        }

        const decoded = jwtDecode(token);
        localStorage.setItem('token', token);
        localStorage.setItem('userRole', role);
        localStorage.setItem('userEmail', email);

        return { token, role };
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Error en el inicio de sesión');
    }
}

/**
 * Registra un nuevo usuario
 * @param {Object} userData - Datos del usuario a registrar
 * @param {string} userData.firstName - Nombre del usuario
 * @param {string} userData.lastName - Apellidos del usuario
 * @param {string} userData.username - Nombre de usuario
 * @param {string} userData.email - Correo electrónico
 * @param {string} userData.phone - Teléfono (opcional)
 * @param {string} userData.password - Contraseña
 * @returns {Promise<{token: string, role: string}>}
 */
export async function signup(userData) {
    try {
        // Eliminar confirmPassword si existe
        const { confirmPassword, ...registrationData } = userData;

        const response = await api.post('/api/v1/auth/signup', registrationData);
        const { token, role } = response.data;

        if (!token) {
            throw new Error('No se recibió token en la respuesta');
        }

        localStorage.setItem('token', token);
        localStorage.setItem('userRole', role);
        localStorage.setItem('userEmail', userData.email);

        return { token, role };
    } catch (error) {
        let errorMessage = 'Error en el registro';

        if (error.response) {
            // Manejar errores de validación del backend
            if (error.response.status === 400 && error.response.data?.errors) {
                const errors = Object.values(error.response.data.errors).flat();
                errorMessage = errors.join(', ');
            } else if (error.response.data?.message) {
                errorMessage = error.response.data.message;
            }
        }

        throw new Error(errorMessage);
    }
}

/**
 * Cierra la sesión del usuario
 */
export function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('userRole');
    localStorage.removeItem('userEmail');
}

/**
 * Verifica si el usuario está autenticado
 * @returns {boolean}
 */
export function isAuthenticated() {
    const token = localStorage.getItem('token');
    if (!token) return false;

    try {
        const decoded = jwtDecode(token);
        return decoded.exp * 1000 > Date.now();
    } catch (error) {
        console.error('Error al decodificar token:', error);
        return false;
    }
}

/**
 * Obtiene el rol del usuario actual
 * @returns {string|null}
 */
export function getCurrentUserRole() {
    return localStorage.getItem('userRole');
}

/**
 * Obtiene el email del usuario actual
 * @returns {string|null}
 */
export function getCurrentUserEmail() {
    return localStorage.getItem('userEmail');
}

/**
 * Obtiene los datos del usuario actual
 * @returns {Promise<Object>}
 */
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

/**
 * Verifica si un nombre de usuario está disponible
 * @param {string} username
 * @returns {Promise<boolean>}
 */
export async function checkUsernameAvailability(username) {
    try {
        const response = await api.get(`/api/v1/auth/check-username?username=${username}`);
        return response.data.available;
    } catch (error) {
        console.error('Error al verificar nombre de usuario:', error);
        throw new Error('Error al verificar disponibilidad del nombre de usuario');
    }
}

/**
 * Verifica si un email está disponible
 * @param {string} email
 * @returns {Promise<boolean>}
 */
export async function checkEmailAvailability(email) {
    try {
        const response = await api.get(`/api/v1/auth/check-email?email=${email}`);
        return response.data.available;
    } catch (error) {
        console.error('Error al verificar email:', error);
        throw new Error('Error al verificar disponibilidad del email');
    }
}

/**
 * Actualiza los datos del usuario
 * @param {Object} userData
 * @returns {Promise<Object>}
 */
export async function updateUserProfile(userData) {
    try {
        const response = await api.put('/api/users/me', userData);
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Error al actualizar perfil');
    }
}

/**
 * Cambia la contraseña del usuario
 * @param {string} currentPassword
 * @param {string} newPassword
 * @returns {Promise<void>}
 */
export async function changePassword(currentPassword, newPassword) {
    try {
        await api.post('/api/users/change-password', { currentPassword, newPassword });
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Error al cambiar contraseña');
    }
}

/**
 * Solicita un reseteo de contraseña
 * @param {string} email
 * @returns {Promise<void>}
 */
export async function requestPasswordReset(email) {
    try {
        await api.post('/api/v1/auth/forgot-password', { email });
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Error al solicitar reseteo de contraseña');
    }
}

/**
 * Resetea la contraseña con un token
 * @param {string} token
 * @param {string} newPassword
 * @returns {Promise<void>}
 */
export async function resetPassword(token, newPassword) {
    try {
        await api.post('/api/v1/auth/reset-password', { token, newPassword });
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Error al resetear contraseña');
    }
}

export default {
    signin,
    signup,
    logout,
    isAuthenticated,
    getCurrentUserRole,
    getCurrentUserEmail,
    fetchCurrentUser,
    checkUsernameAvailability,
    checkEmailAvailability,
    updateUserProfile,
    changePassword,
    requestPasswordReset,
    resetPassword
};