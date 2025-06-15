package org.wolve.geofilm.users.repositories

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.wolve.geofilm.users.models.RolUsuario
import org.wolve.geofilm.users.models.Usuario
import java.util.*

@Repository
interface UsuarioRepository : JpaRepository<Usuario, String> {

    fun findByEmail(email: String): Optional<Usuario>

    fun findByUsername(username: String): Optional<Usuario>

    fun existsByEmail(email: String): Boolean

    fun existsByUsername(username: String): Boolean

    @Query("SELECT u FROM Usuario u WHERE u.isDelete = :isDeleted")
    fun findAllByIsDeleted(isDeleted: Boolean, pageable: Pageable): Page<Usuario>

    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol")
    fun findAllByRol(rol: RolUsuario, pageable: Pageable): Page<Usuario>

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%'))")
    fun searchAll(search: String, pageable: Pageable): Page<Usuario>
}