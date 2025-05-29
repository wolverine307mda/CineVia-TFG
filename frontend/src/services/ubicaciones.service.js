import api from './api';

class UbicacionesService {
    async fetchUbicaciones(filters = {}, pagination = { page: 0, size: 10 }) {
        try {
            const response = await api.post('/api/ubicaciones/filter', filters, {
                params: {
                    page: pagination.page,
                    size: pagination.size,
                    sortBy: 'nombre',
                    sortDirection: 'asc'
                }
            });
            return {
                data: response.data?.data || [],
                totalItems: response.data?.totalItems || 0,
                totalPages: response.data?.totalPages || 1
            };
        } catch (error) {
            console.error('Error fetching ubicaciones:', error);
            throw error;
        }
    }

    async getUbicacionById(id) {
        try {
            const response = await api.get(`/api/ubicaciones/${id}`);
            return response.data;
        } catch (error) {
            console.error('Error getting ubicacion:', error);
            throw error;
        }
    }
}

export default new UbicacionesService();