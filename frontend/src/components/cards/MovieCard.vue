<template>
  <div class="card-base" :class="{ 'dark-mode': darkMode }">
    <div class="card-image" style="padding-bottom: 150%">
      <img :src="movie.poster" :alt="movie.title" loading="lazy">
      <button class="favorite-btn" @click.stop="toggleFavorite"
              :aria-label="movie.isFavorite ? 'Quitar de favoritos' : 'Añadir a favoritos'">
        <i :class="movie.isFavorite ? 'fas fa-heart' : 'far fa-heart'"></i>
      </button>
      <div class="info-badge">
        +{{ movie.clasificacionEdad }}
      </div>
      <div class="rating-badge" :class="{ 'no-rating': !hasRating }">
        <i class="fas fa-star"></i> {{ ratingDisplay }}
      </div>
    </div>

    <div class="card-body">
      <div class="card-header-row">
        <h3 class="card-title">{{ movie.title }}</h3>
        <span class="card-subtitle">{{ movie.year }}</span>
      </div>

      <div class="card-meta">
        <span>
          <i class="fas fa-clock"></i> {{ formatDuration(movie.duration) }}
        </span>
        <span>
          <i class="fas fa-film"></i> {{ formatType(movie.type) }}
        </span>
      </div>

      <div class="tags-container">
        <span v-for="(genre, index) in movie.genres" :key="index" class="tag">
          {{ formatGenre(genre) }}
        </span>
      </div>

      <p class="card-description">{{ truncateText(movie.plot, 120) }}</p>

      <button class="details-btn" @click.stop="viewDetails">
        Ver detalles <i class="fas fa-chevron-right"></i>
      </button>
    </div>
  </div>
</template>

<script>
import '@/assets/styles/cards.css'

export default {
  name: 'MovieCard',
  props: {
    movie: {
      type: Object,
      required: true,
      default: () => ({
        id: '',
        title: '',
        type: '',
        year: 0,
        duration: 0,
        plot: '',
        poster: '',
        puntuacion: 0, // Rating local (segunda opción)
        clasificacionEdad: 0,
        genres: [],
        isFavorite: false
      })
    },
    darkMode: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      apiRating: null,
      usingLocalRating: false,
      isLoadingRating: false,
      ratingError: false
    }
  },
  computed: {
    hasRating() {
      return this.apiRating !== null || this.movie.puntuacion > 0
    },
    ratingDisplay() {
      // PRIORIDAD 1: Rating de la API externa
      if (this.apiRating !== null) {
        this.usingLocalRating = false
        return this.apiRating.toFixed(1)
      }

      // PRIORIDAD 2: Rating local
      if (this.movie.puntuacion > 0) {
        this.usingLocalRating = true
        return this.movie.puntuacion.toFixed(1)
      }

      // No hay rating disponible
      this.usingLocalRating = false
      return 'N/A'
    }
  },
  async created() {
    await this.fetchExternalRating()
  },
  methods: {
    async fetchExternalRating() {
      this.isLoadingRating = true
      this.ratingError = false

      try {
        const response = await fetch(
            `https://www.omdbapi.com/?t=${encodeURIComponent(this.movie.title)}&y=${this.movie.year}&apikey=c129cb21`
        )

        if (!response.ok) throw new Error('Error al conectar con OMDb API')

        const data = await response.json()

        if (data.Response === 'True') {
          // Buscamos primero rating de Rotten Tomatoes
          const rtRating = data.Ratings?.find(r => r.Source === 'Rotten Tomatoes')

          if (rtRating) {
            // Convertimos "87%" a 8.7
            this.apiRating = parseFloat(rtRating.Value)
          }
          // Fallback a IMDB si no hay Rotten Tomatoes
          else if (data.imdbRating && data.imdbRating !== 'N/A') {
            this.apiRating = parseFloat(data.imdbRating) // Ya está en escala 0-10
          }
        }
      } catch (error) {
        console.error('Error obteniendo rating externo:', error)
        this.ratingError = true
      } finally {
        this.isLoadingRating = false
      }
    },
    toggleFavorite() {
      this.$emit('toggle-favorite', this.movie.id)
    },
    viewDetails() {
      this.$router.push(`/movie/${this.movie.id}`)
    },
    truncateText(text, length) {
      if (!text) return ''
      return text.length > length ? text.substring(0, length) + '...' : text
    },
    formatDuration(minutes) {
      const hours = Math.floor(minutes / 60)
      const mins = minutes % 60
      return `${hours}h ${mins}m`
    },
    formatGenre(genre) {
      const genreMap = {
        'ACCION': 'Acción',
        'AVENTURA': 'Aventura',
        'CRIMEN': 'Crimen',
        'DRAMA': 'Drama',
        'FANTASIA': 'Fantasía',
        'CIENCIA_FICCION': 'Ciencia Ficción',
        'ROMANCE': 'Romance',
        'MUSICAL': 'Musical',
        'COMEDIA': 'Comedia',
        'WESTERN': 'Western'
      }
      return genreMap[genre] || genre
    },
    formatType(type) {
      return type === 'PELICULA' ? 'Película' : type
    }
  }
}
</script>

<style scoped>

.rating-badge {
  position: absolute;
  bottom: 10px;
  right: 10px;
  background-color: rgba(0, 0, 0, 0.8);
  color: #FFD700;
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 0.3rem;
  z-index: 2;
}

.rating-badge.no-rating {
  opacity: 0.7;
}

.rating-badge.local-rating::after {
  content: '†';
  font-size: 0.7rem;
  margin-left: 2px;
  opacity: 0.7;
}

.rating-badge i {
  color: #FFD700;
}

.info-badge {
  position: absolute;
  bottom: 10px;
  left: 10px;
  background-color: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: bold;
  z-index: 2;
}

/* Efecto de carga */
@keyframes pulse {
  0% {
    opacity: 0.6;
  }
  50% {
    opacity: 1;
  }
  100% {
    opacity: 0.6;
  }
}

.loading-rating {
  animation: pulse 1.5s infinite;
  width: 60px;
  height: 20px;
  background-color: rgba(255, 215, 0, 0.2);
  border-radius: 20px;
}
</style>