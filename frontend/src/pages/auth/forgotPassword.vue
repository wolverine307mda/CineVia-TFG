<template>
  <AuthLayout v-slot="{ darkMode, toggleDarkMode }">
    <div class="password-reset-container">
      <div class="password-reset-card" :class="{ 'dark-mode': darkMode }">
        <!-- Theme toggle -->
        <button class="theme-toggle" @click="toggleDarkMode" :aria-label="darkMode ? 'Switch to light mode' : 'Switch to dark mode'">
          <i :class="darkMode ? 'fas fa-sun' : 'fas fa-moon'" />
        </button>

        <!-- Logo and header -->
        <div class="header">
          <div class="logo">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="48" height="48">
              <path fill="currentColor" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z"/>
              <path fill="currentColor" d="M12 6c-3.31 0-6 2.69-6 6s2.69 6 6 6 6-2.69 6-6-2.69-6-6-6zm0 10c-2.21 0-4-1.79-4-4s1.79-4 4-4 4 1.79 4 4-1.79 4-4 4z"/>
              <path fill="currentColor" d="M12 8c-2.21 0-4 1.79-4 4s1.79 4 4 4 4-1.79 4-4-1.79-4-4-4zm0 6c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2z"/>
            </svg>
            <h1>Movie<span>Trip</span></h1>
          </div>
          <h2>Recuperar contraseña</h2>
          <p v-if="currentStep === 1">Ingresa tu email para recibir un PIN de verificación</p>
          <p v-if="currentStep === 2">Ingresa el PIN de 6 dígitos que enviamos a tu email</p>
          <p v-if="currentStep === 3">Crea una nueva contraseña</p>
        </div>

        <!-- Stepper -->
        <div class="stepper">
          <div class="step" :class="{ 'active': currentStep === 1, 'completed': currentStep > 1 }">
            <div class="step-number">1</div>
            <div class="step-label">Verificar email</div>
          </div>
          <div class="step-line" :class="{ 'active': currentStep > 1 }"></div>
          <div class="step" :class="{ 'active': currentStep === 2, 'completed': currentStep > 2 }">
            <div class="step-number">2</div>
            <div class="step-label">Validar PIN</div>
          </div>
          <div class="step-line" :class="{ 'active': currentStep > 2 }"></div>
          <div class="step" :class="{ 'active': currentStep === 3 }">
            <div class="step-number">3</div>
            <div class="step-label">Nueva contraseña</div>
          </div>
        </div>

        <!-- Step 1: Email verification -->
        <form v-if="currentStep === 1" @submit.prevent="requestReset" class="form">
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
                  :class="{ 'dark-input': darkMode }"
              >
              <i class="fas fa-envelope input-icon" />
            </div>
          </div>

          <button type="submit" class="submit-button" :disabled="loading">
            <span v-if="!loading">Enviar PIN</span>
            <span v-else><i class="fas fa-spinner fa-spin" /> Enviando...</span>
          </button>

          <div class="back-to-login">
            <router-link to="/auth/login" class="back-link">
              <i class="fas fa-arrow-left" /> Volver al inicio de sesión
            </router-link>
          </div>
        </form>

        <!-- Step 2: PIN verification -->
        <form v-if="currentStep === 2" @submit.prevent="verifyPin" class="form">
          <div class="form-group">
            <label for="pin">PIN de 6 dígitos</label>
            <div class="input-group">
              <input
                  id="pin"
                  v-model="pin"
                  type="text"
                  placeholder="123456"
                  maxlength="6"
                  pattern="\d{6}"
                  required
                  :class="{ 'dark-input': darkMode }"
              >
              <i class="fas fa-key input-icon" />
            </div>
            <p class="hint">Hemos enviado un PIN de 6 dígitos a {{ email }}</p>
          </div>

          <button type="submit" class="submit-button" :disabled="loading">
            <span v-if="!loading">Verificar PIN</span>
            <span v-else><i class="fas fa-spinner fa-spin" /> Verificando...</span>
          </button>

          <div class="resend-pin">
            <p>¿No recibiste el PIN? <a href="#" @click.prevent="resendPin">Reenviar PIN</a></p>
          </div>
        </form>

        <!-- Step 3: New password -->
        <form v-if="currentStep === 3" @submit.prevent="resetPassword" class="form">
          <div class="form-group">
            <label for="newPassword">Nueva contraseña</label>
            <div class="input-group">
              <input
                  id="newPassword"
                  v-model="newPassword"
                  :type="showPassword ? 'text' : 'password'"
                  placeholder="Nueva contraseña"
                  required
                  minlength="6"
                  :class="{ 'dark-input': darkMode }"
              >
              <i class="fas fa-lock input-icon" />
              <button type="button" class="password-toggle" @click="showPassword = !showPassword">
                <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'" />
              </button>
            </div>
          </div>

          <div class="form-group">
            <label for="confirmPassword">Confirmar contraseña</label>
            <div class="input-group">
              <input
                  id="confirmPassword"
                  v-model="confirmPassword"
                  :type="showConfirmPassword ? 'text' : 'password'"
                  placeholder="Confirmar contraseña"
                  required
                  minlength="6"
                  :class="{ 'dark-input': darkMode }"
              >
              <i class="fas fa-lock input-icon" />
              <button type="button" class="password-toggle" @click="showConfirmPassword = !showConfirmPassword">
                <i :class="showConfirmPassword ? 'fas fa-eye-slash' : 'fas fa-eye'" />
              </button>
            </div>
          </div>

          <div class="password-requirements">
            <p>La contraseña debe tener al menos:</p>
            <ul>
              <li :class="{ 'valid': newPassword.length >= 6 }">6 caracteres</li>
            </ul>
          </div>

          <button type="submit" class="submit-button" :disabled="loading || !passwordsMatch">
            <span v-if="!loading">Cambiar contraseña</span>
            <span v-else><i class="fas fa-spinner fa-spin" /> Cambiando...</span>
          </button>
        </form>
      </div>
    </div>
  </AuthLayout>
</template>

<script>
import AuthLayout from '@/layouts/AuthLayout.vue'
import authService from '@/services/auth.service.js'
import toast from '@/utils/toast'

export default {
  components: {
    AuthLayout
  },
  data() {
    return {
      currentStep: 1,
      email: '',
      pin: '',
      newPassword: '',
      confirmPassword: '',
      showPassword: false,
      showConfirmPassword: false,
      loading: false
    }
  },
  computed: {
    passwordsMatch() {
      return this.newPassword === this.confirmPassword && this.newPassword.length >= 6
    }
  },
  methods: {
    async requestReset() {
      this.loading = true
      try {
        const response = await authService.requestPasswordReset(this.email)
        if (response.success) {
          toast.success(response.message)
          this.currentStep = 2
        } else {
          toast.error(response.message)
        }
      } catch (error) {
        toast.error(error.message || 'Error al solicitar el restablecimiento')
      } finally {
        this.loading = false
      }
    },
    async verifyPin() {
      this.loading = true
      try {
        const response = await authService.verifyResetPin(this.email, this.pin)
        if (response.success) {
          toast.success(response.message)
          this.currentStep = 3
        } else {
          toast.error(response.message)
        }
      } catch (error) {
        toast.error(error.message || 'Error al verificar el PIN')
      } finally {
        this.loading = false
      }
    },
    async resetPassword() {
      if (!this.passwordsMatch) {
        toast.error('Las contraseñas no coinciden')
        return
      }

      this.loading = true
      try {
        const response = await authService.resetPassword({
          email: this.email,
          pin: this.pin,
          newPassword: this.newPassword,
          confirmPassword: this.confirmPassword
        })

        if (response.success) {
          toast.success(response.message)
          setTimeout(() => {
            this.$router.push('/auth/login')
          }, 2000)
        } else {
          toast.error(response.message)
        }
      } catch (error) {
        toast.error(error.message || 'Error al cambiar la contraseña')
      } finally {
        this.loading = false
      }
    },
    async resendPin() {
      this.loading = true
      try {
        const response = await authService.requestPasswordReset(this.email)
        if (response.success) {
          toast.success('Se ha reenviado el PIN a tu correo electrónico')
        } else {
          toast.error(response.message)
        }
      } catch (error) {
        toast.error(error.message || 'Error al reenviar el PIN')
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.password-reset-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 2rem;
  transition: background-color 0.3s ease;
}

.password-reset-card {
  width: 100%;
  max-width: 480px;
  padding: 3rem;
  background-color: white;
  border-radius: 1rem;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  position: relative;
  transition: all 0.3s ease;
}

.password-reset-card.dark-mode {
  background-color: #1e293b;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.3);
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

.header {
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

.header h2 {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #1e293b;
}

.dark-mode .header h2 {
  color: #f8fafc;
}

.header p {
  color: #64748b;
  margin: 0;
}

.dark-mode .header p {
  color: #94a3b8;
}

.stepper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  position: relative;
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  z-index: 1;
}

.step-number {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #e2e8f0;
  color: #64748b;
  display: flex;
  justify-content: center;
  align-items: center;
  font-weight: 600;
  margin-bottom: 0.5rem;
  transition: all 0.3s ease;
}

.dark-mode .step-number {
  background-color: #334155;
  color: #94a3b8;
}

.step.active .step-number {
  background-color: #7e5bef;
  color: white;
}

.dark-mode .step.active .step-number {
  background-color: #a78bfa;
}

.step.completed .step-number {
  background-color: #10b981;
  color: white;
}

.step-label {
  font-size: 0.75rem;
  color: #64748b;
  text-align: center;
}

.dark-mode .step-label {
  color: #94a3b8;
}

.step.active .step-label {
  color: #7e5bef;
  font-weight: 500;
}

.dark-mode .step.active .step-label {
  color: #a78bfa;
}

.step.completed .step-label {
  color: #10b981;
}

.step-line {
  position: absolute;
  height: 2px;
  background-color: #e2e8f0;
  top: 16px;
  left: 0;
  right: 0;
  z-index: 0;
}

.dark-mode .step-line {
  background-color: #334155;
}

.step-line.active {
  background-color: #7e5bef;
}

.dark-mode .step-line.active {
  background-color: #a78bfa;
}

.form {
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
  border-radius: 0.5rem;
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

.password-toggle {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #64748b;
  cursor: pointer;
  z-index: 2;
}

.dark-mode .password-toggle {
  color: #94a3b8;
}

.hint {
  font-size: 0.875rem;
  color: #64748b;
  margin-top: 0.5rem;
}

.dark-mode .hint {
  color: #94a3b8;
}

.submit-button {
  width: 100%;
  padding: 0.875rem;
  border-radius: 0.5rem;
  background-color: #7e5bef;
  color: white;
  font-weight: 600;
  border: none;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.9375rem;
}

.dark-mode .submit-button {
  background-color: #a78bfa;
}

.submit-button:hover {
  background-color: #6d46e8;
  transform: translateY(-1px);
}

.dark-mode .submit-button:hover {
  background-color: #8b5cf6;
}

.submit-button:disabled {
  background-color: #cbd5e1;
  transform: none;
  cursor: not-allowed;
}

.dark-mode .submit-button:disabled {
  background-color: #475569;
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

.resend-pin {
  text-align: center;
  margin-top: 1rem;
  font-size: 0.875rem;
  color: #64748b;
}

.dark-mode .resend-pin {
  color: #94a3b8;
}

.resend-pin a {
  color: #7e5bef;
  text-decoration: none;
}

.dark-mode .resend-pin a {
  color: #a78bfa;
}

.resend-pin a:hover {
  text-decoration: underline;
}

.password-requirements {
  margin: 1rem 0;
  padding: 0.75rem;
  background-color: #f8fafc;
  border-radius: 0.5rem;
  font-size: 0.875rem;
}

.dark-mode .password-requirements {
  background-color: #1e293b;
}

.password-requirements p {
  margin-bottom: 0.5rem;
  color: #64748b;
}

.dark-mode .password-requirements p {
  color: #94a3b8;
}

.password-requirements ul {
  list-style-type: none;
  padding-left: 1rem;
  margin: 0;
}

.password-requirements li {
  position: relative;
  padding-left: 1.5rem;
  margin-bottom: 0.25rem;
  color: #64748b;
}

.dark-mode .password-requirements li {
  color: #94a3b8;
}

.password-requirements li:before {
  content: "✗";
  position: absolute;
  left: 0;
  color: #ef4444;
}

.password-requirements li.valid:before {
  content: "✓";
  color: #10b981;
}

@media (max-width: 640px) {
  .password-reset-card {
    padding: 2rem 1.5rem;
  }

  .header h2 {
    font-size: 1.25rem;
  }

  .logo h1 {
    font-size: 1.5rem;
  }

  .logo svg {
    width: 36px;
    height: 36px;
  }
}
</style>