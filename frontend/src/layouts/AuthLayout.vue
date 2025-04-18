<template>
  <div class="auth-layout" :class="{'dark-mode': darkMode}">
    <div class="auth-background">
      <div class="auth-overlay"></div>
    </div>
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
    this.initializeTheme();
  },
  beforeDestroy() {
    this.cleanupThemeListener();
  },
  methods: {
    initializeTheme() {
      const savedMode = localStorage.getItem('darkMode');
      const systemPrefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;

      this.darkMode = savedMode !== null ? savedMode === 'true' : systemPrefersDark;
      this.applyDarkMode();

      this.setupThemeListener();
    },
    setupThemeListener() {
      if (window.matchMedia) {
        this.darkModeMediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
        this.darkModeMediaQuery.addEventListener('change', this.handleSystemThemeChange);
      }
    },
    cleanupThemeListener() {
      if (this.darkModeMediaQuery) {
        this.darkModeMediaQuery.removeEventListener('change', this.handleSystemThemeChange);
      }
    },
    toggleDarkMode() {
      this.darkMode = !this.darkMode;
      this.applyDarkMode();
      localStorage.setItem('darkMode', this.darkMode);
    },
    applyDarkMode() {
      document.documentElement.classList.toggle('dark', this.darkMode);
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

<style scoped>
.auth-layout {
  position: relative;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.auth-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url("https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?q=80&w=1964&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
  background-size: cover;
  background-position: center;
  z-index: 0;
}

.auth-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(2px);
}

.dark-mode .auth-overlay {
  background: rgba(0, 0, 0, 0.7);
}

.auth-container {
  position: relative;
  width: 100%;
  max-width: 1200px;
  padding: 2rem;
  z-index: 1;
}
</style>