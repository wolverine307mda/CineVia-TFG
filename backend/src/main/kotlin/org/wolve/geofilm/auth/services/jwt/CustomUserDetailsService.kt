package org.wolve.geofilm.auth.services.jwt

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.wolve.geofilm.users.repositories.UsuarioRepository

@Service
class CustomUserDetailsService(
    private val usuarioRepository: UsuarioRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return usuarioRepository.findByEmail(username)
            .orElseThrow { UsernameNotFoundException("No se encontró el usuario con email: $username") }
    }
}

