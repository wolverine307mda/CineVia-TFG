<template>
  <AuthLayout v-slot="{ darkMode, toggleDarkMode }">
    <div class="login-container">
      <div class="login-card">
        <!-- Theme toggle -->
        <button class="theme-toggle" @click="toggleDarkMode" :aria-label="darkMode ? 'Switch to light mode' : 'Switch to dark mode'">
          <i :class="darkMode ? 'fas fa-sun' : 'fas fa-moon'" />
        </button>

        <!-- Logo and header -->
        <div class="login-header">
          <div class="logo">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="48" height="48">
              <path fill="currentColor" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z"/>
              <path fill="currentColor" d="M12 6c-3.31 0-6 2.69-6 6s2.69 6 6 6 6-2.69 6-6-2.69-6-6-6zm0 10c-2.21 0-4-1.79-4-4s1.79-4 4-4 4 1.79 4 4-1.79 4-4 4z"/>
              <path fill="currentColor" d="M12 8c-2.21 0-4 1.79-4 4s1.79 4 4 4 4-1.79 4-4-1.79-4-4-4zm0 6c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2z"/>
            </svg>
            <h1>Movie<span>Trip</span></h1>
          </div>
          <h2>Recuperar contraseña</h2>
          <p>Ingresa tu email para recibir instrucciones</p>
        </div>

        <!-- Reset form -->
        <form @submit.prevent="handleResetRequest" class="login-form">
          <div class="form-group">
            <label for="email">Correo electrónico</label>
            <div class="input-group">
              <input
                  id="email"
                  v-model="email"
                  type="email"
                  placeholder="tu@email.com"
                  required
                  autocomplete="email"
              >
              <i class="fas fa-envelope input-icon" />
            </div>
          </div>

          <button type="submit" class="login-button" :disabled="loading">
            <span v-if="!loading">Enviar enlace de recuperación</span>
            <span v-else><i class="fas fa-spinner fa-spin" /> Enviando...</span>
          </button>

          <div class="back-to-login">
            <a href="/auth/login" class="back-link">
              <i class="fas fa-arrow-left" /> Volver al inicio de sesión
            </a>
          </div>
        </form>
      </div>
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
      loading: false
    }
  },
  methods: {
    handleResetRequest() {
      this.loading = true;

      // Simulate API call
      setTimeout(() => {
        this.loading = false;
        alert(`Se ha enviado un enlace de recuperación a ${this.email}`);
      }, 1500);
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  padding: 2rem;
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  padding: 3rem;
  width: 100%;
  max-width: 480px;
  position: relative;
  transition: all 0.3s ease;
}

.dark-mode .login-card {
  background: rgba(15, 23, 42, 0.95);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
}

.theme-toggle {
  position: absolute;
  top: 1.5rem;
  right: 1.5rem;
  background: none;
  border: none;
  color: #64748b;
  cursor: pointer;
  font-size: 1.25rem;
  transition: all 0.3s ease;
  padding: 0.5rem;
  border-radius: 50%;
}

.theme-toggle:hover {
  background: rgba(0, 0, 0, 0.05);
  transform: scale(1.1);
}

.dark-mode .theme-toggle {
  color: #a78bfa;
}

.dark-mode .theme-toggle:hover {
  background: rgba(255, 255, 255, 0.05);
}

.login-header {
  text-align: center;
  margin-bottom: 2.5rem;
}

.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
}

.logo svg {
  color: #7e5bef;
}

.dark-mode .logo svg {
  color: #a78bfa;
}

.logo h1 {
  font-size: 1.75rem;
  font-weight: 700;
  color: #1e293b;
  margin: 0;
}

.dark-mode .logo h1 {
  color: #f8fafc;
}

.logo span {
  color: #7e5bef;
}

.dark-mode .logo span {
  color: #a78bfa;
}

.login-header h2 {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #1e293b;
}

.dark-mode .login-header h2 {
  color: #f8fafc;
}

.login-header p {
  color: #64748b;
  margin: 0;
}

.dark-mode .login-header p {
  color: #94a3b8;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  font-size: 0.875rem;
  font-weight: 500;
  color: #1e293b;
}

.dark-mode .form-group label {
  color: #e2e8f0;
}

.input-group {
  position: relative;
}

.input-group input {
  width: 100%;
  padding: 0.875rem 2.5rem 0.875rem 1rem;
  border: 1px solid #e2e8f0;
  border-radius: 8px !important;
  font-size: 0.9375rem;
  transition: all 0.2s ease;
  background-color: white;
  color: #1e293b;
}

.dark-mode .input-group input {
  background-color: #1e293b;
  border-color: #334155;
  color: #f8fafc;
}

.input-group input:focus {
  outline: none;
  border-color: #7e5bef;
  box-shadow: 0 0 0 3px rgba(126, 91, 239, 0.2);
}

.dark-mode .input-group input:focus {
  border-color: #a78bfa;
  box-shadow: 0 0 0 3px rgba(167, 139, 250, 0.3);
}

.input-icon {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  color: #94a3b8;
  pointer-events: none;
}
.remember-me input {
  width: 1rem;
  height: 1rem;
  accent-color: #7e5bef;
}

.dark-mode .remember-me input {
  accent-color: #a78bfa;
}

.login-button {
  width: 100%;
  padding: 0.875rem;
  border-radius: 8px;
  background-color: #7e5bef;
  color: white;
  font-weight: 600;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.9375rem;
}

.dark-mode .login-button {
  background-color: #a78bfa;
}

.login-button:hover {
  background-color: #6d46e8;
  transform: translateY(-1px);
}

.dark-mode .login-button:hover {
  background-color: #8b5cf6;
}

.login-button:disabled {
  background-color: #cbd5e1;
  transform: none;
  cursor: not-allowed;
}

.dark-mode .login-button:disabled {
  background-color: #475569;
}


.divider span {
  padding: 0 1rem;
}

.signup-link a {
  color: #7e5bef;
  text-decoration: none;
  font-weight: 500;
}

.dark-mode .signup-link a {
  color: #a78bfa;
}

.signup-link a:hover {
  text-decoration: underline;
}

@media (max-width: 640px) {
  .login-card {
    padding: 2rem 1.5rem;
  }
}

.login-header {
  margin-bottom: 2rem;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.login-button {
  margin-top: 0.5rem;
}

.back-to-login {
  text-align: center;
  margin-top: 1.5rem;
}

.back-link {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  color: #7e5bef;
  text-decoration: none;
  font-size: 0.875rem;
  font-weight: 500;
  transition: all 0.2s ease;
}

.dark-mode .back-link {
  color: #a78bfa;
}

.back-link:hover {
  text-decoration: underline;
}

.back-link i {
  font-size: 0.75rem;
}

.login-header svg {
  color: #7e5bef;
}

.dark-mode .login-header svg {
  color: #a78bfa;
}
</style>