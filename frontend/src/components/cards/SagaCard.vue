<template>
  <div class="card-base" :class="{ 'dark-mode': darkMode }">
    <div class="card-image" style="padding-bottom: 150%">
      <img :src="saga.imagen || 'default-saga.jpg'" :alt="saga.nombre" loading="lazy">
      <div class="status-badge" :class="{ 'finished': saga.isAcabada, 'ongoing': !saga.isAcabada }">
        {{ formatStatus(saga.isAcabada) }}
      </div>
      <div class="productions-badge">
        <i class="fas fa-film"></i> {{ saga.producciones.length }}
      </div>
    </div>

    <div class="card-body">
      <div class="card-header-row">
        <h3 class="card-title">{{ saga.nombre }}</h3>
        <span class="card-subtitle">{{ formatYear(saga.fechaInicio) }}</span>
      </div>

      <div class="card-meta">
        <span>
          <i class="fas fa-calendar"></i> {{ formatDateRange(saga.fechaInicio, saga.fechaFin) }}
        </span>
      </div>

      <p class="card-description">{{ truncateText(saga.descripcion, 120) }}</p>

      <button class="details-btn" @click.stop="viewDetails">
        Ver detalles <i class="fas fa-chevron-right"></i>
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SagaCard',
  props: {
    saga: {
      type: Object,
      required: true,
      default: () => ({
        id: '',
        nombre: '',
        descripcion: '',
        isAcabada: false,
        fechaInicio: new Date(),
        fechaFin: null,
        imagen: null,
        producciones: []
      })
    },
    darkMode: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    viewDetails() {
      this.$router.push(`/saga/${this.saga.id}`);
    },
    truncateText(text, length) {
      if (!text) return 'No hay descripción disponible';
      return text.length > length ? text.substring(0, length) + '...' : text;
    },
    formatStatus(isAcabada) {
      return isAcabada ? 'Finalizada' : 'En curso';
    },
    formatYear(date) {
      if (!date) return 'N/A';
      return new Date(date).getFullYear();
    },
    formatDateRange(startDate, endDate) {
      const start = this.formatYear(startDate);
      const end = endDate ? this.formatYear(endDate) : 'Actualidad';
      return `${start} - ${end}`;
    }
  }
};
</script>

<style scoped>
/* Estilos específicos para la tarjeta de saga */
.status-badge {
  position: absolute;
  top: 10px;
  left: 10px;
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: bold;
  z-index: 2;
}

.status-badge.finished {
  background-color: rgba(76, 175, 80, 0.9);
  color: white;
}

.status-badge.ongoing {
  background-color: rgba(33, 150, 243, 0.9);
  color: white;
}

.productions-badge {
  position: absolute;
  bottom: 10px;
  left: 10px;
  background-color: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 0.3rem 0.8rem;
  border-radius: 20px;
  font-size: 0.9rem;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 0.3rem;
  z-index: 2;
}

.card-meta {
  margin-bottom: 1rem;
}

.card-meta i {
  margin-right: 0.3rem;
  color: var(--primary-color);
}
</style>