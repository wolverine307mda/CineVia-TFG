<template>
  <div class="auth-layout" :class="{'dark-mode': darkMode}">
    <!-- Botón flotante home -->
    <button class="floating-btn home-btn" @click="goHome">
      <i class="fas fa-home"></i>
    </button>

    <!-- Animación de burbujas -->
    <div class="bubbles">
      <div v-for="(bubble, index) in bubbles" :key="index" class="bubble"
           :style="bubble.style"></div>
    </div>

    <div class="auth-background">
      <div class="auth-overlay"></div>
    </div>
    <div class="auth-container">
      <slot :darkMode="darkMode" :toggleDarkMode="toggleDarkMode"></slot>
    </div>
  </div>
</template>

<script>
import router from "@/router/index.js";

export default {
  name: 'AuthLayout',
  setup() {
    const goHome = () => router.push('/');
    return {
      goHome
    }
  },
  data() {
    return {
      darkMode: false,
      bubbles: [],
      bubbleCount: 20
    }
  },
  created() {
    this.initializeTheme();
    this.createBubbles();
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
    },
    createBubbles() {
      for (let i = 0; i < this.bubbleCount; i++) {
        this.bubbles.push({
          style: {
            left: `${Math.random() * 100}%`,
            width: `${Math.random() * 30 + 10}px`,
            height: `${Math.random() * 30 + 10}px`,
            opacity: Math.random() * 0.6 + 0.1,
            animationDuration: `${Math.random() * 20 + 10}s`,
            animationDelay: `${Math.random() * 5}s`,
            backgroundColor: this.getRandomBubbleColor()
          }
        });
      }
    },
    getRandomBubbleColor() {
      const colors = [
        'rgba(126, 91, 239, 0.5)',
        'rgba(167, 139, 250, 0.4)',
        'rgba(200, 181, 246, 0.3)',
        'rgba(232, 223, 253, 0.2)'
      ];
      return colors[Math.floor(Math.random() * colors.length)];
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
  overflow: hidden;
}

.auth-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image:
      linear-gradient(rgba(126, 91, 239, 0.1), rgba(126, 91, 239, 0.1)),
      url("https://images.unsplash.com/photo-1573614999645-e5f0f16ec15d?q=80&w=2070&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D");
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
  z-index: 2;
}

/* Botón flotante */
.floating-btn {
  position: fixed;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.25rem;
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 100;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
  border: none;
  color: white;
}

.floating-btn:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 15px rgba(0, 0, 0, 0.3);
}

.home-btn {
  top: 30px;
  left: 30px;
  background-color: #7e5bef;
}

.dark-mode .home-btn {
  background-color: #a78bfa;
}

/* Animación de burbujas */
.bubbles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 1;
}

.bubble {
  position: absolute;
  bottom: -100px;
  border-radius: 50%;
  animation: rise 15s infinite ease-in;
  filter: blur(1px);
}

.bubble:nth-child(odd) {
  animation-direction: alternate-reverse;
}

@keyframes rise {
  0% {
    bottom: -100px;
    transform: translateX(0) rotate(0deg);
  }
  50% {
    transform: translateX(100px) rotate(180deg);
  }
  100% {
    bottom: 100%;
    transform: translateX(-200px) rotate(360deg);
  }
}

/* Responsive */
@media (max-width: 768px) {
  .floating-btn {
    width: 45px;
    height: 45px;
    font-size: 1.1rem;
  }

  .home-btn {
    top: 20px;
    left: 20px;
  }
}

@media (max-width: 576px) {
  .floating-btn {
    width: 40px;
    height: 40px;
    font-size: 1rem;
  }

  .auth-container {
    padding: 1.5rem;
  }
}
</style>