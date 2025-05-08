import axios from 'axios';

class ProduccionesService {
    constructor() {
        this.api = axios.create({
            baseURL: '/api/producciones'
        });
    }

    async fetchProducciones(filters = {}, pagination = { page: 0, size: 10 }) {
        try {
            // Construir parámetros de consulta
            const params = {
                page: pagination.page,
                size: pagination.size,
                sortBy: pagination.sortBy || 'titulo',
                sortDirection: pagination.sortDirection || 'asc',
                ...filters
            };

            // Manejar array de categorías
            if (filters.categorias && filters.categorias.length > 0) {
                params.categorias = filters.categorias;
            }

            const response = await this.api.get('/filtrar', {
                params,
                paramsSerializer: params => {
                    const parts = [];
                    for (const key in params) {
                        if (params.hasOwnProperty(key)) {
                            const value = params[key];
                            if (Array.isArray(value)) {
                                value.forEach(v => parts.push(`${key}=${encodeURIComponent(v)}`));
                            } else {
                                parts.push(`${key}=${encodeURIComponent(value)}`);
                            }
                        }
                    }
                    return parts.join('&');
                }
            });

            // Formato compatible con ambas páginas
            const formattedData = response.data?.data?.map(item => ({
                // Campos para la página de administración
                id: item.id,
                titulo: item.titulo,
                tipo: item.tipo,
                estreno: item.estreno,
                duracion: item.duracion,
                sinopsis: item.sinopsis,
                imagen: item.imagen || 'default-poster.jpg',
                clasificacionEdad: item.clasificacionEdad,
                categorias: item.categorias || [],
                puntuacion: item.puntuacion || 0,

                // Campos para la página normal de producciones
                title: item.titulo,
                type: item.tipo,
                plot: item.sinopsis,
                year: item.estreno ? new Date(item.estreno).getFullYear() : 'N/A',
                poster: item.imagen || 'default-poster.jpg',
                duration: item.duracion,
                isFavorite: false
            })) || [];

            return {
                data: formattedData,
                totalItems: response.data?.totalItems || 0,
                totalPages: response.data?.totalPages || 1
            };
        } catch (error) {
            console.error('Error fetching producciones:', error);
            throw error;
        }
    }

    async createProduccion(produccionData) {
        try {
            const response = await this.api.post('', produccionData);
            return this.formatProduccion(response.data);
        } catch (error) {
            console.error('Error creating produccion:', error);
            throw error;
        }
    }

    async updateProduccion(id, produccionData) {
        try {
            const response = await this.api.put(`/${id}`, produccionData);
            return this.formatProduccion(response.data);
        } catch (error) {
            console.error('Error updating produccion:', error);
            throw error;
        }
    }

    async deleteProduccion(id) {
        try {
            await this.api.delete(`/${id}`);
            return true;
        } catch (error) {
            console.error('Error deleting produccion:', error);
            throw error;
        }
    }

    formatProduccion(item) {
        return {
            id: item.id,
            titulo: item.titulo,
            tipo: item.tipo,
            estreno: item.estreno,
            duracion: item.duracion,
            sinopsis: item.sinopsis,
            imagen: item.imagen || 'default-poster.jpg',
            clasificacionEdad: item.clasificacionEdad,
            categorias: item.categorias || [],
            puntuacion: item.puntuacion || 0
        };
    }

    formatTipoProduccion(tipo) {
        const tiposMap = {
            'PELICULA': 'Película',
            'SERIE': 'Serie',
            'DOCUMENTAL': 'Documental',
            'CORTOMETRAJE': 'Cortometraje'
        };
        return tiposMap[tipo] || tipo;
    }

    formatDate(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleDateString('es-ES', { year: 'numeric', month: 'short', day: 'numeric' });
    }

    formatClasificacionEdad(clasificacion) {
        const map = {
            'TP': 'Todo público',
            '7': '+7 años',
            '12': '+12 años',
            '16': '+16 años',
            '18': '+18 años'
        };
        return map[clasificacion] || clasificacion;
    }
}

export default new ProduccionesService();