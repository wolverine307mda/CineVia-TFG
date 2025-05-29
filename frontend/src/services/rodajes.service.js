import api from './api';

class RodajesService {
    async createRodaje(rodajeData) {
        try {
            const response = await api.post('/api/rodajes', rodajeData);
            return response.data;
        } catch (error) {
            console.error('Error creating rodaje:', error);
            throw error;
        }
    }

    async updateRodaje(id, rodajeData) {
        try {
            const response = await api.put(`/api/rodajes/${id}`, rodajeData);
            return response.data;
        } catch (error) {
            console.error('Error updating rodaje:', error);
            throw error;
        }
    }

    async deleteRodaje(id) {
        try {
            await api.delete(`/api/rodajes/${id}`);
            return true;
        } catch (error) {
            console.error('Error deleting rodaje:', error);
            throw error;
        }
    }

    async getRodajesByProduccion(produccionId, pagination = { page: 0, size: 10 }) {
        try {
            const response = await api.get(`/api/rodajes/produccion/${produccionId}`, {
                params: {
                    page: pagination.page,
                    size: pagination.size
                }
            });
            return {
                data: response.data?.data || [],
                totalItems: response.data?.totalItems || 0,
                totalPages: response.data?.totalPages || 1
            };
        } catch (error) {
            console.error('Error fetching rodajes:', error);
            throw error;
        }
    }
}

export default new RodajesService();