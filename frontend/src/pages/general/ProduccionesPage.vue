<template>
  <Header :dark-mode="darkMode" @toggle-dark-mode="toggleDarkMode"/>

  <div class="movies-container" :class="{ 'dark-mode': darkMode }">
    <!-- Encabezado con título y modo oscuro -->
    <div class="header-section">
      <h1 class="main-title">
        <i class="fas fa-film"></i> Catálogo de Películas
      </h1>
    </div>

    <!-- Barra de búsqueda -->
    <div class="search-bar">
      <div class="search-box">
        <i class="fas fa-search"></i>
        <input
            type="text"
            v-model="filters.titulo"
            placeholder="Buscar por título..."
            @keyup.enter="fetchProducciones"
        />
        <button class="clear-btn" @click="clearSearch" v-if="filters.titulo">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <button class="search-btn" @click="fetchProducciones">
        <i class="fas fa-search"></i> Buscar
      </button>
      <button class="filter-btn" @click="showFiltersModal = true">
        <i class="fas fa-ellipsis-v"></i>
      </button>
    </div>

    <!-- Listado de Películas -->
    <div class="movies-list">
      <div v-if="isLoading" class="loading-indicator">
        <i class="fas fa-spinner fa-spin"></i> Cargando producciones...
      </div>

      <div v-else-if="producciones.length === 0" class="no-results">
        <i class="fas fa-film"></i>
        <h3>No se encontraron producciones</h3>
        <p>Intenta ajustar tus filtros de búsqueda</p>
        <button @click="resetFilters" class="reset-btn">
          <i class="fas fa-undo"></i> Reiniciar filtros
        </button>
      </div>

      <transition-group v-else name="fade-staggered" tag="div" class="movies-grid">
        <movie-card
            v-for="produccion in producciones"
            :key="produccion.id"
            :movie="produccion"
            :dark-mode="darkMode"
            @toggle-favorite="toggleFavorite"
        />
      </transition-group>

      <!-- Paginación -->
      <div class="pagination-controls" v-if="totalPages > 1">
        <button
            class="pagination-btn"
            :disabled="currentPage === 1"
            @click="prevPage"
        >
          <i class="fas fa-chevron-left"></i>
        </button>

        <span class="page-indicator">
          Página {{ currentPage }} de {{ totalPages }}
        </span>

        <button
            class="pagination-btn"
            :disabled="currentPage === totalPages"
            @click="nextPage"
        >
          <i class="fas fa-chevron-right"></i>
        </button>
      </div>
    </div>
  </div>

  <!-- Modal de Filtros -->
  <div class="modal-overlay" v-if="showFiltersModal" @click.self="showFiltersModal = false">
    <div class="filters-modal" :class="{ 'dark-mode': darkMode }">
      <div class="modal-header">
        <h3><i class="fas fa-filter"></i> Filtros Avanzados</h3>
        <button class="close-btn" @click="showFiltersModal = false">
          <i class="fas fa-times"></i>
        </button>
      </div>

      <div class="modal-body">
        <!-- Filtro por tipo -->
        <div class="filter-section">
          <div class="section-header" @click="toggleSection('type')">
            <i class="fas fa-film"></i>
            <span>Tipo</span>
            <i class="section-icon fas" :class="expandedSection === 'type' ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
          </div>
          <div class="section-content" v-show="expandedSection === 'type'">
            <div class="type-options">
              <button
                  v-for="type in typeOptions"
                  :key="type.value"
                  :class="{ 'active': filters.tipo === type.value }"
                  @click="filters.tipo = filters.tipo === type.value ? null : type.value"
              >
                {{ type.label }}
              </button>
            </div>
          </div>
        </div>

        <!-- Filtro por género -->
        <div class="filter-section">
          <div class="section-header" @click="toggleSection('genre')">
            <i class="fas fa-tags"></i>
            <span>Géneros</span>
            <i class="section-icon fas" :class="expandedSection === 'genre' ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
          </div>
          <div class="section-content" v-show="expandedSection === 'genre'">
            <div class="genre-tags">
              <button
                  v-for="genre in genres"
                  :key="genre.value"
                  :class="{ 'active': filters.categorias?.includes(genre.value) }"
                  @click="toggleGenre(genre.value)"
              >
                {{ genre.name }}
              </button>
            </div>
          </div>
        </div>

        <!-- Filtro por año -->
        <div class="filter-section">
          <div class="section-header" @click="toggleSection('year')">
            <i class="fas fa-calendar-alt"></i>
            <span>Año de lanzamiento</span>
            <i class="section-icon fas" :class="expandedSection === 'year' ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
          </div>
          <div class="section-content" v-show="expandedSection === 'year'">
            <div class="year-range">
              <div class="year-inputs">
                <div class="input-group">
                  <label>Desde:</label>
                  <input
                      type="number"
                      v-model="filters.estrenoDesde"
                      placeholder="Año mínimo"
                      min="1900"
                      :max="new Date().getFullYear()"
                  >
                </div>
                <div class="input-group">
                  <label>Hasta:</label>
                  <input
                      type="number"
                      v-model="filters.estrenoHasta"
                      placeholder="Año máximo"
                      min="1900"
                      :max="new Date().getFullYear()"
                  >
                </div>
              </div>
              <div class="quick-years">
                <button @click="setYearRange(new Date().getFullYear() - 3, new Date().getFullYear())">Últimos 3 años</button>
                <button @click="setYearRange(2010, 2019)">2010s</button>
                <button @click="setYearRange(2000, 2009)">2000s</button>
              </div>
            </div>
          </div>
        </div>

        <!-- Filtro por clasificación de edad -->
        <div class="filter-section">
          <div class="section-header" @click="toggleSection('rating')">
            <i class="fas fa-star"></i>
            <span>Clasificación de Edad</span>
            <i class="section-icon fas" :class="expandedSection === 'rating' ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
          </div>
          <div class="section-content" v-show="expandedSection === 'rating'">
            <div class="rating-filter">
              <v-select
                  v-model="filters.clasificacionEdad"
                  :options="clasificacionOptions"
                  label="label"
                  :reduce="option => option.value"
                  placeholder="Selecciona clasificación"
                  :clearable="true"
              ></v-select>
            </div>
          </div>
        </div>

        <!-- Filtro por duración -->
        <div class="filter-section">
          <div class="section-header" @click="toggleSection('duration')">
            <i class="fas fa-clock"></i>
            <span>Duración (minutos)</span>
            <i class="section-icon fas" :class="expandedSection === 'duration' ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
          </div>
          <div class="section-content" v-show="expandedSection === 'duration'">
            <div class="duration-inputs">
              <div class="input-group">
                <label>Mínimo:</label>
                <input
                    type="number"
                    v-model="filters.duracionMin"
                    placeholder="Mínimo"
                    min="0"
                >
              </div>
              <div class="input-group">
                <label>Máximo:</label>
                <input
                    type="number"
                    v-model="filters.duracionMax"
                    placeholder="Máximo"
                    min="0"
                >
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="modal-footer">
        <div class="results-count">
          {{ totalItems }} producciones encontradas
        </div>
        <div class="modal-actions">
          <button class="reset-btn" @click="resetFilters">
            <i class="fas fa-undo"></i> Limpiar Todo
          </button>
          <button class="apply-btn" @click="applyFilters">
            <i class="fas fa-check"></i> Aplicar Filtros
          </button>
        </div>
      </div>
    </div>
  </div>

  <Footer :dark-mode="darkMode" />
</template>

<script>
import vSelect from 'vue-select'
import 'vue-select/dist/vue-select.css'
import MovieCard from '@/components/cards/MovieCard.vue'
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import axios from 'axios';
import qs from 'qs';

export default {
  name: 'MoviesCatalog',
  components: {
    Footer,
    Header,
    vSelect,
    MovieCard
  },
  data() {
    return {
      sortField: 'titulo',
      darkMode: false,
      showFiltersModal: false,
      expandedSection: null,
      producciones: [],
      isLoading: false,
      currentPage: 1,
      itemsPerPage: 12,
      totalItems: 0,
      totalPages: 1,
      filters: {
        titulo: null,
        tipo: null,
        estrenoDesde: null,
        estrenoHasta: null,
        categorias: null,
        clasificacionEdad: null,
        duracionMin: null,
        duracionMax: null,
      },
      typeOptions: [
        {label: 'Película', value: 'PELICULA'},
        {label: 'Serie', value: 'SERIE'}
      ],
      genres: [
        {name: 'Acción', value: 'Acción'},
        {name: 'Aventura', value: 'Aventura'},
        {name: 'Animación', value: 'Animación'},
        {name: 'Comedia', value: 'Comedia'},
        {name: 'Crimen', value: 'Crimen'},
        {name: 'Documental', value: 'Documental'},
        {name: 'Drama', value: 'Drama'},
        {name: 'Fantasía', value: 'Fantasía'},
        {name: 'Horror', value: 'Horror'},
        {name: 'Misterio', value: 'Misterio'},
        {name: 'Romance', value: 'Romance'},
        {name: 'Ciencia Ficción', value: 'Ciencia Ficción'},
        {name: 'Thriller', value: 'Thriller'}
      ],
      clasificacionOptions: [
        {label: 'Todo público', value: 'TP'},
        {label: '+7 años', value: '7'},
        {label: '+12 años', value: '12'},
        {label: '+16 años', value: '16'},
        {label: '+18 años', value: '18'}
      ]
    }
  },
  methods: {
    async fetchProducciones() {
      this.isLoading = true;
      try {
        const params = {
          page: this.currentPage - 1,
          size: this.itemsPerPage,
          sortDirection: 'asc',
          ...this.filters
        };

        console.log('Parámetros enviados:', params);

        const response = await axios.get('/api/producciones/filtrar', {
          params,
          paramsSerializer: params => qs.stringify(params, {arrayFormat: 'repeat'})
        });

        console.log('Respuesta completa:', response);

        if (!response.data || !response.data.data) {
          throw new Error('La respuesta no tiene la estructura esperada');
        }

        this.producciones = Array.isArray(response.data.data)
            ? response.data.data.map(item => {
              console.log('Procesando item:', item);
              return {
                id: item.id || '',
                title: item.titulo || 'Sin título',
                type: item.tipo || 'PELICULA',
                estreno: item.estreno || null,
                duration: item.duracion || 0,
                plot: item.sinopsis || 'Sin sinopsis disponible',
                imagen: item.imagen || 'default-poster.jpg',
                clasificacionEdad: item.clasificacionEdad || 0,
                categorias: item.categorias || [],
                puntuacion: item.puntuacion || 0,
                isFavorite: false,
                year: item.estreno ? new Date(item.estreno).getFullYear() : 'N/A',
                poster: item.imagen || 'default-poster.jpg'
              };
            })
            : [];

        this.totalItems = response.data.totalItems || 0;
        this.totalPages = response.data.totalPages || 1;

      } catch (error) {
        console.error('Error en fetchProducciones:', error);
        console.error('Detalles del error:', error.response?.data || error.message);
        this.producciones = [];
        this.totalItems = 0;
        this.totalPages = 1;
      } finally {
        this.isLoading = false;
      }
    },
    toggleDarkMode() {
      this.darkMode = !this.darkMode;
      localStorage.setItem('darkMode', this.darkMode);
    },

    toggleSection(section) {
      this.expandedSection = this.expandedSection === section ? null : section;
    },

    toggleGenre(genre) {
      if (!this.filters.categorias) {
        this.filters.categorias = [];
      }

      const index = this.filters.categorias.indexOf(genre);
      if (index === -1) {
        this.filters.categorias.push(genre);
      } else {
        this.filters.categorias.splice(index, 1);
      }
    },

    setYearRange(start, end) {
      this.filters.estrenoDesde = start;
      this.filters.estrenoHasta = end;
    },

    resetFilters() {
      this.filters = {
        titulo: null,
        tipo: null,
        estrenoDesde: null,
        estrenoHasta: null,
        categorias: null,
        clasificacionEdad: null,
        duracionMin: null,
        duracionMax: null,
      };
      this.currentPage = 1;
      this.fetchProducciones();
    },

    applyFilters() {
      this.showFiltersModal = false;
      this.currentPage = 1;
      this.fetchProducciones();
    },

    clearSearch() {
      this.filters.titulo = '';
      this.fetchProducciones();
    },

    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchProducciones();
        window.scrollTo({top: 0, behavior: 'smooth'});
      }
    },

    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchProducciones();
        window.scrollTo({top: 0, behavior: 'smooth'});
      }
    },

    toggleFavorite(produccionId) {
      const produccion = this.producciones.find(p => p.id === produccionId);
      if (produccion) {
        produccion.isFavorite = !produccion.isFavorite;
      }
    }
  },
  created() {
    const savedMode = localStorage.getItem('darkMode');
    if (savedMode) {
      this.darkMode = savedMode === 'true';
    }
    this.fetchProducciones();
  }
}
</script>

<style scoped src="@/assets/styles/general.css"></style>