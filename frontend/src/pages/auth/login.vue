<template>
  <AuthLayout v-slot="{ darkMode, toggleDarkMode }">
    <div class="auth-card">
      <button class="toggle-dark" @click="toggleDarkMode">
        <i :class="darkMode ? 'fas fa-sun' : 'fas fa-moon'" />
      </button>

      <div class="text-center mb-6">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 80 80" width="160" height="160">
          <!-- Cuerpo de la cámara -->
          <rect x="15" y="20" width="50" height="40" rx="5" :fill="darkMode ? '#a78bfa' : '#7e5bef'" />

          <!-- Lente -->
          <circle cx="50" cy="40" r="15" fill="#4C1D95" />
          <circle cx="50" cy="40" r="10" fill="#1E293B" />
          <circle cx="50" cy="40" r="5" fill="#94A3B8" />

          <!-- Visor -->
          <rect x="25" y="25" width="10" height="8" rx="2" fill="#1E293B" />

          <!-- Flash -->
          <circle cx="65" cy="30" r="4" fill="#FBBF24" />

          <!-- Botón -->
          <circle cx="65" cy="50" r="3" fill="#1E293B" />

          <!-- Detalle inferior -->
          <rect x="30" y="60" width="20" height="3" rx="1" fill="#1E293B" />
        </svg>

        <h2 class="auth-title">
          Bienvenido a <span class="text-purple">MovieTrip</span>
        </h2>
        <p class="auth-subtitle">
          Ingresa tus credenciales para continuar
        </p>
      </div>

      <form class="auth-form" @submit.prevent="login">
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
          <div class="text-right mt-1">
            <a
                class="auth-link"
                ref="loginBtn"
                href="/auth/forgot-password"
            >¿Olvidaste tu contraseña?</a>
          </div>
        </div>

        <button type="submit" class="btn-auth btn-primary" :disabled="loading">
          <span v-if="!loading">Iniciar Sesión</span>
          <span v-else><i class="fas fa-spinner fa-spin" /> Cargando...</span>
        </button>

        <div class="divider">
          <span class="divider-text">o continúa con</span>
        </div>

        <div class="social-auth">
          <button type="button" class="btn-social btn-google">
            <i class="fab fa-google" /> Google
          </button>
          <button type="button" class="btn-social btn-facebook">
            <i class="fab fa-facebook-f" /> Facebook
          </button>
        </div>

        <div class="auth-footer">
          ¿No tienes una cuenta? <a
            class="auth-link"
            ref="loginBtn"
            href="/auth/register"
        >Regístrate</a>
        </div>
      </form>
    </div>
  </AuthLayout>
</template>

<script>
import AuthLayout from '@/layouts/AuthLayout.vue'

export default {
  components: {
    AuthLayout
  },
  data() {
    return {
      email: '',
      password: '',
      showPassword: false,
      loading: false
    }
  },
  methods: {
    login() {
      this.loading = true
      setTimeout(() => {
        this.loading = false
        alert(`Bienvenido ${this.email}`)
      }, 1500)
    }
  }
}
</script>