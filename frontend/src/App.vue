<script setup>
import { ref, onMounted, watch } from 'vue'
import { useAuthStore } from '@/stores/auth'
import Header from './components/principal/Header.vue'
import Footer from './components/principal/Footer.vue'

const isDarkMode = ref(false)
const authStore = useAuthStore()

// Función para aplicar el modo oscuro
const applyDarkMode = () => {
  if (isDarkMode.value) {
    document.documentElement.classList.add('dark')
    document.documentElement.style.setProperty('color-scheme', 'dark')
    document.documentElement.style.color = 'rgba(255, 255, 255, 0.87)'
    document.documentElement.style.backgroundColor = '#242424'
  } else {
    document.documentElement.classList.remove('dark')
    document.documentElement.style.setProperty('color-scheme', 'light')
    document.documentElement.style.color = '#213547'
    document.documentElement.style.backgroundColor = '#ffffff'
  }
}

// Cargar datos de autenticación al iniciar
const initializeAuth = async () => {
  if (localStorage.getItem('token')) {
    try {
      await authStore.checkAuth()
    } catch (error) {
      console.error('Error al verificar autenticación:', error)
      localStorage.removeItem('token')
    }
  }
}

// Inicialización al montar el componente
onMounted(() => {
  // Configuración del modo oscuro
  const savedMode = localStorage.getItem('darkMode')
  const systemPrefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches

  isDarkMode.value = savedMode !== null ? savedMode === 'true' : systemPrefersDark
  applyDarkMode()

  // Escuchar cambios en la preferencia del sistema
  if (window.matchMedia) {
    const darkModeMediaQuery = window.matchMedia('(prefers-color-scheme: dark)')
    darkModeMediaQuery.addEventListener('change', (e) => {
      if (localStorage.getItem('darkMode') === null) {
        isDarkMode.value = e.matches
        applyDarkMode()
      }
    })
  }

  // Inicializar autenticación
  initializeAuth()
})

// Observar cambios en el modo oscuro
watch(isDarkMode, (newVal) => {
  localStorage.setItem('darkMode', newVal)
  applyDarkMode()
})
</script>

<template>
  <div :class="{ 'dark': isDarkMode }">
    <!-- Main Content -->
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>