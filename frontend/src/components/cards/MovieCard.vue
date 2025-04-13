<template>
  <div class="movie-card" :class="{ 'dark-mode': darkMode }">
    <div class="card-image">
      <img :src="movie.poster" :alt="movie.title" loading="lazy">
      <button
          class="favorite-btn"
          @click.stop="toggleFavorite"
          :aria-label="movie.isFavorite ? 'Quitar de favoritos' : 'Añadir a favoritos'"
      >
        <i :class="movie.isFavorite ? 'fas fa-heart' : 'far fa-heart'"></i>
      </button>
      <div class="rating-badge">
        <i class="fas fa-star"></i> {{ movie.rating.toFixed(1) }}
      </div>
    </div>

    <div class="card-body">
      <div class="card-header">
        <h3 class="movie-title">{{ movie.title }}</h3>
        <span class="movie-year">{{ movie.year }}</span>
      </div>

      <div class="movie-meta">
        <span class="movie-duration">
          <i class="fas fa-clock"></i> {{ Math.floor(movie.duration / 60) }}h {{ movie.duration % 60 }}m
        </span>
        <span class="movie-director">
          <i class="fas fa-user"></i> {{ movie.director }}
        </span>
      </div>

      <div class="genre-tags">
        <span v-for="(genre, index) in movie.genres" :key="index" class="genre-tag">
          {{ genre }}
        </span>
      </div>

      <p class="movie-plot">{{ truncateText(movie.plot, 120) }}</p>

      <button class="details-btn" @click.stop="viewDetails">
        Ver detalles <i class="fas fa-chevron-right"></i>
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MovieCard',
  props: {
    movie: {
      type: Object,
      required: true
    },
    darkMode: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    toggleFavorite() {
      this.$emit('toggle-favorite', this.movie.id)
    },

    viewDetails() {
      // Navegar a la página de detalles
      this.$router.push(`/movie/${this.movie.id}`)
    },

    truncateText(text, length) {
      if (!text) return ''
      return text.length > length ? text.substring(0, length) + '...' : text
    }
  }
}
</script>

<style scoped>
.movie-card {
  background-color: rgba(230, 228, 231, 0.75);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  transition: var(--transition);
  height: 100%;
  display: flex;
  flex-direction: column;
}

.dark-mode .movie-card {
  background-color: rgba(42, 42, 42, 0.92);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.movie-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

.card-image {
  position: relative;
  height: 0;
  padding-bottom: 150%;
  overflow: hidden;
}

.card-image img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: var(--transition);
}

.movie-card:hover .card-image img {
  transform: scale(1.05);
}

.favorite-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: rgba(0, 0, 0, 0.6);
  color: white;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: var(--transition);
  z-index: 2;
}

.favorite-btn:hover {
  background-color: var(--color-primary);
}

.favorite-btn i.fas {
  color: #ff4757;
}

.rating-badge {
  position: absolute;
  bottom: 10px;
  left: 10px;
  background-color: rgba(0, 0, 0, 0.8);
  color: #ffd700;
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: bold;
  z-index: 2;
  display: flex;
  align-items: center;
  gap: 0.3rem;
}

.card-body {
  padding: 1.2rem;
  flex: 1;
  display: flex;
  flex-direction: column;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 0.8rem;
}

.movie-title {
  margin: 0;
  font-size: 1.2rem;
  font-weight: 700;
  line-height: 1.3;
  flex: 1;
}

.movie-year {
  background-color: var(--color-primary);
  color: white;
  padding: 0.2rem 0.6rem;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 600;
  margin-left: 0.5rem;
}

.movie-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 0.8rem;
  margin-bottom: 0.8rem;
  font-size: 0.85rem;
  color: var(--color-text);
  opacity: 0.8;
}

.movie-meta i {
  margin-right: 0.3rem;
  color: var(--color-primary);
}

.genre-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.genre-tag {
  background-color: rgba(108, 92, 231, 0.2);
  color: var(--color-primary);
  padding: 0.2rem 0.6rem;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 600;
}

.movie-plot {
  font-size: 0.9rem;
  line-height: 1.4;
  margin-bottom: 1.2rem;
  flex: 1;
}

.details-btn {
  align-self: flex-start;
  background: none;
  border: none;
  color: var(--color-primary);
  text-decoration: none;
  font-weight: 600;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  gap: 0.3rem;
  transition: var(--transition);
  cursor: pointer;
  padding: 0.3rem 0.6rem;
  border-radius: 4px;
}

.details-btn:hover {
  color: var(--color-primary-dark);
  background-color: rgba(108, 92, 231, 0.1);
}

.details-btn i {
  font-size: 0.8rem;
}

@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    gap: 0.3rem;
  }

  .movie-year {
    align-self: flex-start;
    margin-left: 0;
  }
}
</style>