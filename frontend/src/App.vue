<script setup>
import { ref, onMounted, watch } from 'vue';
import Header from './components/Header.vue';
import Footer from './components/Footer.vue';

const isDarkMode = ref(false);

// Función para aplicar el modo oscuro
const applyDarkMode = () => {
  if (isDarkMode.value) {
    document.documentElement.classList.add('dark');
    document.documentElement.style.setProperty('color-scheme', 'dark');
    document.documentElement.style.color = 'rgba(255, 255, 255, 0.87)';
    document.documentElement.style.backgroundColor = '#242424';
  } else {
    document.documentElement.classList.remove('dark');
    document.documentElement.style.setProperty('color-scheme', 'light');
    document.documentElement.style.color = '#213547';
    document.documentElement.style.backgroundColor = '#ffffff';
  }
};

// Inicialización al montar el componente
onMounted(() => {
  const savedMode = localStorage.getItem('darkMode');
  const systemPrefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;

  isDarkMode.value = savedMode !== null ? savedMode === 'true' : systemPrefersDark;
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
</script>

<template>
  <div :class="{ 'dark': isDarkMode }">
    <!-- Main Content -->
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>