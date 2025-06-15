package org.wolve.geofilm.auth.services.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.wolve.geofilm.users.repositories.UsuarioRepository

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userRepository: UsuarioRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val jwt = authHeader.substring(7)
            val userEmail = jwtService.extractUserName(jwt)

            if (SecurityContextHolder.getContext().authentication == null) {
                val userDetails = userRepository.findByEmail(userEmail).orElse(null)
                if (userDetails == null) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no encontrado")
                    return
                }
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                } else {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado")
                    return
                }
            }
        } catch (e: Exception) {
            logger.error("Cannot set user authentication: ${e.message}")
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error de autenticación: ${e.message}")
            return
        }

        filterChain.doFilter(request, response)
    }

}