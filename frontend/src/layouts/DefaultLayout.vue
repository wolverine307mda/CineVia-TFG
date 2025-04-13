<template>
  <div :class="{ 'dark': isDarkMode }">
    <Header
        :dark-mode="isDarkMode"
        @toggle-dark-mode="toggleDarkMode"
    />
    <main class="main-content">
      <router-view :dark-mode="isDarkMode" />
    </main>
    <Footer :dark-mode="isDarkMode" />
  </div>
</template>

<script>
import { ref, onMounted, watch } from 'vue';
import Header from '@/components/Header.vue';
import Footer from '@/components/Footer.vue';

export default {
  name: 'DefaultLayout',
  components: {
    Header,
    Footer
  },
  setup() {
    const isDarkMode = ref(false);

    // Función para aplicar el modo oscuro
    const applyDarkMode = () => {
      if (isDarkMode.value) {
        document.documentElement.classList.add('dark');
        document.documentElement.style.setProperty('color-scheme', 'dark');
      } else {
        document.documentElement.classList.remove('dark');
        document.documentElement.style.setProperty('color-scheme', 'light');
      }
    };

    // Inicialización al montar el componente
    onMounted(() => {
      const savedMode = localStorage.getItem('darkMode');
      const systemPrefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;

      isDarkMode.value = savedMode !== null ? JSON.parse(savedMode) : systemPrefersDark;
      applyDarkMode();

      // Escuchar cambios en la preferencia del sistema
      if (window.matchMedia) {
        const darkModeMediaQuery = window.matchMedia('(prefers-color-scheme: dark)');
        darkModeMediaQuery.addEventListener('change', (e) => {
          if (localStorage.getItem('darkMode') === null) {
            isDarkMode.value = e.matches;
            applyDarkMode();
          }
        });
      }
    });

    // Observar cambios en el modo oscuro
    watch(isDarkMode, (newVal) => {
      localStorage.setItem('darkMode', newVal);
      applyDarkMode();
    });

    const toggleDarkMode = () => {
      isDarkMode.value = !isDarkMode.value;
    };

    return {
      isDarkMode,
      toggleDarkMode
    };
  }
}
</script>

<style>
.main-content {
  background-color: var(--color-bg);
  color: var(--color-text);
  min-height: calc(100vh - 120px); /* Ajusta según el tamaño de tu header/footer */
  transition: background-color 0.3s ease, color 0.3s ease;
}
</style>