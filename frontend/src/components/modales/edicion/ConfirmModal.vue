<template>
  <div v-if="show" class="modal-overlay">
    <div class="modal-wrapper" style="width: 500px">
      <div class="modal-card">
        <div class="modal-header">
          <h3 class="modal-title">{{ title }}</h3>
          <button @click="cancel" class="modal-close-button">
            <i class="fas fa-times"></i>
          </button>
        </div><br>

        <div class="modal-content">
          <p>{{ message }}</p>

          <div class="modal-footer">
            <button type="button" @click="cancel" class="button button-secondary">Cancelar</button>
            <button type="button" @click="confirm" class="button button-danger">Confirmar</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ConfirmModal',
  props: {
    show: Boolean,
    title: String,
    message: String
  },
  methods: {
    confirm() {
      this.$emit('confirm');
    },
    cancel() {
      this.$emit('cancel');
    }
  }
};
</script>

<style scoped>
.button-danger {
  background-color: #ef4444;
  color: white;
}

.button-danger:hover {
  background-color: #dc2626;
}
</style>

<style scoped src="@/assets/styles/modalesAdmin.css" />