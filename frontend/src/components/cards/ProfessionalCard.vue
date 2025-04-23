<template>
  <div class="card-base" :class="{ 'dark-mode': darkMode }">
    <div class="card-image" style="padding-bottom: 120%">
      <img :src="professional.foto" :alt="professional.nombre" loading="lazy" />
      <button class="favorite-btn" @click.stop="toggleFavorite"
              :aria-label="professional.isFavorite ? 'Quitar de favoritos' : 'Añadir a favoritos'">
        <i :class="professional.isFavorite ? 'fas fa-heart' : 'far fa-heart'"></i>
      </button>
      <div class="info-badge" v-if="professional.fechaInicio">
        <i class="fas fa-chart-line"></i>
        {{ calcularAniosExperiencia(professional.fechaInicio) }} años
      </div>
    </div>

    <div class="card-body">
      <div class="card-header-row">
        <h3 class="card-title">{{ professional.nombre }}</h3>
        <span class="card-subtitle">Actor</span>
      </div>

      <div class="card-meta">
        <span v-if="professional.lugarNacimiento">
          <i class="fas fa-map-marker-alt"></i> {{ professional.lugarNacimiento }}
        </span>
        <span v-if="professional.fechaNacimiento">
          <i class="fas fa-birthday-cake"></i> {{ formatFecha(professional.fechaNacimiento) }}
        </span>
      </div>

      <p class="card-description">{{ truncateText(professional.biografia, 120) }}</p>

      <div class="card-section" v-if="professional.participacionesCount > 0">
        <h4 class="card-section-title">Participaciones:</h4>
        <span class="tag">{{ professional.participacionesCount }} producción(es)</span>
      </div>

      <div class="card-footer">
        <button class="details-btn" @click.stop="viewDetails">
          Ver perfil <i class="fas fa-chevron-right"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import '@/assets/styles/cards.css'

export default {
  name: 'ProfessionalCard',
  props: {
    professional: {
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
      this.$emit('toggle-favorite', this.professional.id)
    },
    viewDetails() {
      this.$router.push(`/professional/${this.professional.id}`)
    },
    truncateText(text, length) {
      if (!text) return ''
      return text.length > length ? text.substring(0, length) + '...' : text
    },
    calcularAniosExperiencia(fechaInicio) {
      const inicio = new Date(fechaInicio)
      const ahora = new Date()
      return ahora.getFullYear() - inicio.getFullYear()
    },
    formatFecha(fecha) {
      const date = new Date(fecha)
      return date.toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      })
    }
  }
}
</script>
