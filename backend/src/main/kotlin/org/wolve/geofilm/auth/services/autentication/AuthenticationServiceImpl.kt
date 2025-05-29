package org.wolve.geofilm.auth.services.autentication

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.wolve.geofilm.auth.dto.JwtAuthenticationResponse
import org.wolve.geofilm.auth.dto.SignUpRequest
import org.wolve.geofilm.auth.dto.SigninRequest
import org.wolve.geofilm.auth.services.jwt.JwtService
import org.wolve.geofilm.users.dto.CreateUsuarioRequest
import org.wolve.geofilm.users.models.RolUsuario
import org.wolve.geofilm.users.repositories.UsuarioRepository
import org.wolve.geofilm.users.mappers.UsuarioMapper
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Service
class AuthenticationServiceImpl(
    private val userRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val usuarioMapper: UsuarioMapper
) : AuthenticationService {

    override fun signup(request: SignUpRequest): JwtAuthenticationResponse {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val fechaNacimientoParseada: LocalDate? = request.fechaNacimiento?.let {
            try {
                LocalDate.parse(it, formatter)
            } catch (e: DateTimeParseException) {
                null
            }
        }

        val createDto = CreateUsuarioRequest(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            nombre = request.firstName,
            apellido = request.lastName,
            telefono = request.phone,
            avatar = request.avatar,
            fechaNacimiento = fechaNacimientoParseada ?: LocalDate.now(),
            rol = RolUsuario.USUARIO
        )
        val user = usuarioMapper.toEntity(createDto)
        userRepository.save(user)
        val jwt = jwtService.generateToken(user)
        return JwtAuthenticationResponse(jwt, user.rol.name)
    }

    override fun signin(request: SigninRequest): JwtAuthenticationResponse {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.email, request.password)
            )
        } catch (e: BadCredentialsException) {
            throw IllegalArgumentException("Correo o contraseña incorrectos")
        }

        val user = userRepository.findByEmail(request.email)
            .orElseThrow { IllegalArgumentException("Usuario no encontrado") }

        val jwt = jwtService.generateToken(user)
        return JwtAuthenticationResponse(jwt, user.rol.name)
    }
}