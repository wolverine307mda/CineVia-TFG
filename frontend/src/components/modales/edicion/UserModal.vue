<template>
  <transition name="modal-fade">
    <div v-if="show" class="modal-overlay" @click.self="close">
      <div class="modal-wrapper">
        <div class="modal-card">
          <div class="modal-header">
            <h3 class="modal-title">{{ isEditMode ? 'Editar Usuario' : 'Crear Nuevo Usuario' }}</h3>
            <button class="modal-close-button" @click="close">
              <i class="fas fa-times"></i>
            </button>
          </div>

          <div class="modal-content">
            <form @submit.prevent="save" class="modal-form">
              <div class="form-row">
                <div class="form-field form-col">
                  <label class="form-label">Nombre <span class="required">*</span></label>
                  <input
                      v-model="formData.nombre"
                      type="text"
                      class="form-input"
                      required
                      placeholder="Nombre del usuario"
                  >
                  <div class="form-error" v-if="errors.nombre">{{ errors.nombre }}</div>
                </div>

                <div class="form-field form-col">
                  <label class="form-label">Apellido</label>
                  <input
                      v-model="formData.apellido"
                      type="text"
                      class="form-input"
                      placeholder="Apellido del usuario"
                  >
                </div>
              </div>

              <div class="form-row">
                <div class="form-field form-col">
                  <label class="form-label">Nombre de usuario <span class="required">*</span></label>
                  <input v-model="formData.username" type="text"
                      class="form-input"
                      required
                      placeholder="Nombre de usuario único"
                      :disabled="isEditMode"
                  >
                  <div class="form-hint" v-if="usernameAvailable !== null" :style="{ color: usernameAvailable ? 'darkgreen' : 'darkred' }">
                    {{ usernameAvailable? '✔ Disponible': '✖ No disponible' }}
                  </div>
                  <div class="form-error" v-if="errors.username">{{ errors.username }}</div>
                </div>

                <div class="form-field form-col">
                  <label class="form-label">Email <span class="required">*</span></label>
                  <input v-model="formData.email" type="email" class="form-input" required placeholder="Email del usuario" :disabled="isEditMode" >
                  <div class="form-hint" v-if="emailAvailable !== null" :style="{ color: emailAvailable ? 'darkgreen' : 'darkred' }">
                    {{ emailAvailable? '✔ Disponible': '✖ No disponible' }}
                  </div>
                  <div class="form-error" v-if="errors.email">{{ errors.email }}</div>
                </div>
              </div>

              <div v-if="!isEditMode" class="form-row">
                <div class="form-field form-col">
                  <label class="form-label">Contraseña <span class="required">*</span></label>
                  <input
                      v-model="formData.password"
                      type="password"
                      class="form-input"
                      required
                      placeholder="Contraseña segura"
                  >
                  <div class="form-error" v-if="errors.password">{{ errors.password }}</div>
                </div>

                <div class="form-field form-col">
                  <label class="form-label">Confirmar Contraseña <span class="required">*</span></label>
                  <input
                      v-model="formData.passwordConfirm"
                      type="password"
                      class="form-input"
                      required
                      placeholder="Repite la contraseña"
                  >
                  <div class="form-error" v-if="errors.passwordConfirm">{{ errors.passwordConfirm }}</div>
                </div>
              </div>

              <div class="form-row">
                <div class="form-field form-col">
                  <label class="form-label">Fecha de Nacimiento</label>
                  <input v-model="formData.fechaNacimiento" type="date" class="form-input" >
                </div>
              </div>

              <div class="form-field checkbox-field" v-if="isAdmin">
                <div class="form-checkbox-item">
                  <input
                      id="isAdmin"
                      v-model="formData.rol"
                      type="checkbox"
                      class="form-checkbox"
                      true-value="ADMINISTRADOR"
                      false-value="USUARIO"
                      :disabled="formData.username === 'SuperAdmin'"
                  >
                  <label for="isAdmin" class="form-checkbox-label">Administrador</label>
                </div>
              </div>

              <div class="form-field" v-if="isEditMode">
                <label class="form-label">Avatar</label>
                <div class="avatar-upload-container">
                  <input
                      ref="avatarInput"
                      type="file"
                      class="form-input"
                      accept="image/*"
                      @change="handleAvatarUpload"
                      :disabled="isUploading"
                  />
                  <div class="upload-status" v-if="isUploading">
                    <div class="upload-loader"></div>
                    <span class="upload-text">Subiendo imagen...</span>
                  </div>
                  <div class="upload-error" v-if="uploadError">
                    <i class="fas fa-exclamation-circle"></i>
                    <span>{{ uploadError }}</span>
                  </div>
                </div>

                <div class="image-preview-container" v-if="previewImage || formData.avatar">
                  <span class="image-preview-label">Vista previa:</span>
                  <div class="image-preview">
                    <img
                        :src="previewImage || formData.avatar"
                        alt="Preview del avatar"
                        @error="handleImageError"
                    />
                  </div>
                </div>
              </div>

              <div class="modal-footer">
                <button type="button" class="button button-secondary" @click="close">
                  Cancelar
                </button>
                <button
                    type="submit"
                    class="button button-primary"
                    :disabled="isUploading || isSaving"
                >
                  <span v-if="isSaving" class="button-loader"></span>
                  {{ isEditMode ? 'Actualizar Usuario' : 'Crear Usuario' }}
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
import UsersService from '@/services/users.service';
import authService from "@/services/auth.service.js";
import { debounce } from 'lodash';

export default {
  name: 'UserModal',
  props: {
    show: {
      type: Boolean,
      required: true
    },
    user: {
      type: Object,
      default: null
    },
    isAdmin: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      formData: {
        username: '',
        email: '',
        password: '',
        passwordConfirm: '',
        nombre: '',
        apellido: '',
        rol: 'USUARIO',
        telefono: '',
        fechaNacimiento: '',
        avatar: null
      },
      errors: {
        username: '',
        email: '',
        password: '',
        passwordConfirm: '',
        nombre: ''
      },
      usernameAvailable: null,
      emailAvailable: null,
      checkAvailability: debounce(this.checkAvailabilityFields, 500),
      isUploading: false,
      isSaving: false,
      previewImage: null,
      uploadError: null
    };
  },
  computed: {
    isEditMode() {
      return !!this.user;
    }
  },
  watch: {
    user: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.formData = {
            username: newVal.username,
            email: newVal.email,
            password: '',
            passwordConfirm: '',
            nombre: newVal.nombre,
            apellido: newVal.apellido || '',
            rol: newVal.rol,
            telefono: newVal.telefono || '',
            fechaNacimiento: this.formatDateForInput(newVal.fechaNacimiento),
            avatar: newVal.avatar || null
          };
          this.usernameAvailable = true;
          this.emailAvailable = true;
        } else {
          this.resetForm();
        }
      }
    },
    'formData.username': {
      handler(newVal) {
        if (newVal && !this.isEditMode) {
          this.checkAvailability();
        }
      }
    },
    'formData.email': {
      handler(newVal) {
        if (newVal && !this.isEditMode) {
          this.checkAvailability();
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
        username: '',
        email: '',
        password: '',
        passwordConfirm: '',
        nombre: '',
        apellido: '',
        rol: 'USUARIO',
        telefono: '',
        fechaNacimiento: '',
        avatar: null
      };
      this.errors = {
        username: '',
        email: '',
        password: '',
        passwordConfirm: '',
        nombre: ''
      };
      this.usernameAvailable = null;
      this.emailAvailable = null;
      this.previewImage = null;
      this.uploadError = null;
    },
    async checkAvailabilityFields() {
      if (this.formData.username && !this.isEditMode) {
        this.usernameAvailable = await authService.checkUsernameAvailability(this.formData.username);
      }
      if (this.formData.email && !this.isEditMode) {
        this.emailAvailable = await authService.checkEmailAvailability(this.formData.email);
      }
    },
    async handleAvatarUpload(event) {
      const file = event.target.files[0];
      if (!file || !this.user?.id) return;

      // Reset previous state
      this.uploadError = null;
      this.isUploading = true;

      // Create preview
      const reader = new FileReader();
      reader.onload = (e) => {
        this.previewImage = e.target.result;
      };
      reader.readAsDataURL(file);

      try {
        const imageUrl = await UsersService.uploadAvatar(this.user.id, file);
        this.formData.avatar = imageUrl;
        this.$toast.success('Avatar actualizado correctamente');
      } catch (error) {
        console.error('Error uploading avatar:', error);
        this.uploadError = 'Error al subir el avatar. Inténtalo de nuevo.';
        this.$toast.error('Error al subir el avatar');
      } finally {
        this.isUploading = false;
      }
    },
    validateForm() {
      let isValid = true;
      this.errors = {
        username: '',
        email: '',
        password: '',
        passwordConfirm: '',
        nombre: ''
      };

      if (!this.formData.nombre.trim()) {
        this.errors.nombre = 'El nombre es requerido';
        isValid = false;
      }

      if (!this.formData.username.trim()) {
        this.errors.username = 'El nombre de usuario es requerido';
        isValid = false;
      } else if (!this.isEditMode && !this.usernameAvailable) {
        this.errors.username = 'El nombre de usuario no está disponible';
        isValid = false;
      }

      if (!this.formData.email.trim()) {
        this.errors.email = 'El email es requerido';
        isValid = false;
      } else if (!this.validateEmail(this.formData.email)) {
        this.errors.email = 'El email no es válido';
        isValid = false;
      } else if (!this.isEditMode && !this.emailAvailable) {
        this.errors.email = 'El email no está disponible';
        isValid = false;
      }

      if (!this.isEditMode) {
        if (!this.formData.password) {
          this.errors.password = 'La contraseña es requerida';
          isValid = false;
        } else if (this.formData.password.length < 6) {
          this.errors.password = 'La contraseña debe tener al menos 6 caracteres';
          isValid = false;
        }

        if (this.formData.password !== this.formData.passwordConfirm) {
          this.errors.passwordConfirm = 'Las contraseñas no coinciden';
          isValid = false;
        }
      }

      return isValid;
    },
    validateEmail(email) {
      const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return re.test(email);
    },
    close() {
      this.$emit('close');
      this.previewImage = null;
      this.uploadError = null;
    },
    async save() {
      if (!this.validateForm()) return;

      this.isSaving = true;
      try {
        let response;
        if (this.isEditMode) {
          response = await UsersService.updateUser(this.user.id, this.formData);
        } else {
          if (this.formData.rol === 'ADMINISTRADOR' && this.isAdmin) {
            response = await UsersService.createAdmin(this.formData);
          } else {
            response = await UsersService.createUser(this.formData);
          }
        }

        this.$emit('save', {
          id: response?.id || response?.data?.id,
          username: this.formData.username,
          email: this.formData.email,
          nombre: this.formData.nombre,
          apellido: this.formData.apellido,
          fechaNacimiento: this.formData.fechaNacimiento,
          rol: this.formData.rol,
          avatar: this.formData.avatar,
          isDeleted: false,
          createdAt: new Date().toISOString()
        });
        this.close();
      } catch (error) {
        console.error('Error saving user:', error);
        this.$toast.error('Error al guardar el usuario: ' + error.message);
      } finally {
        this.isSaving = false;
      }
    },
    handleImageError(e) {
      e.target.src = 'default-user.jpg';
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
  color: darkred;
  font-size: 0.85rem;
  margin-top: 0.25rem;
}

.form-hint {
  font-size: 0.85rem;
  margin-top: 0.25rem;
}

.form-hint[data-available="true"] {
  color: var(--success-color);
}

.form-hint[data-available="false"] {
  color: var(--danger-color);
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

/* Avatar upload styles */
.avatar-upload-container {
  position: relative;
}

.upload-status {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-top: 0.5rem;
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

.upload-error {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-top: 0.5rem;
  color: var(--danger-color);
  font-size: 0.85rem;
}

.upload-error i {
  font-size: 1rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Vista previa de imagen */
.image-preview-container {
  margin-top: 1rem;
}

.image-preview-label {
  font-size: 0.9rem;
  color: var(--text-secondary);
  margin-bottom: 0.5rem;
  display: block;
}

.image-preview {
  border: 1px solid var(--border-color);
  border-radius: 6px;
  overflow: hidden;
  max-width: 200px;
  max-height: 200px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.image-preview img {
  width: 100%;
  height: auto;
  display: block;
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

.button-secondary:hover {
  background-color: var(--secondary-hover);
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
  --danger-color: #ef4444;
  --success-color: #10b981;
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
}
</style>