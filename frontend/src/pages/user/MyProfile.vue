<template>
  <div class="profile-layout" :class="{'dark-mode': darkMode}">
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
            <button class="btn-edit-avatar" @click="openAvatarUpload">
              <i class="fas fa-camera"></i>
            </button>
          </div>
          <h2 class="profile-title">{{ user.nombre }}</h2>
          <p class="profile-subtitle">
            <span class="badge" :class="user.rol === 'ADMINISTRADOR' ? 'bg-admin.css' : 'bg-user'">
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
          <button class="btn btn-primary w-100 mb-3" @click="editProfile">
            <i class="fas fa-edit me-2"></i>Editar perfil
          </button>
          <button class="btn btn-outline-secondary w-100 mb-3" @click="changePassword">
            <i class="fas fa-lock me-2"></i>Cambiar contraseña
          </button>
          <button v-if="isAdmin" class="btn btn-admin w-100" @click="adminPanel">
            <i class="fas fa-shield-alt me-2"></i>Panel de Administración
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { computed, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { format } from 'date-fns';
import { es } from 'date-fns/locale';

export default {
  name: 'ProfilePage',
  setup() {
    const authStore = useAuthStore();
    const router = useRouter();
    const darkMode = ref(false);
    const defaultAvatar = 'https://ui-avatars.com/api/?name=Usuario&background=7e5bef&color=fff';

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

    // Métodos
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

    const editProfile = () => router.push('/profile/edit');
    const changePassword = () => router.push('/profile/change-password');
    const adminPanel = () => router.push('/admin.css');
    const openAvatarUpload = () => console.log('Subir nuevo avatar');

    // Cargar datos iniciales
    onMounted(async () => {
      try {
        if (!authStore.isAuthenticated) {
          await authStore.checkAuth();
        }

        if (!authStore.user) {
          await authStore.fetchCurrentUser();
        }

        // Redirigir si no está autenticado
        if (!authStore.isAuthenticated) {
          router.push('/auth/login');
        }
      } catch (error) {
        console.error('Error loading user:', error);
        router.push('/auth/login');
      }
    });

    return {
      darkMode,
      defaultAvatar,
      user,
      isAdmin,
      formattedBirthDate,
      formattedRegDate,
      toggleDarkMode,
      editProfile,
      changePassword,
      adminPanel,
      openAvatarUpload
    };
  }
};
</script>

<style scoped>
.profile-layout,
.profile-container,
.profile-card,
.profile-title,
.profile-subtitle,
.member-since,
.info-item p {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

/* Estilos base (modo diurno) */
.profile-layout {
  min-height: 100vh;
  transition: all 0.3s ease;
  background-color: #f8f9fa;
}

.profile-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 2rem;
  background-image:
      linear-gradient(rgba(26, 28, 28, 0.6), rgba(44, 43, 43, 0.65)),
      url("https://images.unsplash.com/photo-1573614999645-e5f0f16ec15d?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
  background-attachment: fixed;
  background-size: cover;
  background-position: center;
}

.profile-card {
  width: 100%;
  max-width: 800px;
  padding: 2.5rem;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.95);
  position: relative;
  border: 1px solid rgba(126, 91, 239, 0.1);
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
  box-shadow: 0 5px 15px rgba(126, 91, 239, 0.2);
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
}

.profile-title {
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 0.25rem;
  color: #343a40;
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
  background-color: #ef4444;
  color: white;
}

.section-title {
  font-size: 1.25rem;
  margin-bottom: 1.5rem;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid rgba(126, 91, 239, 0.2);
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
}

.btn-primary {
  background-color: #7e5bef;
  border: none;
  padding: 0.75rem;
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn-primary:hover {
  background-color: #6d46e8;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(126, 91, 239, 0.2);
}

.btn-outline-secondary {
  border: 2px solid #7e5bef;
  color: #7e5bef;
  background-color: transparent;
  padding: 0.75rem;
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn-outline-secondary:hover {
  background-color: #7e5bef;
  color: white;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(126, 91, 239, 0.2);
}

.btn-admin {
  background-color: #ef4444;
  border: none;
  color: white;
  padding: 0.75rem;
  font-weight: 600;
  transition: all 0.3s ease;
}

.btn-admin:hover {
  background-color: #dc2626;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(239, 68, 68, 0.2);
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
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
  border-color: rgba(167, 139, 250, 0.2);
}

.dark-mode .toggle-dark {
  color: #a78bfa;
}

.dark-mode .toggle-dark:hover {
  color: #8b5cf6;
}

.dark-mode .avatar-img {
  border-color: #a78bfa;
  box-shadow: 0 5px 15px rgba(167, 139, 250, 0.3);
}

.dark-mode .btn-edit-avatar {
  background-color: #a78bfa;
}

.dark-mode .btn-edit-avatar:hover {
  background-color: #8b5cf6;
}

.dark-mode .profile-title {
  color: #f8f9fa;
}

.dark-mode .member-since {
  color: #adb5bd;
}

.dark-mode .section-title {
  color: #a78bfa;
  border-bottom-color: rgba(167, 139, 250, 0.3);
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
  box-shadow: 0 5px 15px rgba(167, 139, 250, 0.3);
}

.dark-mode .btn-outline-secondary {
  border-color: #a78bfa;
  color: #a78bfa;
}

.dark-mode .btn-outline-secondary:hover {
  background-color: #a78bfa;
  color: #212529;
}

@media (max-width: 768px) {
  .profile-card {
    padding: 1.5rem;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 576px) {
  .profile-container {
    padding: 1rem;
  }
}
</style>