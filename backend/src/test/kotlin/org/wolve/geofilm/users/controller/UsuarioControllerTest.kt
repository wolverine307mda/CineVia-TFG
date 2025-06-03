package org.wolve.geofilm.users.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.wolve.geofilm.auth.services.jwt.JwtAuthenticationFilter
import org.wolve.geofilm.auth.services.jwt.JwtService
import org.wolve.geofilm.users.dto.*
import org.wolve.geofilm.users.models.RolUsuario
import org.wolve.geofilm.users.services.UsuarioService
import org.wolve.geofilm.users.repositories.UsuarioRepository
import org.wolve.geofilm.utils.storage.images.FirebaseStorageService
import java.time.LocalDate
import java.time.LocalDateTime
import javax.sql.DataSource

@ActiveProfiles("test")
@WebMvcTest(
 controllers = [UsuarioController::class],
 excludeAutoConfiguration = [
  SecurityFilterAutoConfiguration::class,
  DataSourceAutoConfiguration::class,
  HibernateJpaAutoConfiguration::class,
  FlywayAutoConfiguration::class
 ]
)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

 @MockkBean lateinit var jwtService: JwtService
 @MockkBean lateinit var usuarioRepository: UsuarioRepository
 @MockkBean lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter
 @MockkBean lateinit var dataSource: DataSource
 @MockkBean lateinit var flyway: Flyway
 @MockkBean lateinit var firebaseStorageService: FirebaseStorageService
 @MockkBean lateinit var usuarioService: UsuarioService

 @Autowired lateinit var mockMvc: MockMvc
 @Autowired lateinit var objectMapper: ObjectMapper

 private val userResponse = UsuarioResponse(
  id = "user-1",
  nombre = "mario",
  apellido = "de Domingo",
  username = "usuario",
  email = "test@email.com",
  rol = RolUsuario.USUARIO.toString(),
  avatar = null,
  createdAt = LocalDateTime.now(),
  fechaNacimiento = LocalDate.now(),
  isDeleted = false
 )

 @BeforeEach
 fun setup() {
  every { flyway.migrate() } returns mockk()
 }

 @Test
 fun deleteUsuario() {
  justRun { usuarioService.deleteUser("user-1") }

  mockMvc.perform(delete("/api/users/user-1"))
   .andExpect(status().isNoContent)
 }

 @Test
 fun desactivarUsuario() {
  justRun { usuarioService.softDelete("user-1") }

  mockMvc.perform(delete("/api/users/soft/user-1"))
   .andExpect(status().isNoContent)
 }

 @Test
 fun restoreUsuario() {
  justRun { usuarioService.restoreUser("user-1") }

  mockMvc.perform(post("/api/users/restore/user-1"))
   .andExpect(status().isNoContent)
 }

 @Test
 fun shouldUsernameDisponible() {
  every { usuarioService.existsByUsername("usuario") } returns false

  mockMvc.perform(get("/api/users/check-username").param("username", "usuario"))
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.available").value(true))
 }

 @Test
 fun shouldEmailDisponible() {
  every { usuarioService.existsByEmail("test@email.com") } returns true

  mockMvc.perform(get("/api/users/check-email").param("email", "test@email.com"))
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.available").value(false))
 }
}