package org.wolve.geofilm.users.dto

import org.wolve.geofilm.users.models.RolUsuario
import java.time.LocalDate

data class CreateUsuarioRequest(
    val username: String,
    val email: String,
    var password: String,
    val nombre: String,
    val apellido: String?,
    val rol: RolUsuario = RolUsuario.USUARIO,
    val telefono: String?,
    val fechaNacimiento: LocalDate,
    val avatar: String?
)