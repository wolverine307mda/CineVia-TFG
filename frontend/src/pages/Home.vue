<template>
  <div class="home-page" :class="{ 'dark-mode': darkMode }">
    <!-- Hero Section -->
    <section class="hero-section dark-hero">
      <div class="hero-content">
        <h1 class="hero-title">
          Bienvenido a <span class="text-gradient">GeoFilm</span>
        </h1>
        <p class="hero-subtitle">
          Descubre los lugares donde se filmaron tus películas y series favoritas. ¡Explora el cine en todo el mundo!
        </p>
        <div class="cta-buttons">
          <router-link to="/explore" class="btn btn-primary">
            <i class="fas fa-map-marked-alt"></i> Explorar Lugares
          </router-link>
          <router-link to="/favorites" class="btn btn-secondary">
            <i class="fas fa-heart"></i> Mis Favoritos
          </router-link>
        </div>
      </div>
      <div class="hero-slider">
        <!-- Slider Items -->
        <div class="slider-track" :style="trackStyles">
          <div
              v-for="(slide, index) in slides"
              :key="index"
              class="slide"
              :class="{ 'active': currentSlide === index }"
          >
            <img :src="slide.image" :alt="slide.title" class="slide-image">
            <div class="slide-overlay"></div>
            <div class="slide-content">
              <h2 class="slide-title">{{ slide.title }}</h2>
              <p class="slide-subtitle">{{ slide.subtitle }}</p>
            </div>
          </div>
        </div>

        <!-- Slider Controls -->
        <button class="slider-nav prev" @click="prevSlide">
          <i class="fas fa-chevron-left"></i>
        </button>
        <button class="slider-nav next" @click="nextSlide">
          <i class="fas fa-chevron-right"></i>
        </button>

        <!-- Slider Indicators -->
        <div class="slider-indicators">
          <button
              v-for="(slide, index) in slides"
              :key="index"
              @click="goToSlide(index)"
              :class="{ 'active': currentSlide === index }"
          ></button>
        </div>
      </div>
    </section>

    <!-- Featured Movies -->
    <section class="featured-section">
      <h2 class="section-title">Películas Destacadas</h2>
      <div class="movies-grid" v-if="!isLoading">
        <movie-card
            v-for="movie in topRatedMovies"
            :key="movie.id"
            :movie="movie"
            :dark-mode="darkMode"
        />
      </div>
      <div v-else class="loading-movies">
        <i class="fas fa-spinner fa-spin"></i> Cargando películas...
      </div>

      <div class="explore-more-container" style="text-align: center; margin-top: 2rem;">
        <a
            href="/general"
            class="btn btn-primary explore-btn" style="font-weight: bold; font-size: 1.5rem; border: 2px solid #462152; color: #462152; background-color: transparent;"
            :to="`/general`">
          <i class="fas fa-map-marked-alt"></i> Explorar más
        </a>
      </div>
    </section>

    <!-- Features -->
    <section class="features-section">
      <h2 class="section-title">Características Principales</h2>
      <div class="features-grid">
        <div class="feature-card">
          <div class="feature-icon">
            <i class="fas fa-map-marked-alt"></i>
          </div>
          <h3>Mapas Interactivos</h3>
          <p>Ubica en el mapa los escenarios más icónicos del cine y planea tu visita.</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">
            <i class="fas fa-info-circle"></i>
          </div>
          <h3>Información Detallada</h3>
          <p>Datos completos sobre producciones, actores y curiosidades del rodaje.</p>
        </div>
        <div class="feature-card">
          <div class="feature-icon">
            <i class="fas fa-heart"></i>
          </div>
          <h3>Reseñas y Favoritos</h3>
          <p>Guarda tus lugares favoritos y comparte tus experiencias con otros usuarios.</p>
        </div>
      </div>
    </section>

    <!-- Testimonials -->
    <section class="testimonials-section">
      <h2 class="section-title">Lo que dicen nuestros usuarios</h2>
      <div class="testimonials-grid">
        <div class="testimonial-card">
          <div class="testimonial-content">
            <i class="fas fa-quote-left quote-icon"></i>
            <p>GeoFilm es la herramienta perfecta para los cinéfilos. Puedo ver exactamente dónde se filmaron mis películas favoritas. ¡Es increíble!</p>
          </div>
          <div class="testimonial-author">
            <img src="https://randomuser.me/api/portraits/women/44.jpg" alt="Laura">
            <div>
              <h4>Laura</h4>
              <p>Cinéfila</p>
            </div>
          </div>
        </div>
        <div class="testimonial-card">
          <div class="testimonial-content">
            <i class="fas fa-quote-left quote-icon"></i>
            <p>¡Una app espectacular! Me encanta ver los lugares que aparecen en las películas. Definitivamente la recomiendo.</p>
          </div>
          <div class="testimonial-author">
            <img src="https://randomuser.me/api/portraits/men/32.jpg" alt="Andrés">
            <div>
              <h4>Andrés</h4>
              <p>Viajero y amante del cine</p>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import MovieCard from '@/components/cards/MovieCard.vue';
import axios from 'axios';
import qs from 'qs';

export default {
  name: 'HomePage',
  components: {
    MovieCard
  },
  props: {
    darkMode: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      // Datos para el slider
      currentSlide: 0,
      slides: [
        {
          title: "Explora los lugares de tus películas favoritas",
          subtitle: "Descubre ubicaciones icónicas del cine alrededor del mundo",
          image: "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80"
        },
        {
          title: "Planifica tu próxima aventura cinematográfica",
          subtitle: "Encuentra y guarda los lugares que quieres visitar",
          image: "https://images.unsplash.com/photo-1542204165-65bf26472b9b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1974&q=80"
        },
        {
          title: "Conoce las historias detrás de los rodajes",
          subtitle: "Curiosidades y anécdotas de tus producciones favoritas",
          image: "https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80"
        }
      ],
      slideInterval: null,
      topRatedMovies: [],
      isLoading: true,
      error: null
    }
  },
  computed: {
    trackStyles() {
      return {
        transform: `translateX(-${this.currentSlide * 100}%)`,
        transition: 'transform 0.8s cubic-bezier(0.645, 0.045, 0.355, 1)'
      }
    }
  },
  methods: {
    // Métodos para el slider
    nextSlide() {
      this.currentSlide = (this.currentSlide + 1) % this.slides.length;
      this.resetAutoPlay();
    },
    prevSlide() {
      this.currentSlide = (this.currentSlide - 1 + this.slides.length) % this.slides.length;
      this.resetAutoPlay();
    },
    goToSlide(index) {
      this.currentSlide = index;
      this.resetAutoPlay();
    },
    startAutoPlay() {
      this.slideInterval = setInterval(this.nextSlide, 5000);
    },
    stopAutoPlay() {
      clearInterval(this.slideInterval);
    },
    resetAutoPlay() {
      this.stopAutoPlay();
      this.startAutoPlay();
    },

    async fetchTopRatedMovies() {
      this.isLoading = true;
      this.error = null;

      try {
        // Configuración base de la petición
        const config = {
          params: {
            page: 0,
            size: 4,
            sortField: 'puntuacion',
            sortDirection: 'DESC', // Usar 'DESC' en mayúsculas según tu API
            tipo: 'PELICULA'
          },
          paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat' }),
          headers: {
            'Content-Type': 'application/json',
            // 'Authorization': 'Bearer ' + localStorage.getItem('token') // Si necesitas autenticación
          }
        };

        console.log('Configuración de la petición:', config);

        const response = await axios.get('/api/producciones/filtrar', config);

        console.log('Respuesta completa:', response);

        // Manejo flexible de la respuesta
        let moviesData = [];
        if (response.data && Array.isArray(response.data.content)) {
          // Si sigue estructura de paginación Spring
          moviesData = response.data.content;
        } else if (response.data && Array.isArray(response.data.data)) {
          // Si es otra estructura
          moviesData = response.data.data;
        } else if (Array.isArray(response.data)) {
          // Si la respuesta es directamente el array
          moviesData = response.data;
        }

        this.topRatedMovies = moviesData.map(item => ({
          id: item.id?.toString() || Math.random().toString(36).substr(2, 9),
          title: item.titulo || 'Sin título',
          type: item.tipo || 'PELICULA',
          estreno: item.estreno || null,
          duration: item.duracion || 120,
          plot: item.sinopsis || 'Descripción no disponible',
          imagen: item.imagen || this.getDefaultPoster(),
          clasificacionEdad: item.clasificacionEdad || 'TP',
          categorias: Array.isArray(item.categorias) ? item.categorias : [],
          puntuacion: item.puntuacion ? parseFloat(item.puntuacion) : 0,
          isFavorite: false,
          year: item.estreno ? new Date(item.estreno).getFullYear() : 'N/A',
          poster: item.imagen || this.getDefaultPoster()
        }));

      } catch (error) {
        console.error('Error al obtener películas:', error);
        this.error = 'No se pudieron cargar las películas destacadas';

        // Datos de ejemplo para desarrollo
        this.topRatedMovies = this.getSampleMovies();
      } finally {
        this.isLoading = false;
      }
    },

    getDefaultPoster() {
      return 'https://via.placeholder.com/500x750?text=GeoFilm';
    },

    getSampleMovies() {
      return [
        {
          id: '1',
          title: 'Ejemplo Película 1',
          type: 'PELICULA',
          estreno: '2023-01-15',
          duration: 120,
          plot: 'Esta es una película de ejemplo para desarrollo',
          imagen: this.getDefaultPoster(),
          categorias: ['Acción', 'Aventura'],
          puntuacion: 8.5,
          year: 2023,
          poster: this.getDefaultPoster()
        },
        {
          id: '2',
          title: 'Ejemplo Película 2',
          type: 'PELICULA',
          estreno: '2022-05-20',
          duration: 95,
          plot: 'Otra película de ejemplo para desarrollo',
          imagen: this.getDefaultPoster(),
          categorias: ['Comedia'],
          puntuacion: 7.2,
          year: 2022,
          poster: this.getDefaultPoster()
        }
      ];
    }
  },
  mounted() {
    this.startAutoPlay();
    this.fetchTopRatedMovies();
  },
  beforeUnmount() {
    this.stopAutoPlay();
  }
}
</script>

<style scoped>
/* Variables y estilos base */
:root {
  --color-primary: #7e5bef;
  --color-primary-light: #a78bfa;
  --color-primary-dark: #6d46e8;
  --color-secondary: #f8f9fa;
  --color-text: #343a40;
  --color-text-light: #f8f9fa;
  --color-bg: #ffffff;
  --color-bg-dark: #1a1a2e;
  --color-card: #ffffff;
  --color-card-dark: #2a2a3a;
  --transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.dark-mode {
  --color-secondary: #2a2a3a;
  --color-text: #e9ecef;
  --color-bg: #121212;
  --color-card: #2a2a3a;
}

.home-page {
  background-color: var(--color-bg);
  color: var(--color-text);
  transition: var(--transition);
  padding: 0;
}

/* Hero Section */
.hero-section {
  display: flex;
  align-items: center;
  min-height: 100vh;
  padding: 4rem 2rem;
  background: linear-gradient(135deg, #18182f, #0f0f1a); /* Fijo en oscuro */
  color: #f8f9fa; /* Texto claro fijo */
  position: relative;
  overflow: hidden;
}

.hero-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url('https://images.unsplash.com/photo-1573614999645-e5f0f16ec15d?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D') center/cover;
  opacity: 0.15;
  z-index: 0;
}

.hero-content {
  position: relative;
  z-index: 1;
  max-width: 600px;
}

.dark-mode .explore-btn {
  color: #fff !important;
  border-color: #a78bfa !important;
}

.hero-title {
  font-size: 3.5rem;
  font-weight: 800;
  margin-bottom: 1.5rem;
  line-height: 1.2;
}

.text-gradient {
  background: linear-gradient(to right, #a78bfa, #7e5bef);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.hero-subtitle {
  font-size: 1.5rem;
  margin-bottom: 2.5rem;
  opacity: 0.9;
}

.cta-buttons {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.btn {
  padding: 0.8rem 1.8rem;
  border-radius: 50px;
  font-weight: 600;
  text-decoration: none;
  transition: var(--transition);
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-primary {
  background-color: var(--color-primary);
  color: white;
  border: 2px solid var(--color-primary);
}

.btn-primary:hover {
  background-color: var(--color-primary-dark);
  transform: translateY(-3px);
  box-shadow: 0 10px 20px rgba(126, 91, 239, 0.3);
}

.btn-secondary {
  background-color: transparent;
  color: white;
  border: 2px solid white;
}

.btn-secondary:hover {
  background-color: rgba(255, 255, 255, 0.1);
  transform: translateY(-3px);
}

/* Featured Movies */
.featured-section {
  padding: 4rem 2rem;
  max-width: 1400px;
  margin: 0 auto;
}

.section-title {
  font-size: 2.5rem;
  text-align: center;
  margin-bottom: 3rem;
  position: relative;
  color: var(--color-primary);
}

.section-title::after {
  content: '';
  display: block;
  width: 80px;
  height: 4px;
  background: linear-gradient(to right, var(--color-primary), var(--color-primary-light));
  margin: 1rem auto 0;
  border-radius: 2px;
}

.movie-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 2rem;
}

.movie-card {
  background-color: var(--color-card);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
  transition: var(--transition);
}

.movie-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
}

.movie-poster {
  position: relative;
  height: 400px;
  overflow: hidden;
}

.movie-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: var(--transition);
}

.movie-card:hover .movie-image {
  transform: scale(1.05);
}

.movie-badge {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  background-color: rgba(0, 0, 0, 0.7); /* fondo oscuro semitransparente */
  color: #fff;
  padding: 0.25rem 0.5rem;
  border-radius: 6px;
  font-size: 0.875rem;
  font-weight: bold;
  z-index: 2;
}

.movie-info {
  padding: 1.5rem;
}

.movie-title {
  font-size: 1.3rem;
  margin-bottom: 0.5rem;
}

.movie-location {
  color: var(--color-primary);
  margin-bottom: 1rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-details {
  background-color: transparent;
  color: var(--color-primary);
  border: 2px solid var(--color-primary);
  padding: 0.5rem 1.2rem;
}

.btn-details:hover {
  background-color: var(--color-primary);
  color: white;
}

/* Features Section */
.features-section {
  padding: 4rem 2rem;
  background-color: var(--color-secondary);
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.feature-card {
  background-color: var(--color-card);
  padding: 2rem;
  border-radius: 12px;
  text-align: center;
  transition: var(--transition);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

.feature-icon {
  font-size: 2.5rem;
  color: var(--color-primary);
  margin-bottom: 1.5rem;
}

.feature-card h3 {
  font-size: 1.5rem;
  margin-bottom: 1rem;
  color: var(--color-primary);
}

.feature-card p {
  color: var(--color-text);
  opacity: 0.9;
}

/* Testimonials */
.testimonials-section {
  padding: 4rem 2rem;
  max-width: 1400px;
  margin: 0 auto;
}

.testimonials-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.testimonial-card {
  background-color: var(--color-card);
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.05);
  transition: var(--transition);
}

.testimonial-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

.quote-icon {
  font-size: 2rem;
  color: var(--color-primary);
  opacity: 0.3;
  margin-bottom: 1rem;
}

.testimonial-content p {
  font-style: italic;
  margin-bottom: 1.5rem;
  position: relative;
}

.testimonial-author {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.testimonial-author img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
}

.testimonial-author h4 {
  margin-bottom: 0.2rem;
  color: var(--color-primary);
}

.testimonial-author p {
  font-size: 0.9rem;
  opacity: 0.8;
}

/* Modo oscuro específico */
.dark-mode .hero-section {
  background: linear-gradient(135deg, var(--color-primary-dark), #0f0f1a);
}

.dark-mode .feature-card,
.dark-mode .testimonial-card,
.dark-mode .movie-card {
  background-color: var(--color-card-dark);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.dark-mode .feature-card:hover,
.dark-mode .testimonial-card:hover,
.dark-mode .movie-card:hover {
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.4);
}

/* Responsive */
@media (min-width: 768px) {
  .hero-section {
    padding: 4rem;
  }

  .hero-image {
    display: block;
  }

  .hero-title {
    font-size: 4rem;
  }
}

@media (min-width: 992px) {
  .hero-section {
    min-height: 90vh;
  }

  .section-title {
    font-size: 3rem;
  }
}


.hero-slider {
  position: relative;
  width: 100%;
  height: 40vh;
  min-height: 600px;
  overflow: hidden;
  border-radius: 0 0 20px 20px;
  mask-image: linear-gradient(
      to right,
      transparent 0%,
      black 10%,
      black 90%,
      transparent 100%
  );
}

.slider-track {
  display: flex;
  height: 100%;
  width: 100%;
}

.slide {
  position: relative;
  min-width: 100%;
  height: 100%;
}

.slide-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center;
}

.slide-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(to right, rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.3));
}

.slide-content {
  position: absolute;
  top: 50%;
  left: 10%;
  transform: translateY(-50%);
  color: white;
  max-width: 600px;
  text-align: left;
  opacity: 0;
  transform: translateY(30px);
  transition: all 0.8s cubic-bezier(0.645, 0.045, 0.355, 1) 0.3s;
}

.slide.active .slide-content {
  opacity: 1;
  transform: translateY(0);
}

.slide-title {
  font-size: 3rem;
  font-weight: 800;
  margin-bottom: 1rem;
  line-height: 1.2;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.slide-subtitle {
  font-size: 1.5rem;
  margin-bottom: 2rem;
  opacity: 0.9;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.slider-nav {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background-color: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  font-size: 1.2rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  z-index: 10;
}

.slider-nav:hover {
  background-color: rgba(255, 255, 255, 0.3);
  transform: translateY(-50%) scale(1.1);
}

.prev {
  left: 2rem;
}

.next {
  right: 2rem;
}

.slider-indicators {
  position: absolute;
  bottom: 2rem;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 0.8rem;
  z-index: 10;
}

.slider-indicators button {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  border: none;
  background-color: rgba(255, 255, 255, 0.4);
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 0;
}

.slider-indicators button.active {
  background-color: var(--color-primary);
  transform: scale(1.2);
}

.slider-indicators button:hover:not(.active) {
  background-color: rgba(255, 255, 255, 0.7);
}

/* Responsive */
@media (max-width: 768px) {
  .hero-slider {
    display: none;
  }
}

/* Transiciones suaves para cambios de tema */
.home-page,
.hero-section,
.hero-content,
.section-title,
.movie-card,
.feature-card,
.testimonial-card {
  transition: background-color 0.6s ease, color 0.6s ease, border-color 0.6s ease;
}

/* También puedes aplicar esto a imágenes si cambian de brillo u opacidad */
img,
.slide-image {
  transition: opacity 0.6s ease, filter 0.6s ease;
}

/* Fondo suave al cambiar de modo */
.dark-mode .home-page {
  background-color: var(--color-bg);
}

.dark-mode .hero-section,
.dark-mode .movie-card,
.dark-mode .feature-card,
.dark-mode .testimonial-card {
  background-color: var(--color-card-dark);
  color: var(--color-text);
}

/* Si tienes bordes que cambian */
.movie-card,
.feature-card,
.testimonial-card {
  border: 1px solid transparent;
}

.dark-mode .movie-card,
.dark-mode .feature-card,
.dark-mode .testimonial-card {
  border-color: rgba(255, 255, 255, 0.1);
}

.movies-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 2rem;
  padding: 1rem;
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
}

.loading-movies {
  text-align: center;
  padding: 2rem;
  font-size: 1.2rem;
  color: #666;
}

@media (max-width: 768px) {
  .movies-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  }
}
</style>