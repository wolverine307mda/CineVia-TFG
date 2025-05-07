<template>
  <Header :dark-mode="darkMode" @toggle-dark-mode="toggleDarkMode"/>

  <div class="general-container" :class="{ 'dark-mode': darkMode }">

    <!-- Sección de Producciones -->
    <section class="category-section">
      <div class="section-header">
        <h2 class="section-title">Producciones</h2>
        <div class="section-controls">
          <button class="nav-btn" @click="scrollLeft('productions')">
            <i class="fas fa-chevron-left"></i>
          </button>
          <button class="nav-btn" @click="scrollRight('productions')">
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>

      <div class="items-container" ref="productionsContainer">
        <div class="items-scroll">
          <MovieCard
              v-for="production in productions"
              :key="production.id"
              :movie="production"
              :dark-mode="darkMode"
              class="production-card"
              @toggle-favorite="toggleFavorite(production.id, 'PRODUCCION')"
          />
        </div>
      </div>
      <button class="view-more-btn" @click="viewMore('productions')">
        Ver más producciones <i class="fas fa-chevron-right"></i>
      </button>
    </section>

    <!-- Sección de Sagas -->
    <section class="category-section">
      <div class="section-header">
        <h2 class="section-title">Sagas</h2>
        <div class="section-controls">
          <button class="nav-btn" @click="scrollLeft('sagas')">
            <i class="fas fa-chevron-left"></i>
          </button>
          <button class="nav-btn" @click="scrollRight('sagas')">
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>

      <div class="items-container" ref="sagasContainer">
        <div class="items-scroll">
          <SagaCard
              v-for="saga in sagas"
              :key="saga.id"
              :saga="saga"
              :dark-mode="darkMode"
              class="saga-card"
          />
        </div>
      </div>
      <button class="view-more-btn" @click="viewMore('sagas')">
        Ver más sagas <i class="fas fa-chevron-right"></i>
      </button>
    </section>

    <!-- Sección de Profesionales -->
    <section class="category-section">
      <div class="section-header">
        <h2 class="section-title">Profesionales</h2>
        <div class="section-controls">
          <button class="nav-btn" @click="scrollLeft('professionals')">
            <i class="fas fa-chevron-left"></i>
          </button>
          <button class="nav-btn" @click="scrollRight('professionals')">
            <i class="fas fa-chevron-right"></i>
          </button>
        </div>
      </div>

      <div class="items-container" ref="professionalsContainer">
        <div class="items-scroll">
          <ProfessionalCard
              v-for="professional in professionals"
              :key="professional.id"
              :professional="professional"
              :dark-mode="darkMode"
              class="professional-card"
              @toggle-favorite="toggleFavorite(professional.id, 'PROFESIONAL')"
          />
        </div>
      </div>
      <button class="view-more-btn" @click="viewMore('professionals')">
        Ver más profesionales <i class="fas fa-chevron-right"></i>
      </button>
    </section>

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
        productions: { page: 0, size: 10 },
        sagas: { page: 0, size: 10 },
        professionals: { page: 0, size: 10 }
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
  methods: {
    toggleDarkMode() {
      this.darkMode = !this.darkMode;
      localStorage.setItem('darkMode', this.darkMode);
    },
    scrollLeft(section) {
      const container = this.$refs[`${section}Container`];
      container.scrollBy({ left: -300, behavior: 'smooth' });
    },
    scrollRight(section) {
      const container = this.$refs[`${section}Container`];
      container.scrollBy({ left: 300, behavior: 'smooth' });
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
        const { page, size } = this.pagination.productions;
        const response = await axios.get('/api/producciones/filtrar', {
          params: { page, size }
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
.general-container {
  padding: 2rem;
  min-height: 100vh;
  background-image: url('@/assets/fondo_general.jpg');
  background-size: cover;
  background-position: center;
  position: relative;
}

.general-container::before {
  content: "";
  position: absolute;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.55);
  z-index: 0;
}

.general-container > * {
  position: relative;
  z-index: 1;
}

.category-section {
  margin: 3rem 0;
  padding: 1.5rem;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.dark-mode .category-section {
  background: rgba(15, 15, 25, 0.7);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.section-title {
  font-size: 2rem;
  font-weight: 700;
  color: white;
}

.section-controls {
  display: flex;
  gap: 0.5rem;
}

.nav-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: #6c5ce7;
  color: white;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.nav-btn:hover {
  background-color: #a29bfe;
}

.items-container {
  width: 100%;
  overflow-x: auto;
  padding-bottom: 1rem;
  scrollbar-width: none;
}

.items-container::-webkit-scrollbar {
  display: none;
}

.items-scroll {
  display: inline-flex;
  gap: 1.5rem;
  padding: 0.5rem;
}

/* Tarjetas de Producción (Películas/Series) */
.production-card {
  width: 300px;
  height: 720px;
  flex-shrink: 0;
}

/* Tarjetas de Saga */
.saga-card {
  width: 300px;
  min-width: 260px;
  height: 680px;
  flex-shrink: 0;
}

/* Tarjetas de Profesionales */
.professional-card {
  width: 300px;
  height: 700px;
  flex-shrink: 0;
}

.view-more-btn {
  display: block;
  width: fit-content;
  margin: 1.5rem auto 0;
  padding: 0.8rem 1.8rem;
  background: #6c5ce7;
  color: white;
  border: none;
  border-radius: 50px;
  font-weight: 600;
  cursor: pointer;
}

.view-more-btn:hover {
  background: #a29bfe;
}
</style>