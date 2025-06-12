<template>
  <div class="profile-layout" :class="{'dark-mode': darkMode}">
    <!-- Botones flotantes -->
    <button class="floating-btn home-btn" @click="goHome">
      <i class="fas fa-home"></i>
    </button>
    <button class="floating-btn logout-btn" @click="logout">
      <i class="fas fa-sign-out-alt"></i>
    </button>

    <!-- Burbujas de fondo -->
    <div class="bubbles">
      <div v-for="(bubble, index) in bubbles" :key="index" class="bubble"
           :style="bubble.style"></div>
    </div>

    <div class="profile-container">
      <div class="profile-card">
        <!-- Botón de modo oscuro/diurno -->
        <button class="toggle-dark" @click="toggleDarkMode">
          <i :class="darkMode ? 'fas fa-sun' : 'fas fa-moon'" />
        </button>

        <!-- Encabezado del perfil -->
        <div class="profile-header text-center mb-4">
          <div class="avatar-container mb-3">
            <img :src="user.avatar || defaultAvatar" alt="Avatar" class="avatar-img">
            <button class="btn-edit-avatar" @click="showAvatarModal = true">
              <i class="fas fa-camera"></i>
            </button>
          </div>
          <h2 class="profile-title">{{ user.nombre }} {{ user.apellido }}</h2>
          <p class="profile-subtitle">
            <span class="badge" :class="user.rol === 'ADMINISTRADOR' ? 'bg-admin' : 'bg-user'">
              {{ user.rol === 'ADMINISTRADOR' ? 'Administrador' : 'Usuario' }}
            </span>
          </p>
          <p class="member-since">Miembro desde {{ formattedRegDate }}</p>
        </div>

        <!-- Información del usuario -->
        <div class="profile-info">
          <div class="info-section mb-4">
            <h4 class="section-title"><i class="fas fa-user me-2"></i>Información Personal</h4>
            <div class="info-grid">
              <div class="info-item">
                <label>Nombre de usuario</label>
                <p>{{ user.username }}</p>
              </div>
              <div class="info-item">
                <label>Email</label>
                <p>{{ user.email }}</p>
              </div>
              <div class="info-item">
                <label>Teléfono</label>
                <p>{{ user.telefono || 'No especificado' }}</p>
              </div>
              <div class="info-item">
                <label>Fecha de nacimiento</label>
                <p>{{ formattedBirthDate }}</p>
              </div>
            </div>
          </div>
        </div>

        <!-- Acciones -->
        <div class="profile-actions mt-4">
          <button class="btn btn-primary w-100 mb-3" @click="openEditModal">
            <i class="fas fa-edit me-2"></i>Editar perfil
          </button>
          <button v-if="isAdmin" class="btn btn-admin w-100" @click="adminPanel">
            <i class="fas fa-shield-alt me-2"></i>Panel de Administración
          </button>
        </div>
      </div>
    </div>

    <!-- Modal para editar perfil -->
    <div v-if="showEditModal" class="modal-overlay" @click.self="showEditModal = false">
      <div class="edit-modal">
        <div class="modal-header">
          <h3><i class="fas fa-user-edit me-2"></i>Editar Perfil</h3>
          <button class="btn-close" @click="showEditModal = false">
            <i class="fas fa-times"></i>
          </button>
        </div>

        <div class="modal-body">
          <form @submit.prevent="saveProfile">
            <div class="form-group">
              <label for="nombre">Nombre</label>
              <input type="text" id="nombre" v-model="editForm.nombre" :class="{'is-invalid': errors.nombre}">
              <div class="invalid-feedback" v-if="errors.nombre">{{ errors.nombre }}</div>
            </div>

            <div class="form-group">
              <label for="apellido">Apellido</label>
              <input type="text" id="apellido" v-model="editForm.apellido" >
            </div>

            <div class="form-group">
              <label for="telefono">Teléfono</label>
              <input type="tel" id="telefono" v-model="editForm.telefono" :class="{'is-invalid': errors.telefono}" >
              <div class="invalid-feedback" v-if="errors.telefono">{{ errors.telefono }}</div>
            </div>

            <div class="form-group">
              <label for="fechaNacimiento">Fecha de Nacimiento</label>
              <input type="date" id="fechaNacimiento" v-model="editForm.fechaNacimiento" >
            </div>

            <div class="form-actions">
              <button type="button" class="btn btn-cancel" @click="showEditModal = false">
                Cancelar
              </button>
              <button type="submit" class="btn btn-save" :disabled="isSaving">
                <span v-if="isSaving">
                  <i class="fas fa-spinner fa-spin me-2"></i>Guardando...
                </span>
                <span v-else>
                  <i class="fas fa-save me-2"></i>Guardar cambios
                </span>
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- Modal para editar avatar -->
    <div v-if="showAvatarModal" class="modal-overlay" @click.self="showAvatarModal = false">
      <div class="avatar-modal">
        <div class="modal-header">
          <h3>Editar foto de perfil</h3>
          <button class="btn-close" @click="showAvatarModal = false">
            <i class="fas fa-times"></i>
          </button>
        </div>

        <div class="modal-body">
          <!-- Selector de archivo -->
          <div class="upload-section" v-if="!selectedFile">
            <div class="upload-area" @dragover.prevent="dragOver" @drop.prevent="handleDrop">
              <input
                  type="file"
                  id="avatar-upload"
                  ref="fileInput"
                  accept="image/*"
                  @change="handleFileSelect"
                  class="file-input"
              >
              <label for="avatar-upload" class="upload-label">
                <i class="fas fa-cloud-upload-alt upload-icon"></i>
                <p>Arrastra una imagen o haz clic para seleccionar</p>
                <p class="small-text">Formatos soportados: JPG, PNG, GIF (Max. 5MB)</p>
              </label>
            </div>
          </div>

          <!-- Editor de imagen -->
          <div class="editor-section" v-else>
            <div class="preview-container">
              <div class="preview-title">Vista previa</div>
              <div class="preview-image" ref="previewContainer">
                <img :src="imagePreviewUrl" ref="imagePreview" class="original-image" :style="imageStyle">
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-cancel" @click="cancelSelection" v-if="selectedFile">
            <i class="fas fa-times me-2"></i>Cancelar
          </button>
          <button class="btn btn-save" @click="saveAvatar" :disabled="!selectedFile || isSavingAvatar">
            <template v-if="isSavingAvatar">
              <i class="fas fa-spinner fa-spin me-2"></i>Guardando...
            </template>
            <template v-else>
              <i class="fas fa-save me-2"></i>Guardar cambios
            </template>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { computed, onMounted, ref, nextTick } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { format } from 'date-fns';
import { es } from 'date-fns/locale';
import Cropper from 'cropperjs';
import userService from '@/services/users.service.js';

export default {
  name: 'ProfilePage',
  setup() {
    const authStore = useAuthStore();
    const router = useRouter();
    const darkMode = ref(false);
    const defaultAvatar = 'https://ui-avatars.com/api/?name=Usuario&background=7e5bef&color=fff';
    const bubbles = ref([]);

    // Estados para los modales
    const showEditModal = ref(false);
    const showAvatarModal = ref(false);

    // Formulario de edición
    const editForm = ref({
      nombre: '',
      apellido: '',
      telefono: '',
      fechaNacimiento: '',
      avatar: ''
    });

    const errors = ref({});
    const isSaving = ref(false);

    // Estados para el avatar
    const fileInput = ref(null);
    const previewContainer = ref(null);
    const imagePreview = ref(null);
    const selectedFile = ref(null);
    const imagePreviewUrl = ref('');
    const isSavingAvatar = ref(false);
    const zoom = ref(1);
    const rotation = ref(0);
    const imageStyle = ref({});

    // Cargar datos del usuario desde el store
    const user = computed(() => authStore.user || {});
    const isAdmin = computed(() => authStore.isAdmin);

    // Formateadores de fecha
    const formatDate = (dateString) => {
      if (!dateString) return 'No especificada';
      const date = new Date(dateString);
      return format(date, "d 'de' MMMM 'de' yyyy", { locale: es });
    };

    const formattedBirthDate = computed(() => formatDate(user.value.fechaNacimiento));
    const formattedRegDate = computed(() => formatDate(user.value.createdAt));

    // Métodos generales
    const loadDarkModePreference = () => {
      const savedMode = localStorage.getItem('darkMode');
      const systemPrefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
      darkMode.value = savedMode !== null ? savedMode === 'true' : systemPrefersDark;
      applyDarkMode();
    };

    const toggleDarkMode = () => {
      darkMode.value = !darkMode.value;
      localStorage.setItem('darkMode', darkMode.value);
      applyDarkMode();
    };

    const applyDarkMode = () => {
      if (darkMode.value) {
        document.documentElement.classList.add('dark');
      } else {
        document.documentElement.classList.remove('dark');
      }
    };

    const changePassword = () => router.push('/profile/change-password');
    const adminPanel = () => router.push('/admin');
    const goHome = () => router.push('/');
    const logout = () => {
      authStore.logout();
      router.push('/auth/login');
    };

    // Métodos para editar perfil
    const openEditModal = () => {
      editForm.value = {
        nombre: user.value.nombre || '',
        apellido: user.value.apellido || '',
        telefono: user.value.telefono || '',
        fechaNacimiento: user.value.fechaNacimiento ? user.value.fechaNacimiento.split('T')[0] : ''
      };
      showEditModal.value = true;
    };

    const validateForm = () => {
      errors.value = {};
      let isValid = true;

      if (!editForm.value.nombre.trim()) {
        errors.value.nombre = 'El nombre es requerido';
        isValid = false;
      }

      if (editForm.value.telefono && !/^[0-9+\- ]+$/.test(editForm.value.telefono)) {
        errors.value.telefono = 'Teléfono no válido';
        isValid = false;
      }

      return isValid;
    };

    const saveProfile = async () => {
      if (!validateForm()) return;

      isSaving.value = true;

      try {
        const updateData = {
          nombre: editForm.value.nombre,
          apellido: editForm.value.apellido,
          telefono: editForm.value.telefono,
          fechaNacimiento: editForm.value.fechaNacimiento,
          avatar: editForm.value.avatar || user.value.avatar
        };

        await userService.updateUser(authStore.user.id, updateData);
        await authStore.fetchCurrentUser();
        showEditModal.value = false;
      } catch (error) {
        console.error('Error updating profile:', error);
        if (error.response && error.response.data.errors) {
          errors.value = error.response.data.errors;
        }
      } finally {
        isSaving.value = false;
      }
    };

    const handleDrop = (e) => {
      e.preventDefault();
      const file = e.dataTransfer.files[0];
      if (file) {
        processFile(file);
      }
    };

    const dragOver = (e) => {
      e.preventDefault();
    };

// Quita todo lo de cropper y solo usa selectedFile
    const handleFileSelect = (e) => {
      const file = e.target.files[0];
      if (!file) return;

      if (!file.type.match('image.*')) {
        alert('Por favor, selecciona un archivo de imagen válido');
        return;
      }

      if (file.size > 5 * 1024 * 1024) {
        alert('La imagen no debe exceder los 5MB');
        return;
      }

      selectedFile.value = file;
      imagePreviewUrl.value = URL.createObjectURL(file);
    };

    const saveAvatar = async () => {
      if (!selectedFile.value) {
        alert('Selecciona una imagen primero');
        return;
      }

      isSavingAvatar.value = true;
      try {
        await userService.uploadAvatar(authStore.user.id, selectedFile.value);
        await authStore.fetchCurrentUser();
        showAvatarModal.value = false;
      } catch (error) {
        console.error('Error subiendo imagen:', error);
        alert('Ocurrió un error al subir la imagen');
      } finally {
        isSavingAvatar.value = false;
      }
    };

    // Crear burbujas animadas
    const createBubbles = () => {
      const bubbleCount = 15;
      for (let i = 0; i < bubbleCount; i++) {
        bubbles.value.push({
          style: {
            left: `${Math.random() * 100}%`,
            width: `${Math.random() * 20 + 10}px`,
            height: `${Math.random() * 20 + 10}px`,
            opacity: Math.random() * 0.5 + 0.1,
            animationDuration: `${Math.random() * 20 + 10}s`,
            animationDelay: `${Math.random() * 5}s`
          }
        });
      }
    };

    // Cargar datos iniciales
    onMounted(async () => {
      try {
        if (!authStore.isAuthenticated) {
          await authStore.checkAuth();
        }

        if (!authStore.user) {
          await authStore.fetchCurrentUser();
        }

        if (!authStore.isAuthenticated) {
          router.push('/auth/login');
        }

        loadDarkModePreference();
        createBubbles();
      } catch (error) {
        console.error('Error loading user:', error);
        router.push('/auth/login');
      }
    });

    // Dentro de setup, antes del return:
    const cancelSelection = () => {
      selectedFile.value = null;
      imagePreviewUrl.value = '';
    };

    return {
      darkMode,
      defaultAvatar,
      user,
      isAdmin,
      bubbles,
      formattedBirthDate,
      formattedRegDate,
      toggleDarkMode,
      changePassword,
      adminPanel,
      goHome,
      logout,

      // Editar perfil
      showEditModal,
      editForm,
      errors,
      isSaving,
      openEditModal,
      saveProfile,

      // Editar avatar
      showAvatarModal,
      fileInput,
      previewContainer,
      imagePreview,
      selectedFile,
      imagePreviewUrl,
      isSavingAvatar,
      zoom,
      rotation,
      imageStyle,
      handleFileSelect,
      handleDrop,
      dragOver,
      cancelSelection,
      saveAvatar
    };
  }
};
</script>

<style scoped>
/* Estilos existentes del perfil (se mantienen igual) */
.profile-layout,
.profile-container,
.profile-card,
.profile-title,
.profile-subtitle,
.member-since,
.info-item p {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.profile-layout {
  min-height: 100vh;
  transition: all 0.3s ease;
  background-color: #f8f9fa;
  position: relative;
  overflow: hidden;
}

.profile-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 2rem;
  background-image:
      linear-gradient(rgba(126, 91, 239, 0.1), rgba(126, 91, 239, 0.1)),
      url("https://images.unsplash.com/photo-1573614999645-e5f0f16ec15d?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
  background-attachment: fixed;
  background-size: cover;
  background-position: center;
  position: relative;
  z-index: 1;
}

.profile-card {
  width: 100%;
  max-width: 800px;
  padding: 2.5rem;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(126, 91, 239, 0.2);
  background-color: rgba(255, 255, 255, 0.95);
  position: relative;
  border: 1px solid rgba(126, 91, 239, 0.2);
  backdrop-filter: blur(5px);
  z-index: 2;
}

.toggle-dark {
  position: absolute;
  top: 1.5rem;
  right: 1.5rem;
  background: none;
  border: none;
  color: #7e5bef;
  cursor: pointer;
  font-size: 1.25rem;
  transition: all 0.3s ease;
  z-index: 10;
}

.toggle-dark:hover {
  transform: scale(1.1);
  color: #6d46e8;
}

.avatar-container {
  position: relative;
  width: 140px;
  height: 140px;
  margin: 0 auto;
}

.avatar-img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid #7e5bef;
  box-shadow: 0 5px 15px rgba(126, 91, 239, 0.4);
}

.btn-edit-avatar {
  position: absolute;
  bottom: 5px;
  right: 5px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #7e5bef;
  color: white;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-edit-avatar:hover {
  background-color: #6d46e8;
  transform: scale(1.1);
  box-shadow: 0 0 10px rgba(126, 91, 239, 0.5);
}

.profile-title {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 0.25rem;
  color: #5a4fcf;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.1);
}

.profile-subtitle {
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
}

.member-since {
  color: #6c757d;
  font-size: 0.9rem;
  margin-bottom: 1.5rem;
}

.badge {
  padding: 0.35rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 600;
}

.bg-user {
  background-color: #7e5bef;
  color: white;
}

.bg-admin {
  background-color: #ef476f;
  color: white;
}

.section-title {
  font-size: 1.25rem;
  margin-bottom: 1.5rem;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid rgba(126, 91, 239, 0.3);
  color: #7e5bef;
  display: flex;
  align-items: center;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 1.5rem;
}

.info-item {
  margin-bottom: 1rem;
}

.info-item label {
  font-size: 0.875rem;
  color: #6c757d;
  margin-bottom: 0.25rem;
  display: block;
  font-weight: 500;
}

.info-item p {
  margin: 0;
  font-weight: 500;
  color: #495057;
  background-color: rgba(248, 249, 250, 0.8);
  padding: 0.75rem;
  border-radius: 8px;
  border-left: 3px solid #7e5bef;
  box-shadow: 0 2px 5px rgba(126, 91, 239, 0.1);
}

.btn-primary {
  background-color: #7e5bef;
  border: none;
  padding: 0.75rem;
  font-weight: 600;
  transition: all 0.3s ease;
  letter-spacing: 0.5px;
}

.btn-primary:hover {
  background-color: #6d46e8;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(126, 91, 239, 0.4);
}

.btn-outline-secondary {
  border: 2px solid #7e5bef;
  color: #7e5bef;
  background-color: transparent;
  padding: 0.75rem;
  font-weight: 600;
  transition: all 0.3s ease;
  letter-spacing: 0.5px;
}

.btn-outline-secondary:hover {
  background-color: #7e5bef;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(126, 91, 239, 0.3);
}

.btn-admin {
  background-color: #ef476f;
  border: none;
  color: white;
  padding: 0.75rem;
  font-weight: 600;
  transition: all 0.3s ease;
  letter-spacing: 0.5px;
}

.btn-admin:hover {
  background-color: #dc1444;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(239, 71, 111, 0.3);
}

/* Botones flotantes */
.floating-btn {
  position: fixed;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.25rem;
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 100;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  border: none;
  color: white;
}

.floating-btn:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.3);
}

.home-btn {
  top: 30px;
  left: 30px;
  background-color: #7e5bef;
}

.logout-btn {
  top: 30px;
  right: 30px;
  background-color: #c547ef;
}

/* Animación de burbujas */
.bubbles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 0;
}

.bubble {
  position: absolute;
  bottom: -100px;
  background: rgba(126, 91, 239, 0.2);
  border-radius: 50%;
  animation: rise 15s infinite ease-in;
}

.bubble:nth-child(odd) {
  background: rgba(167, 139, 250, 0.3);
}

@keyframes rise {
  0% {
    bottom: -100px;
    transform: translateX(0);
  }
  50% {
    transform: translateX(100px);
  }
  100% {
    bottom: 100%;
    transform: translateX(-200px);
  }
}

/* Estilos para modo oscuro */
.dark-mode .profile-layout {
  background-color: #121212;
}

.dark-mode .profile-container {
  background-image:
      linear-gradient(rgba(18, 18, 18, 0.9), rgba(18, 18, 18, 0.9)),
      url("https://images.unsplash.com/photo-1573614999645-e5f0f16ec15d?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
}

.dark-mode .profile-card {
  background-color: rgba(30, 30, 30, 0.95);
  box-shadow: 0 10px 30px rgba(126, 91, 239, 0.2);
  border-color: rgba(167, 139, 250, 0.3);
}

.dark-mode .toggle-dark {
  color: #a78bfa;
}

.dark-mode .toggle-dark:hover {
  color: #8b5cf6;
}

.dark-mode .avatar-img {
  border-color: #a78bfa;
  box-shadow: 0 5px 15px rgba(167, 139, 250, 0.4);
}

.dark-mode .btn-edit-avatar {
  background-color: #a78bfa;
}

.dark-mode .btn-edit-avatar:hover {
  background-color: #8b5cf6;
}

.dark-mode .profile-title {
  color: #a78bfa;
}

.dark-mode .member-since {
  color: #adb5bd;
}

.dark-mode .section-title {
  color: #a78bfa;
  border-bottom-color: rgba(167, 139, 250, 0.4);
}

.dark-mode .info-item label {
  color: #adb5bd;
}

.dark-mode .info-item p {
  color: #e9ecef;
  background-color: rgba(33, 37, 41, 0.5);
  border-left-color: #a78bfa;
}

.dark-mode .btn-primary {
  background-color: #a78bfa;
}

.dark-mode .btn-primary:hover {
  background-color: #8b5cf6;
  box-shadow: 0 5px 15px rgba(167, 139, 250, 0.4);
}

.dark-mode .btn-outline-secondary {
  border-color: #a78bfa;
  color: #a78bfa;
}

.dark-mode .btn-outline-secondary:hover {
  background-color: #a78bfa;
  color: #212529;
}

.dark-mode .home-btn {
  background-color: #a78bfa;
}

.dark-mode .logout-btn {
  background-color: rgba(233, 71, 239, 0.85);
}

@media (max-width: 768px) {
  .profile-card {
    padding: 1.5rem;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }

  .floating-btn {
    width: 45px;
    height: 45px;
    font-size: 1.1rem;
  }
}

@media (max-width: 576px) {
  .profile-container {
    padding: 1rem;
  }

  .floating-btn {
    width: 40px;
    height: 40px;
    font-size: 1rem;
  }

  .home-btn {
    bottom: 20px;
    left: 20px;
  }

  .logout-btn {
    bottom: 20px;
    right: 20px;
  }
}

/* Estilos para los modales */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(5px);
}

.edit-modal,
.avatar-modal {
  background-color: white;
  border-radius: 12px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

.avatar-modal-lg {
  max-width: 800px;
}

.cropper-wrapper {
  height: 400px;
}

.upload-area-lg {
  height: 400px;
}

.modal-header {
  padding: 1.5rem;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  color: #333;
  font-size: 1.25rem;
  display: flex;
  align-items: center;
}

.btn-close {
  background: none;
  border: none;
  font-size: 1.25rem;
  color: #777;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-close:hover {
  color: #333;
  transform: rotate(90deg);
}

.modal-body {
  padding: 1.5rem;
  flex-grow: 1;
  overflow-y: auto;
}

/* Estilos específicos para el modal de edición */
.edit-modal .form-group {
  margin-bottom: 1.5rem;
}

.edit-modal label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #555;
}

.edit-modal input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.edit-modal input:focus {
  border-color: #7e5bef;
  box-shadow: 0 0 0 0.2rem rgba(126, 91, 239, 0.25);
  outline: none;
}

.is-invalid {
  border-color: #dc3545 !important;
}

.invalid-feedback {
  color: #dc3545;
  font-size: 0.8rem;
  margin-top: 0.25rem;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 2rem;
}

.btn {
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
}

.btn-cancel {
  background-color: #f8f9fa;
  border: 1px solid #ddd;
  color: #495057;
}

.btn-cancel:hover {
  background-color: #e9ecef;
}

.btn-save {
  background-color: #7e5bef;
  border: none;
  color: white;
}

.btn-save:hover {
  background-color: #6d46e8;
}

.btn-save:disabled {
  background-color: #b5a5f5;
  cursor: not-allowed;
}

/* Estilos específicos para el modal de avatar */
.upload-section {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}

.upload-area {
  border: 2px dashed #ccc;
  border-radius: 8px;
  width: 100%;
  padding: 2rem;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.upload-area:hover {
  border-color: #7e5bef;
  background-color: rgba(126, 91, 239, 0.05);
}

.file-input {
  display: none;
}

.upload-label {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
}

.upload-icon {
  font-size: 3rem;
  color: #7e5bef;
  margin-bottom: 1rem;
}

.small-text {
  font-size: 0.8rem;
  color: #777;
  margin-top: 0.5rem;
}

.editor-section {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.preview-container {
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
}

.preview-title {
  background-color: #f8f9fa;
  padding: 0.75rem;
  font-weight: 500;
  color: #555;
  border-bottom: 1px solid #eee;
}

.preview-image {
  width: 100%;
  height: 300px;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  background-color: #f5f5f5;
}

.original-image {
  max-width: 100%;
  max-height: 100%;
  transition: transform 0.2s ease;
}

.controls {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.zoom-control,
.rotation-control {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.zoom-control input[type="range"] {
  width: 100%;
  height: 8px;
  -webkit-appearance: none;
  background: #ddd;
  border-radius: 4px;
  outline: none;
}

.zoom-control input[type="range"]::-webkit-slider-thumb {
  -webkit-appearance: none;
  width: 18px;
  height: 18px;
  background: #7e5bef;
  border-radius: 50%;
  cursor: pointer;
}

.rotation-buttons {
  display: flex;
  gap: 1rem;
}

.rotation-buttons button {
  background-color: #f1f1f1;
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
}

.rotation-buttons button:hover {
  background-color: #7e5bef;
  color: white;
}

.modal-footer {
  padding: 1.5rem;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}

/* Modo oscuro para modales */
.dark-mode .edit-modal,
.dark-mode .avatar-modal {
  background-color: #1e1e1e;
  border: 1px solid #333;
}

.dark-mode .modal-header {
  border-bottom-color: #333;
}

.dark-mode .modal-header h3 {
  color: #e9ecef;
}

.dark-mode .btn-close {
  color: #aaa;
}

.dark-mode .btn-close:hover {
  color: #fff;
}

.dark-mode .edit-modal label {
  color: #ddd;
}

.dark-mode .edit-modal input {
  background-color: #333;
  border-color: #444;
  color: #fff;
}

.dark-mode .edit-modal input:focus {
  border-color: #a78bfa;
  box-shadow: 0 0 0 0.2rem rgba(167, 139, 250, 0.25);
}

.dark-mode .upload-area {
  border-color: #444;
}

.dark-mode .upload-area:hover {
  border-color: #a78bfa;
  background-color: rgba(167, 139, 250, 0.05);
}

.dark-mode .upload-icon {
  color: #a78bfa;
}

.dark-mode .small-text {
  color: #aaa;
}

.dark-mode .preview-container {
  border-color: #333;
}

.dark-mode .preview-title {
  background-color: #333;
  color: #ddd;
  border-bottom-color: #444;
}

.dark-mode .preview-image {
  background-color: #252525;
}

.dark-mode .zoom-control input[type="range"] {
  background: #444;
}

.dark-mode .zoom-control input[type="range"]::-webkit-slider-thumb {
  background: #a78bfa;
}

.dark-mode .rotation-buttons button {
  background-color: #333;
  color: #ddd;
}

.dark-mode .rotation-buttons button:hover {
  background-color: #a78bfa;
  color: #121212;
}

.dark-mode .modal-footer {
  border-top-color: #333;
}

.dark-mode .btn-cancel {
  background-color: #333;
  border-color: #444;
  color: #ddd;
}

.dark-mode .btn-cancel:hover {
  background-color: #444;
}

.dark-mode .btn-save {
  background-color: #a78bfa;
  color: #121212;
}

.dark-mode .btn-save:hover {
  background-color: #8b5cf6;
}

.dark-mode .btn-save:disabled {
  background-color: #5e4b9e;
}

@media (max-width: 576px) {
  .edit-modal,
  .avatar-modal {
    width: 95%;
  }

  .modal-header,
  .modal-body,
  .modal-footer {
    padding: 1rem;
  }

  .preview-image {
    height: 250px;
  }

  .modal-footer {
    flex-direction: column;
  }

  .btn {
    width: 100%;
    justify-content: center;
  }
}
</style>