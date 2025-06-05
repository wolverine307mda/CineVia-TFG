package org.wolve.geofilm.users.services

import jakarta.transaction.Transactional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.wolve.geofilm.users.exceptions.UsuarioNotFoundException
import org.wolve.geofilm.users.dto.*
import org.wolve.geofilm.users.mappers.UsuarioMapper
import org.wolve.geofilm.users.models.RolUsuario
import org.wolve.geofilm.users.models.Usuario
import org.wolve.geofilm.users.repositories.UsuarioRepository
import org.wolve.geofilm.utils.pagination.PaginatedResponse
import java.time.LocalDateTime

@Service
@Transactional
class UsuarioServiceImpl(
    private val usuarioRepository: UsuarioRepository,
    private val usuarioMapper: UsuarioMapper,
    private val passwordEncoder: PasswordEncoder
) : UsuarioService {

    override fun createUsuario(dto: CreateUsuarioRequest): Usuario {
        if (usuarioRepository.existsByEmail(dto.email)) {
            throw IllegalArgumentException("El email ya está en uso")
        }
        if (usuarioRepository.existsByUsername(dto.username)) {
            throw IllegalArgumentException("El nombre de usuario ya está en uso")
        }

        val passwordEncript = passwordEncoder.encode(dto.password)
        dto.password = passwordEncript

        val usuario = usuarioMapper.toEntity(dto)
        return usuarioRepository.save(usuario)
    }

    override fun createAdmin(dto: CreateUsuarioRequest): Usuario {
        if (usuarioRepository.existsByEmail(dto.email)) {
            throw IllegalArgumentException("El email ya está en uso")
        }
        if (usuarioRepository.existsByUsername(dto.username)) {
            throw IllegalArgumentException("El nombre de usuario ya está en uso")
        }

        val adminDto = dto.copy(rol = RolUsuario.ADMINISTRADOR)
        val usuario = usuarioMapper.toEntity(adminDto)
        return usuarioRepository.save(usuario)
    }

    override fun findByUsername(username: String): Usuario {
        return usuarioRepository.findByUsername(username)
            .orElseThrow { UsuarioNotFoundException("Usuario con username $username no encontrado") }
    }

    override fun findByEmail(email: String): Usuario {
        return usuarioRepository.findByEmail(email)
            .orElseThrow { UsuarioNotFoundException("Usuario con email $email no encontrado") }
    }

    override fun findById(id: String): Usuario {
        return usuarioRepository.findById(id)
            .orElseThrow { UsuarioNotFoundException("Usuario con id $id no encontrado") }
    }

    override fun findAll(pageable: Pageable): Page<UsuarioResponse> {
        val page = usuarioRepository.findAll(pageable)
        return page.map { UsuarioResponse.fromEntity(it) }
    }

    override fun updateUser(id: String, dto: UpdateUsuarioRequest): Usuario {
        val usuario = findById(id)
        val updatedUsuario = usuarioMapper.updateEntity(usuario, dto)
        return usuarioRepository.save(updatedUsuario)
    }

    override fun updateAvatar(id: String, avatarUrl: String): Usuario {
        val user = usuarioRepository.findById(id)
            .orElseThrow { UsuarioNotFoundException("Usuario no encontrado con ID $id") }

        user.avatar = avatarUrl
        return usuarioRepository.save(user)
    }

    override fun deleteUser(id: String) {
        val usuario = findById(id)
        usuarioRepository.delete(usuario)
    }

    override fun softDelete(id: String) {
        val usuario = findById(id)
        usuario.isDelete = true
        usuarioRepository.save(usuario)
    }

    override fun restoreUser(id: String) {
        val usuario = findById(id)
        usuario.isDelete = false
        usuarioRepository.save(usuario)
    }

    override fun getCurrentUserProfile(email: String): UsuarioProfileResponse {
        val usuario = findByEmail(email)
        return UsuarioProfileResponse(
            id = usuario.id,
            username = usuario.getNombreUsuario(),
            email = usuario.email,
            nombre = usuario.nombre,
            apellido = usuario.apellido,
            avatar = usuario.avatar,
            telefono = usuario.telefono,
            fechaNacimiento = usuario.fechaNacimiento?.toString(),
        )
    }

    override fun findAllFiltered(
        search: String?,
        rol: RolUsuario?,
        isDeleted: Boolean?,
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginatedResponse<UsuarioResponse> {

        val usuarios = usuarioRepository.findAll()

        val filtrados = usuarios.filter { usuario ->
            (search.isNullOrBlank() || usuario.nombre.contains(search, ignoreCase = true) || usuario.email.contains(search, ignoreCase = true)) &&
                    (rol == null || usuario.rol == rol) &&
                    (isDeleted == null || usuario.isDelete == isDeleted)
        }

        val sorted = when {
            sortBy.any { it.equals("nombre", ignoreCase = true) } && sortDirection.equals("asc", ignoreCase = true) ->
                filtrados.sortedBy { it.nombre }
            sortBy.any { it.equals("nombre", ignoreCase = true) } ->
                filtrados.sortedByDescending { it.nombre }
            sortBy.any { it.equals("email", ignoreCase = true) } && sortDirection.equals("asc", ignoreCase = true) ->
                filtrados.sortedBy { it.email }
            sortBy.any { it.equals("email", ignoreCase = true) } ->
                filtrados.sortedByDescending { it.email }
            sortBy.any { it.equals("createdAt", ignoreCase = true) } && sortDirection.equals("asc", ignoreCase = true) ->
                filtrados.sortedBy { it.createdAt }
            else -> filtrados.sortedByDescending { it.createdAt }
        }

        val totalItems = sorted.size.toLong()
        val totalPages = if (size > 0) (totalItems + size - 1) / size else 0
        val paginados = sorted
            .drop(page * size)
            .take(size)
            .map { UsuarioResponse.fromEntity(it) }

        return PaginatedResponse(
            data = paginados,
            totalItems = totalItems,
            totalPages = totalPages.toInt(),
            currentPage = page,
            pageSize = size
        )
    }

    override fun savePasswordResetPin(email: String, pin: String, expirationTime: LocalDateTime) {
        val usuario = usuarioRepository.findByEmail(email)
            .orElseThrow { RuntimeException("Usuario no encontrado con email $email") }

        usuario.resetPin = pin
        usuario.resetPinExpiration = expirationTime
        usuarioRepository.save(usuario)
    }

    override fun validatePasswordResetPin(email: String, pin: String): Boolean {
        val usuario = usuarioRepository.findByEmail(email)
            .orElseThrow { RuntimeException("Usuario no encontrado con email $email") }

        return usuario.resetPin == pin &&
                usuario.resetPinExpiration != null &&
                usuario.resetPinExpiration!!.isAfter(LocalDateTime.now())
    }

    override fun invalidatePasswordResetPin(email: String) {
        val usuario = usuarioRepository.findByEmail(email)
            .orElseThrow { RuntimeException("Usuario no encontrado con email $email") }

        usuario.resetPin = null
        usuario.resetPinExpiration = null
        usuarioRepository.save(usuario)
    }

    override fun resetPassword(email: String, newPassword: String) {
        val usuario = usuarioRepository.findByEmail(email)
            .orElseThrow { RuntimeException("Usuario no encontrado con email $email") }

        usuario.password = passwordEncoder.encode(newPassword)
        usuario.resetPin = null
        usuario.resetPinExpiration = null
        usuarioRepository.save(usuario)
    }



    override fun existsByEmail(email: String): Boolean {
        return usuarioRepository.existsByEmail(email)
    }

    override fun existsByUsername(username: String): Boolean {
        return usuarioRepository.existsByUsername(username)
    }

    override fun loadUserByUsername(username: String) = findByEmail(username)
}