package org.wolve.geofilm.auth.dto

data class SignUpRequest(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val phone: String,
    val avatar: String?,
    val fechaNacimiento: String?
)
