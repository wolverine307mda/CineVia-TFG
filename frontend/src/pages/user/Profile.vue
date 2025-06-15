<template>
  <div class="profile-layout" :class="{'dark-mode': darkMode}">
    <div class="profile-container">
      <div class="profile-card">
        <!-- Botón de modo oscuro/diurno -->
        <button class="toggle-dark" @click="toggleDarkMode">
          <i :class="darkMode ? 'bi bi-sun-fill' : 'bi bi-moon-fill'"></i>
        </button>

        <!-- Encabezado del perfil -->
        <div class="profile-header text-center mb-4">
          <div class="avatar-container mb-3">
            <img :src="user.avatar || defaultAvatar" alt="Avatar" class="avatar-img">
            <button class="btn-edit-avatar" @click="openAvatarUpload">
              <i class="bi bi-camera-fill"></i>
            </button>
          </div>
          <h2 class="profile-title">{{ user.nombre }} {{ user.apellido }}</h2>
          <p class="profile-subtitle">{{ user.rol === 'ADMINISTRADOR' ? 'Administrador' : 'Usuario' }}</p>
        </div>

        <!-- Información del usuario -->
        <div class="profile-info">
          <div class="info-section mb-4">
            <h4 class="section-title"><i class="bi bi-person-fill me-2"></i>Información Personal</h4>
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

          <div class="info-section mb-4">
            <h4 class="section-title"><i class="bi bi-calendar-event me-2"></i>Datos de la cuenta</h4>
            <div class="info-grid">
              <div class="info-item">
                <label>Fecha de registro</label>
                <p>{{ formattedRegDate }}</p>
              </div>
              <div class="info-item">
                <label>Última actualización</label>
                <p>{{ formattedUpdateDate }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProfilePage',
  data() {
    return {
      darkMode: false,
      defaultAvatar: 'https://ui-avatars.com/api/?name=' + encodeURIComponent('Usuario') + '&background=7e5bef&color=fff',
      user: {
        id: '550e8400-e29b-41d4-a716-446655440000',
        username: 'johndoe',
        nombre: 'John',
        apellido: 'Doe',
        email: 'john.doe@example.com',
        telefono: '+34 612 345 678',
        rol: 'USUARIO',
        fechaRegistro: '2023-05-15T10:30:00',
        fechaNacimiento: '1990-08-25T00:00:00',
        avatar: 'https://randomuser.me/api/portraits/men/32.jpg',
        updatedAt: '2023-11-20T15:45:00'
      }
    }
  },
  computed: {
    formattedBirthDate() {
      if (!this.user.fechaNacimiento) return 'No especificada'
      return new Date(this.user.fechaNacimiento).toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      })
    },
    formattedRegDate() {
      return new Date(this.user.fechaRegistro).toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      })
    },
    formattedUpdateDate() {
      if (!this.user.updatedAt) return 'No disponible'
      return new Date(this.user.updatedAt).toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      })
    }
  },
  created() {
    this.loadDarkModePreference()
    // Aquí iría la llamada al backend para cargar los datos reales del usuario
    // this.loadUserData()
  },
  methods: {
    loadDarkModePreference() {
      const savedMode = localStorage.getItem('darkMode')
      const systemPrefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches
      this.darkMode = savedMode !== null ? savedMode === 'true' : systemPrefersDark
      this.applyDarkMode()
    },
    toggleDarkMode() {
      this.darkMode = !this.darkMode
      localStorage.setItem('darkMode', this.darkMode)
      this.applyDarkMode()
    },
    applyDarkMode() {
      if (this.darkMode) {
        document.documentElement.classList.add('dark')
      } else {
        document.documentElement.classList.remove('dark')
      }
    },
    loadUserData() {
      // Ejemplo de llamada al backend:
      // axios.get('/api/user/profile')
      //   .then(response => {
      //     this.user = response.data
      //   })
      //   .catch(error => {
      //     console.error('Error al cargar datos del usuario:', error)
      //   })
    },
    editProfile() {
      console.log('Editar perfil')
      // this.$router.push('/profile/edit')
    },
    changePassword() {
      console.log('Cambiar contraseña')
      // this.$router.push('/profile/change-password')
    },
    openAvatarUpload() {
      console.log('Cambiar avatar')
      // Implementar lógica para subir nueva imagen
    }
  }
}
</script>

<style scoped>
.profile-layout {
  min-height: 100vh;
  transition: all 0.3s ease;
}

.profile-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 2rem;
  background-image: url("https://images.unsplash.com/photo-1573614999645-e5f0f16ec15d?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
  background-attachment: fixed;
  background-size: cover;
  background-position: center;
}

.profile-card {
  width: 100%;
  max-width: 700px;
  padding: 2.5rem;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.98);
  position: relative;
}

.dark-mode .profile-card {
  background-color: rgba(30, 30, 30, 0.98);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
  border-color: rgba(167, 139, 250, 0.2);
  color: #e2e8f0;
}

.toggle-dark {
  position: absolute;
  top: 1.5rem;
  right: 1.5rem;
  background: none;
  border: none;
  color: #64748b;
  cursor: pointer;
  font-size: 1.25rem;
  transition: all 0.3s ease;
  z-index: 10;
}

.dark-mode .toggle-dark {
  color: #a78bfa;
}

.toggle-dark:hover {
  transform: scale(1.1);
}

.avatar-container {
  position: relative;
  width: 120px;
  height: 120px;
  margin: 0 auto;
}

.avatar-img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  border: 3px solid #7e5bef;
}

.dark-mode .avatar-img {
  border-color: #a78bfa;
}

.btn-edit-avatar {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: #7e5bef;
  color: white;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.dark-mode .btn-edit-avatar {
  background-color: #a78bfa;
}

.profile-title {
  font-size: 1.75rem;
  font-weight: 700;
  margin-bottom: 0.25rem;
  color: #1e293b;
}

.dark-mode .profile-title {
  color: #f8fafc;
}

.profile-subtitle {
  color: #64748b;
  font-size: 1rem;
}

.dark-mode .profile-subtitle {
  color: #94a3b8;
}

.section-title {
  font-size: 1.25rem;
  margin-bottom: 1rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #e2e8f0;
  color: #7e5bef;
}

.dark-mode .section-title {
  color: #a78bfa;
  border-bottom-color: #4338ca;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 1.5rem;
}

.info-item {
  margin-bottom: 0.5rem;
}

.info-item label {
  font-size: 0.875rem;
  color: #64748b;
  margin-bottom: 0.25rem;
  display: block;
}

.dark-mode .info-item label {
  color: #94a3b8;
}

.info-item p {
  margin: 0;
  font-weight: 500;
  color: #1e293b;
}

.dark-mode .info-item p {
  color: #e2e8f0;
}

@media (max-width: 768px) {
  .profile-card {
    padding: 1.5rem;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>