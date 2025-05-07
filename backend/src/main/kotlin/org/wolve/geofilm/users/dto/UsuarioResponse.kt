package org.wolve.geofilm.users.dto

import org.wolve.geofilm.users.models.Usuario
import java.time.LocalDateTime
import java.util.*

data class UsuarioResponse(
    val id: String,
    val username: String,
    val email: String,
    val nombre: String,
    val apellido: String?,
    val rol: String,
    val avatar: String?,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun fromEntity(usuario: Usuario): UsuarioResponse {
            return UsuarioResponse(
                id = usuario.id,
                username = usuario.username,
                email = usuario.email,
                nombre = usuario.nombre,
                apellido = usuario.apellido,
                rol = usuario.rol.name,
                avatar = usuario.avatar,
                createdAt = usuario.createdAt
            )
        }
    }
}