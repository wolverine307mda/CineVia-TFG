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
              <td style="width: 10%; text-align: center;">
                <div class="action-buttons">
                  <button @click="openModal(saga)" class="btn-edit" title="Editar">
                    <i class="fas fa-edit"></i>
                  </button>
                  <button @click="confirmDelete(saga)" class="btn-delete" title="Eliminar">
                    <i class="fas fa-trash"></i>
                  </button>
                  <button @click="openSagaInNewTab(saga.id)" class="btn-view" title="Detalles">
                    <i class="fas fa-info-circle"></i>
                  </button>
                </div>
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
        :key="selectedSaga?.id || 'new'"
        :saga="selectedSaga"
        :show="showModal"
        @close="closeModal"
        @save="handleSave"
    />

    <!-- Modal de confirmación para eliminar -->
    <div v-if="showConfirmModal" class="modal-overlay">
      <div class="delete-confirmation-modal">
        <div class="modal-header">
          <h3>Confirmar eliminación</h3>
        </div>
        <div class="modal-body">
          <p>¿Estás completamente seguro de que deseas eliminar la saga "{{ sagaToDelete?.nombre }}"?</p>
          <p>La acción no se podrá revertir, y las producciones de dicha saga ya no pertenecerán a ninguna saga.</p>
          <p>¿Deseas continuar?</p>
        </div>
        <div class="modal-footer">
          <button @click="showConfirmModal = false" class="cancel-btn">Cancelar</button>
          <button @click="deleteSaga" class="confirm-delete-btn">Eliminar</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SagaModal from "@/components/modales/edicion/SagaModal.vue";
import SagasService from '@/services/sagas.service';

export default {
  name: 'AdministracionSagas',
  components: {
    SagaModal
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
      sagaToDelete: null
    };
  },
  methods: {
    openSagaInNewTab(id) {
      window.open(`/saga/${id}`, '_blank');
    },
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
      this.selectedSaga = saga ? {...saga} : null;
      this.showModal = true;
    },

    closeModal() {
      this.showModal = false;
      this.selectedSaga = null;
    },

    async handleSave({ id, data }) {
      try {
        // Si hay imagen temporal en formData.imagen y es una saga nueva, subimos la imagen primero
        if (!id && data.imagen && this.selectedFile) {
          const imageUrl = await SagasService.uploadTempSagaImage(this.selectedFile);
          data.imagen = imageUrl;
        }

        if (id) {
          await SagasService.updateSaga(id, data);
          this.$toast.success('Saga actualizada correctamente');
        } else {
          await SagasService.createSaga(data);
          this.$toast.success('Saga creada correctamente');
        }

        this.fetchSagas();
        this.closeModal();
      } catch (error) {
        this.closeModal();
      }
    },

    confirmDelete(saga) {
      this.sagaToDelete = saga;
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
/* Contenedor principal */
.sagas-container {
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px;
  font-family: 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.main-content {
  display: flex;
  height: calc(100vh - 180px);
}

/* Sección de la tabla */
.table-section {
  width: 100%;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.search-header {
  margin-bottom: 20px;
  width: 100%;
}

.search-bar {
  display: flex;
  gap: 16px;
  align-items: center;
  width: 100%;
}

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
  height: 44px;
  transition: all 0.3s ease;
}

.dark-mode .search-input-container {
  background: #2d3748;
  border-color: #4a5568;
}

.search-input-container:focus-within {
  border-color: #4299e1;
  box-shadow: 0 0 0 3px rgba(66, 153, 225, 0.2);
}

.search-icon {
  position: absolute;
  left: 14px;
  color: #a0aec0;
  font-size: 16px;
}

.search-input {
  flex: 1;
  padding: 0 14px 0 42px;
  border: none;
  background: transparent;
  color: #2d3748;
  font-size: 14px;
  outline: none;
  height: 100%;
}

.dark-mode .search-input {
  color: #e2e8f0;
}

.search-button {
  padding: 0 20px;
  height: 44px;
  background: #4299e1;
  color: white;
  border: none;
  cursor: pointer;
  font-weight: 500;
  font-size: 14px;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
}

.search-button:hover {
  background: #3182ce;
}

.new-button {
  padding: 0 20px;
  height: 44px;
  background: #10b981;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
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

/* Tabla de sagas */
.table-container {
  background: #ffffff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  border: 1px solid #e2e8f0;
  flex-grow: 1;
  overflow-y: auto;
}

.dark-mode .table-container {
  background: #1e293b;
  border-color: #334155;
}

.sagas-table {
  width: 100%;
  border-collapse: collapse;
}

.sagas-table th,
.sagas-table td {
  padding: 14px 16px;
  text-align: left;
  border-bottom: 1px solid #e2e8f0;
}

.dark-mode .sagas-table th,
.dark-mode .sagas-table td {
  border-bottom-color: #334155;
}

.sagas-table th {
  background: #f7fafc;
  font-weight: 600;
  color: #2d3748;
  padding: 14px 16px;
  position: sticky;
  top: 0;
  z-index: 10;
  border-bottom: 2px solid #e2e8f0;
}

.dark-mode .sagas-table th {
  background: #1e293b;
  color: #e2e8f0;
  border-bottom-color: #334155;
}

.sagas-table th:hover {
  background: rgba(66, 153, 225, 0.05);
  cursor: pointer;
}

.dark-mode .sagas-table th:hover {
  background: rgba(66, 153, 225, 0.1);
}

.sagas-table tr:last-child td {
  border-bottom: none;
}

.sagas-table tr:hover td {
  background: rgba(66, 153, 225, 0.03);
}

.dark-mode .sagas-table tr:hover td {
  background: rgba(66, 153, 225, 0.05);
}

/* Columnas específicas */
.sagas-table td:nth-child(2), /* Fecha Inicio */
.sagas-table td:nth-child(3), /* Estado */
.sagas-table td:nth-child(4), /* Producciones */
.sagas-table td:nth-child(5) { /* Acciones */
  text-align: center;
}

/* Botones de acción */
.actions {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.btn-edit, .btn-delete, .btn-view {
  padding: 6px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  font-size: 14px;
}

.btn-edit {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
}

.btn-edit:hover {
  background: rgba(59, 130, 246, 0.2);
}

.btn-delete {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
}

.btn-delete:hover {
  background: rgba(239, 68, 68, 0.2);
}

.btn-view {
  background: rgba(16, 185, 129, 0.1);
  color: #10b981;
}

.btn-view:hover {
  background: rgba(16, 185, 129, 0.2);
}

.no-results, .loading-row {
  text-align: center;
  padding: 40px;
  color: #718096;
}

.dark-mode .no-results,
.dark-mode .loading-row {
  color: #a0aec0;
}

.loading-row i {
  margin-right: 8px;
  color: #4299e1;
}

/* Paginación */
.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  margin-top: auto;
}

.pagination-btn {
  width: 36px;
  height: 36px;
  border: 1px solid #e2e8f0;
  background: #ffffff;
  color: #4a5568;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.dark-mode .pagination-btn {
  background: #2d3748;
  border-color: #4a5568;
  color: #cbd5e0;
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination-btn:hover:not(:disabled) {
  background: #4299e1;
  color: white;
  border-color: #4299e1;
}

.page-info {
  font-size: 14px;
  color: #718096;
  min-width: 120px;
  text-align: center;
}

.dark-mode .page-info {
  color: #a0aec0;
}

/* Modal de confirmación */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.delete-confirmation-modal {
  background: #ffffff;
  border-radius: 8px;
  border: 2px solid #ef4444;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  width: 90%;
  max-width: 500px;
  overflow: hidden;
  animation: modalFadeIn 0.3s ease;
}

.dark-mode .delete-confirmation-modal {
  background: #1e293b;
  border-color: #ef4444;
}

.modal-header {
  padding: 16px 20px;
  background: #ef4444;
  color: white;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
}

.modal-body {
  padding: 20px;
  color: #2d3748;
}

.dark-mode .modal-body {
  color: #e2e8f0;
}

.modal-body p {
  margin-bottom: 12px;
  line-height: 1.5;
  font-size: 14px;
}

.modal-footer {
  padding: 16px 20px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  background: #f7fafc;
}

.dark-mode .modal-footer {
  background: #1e293b;
}

.cancel-btn {
  padding: 8px 16px;
  background: #ffffff;
  color: #2d3748;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
}

.dark-mode .cancel-btn {
  background: #334155;
  color: #e2e8f0;
  border-color: #475569;
}

.cancel-btn:hover {
  background: #f1f5f9;
}

.dark-mode .cancel-btn:hover {
  background: #475569;
}

.confirm-delete-btn {
  padding: 8px 16px;
  background: #ef4444;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
}

.confirm-delete-btn:hover {
  background: #dc2626;
}

/* Animaciones */
@keyframes modalFadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Badge para estado */
.status-badge {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}

.status-active {
  background: #f0fff4;
  color: #2f855a;
}

.status-inactive {
  background: #fff5f5;
  color: #c53030;
}

.dark-mode .status-active {
  background: rgba(16, 185, 129, 0.1);
  color: #10b981;
}

.dark-mode .status-inactive {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
}

/* Responsive */
@media (max-width: 1024px) {
  .main-content {
    flex-direction: column;
    height: auto;
  }
}

@media (max-width: 768px) {
  .sagas-container {
    padding: 16px;
  }

  .search-bar {
    flex-direction: column;
  }

  .search-input-container,
  .new-button {
    width: 100%;
  }

  .sagas-table th,
  .sagas-table td {
    padding: 12px;
    font-size: 14px;
  }

  .actions {
    flex-wrap: wrap;
    justify-content: center;
  }
}

.action-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  height: 100%;
}

</style>