<template>
  <div class="professional-card" :class="{ 'dark-mode': darkMode }">
    <div class="card-image">
      <img :src="professional.profileImage" :alt="professional.name" loading="lazy">
      <button
          class="favorite-btn"
          @click.stop="toggleFavorite"
          :aria-label="professional.isFavorite ? 'Quitar de favoritos' : 'Añadir a favoritos'"
      >
        <i :class="professional.isFavorite ? 'fas fa-heart' : 'far fa-heart'"></i>
      </button>
      <div class="experience-badge">
        <i class="fas fa-chart-line"></i> {{ professional.experience }} años
      </div>
    </div>

    <div class="card-body">
      <div class="card-header">
        <h3 class="professional-name">{{ professional.name }}</h3>
        <span class="professional-role">{{ professional.role }}</span>
      </div>

      <div class="professional-meta">
        <span class="professional-location">
          <i class="fas fa-map-marker-alt"></i> {{ professional.location }}
        </span>
        <a v-if="professional.contact" :href="'mailto:' + professional.contact" class="professional-contact">
          <i class="fas fa-envelope"></i> Contactar
        </a>
      </div>

      <div class="skill-tags">
        <span v-for="(skill, index) in professional.skills.slice(0, 3)" :key="index" class="skill-tag">
          {{ skill }}
        </span>
        <span v-if="professional.skills.length > 3" class="skill-tag-more">
          +{{ professional.skills.length - 3 }}
        </span>
      </div>

      <p class="professional-bio">{{ truncateText(professional.bio, 120) }}</p>

      <div class="projects-section" v-if="professional.projects && professional.projects.length > 0">
        <h4>Proyectos destacados:</h4>
        <div class="project-tags">
          <span v-for="(project, index) in professional.projects.slice(0, 3)" :key="index" class="project-tag">
            {{ project }}
          </span>
        </div>
      </div>

      <button class="details-btn" @click.stop="viewDetails">
        Ver perfil <i class="fas fa-chevron-right"></i>
      </button>
    </div>
  </div>
</template>

<script>
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
      // Navegar a la página de detalles
      this.$router.push(`/professional/${this.professional.id}`)
    },

    truncateText(text, length) {
      if (!text) return ''
      return text.length > length ? text.substring(0, length) + '...' : text
    }
  }
}
</script>

<style scoped>
.professional-card {
  background-color: rgba(230, 228, 231, 0.75);
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  transition: var(--transition);
  height: 100%;
  display: flex;
  flex-direction: column;
}

.dark-mode .professional-card {
  background-color: rgba(42, 42, 42, 0.92);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
}

.professional-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

.card-image {
  position: relative;
  height: 0;
  padding-bottom: 120%;
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

.professional-card:hover .card-image img {
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

.experience-badge {
  position: absolute;
  bottom: 10px;
  left: 10px;
  background-color: rgba(0, 0, 0, 0.8);
  color: #ffffff;
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
  flex-direction: column;
  gap: 0.3rem;
  margin-bottom: 0.8rem;
}

.professional-name {
  margin: 0;
  font-size: 1.2rem;
  font-weight: 700;
  line-height: 1.3;
}

.professional-role {
  background-color: var(--color-primary);
  color: white;
  padding: 0.2rem 0.6rem;
  border-radius: 4px;
  font-size: 0.8rem;
  font-weight: 600;
  align-self: flex-start;
}

.professional-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 0.8rem;
  margin-bottom: 0.8rem;
  font-size: 0.85rem;
  color: var(--color-text);
  opacity: 0.8;
}

.professional-meta i {
  margin-right: 0.3rem;
  color: var(--color-primary);
}

.professional-contact {
  color: var(--color-primary);
  text-decoration: none;
  transition: var(--transition);
}

.professional-contact:hover {
  text-decoration: underline;
}

.skill-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.skill-tag {
  background-color: rgba(108, 92, 231, 0.2);
  color: var(--color-primary);
  padding: 0.2rem 0.6rem;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 600;
}

.skill-tag-more {
  background-color: rgba(108, 92, 231, 0.1);
  color: var(--color-primary);
  padding: 0.2rem 0.6rem;
  border-radius: 50px;
  font-size: 0.75rem;
  font-weight: 600;
  opacity: 0.7;
}

.professional-bio {
  font-size: 0.9rem;
  line-height: 1.4;
  margin-bottom: 1rem;
  flex: 1;
}

.projects-section {
  margin-bottom: 1rem;
}

.projects-section h4 {
  font-size: 0.85rem;
  margin-bottom: 0.5rem;
  color: var(--color-text);
  opacity: 0.9;
}

.project-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.project-tag {
  background-color: rgba(108, 92, 231, 0.1);
  color: var(--color-text);
  padding: 0.2rem 0.6rem;
  border-radius: 4px;
  font-size: 0.75rem;
  border-left: 3px;
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
}
</style>
