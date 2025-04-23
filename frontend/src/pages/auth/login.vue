<template>
  <AuthLayout v-slot="{ darkMode, toggleDarkMode }">
    <div class="login-container">
      <div class="login-card">
        <!-- Theme toggle -->
        <button class="theme-toggle" @click="toggleDarkMode" :aria-label="darkMode ? 'Switch to light mode' : 'Switch to dark mode'">
          <i :class="darkMode ? 'fas fa-sun' : 'fas fa-moon'" />
        </button>

        <!-- Logo and welcome -->
        <div class="login-header">
          <div class="logo">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="48" height="48">
              <path fill="currentColor" d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V5h14v14z"/>
              <path fill="currentColor" d="M12 17c1.1 0 2-.9 2-2V9c0-1.1-.9-2-2-2s-2 .9-2 2v6c0 1.1.9 2 2 2z"/>
            </svg>
            <h1>Movie<span>Trip</span></h1>
          </div>
          <h2>Inicia sesión en tu cuenta</h2>
          <p>Gestiona tus películas y listas favoritas</p>
        </div>

        <!-- Login form -->
        <form @submit.prevent="handleLogin" class="login-form">
          <!-- Email field -->
          <div class="form-group">
            <label for="email">Correo electrónico</label>
            <div class="input-group">
              <input
                  id="email"
                  v-model="email"
                  type="email"
                  placeholder="tu@email.com"
                  required
                  autocomplete="username"
              >
              <i class="fas fa-envelope input-icon" />
            </div>
          </div>

          <!-- Password field -->
          <div class="form-group">
            <div class="password-header">
              <label for="password">Contraseña</label>
              <a href="/auth/forgot-password" class="forgot-password">¿Olvidaste tu contraseña?</a>
            </div>
            <div class="input-group">
              <input
                  id="password"
                  v-model="password"
                  :type="showPassword ? 'text' : 'password'"
                  placeholder="••••••••"
                  required
                  autocomplete="current-password"
              >
              <button
                  type="button"
                  class="password-toggle"
                  @click="showPassword = !showPassword"
                  :aria-label="showPassword ? 'Hide password' : 'Show password'"
              >
                <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'" />
              </button>
            </div>
          </div>

          <!-- Remember me checkbox -->
          <div class="remember-me">
            <input type="checkbox" id="remember" v-model="rememberMe">
            <label for="remember">Mantener sesión iniciada</label>
          </div>

          <!-- Submit button -->
          <button type="submit" class="login-button" :disabled="loading">
            <span v-if="!loading">Iniciar sesión</span>
            <span v-else><i class="fas fa-spinner fa-spin" /> Procesando...</span>
          </button>

          <!-- Divider -->
          <div class="divider">
            <span>o continúa con</span>
          </div>

          <!-- Social login -->
          <div class="social-login">
            <button type="button" class="social-button google">
              <i class="fab fa-google" /> Google
            </button>
            <button type="button" class="social-button facebook">
              <i class="fab fa-facebook-f" /> Facebook
            </button>
          </div>

          <!-- Sign up link -->
          <div class="signup-link">
            ¿No tienes una cuenta? <a href="/auth/register">Regístrate</a>
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
      password: '',
      showPassword: false,
      rememberMe: false,
      loading: false
    }
  },
  methods: {
    async handleLogin() {
      this.loading = true;
      try {
        const response = await fetch('http://localhost:8080/api/v1/auth/signin', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            email: this.email,
            password: this.password
          })
        });

        if (!response.ok) {
          const errorData = await response.json();
          throw new Error(errorData.message || 'Credenciales inválidas');
        }

        const data = await response.json();
        const token = data.token; // Asegúrate que esto coincide con la estructura de tu JwtAuthenticationResponse

        // Guardar el token en localStorage
        localStorage.setItem('jwt', token);

        // Redirigir al dashboard
        this.$router.push('/myprofile');
      } catch (error) {
        console.error('Error en inicio de sesión:', error);
        alert(error.message || 'Inicio de sesión fallido. Verifica tus credenciales.');
      } finally {
        this.loading = false;
      }
    }  }
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

.password-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.forgot-password {
  font-size: 0.8125rem;
  color: #7e5bef;
  text-decoration: none;
  transition: color 0.2s ease;
}

.dark-mode .forgot-password {
  color: #a78bfa;
}

.forgot-password:hover {
  text-decoration: underline;
}

.password-toggle {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #94a3b8;
  cursor: pointer;
  padding: 0.25rem;
}

.password-toggle:hover {
  color: #64748b;
}

.dark-mode .password-toggle:hover {
  color: #cbd5e1;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.875rem;
  color: #64748b;
}

.dark-mode .remember-me {
  color: #94a3b8;
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

.divider {
  display: flex;
  align-items: center;
  color: #94a3b8;
  font-size: 0.8125rem;
  margin: 1rem 0;
}

.divider::before,
.divider::after {
  content: "";
  flex: 1;
  border-bottom: 1px solid #e2e8f0;
}

.dark-mode .divider::before,
.dark-mode .divider::after {
  border-bottom-color: #334155;
}

.divider span {
  padding: 0 1rem;
}

.social-login {
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.social-button {
  flex: 1;
  padding: 0.75rem;
  border-radius: 8px;
  font-weight: 500;
  font-size: 0.875rem;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  transition: all 0.2s ease;
  border: 1px solid #e2e8f0;
  background-color: white;
  color: #64748b;
}

.dark-mode .social-button {
  background-color: #1e293b;
  border-color: #334155;
  color: #e2e8f0;
}

.social-button.google:hover {
  background-color: #f8fafc;
  border-color: #cbd5e1;
}

.dark-mode .social-button.google:hover {
  background-color: #334155;
}

.social-button.facebook {
  background-color: #3b5998;
  color: white;
  border: none;
}

.social-button.facebook:hover {
  background-color: #344e86;
}

.signup-link {
  text-align: center;
  font-size: 0.875rem;
  color: #64748b;
}

.dark-mode .signup-link {
  color: #94a3b8;
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

  .social-login {
    flex-direction: column;
  }
}
</style>