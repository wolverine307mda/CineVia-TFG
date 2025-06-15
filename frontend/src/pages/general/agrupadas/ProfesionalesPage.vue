<template>
  <Header :dark-mode="darkMode" @toggle-dark-mode="toggleDarkMode"/>

  <div class="professionals-container" :class="{ 'dark-mode': darkMode }">
    <!-- Encabezado con título y modo oscuro -->
    <div class="header-section">
      <h1 class="main-title">
        <i class="fas fa-users"></i> Directorio de Profesionales
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
            @keyup.enter="fetchProfesionales"
        />
        <button class="clear-btn" @click="clearSearch" v-if="filters.nombre">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <button class="search-btn" @click="fetchProfesionales">
        <i class="fas fa-search"></i> Buscar
      </button>
      <button class="filter-btn" @click="showFiltersModal = true">
        <i class="fas fa-ellipsis-v"></i>
      </button>
    </div>

    <!-- Listado de Profesionales -->
    <div class="professionals-list">
      <div v-if="isLoading" class="loading-indicator">
        <i class="fas fa-spinner fa-spin"></i> Cargando profesionales...
      </div>

      <div v-else-if="profesionales.length === 0" class="no-results">
        <i class="fas fa-user"></i>
        <h3>No se encontraron profesionales</h3>
        <p>Intenta ajustar tus filtros de búsqueda</p>
        <button @click="resetFilters" class="reset-btn">
          <i class="fas fa-undo"></i> Reiniciar filtros
        </button>
      </div>

      <transition-group v-else name="fade-staggered" tag="div" class="professionals-grid">
        <professional-card
            v-for="profesional in profesionales"
            :key="profesional.id"
            :professional="profesional"
            :dark-mode="darkMode"
            @toggle-favorite="toggleFavorite"
            @view-details="openProfessionalModal"
        />
      </transition-group>

      <!-- Modal de detalles del profesional -->
      <ProfessionalModal
          v-if="showProfessionalModal"
          :professional-id="selectedProfessionalId"
          :dark-mode="darkMode"
          @close="closeProfessionalModal"
      />

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
          <!-- Filtro por fecha de nacimiento -->
          <div class="filter-section">
            <div class="section-header" @click="toggleSection('birthdate')">
              <i class="fas fa-birthday-cake"></i>
              <span>Fecha de Nacimiento</span>
              <i class="section-icon fas" :class="expandedSection === 'birthdate' ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
            </div>
            <div class="section-content" v-show="expandedSection === 'birthdate'">
              <div class="date-range">
                <div class="date-inputs">
                  <div class="input-group">
                    <label>Desde:</label>
                    <input
                        type="date"
                        v-model="filters.fechaNacimientoDesde"
                        :max="new Date().toISOString().split('T')[0]"
                    >
                  </div>
                  <div class="input-group">
                    <label>Hasta:</label>
                    <input
                        type="date"
                        v-model="filters.fechaNacimientoHasta"
                        :max="new Date().toISOString().split('T')[0]"
                    >
                  </div>
                </div>
                <div class="quick-years">
                  <button @click="setBirthdateRange(20)">Últimos 20 años</button>
                  <button @click="setBirthdateRange(30)">Últimos 30 años</button>
                  <button @click="setBirthdateRange(40)">Últimos 40 años</button>
                  <button @click="setBirthdateRange(50)">Últimos 50 años</button>
                </div>
              </div>
            </div>
          </div>

          <!-- Filtro por fecha de inicio -->
          <div class="filter-section">
            <div class="section-header" @click="toggleSection('startdate')">
              <i class="fas fa-calendar-alt"></i>
              <span>Fecha de Inicio</span>
              <i class="section-icon fas" :class="expandedSection === 'startdate' ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
            </div>
            <div class="section-content" v-show="expandedSection === 'startdate'">
              <div class="date-range">
                <div class="date-inputs">
                  <div class="input-group">
                    <label>Desde:</label>
                    <input
                        type="date"
                        v-model="filters.fechaInicioDesde"
                        :max="new Date().toISOString().split('T')[0]"
                    >
                  </div>
                  <div class="input-group">
                    <label>Hasta:</label>
                    <input
                        type="date"
                        v-model="filters.fechaInicioHasta"
                        :max="new Date().toISOString().split('T')[0]"
                    >
                  </div>
                </div>
                <div class="quick-years">
                  <button @click="setStartdateRange(10)">Últimos 10 años</button>
                  <button @click="setStartdateRange(20)">Últimos 20 años</button>
                  <button @click="setStartdateRange(30)">Últimos 30 años</button>
                  <button @click="setStartdateRange(40)">Últimos 40 años</button>
                </div>
              </div>
            </div>
          </div>

          <!-- Ordenación -->
          <div class="filter-section">
            <div class="section-header" @click="toggleSection('sort')">
              <i class="fas fa-sort"></i>
              <span>Ordenar por</span>
              <i class="section-icon fas" :class="expandedSection === 'sort' ? 'fa-chevron-up' : 'fa-chevron-down'"></i>
            </div>
            <div class="section-content" v-show="expandedSection === 'sort'">
              <div class="sort-options">
                <div class="sort-option" :class="{ 'active': sortBy === 'nombre' }" @click="changeSort('nombre')">
                  <span>Nombre</span>
                  <i class="fas" :class="sortBy === 'nombre' ?
              (sortDirection === 'asc' ? 'fa-sort-alpha-down' : 'fa-sort-alpha-up') : 'fa-sort'"></i>
                </div>
                <div class="sort-option" :class="{ 'active': sortBy === 'fechaNacimiento' }" @click="changeSort('fechaNacimiento')">
                  <span>Fecha de Nacimiento</span>
                  <i class="fas" :class="sortBy === 'fechaNacimiento' ?
              (sortDirection === 'asc' ? 'fa-sort-numeric-down' : 'fa-sort-numeric-up') : 'fa-sort'"></i>
                </div>
                <div class="sort-option" :class="{ 'active': sortBy === 'fechaInicio' }" @click="changeSort('fechaInicio')">
                  <span>Fecha de Inicio</span>
                  <i class="fas" :class="sortBy === 'fechaInicio' ?
              (sortDirection === 'asc' ? 'fa-sort-numeric-down' : 'fa-sort-numeric-up') : 'fa-sort'"></i>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <div class="results-count">
            {{ totalItems }} profesionales encontrados
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
import ProfessionalCard from '@/components/cards/ProfessionalCard.vue';
import Header from "@/components/principal/Header.vue";
import Footer from "@/components/principal/Footer.vue";
import ProfessionalsService from '@/services/profesional.service.js';

export default {
  name: 'ProfessionalsCatalog',
  components: {
    Footer,
    Header,
    ProfessionalCard,
  },
  data() {
    return {
      darkMode: false,
      showFiltersModal: false,
      expandedSection: null,
      profesionales: [],
      isLoading: false,
      currentPage: 1,
      itemsPerPage: 12,
      totalItems: 0,
      totalPages: 1,
      sortBy: 'nombre',
      sortDirection: 'asc',
      filters: {
        nombre: null,
        fechaNacimientoDesde: null,
        fechaNacimientoHasta: null,
        fechaInicioDesde: null,
        fechaInicioHasta: null,
        lugarNacimiento: null
      }
    };
  },
  methods: {
    async fetchProfesionales() {
      this.isLoading = true;

      try {
        const { data, totalItems, totalPages } = await ProfessionalsService.fetchProfessionals(
            this.filters,
            {
              page: this.currentPage - 1,
              size: this.itemsPerPage
            },
            {
              sortBy: this.sortBy,
              sortDirection: this.sortDirection
            }
        );

        this.profesionales = data;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
      } catch (error) {
        console.error("Error fetching profesionales:", error);
        this.profesionales = [];
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

    setBirthdateRange(years) {
      const dateRange = ProfessionalsService.getDateRangeYearsAgo(years);
      this.filters.fechaNacimientoDesde = dateRange.desde;
      this.filters.fechaNacimientoHasta = dateRange.hasta;
    },

    setStartdateRange(years) {
      const dateRange = ProfessionalsService.getDateRangeYearsAgo(years);
      this.filters.fechaInicioDesde = dateRange.desde;
      this.filters.fechaInicioHasta = dateRange.hasta;
    },

    applyFilters() {
      this.showFiltersModal = false;
      this.currentPage = 1;
      this.fetchProfesionales();
    },

    resetFilters() {
      this.filters = {
        nombre: null,
        fechaNacimientoDesde: null,
        fechaNacimientoHasta: null,
        fechaInicioDesde: null,
        fechaInicioHasta: null,
        lugarNacimiento: null
      };
      this.sortBy = 'nombre';
      this.sortDirection = 'asc';
      this.currentPage = 1;
      this.fetchProfesionales();
    },

    clearSearch() {
      this.filters.nombre = '';
      this.fetchProfesionales();
    },

    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchProfesionales();
        window.scrollTo({top: 0, behavior: 'smooth'});
      }
    },

    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchProfesionales();
        window.scrollTo({top: 0, behavior: 'smooth'});
      }
    },

    toggleFavorite(professionalId) {
      const profesional = this.profesionales.find(p => p.id === professionalId);
      if (profesional) {
        profesional.isFavorite = !profesional.isFavorite;
      }
    },

    changeSort(field) {
      if (this.sortBy === field) {
        this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
      } else {
        this.sortBy = field;
        this.sortDirection = 'asc';
      }
      this.fetchProfesionales();
    }
  },
  created() {
    const savedMode = localStorage.getItem('darkMode');
    if (savedMode) {
      this.darkMode = savedMode === 'true';
    }
    this.fetchProfesionales();
  },
};
</script>

<style scoped src="@/assets/styles/general.css"></style>