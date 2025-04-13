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
          Crea tu cuenta
        </h2>
        <p class="auth-subtitle">
          Comienza tu experiencia con MovieTrip
        </p>
      </div>

      <form class="auth-form" @submit.prevent="register">
        <div class="form-group">
          <label for="name">Nombre completo</label>
          <div class="input-group">
            <input
                id="name"
                v-model="name"
                type="text"
                placeholder="Tu nombre"
                required
            >
            <i class="fas fa-user input-icon" />
          </div>
        </div>

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

        <div class="form-group">
          <label for="password">Contraseña</label>
          <div class="input-group">
            <input
                id="password"
                v-model="password"
                :type="showPassword ? 'text' : 'password'"
                placeholder="••••••••"
                required
            >
            <i
                class="input-icon"
                :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"
                style="cursor: pointer"
                @click="showPassword = !showPassword"
            />
          </div>
        </div>

        <div class="form-group">
          <label for="confirmPassword">Confirmar contraseña</label>
          <div class="input-group">
            <input
                id="confirmPassword"
                v-model="confirmPassword"
                type="password"
                placeholder="••••••••"
                required
            >
            <i class="fas fa-lock input-icon" />
          </div>
        </div>

        <div class="form-group">
          <label class="checkbox-container">
            <input
                v-model="termsAccepted"
                type="checkbox"
                required
            >
            <span>Acepto los <a href="#" class="auth-link">Términos y Condiciones</a></span>
          </label>
        </div>

        <button type="submit" class="btn-auth btn-primary" :disabled="loading">
          <span v-if="!loading">Registrarse</span>
          <span v-else><i class="fas fa-spinner fa-spin" /> Cargando...</span>
        </button>

        <div class="auth-footer">
          ¿Ya tienes una cuenta? <a
            class="auth-link"
            ref="loginBtn"
            href="/auth/login">
          Inicia sesión</a>
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
      name: '',
      email: '',
      password: '',
      confirmPassword: '',
      termsAccepted: false,
      showPassword: false,
      loading: false
    }
  },
  methods: {
    register() {
      if (this.password !== this.confirmPassword) {
        alert('Las contraseñas no coinciden')
        return
      }

      this.loading = true
      setTimeout(() => {
        this.loading = false
        alert(`Registro exitoso para ${this.email}`)
        this.$emit('toggle-view', 'LoginForm')
      }, 1500)
    }
  }
}
</script>