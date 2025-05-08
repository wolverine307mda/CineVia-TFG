<template>
  <div v-if="show" class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <div class="modal-header">
        <h3>{{ saga ? 'Editar Saga' : 'Nueva Saga' }}</h3>
        <button class="close-btn" @click="close">&times;</button>
      </div>
      <div class="modal-body">
        <form @submit.prevent="save">
          <div class="form-group">
            <label for="nombre">Nombre*</label>
            <input
                id="nombre"
                v-model="formData.nombre"
                type="text"
                required
                placeholder="Nombre de la saga"
            />
          </div>

          <div class="form-group">
            <label for="descripcion">Descripción</label>
            <textarea
                id="descripcion"
                v-model="formData.descripcion"
                rows="3"
                placeholder="Descripción de la saga"
            ></textarea>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="fechaInicio">Fecha Inicio*</label>
              <input
                  id="fechaInicio"
                  v-model="formData.fechaInicio"
                  type="date"
                  required
              />
            </div>

            <div class="form-group">
              <label for="fechaFin">Fecha Fin</label>
              <input
                  id="fechaFin"
                  v-model="formData.fechaFin"
                  type="date"
                  :disabled="!formData.isAcabada"
              />
            </div>
          </div>

          <div class="form-group checkbox-group">
            <input
                id="isAcabada"
                v-model="formData.isAcabada"
                type="checkbox"
            />
            <label for="isAcabada">Saga finalizada</label>
          </div>

          <div class="form-group">
            <label for="imagen">URL de la imagen</label>
            <input
                id="imagen"
                v-model="formData.imagen"
                type="text"
                placeholder="URL de la imagen"
            />
            <div v-if="formData.imagen" class="image-preview">
              <img :src="formData.imagen" alt="Preview" @error="handleImageError" />
            </div>
          </div>

          <div class="modal-footer">
            <button type="button" class="btn-cancel" @click="close">
              Cancelar
            </button>
            <button type="submit" class="btn-save">
              {{ saga ? 'Actualizar' : 'Guardar' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'SagaModal',
  props: {
    show: {
      type: Boolean,
      required: true
    },
    saga: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      formData: {
        nombre: '',
        descripcion: '',
        isAcabada: false,
        fechaInicio: '',
        fechaFin: null,
        imagen: null
      }
    };
  },
  watch: {
    saga: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.formData = {
            nombre: newVal.nombre,
            descripcion: newVal.descripcion || '',
            isAcabada: newVal.isAcabada,
            fechaInicio: this.formatDateForInput(newVal.fechaInicio),
            fechaFin: newVal.fechaFin ? this.formatDateForInput(newVal.fechaFin) : null,
            imagen: newVal.imagen || null
          };
        } else {
          this.resetForm();
        }
      }
    },
    'formData.isAcabada': {
      handler(newVal) {
        if (!newVal) {
          this.formData.fechaFin = null;
        }
      }
    }
  },
  methods: {
    formatDateForInput(dateString) {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toISOString().split('T')[0];
    },
    resetForm() {
      this.formData = {
        nombre: '',
        descripcion: '',
        isAcabada: false,
        fechaInicio: '',
        fechaFin: null,
        imagen: null
      };
    },
    close() {
      this.$emit('close');
    },
    save() {
      const sagaData = {
        ...this.formData,
        fechaFin: this.formData.isAcabada ? this.formData.fechaFin : null
      };
      this.$emit('save', sagaData);
    },
    handleImageError(e) {
      e.target.src = 'default-saga.jpg';
    }
  }
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background-color: var(--card-bg);
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  padding: 1rem 1.5rem;
  border-bottom: 1px solid var(--border-color);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  color: var(--text-primary);
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: var(--text-secondary);
}

.modal-body {
  padding: 1.5rem;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: var(--text-primary);
  font-weight: 500;
}

.form-group input[type="text"],
.form-group input[type="date"],
.form-group textarea {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid var(--border-color);
  border-radius: 4px;
  background-color: var(--input-bg);
  color: var(--text-primary);
}

.form-group textarea {
  min-height: 100px;
  resize: vertical;
}

.form-row {
  display: flex;
  gap: 1rem;
}

.form-row .form-group {
  flex: 1;
}

.checkbox-group {
  display: flex;
  align-items: center;
}

.checkbox-group input {
  margin-right: 0.5rem;
}

.image-preview {
  margin-top: 1rem;
}

.image-preview img {
  max-width: 100%;
  max-height: 200px;
  border-radius: 4px;
  border: 1px solid var(--border-color);
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding-top: 1rem;
  border-top: 1px solid var(--border-color);
}

.btn-cancel, .btn-save {
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
}

.btn-cancel {
  background-color: var(--btn-cancel-bg);
  color: var(--btn-cancel-text);
  border: 1px solid var(--border-color);
}

.btn-save {
  background-color: var(--primary-color);
  color: white;
  border: none;
}

.btn-save:hover {
  background-color: var(--primary-hover);
}

/* Dark mode variables */
:root {
  --input-bg: #ffffff;
  --btn-cancel-bg: #ffffff;
  --btn-cancel-text: #64748b;
}

.dark-mode {
  --input-bg: #1e293b;
  --btn-cancel-bg: #1e293b;
  --btn-cancel-text: #94a3b8;
}
</style>