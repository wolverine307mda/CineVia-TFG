<template>
  <Header :dark-mode="darkMode" @toggle-dark-mode="toggleDarkMode"/>

  <div class="movies-container" :class="{ 'dark-mode': darkMode }">
    <!-- Encabezado con título y modo oscuro -->
    <div class="header-section">
      <h1 class="main-title">
        <i class="fas fa-film"></i> Catálogo de Películas
      </h1>
    </div>

    <!-- Panel de Filtros Avanzados -->
    <div class="advanced-filters">
      <!-- Encabezado del panel -->
      <div class="filters-header">
        <h2 class="filters-title">
          <i class="fas fa-sliders-h"></i> Filtros Avanzados
        </h2>
        <button class="toggle-filters" @click="toggleFilters">
          {{ filtersVisible ? 'Ocultar filtros' : 'Mostrar filtros' }}
          <i :class="filtersVisible ? 'fas fa-chevron-up' : 'fas fa-chevron-down'"></i>
        </button>
      </div>

      <!-- Contenido colapsable -->
      <transition name="slide-fade">
        <div class="filters-content" v-show="filtersVisible">
          <!-- Búsqueda rápida -->
          <div class="search-container">
            <div class="search-box">
              <i class="fas fa-search"></i>
              <input
                  type="text"
                  v-model="searchQuery"
                  placeholder="Buscar por título, director o actor..."
                  @input="applyFilters"
              />
              <button class="clear-btn" @click="clearSearch" v-if="searchQuery">
                <i class="fas fa-times"></i>
              </button>
            </div>
          </div>

          <!-- Filtros en acordeón -->
          <div class="filter-accordion">
            <!-- Filtro por género -->
            <div class="accordion-item" :class="{ 'expanded': expandedSection === 'genre' }">
              <div class="accordion-header" @click="toggleSection('genre')">
                <i class="fas fa-tags"></i>
                <span>Géneros</span>
                <i class="accordion-icon fas fa-chevron-down"></i>
              </div>
              <transition name="expand">
                <div class="accordion-content" v-show="expandedSection === 'genre'">
                  <div class="genre-tags">
                    <button
                        v-for="genre in genres"
                        :key="genre.value"
                        :class="{ 'active': selectedGenres.includes(genre.value) }"
                        @click="toggleGenre(genre.value)"
                    >
                      {{ genre.name }}
                    </button>
                  </div>
                </div>
              </transition>
            </div>

            <!-- Filtro por año -->
            <div class="accordion-item" :class="{ 'expanded': expandedSection === 'year' }">
              <div class="accordion-header" @click="toggleSection('year')">
                <i class="fas fa-calendar-alt"></i>
                <span>Año de lanzamiento</span>
                <i class="accordion-icon fas fa-chevron-down"></i>
              </div>
              <transition name="expand">
                <div class="accordion-content" v-show="expandedSection === 'year'">
                  <div class="year-range">
                    <date-picker
                        v-model="yearRange"
                        type="year"
                        range
                        placeholder="Seleccionar rango"
                        :clearable="false"
                        @change="applyFilters"
                        class="styled-datepicker"
                    />
                    <div class="quick-years">
                      <button @click="setYearRange(2020, 2023)">Últimos 3 años</button>
                      <button @click="setYearRange(2010, 2019)">2010s</button>
                      <button @click="setYearRange(2000, 2009)">2000s</button>
                    </div>
                  </div>
                </div>
              </transition>
            </div>

            <!-- Filtro por valoración -->
            <div class="accordion-item" :class="{ 'expanded': expandedSection === 'rating' }">
              <div class="accordion-header" @click="toggleSection('rating')">
                <i class="fas fa-star"></i>
                <span>Valoración</span>
                <i class="accordion-icon fas fa-chevron-down"></i>
              </div>
              <transition name="expand">
                <div class="accordion-content" v-show="expandedSection === 'rating'">
                  <div class="rating-filter">
                    <div class="rating-stars">
                      <i
                          v-for="n in 5"
                          :key="n"
                          class="fas fa-star"
                          :class="{ 'active': ratingFilter >= (n * 2) }"
                          @click="ratingFilter = (n * 2)"
                      ></i>
                    </div>
                    <div class="rating-slider">
                      <input
                          type="range"
                          v-model="ratingFilter"
                          min="0"
                          max="10"
                          step="0.5"
                          @input="applyFilters"
                      />
                      <div class="rating-labels">
                        <span>0</span>
                        <span>5</span>
                        <span>10</span>
                      </div>
                    </div>
                    <div class="selected-rating">
                      Mínimo: <strong>{{ ratingFilter }}+</strong>
                    </div>
                  </div>
                </div>
              </transition>
            </div>

            <!-- Filtro por duración -->
            <div class="accordion-item" :class="{ 'expanded': expandedSection === 'duration' }">
              <div class="accordion-header" @click="toggleSection('duration')">
                <i class="fas fa-clock"></i>
                <span>Duración</span>
                <i class="accordion-icon fas fa-chevron-down"></i>
              </div>
              <transition name="expand">
                <div class="accordion-content" v-show="expandedSection === 'duration'">
                  <div class="duration-options">
                    <div
                        v-for="duration in durationOptions"
                        :key="duration.value"
                        class="duration-option"
                        :class="{ 'active': selectedDuration === duration.value }"
                        @click="setDuration(duration.value)"
                    >
                      <div class="duration-icon">
                        <i :class="duration.icon"></i>
                      </div>
                      <div class="duration-label">
                        <span>{{ duration.label }}</span>
                        <small>{{ duration.description }}</small>
                      </div>
                    </div>
                  </div>
                </div>
              </transition>
            </div>
          </div>

          <!-- Acciones del filtro -->
          <div class="filter-actions">
            <button class="apply-btn" @click="applyFilters">
              <i class="fas fa-check"></i> Aplicar Filtros
            </button>
            <button class="reset-btn" @click="resetFilters">
              <i class="fas fa-undo"></i> Limpiar Todo
            </button>
            <div class="results-count">
              {{ filteredMovies.length }} resultados
            </div>
          </div>
        </div>
      </transition>
    </div>

    <!-- Listado de Películas -->
    <div class="movies-list">
      <transition-group name="fade-staggered" tag="div" class="movies-grid">
        <movie-card
            v-for="movie in paginatedMovies"
            :key="movie.id"
            :movie="movie"
            :dark-mode="darkMode"
            @toggle-favorite="toggleFavorite"
        />
      </transition-group>

      <!-- Paginación -->
      <div class="pagination-controls" v-if="filteredMovies.length > itemsPerPage">
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

import Datepicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'

import MovieCard from '@/components/cards/MovieCard.vue'
import Header from "@/components/Header.vue";
import Footer from "@/components/Footer.vue";

export default {
  name: 'MoviesCatalog',
  components: {
    Footer,
    Header,
    vSelect,
    Datepicker,
    MovieCard
  },
  data() {
    return {
      darkMode: false,
      movies: [
        {
          id: '1',
          title: 'El Padrino',
          year: 1972,
          duration: 175,
          rating: 9.2,
          genres: ['Drama', 'Crimen'],
          director: 'Francis Ford Coppola',
          actors: ['Marlon Brando', 'Al Pacino', 'James Caan'],
          plot: 'El patriarca de una familia mafiosa transfiere el control de su imperio clandestino a su hijo reacio.',
          poster: 'https://image.tmdb.org/t/p/w500/r4gnMXoY1efvaolNDjn3nj4046S.jpg',
          isFavorite: false
        },
        {
          id: '2',
          title: 'El Caballero Oscuro',
          year: 2008,
          duration: 152,
          rating: 9.0,
          genres: ['Acción', 'Crimen', 'Drama'],
          director: 'Christopher Nolan',
          actors: ['Christian Bale', 'Heath Ledger', 'Aaron Eckhart'],
          plot: 'Batman tiene que aceptar la ayuda de un nuevo fiscal para combatir a un criminal anarquista conocido como el Joker.',
          poster: 'https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg',
          isFavorite: true
        },
        {
          "id": "3",
          "title": "Pulp Fiction",
          "year": 1994,
          "duration": 154,
          "rating": 8.9,
          "genres": ["Drama", "Crimen"],
          "director": "Quentin Tarantino",
          "actors": ["John Travolta", "Uma Thurman", "Samuel L. Jackson"],
          "plot": "Las vidas de dos sicarios, un boxeador, la esposa de un gánster y dos bandidos se entrelazan en cuatro historias de violencia y redención.",
          "poster": "https://m.media-amazon.com/images/I/81UTs3sC5hL._AC_UF894,1000_QL80_.jpg",
          "isFavorite": false
        },
        {
          "id": "4",
          "title": "Forrest Gump",
          "year": 1994,
          "duration": 142,
          "rating": 8.8,
          "genres": ["Drama", "Romance"],
          "director": "Robert Zemeckis",
          "actors": ["Tom Hanks", "Robin Wright", "Gary Sinise"],
          "plot": "La presidencia de Kennedy y Johnson, Vietnam, Watergate y otros eventos históricos se desarrollan desde la perspectiva de un hombre de Alabama con un coeficiente intelectual de 75.",
          "poster": "https://media.es.wired.com/photos/6679bb407faf643236b6024c/2:3/w_576,h_864,c_limit/Forrest%20Gump.jpg",
          "isFavorite": false
        },
        {
          "id": "5",
          "title": "Inception",
          "year": 2010,
          "duration": 148,
          "rating": 8.8,
          "genres": ["Acción", "Ciencia Ficción"],
          "director": "Christopher Nolan",
          "actors": ["Leonardo DiCaprio", "Joseph Gordon-Levitt", "Ellen Page"],
          "plot": "Un ladrón que roba secretos corporativos a través del uso de la tecnología de compartir sueños es encargado con la tarea inversa de implantar una idea en la mente de un CEO.",
          "poster": "https://m.media-amazon.com/images/M/MV5BMjExMjkwNTQ0Nl5BMl5BanBnXkFtZTcwNTY0OTk1Mw@@._V1_.jpg",
          "isFavorite": false
        },
        {
          "id": "6",
          "title": "The Matrix",
          "year": 1999,
          "duration": 136,
          "rating": 8.7,
          "genres": ["Acción", "Ciencia Ficción"],
          "director": "Lana Wachowski, Lilly Wachowski",
          "actors": ["Keanu Reeves", "Laurence Fishburne", "Carrie-Anne Moss"],
          "plot": "Un hacker aprende de misteriosos rebeldes sobre la verdadera naturaleza de su realidad y su papel en la guerra contra sus controladores.",
          "poster": "https://m.media-amazon.com/images/M/MV5BNGE1YzI4NzMtZTUxNi00Y2I5LTg2MmQtODE0NThmYTFmMDk0XkEyXkFqcGc@._V1_.jpg",
          "isFavorite": false
        },
        {
          "id": "7",
          "title": "Titanic",
          "year": 1997,
          "duration": 195,
          "rating": 7.8,
          "genres": ["Drama", "Romance"],
          "director": "James Cameron",
          "actors": ["Leonardo DiCaprio", "Kate Winslet", "Billy Zane"],
          "plot": "Una aristócrata de 17 años se enamora de un artista amable pero pobre a bordo del lujoso e infortunado R.M.S. Titanic.",
          "poster": "https://pics.filmaffinity.com/Titanic-321994924-large.jpg",
          "isFavorite": false
        },
        {
          "id": "8",
          "title": "Gladiator",
          "year": 2000,
          "duration": 155,
          "rating": 8.5,
          "genres": ["Acción", "Aventura", "Drama"],
          "director": "Ridley Scott",
          "actors": ["Russell Crowe", "Joaquin Phoenix", "Connie Nielsen"],
          "plot": "Un ex general romano busca venganza contra el corrupto emperador que asesinó a su familia y lo envió a la esclavitud.",
          "poster": "https://m.media-amazon.com/images/I/51GA6V6VE1L._AC_UF894,1000_QL80_.jpg",
          "isFavorite": false
        },
        {
          "id": "9",
          "title": "The Shawshank Redemption",
          "year": 1994,
          "duration": 142,
          "rating": 9.3,
          "genres": ["Drama"],
          "director": "Frank Darabont",
          "actors": ["Tim Robbins", "Morgan Freeman", "Bob Gunton"],
          "plot": "Dos hombres encarcelados se vinculan durante varios años, encontrando consuelo y eventual redención a través de actos de decencia común.",
          "poster": "https://pics.filmaffinity.com/Cadena_perpetua-576140557-large.jpg",
          "isFavorite": false
        },
        {
          "id": "10",
          "title": "The Godfather Part II",
          "year": 1974,
          "duration": 202,
          "rating": 9.0,
          "genres": ["Drama", "Crimen"],
          "director": "Francis Ford Coppola",
          "actors": ["Al Pacino", "Robert De Niro", "Robert Duvall"],
          "plot": "La temprana vida y carrera de Vito Corleone en 1920 es representada mientras su hijo, Michael, expande y refuerza su control sobre el sindicato del crimen familiar.",
          "poster": "https://s3.amazonaws.com/nightjarprod/content/uploads/sites/192/2022/10/17140449/hek3koDUyRQk7FIhPXsa6mT2Zc3.jpg",
          "isFavorite": false
        },
        {
          "id": "11",
          "title": "The Lord of the Rings: The Return of the King",
          "year": 2003,
          "duration": 201,
          "rating": 8.9,
          "genres": ["Aventura", "Drama", "Fantasía"],
          "director": "Peter Jackson",
          "actors": ["Elijah Wood", "Viggo Mortensen", "Ian McKellen"],
          "plot": "Gandalf y Aragorn lideran el Mundo de los Hombres contra el ejército de Sauron para distraer su atención de Frodo y Sam mientras se acercan al Monte del Destino con el Anillo Único.",
          "poster": "https://m.media-amazon.com/images/M/MV5BMTZkMjBjNWMtZGI5OC00MGU0LTk4ZTItODg2NWM3NTVmNWQ4XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
          "isFavorite": false
        },
        {
          id: '13',
          title: 'Interstellar',
          year: 2014,
          duration: 169,
          rating: 8.6,
          genres: ['Aventura', 'Drama', 'Ciencia Ficción'],
          director: 'Christopher Nolan',
          actors: ['Matthew McConaughey', 'Anne Hathaway', 'Jessica Chastain'],
          plot: 'Un equipo de exploradores viaja a través de un agujero de gusano en el espacio en un intento por asegurar la supervivencia de la humanidad.',
          poster: 'https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg',
          isFavorite: false
        },
        {
          id: '14',
          title: 'Whiplash',
          year: 2014,
          duration: 106,
          rating: 8.5,
          genres: ['Drama', 'Música'],
          director: 'Damien Chazelle',
          actors: ['Miles Teller', 'J.K. Simmons', 'Paul Reiser'],
          plot: 'Un joven baterista ambicioso se inscribe en un conservatorio de música donde su instructor abusivo no se detendrá ante nada para hacer que un estudiante alcance la grandeza.',
          poster: 'https://image.tmdb.org/t/p/w500/oPxnRhyAIzJKGUEdSiwTJQBa3NM.jpg',
          isFavorite: false
        },
        {
          id: '15',
          title: 'La La Land',
          year: 2016,
          duration: 128,
          rating: 8.0,
          genres: ['Comedia', 'Drama', 'Música', 'Romance'],
          director: 'Damien Chazelle',
          actors: ['Ryan Gosling', 'Emma Stone', 'John Legend'],
          plot: 'Mientras navegan por sus carreras en Los Ángeles, un pianista y una actriz se enamoran mientras intentan reconciliar sus aspiraciones para el futuro.',
          poster: 'https://image.tmdb.org/t/p/w500/uDO8zWDhfWwoFdKS4fzkUJt0Rf0.jpg',
          isFavorite: false
        },
        {
          id: '16',
          title: 'Django Desencadenado',
          year: 2012,
          duration: 165,
          rating: 8.4,
          genres: ['Drama', 'Western'],
          director: 'Quentin Tarantino',
          actors: ['Jamie Foxx', 'Christoph Waltz', 'Leonardo DiCaprio'],
          plot: 'Con la ayuda de un cazarrecompensas alemán, un esclavo liberado se embarca en una misión para rescatar a su esposa de un brutal propietario de una plantación en Mississippi.',
          poster: 'https://image.tmdb.org/t/p/w500/7oWY8VDWW7thTzWh3OKYRkWUlD5.jpg',
          isFavorite: false
        }
      ],
      searchQuery: '',
      selectedGenres: [],
      yearRange: null,
      ratingFilter: 0,
      selectedDuration: '',
      currentPage: 1,
      itemsPerPage: 12,
      genres: [
        { name: 'Acción', value: 'Acción' },
        { name: 'Aventura', value: 'Aventura' },
        { name: 'Animación', value: 'Animación' },
        { name: 'Comedia', value: 'Comedia' },
        { name: 'Crimen', value: 'Crimen' },
        { name: 'Documental', value: 'Documental' },
        { name: 'Drama', value: 'Drama' },
        { name: 'Fantasía', value: 'Fantasía' },
        { name: 'Horror', value: 'Horror' },
        { name: 'Misterio', value: 'Misterio' },
        { name: 'Romance', value: 'Romance' },
        { name: 'Ciencia Ficción', value: 'Ciencia Ficción' },
        { name: 'Thriller', value: 'Thriller' }
      ],
      durationOptions: [
        {
          label: 'Corta',
          value: 'short',
          description: '(Menos de 90 min)',
          icon: 'fas fa-hourglass-start'
        },
        {
          label: 'Media',
          value: 'medium',
          description: '(90-150 min)',
          icon: 'fas fa-hourglass-half'
        },
        {
          label: 'Larga',
          value: 'long',
          description: '(Más de 150 min)',
          icon: 'fas fa-hourglass-end'
        },
        {
          label: 'Cualquiera',
          value: '',
          description: '(Sin filtro)',
          icon: 'fas fa-hourglass'
        }
      ]
    }
  },
  computed: {
    filteredMovies() {
      return this.movies.filter(movie => {
        // Filtro de búsqueda
        const matchesSearch = this.searchQuery === '' ||
            movie.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
            movie.director.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
            movie.actors.some(actor => actor.toLowerCase().includes(this.searchQuery.toLowerCase())) ||
            movie.plot.toLowerCase().includes(this.searchQuery.toLowerCase())

        // Filtro de género
        const matchesGenre = this.selectedGenres.length === 0 ||
            this.selectedGenres.some(genre => movie.genres.includes(genre))

        // Filtro de año
        let matchesYear = true
        if (this.yearRange && this.yearRange[0] && this.yearRange[1]) {
          matchesYear = movie.year >= this.yearRange[0].getFullYear() &&
              movie.year <= this.yearRange[1].getFullYear()
        }

        // Filtro de valoración
        const matchesRating = movie.rating >= this.ratingFilter

        // Filtro de duración
        let matchesDuration = true
        if (this.selectedDuration === 'short') {
          matchesDuration = movie.duration < 90
        } else if (this.selectedDuration === 'medium') {
          matchesDuration = movie.duration >= 90 && movie.duration <= 150
        } else if (this.selectedDuration === 'long') {
          matchesDuration = movie.duration > 150
        }

        return matchesSearch && matchesGenre && matchesYear && matchesRating && matchesDuration
      })
    },

    paginatedMovies() {
      const start = (this.currentPage - 1) * this.itemsPerPage
      const end = start + this.itemsPerPage
      return this.filteredMovies.slice(start, end)
    },

    totalPages() {
      return Math.ceil(this.filteredMovies.length / this.itemsPerPage)
    }
  },
  methods: {
    toggleDarkMode() {
      this.darkMode = !this.darkMode
      localStorage.setItem('darkMode', this.darkMode)
    },
    toggleFilters() {
      this.filtersVisible = !this.filtersVisible
    },
    toggleSection(section) {
      this.expandedSection = this.expandedSection === section ? null : section
    },
    toggleGenre(genre) {
      if (this.selectedGenres.includes(genre)) {
        this.selectedGenres = this.selectedGenres.filter(g => g !== genre)
      } else {
        this.selectedGenres = [...this.selectedGenres, genre]
      }
      this.applyFilters()
    },
    setYearRange(start, end) {
      this.yearRange = [new Date(start, 0, 1), new Date(end, 11, 31)]
      this.applyFilters()
    },
    setDuration(duration) {
      this.selectedDuration = duration
      this.applyFilters()
    },
    applyFilters() {
      this.$emit('filters-changed', {
        searchQuery: this.searchQuery,
        selectedGenres: this.selectedGenres,
        yearRange: this.yearRange,
        ratingFilter: this.ratingFilter,
        selectedDuration: this.selectedDuration
      })
    },
    resetFilters() {
      this.searchQuery = ''
      this.selectedGenres = []
      this.yearRange = null
      this.ratingFilter = 0
      this.selectedDuration = ''
      this.applyFilters()
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

    toggleFavorite(movieId) {
      const movie = this.movies.find(m => m.id === movieId)
      if (movie) {
        movie.isFavorite = !movie.isFavorite
      }
    },

    generateSampleMovies() {
      const sampleMovies = [
        // Más películas de ejemplo...
      ]

      if (this.movies.length < 100) {
        this.movies = [...this.movies, ...sampleMovies.slice(0, 100 - this.movies.length)]
      }
    }
  },
  created() {
    // Verificar preferencia de modo oscuro
    const savedMode = localStorage.getItem('darkMode')
    if (savedMode) {
      this.darkMode = savedMode === 'true'
    }

    this.generateSampleMovies()
  }
}
</script>

<style scoped>
/* Variables de color para ambos modos */
:root {
  --filter-bg: rgba(255, 255, 255, 0.95);
  --filter-border: rgba(0, 0, 0, 0.1);
  --filter-text: #333;
  --filter-header-bg: #f8f9fa;
  --filter-section-bg: #ffffff;
  --filter-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  --filter-active: #6c5ce7;
  --filter-active-light: rgba(108, 92, 231, 0.1);
  --filter-hover: #f1f3f5;
  --filter-divider: #e9ecef;
}
.dark-mode {
  --filter-bg: rgba(30, 30, 30, 0.98);
  --filter-border: rgba(255, 255, 255, 0.1);
  --filter-text: #e9ecef;
  --filter-header-bg: #2a2a2a;
  --filter-section-bg: #1e1e1e;
  --filter-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  --filter-active: #a78bfa;
  --filter-active-light: rgba(167, 139, 250, 0.15);
  --filter-hover: #2a2a2a;
  --filter-divider: #333;
}

.movies-container {
  position: relative; /* necesario para ::before */
  padding: 2rem 5%;
  color: var(--color-text);
  transition: var(--transition);
  min-height: 100vh;
  background-image: url("https://images.unsplash.com/photo-1482686115713-0fbcaced6e28?q=80&w=2067&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
  background-attachment: fixed;
  background-size: cover;
  background-position: center;
  overflow: hidden;
  box-shadow: inset 0 0 50px rgba(0, 0, 0, 0.5);
}

.movies-container::before {
  content: "";
  position: absolute;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.55);
  z-index: 0;
}

.movies-container > * {
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

/* Listado de Películas */
.movies-list {
  margin-top: 2rem;
}

.movies-grid {
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
  .movies-container {
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
  .movies-container {
    padding: 1.5rem;
  }

  .main-title {
    font-size: 1.8rem;
  }

  .movies-grid {
    grid-template-columns: 1fr;
  }

  .duration-buttons button {
    min-width: auto;
  }
}

/* Panel principal */
.advanced-filters {
  background: var(--filter-bg);
  border-radius: 12px;
  border: 1px solid var(--filter-border);
  box-shadow: var(--filter-shadow);
  overflow: hidden;
  margin-bottom: 2rem;
  transition: all 0.3s ease;
}

/* Encabezado */
.filters-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 1.5rem;
  background: var(--filter-header-bg);
  border-bottom: 1px solid var(--filter-divider);
}

.filters-title {
  font-size: 1.25rem;
  font-weight: 600;
  color: var(--filter-text);
  margin: 0;
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.filters-title i {
  color: var(--filter-active);
}

.toggle-filters {
  background: none;
  border: none;
  color: var(--filter-active);
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 0.75rem;
  border-radius: 6px;
  transition: all 0.2s ease;
}

.toggle-filters:hover {
  background: var(--filter-active-light);
}

/* Contenido */
.filters-content {
  padding: 1.5rem;
}

/* Búsqueda */
.search-container {
  margin-bottom: 1.5rem;
}

.search-box {
  position: relative;
  display: flex;
  align-items: center;
  background: var(--filter-section-bg);
  border-radius: 8px;
  border: 1px solid var(--filter-border);
  overflow: hidden;
  transition: all 0.3s ease;
}

.search-box:focus-within {
  border-color: var(--filter-active);
  box-shadow: 0 0 0 2px var(--filter-active-light);
}

.search-box i {
  position: absolute;
  left: 1rem;
  color: var(--filter-active);
  font-size: 1rem;
}

.search-box input {
  flex: 1;
  padding: 0.875rem 1rem 0.875rem 2.5rem;
  border: none;
  background: transparent;
  color: var(--filter-text);
  font-size: 0.95rem;
}

.search-box input::placeholder {
  color: rgba(var(--filter-text), 0.6);
}

.clear-btn {
  background: none;
  border: none;
  color: rgba(var(--filter-text), 0.5);
  padding: 0 1rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.clear-btn:hover {
  color: var(--filter-active);
}

/* Acordeón de filtros */
.filter-accordion {
  margin-bottom: 1rem;
}

.accordion-item {
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 0.5rem;
  border: 1px solid var(--filter-border);
  background: var(--filter-section-bg);
}

.accordion-item.expanded {
  border-color: var(--filter-active);
}

.accordion-header {
  padding: 1rem 1.25rem;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  cursor: pointer;
  user-select: none;
  font-weight: 500;
  color: var(--filter-text);
  transition: all 0.2s ease;
}

.accordion-header:hover {
  background: var(--filter-hover);
}

.accordion-header i:first-child {
  color: var(--filter-active);
  width: 1.25rem;
  text-align: center;
}

.accordion-icon {
  margin-left: auto;
  transition: transform 0.3s ease;
}

.accordion-item.expanded .accordion-icon {
  transform: rotate(180deg);
}

.accordion-content {
  padding: 0 1.25rem 1.25rem;
  border-top: 1px solid var(--filter-divider);
}

/* Estilos específicos para cada tipo de filtro */
.genre-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.genre-tags button {
  padding: 0.5rem 0.75rem;
  border-radius: 20px;
  border: 1px solid var(--filter-border);
  background: var(--filter-section-bg);
  color: var(--filter-text);
  font-size: 0.85rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.genre-tags button:hover {
  border-color: var(--filter-active);
}

.genre-tags button.active {
  background: var(--filter-active);
  color: white;
  border-color: var(--filter-active);
}

.year-range {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.quick-years {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.quick-years button {
  padding: 0.5rem 0.75rem;
  border-radius: 6px;
  border: 1px solid var(--filter-border);
  background: var(--filter-section-bg);
  color: var(--filter-text);
  font-size: 0.85rem;
  cursor: pointer;
  transition: all 0.2s ease;
}

.quick-years button:hover {
  border-color: var(--filter-active);
}

.rating-filter {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.rating-stars {
  display: flex;
  gap: 0.5rem;
  justify-content: center;
}

.rating-stars i {
  font-size: 1.5rem;
  color: var(--filter-divider);
  cursor: pointer;
  transition: all 0.2s ease;
}

.rating-stars i.active {
  color: #fbbf24;
}

.rating-slider {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.rating-slider input[type="range"] {
  width: 100%;
  height: 6px;
  -webkit-appearance: none;
  background: var(--filter-divider);
  border-radius: 3px;
  outline: none;
}

.rating-slider input[type="range"]::-webkit-slider-thumb {
  -webkit-appearance: none;
  width: 18px;
  height: 18px;
  background: var(--filter-active);
  border-radius: 50%;
  cursor: pointer;
}

.rating-labels {
  display: flex;
  justify-content: space-between;
  font-size: 0.8rem;
  color: rgba(var(--filter-text), 0.7);
}

.selected-rating {
  text-align: center;
  font-size: 0.9rem;
  color: var(--filter-text);
}

.selected-rating strong {
  color: var(--filter-active);
}

.duration-options {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 0.75rem;
}

.duration-option {
  padding: 0.75rem;
  border-radius: 8px;
  border: 1px solid var(--filter-border);
  background: var(--filter-section-bg);
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.duration-option:hover {
  border-color: var(--filter-active);
}

.duration-option.active {
  border-color: var(--filter-active);
  background: var(--filter-active-light);
}

.duration-icon {
  font-size: 1.5rem;
  color: var(--filter-active);
  margin-bottom: 0.5rem;
}

.duration-label span {
  display: block;
  font-weight: 500;
  color: var(--filter-text);
}

.duration-label small {
  display: block;
  font-size: 0.75rem;
  color: rgba(var(--filter-text), 0.7);
}

/* Acciones */
.filter-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 1rem;
  border-top: 1px solid var(--filter-divider);
  flex-wrap: wrap;
  gap: 0.75rem;
}

.apply-btn, .reset-btn {
  padding: 0.75rem 1.25rem;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.2s ease;
}

.apply-btn {
  background: var(--filter-active);
  color: white;
  border: none;
}

.apply-btn:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

.reset-btn {
  background: none;
  border: 1px solid var(--filter-border);
  color: var(--filter-text);
}

.reset-btn:hover {
  border-color: var(--filter-active);
  color: var(--filter-active);
}

.results-count {
  font-size: 0.9rem;
  color: rgba(var(--filter-text), 0.8);
  margin-left: auto;
}

/* Transiciones */
.slide-fade-enter-active, .slide-fade-leave-active {
  transition: all 0.3s ease;
}
.slide-fade-enter-from, .slide-fade-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

.expand-enter-active, .expand-leave-active {
  transition: all 0.3s ease;
  overflow: hidden;
}
.expand-enter-from, .expand-leave-to {
  max-height: 0;
  opacity: 0;
}
.expand-enter-to, .expand-leave-from {
  max-height: 500px;
  opacity: 1;
}

/* Responsive */
@media (max-width: 768px) {
  .filters-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.75rem;
  }

  .filter-actions {
    flex-direction: column;
    align-items: stretch;
  }

  .apply-btn, .reset-btn {
    width: 100%;
    justify-content: center;
  }

  .results-count {
    margin-left: 0;
    text-align: center;
  }
}

@media (max-width: 480px) {
  .duration-options {
    grid-template-columns: 1fr;
  }
}
</style>