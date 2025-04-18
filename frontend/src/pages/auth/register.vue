<template>
  <AuthLayout v-slot="{ darkMode, toggleDarkMode }">
    <div class="register-container">
      <div class="register-card">
        <!-- Theme toggle -->
        <button class="theme-toggle" @click="toggleDarkMode" :aria-label="darkMode ? 'Switch to light mode' : 'Switch to dark mode'">
          <i :class="darkMode ? 'fas fa-sun' : 'fas fa-moon'" />
        </button>

        <!-- Logo and welcome -->
        <div class="register-header">
          <div class="logo">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="48" height="48">
              <path fill="currentColor" d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm0 16H5V5h14v14z"/>
              <path fill="currentColor" d="M12 17c1.1 0 2-.9 2-2V9c0-1.1-.9-2-2-2s-2 .9-2 2v6c0 1.1.9 2 2 2z"/>
            </svg>
            <h1>Movie<span>Trip</span></h1>
          </div>
          <h2>Crea tu cuenta</h2>
          <p>Completa los siguientes pasos para registrarte</p>
        </div>

        <!-- Stepper -->
        <div class="stepper">
          <div
              v-for="(step, index) in steps"
              :key="index"
              :class="['step', { 'active': currentStep === index, 'completed': currentStep > index }]"
          >
            <div class="step-number">{{ index + 1 }}</div>
            <div class="step-label">{{ step.label }}</div>
            <div class="step-connector" v-if="index < steps.length - 1"></div>
          </div>
        </div>

        <!-- Step 1: Personal Information -->
        <form @submit.prevent="nextStep" class="register-form" v-show="currentStep === 0">
          <div class="form-row">
            <div class="form-group">
              <label for="firstName">Nombre</label>
              <div class="input-group">
                <input
                    id="firstName"
                    v-model="formData.firstName"
                    type="text"
                    placeholder="Ej: Juan"
                    required
                    autocomplete="given-name"
                >
                <i class="fas fa-user input-icon" />
              </div>
            </div>

            <div class="form-group">
              <label for="lastName">Apellidos</label>
              <div class="input-group">
                <input
                    id="lastName"
                    v-model="formData.lastName"
                    type="text"
                    placeholder="Ej: Pérez García"
                    required
                    autocomplete="family-name"
                >
                <i class="fas fa-user input-icon" />
              </div>
            </div>
          </div>

          <div class="form-group">
            <label for="username">Nombre de usuario</label>
            <div class="input-group">
              <input
                  id="username"
                  v-model="formData.username"
                  type="text"
                  placeholder="Ej: juanpg"
                  required
                  autocomplete="username"
              >
              <i class="fas fa-at input-icon" />
            </div>
            <div class="input-hint">Este será tu identificador único</div>
          </div>

          <div class="form-actions">
            <button type="submit" class="step-button next">
              Siguiente <i class="fas fa-arrow-right" />
            </button>
          </div>
        </form>

        <!-- Step 2: Contact Information -->
        <form @submit.prevent="nextStep" class="register-form" v-show="currentStep === 1">
          <div class="form-group">
            <label for="email">Correo electrónico</label>
            <div class="input-group">
              <input
                  id="email"
                  v-model="formData.email"
                  type="email"
                  placeholder="tu@email.com"
                  required
                  autocomplete="email"
              >
              <i class="fas fa-envelope input-icon" />
            </div>
          </div>

          <div class="form-group">
            <label for="phone">Teléfono</label>
            <div class="input-group">
              <input
                  id="phone"
                  v-model="formData.phone"
                  type="tel"
                  placeholder="Ej: +34 123 456 789"
                  required
                  autocomplete="tel"
              >
              <i class="fas fa-phone input-icon" />
            </div>
          </div>

          <div class="form-actions">
            <button type="button" class="step-button back" @click="prevStep">
              <i class="fas fa-arrow-left" /> Anterior
            </button>
            <button type="submit" class="step-button next">
              Siguiente <i class="fas fa-arrow-right" />
            </button>
          </div>
        </form>

        <!-- Step 3: Security -->
        <form @submit.prevent="handleRegister" class="register-form" v-show="currentStep === 2">
          <div class="form-group">
            <label for="password">Contraseña</label>
            <div class="input-group">
              <input
                  id="password"
                  v-model="formData.password"
                  :type="showPassword ? 'text' : 'password'"
                  placeholder="••••••••"
                  required
                  autocomplete="new-password"
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
            <div class="password-strength" :class="passwordStrengthClass">
              Seguridad: {{ passwordStrength }}
            </div>
          </div>

          <div class="form-group">
            <label for="confirmPassword">Confirmar contraseña</label>
            <div class="input-group">
              <input
                  id="confirmPassword"
                  v-model="formData.confirmPassword"
                  type="password"
                  placeholder="••••••••"
                  required
                  autocomplete="new-password"
              >
              <i class="fas fa-lock input-icon" />
            </div>
            <div class="input-error" v-if="passwordMismatch">
              Las contraseñas no coinciden
            </div>
          </div>

          <div class="terms-container">
            <input type="checkbox" id="terms" v-model="formData.termsAccepted" required>
            <label for="terms">
              Acepto los <a href="/terms" class="terms-link">Términos y Condiciones</a> y la
              <a href="/privacy" class="terms-link">Política de Privacidad</a>
            </label>
          </div>

          <div class="form-actions">
            <button type="button" class="step-button back" @click="prevStep">
              <i class="fas fa-arrow-left" /> Anterior
            </button>
            <button type="submit" class="register-button" :disabled="loading || !formData.termsAccepted || passwordMismatch">
              <span v-if="!loading">Completar registro</span>
              <span v-else><i class="fas fa-spinner fa-spin" /> Procesando...</span>
            </button>
          </div>
        </form>

        <!-- Login link -->
        <div class="auth-footer">
          ¿Ya tienes una cuenta? <a href="/auth/login" class="auth-link">Inicia sesión</a>
        </div>
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
      currentStep: 0,
      steps: [
        { label: 'Información personal' },
        { label: 'Contacto' },
        { label: 'Seguridad' }
      ],
      formData: {
        firstName: '',
        lastName: '',
        username: '',
        email: '',
        phone: '',
        password: '',
        confirmPassword: '',
        termsAccepted: false
      },
      showPassword: false,
      loading: false
    }
  },
  computed: {
    passwordMismatch() {
      return this.formData.password && this.formData.confirmPassword &&
          this.formData.password !== this.formData.confirmPassword;
    },
    passwordStrength() {
      if (!this.formData.password) return 'Débil';
      if (this.formData.password.length < 6) return 'Débil';
      if (this.formData.password.length < 8) return 'Media';
      if (!/[A-Z]/.test(this.formData.password)) return 'Media';
      if (!/[0-9]/.test(this.formData.password)) return 'Media';
      if (!/[^A-Za-z0-9]/.test(this.formData.password)) return 'Fuerte';
      return 'Muy fuerte';
    },
    passwordStrengthClass() {
      return {
        'weak': this.passwordStrength === 'Débil',
        'medium': this.passwordStrength === 'Media',
        'strong': this.passwordStrength === 'Fuerte',
        'very-strong': this.passwordStrength === 'Muy fuerte'
      };
    }
  },
  methods: {
    nextStep() {
      if (this.currentStep < this.steps.length - 1) {
        this.currentStep++;
      }
    },
    prevStep() {
      if (this.currentStep > 0) {
        this.currentStep--;
      }
    },
    handleRegister() {
      if (this.passwordMismatch) {
        return;
      }

      this.loading = true;

      // Simulate API call
      setTimeout(() => {
        this.loading = false;
        console.log('Registration data:', this.formData);
        alert(`Registro exitoso para ${this.formData.email}`);
      }, 1500);
    }
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  padding: 2rem;
}

.register-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  padding: 3rem;
  width: 100%;
  max-width: 520px;
  position: relative;
  transition: all 0.3s ease;
}

.dark-mode .register-card {
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

.register-header {
  text-align: center;
  margin-bottom: 2rem;
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

.register-header h2 {
  font-size: 1.5rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
  color: #1e293b;
}

.dark-mode .register-header h2 {
  color: #f8fafc;
}

.register-header p {
  color: #64748b;
  margin: 0;
}

.dark-mode .register-header p {
  color: #94a3b8;
}

/* Stepper styles */
.stepper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2.5rem;
  position: relative;
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  flex: 1;
}

.step-number {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: #e2e8f0;
  color: #64748b;
  display: flex;
  align-items: center;
  justify-content: center;
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
  font-weight: 500;
  color: #64748b;
  text-align: center;
  transition: all 0.3s ease;
}

.dark-mode .step-label {
  color: #94a3b8;
}

.step.active .step-label {
  color: #7e5bef;
  font-weight: 600;
}

.dark-mode .step.active .step-label {
  color: #a78bfa;
}

.step.completed .step-label {
  color: #10b981;
}

.step-connector {
  position: absolute;
  top: 16px;
  left: calc(50% + 16px);
  right: calc(-50% + 16px);
  height: 2px;
  background-color: #e2e8f0;
}

.dark-mode .step-connector {
  background-color: #334155;
}

.step.completed .step-connector {
  background-color: #10b981;
}

/* Form styles */
.register-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-row {
  display: flex;
  gap: 1rem;
}

.form-row .form-group {
  flex: 1;
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

.input-hint {
  font-size: 0.75rem;
  color: #64748b;
  margin-top: 0.25rem;
}

.dark-mode .input-hint {
  color: #94a3b8;
}

.input-error {
  font-size: 0.75rem;
  color: #ef4444;
  margin-top: 0.25rem;
}

.password-strength {
  font-size: 0.75rem;
  margin-top: 0.25rem;
  font-weight: 500;
}

.password-strength.weak {
  color: #ef4444;
}

.password-strength.medium {
  color: #f59e0b;
}

.password-strength.strong {
  color: #10b981;
}

.password-strength.very-strong {
  color: #3b82f6;
}

/* Form actions */
.form-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 1.5rem;
}

.step-button {
  padding: 0.75rem 1.25rem;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.9375rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.step-button.back {
  background-color: #e2e8f0;
  color: #64748b;
  border: none;
}

.dark-mode .step-button.back {
  background-color: #334155;
  color: #e2e8f0;
}

.step-button.back:hover {
  background-color: #cbd5e1;
}

.dark-mode .step-button.back:hover {
  background-color: #475569;
}

.step-button.next {
  background-color: #7e5bef;
  color: white;
  border: none;
}

.dark-mode .step-button.next {
  background-color: #a78bfa;
}

.step-button.next:hover {
  background-color: #6d46e8;
  transform: translateY(-1px);
}

.dark-mode .step-button.next:hover {
  background-color: #8b5cf6;
}

.register-button {
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

.dark-mode .register-button {
  background-color: #a78bfa;
}

.register-button:hover {
  background-color: #6d46e8;
  transform: translateY(-1px);
}

.dark-mode .register-button:hover {
  background-color: #8b5cf6;
}

.register-button:disabled {
  background-color: #cbd5e1;
  transform: none;
  cursor: not-allowed;
}

.dark-mode .register-button:disabled {
  background-color: #475569;
}

/* Terms checkbox */
.terms-container {
  display: flex;
  align-items: flex-start;
  gap: 0.5rem;
  margin: 1.5rem 0;
  font-size: 0.875rem;
  color: #64748b;
}

.dark-mode .terms-container {
  color: #94a3b8;
}

.terms-container input {
  margin-top: 0.25rem;
  min-width: 1rem;
  height: 1rem;
  accent-color: #7e5bef;
}

.dark-mode .terms-container input {
  accent-color: #a78bfa;
}

.terms-link {
  color: #7e5bef;
  text-decoration: none;
  font-weight: 500;
}

.dark-mode .terms-link {
  color: #a78bfa;
}

.terms-link:hover {
  text-decoration: underline;
}

/* Footer */
.auth-footer {
  text-align: center;
  font-size: 0.875rem;
  color: #64748b;
  margin-top: 1.5rem;
}

.dark-mode .auth-footer {
  color: #94a3b8;
}

.auth-footer a {
  color: #7e5bef;
  text-decoration: none;
  font-weight: 500;
}

.dark-mode .auth-footer a {
  color: #a78bfa;
}

.auth-footer a:hover {
  text-decoration: underline;
}

@media (max-width: 640px) {
  .register-card {
    padding: 2rem 1.5rem;
  }

  .form-row {
    flex-direction: column;
    gap: 1rem;
  }

  .stepper {
    margin-bottom: 1.5rem;
  }

  .step-label {
    display: none;
  }
}
</style>