package org.wolve.geofilm.users.dto

data class UsuarioProfileResponse(
    val id: String,
    val username: String,
    val email: String,
    val nombre: String,
    val apellido: String?,
    val avatar: String?,
    val telefono: String?,
    val fechaNacimiento: String?,
    val stats: Map<String, Int>,
    val favorites: List<Any>,
    val reviews: List<Any>
)