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

        <!-- Nuevo elemento para copias de seguridad -->
        <li>
          <a href="#" class="nav-link" @click.prevent="showBackupModal = true">
            <span class="nav-icon">
              <i class="fas fa-database"></i>
            </span>
            <span class="nav-text" v-if="!isCollapsed">Copias de Seguridad</span>
          </a>
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

    <!-- Modal de copias de seguridad -->
    <div v-if="showBackupModal" class="modal-overlay" @click.self="showBackupModal = false">
      <div class="modal-content">
        <div class="modal-header">
          <h3>Gestión de Copias de Seguridad</h3>
          <button class="close-btn" @click="showBackupModal = false">
            <i class="fas fa-times"></i>
          </button>
        </div>

        <div class="modal-body">
          <div class="backup-actions">
            <button class="action-btn export-btn" @click="handleExport">
              <i class="fas fa-file-export"></i> Exportar Backup
            </button>
          </div>

          <div class="import-section">
            <h4>Importar Backup</h4>
            <div v-if="loading" class="loading-spinner">
              <i class="fas fa-spinner fa-spin"></i> Cargando...
            </div>

            <div v-else>
              <div v-if="backups.length === 0" class="no-backups">
                No hay copias de seguridad disponibles
              </div>

              <ul v-else class="backup-list">
                <li v-for="backup in backups" :key="backup" class="backup-item">
                  <span class="backup-name">{{ backup }}</span>
                  <button class="restore-btn" @click="handleImport(backup)">
                    <i class="fas fa-undo"></i> Restaurar
                  </button>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </aside>
</template>

<script>
import {useAuthStore} from "@/stores/auth.js";
import router from "@/router/index.js";
import BackupService from '@/services/backup.service.js';
import { saveAs } from 'file-saver';

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
  data() {
    return {
      showBackupModal: false,
      backups: [],
      loading: false,
    }
  },
  methods: {
    async loadBackups() {
      this.loading = true;
      try {
        this.backups = await BackupService.listBackups();
      } catch (error) {
        console.error('Error loading backups:', error);
        alert('Error al cargar las copias de seguridad');
      } finally {
        this.loading = false;
      }
    },
    async handleExport() {
      try {
        const response = await BackupService.exportBackup();
        const contentDisposition = response.headers['content-disposition'];
        const filename = contentDisposition
            ? contentDisposition.split('filename=')[1]
            : `backup_${new Date().toISOString().slice(0, 10)}.sql`;

        saveAs(new Blob([response.data]), filename);
        alert('Backup exportado correctamente');
      } catch (error) {
        console.error('Error exporting backup:', error);
        alert('Error al exportar el backup');
      }
    },
    async handleImport(filename) {
      if (!confirm(`¿Estás seguro de que quieres restaurar el backup "${filename}"? Esto sobrescribirá todos los datos actuales.`)) {
        return;
      }

      try {
        const result = await BackupService.importBackup(filename);
        alert(result);
        this.showBackupModal = false;
      } catch (error) {
        console.error('Error importing backup:', error);
        alert('Error al importar el backup');
      }
    },
    logout() {
      const authStore = useAuthStore();
      authStore.logout();
      router.push('/');
    }
  },
  watch: {
    showBackupModal(val) {
      if (val) {
        this.loadBackups();
      }
    }
  }
}
</script>

<style scoped>
/* Estilos anteriores del sidebar... */

/* Estilos del modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2000;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  width: 90%;
  max-width: 600px;
  max-height: 80vh;
  overflow-y: auto;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  color: #333;
}

.modal-header {
  padding: 1rem 1.5rem;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  font-size: 1.25rem;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.25rem;
  cursor: pointer;
  color: #666;
}

.close-btn:hover {
  color: #333;
}

.modal-body {
  padding: 1.5rem;
}

.backup-actions {
  margin-bottom: 2rem;
}

.action-btn {
  padding: 0.75rem 1.25rem;
  border-radius: 6px;
  border: none;
  font-weight: 500;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  transition: all 0.2s;
}

.export-btn {
  background-color: #4CAF50;
  color: white;
}

.export-btn:hover {
  background-color: #3e8e41;
}

.import-section h4 {
  margin-top: 0;
  margin-bottom: 1rem;
  font-size: 1.1rem;
  color: #444;
}

.loading-spinner {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
}

.no-backups {
  padding: 1rem;
  background-color: #f5f5f5;
  border-radius: 6px;
  text-align: center;
  color: #666;
}

.backup-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.backup-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.75rem 1rem;
  border-bottom: 1px solid #eee;
}

.backup-item:last-child {
  border-bottom: none;
}

.backup-name {
  flex-grow: 1;
}

.restore-btn {
  padding: 0.5rem 1rem;
  background-color: #2196F3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 0.9rem;
}

.restore-btn:hover {
  background-color: #0b7dda;
}

/* Dark mode para el modal */
.dark-mode .modal-content {
  background-color: #2d3748;
  color: #f7fafc;
}

.dark-mode .modal-header {
  border-bottom-color: #4a5568;
}

.dark-mode .close-btn {
  color: #a0aec0;
}

.dark-mode .close-btn:hover {
  color: #f7fafc;
}

.dark-mode .import-section h4 {
  color: #e2e8f0;
}

.dark-mode .no-backups {
  background-color: #4a5568;
  color: #cbd5e0;
}

.dark-mode .backup-item {
  border-bottom-color: #4a5568;
}

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