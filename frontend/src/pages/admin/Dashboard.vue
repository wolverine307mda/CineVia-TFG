<template>
  <div class="dashboard-container">
    <div class="dashboard-content">
      <!-- Estadísticas -->
      <div class="stats-grid">
        <div class="stat-card" v-for="stat in stats" :key="stat.title" :class="stat.trend">
          <div class="stat-icon">
            <i :class="stat.icon"></i>
          </div>
          <div class="stat-info">
            <h3>{{ stat.value.toLocaleString() }}</h3>
            <p>{{ stat.title }}</p>
          </div>
          <div class="stat-trend">
            <i :class="stat.trendIcon"></i> {{ stat.trendValue }}
          </div>
          <div class="stat-wave"></div>
        </div>
      </div>

      <!-- Sección principal -->
      <div class="main-content">
        <!-- Mapa y Producciones -->
        <div class="content-section">
          <div class="map-card">
            <div class="card-header">
            </div>
            <div class="map-container">
              <div ref="map" class="google-map" v-show="!loadingLocations && locations.length > 0"></div>
              <div v-if="loadingLocations" class="loading-overlay">
                <div class="spinner"></div>
                <p>Cargando ubicaciones...</p>
              </div>
              <div v-if="!loadingLocations && locations.length === 0" class="empty-state">
                <i class="fas fa-map-marked-alt"></i>
                <p>No hay ubicaciones disponibles</p>
                <button @click="fetchLocations" class="btn-primary">Intentar de nuevo</button>
              </div>
            </div>
          </div>

          <div class="productions-card">
            <div class="card-header">
              <h3><i class="fas fa-video"></i> Últimas Producciones</h3>
              <router-link to="/admin/producciones" class="view-all">
                Ver todo <i class="fas fa-chevron-right"></i>
              </router-link>
            </div>
            <div class="productions-list">
              <div v-for="(production, index) in recentProductions" :key="index" class="production-item">
                <img :src="production.image" :alt="production.title" class="production-image">
                <div class="production-info">
                  <h4>{{ production.title }}</h4>
                  <div class="production-meta">
                    <span class="badge" :class="production.type">{{ production.type }}</span>
                    <span class="date">{{ production.date }}</span>
                  </div>
                </div>
                <div class="production-status" :class="production.status">
                  <i :class="statusIcons[production.status]"></i>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Acciones rápidas y actividad -->
        <div class="content-section">
          <div class="actions-card">
            <div class="card-header">
              <h3><i class="fas fa-bolt"></i> Acciones Rápidas</h3>
            </div>
            <div class="actions-grid">
              <button v-for="action in quickActions" :key="action.path" @click="navigateTo(action.path)" class="action-btn">
                <div class="action-icon">
                  <i :class="action.icon"></i>
                </div>
                <span>{{ action.label }}</span>
              </button>
            </div>
          </div>

          <div class="activity-card">
            <div class="card-header">
              <h3><i class="fas fa-chart-line"></i> Actividad Reciente</h3>
              <select v-model="timeRange" class="time-select">
                <option value="7">7 días</option>
                <option value="30">30 días</option>
                <option value="90">90 días</option>
              </select>
            </div>
            <div class="chart-container">
              <canvas ref="activityChart"></canvas>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Loader } from '@googlemaps/js-api-loader';
import Chart from 'chart.js/auto';

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: [
        { title: 'Producciones', icon: 'fas fa-film', value: 1245, trend: 'up', trendValue: '12%', trendIcon: 'fas fa-arrow-up' },
        { title: 'Ubicaciones', icon: 'fas fa-map-marker-alt', value: 856, trend: 'up', trendValue: '8%', trendIcon: 'fas fa-arrow-up' },
        { title: 'Profesionales', icon: 'fas fa-user-tie', value: 542, trend: 'up', trendValue: '5%', trendIcon: 'fas fa-arrow-up' },
        { title: 'Sagas', icon: 'fas fa-layer-group', value: 89, trend: 'down', trendValue: '2%', trendIcon: 'fas fa-arrow-down' }
      ],
      recentProductions: [
        {
          title: 'Dune: Parte Dos',
          type: 'Película',
          date: '2023-11-17',
          status: 'active',
          image: 'https://via.placeholder.com/50'
        },
        {
          title: 'The Last of Us',
          type: 'Serie',
          date: '2023-01-15',
          status: 'active',
          image: 'https://via.placeholder.com/50'
        },
        {
          title: 'Oppenheimer',
          type: 'Película',
          date: '2023-07-21',
          status: 'pending',
          image: 'https://via.placeholder.com/50'
        },
        {
          title: 'Stranger Things 4',
          type: 'Serie',
          date: '2022-05-27',
          status: 'active',
          image: 'https://via.placeholder.com/50'
        }
      ],
      quickActions: [
        { label: 'Nueva Producción', icon: 'fas fa-plus', path: '/admin/producciones/nueva' },
        { label: 'Nuevo Lugar', icon: 'fas fa-map-marker-alt', path: '/admin/lugares/nuevo' },
        { label: 'Nuevo Profesional', icon: 'fas fa-user-tie', path: '/admin/profesionales/nuevo' },
        { label: 'Nueva Saga', icon: 'fas fa-layer-group', path: '/admin/sagas/nueva' },
        { label: 'Nuevo Usuario', icon: 'fas fa-user-plus', path: '/admin/usuarios/nuevo' },
        { label: 'Configuración', icon: 'fas fa-cog', path: '/admin/configuracion' }
      ],
      statusIcons: {
        active: 'fas fa-check-circle',
        pending: 'fas fa-clock',
        inactive: 'fas fa-times-circle'
      },
      locations: [],
      loadingLocations: false,
      map: null,
      markers: [],
      timeRange: '7',
      activityChart: null,
      darkMode: false,
      darkMapStyle: [
        { elementType: "geometry", stylers: [{ color: "#242f3e" }] },
        { elementType: "labels.text.fill", stylers: [{ color: "#746855" }] },
        { elementType: "labels.text.stroke", stylers: [{ color: "#242f3e" }] },
        { elementType: "labels.icon", stylers: [{ visibility: "off" }] },
        { featureType: "administrative", elementType: "geometry.stroke", stylers: [{ color: "#1a1a1a" }] },
        { featureType: "administrative.land_parcel", elementType: "geometry.stroke", stylers: [{ color: "#1a1a1a" }] },
        { featureType: "poi", stylers: [{ visibility: "off" }] },
        { featureType: "road", elementType: "geometry.fill", stylers: [{ color: "#2c2c2c" }] },
        { featureType: "road", elementType: "labels.text.fill", stylers: [{ color: "#9ca5b3" }] },
        { featureType: "road.highway", elementType: "geometry.fill", stylers: [{ color: "#3d3d3d" }] },
        { featureType: "road.highway", elementType: "geometry.stroke", stylers: [{ color: "#1a1a1a" }] },
        { featureType: "transit", stylers: [{ visibility: "off" }] },
        { featureType: "water", elementType: "geometry", stylers: [{ color: "#1e1e1e" }] },
        { featureType: "water", elementType: "labels.text.fill", stylers: [{ color: "#515c6d" }] }
      ]
    }
  },
  methods: {
    navigateTo(path) {
      this.$router.push(path);
    },
    async fetchLocations() {
      this.loadingLocations = true;
      this.locations = [];

      try {
        const response = await fetch('http://localhost:8080/api/ubicaciones/all');

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
        } else {
          console.warn('No se encontraron ubicaciones válidas');
        }

      } catch (error) {
        console.error('Error al cargar ubicaciones:', error);

        if (process.env.NODE_ENV !== 'production') {
          this.locations = [
            {
              id: "u007",
              nombre: "Chicago, USA",
              latitud: 41.8781,
              longitud: -87.6298,
              createdAt: null,
              updatedAt: null
            },
            {
              id: "u008",
              nombre: "Torre Eiffel, París",
              latitud: 48.8584,
              longitud: 2.2945,
              createdAt: null,
              updatedAt: null
            }
          ];
          await this.initMap();
        }
      } finally {
        this.loadingLocations = false;
      }
    },
    async initMap() {
      try {
        const loader = new Loader({
          apiKey: "AIzaSyCtMIHC_PntZzdioTFmRcamhjRNKZMw4hc",
          version: "weekly",
          libraries: ["places"]
        });

        await loader.load();

        const mapOptions = {
          center: { lat: 20, lng: 0 },
          zoom: 2,
          mapTypeControl: true,
          mapTypeControlOptions: {
            style: google.maps.MapTypeControlStyle.DROPDOWN_MENU,
            position: google.maps.ControlPosition.TOP_RIGHT,
            mapTypeIds: ['roadmap', 'satellite', 'hybrid']
          },
          streetViewControl: false,
          zoomControl: true,
          zoomControlOptions: {
            position: google.maps.ControlPosition.RIGHT_BOTTOM
          },
          fullscreenControl: true,
          fullscreenControlOptions: {
            position: google.maps.ControlPosition.RIGHT_BOTTOM
          },
          styles: this.darkMode ? this.darkMapStyle : []
        };

        this.map = new google.maps.Map(this.$refs.map, mapOptions);

        this.clearMarkers();

        this.locations.forEach(location => {
          const marker = new google.maps.Marker({
            position: { lat: location.latitud, lng: location.longitud },
            map: this.map,
            title: location.nombre
          });

          const infoWindow = new google.maps.InfoWindow({
            content: `
              <div class="map-info-window">
                <h4>${location.nombre}</h4>
                <p><strong>ID:</strong> ${location.id}</p>
                <p><strong>Producción ID:</strong> ${location.produccionId}</p>
                <p><strong>Coordenadas:</strong> ${location.latitud.toFixed(4)}, ${location.longitud.toFixed(4)}</p>
              </div>
            `
          });

          marker.addListener('click', () => {
            infoWindow.open(this.map, marker);
          });

          this.markers.push(marker);
        });

        if (this.markers.length > 0) {
          const bounds = new google.maps.LatLngBounds();
          this.markers.forEach(marker => bounds.extend(marker.getPosition()));
          this.map.fitBounds(bounds, { top: 50, right: 50, bottom: 50, left: 50 });
        }

      } catch (error) {
        console.error('Error al inicializar Google Maps:', error);
      }
    },
    clearMarkers() {
      this.markers.forEach(marker => marker.setMap(null));
      this.markers = [];
    },
    initActivityChart() {
      const ctx = this.$refs.activityChart.getContext('2d');

      if (this.activityChart) {
        this.activityChart.destroy();
      }

      this.activityChart = new Chart(ctx, {
        type: 'line',
        data: {
          labels: Array.from({ length: this.timeRange }, (_, i) => `Día ${i + 1}`),
          datasets: [{
            label: 'Actividad',
            data: Array.from({ length: this.timeRange }, () => Math.floor(Math.random() * 100)),
            borderColor: '#7e5bef',
            backgroundColor: 'rgba(126, 91, 239, 0.1)',
            borderWidth: 2,
            tension: 0.3,
            fill: true
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: { display: false }
          },
          scales: {
            y: {
              beginAtZero: true,
              grid: {
                color: this.darkMode ? 'rgba(255, 255, 255, 0.1)' : 'rgba(0, 0, 0, 0.1)'
              },
              ticks: {
                color: this.darkMode ? '#e2e8f0' : '#4a5568'
              }
            },
            x: {
              grid: {
                color: this.darkMode ? 'rgba(255, 255, 255, 0.1)' : 'rgba(0, 0, 0, 0.1)'
              },
              ticks: {
                color: this.darkMode ? '#e2e8f0' : '#4a5568'
              }
            }
          }
        }
      });
    },
    toggleDarkMode() {
      this.darkMode = !this.darkMode;
      if (this.map) {
        this.map.setOptions({
          styles: this.darkMode ? this.darkMapStyle : []
        });
      }
      this.initActivityChart();
    }
  },
  async mounted() {
    await this.fetchLocations();
    this.initActivityChart();
  },
  beforeUnmount() {
    if (this.activityChart) {
      this.activityChart.destroy();
    }
    this.clearMarkers();
  },
  watch: {
    timeRange() {
      this.initActivityChart();
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  margin-left: 30px;
  width: calc(100% - 30px);
  min-height: 100vh;
  transition: background-color 0.3s ease;
}

.dashboard-content {
  padding: 20px;
  height: 100%;
  margin: 0;
  justify-content: center;
  align-items: center;
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
  padding: 25px;
  display: flex;
  flex-direction: column;
  box-shadow: 0 4px 6px var(--shadow-color);
  position: relative;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
  border: 1px solid var(--border-color);
  z-index: 1;
}

.stat-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: var(--stat-gradient);
  opacity: 0;
  transition: opacity 0.3s ease;
  z-index: -1;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px var(--hover-shadow-color);
  border-color: var(--primary-color);
}

.stat-card:hover::before {
  opacity: 0.1;
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
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.stat-card:hover .stat-icon {
  background: rgba(126, 91, 239, 0.2);
  transform: scale(1.1);
}

.stat-info h3 {
  font-size: 2rem;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 5px;
  transition: color 0.3s ease;
}

.stat-info p {
  color: var(--text-secondary);
  font-size: 0.95rem;
  transition: color 0.3s ease;
}

.stat-trend {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 0.9rem;
  font-weight: 600;
  margin-top: 10px;
}

.stat-trend.up {
  color: var(--success-color);
}

.stat-trend.down {
  color: var(--danger-color);
}

.stat-wave {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, var(--primary-color), transparent);
  opacity: 0.5;
  transition: all 0.3s ease;
}

.stat-card:hover .stat-wave {
  height: 6px;
  opacity: 0.8;
}

/* Main Content Layout */
.main-content {
  display: grid;
  grid-template-columns: 1.5fr 1fr;
  gap: 25px;
  transition: all 0.3s ease;
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
  border-radius: 2px !important;
  overflow: hidden;
  box-shadow: 0 4px 6px var(--shadow-color);
  border: 1px solid var(--border-color);
  transition: all 0.3s ease;
}

.map-card:hover {
  box-shadow: 0 8px 15px var(--hover-shadow-color);
}

.card-header {
  padding: 18px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid var(--border-color);
  background: var(--card-header-bg);
  transition: all 0.3s ease;
}

.card-header h3 {
  font-size: 1.1rem;
  color: var(--text-primary);
  display: flex;
  align-items: center;
  gap: 10px;
  transition: color 0.3s ease;
}

.card-header h3 i {
  color: var(--primary-color);
}

.map-container {
  position: relative;
  height: 450px;
  overflow: hidden;
  border-radius: 5px;
}

.google-map {
  width: 100%;
  height: 100%;
  border-radius: 5px;
  transition: all 0.3s ease;
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
  border-radius: 0 0 16px 16px;
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
  border-radius: 0 0 16px 16px;
}

.empty-state i {
  font-size: 2.5rem;
  color: var(--primary-color);
  margin-bottom: 15px;
  opacity: 0.8;
}

.btn-primary {
  padding: 10px 20px;
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: 8px;
  margin-top: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
  box-shadow: 0 2px 5px rgba(126, 91, 239, 0.3);
}

.btn-primary:hover {
  background: var(--primary-hover);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(126, 91, 239, 0.4);
}

/* Productions Card */
.productions-card {
  background: var(--card-bg);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 6px var(--shadow-color);
  border: 1px solid var(--border-color);
  transition: all 0.3s ease;
}

.productions-card:hover {
  box-shadow: 0 8px 15px var(--hover-shadow-color);
}

.productions-list {
  padding: 15px;
}

.production-item {
  display: flex;
  align-items: center;
  padding: 15px;
  border-radius: 12px;
  margin-bottom: 12px;
  background: var(--production-item-bg);
  border: 1px solid var(--border-color);
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  cursor: pointer;
}

.production-item:last-child {
  margin-bottom: 0;
}

.production-item:hover {
  transform: translateX(5px);
  box-shadow: 0 5px 10px var(--hover-shadow-color);
  border-color: var(--primary-color);
}

.production-image {
  width: 50px;
  height: 50px;
  border-radius: 8px;
  object-fit: cover;
  margin-right: 15px;
  transition: all 0.3s ease;
}

.production-item:hover .production-image {
  transform: scale(1.05);
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.1);
}

.production-info {
  flex: 1;
  min-width: 0;
}

.production-info h4 {
  font-size: 0.95rem;
  color: var(--text-primary);
  margin-bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: color 0.3s ease;
}

.production-meta {
  display: flex;
  align-items: center;
  gap: 10px;
}

.badge {
  font-size: 0.7rem;
  padding: 3px 8px;
  border-radius: 6px;
  font-weight: 600;
}

.badge.Película {
  background: rgba(16, 185, 129, 0.1);
  color: var(--success-color);
}

.badge.Serie {
  background: rgba(59, 130, 246, 0.1);
  color: var(--info-color);
}

.date {
  font-size: 0.75rem;
  color: var(--text-secondary);
  transition: color 0.3s ease;
}

.production-status {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1rem;
  margin-left: 10px;
  flex-shrink: 0;
  transition: all 0.3s ease;
}

.production-status.active {
  color: var(--success-color);
  background: rgba(16, 185, 129, 0.1);
}

.production-status.pending {
  color: var(--warning-color);
  background: rgba(245, 158, 11, 0.1);
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

/* Actions Card */
.actions-card {
  background: var(--card-bg);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 6px var(--shadow-color);
  border: 1px solid var(--border-color);
  transition: all 0.3s ease;
}

.actions-card:hover {
  box-shadow: 0 8px 15px var(--hover-shadow-color);
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
  padding: 20px;
}

@media (max-width: 768px) {
  .actions-grid {
    grid-template-columns: 1fr;
  }
}

.action-btn {
  padding: 20px 15px;
  background: var(--action-btn-bg);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.action-btn:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 15px var(--hover-shadow-color);
  border-color: var(--primary-color);
  background: var(--action-btn-hover);
}

.action-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  background: rgba(126, 91, 239, 0.1);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--primary-color);
  font-size: 1.2rem;
  transition: all 0.3s ease;
}

.action-btn:hover .action-icon {
  background: rgba(126, 91, 239, 0.2);
  transform: scale(1.1);
}

.action-btn span {
  font-size: 0.9rem;
  color: var(--text-primary);
  font-weight: 500;
  text-align: center;
  transition: color 0.3s ease;
}

.actions-grid span{
  color: black;
}
.dark-mode span {
  color: white;
}

/* Activity Card */
.activity-card {
  background: var(--card-bg);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 6px var(--shadow-color);
  border: 1px solid var(--border-color);
  transition: all 0.3s ease;
}

.activity-card:hover {
  box-shadow: 0 8px 15px var(--hover-shadow-color);
}

.chart-container {
  padding: 20px;
  height: 250px;
  position: relative;
}

.time-select {
  padding: 6px 10px;
  border-radius: 8px;
  border: 1px solid var(--border-color);
  background: var(--card-bg);
  color: var(--text-primary);
  font-size: 0.85rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.time-select:hover {
  border-color: var(--primary-color);
}

.time-select:focus {
  outline: none;
  box-shadow: 0 0 0 2px rgba(126, 91, 239, 0.2);
}

/* Animations */
@keyframes spin {
  to { transform: rotate(360deg); }
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

/* Dark Mode Variables */
:root {
  --primary-color: #7e5bef;
  --primary-hover: #6d46e8;
  --success-color: #10b981;
  --danger-color: #ef4444;
  --warning-color: #f59e0b;
  --info-color: #3b82f6;

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
  --action-btn-bg: #ffffff;
  --action-btn-hover: #f8fafc;
  --stat-gradient: linear-gradient(135deg, #7e5bef 0%, #a78bfa 100%);
}

.dark-mode {
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
  --action-btn-bg: #1e293b;
  --action-btn-hover: #334155;
  --stat-gradient: linear-gradient(135deg, #8b5cf6 0%, #c4b5fd 100%);
}

/* Apply dark mode to body */
body.dark-mode {
  background-color: var(--bg-color);
  color: var(--text-primary);
}

/* Smooth transitions for dark mode */
body, .dashboard-container, .stat-card, .map-card,
.productions-card, .actions-card, .activity-card {
  transition: background-color 0.3s ease, color 0.3s ease, border-color 0.3s ease;
}

/* Map info window styles */
.map-info-window {
  color: #1e293b;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;
  padding: 10px;
  min-width: 200px;
}

.map-info-window h4 {
  margin: 0 0 8px 0;
  color: #7e5bef;
  font-size: 1rem;
}

.map-info-window p {
  margin: 4px 0;
  font-size: 0.85rem;
  color: #475569;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .dashboard-container {
    margin-left: 0;
    width: 100%;
    padding: 15px;
  }
}

@media (max-width: 480px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .production-item {
    padding: 12px;
  }

  .production-image {
    width: 40px;
    height: 40px;
    margin-right: 12px;
  }
}
</style>