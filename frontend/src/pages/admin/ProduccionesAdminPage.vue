<template>
  <div class="producciones-container">
    <!-- Contenedor principal -->
    <div class="main-content">
      <!-- Sección de tabla -->
      <div class="table-section">
        <!-- Búsqueda -->
        <div class="search-container">
          <div class="search-box">
            <i class="fas fa-search"></i>
            <input
                v-model="searchQuery"
                @input="debouncedSearch"
                placeholder="Buscar producciones..."
                type="search"
            >
            <button @click="openModal(null)" class="btn-new-icon" title="Nueva Producción">
              <i class="fas fa-plus"></i>
            </button>
          </div>
        </div>

        <!-- Tabla de producciones -->
        <div class="table-container">
          <table class="producciones-table">
            <thead>
            <tr style="background-color: inherit;">
              <th style="background-color: inherit !important; padding: 1rem; font-weight: 600; border-bottom: 2px solid #e2e8f0; position: sticky; top: 0; z-index: 100;"
                  @click="sortBy('titulo')">
                Título
              </th>
              <th style="background-color: inherit !important; padding: 1rem; font-weight: 600; border-bottom: 2px solid #e2e8f0; position: sticky; top: 0; z-index: 100;"
                  @click="sortBy('tipo')">
                Tipo
              </th>
              <th style="background-color: inherit !important; padding: 1rem; font-weight: 600; border-bottom: 2px solid #e2e8f0; position: sticky; top: 0; z-index: 100;"
                  @click="sortBy('estreno')">
                Estreno
              </th>
              <th style="background-color: inherit !important; padding: 1rem; font-weight: 600; border-bottom: 2px solid #e2e8f0; position: sticky; top: 0; z-index: 100;"
                  @click="sortBy('clasificacionEdad')">
                Clasificación
              </th>
              <th style="background-color: inherit !important; padding: 1rem; font-weight: 600; border-bottom: 2px solid #e2e8f0; position: sticky; top: 0; z-index: 100;">
                Acciones
              </th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="produccion in producciones" :key="produccion.id">
              <td>{{ produccion.titulo }}</td>
              <td>{{ formatTipoProduccion(produccion.tipo) }}</td>
              <td>{{ formatDate(produccion.estreno) }}</td>
              <td>{{ produccion.clasificacionEdad }}+</td>
              <td class="actions">
                <button @click="openModal(produccion)" class="btn-edit">
                  <i class="fas fa-edit"></i>
                </button>
                <button @click="confirmDelete(produccion)" class="btn-delete">
                  <i class="fas fa-trash"></i>
                </button>
                <button @click="viewDetails(produccion)" class="btn-view">
                  <i class="fas fa-info-circle"></i>
                </button>
              </td>
            </tr>
            <tr v-if="producciones.length === 0">
              <td colspan="5" class="no-results">
                No se encontraron producciones
              </td>
            </tr>
            </tbody>
          </table>
        </div>

        <!-- Paginación -->
        <div class="pagination-container">
          <button
              @click="prevPage"
              :disabled="currentPage === 1"
              class="pagination-btn"
          >
            <i class="fas fa-chevron-left"></i>
          </button>
          <span class="page-info">
            Página {{ currentPage }} de {{ totalPages }}
          </span>
          <button
              @click="nextPage"
              :disabled="currentPage >= totalPages"
              class="pagination-btn"
          >
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>
    </div>

    <!-- Modal para crear/editar -->
    <ProduccionModal
        v-if="showModal"
        :produccion="selectedProduccion"
        :show="showModal"
        @close="closeModal"
        @save="handleSave"
    />
  </div>
</template>

<script>
import { debounce } from 'lodash';
import ProduccionModal from "@/components/modales/ProduccionModal.vue";

export default {
  name: 'Administracion de Producciones',
  components: {
    ProduccionModal
  },
  data() {
    return {
      producciones: [],
      currentPage: 1,
      itemsPerPage: 10,
      totalItems: 0,
      totalPages: 1,
      loading: false,
      searchQuery: '',
      sortField: 'titulo',
      sortDirection: 'asc',
      showModal: false,
      selectedProduccion: null
    }
  },
  created() {
    this.debouncedSearch = debounce(this.fetchProducciones, 500);
    this.fetchProducciones();
  },
  methods: {
    async fetchProducciones() {
      this.loading = true;
      try {
        const response = await fetch(
            `http://localhost:8080/api/producciones?page=${this.currentPage - 1}&size=${this.itemsPerPage}&sortBy=${this.sortField}&sortDirection=${this.sortDirection}&search=${this.searchQuery}`
        );

        if (!response.ok) {
          throw new Error('Error al cargar producciones');
        }

        const data = await response.json();
        this.producciones = data.data;
        this.totalItems = data.totalItems;
        this.totalPages = data.totalPages;
      } catch (error) {
        console.error('Error:', error);
        if (process.env.NODE_ENV !== 'production') {
          this.loadMockData();
        }
      } finally {
        this.loading = false;
      }
    },
    formatTipoProduccion(tipo) {
      const tipos = {
        'PELICULA': 'Película',
        'SERIE': 'Serie',
        'CORTO': 'Cortometraje'
      };
      return tipos[tipo] || tipo;
    },
    formatDate(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleDateString('es-ES', { year: 'numeric', month: 'short', day: 'numeric' });
    },
    sortBy(field) {
      if (this.sortField === field) {
        this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
      } else {
        this.sortField = field;
        this.sortDirection = 'asc';
      }
      this.fetchProducciones();
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchProducciones();
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchProducciones();
      }
    },
    openModal(produccion) {
      this.selectedProduccion = produccion ? { ...produccion } : null;
      this.showModal = true;
    },
    closeModal() {
      this.showModal = false;
      this.selectedProduccion = null;
    },
    async handleSave(produccionData) {
      try {
        const method = produccionData.id ? 'PUT' : 'POST';
        const url = produccionData.id
            ? `http://localhost:8080/api/producciones/${produccionData.id}`
            : 'http://localhost:8080/api/producciones';

        const response = await fetch(url, {
          method,
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(produccionData)
        });

        if (!response.ok) {
          throw new Error('Error al guardar la producción');
        }

        this.fetchProducciones();
        this.closeModal();
      } catch (error) {
        console.error('Error:', error);
        alert('Error al guardar la producción');
      }
    },
    viewDetails(produccion) {
      // Navegar a la vista de detalles o abrir un modal de detalles
      console.log("Ver detalles de:", produccion);
      // this.$router.push(`/producciones/${produccion.id}`);
    },
    confirmDelete(produccion) {
      if (confirm(`¿Estás seguro de que quieres eliminar "${produccion.titulo}"?`)) {
        this.deleteProduccion(produccion.id);
      }
    },
    async deleteProduccion(id) {
      try {
        const response = await fetch(`http://localhost:8080/api/producciones/${id}`, {
          method: 'DELETE'
        });

        if (!response.ok) {
          throw new Error('Error al eliminar la producción');
        }

        this.fetchProducciones();
      } catch (error) {
        console.error('Error:', error);
        alert('Error al eliminar la producción');
      }
    }
  }
}
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
</style>