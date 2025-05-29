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
        <div class="pagination-inner">
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
      <div class="filters-modal">
        <div class="modal-header">
          <h3><i class="fas fa-filter"></i> Filtros Avanzados</h3>
          <button class="close-btn" @click="showFiltersModal = false">
            <i class="fas fa-times"></i>
          </button>
        </div>

        <div class="modal-body">
          <!-- Sección de Ordenación -->
          <div class="filter-section">
            <div class="section-header" @click="toggleSection('sort')">
              <i class="fas fa-sort"></i>
              <span>Ordenar por</span>
              <i class="section-icon fas" :class="expandedSection === 'sort' ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
            </div>
            <div class="section-content" v-show="expandedSection === 'sort'">
              <div class="sort-options">
                <div class="sort-option">
                  <label>
                    <input
                        type="radio"
                        v-model="sortBy"
                        value="titulo"
                        @change="fetchProducciones"
                    >
                    <span>Título</span>
                  </label>
                  <button
                      class="sort-direction"
                      @click="sortDirection = sortDirection === 'asc' ? 'desc' : 'asc'; fetchProducciones()"
                      :title="sortDirection === 'asc' ? 'Ascendente' : 'Descendente'"
                  >
                    <i class="fas" :class="sortDirection === 'asc' ? 'fa-sort-alpha-down' : 'fa-sort-alpha-up'"></i>
                  </button>
                </div>
                <div class="sort-option">
                  <label>
                    <input
                        type="radio"
                        v-model="sortBy"
                        value="estreno"
                        @change="fetchProducciones"
                    >
                    <span>Fecha de estreno</span>
                  </label>
                  <button
                      class="sort-direction"
                      @click="sortDirection = sortDirection === 'asc' ? 'desc' : 'asc'; fetchProducciones()"
                      :title="sortDirection === 'asc' ? 'Ascendente' : 'Descendente'"
                  >
                    <i class="fas" :class="sortDirection === 'asc' ? 'fa-sort-numeric-down' : 'fa-sort-numeric-up'"></i>
                  </button>
                </div>
                <div class="sort-option">
                  <label>
                    <input
                        type="radio"
                        v-model="sortBy"
                        value="duracion"
                        @change="fetchProducciones"
                    >
                    <span>Duración</span>
                  </label>
                  <button
                      class="sort-direction"
                      @click="sortDirection = sortDirection === 'asc' ? 'desc' : 'asc'; fetchProducciones()"
                      :title="sortDirection === 'asc' ? 'Ascendente' : 'Descendente'"
                  >
                    <i class="fas" :class="sortDirection === 'asc' ? 'fa-sort-amount-down' : 'fa-sort-amount-up'"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- Filtro por tipo -->
          <div class="filter-section">
            <div class="section-header" @click="toggleSection('type')">
              <i class="fas fa-film"></i>
              <span>Tipo de Producción</span>
              <i class="section-icon fas" :class="expandedSection === 'type' ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
            </div>
            <div class="section-content" v-show="expandedSection === 'type'">
              <div class="type-options">
                <button
                    v-for="type in tiposProduccion"
                    :key="type"
                    :class="{ 'active': filters.tipo === type }"
                    @click="filters.tipo = filters.tipo === type ? null : type"
                >
                  {{ formatTipoProduccion(type) }}
                </button>
              </div>
            </div>
          </div>

          <!-- Filtro por categoría -->
          <div class="filter-section">
            <div class="section-header" @click="toggleSection('category')">
              <i class="fas fa-tags"></i>
              <span>Categorías</span>
              <i class="section-icon fas" :class="expandedSection === 'category' ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
            </div>
            <div class="section-content" v-show="expandedSection === 'category'">
              <div class="category-tags">
                <button
                    v-for="category in categoriasDisponibles"
                    :key="category"
                    :class="{ 'active': filters.categorias && filters.categorias.includes(category) }"
                    @click="toggleCategory(category)"
                >
                  {{ formatCategoria(category) }}
                </button>
              </div>
            </div>
          </div>

          <!-- Filtro por año -->
          <div class="filter-section">
            <div class="section-header" @click="toggleSection('year')">
              <i class="fas fa-calendar-alt"></i>
              <span>Año de Estreno</span>
              <i class="section-icon fas" :class="expandedSection === 'year' ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
            </div>
            <div class="section-content" v-show="expandedSection === 'year'">
              <div class="year-range-slider">
                <div class="slider-container">
                  <input
                      type="range"
                      v-model.number="filters.estrenoDesde"
                      :min="minYear"
                      :max="maxYear"
                      @input="updateYearRange"
                      class="slider"
                  >
                  <input
                      type="range"
                      v-model.number="filters.estrenoHasta"
                      :min="minYear"
                      :max="maxYear"
                      @input="updateYearRange"
                      class="slider"
                  >
                </div>
                <div class="slider-values">
                  <span>{{ filters.estrenoDesde || minYear }}</span>
                  <span>{{ filters.estrenoHasta || maxYear }}</span>
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
              <div class="rating-options">
                <button
                    v-for="rating in clasificacionesEdad"
                    :key="rating"
                    :class="{ 'active': filters.clasificacionEdad === rating }"
                    @click="filters.clasificacionEdad = filters.clasificacionEdad === rating ? null : rating"
                >
                  {{ formatClasificacionEdad(rating) }}
                </button>
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
              <div class="duration-range">
                <div class="input-group">
                  <label>Mínimo:</label>
                  <input
                      type="number"
                      v-model.number="filters.duracionMin"
                      placeholder="Mínimo"
                      min="0"
                  >
                </div>
                <div class="input-group">
                  <label>Máximo:</label>
                  <input
                      type="number"
                      v-model.number="filters.duracionMax"
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
  </div>

  <Footer :dark-mode="darkMode" />
</template>

<script>
import MovieCard from '../../../components/cards/MovieCard.vue';
import Header from "../../../components/principal/Header.vue";
import Footer from "../../../components/principal/Footer.vue";
import ProduccionesService from '../../../services/producciones.service.js';

export default {
  name: 'MoviesCatalog',
  components: {
    Footer,
    Header,
    MovieCard
  },
  data() {
    return {
      minYear: 1950,
      maxYear: new Date().getFullYear(),
      darkMode: false,
      showFiltersModal: false,
      expandedSection: null,
      producciones: [],
      isLoading: false,
      currentPage: 1,
      itemsPerPage: 12,
      totalItems: 0,
      totalPages: 1,
      sortBy: 'titulo',
      sortDirection: 'asc',
      filters: {
        titulo: null,
        tipo: null,
        estrenoDesde: 1950,
        estrenoHasta: new Date().getFullYear(),
        categorias: null,
        clasificacionEdad: null,
        duracionMin: null,
        duracionMax: null,
      },
      tiposProduccion: [],
      categoriasDisponibles: [],
      clasificacionesEdad: []
    };
  },
  methods: {
    async fetchProducciones() {
      this.isLoading = true;

      // Preparar filtros (eliminar valores null o vacíos)
      const cleanFilters = Object.fromEntries(
          Object.entries(this.filters).filter(([_, v]) => v !== null && v !== '' && !(Array.isArray(v) && v.length === 0))
      );

      // Si estrenoDesde/estrenoHasta no están definidos, usar los valores por defecto
      if (cleanFilters.estrenoDesde === undefined) {
        cleanFilters.estrenoDesde = this.minYear;
      }
      if (cleanFilters.estrenoHasta === undefined) {
        cleanFilters.estrenoHasta = this.maxYear;
      }

      // Manejar array de categorías
      if (cleanFilters.categorias && cleanFilters.categorias.length === 0) {
        delete cleanFilters.categorias;
      }

      try {
        const { data, totalItems, totalPages } = await ProduccionesService.fetchProducciones(
            cleanFilters,
            {
              page: this.currentPage - 1,
              size: this.itemsPerPage,
              sortBy: this.sortBy,
              sortDirection: this.sortDirection
            }
        );

        this.producciones = data;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
      } catch (error) {
        console.error("Error fetching producciones:", error);
        this.producciones = [];
        this.totalItems = 0;
        this.totalPages = 1;
      } finally {
        this.isLoading = false;
      }
    },

    async fetchFilterOptions() {
      try {
        const { tiposProduccion, categoriasDisponibles, clasificacionesEdad } =
            await ProduccionesService.fetchFilterOptions();

        this.tiposProduccion = tiposProduccion;
        this.categoriasDisponibles = categoriasDisponibles;
        this.clasificacionesEdad = clasificacionesEdad;
      } catch (error) {
        console.error("Error fetching filter options:", error);
      }
    },

    toggleDarkMode() {
      this.darkMode = !this.darkMode;
      localStorage.setItem('darkMode', this.darkMode);
    },

    toggleSection(section) {
      this.expandedSection = this.expandedSection === section ? null : section;
    },

    toggleCategory(category) {
      if (!this.filters.categorias) {
        this.filters.categorias = [];
      }

      const index = this.filters.categorias.indexOf(category);
      if (index === -1) {
        this.filters.categorias.push(category);
      } else {
        this.filters.categorias.splice(index, 1);
      }

      if (this.filters.categorias.length === 0) {
        this.filters.categorias = null;
      }
    },

    applyFilters() {
      this.showFiltersModal = false;
      this.currentPage = 1;
      this.fetchProducciones();
    },

    resetFilters() {
      this.filters = {
        titulo: null,
        tipo: null,
        estrenoDesde: this.minYear,
        estrenoHasta: this.maxYear,
        categorias: null,
        clasificacionEdad: null,
        duracionMin: null,
        duracionMax: null,
      };
      this.sortBy = 'titulo';
      this.sortDirection = 'asc';
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
    },

    updateYearRange() {
      // Asegurar que estrenoDesde no sea mayor que estrenoHasta
      if (this.filters.estrenoDesde > this.filters.estrenoHasta) {
        this.filters.estrenoHasta = this.filters.estrenoDesde;
      }
    },

    // Métodos de formato que usan el servicio
    formatTipoProduccion(tipo) {
      return ProduccionesService.formatTipoProduccion(tipo);
    },

    formatCategoria(categoria) {
      return ProduccionesService.formatCategoria(categoria);
    },

    formatClasificacionEdad(clasificacion) {
      return ProduccionesService.formatClasificacionEdad(clasificacion);
    },

    sortProducciones(field) {
      if (this.sortBy === field) {
        this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
      } else {
        this.sortBy = field;
        this.sortDirection = 'asc';
      }
      this.fetchProducciones();
    }
  },
  created() {
    const savedMode = localStorage.getItem('darkMode');
    if (savedMode) {
      this.darkMode = savedMode === 'true';
    }
    this.fetchFilterOptions();
    this.fetchProducciones();
  },
};
</script>

<style scoped>
.movies-container {
  font-family: 'Poppins', sans-serif;
  min-height: 100vh;
  position: relative;
  background: linear-gradient(-45deg,
  #4b0082,
  #ff6b00,
  #6a0dad,
  #e85d04,
  #8a2be2,
  #ff8800,
  #9d4edd,
  #f48c06);
  background-size: 500% 500%;
  animation: gradientBackground 50s ease infinite;
  transition: background 0.3s ease;
}

/* Estilos para el slider de rango mejorado */
.year-range-slider {
  padding: 15px 10px;
}

.dark-mode .filters-modal {
  background-color: rgba(30, 30, 30, 0.95);
}

.slider-container {
  position: relative;
  height: 40px;
  display: flex;
  align-items: center;
}

.slider {
  width: 100%;
  height: 4px;
  background: transparent;
  outline: none;
  position: absolute;
  margin: 0;
  pointer-events: none;
  z-index: 2;
}

.slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  appearance: none;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: #4299e1;
  cursor: pointer;
  pointer-events: auto;
}

.slider::-moz-range-thumb {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: #4299e1;
  cursor: pointer;
  pointer-events: auto;
}

.slider-values {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.slider-values span {
  font-size: 0.9rem;
  color: #333;
}

.dark-mode .slider-values span {
  color: white;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
}

.filter-section {
  margin-bottom: 15px;
  border-bottom: 1px solid #eee;
  padding-bottom: 15px;
}

.dark-mode .filter-section {
  border-bottom-color: #4a5568;
}

.section-header {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 5px 0;
}

.section-header i:first-child {
  margin-right: 10px;
}

.section-icon {
  margin-left: auto;
}

.section-content {
  padding: 10px 0;
}

.type-options, .category-tags, .rating-options {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.type-options button, .category-tags button, .rating-options button {
  padding: 6px 12px;
  border-radius: 4px;
  border: 1px solid #ddd;
  background: white;
  cursor: pointer;
}

.dark-mode .type-options button,
.dark-mode .category-tags button,
.dark-mode .rating-options button {
  background: #4a5568;
  border-color: #4a5568;
  color: white;
}

.type-options button.active,
.category-tags button.active,
.rating-options button.active {
  background: #4299e1;
  color: white;
  border-color: #4299e1;
}

.input-group {
  margin-bottom: 10px;
}

.input-group label {
  display: block;
  margin-bottom: 5px;
}

.input-group input {
  width: 100%;
  padding: 8px;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.dark-mode .input-group input {
  background: #4a5568;
  border-color: #4a5568;
  color: white;
}

.modal-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
}

.modal-actions {
  display: flex;
  gap: 10px;
}

.reset-btn, .apply-btn {
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.reset-btn {
  background: #e53e3e;
  color: white;
  border: none;
}

.apply-btn {
  background: #4299e1;
  color: white;
  border: none;
}

/* Estilos para la lista de películas */
.movies-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
  padding: 20px 0;
}

.loading-indicator, .no-results {
  text-align: center;
  padding: 40px 0;
}

.no-results i {
  font-size: 3rem;
  color: #718096;
  margin-bottom: 20px;
}

.no-results h3 {
  margin-bottom: 10px;
}

/* Estilos para la sección de ordenación */
.sort-options {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.sort-option {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px;
  border-radius: 4px;
  background-color: rgba(0, 0, 0, 0.05);
}

.sort-option label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  flex-grow: 1;
}

.sort-option input[type="radio"] {
  margin: 0;
}

.sort-direction {
  background: none;
  border: none;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  color: inherit;
}

.sort-direction:hover {
  background-color: rgba(0, 0, 0, 0.1);
}

.dark-mode .sort-option {
  background-color: rgba(255, 255, 255, 0.05);
}

.dark-mode .sort-direction:hover {
  background-color: rgba(255, 255, 255, 0.1);
}
</style>

<style scoped src="../../../assets/styles/general.css"></style>