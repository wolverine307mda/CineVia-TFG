<template>
  <Header
      :dark-mode="darkMode"
      @toggle-dark-mode="toggleDarkMode"
  />

  <div class="saga-page" :class="{ 'dark-mode': darkMode }">
    <!-- Loading State -->
    <div v-if="loading" class="loading-container">
      <div class="spinner"></div>
      <p>Cargando saga...</p>
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
    <template v-else-if="saga">
      <!-- Hero Section -->
      <section class="saga-hero">
        <div class="hero-backdrop" :style="{ 'background-image': `url(${saga.imagen})` }"></div>
        <div class="hero-overlay"></div>
        <div class="hero-content">
          <div class="saga-poster-container">
            <img :src="saga.imagen" :alt="saga.nombre" class="saga-poster" loading="lazy">
            <div class="saga-badges">
              <span class="badge status">{{ formatStatus(saga.isAcabada) }}</span>
            </div>
          </div>
          <div class="saga-info">
            <h1 class="saga-title">{{ saga.nombre }}</h1>
            <div class="saga-meta">
              <span class="years">{{ formatDateRange(saga.fechaInicio, saga.fechaFin) }}</span>
              <span class="movies-count">{{ saga.producciones.length }} películas</span>
            </div>
          </div>
        </div>
      </section>

      <!-- Main Content -->
      <div class="saga-main">
        <!-- Left Column -->
        <div class="saga-content">
          <!-- Description -->
          <section class="saga-section">
            <h2 class="section-title">Descripción</h2>
            <p class="saga-description">{{ saga.descripcion }}</p>
          </section>

          <!-- Movies List -->
          <section class="saga-section">
            <h2 class="section-title">Películas de la saga</h2>
            <div class="movies-grid">
              <MovieCard
                  v-for="movie in saga.producciones"
                  :key="movie.id"
                  :movie="mapProductionToMovieCard(movie)"
                  :dark-mode="darkMode"
                  @toggle-favorite="toggleFavorite"
              />
            </div>
          </section>
        </div>

        <!-- Right Column -->
        <div class="saga-sidebar">
          <!-- Technical Data -->
          <section class="sidebar-section">
            <h3 class="sidebar-title">Datos de la saga</h3>
            <ul class="saga-facts">
              <li>
                <span class="fact-label">Nombre</span>
                <span class="fact-value">{{ saga.nombre }}</span>
              </li>
              <li>
                <span class="fact-label">Estado</span>
                <span class="fact-value">{{ formatStatus(saga.isAcabada) }}</span>
              </li>
              <li>
                <span class="fact-label">Fecha inicio</span>
                <span class="fact-value">{{ formatFullDate(saga.fechaInicio) }}</span>
              </li>
              <li v-if="saga.isAcabada">
                <span class="fact-label">Fecha fin</span>
                <span class="fact-value">{{ formatFullDate(saga.fechaFin) }}</span>
              </li>
              <li>
                <span class="fact-label">Duración total</span>
                <span class="fact-value">{{ calculateTotalDuration() }}</span>
              </li>
              <li>
                <span class="fact-label">Películas</span>
                <span class="fact-value">{{ saga.producciones.length }}</span>
              </li>
            </ul>
          </section>

          <!-- Timeline -->
          <section class="sidebar-section">
            <h3 class="sidebar-title">Línea de tiempo</h3>
            <div class="timeline">
              <div
                  v-for="(movie, index) in sortedProductions"
                  :key="movie.id"
                  class="timeline-item"
                  :class="{ 'first': index === 0, 'last': index === sortedProductions.length - 1 }"
              >
                <div class="timeline-dot"></div>
                <div class="timeline-content">
                  <h4>{{ movie.titulo }}</h4>
                  <p>{{ formatFullDate(movie.estreno) }}</p>
                </div>
              </div>
            </div>
          </section>
        </div>
      </div>
    </template><br><br>
  </div>

  <Footer :dark-mode="darkMode" />
</template>

<script>
import Header from "@/components/principal/Header.vue";
import Footer from "@/components/principal/Footer.vue";
import MovieCard from "@/components/cards/MovieCard.vue";
import sagaService from '@/services/sagas.service.js';

export default {
  name: 'SagaDetail',
  components: { Footer, Header, MovieCard },
  mounted() {
    window.scrollTo(0, 0);
  },
  data() {
    return {
      darkMode: false,
      saga: null,
      loading: true,
      error: null
    }
  },
  computed: {
    sortedProductions() {
      if (!this.saga?.producciones) return [];
      return [...this.saga.producciones].sort((a, b) =>
          new Date(a.estreno) - new Date(b.estreno))
    }
  },
  methods: {
    async loadSagaData() {
      this.loading = true;
      this.error = null;
      try {
        const idSaga = this.$route.params.id;
        this.saga = await sagaService.getSagaById(idSaga);
      } catch (error) {
        console.error('Error al cargar la saga:', error);
        this.error = 'No se pudo cargar la información de la saga.';
      } finally {
        this.loading = false;
      }
    },
    retryLoading() {
      this.loadSagaData();
    },
    toggleDarkMode() {
      this.darkMode = !this.darkMode;
      localStorage.setItem('darkMode', this.darkMode);
    },
    formatStatus(isFinished) {
      return isFinished ? 'Completada' : 'En producción';
    },
    formatFullDate(dateString) {
      if (!dateString) return 'N/A';
      const options = {year: 'numeric', month: 'long', day: 'numeric'};
      return new Date(dateString).toLocaleDateString('es-ES', options);
    },
    formatDateRange(startDate, endDate) {
      const startYear = new Date(startDate).getFullYear();
      const endYear = endDate ? new Date(endDate).getFullYear() : 'Presente';
      return `${startYear} - ${endYear}`;
    },
    calculateTotalDuration() {
      if (!this.saga?.producciones) return 'N/A';
      const totalMinutes = this.saga.producciones.reduce(
          (total, movie) => total + (movie.duracion || 0), 0);

      const hours = Math.floor(totalMinutes / 60);
      const mins = totalMinutes % 60;
      return `${hours}h ${mins}m`;
    },
    mapProductionToMovieCard(production) {
      return {
        id: production.id,
        title: production.titulo,
        type: production.tipo,
        year: new Date(production.estreno).getFullYear(),
        duration: production.duracion,
        plot: production.sinopsis,
        poster: production.imagen,
        puntuacion: production.puntuacion,
        clasificacionEdad: production.clasificacionEdad,
        genres: production.categorias || [],
        isFavorite: false // Esto debería venir de tu estado de favoritos
      };
    },
    toggleFavorite(movieId) {
      // Implementar lógica para alternar favoritos
      console.log(`Toggle favorite for movie ${movieId}`);
    }
  },
  watch: {
    '$route.params.id': {
      immediate: true,
      handler() {
        this.loadSagaData();
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
}

.dark-mode {
  --color-secondary: #2a2a3a;
  --color-text: #e9ecef;
  --color-bg: #121212;
  --color-card: #1e1e2e;
  --color-border: #2d3748;
  --modal-bg: rgba(20, 20, 30, 0.95);
}

.saga-page {
  background-color: var(--color-bg);
  color: var(--color-text);
  transition: var(--transition);
  min-height: 100vh;
}

.saga-hero {
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

.saga-hero:hover .hero-backdrop {
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

.saga-poster-container {
  flex: 0 0 280px;
  position: relative;
  transform: translateY(25%);
  margin-bottom: -80px;
  transition: transform 0.3s ease;
}

.saga-poster-container:hover {
  transform: translateY(15%);
}

.saga-poster {
  width: 100%;
  border-radius: 8px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  object-fit: cover;
  object-position: center top;
  height: 120%;
}

.saga-poster:hover {
  transform: scale(1.02);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.4);
}

.saga-badges {
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
  backdrop-filter: blur(5px);
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  transition: transform 0.2s ease;
}

.badge:hover {
  transform: scale(1.05);
}

.badge.status {
  background-color: rgba(126, 91, 239, 0.8);
}

.saga-info {
  flex: 1;
  color: white;
}

.saga-title {
  font-size: 3rem;
  font-weight: 800;
  margin-bottom: 1rem;
  line-height: 1.2;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
  animation: fadeIn 0.8s ease 0.2s both;
}

.saga-meta {
  display: flex;
  gap: 1rem;
  margin-bottom: 2rem;
  font-size: 1.1rem;
  opacity: 0.9;
  animation: fadeIn 0.8s ease 0.4s both;
}

/* Main Content */
.saga-main {
  max-width: 1400px;
  margin: 5rem auto 3rem;
  padding: 0 2rem;
  display: flex;
  gap: 3rem;
}

.saga-content {
  flex: 2;
}

.saga-sidebar {
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

.saga-section {
  margin-bottom: 3rem;
  animation: fadeInUp 0.6s ease;
}

.saga-description {
  line-height: 1.7;
  font-size: 1.1rem;
  margin-bottom: 1.5rem;
  white-space: pre-line;
}

/* Movies Grid */
.movies-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 2rem;
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

.saga-facts {
  list-style: none;
  padding: 0;
}

.saga-facts li {
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

/* Timeline */
.timeline {
  position: relative;
  padding-left: 1.5rem;
}

.timeline::before {
  content: '';
  position: absolute;
  top: 0;
  bottom: 0;
  left: 7px;
  width: 2px;
  background: var(--color-primary);
}

.timeline-item {
  position: relative;
  padding-bottom: 1.5rem;
}

.timeline-item.first .timeline-dot::after {
  content: '';
  position: absolute;
  top: -5px;
  left: -5px;
  right: -5px;
  bottom: -5px;
  border: 2px solid var(--color-primary-light);
  border-radius: 50%;
  animation: pulse 2s infinite;
}

.timeline-item.last {
  padding-bottom: 0;
}

.timeline-dot {
  position: absolute;
  left: -1.5rem;
  top: 0;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background-color: var(--color-primary);
  z-index: 1;
}

.timeline-content {
  padding-left: 1rem;
}

.timeline-content h4 {
  font-size: 1.1rem;
  margin-bottom: 0.3rem;
}

.timeline-content p {
  font-size: 0.9rem;
  opacity: 0.8;
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

@keyframes expandWidth {
  from {
    width: 0;
  }
  to {
    width: 60px;
  }
}

@keyframes pulse {
  0% {
    opacity: 0.6;
    transform: scale(1);
  }
  50% {
    opacity: 1;
    transform: scale(1.05);
  }
  100% {
    opacity: 0.6;
    transform: scale(1);
  }
}

/* Loading State */
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

.error-container p {
  font-size: 1.2rem;
  margin-bottom: 1rem;
}

/* Responsive */
@media (max-width: 1200px) {
  .hero-content {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .saga-poster-container {
    flex: 0 0 auto;
    max-width: 300px;
  }

  .saga-meta {
    justify-content: center;
  }

  .saga-main {
    flex-direction: column;
  }
}

@media (max-width: 768px) {
  .saga-hero {
    height: auto;
    min-height: auto;
    padding-top: 5rem;
    padding-bottom: 2rem;
  }

  .saga-title {
    font-size: 2.2rem;
  }

  .movies-grid {
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  }
}

@media (max-width: 480px) {
  .hero-content {
    padding: 0 1rem;
  }

  .movies-grid {
    grid-template-columns: 1fr;
  }
}
</style>