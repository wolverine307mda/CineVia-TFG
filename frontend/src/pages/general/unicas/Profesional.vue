<template>
  <Header :dark-mode="darkMode" @toggle-dark-mode="toggleDarkMode" />

  <div class="professional-page" :class="{ 'dark-mode': darkMode }">
    <!-- Loading State -->
    <div v-if="loading" class="loading-container">
      <div class="spinner"></div>
      <p>Cargando profesional...</p>
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
    <template v-else-if="professional">
      <!-- Minimal Hero Section -->
      <section class="professional-hero">
        <div class="hero-content">
          <img :src="professional.foto" :alt="professional.nombre" class="professional-photo" loading="lazy">
        </div>
      </section>
      <h1 class="professional-title">{{ professional.nombre }}</h1>

      <!-- Main Content -->
      <div class="professional-container">
        <!-- Basic Info Section -->
        <section class="info-section">
          <div class="info-item">
            <span class="info-label">Nacimiento:
            <span class="info-value" style="color: white">{{ professional.birthDate }}</span></span>
          </div>
          <div class="info-item">
            <span class="info-label">Lugar:
            <span class="info-value" style="color: white">{{ professional.lugarNacimiento }}</span></span>
          </div>
          <div class="info-item">
            <span class="info-label">Carrera:
            <span class="info-value" style="color: white">Desde {{ professional.startDate }}</span></span>
          </div>
        </section>

        <!-- Biography -->
        <section class="bio-section" v-if="professional.biografia">
          <h2 class="section-title">Biografía</h2>
          <p class="bio-text">{{ professional.biografia }}</p>
        </section>

        <!-- Participaciones -->
        <section class="works-section">
          <h2 class="section-title">
            Participaciones
            <span class="works-count">{{ professional.participaciones }}</span>
          </h2>

          <div v-if="participacionesLoading" class="loading-participaciones">
            <div class="small-spinner"></div>
            <p>Cargando filmografía...</p>
          </div>

          <div v-else-if="participacionesError" class="error-participaciones">
            <i class="fas fa-exclamation-circle"></i>
            <p>{{ participacionesError }}</p>
          </div>

          <div v-else class="participaciones-container">
            <div v-for="participacion in participaciones" :key="participacion.id" class="participacion-card">
              <router-link :to="`/produccion/${participacion.id}`" class="participacion-link">
                <div class="poster-container">
                  <img
                      :src="participacion.imagen"
                      :alt="participacion.titulo"
                      class="production-poster"
                      loading="lazy"
                  >
                  <div class="production-badge">
                    {{ participacion.tipo === 'PELICULA' ? 'Película' : 'Serie' }}
                  </div>
                </div>
                <div class="participacion-info">
                  <h3 class="production-title">{{ participacion.titulo }}</h3>
                  <div class="production-meta">
                    <span class="production-year">{{ new Date(participacion.estreno).getFullYear() }}</span>
                    <span class="production-rating" v-if="participacion.puntuacion">
                      <i class="fas fa-star"></i> {{ participacion.puntuacion.toFixed(1) }}
                    </span>
                  </div>
                  <div class="role-info">
                    <span class="role-label">{{ participacion.rol }}</span>
                    <span class="role-character" v-if="participacion.papel">como {{ participacion.papel }}</span>
                  </div>
                  <p class="production-sinopsis" v-if="participacion.sinopsis">
                    {{ truncateText(participacion.sinopsis, 120) }}
                  </p>
                </div>
              </router-link>
            </div>
          </div>
        </section>
      </div>
    </template>

    <Footer :dark-mode="darkMode" />
  </div>
</template>

<script>
import Header from "@/components/principal/Header.vue";
import Footer from "@/components/principal/Footer.vue";
import ProfesionalesService from '@/services/profesional.service.js';

export default {
  name: 'ProfessionalDetail',
  components: { Footer, Header },
  mounted() {
    window.scrollTo(0, 0);
  },
  data() {
    return {
      darkMode: false,
      professional: null,
      participaciones: [],
      loading: true,
      participacionesLoading: false,
      participacionesError: null,
      error: null
    }
  },
  methods: {
    async loadProfessionalData() {
      this.loading = true;
      this.error = null;
      try {
        const idProfessional = this.$route.params.id;
        this.professional = await ProfesionalesService.getProfessionalById(idProfessional);
        await this.loadParticipaciones(idProfessional);
      } catch (error) {
        console.error('Error al cargar el profesional:', error);
        this.error = 'No se pudo cargar la información del profesional.';
      } finally {
        this.loading = false;
      }
    },

    async loadParticipaciones(profesionalId) {
      this.participacionesLoading = true;
      this.participacionesError = null;
      try {
        const participaciones = await ProfesionalesService.getParticipacionesByProfesional(profesionalId);
        this.participaciones = participaciones.map(p => ({
          ...p,
          tipo: p.tipo || 'PELICULA' // Asegurar que siempre haya un tipo
        }));
      } catch (error) {
        console.error('Error al cargar participaciones:', error);
        this.participacionesError = 'No se pudieron cargar las participaciones.';
      } finally {
        this.participacionesLoading = false;
      }
    },

    retryLoading() {
      this.loadProfessionalData();
    },

    toggleDarkMode() {
      this.darkMode = !this.darkMode;
      localStorage.setItem('darkMode', this.darkMode);
    },

    truncateText(text, length) {
      if (!text) return '';
      return text.length > length ? text.substring(0, length) + '...' : text;
    }
  },
  watch: {
    '$route.params.id': {
      immediate: true,
      handler() {
        this.loadProfessionalData();
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
/* Base Styles */
.professional-page {
  background-color: var(--color-bg);
  color: var(--color-text);
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* Hero Section */
.professional-hero {
  position: relative;
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image:
      linear-gradient(rgba(126, 91, 239, 0.1), rgba(126, 91, 239, 0.1)),
      url("https://images.unsplash.com/photo-1573614999645-e5f0f16ec15d?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  margin-bottom: 3rem;
  overflow: hidden;
}

.professional-hero::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  z-index: 0;
}

.hero-content {
  position: relative;
  z-index: 1;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding-top: 2rem;
}

.professional-photo {
  margin-top: -1rem;
  width: 250px;
  height: 250px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid white;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  animation: float 6s ease-in-out infinite;
  transform-style: preserve-3d;
  transition: all 0.5s ease;
}

.professional-photo:hover {
  animation: pulse 1.5s ease infinite, float 6s ease-in-out infinite;
  transform: scale(1.05) rotateY(15deg);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.3);
}

.professional-title {
  margin-top: -2rem;
  margin-bottom: -0.5rem;
  font-size: 3.2rem;
  font-weight: 700;
  text-align: center;
  color: var(--color-text);
}

/* Container */
.professional-container {
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 0 1.5rem 3rem;
  flex: 1;
}

/* Info Section */
.info-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin: 2rem 0 3rem;
  padding: 1.5rem;
  background-color: var(--color-card);
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.info-item {
  display: flex;
  flex-direction: column;
}

.info-label {
  font-weight: 600;
  color: var(--color-primary);
  margin-bottom: 0.3rem;
  font-size: 0.95rem;
}

.info-value {
  font-size: 1.05rem;
}

/* Bio Section */
.bio-section {
  margin-bottom: 3rem;
}

.bio-text {
  line-height: 1.7;
  color: var(--color-text);
}

/* Works Section */
.works-section {
  margin-top: 2rem;
}

.section-title {
  font-size: 1.5rem;
  margin-bottom: 1.5rem;
  display: flex;
  align-items: center;
  gap: 0.8rem;
  color: var(--color-primary);
}

.works-count {
  background-color: var(--color-primary);
  color: white;
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 1rem;
  font-weight: 600;
}

/* Participaciones Container */
.participaciones-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 2rem;
}

.participacion-card {
  background-color: var(--color-card);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.participacion-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

.participacion-link {
  display: flex;
  flex-direction: column;
  height: 100%;
  text-decoration: none;
  color: inherit;
}

.poster-container {
  position: relative;
  width: 100%;
  height: 180px;
  overflow: hidden;
}

.production-poster {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.participacion-card:hover .production-poster {
  transform: scale(1.05);
}

.production-badge {
  position: absolute;
  top: 0.5rem;
  right: 0.5rem;
  background-color: var(--color-primary);
  color: white;
  padding: 0.3rem 0.8rem;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 600;
}

.participacion-info {
  padding: 1.5rem;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.production-title {
  font-size: 1.2rem;
  margin-bottom: 0.5rem;
  color: var(--color-primary);
}

.production-meta {
  display: flex;
  gap: 1rem;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
  opacity: 0.8;
}

.role-info {
  margin: 0.5rem 0;
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
}

.role-label {
  font-weight: 600;
  color: var(--color-primary-light);
  font-size: 0.9rem;
}

.role-character {
  font-style: italic;
  font-size: 0.9rem;
  opacity: 0.9;
}

.production-sinopsis {
  font-size: 0.9rem;
  line-height: 1.5;
  margin-top: 0.5rem;
  opacity: 0.8;
}

/* Animations */
@keyframes float {
  0%, 100% {
    transform: translateY(0) rotateY(0);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }
  50% {
    transform: translateY(-10px) rotateY(5deg);
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
  }
}

@keyframes pulse {
  0%, 100% {
    transform: scale(1.05) rotateY(15deg);
  }
  50% {
    transform: scale(1.08) rotateY(20deg);
  }
}

/* Loading States */
.loading-container,
.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 50vh;
  gap: 1rem;
  text-align: center;
}

.loading-participaciones {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 3rem;
  gap: 1rem;
}

.spinner,
.small-spinner {
  border: 3px solid rgba(0, 0, 0, 0.1);
  border-radius: 50%;
  border-top: 3px solid var(--color-primary);
  animation: spin 1s linear infinite;
}

.spinner {
  width: 40px;
  height: 40px;
}

.small-spinner {
  width: 30px;
  height: 30px;
}

.error-participaciones {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  padding: 1rem;
  background-color: rgba(255, 0, 0, 0.05);
  border-radius: 8px;
  color: #ff4757;
  margin: 1rem 0;
}

.error-container i,
.error-participaciones i {
  font-size: 2rem;
  color: #ff4757;
  animation: pulse 1.5s infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Responsive */
@media (max-width: 768px) {
  .professional-title {
    font-size: 1.8rem;
  }

  .info-section {
    grid-template-columns: 1fr;
  }

  .participaciones-container {
    grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  }
}

@media (max-width: 480px) {
  .professional-hero {
    padding: 2rem 1rem 1.5rem;
    min-height: 200px;
    justify-content: flex-start;
    padding-top: 3rem;
  }

  .professional-photo {
    width: 120px;
    height: 120px;
  }

  .professional-title {
    font-size: 1.5rem;
    margin-top: 1rem;
  }

  .professional-container {
    padding: 0 1rem 2rem;
  }

  .section-title {
    font-size: 1.3rem;
  }

  .participaciones-container {
    grid-template-columns: 1fr;
  }
}
</style>