<template>
  <div class="ubicaciones-container">
    <!-- Contenedor principal (mapa + tabla) -->
    <div class="main-content">
      <!-- Mapa a la izquierda -->
      <div class="map-section">
        <div class="map-container">
          <div ref="map" class="google-map" v-show="!loading && ubicaciones.length > 0"></div>
          <div v-if="loading" class="loading-overlay">
            <div class="spinner"></div>
            <p>Cargando mapa...</p>
          </div>
          <div v-if="!loading && ubicaciones.length === 0" class="empty-state">
            <i class="fas fa-map-marked-alt"></i>
            <p>No hay ubicaciones para mostrar</p>
          </div>
        </div>
      </div>

      <!-- Sección de tabla a la derecha -->
      <div class="table-section">
        <!-- Búsqueda -->
        <div class="search-container">
          <div class="search-box">
            <i class="fas fa-search"></i>
            <input
                v-model="searchQuery"
                @input="debouncedSearch"
                placeholder="Buscar ubicaciones..."
                type="search"
            >
            <button @click="openModal(null)" class="btn-new-icon" title="Nueva Ubicación">
              <i class="fas fa-plus"></i>
            </button>
          </div>
        </div>

        <!-- Tabla de ubicaciones -->
        <div class="table-container">
          <table class="ubicaciones-table">
            <thead>
            <tr>
              <th @click="sortBy('nombre')">
                Nombre
              </th>
              <th @click="sortBy('latitud')">
                Latitud
              </th>
              <th @click="sortBy('longitud')">
                Longitud
              </th>
              <th>Acciones</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(ubicacion, index) in ubicaciones" :key="ubicacion.id"
                :class="{'even-row': index % 2 === 0, 'odd-row': index % 2 !== 0}"
                @mouseenter="showInfoWindow(ubicacion)"
                @mouseleave="hideInfoWindow()">
              <td>{{ ubicacion.nombre }}</td>
              <td>{{ ubicacion.latitud.toFixed(4) }}</td>
              <td>{{ ubicacion.longitud.toFixed(4) }}</td>
              <td class="actions">
                <button @click="openModal(ubicacion)" class="btn-edit" title="Editar">
                  <i class="fas fa-edit"></i>
                </button>
                <button @click="confirmDelete(ubicacion)" class="btn-delete" title="Eliminar">
                  <i class="fas fa-trash"></i>
                </button>
                <button @click="centerMap(ubicacion)" class="btn-view" title="Centrar en mapa">
                  <i class="fas fa-map-marker-alt"></i>
                </button>
              </td>
            </tr>
            <tr v-if="ubicaciones.length === 0">
              <td colspan="5" class="no-results">
                No se encontraron ubicaciones
              </td>
            </tr>
            </tbody>
          </table>
        </div>

        <!-- Paginación -->
        <div class="pagination-container">
          <button
              @click="prevPage"
              :disabled="currentPage === 1"
              class="pagination-btn"
              title="Página anterior"
          >
            <i class="fas fa-chevron-left"></i>
          </button>
          <span class="page-info">
            Página {{ currentPage }} de {{ totalPages }}
          </span>
          <button
              @click="nextPage"
              :disabled="currentPage >= totalPages"
              class="pagination-btn"
              title="Página siguiente"
          >
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>
    </div>

    <!-- Modal para crear/editar -->
    <UbicacionModal
        v-if="showModal"
        :ubicacion="selectedUbicacion"
        :show="showModal"
        @close="closeModal"
        @save="handleSave"
    />
  </div>
</template>

<script>
import { Loader } from '@googlemaps/js-api-loader';
import { debounce } from 'lodash';
import UbicacionModal from "@/components/modales/UbicacionModal.vue";

export default {
  name: 'AdministracionUbicaciones',
  components: {
    UbicacionModal
  },
  data() {
    return {
      ubicaciones: [],
      currentPage: 1,
      itemsPerPage: 10,
      totalItems: 0,
      totalPages: 1,
      loading: false,
      searchQuery: '',
      sortField: 'nombre',
      sortDirection: 'asc',
      showModal: false,
      selectedUbicacion: null,
      map: null,
      markers: [],
      infoWindow: null,
      hoveredMarker: null,
      currentZoom: null,
      currentCenter: null,
      darkMapStyle: [
        {elementType: "geometry", stylers: [{color: "#242f3e"}]},
        {elementType: "labels.text.stroke", stylers: [{color: "#242f3e"}]},
        {elementType: "labels.text.fill", stylers: [{color: "#746855"}]},
        {
          featureType: "administrative.locality",
          elementType: "labels.text.fill",
          stylers: [{color: "#d59563"}]
        },
        {
          featureType: "poi",
          elementType: "labels.text.fill",
          stylers: [{color: "#d59563"}]
        },
        {
          featureType: "poi.park",
          elementType: "geometry",
          stylers: [{color: "#263c3f"}]
        },
        {
          featureType: "poi.park",
          elementType: "labels.text.fill",
          stylers: [{color: "#6b9a76"}]
        },
        {
          featureType: "road",
          elementType: "geometry",
          stylers: [{color: "#38414e"}]
        },
        {
          featureType: "road",
          elementType: "geometry.stroke",
          stylers: [{color: "#212a37"}]
        },
        {
          featureType: "road",
          elementType: "labels.text.fill",
          stylers: [{color: "#9ca5b3"}]
        },
        {
          featureType: "road.highway",
          elementType: "geometry",
          stylers: [{color: "#746855"}]
        },
        {
          featureType: "road.highway",
          elementType: "geometry.stroke",
          stylers: [{color: "#1f2835"}]
        },
        {
          featureType: "road.highway",
          elementType: "labels.text.fill",
          stylers: [{color: "#f3d19c"}]
        },
        {
          featureType: "transit",
          elementType: "geometry",
          stylers: [{color: "#2f3948"}]
        },
        {
          featureType: "transit.station",
          elementType: "labels.text.fill",
          stylers: [{color: "#d59563"}]
        },
        {
          featureType: "water",
          elementType: "geometry",
          stylers: [{color: "#17263c"}]
        },
        {
          featureType: "water",
          elementType: "labels.text.fill",
          stylers: [{color: "#515c6d"}]
        },
        {
          featureType: "water",
          elementType: "labels.text.stroke",
          stylers: [{color: "#17263c"}]
        }
      ]
    }
  },
  computed: {
    darkMode() {
      return document.documentElement.classList.contains('dark-mode');
    }
  },
  watch: {
    darkMode(newVal) {
      if (this.map) {
        this.map.setOptions({
          styles: newVal ? this.darkMapStyle : []
        });
      }
    }
  },
  created() {
    this.debouncedSearch = debounce(this.fetchUbicaciones, 500);
    this.fetchUbicaciones();
  },
  mounted() {
    this.initMap();
  },
  methods: {
    async fetchUbicaciones() {
      this.loading = true;
      try {
        const response = await fetch(
            `http://localhost:8080/api/ubicaciones?page=${this.currentPage - 1}&size=${this.itemsPerPage}&sortBy=${this.sortField}&sortDirection=${this.sortDirection}&search=${this.searchQuery}`
        );

        if (!response.ok) {
          throw new Error('Error al cargar ubicaciones');
        }

        const data = await response.json();
        this.ubicaciones = data.data;
        this.totalItems = data.totalItems;
        this.totalPages = data.totalPages;

        this.updateMapMarkers();
      } catch (error) {
        console.error('Error:', error);
      } finally {
        this.loading = false;
      }
    },
    loadMockData() {
      this.totalItems = 4;
      this.totalPages = 1;
    },
    async initMap() {
      try {
        const loader = new Loader({
          apiKey: "AIzaSyCtMIHC_PntZzdioTFmRcamhjRNKZMw4hc",
          version: "weekly",
          libraries: ["places"]
        });

        await loader.load();

        this.map = new google.maps.Map(this.$refs.map, {
          center: { lat: 20, lng: 0 },
          zoom: 2,
          styles: this.darkMode ? this.darkMapStyle : [],
          mapTypeControl: true,
          streetViewControl: false
        });

        // Guardar el estado inicial del mapa
        this.currentZoom = this.map.getZoom();
        this.currentCenter = this.map.getCenter();

        this.updateMapMarkers();
      } catch (error) {
        console.error('Error al cargar Google Maps:', error);
      }
    },
    updateMapMarkers() {
      this.clearMarkers();
      this.infoWindow = null;
      this.hoveredMarker = null;

      if (this.ubicaciones.length === 0) {
        // Restaurar la vista inicial si no hay ubicaciones
        if (this.map && this.currentZoom && this.currentCenter) {
          this.map.setZoom(this.currentZoom);
          this.map.setCenter(this.currentCenter);
        }
        return;
      }

      const bounds = new google.maps.LatLngBounds();

      this.ubicaciones.forEach(ubicacion => {
        const marker = new google.maps.Marker({
          position: { lat: ubicacion.latitud, lng: ubicacion.longitud },
          map: this.map,
          title: ubicacion.nombre
        });

        const infoWindow = new google.maps.InfoWindow({
          content: `
            <div class="map-info-window ${this.darkMode ? 'dark' : ''}">
              <h4>${ubicacion.nombre}</h4>
              <p><strong>Coordenadas:</strong> ${ubicacion.latitud.toFixed(4)}, ${ubicacion.longitud.toFixed(4)}</p>
            </div>
          `
        });

        marker.addListener('click', () => {
          infoWindow.open(this.map, marker);
        });

        this.markers.push(marker);
        bounds.extend(marker.getPosition());
      });

      // Solo ajustar los límites si no hay un zoom o centro específico
      if (!this.currentZoom || !this.currentCenter) {
        this.map.fitBounds(bounds, { top: 50, right: 50, bottom: 50, left: 50 });
      }
    },
    centerMap(ubicacion) {
      if (!this.map) return;

      this.currentZoom = 12;
      this.currentCenter = new google.maps.LatLng(ubicacion.latitud, ubicacion.longitud);

      this.map.setCenter(this.currentCenter);
      this.map.setZoom(this.currentZoom);
    },
    clearMarkers() {
      this.markers.forEach(marker => marker.setMap(null));
      this.markers = [];
    },
    sortBy(field) {
      if (this.sortField === field) {
        this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
      } else {
        this.sortField = field;
        this.sortDirection = 'asc';
      }
      this.fetchUbicaciones();
    },
    sortIcon(field) {
      if (this.sortField !== field) return 'fas fa-sort';
      return this.sortDirection === 'asc' ? 'fas fa-sort-up' : 'fas fa-sort-down';
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchUbicaciones(false); // No reiniciar el mapa
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchUbicaciones(false); // No reiniciar el mapa
      }
    },
    showInfoWindow(ubicacion) {
      if (!this.map) return;

      const marker = this.markers.find(m =>
          m.getPosition().lat() === ubicacion.latitud &&
          m.getPosition().lng() === ubicacion.longitud
      );

      if (marker) {
        if (this.infoWindow) {
          this.infoWindow.close();
        }

        this.infoWindow = new google.maps.InfoWindow({
          content: `
            <div class="map-info-window ${this.darkMode ? 'dark' : ''}">
              <h4>${ubicacion.nombre}</h4>
              <p><strong>Coordenadas:</strong> ${ubicacion.latitud.toFixed(4)}, ${ubicacion.longitud.toFixed(4)}</p>
            </div>
          `
        });

        this.hoveredMarker = marker;
        this.infoWindow.open(this.map, marker);
      }
    },
    hideInfoWindow() {
      if (this.infoWindow) {
        this.infoWindow.close();
        this.infoWindow = null;
        this.hoveredMarker = null;
      }
    },
    openModal(ubicacion) {
      this.selectedUbicacion = ubicacion ? { ...ubicacion } : null;
      this.showModal = true;
    },
    closeModal() {
      this.showModal = false;
      this.selectedUbicacion = null;
    },
    async handleSave(ubicacionData) {
      try {
        const method = ubicacionData.id ? 'PUT' : 'POST';
        const url = ubicacionData.id
            ? `http://localhost:8080/api/ubicaciones/${ubicacionData.id}`
            : 'http://localhost:8080/api/ubicaciones';

        const response = await fetch(url, {
          method,
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(ubicacionData)
        });

        if (!response.ok) {
          throw new Error('Error al guardar la ubicación');
        }

        this.fetchUbicaciones();
        this.closeModal();
      } catch (error) {
        console.error('Error:', error);
        alert('Error al guardar la ubicación');
      }
    },
    confirmDelete(ubicacion) {
      if (confirm(`¿Estás seguro de que quieres eliminar "${ubicacion.nombre}"?`)) {
        this.deleteUbicacion(ubicacion.id);
      }
    },
    async deleteUbicacion(id) {
      try {
        const response = await fetch(`http://localhost:8080/api/ubicaciones/${id}`, {
          method: 'DELETE'
        });

        if (!response.ok) {
          throw new Error('Error al eliminar la ubicación');
        }

        this.fetchUbicaciones();
      } catch (error) {
        console.error('Error:', error);
        alert('Error al eliminar la ubicación');
      }
    }
  }
}
</script>

<style scoped>
.ubicaciones-container {
  margin-left: 50px;
  margin-top: 26px;
  width: calc(100% - 100px);
  min-height: 50vh;
  transition: background-color 0.3s ease;
  color: var(--text-primary);
}

.main-content {
  display: flex;
  gap: 2rem;
  height: calc(100vh - 180px);
}

.map-section {
  flex: 1;
  min-width: 0;
  margin-right: 20px;
}

.map-container {
  position: relative;
  height: 100%;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px var(--shadow-color);
  border: 1px solid var(--border-color);
}

.table-section {
  width: 43%;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.search-container {
  display: flex;
  margin-bottom: 1.5rem;
}

.search-box {
  position: relative;
  flex-grow: 1;
  display: flex;
  align-items: center;
}

.search-box i.fa-search {
  position: absolute;
  left: 12px;
  color: var(--text-secondary);
}

.search-box input {
  width: 100%;
  padding: 0.75rem 1rem 0.75rem 2.5rem;
  border-radius: 8px;
  border: 1px solid var(--border-color);
  background: var(--card-bg);
  color: var(--text-primary);
  transition: all 0.3s ease;
  font-size: 1rem;
}

.search-box input:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(126, 91, 239, 0.2);
}

.btn-new-icon {
  position: absolute;
  right: 8px;
  background: var(--primary-color);
  color: white;
  border: none;
  border-radius: 6px;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-new-icon:hover {
  background: var(--primary-hover);
  transform: scale(1.05);
}

.table-container {
  background: var(--card-bg);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px var(--shadow-color);
  margin-bottom: 1.5rem;
  border: 1px solid var(--border-color);
  flex-grow: 1;
  overflow-y: auto;
}

.ubicaciones-table {
  width: 100%;
  border-collapse: collapse;
}

.ubicaciones-table th,
.ubicaciones-table td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid var(--border-color);
}

.ubicaciones-table th {
  background: var(--table-header-bg);
  font-weight: 600;
  color: var(--text-primary);
  padding: 1rem;
  position: sticky;
  top: 0;
  z-index: 10;
  border-bottom: 2px solid var(--border-color);
}

.ubicaciones-table th:hover {
  background: var(--table-header-hover);
  cursor: pointer;
}

.ubicaciones-table tr:last-child td {
  border-bottom: none;
}

.ubicaciones-table tr:hover td {
  background: var(--table-row-hover);
}

.ubicaciones-table .even-row td {
  background: var(--table-row-even);
}

.ubicaciones-table .even-row:hover td {
  background: var(--table-row-even-hover);
}

.ubicaciones-table .odd-row td {
  background: var(--card-bg);
}

.ubicaciones-table .odd-row:hover td {
  background: var(--table-row-hover);
}

.actions {
  display: flex;
  gap: 0.5rem;
}

.btn-edit, .btn-delete, .btn-view {
  padding: 0.5rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
}

.btn-edit {
  background: rgba(59, 130, 246, 0.1);
  color: var(--info-color);
}

.btn-edit:hover {
  background: rgba(59, 130, 246, 0.2);
}

.btn-delete {
  background: rgba(239, 68, 68, 0.1);
  color: var(--danger-color);
}

.btn-delete:hover {
  background: rgba(239, 68, 68, 0.2);
}

.btn-view {
  background: rgba(16, 185, 129, 0.1);
  color: var(--success-color);
}

.btn-view:hover {
  background: rgba(16, 185, 129, 0.2);
}

.no-results {
  text-align: center;
  padding: 2rem;
  color: var(--text-secondary);
}

.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1rem;
  margin-bottom: 2rem;
  margin-top: auto;
}

.pagination-btn {
  padding: 0.5rem 1rem;
  border: 1px solid var(--border-color);
  background: var(--card-bg);
  color: var(--text-primary);
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination-btn:hover:not(:disabled) {
  background: var(--primary-color);
  color: white;
  border-color: var(--primary-color);
}

.page-info {
  font-size: 0.9rem;
  color: var(--text-secondary);
}

.google-map {
  width: 100%;
  height: 100%;
}

.loading-overlay, .empty-state {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: var(--card-bg);
}

.loading-overlay {
  color: var(--text-primary);
  z-index: 10;
}

.empty-state {
  color: var(--text-secondary);
}

.empty-state i {
  font-size: 3rem;
  margin-bottom: 1rem;
  color: var(--text-secondary);
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid rgba(126, 91, 239, 0.2);
  border-radius: 50%;
  border-top-color: var(--primary-color);
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Dark mode variables */
:root {
  --primary-color: #7e5bef;
  --primary-hover: #6d46e8;
  --success-color: #10b981;
  --danger-color: #ef4444;
  --warning-color: #f59e0b;
  --info-color: #3b82f6;
  --table-header-bg: #f1f5f9;
  --table-header-hover: rgba(126, 91, 239, 0.05);
  --table-row-even: #f8fafc;
  --table-row-even-hover: rgba(126, 91, 239, 0.03);
  --table-row-hover: rgba(126, 91, 239, 0.03);
  --bg-color: #f8fafc;
  --card-bg: #ffffff;
  --border-color: #e2e8f0;
  --text-primary: #1e293b;
  --text-secondary: #64748b;
  --shadow-color: rgba(0, 0, 0, 0.05);
}

.dark-mode {
  --primary-color: #8b5cf6;
  --primary-hover: #7c3aed;
  --success-color: #10b981;
  --danger-color: #ef4444;
  --warning-color: #f59e0b;
  --info-color: #3b82f6;
  --table-header-bg: #1e293b;
  --table-header-hover: rgba(126, 91, 239, 0.1);
  --table-row-even: #1e293b;
  --table-row-even-hover: rgba(126, 91, 239, 0.05);
  --table-row-hover: rgba(126, 91, 239, 0.05);
  --bg-color: #0f172a;
  --card-bg: #1e293b;
  --border-color: #334155;
  --text-primary: #f8fafc;
  --text-secondary: #94a3b8;
  --shadow-color: rgba(0, 0, 0, 0.2);
}

/* Estilos para la ventana de información del mapa */
.map-info-window {
  padding: 12px;
  color: #1e293b;
  font-family: Arial, sans-serif;
  min-width: 200px;
  background-color: white;
  border-radius: 8px;
}

.map-info-window.dark {
  background-color: #1e293b;
  color: #f8fafc;
}

.map-info-window h4 {
  margin: 0 0 8px 0;
  font-size: 16px;
  color: inherit;
  font-weight: 600;
}

.map-info-window p {
  margin: 4px 0;
  font-size: 14px;
  color: inherit;
}

.map-info-window.dark p strong {
  color: #94a3b8;
}

/* Responsive */
@media (max-width: 1024px) {
  .main-content {
    flex-direction: column;
    height: auto;
  }

  .map-section {
    width: 100%;
    height: 400px;
    margin-right: 0;
    margin-bottom: 20px;
  }

  .table-section {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .ubicaciones-container {
    margin-left: 20px;
    margin-right: 20px;
    width: calc(100% - 40px);
  }

  .ubicaciones-table {
    display: block;
    overflow-x: auto;
  }
}

.ubicaciones-table tbody tr {
  cursor: pointer;
}
</style>