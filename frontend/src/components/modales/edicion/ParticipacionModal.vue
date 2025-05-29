<template>
  <div v-if="show" class="modal-overlay" @click.self="close">
    <div class="modal-wrapper">
      <div class="modal-card">
        <div class="modal-header">
          <h3 class="modal-title">{{ isEditMode ? 'Editar Participación' : 'Nueva Participación' }}</h3>
          <button @click="close" class="modal-close-button">
            <i class="fas fa-times"></i>
          </button>
        </div>

        <div class="modal-content">
          <form @submit.prevent="save" class="modal-form">
            <div class="form-field"><br>
              <label class="form-label">Profesional <span class="required">*</span></label>
              <div class="search-container">
                <input
                    v-model="profesionalSearchQuery"
                    @input="searchProfesionales"
                    type="text"
                    class="form-input"
                    placeholder="Buscar profesional..."
                    :disabled="isEditMode"
                >
                <i class="fas fa-search search-icon"></i>
              </div>
            </div>

            <div v-if="profesionalSearchQuery" class="search-results-container">
              <div v-if="searchLoading" class="loading-spinner">
                <i class="fas fa-spinner fa-spin"></i> Buscando...
              </div>

              <ul v-else class="search-results-list">
                <li
                    v-for="profesional in filteredProfesionales"
                    :key="profesional.id"
                    class="search-result-item"
                    :class="{ 'disabled': isProfesionalAsignado(profesional.id) }"
                    @click="selectProfesional(profesional)"
                >
                  <div class="profesional-info">
                    <img
                        :src="profesional.foto || 'default-avatar.jpg'"
                        class="profesional-avatar"
                        @error="handleImageError"
                    >
                    <div>
                      <div class="profesional-name">{{ profesional.nombre }}</div>
                      <div class="profesional-meta">{{ formatDate(profesional.fechaNacimiento) }}</div>
                    </div>
                  </div>
                  <span v-if="isProfesionalAsignado(profesional.id)" class="asignado-badge">Ya asignado</span>
                </li>
              </ul>
            </div>

            <div v-if="selectedProfesional" class="selected-profesional">
              <div class="profesional-info">
                <img
                    :src="selectedProfesional.foto || 'default-avatar.jpg'"
                    class="profesional-avatar"
                    @error="handleImageError"
                >
                <div>
                  <div class="profesional-name">{{ selectedProfesional.nombre }}</div>
                  <div class="profesional-meta">{{ formatDate(selectedProfesional.fechaNacimiento) }}</div>
                </div>
              </div>
            </div>

            <!-- Combobox para el rol (solo ACTOR o DIRECTOR) -->
            <div class="form-field">
              <label class="form-label">Rol <span class="required">*</span></label>
              <select
                  v-model="formData.rol"
                  class="form-select"
                  required
              >
                <option value="" disabled selected>Seleccione un rol</option>
                <option value="DIRECTOR">Director</option>
                <option value="ACTOR">Actor/Actriz</option>
              </select>
            </div>

            <div v-if="formData.rol === 'ACTOR'" class="form-field">
              <label class="form-label">Papel (opcional)</label>
              <input v-model="formData.papel" type="text" class="form-input">
            </div>

            <div class="modal-footer">
              <button type="button" @click="close" class="button button-secondary">Cancelar</button>
              <button
                  type="submit"
                  class="button button-primary"
                  :disabled="!formData.profesionalId || !formData.rol"
              >
                {{ isEditMode ? 'Actualizar' : 'Crear' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ProfesionalesService from '@/services/profesional.service';

export default {
  name: 'ParticipacionModal',
  props: {
    show: Boolean,
    participacion: Object,
    produccionId: [String, Number],
    participacionesExistentes: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      formData: {
        id: null,
        produccionId: null,
        profesionalId: null,
        rol: '', // ACTOR o DIRECTOR en mayúsculas
        papel: null
      },
      selectedProfesional: null,
      profesionalSearchQuery: '',
      filteredProfesionales: [],
      searchLoading: false,
      searchTimeout: null
    };
  },
  computed: {
    isEditMode() {
      return !!this.participacion;
    }
  },
  watch: {
    participacion: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.formData = {
            id: newVal.id,
            produccionId: this.produccionId,
            profesionalId: newVal.profesional.id,
            rol: newVal.rol, // Viene del backend en mayúsculas
            papel: newVal.papel
          };
          this.selectedProfesional = {
            id: newVal.profesional.id,
            nombre: newVal.profesional.nombre,
            foto: newVal.profesional.foto,
            fechaNacimiento: newVal.profesional.fechaNacimiento
          };
          this.profesionalSearchQuery = newVal.profesional.nombre;
        } else {
          this.resetForm();
        }
      }
    },
    produccionId(newVal) {
      if (newVal) {
        this.formData.produccionId = newVal;
      }
    }
  },
  methods: {
    resetForm() {
      this.formData = {
        id: null,
        produccionId: this.produccionId,
        profesionalId: null,
        rol: '',
        papel: null
      };
      this.selectedProfesional = null;
      this.profesionalSearchQuery = '';
      this.filteredProfesionales = [];
    },
    close() {
      this.$emit('close');
    },
    save() {
      // El rol ya está en mayúsculas (ACTOR/DIRECTOR) por el select
      this.$emit('save', this.formData);
    },
    handleImageError(e) {
      e.target.src = 'default-avatar.jpg';
    },
    formatDate(dateString) {
      if (!dateString) return 'N/A';
      const date = new Date(dateString);
      return date.toLocaleDateString('es-ES');
    },
    async searchProfesionales() {
      if (this.searchTimeout) {
        clearTimeout(this.searchTimeout);
      }

      this.searchTimeout = setTimeout(async () => {
        if (!this.profesionalSearchQuery.trim()) {
          this.filteredProfesionales = [];
          return;
        }

        this.searchLoading = true;
        try {
          const result = await ProfesionalesService.fetchProfessionals(
              { nombre: this.profesionalSearchQuery },
              { page: 0, size: 10 }
          );

          // Filtrar profesionales ya asignados
          this.filteredProfesionales = result.data.filter(prof =>
              !this.isProfesionalAsignado(prof.id) || prof.id === this.formData.profesionalId
          );
        } catch (error) {
          console.error('Error al buscar profesionales:', error);
          this.filteredProfesionales = [];
        } finally {
          this.searchLoading = false;
        }
      }, 500);
    },
    selectProfesional(profesional) {
      if (this.isProfesionalAsignado(profesional.id) && profesional.id !== this.formData.profesionalId) {
        return;
      }

      this.formData.profesionalId = profesional.id;
      this.selectedProfesional = {
        id: profesional.id,
        nombre: profesional.nombre,
        foto: profesional.foto,
        fechaNacimiento: profesional.fechaNacimiento
      };
    },
    isProfesionalAsignado(profesionalId) {
      if (!profesionalId || !this.participacionesExistentes) return false;
      return this.participacionesExistentes.some(
          p => p.profesional.id === profesionalId && p.id !== this.formData.id
      );
    }
  }
};
</script>

<style scoped>
/* Estilos base del modal (igual que en SagaModal) */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
  transition: opacity 0.3s ease;
}

.modal-fade-enter,
.modal-fade-leave-to {
  opacity: 0;
}

.modal-wrapper {
  width: 100%;
  max-width: 600px;
  max-height: 90vh;
  margin: 1rem;
  overflow: hidden;
}

.modal-card {
  display: flex;
  flex-direction: column;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  background-color: white;
  transform: translateY(0);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.dark-mode .modal-card {
  background-color: #1e293b;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  background-color: var(--modal-header-bg);
  border-bottom: 1px solid var(--modal-border);
}

.modal-title {
  margin: 0;
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--modal-text);
}

.modal-close-button {
  background: none;
  border: none;
  font-size: 1.25rem;
  cursor: pointer;
  transition: transform 0.2s ease;
  padding: 0.5rem;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--modal-close);
}

.modal-close-button:hover {
  transform: rotate(90deg);
  color: var(--modal-close-hover);
  background-color: rgba(239, 68, 68, 0.1);
}

.modal-content {
  padding: 1.5rem;
  overflow-y: auto;
  max-height: calc(90vh - 100px);
  background-color: var(--modal-bg);
}

.modal-form {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

/* Estilos de formulario */
.form-field {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-label {
  font-weight: 500;
  font-size: 0.9rem;
  color: var(--modal-text);
}

.required {
  color: var(--danger-color);
  margin-left: 0.25rem;
}

.form-input,
.form-textarea {
  padding: 0.75rem;
  width: 100%;
  border-radius: 6px;
  border: 1px solid;
  font-size: 0.95rem;
  border-color: blueviolet;
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
  background-color: var(--input-bg);
  color: var(--input-text);
}

.form-input:focus,
.form-textarea:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(126, 91, 239, 0.3);
}

.form-input.disabled {
  background-color: var(--disabled-bg);
  cursor: not-allowed;
}

.form-textarea {
  min-height: 100px;
  resize: vertical;
}

.form-error {
  color: var(--danger-color);
  font-size: 0.85rem;
  margin-top: 0.25rem;
}

/* Search container */
.search-container {
  position: relative;
  margin-bottom: 0.5rem;
}

.search-icon {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: #64748b;
}

/* Resultados de búsqueda */
.search-results-container {
  border: 1px solid var(--border-color);
  border-radius: 6px;
  max-height: 300px;
  overflow-y: auto;
  margin-bottom: 1rem;
}

.loading-spinner {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 1rem;
  color: var(--text-secondary);
}

.no-results {
  text-align: center;
  padding: 1rem;
  color: var(--text-secondary);
  font-style: italic;
}

.search-results-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.search-result-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 1rem;
  border-bottom: 1px solid var(--border-color);
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.search-result-item:last-child {
  border-bottom: none;
}

.search-result-item:hover {
  background-color: var(--hover-bg);
}

.search-result-item.disabled {
  background-color: var(--disabled-bg);
  cursor: not-allowed;
  opacity: 0.7;
}

.profesional-info {
  display: flex;
  align-items: center;
  gap: 1rem;
  flex-grow: 1;
}

.profesional-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
}

.profesional-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.profesional-details {
  display: flex;
  flex-direction: column;
}

.profesional-name {
  font-weight: 500;
}

.profesional-meta {
  font-size: 0.85rem;
  color: var(--text-secondary);
}

.asignado-badge {
  font-size: 0.75rem;
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  background-color: rgba(239, 68, 68, 0.1);
  color: var(--danger-color);
}

.selected-icon {
  color: var(--primary-color);
  font-size: 1.25rem;
}

/* Profesional seleccionado */
.selected-profesional {
  border: 1px solid var(--border-color);
  border-radius: 6px;
  padding: 1rem;
  margin-bottom: 1rem;
  background-color: var(--card-bg);
}

/* Paginación */
.pagination-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-top: 1rem;
  padding: 1rem;
  border-top: 1px solid var(--border-color);
}

.pagination-button {
  padding: 0.5rem;
  border-radius: 6px;
  border: 1px solid var(--border-color);
  background: var(--card-bg);
  color: var(--text-primary);
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.pagination-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination-button:hover:not(:disabled) {
  background: var(--primary-color);
  color: white;
  border-color: var(--primary-color);
}

.page-info {
  font-size: 0.9rem;
  color: var(--text-secondary);
}

/* Footer del modal */
.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding-top: 1.5rem;
  margin-top: 1rem;
  border-top: 1px solid var(--modal-border);
}

/* Botones */
.button {
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
  font-size: 0.95rem;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.button-primary {
  background-color: var(--primary-color);
  color: white;
}

.button-primary:hover {
  background-color: var(--primary-hover);
  transform: translateY(-1px);
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.button-primary:disabled {
  opacity: 0.6;
  background-color: var(--primary-color);
  cursor: not-allowed;
}

.button-secondary {
  background-color: var(--secondary-bg);
  color: var(--secondary-text);
}

.button-secondary:hover {
  background-color: var(--secondary-hover);
}

/* Variables CSS para temas */
:root {
  --modal-bg: #ffffff;
  --modal-header-bg: #f8fafc;
  --modal-border: #e2e8f0;
  --modal-text: #1e293b;
  --modal-close: #64748b;
  --modal-close-hover: #ef4444;
  --input-bg: #ffffff;
  --input-border: #e2e8f0;
  --input-text: #1e293b;
  --disabled-bg: #f1f5f9;
  --hover-bg: #f8fafc;
  --primary-color: #7e5bef;
  --primary-hover: #6d46e8;
  --danger-color: #ef4444;
  --secondary-bg: #f1f5f9;
  --secondary-text: #64748b;
  --secondary-hover: #e2e8f0;
  --text-secondary: #64748b;
  --text-primary: #1e293b;
  --card-bg: #ffffff;
  --border-color: #e2e8f0;
}

.dark-mode {
  --modal-bg: #1e293b;
  --modal-header-bg: #0f172a;
  --modal-border: #334155;
  --modal-text: #f8fafc;
  --modal-close: #94a3b8;
  --modal-close-hover: #ef4444;
  --input-bg: #1e293b;
  --input-border: #334155;
  --input-text: #f8fafc;
  --disabled-bg: #334155;
  --hover-bg: #1e293b;
  --primary-color: #8b5cf6;
  --primary-hover: #7c3aed;
  --secondary-bg: #334155;
  --secondary-text: #94a3b8;
  --secondary-hover: #475569;
  --text-secondary: #94a3b8;
  --text-primary: #f8fafc;
  --card-bg: #1e293b;
  --border-color: #334155;
}

/* Responsive */
@media (max-width: 768px) {
  .modal-wrapper {
    margin: 0.5rem;
  }

  .modal-header {
    padding: 1rem;
  }

  .modal-content {
    padding: 1rem;
  }

  .modal-footer {
    flex-direction: column;
    gap: 0.75rem;
  }

  .button {
    width: 100%;
  }
}

.form-select {
  appearance: none;
  background-image: url("data:image/svg+xml;charset=UTF-8,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='currentColor' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3e%3cpolyline points='6 9 12 15 18 9'%3e%3c/polyline%3e%3c/svg%3e");
  background-repeat: no-repeat;
  background-position: right 0.75rem center;
  background-size: 1em;
  padding-right: 2.5rem;
}

.form-field[data-role="actor"] {
  transition: all 0.3s ease;
  overflow: hidden;
}

</style>

<style scoped src="@/assets/styles/modalesAdmin.css"/>