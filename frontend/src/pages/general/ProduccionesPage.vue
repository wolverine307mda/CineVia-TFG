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
    <div class="filters-panel">
      <!-- Sección de Búsqueda -->
      <section class="search-section">
        <div class="search-box">
          <i class="fas fa-search"></i>
          <input
              type="text"
              v-model="searchQuery"
              placeholder="Buscar películas..."
              @input="applyFilters"
          />
          <button class="clear-btn" @click="clearSearch" v-if="searchQuery">
            <i class="fas fa-times"></i>
          </button>
        </div>
      </section>

      <!-- Filtros principales -->
      <section class="filters-grid">
        <!-- Género -->
        <div class="filter-group">
          <label class="filter-label">
            <i class="fas fa-tags"></i> Género
          </label>
          <v-select
              v-model="selectedGenres"
              :options="genres"
              multiple
              placeholder="Todos los géneros"
              class="styled-select"
              label="name"
              :reduce="genre => genre.value"
              @input="applyFilters"
          />
        </div>

        <!-- Año -->
        <div class="filter-group">
          <label class="filter-label">
            <i class="fas fa-calendar-alt"></i> Año
          </label>
          <div class="date-range">
            <date-picker
                v-model="yearRange"
                type="year"
                range
                placeholder="Seleccionar rango"
                :clearable="false"
                @change="applyFilters"
                class="styled-datepicker"
            />
          </div>
        </div>

        <!-- Valoración -->
        <div class="filter-group">
          <label class="filter-label">
            <i class="fas fa-star"></i> Valoración
          </label>
          <div class="rating-filter">
            <input
                type="range"
                v-model="ratingFilter"
                min="0"
                max="10"
                step="0.5"
                @input="applyFilters"
            />
            <span class="rating-value">{{ ratingFilter }}+</span>
          </div>
        </div>

        <!-- Duración -->
        <div class="filter-group">
          <label class="filter-label">
            <i class="fas fa-clock"></i> Duración
          </label>
          <div class="duration-buttons">
            <button
                v-for="duration in durationOptions"
                :key="duration.value"
                :class="{ active: selectedDuration === duration.value }"
                @click="setDuration(duration.value)"
            >
              {{ duration.label }}
            </button>
          </div>
        </div>
      </section>

      <!-- Acciones -->
      <section class="filter-actions">
        <button class="reset-btn" @click="resetFilters">
          <i class="fas fa-undo"></i> Reiniciar Filtros
        </button>
        <span class="results-count">
      Mostrando {{ filteredMovies.length }} de {{ movies.length }} películas
    </span>
      </section>
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
        { label: 'Corta (<90 min)', value: 'short' },
        { label: 'Media (90-150 min)', value: 'medium' },
        { label: 'Larga (>150 min)', value: 'long' },
        { label: 'Cualquiera', value: '' }
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

    applyFilters() {
      this.currentPage = 1
    },

    resetFilters() {
      this.searchQuery = ''
      this.selectedGenres = []
      this.yearRange = null
      this.ratingFilter = 0
      this.selectedDuration = ''
      this.currentPage = 1
    },

    clearSearch() {
      this.searchQuery = ''
      this.applyFilters()
    },

    setDuration(duration) {
      this.selectedDuration = duration
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

.theme-toggle {
  background: var(--color-primary);
  color: white;
  border: none;
  padding: 0.6rem 1.2rem;
  border-radius: 50px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: var(--transition);
}

.theme-toggle:hover {
  background: var(--color-primary-dark);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(108, 92, 231, 0.3);
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

/* Datepicker Estilizado */
.styled-datepicker {
  width: 100%;
}

.styled-datepicker >>> .dp__input {
  background-color: var(--color-card);
  border: 1px solid var(--color-border);
  color: var(--color-text);
  height: 42px;
  padding: 0.7rem 1rem;
  border-radius: 8px;
}

.dark-mode .styled-datepicker >>> .dp__input {
  background-color: #2a2a2a;
  border-color: #444;
}

/* Filtro de Rating */
.rating-filter {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0.5rem 0;
}

.rating-filter input[type="range"] {
  flex: 1;
  -webkit-appearance: none;
  height: 6px;
  background: var(--color-border);
  border-radius: 3px;
  outline: none;
}

.rating-filter input[type="range"]::-webkit-slider-thumb {
  -webkit-appearance: none;
  width: 18px;
  height: 18px;
  background: var(--color-primary);
  border-radius: 50%;
  cursor: pointer;
  transition: var(--transition);
}

.rating-value {
  font-weight: 600;
  color: var(--color-primary);
  min-width: 40px;
  text-align: center;
}

/* Botones de Duración */
.duration-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.duration-buttons button {
  flex: 1;
  min-width: 100px;
  padding: 0.6rem 0.8rem;
  background-color: var(--color-card);
  border: 1px solid var(--color-border);
  border-radius: 8px;
  color: var(--color-text);
  font-size: 0.85rem;
  cursor: pointer;
  transition: var(--transition);
}

.duration-buttons button:hover {
  border-color: var(--color-primary-light);
}

.duration-buttons button.active {
  background-color: var(--color-primary);
  color: white;
  border-color: var(--color-primary);
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
</style>