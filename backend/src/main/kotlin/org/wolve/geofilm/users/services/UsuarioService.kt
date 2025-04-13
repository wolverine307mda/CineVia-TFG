package org.wolve.geofilm.users.services

import org.springframework.security.core.userdetails.UserDetailsService

interface UsuarioService {
    fun userDetailsService(): UserDetailsService
}
