<template>
  <div class="modal-overlay" v-if="show" @click.self="close">
    <div class="modal-container">
      <div class="modal-header">
        <h3>{{ profesional.id ? 'Editar Profesional' : 'Nuevo Profesional' }}</h3>
        <button class="close-btn" @click="close">
          <i class="fas fa-times"></i>
        </button>
      </div>

      <div class="modal-body">
        <form @submit.prevent="save">
          <div class="form-group">
            <label>Nombre completo</label>
            <input v-model="formData.nombre" type="text" required>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label>Fecha de nacimiento</label>
              <input v-model="formData.fechaNacimiento" type="date">
            </div>

            <div class="form-group">
              <label>Fecha de inicio</label>
              <input v-model="formData.fechaInicio" type="date">
            </div>
          </div>

          <div class="form-group">
            <label>Lugar de nacimiento</label>
            <input v-model="formData.lugarNacimiento" type="text">
          </div>

          <div class="form-group">
            <label>Biografía</label>
            <textarea v-model="formData.biografia" rows="4"></textarea>
          </div>

          <div class="form-group">
            <label>Foto (URL)</label>
            <input v-model="formData.foto" type="url" placeholder="https://ejemplo.com/foto.jpg">
          </div>

          <div class="modal-footer">
            <button type="button" class="btn-cancel" @click="close">
              Cancelar
            </button>
            <button type="submit" class="btn-save">
              {{ profesional.id ? 'Actualizar' : 'Guardar' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProfesionalModal',
  props: {
    show: Boolean,
    profesional: Object
  },
  data() {
    return {
      formData: {
        nombre: '',
        fechaNacimiento: '',
        fechaInicio: '',
        lugarNacimiento: '',
        biografia: '',
        foto: ''
      }
    };
  },
  watch: {
    profesional: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.formData = { ...newVal };
        } else {
          this.resetForm();
        }
      }
    }
  },
  methods: {
    resetForm() {
      this.formData = {
        nombre: '',
        fechaNacimiento: '',
        fechaInicio: '',
        lugarNacimiento: '',
        biografia: '',
        foto: ''
      };
    },
    close() {
      this.$emit('close');
    },
    save() {
      this.$emit('save', { ...this.formData });
    }
  }
};
</script>

<style scoped>
/* Estilos similares al modal de producciones pero adaptados */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-container {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  padding: 16px 24px;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  font-size: 1.25rem;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.25rem;
  cursor: pointer;
  color: #666;
}

.modal-body {
  padding: 24px;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.form-group textarea {
  min-height: 100px;
  resize: vertical;
}

.form-row {
  display: flex;
  gap: 16px;
}

.form-row .form-group {
  flex: 1;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 16px;
  margin-top: 16px;
  border-top: 1px solid #eee;
}

.btn-cancel {
  padding: 8px 16px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}

.btn-save {
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

/* Dark mode */
.dark-mode .modal-container {
  background-color: #2d3748;
  color: white;
}

.dark-mode .modal-header {
  border-bottom-color: #4a5568;
}

.dark-mode .close-btn {
  color: #a0aec0;
}

.dark-mode .form-group input,
.dark-mode .form-group textarea {
  background-color: #4a5568;
  border-color: #4a5568;
  color: white;
}

.dark-mode .btn-cancel {
  background-color: #4a5568;
  border-color: #4a5568;
  color: white;
}

.dark-mode .modal-footer {
  border-top-color: #4a5568;
}
</style>