// participaciones.service.js
import api from './api';

class ParticipacionesService {
    async createParticipacion(participacionData) {
        try {
            const response = await api.post('/api/participaciones', participacionData);
            return response.data;
        } catch (error) {
            console.error('Error creating participacion:', error);
            throw error;
        }
    }

    async updateParticipacion(id, participacionData) {
        try {
            const response = await api.put(`/api/participaciones/${id}`, participacionData);
            return response.data;
        } catch (error) {
            console.error('Error updating participacion:', error);
            throw error;
        }
    }

    async deleteParticipacion(id) {
        try {
            await api.delete(`/api/participaciones/${id}`);
            return true;
        } catch (error) {
            console.error('Error deleting participacion:', error);
            throw error;
        }
    }

    async getParticipacionesByProduccion(produccionId, pagination = { page: 0, size: 10 }) {
        try {
            const response = await api.get(`/api/participaciones/produccion/${produccionId}`, {
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
            console.error('Error fetching participaciones:', error);
            throw error;
        }
    }
}

export default new ParticipacionesService();