package org.wolve.geofilm.auth.controller

import org.wolve.geofilm.utils.email.EmailService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.wolve.geofilm.auth.dto.JwtAuthenticationResponse
import org.wolve.geofilm.auth.dto.SignUpRequest
import org.wolve.geofilm.auth.dto.SigninRequest
import org.wolve.geofilm.auth.services.autentication.AuthenticationService
import org.wolve.geofilm.users.services.UsuarioService
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticación", description = "Endpoints para autenticación de usuarios")
class AuthController(
    private val authenticationService: AuthenticationService,
    private val emailService: EmailService,
    private val usuarioService: UsuarioService
) {

    @PostMapping("/signup")
    @Operation(summary = "Registrar un nuevo usuario", description = "Registra un usuario en el sistema y devuelve un token JWT.")
    fun signup(@RequestBody request: SignUpRequest): ResponseEntity<JwtAuthenticationResponse> {
        // Verificar si el email ya existe
        if (usuarioService.existsByEmail(request.email)) {
            throw IllegalArgumentException("El correo electrónico ya está registrado")
        }

        // Verificar si el username ya existe
        if (usuarioService.existsByUsername(request.username)) {
            throw IllegalArgumentException("El nombre de usuario ya está en uso")
        }

        val response = authenticationService.signup(request)

        // Enviar correo de bienvenida
        emailService.sendWelcomeEmail(request.email, request.firstName, request.email)

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/signin")
    fun signin(
        @RequestBody request: SigninRequest,
        httpRequest: HttpServletRequest
    ): ResponseEntity<JwtAuthenticationResponse> {
        val response = authenticationService.signin(request)

        val ipAddress = httpRequest.remoteAddr ?: "Desconocida"
        val userAgent = httpRequest.getHeader("User-Agent") ?: "Desconocido"

        // Usa la información del dispositivo del request o detecta desde el userAgent
        val deviceType = request.deviceInfo?.type ?: detectDeviceType(userAgent)
        val os = request.deviceInfo?.os ?: detectOs(userAgent)
        val browser = request.deviceInfo?.browser ?: detectBrowser(userAgent)

        val userName = usuarioService.findByEmail(request.email).nombre

        emailService.sendLoginAlertEmail(
            request.email,
            userName,
            ipAddress,
            deviceType,
            os,
            browser,
            LocalDateTime.now()
        )

        return ResponseEntity.ok(response)
    }

    private fun detectDeviceType(userAgent: String): String {
        return when {
            userAgent.contains("Mobile") -> "Móvil"
            userAgent.contains("Tablet") -> "Tablet"
            userAgent.contains("Android") -> "Android"
            userAgent.contains("iPhone") -> "iPhone"
            userAgent.contains("iPad") -> "iPad"
            userAgent.contains("Windows") -> "Windows"
            userAgent.contains("Mac") -> "Mac"
            userAgent.contains("Linux") -> "Linux"
            else -> "Desconocido"
        }
    }

    private fun detectOs(userAgent: String): String {
        return when {
            userAgent.contains("Windows") -> "Windows"
            userAgent.contains("Mac") -> "MacOS"
            userAgent.contains("Linux") -> "Linux"
            userAgent.contains("Android") -> "Android"
            userAgent.contains("iPhone") || userAgent.contains("iPad") -> "iOS"
            else -> "Desconocido"
        }
    }

    private fun detectBrowser(userAgent: String): String {
        return when {
            userAgent.contains("Chrome") -> "Chrome"
            userAgent.contains("Firefox") -> "Firefox"
            userAgent.contains("Safari") -> "Safari"
            userAgent.contains("Edge") -> "Edge"
            userAgent.contains("Opera") -> "Opera"
            else -> "Desconocido"
        }
    }
}