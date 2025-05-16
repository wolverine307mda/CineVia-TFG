import axios from 'axios';
import qs from 'qs';

class ProfesionalesService {
    constructor() {
        this.api = axios.create({
            baseURL: '/api/profesionales'
        });

        // Asegurar que 'this' se mantenga en el contexto correcto
        this.mapProfessionalToCard = this.mapProfessionalToCard.bind(this);
        this.formatDate = this.formatDate.bind(this);
        this.formatShortDate = this.formatShortDate.bind(this);
    }

    async fetchProfessionals(filters = {}, pagination = { page: 0, size: 10 }, sorting = {}) {
        try {
            const params = {
                ...filters,
                page: pagination.page,
                size: pagination.size,
                ...sorting
            };

            const cleanParams = Object.fromEntries(
                Object.entries(params).filter(([_, v]) => v !== null && v !== '')
            );

            const response = await this.api.get('/filtrar', {
                params: cleanParams,
                paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat', skipNulls: true })
            });

            return {
                data: response.data?.data?.map(this.mapProfessionalToCard) || [],
                totalItems: response.data?.totalItems || 0,
                totalPages: response.data?.totalPages || 1
            };
        } catch (error) {
            console.error('Error fetching professionals:', error);
            return {
                data: [],
                totalItems: 0,
                totalPages: 1
            };
        }
    }

    async getProfessionalById(id) {
        try {
            const response = await this.api.get(`/${id}`);
            return this.mapProfessionalToCard(response.data);
        } catch (error) {
            console.error('Error getting professional:', error);
            throw error;
        }
    }

    async createProfessional(professionalData) {
        try {
            const response = await this.api.post('', professionalData);
            return this.mapProfessionalToCard(response.data);
        } catch (error) {
            console.error('Error creating professional:', error);
            throw error;
        }
    }

    async updateProfessional(id, professionalData) {
        try {
            const response = await this.api.put(`/${id}`, professionalData);
            return this.mapProfessionalToCard(response.data);
        } catch (error) {
            console.error('Error updating professional:', error);
            throw error;
        }
    }

    async deleteProfessional(id) {
        try {
            await this.api.delete(`/${id}`);
            return true;
        } catch (error) {
            console.error('Error deleting professional:', error);
            throw error;
        }
    }

    async getParticipacionesByProfesional(profesionalId) {
        try {
            const response = await axios.get(`/api/participaciones/profesional/${profesionalId}`);
            return response.data.map(part => ({
                ...part.produccion,
                rol: part.rol,
                papel: part.papel
            }));
        } catch (error) {
            console.error('Error fetching participaciones:', error);
            throw error;
        }
    }

    mapProfessionalToCard(prof) {
        return {
            id: prof.id,
            nombre: prof.nombre,
            foto: prof.foto || 'default-avatar.jpg',
            fechaNacimiento: prof.fechaNacimiento,
            fechaInicio: prof.fechaInicio,
            lugarNacimiento: prof.lugarNacimiento,
            biografia: prof.biografia,
            participacionesCount: prof.participacionesCount || 0,
            name: prof.nombre,
            image: prof.foto || 'default-avatar.jpg',
            birthDate: this.formatDate(prof.fechaNacimiento),
            startDate: this.formatDate(prof.fechaInicio),
            location: prof.lugarNacimiento,
            bio: prof.biografia,
            worksCount: prof.participacionesCount || 0,
            isFavorite: false
        };
    }

    getDateRangeYearsAgo(years) {
        const today = new Date();
        const pastDate = new Date();
        pastDate.setFullYear(today.getFullYear() - years);

        return {
            desde: pastDate.toISOString().split('T')[0],
            hasta: today.toISOString().split('T')[0]
        };
    }

    formatDate(dateString) {
        if (!dateString) return 'N/A';
        const options = { year: 'numeric', month: 'short', day: 'numeric' };
        return new Date(dateString).toLocaleDateString('es-ES', options);
    }

    formatShortDate(dateString) {
        if (!dateString) return '';
        const options = { year: 'numeric', month: 'short' };
        return new Date(dateString).toLocaleDateString('es-ES', options);
    }

    handleError(error, defaultMessage = 'Error') {
        console.error(defaultMessage, error);
        let message = defaultMessage;

        if (error.response) {
            message = error.response.data?.message || defaultMessage;
        } else if (error.request) {
            message = 'No se recibió respuesta del servidor';
        } else {
            message = 'Error al configurar la solicitud';
        }

        throw new Error(message);
    }
}

export default new ProfesionalesService();