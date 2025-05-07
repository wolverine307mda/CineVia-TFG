// UsuarioServiceImpl.kt
package org.wolve.geofilm.users.services

import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.wolve.geofilm.users.dto.*
import org.wolve.geofilm.users.mappers.UsuarioMapper
import org.wolve.geofilm.users.models.Usuario
import org.wolve.geofilm.users.repositories.UsuarioRepository
import java.util.*

@Service
class UsuarioServiceImpl(
    private val usuarioRepository: UsuarioRepository,
    private val usuarioMapper: UsuarioMapper
) : UsuarioService {

    override fun createUsuario(dto: CreateUsuarioRequest): Usuario {
        if (usuarioRepository.existsByEmail(dto.email)) {
            throw IllegalArgumentException("El email ya está registrado")
        }
        if (usuarioRepository.existsByUsername(dto.username)) {
            throw IllegalArgumentException("El nombre de usuario ya está en uso")
        }

        val usuario = usuarioMapper.toEntity(dto)
        return usuarioRepository.save(usuario)
    }

    override fun findByUsername(username: String): Usuario {
        return usuarioRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("Usuario no encontrado: $username") }
    }

    override fun findByEmail(email: String): Usuario {
        return usuarioRepository.findByEmail(email)
            .orElseThrow { UsernameNotFoundException("Usuario no encontrado: $email") }
    }

    override fun findById(id: UUID): Usuario {
        return usuarioRepository.findById(id)
            .orElseThrow { NoSuchElementException("Usuario no encontrado con ID: $id") }
    }

    override fun findAll(): List<Usuario> {
        return usuarioRepository.findAll()
    }

    override fun updateUser(id: UUID, dto: UpdateUsuarioRequest): Usuario {
        val usuario = findById(id)
        val updatedUsuario = usuarioMapper.updateEntity(usuario, dto)
        return usuarioRepository.save(updatedUsuario)
    }

    override fun deleteUser(id: UUID) {
        val usuario = findById(id)
        usuario.idDelete = UUID.randomUUID()
        usuarioRepository.save(usuario)
    }

    override fun getCurrentUserProfile(email: String): UsuarioProfileResponse {
        val usuario = findByEmail(email)
        return UsuarioProfileResponse(
            id = usuario.id,
            username = usuario.username,
            email = usuario.email,
            nombre = usuario.nombre,
            apellido = usuario.apellido,
            avatar = usuario.avatar,
            telefono = usuario.telefono,
            fechaNacimiento = usuario.fechaNacimiento?.toString(),
            stats = getProfileStats(usuario.id),
            favorites = getRecentFavorites(usuario.id),
            reviews = getRecentReviews(usuario.id)
        )
    }

    override fun getProfileStats(userId: String): Map<String, Int> {
        // Implementar lógica real aquí
        return mapOf(
            "favorites" to 0,
            "reviews" to 0,
            "comments" to 0
        )
    }

    override fun getRecentFavorites(userId: String): List<Any> {
        // Implementar lógica real aquí
        return emptyList()
    }

    override fun getRecentReviews(userId: String): List<Any> {
        // Implementar lógica real aquí
        return emptyList()
    }

    override fun loadUserByUsername(username: String): Usuario {
        // Primero intenta por email, luego por username
        return try {
            findByEmail(username)
        } catch (e: UsernameNotFoundException) {
            findByUsername(username)
        }
    }
}