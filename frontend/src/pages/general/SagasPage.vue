<template>
  <Header :dark-mode="darkMode" @toggle-dark-mode="toggleDarkMode"/>

  <div class="sagas-container" :class="{ 'dark-mode': darkMode }">
    <!-- Encabezado con título y modo oscuro -->
    <div class="header-section">
      <h1 class="main-title">
        <i class="fas fa-book"></i> Catálogo de Sagas
      </h1>
    </div>

    <!-- Barra de búsqueda -->
    <div class="search-bar">
      <div class="search-box">
        <i class="fas fa-search"></i>
        <input
            type="text"
            v-model="filters.nombre"
            placeholder="Buscar por nombre..."
            @keyup.enter="fetchSagas"
        />
        <button class="clear-btn" @click="clearSearch" v-if="filters.nombre">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <button class="search-btn" @click="fetchSagas">
        <i class="fas fa-search"></i> Buscar
      </button>
      <button class="filter-btn" @click="showFiltersModal = true">
        <i class="fas fa-ellipsis-v"></i>
      </button>
    </div>

    <!-- Listado de Sagas -->
    <div class="sagas-list">
      <div v-if="isLoading" class="loading-indicator">
        <i class="fas fa-spinner fa-spin"></i> Cargando sagas...
      </div>

      <div v-else-if="sagas.length === 0" class="no-results">
        <i class="fas fa-book"></i>
        <h3>No se encontraron sagas</h3>
        <p>Intenta ajustar tus filtros de búsqueda</p>
        <button @click="resetFilters" class="reset-btn">
          <i class="fas fa-undo"></i> Reiniciar filtros
        </button>
      </div>

      <transition-group v-else name="fade-staggered" tag="div" class="sagas-grid">
        <saga-card
            v-for="saga in sagas"
            :key="saga.id"
            :saga="saga"
            :dark-mode="darkMode"
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
          <!-- Filtro por estado -->
          <div class="filter-section">
            <div class="section-header" @click="toggleSection('status')">
              <i class="fas fa-flag"></i>
              <span>Estado de la Saga</span>
              <i class="section-icon fas" :class="expandedSection === 'status' ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
            </div>
            <div class="section-content" v-show="expandedSection === 'status'">
              <div class="status-options">
                <button
                    :class="{ 'active': filters.isAcabada === true }"
                    @click="filters.isAcabada = filters.isAcabada === true ? null : true"
                >
                  Finalizada
                </button>
                <button
                    :class="{ 'active': filters.isAcabada === false }"
                    @click="filters.isAcabada = filters.isAcabada === false ? null : false"
                >
                  En curso
                </button>
              </div>
            </div>
          </div>

          <!-- Filtro por fecha de inicio -->
          <div class="filter-section">
            <div class="section-header" @click="toggleSection('date')">
              <i class="fas fa-calendar-alt"></i>
              <span>Fecha de Inicio</span>
              <i class="section-icon fas" :class="expandedSection === 'date' ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
            </div>
            <div class="section-content" v-show="expandedSection === 'date'">
              <div class="date-range">
                <div class="input-group">
                  <label>Desde:</label>
                  <input
                      type="date"
                      v-model="filters.fechaInicioDesde"
                  >
                </div>
                <div class="input-group">
                  <label>Hasta:</label>
                  <input
                      type="date"
                      v-model="filters.fechaInicioHasta"
                  >
                </div>
              </div>
            </div>
          </div>

          <!-- Filtro por imagen -->
          <div class="filter-section">
            <div class="section-header" @click="toggleSection('image')">
              <i class="fas fa-image"></i>
              <span>Imagen</span>
              <i class="section-icon fas" :class="expandedSection === 'image' ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
            </div>
            <div class="section-content" v-show="expandedSection === 'image'">
              <div class="image-options">
                <button
                    :class="{ 'active': filters.tieneImagen === true }"
                    @click="filters.tieneImagen = filters.tieneImagen === true ? null : true"
                >
                  Con imagen
                </button>
                <button
                    :class="{ 'active': filters.tieneImagen === false }"
                    @click="filters.tieneImagen = filters.tieneImagen === false ? null : false"
                >
                  Sin imagen
                </button>
              </div>
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <div class="results-count">
            {{ totalItems }} sagas encontradas
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
import SagaCard from '@/components/cards/SagaCard.vue';
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import axios from 'axios';

export default {
  name: 'SagasCatalog',
  components: {
    Footer,
    Header,
    SagaCard
  },
  data() {
    return {
      darkMode: false,
      showFiltersModal: false,
      expandedSection: null,
      sagas: [],
      isLoading: false,
      currentPage: 1,
      itemsPerPage: 12,
      totalItems: 0,
      totalPages: 1,
      filters: {
        nombre: null,
        isAcabada: null,
        fechaInicioDesde: null,
        fechaInicioHasta: null,
        tieneImagen: null
      }
    };
  },
  methods: {
    async fetchSagas() {
      this.isLoading = true;
      try {
        // Construir parámetros de consulta
        const params = {
          page: this.currentPage - 1, // Spring usa 0-based
          size: this.itemsPerPage,
          sortBy: 'nombre',
          sortDirection: 'asc'
        };

        // Añadir filtros no nulos
        Object.keys(this.filters).forEach(key => {
          if (this.filters[key] !== null && this.filters[key] !== '') {
            params[key] = this.filters[key];
          }
        });

        // Convertir fechas a formato string si existen
        if (this.filters.fechaInicioDesde instanceof Date) {
          params.fechaInicioDesde = this.formatDate(this.filters.fechaInicioDesde);
        }
        if (this.filters.fechaInicioHasta instanceof Date) {
          params.fechaInicioHasta = this.formatDate(this.filters.fechaInicioHasta);
        }

        const response = await axios.get('/api/sagas/filter', {
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

        this.sagas = response.data?.data?.map(item => ({
          id: item.id,
          nombre: item.nombre,
          descripcion: item.descripcion,
          isAcabada: item.isAcabada,
          fechaInicio: item.fechaInicio,
          fechaFin: item.fechaFin,
          imagen: item.imagen || 'default-saga.jpg',
          producciones: item.producciones || []
        })) || [];

        this.totalItems = response.data?.totalItems || 0;
        this.totalPages = response.data?.totalPages || 1;

      } catch (error) {
        console.error('Error fetching sagas:', error);
        this.sagas = [];
        this.totalItems = 0;
        this.totalPages = 1;
      } finally {
        this.isLoading = false;
      }
    },

    formatDate(date) {
      if (!date) return null;
      const d = new Date(date);
      let month = '' + (d.getMonth() + 1);
      let day = '' + d.getDate();
      const year = d.getFullYear();

      if (month.length < 2) month = '0' + month;
      if (day.length < 2) day = '0' + day;

      return [year, month, day].join('-');
    },

    toggleDarkMode() {
      this.darkMode = !this.darkMode;
      localStorage.setItem('darkMode', this.darkMode);
    },

    toggleSection(section) {
      this.expandedSection = this.expandedSection === section ? null : section;
    },

    applyFilters() {
      this.showFiltersModal = false;
      this.currentPage = 1;
      this.fetchSagas();
    },

    resetFilters() {
      this.filters = {
        nombre: null,
        isAcabada: null,
        fechaInicioDesde: null,
        fechaInicioHasta: null,
        tieneImagen: null
      };
      this.currentPage = 1;
      this.fetchSagas();
    },

    clearSearch() {
      this.filters.nombre = '';
      this.fetchSagas();
    },

    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchSagas();
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    },

    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchSagas();
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    },

    formatStatus(isAcabada) {
      return isAcabada ? 'Finalizada' : 'En curso';
    }
  },
  created() {
    const savedMode = localStorage.getItem('darkMode');
    if (savedMode) {
      this.darkMode = savedMode === 'true';
    }
    this.fetchSagas();
  },
};
</script>

<style scoped>
.sagas-list {
  margin-top: 2rem;
}

.sagas-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.status-options, .image-options {
  display: flex;
  gap: 0.8rem;
  flex-wrap: wrap;
}

.status-options button, .image-options button {
  padding: 0.5rem 1rem;
  border-radius: 6px;
  border: 1px solid var(--border-color);
  background: var(--bg-color);
  color: var(--text-color);
  cursor: pointer;
  transition: var(--transition);
}

.status-options button.active, .image-options button.active {
  background: var(--primary-color);
  color: white;
  border-color: var(--primary-color);
}

.date-range {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.date-range .input-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.date-range input {
  padding: 0.7rem;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  background: var(--bg-color);
  color: var(--text-color);
}
</style>

<style scoped src="@/assets/styles/general.css"></style>