<template>
  <transition name="modal-fade">
    <div v-if="show" class="modal-overlay" @click.self="close">
      <div class="modal-wrapper">
        <div class="modal-card">
          <div class="modal-header">
            <h3 class="modal-title">{{ isEditMode ? 'Editar Saga' : 'Crear Nueva Saga' }}</h3>
            <button class="modal-close-button" @click="close">
              <i class="fas fa-times"></i>
            </button>
          </div>

          <div class="modal-content">
            <form @submit.prevent="save" class="modal-form">
              <div class="form-field">
                <label class="form-label">Nombre <span class="required">*</span></label>
                <input
                    v-model="formData.nombre"
                    type="text"
                    class="form-input"
                    required
                    placeholder="Ej: El Señor de los Anillos"
                >
                <div class="form-error" v-if="errors.nombre">{{ errors.nombre }}</div>
              </div>

              <div class="form-field">
                <label class="form-label">Descripción</label>
                <textarea
                    v-model="formData.descripcion"
                    rows="3"
                    class="form-textarea"
                    placeholder="Descripción detallada de la saga..."
                ></textarea>
              </div>

              <div class="form-row">
                <div class="form-field form-col">
                  <label class="form-label">Fecha de Inicio <span class="required">*</span></label>
                  <input
                      v-model="formData.fechaInicio"
                      type="date"
                      class="form-input"
                      required
                  >
                  <div class="form-error" v-if="errors.fechaInicio">{{ errors.fechaInicio }}</div>
                </div>

                <div class="form-field form-col">
                  <label class="form-label">Fecha de Finalización</label>
                  <input
                      v-model="formData.fechaFin"
                      type="date"
                      class="form-input"
                      :disabled="!formData.isAcabada"
                      :class="{ 'disabled': !formData.isAcabada }"
                  >
                </div>
              </div>

              <div class="form-field checkbox-field">
                <div class="form-checkbox-item">
                  <input
                      id="isAcabada"
                      v-model="formData.isAcabada"
                      type="checkbox"
                      class="form-checkbox"
                  >
                  <label for="isAcabada" class="form-checkbox-label">Saga finalizada</label>
                </div>
              </div>

              <div class="form-field">
                <label class="form-label">Imagen</label>
                <div class="image-upload-container">
                  <div class="upload-options">
                    <input
                        ref="fileInput"
                        type="file"
                        accept="image/*"
                        @change="handleFileUpload"
                        class="file-input"
                        :disabled="isUploading"
                    >
                    <button
                        type="button"
                        class="button button-small button-secondary"
                        @click="triggerFileInput"
                        :disabled="isUploading"
                    >
                      <i class="fas fa-upload"></i> Seleccionar imagen
                    </button>
                    <span class="file-name" v-if="selectedFile">{{ selectedFile.name }}</span>
                  </div>

                  <div class="upload-status" v-if="isUploading">
                    <div class="upload-loader"></div>
                    <span class="upload-text">Subiendo imagen...</span>
                  </div>

                  <div class="upload-error" v-if="uploadError">
                    <i class="fas fa-exclamation-circle"></i>
                    <span>{{ uploadError }}</span>
                  </div>

                  <div class="image-preview-container" v-if="previewImage || formData.imagen">
                    <span class="image-preview-label">Vista previa:</span>
                    <div class="image-preview">
                      <img
                          :src="previewImage || formData.imagen"
                          alt="Preview de la imagen"
                          @error="handleImageError"
                      >
                      <button
                          v-if="(previewImage || formData.imagen) && isEditMode"
                          type="button"
                          class="button-icon danger remove-image-button"
                          @click="removeImage"
                          title="Eliminar imagen"
                      >
                        <i class="fas fa-trash"></i>
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              <br><br>

              <!-- Sección de Producciones -->
              <div class="form-field">
                <div class="producciones-header">
                  <h2>Producciones en la Saga</h2>
                  <button
                      v-if="isEditMode"
                      type="button"
                      class="button button-small button-primary"
                      @click="openAddProduccionModal"
                      :disabled="isUploading || isSaving"
                  >
                    <i class="fas fa-plus"></i> Añadir Producción
                  </button>
                </div>

                <div v-if="producciones.length === 0" class="no-productions">
                  No hay producciones en esta saga
                </div>

                <ul v-else class="producciones-list">
                  <li v-for="produccion in producciones" :key="produccion.id" class="produccion-item">
                    <div class="produccion-info">
                      <span class="produccion-title">{{ produccion.titulo }}</span>
                      <span class="produccion-meta">{{ formatTipoProduccion(produccion.tipo) }} • {{ formatYear(produccion.estreno) }}</span>
                    </div>
                    <button
                        v-if="isEditMode"
                        class="button-icon danger"
                        @click="removeProduccion(produccion.id)"
                        title="Eliminar de la saga"
                        :disabled="isUploading || isSaving"
                    >
                      <i class="fas fa-trash"></i>
                    </button>
                  </li>
                </ul>
              </div>

              <div class="modal-footer">
                <button type="button" class="button button-secondary" @click="close" :disabled="isUploading || isSaving">
                  Cancelar
                </button>
                <button type="submit" class="button button-primary" :disabled="isUploading || isSaving">
                  <span v-if="isSaving" class="button-loader"></span>
                  {{ isEditMode ? 'Actualizar Saga' : 'Crear Saga' }}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </transition>

  <!-- Modal para añadir producciones -->
  <div v-if="showAddProduccionModal" class="modal-overlay" @click.self="closeAddProduccionModal">
    <div class="modal-wrapper modal-sm">
      <div class="modal-card">
        <div class="modal-header">
          <h3 class="modal-title">Añadir Producción a la Saga</h3>
          <button class="modal-close-button" @click="closeAddProduccionModal">
            <i class="fas fa-times"></i>
          </button>
        </div>

        <div class="modal-content">
          <div class="search-container">
            <input
                v-model="produccionSearchQuery"
                @input="searchProducciones"
                type="text"
                class="form-input"
                placeholder="Buscar producciones por título..."
            >
          </div>

          <div v-if="searchLoading" class="loading-spinner">
            <i class="fas fa-spinner fa-spin"></i> Buscando...
          </div>

          <div v-else>
            <div v-if="searchResults.length === 0 && produccionSearchQuery" class="no-results">
              No se encontraron producciones con ese título
            </div>
            <div v-else-if="!produccionSearchQuery" class="no-results">
              Ingresa un término de búsqueda
            </div>

            <ul v-else class="search-results-list">
              <li v-for="produccion in searchResults" :key="produccion.id" class="search-result-item">
                <div class="produccion-info">
                  <span class="produccion-title">{{ produccion.titulo }}</span>
                  <span class="produccion-meta">
                    {{ formatTipoProduccion(produccion.tipo) }} • {{ formatYear(produccion.estreno) }}
                  </span>
                </div>
                <button
                    class="button-icon primary"
                    @click="addProduccionToSaga(produccion.id)"
                    title="Añadir a la saga"
                >
                  <i class="fas fa-plus"></i>
                </button>
              </li>
            </ul>

            <div class="pagination-controls" v-if="totalSearchPages > 1">
              <button
                  @click="prevSearchPage"
                  :disabled="currentSearchPage === 0"
                  class="pagination-button"
              >
                <i class="fas fa-chevron-left"></i>
              </button>
              <span class="page-info">
                Página {{ currentSearchPage + 1 }} de {{ totalSearchPages }}
              </span>
              <button
                  @click="nextSearchPage"
                  :disabled="currentSearchPage >= totalSearchPages - 1"
                  class="pagination-button"
              >
                <i class="fas fa-chevron-right"></i>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import ProduccionesService from '@/services/producciones.service';
import SagasService from '@/services/sagas.service';

export default {
  name: 'SagaModal',
  props: {
    show: {
      type: Boolean,
      required: true
    },
    saga: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      formData: {
        nombre: '',
        descripcion: '',
        isAcabada: false,
        fechaInicio: '',
        fechaFin: null,
        imagen: null
      },
      errors: {
        nombre: '',
        fechaInicio: ''
      },
      producciones: [],
      showAddProduccionModal: false,
      produccionSearchQuery: '',
      searchResults: [],
      searchLoading: false,
      searchTimeout: null,
      currentSearchPage: 0,
      totalSearchPages: 1,
      searchPageSize: 5,
      isUploading: false,
      isSaving: false,
      uploadError: null,
      selectedFile: null,
      previewImage: null
    };
  },
  computed: {
    isEditMode() {
      return !!this.saga;
    }
  },
  watch: {
    saga: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.formData = {
            nombre: newVal.nombre,
            descripcion: newVal.descripcion || '',
            isAcabada: newVal.isAcabada,
            fechaInicio: this.formatDateForInput(newVal.fechaInicio),
            fechaFin: newVal.fechaFin ? this.formatDateForInput(newVal.fechaFin) : null,
            imagen: newVal.imagen || null
          };
          this.producciones = [...newVal.producciones];
          this.previewImage = null;
          this.selectedFile = null;
        } else {
          this.resetForm();
        }
      }
    },
    'formData.isAcabada': {
      handler(newVal) {
        if (!newVal) {
          this.formData.fechaFin = null;
        }
      }
    }
  },
  methods: {
    formatDateForInput(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toISOString().split('T')[0];
    },
    resetForm() {
      this.formData = {
        nombre: '',
        descripcion: '',
        isAcabada: false,
        fechaInicio: '',
        fechaFin: null,
        imagen: null
      };
      this.errors = {
        nombre: '',
        fechaInicio: ''
      };
      this.producciones = [];
      this.previewImage = null;
      this.selectedFile = null;
      this.uploadError = null;
    },
    validateForm() {
      let isValid = true;
      this.errors = {nombre: '', fechaInicio: ''};

      if (!this.formData.nombre.trim()) {
        this.errors.nombre = 'El nombre es requerido';
        isValid = false;
      }

      if (!this.formData.fechaInicio) {
        this.errors.fechaInicio = 'La fecha de inicio es requerida';
        isValid = false;
      }

      return isValid;
    },
    close() {
      this.$emit('close');
    },
    async save() {
      if (!this.validateForm()) return;

      this.isSaving = true;
      try {
        const sagaData = {
          ...this.formData,
          fechaFin: this.formData.isAcabada ? this.formData.fechaFin : null
        };

        // Si hay un archivo seleccionado pero no se ha subido (para edición)
        if (this.selectedFile && this.isEditMode) {
          try {
            const imageUrl = await SagasService.uploadSagaImage(this.saga.id, this.selectedFile);
            sagaData.imagen = imageUrl;
          } catch (error) {
            console.error('Error uploading image:', error);
            this.$toast.error('Error al subir la imagen');
          }
        }

        const id = this.isEditMode ? this.saga.id : null;
        this.$emit('save', {id, data: sagaData});
      } catch (error) {
        console.error('Error saving saga:', error);
        this.$toast.error('Error al guardar la saga: ' + error.message);
      } finally {
        this.isSaving = false;
      }
    },
    handleImageError(e) {
      e.target.src = 'default-saga.jpg';
    },

    // Métodos para subida de imágenes
    triggerFileInput() {
      this.$refs.fileInput.click();
    },

    handleFileUpload(event) {
      const file = event.target.files[0];
      if (!file) return;

      this.selectedFile = file;
      this.uploadError = null;

      // Crear vista previa
      const reader = new FileReader();
      reader.onload = (e) => {
        this.previewImage = e.target.result;
      };
      reader.readAsDataURL(file);

      // Subir automáticamente si estamos en modo edición
      if (this.isEditMode) {
        this.uploadImage();
      }
    },

    async uploadImage() {
      if (!this.selectedFile || !this.isEditMode) return;

      this.isUploading = true;
      this.uploadError = null;

      try {
        const imageUrl = await SagasService.uploadSagaImage(this.saga.id, this.selectedFile);
        this.formData.imagen = imageUrl;
        this.$toast.success('Imagen subida correctamente');
        this.selectedFile = null; // Limpiar después de subir
      } catch (error) {
        console.error('Error uploading image:', error);
        this.uploadError = 'Error al subir la imagen. Inténtalo de nuevo.';
        this.$toast.error('Error al subir la imagen');
      } finally {
        this.isUploading = false;
      }
    },

    removeImage() {
      this.formData.imagen = null;
      this.previewImage = null;
      this.selectedFile = null;
      if (this.$refs.fileInput) {
        this.$refs.fileInput.value = '';
      }
    },

    // Métodos para gestión de producciones
    openAddProduccionModal() {
      this.showAddProduccionModal = true;
      this.produccionSearchQuery = '';
      this.searchResults = [];
      this.currentSearchPage = 0;
    },

    closeAddProduccionModal() {
      this.showAddProduccionModal = false;
    },

    async searchProducciones() {
      if (this.searchTimeout) {
        clearTimeout(this.searchTimeout);
      }

      this.searchTimeout = setTimeout(async () => {
        if (!this.produccionSearchQuery.trim()) {
          this.searchResults = [];
          this.totalSearchPages = 1;
          return;
        }

        this.searchLoading = true;
        try {
          const response = await ProduccionesService.fetchProducciones(
              {titulo: this.produccionSearchQuery},
              {page: this.currentSearchPage, size: this.searchPageSize}
          );

          // Filtrar producciones que ya están en la saga
          this.searchResults = response.data.filter(prod =>
              !this.producciones.some(p => p.id === prod.id)
          );
          this.totalSearchPages = response.totalPages;
        } catch (error) {
          console.error('Error searching producciones:', error);
          this.searchResults = [];
          this.$toast.error('Error al buscar producciones');
        } finally {
          this.searchLoading = false;
        }
      }, 500);
    },

    async addProduccionToSaga(produccionId) {
      try {
        await SagasService.addProduccionToSaga(this.saga.id, produccionId);

        // Encontrar la producción añadida en los resultados de búsqueda
        const addedProduccion = this.searchResults.find(p => p.id === produccionId);
        if (addedProduccion) {
          this.producciones.push(addedProduccion);
          // Eliminar de los resultados de búsqueda
          this.searchResults = this.searchResults.filter(p => p.id !== produccionId);
        }

        this.$toast.success('Producción añadida a la saga');
      } catch (error) {
        console.error('Error adding produccion to saga:', error);
        this.$toast.error('Error al añadir la producción a la saga');
      }
    },

    async removeProduccion(produccionId) {
      if (confirm('¿Estás seguro de que quieres eliminar esta producción de la saga?')) {
        try {
          await SagasService.removeProduccionFromSaga(this.saga.id, produccionId);
          this.producciones = this.producciones.filter(p => p.id !== produccionId);
          this.$toast.success('Producción eliminada de la saga');
        } catch (error) {
          console.error('Error removing produccion from saga:', error);
          this.$toast.error('Error al eliminar la producción de la saga');
        }
      }
    },

    nextSearchPage() {
      if (this.currentSearchPage < this.totalSearchPages - 1) {
        this.currentSearchPage++;
        this.searchProducciones();
      }
    },

    prevSearchPage() {
      if (this.currentSearchPage > 0) {
        this.currentSearchPage--;
        this.searchProducciones();
      }
    },

    formatTipoProduccion(tipo) {
      const tiposMap = {
        'PELICULA': 'Película',
        'SERIE': 'Serie',
        'DOCUMENTAL': 'Documental',
        'CORTOMETRAJE': 'Cortometraje'
      };
      return tiposMap[tipo] || tipo;
    },

    formatYear(dateString) {
      if (!dateString) return 'N/A';
      const date = new Date(dateString);
      return isNaN(date.getTime()) ? 'N/A' : date.getFullYear();
    }
  }
};
</script>

<style scoped>
/* Estilos base del modal */
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
  max-width: 700px;
  max-height: 90vh;
  margin: 1rem;
  overflow: hidden;
}

.modal-sm {
  max-width: 500px;
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

.form-row {
  display: flex;
  gap: 1rem;
}

.form-col {
  flex: 1;
}

.form-error {
  color: var(--danger-color);
  font-size: 0.85rem;
  margin-top: 0.25rem;
}

.checkbox-field {
  margin-top: 0.5rem;
}

.form-checkbox-item {
  display: flex;
  align-items: center;
}

.form-checkbox {
  width: 18px;
  height: 18px;
  margin-right: 0.75rem;
  cursor: pointer;
}

.form-checkbox-label {
  font-weight: normal;
  cursor: pointer;
  color: var(--modal-text);
}

/* Estilos para subida de imágenes */
.image-upload-container {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.upload-options {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  flex-wrap: wrap;
}

.file-input {
  display: none;
}

.file-name {
  font-size: 0.9rem;
  color: var(--text-secondary);
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.upload-status {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--primary-color);
}

.upload-loader {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(126, 91, 239, 0.2);
  border-top-color: var(--primary-color);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.upload-text {
  font-size: 0.9rem;
}

.upload-error {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--danger-color);
  font-size: 0.85rem;
}

.upload-error i {
  font-size: 1rem;
}

/* Vista previa de imagen */
.image-preview-container {
  margin-top: 0.5rem;
}

.image-preview-label {
  font-size: 0.9rem;
  color: var(--text-secondary);
  margin-bottom: 0.5rem;
  display: block;
}

.image-preview {
  position: relative;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  overflow: hidden;
  max-width: 300px;
  max-height: 300px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.image-preview img {
  width: 100%;
  height: auto;
  display: block;
}

.remove-image-button {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: rgba(239, 68, 68, 0.7);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
}

.remove-image-button:hover {
  background-color: var(--danger-color);
  transform: scale(1.1);
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
  position: relative;
}

.button-primary {
  background-color: var(--primary-color);
  color: white;
}

.button-primary:hover:not(:disabled) {
  background-color: var(--primary-hover);
  transform: translateY(-1px);
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.button-primary:disabled {
  background-color: var(--primary-disabled);
  cursor: not-allowed;
  opacity: 0.7;
}

.button-secondary {
  background-color: var(--secondary-bg);
  color: var(--secondary-text);
}

.button-secondary:hover:not(:disabled) {
  background-color: var(--secondary-hover);
}

.button-secondary:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.button-small {
  padding: 0.5rem 1rem;
  font-size: 0.85rem;
}

.button-icon {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
}

.button-icon.primary {
  background-color: rgba(126, 91, 239, 0.1);
  color: var(--primary-color);
}

.button-icon.primary:hover {
  background-color: rgba(126, 91, 239, 0.2);
}

.button-icon.danger {
  background-color: rgba(239, 68, 68, 0.1);
  color: var(--danger-color);
}

.button-icon.danger:hover {
  background-color: rgba(239, 68, 68, 0.2);
}

.button-loader {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-right: 8px;
}

/* Estilos para la sección de producciones */
.producciones-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.no-productions {
  color: var(--text-secondary);
  font-style: italic;
  padding: 0.5rem 0;
}

.producciones-list {
  list-style: none;
  padding: 0;
  margin: 0;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  overflow: hidden;
}

.produccion-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 1rem;
  border-bottom: 1px solid var(--border-color);
  background-color: var(--card-bg);
}

.produccion-item:last-child {
  border-bottom: none;
}

.produccion-info {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
}

.produccion-title {
  font-weight: 500;
}

.produccion-meta {
  font-size: 0.85rem;
  color: var(--text-secondary);
}

/* Estilos para el modal de búsqueda */
.search-container {
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
  max-height: 400px;
  overflow-y: auto;
}

.search-result-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 1rem;
  border-bottom: 1px solid var(--border-color);
}

.search-result-item:last-child {
  border-bottom: none;
}

/* Paginación */
.pagination-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-top: 1rem;
  padding-top: 1rem;
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

/* Animaciones */
@keyframes spin {
  to {
    transform: rotate(360deg);
  }
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
  --primary-color: #7e5bef;
  --primary-hover: #6d46e8;
  --primary-disabled: #a78bfa;
  --danger-color: #ef4444;
  --success-color: #10b981;
  --secondary-bg: #f1f5f9;
  --secondary-text: #64748b;
  --secondary-hover: #e2e8f0;
  --text-secondary: #64748b;
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
  --primary-color: #8b5cf6;
  --primary-hover: #7c3aed;
  --primary-disabled: #6d28d9;
  --secondary-bg: #334155;
  --secondary-text: #94a3b8;
  --secondary-hover: #475569;
  --text-secondary: #94a3b8;
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

  .form-row {
    flex-direction: column;
    gap: 1rem;
  }

  .modal-footer {
    flex-direction: column;
    gap: 0.75rem;
  }

  .button {
    width: 100%;
  }

  .image-preview {
    max-width: 100%;
  }

  .upload-options {
    flex-direction: column;
    align-items: flex-start;
  }

  .producciones-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.5rem;
  }
}
</style>