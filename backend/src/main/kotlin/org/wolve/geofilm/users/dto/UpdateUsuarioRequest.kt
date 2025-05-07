package org.wolve.geofilm.users.dto

import java.time.LocalDateTime

data class UpdateUsuarioRequest(
    val nombre: String?,
    val apellido: String?,
    val telefono: String?,
    val fechaNacimiento: LocalDateTime?,
    val avatar: String?
)