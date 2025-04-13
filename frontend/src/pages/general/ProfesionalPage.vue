<template>
  <Header :dark-mode="darkMode" @toggle-dark-mode="toggleDarkMode"/>

  <div class="professionals-container" :class="{ 'dark-mode': darkMode }">
    <!-- Encabezado con título y modo oscuro -->
    <div class="header-section">
      <h1 class="main-title">
        <i class="fas fa-users"></i> Directorio de Profesionales
      </h1>
    </div>

    <!-- Panel de Filtros Avanzados -->
    <div class="filters-panel">
      <!-- Sección de Búsqueda -->
      <section class="search-section">
        <div class="search-box">
          <i class="fas fa-search"></i>
          <input
              type="text"
              v-model="searchQuery"
              placeholder="Buscar profesionales..."
              @input="applyFilters"
          />
          <button class="clear-btn" @click="clearSearch" v-if="searchQuery">
            <i class="fas fa-times"></i>
          </button>
        </div>
      </section>

      <!-- Filtros principales -->
      <section class="filters-grid">
        <!-- Rol/Profesión -->
        <div class="filter-group">
          <label class="filter-label">
            <i class="fas fa-briefcase"></i> Profesión
          </label>
          <v-select
              v-model="selectedRoles"
              :options="roles"
              multiple
              placeholder="Todos los roles"
              class="styled-select"
              label="name"
              :reduce="role => role.value"
              @input="applyFilters"
          />
        </div>

        <!-- Experiencia -->
        <div class="filter-group">
          <label class="filter-label">
            <i class="fas fa-chart-line"></i> Experiencia
          </label>
          <div class="experience-range">
            <input
                type="range"
                v-model="experienceFilter"
                min="0"
                max="50"
                step="1"
                @input="applyFilters"
            />
            <span class="experience-value">{{ experienceFilter }}+ años</span>
          </div>
        </div>

        <!-- Ubicación -->
        <div class="filter-group">
          <label class="filter-label">
            <i class="fas fa-map-marker-alt"></i> Ubicación
          </label>
          <v-select
              v-model="selectedLocations"
              :options="locations"
              multiple
              placeholder="Todas las ubicaciones"
              class="styled-select"
              label="name"
              :reduce="location => location.value"
              @input="applyFilters"
          />
        </div>

        <!-- Habilidades -->
        <div class="filter-group">
          <label class="filter-label">
            <i class="fas fa-tools"></i> Habilidades
          </label>
          <v-select
              v-model="selectedSkills"
              :options="skills"
              multiple
              placeholder="Todas las habilidades"
              class="styled-select"
              label="name"
              :reduce="skill => skill.value"
              @input="applyFilters"
          />
        </div>
      </section>

      <!-- Acciones -->
      <section class="filter-actions">
        <button class="reset-btn" @click="resetFilters">
          <i class="fas fa-undo"></i> Reiniciar Filtros
        </button>
        <span class="results-count">
          Mostrando {{ filteredProfessionals.length }} de {{ professionals.length }} profesionales
        </span>
      </section>
    </div>

    <!-- Listado de Profesionales -->
    <div class="professionals-list">
      <transition-group name="fade-staggered" tag="div" class="professionals-grid">
        <professional-card
            v-for="professional in paginatedProfessionals"
            :key="professional.id"
            :professional="professional"
            :dark-mode="darkMode"
            @toggle-favorite="toggleFavorite"
        />
      </transition-group>

      <!-- Paginación -->
      <div class="pagination-controls" v-if="filteredProfessionals.length > itemsPerPage">
        <button
            class="pagination-btn"
            :disabled="currentPage === 1"
            @click="prevPage"
        >
          <i class="fas fa-chevron-left"></i>
        </button>

        <span class="page-indicator">
          Página {{ currentPage }} de {{ totalPages }}
        </span>

        <button
            class="pagination-btn"
            :disabled="currentPage === totalPages"
            @click="nextPage"
        >
          <i class="fas fa-chevron-right"></i>
        </button>
      </div>
    </div>
  </div>

  <Footer :dark-mode="darkMode" />
</template>

<script>
import vSelect from 'vue-select'
import 'vue-select/dist/vue-select.css'
import ProfessionalCard from '@/components/cards/ProfessionalCard.vue'
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";

export default {
  name: 'ProfessionalsCatalog',
  components: {
    Footer,
    Header,
    vSelect,
    ProfessionalCard
  },
  data() {
    return {
      darkMode: false,
      professionals: [
        {
          id: '1',
          name: 'Alejandro González Iñárritu',
          role: 'Director',
          experience: 25,
          location: 'Ciudad de México',
          skills: ['Dirección', 'Guión', 'Producción'],
          bio: 'Director ganador del Oscar conocido por películas como Birdman y El Renacido.',
          profileImage: 'https://m.media-amazon.com/images/M/MV5BMTY2NTY5OTkzN15BMl5BanBnXkFtZTcwODQ0OTY0OQ@@._V1_.jpg',
          isFavorite: false,
          projects: ['Birdman', 'El Renacido', 'Babel'],
          contact: 'contacto@alejandrogi.com'
        },
        {
          id: '2',
          name: 'Guillermo del Toro',
          role: 'Director',
          experience: 30,
          location: 'Guadalajara',
          skills: ['Dirección', 'Guión', 'Diseño de Creaturas'],
          bio: 'Director, guionista, productor y novelista mexicano, conocido por su estilo visual único.',
          profileImage: 'https://m.media-amazon.com/images/M/MV5BMTU0OTc1MzI5OF5BMl5BanBnXkFtZTgwMzg5NjM5NjE@._V1_.jpg',
          isFavorite: true,
          projects: ['El Laberinto del Fauno', 'La Forma del Agua', 'Hellboy'],
          contact: 'info@guillermodeltoro.com'
        },
        {
          id: '3',
          name: 'Emmanuel Lubezki',
          role: 'Director de Fotografía',
          experience: 35,
          location: 'Ciudad de México',
          skills: ['Cinematografía', 'Iluminación', 'Fotografía'],
          bio: 'Conocido como "Chivo", es un director de fotografía ganador de múltiples premios Oscar.',
          profileImage: 'https://m.media-amazon.com/images/M/MV5BMTU0OTk2MDQ3Ml5BMl5BanBnXkFtZTgwMzM0NjUxNjE@._V1_.jpg',
          isFavorite: false,
          projects: ['El Renacido', 'Gravity', 'Birdman'],
          contact: 'elubezki@cinematography.com'
        },
        {
          id: '4',
          name: 'Salma Hayek',
          role: 'Actriz',
          experience: 30,
          location: 'Los Ángeles',
          skills: ['Actuación', 'Producción', 'Dirección'],
          bio: 'Actriz y productora mexicana-estadounidense, nominada al Oscar por su papel en Frida.',
          profileImage: 'https://m.media-amazon.com/images/M/MV5BMjI4NjM1NDkyN15BMl5BanBnXkFtZTgwODgyNTY1MjE@._V1_.jpg',
          isFavorite: false,
          projects: ['Frida', 'Eternals', 'Desde el Cielo'],
          contact: 'management@salma-hayek.com'
        },
        {
          id: '5',
          name: 'Gael García Bernal',
          role: 'Actor',
          experience: 25,
          location: 'Madrid',
          skills: ['Actuación', 'Dirección', 'Producción'],
          bio: 'Actor, director y productor mexicano conocido por su versatilidad en roles dramáticos y cómicos.',
          profileImage: 'https://m.media-amazon.com/images/M/MV5BMTUxNDY4MTMzM15BMl5BanBnXkFtZTcwMjg5NzM2Ng@@._V1_.jpg',
          isFavorite: false,
          projects: ['Amores Perros', 'El Crimen del Padre Amaro', 'Mozart in the Jungle'],
          contact: 'contact@gaelgarcia.com'
        },
        {
          id: '6',
          name: 'Patricia Riggen',
          role: 'Directora',
          experience: 20,
          location: 'Nueva York',
          skills: ['Dirección', 'Guión', 'Edición'],
          bio: 'Directora mexicana conocida por películas como La Misma Luna y Los 33.',
          profileImage: 'https://m.media-amazon.com/images/M/MV5BMTU5NjQwMzQ4Ml5BMl5BanBnXkFtZTgwNTk1OTk0ODE@._V1_.jpg',
          isFavorite: false,
          projects: ['La Misma Luna', 'Los 33', 'Girl in Progress'],
          contact: 'priggen@director.com'
        }
      ],
      searchQuery: '',
      selectedRoles: [],
      selectedLocations: [],
      selectedSkills: [],
      experienceFilter: 0,
      currentPage: 1,
      itemsPerPage: 12,
      roles: [
        { name: 'Director', value: 'Director' },
        { name: 'Actor', value: 'Actor' },
        { name: 'Actriz', value: 'Actriz' },
        { name: 'Productor', value: 'Productor' },
        { name: 'Director de Fotografía', value: 'Director de Fotografía' },
        { name: 'Guionista', value: 'Guionista' },
        { name: 'Editor', value: 'Editor' },
        { name: 'Diseñador de Producción', value: 'Diseñador de Producción' },
        { name: 'Compositor', value: 'Compositor' },
        { name: 'Técnico de Sonido', value: 'Técnico de Sonido' },
        { name: 'Vestuarista', value: 'Vestuarista' },
        { name: 'Maquillista', value: 'Maquillista' }
      ],
      locations: [
        { name: 'Ciudad de México', value: 'Ciudad de México' },
        { name: 'Guadalajara', value: 'Guadalajara' },
        { name: 'Monterrey', value: 'Monterrey' },
        { name: 'Los Ángeles', value: 'Los Ángeles' },
        { name: 'Nueva York', value: 'Nueva York' },
        { name: 'Madrid', value: 'Madrid' },
        { name: 'Barcelona', value: 'Barcelona' },
        { name: 'Buenos Aires', value: 'Buenos Aires' }
      ],
      skills: [
        { name: 'Dirección', value: 'Dirección' },
        { name: 'Guión', value: 'Guión' },
        { name: 'Producción', value: 'Producción' },
        { name: 'Cinematografía', value: 'Cinematografía' },
        { name: 'Edición', value: 'Edición' },
        { name: 'Actuación', value: 'Actuación' },
        { name: 'Diseño de Producción', value: 'Diseño de Producción' },
        { name: 'Efectos Visuales', value: 'Efectos Visuales' },
        { name: 'Sonido', value: 'Sonido' },
        { name: 'Iluminación', value: 'Iluminación' },
        { name: 'Coreografía', value: 'Coreografía' },
        { name: 'Doblaje', value: 'Doblaje' }
      ]
    }
  },
  computed: {
    filteredProfessionals() {
      return this.professionals.filter(professional => {
        // Filtro de búsqueda
        const matchesSearch = this.searchQuery === '' ||
            professional.name.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
            professional.role.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
            professional.skills.some(skill => skill.toLowerCase().includes(this.searchQuery.toLowerCase())) ||
            professional.bio.toLowerCase().includes(this.searchQuery.toLowerCase())

        // Filtro de rol
        const matchesRole = this.selectedRoles.length === 0 ||
            this.selectedRoles.some(role => professional.role.includes(role))

        // Filtro de ubicación
        const matchesLocation = this.selectedLocations.length === 0 ||
            this.selectedLocations.some(location => professional.location.includes(location))

        // Filtro de habilidades
        const matchesSkills = this.selectedSkills.length === 0 ||
            this.selectedSkills.every(skill => professional.skills.includes(skill))

        // Filtro de experiencia
        const matchesExperience = professional.experience >= this.experienceFilter

        return matchesSearch && matchesRole && matchesLocation && matchesSkills && matchesExperience
      })
    },

    paginatedProfessionals() {
      const start = (this.currentPage - 1) * this.itemsPerPage
      const end = start + this.itemsPerPage
      return this.filteredProfessionals.slice(start, end)
    },

    totalPages() {
      return Math.ceil(this.filteredProfessionals.length / this.itemsPerPage)
    }
  },
  methods: {
    toggleDarkMode() {
      this.darkMode = !this.darkMode
      localStorage.setItem('darkMode', this.darkMode)
    },

    applyFilters() {
      this.currentPage = 1
    },

    resetFilters() {
      this.searchQuery = ''
      this.selectedRoles = []
      this.selectedLocations = []
      this.selectedSkills = []
      this.experienceFilter = 0
      this.currentPage = 1
    },

    clearSearch() {
      this.searchQuery = ''
      this.applyFilters()
    },

    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--
        window.scrollTo({ top: 0, behavior: 'smooth' })
      }
    },

    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++
        window.scrollTo({ top: 0, behavior: 'smooth' })
      }
    },

    toggleFavorite(professionalId) {
      const professional = this.professionals.find(p => p.id === professionalId)
      if (professional) {
        professional.isFavorite = !professional.isFavorite
      }
    }
  },
  created() {
    // Verificar preferencia de modo oscuro
    const savedMode = localStorage.getItem('darkMode')
    if (savedMode) {
      this.darkMode = savedMode === 'true'
    }
  }
}
</script>

<style scoped>
:root {
  --color-primary: #6c5ce7;
  --color-primary-light: #a29bfe;
  --color-primary-dark: #5649c0;
  --color-secondary: #f8f9fa;
  --color-text: #2d3436;
  --color-text-light: #f8f9fa;
  --color-bg: #ffffff;
  --color-bg-dark: #121212;
  --color-card: #ffffff;
  --color-card-dark: #1e1e1e;
  --color-filter-panel: #f1f3f5;
  --color-filter-panel-dark: #2a2a2a;
  --color-border: #e0e0e0;
  --color-border-dark: #333333;
  --transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.dark-mode {
  --color-secondary: #2a2a3a;
  --color-text: #e9ecef;
  --color-bg: #121212;
  --color-card: #1e1e1e;
  --color-filter-panel: #2a2a2a;
  --color-border: #333333;
}

.professionals-container {
  position: relative;
  padding: 2rem 5%;
  color: var(--color-text);
  transition: var(--transition);
  min-height: 100vh;
  background-image: url("https://images.unsplash.com/photo-1517604931442-7e0c8ed2963c?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
  background-attachment: fixed;
  background-size: cover;
  background-position: center;
  overflow: hidden;
  box-shadow: inset 0 0 50px rgba(0, 0, 0, 0.5);
}

.professionals-container::before {
  content: "";
  position: absolute;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.55);
  z-index: 0;
}

.professionals-container > * {
  position: relative;
  z-index: 1;
}

/* Encabezado */
.header-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.main-title {
  font-size: 2.2rem;
  font-weight: 700;
  color: #a1a1a1;
  display: flex;
  align-items: center;
  gap: 0.8rem;
}

/* Panel de Filtros - Versión Mejorada */
.filters-panel {
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  border: 1px solid var(--color-border);
  transition: var(--transition);
}

.dark-mode .filters-panel {
  background-color: rgba(30, 30, 30, 0.9);
  border-color: var(--color-border-dark);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

/* Cuadro de Búsqueda Mejorado */
.search-section {
  margin-bottom: 1.5rem;
  position: relative;
}

.search-box {
  position: relative;
  max-width: 600px;
  margin: 0 auto;
  border-radius: 50px;
  overflow: hidden;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.dark-mode .search-box {
  background: rgba(0, 0, 0, 0.2);
  border-color: rgba(255, 255, 255, 0.05);
}

.search-box i {
  position: absolute;
  left: 1.5rem;
  top: 50%;
  transform: translateY(-50%);
  color: var(--color-primary);
  font-size: 1.1rem;
  z-index: 2;
}

.search-box input {
  width: 100%;
  padding: 0.9rem 1.5rem 0.9rem 3.5rem;
  border: none;
  background: transparent;
  color: var(--color-text);
  font-size: 1rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.dark-mode .search-box input {
  color: white;
}

.search-box input::placeholder {
  color: rgba(255, 255, 255, 0.7);
  font-weight: 400;
}

.search-box input:focus {
  outline: none;
  background: rgba(255, 255, 255, 0.15);
}

.dark-mode .search-box input:focus {
  background: rgba(0, 0, 0, 0.3);
}

.clear-btn {
  position: absolute;
  right: 1.5rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.7);
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 2;
}

.clear-btn:hover {
  color: var(--color-primary);
}

/* Filtros Grid */
.filters-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 1.5rem;
}

.filter-group {
  margin-bottom: 1rem;
}

.filter-label {
  display: block;
  margin-bottom: 0.8rem;
  font-weight: 600;
  color: var(--color-text);
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.95rem;
}

/* Select Estilizado */
.styled-select {
  background-color: var(--color-card);
  border-radius: 8px;
  border: 1px solid var(--color-border);
  transition: var(--transition);
}

.dark-mode .styled-select {
  background-color: #2a2a2a;
  border-color: #444;
}

.styled-select >>> .vs__dropdown-toggle {
  border: none;
  padding: 0.7rem 1rem;
  background: transparent;
}

.styled-select >>> .vs__selected {
  background-color: var(--color-primary);
  color: white;
  border: none;
  border-radius: 4px;
  padding: 0.3rem 0.6rem;
}

.styled-select >>> .vs__search {
  color: var(--color-text);
  padding: 0.3rem 0.6rem;
}

.styled-select >>> .vs__dropdown-menu {
  background-color: var(--color-card);
  border: 1px solid var(--color-border);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.dark-mode .styled-select >>> .vs__dropdown-menu {
  background-color: #2a2a2a;
  border-color: #444;
}

/* Filtro de Experiencia */
.experience-range {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0.5rem 0;
}

.experience-range input[type="range"] {
  flex: 1;
  -webkit-appearance: none;
  height: 6px;
  background: var(--color-border);
  border-radius: 3px;
  outline: none;
}

.experience-range input[type="range"]::-webkit-slider-thumb {
  -webkit-appearance: none;
  width: 18px;
  height: 18px;
  background: var(--color-primary);
  border-radius: 50%;
  cursor: pointer;
  transition: var(--transition);
}

.experience-value {
  font-weight: 600;
  color: var(--color-primary);
  min-width: 80px;
  text-align: center;
}

/* Acciones de Filtro */
.filter-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 1.5rem;
  padding-top: 1.5rem;
  border-top: 1px solid var(--color-border);
}

.reset-btn {
  background: none;
  border: none;
  color: var(--color-primary);
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: var(--transition);
  padding: 0.5rem 1rem;
  border-radius: 6px;
}

.reset-btn:hover {
  background-color: rgba(108, 92, 231, 0.1);
}

.results-count {
  font-size: 0.9rem;
  color: var(--color-text);
  opacity: 0.8;
}

/* Listado de Profesionales */
.professionals-list {
  margin-top: 2rem;
}

.professionals-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

/* Paginación */
.pagination-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 1.5rem;
  margin-top: 2rem;
}

.pagination-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background-color: var(--color-primary);
  color: white;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: var(--transition);
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination-btn:hover:not(:disabled) {
  background-color: var(--color-primary-dark);
  transform: scale(1.1);
}

.page-indicator {
  font-weight: 600;
  color: white;
}

/* Transiciones */
.fade-staggered-enter-active {
  transition: all 0.5s ease;
}
.fade-staggered-leave-active {
  transition: all 0.3s ease;
}
.fade-staggered-enter-from,
.fade-staggered-leave-to {
  opacity: 0;
  transform: translateY(20px);
}

/* Responsive */
@media (max-width: 1024px) {
  .professionals-container {
    padding: 2rem;
  }
}

@media (max-width: 768px) {
  .header-section {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }

  .filters-grid {
    grid-template-columns: 1fr;
  }

  .filter-actions {
    flex-direction: column;
    gap: 1rem;
    align-items: flex-start;
  }
}

@media (max-width: 480px) {
  .professionals-container {
    padding: 1.5rem;
  }

  .main-title {
    font-size: 1.8rem;
  }

  .professionals-grid {
    grid-template-columns: 1fr;
  }
}
</style>