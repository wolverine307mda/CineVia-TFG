package org.wolve.geofilm.users.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
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
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.core.Authentication
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.wolve.geofilm.auth.services.jwt.JwtAuthenticationFilter
import org.wolve.geofilm.auth.services.jwt.JwtService
import org.wolve.geofilm.users.dto.CreateUsuarioRequest
import org.wolve.geofilm.users.dto.UpdateUsuarioRequest
import org.wolve.geofilm.users.dto.UsuarioProfileResponse
import org.wolve.geofilm.users.dto.UsuarioResponse
import org.wolve.geofilm.users.models.RolUsuario
import org.wolve.geofilm.users.models.Usuario
import org.wolve.geofilm.users.repositories.UsuarioRepository
import org.wolve.geofilm.users.services.UsuarioService
import org.wolve.geofilm.utils.pagination.PaginatedResponse
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

 private val sampleUsuario = Usuario(
  id = "user-1",
  username = "usuario",
  email = "user@example.com",
  password = "pwd",
  nombre = "Nombre",
  apellido = "Apellido",
  rol = RolUsuario.USUARIO,
  telefono = "123456",
  fechaNacimiento = LocalDate.of(1990, 1, 1),
  avatar = null,
  createdAt = LocalDateTime.now().minusDays(1),
  updatedAt = LocalDateTime.now().minusDays(1),
  isDelete = false,
  resetPin = null,
  resetPinExpiration = null
 )

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

 @Test
 fun createUsuario() {
  val request = CreateUsuarioRequest(
   username = "usuario1",
   email = "u1@example.com",
   password = "rawpass",
   nombre = "N1",
   apellido = "A1",
   rol = RolUsuario.USUARIO,
   telefono = "111",
   fechaNacimiento = LocalDate.of(1995, 5, 5),
   avatar = null
  )
  val created = sampleUsuario.copy(id = "u-created", username = "usuario1", email = "u1@example.com")

  every { usuarioService.createUsuario(request) } returns created

  mockMvc.perform(
   post("/api/users")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(request))
  )
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.id").value("u-created"))
   .andExpect(jsonPath("$.email").value("u1@example.com"))
   .andExpect(jsonPath("$.username").value("usuario1"))

  verify { usuarioService.createUsuario(request) }
 }

 @Test
 fun createAdmin() {
  val request = CreateUsuarioRequest(
   username = "admin1",
   email = "admin1@example.com",
   password = "rawpass",
   nombre = "Admin",
   apellido = "One",
   rol = RolUsuario.USUARIO, // controller itself doesn’t change rol
   telefono = "222",
   fechaNacimiento = LocalDate.of(1985, 3, 3),
   avatar = null
  )
  val createdAdmin = sampleUsuario.copy(
   id = "u-admin",
   username = "admin1",
   email = "admin1@example.com",
   rol = RolUsuario.ADMINISTRADOR
  )

  every { usuarioService.createAdmin(request) } returns createdAdmin

  mockMvc.perform(
   post("/api/users/admin")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(request))
  )
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.id").value("u-admin"))
   .andExpect(jsonPath("$.rol").value("ADMINISTRADOR"))

  verify { usuarioService.createAdmin(request) }
 }

 @Test
 fun getCurrentUserProfile() {
  val profileResponse = UsuarioProfileResponse(
   id = "user-1",
   username = "usuario",
   email = "user@example.com",
   nombre = "Nombre",
   apellido = "Apellido",
   avatar = null,
   telefono = "123456",
   fechaNacimiento = "1990-01-01"
  )
  val authentication = mockk<Authentication>()
  every { authentication.name } returns "user@example.com"
  every { usuarioService.getCurrentUserProfile("user@example.com") } returns profileResponse

  mockMvc.perform(
   get("/api/users/me").principal(authentication)
  )
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.id").value("user-1"))
   .andExpect(jsonPath("$.email").value("user@example.com"))
   .andExpect(jsonPath("$.nombre").value("Nombre"))

  verify { usuarioService.getCurrentUserProfile("user@example.com") }
 }

 @Test
 fun getByIdUsuarioResponse() {
  val usuario = sampleUsuario.copy(id = "u-42")
  every { usuarioService.findById("u-42") } returns usuario

  mockMvc.perform(get("/api/users/u-42"))
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.id").value("u-42"))
   .andExpect(jsonPath("$.email").value("user@example.com"))

  verify { usuarioService.findById("u-42") }
 }

 @Test
 fun getAllUsuarioResponse() {
  val u1 = UsuarioResponse.fromEntity(sampleUsuario.copy(id = "a"))
  val u2 = UsuarioResponse.fromEntity(sampleUsuario.copy(id = "b"))
  val pageImpl = PageImpl(listOf(u1, u2), PageRequest.of(0, 2), 2)
  every { usuarioService.findAll(any<Pageable>()) } returns pageImpl

  mockMvc.perform(get("/api/users?page=0&size=2"))
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.content.length()").value(2))
   .andExpect(jsonPath("$.content[0].id").value("a"))
   .andExpect(jsonPath("$.totalElements").value(2))

  verify { usuarioService.findAll(any<Pageable>()) }
 }

 @Test
 fun getAllFilteredreturnsPaginated() {
  val uResponse = UsuarioResponse.fromEntity(sampleUsuario)
  val paginated = PaginatedResponse(
   data = listOf(uResponse),
   totalItems = 1,
   totalPages = 1,
   currentPage = 0,
   pageSize = 1
  )
  every {
   usuarioService.findAllFiltered(
    search = "Name",
    rol = RolUsuario.USUARIO,
    isDeleted = false,
    page = 0,
    size = 1,
    sortBy = listOf("email"),
    sortDirection = "asc"
   )
  } returns paginated

  mockMvc.perform(
   get("/api/users/filter")
    .param("page", "0")
    .param("size", "1")
    .param("search", "Name")
    .param("rol", "USUARIO")
    .param("isDeleted", "false")
    .param("sortBy", "email")
    .param("sortDirection", "asc")
  )
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.data[0].id").value("user-1"))
   .andExpect(jsonPath("$.totalItems").value(1))

  verify {
   usuarioService.findAllFiltered(
    search = "Name",
    rol = RolUsuario.USUARIO,
    isDeleted = false,
    page = 0,
    size = 1,
    sortBy = listOf("email"),
    sortDirection = "asc"
   )
  }
 }

 @Test
 fun updatereturnsUpdatedUsuarioResponse() {
  val updateReq = UpdateUsuarioRequest(
   nombre = "Modificado",
   apellido = "Mod",
   telefono = "999",
   rol = "USUARIO",
   fechaNacimiento = LocalDate.of(2000, 2, 2),
   avatar = "avatar.png"
  )
  val updatedEntity = sampleUsuario.copy(
   id = "user-1",
   nombre = "Modificado",
   apellido = "Mod",
   telefono = "999",
   fechaNacimiento = LocalDate.of(2000, 2, 2),
   avatar = "avatar.png"
  )
  every { usuarioService.updateUser("user-1", updateReq) } returns updatedEntity

  mockMvc.perform(
   put("/api/users/user-1")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(updateReq))
  )
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.nombre").value("Modificado"))
   .andExpect(jsonPath("$.apellido").value("Mod"))
   .andExpect(jsonPath("$.rol").value("USUARIO"))

  verify { usuarioService.updateUser("user-1", updateReq) }
 }

 @Test
 fun uploadAvataruploadsAndUpdates() {
  val fileContents = "dummy".toByteArray()
  val multipartFile = MockMultipartFile("file", "avatar.png", MediaType.IMAGE_PNG_VALUE, fileContents)
  val usuarioNoAvatar = sampleUsuario.copy(id = "u-img", avatar = null)
  every { usuarioService.findById("u-img") } returns usuarioNoAvatar
  every { firebaseStorageService.uploadImage(multipartFile, "usuarios", any()) } returns "http://new-avatar-url"
  // Stub updateAvatar to return a Usuario instance instead of Unit
  val afterUpdate = usuarioNoAvatar.copy(avatar = "http://new-avatar-url")
  every { usuarioService.updateAvatar("u-img", "http://new-avatar-url") } returns afterUpdate

  mockMvc.perform(
   multipart("/api/users/u-img/upload-avatar")
    .file(multipartFile)
    .with { request -> request.method = "POST"; request }
  )
   .andExpect(status().isOk)
   .andExpect(content().string("http://new-avatar-url"))

  verify { usuarioService.findById("u-img") }
  verify { firebaseStorageService.uploadImage(multipartFile, "usuarios", any()) }
  verify { usuarioService.updateAvatar("u-img", "http://new-avatar-url") }
 }

 @Test
 fun uploadAvatardeletesOldAndUploads() {
  val oldUrl = "https://storage-download.googleapis.com/movietrip-e3a91.firebasestorage.app/old/path/avatar.png"
  val usuarioOldAvatar = sampleUsuario.copy(id = "u-img2", avatar = oldUrl)
  every { usuarioService.findById("u-img2") } returns usuarioOldAvatar
  every { firebaseStorageService.deleteImage("old/path/avatar.png") } returns true
  val multipartFile = MockMultipartFile("file", "avatar.png", MediaType.IMAGE_PNG_VALUE, "data".toByteArray())
  every { firebaseStorageService.uploadImage(multipartFile, "usuarios", any()) } returns "http://new-avatar"
  val afterUpdate = usuarioOldAvatar.copy(avatar = "http://new-avatar")
  every { usuarioService.updateAvatar("u-img2", "http://new-avatar") } returns afterUpdate

  mockMvc.perform(
   multipart("/api/users/u-img2/upload-avatar")
    .file(multipartFile)
    .with { request -> request.method = "POST"; request }
  )
   .andExpect(status().isOk)
   .andExpect(content().string("http://new-avatar"))

  verify { usuarioService.findById("u-img2") }
  verify { firebaseStorageService.deleteImage("old/path/avatar.png") }
  verify { firebaseStorageService.uploadImage(multipartFile, "usuarios", any()) }
  verify { usuarioService.updateAvatar("u-img2", "http://new-avatar") }
 }

 @Test
 fun uploadAvatardeleteOldAvatarFails() {
  val oldUrl = "https://storage-download.googleapis.com/movietrip-e3a91.firebasestorage.app/old/fail-avatar.png"
  val usuarioOldAvatar = sampleUsuario.copy(id = "u-img3", avatar = oldUrl)
  every { usuarioService.findById("u-img3") } returns usuarioOldAvatar
  every { firebaseStorageService.deleteImage("old/fail-avatar.png") } throws IllegalArgumentException("No existe")
  val multipartFile = MockMultipartFile("file", "avatar.png", MediaType.IMAGE_PNG_VALUE, "data".toByteArray())
  every { firebaseStorageService.uploadImage(multipartFile, "usuarios", any()) } returns "http://new-avatar3"
  val afterUpdate = usuarioOldAvatar.copy(avatar = "http://new-avatar3")
  every { usuarioService.updateAvatar("u-img3", "http://new-avatar3") } returns afterUpdate

  mockMvc.perform(
   multipart("/api/users/u-img3/upload-avatar")
    .file(multipartFile)
    .with { request -> request.method = "POST"; request }
  )
   .andExpect(status().isOk)
   .andExpect(content().string("http://new-avatar3"))

  verify { usuarioService.findById("u-img3") }
  verify { firebaseStorageService.deleteImage("old/fail-avatar.png") }
  verify { firebaseStorageService.uploadImage(multipartFile, "usuarios", any()) }
  verify { usuarioService.updateAvatar("u-img3", "http://new-avatar3") }
 }
}