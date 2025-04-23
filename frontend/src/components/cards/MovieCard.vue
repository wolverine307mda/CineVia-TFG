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
      <div class="rating-badge">
        <i class="fas fa-star"></i> {{ movie.puntuacion.toFixed(1) }}
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
        puntuacion: 0,
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
  methods: {
    toggleFavorite() {
      this.$emit('toggle-favorite', this.movie.id);
    },
    viewDetails() {
      this.$router.push(`/movie/${this.movie.id}`);
    },
    truncateText(text, length) {
      if (!text) return '';
      return text.length > length ? text.substring(0, length) + '...' : text;
    },
    formatDuration(minutes) {
      const hours = Math.floor(minutes / 60);
      const mins = minutes % 60;
      return `${hours}h ${mins}m`;
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
      };
      return genreMap[genre] || genre;
    },
    formatType(type) {
      return type === 'PELICULA' ? 'Película' : type;
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

</style>