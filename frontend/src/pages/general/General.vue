<template>
  <Header :dark-mode="darkMode" @toggle-dark-mode="toggleDarkMode" />

  <div class="general-container" :class="{ 'dark-mode': darkMode }">

    <!-- Hero Section Simplificada -->
    <section class="hero-section">
      <div class="hero-content">
        <h1 class="hero-title">Explora el Universo Cinematográfico</h1>
        <p class="hero-subtitle">Descubre lo mejor del cine y la televisión en un solo lugar</p>
      </div>
      <div class="hero-overlay"></div>
    </section>

    <!-- Sección de Producciones (Izquierda) -->
    <section class="category-section left-layout" data-aos="fade-up">
      <div class="section-intro">
        <h2 class="section-title">Producciones</h2>
        <div class="section-description">
          <p>Explora nuestras películas y series más destacadas. Desde los últimos estrenos hasta clásicos atemporales.</p>
          <p>Calificaciones reales de usuarios y críticos para ayudarte a encontrar tu próxima historia favorita.</p>
        </div>
        <button class="view-more-btn" @click="viewMore('productions')">
          Ver más <i class="fas fa-chevron-right"></i>
        </button>
      </div>

      <div class="items-wrapper">
        <div class="items-container" ref="productionsContainer">
          <div class="items-scroll">
            <MovieCard
                v-for="(production, index) in productions"
                :key="production.id"
                :movie="production"
                :dark-mode="darkMode"
                class="production-card"
                :style="`--delay: ${index * 0.05}s`"
                @toggle-favorite="toggleFavorite(production.id, 'PRODUCCION')"
            />
          </div>
        </div>
        <div class="scroll-controls">
          <button class="nav-btn" @click="scrollLeft('productions')" aria-label="Scroll left">
            <i class="fas fa-chevron-left"></i>
          </button>
          <button class="nav-btn" @click="scrollRight('productions')" aria-label="Scroll right">
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>
    </section>

    <!-- Sección de Sagas (Derecha) -->
    <section class="category-section right-layout" data-aos="fade-up">
      <div class="section-intro">
        <h2 class="section-title">Sagas</h2>
        <div class="section-description">
          <p>Sumérgete en universos narrativos expandidos. Colecciones de historias que trascienden una sola película.</p>
          <p>Descubre el orden cronológico, personajes recurrentes y conexiones entre producciones.</p>
        </div>
        <button class="view-more-btn" @click="viewMore('sagas')">
          Ver más <i class="fas fa-chevron-right"></i>
        </button>
      </div>

      <div class="items-wrapper">
        <div class="items-container" ref="sagasContainer">
          <div class="items-scroll">
            <SagaCard
                v-for="(saga, index) in sagas"
                :key="saga.id"
                :saga="saga"
                :dark-mode="darkMode"
                class="saga-card"
                :style="`--delay: ${index * 0.05}s`"
            />
          </div>
        </div>
        <div class="scroll-controls">
          <button class="nav-btn" @click="scrollLeft('sagas')" aria-label="Scroll left">
            <i class="fas fa-chevron-left"></i>
          </button>
          <button class="nav-btn" @click="scrollRight('sagas')" aria-label="Scroll right">
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>
    </section>

    <!-- Sección de Profesionales (Izquierda) -->
    <section class="category-section left-layout" data-aos="fade-up">
      <div class="section-intro">
        <h2 class="section-title">Profesionales</h2>
        <div class="section-description">
          <p>Conoce a los talentos creativos detrás de cámaras y en pantalla. Directores, actores, guionistas y más.</p>
          <p>Explora sus filmografías completas y descubre cómo han dado forma a la industria.</p>
        </div>
        <button class="view-more-btn" @click="viewMore('professionals')">
          Ver más <i class="fas fa-chevron-right"></i>
        </button>
      </div>

      <div class="items-wrapper">
        <div class="items-container" ref="professionalsContainer">
          <div class="items-scroll">
            <ProfessionalCard
                v-for="(professional, index) in professionals"
                :key="professional.id"
                :professional="professional"
                :dark-mode="darkMode"
                class="professional-card"
                :style="`--delay: ${index * 0.05}s`"
                @toggle-favorite="toggleFavorite(professional.id, 'PROFESIONAL')"
            />
          </div>
        </div>
        <div class="scroll-controls">
          <button class="nav-btn" @click="scrollLeft('professionals')" aria-label="Scroll left">
            <i class="fas fa-chevron-left"></i>
          </button>
          <button class="nav-btn" @click="scrollRight('professionals')" aria-label="Scroll right">
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>
    </section><br>

  </div>

  <Footer :dark-mode="darkMode" />
</template>

<script>
import Header from "@/components/principal/Header.vue";
import Footer from "@/components/principal/Footer.vue";
import MovieCard from "@/components/cards/MovieCard.vue";
import ProfessionalCard from "@/components/cards/ProfessionalCard.vue";
import SagaCard from "@/components/cards/SagaCard.vue";
import axios from 'axios';
import AOS from 'aos';
import 'aos/dist/aos.css';

export default {
  name: 'GeneralPage',
  components: { Footer, Header, MovieCard, ProfessionalCard, SagaCard },
  data() {
    return {
      darkMode: false,
      productions: [],
      sagas: [],
      professionals: [],
      favoritos: [],
      pagination: {
        productions: {page: 0, size: 10},
        sagas: {page: 0, size: 10},
        professionals: {page: 0, size: 10}
      },
      scrollIntervals: {
        productions: null,
        sagas: null,
        professionals: null
      }
    }
  },
  created() {
    const savedMode = localStorage.getItem('darkMode');
    if (savedMode) {
      this.darkMode = savedMode === 'true';
    }

    this.fetchProductions();
    this.fetchSagas();
    this.fetchProfessionals();
  },
  mounted() {
    AOS.init({
      duration: 800,
      easing: 'ease-in-out',
      once: false,
      offset: 120
    });

    // Iniciar auto-scroll
    this.startAutoScroll('productions');
    this.startAutoScroll('sagas');
    this.startAutoScroll('professionals');

    // Pausar auto-scroll al hacer hover
    const sections = ['productions', 'sagas', 'professionals'];
    sections.forEach(section => {
      const container = this.$refs[`${section}Container`];
      if (container) {
        container.addEventListener('mouseenter', () => this.stopAutoScroll(section));
        container.addEventListener('mouseleave', () => this.startAutoScroll(section));
      }
    });
  },
  beforeUnmount() {
    // Limpiar intervalos al desmontar
    Object.keys(this.scrollIntervals).forEach(section => {
      this.stopAutoScroll(section);
    });
  },
  methods: {
    toggleDarkMode() {
      this.darkMode = !this.darkMode;
      localStorage.setItem('darkMode', this.darkMode);
    },
    scrollLeft(section) {
      const container = this.$refs[`${section}Container`];
      container.scrollBy({left: -300, behavior: 'smooth'});
    },
    scrollRight(section) {
      const container = this.$refs[`${section}Container`];
      container.scrollBy({left: 300, behavior: 'smooth'});
    },
    startAutoScroll(section) {
      this.stopAutoScroll(section); // Limpiar cualquier intervalo existente
      this.scrollIntervals[section] = setInterval(() => {
        const container = this.$refs[`${section}Container`];
        if (container) {
          // Verificar si hemos llegado al final
          if (container.scrollLeft + container.clientWidth >= container.scrollWidth - 10) {
            container.scrollTo({left: 0, behavior: 'smooth'});
          } else {
            container.scrollBy({left: 300, behavior: 'smooth'});
          }
        }
      }, 5000); // Desplazamiento cada 5 segundos
    },
    stopAutoScroll(section) {
      if (this.scrollIntervals[section]) {
        clearInterval(this.scrollIntervals[section]);
        this.scrollIntervals[section] = null;
      }
    },
    viewMore(section) {
      this.pagination[section].page++;
      if (section === 'productions') this.fetchProductions();
      else if (section === 'sagas') this.fetchSagas();
      else if (section === 'professionals') this.fetchProfessionals();
    },
    toggleFavorite(id, type) {
      const index = this.favoritos.findIndex(f => f.referenciaId === id && f.tipo === type);
      if (index >= 0) {
        this.favoritos.splice(index, 1);
      } else {
        this.favoritos.push({
          id: Date.now().toString(),
          usuarioId: 'current-user',
          tipo: type,
          referenciaId: id,
          fecha: new Date()
        });
      }
    },
    isFavorite(id, type) {
      return this.favoritos.some(f => f.referenciaId === id && f.tipo === type);
    },
    async fetchProductions() {
      try {
        const {page, size} = this.pagination.productions;
        const response = await axios.get('/api/producciones/filtrar', {
          params: {page, size}
        });
        this.productions = response.data.data.map(item => ({
          id: item.id,
          title: item.titulo,
          type: item.tipo,
          year: item.estreno ? new Date(item.estreno).getFullYear() : 'N/A',
          duration: item.duracion || 0,
          plot: item.sinopsis || 'Sin descripción disponible',
          poster: item.imagen || 'https://via.placeholder.com/300x450?text=' + item.titulo,
          puntuacion: item.puntuacion || 0,
          clasificacionEdad: item.clasificacionEdad || 0,
          genres: item.categorias || [],
          isFavorite: this.isFavorite(item.id, 'PRODUCCION')
        }));
      } catch (error) {
        console.error('Error fetching productions:', error);
      }
    },
    async fetchSagas() {
      try {
        const {page, size} = this.pagination.sagas;
        const response = await axios.get('/api/sagas/filter', {
          params: {page, size}
        });
        this.sagas = response.data.data.map(item => ({
          id: item.id,
          nombre: item.nombre,
          descripcion: item.descripcion || 'Sin descripción disponible',
          isAcabada: item.isAcabada || false,
          fechaInicio: item.fechaInicio ? new Date(item.fechaInicio) : null,
          fechaFin: item.fechaFin ? new Date(item.fechaFin) : null,
          imagen: item.imagen || 'https://via.placeholder.com/300x450?text=' + item.nombre,
          producciones: item.producciones || []
        }));
      } catch (error) {
        console.error('Error fetching sagas:', error);
      }
    },
    async fetchProfessionals() {
      try {
        const {page, size} = this.pagination.professionals;
        const response = await axios.get('/api/profesionales/filtrar', {
          params: {page, size}
        });
        this.professionals = response.data.data.map(item => ({
          id: item.id,
          nombre: item.nombre,
          foto: item.foto || 'https://via.placeholder.com/300x450?text=' + item.nombre,
          fechaNacimiento: item.fechaNacimiento ? new Date(item.fechaNacimiento) : null,
          lugarNacimiento: item.lugarNacimiento || 'Desconocido',
          biografia: item.biografia || 'Sin biografía disponible',
          fechaInicio: item.fechaInicio ? new Date(item.fechaInicio) : null,
          participacionesCount: item.participacionesCount || 0,
          isFavorite: this.isFavorite(item.id, 'PROFESIONAL')
        }));
      } catch (error) {
        console.error('Error fetching professionals:', error);
      }
    }
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap');

.general-container {
  font-family: 'Poppins', sans-serif;
  min-height: 100vh;
  position: relative;
  background-color: #f8f9fa;
  transition: all 0.3s ease;
}

.general-container.dark-mode {
  background-color: #121212;
  color: #f8f9fa;
}

/* Hero Section */
.hero-section {
  position: relative;
  height: 60vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-image: url('@/assets/fondo_home.jpg');
  background-size: cover;
  background-position: center;
  margin-bottom: 4rem;
  overflow: hidden;
}

.hero-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(to right, rgba(0, 0, 0, 0.8), rgba(0, 0, 0, 0.4));
  z-index: 1;
}

.hero-content {
  position: relative;
  z-index: 2;
  text-align: center;
  padding: 2rem;
  max-width: 800px;
  margin: 0 auto;
  color: white;
  animation: fadeInUp 1s ease-out;
}

.hero-title {
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 1rem;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.hero-subtitle {
  font-size: 1.3rem;
  margin-bottom: 2rem;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
}

/* Category Sections */
.category-section {
  margin: 0 auto 6rem;
  padding: 2rem 0;
  max-width: 1400px;
  display: flex;
  align-items: center;
  gap: 3rem;
}

.left-layout {
  flex-direction: row;
}

.right-layout {
  flex-direction: row-reverse;
}

.section-intro {
  flex: 1;
  padding: 2rem;
  margin: 3rem 0;
  min-width: 300px;
  background-color: rgba(255, 255, 255, 0.65); /* más opaco */
  border-radius: 20px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
  animation: fadeInSlide 0.8s ease-out;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  color: #ffffff;
  transition: background-color 0.3s, color 0.3s;
}

/* Modo oscuro */
.dark-mode .section-intro {
  background-color: rgba(37, 37, 37, 0.65); /* tono morado más opaco */
  box-shadow: 0 20px 50px rgba(255, 255, 255, 0.05);
  color: #f1f1f1;
}

/* Animación */
@keyframes fadeInSlide {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}


.section-title {
  font-size: 2.2rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
  color: #2d3436;
  transition: color 0.3s;
}

.dark-mode .section-title {
  color: #f8f9fa;
}

.section-description {
  font-size: 1.1rem;
  color: #636e72;
  margin-bottom: 2rem;
  line-height: 1.6;
  transition: color 0.3s;
}

.dark-mode .section-description {
  color: #b2bec3;
}

.items-wrapper {
  flex: 2;
  position: relative;
  min-width: 0;
}

.items-container {
  width: 100%;
  overflow-x: auto;
  padding-bottom: 1.5rem;
  scrollbar-width: none;
  scroll-behavior: smooth;
}

.items-container::-webkit-scrollbar {
  display: none;
}

.items-scroll {
  display: inline-flex;
  gap: 1.8rem;
  padding: 0.5rem;
}

.scroll-controls {
  display: flex;
  justify-content: center;
  gap: 1rem;
  margin-top: 1rem;
}

/* Cards (Manteniendo tamaños originales) */
.production-card {
  width: 300px;
  height: 720px;
  flex-shrink: 0;
  animation: fadeIn 0.5s ease-out both;
  animation-delay: var(--delay);
}

.saga-card {
  width: 300px;
  min-width: 260px;
  height: 680px;
  flex-shrink: 0;
  animation: fadeIn 0.5s ease-out both;
  animation-delay: var(--delay);
}

.professional-card {
  width: 300px;
  height: 700px;
  flex-shrink: 0;
  animation: fadeIn 0.5s ease-out both;
  animation-delay: var(--delay);
}

/* Navigation Buttons */
.nav-btn {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background-color: #6c5ce7;
  color: white;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 8px rgba(108, 92, 231, 0.3);
}

.nav-btn:hover {
  background-color: #5649c0;
  transform: scale(1.1);
}

.nav-btn:active {
  transform: scale(0.95);
}

/* View More Button */
.view-more-btn {
  display: inline-flex;
  align-items: center;
  padding: 0.8rem 1.8rem;
  background: #6c5ce7;
  color: white;
  border: none;
  border-radius: 50px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 8px rgba(108, 92, 231, 0.3);
}

.view-more-btn:hover {
  background: #5649c0;
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(108, 92, 231, 0.4);
}

.view-more-btn i {
  margin-left: 0.5rem;
  transition: transform 0.3s;
}

.view-more-btn:hover i {
  transform: translateX(3px);
}

/* Animations */
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

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Responsive Design */
@media (max-width: 1024px) {
  .category-section {
    flex-direction: column;
    gap: 2rem;
  }

  .left-layout, .right-layout {
    flex-direction: column;
  }

  .section-intro {
    text-align: center;
    padding: 0 1rem;
  }

  .items-wrapper {
    width: 100%;
  }
}

@media (max-width: 768px) {
  .hero-section {
    height: 50vh;
  }

  .hero-title {
    font-size: 2.2rem;
  }

  .hero-subtitle {
    font-size: 1.1rem;
  }

  .section-title {
    font-size: 1.8rem;
  }
}

@media (max-width: 480px) {
  .hero-section {
    height: 40vh;
  }

  .hero-title {
    font-size: 1.8rem;
  }

  .section-description {
    font-size: 1rem;
  }

  .view-more-btn {
    padding: 0.7rem 1.5rem;
  }
}

/*
.general-container {
  font-family: 'Poppins', sans-serif;
  min-height: 100vh;
  position: relative;
  background-color: #ffffff;
  background-image: url("data:image/svg+xml;utf8,\
    <svg xmlns='http://www.w3.org/2000/svg' width='300' height='300'>\
      <text x='0' y='60' font-size='60'>🚀</text>\
      <text x='100' y='160' font-size='60'>🍿</text>\
      <text x='200' y='260' font-size='60'>🎥</text>\
    </svg>");
  background-size: 300px 300px;
  background-repeat: repeat;
  animation: diagonalScroll 100s linear infinite;
  transition: background-color 0.3s ease;
}

.general-container.dark-mode {
  background-color: #121212;
  background-image: url("data:image/svg+xml;utf8,\
    <svg xmlns='http://www.w3.org/2000/svg' width='300' height='300'>\
      <text x='0' y='60' font-size='60' fill='white'>🚀</text>\
      <text x='100' y='160' font-size='60' fill='white'>🍿</text>\
      <text x='200' y='260' font-size='60' fill='white'>🎥</text>\
    </svg>");
  background-size: 300px 300px;
  background-repeat: repeat;
  animation: diagonalScroll 100s linear infinite;
  color: #f8f9fa;
}*/

.general-container {
  font-family: 'Poppins', sans-serif;
  min-height: 100vh;
  position: relative;
  background: linear-gradient(-45deg,
  #4b0082,
  #ff6b00,
  #6a0dad,
  #e85d04,
  #8a2be2,
  #ff8800,
  #9d4edd,
  #f48c06);
  background-size: 500% 500%;
  animation: gradientBackground 50s ease infinite;
  transition: background 0.3s ease;
}

/* 🌌 Animación fluida del degradado */
@keyframes gradientBackground {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}


</style>