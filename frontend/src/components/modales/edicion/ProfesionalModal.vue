<template>
  <transition name="modal-fade">
    <div v-if="show" class="modal-overlay" @click.self="close">
      <div class="modal-wrapper">
        <div class="modal-card">
          <div class="modal-header">
            <h3 class="modal-title">{{ isEditMode ? 'Editar Profesional' : 'Nuevo Profesional' }}</h3>
            <button class="modal-close-button" @click="close">
              <i class="fas fa-times"></i>
            </button>
          </div>

          <div class="modal-content">
            <form @submit.prevent="save" class="modal-form">
              <div class="form-field">
                <label class="form-label">Nombre completo <span class="required">*</span></label>
                <input
                    v-model="formData.nombre"
                    type="text"
                    class="form-input"
                    required
                    placeholder="Ej: John Doe"
                >
                <div class="form-error" v-if="errors.nombre">{{ errors.nombre }}</div>
              </div>

              <div class="form-row">
                <div class="form-field form-col">
                  <label class="form-label">Fecha de nacimiento</label>
                  <input
                      v-model="formData.fechaNacimiento"
                      type="date"
                      class="form-input"
                  >
                </div>

                <div class="form-field form-col">
                  <label class="form-label">Fecha de inicio</label>
                  <input
                      v-model="formData.fechaInicio"
                      type="date"
                      class="form-input"
                  >
                </div>
              </div>

              <div class="form-field">
                <label class="form-label">Lugar de nacimiento</label>
                <input
                    v-model="formData.lugarNacimiento"
                    type="text"
                    class="form-input"
                    placeholder="Ciudad, País"
                >
              </div>

              <div class="form-field">
                <label class="form-label">Biografía</label>
                <textarea
                    v-model="formData.biografia"
                    rows="4"
                    class="form-textarea"
                    placeholder="Descripción detallada del profesional..."
                ></textarea>
              </div>

              <div class="form-field">
                <label class="form-label">Foto</label>
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

                  <div class="image-preview-container" v-if="previewImage || formData.foto">
                    <span class="image-preview-label">Vista previa:</span>
                    <div class="image-preview">
                      <img
                          :src="previewImage || formData.foto"
                          alt="Foto del profesional"
                          @error="handleImageError"
                      >
                      <button
                          v-if="(previewImage || formData.foto) && isEditMode"
                          type="button"
                          class="button-icon danger remove-image-button"
                          @click="removeImage"
                          title="Eliminar foto"
                          :disabled="isUploading"
                      >
                        <i class="fas fa-trash"></i>
                      </button>
                    </div>
                  </div>
                </div>
              </div>

              <div class="modal-footer">
                <button type="button" class="button button-secondary" @click="close" :disabled="isUploading || isSaving">
                  Cancelar
                </button>
                <button type="submit" class="button button-primary" :disabled="isUploading || isSaving">
                  <span v-if="isSaving" class="button-loader"></span>
                  {{ isEditMode ? 'Actualizar Profesional' : 'Guardar Profesional' }}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
import ProfesionalesService from '@/services/profesional.service';

export default {
  name: 'ProfesionalModal',
  props: {
    show: {
      type: Boolean,
      required: true
    },
    profesional: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      formData: {
        nombre: '',
        fechaNacimiento: '',
        fechaInicio: '',
        lugarNacimiento: '',
        biografia: '',
        foto: null
      },
      errors: {
        nombre: ''
      },
      isUploading: false,
      isSaving: false,
      uploadError: null,
      selectedFile: null,
      previewImage: null
    };
  },
  computed: {
    isEditMode() {
      return !!this.profesional;
    }
  },
  watch: {
    profesional: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.formData = {
            nombre: newVal.nombre,
            fechaNacimiento: this.formatDateForInput(newVal.fechaNacimiento),
            fechaInicio: this.formatDateForInput(newVal.fechaInicio),
            lugarNacimiento: newVal.lugarNacimiento || '',
            biografia: newVal.biografia || '',
            foto: newVal.foto || null
          };
          this.previewImage = null;
          this.selectedFile = null;
        } else {
          this.resetForm();
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
        fechaNacimiento: '',
        fechaInicio: '',
        lugarNacimiento: '',
        biografia: '',
        foto: null
      };
      this.errors = {
        nombre: ''
      };
      this.previewImage = null;
      this.selectedFile = null;
      this.uploadError = null;
    },
    validateForm() {
      let isValid = true;
      this.errors = {nombre: ''};

      if (!this.formData.nombre.trim()) {
        this.errors.nombre = 'El nombre es requerido';
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
        const profesionalData = {...this.formData};

        // Si hay un archivo seleccionado pero no se ha subido (para edición)
        if (this.selectedFile && this.isEditMode) {
          try {
            const imageUrl = await ProfesionalesService.uploadProfesionalImage(this.profesional.id, this.selectedFile);
            profesionalData.foto = imageUrl;
          } catch (error) {
            console.error('Error uploading image:', error);
            this.$toast.error('Error al subir la imagen');
          }
        }

        const id = this.isEditMode ? this.profesional.id : null;
        this.$emit('save', {id, data: profesionalData});
      } catch (error) {
        console.error('Error saving profesional:', error);
        this.$toast.error('Error al guardar el profesional: ' + error.message);
      } finally {
        this.isSaving = false;
      }
    },
    handleImageError(e) {
      e.target.src = 'default-profesional.jpg';
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

      const reader = new FileReader();
      reader.onload = (e) => {
        this.previewImage = e.target.result;
      };
      reader.readAsDataURL(file);

      if (this.isEditMode) {
        this.uploadImage();
      }
    },

    async uploadImage() {
      if (!this.selectedFile || !this.isEditMode) return;

      this.isUploading = true;
      this.uploadError = null;

      try {
        const imageUrl = await ProfesionalesService.uploadProfesionalImage(this.profesional.id, this.selectedFile);
        this.formData.foto = imageUrl;
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
      this.formData.foto = null;
      this.previewImage = null;
      this.selectedFile = null;
      if (this.$refs.fileInput) {
        this.$refs.fileInput.value = '';
      }
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

.button-loader {
  width: 16px;
  height: 16px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-right: 8px;
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
}
</style>