import api from './api';
import toast from "@/utils/toast.js";

export default {
    async fetchSagas(filters, pagination, sortOptions) {
        try {
            const params = {
                page: pagination.currentPage - 1,
                size: pagination.itemsPerPage,
                sortBy: sortOptions?.field || 'nombre',
                sortDirection: sortOptions?.direction || 'asc',
                ...this.cleanFilters(filters)
            };

            const response = await api.get('/api/sagas/filter', {
                params,
                paramsSerializer: params => {
                    return Object.entries(params)
                        .filter(([_, value]) => value !== null && value !== undefined && value !== '')
                        .map(([key, value]) => {
                            if (Array.isArray(value)) {
                                return value.map(v => `${key}=${encodeURIComponent(v)}`).join('&');
                            }
                            return `${key}=${encodeURIComponent(value)}`;
                        })
                        .join('&');
                }
            });

            return {
                data: response.data?.data?.map(this.mapSagaResponse) || [],
                totalItems: response.data?.totalItems || 0,
                totalPages: response.data?.totalPages || 1
            };
        } catch (error) {
            console.error('Error fetching sagas:', error);
            throw this.handleApiError(error);
        }
    },

    async getSagaById(id) {
        try {
            const response = await api.get(`/api/sagas/${id}`);
            return this.mapSagaResponse(response.data);
        } catch (error) {
            console.error('Error fetching saga:', error);
            throw this.handleApiError(error);
        }
    },

    async createSaga(sagaData) {
        try {
            const response = await api.post('/api/sagas', this.prepareSagaRequest(sagaData));
            return { success: true, data: this.mapSagaResponse(response.data) };
        } catch (error) {
            console.error('Error creating saga:', error);
            toast.error('Error al crear saga:', error)
            throw this.handleApiError(error);
        }
    },

    async updateSaga(id, sagaData) {
        try {
            const response = await api.put(`/api/sagas/${id}`, this.prepareSagaRequest(sagaData));
            return { success: true, data: this.mapSagaResponse(response.data) };
        } catch (error) {
            console.error('Error updating saga:', error);
            toast.error('Error al actulizar saga:', error)
            throw this.handleApiError(error);
        }
    },

    async deleteSaga(id) {
        try {
            await api.delete(`/api/sagas/${id}`);
        } catch (error) {
            console.error('Error deleting saga:', error);
            throw this.handleApiError(error);
        }
    },

    async addProduccionToSaga(sagaId, produccionId) {
        try {
            await api.post(`/api/sagas/${sagaId}/producciones/${produccionId}`);
            return { success: true };
        } catch (error) {
            console.error('Error adding produccion to saga:', error);
            throw this.handleApiError(error);
        }
    },

    async removeProduccionFromSaga(sagaId, produccionId) {
        try {
            await api.delete(`/api/sagas/${sagaId}/producciones/${produccionId}`);
            return { success: true };
        } catch (error) {
            console.error('Error removing produccion from saga:', error);
            throw this.handleApiError(error);
        }
    },

    cleanFilters(filters) {
        const cleaned = {};
        Object.keys(filters).forEach(key => {
            if (filters[key] !== null && filters[key] !== undefined && filters[key] !== '') {
                cleaned[key] = filters[key];
            }
        });
        return cleaned;
    },

    prepareSagaRequest(sagaData) {
        return {
            nombre: sagaData.nombre,
            descripcion: sagaData.descripcion || '',
            isAcabada: sagaData.isAcabada || false,
            fechaInicio: sagaData.fechaInicio || null,
            fechaFin: sagaData.isAcabada ? sagaData.fechaFin : null,
            imagen: sagaData.imagen || ''
        };
    },

    mapSagaResponse(response) {
        return {
            id: response.id,
            nombre: response.nombre,
            descripcion: response.descripcion,
            isAcabada: response.isAcabada,
            fechaInicio: response.fechaInicio,
            fechaFin: response.fechaFin,
            imagen: response.imagen || 'default-saga.jpg',
            producciones: response.producciones || []
        };
    },

    formatDate(dateString) {
        if (!dateString) return 'N/A';

        try {
            const date = new Date(dateString);
            return isNaN(date.getTime()) ? 'N/A' :
                date.toLocaleDateString('es-ES', {
                    year: 'numeric',
                    month: 'short',
                    day: 'numeric'
                });
        } catch (e) {
            console.error('Error formatting date:', e);
            return 'N/A';
        }
    },

    formatStatus(isFinished) {
        return isFinished ? 'Finalizada' : 'En curso';
    },

    handleApiError(error) {
        if (error.response) {
            // Error de servidor con respuesta
            const message = error.response.data?.message || 'Error en el servidor';
            const status = error.response.status;

            if (status === 400) {
                return new Error(`Datos inválidos: ${message}`);
            } else if (status === 404) {
                return new Error('Recurso no encontrado');
            } else if (status === 500) {
                return new Error('Error interno del servidor');
            }
            return new Error(`Error ${status}: ${message}`);
        } else if (error.request) {
            return new Error('No se recibió respuesta del servidor');
        } else {
            return new Error('Error al configurar la solicitud');
        }
    },

    async uploadTempSagaImage(file, oldUrl = null) {
        const formData = new FormData();
        formData.append('file', file);
        if (oldUrl) formData.append('oldUrl', oldUrl);

        const response = await api.post(`/api/sagas/upload-image-temp`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
        return response.data;
    }
};