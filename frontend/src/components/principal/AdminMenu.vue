<template>
  <aside class="sidebar" :class="{ 'sidebar-collapsed': isCollapsed, 'dark-mode': darkMode }">
    <div class="sidebar-header">
        <span class="logo-icon" @click="$emit('toggle-sidebar')" style="cursor: pointer;">
          <i class="fas fa-rocket"></i>
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
      <button class="logout-btn" @click="$emit('logout')">
        <span class="logout-icon">
          <i class="fas fa-sign-out-alt"></i>
        </span>
        <span class="logout-text" v-if="!isCollapsed">Cerrar Sesión</span>
      </button>
    </div>
  </aside>
</template>

<script>
export default {
  name: 'AdminMenu',
  props: {
    isCollapsed: Boolean,
    darkMode: Boolean,
    menuItems: {
      type: Array,
      default: () => [
        { path: '/admin', icon: 'fas fa-tachometer-alt', text: 'Dashboard', badge: '' },
        { path: '/admin/producciones', icon: 'fas fa-film', text: 'Producciones', badge: '5' },
        { path: '/admin/sagas', icon: 'fas fa-layer-group', text: 'Sagas', badge: '' },
        { path: '/admin/ubicaciones', icon: 'fas fa-map-marked-alt', text: 'Lugares', badge: '12' },
        { path: '/admin/usuarios', icon: 'fas fa-users', text: 'Usuarios', badge: '' },
        { path: '/admin/comentarios', icon: 'fas fa-comments', text: 'Comentarios', badge: '3' },
        { path: '/admin/configuracion', icon: 'fas fa-cog', text: 'Configuración', badge: '' }
      ]
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
  transition: all 0.3s;
  z-index: 100;
  box-shadow: 3px 0 15px rgba(0, 0, 0, 0.2);
  overflow: hidden;
}

.sidebar-collapsed .sidebar {
  width: 80px;
}

.sidebar-header {
  padding: 1.5rem 1.5rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  background-color: rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
}

.logo-link {
  display: flex;
  align-items: center;
  text-decoration: none;
  color: white;
  overflow: hidden;
}

.logo-icon {
  font-size: 1.8rem;
  margin-right: 0.75rem;
  color: var(--color-primary-light);
  min-width: 24px;
  transition: all 0.3s;
}

.logo-text {
  font-size: 1.2rem;
  font-weight: 700;
  white-space: nowrap;
  transition: all 0.3s;
  opacity: 1;
}

.sidebar-collapsed .logo-text {
  opacity: 0;
  width: 0;
  margin-right: 0;
}

.collapse-btn {
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.7);
  font-size: 1rem;
  cursor: pointer;
  padding: 0.5rem;
  border-radius: 50%;
  transition: all 0.3s;
  flex-shrink: 0;
}

.collapse-btn:hover {
  color: white;
  background: rgba(255, 255, 255, 0.2);
  transform: scale(1.1);
}

.sidebar-nav {
  flex: 1;
  overflow-y: auto;
  padding: 1rem 0;
  scrollbar-width: thin;
  scrollbar-color: var(--color-primary-light) transparent;
}

.sidebar-nav::-webkit-scrollbar {
  width: 6px;
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
  margin: 0.25rem 0;
  position: relative;
}

.nav-link {
  display: flex;
  align-items: center;
  padding: 0.75rem 1.5rem;
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  transition: all 0.3s;
  position: relative;
  overflow: hidden;
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
  font-size: 1.1rem;
  width: 24px;
  text-align: center;
  margin-right: 1rem;
  flex-shrink: 0;
  transition: all 0.3s;
}

.nav-text {
  flex: 1;
  white-space: nowrap;
  transition: all 0.3s;
  opacity: 1;
}

.sidebar-collapsed .nav-text {
  opacity: 0;
  width: 0;
  margin-right: 0;
}

.nav-badge {
  background: rgba(255, 255, 255, 0.2);
  padding: 0.25rem 0.5rem;
  border-radius: 50px;
  font-size: 0.75rem;
  transition: all 0.3s;
}

.sidebar-footer {
  padding: 1rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  background-color: rgba(0, 0, 0, 0.1);
}

.logout-btn {
  display: flex;
  align-items: center;
  width: 100%;
  padding: 0.75rem 1rem;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 6px;
  color: rgba(255, 255, 255, 0.8);
  cursor: pointer;
  transition: all 0.3s;
}

.logout-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  transform: translateX(5px);
}

.logout-icon {
  font-size: 1.1rem;
  width: 24px;
  text-align: center;
  margin-right: 1rem;
  flex-shrink: 0;
}

.logout-text {
  transition: all 0.3s;
  opacity: 1;
  white-space: nowrap;
}

.sidebar-collapsed .logout-text {
  opacity: 0;
  width: 0;
  margin-right: 0;
}

.dark-mode .sidebar {
  background-color: var(--color-primary-darkest);
}

@media (max-width: 992px) {
  .sidebar {
    transform: translateX(-100%);
    z-index: 1000;
  }

  .sidebar-collapsed .sidebar {
    transform: translateX(0);
    width: 260px;
  }

  .collapse-btn {
    display: none;
  }
}
</style>