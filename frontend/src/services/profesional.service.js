import axios from 'axios';
import qs from 'qs';

const API_BASE_URL = 'http://localhost:8080/api/profesionales';

export default {
    // Operaciones CRUD básicas
    async getAllProfessionals(pagination = {}, sorting = {}) {
        try {
            const params = {
                ...pagination,
                ...sorting
            };

            const response = await axios.get(API_BASE_URL, {
                params,
                paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat', skipNulls: true })
            });

            return {
                data: response.data.data || [],
                totalItems: response.data.totalItems || 0,
                totalPages: response.data.totalPages || 1
            };
        } catch (error) {
            this.handleError(error, 'Error al obtener profesionales');
            return {
                data: [],
                totalItems: 0,
                totalPages: 1
            };
        }
    },

    async getProfessionalById(id) {
        try {
            const response = await axios.get(`${API_BASE_URL}/${id}`);
            return response.data;
        } catch (error) {
            this.handleError(error, 'Error al obtener el profesional');
            return null;
        }
    },

    async createProfessional(professionalData) {
        try {
            const response = await axios.post(API_BASE_URL, professionalData);
            return response.data;
        } catch (error) {
            this.handleError(error, 'Error al crear el profesional');
            throw error;
        }
    },

    async updateProfessional(id, professionalData) {
        try {
            const response = await axios.put(`${API_BASE_URL}/${id}`, professionalData);
            return response.data;
        } catch (error) {
            this.handleError(error, 'Error al actualizar el profesional');
            throw error;
        }
    },

    async deleteProfessional(id) {
        try {
            await axios.delete(`${API_BASE_URL}/${id}`);
            return true;
        } catch (error) {
            this.handleError(error, 'Error al eliminar el profesional');
            throw error;
        }
    },

    // Búsqueda y filtrado
    async searchProfessionals(filters, pagination = {}, sorting = {}) {
        try {
            const params = {
                ...filters,
                ...pagination,
                ...sorting
            };

            // Eliminar parámetros nulos o vacíos
            const cleanParams = Object.fromEntries(
                Object.entries(params).filter(([_, value]) => value !== null && value !== '')
            );

            const response = await axios.get(`${API_BASE_URL}/filtrar`, {
                params: cleanParams,
                paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat', skipNulls: true })
            });

            return {
                data: response.data.data || [],
                totalItems: response.data.totalItems || 0,
                totalPages: response.data.totalPages || 1
            };
        } catch (error) {
            this.handleError(error, 'Error al buscar profesionales');
            return {
                data: [],
                totalItems: 0,
                totalPages: 1
            };
        }
    },

    // Funciones utilitarias
    getDateRangeYearsAgo(years) {
        const today = new Date();
        const pastDate = new Date();
        pastDate.setFullYear(today.getFullYear() - years);

        return {
            desde: pastDate.toISOString().split('T')[0],
            hasta: today.toISOString().split('T')[0]
        };
    },

    mapProfessionalToCard(prof) {
        return {
            id: prof.id,
            nombre: prof.nombre,
            foto: prof.foto,
            fechaNacimiento: prof.fechaNacimiento,
            fechaInicio: prof.fechaInicio,
            lugarNacimiento: prof.lugarNacimiento,
            biografia: prof.biografia,
            participaciones: prof.participacionesCount
        };
    },

    formatDate(dateString) {
        if (!dateString) return '';
        const options = { year: 'numeric', month: 'short', day: 'numeric' };
        return new Date(dateString).toLocaleDateString('es-ES', options);
    },

    handleError(error, defaultMessage) {
        console.error(defaultMessage, error);

        let message = defaultMessage;
        if (error.response) {
            if (error.response.data.message) {
                message = error.response.data.message;
            }
            console.error('Detalles del error:', error.response.data);
        } else if (error.request) {
            message = 'No se recibió respuesta del servidor';
            console.error(message);
        } else {
            message = 'Error al configurar la solicitud';
            console.error(message, error.message);
        }

        return message;
    }
};