// IUsuarioService.kt
package org.wolve.geofilm.users.services

import org.springframework.security.core.userdetails.UserDetailsService
import org.wolve.geofilm.users.dto.*
import org.wolve.geofilm.users.models.Usuario
import java.util.*

interface UsuarioService : UserDetailsService {
    fun createUsuario(dto: CreateUsuarioRequest): Usuario
    fun findByUsername(username: String): Usuario
    fun findByEmail(email: String): Usuario
    fun findById(id: UUID): Usuario
    fun findAll(): List<Usuario>
    fun updateUser(id: UUID, dto: UpdateUsuarioRequest): Usuario
    fun deleteUser(id: UUID)
    fun getCurrentUserProfile(email: String): UsuarioProfileResponse
    fun getProfileStats(userId: String): Map<String, Int>
    fun getRecentFavorites(userId: String): List<Any>
    fun getRecentReviews(userId: String): List<Any>
}