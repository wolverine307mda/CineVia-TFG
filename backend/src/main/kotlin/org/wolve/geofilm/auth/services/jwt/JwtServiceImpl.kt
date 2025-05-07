package org.wolve.geofilm.auth.services.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.wolve.geofilm.config.auth.JwtConfig
import org.wolve.geofilm.users.models.Usuario
import java.util.*

@Service
class JwtServiceImpl(
    private val jwtConfig: JwtConfig
) : JwtService {

    // Cambiar esto para usar consistentemente el email como subject
    override fun extractUserName(token: String): String {
        return extractClaim(token) { it.subject } // Usar subject en lugar de claim "email"
    }

    override fun generateToken(userDetails: UserDetails): String {
        return generateToken(emptyMap(), userDetails)
    }

    override fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUserName(token) // Ahora usa el subject (email)
        return (username == userDetails.username) && !isTokenExpired(token)
    }

    private fun <T> extractClaim(token: String, claimsResolver: (DecodedJWT) -> T): T {
        val decodedJWT = JWT.decode(token)
        return claimsResolver(decodedJWT)
    }

    private fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
        val now = Date()
        val expirationDate = Date(now.time + (1000 * jwtConfig.expiration))
        val algorithm = Algorithm.HMAC512(getSigningKey())

        val usuario = userDetails as Usuario

        return JWT.create()
            .withHeader(mapOf("typ" to "JWT"))
            .withSubject(usuario.email) // Usar email como subject consistentemente
            .withIssuedAt(now)
            .withExpiresAt(expirationDate)
            .withClaim("userId", usuario.id.toString())
            .withClaim("role", usuario.rol.toString())
            .withClaim("email", usuario.email) // Mantener como claim adicional
            .withClaim("username", usuario.username)
            .sign(algorithm)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token) { it.expiresAt }
    }

    private fun getSigningKey(): ByteArray {
        return Base64.getEncoder().encode(jwtConfig.secret.toByteArray())
    }
}