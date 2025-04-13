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
          <h2 class="profile-title">{{ user.nombre }} {{ user.apellido }}</h2>
          <p class="profile-subtitle">
            <span class="badge" :class="user.rol === 'ADMINISTRADOR' ? 'bg-admin' : 'bg-user'">
              {{ user.rol === 'ADMINISTRADOR' ? 'Administrador' : 'Usuario' }}
            </span>
          </p>
          <p class="member-since">Miembro desde {{ formattedRegDate }}</p>
        </div>

        <!-- Sección de estadísticas -->
        <div class="stats-section mb-4">
          <div class="stats-grid">
            <div class="stat-item">
              <div class="stat-value">{{ userStats.favorites }}</div>
              <div class="stat-label">Favoritos</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ userStats.reviews }}</div>
              <div class="stat-label">Reseñas</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ userStats.comments }}</div>
              <div class="stat-label">Comentarios</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ userStats.pdfsGenerated }}</div>
              <div class="stat-label">PDFs generados</div>
            </div>
          </div>
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

          <div class="info-section mb-4">
            <h4 class="section-title"><i class="fas fa-film me-2"></i>Actividad en GeoFilm</h4>
            <div class="activity-grid">
              <div class="activity-item" v-if="recentFavorites.length > 0">
                <label>Últimos favoritos</label>
                <div class="favorites-list">
                  <div v-for="fav in recentFavorites" :key="fav.id" class="favorite-item">
                    <i :class="fav.tipo === 'PRODUCCION' ? 'fas fa-film' : 'fas fa-user'"></i>
                    {{ fav.titulo }}
                    <small class="text-muted">{{ formatDate(fav.fecha) }}</small>
                  </div>
                </div>
              </div>
              <div class="activity-item" v-if="recentReviews.length > 0">
                <label>Últimas reseñas</label>
                <div class="reviews-list">
                  <div v-for="review in recentReviews" :key="review.id" class="review-item">
                    <div class="review-rating">
                      <i v-for="n in 5" :key="n"
                         :class="n <= review.puntuacion ? 'fas fa-star' : 'far fa-star'"></i>
                    </div>
                    <div class="review-content">
                      "{{ truncateText(review.comentario, 50) }}"
                      <span class="review-location">@{{ review.ubicacionNombre }}</span>
                    </div>
                  </div>
                </div>
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
          <button v-if="user.rol === 'ADMINISTRADOR'" class="btn btn-admin w-100" @click="adminPanel">
            <i class="fas fa-shield-alt me-2"></i>Panel de Administración
          </button>
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
        username: 'cinemexplorer',
        nombre: 'María',
        apellido: 'García',
        email: 'maria.garcia@example.com',
        telefono: '+34 612 345 678',
        rol: 'USUARIO',
        fechaRegistro: '2023-05-15T10:30:00',
        fechaNacimiento: '1990-08-25T00:00:00',
        avatar: 'https://randomuser.me/api/portraits/women/44.jpg',
        updatedAt: '2023-11-20T15:45:00'
      },
      userStats: {
        favorites: 24,
        reviews: 8,
        comments: 15,
        pdfsGenerated: 3
      },
      recentFavorites: [
        { id: 1, tipo: 'PRODUCCION', titulo: 'El Señor de los Anillos', fecha: '2023-11-15T14:30:00' },
        { id: 2, tipo: 'PARTICIPANTE', titulo: 'Viggo Mortensen', fecha: '2023-11-10T09:15:00' },
        { id: 3, tipo: 'PRODUCCION', titulo: 'Juego de Tronos', fecha: '2023-11-05T18:45:00' }
      ],
      recentReviews: [
        { id: 1, puntuacion: 5, comentario: 'Increíble ubicación, exactamente como en la película. Las vistas son espectaculares.',
          ubicacionNombre: 'Hobbiton, Nueva Zelanda', fecha: '2023-11-12T16:20:00' },
        { id: 2, puntuacion: 4, comentario: 'El lugar es mágico aunque un poco masificado de turistas.',
          ubicacionNombre: 'Dubrovnik (Desembarco del Rey)', fecha: '2023-10-28T11:40:00' }
      ]
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
        day: 'numeric'
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
    formatDate(dateString) {
      return new Date(dateString).toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
      })
    },
    truncateText(text, length) {
      return text.length > length ? text.substring(0, length) + '...' : text
    },
    editProfile() {
      console.log('Editar perfil')
      // this.$router.push('/profile/edit')
    },
    changePassword() {
      console.log('Cambiar contraseña')
      // this.$router.push('/profile/change-password')
    },
    adminPanel() {
      console.log('Acceder al panel de administración')
      // this.$router.push('/admin')
    },
    openAvatarUpload() {
      console.log('Cambiar avatar')
      // Implementar lógica para subir nueva imagen
    }
  }
}
</script>

<style scoped>

.profile-layout,
.profile-container,
.profile-card,
.profile-title,
.profile-subtitle,
.member-since,
.stat-item,
.info-item p,
.favorite-item,
.review-item {
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

.stats-section {
  margin: 2rem 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1rem;
}

.stat-item {
  background-color: rgba(126, 91, 239, 0.05);
  border-radius: 10px;
  padding: 1rem;
  text-align: center;
  border: 1px solid rgba(126, 91, 239, 0.1);
  transition: all 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(126, 91, 239, 0.1);
}

.stat-value {
  font-size: 1.75rem;
  font-weight: 700;
  color: #7e5bef;
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 0.85rem;
  color: #6c757d;
  text-transform: uppercase;
  letter-spacing: 1px;
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

.activity-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 1.5rem;
}

@media (min-width: 992px) {
  .activity-grid {
    grid-template-columns: 1fr 1fr;
  }
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
.activity-item label {
  color: #abb1b6;
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

.favorites-list {
  display: flex;
  flex-direction: column;
  gap: 0.75rem;
}

.dark-mode .favorites-list i{
  color: #c4bbbb;
}

.favorite-item {
  background-color: rgba(126, 91, 239, 0.05);
  padding: 0.75rem;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 0.95rem;
  border-left: 3px solid #7e5bef;
}

.dark-mode .favorite-item{
  color: white;
}

.reviews-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.review-item {
  background-color: rgba(126, 91, 239, 0.05);
  padding: 1rem;
  border-radius: 8px;
}

.review-rating {
  color: #ffc107;
  margin-bottom: 0.5rem;
}

.review-content {
  font-style: italic;
  margin-bottom: 0.25rem;
}

.dark-mode .review-content {
  color: white;
}

.review-location {
  display: block;
  font-size: 0.85rem;
  color: #6c757d;
  font-style: normal;
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

.text-muted {
  color: #adb5bd !important;
  margin-left: auto;
  font-size: 0.8rem;
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

.dark-mode .stat-item {
  background-color: rgba(49, 27, 75, 0.3);
  border-color: rgba(167, 139, 250, 0.2);
}

.dark-mode .stat-item:hover {
  box-shadow: 0 5px 15px rgba(167, 139, 250, 0.2);
}

.dark-mode .stat-value {
  color: #a78bfa;
}

.dark-mode .stat-label {
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

.dark-mode .favorite-item {
  background-color: rgba(49, 27, 75, 0.3);
  border-left-color: #a78bfa;
}

.dark-mode .favorite-item i {
  color: #a78bfa;
}

.dark-mode .review-item {
  background-color: rgba(49, 27, 75, 0.3);
}

.dark-mode .review-location {
  color: #adb5bd;
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

.dark-mode .text-muted {
  color: #6c757d !important;
}

@media (max-width: 768px) {
  .profile-card {
    padding: 1.5rem;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .info-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 576px) {
  .profile-container {
    padding: 1rem;
  }

  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>