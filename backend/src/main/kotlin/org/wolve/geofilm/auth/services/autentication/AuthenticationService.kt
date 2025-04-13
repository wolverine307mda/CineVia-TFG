package org.wolve.geofilm.auth.services.autentication

import org.wolve.geofilm.auth.dto.JwtAuthenticationResponse
import org.wolve.geofilm.auth.dto.SignUpRequest
import org.wolve.geofilm.auth.dto.SigninRequest

interface AuthenticationService {
    fun signup(request: SignUpRequest): JwtAuthenticationResponse
    fun signin(request: SigninRequest): JwtAuthenticationResponse
}