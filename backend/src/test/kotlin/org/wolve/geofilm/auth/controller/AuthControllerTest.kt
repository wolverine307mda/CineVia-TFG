/*package org.wolve.geofilm.auth.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.*
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
import org.wolve.geofilm.auth.dto.JwtAuthenticationResponse
import org.wolve.geofilm.auth.dto.SignUpRequest
import org.wolve.geofilm.auth.dto.SigninRequest
import org.wolve.geofilm.auth.services.autentication.AuthenticationService
import org.wolve.geofilm.auth.services.jwt.JwtAuthenticationFilter
import org.wolve.geofilm.auth.services.jwt.JwtService
import org.wolve.geofilm.users.repositories.UsuarioRepository
import org.wolve.geofilm.users.services.UsuarioService
import org.wolve.geofilm.utils.email.EmailService
import javax.sql.DataSource

@ActiveProfiles("test")
@WebMvcTest(
 controllers = [AuthController::class],
 excludeAutoConfiguration = [
  SecurityFilterAutoConfiguration::class,
  DataSourceAutoConfiguration::class,
  HibernateJpaAutoConfiguration::class,
  FlywayAutoConfiguration::class
 ]
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

 @Autowired
 private lateinit var mockMvc: MockMvc

 @Autowired
 private lateinit var objectMapper: ObjectMapper

 @MockkBean
 private lateinit var authenticationService: AuthenticationService

 @MockkBean
 private lateinit var emailService: EmailService

    @MockkBean private lateinit var usuarioService: UsuarioService

 @MockkBean private lateinit var jwtService: JwtService
 @MockkBean private lateinit var usuarioRepository: UsuarioRepository
 @MockkBean private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter
 @MockkBean private lateinit var dataSource: DataSource

 private lateinit var signUpRequest: SignUpRequest
 private lateinit var signinRequest: SigninRequest
 private lateinit var jwtResponse: JwtAuthenticationResponse
 @MockkBean private lateinit var flyway: Flyway


 @BeforeEach
 fun setUp() {
  every { flyway.migrate() } returns mockk()

  signUpRequest = SignUpRequest(
   username = "testuser",
   email = "test@example.com",
   password = "password123",
   firstName = "Test",
   lastName = "User",
   phone = "123456789",
   fechaNacimiento = "2000-01-01",
   avatar = "https://example.com/avatar.png"
  )

  signinRequest = SigninRequest(
   email = "test@example.com",
   password = "password123"
  )

  jwtResponse = JwtAuthenticationResponse(
   token = "jwt.token.here",
   role = "USUARIO"
  )
 }

 @Test
 fun registroUsuarioExitoso() {
  // Given
  whenever(authenticationService.signup(any<SignUpRequest>())).thenReturn(jwtResponse)

  // When & Then
  mockMvc.perform(
   post("/auth/signup")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(signUpRequest))
  )
   .andExpect(status().isOk)
   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
   .andExpect(jsonPath("$.token").value("jwt.token.here"))
   .andExpect(jsonPath("$.role").value("USUARIO"))

  verify(authenticationService).signup(argThat<SignUpRequest> {
   username == "testuser" &&
           email == "test@example.com" &&
           password == "password123" &&
           firstName == "Test" &&
           lastName == "User"
  })
 }

 @Test
 fun registroUsuarioConDatosIncompletos() {
  // Given
  val requestIncompleto = SignUpRequest(
   username = "",
   email = "test@example.com",
   password = "password123",
   firstName = "Test",
   lastName = "User",
   phone = "123456789",
   fechaNacimiento = "2000-01-01",
   avatar = "https://example.com/avatar.png"
  )

  // When & Then
  mockMvc.perform(
   post("/auth/signup")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(requestIncompleto))
  )
   .andExpect(status().isBadRequest)

  verify(authenticationService, never()).signup(any())
 }

 @Test
 fun registroUsuarioConEmailInvalido() {
  // Given
  val requestEmailInvalido = signUpRequest.copy(email = "email-invalido")

  // When & Then
  mockMvc.perform(
   post("/auth/signup")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(requestEmailInvalido))
  )
   .andExpect(status().isBadRequest)

  verify(authenticationService, never()).signup(any())
 }

 @Test
 fun registroUsuarioConPasswordCorta() {
  // Given
  val requestPasswordCorta = signUpRequest.copy(password = "123")

  // When & Then
  mockMvc.perform(
   post("/auth/signup")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(requestPasswordCorta))
  )
   .andExpect(status().isBadRequest)

  verify(authenticationService, never()).signup(any())
 }

 @Test
 fun registroUsuarioConErrorDelServicio() {
  // Given
  whenever(authenticationService.signup(any<SignUpRequest>()))
   .thenThrow(IllegalArgumentException("Email ya existe"))

  // When & Then
  mockMvc.perform(
   post("/auth/signup")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(signUpRequest))
  )
   .andExpect(status().isBadRequest)
   .andExpect(jsonPath("$.error").value("Email ya existe"))

  verify(authenticationService).signup(any())
 }

 @Test
 fun loginUsuarioExitoso() {
  // Given
  whenever(authenticationService.signin(any<SigninRequest>())).thenReturn(jwtResponse)

  // When & Then
  mockMvc.perform(
   post("/auth/signin")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(signinRequest))
  )
   .andExpect(status().isOk)
   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
   .andExpect(jsonPath("$.token").value("jwt.token.here"))
   .andExpect(jsonPath("$.role").value("USUARIO"))

  verify(authenticationService).signin(argThat<SigninRequest> {
   email == "test@example.com" && password == "password123"
  })
 }

 @Test
 fun loginUsuarioConDatosIncompletos() {
  // Given
  val requestIncompleto = SigninRequest(email = "", password = "password123")

  // When & Then
  mockMvc.perform(
   post("/auth/signin")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(requestIncompleto))
  )
   .andExpect(status().isBadRequest)

  verify(authenticationService, never()).signin(any())
 }

 @Test
 fun loginUsuarioConCredencialesIncorrectas() {
  // Given
  whenever(authenticationService.signin(any<SigninRequest>()))
   .thenThrow(IllegalArgumentException("Correo o contraseña incorrectos"))

  // When & Then
  mockMvc.perform(
   post("/auth/signin")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(signinRequest))
  )
   .andExpect(status().isBadRequest)
   .andExpect(jsonPath("$.error").value("Correo o contraseña incorrectos"))

  verify(authenticationService).signin(any())
 }

 @Test
 fun loginUsuarioConEmailInvalido() {
  // Given
  val requestEmailInvalido = signinRequest.copy(email = "email-invalido")

  // When & Then
  mockMvc.perform(
   post("/auth/signin")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(requestEmailInvalido))
  )
   .andExpect(status().isBadRequest)

  verify(authenticationService, never()).signin(any())
 }

 @Test
 fun registroUsuarioSinContentType() {
  // When & Then
  mockMvc.perform(
   post("/auth/signup")
    .content(objectMapper.writeValueAsString(signUpRequest))
  )
   .andExpect(status().isUnsupportedMediaType)

  verify(authenticationService, never()).signup(any())
 }

 @Test
 fun loginUsuarioSinContentType() {
  // When & Then
  mockMvc.perform(
   post("/auth/signin")
    .content(objectMapper.writeValueAsString(signinRequest))
  )
   .andExpect(status().isUnsupportedMediaType)

  verify(authenticationService, never()).signin(any())
 }

 @Test
 fun registroUsuarioConFechaNacimientoNula() {
  // Given
  val requestFechaNula = signUpRequest.copy(fechaNacimiento = null)
  whenever(authenticationService.signup(any<SignUpRequest>())).thenReturn(jwtResponse)

  // When & Then
  mockMvc.perform(
   post("/auth/signup")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(requestFechaNula))
  )
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.token").value("jwt.token.here"))

  verify(authenticationService).signup(argThat<SignUpRequest> {
   fechaNacimiento == null
  })
 }

 @Test
 fun endpointInexistenteDevuelve404() {
  // When & Then
  mockMvc.perform(get("/auth/endpoint-inexistente"))
   .andExpect(status().isNotFound)

  verify(authenticationService, never()).signup(any())
  verify(authenticationService, never()).signin(any())
 }

 @Test
 fun metodoHttpIncorrectoDevuelve405() {
  // When & Then
  mockMvc.perform(get("/auth/signup"))
   .andExpect(status().isMethodNotAllowed)

  verify(authenticationService, never()).signup(any())
 }

}*/