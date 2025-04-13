package org.wolve.geofilm.users.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.wolve.geofilm.users.models.Usuario
import java.util.*

@Repository
interface UsuarioRepository : JpaRepository<Usuario, UUID> {

    fun findByEmail(email: String): Optional<Usuario>

    fun findByUsername(username: String): Optional<Usuario>

    fun existsByEmail(email: String): Boolean

    fun existsByUsername(username: String): Boolean
}
