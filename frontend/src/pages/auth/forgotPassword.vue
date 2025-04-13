<template>
  <AuthLayout v-slot="{ darkMode, toggleDarkMode }">
    <div class="auth-card">
      <button class="toggle-dark" @click="toggleDarkMode">
        <i :class="darkMode ? 'fas fa-sun' : 'fas fa-moon'" />
      </button>

      <div class="text-center mb-6">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="80" height="80">
          <!-- Cabeza -->
          <circle cx="12" cy="8" r="4" fill="#7e5bef" />
          <!-- Cuerpo -->
          <path d="M12 14c-3.314 0-6 1.686-6 3v2h12v-2c0-1.314-2.686-3-6-3z" fill="#7e5bef" />
        </svg>

        <h2 class="auth-title">
          Recuperar contraseña
        </h2>
        <p class="auth-subtitle">
          Ingresa tu email para recibir instrucciones
        </p>
      </div>

      <form class="auth-form" @submit.prevent="sendResetLink">
        <div class="form-group">
          <label for="email">Correo electrónico</label>
          <div class="input-group">
            <input
                id="email"
                v-model="email"
                type="email"
                placeholder="tu@email.com"
                required
            >
            <i class="fas fa-envelope input-icon" />
          </div>
        </div>

        <button type="submit" class="btn-auth btn-primary" :disabled="loading">
          <span v-if="!loading">Enviar enlace</span>
          <span v-else><i class="fas fa-spinner fa-spin" /> Enviando...</span>
        </button>

        <div class="auth-footer">
          <a class="auth-link"
             ref="loginBtn"
             href="/auth/login">
            <i class="fas fa-arrow-left" /> Volver al inicio de sesión
          </a>
        </div>
      </form>
    </div>
  </AuthLayout>
</template>

<script>
import AuthLayout from '@/layouts/AuthLayout.vue'

export default {
  components: { AuthLayout },
  data() {
    return {
      email: '',
      loading: false
    }
  },
  methods: {
    sendResetLink() {
      this.loading = true
      setTimeout(() => {
        this.loading = false
        alert(`Se ha enviado un enlace a ${this.email}`)
        this.$emit('toggle-view', 'LoginForm')
      }, 1500)
    }
  }
}
</script>