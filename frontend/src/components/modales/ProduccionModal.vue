<template>
  <div v-if="show" class="modal-overlay">
    <div class="modal-container">
      <div class="modal-header">
        <h3>{{ produccion ? 'Editar Producción' : 'Nueva Producción' }}</h3>
        <button @click="close" class="modal-close-btn">
          <i class="fas fa-times"></i>
        </button>
      </div>
      <div class="modal-body">
        <form @submit.prevent="save">
          <div class="form-group">
            <label>Título</label>
            <input v-model="formData.titulo" type="text" required>
          </div>

          <div class="form-group">
            <label>Tipo</label>
            <select v-model="formData.tipo" required>
              <option value="PELICULA">Película</option>
              <option value="SERIE">Serie</option>
              <option value="CORTO">Cortometraje</option>
            </select>
          </div>

          <div class="form-group">
            <label>Fecha de Estreno</label>
            <input v-model="formData.estreno" type="date" required>
          </div>

          <div class="form-group">
            <label>Duración (minutos)</label>
            <input v-model="formData.duracion" type="number" min="1" required>
          </div>

          <div class="form-group">
            <label>Sinopsis</label>
            <textarea v-model="formData.sinopsis" rows="3" required></textarea>
          </div>

          <div class="form-group">
            <label>Clasificación de Edad</label>
            <select v-model="formData.clasificacionEdad" required>
              <option v-for="clasificacion in clasificacionesEdad"
                      :key="clasificacion.valorNumerico"
                      :value="clasificacion.valorNumerico">
                {{ clasificacion.valorNumerico }}+ - {{ clasificacion }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label>Categorías</label>
            <div class="categories-container">
              <div v-for="categoria in categoriasDisponibles" :key="categoria" class="category-checkbox">
                <input type="checkbox"
                       :id="'cat-'+categoria"
                       :value="categoria"
                       v-model="formData.categorias">
                <label :for="'cat-'+categoria">{{ categoria }}</label>
              </div>
            </div>
          </div>

          <div class="form-group">
            <label>URL de la Imagen</label>
            <input v-model="formData.imagen" type="url">
          </div>

          <div class="form-group">
            <label>Información Adicional</label>
            <textarea v-model="formData.informacion" rows="3"></textarea>
          </div>

          <div class="form-actions">
            <button type="button" @click="close" class="btn-cancel">Cancelar</button>
            <button type="submit" class="btn-save">Guardar</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ProduccionModal',
  props: {
    show: Boolean,
    produccion: Object
  },
  data() {
    return {
      formData: {
        titulo: '',
        tipo: 'PELICULA',
        estreno: new Date().toISOString().split('T')[0],
        duracion: 90,
        sinopsis: '',
        imagen: null,
        informacion: null,
        categorias: [],
        clasificacionEdad: 0
      },
      clasificacionesEdad: [
        { valorNumerico: 0, nombre: 'Todos los públicos' },
        { valorNumerico: 7, nombre: 'Mayores de 7 años' },
        { valorNumerico: 12, nombre: 'Mayores de 12 años' },
        { valorNumerico: 16, nombre: 'Mayores de 16 años' },
        { valorNumerico: 18, nombre: 'Mayores de 18 años' }
      ],
      categoriasDisponibles: [
        'FICCION', 'AVENTURA', 'TERROR', 'COMEDIA', 'DRAMA',
        'DOCUMENTAL', 'ANIMACION', 'ROMANCE', 'OTROS', 'CRIMEN',
        'ACCION', 'CIENCIA_FICCION', 'FANTASIA', 'MUSICAL', 'WESTERN', 'SUSPENSE'
      ]
    };
  },
  watch: {
    produccion: {
      immediate: true,
      handler(newVal) {
        if (newVal) {
          this.formData = {
            titulo: newVal.titulo,
            tipo: newVal.tipo,
            estreno: new Date(newVal.estreno).toISOString().split('T')[0],
            duracion: newVal.duracion,
            sinopsis: newVal.sinopsis,
            imagen: newVal.imagen || null,
            informacion: newVal.informacion || null,
            categorias: [...newVal.categorias],
            clasificacionEdad: newVal.clasificacionEdad.valorNumerico
          };
        } else {
          this.resetForm();
        }
      }
    }
  },
  methods: {
    resetForm() {
      this.formData = {
        titulo: '',
        tipo: 'PELICULA',
        estreno: new Date().toISOString().split('T')[0],
        duracion: 90,
        sinopsis: '',
        imagen: null,
        informacion: null,
        categorias: [],
        clasificacionEdad: 0
      };
    },
    close() {
      this.$emit('close');
    },
    save() {
      const produccionData = {
        ...this.formData,
        id: this.produccion ? this.produccion.id : null
      };
      this.$emit('save', produccionData);
    }
  }
};
</script>

<style scoped>
/* Estilos base que funcionan en ambos modos */
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

.modal-container {
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border-bottom: 1px solid;
}

.modal-header h3 {
  margin: 0;
}

.modal-close-btn {
  background: none;
  border: none;
  font-size: 1.2rem;
  cursor: pointer;
}

.modal-body {
  padding: 1rem;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 0.5rem;
  border: 1px solid;
  border-radius: 4px;
}

.form-group textarea {
  min-height: 80px;
  resize: vertical;
}

.categories-container {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 0.5rem;
}

.category-checkbox {
  display: flex;
  align-items: center;
}

.category-checkbox input {
  width: auto;
  margin-right: 0.5rem;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1.5rem;
  padding-top: 1rem;
  border-top: 1px solid;
}

.btn-cancel,
.btn-save {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.btn-save:hover {
  opacity: 0.9;
}

/* Modo claro */
@media (prefers-color-scheme: light) {
  .modal-container {
    background-color: #ffffff;
  }

  .modal-header {
    border-bottom-color: #e2e8f0;
  }

  .modal-header h3 {
    color: #1e293b;
  }

  .modal-close-btn {
    color: #64748b;
  }

  .modal-close-btn:hover {
    color: #ef4444;
  }

  .form-group label {
    color: #1e293b;
  }

  .form-group input,
  .form-group select,
  .form-group textarea {
    background-color: #ffffff;
    color: #1e293b;
    border-color: #e2e8f0;
  }

  .form-actions {
    border-top-color: #e2e8f0;
  }

  .btn-cancel {
    background-color: #ef4444;
    color: white;
  }

  .btn-save {
    background-color: #7e5bef;
    color: white;
  }

  .btn-save:hover {
    background-color: #6d46e8;
  }

  .category-checkbox label {
    color: #1e293b;
  }
}

/* Modo oscuro */
@media (prefers-color-scheme: dark) {
  .modal-container {
    background-color: #1e293b;
  }

  .modal-header {
    border-bottom-color: #334155;
  }

  .modal-header h3 {
    color: #f8fafc;
  }

  .modal-close-btn {
    color: #94a3b8;
  }

  .modal-close-btn:hover {
    color: #ef4444;
  }

  .form-group label {
    color: #f8fafc;
  }

  .form-group input,
  .form-group select,
  .form-group textarea {
    background-color: #1e293b;
    color: #f8fafc;
    border-color: #334155;
  }

  .form-actions {
    border-top-color: #334155;
  }

  .btn-cancel {
    background-color: #ef4444;
    color: white;
  }

  .btn-save {
    background-color: #8b5cf6;
    color: white;
  }

  .btn-save:hover {
    background-color: #7c3aed;
  }

  .category-checkbox label {
    color: #f8fafc;
  }
}
</style>