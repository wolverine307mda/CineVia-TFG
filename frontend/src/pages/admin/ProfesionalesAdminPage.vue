<template>
  <div class="producciones-container">
    <div class="main-content">
      <div class="table-section">
        <!-- Barra de búsqueda con botón -->
        <div class="search-header">
          <div class="search-bar">
            <div class="search-input-container">
              <i class="fas fa-search search-icon"></i>
              <input v-model="searchQuery" @keyup.enter="fetchProfesionales" placeholder="Buscar por nombre..." class="search-input">
              <button @click="fetchProfesionales" class="search-button">
                Buscar
              </button>
            </div>
            <button @click="openModal(null)" class="new-button">
              <i class="fas fa-plus"></i>
              <span>Nuevo Profesional</span>
            </button>
          </div>
        </div>

        <!-- Tabla de profesionales con tamaños fijos -->
        <div class="table-container">
          <table class="producciones-table" translate="no">
            <thead>
            <tr>
              <th style="width: 40%;" @click="sortBy('nombre')">
                Nombre
              </th>
              <th style="width: 15%; text-align: center;" @click="sortBy('fechaNacimiento')">
                Nacimiento
              </th>
              <th style="width: 15%; text-align: center;" @click="sortBy('fechaInicio')">
                Inicio
              </th>
              <th style="width: 20%; text-align: center;">
                Lugar Nacimiento
              </th>
              <th style="width: 10%; text-align: left;">Acciones</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="profesional in profesionales" :key="profesional.id">
              <td style="width: 40%;">{{ profesional.nombre }}</td>
              <td style="width: 15%; text-align: center;">
                <span class="truncate-cell" :title="profesional.nombre">
                  {{ formatDate(profesional.fechaNacimiento) }}
                </span>
              </td>
              <td style="width: 15%; text-align: center;">
                  <span class="truncate-cell" :title="profesional.nombre">
                    {{ formatDate(profesional.fechaInicio) }}
                  </span>
              </td>
              <td style="width: 20%; text-align: center;">
                <span class="truncate-cell" :title="profesional.lugarNacimiento">
                  {{ profesional.lugarNacimiento }}
                </span>
              </td>
              <td style="width: 10%; text-align: center;">
                  <div class="action-buttons">
                    <button @click="openModal(profesional)" class="btn-edit" title="Editar">
                      <i class="fas fa-edit"></i>
                    </button>
                    <button @click="confirmDelete(profesional)" class="btn-delete" title="Eliminar">
                      <i class="fas fa-trash"></i>
                    </button>
                    <button @click="$router.push(`/profesional/${profesional.id}`)" class="btn-view" title="Detalles">
                      <i class="fas fa-info-circle"></i>
                    </button>
                  </div>
              </td>
            </tr>
            <tr v-if="profesionales.length === 0 && !loading">
              <td colspan="5" class="no-results">
                No se encontraron profesionales
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
    <ProfesionalModal
        v-if="showModal"
        :profesional="selectedProfesional"
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
          <p>¿Estás completamente seguro de que deseas eliminar al profesional "{{ profesionalToDelete?.nombre }}"?</p>
          <p>La acción no se podrá revertir y toda la información asociada se perderá permanentemente.</p>
          <p>¿Deseas continuar?</p>
        </div>
        <div class="modal-footer">
          <button @click="showConfirmModal = false" class="cancel-btn">Cancelar</button>
          <button @click="deleteProfesional" class="confirm-delete-btn">Eliminar</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ProfesionalModal from "@/components/modales/edicion/ProfesionalModal.vue";
import ProfessionalsService from '@/services/profesional.service.js';

export default {
  name: 'AdminProfesionales',
  components: {
    ProfesionalModal
  },
  data() {
    return {
      profesionales: [],
      currentPage: 1,
      itemsPerPage: 10,
      totalItems: 0,
      totalPages: 1,
      loading: false,
      searchQuery: '',
      sortField: 'nombre',
      sortDirection: 'asc',
      showModal: false,
      showConfirmModal: false,
      selectedProfesional: null,
      profesionalToDelete: null
    };
  },
  methods: {
    async fetchProfesionales() {
      this.loading = true;
      try {
        const filters = this.searchQuery ? {nombre: this.searchQuery} : {};

        const result = await ProfessionalsService.fetchProfessionals(filters, {
          page: this.currentPage - 1,
          size: this.itemsPerPage,
          sortBy: this.sortField,
          sortDirection: this.sortDirection
        });

        this.profesionales = result.data;
        this.totalItems = result.totalItems;
        this.totalPages = result.totalPages;
      } catch (error) {
        console.error("Error fetching profesionales:", error);
        this.$toast.error("Error al cargar los profesionales");
      } finally {
        this.loading = false;
      }
    },

    formatDate(dateString) {
      return ProfessionalsService.formatDate(dateString);
    },

    sortBy(field) {
      if (this.sortField === field) {
        this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
      } else {
        this.sortField = field;
        this.sortDirection = 'asc';
      }
      this.currentPage = 1;
      this.fetchProfesionales();
    },

    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchProfesionales();
      }
    },

    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchProfesionales();
      }
    },

    openModal(profesional) {
      this.selectedProfesional = profesional ? { ...profesional } : {
        nombre: '',
        fechaNacimiento: '',
        fechaInicio: '',
        lugarNacimiento: '',
        biografia: '',
        foto: ''
      };
      this.showModal = true;
    },

    closeModal() {
      this.showModal = false;
      this.selectedProfesional = null;
    },

    async handleSave({ id, data }) {
      try {
        if (id) {
          await ProfessionalsService.updateProfessional(id, data);
          this.$toast.success('Profesional actualizado correctamente');
        } else {
          await ProfessionalsService.createProfessional(data);
          this.$toast.success('Profesional creado correctamente');
        }

        await this.fetchProfesionales();
        this.closeModal();
      } catch (error) {
        const errorMessage = ProfessionalsService.handleError(error, 'Error al guardar el profesional');
        this.$toast.error(errorMessage);
      }
    },

    confirmDelete(profesional) {
      this.profesionalToDelete = profesional;
      this.showConfirmModal = true;
    },

    async deleteProfesional() {
      try {
        await ProfessionalsService.deleteProfessional(this.profesionalToDelete.id);
        this.$toast.success('Profesional eliminado correctamente');
        this.fetchProfesionales();
      } catch (error) {
        const errorMessage = ProfessionalsService.handleError(error, 'Error al eliminar el profesional');
        this.$toast.error(errorMessage);
      } finally {
        this.showConfirmModal = false;
        this.profesionalToDelete = null;
      }
    }
  },
  created() {
    this.fetchProfesionales();
  }
};
</script>

<style scoped>
/* Contenedor principal */
.producciones-container {
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

/* Tabla de profesionales */
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

.producciones-table {
  width: 100%;
  border-collapse: collapse;
}

.producciones-table th,
.producciones-table td {
  padding: 14px 16px;
  text-align: left;
  border-bottom: 1px solid #e2e8f0;
}

.dark-mode .producciones-table th,
.dark-mode .producciones-table td {
  border-bottom-color: #334155;
}

.producciones-table th {
  background: #f7fafc;
  font-weight: 600;
  color: #2d3748;
  padding: 14px 16px;
  position: sticky;
  top: 0;
  z-index: 10;
  border-bottom: 2px solid #e2e8f0;
}

.dark-mode .producciones-table th {
  background: #1e293b;
  color: #e2e8f0;
  border-bottom-color: #334155;
}

.producciones-table th:hover {
  background: rgba(66, 153, 225, 0.05);
  cursor: pointer;
}

.dark-mode .producciones-table th:hover {
  background: rgba(66, 153, 225, 0.1);
}

.producciones-table tr:last-child td {
  border-bottom: none;
}

.producciones-table tr:hover td {
  background: rgba(66, 153, 225, 0.03);
}

.dark-mode .producciones-table tr:hover td {
  background: rgba(66, 153, 225, 0.05);
}

/* Columnas específicas */
.producciones-table td:nth-child(2), /* Fecha Nacimiento */
.producciones-table td:nth-child(3), /* Fecha Inicio */
.producciones-table td:nth-child(4), /* Lugar Nacimiento */
.producciones-table td:nth-child(5) { /* Acciones */
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
  background: white;
}

.dark-mode .modal-body {
  color: #e2e8f0;
  background: #1e293b;
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

/* Estilo para fechas */
.date-cell {
  font-family: 'Roboto Mono', monospace;
  font-size: 13px;
  color: #4a5568;
}

.dark-mode .date-cell {
  color: #a0aec0;
}

/* Responsive */
@media (max-width: 1024px) {
  .main-content {
    flex-direction: column;
    height: auto;
  }
}

@media (max-width: 768px) {
  .producciones-container {
    padding: 16px;
  }

  .search-bar {
    flex-direction: column;
  }

  .search-input-container,
  .new-button {
    width: 100%;
  }

  .producciones-table th,
  .producciones-table td {
    padding: 12px;
    font-size: 14px;
  }

  .actions {
    flex-wrap: wrap;
    justify-content: center;
  }
}
.producciones-table tr {
  display: table-row;
}

.producciones-table td {
  vertical-align: middle !important;
  height: 100%;
}

.producciones-table td span,
.producciones-table td div,
.producciones-table td font {
  display: flex;
  align-items: center;
  height: 100%;
}


</style>