<template>
  <div v-if="show" class="modal-overlay" @click.self="close">
    <div class="modal-wrapper">
      <div class="modal-card">
        <div class="modal-header">
          <h3 class="modal-title">{{ isEditMode ? 'Editar Rodaje' : 'Nuevo Rodaje' }}</h3>
          <button @click="close" class="modal-close-button">
            <i class="fas fa-times"></i>
          </button>
        </div>

        <div class="modal-content"><br>
          <form @submit.prevent="save" class="modal-form">
            <!-- Búsqueda de ubicación -->
            <div class="form-field">
              <label class="form-label">Ubicación <span class="required">*</span></label>
              <div style="position: relative; width: 100%;">
                <input
                    v-model="ubicacionSearchQuery"
                    @input="searchUbicaciones"
                    type="text"
                    class="form-input"
                    placeholder="Buscar ubicación por nombre..."
                    style="width: 100%; padding-right: 2.5rem;"
                >
                <i
                    class="fas fa-search"
                    style="
                      position: absolute;
                      right: 0.75rem;
                      top: 50%;
                      transform: translateY(-50%);
                      color: #888;
                      pointer-events: none;"
                ></i>
              </div>
              <div class="form-error" v-if="errors.ubicacion">{{ errors.ubicacion }}</div>
            </div>

            <!-- Lista de resultados -->
            <div v-if="ubicacionSearchQuery" class="search-results-container">
              <div v-if="searchLoading" class="loading-spinner">
                <i class="fas fa-spinner fa-spin"></i> Buscando...
              </div>

              <div v-else>
                <div v-if="filteredUbicaciones.length === 0" class="no-results">
                  No se encontraron ubicaciones con ese nombre
                </div>

                <ul v-else class="search-results-list">
                  <li
                      v-for="ubicacion in filteredUbicaciones"
                      :key="ubicacion.id"
                      class="search-result-item"
                      @click="selectUbicacion(ubicacion)"
                  >
                    <div class="ubicacion-info">
                      <div class="ubicacion-details">
                        <span class="ubicacion-name">{{ ubicacion.nombre }}</span>
                        <span class="ubicacion-meta">
                          {{ ubicacion.latitud }}, {{ ubicacion.longitud }}
                        </span>
                      </div>
                    </div>
                    <i class="fas fa-check-circle selected-icon" v-show="formData.ubicacionId === ubicacion.id"></i>
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

            <!-- Ubicación seleccionada -->
            <div v-if="selectedUbicacion" class="selected-ubicacion">
              <div class="ubicacion-info">
                <div class="ubicacion-details">
                  <span class="ubicacion-name">{{ selectedUbicacion.nombre }}</span>
                  <span class="ubicacion-meta">
                    {{ selectedUbicacion.latitud }}, {{ selectedUbicacion.longitud }}
                  </span>
                </div>
              </div>
            </div>

            <!-- Notas -->
            <div class="form-field">
              <label class="form-label">Notas</label>
              <textarea
                  v-model="formData.notas"
                  rows="3"
                  class="form-textarea"
                  placeholder="Información adicional sobre el rodaje..."
              ></textarea>
            </div>

            <!-- Subida de Imágenes -->
            <div class="form-field">
              <label class="form-label">Imágenes</label>
              <input type="file" accept="image/*" multiple @change="handleFileUpload" class="form-input">

              <div class="image-preview-list">
                <div v-for="(img, idx) in formData.imagenes" :key="idx" class="image-preview-item">
                  <img :src="img" alt="Imagen subida">
                  <button type="button" class="button-icon danger" @click="removeImage(idx)">
                    <i class="fas fa-trash"></i>
                  </button>
                </div>
              </div>
            </div>

            <div class="modal-footer">
              <button type="button" @click="close" class="button button-secondary">Cancelar</button>
              <button type="submit" class="button button-primary" :disabled="!formData.ubicacionId">
                {{ isEditMode ? 'Actualizar Rodaje' : 'Crear Rodaje' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import UbicacionesService from '@/services/ubicaciones.service';
import RodajesService from '@/services/rodajes.service';

export default {
  name: 'RodajeModal',
  props: {
    show: Boolean,
    rodaje: Object,
    produccionId: [String, Number]
  },
  data() {
    return {
      formData: {
        id: null,
        produccionId: null,
        ubicacionId: null,
        notas: null,
        imagenes: []
      },
      selectedUbicacion: null,
      errors: {
        ubicacion: ''
      },
      ubicacionSearchQuery: '',
      filteredUbicaciones: [],
      searchLoading: false,
      searchTimeout: null,
      currentSearchPage: 0,
      totalSearchPages: 1,
      searchPageSize: 5
    };
  },
  computed: {
    isEditMode() {
      return !!this.rodaje;
    }
  },
  watch: {
    rodaje: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.formData = {
            id: newVal.id,
            produccionId: this.produccionId,
            ubicacionId: newVal.ubicacion.id,
            notas: newVal.notas,
            imagenes: [...newVal.imagenes]
          };
          this.selectedUbicacion = {
            id: newVal.ubicacion.id,
            nombre: newVal.ubicacion.nombre,
            latitud: newVal.ubicacion.latitud,
            longitud: newVal.ubicacion.longitud
          };
          this.ubicacionSearchQuery = newVal.ubicacion.nombre;
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
    async handleFileUpload(event) {
      const files = Array.from(event.target.files);
      for (const file of files) {
        try {
          const url = await RodajesService.uploadTempRodajeImage(file);
          this.formData.imagenes.push(url);
        } catch (e) {
          console.error('Error subiendo imagen:', e);
          this.$toast.error('Error subiendo imagen');
        }
      }
    },
    removeImage(index) {
      this.formData.imagenes.splice(index, 1);
    },
    resetForm() {
      this.formData = {
        id: null,
        produccionId: this.produccionId,
        ubicacionId: null,
        notas: null,
        imagenes: []
      };
      this.selectedUbicacion = null;
      this.ubicacionSearchQuery = '';
      this.filteredUbicaciones = [];
      this.errors = {
        ubicacion: ''
      };
    },
    validateForm() {
      let isValid = true;
      this.errors = { ubicacion: '' };

      if (!this.formData.ubicacionId) {
        this.errors.ubicacion = 'Debes seleccionar una ubicación';
        isValid = false;
      }

      return isValid;
    },
    close() {
      this.$emit('close');
    },
    async save() {
      if (!this.validateForm()) return;

      try {
        const rodajeData = {
          id: this.formData.id,
          produccionId: this.produccionId,
          ubicacionId: this.formData.ubicacionId,
          notas: this.formData.notas,
          imagenes: this.formData.imagenes.filter(img => img.trim() !== '')
        };

        this.$emit('save', rodajeData);
        this.close();
      } catch (error) {
        console.error('Error al emitir rodaje:', error);
        this.$toast.error('Error al procesar el rodaje');
      }
    },
    addImagen() {
      this.formData.imagenes.push('');
    },
    removeImagen(index) {
      this.formData.imagenes.splice(index, 1);
    },
    async searchUbicaciones() {
      if (this.searchTimeout) {
        clearTimeout(this.searchTimeout);
      }

      this.searchTimeout = setTimeout(async () => {
        if (!this.ubicacionSearchQuery.trim()) {
          this.filteredUbicaciones = [];
          this.totalSearchPages = 1;
          return;
        }

        this.searchLoading = true;
        try {
          const response = await UbicacionesService.fetchUbicaciones(
              { nombre: this.ubicacionSearchQuery },
              { page: this.currentSearchPage, size: this.searchPageSize }
          );

          this.filteredUbicaciones = response.data;
          this.totalSearchPages = response.totalPages;
        } catch (error) {
          console.error('Error searching ubicaciones:', error);
          this.filteredUbicaciones = [];
          this.$toast.error('Error al buscar ubicaciones');
        } finally {
          this.searchLoading = false;
        }
      }, 500);
    },
    selectUbicacion(ubicacion) {
      this.formData.ubicacionId = ubicacion.id;
      this.selectedUbicacion = {
        id: ubicacion.id,
        nombre: ubicacion.nombre,
        latitud: ubicacion.latitud,
        longitud: ubicacion.longitud
      };
    },
    nextSearchPage() {
      if (this.currentSearchPage < this.totalSearchPages - 1) {
        this.currentSearchPage++;
        this.searchUbicaciones();
      }
    },
    prevSearchPage() {
      if (this.currentSearchPage > 0) {
        this.currentSearchPage--;
        this.searchUbicaciones();
      }
    }
  }
};
</script>

<style scoped>
/* Estilos similares al modal de participación */
.search-container {
  position: relative;
  margin-bottom: 0.5rem;
}

.search-icon {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  color: var(--text-secondary);
}

.search-results-container {
  border: 1px solid var(--border-color);
  border-radius: 6px;
  max-height: 300px;
  overflow-y: auto;
  margin-bottom: 1rem;
}

.search-results-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.search-result-item {
  padding: 0.75rem 1rem;
  border-bottom: 1px solid var(--border-color);
  cursor: pointer;
  transition: background-color 0.2s;
}

.search-result-item:hover {
  background-color: var(--hover-bg);
}

.ubicacion-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.ubicacion-name {
  font-weight: 500;
}

.ubicacion-meta {
  font-size: 0.85rem;
  color: var(--text-secondary);
}

.selected-icon {
  color: var(--primary-color);
  font-size: 1.25rem;
}

.selected-ubicacion {
  margin-bottom: 1rem;
  padding: 1rem;
  border: 1px solid var(--border-color);
  border-radius: 6px;
}

.loading-spinner {
  padding: 1rem;
  text-align: center;
  color: var(--text-secondary);
}

.no-results {
  padding: 1rem;
  text-align: center;
  color: var(--text-secondary);
  font-style: italic;
}

.image-input-container {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 0.5rem;
}

.image-input-container .form-input {
  flex-grow: 1;
}

.button-icon {
  width: 40px;
  height: 40px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  cursor: pointer;
}

.button-icon.danger {
  background-color: rgba(239, 68, 68, 0.1);
  color: var(--danger-color);
}

.button-icon.danger:hover {
  background-color: rgba(239, 68, 68, 0.2);
}

.button-small {
  padding: 0.5rem 1rem;
  font-size: 0.85rem;
}

/* Variables de tema */
:root {
  --border-color: #e2e8f0;
  --text-secondary: #64748b;
  --hover-bg: #f8fafc;
  --primary-color: #7e5bef;
  --danger-color: #ef4444;
}

.dark-mode {
  --border-color: #334155;
  --text-secondary: #94a3b8;
  --hover-bg: #1e293b;
}

.image-preview-list {
  display: flex;
  flex-wrap: wrap;
  gap: 0.75rem;
  margin-top: 0.5rem;
}
.image-preview-item {
  position: relative;
  width: 100px;
  height: 100px;
  border: 1px solid #ccc;
  border-radius: 6px;
  overflow: hidden;
}
.image-preview-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.image-preview-item .button-icon {
  position: absolute;
  top: 4px;
  right: 4px;
  background-color: rgba(0,0,0,0.5);
  color: white;
  border: none;
  padding: 4px;
  border-radius: 4px;
  cursor: pointer;
}

.pagination-controls {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
  margin: 1rem 0 0.5rem 0;
}

.pagination-button {
  background: var(--primary-color, #7e5bef);
  color: #fff;
  border: none;
  border-radius: 6px;
  padding: 0.5rem 0.9rem;
  font-size: 1rem;
  cursor: pointer;
  transition: background 0.2s, box-shadow 0.2s;
  box-shadow: 0 1px 2px rgba(0,0,0,0.04);
  display: flex;
  align-items: center;
  justify-content: center;
}

.pagination-button:disabled {
  background: #e2e8f0;
  color: #a0aec0;
  cursor: not-allowed;
  box-shadow: none;
}

.page-info {
  font-size: 1rem;
  color: var(--text-secondary, #64748b);
  min-width: 120px;
  text-align: center;
}
</style>

<style scoped src="@/assets/styles/modalesAdmin.css" />