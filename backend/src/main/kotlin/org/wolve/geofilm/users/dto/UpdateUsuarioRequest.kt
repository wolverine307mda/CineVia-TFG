package org.wolve.geofilm.users.dto

import java.time.LocalDate

data class UpdateUsuarioRequest(
    val nombre: String?,
    val apellido: String?,
    val telefono: String?,
    val rol: String?,
    val fechaNacimiento: LocalDate?,
    val avatar: String?
)