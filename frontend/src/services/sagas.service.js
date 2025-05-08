import axios from 'axios';

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

            const response = await axios.get('/api/sagas/filter', {
                params,
                paramsSerializer: params => {
                    return Object.entries(params)
                        .filter(([_, value]) => value !== null && value !== undefined)
                        .map(([key, value]) => `${key}=${encodeURIComponent(value)}`)
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
            throw error;
        }
    },

    async getSagaById(id) {
        try {
            const response = await axios.get(`/api/sagas/${id}`);
            return this.mapSagaResponse(response.data);
        } catch (error) {
            console.error('Error fetching saga:', error);
            throw error;
        }
    },

    async createSaga(sagaData) {
        try {
            const response = await axios.post('/api/sagas', this.prepareSagaRequest(sagaData));
            return this.mapSagaResponse(response.data);
        } catch (error) {
            console.error('Error creating saga:', error);
            throw error;
        }
    },

    async updateSaga(id, sagaData) {
        try {
            const response = await axios.put(`/api/sagas/${id}`, this.prepareSagaRequest(sagaData));
            return this.mapSagaResponse(response.data);
        } catch (error) {
            console.error('Error updating saga:', error);
            throw error;
        }
    },

    async deleteSaga(id) {
        try {
            await axios.delete(`/api/sagas/${id}`);
        } catch (error) {
            console.error('Error deleting saga:', error);
            throw error;
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
            fechaFin: sagaData.fechaFin || null,
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
    }
};