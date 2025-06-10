import { defineStore } from 'pinia';
import { signin, signup, logout, fetchCurrentUser } from '../services/auth.service.js';
import { jwtDecode } from 'jwt-decode';

export const useAuthStore = defineStore('auth', {
    state: () => ({
        user: null,
        token: localStorage.getItem('token') || null,
        role: localStorage.getItem('userRole') || null,
        isAuthenticated: !!localStorage.getItem('token')
    }),

    getters: {
        isAdmin: (state) => state.role === 'ADMINISTRADOR',
        isUser: (state) => state.role === 'USUARIO'
    },

    actions: {
        async fetchCurrentUser() {
            this.user = await fetchCurrentUser();
            return this.user;
        },

        async login(email, password) {
            try {
                const response = await signin(email, password);
                this.token = response.token;
                this.role = response.role;
                this.isAuthenticated = true;

                // Guardar en localStorage
                localStorage.setItem('token', response.token);
                localStorage.setItem('userRole', response.role);

                // Obtener y establecer usuario ANTES de redirigir
                this.user = await fetchCurrentUser();

                return response;
            } catch (error) {
                this.logout();
                throw error;
            }
        },

        async register(userData) {
            try {
                const response = await signup(userData);
                this.token = response.token;
                this.role = response.role;
                this.isAuthenticated = true;
                this.user = await fetchCurrentUser();
                return response;
            } catch (error) {
                this.logout();
                throw error;
            }
        },

        async checkAuth() {
            if (this.token) {
                try {
                    // Verificar si el token es válido
                    const decoded = jwtDecode(this.token);
                    if (decoded.exp * 1000 < Date.now()) {
                        throw new Error('Token expirado');
                    }

                    // Actualizar datos del usuario
                    this.user = await fetchCurrentUser();
                    this.role = decoded.role;
                    this.isAuthenticated = true;
                } catch (error) {
                    this.logout();
                    throw error;
                }
            }
        },

        logout() {
            logout();
            this.token = null;
            this.role = null;
            this.isAuthenticated = false;
            this.user = null;
        },

        // Método para verificar roles
        hasRole(requiredRole) {
            if (!this.role) return false;
            return this.role === requiredRole;
        }
    }
});