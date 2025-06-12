<template>
  <div v-if="show" class="modal-overlay">
    <div class="modal-wrapper">
      <div class="modal-card">
        <div class="modal-header">
          <h3 class="modal-title">Nueva Producción</h3>
          <button @click="close" class="modal-close-button">
            <i class="fas fa-times"></i>
          </button>
        </div>

        <div class="modal-form">
          <div class="form-field">
            <label class="form-label">Título <span class="required">*</span></label>
            <input
                v-model="formData.titulo"
                type="text"
                class="form-input"
                required
            >
          </div>

          <div class="form-field">
            <label class="form-label">Tipo <span class="required">*</span></label>
            <select
                v-model="formData.tipo"
                class="form-select"
                required
            >
              <option value="PELICULA">Película</option>
              <option value="SERIE">Serie</option>
              <option value="CORTO">Cortometraje</option>
            </select>
          </div>

          <div class="form-row">
            <div class="form-field form-col">
              <label class="form-label">Fecha de Estreno <span class="required">*</span></label>
              <input
                  v-model="formData.estreno"
                  type="date"
                  class="form-input"
                  required
                  :disabled="isUploading || isSaving"
              >
            </div>

            <div class="form-field form-col">
              <label class="form-label">Duración (minutos) <span class="required">*</span></label>
              <input
                  v-model="formData.duracion"
                  type="number"
                  min="1"
                  class="form-input"
                  required
                  :disabled="isUploading || isSaving"
              >
            </div>
          </div>

          <div class="form-field" style="display: none">
            <label class="form-label">Sinopsis <span class="required">*</span></label>
            <textarea
                v-model="formData.sinopsis"
                rows="3"
                class="form-textarea"
                required
                :disabled="isUploading || isSaving"
            ></textarea>
          </div>

          <div class="form-field">
            <label class="form-label">Clasificación de Edad <span class="required">*</span></label>
            <select v-model="formData.clasificacionEdad" class="form-select" required>
              <option v-for="clasificacion in clasificacionesEdad"
                      :key="clasificacion.valorNumerico"
                      :value="clasificacion.valorNumerico">
                {{ clasificacion.valorNumerico }}+ - {{ clasificacion.descripcion }}
              </option>
            </select>
          </div>

          <div class="form-field">
            <label class="form-label">Categorías</label>
            <div class="form-checkbox-group">
              <div v-for="categoria in categoriasDisponibles" :key="categoria" class="form-checkbox-item">
                <input type="checkbox"
                       :id="'cat-'+categoria"
                       :value="categoria"
                       v-model="formData.categorias"
                       class="form-checkbox">
                <label :for="'cat-'+categoria" class="form-checkbox-label">{{ categoria }}</label>
              </div>
            </div>
          </div>

          <div class="modal-footer">
            <button
                type="button"
                @click="close"
                class="button button-secondary"
            >
              Cancelar
            </button>
            <button
                type="submit"
                @click="save"
                class="button button-primary"
            >
              Guardar
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CreateProduccionModal',
  props: {
    show: Boolean
  },
  data() {
    return {
      formData: {
        titulo: '',
        duracion: 120,
        tipo: 'PELICULA',
        estreno: new Date().toISOString().split('T')[0],
        categorias: [],
        clasificacionEdad: 0
      },
      clasificacionesEdad: [
        { valorNumerico: 0, descripcion: 'Para todos los públicos' },
        { valorNumerico: 7, descripcion: 'Mayores de 7 años' },
        { valorNumerico: 12, descripcion: 'Mayores de 12 años' },
        { valorNumerico: 16, descripcion: 'Mayores de 16 años' },
        { valorNumerico: 18, descripcion: 'Mayores de 18 años' }
      ],
      categoriasDisponibles: [
        'FICCION', 'AVENTURA', 'COMEDIA', 'DRAMA', 'TERROR',
        'CIENCIA_FICCION', 'FANTASIA', 'ROMANCE', 'DOCUMENTAL',
        'ANIMACION', 'ACCION', 'CRIMEN', 'OTROS', 'MUSICAL', 'SUSPENSE'
      ]
    };
  },
  methods: {
    close() {
      this.$emit('close');
    },
    save() {
      if (!this.formData.titulo || !this.formData.tipo || !this.formData.estreno) {
        this.$toast.error('Por favor complete los campos obligatorios');
        return;
      }

      this.$emit('save', {...this.formData});
      this.resetForm();
    },
    resetForm() {
      this.formData = {
        titulo: '',
        duracion: 120,
        tipo: 'PELICULA',
        estreno: new Date().toISOString().split('T')[0],
        categorias: [],
        clasificacionEdad: 0
      };
    }
  }
};
</script>

<style scoped>
.modal-card {
  max-height: 90vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  /* Quita el padding aquí */
}

.modal-form {
  margin-top: 1rem;
  flex: 1 1 auto;
  overflow-y: auto;
  min-height: 0;
  padding: 2rem; /* Pon el padding aquí */
}

.modal-footer {
  position: static;
  background: #fff;
  padding-top: 1rem;
  margin-top: 2rem;
  z-index: 2;
}
</style>
<style scoped src="@/assets/styles/modalesAdmin.css" />