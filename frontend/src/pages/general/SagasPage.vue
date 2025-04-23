<template>
  <Header :dark-mode="darkMode" @toggle-dark-mode="toggleDarkMode"/>

  <div class="sagas-container" :class="{ 'dark-mode': darkMode }">
    <!-- Encabezado con título y modo oscuro -->
    <div class="header-section">
      <h1 class="main-title">
        <i class="fas fa-film"></i> Catálogo de Sagas Cinematográficas
      </h1>
    </div>

    <!-- Panel de Filtros -->
    <div class="filters-panel">
      <!-- Sección de Búsqueda -->
      <section class="search-section">
        <div class="search-box">
          <i class="fas fa-search"></i>
          <input
              type="text"
              v-model="searchQuery"
              placeholder="Buscar sagas..."
              @input="searchSagas"
              debounce="500"
          />
          <button class="clear-btn" @click="clearSearch" v-if="searchQuery">
            <i class="fas fa-times"></i>
          </button>
        </div>
      </section>

      <!-- Filtros principales -->
      <section class="filters-grid">
        <!-- Estado -->
        <div class="filter-group">
          <label class="filter-label">
            <i class="fas fa-flag"></i> Estado
          </label>
          <v-select
              v-model="selectedStatus"
              :options="statusOptions"
              placeholder="Todos los estados"
              class="styled-select"
              label="name"
              :reduce="status => status.value"
              @input="fetchSagas"
          />
        </div>

        <!-- Ordenación -->
        <div class="filter-group">
          <label class="filter-label">
            <i class="fas fa-sort"></i> Ordenar por
          </label>
          <v-select
              v-model="selectedSort"
              :options="sortOptions"
              placeholder="Ordenar por..."
              class="styled-select"
              label="name"
              :reduce="sort => sort.value"
              @input="fetchSagas"
          />
        </div>
      </section>

      <!-- Acciones -->
      <section class="filter-actions">
        <button class="reset-btn" @click="resetFilters">
          <i class="fas fa-undo"></i> Reiniciar Filtros
        </button>
        <span class="results-count">
          Mostrando {{ sagas.content.length }} de {{ sagas.totalElements }} sagas
        </span>
      </section>
    </div>

    <!-- Listado de Sagas -->
    <div class="sagas-list">
      <div v-if="loading" class="loading-spinner">
        <i class="fas fa-spinner fa-spin"></i> Cargando sagas...
      </div>

      <div v-else-if="sagas.content.length === 0" class="no-results">
        <i class="fas fa-film"></i>
        <h3>No se encontraron sagas</h3>
        <p>Intenta ajustar tus filtros de búsqueda</p>
        <button @click="resetFilters" class="reset-btn">
          <i class="fas fa-undo"></i> Reiniciar filtros
        </button>
      </div>

      <transition-group v-else name="fade-staggered" tag="div" class="sagas-grid">
        <saga-card
            v-for="saga in sagas.content"
            :key="saga.id"
            :saga="saga"
            :dark-mode="darkMode"
        />
      </transition-group>

      <!-- Paginación -->
      <div class="pagination-controls" v-if="sagas.totalElements > itemsPerPage">
        <button
            class="pagination-btn"
            :disabled="currentPage === 0 || loading"
            @click="prevPage"
        >
          <i class="fas fa-chevron-left"></i>
        </button>

        <span class="page-indicator">
          Página {{ currentPage + 1 }} de {{ sagas.totalPages }}
        </span>

        <button
            class="pagination-btn"
            :disabled="currentPage === sagas.totalPages - 1 || loading"
            @click="nextPage"
        >
          <i class="fas fa-chevron-right"></i>
        </button>
      </div>
    </div>
  </div>

  <Footer :dark-mode="darkMode" />
</template>

<script>
import vSelect from 'vue-select'
import 'vue-select/dist/vue-select.css'
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";
import SagaCard from "@/components/cards/SagaCard.vue";
import axios from 'axios';

export default {
  name: 'SagasCatalog',
  components: {
    Footer,
    Header,
    vSelect,
    SagaCard
  },
  data() {
    return {
      darkMode: false,
      loading: false,
      sagas: {
        content: [],
        totalElements: 0,
        totalPages: 0
      },
      searchQuery: '',
      selectedStatus: null,
      selectedSort: 'nombre,asc',
      currentPage: 0,
      itemsPerPage: 12,
      statusOptions: [
        { name: 'Todas las sagas', value: null },
        { name: 'Sagas completadas', value: true },
        { name: 'Sagas en curso', value: false }
      ],
      sortOptions: [
        { name: 'Nombre (A-Z)', value: 'nombre,asc' },
        { name: 'Nombre (Z-A)', value: 'nombre,desc' },
        { name: 'Más recientes', value: 'fechaCreacion,desc' },
        { name: 'Más antiguas', value: 'fechaCreacion,asc' }
      ]
    }
  },
  computed: {
    apiUrl() {
      return 'http://localhost:8080/api/sagas';
    }
  },
  methods: {
    toggleDarkMode() {
      this.darkMode = !this.darkMode;
      localStorage.setItem('darkMode', this.darkMode);
    },

    async fetchSagas() {
      this.loading = true;
      try {
        let url = this.apiUrl;
        const params = {
          page: this.currentPage,
          size: this.itemsPerPage,
          sort: this.selectedSort
        };

        // Si hay búsqueda, usar endpoint de búsqueda
        if (this.searchQuery) {
          url += '/search';
          params.nombre = this.searchQuery;
        }
        // Si hay filtro por estado
        else if (this.selectedStatus !== null) {
          url += '/filter/estado';
          params.isAcabada = this.selectedStatus;
        }

        const response = await axios.get(url, { params });
        this.sagas = {
          content: response.data.sagas.content,
          totalElements: response.data.total,
          totalPages: response.data.sagas.totalPages
        };
      } catch (error) {
        console.error('Error fetching sagas:', error);
        this.$toast.error('Error al cargar las sagas');
      } finally {
        this.loading = false;
      }
    },

    searchSagas() {
      this.currentPage = 0;
      this.fetchSagas();
    },

    resetFilters() {
      this.searchQuery = '';
      this.selectedStatus = null;
      this.selectedSort = 'nombre,asc';
      this.currentPage = 0;
      this.fetchSagas();
    },

    clearSearch() {
      this.searchQuery = '';
      this.fetchSagas();
    },

    prevPage() {
      if (this.currentPage > 0) {
        this.currentPage--;
        this.fetchSagas();
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    },

    nextPage() {
      if (this.currentPage < this.sagas.totalPages - 1) {
        this.currentPage++;
        this.fetchSagas();
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    }
  },
  created() {
    const savedMode = localStorage.getItem('darkMode');
    if (savedMode) {
      this.darkMode = savedMode === 'true';
    }
    this.fetchSagas();
  }
}
</script>

<style scoped>
:root {
  --color-primary: #6c5ce7;
  --color-primary-light: #a29bfe;
  --color-primary-dark: #5649c0;
  --color-secondary: #f8f9fa;
  --color-text: #2d3436;
  --color-text-light: #f8f9fa;
  --color-bg: #ffffff;
  --color-bg-dark: #121212;
  --color-card: #ffffff;
  --color-card-dark: #1e1e1e;
  --color-filter-panel: #f1f3f5;
  --color-filter-panel-dark: #2a2a2a;
  --color-border: #e0e0e0;
  --color-border-dark: #333333;
  --transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.dark-mode {
  --color-secondary: #2a2a3a;
  --color-text: #e9ecef;
  --color-bg: #121212;
  --color-card: #1e1e1e;
  --color-filter-panel: #2a2a2a;
  --color-border: #333333;
}

.sagas-container {
  position: relative;
  padding: 2rem 5%;
  color: var(--color-text);
  transition: var(--transition);
  min-height: 100vh;
  background-image: url("https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
  background-attachment: fixed;
  background-size: cover;
  background-position: center;
  overflow: hidden;
  box-shadow: inset 0 0 50px rgba(0, 0, 0, 0.5);
}

.sagas-container::before {
  content: "";
  position: absolute;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.55);
  z-index: 0;
}

.sagas-container > * {
  position: relative;
  z-index: 1;
}

/* Encabezado */
.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.main-title {
  font-size: 2.2rem;
  font-weight: 700;
  color: #ffffff;
  display: flex;
  align-items: center;
  gap: 0.8rem;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

/* Panel de Filtros */
.filters-panel {
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  border: 1px solid var(--color-border);
  transition: var(--transition);
}

.dark-mode .filters-panel {
  background-color: rgba(30, 30, 30, 0.9);
  border-color: var(--color-border-dark);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

/* Cuadro de Búsqueda */
.search-section {
  margin-bottom: 1.5rem;
  position: relative;
}

.search-box {
  position: relative;
  max-width: 600px;
  margin: 0 auto;
  border-radius: 50px;
  overflow: hidden;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.dark-mode .search-box {
  background: rgba(0, 0, 0, 0.2);
  border-color: rgba(255, 255, 255, 0.05);
}

.search-box i {
  position: absolute;
  left: 1.5rem;
  top: 50%;
  transform: translateY(-50%);
  color: var(--color-primary);
  font-size: 1.1rem;
  z-index: 2;
}

.search-box input {
  width: 100%;
  padding: 0.9rem 1.5rem 0.9rem 3.5rem;
  border: none;
  background: transparent;
  color: var(--color-text);
  font-size: 1rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.dark-mode .search-box input {
  color: white;
}

.search-box input::placeholder {
  color: rgba(255, 255, 255, 0.7);
  font-weight: 400;
}

.search-box input:focus {
  outline: none;
  background: rgba(255, 255, 255, 0.15);
}

.dark-mode .search-box input:focus {
  background: rgba(0, 0, 0, 0.3);
}

.clear-btn {
  position: absolute;
  right: 1.5rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.7);
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 2;
}

.clear-btn:hover {
  color: var(--color-primary);
}

/* Filtros Grid */
.filters-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 1.5rem;
}

.filter-group {
  margin-bottom: 1rem;
}

.filter-label {
  display: block;
  margin-bottom: 0.8rem;
  font-weight: 600;
  color: var(--color-text);
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.95rem;
}

/* Select Estilizado */
.styled-select {
  background-color: var(--color-card);
  border-radius: 8px;
  border: 1px solid var(--color-border);
  transition: var(--transition);
}

.dark-mode .styled-select {
  background-color: #2a2a2a;
  border-color: #444;
}

.styled-select >>> .vs__dropdown-toggle {
  border: none;
  padding: 0.7rem 1rem;
  background: transparent;
}

.styled-select >>> .vs__selected {
  background-color: var(--color-primary);
  color: white;
  border: none;
  border-radius: 4px;
  padding: 0.3rem 0.6rem;
}

.styled-select >>> .vs__search {
  color: var(--color-text);
  padding: 0.3rem 0.6rem;
}

.styled-select >>> .vs__dropdown-menu {
  background-color: var(--color-card);
  border: 1px solid var(--color-border);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.dark-mode .styled-select >>> .vs__dropdown-menu {
  background-color: #2a2a2a;
  border-color: #444;
}

/* Acciones de Filtro */
.filter-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 1.5rem;
  padding-top: 1.5rem;
  border-top: 1px solid var(--color-border);
}

.reset-btn {
  background: none;
  border: none;
  color: var(--color-primary);
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: var(--transition);
  padding: 0.5rem 1rem;
  border-radius: 6px;
}

.reset-btn:hover {
  background-color: rgba(108, 92, 231, 0.1);
}

.results-count {
  font-size: 0.9rem;
  color: var(--color-text);
  opacity: 0.8;
}

/* Listado de Sagas */
.sagas-list {
  margin-top: 2rem;
}

.sagas-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.loading-spinner {
  text-align: center;
  padding: 2rem;
  color: var(--color-primary);
  font-size: 1.2rem;
}

.loading-spinner i {
  margin-right: 0.5rem;
}

.no-results {
  text-align: center;
  padding: 3rem;
  background: var(--color-bg);
  border-radius: 12px;
  border: 1px solid var(--color-border);
}

.no-results i {
  font-size: 3rem;
  color: var(--color-primary);
  margin-bottom: 1rem;
}

.no-results h3 {
  font-size: 1.5rem;
  margin-bottom: 0.5rem;
  color: var(--color-text);
}

.no-results p {
  color: rgba(var(--color-text), 0.7);
  margin-bottom: 1.5rem;
}

/* Paginación */
.pagination-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1.5rem;
  margin-top: 2rem;
}

.pagination-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: var(--color-primary);
  color: white;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: var(--transition);
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination-btn:hover:not(:disabled) {
  background-color: var(--color-primary-dark);
  transform: scale(1.1);
}

.page-indicator {
  font-weight: 600;
  color: white;
}

/* Transiciones */
.fade-staggered-move,
.fade-staggered-enter-active,
.fade-staggered-leave-active {
  transition: all 0.5s ease;
}
.fade-staggered-enter-from,
.fade-staggered-leave-to {
  opacity: 0;
  transform: translateY(20px);
}
.fade-staggered-leave-active {
  position: absolute;
}

/* Responsive */
@media (max-width: 1024px) {
  .sagas-container {
    padding: 2rem;
  }
}

@media (max-width: 768px) {
  .header-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }

  .filters-grid {
    grid-template-columns: 1fr;
  }

  .filter-actions {
    flex-direction: column;
    gap: 1rem;
    align-items: flex-start;
  }
}

@media (max-width: 480px) {
  .sagas-container {
    padding: 1.5rem;
  }

  .main-title {
    font-size: 1.8rem;
  }

  .sagas-grid {
    grid-template-columns: 1fr;
  }
}
</style>