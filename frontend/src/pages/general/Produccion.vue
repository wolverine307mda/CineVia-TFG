<template>

  <Header
      :dark-mode="darkMode"
      @toggle-dark-mode="toggleDarkMode"
  />

    <div class="movie-page" :class="{ 'dark-mode': darkMode }">

      <!-- Hero Section con imagen de la película -->
      <section class="movie-hero">
        <div class="hero-backdrop" :style="{ 'background-image': `url(${movie.image})` }"></div>
        <div class="hero-overlay"></div>
        <div class="hero-content">
          <div class="movie-poster-container">
            <img :src="movie.image" :alt="movie.title" class="movie-poster">
            <div class="movie-badges">
              <span class="badge age-rating">{{ movie.clasificacionEdad }}+</span>
              <span class="badge runtime">{{ formatRuntime(movie.duracion) }}</span>
            </div>
          </div>
          <div class="movie-info">
            <h1 class="movie-title">{{ movie.title }}</h1>
            <div class="movie-meta">
              <span class="release-year">{{ new Date(movie.estreno).getFullYear() }}</span>
              <span class="genre">{{ movie.categoria }}</span>
            </div>
            <div class="movie-actions">
              <button class="btn btn-outline">
                <i class="fas fa-heart"></i> Añadir a favoritos
              </button>
            </div>
          </div>
        </div>
      </section>

      <!-- Sección principal -->
      <div class="movie-main">
        <!-- Columna izquierda -->
        <div class="movie-content">
          <!-- Sinopsis -->
          <section class="movie-section">
            <h2 class="section-title">Sinopsis</h2>
            <p class="movie-sinopsis">{{ movie.sinopsis }}</p>
          </section>

          <!-- Información del rodaje -->
          <section class="movie-section" v-if="movie.informacion">
            <h2 class="section-title">Sobre el rodaje</h2>
            <p class="movie-filming-info">{{ movie.informacion }}</p>
          </section>

          <!-- Ubicaciones -->
          <section class="movie-section">
            <h2 class="section-title">Ubicaciones de rodaje</h2>
            <div class="locations-container">
              <div v-for="location in movie.locations" :key="location.id" class="location-card">
                <div class="location-map" ref="mapContainer"></div>
                <div class="location-info">
                  <h3>{{ location.nombre }}</h3>
                  <p>{{ location.descripcion }}</p>
                  <div class="location-actions">
                    <button class="btn btn-sm btn-ver-mapa">
                      <i class="fas fa-map-marker-alt"></i> Ver en mapa
                    </button>
                    <button class="btn btn-sm btn-outline">
                      <i class="fas fa-comment"></i> Reseñas
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </section>

          <!-- Reparto -->
          <section class="movie-section">
            <h2 class="section-title">Reparto</h2>
            <div class="cast-grid">
              <div v-for="participant in movie.cast" :key="participant.id" class="cast-card">
                <img :src="participant.foto" :alt="participant.nombre" class="cast-photo">
                <div class="cast-info">
                  <h4>{{ participant.nombre }}</h4>
                  <p class="character">{{ participant.papel }}</p>
                  <p class="role">{{ participant.rol }}</p>
                </div>
              </div>
            </div>
          </section>

          <!-- Reseñas -->
          <section class="movie-section">
            <h2 class="section-title">Reseñas</h2>
            <div class="reviews-container">
              <div v-for="review in movie.reviews" :key="review.id" class="review-card">
                <div class="review-header">
                  <img :src="review.userAvatar" :alt="review.userName" class="review-avatar">
                  <div class="review-user">
                    <h4>{{ review.userName }}</h4>
                    <div class="review-rating">
                      <i v-for="i in 5" :key="i"
                         :class="i <= review.puntuacion ? 'fas fa-star' : 'far fa-star'"></i>
                    </div>
                  </div>
                  <span class="review-date">{{ formatDate(review.fecha) }}</span>
                </div>
                <p class="review-text">{{ review.comentario }}</p>
              </div>
            </div>
            <button class="btn btn-outline">
              <i class="fas fa-pen"></i> Escribir reseña
            </button>
          </section>
        </div>

        <!-- Columna derecha -->
        <div class="movie-sidebar">
          <!-- Datos técnicos -->
          <section class="sidebar-section">
            <h3 class="sidebar-title">Datos técnicos</h3>
            <ul class="movie-facts">
              <li>
                <span class="fact-label">Título original</span>
                <span class="fact-value">The Lord of the Rings: The Fellowship of the Ring</span>
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
                <span class="fact-label">Género</span>
                <span class="fact-value">{{ movie.categoria }}</span>
              </li>
              <li>
                <span class="fact-label">Clasificación</span>
                <span class="fact-value">{{ movie.clasificacionEdad }}+</span>
              </li>
            </ul>
          </section>

          <!-- Saga -->
          <section class="sidebar-section" v-if="movie.saga">
            <h3 class="sidebar-title">Parte de la saga</h3>
            <div class="saga-info">
              <h4>{{ movie.saga.nombre }}</h4>
              <p>{{ movie.saga.descripcion }}</p>
              <div class="saga-movies">
                <div v-for="(film, index) in sagaMovies" :key="film.id"
                     class="saga-movie" :class="{ 'current': film.id === movie.id }">
                  <span class="saga-number">{{ index + 1 }}</span>
                  <span class="saga-title">{{ film.title }}</span>
                  <span class="saga-year">{{ new Date(film.estreno).getFullYear() }}</span>
                </div>
              </div>
            </div>
          </section>

          <!-- Galería -->
          <section class="sidebar-section">
            <h3 class="sidebar-title">Galería</h3>
            <div class="gallery-grid">
              <div v-for="(image, index) in movie.gallery" :key="index" class="gallery-item">
                <img :src="image" :alt="`${movie.title} - Imagen ${index + 1}`">
              </div>
            </div>
          </section>
        </div>
      </div>

      <!-- Mapa de ubicaciones -->
      <section class="map-section">
        <h2 class="section-title">Mapa de ubicaciones</h2>
        <div class="fullscreen-map" ref="fullMap"></div>
      </section><br>
    </div>

  <Footer :dark-mode="darkMode" />

</template>

<script>
import { Loader } from '@googlemaps/js-api-loader';
import DefaultLayout from "@/layouts/DefaultLayout.vue";
import Header from "@/components/principal/Header.vue";
import Footer from "@/components/principal/Footer.vue";

export default {
  name: 'MoviePage',
  components: {Footer, Header, DefaultLayout},
  data() {
    return {
      darkMode: false,
      movie: {
        id: '3',
        title: 'El Señor de los Anillos: La Comunidad del Anillo',
        tipo: 'PELICULA',
        estreno: '2001-12-19T00:00:00',
        duracion: 178,
        sinopsis: 'Un hobbit llamado Frodo Bolsón hereda un anillo mágico que podría ser la clave para salvar o destruir la Tierra Media. Junto a ocho compañeros, emprende un viaje hacia el Monte del Destino para destruirlo.',
        image: 'https://hips.hearstapps.com/hmg-prod/images/el-senor-de-los-anillos-la-comunidad-del-anillo-pelicula-2001-1639853382.jpg',
        informacion: 'Filmada enteramente en Nueva Zelanda durante 438 días. Peter Jackson utilizó efectos prácticos y maquetas combinados con CGI innovador para la época.',
        categoria: 'Fantasía, Aventura',
        clasificacionEdad: 12,
        saga: {
          id: '1',
          nombre: 'El Señor de los Anillos',
          descripcion: 'Trilogía basada en la obra de J.R.R. Tolkien sobre la lucha por destruir el Anillo Único.',
          isAcabada: true,
          fechaInicio: '2001-12-19T00:00:00',
          fechaFin: '2003-12-17T00:00:00'
        },
        locations: [
          {
            id: '1',
            nombre: 'Hobbiton, Matamata',
            descripcion: 'La icónica Comarca fue construida en una granja cerca de Matamata.',
            coordenadas: '-37.8722,175.6830',
            imagen: 'https://images.unsplash.com/photo-1580041065738-e606e68d261a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80'
          },
          {
            id: '2',
            nombre: 'Parque Nacional Tongariro',
            descripcion: 'Las tierras de Mordor fueron filmadas en este parque volcánico.',
            coordenadas: '-39.2727,175.5802',
            imagen: 'https://images.unsplash.com/photo-1589471383477-6dab7f6d2b11?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80'
          }
        ],
        cast: [
          {
            id: '1',
            nombre: 'Elijah Wood',
            foto: 'https://image.tmdb.org/t/p/w200/7UKRbJBNG7mxBl2QQc5XsAh6F8B.jpg',
            rol: 'ACTOR',
            papel: 'Frodo Bolsón'
          },
          {
            id: '2',
            nombre: 'Viggo Mortensen',
            foto: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQbO9C8XCO7o9Tj-R1rcjDIn1lj-KE_BfnK8g&s',
            rol: 'ACTOR',
            papel: 'Aragorn'
          },
          {
            id: '3',
            nombre: 'Peter Jackson',
            foto: 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQAbYBQ_cRtd-38I1BqfZBFyke5KEwANdEfOA&s',
            rol: 'DIRECTOR',
            papel: 'Director'
          }
        ],
        reviews: [
          {
            id: '1',
            userName: 'Laura Cinefila',
            userAvatar: 'https://randomuser.me/api/portraits/women/44.jpg',
            puntuacion: 5,
            comentario: 'Una obra maestra del cine fantástico. Los paisajes de Nueva Zelanda son simplemente espectaculares.',
            fecha: '2023-05-15T10:30:00'
          },
          {
            id: '2',
            userName: 'Carlos Viajero',
            userAvatar: 'https://randomuser.me/api/portraits/men/32.jpg',
            puntuacion: 4,
            comentario: 'Increíble adaptación del libro. Las ubicaciones son fieles a como me las imaginaba al leer la novela.',
            fecha: '2023-04-22T15:45:00'
          }
        ],
        gallery: [
          "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTfM2S9A1PJfCMTnygFHdQ0rl08mfIIlLgRDA&s",
          "https://hips.hearstapps.com/hmg-prod/images/el-sen-or-de-los-anillos-los-anillos-de-poder-66d6de11d8885.jpg?crop=1xw:1xh;center,top&resize=980:*",
          "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQjCLF5ghLkuOOHMVu11Z6PfNhMBKDxRvlUOw&s",
        ]
      },
      sagaMovies: [
        {
          id: '3',
          title: 'El Señor de los Anillos: La Comunidad del Anillo',
          estreno: '2001-12-19T00:00:00'
        },
        {
          id: '4',
          title: 'El Señor de los Anillos: Las dos torres',
          estreno: '2002-12-18T00:00:00'
        },
        {
          id: '5',
          title: 'El Señor de los Anillos: El retorno del Rey',
          estreno: '2003-12-17T00:00:00'
        }
      ],
      mapsLoaded: false
    }
  },
  computed: {
    // Puedes añadir computed properties si necesitas
  },
  methods: {
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
      const options = { year: 'numeric', month: 'long', day: 'numeric' };
      return new Date(dateString).toLocaleDateString('es-ES', options);
    },
    formatDate(dateString) {
      const options = { year: 'numeric', month: 'short', day: 'numeric' };
      return new Date(dateString).toLocaleDateString('es-ES', options);
    },
    loadMaps() {
      const loader = new Loader({
        apiKey: 'AIzaSyCtMIHC_PntZzdioTFmRcamhjRNKZMw4hc',
        version: 'weekly',
        libraries: ['places']
      });

      loader.load().then(() => {
        this.mapsLoaded = true;
        this.initLocationMaps();
        this.initFullMap();
      });
    },
    initLocationMaps() {
      this.$nextTick(() => {
        this.movie.locations.forEach((location, index) => {
          const [lat, lng] = location.coordenadas.split(',').map(Number);
          const mapContainer = this.$refs.mapContainer[index];

          if (mapContainer) {
            const map = new google.maps.Map(mapContainer, {
              center: { lat, lng },
              zoom: 12,
              disableDefaultUI: true,
              styles: this.darkMode ? this.getDarkMapStyle() : []
            });

            new google.maps.Marker({
              position: { lat, lng },
              map,
              title: location.nombre
            });
          }
        });
      });
    },
    initFullMap() {
      if (this.movie.locations.length > 0) {
        const bounds = new google.maps.LatLngBounds();
        const map = new google.maps.Map(this.$refs.fullMap, {
          zoom: 6,
          styles: this.darkMode ? this.getDarkMapStyle() : []
        });

        this.movie.locations.forEach(location => {
          const [lat, lng] = location.coordenadas.split(',').map(Number);
          const position = new google.maps.LatLng(lat, lng);

          new google.maps.Marker({
            position,
            map,
            title: location.nombre
          });

          bounds.extend(position);
        });

        map.fitBounds(bounds);
      }
    },
    getDarkMapStyle() {
      return [
        { elementType: "geometry", stylers: [{ color: "#242f3e" }] },
        { elementType: "labels.text.stroke", stylers: [{ color: "#242f3e" }] },
        { elementType: "labels.text.fill", stylers: [{ color: "#746855" }] },
        {
          featureType: "administrative.locality",
          elementType: "labels.text.fill",
          stylers: [{ color: "#d59563" }]
        },
        {
          featureType: "poi",
          elementType: "labels.text.fill",
          stylers: [{ color: "#d59563" }]
        },
        {
          featureType: "poi.park",
          elementType: "geometry",
          stylers: [{ color: "#263c3f" }]
        },
        {
          featureType: "poi.park",
          elementType: "labels.text.fill",
          stylers: [{ color: "#6b9a76" }]
        },
        {
          featureType: "road",
          elementType: "geometry",
          stylers: [{ color: "#38414e" }]
        },
        {
          featureType: "road",
          elementType: "geometry.stroke",
          stylers: [{ color: "#212a37" }]
        },
        {
          featureType: "road",
          elementType: "labels.text.fill",
          stylers: [{ color: "#9ca5b3" }]
        },
        {
          featureType: "road.highway",
          elementType: "geometry",
          stylers: [{ color: "#746855" }]
        },
        {
          featureType: "road.highway",
          elementType: "geometry.stroke",
          stylers: [{ color: "#1f2835" }]
        },
        {
          featureType: "road.highway",
          elementType: "labels.text.fill",
          stylers: [{ color: "#f3d19c" }]
        },
        {
          featureType: "transit",
          elementType: "geometry",
          stylers: [{ color: "#2f3948" }]
        },
        {
          featureType: "transit.station",
          elementType: "labels.text.fill",
          stylers: [{ color: "#d59563" }]
        },
        {
          featureType: "water",
          elementType: "geometry",
          stylers: [{ color: "#17263c" }]
        },
        {
          featureType: "water",
          elementType: "labels.text.fill",
          stylers: [{ color: "#515c6d" }]
        },
        {
          featureType: "water",
          elementType: "labels.text.stroke",
          stylers: [{ color: "#17263c" }]
        }
      ];
    }
  },
  created() {
    const savedMode = localStorage.getItem('darkMode');
    this.darkMode = savedMode === 'true';
  },
  mounted() {
    this.loadMaps();
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
}

.dark-mode {
  --color-secondary: #2a2a3a;
  --color-text: #e9ecef;
  --color-bg: #121212;
  --color-card: #1e1e2e;
  --color-border: #2d3748;
}

.movie-page {
  background-color: var(--color-bg);
  color: var(--color-text);
  transition: var(--transition);
  min-height: 100vh;
  transition: background-color 0.6s ease, color 0.6s ease, border-color 0.6s ease;
}

.toggle-dark {
  position: fixed;
  top: 1.5rem;
  right: 1.5rem;
  background-color: var(--color-card);
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: white;
  z-index: 100;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  transition: var(--transition);
}

.toggle-dark:hover {
  transform: scale(1.1);
}

/* Hero Section */
.movie-hero {
  position: relative;
  height: 70vh;
  min-height: 600px;
  display: flex;
  align-items: flex-end;
  padding-bottom: 3rem;
  overflow: hidden;
}

.hero-backdrop {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-size: cover;
  background-position: center;
  filter: blur(5px);
  transform: scale(1.1);
  z-index: 0;
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
}

.movie-poster-container {
  flex: 0 0 300px;
  position: relative;
}

.movie-poster {
  width: 100%;
  border-radius: 8px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
}

.movie-badges {
  position: absolute;
  top: 1rem;
  right: 1rem;
  display: flex;
  gap: 0.5rem;
}

.badge {
  padding: 0.3rem 0.8rem;
  border-radius: 4px;
  font-weight: 600;
  font-size: 0.9rem;
}

.age-rating {
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  backdrop-filter: blur(5px);
}

.runtime {
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  backdrop-filter: blur(5px);
}

.movie-info {
  flex: 1;
  color: white;
}

.movie-title {
  font-size: 3rem;
  font-weight: 800;
  margin-bottom: 1rem;
  line-height: 1.2;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
}

.movie-meta {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  font-size: 1.1rem;
  opacity: 0.9;
}

.movie-actions {
  display: flex;
  gap: 1rem;
  margin-top: 2rem;
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
}

.btn-primary {
  background-color: var(--color-primary);
  color: white;
  border: none;
}

.btn-primary:hover {
  background-color: var(--color-primary-dark);
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(126, 91, 239, 0.4);
}

.btn-outline {
  background-color: transparent;
  color: black;
  border: 2px solid black;
  transition: background-color 0.6s ease, color 0.6s ease, border-color 0.6s ease;
}

.dark-mode .btn-outline {
  background-color: transparent;
  color: white;
  border: 2px solid white;
}

.btn-outline:hover {
  background-color: rgba(255, 255, 255, 0.1);
  transform: translateY(-2px);
}

.dark-mode .btn-ver-mapa{
  color: white;
  transition: background-color 0.6s ease, color 0.6s ease, border-color 0.6s ease;
}

.btn-sm {
  padding: 0.5rem 1rem;
  font-size: 0.9rem;
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
}

.movie-section {
  margin-bottom: 3rem;
}

.movie-sinopsis, .movie-filming-info {
  line-height: 1.7;
  font-size: 1.1rem;
  margin-bottom: 1.5rem;
}

/* Ubicaciones */
.locations-container {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.location-card {
  transition: background-color 0.6s ease, color 0.6s ease, border-color 0.6s ease;
  display: flex;
  gap: 2rem;
  background-color: var(--color-card);
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.location-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

.location-map {
  flex: 1;
  min-height: 200px;
  background-color: #eee;
}

.location-info {
  flex: 1;
  padding: 1.5rem;
}

.location-info h3 {
  font-size: 1.3rem;
  margin-bottom: 0.5rem;
  color: var(--color-primary);
}

.location-info p {
  margin-bottom: 1.5rem;
  line-height: 1.6;
}

.location-actions {
  display: flex;
  gap: 1rem;
}

/* Reparto */
.cast-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 1.5rem;
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
  height: 200px;
  object-fit: cover;
}

.cast-info {
  padding: 1rem;
}

.cast-info h4 {
  font-size: 1.1rem;
  margin-bottom: 0.3rem;
}

.character {
  font-weight: 600;
  color: var(--color-primary);
  margin-bottom: 0.3rem;
}

.role {
  font-size: 0.9rem;
  opacity: 0.8;
}

/* Reseñas */
.reviews-container {
  margin-bottom: 2rem;
}

.review-card {
  background-color: var(--color-card);
  border-radius: 8px;
  padding: 1.5rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.review-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
}

.review-avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}

.review-user {
  flex: 1;
}

.review-user h4 {
  margin-bottom: 0.3rem;
}

.review-rating {
  color: #ffc107;
}

.review-date {
  font-size: 0.9rem;
  opacity: 0.7;
}

.review-text {
  line-height: 1.6;
}

/* Sidebar */
.sidebar-section {
  transition: background-color 0.6s ease, color 0.6s ease, border-color 0.6s ease;
  background-color: var(--color-card);
  border-radius: 8px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
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
}

.saga-movie {
  display: flex;
  align-items: center;
  padding: 0.8rem 0;
  border-bottom: 1px solid var(--color-border);
}

.saga-movie.current {
  font-weight: 600;
  color: var(--color-primary);
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
}

.gallery-item img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: var(--transition);
}

.gallery-item:hover img {
  transform: scale(1.05);
}

/* Mapa completo */
.map-section {
  max-width: 1400px;
  margin: 3rem auto;
  padding: 0 2rem;
}

.fullscreen-map {
  height: 500px;
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  background-color: #eee;
}

/* Modo oscuro específico */
.dark-mode .section-title {
  color: var(--color-primary-light);
}

.dark-mode .location-card,
.dark-mode .cast-card,
.dark-mode .review-card,
.dark-mode .sidebar-section {
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.dark-mode .location-card:hover,
.dark-mode .cast-card:hover {
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.4);
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

  .movie-actions {
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

  .location-card {
    flex-direction: column;
  }

  .location-map {
    min-height: 200px;
  }

  .cast-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  }

  .gallery-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .hero-content {
    padding: 0 1rem;
  }

  .movie-actions {
    flex-direction: column;
  }

  .btn {
    width: 100%;
    justify-content: center;
  }

  .gallery-grid {
    grid-template-columns: 1fr;
  }
}
</style>