<template>
  <div class="sagas-container">
    <div class="main-content">
      <div class="table-section">
        <!-- Barra de búsqueda con botón -->
        <div class="search-header">
          <div class="search-bar">
            <div class="search-input-container">
              <i class="fas fa-search search-icon"></i>
              <input v-model="searchQuery" @keyup.enter="fetchSagas" placeholder="Buscar por nombre..." class="search-input">
              <button @click="fetchSagas" class="search-button">
                Buscar
              </button>
            </div>
            <button @click="openModal(null)" class="new-button">
              <i class="fas fa-plus"></i>
              <span>Nueva Saga</span>
            </button>
          </div>
        </div>

        <!-- Tabla de sagas con tamaños fijos -->
        <div class="table-container">
          <table class="sagas-table">
            <thead>
            <tr>
              <th style="width: 40%;" @click="sortBy('nombre')">
                Nombre
              </th>
              <th style="width: 30%; text-align: center;" @click="sortBy('fechaInicio')">
                Fecha Inicio
              </th>
              <th style="width: 10%; text-align: center;" @click="sortBy('isAcabada')">
                Estado
              </th>
              <th style="width: 10%; text-align: center;">
                Producciones
              </th>
              <th style="width: 10%; text-align: center;">Acciones</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="saga in sagas" :key="saga.id">
              <td style="width: 40%;">{{ saga.nombre }}</td>
              <td style="width: 30%; text-align: center;">{{ formatDate(saga.fechaInicio) }}</td>
              <td style="width: 10%; text-align: center;">{{ formatStatus(saga.isAcabada) }}</td>
              <td style="width: 10%; text-align: center;">{{ saga.producciones.length }}</td>
              <td style="width: 10%; text-align: center;" class="actions">
                <button @click="openModal(saga)" class="btn-edit" title="Editar">
                  <i class="fas fa-edit"></i>
                </button>
                <button @click="confirmDelete(saga)" class="btn-delete" title="Eliminar">
                  <i class="fas fa-trash"></i>
                </button>
                <button @click="viewDetails(saga)" class="btn-view" title="Detalles">
                  <i class="fas fa-info-circle"></i>
                </button>
              </td>
            </tr>
            <tr v-if="sagas.length === 0 && !loading">
              <td colspan="5" class="no-results">
                No se encontraron sagas
              </td>
            </tr>
            <tr v-if="loading">
              <td colspan="5" class="loading-row">
                <i class="fas fa-spinner fa-spin"></i> Cargando...
              </td>
            </tr>
            </tbody>
          </table>
        </div>

        <!-- Paginación -->
        <div class="pagination-container">
          <button
              @click="prevPage"
              :disabled="currentPage === 1 || loading"
              class="pagination-btn"
          >
            <i class="fas fa-chevron-left"></i>
          </button>
          <span class="page-info">
            Página {{ currentPage }} de {{ totalPages }}
          </span>
          <button
              @click="nextPage"
              :disabled="currentPage >= totalPages || loading"
              class="pagination-btn"
          >
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>
    </div>

    <!-- Modal para crear/editar -->
    <SagaModal
        v-if="showModal"
        :saga="selectedSaga"
        :show="showModal"
        @close="closeModal"
        @save="handleSave"
    />

    <!-- Modal de confirmación para eliminar -->
    <ConfirmModal
        v-if="showConfirmModal"
        :show="showConfirmModal"
        title="Confirmar eliminación"
        :message="confirmMessage"
        @confirm="deleteSaga"
        @cancel="showConfirmModal = false"
    />
  </div>
</template>

<script>
import SagaModal from "@/components/modales/edicion/SagaModal.vue";
import ConfirmModal from "@/components/cards/ConfirmModal.vue";
import SagasService from '@/services/sagas.service';

export default {
  name: 'AdministracionSagas',
  components: {
    SagaModal,
    ConfirmModal
  },
  data() {
    return {
      sagas: [],
      currentPage: 1,
      itemsPerPage: 10,
      totalItems: 0,
      totalPages: 1,
      loading: false,
      searchQuery: '',
      sortOptions: {
        field: 'nombre',
        direction: 'asc'
      },
      showModal: false,
      showConfirmModal: false,
      selectedSaga: null,
      sagaToDelete: null,
      confirmMessage: '¿Estás seguro de que deseas eliminar esta saga?'
    };
  },
  methods: {
    async fetchSagas() {
      this.loading = true;
      try {
        const filters = {
          nombre: this.searchQuery || null
        };

        const pagination = {
          currentPage: this.currentPage,
          itemsPerPage: this.itemsPerPage
        };

        const response = await SagasService.fetchSagas(filters, pagination, this.sortOptions);

        this.sagas = response.data;
        this.totalItems = response.totalItems;
        this.totalPages = response.totalPages;
      } catch (error) {
        console.error('Error fetching sagas:', error);
        this.$toast.error('Error al cargar las sagas');
        this.sagas = [];
        this.totalItems = 0;
        this.totalPages = 1;
      } finally {
        this.loading = false;
      }
    },

    sortBy(field) {
      if (this.sortOptions.field === field) {
        this.sortOptions.direction = this.sortOptions.direction === 'asc' ? 'desc' : 'asc';
      } else {
        this.sortOptions.field = field;
        this.sortOptions.direction = 'asc';
      }
      this.currentPage = 1;
      this.fetchSagas();
    },

    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchSagas();
      }
    },

    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchSagas();
      }
    },

    openModal(saga) {
      this.selectedSaga = saga ? { ...saga } : null;
      this.showModal = true;
    },

    closeModal() {
      this.showModal = false;
      this.selectedSaga = null;
    },

    async handleSave(sagaData) {
      try {
        if (sagaData.id) {
          await SagasService.updateSaga(sagaData.id, sagaData);
          this.$toast.success('Saga actualizada correctamente');
        } else {
          await SagasService.createSaga(sagaData);
          this.$toast.success('Saga creada correctamente');
        }

        this.fetchSagas();
        this.closeModal();
      } catch (error) {
        console.error('Error al guardar la saga:', error);
        this.$toast.error('Error al guardar la saga');
      }
    },

    viewDetails(saga) {
      this.$router.push({ name: 'SagaDetalle', params: { id: saga.id } });
    },

    confirmDelete(saga) {
      this.sagaToDelete = saga;
      this.confirmMessage = `¿Estás seguro de que deseas eliminar la saga "${saga.nombre}"?`;
      this.showConfirmModal = true;
    },

    async deleteSaga() {
      try {
        await SagasService.deleteSaga(this.sagaToDelete.id);
        this.$toast.success('Saga eliminada correctamente');
        this.fetchSagas();
      } catch (error) {
        console.error('Error al eliminar la saga:', error);
        this.$toast.error('Error al eliminar la saga');
      } finally {
        this.showConfirmModal = false;
        this.sagaToDelete = null;
      }
    },

    formatDate(dateString) {
      return SagasService.formatDate(dateString);
    },

    formatStatus(isFinished) {
      return SagasService.formatStatus(isFinished);
    }
  },
  created() {
    this.fetchSagas();
  }
};
</script>

<style scoped>
.sagas-container {
  margin-left: 50px;
  margin-top: 26px;
  width: calc(100% - 100px);
  min-height: 50vh;
  transition: background-color 0.3s ease;
  color: var(--text-primary);
}

.main-content {
  display: flex;
  gap: 2rem;
  height: calc(100vh - 180px);
}

.table-section {
  width: 100%;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.search-container {
  display: flex;
  margin-bottom: 1.5rem;
}

.search-box {
  position: relative;
  flex-grow: 1;
  display: flex;
  align-items: center;
}

.search-box i.fa-search {
  position: absolute;
  left: 12px;
  color: var(--text-secondary);
}

.search-box input {
  width: 100%;
  padding: 0.75rem 1rem 0.75rem 2.5rem;
  border-radius: 8px;
  border: 1px solid var(--border-color);
  background: var(--card-bg);
  color: var(--text-primary);
  transition: all 0.3s ease;
  font-size: 1rem;
}

.search-box input:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(126, 91, 239, 0.2);
}

.btn-new-icon {
  position: absolute;
  right: 8px;
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: 6px;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-new-icon:hover {
  background: var(--primary-hover);
  transform: scale(1.05);
}

.table-container {
  background: var(--card-bg);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px var(--shadow-color);
  margin-bottom: 1.5rem;
  border: 1px solid var(--border-color);
  flex-grow: 1;
  overflow-y: auto;
}

.sagas-table {
  width: 100%;
  border-collapse: collapse;
}

.sagas-table th,
.sagas-table td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid var(--border-color);
}

.sagas-table th {
  background: var(--card-header-bg-solid);
  font-weight: 600;
  color: var(--text-primary);
  padding: 1rem;
  position: sticky;
  top: 0;
  z-index: 10;
  border-bottom: 2px solid var(--border-color);
}

.sagas-table th:hover {
  background: rgba(126, 91, 239, 0.05);
}

.sagas-table tr:last-child td {
  border-bottom: none;
}

.sagas-table tr:hover td {
  background: rgba(126, 91, 239, 0.03);
}

.actions {
  display: flex;
  gap: 0.5rem;
}

.btn-edit, .btn-delete, .btn-view {
  padding: 0.5rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
}

.btn-edit {
  background: rgba(59, 130, 246, 0.1);
  color: var(--info-color);
}

.btn-edit:hover {
  background: rgba(59, 130, 246, 0.2);
}

.btn-delete {
  background: rgba(239, 68, 68, 0.1);
  color: var(--danger-color);
}

.btn-delete:hover {
  background: rgba(239, 68, 68, 0.2);
}

.btn-view {
  background: rgba(16, 185, 129, 0.1);
  color: var(--success-color);
}

.btn-view:hover {
  background: rgba(16, 185, 129, 0.2);
}

.no-results {
  text-align: center;
  padding: 2rem;
  color: var(--text-secondary);
}

.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-bottom: 2rem;
  margin-top: auto;
}

.pagination-btn {
  padding: 0.5rem 1rem;
  border: 1px solid var(--border-color);
  background: var(--card-bg);
  color: var(--text-primary);
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination-btn:hover:not(:disabled) {
  background: var(--primary-color);
  color: white;
  border-color: var(--primary-color);
}

.page-info {
  font-size: 0.9rem;
  color: var(--text-secondary);
}

/* Dark mode variables */
:root {
  --primary-color: #7e5bef;
  --primary-hover: #6d46e8;
  --success-color: #10b981;
  --danger-color: #ef4444;
  --warning-color: #f59e0b;
  --info-color: #3b82f6;
  --card-header-bg-solid: #f1f5f9;
  --bg-color: #f8fafc;
  --card-bg: #ffffff;
  --card-header-bg: #f8fafc;
  --border-color: #e2e8f0;
  --text-primary: #1e293b;
  --text-secondary: #64748b;
  --shadow-color: rgba(0, 0, 0, 0.05);
}

.dark-mode {
  --primary-color: #8b5cf6;
  --primary-hover: #7c3aed;
  --success-color: #10b981;
  --danger-color: #ef4444;
  --warning-color: #f59e0b;
  --info-color: #3b82f6;
  --card-header-bg-solid: #1e293b;
  --bg-color: #0f172a;
  --card-bg: #1e293b;
  --card-header-bg: #1e293b;
  --border-color: #334155;
  --text-primary: #f8fafc;
  --text-secondary: #94a3b8;
  --shadow-color: rgba(0, 0, 0, 0.2);
}

/* Responsive */
@media (max-width: 1024px) {
  .main-content {
    flex-direction: column;
    height: auto;
  }

  .table-section {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .sagas-table {
    display: block;
    overflow-x: auto;
  }
}

/* Contenedor principal */
.search-header {
  margin-bottom: 1.5rem;
  width: 100%;
}

/* Barra de búsqueda contenedor */
.search-bar {
  display: flex;
  gap: 1rem;
  align-items: center;
  width: 100%;
}

/* Contenedor del input de búsqueda */
.search-input-container {
  flex: 1;
  position: relative;
  display: flex;
  align-items: center;
  background: #ffffff;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  overflow: hidden;
  height: 3rem;
  transition: all 0.3s ease;
}

/* Efecto al enfocar el input */
.search-input-container:focus-within {
  border-color: #7e5bef;
  box-shadow: 0 0 0 3px rgba(126, 91, 239, 0.15);
}

/* Icono de lupa */
.search-icon {
  position: absolute;
  left: 1rem;
  color: #64748b;
  font-size: 1rem;
}

/* Campo de entrada */
.search-input {
  flex: 1;
  padding: 0 1rem 0 2.5rem;
  border: none;
  background: transparent;
  color: #1e293b;
  font-size: 0.95rem;
  outline: none;
  height: 100%;
}

/* Botón de búsqueda */
.search-button {
  padding: 0 1.5rem;
  height: 100%;
  background: #7e5bef;
  color: white;
  border: none;
  cursor: pointer;
  font-weight: 500;
  font-size: 0.95rem;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search-button:hover {
  background: #6d46e8;
}

/* Botón "Nueva Saga" */
.new-button {
  padding: 0 1.5rem;
  height: 3rem;
  background: #10b981;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  font-size: 0.95rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.2s ease;
  white-space: nowrap;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}

.new-button:hover {
  background: #0d9f6e;
  transform: translateY(-1px);
}

.new-button:active {
  transform: translateY(0);
}

/* Modo oscuro */
.dark-mode .search-input-container {
  background: #1e293b;
  border-color: #334155;
}

.dark-mode .search-input {
  color: #f8fafc;
}

.dark-mode .search-icon {
  color: #94a3b8;
}

/* Responsive para móviles */
@media (max-width: 768px) {
  .search-bar {
    flex-direction: column;
    gap: 0.75rem;
  }

  .search-input-container,
  .new-button {
    width: 100%;
  }

  .search-button {
    padding: 0.75rem;
    border-left: none;
    border-top: 1px solid rgba(255, 255, 255, 0.2);
  }
}
</style>