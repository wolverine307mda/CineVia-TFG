package org.wolve.geofilm.users.services

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.UserDetailsService
import org.wolve.geofilm.users.dto.*
import org.wolve.geofilm.users.models.RolUsuario
import org.wolve.geofilm.users.models.Usuario
import org.wolve.geofilm.utils.pagination.PaginatedResponse
import java.time.LocalDateTime

interface UsuarioService : UserDetailsService {
    // CRUD básico
    fun createUsuario(dto: CreateUsuarioRequest): Usuario
    fun createAdmin(dto: CreateUsuarioRequest): Usuario
    fun findByUsername(username: String): Usuario
    fun findByEmail(email: String): Usuario
    fun findById(id: String): Usuario
    fun findAll(pageable: Pageable): Page<UsuarioResponse>
    fun updateUser(id: String, dto: UpdateUsuarioRequest): Usuario
    fun updateAvatar(id: String, avatarUrl: String): Usuario
    fun deleteUser(id: String)

    fun getCurrentUserProfile(email: String): UsuarioProfileResponse
    fun findAllFiltered( search: String?, rol: RolUsuario?, isDeleted: Boolean?, page: Int, size: Int, sortBy: List<String>, sortDirection: String): PaginatedResponse<UsuarioResponse>

    fun invalidatePasswordResetPin(email: String)
    fun validatePasswordResetPin(email: String, pin: String): Boolean
    fun resetPassword(email: String, newPassword: String)
    fun savePasswordResetPin(email: String, pin: String, expirationTime: LocalDateTime)

    fun softDelete(id: String)
    fun restoreUser(id: String)
    fun existsByEmail(email: String): Boolean
    fun existsByUsername(username: String): Boolean
}