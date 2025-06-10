package org.wolve.geofilm.auth.controller

import org.wolve.geofilm.utils.email.EmailService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.wolve.geofilm.auth.dto.JwtAuthenticationResponse
import org.wolve.geofilm.auth.dto.SignUpRequest
import org.wolve.geofilm.auth.dto.SigninRequest
import org.wolve.geofilm.auth.services.autentication.AuthenticationService
import org.wolve.geofilm.producciones.produccion.service.ProduccionServiceImpl
import org.wolve.geofilm.users.models.RolUsuario
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

    private val logger = LoggerFactory.getLogger(ProduccionServiceImpl::class.java)

    @PostMapping("/signup")
    @Operation(
        summary = "Registrar un nuevo usuario",
        description = "Registra un usuario en el sistema y devuelve un token JWT."
    )
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

        //nombre completo, unimos nombre y apelido
        val fullName = "${request.firstName} ${request.lastName}"

        // Enviar correo de bienvenida
        emailService.sendWelcomeEmail(request.email, fullName, request.email, request.username)

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @PostMapping("/signin")
    fun signin(@RequestBody request: SigninRequest, httpRequest: HttpServletRequest): ResponseEntity<Any> {
        val usuario = usuarioService.findByEmail(request.email)
        if (usuario.isDelete) {
            logger.warn("Intento de inicio de sesión con usuario desactivado: ${usuario.email}")
            return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(mapOf("message" to "Usuario desactivado"))
        }

        val response = authenticationService.signin(request)
        val ipAddress = httpRequest.remoteAddr ?: "Desconocida"
        val userAgent = httpRequest.getHeader("User-Agent") ?: "Desconocido"
        val deviceType = request.deviceInfo?.type ?: detectDeviceType(userAgent)
        val os = request.deviceInfo?.os ?: detectOs(userAgent)
        val browser = request.deviceInfo?.browser ?: detectBrowser(userAgent)
        val userName = usuario.nombre

        if (usuario.rol == RolUsuario.ADMINISTRADOR) {
            emailService.sendLoginAlertEmail(
                request.email,
                userName,
                ipAddress,
                deviceType,
                os,
                browser,
                LocalDateTime.now()
            )
        }

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

    @PostMapping("/request-password-reset")
    @Operation(
        summary = "Solicitar restablecimiento de contraseña",
        description = "Envía un PIN al correo electrónico para restablecer la contraseña"
    )
    fun requestPasswordReset(@RequestBody request: PasswordResetRequest): ResponseEntity<ApiResponse> {
        if (!usuarioService.existsByEmail(request.email)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse(false, "No se encontró una cuenta con ese correo electrónico"))
        }

        val pin = generateRandomPin()
        val expirationTime = LocalDateTime.now().plusMinutes(15)

        // Guardar el PIN en la base de datos (implementar en usuarioService)
        usuarioService.savePasswordResetPin(request.email, pin, expirationTime)

        // Enviar correo con el PIN
        emailService.sendPasswordResetPinEmail(request.email, pin)

        return ResponseEntity.ok(ApiResponse(true, "Se ha enviado un PIN a tu correo electrónico"))
    }

    @PostMapping("/verify-reset-pin")
    @Operation(
        summary = "Verificar PIN de restablecimiento",
        description = "Verifica el PIN enviado para restablecer la contraseña"
    )
    fun verifyResetPin(@RequestBody request: VerifyPinRequest): ResponseEntity<ApiResponse> {
        val isValid = usuarioService.validatePasswordResetPin(request.email, request.pin)

        if (!isValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse(false, "PIN inválido o expirado"))
        }

        return ResponseEntity.ok(ApiResponse(true, "PIN verificado correctamente"))
    }

    @PostMapping("/reset-password")
    @Operation(
        summary = "Restablecer contraseña",
        description = "Restablece la contraseña después de verificar el PIN"
    )
    fun resetPassword(@RequestBody request: ResetPasswordRequest): ResponseEntity<ApiResponse> {
        if (request.newPassword != request.confirmPassword) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse(false, "Las contraseñas no coinciden"))
        }

        // Verificar el PIN nuevamente por seguridad
        val isPinValid = usuarioService.validatePasswordResetPin(request.email, request.pin)
        if (!isPinValid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse(false, "PIN inválido o expirado"))
        }

        // Cambiar la contraseña
        usuarioService.resetPassword(request.email, request.newPassword)

        // Invalidar el PIN usado
        usuarioService.invalidatePasswordResetPin(request.email)

        return ResponseEntity.ok(ApiResponse(true, "Contraseña restablecida correctamente"))
    }

    private fun generateRandomPin(): String {
        return (100000..999999).random().toString()
    }
}



data class PasswordResetRequest(val email: String)
data class VerifyPinRequest(val email: String, val pin: String)
data class ResetPasswordRequest(
    val email: String,
    val pin: String,
    val newPassword: String,
    val confirmPassword: String
)

data class ApiResponse(val success: Boolean, val message: String)
