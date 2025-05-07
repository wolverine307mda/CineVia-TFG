package org.wolve.geofilm.users.dto

import org.wolve.geofilm.users.models.RolUsuario
import java.time.LocalDateTime

data class CreateUsuarioRequest(
    val username: String,
    val email: String,
    val password: String,
    val nombre: String,
    val apellido: String?,
    val rol: RolUsuario = RolUsuario.USUARIO,
    val telefono: String?,
    val fechaNacimiento: LocalDateTime?,
    val avatar: String?
)