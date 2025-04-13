package org.wolve.geofilm.auth.dto

data class SigninRequest(
    val email: String,
    val password: String
)