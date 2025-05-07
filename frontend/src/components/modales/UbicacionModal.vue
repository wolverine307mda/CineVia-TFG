<template>
  <transition name="modal">
    <div class="modal-overlay" @click.self="close">
      <div class="modal-container">
        <div class="modal-header">
          <h2>{{ ubicacion ? 'Editar Ubicación' : 'Nueva Ubicación' }}</h2>
          <button @click="close" class="modal-close">
            <i class="fas fa-times"></i>
          </button>
        </div>

        <div class="modal-body">
          <form @submit.prevent="save">
            <div class="form-group">
              <label for="nombre">Nombre</label>
              <input
                  id="nombre"
                  v-model="formData.nombre"
                  type="text"
                  required
                  placeholder="Nombre de la ubicación"
              >
            </div>

            <div class="form-row">
              <div class="form-group">
                <label for="latitud">Latitud</label>
                <input
                    id="latitud"
                    v-model.number="formData.latitud"
                    type="number"
                    step="0.000001"
                    required
                    placeholder="Ej: 40.4168"
                >
              </div>

              <div class="form-group">
                <label for="longitud">Longitud</label>
                <input
                    id="longitud"
                    v-model.number="formData.longitud"
                    type="number"
                    step="0.000001"
                    required
                    placeholder="Ej: -3.7038"
                >
              </div>
            </div>

            <div class="map-preview">
              <div ref="previewMap" class="preview-map"></div>
              <small>Haz clic en el mapa para cambiar las coordenadas</small>
            </div>

            <div class="modal-actions">
              <button type="button" @click="close" class="btn-cancel">
                Cancelar
              </button>
              <button type="submit" class="btn-save">
                {{ ubicacion ? 'Actualizar' : 'Guardar' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </transition>
</template>

<script>
import { Loader } from '@googlemaps/js-api-loader';

const loader = new Loader({
  apiKey: "AIzaSyCtMIHC_PntZzdioTFmRcamhjRNKZMw4hc",
  version: "weekly",
  libraries: ["places"]
});

export default {
  name: 'UbicacionModal',
  props: {
    ubicacion: {
      type: Object,
      default: null
    },
    show: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      formData: {
        nombre: '',
        latitud: null,
        longitud: null,
        produccionId: null
      },
      previewMap: null,
      marker: null,
      clickListener: null,
      mapLoaded: false // Nuevo flag para controlar carga
    }
  },
  watch: {
    ubicacion: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.formData = { ...newVal };
        } else {
          this.formData = {
            nombre: '',
            latitud: null,
            longitud: null,
            produccionId: null
          };
        }
        this.$nextTick(() => {
          if (this.show) {
            this.initPreviewMap();
          }
        });
      }
    },
    show(newVal) {
      if (newVal && !this.previewMap) {
        this.$nextTick(() => {
          this.initPreviewMap();
        });
      }
    }
  },
  methods: {
    close() {
      this.$emit('close');
    },
    save() {
      this.$emit('save', this.formData);
    },
    async initPreviewMap() {
      if (!this.$refs.previewMap || this.previewMap) return;

      try {
        // Cargar la API solo si no está cargada
        if (!this.mapLoaded) {
          await loader.load();
          this.mapLoaded = true;
        }

        const initialPosition = this.formData.latitud && this.formData.longitud
            ? { lat: this.formData.latitud, lng: this.formData.longitud }
            : { lat: 0, lng: 0 };

        this.previewMap = new google.maps.Map(this.$refs.previewMap, {
          center: initialPosition,
          zoom: this.formData.latitud ? 12 : 2,
          disableDefaultUI: true,
          clickableIcons: false
        });

        this.marker = new google.maps.Marker({
          position: initialPosition,
          map: this.previewMap,
          title: this.formData.nombre || 'Nueva ubicación',
          draggable: true
        });

        this.marker.addListener('dragend', (event) => {
          this.updateFormFromPosition(event.latLng);
        });

        this.clickListener = this.previewMap.addListener('click', (event) => {
          this.updateFormFromPosition(event.latLng);
          this.marker.setPosition(event.latLng);
        });

      } catch (error) {
        console.error('Error al cargar el mapa de previsualización:', error);
      }
    },
    updateFormFromPosition(latLng) {
      this.formData.latitud = parseFloat(latLng.lat().toFixed(6));
      this.formData.longitud = parseFloat(latLng.lng().toFixed(6));
    },
    updateMarker() {
      if (!this.previewMap || !this.marker) return;

      const newPos = {
        lat: parseFloat(this.formData.latitud),
        lng: parseFloat(this.formData.longitud)
      };

      this.marker.setPosition(newPos);
      this.previewMap.setCenter(newPos);
      this.previewMap.setZoom(12);
    }
  },
  beforeDestroy() {
    if (this.clickListener) {
      google.maps.event.removeListener(this.clickListener);
    }
  }
}
</script>

<style scoped>
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
  animation: fadeIn 0.3s ease;
}

.modal-container {
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
  width: 100%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  animation: slideUp 0.3s ease;
}

.dark-mode .modal-container {
  background: #1e293b;
  color: white;
}

.modal-header {
  padding: 1.5rem;
  border-bottom: 1px solid #e2e8f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dark-mode .modal-header {
  border-bottom-color: #334155;
}

.modal-header h2 {
  font-size: 1.5rem;
  margin: 0;
  color: inherit;
}

.modal-close {
  background: none;
  border: none;
  font-size: 1.2rem;
  color: #64748b;
  cursor: pointer;
  padding: 0.5rem;
  transition: all 0.2s ease;
}

.dark-mode .modal-close {
  color: #94a3b8;
}

.modal-close:hover {
  color: #ef4444;
  transform: rotate(90deg);
}

.modal-body {
  padding: 1.5rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: inherit;
}

.form-group input {
  width: 100%;
  padding: 0.75rem;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
  background: white;
  color: #1e293b;
  transition: all 0.3s ease;
}

.dark-mode .form-group input {
  background: #334155;
  border-color: #475569;
  color: white;
}

.form-group input:focus {
  outline: none;
  border-color: #7e5bef;
  box-shadow: 0 0 0 2px rgba(126, 91, 239, 0.2);
}

.dark-mode .form-group input:focus {
  border-color: #8b5cf6;
  box-shadow: 0 0 0 2px rgba(139, 92, 246, 0.2);
}

.form-row {
  display: flex;
  gap: 1rem;
}

.form-row .form-group {
  flex: 1;
}

.map-preview {
  margin: 1.5rem 0;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #e2e8f0;
}

.dark-mode .map-preview {
  border-color: #334155;
}

.preview-map {
  height: 250px;
  width: 100%;
}

.map-preview small {
  display: block;
  margin-top: 0.5rem;
  font-size: 0.8rem;
  color: #64748b;
  text-align: center;
}

.dark-mode .map-preview small {
  color: #94a3b8;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 2rem;
}

.btn-cancel, .btn-save {
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.btn-cancel {
  background: white;
  border: 1px solid #e2e8f0;
  color: #1e293b;
}

.dark-mode .btn-cancel {
  background: #334155;
  border-color: #475569;
  color: white;
}

.btn-cancel:hover {
  background: rgba(239, 68, 68, 0.1);
  border-color: #ef4444;
  color: #ef4444;
}

.btn-save {
  background: #7e5bef;
  border: 1px solid #7e5bef;
  color: white;
}

.dark-mode .btn-save {
  background: #8b5cf6;
  border-color: #8b5cf6;
}

.btn-save:hover {
  background: #6d46e8;
  border-color: #6d46e8;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 600px) {
  .modal-container {
    margin: 1rem;
    max-height: 80vh;
  }

  .form-row {
    flex-direction: column;
    gap: 0;
  }
}
</style>