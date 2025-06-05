import api from './api';

class ProduccionesService {

    async fetchProducciones(filters = {}, pagination = { page: 0, size: 10 }) {
        try {
            const cleanFilters = Object.fromEntries(
                Object.entries(filters).filter(([_, v]) => v !== null && v !== '' && !(Array.isArray(v) && v.length === 0))
            );

            if (cleanFilters.estrenoDesde) cleanFilters.estrenoDesde = parseInt(cleanFilters.estrenoDesde);
            if (cleanFilters.estrenoHasta) cleanFilters.estrenoHasta = parseInt(cleanFilters.estrenoHasta);
            if (cleanFilters.duracionMin) cleanFilters.duracionMin = parseInt(cleanFilters.duracionMin);
            if (cleanFilters.duracionMax) cleanFilters.duracionMax = parseInt(cleanFilters.duracionMax);

            const params = {
                page: pagination.page,
                size: pagination.size,
                ...cleanFilters
            };

            if (pagination.sortBy) {
                params.sortBy = pagination.sortBy;
                params.sortDirection = pagination.sortDirection || 'asc';
            }

            const response = await api.get('/api/producciones/filtrar', {
                params,
                paramsSerializer: params => {
                    const parts = [];
                    for (const key in params) {
                        if (params.hasOwnProperty(key)) {
                            const value = params[key];
                            if (Array.isArray(value)) {
                                value.forEach(v => parts.push(`${key}=${encodeURIComponent(v)}`));
                            } else if (value !== null && value !== undefined) {
                                parts.push(`${key}=${encodeURIComponent(value)}`);
                            }
                        }
                    }
                    return parts.join('&');
                }
            });

            const formattedData = response.data?.data?.map(item => ({
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

    async fetchFilterOptions() {
        try {
            const [tiposRes, categoriasRes, clasificacionesRes] = await Promise.all([
                api.get('/api/producciones/tipos'),
                api.get('/api/producciones/categorias'),
                api.get('/api/producciones/clasificaciones-edad')
            ]);

            return {
                tiposProduccion: tiposRes.data?.map(t => t.toString()) || [],
                categoriasDisponibles: categoriasRes.data?.map(c => c.toString()) || [],
                clasificacionesEdad: clasificacionesRes.data?.map(c => c.toString()) || []
            };
        } catch (error) {
            console.error('Error fetching filter options:', error);
            throw error;
        }
    }

    async createProduccion(produccionData) {
        try {
            const response = await api.post('/api/producciones', produccionData);
            return this.formatProduccion(response.data);
        } catch (error) {
            console.error('Error creating produccion:', error);
            throw error;
        }
    }

    async updateProduccion(id, produccionData) {
        try {
            const response = await api.put(`/api/producciones/${id}`, produccionData);
            return this.formatProduccion(response.data);
        } catch (error) {
            console.error('Error updating produccion:', error);
            throw error;
        }
    }

    async deleteProduccion(id) {
        try {
            await api.delete(`/api/producciones/${id}`);
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

    formatClasificacionEdad(clasificacion) {
        const map = {
            '0': 'Todo público',
            '7': '+7 años',
            '12': '+12 años',
            '16': '+16 años',
            '18': '+18 años'
        };
        return map[clasificacion] || clasificacion;
    }

    formatDate(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleDateString('es-ES', { year: 'numeric', month: 'short', day: 'numeric' });
    }

    async uploadProduccionImage(id, file) {
        const formData = new FormData();
        formData.append('file', file);
        const response = await api.post(`/api/producciones/${id}/upload-image`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
        return response.data;
    }

    async fetchPeliculaCompleta(idProduccion) {
        try {
            const produccionResponse = await api.get(`/api/producciones/completa/${idProduccion}`);
            const produccionData = produccionResponse.data;

            const peliculaCompleta = {
                ...produccionData,
                sagaCompleta: null,
                participacionesCompletas: [],
                rodajesCompletos: []
            };

            const secondaryRequests = [];

            if (produccionData.saga?.id) {
                secondaryRequests.push(
                    api.get(`/api/sagas/${produccionData.saga.id}`)
                        .then(response => {
                            peliculaCompleta.sagaCompleta = response.data;
                        })
                        .catch(error => {
                            console.error('Error al obtener la saga:', error);
                            peliculaCompleta.sagaCompleta = null;
                        })
                );
            }

            if (produccionData.participaciones?.length > 0) {
                produccionData.participaciones.forEach(participacion => {
                    secondaryRequests.push(
                        api.get(`/api/participaciones/${participacion.id}`)
                            .then(response => {
                                peliculaCompleta.participacionesCompletas.push(response.data);
                            })
                            .catch(error => {
                                console.error(`Error al obtener participación ${participacion.id}:`, error);
                            })
                    );
                });
            }

            if (produccionData.rodajes?.length > 0) {
                produccionData.rodajes.forEach(rodaje => {
                    secondaryRequests.push(
                        api.get(`/api/rodajes/${rodaje.id}`)
                            .then(response => {
                                peliculaCompleta.rodajesCompletos.push(response.data);
                            })
                            .catch(error => {
                                console.error(`Error al obtener rodaje ${rodaje.id}:`, error);
                            })
                    );
                });
            }

            await Promise.all(secondaryRequests);

            return peliculaCompleta;
        } catch (error) {
            console.error('Error al obtener la película completa:', error);
            throw error;
        }
    }

}

export default new ProduccionesService();
