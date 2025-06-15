<template>
  <header class="sticky-top">
    <nav class="navbar navbar-expand-lg navbar-dark main-nav">
      <div class="container">
        <!-- Logo y marca con animación -->
        <router-link class="navbar-brand d-flex align-items-center" to="/" @mouseenter="animateLogo">
          <svg ref="logoIcon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="36" height="36" class="logo-icon">
            <path
                fill="#a78bfa"
                d="M18,9H16V7H18M18,13H16V11H18M18,17H16V15H18M8,9H6V7H8M8,13H6V11H8M8,17H6V15H8M18,3V5H16V3H8V5H6V3H4V21H6V19H8V21H16V19H18V21H20V3H18Z"
            />
          </svg>
          <span class="brand-text ms-2">Movie<span class="text-purple">Trip</span></span>
        </router-link>

        <!-- Botón móvil -->
        <button
            class="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarContent"
        >
          <span class="navbar-toggler-icon" />
        </button>

        <!-- Contenido del navbar -->
        <div
            id="navbarContent"
            class="collapse navbar-collapse"
        >
          <!-- Menú principal -->
          <ul class="navbar-nav mx-auto">
            <li
                v-for="(item, index) in menuItems"
                :key="index"
                class="nav-item"
            >
              <router-link
                  class="nav-link"
                  :to="item.link"
                  @mouseenter="animateNavItem($event)"
              >
                <i :class="`${item.icon} me-1`" /> {{ item.text }}
              </router-link>
            </li>
          </ul>

          <!-- Botón de modo oscuro -->
          <button
              class="dark-mode-toggle me-3"
              @click="toggleDarkMode"
              @mouseenter="animateDarkModeButton"
          >
            <i :class="darkMode ? 'fas fa-sun' : 'fas fa-moon'" />
          </button>

          <!-- Acciones de usuario -->
          <div class="user-actions">
            <template v-if="!authStore.isAuthenticated">
              <router-link
                  ref="loginBtn"
                  to="/auth/login"
                  class="login-btn"
                  @mouseenter="animateLogin"
              >
                <i class="fas fa-user me-2" /> Iniciar sesión
              </router-link>
            </template>
            <template v-else>
              <div class="dropdown">
                <button
                    class="user-dropdown-btn"
                    type="button"
                    id="userDropdown"
                    data-bs-toggle="dropdown"
                    aria-expanded="false"
                >
                  <i class="fas fa-user-circle me-2" /> {{ authStore.user?.nombre || 'Mi perfil' }}
                </button>
                <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                  <li>
                    <router-link class="dropdown-item" to="/myprofile">
                      <i class="fas fa-user me-2" /> Mi perfil
                    </router-link>
                  </li>
                  <li v-if="authStore.isAdmin">
                    <router-link class="dropdown-item" to="/admin">
                      <i class="fas fa-shield-alt me-2" /> Panel de Administración
                    </router-link>
                  </li>
                  <li>
                    <button class="dropdown-item" @click="handleLogout">
                      <i class="fas fa-sign-out-alt me-2" /> Cerrar sesión
                    </button>
                  </li>
                </ul>
              </div>
            </template>
          </div>
        </div>
      </div>
    </nav>
  </header>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()
const logoIcon = ref(null)
const loginBtn = ref(null)

const props = defineProps({
  darkMode: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['toggle-dark-mode'])

const menuItems = [
  { text: 'General', icon: 'fas fa-th-large', link: '/general' },
  { text: 'Producciones', icon: 'fas fa-film', link: '/producciones' },
  { text: 'Profesionales', icon: 'fas fa-user-tie', link: '/profesionales' },
  { text: 'Sagas', icon: 'fas fa-stream', link: '/sagas' }
]

const toggleDarkMode = () => {
  emit('toggle-dark-mode')
  animateDarkModeButton()
}

const handleLogout = async () => {
  try {
    await authStore.logout()
    router.push('/')
  } catch (error) {
    console.error('Error al cerrar sesión:', error)
  }
}

const animateLogo = () => {
  if (logoIcon.value) {
    const logo = logoIcon.value
    logo.style.transform = 'rotate(15deg)'
    setTimeout(() => {
      logo.style.transform = 'rotate(0)'
    }, 300)
  }
}

const animateNavItem = (event) => {
  if (event?.currentTarget) {
    event.currentTarget.style.transform = 'translateY(-3px)'
    setTimeout(() => {
      if (event?.currentTarget) {
        event.currentTarget.style.transform = 'translateY(0)'
      }
    }, 200)
  }
}

const animateLogin = () => {
  if (loginBtn.value) {
    const btn = loginBtn.value
    btn.style.boxShadow = '0 0 15px rgba(167, 139, 250, 0.6)'
    setTimeout(() => {
      if (loginBtn.value) {
        btn.style.boxShadow = 'none'
      }
    }, 500)
  }
}

const animateDarkModeButton = () => {
  const button = document.querySelector('.dark-mode-toggle')
  if (button) {
    button.style.transform = 'scale(1.1)'
    setTimeout(() => {
      button.style.transform = 'scale(1)'
    }, 300)
  }
}
</script>

<style scoped>
header {
  background: #07051a;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.main-nav {
  background-color: #07051a !important;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.logo-icon {
  transition: all 0.3s cubic-bezier(0.68, -0.55, 0.27, 1.55);
}

.brand-text {
  font-size: 1.6rem;
  font-weight: 700;
  color: #f8fafc;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
}

.text-purple {
  color: #a78bfa;
  transition: all 0.3s ease;
}

.navbar-brand:hover .text-purple {
  color: #c4b5fd;
  text-shadow: 0 0 8px rgba(167, 139, 250, 0.4);
}

.nav-link {
  color: #f8fafc;
  font-weight: 500;
  padding: 0.6rem 1.2rem !important;
  margin: 0 0.2rem;
  border-radius: 6px;
  transition: all 0.3s ease;
  position: relative;
  text-decoration: none;
}

.nav-link:hover {
  background: rgba(167, 139, 250, 0.1);
  transform: translateY(-2px);
}

.nav-link i {
  color: #a78bfa;
  transition: all 0.3s ease;
}

.nav-link:hover i {
  color: #c4b5fd;
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: #a78bfa;
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.nav-link:hover::after {
  width: 60%;
}

.dark-mode-toggle {
  background: rgba(167, 139, 250, 0.1);
  border: none;
  color: #a78bfa;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.dark-mode-toggle:hover {
  background: rgba(167, 139, 250, 0.2);
  color: #c4b5fd;
}

.user-actions {
  display: flex;
  align-items: center;
}

.login-btn {
  background: #a78bfa;
  color: #0f0c29;
  padding: 0.5rem 1.2rem;
  border-radius: 30px;
  font-weight: 500;
  transition: all 0.3s ease;
  text-decoration: none;
}

.login-btn:hover {
  background: #c4b5fd;
  transform: translateY(-2px);
}

.user-dropdown-btn {
  background: #a78bfa;
  color: #0f0c29;
  padding: 0.5rem 1.2rem;
  border-radius: 30px;
  font-weight: 500;
  transition: all 0.3s ease;
  border: none;
  cursor: pointer;
}

.user-dropdown-btn:hover {
  background: #c4b5fd;
  transform: translateY(-2px);
}

.dropdown-menu {
  background-color: #1e1b4b;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.dropdown-item {
  color: #f8fafc;
  transition: all 0.2s ease;
  text-decoration: none;
  display: block;
  padding: 0.5rem 1rem;
}

.dropdown-item:hover {
  background-color: #a78bfa;
  color: #0f0c29;
}

.dropdown-divider {
  border-color: rgba(255, 255, 255, 0.1);
}

@media (max-width: 992px) {
  .search-box {
    width: 100% !important;
    margin: 1rem 0;
  }

  .dark-mode-toggle {
    margin: 1rem auto;
    display: flex;
  }

  .user-actions {
    margin-top: 1rem;
    padding-top: 1rem;
    border-top: 1px solid rgba(255, 255, 255, 0.1);
    justify-content: center;
    width: 100%;
  }

  .login-btn {
    width: 100%;
    text-align: center;
  }

  .user-dropdown-btn {
    width: 100%;
    text-align: center;
    margin-top: 1rem;
  }

  .dropdown-menu {
    width: 100%;
    text-align: center;
  }
}
</style>