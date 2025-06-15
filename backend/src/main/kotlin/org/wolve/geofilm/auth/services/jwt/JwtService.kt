package org.wolve.geofilm.auth.services.jwt

import org.springframework.security.core.userdetails.UserDetails

interface JwtService {
    fun extractUserName(token: String): String
    fun generateToken(userDetails: UserDetails): String
    fun isTokenValid(token: String, userDetails: UserDetails): Boolean
}