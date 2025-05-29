<template>
  <aside class="sidebar" :class="{ 'sidebar-collapsed': isCollapsed, 'dark-mode': darkMode }">
    <div class="sidebar-header">
      <span class="logo-icon" @click="$emit('toggle-sidebar')" style="cursor: pointer;">
        <i class="fas fa-rocket" :class="{ 'centered-icon': isCollapsed }"></i>
      </span>
      <span class="logo-text" v-if="!isCollapsed">Administración</span>
    </div>

    <nav class="sidebar-nav">
      <ul>
        <li v-for="item in menuItems" :key="item.path" :class="{ 'active': $route.path.startsWith(item.path) }">
          <router-link :to="item.path" class="nav-link">
            <span class="nav-icon">
              <i :class="item.icon"></i>
            </span>
            <span class="nav-text" v-if="!isCollapsed">{{ item.text }}</span>
            <span class="nav-badge" v-if="item.badge && !isCollapsed">{{ item.badge }}</span>
          </router-link>
        </li>
      </ul>
    </nav>

    <div class="sidebar-footer">
      <button class="logout-btn" @click="logout">
        <span class="logout-icon">
          <i class="fas fa-sign-out-alt"></i>
        </span>
        <span class="logout-text" v-if="!isCollapsed">Cerrar Sesión</span>
      </button>
    </div>
  </aside>
</template>

<script>
import { logout } from '@/services/auth.service';

export default {
  name: 'AdminMenu',
  props: {
    isCollapsed: Boolean,
    darkMode: Boolean,
    menuItems: {
      type: Array,
      default: () => [
        { path: '/admin', icon: 'fas fa-tachometer-alt', text: 'Dashboard', badge: '' },
        { path: '/admin/producciones', icon: 'fas fa-film', text: 'Producciones', badge: '' },
        { path: '/admin/profesionales', icon: 'fa-solid fa-user-tie', text: 'Profesionales', badge: '' },
        { path: '/admin/sagas', icon: 'fas fa-layer-group', text: 'Sagas', badge: '' },
        { path: '/admin/ubicaciones', icon: 'fas fa-map-marked-alt', text: 'Ubicaciones', badge: '' },
        { path: '/admin/usuarios', icon: 'fas fa-users', text: 'Usuarios', badge: '' },
      ]
    }
  },
  methods: {
    logout() {
      logout();
      this.$router.push('/');
    }
  }
}
</script>

<style scoped>
:root {
  --color-primary: #7e5bef;
  --color-primary-light: #9a7bff;
  --color-primary-dark: #6d46e8;
  --color-primary-darker: #4a2d9e;
  --color-primary-darkest: #2a1a5e;
  --color-text-light: #f8f9fa;
}

.sidebar {
  width: 260px;
  height: 100vh;
  position: fixed;
  left: 0;
  top: 0;
  background-color: var(--color-primary-darker);
  color: white;
  display: flex;
  flex-direction: column;
  transition: all 0.3s ease;
  z-index: 1000;
  box-shadow: 3px 0 15px rgba(0, 0, 0, 0.2);
  overflow: hidden;
}

.sidebar-collapsed {
  width: 70px;
}

.sidebar-header {
  padding: 1.09rem 1.7rem;
  display: flex;
  align-items: center;
  gap: 12px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  background-color: rgba(0, 0, 0, 0.1);
}

.logo-icon {
  padding: 0.38rem 0.38rem;
  margin-left: -0.3rem;

  font-size: 1.5rem;
  color: var(--color-primary-light);
  min-width: 24px;
  text-align: center;
  display: flex;
  justify-content: center;
}

.centered-icon {
  margin: 0 auto;
}

.logo-text {
  font-size: 1.5rem;
  font-weight: 600;
  white-space: nowrap;
  transition: all 0.3s ease;
}

.sidebar-collapsed .logo-text {
  display: none;
}

.sidebar-nav {
  flex: 1;
  overflow-y: auto;
  padding: 0.5rem 0;
  scrollbar-width: thin;
  scrollbar-color: var(--color-primary-light) transparent;
}

.sidebar-nav::-webkit-scrollbar {
  width: 5px;
}

.sidebar-nav::-webkit-scrollbar-thumb {
  background-color: var(--color-primary-light);
  border-radius: 3px;
}

.sidebar-nav ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.sidebar-nav li {
  margin: 0.15rem 0;
}

.nav-link {
  display: flex;
  align-items: center;
  padding: 0.9rem 1.35rem;
  color: rgba(255, 255, 255, 0.85);
  text-decoration: none;
  transition: all 0.2s ease;
  position: relative;
}

.nav-link:hover {
  color: white;
  background: rgba(255, 255, 255, 0.1);
}

.nav-link.active {
  color: white;
  background: linear-gradient(90deg, var(--color-primary), rgba(126, 91, 239, 0.3));
}

.nav-link.active::after {
  content: '';
  position: absolute;
  right: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 20px;
  background: white;
  border-radius: 3px 0 0 3px;
}

.nav-icon {
  font-size: 1.2rem;
  width: 24px;
  text-align: center;
  margin-right: 12px;
  flex-shrink: 0;
}

.nav-text {
  font-size: 0.95rem;
  white-space: nowrap;
  transition: all 0.3s ease;
}

.sidebar-collapsed .nav-text {
  display: none;
}

.nav-badge {
  background: rgba(255, 255, 255, 0.2);
  padding: 0.2rem 0.4rem;
  border-radius: 50px;
  font-size: 0.7rem;
  margin-left: auto;
}

.sidebar-footer {
  padding: 0.8rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  background-color: rgba(0, 0, 0, 0.1);
}

.logout-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  padding: 0.7rem;
  background: rgba(255, 69, 69, 0.15);
  border: none;
  border-radius: 6px;
  color: rgba(255, 255, 255, 0.9);
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.95rem;
}

.logout-btn:hover {
  background: rgba(255, 69, 69, 0.25);
  color: white;
}

.logout-icon {
  font-size: 1rem;
  width: 24px;
  text-align: center;
}

.logout-text {
  margin-left: 10px;
}

.sidebar-collapsed .logout-text {
  display: none;
}

.dark-mode .sidebar {
  background-color: var(--color-primary-darkest);
}

/* Responsive para móviles */
@media (max-width: 768px) {
  .sidebar {
    width: 260px;
    transform: translateX(-100%);
    transition: transform 0.3s ease;
    z-index: 1100;
  }

  .sidebar:not(.sidebar-collapsed) {
    transform: translateX(0);
  }

  .sidebar-collapsed {
    width: 0;
    overflow: hidden;
  }

  .nav-link {
    padding: 0.8rem 1rem;
  }

  .logo-text, .nav-text {
    font-size: 0.9rem;
  }
}

@media (max-width: 480px) {
  .sidebar {
    width: 240px;
  }

  .nav-icon, .logout-icon {
    font-size: 1rem;
    margin-right: 8px;
  }
}
</style>