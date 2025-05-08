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
          <table class="producciones-table">
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
              <th style="width: 10%; text-align: center;">Acciones</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="profesional in profesionales" :key="profesional.id">
              <td style="width: 40%;">{{ profesional.nombre }}</td>
              <td style="width: 15%; text-align: center;">{{ formatDate(profesional.fechaNacimiento) }}</td>
              <td style="width: 15%; text-align: center;">{{ formatDate(profesional.fechaInicio) }}</td>
              <td style="width: 20%; text-align: center;">{{ profesional.lugarNacimiento }}</td>
              <td style="width: 10%; text-align: center;" class="actions">
                <button @click="openModal(profesional)" class="btn-edit" title="Editar">
                  <i class="fas fa-edit"></i>
                </button>
                <button @click="confirmDelete(profesional)" class="btn-delete" title="Eliminar">
                  <i class="fas fa-trash"></i>
                </button>
                <button @click="viewDetails(profesional)" class="btn-view" title="Detalles">
                  <i class="fas fa-info-circle"></i>
                </button>
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
    <ConfirmModal
        v-if="showConfirmModal"
        :show="showConfirmModal"
        title="Confirmar eliminación"
        :message="confirmMessage"
        @confirm="deleteProfesional"
        @cancel="showConfirmModal = false"
    />
  </div>
</template>

<script>
import ProfesionalModal from "@/components/modales/ProfesionalModal.vue";
import ProfessionalsService from '@/services/profesional.service.js';
import ConfirmModal from "@/components/cards/ConfirmModal.vue";

export default {
  name: 'AdminProfesionales',
  components: {
    ProfesionalModal,
    ConfirmModal
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
  computed: {
    confirmMessage() {
      return this.profesionalToDelete
          ? `¿Estás seguro de que deseas eliminar a ${this.profesionalToDelete.nombre}?`
          : '';
    }
  },
  methods: {
    async fetchProfesionales() {
      this.loading = true;
      try {
        const filters = this.searchQuery ? { nombre: this.searchQuery } : {};

        const result = await ProfessionalsService.searchProfessionals(filters, {
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
      this.selectedProfesional = profesional ? { ...profesional } : null;
      this.showModal = true;
    },

    closeModal() {
      this.showModal = false;
      this.selectedProfesional = null;
    },

    async handleSave(profesionalData) {
      try {
        if (profesionalData.id) {
          await ProfessionalsService.updateProfessional(profesionalData.id, profesionalData);
          this.$toast.success('Profesional actualizado correctamente');
        } else {
          await ProfessionalsService.createProfessional(profesionalData);
          this.$toast.success('Profesional creado correctamente');
        }

        this.fetchProfesionales();
        this.closeModal();
      } catch (error) {
        const errorMessage = ProfessionalsService.handleError(error, 'Error al guardar el profesional');
        this.$toast.error(errorMessage);
      }
    },

    viewDetails(profesional) {
      this.$router.push({ name: 'ProfesionalDetalle', params: { id: profesional.id } });
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

.table-container thead{
  background-color: #f8fafc;
  position: sticky;
  top: 0;
  z-index: 100;
}

.table-container th{
  color: #1e293b;
}

.dark-mode .table-container th{
  color: white;
}

.dark-mode .table-container thead{
  background-color: #121212;
  color: white;
}

.producciones-container {
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

.producciones-table {
  width: 100%;
  border-collapse: collapse;
}

.producciones-table th,
.producciones-table td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid var(--border-color);
}

.producciones-table th {
  background: var(--card-header-bg-solid);
  font-weight: 600;
  color: var(--text-primary);
  padding: 1rem;
  position: sticky;
  top: 0;
  z-index: 10;
  border-bottom: 2px solid var(--border-color);
}

.producciones-table th:hover {
  background: rgba(126, 91, 239, 0.05);
}

.producciones-table tr:last-child td {
  border-bottom: none;
}

.producciones-table tr:hover td {
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
  .producciones-table {
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

/* Botón "Nueva Producción" */
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