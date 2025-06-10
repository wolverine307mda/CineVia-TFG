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
            <button class="action-btn export-btn" @click="confirmExport">
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

              <div v-else>
                <div class="backup-list-container">
                  <ul class="backup-list">
                    <li v-for="backup in paginatedBackups" :key="backup" class="backup-item">
                      <div class="backup-info">
                        <span class="backup-date">{{ formatBackupDate(backup) }}</span>
                        <span class="backup-filename">{{ backup }}</span>
                      </div>
                      <button class="restore-btn" @click="confirmImport(backup)">
                        <i class="fas fa-undo"></i> Restaurar
                      </button>
                    </li>
                  </ul>
                </div>

                <div class="pagination-controls" v-if="backups.length > itemsPerPage">
                  <button
                      class="pagination-btn"
                      @click="currentPage--"
                      :disabled="currentPage === 1"
                  >
                    <i class="fas fa-chevron-left"></i>
                  </button>
                  <span class="page-indicator">Página {{ currentPage }} de {{ totalPages }}</span>
                  <button
                      class="pagination-btn"
                      @click="currentPage++"
                      :disabled="currentPage === totalPages"
                  >
                    <i class="fas fa-chevron-right"></i>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal de confirmación para exportar -->
    <div v-if="showExportConfirm" class="modal-overlay" @click.self="showExportConfirm = false">
      <div class="confirm-modal">
        <div class="confirm-header">
          <h3>Confirmar Exportación</h3>
        </div>
        <div class="confirm-body">
          <p>¿Estás seguro de que deseas exportar una copia de seguridad de la base de datos?</p>
        </div>
        <div class="confirm-footer">
          <button class="confirm-btn cancel-btn" @click="showExportConfirm = false">Cancelar</button>
          <button class="confirm-btn confirm-export-btn" @click="handleExport">Confirmar</button>
        </div>
      </div>
    </div>

    <!-- Modal de confirmación para importar -->
    <div v-if="showImportConfirm" class="modal-overlay" @click.self="showImportConfirm = false">
      <div class="confirm-modal">
        <div class="confirm-header">
          <h3>Confirmar Restauración</h3>
        </div>
        <div class="confirm-body">
          <p>¿Estás seguro de que deseas restaurar la copia de seguridad <strong>{{ selectedBackupName }}</strong>?</p>
          <p class="warning-text">¡ADVERTENCIA! Esto sobrescribirá todos los datos actuales.</p>
        </div>
        <div class="confirm-footer">
          <button class="confirm-btn cancel-btn" @click="showImportConfirm = false">Cancelar</button>
          <button class="confirm-btn confirm-import-btn" @click="handleImport">Confirmar</button>
        </div>
      </div>
    </div>

    <!-- Toast notifications -->
    <div class="toast-container">
      <div v-for="(toast, index) in toasts" :key="index"
           class="toast"
           :class="`toast-${toast.type}`"
           @click="removeToast(index)">
        <div class="toast-icon">
          <i :class="toastIcon(toast.type)"></i>
        </div>
        <div class="toast-content">
          <p class="toast-title">{{ toast.title }}</p>
          <p class="toast-message">{{ toast.message }}</p>
        </div>
      </div>
    </div>
  </aside>
</template>

<script>
import { useAuthStore } from "@/stores/auth.js";
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
      showExportConfirm: false,
      showImportConfirm: false,
      backups: [],
      loading: false,
      selectedBackup: null,
      selectedBackupName: '',
      toasts: [],
      currentPage: 1,
      itemsPerPage: 5
    }
  },
  computed: {
    paginatedBackups() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.backups.slice(start, end);
    },
    totalPages() {
      return Math.ceil(this.backups.length / this.itemsPerPage);
    }
  },
  methods: {
    async loadBackups() {
      this.loading = true;
      try {
        this.backups = await BackupService.listBackups();
        // Ordenar backups por fecha (más recientes primero)
        this.backups.sort((a, b) => {
          const dateA = this.extractDateFromFilename(a);
          const dateB = this.extractDateFromFilename(b);
          return dateB - dateA;
        });
      } catch (error) {
        console.error('Error loading backups:', error);
        this.showToast('error', 'Error', 'No se pudieron cargar las copias de seguridad');
      } finally {
        this.loading = false;
      }
    },
    extractDateFromFilename(filename) {
      // Extrae la fecha del formato backup_08-06-2025_23-28-45.sql
      const matches = filename.match(/backup_(\d{2})-(\d{2})-(\d{4})_(\d{2})-(\d{2})-(\d{2})\.sql/);
      if (matches) {
        const [, day, month, year, hours, minutes, seconds] = matches;
        return new Date(`${year}-${month}-${day}T${hours}:${minutes}:${seconds}`);
      }
      return new Date(0); // Fecha por defecto si no coincide
    },
    formatBackupDate(filename) {
      const date = this.extractDateFromFilename(filename);
      return date.toLocaleString('es-ES', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false
      });
    },
    confirmExport() {
      this.showExportConfirm = true;
    },
    confirmImport(backup) {
      this.selectedBackup = backup;
      this.selectedBackupName = this.formatBackupDate(backup);
      this.showImportConfirm = true;
    },
    async handleExport() {
      this.showExportConfirm = false;
      try {
        const response = await BackupService.exportBackup();
        const contentDisposition = response.headers['content-disposition'];
        const filename = contentDisposition
            ? contentDisposition.split('filename=')[1]
            : `backup_${new Date().toISOString().slice(0, 10)}.sql`;

        saveAs(new Blob([response.data]), filename);
        this.showToast('success', 'Éxito', 'Backup exportado correctamente');

        await this.loadBackups();
      } catch (error) {
        console.error('Error exporting backup:', error);
        this.showToast('error', 'Error', 'No se pudo exportar el backup');
      }
    },
    async handleImport() {
      this.showImportConfirm = false;
      try {
        const result = await BackupService.importBackup(this.selectedBackup);
        this.showToast('success', 'Éxito', result);
        this.showBackupModal = false;
      } catch (error) {
        console.error('Error importing backup:', error);
        this.showToast('error', 'Error', 'No se pudo importar el backup');
      }
    },
    logout() {
      const authStore = useAuthStore();
      authStore.logout();
      this.showToast('info', 'Sesión cerrada', 'Has cerrado sesión correctamente');
      router.push('/');
    },
    showToast(type, title, message) {
      const toast = {
        type,
        title,
        message,
        id: Date.now()
      };
      this.toasts.push(toast);
      setTimeout(() => {
        this.removeToast(this.toasts.indexOf(toast));
      }, 5000);
    },
    removeToast(index) {
      if (index >= 0 && index < this.toasts.length) {
        this.toasts.splice(index, 1);
      }
    },
    toastIcon(type) {
      return {
        'success': 'fas fa-check-circle',
        'error': 'fas fa-exclamation-circle',
        'warning': 'fas fa-exclamation-triangle',
        'info': 'fas fa-info-circle'
      }[type];
    }
  },
  watch: {
    showBackupModal(val) {
      if (val) {
        this.loadBackups();
        this.currentPage = 1; // Resetear paginación al abrir el modal
      }
    }
  }
}
</script>

<style scoped>
/* Estilos del sidebar */
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

/* Estilos del modal principal */
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
  max-width: 700px;
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
  background-color: var(--color-primary);
  color: white;
  border-radius: 8px 8px 0 0;
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
  color: rgba(255, 255, 255, 0.8);
}

.close-btn:hover {
  color: white;
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
  font-size: 0.95rem;
}

.export-btn {
  background-color: var(--color-primary);
  color: white;
}

.export-btn:hover {
  background-color: var(--color-primary-dark);
}

.import-section h4 {
  margin-top: 0;
  margin-bottom: 1rem;
  font-size: 1.1rem;
  color: #444;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #eee;
}

.loading-spinner {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
  justify-content: center;
  padding: 2rem;
}

.no-backups {
  padding: 1.5rem;
  background-color: #f5f5f5;
  border-radius: 6px;
  text-align: center;
  color: #666;
  margin-top: 1rem;
}

.backup-list-container {
  margin-top: 1rem;
}

.backup-list {
  list-style: none;
  padding: 0;
  margin: 0;
  border: 1px solid #eee;
  border-radius: 6px;
  overflow: hidden;
}

.backup-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  border-bottom: 1px solid #eee;
  transition: background-color 0.2s;
}

.backup-item:hover {
  background-color: #f9f9f9;
}

.backup-item:last-child {
  border-bottom: none;
}

.backup-info {
  display: flex;
  flex-direction: column;
}

.backup-date {
  font-weight: 500;
  color: #333;
  margin-bottom: 0.25rem;
}

.backup-filename {
  font-size: 0.8rem;
  color: #666;
}

.restore-btn {
  padding: 0.5rem 1rem;
  background-color: var(--color-primary);
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 0.9rem;
  transition: all 0.2s;
}

.restore-btn:hover {
  background-color: var(--color-primary-dark);
}

.pagination-controls {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 1.5rem;
  gap: 1rem;
}

.pagination-btn {
  background-color: var(--color-primary);
  color: white;
  border: none;
  border-radius: 4px;
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.2s;
}

.pagination-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.pagination-btn:hover:not(:disabled) {
  background-color: var(--color-primary-dark);
}

.page-indicator {
  font-size: 0.9rem;
  color: #666;
}

/* Modales de confirmación */
.confirm-modal {
  background-color: white;
  border-radius: 8px;
  width: 90%;
  max-width: 500px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.confirm-header {
  padding: 1rem 1.5rem;
  background-color: var(--color-primary);
  color: white;
}

.confirm-header h3 {
  margin: 0;
  font-size: 1.2rem;
}

.confirm-body {
  padding: 1.5rem;
}

.confirm-body p {
  margin: 0 0 1rem;
  color: #333;
}

.warning-text {
  color: #d32f2f;
  font-weight: 500;
  margin-top: 1rem !important;
}

.confirm-footer {
  display: flex;
  justify-content: flex-end;
  padding: 1rem 1.5rem;
  border-top: 1px solid #eee;
  gap: 0.75rem;
}

.confirm-btn {
  padding: 0.5rem 1.25rem;
  border-radius: 4px;
  border: none;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.cancel-btn {
  background-color: #f5f5f5;
  color: #333;
}

.cancel-btn:hover {
  background-color: #e0e0e0;
}

.confirm-export-btn, .confirm-import-btn {
  background-color: var(--color-primary);
  color: white;
}

.confirm-export-btn:hover, .confirm-import-btn:hover {
  background-color: var(--color-primary-dark);
}

.confirm-import-btn {
  background-color: #d32f2f;
}

.confirm-import-btn:hover {
  background-color: #b71c1c;
}

/* Toast notifications */
.toast-container {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 3000;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.toast {
  display: flex;
  align-items: flex-start;
  width: 300px;
  padding: 1rem;
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  cursor: pointer;
  transition: all 0.3s ease;
  animation: slideIn 0.3s ease-out;
  opacity: 0.95;
}

.toast:hover {
  opacity: 1;
  transform: translateY(-2px);
}

.toast-icon {
  font-size: 1.25rem;
  margin-right: 0.75rem;
  margin-top: 2px;
}

.toast-content {
  flex: 1;
}

.toast-title {
  font-weight: 600;
  margin: 0 0 0.25rem;
}

.toast-message {
  margin: 0;
  font-size: 0.9rem;
  line-height: 1.4;
}

.toast-success {
  background-color: #4caf50;
  color: white;
}

.toast-error {
  background-color: #f44336;
  color: white;
}

.toast-warning {
  background-color: #ff9800;
  color: white;
}

.toast-info {
  background-color: #2196f3;
  color: white;
}

@keyframes slideIn {
  from {
    transform: translateX(100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 0.95;
  }
}

/* Dark mode styles */
.dark-mode .modal-content,
.dark-mode .confirm-modal {
  background-color: #2d3748;
  color: #f7fafc;
}

.dark-mode .modal-header,
.dark-mode .confirm-header {
  background-color: var(--color-primary-dark);
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
  border-bottom-color: #4a5568;
}

.dark-mode .no-backups {
  background-color: #4a5568;
  color: #cbd5e0;
}

.dark-mode .backup-list {
  border-color: #4a5568;
}

.dark-mode .backup-item {
  border-bottom-color: #4a5568;
  background-color: #1a202c;
}

.dark-mode .backup-item:hover {
  background-color: #2d3748;
}

.dark-mode .backup-date {
  color: #f7fafc;
}

.dark-mode .backup-filename {
  color: #a0aec0;
}

.dark-mode .page-indicator {
  color: #a0aec0;
}

.dark-mode .confirm-body p {
  color: #e2e8f0;
}

.dark-mode .confirm-footer {
  border-top-color: #4a5568;
}

.dark-mode .cancel-btn {
  background-color: #4a5568;
  color: #e2e8f0;
}

.dark-mode .cancel-btn:hover {
  background-color: #2d3748;
}

/* Variables de color */
:root {
  --color-primary: #7e5bef;
  --color-primary-light: #9a7bff;
  --color-primary-dark: #6d46e8;
  --color-primary-darker: #4a2d9e;
  --color-primary-darkest: #2a1a5e;
  --color-text-light: #f8f9fa;
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

  .modal-content, .confirm-modal {
    width: 95%;
    max-height: 90vh;
  }

  .toast {
    width: 250px;
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

  .backup-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.75rem;
  }

  .restore-btn {
    align-self: flex-end;
  }

  .confirm-footer {
    justify-content: center;
  }
}
</style>