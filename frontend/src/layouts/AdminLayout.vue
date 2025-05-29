<template>
  <div class="admin-layout" :class="{ 'sidebar-collapsed': isCollapsed, 'dark-mode': darkMode }">
    <AdminMenu :is-collapsed="isCollapsed" :dark-mode="darkMode" @toggle-sidebar="toggleSidebar" @logout="logout"/>
    <main class="main-admin-content">
      <header class="topbar">
        <div class="topbar-left">
          <button class="menu-toggle" @click="toggleSidebar">
            <i class="fas fa-bars"></i>
          </button>
          <h1 class="page-title">{{ currentRouteName }}</h1>
        </div>
        <div class="topbar-right">
          <div class="user-profile" @click="toggleDropdown" ref="userProfile">
            <img :src="user.avatar || defaultAvatar" alt="User" class="avatar">
            <span class="user-name">{{ user.nombre }}</span>
            <i class="fas fa-chevron-down dropdown-icon" :class="{ 'rotate': showDropdown }"></i>

            <transition name="dropdown">
              <div class="dropdown-menu" v-show="showDropdown">
                <ul>
                  <li @click="goToHome">
                    <i class="fas fa-home"></i> Ir a Inicio
                  </li>
                  <li @click="goToProfile">
                    <i class="fas fa-user"></i> Ver Perfil
                  </li>
                  <li @click="logout">
                    <i class="fas fa-sign-out-alt"></i> Cerrar Sesión
                  </li>
                </ul>
              </div>
            </transition>
          </div>
          <button class="theme-toggle" @click="toggleDarkMode">
            <i :class="darkMode ? 'fas fa-sun' : 'fas fa-moon'"></i>
          </button>
        </div>
      </header>
      <router-view :dark-mode="darkMode" />
    </main>
  </div>
</template>

<script>
import { computed, onMounted, ref, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import AdminMenu from '@/components/principal/AdminMenu.vue'

export default {
  name: 'AdminLayout',
  components: { AdminMenu },
  setup() {
    const authStore = useAuthStore()
    const router = useRouter()
    const isCollapsed = ref(false)
    const darkMode = ref(false)
    const showDropdown = ref(false)
    const userProfile = ref(null)
    const defaultAvatar = 'https://ui-avatars.com/api/?name=Usuario&background=7e5bef&color=fff'

    // Obtener datos del usuario desde el store
    const user = computed(() => authStore.user || {})

    const currentRouteName = computed(() => {
      return router.currentRoute.value.meta.title || 'Admin'
    })

    const toggleSidebar = () => {
      isCollapsed.value = !isCollapsed.value
      localStorage.setItem('sidebarCollapsed', isCollapsed.value)
    }

    const toggleDarkMode = () => {
      darkMode.value = !darkMode.value
      document.body.classList.toggle('dark-mode', darkMode.value)
      localStorage.setItem('darkMode', darkMode.value)
    }

    const toggleDropdown = () => {
      showDropdown.value = !showDropdown.value
    }

    const goToHome = () => {
      router.push('/')
      showDropdown.value = false
    }

    const goToProfile = () => {
      router.push('/myprofile')
      showDropdown.value = false
    }

    const logout = () => {
      authStore.logout()
      router.push('/auth/login')
      showDropdown.value = false
    }

    const closeDropdown = (event) => {
      if (userProfile.value && !userProfile.value.contains(event.target)) {
        showDropdown.value = false
      }
    }

    const loadDarkModePreference = () => {
      const savedMode = localStorage.getItem('darkMode')
      const systemPrefersDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches
      darkMode.value = savedMode !== null ? savedMode === 'true' : systemPrefersDark
      document.body.classList.toggle('dark-mode', darkMode.value)
    }

    const loadSidebarPreference = () => {
      const savedSidebarState = localStorage.getItem('sidebarCollapsed')
      if (savedSidebarState !== null) {
        isCollapsed.value = savedSidebarState === 'true'
      }
    }

    onMounted(async () => {
      try {
        if (!authStore.isAuthenticated) {
          await authStore.checkAuth()
        }

        if (!authStore.user) {
          await authStore.fetchCurrentUser()
        }

        loadSidebarPreference()
        loadDarkModePreference()
        document.addEventListener('click', closeDropdown)
      } catch (error) {
        console.error('Error loading user:', error)
        router.push('/auth/login')
      }
    })

    onBeforeUnmount(() => {
      document.removeEventListener('click', closeDropdown)
    })

    return {
      isCollapsed,
      darkMode,
      showDropdown,
      userProfile,
      defaultAvatar,
      user,
      currentRouteName,
      toggleSidebar,
      toggleDarkMode,
      toggleDropdown,
      goToHome,
      goToProfile,
      logout
    }
  }
}
</script>

<style>
:root {
  --color-primary: #7e5bef;
  --color-primary-light: #9a7bff;
  --color-primary-dark: #6d46e8;
  --color-primary-darker: #4a2d9e;
  --color-primary-darkest: #2a1a5e;
  --color-secondary: #f8f9fa;
  --color-text: #343a40;
  --color-text-light: #f8f9fa;
  --color-bg: #f5f7fa;
  --color-bg-dark: #121212;
  --color-card: #ffffff;
  --color-card-dark: #1e1e2d;
  --color-topbar: #ffffff;
  --color-topbar-dark: var(--color-primary-darkest);
  --sidebar-width: 260px;
  --sidebar-collapsed-width: 70px;
  --topbar-height: 70px;
  --transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  --transition-slow: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.dark-mode {
  --color-secondary: #2a2a3a;
  --color-text: #e9ecef;
  --color-bg: var(--color-bg-dark);
  --color-card: var(--color-card-dark);
  --color-topbar: var(--color-topbar-dark);
}

.admin-layout {
  display: flex;
  min-height: 100vh;
  background-color: var(--color-bg);
  color: var(--color-text);
  transition: var(--transition-slow);
}

.main-admin-content {
  flex: 1;
  margin-left: var(--sidebar-width);
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  transition: var(--transition-slow);
}

.sidebar-collapsed .main-admin-content {
  margin-left: var(--sidebar-collapsed-width);
}

.topbar {
  height: var(--topbar-height);
  background-color: var(--color-topbar);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 1.5rem;
  box-shadow: 3px 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 90;
  position: sticky;
  top: 0;
  transition: var(--transition-slow);
}

.dark-mode .topbar {
  background-color: var(--color-topbar-dark);
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.3);
}

.topbar-left {
  display: flex;
  align-items: center;
}

.menu-toggle {
  background: none;
  border: none;
  color: var(--color-text);
  font-size: 1.2rem;
  margin-right: 1.5rem;
  cursor: pointer;
  display: none;
  transition: var(--transition);
}

.dark-mode .menu-toggle {
  color: white;
}

.menu-toggle:hover {
  color: var(--color-primary);
  transform: scale(1.1);
}

.page-title {
  font-size: 1.5rem;
  margin: 0;
  color: var(--color-text);
  transition: var(--transition);
}

.dark-mode .page-title {
  color: white;
}

.topbar-right {
  display: flex;
  align-items: center;
  gap: 1.8rem;
}

.theme-toggle {
  background: none;
  border: none;
  color: var(--color-text);
  font-size: 1.2rem;
  cursor: pointer;
  transition: var(--transition);
}

.dark-mode .theme-toggle {
  color: white;
}

.theme-toggle:hover {
  color: var(--color-primary-light);
  transform: rotate(15deg) scale(1.1);
}

.user-profile {
  position: relative;
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 50px;
  transition: var(--transition);
}

.user-profile:hover {
  background: rgba(0, 0, 0, 0.05);
}

.dark-mode .user-profile:hover {
  background: rgba(255, 255, 255, 0.1);
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 0.75rem;
  border: 2px solid var(--color-primary-light);
  transition: var(--transition);
}

.user-profile:hover .avatar {
  transform: scale(1.1);
  box-shadow: 0 0 10px rgba(167, 139, 250, 0.5);
}

.user-name {
  font-weight: 500;
  margin-right: 0.5rem;
  transition: var(--transition);
}

.dark-mode .user-name {
  color: white;
}

.dropdown-icon {
  transition: transform 0.3s ease;
  font-size: 0.8rem;
  margin-left: 0.3rem;
}

.dropdown-icon.rotate {
  transform: rotate(180deg);
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  background-color: var(--color-card);
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  min-width: 200px;
  z-index: 1000;
  margin-top: 5px;
  overflow: hidden;
  transform-origin: top right;
}

.dark-mode .dropdown-menu {
  background-color: var(--color-card-dark);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.dropdown-menu ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.dropdown-menu li {
  padding: 10px 15px;
  display: flex;
  align-items: center;
  cursor: pointer;
  transition: var(--transition);
}

.dropdown-menu li:hover {
  background-color: rgba(0, 0, 0, 0.05);
}

.dark-mode .dropdown-menu li:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.dropdown-menu li i {
  margin-right: 10px;
  width: 20px;
  text-align: center;
  color: var(--color-primary);
}

.dark-mode .dropdown-menu li i {
  color: var(--color-primary-light);
}

/* Animaciones */
.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.3s ease;
}

.dropdown-enter-from,
.dropdown-leave-to {
  opacity: 0;
  transform: scale(0.95);
}

.content-wrapper {
  flex: 1;
  padding: 1.5rem;
  background-color: var(--color-bg);
  transition: var(--transition-slow);
}

@media (max-width: 992px) {
  .main-admin-content {
    margin-left: 0;
  }

  .sidebar-collapsed .main-admin-content {
    margin-left: 0;
  }

  .menu-toggle {
    display: block;
  }

  .topbar {
    padding: 0 1rem;
  }

  .user-name {
    display: none;
  }
}

@media (max-width: 576px) {
  .theme-toggle {
    display: none;
  }
}
</style>