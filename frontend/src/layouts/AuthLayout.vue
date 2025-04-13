<template>
  <div class="auth-layout" :class="{'dark-mode': darkMode}">
    <div class="auth-container">
      <slot :darkMode="darkMode" :toggleDarkMode="toggleDarkMode"></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AuthLayout',
  data() {
    return {
      darkMode: false
    }
  },
  created() {
    const savedMode = localStorage.getItem('darkMode');
    const systemPrefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;

    this.darkMode = savedMode !== null ? savedMode === 'true' : systemPrefersDark;
    this.applyDarkMode();

    if (window.matchMedia) {
      this.darkModeMediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
      this.darkModeMediaQuery.addEventListener('change', this.handleSystemThemeChange);
    }
  },
  beforeDestroy() {
    if (this.darkModeMediaQuery) {
      this.darkModeMediaQuery.removeEventListener('change', this.handleSystemThemeChange);
    }
  },
  methods: {
    toggleDarkMode() {
      this.darkMode = !this.darkMode;
      this.applyDarkMode();
      localStorage.setItem('darkMode', this.darkMode);
    },
    applyDarkMode() {
      if (this.darkMode) {
        document.documentElement.classList.add('dark');
      } else {
        document.documentElement.classList.remove('dark');
      }
    },
    handleSystemThemeChange(e) {
      if (localStorage.getItem('darkMode') === null) {
        this.darkMode = e.matches;
        this.applyDarkMode();
      }
    }
  }
}
</script>

<style>
.auth-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 2rem;
  /* background-image: url("https://images.unsplash.com/photo-1573614999645-e5f0f16ec15d?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");*/
  background-image: url("https://images.unsplash.com/photo-1703221196953-d510425e4807?q=80&w=2127&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
  background-attachment: fixed;
  background-size: cover;
  background-position: center;
  transition: all 0.3s ease;
}

.auth-card {
  width: 100%;
  max-width: 450px;
  padding: 2.5rem;
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  background-color: rgba(227, 225, 227, 0.93);
  transition: all 0.3s ease;
}

.dark-mode .auth-card {
  background-color: rgba(39, 28, 49, 0.95);
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.3);
  color: #e2e8f0;
}

.auth-title {
  font-size: 1.75rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
  text-align: center;
  color: #1e293b;
}

.dark-mode .auth-title {
  color: #f8fafc;
}

.text-purple {
  color: #7e5bef;
}

.dark-mode .text-purple {
  color: #a78bfa;
}

.auth-subtitle {
  color: #64748b;
  text-align: center;
  margin-bottom: 2rem;
}

.dark-mode .auth-subtitle {
  color: #94a3b8;
}

.auth-form {
  margin-top: 2rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 500;
  color: #1e293b;
}

.dark-mode .form-group label {
  color: #e2e8f0;
}

.input-group {
  position: relative;
  display: flex;
  align-items: center;
}

.input-group input {
  width: 100%;
  padding: 0.875rem 1rem;
  border: 1px solid #e2e8f0;
  border-radius: 10px !important;
  font-size: 1rem;
  transition: all 0.3s ease;
  background-color: white;
  color: #1e293b;
}

.dark-mode .input-group input {
  background-color: #312e81;
  border-color: #4338ca;
  color: #f8fafc;
}

.input-group input:focus {
  outline: none;
  border-color: #7e5bef;
  box-shadow: 0 0 0 3px rgba(126, 91, 239, 0.2);
}

.dark-mode .input-group input:focus {
  box-shadow: 0 0 0 3px rgba(167, 139, 250, 0.3);
}

.input-icon {
  position: absolute;
  right: 1rem;
  color: #94a3b8;
}

.btn-auth {
  width: 100%;
  padding: 0.875rem;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  border: none;
  margin-top: 0.5rem;
}

.btn-primary {
  background-color: #7e5bef;
  color: white;
}

.btn-primary:hover {
  background-color: #6d46e8;
  transform: translateY(-2px);
}

.btn-primary:disabled {
  background-color: #cbd5e1;
  transform: none;
  cursor: not-allowed;
}

.dark-mode .btn-primary:disabled {
  background-color: #475569;
}

.auth-footer {
  text-align: center;
  margin-top: 1.5rem;
  color: #64748b;
}

.dark-mode .auth-footer {
  color: #94a3b8;
}

.auth-link {
  color: #7e5bef;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.dark-mode .auth-link {
  color: #a78bfa;
}

.auth-link:hover {
  text-decoration: underline;
}

.divider {
  display: flex;
  align-items: center;
  margin: 1.5rem 0;
  color: #94a3b8;
}

.divider::before,
.divider::after {
  content: "";
  flex: 1;
  border-bottom: 1px solid #e2e8f0;
}

.dark-mode .divider::before,
.dark-mode .divider::after {
  border-bottom-color: #475569;
}

.divider-text {
  padding: 0 1rem;
}

.social-auth {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1rem;
  margin-top: 1.5rem;
}

.btn-social {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  padding: 0.75rem;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.btn-google {
  background-color: white;
  color: #475569;
  border: 1px solid #e2e8f0;
}

.dark-mode .btn-google {
  background-color: #312e81;
  color: #e2e8f0;
  border-color: #4338ca;
}

.btn-facebook {
  background-color: #3b5998;
  color: white;
  border: none;
}

.toggle-dark {
  position: absolute;
  top: 1.5rem;
  right: 1.5rem;
  background: none;
  border: none;
  color: #64748b;
  cursor: pointer;
  font-size: 1.25rem;
  transition: all 0.3s ease;
}

.dark-mode .toggle-dark {
  color: #a78bfa;
}

.toggle-dark:hover {
  transform: scale(1.1);
}

.logo-icon {
  margin-bottom: 1rem;
}

.text-right {
  text-align: right;
}

.mt-1 {
  margin-top: 0.25rem;
}

.mb-6 {
  margin-bottom: 1.5rem;
}

.checkbox-container {
  display: flex;
  align-items: center;
  cursor: pointer;
  margin-bottom: 1.5rem;
}

.checkbox-container input {
  margin-right: 0.5rem;
}

.dark-mode .checkbox-container {
  color: #e2e8f0;
}

@media (max-width: 576px) {
  .auth-container {
    padding: 1rem;
  }

  .auth-card {
    padding: 1.5rem;
  }

  .social-auth {
    grid-template-columns: 1fr;
  }
}
.input-group {
  position: relative;
}

.input-group input {
  width: 100%;
  padding-right: 2.5rem; /* espacio para que no se sobreponga el ícono */
}

.input-icon {
  position: absolute;
  top: 50%;
  right: 10px; /* ajusta según tus necesidades */
  transform: translateY(-50%);
  cursor: pointer;
}

</style>