package org.wolve.geofilm.auth.services.autentication

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.wolve.geofilm.auth.dto.JwtAuthenticationResponse
import org.wolve.geofilm.auth.dto.SignUpRequest
import org.wolve.geofilm.auth.dto.SigninRequest
import org.wolve.geofilm.auth.services.jwt.JwtService
import org.wolve.geofilm.users.models.RolUsuario
import org.wolve.geofilm.users.models.Usuario
import org.wolve.geofilm.users.repositories.UsuarioRepository

@Service
class AuthenticationServiceImpl(
    private val userRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) : AuthenticationService {

    override fun signup(request: SignUpRequest): JwtAuthenticationResponse {
        val user = Usuario(
            username = request.username,
            nombre = request.firstName,
            apellido = request.lastName,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            rol = RolUsuario.USUARIO
        )
        userRepository.save(user)
        val jwt = jwtService.generateToken(user)
        return JwtAuthenticationResponse(jwt, user.rol.toString())
    }

    override fun signin(request: SigninRequest): JwtAuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        val user = userRepository.findByEmail(request.email)
            .orElseThrow { IllegalArgumentException("Correo o contraseña incorrectos") }
        val jwt = jwtService.generateToken(user)
        return JwtAuthenticationResponse(jwt, user.rol.toString())
    }

}
