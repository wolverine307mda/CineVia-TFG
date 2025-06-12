package org.wolve.geofilm.auth.services.autentication

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.*
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.wolve.geofilm.auth.dto.JwtAuthenticationResponse
import org.wolve.geofilm.auth.dto.SignUpRequest
import org.wolve.geofilm.auth.dto.SigninRequest
import org.wolve.geofilm.auth.services.jwt.JwtService
import org.wolve.geofilm.users.dto.CreateUsuarioRequest
import org.wolve.geofilm.users.models.RolUsuario
import org.wolve.geofilm.users.models.Usuario
import org.wolve.geofilm.users.repositories.UsuarioRepository
import org.wolve.geofilm.users.mappers.UsuarioMapper
import java.time.LocalDate
import java.util.*

class AuthenticationServiceImplTest {

 @Mock
 private lateinit var userRepository: UsuarioRepository

 @Mock
 private lateinit var passwordEncoder: PasswordEncoder

 @Mock
 private lateinit var jwtService: JwtService

 @Mock
 private lateinit var authenticationManager: AuthenticationManager

 @Mock
 private lateinit var usuarioMapper: UsuarioMapper

 private lateinit var authenticationService: AuthenticationServiceImpl

 private lateinit var mockUsuario: Usuario

 @BeforeEach
 fun setUp() {
  MockitoAnnotations.openMocks(this)
  authenticationService = AuthenticationServiceImpl(
   userRepository,
   passwordEncoder,
   jwtService,
   authenticationManager,
   usuarioMapper
  )

  mockUsuario = Usuario().apply {
   id = "kvrfvsv"
   email = "test@example.com"
   password = "encodedPassword"
   nombre = "Test"
   apellido = "User"
   rol = RolUsuario.USUARIO
  }
 }

 @Test
 fun signUpExitoso() {
  // Given
  val signUpRequest = SignUpRequest(
   username = "testuser",
   email = "test@example.com",
   password = "password123",
   firstName = "Test",
   lastName = "User",
   phone = "123456789",
   fechaNacimiento = "2000-01-01",
   avatar = "https://example.com/avatar.png"
  )

  val encodedPassword = "encodedPassword"
  val jwtToken = "jwt.token.here"
  val expectedResponse = JwtAuthenticationResponse(jwtToken, "USUARIO")

  whenever(passwordEncoder.encode(signUpRequest.password)).thenReturn(encodedPassword)
  whenever(usuarioMapper.toEntity(any<CreateUsuarioRequest>())).thenReturn(mockUsuario)
  whenever(userRepository.save(any<Usuario>())).thenReturn(mockUsuario)
  whenever(jwtService.generateToken(mockUsuario)).thenReturn(jwtToken)

  // When
  val result = authenticationService.signup(signUpRequest)

  // Then
  assertEquals(expectedResponse.token, result.token)
  assertEquals(expectedResponse.role, result.role)

  verify(passwordEncoder).encode(signUpRequest.password)
  verify(usuarioMapper).toEntity(any<CreateUsuarioRequest>())
  verify(userRepository).save(mockUsuario)
  verify(jwtService).generateToken(mockUsuario)
 }

 @Test
 fun signUpConFechaNacimientoNula() {
  // Given
  val signUpRequest = SignUpRequest(
   username = "testuser",
   email = "test@example.com",
   password = "password123",
   firstName = "Test",
   lastName = "User",
   phone = "123456789",
   fechaNacimiento = null,
   avatar = "https://example.com/avatar.png"
  )

  val encodedPassword = "encodedPassword"
  val jwtToken = "jwt.token.here"

  whenever(passwordEncoder.encode(signUpRequest.password)).thenReturn(encodedPassword)
  whenever(usuarioMapper.toEntity(any<CreateUsuarioRequest>())).thenReturn(mockUsuario)
  whenever(userRepository.save(any<Usuario>())).thenReturn(mockUsuario)
  whenever(jwtService.generateToken(mockUsuario)).thenReturn(jwtToken)

  // When
  val result = authenticationService.signup(signUpRequest)

  // Then
  assertNotNull(result)
  assertEquals(jwtToken, result.token)
  assertEquals("USUARIO", result.role)

  // Verificar que se usa LocalDate.now() cuando fechaNacimiento es null
  verify(usuarioMapper).toEntity(argThat<CreateUsuarioRequest> {
   fechaNacimiento == LocalDate.now() || fechaNacimiento.isEqual(LocalDate.now())
  })
 }

 @Test
 fun signUpConFechaNacimientoInvalida() {
  // Given
  val signUpRequest = SignUpRequest(
   username = "testuser",
   email = "test@example.com",
   password = "password123",
   firstName = "Test",
   lastName = "User",
   phone = "123456789",
   fechaNacimiento = "fecha-invalida",
   avatar = "https://example.com/avatar.png"
  )

  val encodedPassword = "encodedPassword"
  val jwtToken = "jwt.token.here"

  whenever(passwordEncoder.encode(signUpRequest.password)).thenReturn(encodedPassword)
  whenever(usuarioMapper.toEntity(any<CreateUsuarioRequest>())).thenReturn(mockUsuario)
  whenever(userRepository.save(any<Usuario>())).thenReturn(mockUsuario)
  whenever(jwtService.generateToken(mockUsuario)).thenReturn(jwtToken)

  // When
  val result = authenticationService.signup(signUpRequest)

  // Then
  assertNotNull(result)
  assertEquals(jwtToken, result.token)

  // Verificar que se usa LocalDate.now() cuando la fecha es inválida
  verify(usuarioMapper).toEntity(argThat<CreateUsuarioRequest> {
   fechaNacimiento == LocalDate.now() || fechaNacimiento.isEqual(LocalDate.now())
  })
 }

 @Test
 fun signInExitoso() {
  // Given
  val signinRequest = SigninRequest(
   email = "test@example.com",
   password = "password123"
  )

  val jwtToken = "jwt.token.here"
  val expectedResponse = JwtAuthenticationResponse(jwtToken, "USUARIO")

  whenever(authenticationManager.authenticate(any<UsernamePasswordAuthenticationToken>()))
   .thenReturn(mock())
  whenever(userRepository.findByEmail(signinRequest.email))
   .thenReturn(Optional.of(mockUsuario))
  whenever(jwtService.generateToken(mockUsuario)).thenReturn(jwtToken)

  // When
  val result = authenticationService.signin(signinRequest)

  // Then
  assertEquals(expectedResponse.token, result.token)
  assertEquals(expectedResponse.role, result.role)

  verify(authenticationManager).authenticate(
   UsernamePasswordAuthenticationToken(signinRequest.email, signinRequest.password)
  )
  verify(userRepository).findByEmail(signinRequest.email)
  verify(jwtService).generateToken(mockUsuario)
 }

 @Test
 fun signInConCredencialesIncorrectas() {
  // Given
  val signinRequest = SigninRequest(
   email = "test@example.com",
   password = "wrongpassword"
  )

  whenever(authenticationManager.authenticate(any<UsernamePasswordAuthenticationToken>()))
   .thenThrow(BadCredentialsException("Bad credentials"))

  // When & Then
  val exception = assertThrows<IllegalArgumentException> {
   authenticationService.signin(signinRequest)
  }

  assertEquals("Correo o contraseña incorrectos", exception.message)

  verify(authenticationManager).authenticate(
   UsernamePasswordAuthenticationToken(signinRequest.email, signinRequest.password)
  )
  verify(userRepository, never()).findByEmail(any())
  verify(jwtService, never()).generateToken(any())
 }

 @Test
 fun signInConUsuarioNoEncontrado() {
  // Given
  val signinRequest = SigninRequest(
   email = "nonexistent@example.com",
   password = "password123"
  )

  whenever(authenticationManager.authenticate(any<UsernamePasswordAuthenticationToken>()))
   .thenReturn(mock())
  whenever(userRepository.findByEmail(signinRequest.email))
   .thenReturn(Optional.empty())

  // When & Then
  val exception = assertThrows<IllegalArgumentException> {
   authenticationService.signin(signinRequest)
  }

  assertEquals("Usuario no encontrado", exception.message)

  verify(authenticationManager).authenticate(
   UsernamePasswordAuthenticationToken(signinRequest.email, signinRequest.password)
  )
  verify(userRepository).findByEmail(signinRequest.email)
  verify(jwtService, never()).generateToken(any())
 }

 @Test
 fun signUpGeneraAvatarAleatorio() {
  // Given
  val signUpRequest = SignUpRequest(
   username = "testuser",
   email = "test@example.com",
   password = "password123",
   firstName = "Test",
   lastName = "User",
   phone = "123456789",
   fechaNacimiento = "2000-01-01",
   avatar = "https://example.com/avatar.png"
  )

  whenever(passwordEncoder.encode(any())).thenReturn("encodedPassword")
  whenever(usuarioMapper.toEntity(any<CreateUsuarioRequest>())).thenReturn(mockUsuario)
  whenever(userRepository.save(any<Usuario>())).thenReturn(mockUsuario)
  whenever(jwtService.generateToken(any())).thenReturn("jwt.token")

  // When
  authenticationService.signup(signUpRequest)

  // Then
  verify(usuarioMapper).toEntity(argThat<CreateUsuarioRequest> {
   avatar?.contains("foto_perfil_")
   avatar?.contains(".png")
      avatar?.contains("fotosPerfil%2F") == true
  })
 }

 @Test
 fun signUpConFechaValidaEnFormatoISO() {
  // Given
  val fechaValida = "1995-12-25"
  val signUpRequest = SignUpRequest(
   username = "testuser",
   email = "test@example.com",
   password = "password123",
   firstName = "Test",
   lastName = "User",
   phone = "123456789",
   fechaNacimiento = fechaValida,
   avatar = "https://example.com/avatar.png"
  )

  whenever(passwordEncoder.encode(any())).thenReturn("encodedPassword")
  whenever(usuarioMapper.toEntity(any<CreateUsuarioRequest>())).thenReturn(mockUsuario)
  whenever(userRepository.save(any<Usuario>())).thenReturn(mockUsuario)
  whenever(jwtService.generateToken(any())).thenReturn("jwt.token")

  // When
  authenticationService.signup(signUpRequest)

  // Then
  verify(usuarioMapper).toEntity(argThat<CreateUsuarioRequest> {
   fechaNacimiento == LocalDate.parse(fechaValida)
  })
 }
}