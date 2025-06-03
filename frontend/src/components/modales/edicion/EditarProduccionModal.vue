<template>
  <div v-if="show" class="modal-overlay">
    <div class="modal-wrapper">
      <div class="modal-card">
        <div class="modal-header">
          <h3 class="modal-title">{{ produccion ? 'Editar Producción' : 'Nueva Producción' }}</h3>
          <button @click="close" class="modal-close-button">
            <i class="fas fa-times"></i>
          </button>
        </div>

        <div class="modal-content">
          <div class="modal-tabs">
            <button
                @click="activeTab = 'info'"
                :class="['tab-button', { 'active': activeTab === 'info' }]"
            >
              Información
            </button>
            <button
                @click="activeTab = 'participaciones'"
                :class="['tab-button', { 'active': activeTab === 'participaciones' }]"
                :disabled="!formData.id"
            >
              Participaciones
            </button>
            <button
                @click="activeTab = 'rodajes'"
                :class="['tab-button', { 'active': activeTab === 'rodajes' }]"
                :disabled="!formData.id"
            >
              Rodajes
            </button>
          </div>

          <div v-if="activeTab === 'info'" class="modal-form">
            <div class="form-field">
              <label class="form-label">Título <span class="required">*</span></label>
              <input
                  v-model="formData.titulo"
                  type="text"
                  class="form-input"
                  required
                  :disabled="isUploading || isSaving"
              >
            </div>

            <div class="form-field">
              <label class="form-label">Tipo <span class="required">*</span></label>
              <select
                  v-model="formData.tipo"
                  class="form-select"
                  required
                  :disabled="isUploading || isSaving"
              >
                <option value="PELICULA">Película</option>
                <option value="SERIE">Serie</option>
                <option value="CORTO">Cortometraje</option>
              </select>
            </div>

            <div class="form-row">
              <div class="form-field form-col">
                <label class="form-label">Fecha de Estreno <span class="required">*</span></label>
                <input
                    v-model="formData.estreno"
                    type="date"
                    class="form-input"
                    required
                    :disabled="isUploading || isSaving"
                >
              </div>

              <div class="form-field form-col">
                <label class="form-label">Duración (minutos) <span class="required">*</span></label>
                <input
                    v-model="formData.duracion"
                    type="number"
                    min="1"
                    class="form-input"
                    required
                    :disabled="isUploading || isSaving"
                >
              </div>
            </div>

            <div class="form-field">
              <label class="form-label">Sinopsis <span class="required">*</span></label>
              <textarea
                  v-model="formData.sinopsis"
                  rows="3"
                  class="form-textarea"
                  required
                  :disabled="isUploading || isSaving"
              ></textarea>
            </div>

            <div class="form-field">
              <label class="form-label">Clasificación de Edad <span class="required">*</span></label>
              <select
                  v-model="formData.clasificacionEdad"
                  class="form-select"
                  required
                  :disabled="isUploading || isSaving"
              >
                <option v-for="clasificacion in clasificacionesEdad"
                        :key="clasificacion.valorNumerico"
                        :value="clasificacion.valorNumerico">
                  {{ clasificacion.valorNumerico }}+ - {{ clasificacion }}
                </option>
              </select>
            </div>

            <div class="form-field">
              <label class="form-label">Categorías</label>
              <div class="form-checkbox-group">
                <div v-for="categoria in categoriasDisponibles" :key="categoria" class="form-checkbox-item">
                  <input type="checkbox"
                         :id="'cat-'+categoria"
                         :value="categoria"
                         v-model="formData.categorias"
                         class="form-checkbox"
                         :disabled="isUploading || isSaving">
                  <label :for="'cat-'+categoria" class="form-checkbox-label">{{ categoria }}</label>
                </div>
              </div>
            </div>

            <div class="form-field" v-if="isEditMode">
              <label class="form-label">Imagen</label>
              <div class="image-upload-container">
                <div class="upload-options">
                  <input
                      ref="fileInput"
                      type="file"
                      accept="image/*"
                      @change="handleFileUpload"
                      class="file-input"
                      :disabled="isUploading || isSaving"
                  >
                  <button
                      type="button"
                      class="button button-small button-secondary"
                      @click="triggerFileInput"
                      :disabled="isUploading || isSaving"
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
                        alt="Imagen de la producción"
                        @error="handleImageError"
                    >
                    <button
                        v-if="(previewImage || formData.imagen) && isEditMode"
                        type="button"
                        class="button-icon danger remove-image-button"
                        @click="removeImage"
                        title="Eliminar imagen"
                        :disabled="isUploading || isSaving"
                    >
                      <i class="fas fa-trash"></i>
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <div class="form-field">
              <label class="form-label">Información Adicional</label>
              <textarea
                  v-model="formData.informacion"
                  rows="3"
                  class="form-textarea"
                  :disabled="isUploading || isSaving"
              ></textarea>
            </div>
          </div>

          <div v-if="activeTab === 'participaciones'" class="tab-content">
            <div class="section-header">
              <h4>Participaciones en esta producción</h4>
              <button @click="openParticipacionModal(null)" class="add-button" :disabled="isUploading || isSaving">
                <i class="fas fa-plus"></i> Añadir Participación
              </button>
            </div>

            <div v-if="loadingParticipaciones" class="loading-section">
              <i class="fas fa-spinner fa-spin"></i> Cargando participaciones...
            </div>

            <div v-else>
              <table class="data-table" v-if="participaciones.data.length > 0">
                <thead>
                <tr>
                  <th>Profesional</th>
                  <th>Rol</th>
                  <th>Papel</th>
                  <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="part in participaciones.data" :key="part.id">
                  <td>{{ part.profesional.nombre }}</td>
                  <td>{{ part.rol }}</td>
                  <td>{{ part.papel || '-' }}</td>
                  <td class="actions">
                    <button @click="openParticipacionModal(part)" class="btn-edit" :disabled="isUploading || isSaving">
                      <i class="fas fa-edit"></i>
                    </button>
                    <button @click="confirmDeleteParticipacion(part)" class="btn-delete" :disabled="isUploading || isSaving">
                      <i class="fas fa-trash"></i>
                    </button>
                  </td>
                </tr>
                </tbody>
              </table>
              <div v-else class="no-data">
                No hay participaciones registradas para esta producción.
              </div>

              <div class="pagination" v-if="participaciones.totalPages > 1">
                <button
                    @click="prevParticipacionesPage"
                    :disabled="participaciones.currentPage === 1 || isUploading || isSaving"
                >
                  <i class="fas fa-chevron-left"></i>
                </button>
                <span>Página {{ participaciones.currentPage }} de {{ participaciones.totalPages }}</span>
                <button
                    @click="nextParticipacionesPage"
                    :disabled="participaciones.currentPage >= participaciones.totalPages || isUploading || isSaving"
                >
                  <i class="fas fa-chevron-right"></i>
                </button>
              </div>
            </div>
          </div>

          <div v-if="activeTab === 'rodajes'" class="tab-content">
            <div class="section-header">
              <h4>Rodajes de esta producción</h4>
              <button @click="openRodajeModal(null)" class="add-button" :disabled="isUploading || isSaving">
                <i class="fas fa-plus"></i> Añadir Rodaje
              </button>
            </div>

            <div v-if="loadingRodajes" class="loading-section">
              <i class="fas fa-spinner fa-spin"></i> Cargando rodajes...
            </div>

            <div v-else>
              <table class="data-table" v-if="rodajes.data.length > 0">
                <thead>
                <tr>
                  <th>Ubicación</th>
                  <th>Coordenadas</th>
                  <th>Notas</th>
                  <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="rodaje in rodajes.data" :key="rodaje.id">
                  <td>{{ rodaje.ubicacion.nombre }}</td>
                  <td>{{ rodaje.ubicacion.latitud }}, {{ rodaje.ubicacion.longitud }}</td>
                  <td>{{ rodaje.notas || '-' }}</td>
                  <td class="actions">
                    <button @click="openRodajeModal(rodaje)" class="btn-edit" :disabled="isUploading || isSaving">
                      <i class="fas fa-edit"></i>
                    </button>
                    <button @click="confirmDeleteRodaje(rodaje)" class="btn-delete" :disabled="isUploading || isSaving">
                      <i class="fas fa-trash"></i>
                    </button>
                  </td>
                </tr>
                </tbody>
              </table>
              <div v-else class="no-data">
                No hay rodajes registrados para esta producción.
              </div>

              <div class="pagination" v-if="rodajes.totalPages > 1">
                <button
                    @click="prevRodajesPage"
                    :disabled="rodajes.currentPage === 1 || isUploading || isSaving"
                >
                  <i class="fas fa-chevron-left"></i>
                </button>
                <span>Página {{ rodajes.currentPage }} de {{ rodajes.totalPages }}</span>
                <button
                    @click="nextRodajesPage"
                    :disabled="rodajes.currentPage >= rodajes.totalPages || isUploading || isSaving"
                >
                  <i class="fas fa-chevron-right"></i>
                </button>
              </div>
            </div>
          </div>

          <div class="modal-footer" v-if="activeTab === 'info'">
            <button
                type="button"
                @click="close"
                class="button button-secondary"
                :disabled="isUploading || isSaving"
            >
              Cancelar
            </button>
            <button
                type="submit"
                @click="save"
                class="button button-primary"
                :disabled="isUploading || isSaving"
            >
              <span v-if="isSaving" class="button-loader"></span>
              {{ produccion ? 'Actualizar' : 'Guardar' }}
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal para Participaciones -->
    <ParticipacionModal
        v-if="showParticipacionModal && formData.id"
        :participacion="selectedParticipacion"
        :produccionId="formData.id"
        :participacionesExistentes="participaciones.data"
        :show="showParticipacionModal"
        @close="closeParticipacionModal"
        @save="handleSaveParticipacion"
    />

    <!-- Modal para Rodajes -->
    <RodajeModal
        v-if="showRodajeModal && formData.id"
        :rodaje="selectedRodaje"
        :produccionId="formData.id"
        :show="showRodajeModal"
        @close="closeRodajeModal"
        @save="handleSaveRodaje"
    />

    <!-- Modal de confirmación para eliminar -->
    <ConfirmModal
        v-if="showConfirmModal"
        :show="showConfirmModal"
        :title="confirmTitle"
        :message="confirmMessage"
        @confirm="handleConfirmDelete"
        @cancel="showConfirmModal = false"
    />
  </div>
</template>

<script>
import ParticipacionModal from './ParticipacionModal.vue';
import RodajeModal from './RodajeModal.vue';
import ConfirmModal from './ConfirmModal.vue';
import ProduccionesService from '@/services/producciones.service';
import ParticipacionesService from '@/services/participaciones.service';
import RodajesService from '@/services/rodajes.service';

export default {
  name: 'ProduccionModal',
  components: {
    ParticipacionModal,
    RodajeModal,
    ConfirmModal
  },
  props: {
    show: Boolean,
    produccion: Object
  },
  data() {
    return {
      activeTab: 'info',
      formData: {
        titulo: '',
        tipo: 'PELICULA',
        estreno: new Date().toISOString().split('T')[0],
        duracion: 90,
        sinopsis: '',
        imagen: null,
        informacion: null,
        categorias: [],
        clasificacionEdad: 0
      },
      // Participaciones
      participaciones: {
        data: [],
        currentPage: 1,
        totalPages: 1,
        totalItems: 0
      },
      loadingParticipaciones: false,
      showParticipacionModal: false,
      selectedParticipacion: null,
      // Rodajes
      rodajes: {
        data: [],
        currentPage: 1,
        totalPages: 1,
        totalItems: 0
      },
      loadingRodajes: false,
      showRodajeModal: false,
      selectedRodaje: null,
      // Confirmación
      showConfirmModal: false,
      confirmTitle: '',
      confirmMessage: '',
      itemToDelete: null,
      deleteType: null,
      // Subida de imágenes
      isUploading: false,
      isSaving: false,
      uploadError: null,
      selectedFile: null,
      previewImage: null,
      // Datos estáticos
      clasificacionesEdad: [
        { valorNumerico: 0, descripcion: 'Para todos los públicos' },
        { valorNumerico: 7, descripcion: 'Mayores de 7 años' },
        { valorNumerico: 12, descripcion: 'Mayores de 12 años' },
        { valorNumerico: 16, descripcion: 'Mayores de 16 años' },
        { valorNumerico: 18, descripcion: 'Mayores de 18 años' }
      ],
      categoriasDisponibles: [
        'Acción', 'Aventura', 'Comedia', 'Drama', 'Terror',
        'Ciencia Ficción', 'Fantasía', 'Romance', 'Documental',
        'Animación', 'Suspense', 'Crimen', 'Misterio'
      ]
    };
  },
  computed: {
    isEditMode() {
      return !!this.produccion;
    }
  },
  watch: {
    produccion: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.formData = {
            id: newVal.id,
            titulo: newVal.titulo,
            tipo: newVal.tipo,
            estreno: new Date(newVal.estreno).toISOString().split('T')[0],
            duracion: newVal.duracion,
            sinopsis: newVal.sinopsis,
            imagen: newVal.imagen || null,
            informacion: newVal.informacion || null,
            categorias: [...newVal.categorias],
            clasificacionEdad: newVal.clasificacionEdad?.valorNumerico || 0
          };

          // Cargar participaciones y rodajes si ya tiene ID
          if (newVal.id) {
            this.fetchParticipaciones();
            this.fetchRodajes();
          }
        } else {
          this.resetForm();
        }
      }
    },
    activeTab(newTab) {
      if (newTab === 'participaciones' && this.formData.id) {
        this.fetchParticipaciones();
      } else if (newTab === 'rodajes' && this.formData.id) {
        this.fetchRodajes();
      }
    }
  },
  methods: {
    resetForm() {
      this.formData = {
        titulo: '',
        tipo: 'PELICULA',
        estreno: new Date().toISOString().split('T')[0],
        duracion: 90,
        sinopsis: '',
        imagen: null,
        informacion: null,
        categorias: [],
        clasificacionEdad: 0
      };
      this.participaciones = {
        data: [],
        currentPage: 1,
        totalPages: 1,
        totalItems: 0
      };
      this.rodajes = {
        data: [],
        currentPage: 1,
        totalPages: 1,
        totalItems: 0
      };
      this.activeTab = 'info';
      this.selectedFile = null;
      this.previewImage = null;
      this.uploadError = null;
    },
    close() {
      this.$emit('close');
    },
    async save() {
      this.isSaving = true;
      try {
        const produccionData = { ...this.formData };

        // Si hay un archivo seleccionado pero no se ha subido (para edición)
        if (this.selectedFile && this.isEditMode) {
          try {
            const imageUrl = await ProduccionesService.uploadProduccionImage(this.produccion.id, this.selectedFile);
            produccionData.imagen = imageUrl;
          } catch (error) {
            console.error('Error uploading image:', error);
            this.$toast.error('Error al subir la imagen');
          }
        }

        if (this.isEditMode) {
          await ProduccionesService.updateProduccion(this.produccion.id, produccionData);
          this.$toast.success('Producción actualizada correctamente');
        } else {
          const result = await ProduccionesService.createProduccion(produccionData);
          produccionData.id = result.id;

          // Si hay una imagen para subir en nueva producción
          if (this.selectedFile) {
            try {
              const imageUrl = await ProduccionesService.uploadProduccionImage(result.id, this.selectedFile);
              await ProduccionesService.updateProduccion(result.id, { imagen: imageUrl });
              produccionData.imagen = imageUrl;
            } catch (error) {
              console.error('Error uploading image:', error);
              this.$toast.error('Error al subir la imagen');
            }
          }

          this.$toast.success('Producción creada correctamente');
        }

        this.$emit('save', produccionData);
      } catch (error) {
        console.error('Error saving produccion:', error);
        this.$toast.error('Error al guardar la producción: ' + error.message);
      } finally {
        this.isSaving = false;
      }
    },
    handleImageError(e) {
      e.target.src = 'default-produccion.jpg';
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
        const imageUrl = await ProduccionesService.uploadProduccionImage(this.produccion.id, this.selectedFile);
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

    // Métodos para Participaciones
    async fetchParticipaciones() {
      if (!this.formData.id) return;

      this.loadingParticipaciones = true;
      try {
        const page = isNaN(this.participaciones.currentPage - 1)
            ? 0
            : this.participaciones.currentPage - 1;

        const result = await ParticipacionesService.getParticipacionesByProduccion(
            this.formData.id,
            { page, size: 10 }
        );

        this.participaciones = {
          data: result.data,
          currentPage: result.currentPage + 1,
          totalPages: result.totalPages,
          totalItems: result.totalItems
        };
      } catch (error) {
        console.error('Error al cargar participaciones:', error);
        this.$toast.error('Error al cargar las participaciones');
      } finally {
        this.loadingParticipaciones = false;
      }
    },
    openParticipacionModal(participacion) {
      if (!this.formData.id) {
        this.$toast.error('Debes guardar la producción antes de añadir participaciones.');
        return;
      }
      this.selectedParticipacion = participacion ? { ...participacion } : null;
      this.showParticipacionModal = true;
    },
    closeParticipacionModal() {
      this.showParticipacionModal = false;
      this.selectedParticipacion = null;
    },
    async handleSaveParticipacion(participacionData) {
      try {
        if (participacionData.id) {
          await ParticipacionesService.updateParticipacion(participacionData.id, participacionData);
          this.$toast.success('Participación actualizada correctamente');
        } else {
          await ParticipacionesService.createParticipacion(participacionData);
          this.$toast.success('Participación creada correctamente');
        }
        this.fetchParticipaciones();
      } catch (error) {
        console.error('Error al guardar participación:', error);
        this.$toast.error('Error al guardar la participación');
      } finally {
        this.closeParticipacionModal();
      }
    },
    confirmDeleteParticipacion(participacion) {
      this.itemToDelete = participacion;
      this.deleteType = 'participacion';
      this.confirmTitle = 'Confirmar eliminación';
      this.confirmMessage = `¿Estás seguro de eliminar la participación de ${participacion.profesional.nombre}?`;
      this.showConfirmModal = true;
    },

    // Métodos para Rodajes
    async fetchRodajes() {
      if (!this.formData.id) return;

      this.loadingRodajes = true;
      try {
        const page = isNaN(this.rodajes.currentPage - 1)
            ? 0
            : this.rodajes.currentPage - 1;

        const result = await RodajesService.getRodajesByProduccion(
            this.formData.id,
            { page, size: 10 }
        );

        this.rodajes = {
          data: result.data,
          currentPage: result.currentPage + 1,
          totalPages: result.totalPages,
          totalItems: result.totalItems
        };
      } catch (error) {
        console.error('Error al cargar rodajes:', error);
        this.$toast.error('Error al cargar los rodajes');
      } finally {
        this.loadingRodajes = false;
      }
    },
    openRodajeModal(rodaje) {
      if (!this.formData.id) {
        this.$toast.error('Debes guardar la producción antes de añadir rodajes.');
        return;
      }
      this.selectedRodaje = rodaje ? { ...rodaje } : null;
      this.showRodajeModal = true;
    },
    closeRodajeModal() {
      this.showRodajeModal = false;
      this.selectedRodaje = null;
    },
    async handleSaveRodaje(rodajeData) {
      try {
        rodajeData.produccionId = this.formData.id;

        if (rodajeData.id) {
          await RodajesService.updateRodaje(rodajeData.id, rodajeData);
          this.$toast.success('Rodaje actualizado correctamente');
        } else {
          await RodajesService.createRodaje(rodajeData);
          this.$toast.success('Rodaje creado correctamente');
        }
        this.fetchRodajes();
      } catch (error) {
        console.error('Error al guardar rodaje:', error);
        this.$toast.error('Error al guardar el rodaje');
      } finally {
        this.closeRodajeModal();
      }
    },
    confirmDeleteRodaje(rodaje) {
      this.itemToDelete = rodaje;
      this.deleteType = 'rodaje';
      this.confirmTitle = 'Confirmar eliminación';
      this.confirmMessage = `¿Estás seguro de eliminar el rodaje de ${rodaje.pelicula.titulo}?`;
      this.showConfirmModal = true;
    },

    // Métodos de paginación
    nextParticipacionesPage() {
      if (this.participaciones.currentPage < this.participaciones.totalPages) {
        this.participaciones.currentPage++;
        this.fetchParticipaciones();
      }
    },
    prevParticipacionesPage() {
      if (this.participaciones.currentPage > 1) {
        this.participaciones.currentPage--;
        this.fetchParticipaciones();
      }
    },
    nextRodajesPage() {
      if (this.rodajes.currentPage < this.rodajes.totalPages) {
        this.rodajes.currentPage++;
        this.fetchRodajes();
      }
    },
    prevRodajesPage() {
      if (this.rodajes.currentPage > 1) {
        this.rodajes.currentPage--;
        this.fetchRodajes();
      }
    },

    // Manejo de confirmación de eliminación
    async handleConfirmDelete() {
      try {
        if (this.deleteType === 'participacion') {
          await ParticipacionesService.deleteParticipacion(this.itemToDelete.id);
          this.$toast.success('Participación eliminada correctamente');
          this.fetchParticipaciones();
        } else if (this.deleteType === 'rodaje') {
          await RodajesService.deleteRodaje(this.itemToDelete.id);
          this.$toast.success('Rodaje eliminado correctamente');
          this.fetchRodajes();
        }
      } catch (error) {
        console.error('Error al eliminar:', error);
        this.$toast.error('Error al eliminar');
      } finally {
        this.showConfirmModal = false;
        this.itemToDelete = null;
        this.deleteType = null;
      }
    }
  }
};
</script>

<style scoped>
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
</style>
<style scoped src="@/assets/styles/modalesAdmin.css" />