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
        <div class="search-header">
          <div class="search-bar">
            <div class="search-input-container">
              <i class="fas fa-search search-icon"></i>
              <input
                  v-model="searchQuery"
                  placeholder="Buscar ubicaciones..."
                  type="search"
                  class="search-input"
              >
              <button @click="fetchUbicaciones" class="search-button">
                Buscar
              </button>
            </div>
            <button @click="openModal(null)" class="new-button">
              <i class="fas fa-plus"></i>
              <span>Nueva Ubicación</span>
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
              <th style="text-align: center;">Acciones</th>
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
              <td>
                <div class="action-buttons">
                  <button @click="openModal(ubicacion)" class="btn-edit" title="Editar">
                    <i class="fas fa-edit"></i>
                  </button>
                  <button @click="confirmDelete(ubicacion)" class="btn-delete" title="Eliminar">
                    <i class="fas fa-trash"></i>
                  </button>
                  <button @click="centerMap(ubicacion)" class="btn-view" title="Centrar en mapa">
                    <i class="fas fa-map-marker-alt"></i>
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="ubicaciones.length === 0 && !loading">
              <td colspan="5" class="no-results">
                No se encontraron ubicaciones
              </td>
            </tr>
            <tr v-if="loading">
              <td colspan="5" class="loading-row">
                <i class="fas fa-spinner fa-spin"></i> Cargando...
              </td>
            </tr>
            </tbody>
          </table>
        </div>

        <!-- Paginación -->
        <div class="pagination-container">
          <button
              @click="prevPage"
              :disabled="currentPage === 1 || loading"
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
              :disabled="currentPage >= totalPages || loading"
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

    <!-- Modal de confirmación para eliminar -->
    <div v-if="showConfirmModal" class="modal-overlay">
      <div class="delete-confirmation-modal">
        <div class="modal-header">
          <h3>Confirmar eliminación</h3>
        </div>
        <div class="modal-body">
          <p>¿Estás completamente seguro de que deseas eliminar la ubicación "{{ ubicacionToDelete?.nombre }}"?</p>
          <p>La acción no se podrá revertir, y todas las referencias a esta ubicación se perderán.</p>
          <p>¿Deseas continuar?</p>
        </div>
        <div class="modal-footer">
          <button @click="showConfirmModal = false" class="cancel-btn">Cancelar</button>
          <button @click="deleteUbicacion" class="confirm-delete-btn">Eliminar</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Loader } from '@googlemaps/js-api-loader';
import UbicacionModal from "@/components/modales/edicion/UbicacionModal.vue";
import {getGoogleMapsLoader} from "@/utils/googleMapsLoader.js";

const API_BASE_URL = import.meta.env.VITE_API_URL;

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
      showConfirmModal: false,
      selectedUbicacion: null,
      ubicacionToDelete: null,
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
            `${API_BASE_URL}/api/ubicaciones/filter?page=${this.currentPage - 1}&size=${this.itemsPerPage}&sortBy=nombre&sortDirection=${this.sortDirection}`,
            {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${localStorage.getItem('token')}` // si usas autenticación
              },
              body: JSON.stringify({
                nombre: this.searchQuery || null
              })
            }
        );

        if (!response.ok) {
          throw new Error('Error al cargar ubicaciones');
        }

        const data = await response.json();
        this.ubicaciones = data.data;
        this.totalItems = data.totalItems;
        this.totalPages = data.totalPages;
        this.currentPage = data.currentPage + 1 || 1;
      } catch (error) {
        console.error('Error:', error);
        this.$toast.error('Error al cargar las ubicaciones');
      } finally {
        this.loading = false;
      }
      this.updateMapMarkers();
    },
    async initMap() {
      try {
        const loader = getGoogleMapsLoader();

        await loader.load();

        this.map = new google.maps.Map(this.$refs.map, {
          center: {lat: 20, lng: 0},
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
          position: {lat: ubicacion.latitud, lng: ubicacion.longitud},
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
        this.map.fitBounds(bounds, {top: 50, right: 50, bottom: 50, left: 50});
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
      this.currentPage = 1;
      this.fetchUbicaciones();
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
        this.fetchUbicaciones();
      }
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
        this.fetchUbicaciones();
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
      this.selectedUbicacion = ubicacion ? {...ubicacion} : null;
      this.showModal = true;
    },
    closeModal() {
      this.showModal = false;
      this.selectedUbicacion = null;
    },
    async handleSave(ubicacionData) {
      try {
        const isEdit = !!ubicacionData.id;
        const url = isEdit
            ? `${API_BASE_URL}/api/ubicaciones/${ubicacionData.id}`
            : `${API_BASE_URL}/api/ubicaciones`;

        const method = isEdit ? 'PUT' : 'POST';

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

        this.$toast.success('Ubicación guardada correctamente');
        this.fetchUbicaciones();
        this.closeModal();
      } catch (error) {
        console.error('Error:', error);
        this.$toast.error('Error al guardar la ubicación');
      }
    },
    confirmDelete(ubicacion) {
      this.ubicacionToDelete = ubicacion;
      this.showConfirmModal = true;
    },
    async deleteUbicacion() {
      try {
        const response = await fetch(`${API_BASE_URL}/api/ubicaciones/${this.ubicacionToDelete.id}`, {
          method: 'DELETE'
        });

        if (!response.ok) {
          throw new Error('Error al eliminar la ubicación');
        }

        this.$toast.success('Ubicación eliminada correctamente');
        this.fetchUbicaciones();
      } catch (error) {
        console.error('Error:', error);
        this.$toast.error('Error al eliminar la ubicación');
      } finally {
        this.showConfirmModal = false;
        this.ubicacionToDelete = null;
      }
    }
  }
}
</script>

<style scoped>
/* Contenedor principal */
.ubicaciones-container {
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px;
  font-family: 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.main-content {
  display: flex;
  gap: 24px;
  height: calc(100vh - 180px);
}

/* Sección del mapa */
.map-section {
  flex: 1;
  min-width: 0;
}

.map-container {
  position: relative;
  height: 100%;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border: 1px solid #e2e8f0;
}

.dark-mode .map-container {
  border-color: #334155;
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
  background: #ffffff;
}

.dark-mode .loading-overlay,
.dark-mode .empty-state {
  background: #1e293b;
}

.loading-overlay {
  color: #2d3748;
  z-index: 10;
}

.dark-mode .loading-overlay {
  color: #e2e8f0;
}

.empty-state {
  color: #718096;
}

.empty-state i {
  font-size: 3rem;
  margin-bottom: 1rem;
  color: #a0aec0;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid rgba(66, 153, 225, 0.2);
  border-radius: 50%;
  border-top-color: #4299e1;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

/* Sección de la tabla */
.table-section {
  width: 45%;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.search-header {
  margin-bottom: 20px;
  width: 100%;
}

.search-bar {
  display: flex;
  gap: 16px;
  align-items: center;
  width: 100%;
}

.search-input-container {
  flex: 1;
  position: relative;
  display: flex;
  align-items: center;
  background: #ffffff;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  overflow: hidden;
  height: 44px;
  transition: all 0.3s ease;
}

.dark-mode .search-input-container {
  background: #2d3748;
  border-color: #4a5568;
}

.search-input-container:focus-within {
  border-color: #4299e1;
  box-shadow: 0 0 0 3px rgba(66, 153, 225, 0.2);
}

.search-icon {
  position: absolute;
  left: 14px;
  color: #a0aec0;
  font-size: 16px;
}

.search-input {
  flex: 1;
  padding: 0 14px 0 42px;
  border: none;
  background: transparent;
  color: #2d3748;
  font-size: 14px;
  outline: none;
  height: 100%;
}

.dark-mode .search-input {
  color: #e2e8f0;
}

.search-button {
  padding: 0 20px;
  height: 44px;
  background: #4299e1;
  color: white;
  border: none;
  cursor: pointer;
  font-weight: 500;
  font-size: 14px;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
}

.search-button:hover {
  background: #3182ce;
}

.new-button {
  padding: 0 20px;
  height: 44px;
  background: #10b981;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 500;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
  transition: all 0.2s ease;
  white-space: nowrap;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}

.new-button:hover {
  background: #0d9f6e;
  transform: translateY(-1px);
}

.new-button:active {
  transform: translateY(0);
}

/* Tabla de ubicaciones */
.table-container {
  background: #ffffff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  border: 1px solid #e2e8f0;
  flex-grow: 1;
  overflow-y: auto;
}

.dark-mode .table-container {
  background: #1e293b;
  border-color: #334155;
}

.ubicaciones-table {
  width: 100%;
  border-collapse: collapse;
}

.ubicaciones-table th,
.ubicaciones-table td {
  padding: 14px 16px;
  text-align: left;
  border-bottom: 1px solid #e2e8f0;
}

.dark-mode .ubicaciones-table th,
.dark-mode .ubicaciones-table td {
  border-bottom-color: #334155;
}

.ubicaciones-table th {
  background: #f7fafc;
  font-weight: 600;
  color: #2d3748;
  padding: 14px 16px;
  position: sticky;
  top: 0;
  z-index: 10;
  border-bottom: 2px solid #e2e8f0;
}

.dark-mode .ubicaciones-table th {
  background: #1e293b;
  color: #e2e8f0;
  border-bottom-color: #334155;
}

.ubicaciones-table th:hover {
  background: rgba(66, 153, 225, 0.05);
  cursor: pointer;
}

.dark-mode .ubicaciones-table th:hover {
  background: rgba(66, 153, 225, 0.1);
}

.ubicaciones-table tr:last-child td {
  border-bottom: none;
}

.ubicaciones-table tr:hover td {
  background: rgba(66, 153, 225, 0.03);
}

.dark-mode .ubicaciones-table tr:hover td {
  background: rgba(66, 153, 225, 0.05);
}

.ubicaciones-table .even-row td {
  background: #f8fafc;
}

.dark-mode .ubicaciones-table .even-row td {
  background: #1e293b;
}

.ubicaciones-table .even-row:hover td {
  background: rgba(66, 153, 225, 0.03);
}

.dark-mode .ubicaciones-table .even-row:hover td {
  background: rgba(66, 153, 225, 0.07);
}

.ubicaciones-table .odd-row td {
  background: #ffffff;
}

.dark-mode .ubicaciones-table .odd-row td {
  background: #1e293b;
}

.ubicaciones-table .odd-row:hover td {
  background: rgba(66, 153, 225, 0.03);
}

.dark-mode .ubicaciones-table .odd-row:hover td {
  background: rgba(66, 153, 225, 0.07);
}

/* Botones de acción */
.actions {
  display: flex;
  gap: 8px;
  justify-content: center;
}

.btn-edit, .btn-delete, .btn-view {
  padding: 6px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  font-size: 14px;
}

.btn-edit {
  background: rgba(59, 130, 246, 0.1);
  color: #3b82f6;
}

.btn-edit:hover {
  background: rgba(59, 130, 246, 0.2);
}

.btn-delete {
  background: rgba(239, 68, 68, 0.1);
  color: #ef4444;
}

.btn-delete:hover {
  background: rgba(239, 68, 68, 0.2);
}

.btn-view {
  background: rgba(16, 185, 129, 0.1);
  color: #10b981;
}

.btn-view:hover {
  background: rgba(16, 185, 129, 0.2);
}

.no-results, .loading-row {
  text-align: center;
  padding: 40px;
  color: #718096;
}

.dark-mode .no-results,
.dark-mode .loading-row {
  color: #a0aec0;
}

.loading-row i {
  margin-right: 8px;
  color: #4299e1;
}

/* Paginación */
.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  margin-top: auto;
}

.pagination-btn {
  width: 36px;
  height: 36px;
  border: 1px solid #e2e8f0;
  background: #ffffff;
  color: #4a5568;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.dark-mode .pagination-btn {
  background: #2d3748;
  border-color: #4a5568;
  color: #cbd5e0;
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination-btn:hover:not(:disabled) {
  background: #4299e1;
  color: white;
  border-color: #4299e1;
}

.page-info {
  font-size: 14px;
  color: #718096;
  min-width: 120px;
  text-align: center;
}

.dark-mode .page-info {
  color: #a0aec0;
}

/* Modal de confirmación */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.delete-confirmation-modal {
  background: #ffffff;
  border-radius: 8px;
  border: 2px solid #ef4444;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  width: 90%;
  max-width: 500px;
  overflow: hidden;
  animation: modalFadeIn 0.3s ease;
}

.dark-mode .delete-confirmation-modal {
  background: #1e293b;
  border-color: #ef4444;
}

.modal-header {
  padding: 16px 20px;
  background: #ef4444;
  color: white;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
}

.modal-body {
  padding: 20px;
  color: #2d3748;
}

.dark-mode .modal-body {
  color: #e2e8f0;
}

.modal-body p {
  margin-bottom: 12px;
  line-height: 1.5;
  font-size: 14px;
}

.modal-footer {
  padding: 16px 20px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  background: #f7fafc;
}

.dark-mode .modal-footer {
  background: #1e293b;
}

.cancel-btn {
  padding: 8px 16px;
  background: #ffffff;
  color: #2d3748;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
}

.dark-mode .cancel-btn {
  background: #334155;
  color: #e2e8f0;
  border-color: #475569;
}

.cancel-btn:hover {
  background: #f1f5f9;
}

.dark-mode .cancel-btn:hover {
  background: #475569;
}

.confirm-delete-btn {
  padding: 8px 16px;
  background: #ef4444;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
}

.confirm-delete-btn:hover {
  background: #dc2626;
}

/* Animaciones */
@keyframes modalFadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* Estilos para la ventana de información del mapa */
.map-info-window {
  padding: 12px;
  color: #2d3748;
  font-family: Arial, sans-serif;
  min-width: 200px;
  background-color: white;
  border-radius: 8px;
}

.dark-mode .map-info-window {
  background-color: #1e293b;
  color: #e2e8f0;
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

/* Responsive */
@media (max-width: 1200px) {
  .main-content {
    flex-direction: column;
    height: auto;
  }

  .map-section {
    width: 100%;
    height: 400px;
    margin-bottom: 20px;
  }

  .table-section {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .ubicaciones-container {
    padding: 16px;
  }

  .search-bar {
    flex-direction: column;
  }

  .search-input-container,
  .new-button {
    width: 100%;
  }

  .ubicaciones-table th,
  .ubicaciones-table td {
    padding: 12px;
    font-size: 14px;
  }
}

.action-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 8px;
  height: 100%;
}

</style>