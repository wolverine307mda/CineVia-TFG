<template>
  <Header
      :dark-mode="darkMode"
      @toggle-dark-mode="toggleDarkMode"
  />

  <div class="movie-page" :class="{ 'dark-mode': darkMode }">
    <!-- Loading State -->
    <div v-if="loading" class="loading-container">
      <div class="spinner"></div>
      <p>Cargando producción...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="error-container">
      <i class="fas fa-exclamation-triangle"></i>
      <p>{{ error }}</p>
      <button @click="retryLoading" class="btn btn-primary">
        <i class="fas fa-sync-alt"></i> Intentar nuevamente
      </button>
    </div>

    <!-- Content -->
    <template v-else-if="movie">
      <!-- Hero Section -->
      <section class="movie-hero">
        <div class="hero-backdrop" :style="{ 'background-image': `url(${movie.imagen})` }"></div>
        <div class="hero-overlay"></div>
        <div class="hero-content">
          <div class="movie-poster-container">
            <img :src="movie.imagen" :alt="movie.titulo" class="movie-poster" loading="lazy">
            <div class="movie-badges">
              <span class="badge age-rating">{{ movie.clasificacionEdad }}+</span>
              <span class="badge runtime">{{ formatRuntime(movie.duracion) }}</span>
            </div>
          </div>
          <div class="movie-info">
            <div class="categories-container" v-if="movie.categorias && movie.categorias.length > 0">
              <span class="category-badge" v-for="(categoria, index) in movie.categorias" :key="index">
                {{ categoria }}
              </span>
            </div>
            <h1 class="movie-title">{{ movie.titulo }}</h1>
            <div class="movie-meta">
              <span class="release-year">{{ new Date(movie.estreno).getFullYear() }}</span>
              <span class="genre">{{ movie.tipo }}</span>
              <span class="rating" v-if="movie.puntuacion">
                <i class="fas fa-star"></i> {{ movie.puntuacion.toFixed(1) }}
              </span>
            </div>
          </div>
        </div>
      </section>

      <!-- Main Content -->
      <div class="movie-main">
        <!-- Left Column -->
        <div class="movie-content">
          <!-- Synopsis -->
          <section class="movie-section">
            <h2 class="section-title" style="font-size: 3rem">Sinopsis</h2>
            <p class="movie-sinopsis">{{ movie.sinopsis }}</p>
          </section>

          <!-- Additional Info -->
          <section class="movie-section" v-if="movie.informacion">
            <h2 class="section-title">Información adicional</h2>
            <p class="movie-filming-info">{{ movie.informacion }}</p>
          </section>

          <!-- Cast -->
          <section class="movie-section" v-if="movie.participacionesCompletas && movie.participacionesCompletas.length > 0">
            <h2 class="section-title">Reparto</h2>
            <div class="cast-scroller">
              <div class="cast-grid">
                <div v-for="participacion in movie.participacionesCompletas" :key="participacion.id" class="cast-card-wrapper">
                  <router-link :to="`/profesional/${participacion.profesional.id}`" class="cast-card">
                    <img
                        v-if="participacion.profesional.foto"
                        :src="participacion.profesional.foto"
                        :alt="participacion.profesional.nombre"
                        class="cast-photo"
                        loading="lazy"
                    >
                    <div v-else class="cast-photo placeholder">
                      <i class="fas fa-user"></i>
                    </div>
                    <div class="cast-info">
                      <h4>{{ participacion.profesional.nombre }}</h4>
                      <p class="character">{{ participacion.papel || participacion.rol }}</p>
                      <p class="role">{{ participacion.rol }}</p>
                    </div>
                  </router-link>
                </div>

              </div>
            </div>
          </section>
        </div>

        <!-- Right Column -->
        <div class="movie-sidebar">
          <!-- Technical Data -->
          <section class="sidebar-section">
            <h3 class="sidebar-title">Datos técnicos</h3>
            <ul class="movie-facts">
              <li>
                <span class="fact-label">Título</span>
                <span class="fact-value">{{ movie.titulo }}</span>
              </li>
              <li>
                <span class="fact-label">Duración</span>
                <span class="fact-value">{{ formatRuntime(movie.duracion) }}</span>
              </li>
              <li>
                <span class="fact-label">Estreno</span>
                <span class="fact-value">{{ formatFullDate(movie.estreno) }}</span>
              </li>
              <li>
                <span class="fact-label">Tipo</span>
                <span class="fact-value">{{ movie.tipo }}</span>
              </li>
              <li>
                <span class="fact-label">Clasificación</span>
                <span class="fact-value">{{ movie.clasificacionEdad }}+</span>
              </li>
              <li v-if="movie.puntuacion">
                <span class="fact-label">Puntuación</span>
                <span class="fact-value">{{ movie.puntuacion.toFixed(1) }}/10</span>
              </li>
            </ul>
          </section>

          <!-- Saga -->
          <section class="sidebar-section" v-if="movie.sagaCompleta">
            <div class="saga-info">
              <h4>
                <a :href="`/saga/${movie.sagaCompleta.id}`" style="text-decoration: none; color: #7e5bef">
                  {{ movie.sagaCompleta.nombre }}
                </a>
              </h4>
              <p v-if="movie.sagaCompleta.descripcion">{{ movie.sagaCompleta.descripcion }}</p>
              <div class="saga-movies">
                <router-link  v-for="(film, index) in movie.sagaCompleta.producciones" :key="film.id" :to="`/produccion/${film.id}`" class="saga-movie" :class="{ 'current': film.id === movie.id }">
                  <span class="saga-number">{{ index + 1 }}</span>
                  <span class="saga-title">{{ film.titulo }}</span>
                  <span class="saga-year">{{ new Date(film.estreno).getFullYear() }}</span>
                </router-link>
              </div>
            </div>
          </section>

          <!-- Production Gallery -->
          <section class="sidebar-section" v-if="productionImages.length > 0">
            <h3 class="sidebar-title">Imágenes de la producción</h3>
            <div class="gallery-grid">
              <div class="gallery-item" v-for="(image, index) in productionImages" :key="index">
                <img :src="image" :alt="`${movie.titulo} - Imagen ${index + 1}`" loading="lazy">
              </div>
            </div>
          </section>
        </div>
      </div>

      <!-- Shooting Locations -->
      <section class="movie-section full-width-section" style="margin: 4rem" v-if="movie.rodajesCompletos && movie.rodajesCompletos.length > 0">
        <h2 class="section-title">Ubicaciones de rodaje</h2>
        <div class="locations-container">
          <div v-for="(rodaje, index) in movie.rodajesCompletos" :key="rodaje.id" class="location-card">
            <div
                class="location-map"
                :ref="el => registerMapContainer(el, index)"
            ></div>
            <div class="location-info">
              <h3>{{ rodaje.ubicacion.nombre }}</h3>
              <p v-if="rodaje.notas" class="location-notes">{{ rodaje.notas }}</p>

              <div class="location-gallery-preview" v-if="rodaje.imagenes && rodaje.imagenes.length > 0">
                <div class="preview-images">
                  <img
                      v-for="(image, idx) in rodaje.imagenes.slice(0, 3)"
                      :key="idx"
                      :src="image"
                      :alt="`Imagen ${idx + 1} de ${rodaje.ubicacion.nombre}`"
                      loading="lazy"
                      @click="openGalleryModal(rodaje.imagenes, rodaje.ubicacion.nombre)"
                  >
                  <div
                      class="more-images-overlay"
                      v-if="rodaje.imagenes.length > 3"
                      @click="openGalleryModal(rodaje.imagenes, rodaje.ubicacion.nombre)"
                  >
                    +{{ rodaje.imagenes.length - 3 }}
                  </div>
                </div>
              </div>

              <div class="location-actions">
                <button
                    class="btn btn-gallery"
                    v-if="rodaje.imagenes && rodaje.imagenes.length > 0"
                    @click="openGalleryModal(rodaje.imagenes, rodaje.ubicacion.nombre)"
                >
                  <i class="fas fa-images"></i> Ver galería ({{ rodaje.imagenes.length }})
                </button>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- Locations Map -->
      <section class="map-section" v-if="movie.rodajesCompletos && movie.rodajesCompletos.length > 0">
        <h2 class="section-title">Mapa de ubicaciones</h2>
        <div class="fullscreen-map" ref="fullMap"></div>
      </section>
      <br><br>

      <!-- Gallery Modal -->
      <div class="modal-overlay" v-if="showGalleryModal" @click.self="closeGalleryModal">
        <div class="gallery-modal" :class="{ 'dark-mode': darkMode }">
          <button class="modal-close" @click="closeGalleryModal">
            <i class="fas fa-times"></i>
          </button>
          <h3 class="modal-title">{{ currentGalleryTitle }}</h3>

          <div class="modal-main-image-container">
            <button class="nav-arrow left" @click.stop="prevImage">
              <i class="fas fa-chevron-left"></i>
            </button>

            <div class="main-image-wrapper">
              <img
                  :src="currentGalleryImages[currentImageIndex]"
                  :alt="`Imagen ${currentImageIndex + 1}`"
                  class="main-image"
                  loading="eager"
                  @click="toggleZoom"
                  :class="{ 'zoomed': isZoomed }"
              >
            </div>

            <button class="nav-arrow right" @click.stop="nextImage">
              <i class="fas fa-chevron-right"></i>
            </button>
          </div>

          <div class="image-counter">
            {{ currentImageIndex + 1 }} / {{ currentGalleryImages.length }}
          </div>

          <div class="thumbnails-scroller">
            <div
                v-for="(image, index) in currentGalleryImages"
                :key="index"
                class="thumbnail-container"
                :class="{ 'active': index === currentImageIndex }"
                @click="selectImage(index)"
            >
              <img
                  :src="image"
                  :alt="`Miniatura ${index + 1}`"
                  class="thumbnail"
                  loading="lazy"
              >
            </div>
          </div>
        </div>
      </div>

    </template>
  </div>

  <Footer :dark-mode="darkMode" />
</template>

<script>
import { getGoogleMapsLoader } from '@/utils/googleMapsLoader';
import peliculaService from '@/services/producciones.service.js';
import Header from "@/components/principal/Header.vue";
import Footer from "@/components/principal/Footer.vue";

export default {
  name: 'Produccion',
  components: { Footer, Header },
  mounted() {
    window.scrollTo(0, 0);
  },
  data() {
    return {
      darkMode: false,
      movie: null,
      loading: true,
      error: null,
      mapsLoaded: false,
      showGalleryModal: false,
      mapContainers: [],
      currentGalleryImages: [],
      currentGalleryTitle: '',
      currentImageIndex: 0,
      isZoomed: false,
      productionImages: []
    }
  },
  computed: {
    hasRodajeImages() {
      return this.movie?.rodajesCompletos?.some(rodaje => rodaje.imagenes?.length > 0);
    },
    allRodajeImages() {
      if (!this.movie?.rodajesCompletos) return [];
      return this.movie.rodajesCompletos
          .filter(rodaje => rodaje.imagenes?.length > 0)
          .flatMap(rodaje => rodaje.imagenes);
    }
  },
  methods: {
    async loadProductionData() {
      this.loading = true;
      this.error = null;
      this.mapContainers = [];
      try {
        const idProduccion = this.$route.params.id;
        this.movie = await peliculaService.fetchPeliculaCompleta(idProduccion);

        // Prepare production images (main image + first 3 rodaje images)
        this.productionImages = [this.movie.imagen];
        if (this.movie.rodajesCompletos) {
          for (const rodaje of this.movie.rodajesCompletos) {
            if (rodaje.imagenes && rodaje.imagenes.length > 0) {
              this.productionImages.push(rodaje.imagenes[0]);
              if (this.productionImages.length >= 4) break;
            }
          }
        }

        // Cargar mapas después de obtener los datos
        if (this.movie.rodajesCompletos && this.movie.rodajesCompletos.length > 0) {
          this.loadMaps();
        }
      } catch (error) {
        console.error('Error al cargar la producción:', error);
        this.error = 'No se pudo cargar la información de la producción.';
      } finally {
        this.loading = false;
      }
    },

    retryLoading() {
      this.loadProductionData();
    },

    toggleDarkMode() {
      this.darkMode = !this.darkMode;
      localStorage.setItem('darkMode', this.darkMode);
    },

    formatRuntime(minutes) {
      const hours = Math.floor(minutes / 60);
      const mins = minutes % 60;
      return `${hours}h ${mins}m`;
    },

    formatFullDate(dateString) {
      const options = {year: 'numeric', month: 'long', day: 'numeric'};
      return new Date(dateString).toLocaleDateString('es-ES', options);
    },

    openGalleryModal(images, title) {
      this.currentGalleryImages = images;
      this.currentGalleryTitle = `Galería: ${title}`;
      this.currentImageIndex = 0;
      this.isZoomed = false;
      this.showGalleryModal = true;
      document.body.style.overflow = 'hidden';
      window.addEventListener('keydown', this.handleKeyDown);
    },

    closeGalleryModal() {
      this.showGalleryModal = false;
      document.body.style.overflow = '';
      window.removeEventListener('keydown', this.handleKeyDown);
    },

    nextImage() {
      this.currentImageIndex = (this.currentImageIndex + 1) % this.currentGalleryImages.length;
      this.isZoomed = false;
    },

    prevImage() {
      this.currentImageIndex = (this.currentImageIndex - 1 + this.currentGalleryImages.length) % this.currentGalleryImages.length;
      this.isZoomed = false;
    },

    selectImage(index) {
      this.currentImageIndex = index;
      this.isZoomed = false;
    },

    toggleZoom() {
      this.isZoomed = !this.isZoomed;
    },

    handleKeyDown(e) {
      if (!this.showGalleryModal) return;

      switch(e.key) {
        case 'ArrowRight':
          this.nextImage();
          break;
        case 'ArrowLeft':
          this.prevImage();
          break;
        case 'Escape':
          this.closeGalleryModal();
          break;
        case ' ':
          this.toggleZoom();
          break;
      }
    },

    loadMaps() {
      const loader = getGoogleMapsLoader();
      loader.load()
          .then(() => {
            this.mapsLoaded = true;
            this.initLocationMaps();
            this.initFullMap();
          })
          .catch(error => {
            console.error('Error al cargar Google Maps:', error);
          });
    },

    registerMapContainer(el, index) {
      if (!this.mapContainers) this.mapContainers = [];
      if (el) {
        this.mapContainers[index] = el;
      }
    },
    initLocationMaps() {
      this.$nextTick(() => {
        if (!this.movie.rodajesCompletos) return;

        this.movie.rodajesCompletos.forEach((rodaje, index) => {
          if (!rodaje.ubicacion) return;

          const mapContainer = this.mapContainers[index];
          if (!mapContainer) return;

          const map = new google.maps.Map(mapContainer, {
            center: {
              lat: rodaje.ubicacion.latitud,
              lng: rodaje.ubicacion.longitud,
            },
            mapId: import.meta.env.VITE_GOOGLE_MAP_ID,
            zoom: 12,
            disableDefaultUI: true,
          });

          new google.maps.Marker({
            position: {
              lat: rodaje.ubicacion.latitud,
              lng: rodaje.ubicacion.longitud,
            },
            map,
            title: rodaje.ubicacion.nombre,
          });
        });
      });
    },

    initFullMap() {
      if (!this.movie.rodajesCompletos || this.movie.rodajesCompletos.length === 0) return;

      const bounds = new google.maps.LatLngBounds();
      const map = new google.maps.Map(this.$refs.fullMap, {
        mapId: import.meta.env.VITE_GOOGLE_MAP_ID,
        zoom: 6,
      });

      this.movie.rodajesCompletos.forEach(rodaje => {
        if (!rodaje.ubicacion) return;

        const position = new google.maps.LatLng(
            rodaje.ubicacion.latitud,
            rodaje.ubicacion.longitud
        );

        new google.maps.marker.AdvancedMarkerElement({
          position,
          map,
          title: rodaje.ubicacion.nombre
        });

        bounds.extend(position);
      });

      map.fitBounds(bounds);
    },
  },
  watch: {
    '$route.params.id': {
      immediate: true,
      handler() {
        this.loadProductionData();
      }
    }
  },
  created() {
    const savedMode = localStorage.getItem('darkMode');
    this.darkMode = savedMode === 'true';
  }
}
</script>

<style scoped>
/* Estilos base */
:root {
  --color-primary: #7e5bef;
  --color-primary-light: #a78bfa;
  --color-primary-dark: #6d46e8;
  --color-secondary: #f8f9fa;
  --color-text: #343a40;
  --color-text-light: #f8f9fa;
  --color-bg: #ffffff;
  --color-bg-dark: #121212;
  --color-card: #ffffff;
  --color-card-dark: #1e1e2e;
  --color-border: #e1e4e8;
  --color-border-dark: #2d3748;
  --transition: all 0.3s ease;
  --modal-bg: rgba(0, 0, 0, 0.8);
  --modal-bg-dark: rgba(0, 0, 0, 0.9);
  --max-section-height: 500px;
}

.dark-mode {
  --color-secondary: #2a2a3a;
  --color-text: #e9ecef;
  --color-bg: #121212;
  --color-card: #1e1e2e;
  --color-border: #2d3748;
  --modal-bg: rgba(20, 20, 30, 0.95);
}

.movie-page {
  background-color: var(--color-bg);
  color: var(--color-text);
  transition: var(--transition);
  min-height: 100vh;
}

.movie-hero {
  position: relative;
  height: 40vh;
  min-height: 370px;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.hero-backdrop {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 120%;
  background-size: cover;
  background-position: center 30%;
  filter: blur(5px);
  transform: scale(1.1);
  z-index: 0;
  transition: transform 0.5s ease, filter 0.5s ease;
}

.movie-hero:hover .hero-backdrop {
  filter: blur(3px);
  transform: scale(1.05);
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.9), rgba(0, 0, 0, 0.5));
  z-index: 1;
}

.hero-content {
  position: relative;
  z-index: 2;
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 2rem;
  display: flex;
  gap: 3rem;
  align-items: flex-end;
  padding-bottom: 2rem;
  animation: fadeInUp 0.8s ease;
}

.movie-poster-container {
  flex: 0 0 280px;
  position: relative;
  transform: translateY(25%);
  margin-bottom: -80px;
  transition: transform 0.3s ease;
}

.movie-poster-container:hover {
  transform: translateY(15%);
}

.movie-poster {
  width: 100%;
  border-radius: 8px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  object-fit: cover;
  object-position: center top;
  height: 120%;
}

.movie-poster:hover {
  transform: scale(1.02);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.4);
}

.movie-badges {
  position: absolute;
  top: 1rem;
  right: 1rem;
  display: flex;
  gap: 0.5rem;
}

/* Ajustes responsivos */
@media (max-width: 768px) {
  .movie-hero {
    height: 50vh;
    min-height: 400px;
  }

  .movie-poster-container {
    flex: 0 0 220px;
    transform: translateY(15%);
    margin-bottom: -60px;
  }

  .hero-content {
    flex-direction: column;
    align-items: center;
    text-align: center;
    padding-bottom: 1rem;
  }

  .movie-poster-container {
    transform: translateY(10%);
    margin-bottom: -40px;
  }
}

.badge {
  padding: 0.3rem 0.8rem;
  border-radius: 4px;
  font-weight: 600;
  font-size: 0.9rem;
  backdrop-filter: blur(5px);
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  transition: transform 0.2s ease;
}

.badge:hover {
  transform: scale(1.05);
}

.movie-info {
  flex: 1;
  color: white;
}

.categories-container {
  display: flex;
  gap: 0.8rem;
  margin-bottom: 1rem;
  flex-wrap: wrap;
}

.category-badge {
  padding: 0.4rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: 600;
  background-color: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(5px);
  transition: transform 0.2s ease, background-color 0.2s ease;
}

.category-badge:hover {
  transform: translateY(-2px);
  background-color: rgba(126, 91, 239, 0.7);
}

.movie-title {
  font-size: 3rem;
  font-weight: 800;
  margin-bottom: 1rem;
  line-height: 1.2;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
  animation: fadeIn 0.8s ease 0.2s both;
}

.movie-meta {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  font-size: 1.1rem;
  opacity: 0.9;
  animation: fadeIn 0.8s ease 0.4s both;
}

.btn {
  padding: 0.8rem 1.5rem;
  border-radius: 50px;
  font-weight: 600;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  transition: var(--transition);
  cursor: pointer;
  border: none;
}

.btn-primary {
  background-color: var(--color-primary);
  color: white;
}

.btn-primary:hover {
  background-color: var(--color-primary-dark);
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(126, 91, 239, 0.4);
}

.btn-sm {
  padding: 0.5rem 1rem;
  font-size: 0.9rem;
}

.btn-gallery {
  background-color: rgba(255, 255, 255, 0.1);
  color: white;
}

.btn-gallery:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

.dark-mode .btn-gallery, .btn-ver-mapa {
  color: white;
}

/* Main Content */
.movie-main {
  max-width: 1400px;
  margin: 3rem auto;
  padding: 0 2rem;
  display: flex;
  gap: 3rem;
  transition: background-color 0.6s ease, color 0.6s ease, border-color 0.6s ease;
}

.movie-content {
  flex: 2;
}

.movie-sidebar {
  flex: 1;
}

.section-title {
  font-size: 1.8rem;
  margin-bottom: 1.5rem;
  position: relative;
  padding-bottom: 0.5rem;
  color: var(--color-primary);
  animation: fadeIn 0.6s ease;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 60px;
  height: 3px;
  background: linear-gradient(to right, var(--color-primary), var(--color-primary-light));
  border-radius: 3px;
  animation: expandWidth 0.8s ease;
}

.movie-section {
  margin-bottom: 3rem;
  animation: fadeInUp 0.6s ease;
}

.movie-sinopsis, .movie-filming-info {
  line-height: 1.7;
  font-size: 1.1rem;
  margin-bottom: 1.5rem;
}

/* Reparto */
.cast-scroller {
  max-height: var(--max-section-height);
  overflow-y: auto;
  border-radius: 8px;
  box-shadow: inset 0 0 10px rgba(0, 0, 0, 0.1);
  padding: 0.5rem;
  margin: -0.5rem;
}

.cast-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 1.5rem;
  padding: 0.5rem;
}

.cast-card {
  background-color: var(--color-card);
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  transition: var(--transition);
}

.cast-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

.cast-photo {
  width: 100%;
  height: 240px;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.cast-card:hover .cast-photo {
  transform: scale(1.05);
}

.cast-photo.placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #eee;
  color: #999;
  font-size: 3rem;
}

.cast-info {
  padding: 1rem;
}

.cast-info h4 {
  font-size: 1.1rem;
  margin-bottom: 0.3rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.character {
  font-weight: 600;
  color: var(--color-primary);
  margin-bottom: 0.3rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.role {
  font-size: 0.9rem;
  opacity: 0.8;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Sidebar */
.sidebar-section {
  transition: all 0.4s ease;
  background-color: var(--color-card);
  border-radius: 8px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  animation: fadeIn 0.6s ease;
}

.sidebar-section:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}

.sidebar-title {
  font-size: 1.3rem;
  margin-bottom: 1rem;
  color: var(--color-primary);
}

.movie-facts {
  list-style: none;
  padding: 0;
}

.movie-facts li {
  display: flex;
  margin-bottom: 0.8rem;
  padding-bottom: 0.8rem;
  border-bottom: 1px solid var(--color-border);
}

.fact-label {
  font-weight: 600;
  min-width: 120px;
  opacity: 0.8;
}

.fact-value {
  flex: 1;
}

/* Saga */
.saga-movies {
  margin-top: 1rem;
  max-height: 200px;
  overflow-y: auto;
}

.saga-movie {
  display: flex;
  align-items: center;
  padding: 0.8rem;
  border-bottom: 1px solid var(--color-border);
  transition: all 0.3s ease;
}

.saga-movie:hover {
  background-color: rgba(126, 91, 239, 0.1);
  padding-left: 0.5rem;
}

.saga-movie.current {
  font-weight: 600;
  color: var(--color-primary);
  background-color: rgba(126, 91, 239, 0.1);
}

.saga-number {
  width: 30px;
  font-weight: 600;
}

.saga-title {
  flex: 1;
}

.saga-year {
  opacity: 0.7;
}

/* Galería */
.gallery-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 0.5rem;
}

.gallery-item {
  aspect-ratio: 1;
  overflow: hidden;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.gallery-item:hover {
  transform: scale(1.03);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
}

.gallery-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.gallery-item:hover img {
  transform: scale(1.1);
}

/* Mapa completo */
.map-section {
  max-width: 1400px;
  margin: 3rem auto;
  padding: 0 2rem;
  animation: fadeIn 0.6s ease;
}

.fullscreen-map {
  height: 500px;
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  background-color: #eee;
  transition: all 0.4s ease;
}

.fullscreen-map:hover {
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

/* Modal de Galería */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.9);
  z-index: 1000;
  display: flex;
  justify-content: center;
  align-items: center;
  backdrop-filter: blur(5px);
}

.gallery-modal {
  background-color: var(--color-card);
  border-radius: 12px;
  width: 90%;
  max-width: 1200px;
  max-height: 90vh;
  padding: 2rem;
  display: flex;
  flex-direction: column;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
}

.modal-close {
  position: absolute;
  top: 1rem;
  right: 1rem;
  background: none;
  border: none;
  font-size: 1.5rem;
  color: var(--color-text);
  cursor: pointer;
  transition: transform 0.2s ease;
}

.modal-close:hover {
  transform: rotate(90deg);
  color: var(--color-primary);
}

.modal-main-image-container {
  position: relative;
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 1rem 0;
  min-height: 50vh;
}

.main-image {
  max-width: 100%;
  max-height: 60vh;
  object-fit: contain;
  cursor: zoom-in;
  transition: transform 0.3s ease;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.2);
}

.main-image.zoomed {
  transform: scale(1.5);
  cursor: zoom-out;
}

.nav-arrow {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  font-size: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 10;
  transition: all 0.3s ease;
}

.nav-arrow:hover {
  background-color: var(--color-primary);
  transform: translateY(-50%) scale(1.1);
}

.nav-arrow.left {
  left: 1rem;
}

.nav-arrow.right {
  right: 1rem;
}

.image-counter {
  text-align: center;
  margin: 0.5rem 0;
  font-size: 0.9rem;
  color: var(--color-text);
  opacity: 0.8;
}

.thumbnails-scroller {
  display: flex;
  gap: 0.5rem;
  padding: 0.5rem;
  overflow-x: auto;
  scrollbar-width: thin;
}

.thumbnails-scroller::-webkit-scrollbar {
  height: 6px;
}

.thumbnails-scroller::-webkit-scrollbar-thumb {
  background-color: var(--color-primary);
  border-radius: 3px;
}

.thumbnail-container {
  flex: 0 0 80px;
  height: 60px;
  border-radius: 4px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.thumbnail-container.active {
  border-color: var(--color-primary);
  transform: scale(1.05);
}

.thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* Animaciones */
@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(50px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes expandWidth {
  from {
    width: 0;
  }
  to {
    width: 60px;
  }
}

/* Responsive */
@media (max-width: 1200px) {
  .hero-content {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .movie-poster-container {
    flex: 0 0 auto;
    max-width: 300px;
  }

  .movie-meta {
    justify-content: center;
  }

  .movie-main {
    flex-direction: column;
  }
}

@media (max-width: 768px) {
  .movie-hero {
    height: auto;
    min-height: auto;
    padding-top: 5rem;
    padding-bottom: 2rem;
  }

  .movie-title {
    font-size: 2.2rem;
  }

  .gallery-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .cast-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  }
}

@media (max-width: 480px) {
  .hero-content {
    padding: 0 1rem;
  }

  .btn {
    width: 100%;
    justify-content: center;
  }

  .gallery-grid {
    grid-template-columns: 1fr;
  }

  .cast-grid {
    grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  }
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 50vh;
  gap: 1rem;
}

.spinner {
  border: 4px solid rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  border-top: 4px solid var(--color-primary);
  width: 40px;
  height: 40px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

/* Error State */
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 50vh;
  gap: 1rem;
  text-align: center;
  padding: 2rem;
}

.error-container i {
  font-size: 3rem;
  color: #ff4757;
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

.error-container p {
  font-size: 1.2rem;
  margin-bottom: 1rem;
}

.cast-card-wrapper {
  text-decoration: none;
  color: inherit;
  display: block;
}

.cast-card-wrapper .cast-card {
  cursor: pointer;
  text-decoration: none;
  color: inherit;
  display: block;
}

.saga-movie,
.saga-movie:visited,
.saga-movie:hover,
.saga-movie:active {
  text-decoration: none;
  color: inherit;
}

/* Estilos para la sección de rodajes */
.locations-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 2rem;
  margin-top: 2rem;
}

.location-card {
  background-color: var(--color-card);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.location-map {
  height: 200px;
  background-color: #eee;
}

.location-info {
  padding: 1.5rem;
}

.location-gallery-preview {
  margin: 1rem 0;
}

.preview-images {
  display: flex;
  gap: 0.5rem;
  position: relative;
}

.preview-images img {
  width: 80px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.preview-images img:hover {
  transform: scale(1.05);
}

.more-images-overlay {
  width: 80px;
  height: 60px;
  background-color: rgba(0, 0, 0, 0.6);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  border-radius: 4px;
  cursor: pointer;
}

.location-actions {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
}

.btn {
  padding: 0.7rem 1.2rem;
  border-radius: 6px;
  font-weight: 600;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.3s ease;
  cursor: pointer;
  border: none;
}

.btn-gallery {
  background-color: var(--color-primary);
  color: white;
}

.btn-gallery:hover {
  background-color: var(--color-primary-dark);
}

/* Responsive */
@media (max-width: 768px) {
  .locations-container {
    grid-template-columns: 1fr;
  }

  .modal-main-image-container {
    min-height: 40vh;
  }

  .nav-arrow {
    width: 40px;
    height: 40px;
  }

  .location-actions {
    flex-direction: column;
  }
}

@media (max-width: 480px) {
  .gallery-modal {
    width: 95%;
    padding: 1rem;
  }

  .thumbnail-container {
    flex: 0 0 60px;
    height: 45px;
  }
}

</style>