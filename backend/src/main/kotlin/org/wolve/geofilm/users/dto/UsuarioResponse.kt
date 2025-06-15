package org.wolve.geofilm.users.dto

import org.wolve.geofilm.users.models.Usuario
import java.time.LocalDate
import java.time.LocalDateTime

data class UsuarioResponse(
    val id: String,
    val username: String,
    val email: String,
    val nombre: String,
    val apellido: String?,
    val rol: String,
    val fechaNacimiento: LocalDate?,
    val avatar: String?,
    val createdAt: LocalDateTime?,
    val isDeleted: Boolean
) {
    companion object {
        fun fromEntity(usuario: Usuario): UsuarioResponse {
            return UsuarioResponse(
                id = usuario.id,
                username = usuario.getNombreUsuario(),
                email = usuario.email,
                nombre = usuario.nombre,
                apellido = usuario.apellido,
                rol = usuario.rol.name,
                avatar = usuario.avatar,
                createdAt = usuario.createdAt,
                isDeleted = usuario.isDelete,
                fechaNacimiento = usuario.fechaNacimiento,
            )
        }
    }
}