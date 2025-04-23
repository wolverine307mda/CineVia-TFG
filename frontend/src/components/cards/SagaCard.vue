<template>
  <div class="card-base" :class="{ 'dark-mode': darkMode }">
    <div class="card-image" style="padding-bottom: 150%">
      <img :src="movie.poster" :alt="movie.title" loading="lazy">
      <button class="favorite-btn" @click.stop="toggleFavorite"
              :aria-label="movie.isFavorite ? 'Quitar de favoritos' : 'Añadir a favoritos'">
        <i :class="movie.isFavorite ? 'fas fa-heart' : 'far fa-heart'"></i>
      </button>
      <div class="info-badge">
        +{{ movie.ageRating }}
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

      <div class="card-footer">
        <button class="details-btn" @click.stop="viewDetails">
          Ver detalles <i class="fas fa-chevron-right"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SagaCard',
  props: {
    saga: {
      type: Object,
      required: true
    },
    darkMode: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    viewDetails() {
      this.$router.push(`/sagas/${this.saga.id}`);
    }
  }
}
</script>