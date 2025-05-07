package org.wolve.geofilm.config.auth

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.wolve.geofilm.auth.services.jwt.JwtAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfiguration(
    private val jwtAuthFilter: JwtAuthenticationFilter
) {

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager {
        return authConfig.authenticationManager
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers(
                        "/api/v1/auth/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/api/producciones/**",
                        "/api/sagas/**",
                        "/api/profesionales/**",
                        "/api/participaciones/**",
                        "/api/ubicaciones/**"
                    ).permitAll()
                    .requestMatchers("/api/admin/**").hasRole("ADMINISTRADOR")
                    .requestMatchers("/api/users/me").authenticated()
                    .anyRequest().authenticated()
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    // En tu SecurityConfig de Spring Boot
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:5173", "http://localhost:5174")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*") // Permitimos todos los headers
        configuration.exposedHeaders = listOf("Authorization")
        configuration.allowCredentials = true

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }


    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}