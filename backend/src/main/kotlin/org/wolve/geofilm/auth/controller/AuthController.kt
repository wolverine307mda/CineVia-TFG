package org.wolve.geofilm.auth.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.wolve.geofilm.auth.dto.JwtAuthenticationResponse
import org.wolve.geofilm.auth.dto.SignUpRequest
import org.wolve.geofilm.auth.dto.SigninRequest
import org.wolve.geofilm.auth.services.autentication.AuthenticationService

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Endpoints para autenticación de usuarios")
class AuthController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/signup")
    @Operation(summary = "Registrar un nuevo usuario", description = "Registra un usuario en el sistema y devuelve un token JWT.")
    fun signup(@RequestBody request: SignUpRequest): ResponseEntity<JwtAuthenticationResponse> {
        val response = authenticationService.signup(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/signin")
    @Operation(summary = "Iniciar sesión", description = "Permite a un usuario autenticarse y obtener un token JWT.")
    fun signin(@RequestBody request: SigninRequest): ResponseEntity<JwtAuthenticationResponse> {
        val response = authenticationService.signin(request)
        return ResponseEntity.ok(response)
    }
}
