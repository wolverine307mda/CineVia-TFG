import api from './api';

export default {
    async fetchUsers(filters = {}, pagination = {}, sortOptions = {}) {
        try {
            const params = {
                page: pagination.currentPage ? pagination.currentPage - 1 : 0,
                size: pagination.itemsPerPage || 10,
                sortBy: sortOptions.field || 'createdAt',
                sortDirection: sortOptions.direction || 'desc',
                ...this.cleanFilters(filters)
            };

            const response = await api.get('/api/users/filter', {
                params: this.serializeParams(params)
            });

            // Ajustamos los campos a tu respuesta real
            return {
                content: response.data?.data?.map(this.mapUserResponse) || [],
                totalItems: response.data?.totalItems || 0,
                totalPages: response.data?.totalPages || 1,
                pageable: {}, // puedes ignorar o adaptar si necesitas
                first: response.data?.currentPage === 0,
                last: response.data?.currentPage === response.data?.totalPages - 1,
                number: response.data?.currentPage || 0,
                size: response.data?.pageSize || 10
            };
        } catch (error) {
            console.error('Error fetching users:', error);
            throw this.handleApiError(error);
        }
    },

    async getUserById(id) {
        try {
            const response = await api.get(`/api/users/${id}`);
            return this.mapUserResponse(response.data);
        } catch (error) {
            console.error('Error fetching user:', error);
            throw this.handleApiError(error);
        }
    },

    async createUser(userData) {
        try {
            const response = await api.post('/api/users', this.prepareUserRequest(userData));
            return this.mapUserResponse(response.data);
        } catch (error) {
            console.error('Error creating user:', error);
            throw this.handleApiError(error);
        }
    },

    async updateUser(id, userData) {
        try {
            const response = await api.put(`/api/users/${id}`, this.prepareUpdateRequest(userData));
            return this.mapUserResponse(response.data);
        } catch (error) {
            console.error('Error updating user:', error);
            throw this.handleApiError(error);
        }
    },

    async deleteUser(id) {
        try {
            await api.delete(`/api/users/${id}`);
            return true;
        } catch (error) {
            console.error('Error deleting user:', error);
            throw this.handleApiError(error);
        }
    },

    async softDeleteUser(id) {
        try {
            await api.delete(`/api/users/soft/${id}`);
            return true;
        } catch (error) {
            console.error('Error soft deleting user:', error);
            throw this.handleApiError(error);
        }
    },

    async restoreUser(id) {
        try {
            await api.post(`/api/users/restore/${id}`);
            return true;
        } catch (error) {
            console.error('Error restoring user:', error);
            throw this.handleApiError(error);
        }
    },
    async uploadAvatar(id, file) {
        const formData = new FormData();
        formData.append('file', file);
        const response = await api.post(`/api/users/${id}/upload-avatar`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
        return response.data;
    },

    // Helper methods
    cleanFilters(filters) {
        const cleaned = {};
        Object.keys(filters).forEach(key => {
            if (filters[key] !== null && filters[key] !== undefined && filters[key] !== '') {
                cleaned[key] = filters[key];
            }
        });
        return cleaned;
    },

    serializeParams(params) {
        const serialized = {};
        Object.keys(params).forEach(key => {
            if (params[key] !== null && params[key] !== undefined) {
                serialized[key] = params[key];
            }
        });
        return serialized;
    },

    prepareUserRequest(userData) {
        return {
            username: userData.username,
            email: userData.email,
            password: userData.password,
            nombre: userData.nombre,
            fechaNacimiento: userData.fechaNacimiento,
            telefono: userData.telefono,
            apellido: userData.apellido || '',
            rol: userData.rol || 'USUARIO',
            avatar: userData.avatar || ''
        };
    },

    prepareUpdateRequest(userData) {
        return {
            nombre: userData.nombre,
            apellido: userData.apellido || '',
            telefono: userData.telefono || '',
            fechaNacimiento: userData.fechaNacimiento || '',
            rol: userData.rol || 'USUARIO',
            avatar: userData.avatar || ''
        };
    },

    mapUserResponse(response) {
        return {
            id: response.id,
            username: response.username,
            email: response.email,
            nombre: response.nombre,
            fechaNacimiento: response.fechaNacimiento,
            telefono: response.telefono,
            apellido: response.apellido,
            rol: response.rol,
            avatar: response.avatar || 'default-user.jpg',
            createdAt: response.createdAt,
            isDeleted: response.isDeleted || false
        };
    },

    handleApiError(error) {
        if (error.response) {
            const message = error.response.data?.message || 'Error en el servidor';
            const status = error.response.status;

            if (status === 400) return new Error(`Datos inválidos: ${message}`);
            if (status === 401) return new Error('No autorizado');
            if (status === 403) return new Error('Prohibido');
            if (status === 404) return new Error('Recurso no encontrado');
            if (status === 409) return new Error('Conflicto: El recurso ya existe');
            if (status === 500) return new Error('Error interno del servidor');

            return new Error(`Error ${status}: ${message}`);
        } else if (error.request) {
            return new Error('No se recibió respuesta del servidor');
        } else {
            return new Error('Error al configurar la solicitud');
        }
    }
};