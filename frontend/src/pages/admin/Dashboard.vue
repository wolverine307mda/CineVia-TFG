<template>
  <div class="dashboard-container" :class="{ 'dark-mode': darkMode }">
    <div class="dashboard-content">
      <!-- Estadísticas -->
      <div class="stats-grid">
        <div class="stat-card" v-for="stat in stats" :key="stat.title">
          <div class="stat-content">
            <div class="stat-icon">
              <i :class="stat.icon"></i>
            </div>
            <div class="stat-info">
              <h3>{{ stat.loading ? '...' : stat.value }}</h3>
              <p>{{ stat.title }}</p>
            </div>
          </div>
          <div class="stat-progress" v-if="!stat.loading">
            <div class="progress-bar" :style="{ width: stat.progress + '%' }"></div>
          </div>
          <div class="stat-loading" v-else>
            <div class="loading-bar"></div>
          </div>
        </div>
      </div>

      <!-- Sección principal -->
      <div class="main-content">
        <!-- Mapa y Producciones -->
        <div class="content-section">
          <div class="map-card">
            <div class="map-container">
              <div ref="mapContainer" class="google-map" v-show="!loadingLocations && locations.length > 0"></div>
              <div v-if="loadingLocations" class="loading-overlay">
                <div class="spinner"></div>
                <p>Cargando ubicaciones...</p>
              </div>
              <div v-if="!loadingLocations && locations.length === 0" class="empty-state">
                <i class="fas fa-map-marked-alt"></i>
                <p>No hay ubicaciones disponibles</p>
                <button @click="fetchLocations" class="btn-primary">Recargar</button>
              </div>
            </div>
          </div>

          <div class="users-card">
            <div class="card-header">
              <h3><i class="fas fa-users"></i> Usuarios Recientes</h3>
              <router-link style="text-decoration: none" to="/admin/usuarios" class="btn-primary" v-if="isAdmin">
                <i class="fas fa-plus"></i> Gestionar usuarios
              </router-link>
            </div>
            <div class="users-list">
              <div v-if="loadingUsers" class="loading-users">
                <i class="fas fa-spinner fa-spin"></i>
                <span>Cargando usuarios...</span>
              </div>
              <div v-else-if="recentUsers.length === 0" class="empty-users">
                <i class="fas fa-user-slash"></i>
                <span>No se encontraron usuarios</span>
              </div>
              <div v-for="user in recentUsers" :key="user.id" class="user-item">
                <div class="user-avatar">
                  <img :src="user.avatar" :alt="user.nombre" @error="handleAvatarError">
                </div>
                <div class="user-info">
                  <h4>{{ user.nombre }} {{ user.apellido }}</h4>
                  <p>{{ user.email }}</p>
                  <span class="user-role" :class="user.rol.toLowerCase()">{{ formatRole(user.rol) }}</span>
                </div>
                <div class="user-actions">
                  <button v-if="isAdmin && !user.isDeleted" @click="confirmAction('softDelete', user.id, 'Desactivar usuario', '¿Estás seguro de que quieres desactivar este usuario?')" class="icon-button deactivate-button" title="Desactivar">
                    <i class="fas fa-user-slash"></i>
                  </button>
                  <button v-if="isAdmin && user.isDeleted" @click="confirmAction('restore', user.id, 'Activar usuario', '¿Estás seguro de que quieres activar este usuario?')" class="icon-button activate-button" title="Activar">
                    <i class="fas fa-user-check"></i>
                  </button>
                  <button v-if="isAdmin && user.rol !== 'ADMINISTRADOR'" @click="confirmAction('delete', user.id, 'Eliminar usuario', '¿Estás seguro de que quieres eliminar permanentemente este usuario?')" class="icon-button delete-button" title="Eliminar">
                    <i class="fas fa-trash-alt"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Sección de Usuarios y Estadísticas -->
        <div class="content-section">
          <div class="productions-card">
            <div class="card-header">
              <h3><i class="fas fa-film"></i> Últimas Producciones</h3>
              <router-link to="/admin/producciones" class="view-all">
                Ver todo <i class="fas fa-chevron-right"></i>
              </router-link>
            </div>
            <div class="productions-list">
              <div v-for="production in recentProductions" :key="production.id" class="production-item">
                <div class="production-poster">
                  <img :src="production.imagen" :alt="production.titulo" class="production-image" @error="handleImageError">
                </div>
                <div class="production-details">
                  <h4>{{ production.titulo }}</h4>
                  <div class="production-meta">
                    <span class="badge" :class="production.tipo.toLowerCase()">{{ formatTipoProduccion(production.tipo) }}</span>
                    <span class="year">{{ production.estreno ? new Date(production.estreno).getFullYear() : 'N/A' }}</span>
                    <span class="duration" v-if="production.duracion"><i class="far fa-clock"></i> {{ production.duracion }} min</span>
                  </div>
                  <div class="production-rating">
                    <i class="fas fa-star"></i> {{ production.puntuacion?.toFixed(1) || 'N/A' }}
                  </div>
                  <div class="production-categories">
                    <span v-for="(category, index) in production.categorias" :key="index" class="category-tag">
                      {{ formatCategoria(category) }}
                    </span>
                  </div>
                </div>
                <div class="production-actions">
                  <router-link :to="`/produccion/${production.id}`" class="action-btn">
                    <i class="fas fa-eye"></i> ver
                  </router-link>
                </div>
              </div>
            </div>
          </div>

          <div class="stats-card">
            <div class="card-header">
              <h3><i class="fas fa-chart-pie"></i> Resumen</h3>
            </div>
            <div class="stats-summary">
              <div class="summary-item">
                <div class="summary-icon">
                  <i class="fas fa-layer-group"></i>
                </div>
                <div class="summary-info">
                  <h4>{{ sagasCount }}</h4>
                  <p>Sagas registradas</p>
                </div>
              </div>
              <div class="summary-item">
                <div class="summary-icon">
                  <i class="fas fa-user-tie"></i>
                </div>
                <div class="summary-info">
                  <h4>{{ profesionalesCount }}</h4>
                  <p>Profesionales</p>
                </div>
              </div>
              <div class="summary-item">
                <div class="summary-icon">
                  <i class="fas fa-film"></i>
                </div>
                <div class="summary-info">
                  <h4>{{ produccionesCount }}</h4>
                  <p>Producciones</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <ConfirmationModal
      v-if="showConfirmationModal"
      :show="showConfirmationModal"
      :title="confirmationTitle"
      :message="confirmationMessage"
      :is-loading="isProcessingAction"
      @confirm="executeAction"
      @cancel="showConfirmationModal = false"
  />
</template>

<script>
import ProduccionesService from '@/services/producciones.service.js';
import ProfesionalesService from '@/services/profesional.service.js';
import SagasService from '@/services/sagas.service.js';
import UsersService from '@/services/users.service';
import { useAuthStore } from '@/stores/auth';
import ConfirmationModal from "@/components/modales/edicion/ConfirmModal.vue";
import ubicacionesService from "@/services/ubicaciones.service.js";
import {getGoogleMapsLoader} from "@/utils/googleMapsLoader.js";

export default {
  name: 'Dashboard',
  components: {ConfirmationModal},
  data() {
    return {
      darkMode: false,
      stats: [
        { title: 'Producciones', icon: 'fas fa-film', value: 0, loading: true, progress: 0 },
        { title: 'Profesionales', icon: 'fas fa-users', value: 0, loading: true, progress: 0 },
        { title: 'Sagas', icon: 'fas fa-eye', value: 0, loading: true, progress: 0 },
        { title: 'Ubicaciones', icon: 'fas fa-map-marker-alt', value: 0, loading: true, progress: 0 }
      ],
      recentProductions: [],
      locations: [],
      loadingLocations: false,
      loadingUsers: false,
      recentUsers: [],
      map: null,
      markers: [],
      showUserModal: false,
      showConfirmationModal: false,
      selectedUser: null,
      actionToConfirm: null,
      actionParams: null,
      confirmationTitle: '',
      confirmationMessage: '',
      produccionesCount: 0,
      profesionalesCount: 0,
      sagasCount: 0,
      ubicacionesCount: 0,
      newAdmin: {
        name: '',
        email: '',
        password: ''
      },
    }
  },
  computed: {
    authStore() {
      return useAuthStore();
    },
    isAdmin() {
      return this.authStore.isAdmin;
    }
  },
  methods: {
    toggleDarkMode() {
      this.darkMode = !this.darkMode;
      if (this.map) {
        this.map.setOptions({
          styles: this.darkMode
        });
      }
    },
    confirmAction(action, id, title, message) {
      this.actionToConfirm = action;
      this.actionParams = id;
      this.confirmationTitle = title;
      this.confirmationMessage = message;
      this.showConfirmationModal = true;
    },
    async executeAction() {
      this.isProcessingAction = true;
      try {
        let success = false;
        let message = '';

        switch (this.actionToConfirm) {
          case 'delete':
            success = await UsersService.deleteUser(this.actionParams);
            message = 'Usuario eliminado permanentemente';
            break;
          case 'softDelete':
            success = await UsersService.softDeleteUser(this.actionParams);
            message = 'Usuario desactivado correctamente';
            break;
          case 'restore':
            success = await UsersService.restoreUser(this.actionParams);
            message = 'Usuario activado correctamente';
            break;
        }

        if (success) {
          this.$toast.success(message);
          this.fetchUsers();
        }
      } catch (error) {
        console.error('Error performing action:', error);
        this.$toast.error(error.message || 'Error al realizar la acción');
      } finally {
        this.isProcessingAction = false;
        this.showConfirmationModal = false;
        this.actionToConfirm = null;
        this.actionParams = null;
      }
    },
    async fetchStats() {
      try {
        // Obtener conteo de producciones
        const produccionesRes = await ProduccionesService.fetchProducciones({}, { page: 0, size: 1 });
        this.stats[0].value = produccionesRes.totalItems;
        this.stats[0].loading = false;
        this.stats[0].progress = Math.min(100, produccionesRes.totalItems / 20 * 100);
        this.produccionesCount = produccionesRes.totalItems;

        // Obtener conteo de profesionales
        const profesionalesRes = await ProfesionalesService.fetchProfessionals({}, { page: 0, size: 1 });
        this.stats[1].value = profesionalesRes.totalItems;
        this.stats[1].loading = false;
        this.stats[1].progress = Math.min(100, profesionalesRes.totalItems / 20 * 100);
        this.profesionalesCount = profesionalesRes.totalItems;

        // Obtener conteo de sagas
        const sagasRes = await SagasService.fetchSagas({}, { currentPage: 1, itemsPerPage: 1 });
        this.stats[2].value = sagasRes.totalItems;
        this.stats[2].loading = false;
        this.stats[2].progress = Math.min(100, sagasRes.totalItems / 20 * 100);
        this.sagasCount = sagasRes.totalItems || 0;

        // Obtener conteo de ubicaciones
        const ubicacionesRes = await ubicacionesService.fetchUbicaciones({}, { page: 0, size: 1 });
        this.stats[3].value = ubicacionesRes.totalItems;
        this.stats[3].loading = false;
        this.stats[3].progress = Math.min(100, ubicacionesRes.totalItems / 20 * 100);
        this.ubicacionesCount = ubicacionesRes.totalItems || 0;

      } catch (error) {
        console.error('Error fetching stats:', error);
        this.stats.forEach(stat => {
          stat.loading = false;
          stat.value = 0;
          stat.progress = 0;
        });
      }
    },

    async fetchRecentProductions() {
      try {
        const res = await ProduccionesService.fetchProducciones(
            {},
            { page: 0, size: 4, sortBy: 'estreno', sortDirection: 'desc' }
        );
        this.recentProductions = res.data;
      } catch (error) {
        console.error('Error fetching recent productions:', error);
        this.recentProductions = [];
      }
    },

    async fetchRecentUsers() {
      this.loadingUsers = true;
      try {
        const response = await UsersService.fetchUsers(
            { isDeleted: false }, // Solo usuarios activos
            { currentPage: 1, itemsPerPage: 6 }, // Solo 6 usuarios
            { field: 'createdAt', direction: 'desc' } // Ordenados por fecha de creación
        );

        this.recentUsers = response.content;
        // Actualizar el contador de usuarios
      } catch (error) {
        console.error('Error fetching recent users:', error);
        this.recentUsers = [];
      } finally {
        this.loadingUsers = false;
      }
    },

    async fetchLocations() {
      this.loadingLocations = true;
      this.locations = [];

      try {
        const response = await fetch(`${import.meta.env.VITE_API_URL}/api/ubicaciones/all`);

        if (!response.ok) {
          throw new Error(`Error HTTP: ${response.status}`);
        }

        const data = await response.json();

        this.locations = data.filter(location =>
            location.id &&
            location.nombre &&
            !isNaN(location.latitud) &&
            !isNaN(location.longitud)
        ).map(location => ({
          ...location,
          latitud: Number(location.latitud),
          longitud: Number(location.longitud)
        }));

        if (this.locations.length > 0) {
          await this.initMap();
        }

      } catch (error) {
        console.error('Error al cargar ubicaciones:', error);
      } finally {
        this.loadingLocations = false;
      }
    },

    async initMap() {
      try {
        if (!this.$refs.mapContainer) {
          throw new Error('El contenedor del mapa no está disponible');
        }

        const loader = getGoogleMapsLoader();

        await loader.load();

        const mapOptions = {
          center: { lat: 20, lng: 0 },
          zoom: 2,
          mapId: import.meta.env.VITE_GOOGLE_MAP_ID,
          mapTypeControl: true,
          styles: this.darkMode ? this.darkMapStyle : []
        };

        this.map = new google.maps.Map(this.$refs.mapContainer, mapOptions);
        this.clearMarkers();

        this.locations.forEach(location => {
          const marker = new google.maps.Marker({
            position: { lat: location.latitud, lng: location.longitud },
            map: this.map,
            title: location.nombre
          });

          this.markers.push(marker);
        });

        if (this.markers.length > 0) {
          const bounds = new google.maps.LatLngBounds();
          this.markers.forEach(marker => bounds.extend(marker.getPosition()));
          this.map.fitBounds(bounds, { top: 50, right: 50, bottom: 50, left: 50 });

          // Ajustar centro ligeramente hacia abajo para evitar el vacío superior
          google.maps.event.addListenerOnce(this.map, 'bounds_changed', () => {
            const currentZoom = this.map.getZoom();
            const maxZoom = 3.5;
            if (currentZoom > maxZoom) {
              this.map.setZoom(maxZoom);
            }

            const currentCenter = this.map.getCenter();
            this.map.setCenter({
              lat: currentCenter.lat() - 10, // Desplaza hacia abajo el centro
              lng: currentCenter.lng()
            });
          });
        }

      } catch (error) {
        console.error('Error al inicializar Google Maps:', error);
      }
    },

    zoomIn() {
      if (this.map) this.map.setZoom(this.map.getZoom() + 1);
    },

    zoomOut() {
      if (this.map) this.map.setZoom(this.map.getZoom() - 1);
    },

    resetView() {
      if (this.map && this.markers.length > 0) {
        const bounds = new google.maps.LatLngBounds();
        this.markers.forEach(marker => bounds.extend(marker.getPosition()));
        this.map.fitBounds(bounds, { top: 50, right: 50, bottom: 50, left: 50 });
      } else if (this.map) {
        this.map.setCenter({ lat: 0, lng: 0 });
        this.map.setZoom(3);
      }
    },

    clearMarkers() {
      if (this.markers) {
        this.markers.forEach(marker => marker.setMap(null));
        this.markers = [];
      }
    },

    handleImageError(event) {
      event.target.src = 'https://via.placeholder.com/150x225?text=Poster+No+Disponible';
    },

    handleAvatarError(event) {
      event.target.src = 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png';
    },

    formatTipoProduccion(tipo) {
      const tipos = {
        'PELICULA': 'Película',
        'SERIE': 'Serie',
        'DOCUMENTAL': 'Documental',
        'CORTOMETRAJE': 'Cortometraje'
      };
      return tipos[tipo] || tipo;
    },

    formatCategoria(categoria) {
      const categorias = {
        'ACCION': 'Acción',
        'AVENTURA': 'Aventura',
        'COMEDIA': 'Comedia',
        'DRAMA': 'Drama',
        'TERROR': 'Terror',
        'CIENCIA_FICCION': 'Ciencia Ficción',
        'ANIMACION': 'Animación',
        'SUSPENSE': 'Suspense',
        'ROMANCE': 'Romance',
        'FANTASIA': 'Fantasía',
        'DOCUMENTAL': 'Documental',
        'INFANTIL': 'Infantil'
      };
      return categorias[categoria] || categoria;
    },

    formatRole(role) {
      const rolesMap = {
        'USUARIO': 'Usuario',
        'ADMINISTRADOR': 'Administrador'
      };
      return rolesMap[role] || role;
    },

    viewUser(userId) {
      console.log('Ver usuario:', userId);
      // Aquí podrías redirigir a la página de edición del usuario
      // this.$router.push(`/admin/usuarios/editar/${userId}`);
    },

    addNewAdmin() {
      console.log('Añadir nuevo admin:', this.newAdmin);
      this.showAddAdminModal = false;
      this.newAdmin = { name: '', email: '', password: '' };
      alert(`Administrador ${this.newAdmin.name} añadido correctamente`);
    }
  },
  async mounted() {
    await this.fetchStats();
    await this.fetchRecentProductions();
    await this.fetchRecentUsers();
    await this.fetchLocations();
  },
  beforeUnmount() {
    this.clearMarkers();
  }
}
</script>

<style scoped>
:root {
  /* Colores base */
  --primary-color: #7e5bef;
  --primary-hover: #6d46e8;
  --success-color: #10b981;
  --danger-color: #ef4444;
  --warning-color: #f59e0b;
  --info-color: #3b82f6;

  /* Colores modo claro */
  --bg-color: #f8fafc;
  --card-bg: #ffffff;
  --card-header-bg: #f8fafc;
  --border-color: #e2e8f0;
  --text-primary: #1e293b;
  --text-secondary: #64748b;
  --shadow-color: rgba(0, 0, 0, 0.05);
  --hover-shadow-color: rgba(126, 91, 239, 0.1);
  --overlay-bg: rgba(0, 0, 0, 0.7);
  --production-item-bg: rgba(126, 91, 239, 0.03);
  --stat-icon-bg: rgba(126, 91, 239, 0.1);
  --progress-bg: rgba(126, 91, 239, 0.1);
}

.dark-mode {
  /* Colores modo oscuro */
  --primary-color: #8b5cf6;
  --primary-hover: #7c3aed;
  --success-color: #10b981;
  --danger-color: #ef4444;
  --warning-color: #f59e0b;
  --info-color: #3b82f6;

  --bg-color: #0f172a;
  --card-bg: #1e293b;
  --card-header-bg: #1e293b;
  --border-color: #334155;
  --text-primary: #f8fafc;
  --text-secondary: #94a3b8;
  --shadow-color: rgba(0, 0, 0, 0.2);
  --hover-shadow-color: rgba(139, 92, 246, 0.2);
  --overlay-bg: rgba(15, 23, 42, 0.9);
  --production-item-bg: rgba(139, 92, 246, 0.08);
  --stat-icon-bg: rgba(139, 92, 246, 0.2);
  --progress-bg: rgba(139, 92, 246, 0.2);
}

.dashboard-content {
  padding: 20px;
}

/* Stats Grid */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: var(--card-bg);
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 4px 6px var(--shadow-color);
  border: 1px solid var(--border-color);
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 15px var(--hover-shadow-color);
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 15px;
  position: relative;
  z-index: 2;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  background: rgba(126, 91, 239, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-color);
  font-size: 1.2rem;
  flex-shrink: 0;
}

.stat-info h3 {
  font-size: 1.8rem;
  font-weight: 700;
  margin: 0;
  color: var(--text-primary);
}

.stat-info p {
  margin: 5px 0 0;
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.stat-progress {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: rgba(126, 91, 239, 0.1);
}

.progress-bar {
  height: 100%;
  background: var(--primary-color);
  transition: width 0.5s ease;
}

.stat-loading {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: rgba(126, 91, 239, 0.1);
  overflow: hidden;
}

.loading-bar {
  height: 100%;
  width: 100%;
  background: linear-gradient(90deg, transparent, rgba(126, 91, 239, 0.5), transparent);
  animation: loading 1.5s infinite;
}

@keyframes loading {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}

/* Main Content Layout */
.main-content {
  display: grid;
  grid-template-columns: 1.5fr 1fr;
  gap: 25px;
}

@media (max-width: 1200px) {
  .main-content {
    grid-template-columns: 1fr;
  }
}

.content-section {
  display: flex;
  flex-direction: column;
  gap: 25px;
}

/* Map Card */
.map-card {
  background: var(--card-bg);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px var(--shadow-color);
  border: 1px solid var(--border-color);
  transition: all 0.3s ease;
}

.map-card:hover {
  box-shadow: 0 8px 15px var(--hover-shadow-color);
}

.card-header {
  padding: 15px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid var(--border-color);
  background: var(--card-header-bg);
}

.card-header h3 {
  font-size: 1.1rem;
  font-weight: 600;
  margin: 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.card-header h3 i {
  color: var(--primary-color);
}

.map-controls {
  display: flex;
  gap: 8px;
}

.map-control-btn {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: var(--card-bg);
  border: 1px solid var(--border-color);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: var(--text-primary);
  transition: all 0.3s ease;
}

.map-control-btn:hover {
  background: var(--primary-color);
  color: white;
  border-color: var(--primary-color);
}

.map-container {
  position: relative;
  height: 450px;
  overflow: hidden;
}

.google-map {
  width: 100%;
  height: 100%;
}

.loading-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: var(--overlay-bg);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
  z-index: 10;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  border-top-color: var(--primary-color);
  animation: spin 1s ease-in-out infinite;
  margin-bottom: 15px;
}

.empty-state {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: var(--text-secondary);
  text-align: center;
  padding: 20px;
  background: var(--card-bg);
}

.empty-state i {
  font-size: 2.5rem;
  color: var(--primary-color);
  margin-bottom: 15px;
  opacity: 0.8;
}

.btn-primary {
  padding: 8px 16px;
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: 8px;
  margin-top: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  font-size: 0.9rem;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.btn-primary:hover {
  background: var(--primary-hover);
  transform: translateY(-2px);
}

.view-all {
  font-size: 0.85rem;
  color: var(--primary-color);
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 5px;
  transition: all 0.3s ease;
  font-weight: 500;
}

.view-all:hover {
  color: var(--primary-hover);
  transform: translateX(3px);
}

/* Productions Card */
.productions-card {
  background: var(--card-bg);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px var(--shadow-color);
  border: 1px solid var(--border-color);
}

.productions-list {
  padding: 15px;
}

.production-item {
  display: flex;
  gap: 15px;
  padding: 15px;
  border-radius: 12px;
  margin-bottom: 12px;
  background: var(--production-item-bg);
  border: 1px solid var(--border-color);
  transition: all 0.3s ease;
}

.production-item:last-child {
  margin-bottom: 0;
}

.production-item:hover {
  transform: translateX(5px);
  border-color: var(--primary-color);
  box-shadow: 0 5px 10px var(--hover-shadow-color);
}

.production-poster {
  position: relative;
  flex-shrink: 0;
}

.production-image {
  width: 120px;
  height: 180px;
  border-radius: 8px;
  object-fit: cover;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.production-details {
  flex: 1;
  min-width: 0;
}

.production-details h4 {
  font-size: 1.1rem;
  margin: 0 0 8px 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.production-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
  flex-wrap: wrap;
}

.badge {
  font-size: 0.7rem;
  padding: 3px 8px;
  border-radius: 6px;
  font-weight: 600;
  color: black;
}

.dark-mode .badge{
  color:white;
}

.badge.película {
  background: rgba(16, 185, 129, 0.1);
}

.badge.serie {
  background: rgba(59, 130, 246, 0.1);
}

.badge.documental {
  background: rgba(139, 92, 246, 0.1);
}

.badge.cortometraje {
  background: rgba(245, 158, 11, 0.1);
  color: var(--warning-color);
}

.year, .duration {
  font-size: 0.8rem;
  color: var(--text-secondary);
}

.duration i {
  margin-right: 3px;
}

.production-rating {
  display: inline-flex;
  align-items: center;
  gap: 3px;
  font-size: 0.9rem;
  color: var(--warning-color);
  margin-bottom: 8px;
}

.production-categories {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.category-tag {
  font-size: 0.7rem;
  padding: 3px 8px;
  background: rgba(126, 91, 239, 0.1);
  color: var(--primary-color);
  border-radius: 20px;
}

.production-actions {
  display: flex;
  align-items: flex-start;
}

.production-actions .action-btn {
  padding: 6px 12px;
  font-size: 0.8rem;
  display: inline-flex;
  align-items: center;
  gap: 5px;
  text-decoration: none;
}

/* Users Card */
.users-card {
  background: var(--card-bg);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px var(--shadow-color);
  border: 1px solid var(--border-color);
}

.users-list {
  padding: 15px;
}

.user-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 12px;
  border-radius: 10px;
  margin-bottom: 10px;
  background: var(--production-item-bg);
  border: 1px solid var(--border-color);
  transition: all 0.3s ease;
}

.user-item:hover {
  transform: translateX(3px);
  border-color: var(--primary-color);
}

.user-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-info {
  flex: 1;
  min-width: 0;
}

.user-info h4 {
  font-size: 1rem;
  margin: 0;
}

.user-info p {
  font-size: 0.8rem;
  color: var(--text-secondary);
  margin: 4px 0 0;
}

.user-role {
  font-size: 0.7rem;
  padding: 2px 8px;
  border-radius: 12px;
  font-weight: 600;
  display: inline-block;
  margin-top: 4px;
}

.user-role.administrador {
  background: rgba(16, 185, 129, 0.1);
  color: var(--success-color);
}

.user-role.editor {
  background: rgba(59, 130, 246, 0.1);
  color: var(--info-color);
}

.user-role.moderador {
  background: rgba(245, 158, 11, 0.1);
  color: var(--warning-color);
}

.user-actions {
  display: flex;
  align-items: center;
}

.btn-icon {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: rgba(126, 91, 239, 0.1);
  border: none;
  color: var(--primary-color);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-icon:hover {
  background: var(--primary-color);
  color: white;
}

/* Stats Card */
.stats-card {
  background: var(--card-bg);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px var(--shadow-color);
  border: 1px solid var(--border-color);
}

.stats-summary {
  padding: 20px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 15px;
}

.summary-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  border-radius: 10px;
  background: rgba(126, 91, 239, 0.05);
  border: 1px solid var(--border-color);
  transition: all 0.3s ease;
}

.summary-item:hover {
  border-color: var(--primary-color);
  transform: translateY(-3px);
}

.summary-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  background: rgba(126, 91, 239, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-color);
  font-size: 1.1rem;
}

.summary-info h4 {
  font-size: 1.5rem;
  margin: 0;
  color: var(--text-primary);
}

.summary-info p {
  margin: 5px 0 0;
  font-size: 0.8rem;
  color: var(--text-secondary);
}

/* Modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: var(--card-bg);
  border-radius: 12px;
  width: 100%;
  max-width: 500px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
  overflow: hidden;
}

.modal-header {
  padding: 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid var(--border-color);
}

.modal-header h3 {
  margin: 0;
  font-size: 1.2rem;
}

.modal-close {
  background: none;
  border: none;
  color: var(--text-secondary);
  font-size: 1.2rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.modal-close:hover {
  color: var(--primary-color);
  transform: rotate(90deg);
}

.modal-body {
  padding: 20px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-size: 0.9rem;
  color: var(--text-primary);
}

.form-group input {
  width: 100%;
  padding: 10px;
  border-radius: 8px;
  border: 1px solid var(--border-color);
  background: var(--card-bg);
  color: var(--text-primary);
  transition: all 0.3s ease;
}

.form-group input:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(126, 91, 239, 0.2);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.btn-secondary {
  padding: 10px 20px;
  background: var(--card-bg);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  color: var(--text-primary);
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-secondary:hover {
  background: var(--border-color);
}

/* Animations */
@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Responsive */
@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .main-content {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .production-item {
    flex-direction: column;
  }

  .production-image {
    width: 100%;
    height: auto;
    max-height: 200px;
  }

  .stats-summary {
    grid-template-columns: 1fr;
  }
}

.icon-button {
  width: 32px;
  height: 32px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border: none;
  background-color: transparent;
  transition: all 0.2s;
  font-size: 14px;
}

.icon-button:hover {
  transform: translateY(-1px);
}

.deactivate-button {
  color: #ed8936;
}

.deactivate-button:hover {
  background-color: #fffaf0;
}

.activate-button {
  color: #48bb78;
}

.activate-button:hover {
  background-color: #f0fff4;
}

.delete-button {
  color: #f56565;
}

.delete-button:hover {
  background-color: #fff5f5;
}

.dark-mode .icon-button.edit-button:hover,
.dark .icon-button.edit-button:hover {
  background-color: rgba(66, 153, 225, 0.1);
}

.dark-mode .icon-button.deactivate-button:hover,
.dark .icon-button.deactivate-button:hover {
  background-color: rgba(237, 137, 54, 0.1);
}

.dark-mode .icon-button.activate-button:hover,
.dark .icon-button.activate-button:hover {
  background-color: rgba(72, 187, 120, 0.1);
}

.dark-mode .icon-button.delete-button:hover,
.dark .icon-button.delete-button:hover {
  background-color: rgba(245, 101, 101, 0.1);
}
</style>