<template>
  <div class="users-management-container">
    <!-- Header con título y botón de acción -->
    <div class="management-header">
      <h1 class="management-title" style="display: none">Gestión de Usuarios</h1>
    </div>

    <!-- Panel de filtros -->
    <div class="filters-panel">
      <div class="filter-group">
        <label class="filter-label">Buscar</label>
        <div class="search-input-container">
          <i class="fas fa-search search-icon"></i>
          <input
              v-model="filters.search"
              type="text"
              placeholder="Nombre, email o username"
              class="search-input"
              @input="onSearchInput"
          >
        </div>
      </div>

      <div class="filter-group">
        <label class="filter-label">Rol</label>
        <select
            v-model="filters.rol"
            class="filter-select"
            @change="fetchUsers"
        >
          <option :value="null">Todos los roles</option>
          <option value="USUARIO">Usuario</option>
          <option value="ADMINISTRADOR">Administrador</option>
        </select>
      </div>

      <div class="filter-group">
        <label class="filter-label">Estado</label>
        <select
            v-model="filters.isDeleted"
            class="filter-select"
            @change="fetchUsers"
        >
          <option :value="null">Todos</option>
          <option :value="false">Activos</option>
          <option :value="true">Inactivos</option>
        </select>
      </div>

      <div class="filter-group">
        <button
            @click="resetFilters"
            class="action-button secondary"
        >
          <i class="fas fa-filter-circle-xmark button-icon"></i>
          <span class="button-text">Limpiar</span>
        </button>
        <button
            v-if="isAdmin"
            @click="openCreateModal"
            class="action-button primary"
        >
          <i class="fas fa-plus button-icon"></i>
          <span class="button-text">Nuevo Usuario</span>
        </button>
      </div>
    </div>

    <!-- Tabla de usuarios -->
    <div class="table-wrapper">
      <table class="users-table">
        <thead>
        <tr>
          <th v-for="header in tableHeaders" :key="header.value" class="table-header" @click="sortBy(header.value)">
            <div class="header-content">
              <span>{{ header.text }}</span>
              <i v-if="sort.field === header.value" :class="['sort-icon', sort.direction === 'asc' ? 'fa-arrow-up' : 'fa-arrow-down']" ></i>
            </div>
          </th>
          <th class="table-header actions-header">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <tr v-if="isLoading">
          <td :colspan="tableHeaders.length + 1" class="loading-cell">
            <div class="loading-spinner">
              <i class="fas fa-spinner fa-spin"></i>
              <span>Cargando usuarios...</span>
            </div>
          </td>
        </tr>
        <tr v-else-if="users.length === 0">
          <td :colspan="tableHeaders.length + 1" class="empty-cell">
            <div class="empty-content">
              <i class="fas fa-user-slash empty-icon"></i>
              <span>No se encontraron usuarios</span>
            </div>
          </td>
        </tr>
        <tr v-for="user in users" :key="user.id" class="user-row">
          <td class="user-cell">
            <div class="user-info">
              <div class="avatar-container">
                <img class="user-avatar" :src="user.avatar" :alt="`Avatar de ${user.nombre}`" @error="setDefaultAvatar" >
              </div>
              <div class="user-details">
                <span class="user-name">{{ user.nombre }} {{ user.apellido }}</span>
                <span class="user-username">@{{ user.username }}</span>
              </div>
            </div>
          </td>
          <td class="user-cell email-cell">
            <span class="user-email">{{ user.email }}</span>
          </td>
          <td class="user-cell">
              <span class="role-badge" :class="roleBadgeClass(user.rol)">
                {{ formatRole(user.rol) }}
              </span>
          </td>
          <td class="user-cell">
            <span class="date-text">{{ formatDate(user.createdAt) }}</span>
          </td>
          <td class="user-cell">
              <span class="status-badge" :class="statusBadgeClass(user.isDeleted)">
                {{ user.isDeleted ? 'Inactivo' : 'Activo' }}
              </span>
          </td>
          <td class="user-cell actions-cell">
            <div class="action-buttons">
              <!-- EDITAR -->
              <button v-if=" isAdmin && currentUserId !== user.id && ( user.rol !== 'ADMINISTRADOR' || currentUsername === 'superAdmin')" @click="openEditModal(user)" class="icon-button edit-button" title="Editar" >
                <i class="fas fa-edit"></i>
              </button>

              <!-- DESACTIVAR -->
              <button v-if=" isAdmin && currentUserId !== user.id && !user.isDeleted && ( user.rol !== 'ADMINISTRADOR' || currentUsername === 'superAdmin' ) " @click="confirmAction( 'softDelete', user.id, 'Desactivar usuario', '¿Estás seguro de que quieres desactivar este usuario?')" class="icon-button deactivate-button" title="Desactivar" >
                <i class="fas fa-user-slash"></i>
              </button>

              <!-- ACTIVAR (restore) -->
              <button v-if=" isAdmin &&  currentUserId !== user.id &&  user.isDeleted && ( user.rol !== 'ADMINISTRADOR' || currentUsername === 'superAdmin' ) " @click="confirmAction( 'restore', user.id, 'Activar usuario', '¿Estás seguro de que quieres activar este usuario?' )" class="icon-button activate-button" title="Activar" >
                <i class="fas fa-user-check"></i>
              </button>

              <!-- ELIMINAR PERMANENTE -->
              <button v-if=" isAdmin && currentUserId !== user.id &&  user.rol !== 'ADMINISTRADOR' " @click="confirmAction( 'delete', user.id, 'Eliminar usuario', '¿Estás seguro de que quieres eliminar permanentemente este usuario?' )" class="icon-button delete-button" title="Eliminar" >
                <i class="fas fa-trash-alt"></i>
              </button>

            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- Paginación -->
    <div class="pagination-container">
      <button
          @click="prevPage"
          :disabled="currentPage === 1 || loading"
          class="pagination-btn"
          title="Página anterior"
      >
        <i class="fas fa-chevron-left"></i>
      </button>
      <span class="page-info">
            Página {{ currentPage }} de {{ totalPages }}
          </span>
      <button
          @click="nextPage"
          :disabled="currentPage >= totalPages || loading"
          class="pagination-btn"
          title="Página siguiente"
      >
        <i class="fas fa-chevron-right"></i>
      </button>
    </div>

    <!-- Modal para crear/editar usuario -->
    <UserModal
        v-if="showUserModal"
        :show="showUserModal"
        :user="selectedUser"
        :is-admin="isAdmin"
        @close="closeUserModal"
        @save="handleSaveUser"
    />

    <!-- Modal de confirmación -->
    <ConfirmationModal
        v-if="showConfirmationModal"
        :show="showConfirmationModal"
        :title="confirmationTitle"
        :message="confirmationMessage"
        :is-loading="isProcessingAction"
        @confirm="executeAction"
        @cancel="showConfirmationModal = false"
    />
  </div>
</template>

<script>
import { debounce } from 'lodash';
import UsersService from '@/services/users.service';
import UserModal from '@/components/modales/edicion/UserModal.vue';
import ConfirmationModal from '@/components/modales/edicion/ConfirmModal.vue';
import { useAuthStore } from '@/stores/auth';

export default {
  name: 'UsersManagement',
  components: {
    UserModal,
    ConfirmationModal
  },
  data() {
    return {
      users: [],
      isLoading: false,
      isProcessingAction: false,
      showUserModal: false,
      showConfirmationModal: false,
      selectedUser: null,
      actionToConfirm: null,
      actionParams: null,
      confirmationTitle: '',
      confirmationMessage: '',
      filters: {
        search: null,
        rol: null,
        isDeleted: false
      },
      sort: {
        field: 'createdAt',
        direction: 'desc'
      },
      pagination: {
        currentPage: 1,
        itemsPerPage: 10,
        totalItems: 0,
        totalPages: 1,
        from: 0,
        to: 0
      },
      tableHeaders: [
        {text: 'Usuario', value: 'nombre'},
        {text: 'Email', value: 'email'},
        {text: 'Rol', value: 'rol'},
        {text: 'Fecha Creación', value: 'createdAt'},
        {text: 'Estado', value: 'isDeleted'}
      ]
    };
  },
  computed: {
    currentPage() {
      return this.pagination.currentPage;
    },
    totalPages() {
      return this.pagination.totalPages;
    },
    loading() {
      return this.isLoading;
    },
    authStore() {
      return useAuthStore();
    },
    isAdmin() {
      return this.authStore.isAdmin;
    },
    currentUserId() {
      return this.authStore.user?.id;
    },
    currentUsername() {
      return this.authStore.user?.username || '';
    },
    visiblePages() {
      const pages = [];
      const maxVisible = 5;
      let start = Math.max(1, this.pagination.currentPage - Math.floor(maxVisible / 2));
      const end = Math.min(start + maxVisible - 1, this.pagination.totalPages);

      if (end - start + 1 < maxVisible) {
        start = Math.max(1, end - maxVisible + 1);
      }

      for (let i = start; i <= end; i++) {
        pages.push(i);
      }

      return pages;
    }
  },
  created() {
    this.fetchUsers();
  },
  methods: {
    async fetchUsers() {
      this.isLoading = true;
      try {
        const response = await UsersService.fetchUsers(
            this.filters,
            {
              currentPage: this.pagination.currentPage,
              itemsPerPage: this.pagination.itemsPerPage
            },
            this.sort
        );

        this.users = response.content;
        this.pagination.totalItems = response.totalElements;
        this.pagination.totalPages = response.totalPages;
        this.pagination.from = (this.pagination.currentPage - 1) * this.pagination.itemsPerPage + 1;
        this.pagination.to = Math.min(
            this.pagination.currentPage * this.pagination.itemsPerPage,
            this.pagination.totalItems
        );
      } catch (error) {
        console.error('Error fetching users:', error);
        this.$toast.error('Error al cargar los usuarios');
      } finally {
        this.isLoading = false;
      }
    },

    onSearchInput: debounce(function () {
      this.pagination.currentPage = 1;
      this.fetchUsers();
    }, 500),

    sortBy(field) {
      if (this.sort.field === field) {
        this.sort.direction = this.sort.direction === 'asc' ? 'desc' : 'asc';
      } else {
        this.sort.field = field;
        this.sort.direction = 'asc';
      }
      this.fetchUsers();
    },

    resetFilters() {
      this.filters = {
        search: null,
        rol: null,
        isDeleted: false
      };
      this.pagination.currentPage = 1;
      this.fetchUsers();
    },

    prevPage() {
      if (this.pagination.currentPage > 1) {
        this.pagination.currentPage--;
        this.fetchUsers();
      }
    },

    nextPage() {
      if (this.pagination.currentPage < this.pagination.totalPages) {
        this.pagination.currentPage++;
        this.fetchUsers();
      }
    },

    goToPage(page) {
      if (page !== this.pagination.currentPage) {
        this.pagination.currentPage = page;
        this.fetchUsers();
      }
    },

    openCreateModal() {
      this.selectedUser = null;
      this.showUserModal = true;
    },

    openEditModal(user) {
      this.selectedUser = {...user};
      this.showUserModal = true;
    },

    closeUserModal() {
      this.showUserModal = false;
      this.selectedUser = null;
    },

    confirmAction(action, id, title, message) {
      this.actionToConfirm = action;
      this.actionParams = id;
      this.confirmationTitle = title;
      this.confirmationMessage = message;
      this.showConfirmationModal = true;
    },

    async executeAction() {
      this.isProcessingAction = true;
      try {
        let success = false;
        let message = '';

        switch (this.actionToConfirm) {
          case 'delete':
            success = await UsersService.deleteUser(this.actionParams);
            message = 'Usuario eliminado permanentemente';
            break;
          case 'softDelete':
            success = await UsersService.softDeleteUser(this.actionParams);
            message = 'Usuario desactivado correctamente';
            break;
          case 'restore':
            success = await UsersService.restoreUser(this.actionParams);
            message = 'Usuario activado correctamente';
            break;
        }

        if (success) {
          this.$toast.success(message);
          this.fetchUsers();
        }
      } catch (error) {
        console.error('Error performing action:', error);
        this.$toast.error(error.message || 'Error al realizar la acción');
      } finally {
        this.isProcessingAction = false;
        this.showConfirmationModal = false;
        this.actionToConfirm = null;
        this.actionParams = null;
      }
    },

    async handleSaveUser(userData) {
      this.closeUserModal();

      await this.fetchUsers();

      this.$toast.success(
          this.selectedUser
              ? 'Usuario actualizado correctamente'
              : 'Usuario creado correctamente'
      );
    },

    formatDate(dateString) {
      if (!dateString) return 'N/A';
      const date = new Date(dateString);
      return isNaN(date.getTime()) ? 'N/A' :
          date.toLocaleDateString('es-ES', {
            year: 'numeric',
            month: 'short',
            day: 'numeric'
          });
    },

    formatRole(role) {
      const rolesMap = {
        'USUARIO': 'Usuario',
        'ADMINISTRADOR': 'Administrador'
      };
      return rolesMap[role] || role;
    },

    setDefaultAvatar(event) {
      event.target.src = 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_960_720.png';
    },

    roleBadgeClass(role) {
      return {
        'role-user': role === 'USUARIO',
        'role-admin': role === 'ADMINISTRADOR'
      };
    },

    statusBadgeClass(isDeleted) {
      return {
        'status-active': !isDeleted,
        'status-inactive': isDeleted
      };
    }
  }
};
</script>

<style scoped>
/* Estilos base */
.users-management-container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  font-family: 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

/* Header */
.management-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.management-title {
  font-size: 24px;
  font-weight: 600;
  color: #2d3748;
}

/* Filtros */
.filters-panel {
  display: flex;
  gap: 16px;
  align-items: flex-end;
  margin-bottom: 24px;
  padding: 16px;
  background-color: #f7fafc;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.filter-group {
  flex: 1;
  min-width: 180px;
}

.filter-label {
  display: block;
  margin-bottom: 6px;
  font-size: 14px;
  font-weight: 500;
  color: #4a5568;
}

.search-input-container {
  position: relative;
}

.search-icon {
  position: absolute;
  left: 12px;
  top: 50%;
  transform: translateY(-50%);
  color: #a0aec0;
  font-size: 14px;
}

.search-input {
  width: 100%;
  padding: 10px 12px 10px 36px;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.2s;
}

.search-input:focus {
  outline: none;
  border-color: #4299e1;
  box-shadow: 0 0 0 3px rgba(66, 153, 225, 0.2);
}

.filter-select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  font-size: 14px;
  background-color: white;
  appearance: none;
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 20 20'%3e%3cpath stroke='%236b7280' stroke-linecap='round' stroke-linejoin='round' stroke-width='1.5' d='M6 8l4 4 4-4'/%3e%3c/svg%3e");
  background-position: right 0.5rem center;
  background-repeat: no-repeat;
  background-size: 1.5em 1.5em;
}

.filter-select:focus {
  outline: none;
  border-color: #4299e1;
  box-shadow: 0 0 0 3px rgba(66, 153, 225, 0.2);
}

/* Botones */
.action-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 10px 16px;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  border: none;
}

.action-button.primary {
  background-color: #4299e1;
  color: white;
}

.action-button.primary:hover {
  background-color: #3182ce;
}

.action-button.secondary {
  background-color: #edf2f7;
  color: #2d3748;
}

.action-button.secondary:hover {
  background-color: #e2e8f0;
}

.button-icon {
  margin-right: 8px;
  font-size: 14px;
}

.button-text {
  white-space: nowrap;
}

/* Tabla */
.table-wrapper {
  overflow-x: auto;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  background-color: white;
  margin-bottom: 24px;
}

.users-table {
  width: 100%;
  border-collapse: collapse;
}

.table-header {
  padding: 12px 16px;
  text-align: left;
  font-size: 14px;
  font-weight: 600;
  color: #4a5568;
  background-color: #f7fafc;
  border-bottom: 1px solid #e2e8f0;
  cursor: pointer;
  user-select: none;
}

.table-header:hover {
  background-color: #edf2f7;
}

.header-content {
  display: flex;
  align-items: center;
}

.sort-icon {
  margin-left: 8px;
  font-size: 12px;
}

.actions-header {
  text-align: right;
}

/* Filas de la tabla */
.user-row {
  border-bottom: 1px solid #e2e8f0;
  transition: background-color 0.2s;
}

.user-row:hover {
  background-color: #f8fafc;
}

.user-cell {
  padding: 16px;
  vertical-align: middle;
}

.loading-cell, .empty-cell {
  padding: 40px 16px;
  text-align: center;
}

.loading-spinner {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  color: #718096;
}

.loading-spinner i {
  font-size: 24px;
  color: #4299e1;
}

.empty-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  color: #a0aec0;
}

.empty-icon {
  font-size: 24px;
}

/* Información de usuario */
.user-info {
  display: flex;
  align-items: center;
}

.avatar-container {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 12px;
  flex-shrink: 0;
}

.user-avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.user-details {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-weight: 500;
  color: #2d3748;
  margin-bottom: 2px;
}

.user-username, .user-email {
  font-size: 13px;
  color: #718096;
}

.email-cell {
  min-width: 200px;
}

/* Badges */
.role-badge, .status-badge {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}

.role-user {
  background-color: #ebf8ff;
  color: #2b6cb0;
}

.role-admin {
  background-color: #faf5ff;
  color: #6b46c1;
}

.status-active {
  background-color: #f0fff4;
  color: #2f855a;
}

.status-inactive {
  background-color: #fff5f5;
  color: #c53030;
}

/* Botones de acción */
.action-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.icon-button {
  width: 32px;
  height: 32px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  border: none;
  background-color: transparent;
  transition: all 0.2s;
  font-size: 14px;
}

.icon-button:hover {
  transform: translateY(-1px);
}

.edit-button {
  color: #4299e1;
}

.edit-button:hover {
  background-color: #ebf8ff;
}

.deactivate-button {
  color: #ed8936;
}

.deactivate-button:hover {
  background-color: #fffaf0;
}

.activate-button {
  color: #48bb78;
}

.activate-button:hover {
  background-color: #f0fff4;
}

.delete-button {
  color: #f56565;
}

.delete-button:hover {
  background-color: #fff5f5;
}

/* Paginación */
.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  margin-top: auto;
}

.pagination-btn {
  width: 36px;
  height: 36px;
  border: 1px solid #e2e8f0;
  background: #ffffff;
  color: #4a5568;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.pagination-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination-btn:hover:not(:disabled) {
  background: #4299e1;
  color: white;
  border-color: #4299e1;
}

.page-info {
  font-size: 14px;
  color: #718096;
  min-width: 120px;
  text-align: center;
}

/* ==================== */
/* ESTILOS PARA MODO OSCURO */
/* ==================== */

.dark-mode .management-title,
.dark .management-title {
  color: #e2e8f0;
}

.dark-mode .filters-panel,
.dark .filters-panel {
  background-color: #2d3748;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
  border: 1px solid #374151;
}

.dark-mode .filter-label,
.dark .filter-label {
  color: #cbd5e0;
}

.dark-mode .search-input,
.dark-mode .filter-select,
.dark .search-input,
.dark .filter-select {
  background-color: #1f2937;
  border-color: #4a5568;
  color: #e5e7eb;
}

.dark-mode .search-input::placeholder,
.dark .search-input::placeholder {
  color: #9ca3af;
}

.dark-mode .search-input:focus,
.dark-mode .filter-select:focus,
.dark .search-input:focus,
.dark .filter-select:focus {
  border-color: #63b3ed;
  box-shadow: 0 0 0 3px rgba(99, 179, 237, 0.3);
}

.dark-mode .search-icon,
.dark .search-icon {
  color: #718096;
}

.dark-mode .action-button.secondary,
.dark .action-button.secondary {
  background-color: #4a5568;
  color: #e2e8f0;
}

.dark-mode .action-button.secondary:hover,
.dark .action-button.secondary:hover {
  background-color: #718096;
}

.dark-mode .table-wrapper,
.dark .table-wrapper {
  background-color: #1f2937;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);
  border-color: #374151;
}

.dark-mode .users-table,
.dark .users-table {
  background-color: transparent;
}

.dark-mode .table-header,
.dark .table-header {
  background-color: #111827;
  color: #f9fafb;
  border-bottom-color: #374151;
}

.dark-mode .table-header:hover,
.dark .table-header:hover {
  background-color: #4a5568;
}

.dark-mode .user-row,
.dark .user-row {
  border-bottom-color: #4a5568;
}

.dark-mode .user-row:hover,
.dark .user-row:hover {
  background-color: #4a5568;
}

.dark-mode .user-name,
.dark .user-name {
  color: #e2e8f0;
}

.dark-mode .user-username,
.dark-mode .user-email,
.dark .user-username,
.dark .user-email {
  color: #a0aec0;
}

.dark-mode .loading-spinner,
.dark-mode .empty-content,
.dark .loading-spinner,
.dark .empty-content {
  color: #a0aec0;
}

.dark-mode .role-user,
.dark .role-user {
  background-color: rgba(66, 153, 225, 0.1);
  color: #90cdf4;
}

.dark-mode .role-admin,
.dark .role-admin {
  background-color: rgba(102, 126, 234, 0.1);
  color: #a3bffa;
}

.dark-mode .status-active,
.dark .status-active {
  background-color: rgba(72, 187, 120, 0.1);
  color: #9ae6b4;
}

.dark-mode .status-inactive,
.dark .status-inactive {
  background-color: rgba(245, 101, 101, 0.1);
  color: #feb2b2;
}

.dark-mode .pagination-btn,
.dark .pagination-btn {
  background-color: #2d3748;
  border-color: #4a5568;
  color: #cbd5e0;
}

.dark-mode .pagination-btn:hover:not(:disabled),
.dark .pagination-btn:hover:not(:disabled) {
  background-color: #4299e1;
  border-color: #4299e1;
  color: white;
}

.dark-mode .page-info,
.dark .page-info {
  color: #a0aec0;
}

.dark-mode .icon-button.edit-button:hover,
.dark .icon-button.edit-button:hover {
  background-color: rgba(66, 153, 225, 0.1);
}

.dark-mode .icon-button.deactivate-button:hover,
.dark .icon-button.deactivate-button:hover {
  background-color: rgba(237, 137, 54, 0.1);
}

.dark-mode .icon-button.activate-button:hover,
.dark .icon-button.activate-button:hover {
  background-color: rgba(72, 187, 120, 0.1);
}

.dark-mode .icon-button.delete-button:hover,
.dark .icon-button.delete-button:hover {
  background-color: rgba(245, 101, 101, 0.1);
}
</style>