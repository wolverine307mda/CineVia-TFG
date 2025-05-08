<template>
  <div class="not-found-container">
    <!-- Burbujas animadas de fondo -->
    <div class="bubbles">
      <div v-for="i in 15" :key="i" class="bubble" :style="bubbleStyle(i)"></div>
    </div>

    <div class="not-found-content">
      <div class="error-header">
        <h1 class="error-code">404</h1>
        <div class="error-icon">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
            <path d="M11 15h2v2h-2zm0-8h2v6h-2zm.99-5C6.47 2 2 6.48 2 12s4.47 10 9.99 10C17.52 22 22 17.52 22 12S17.52 2 11.99 2zM12 20c-4.42 0-8-3.58-8-8s3.58-8 8-8 8 3.58 8 8-3.58 8-8 8z"/>
          </svg>
        </div>
      </div>
      <h2 class="error-title">Página no encontrada</h2>
      <p class="error-message">Lo sentimos, no pudimos encontrar la página que buscas</p>
      <div class="action-buttons">
        <router-link to="/" class="home-button" style="text-decoration: none">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
            <path d="M10 20v-6h4v6h5v-8h3L12 3 2 12h3v8z"/>
          </svg>
          Volver al inicio
        </router-link>
        <button @click="goBack" class="back-button">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor">
            <path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"/>
          </svg>
          Volver atrás
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    bubbleStyle(index) {
      const size = Math.random() * 20 + 10;
      const posX = Math.random() * 100;
      const delay = Math.random() * 5;
      const duration = Math.random() * 10 + 10;
      const opacity = Math.random() * 0.3 + 0.1;

      return {
        width: `${size}px`,
        height: `${size}px`,
        left: `${posX}%`,
        animationDelay: `${delay}s`,
        animationDuration: `${duration}s`,
        opacity: opacity
      };
    }
  }
}
</script>

<style scoped>
.not-found-container {
  background: url('@/assets/fondo_home.jpg') center/cover no-repeat;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.not-found-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(42, 16, 58, 0.85) 0%, rgba(18, 18, 24, 0.85) 100%);
  z-index: 0;
}

.bubbles {
  position: absolute;
  width: 100%;
  height: 100%;
  z-index: 1;
  overflow: hidden;
}

.bubble {
  position: absolute;
  bottom: -100px;
  background: rgba(255, 255, 255, 0.15);
  border-radius: 50%;
  animation: float-up linear infinite;
  filter: blur(2px);
}

@keyframes float-up {
  0% {
    transform: translateY(0) rotate(0deg);
    bottom: -100px;
  }
  100% {
    transform: translateY(-120vh) rotate(360deg);
    bottom: 100%;
  }
}

.not-found-content {
  text-align: center;
  color: white;
  position: relative;
  z-index: 2;
  padding: 2rem;
  max-width: 600px;
  width: 90%;
  animation: fade-in 0.8s ease-out;
}

@keyframes fade-in {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.error-header {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
  margin-bottom: 1rem;
}

.error-code {
  font-size: 6rem;
  font-weight: 800;
  margin: 0;
  line-height: 1;
  color: #ffffff;
  text-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  animation: pulse 2s infinite alternate;
}

@keyframes pulse {
  from {
    transform: scale(1);
    text-shadow: 0 0 10px rgba(255, 255, 255, 0.2);
  }
  to {
    transform: scale(1.05);
    text-shadow: 0 0 20px rgba(255, 255, 255, 0.4);
  }
}

.error-icon {
  font-size: 4rem;
  color: #ffffff;
  animation: shake 3s infinite;
}

@keyframes shake {
  0%, 100% {
    transform: rotate(0deg);
  }
  2%, 6%, 10%, 14%, 18% {
    transform: rotate(-5deg);
  }
  4%, 8%, 12%, 16% {
    transform: rotate(5deg);
  }
}

.error-icon svg {
  width: 80px;
  height: 80px;
  filter: drop-shadow(0 4px 6px rgba(0, 0, 0, 0.1));
}

.error-title {
  font-size: 2.5rem;
  font-weight: 700;
  margin: 0.5rem 0;
  color: #ffffff;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  animation: slide-in 0.8s ease-out 0.2s both;
}

.error-message {
  font-size: 1.25rem;
  margin-bottom: 2.5rem;
  color: rgba(255, 255, 255, 0.9);
  max-width: 80%;
  margin-left: auto;
  margin-right: auto;
  animation: slide-in 0.8s ease-out 0.4s both;
}

@keyframes slide-in {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.action-buttons {
  display: flex;
  gap: 1.5rem;
  justify-content: center;
  flex-wrap: wrap;
  animation: slide-in 0.8s ease-out 0.6s both;
}

.home-button, .back-button {
  display: inline-flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem 2rem;
  border-radius: 50px;
  font-weight: 600;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
  font-size: 1.1rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border: none;
  position: relative;
  overflow: hidden;
}

.home-button::after, .back-button::after {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255,255,255,0.3) 0%, rgba(255,255,255,0) 70%);
  transform: scale(0);
  transition: transform 0.5s ease;
}

.home-button:hover::after, .back-button:hover::after {
  transform: scale(1);
}

.home-button {
  background-color: #ffffff;
  color: #6a1b9a;
}

.home-button:hover {
  background-color: #f3e5f5;
  transform: translateY(-3px);
  box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
}

.back-button {
  background-color: transparent;
  color: white;
  border: 2px solid white;
}

.back-button:hover {
  background-color: rgba(255, 255, 255, 0.1);
  transform: translateY(-3px);
  box-shadow: 0 10px 15px rgba(0, 0, 0, 0.1);
}

.home-button svg, .back-button svg {
  width: 24px;
  height: 24px;
  transition: transform 0.3s ease;
}

.home-button:hover svg {
  transform: translateX(-3px);
}

.back-button:hover svg {
  transform: translateX(-3px);
}

@media (max-width: 768px) {
  .error-header {
    flex-direction: column;
    gap: 0.5rem;
  }

  .error-code {
    font-size: 4.5rem;
  }

  .error-icon svg {
    width: 60px;
    height: 60px;
  }

  .error-title {
    font-size: 2rem;
  }

  .error-message {
    font-size: 1.1rem;
    max-width: 100%;
  }

  .action-buttons {
    flex-direction: column;
    gap: 1rem;
  }

  .home-button, .back-button {
    width: 100%;
    justify-content: center;
    padding: 0.9rem 1.5rem;
  }
}
</style>