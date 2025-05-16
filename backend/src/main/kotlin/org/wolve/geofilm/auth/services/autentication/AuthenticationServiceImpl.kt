
package org.wolve.geofilm.auth.services.autentication

import org.springframework.security.authentication.AuthenticationManager
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
        val fechaNacimientoParseada: LocalDate = LocalDate.parse(request.fechaNacimiento, formatter)

        val createDto = CreateUsuarioRequest(
            username = request.username,
            email = request.email,
            password = request.password,
            nombre = request.firstName,
            apellido = request.lastName,
            telefono = request.telefono,
            avatar = request.avatar,
            fechaNacimiento = fechaNacimientoParseada,
            rol = RolUsuario.USUARIO
        )
        val user = usuarioMapper.toEntity(createDto)
        userRepository.save(user)
        val jwt = jwtService.generateToken(user)
        return JwtAuthenticationResponse(jwt, user.rol.name)
    }

    override fun signin(request: SigninRequest): JwtAuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        val user = userRepository.findByEmail(request.email)
            .orElseThrow { IllegalArgumentException("Correo o contraseña incorrectos") }
        val jwt = jwtService.generateToken(user)
        return JwtAuthenticationResponse(jwt, user.rol.name)
    }
}
