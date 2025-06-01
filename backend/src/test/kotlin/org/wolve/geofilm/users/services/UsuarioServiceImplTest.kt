package org.wolve.geofilm.users.services

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.security.crypto.password.PasswordEncoder
import org.wolve.geofilm.users.dto.*
import org.wolve.geofilm.users.exceptions.UsuarioNotFoundException
import org.wolve.geofilm.users.mappers.UsuarioMapper
import org.wolve.geofilm.users.models.RolUsuario
import org.wolve.geofilm.users.models.Usuario
import org.wolve.geofilm.users.repositories.UsuarioRepository
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class UsuarioServiceImplTest {

 @Mock private lateinit var usuarioRepository: UsuarioRepository
 @Mock private lateinit var usuarioMapper: UsuarioMapper
 @Mock private lateinit var passwordEncoder: PasswordEncoder

 private lateinit var service: UsuarioServiceImpl

 @BeforeEach
 fun setUp() {
  service = UsuarioServiceImpl(usuarioRepository, usuarioMapper, passwordEncoder)
 }

 @Test
 fun `createUsuario - valid request saves with encoded password`() {
  val raw = "rawPass"
  val encoded = "encPass"
  val request = CreateUsuarioRequest(
   username = "user1",
   email = "u1@example.com",
   password = raw,
   nombre = "N",
   apellido = "A",
   rol = RolUsuario.USUARIO,
   telefono = "123",
   fechaNacimiento = LocalDate.of(1990,1,1),
   avatar = "avatar.png"
  )
  whenever(usuarioRepository.existsByEmail(request.email)).thenReturn(false)
  whenever(usuarioRepository.existsByUsername(request.username)).thenReturn(false)
  whenever(passwordEncoder.encode(raw)).thenReturn(encoded)
  val entidad = request.apellido?.let {
   request.telefono?.let { it1 ->
    Usuario(
     id = "u1",
     username = request.username,
     email = request.email,
     password = encoded,
     nombre = request.nombre,
     apellido = it,
     rol = request.rol,
     telefono = it1,
     fechaNacimiento = request.fechaNacimiento,
     avatar = request.avatar,
     createdAt = null,
     updatedAt = null
    )
   }
  }
  whenever(usuarioMapper.toEntity(request)).thenReturn(entidad)
  whenever(usuarioRepository.save(entidad)).thenReturn(entidad)

  val result = service.createUsuario(request)

  assertEquals(entidad, result)
  verify(passwordEncoder).encode(raw)
  verify(usuarioRepository).save(entidad)
 }

 @Test
 fun `createUsuario - duplicate email throws exception`() {
  val request = CreateUsuarioRequest(
   username = "u", email = "e", password = "p",
   nombre = "N", apellido = "A", rol = RolUsuario.USUARIO,
   telefono = "", fechaNacimiento = LocalDate.now(), avatar = null
  )
  whenever(usuarioRepository.existsByEmail(request.email)).thenReturn(true)

  assertThrows(IllegalArgumentException::class.java) {
   service.createUsuario(request)
  }
  verify(usuarioRepository).existsByEmail(request.email)
  verify(usuarioRepository, never()).save(any())
 }

 @Test
 fun `createUsuario - duplicate username throws exception`() {
  val request = CreateUsuarioRequest(
   username = "u", email = "e", password = "p",
   nombre = "N", apellido = "A", rol = RolUsuario.USUARIO,
   telefono = "", fechaNacimiento = LocalDate.now(), avatar = null
  )
  whenever(usuarioRepository.existsByEmail(request.email)).thenReturn(false)
  whenever(usuarioRepository.existsByUsername(request.username)).thenReturn(true)

  assertThrows(IllegalArgumentException::class.java) {
   service.createUsuario(request)
  }
  verify(usuarioRepository).existsByUsername(request.username)
  verify(usuarioRepository, never()).save(any())
 }

 /*@Test
 fun `createAdmin - sets role to ADMINISTRADOR and saves`() {
  val request = CreateUsuarioRequest(
   username = "admin", email = "a@e", password = "p",
   nombre = "N", apellido = "A", rol = RolUsuario.USUARIO,
   telefono = "", fechaNacimiento = LocalDate.now(), avatar = null
  )
  whenever(usuarioRepository.existsByEmail(request.email)).thenReturn(false)
  whenever(usuarioRepository.existsByUsername(request.username)).thenReturn(false)
  val adminDto = request.copy(rol = RolUsuario.ADMINISTRADOR)
  val entidad = Usuario(
   id = "u2", username = adminDto.username, email = adminDto.email,
   password = raw = adminDto.password, nombre = adminDto.nombre,
   apellido = adminDto.apellido, rol = RolUsuario.ADMINISTRADOR,
   telefono = adminDto.telefono, fechaNacimiento = adminDto.fechaNacimiento,
   avatar = adminDto.avatar, createdAt = null, updatedAt = null
  )
  whenever(usuarioMapper.toEntity(adminDto)).thenReturn(entidad)
  whenever(usuarioRepository.save(entidad)).thenReturn(entidad)

  val result = service.createAdmin(request)

  assertEquals(entidad, result)
  verify(usuarioMapper).toEntity(adminDto)
  verify(usuarioRepository).save(entidad)
 }

 @Test
 fun `findByUsername - existing returns usuario`() {
  val username = "uX"
  val usuario = Usuario(
   id = "ux", username = username, email = "e",
   password = "p", nombre = "N", apellido = "A",
   rol = RolUsuario.USUARIO, telefono = "", fechaNacimiento = null,
   avatar = null, createdAt = null, updatedAt = null
  )
  whenever(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(usuario))

  val result = service.findByUsername(username)

  assertEquals(usuario, result)
  verify(usuarioRepository).findByUsername(username)
 }*/

 @Test
 fun `findByUsername - missing throws exception`() {
  whenever(usuarioRepository.findByUsername("no")).thenReturn(Optional.empty())

  assertThrows(UsuarioNotFoundException::class.java) {
   service.findByUsername("no")
  }
 }

 @Test
 fun `findByEmail - existing returns usuario`() {
  val email = "eX"
  val usuario = Usuario(
   id = "id", username = "u", email = email,
   password = "p", nombre = "N", apellido = "A",
   rol = RolUsuario.USUARIO, telefono = "", fechaNacimiento = null,
   avatar = null, createdAt = null, updatedAt = null
  )
  whenever(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario))

  val result = service.findByEmail(email)

  assertEquals(usuario, result)
  verify(usuarioRepository).findByEmail(email)
 }

 @Test
 fun `findByEmail - missing throws exception`() {
  whenever(usuarioRepository.findByEmail("no")).thenReturn(Optional.empty())

  assertThrows(UsuarioNotFoundException::class.java) {
   service.findByEmail("no")
  }
 }

 @Test
 fun `findById - existing returns usuario`() {
  val id = "id1"
  val usuario = Usuario(
   id = id, username = "u", email = "e",
   password = "p", nombre = "N", apellido = "A",
   rol = RolUsuario.USUARIO, telefono = "", fechaNacimiento = null,
   avatar = null, createdAt = null, updatedAt = null
  )
  whenever(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario))

  val result = service.findById(id)

  assertEquals(usuario, result)
  verify(usuarioRepository).findById(id)
 }

 @Test
 fun `findById - missing throws exception`() {
  whenever(usuarioRepository.findById("no")).thenReturn(Optional.empty())

  assertThrows(UsuarioNotFoundException::class.java) {
   service.findById("no")
  }
 }

 /*@Test
 fun `findAll returns paginated UsuarioResponse`() {
  val page = 0; val size = 2
  val pageable: Pageable = PageRequest.of(page, size)
  val u1 = Usuario("u1","u","e","p","N","A","USUARIO",null,null,null,null)
  val u2 = Usuario("u2","u2","e2","p2","N2","A2","USUARIO",null,null,null,null)
  val springPage = PageImpl(listOf(u1, u2), pageable, 5)
  whenever(usuarioRepository.findAll(pageable)).thenReturn(springPage)

  val result = service.findAll(pageable)

  assertEquals(5, result.totalElements)
  assertEquals(2, result.content.size)
  assertTrue(result.content.all { it is UsuarioResponse })
  verify(usuarioRepository).findAll(pageable)
 }

 @Test
 fun `updateUser - existing id updates and returns usuario`() {
  val id = "u3"
  val usuario = Usuario(id,"u","e","p","N","A",RolUsuario.USUARIO,"",null,null,null,null)
  val request = UpdateUsuarioRequest(
   nombre = "NX", apellido = "AX", telefono = "1234",
   rol = "ADMINISTRADOR", fechaNacimiento = LocalDate.of(2000,1,1), avatar = "img.png"
  )
  val actualizado = usuario.copy(
   nombre = "NX", apellido = "AX", rol = RolUsuario.ADMINISTRADOR,
   telefono = "1234", fechaNacimiento = LocalDate.of(2000,1,1), avatar = "img.png"
  )
  whenever(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario))
  whenever(usuarioMapper.updateEntity(usuario, request)).thenReturn(actualizado)
  whenever(usuarioRepository.save(actualizado)).thenReturn(actualizado)

  val result = service.updateUser(id, request)

  assertEquals(actualizado, result)
  verify(usuarioMapper).updateEntity(usuario, request)
  verify(usuarioRepository).save(actualizado)
 }*/

 @Test
 fun `updateUser - missing id throws exception`() {
  whenever(usuarioRepository.findById("no")).thenReturn(Optional.empty())
  val request = UpdateUsuarioRequest(null, null, null, null, null, null)

  assertThrows(UsuarioNotFoundException::class.java) {
   service.updateUser("no", request)
  }
 }

 /*@Test
 fun `updateAvatar - existing updates avatar`() {
  val id = "u4"
  val usuario = Usuario(id,"u","e","p","N","A",RolUsuario.USUARIO,"",null,null,null,null)
  whenever(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario))
  whenever(usuarioRepository.save(any())).thenReturn(usuario)

  val result = service.updateAvatar(id, "new.png")

  assertEquals("new.png", result.avatar)
  verify(usuarioRepository).save(usuario)
 }*/

 @Test
 fun `updateAvatar - missing id throws exception`() {
  whenever(usuarioRepository.findById("no")).thenReturn(Optional.empty())

  assertThrows(UsuarioNotFoundException::class.java) {
   service.updateAvatar("no", "img.png")
  }
 }

 /*@Test
 fun `deleteUser - existing deletes user`() {
  val id = "u5"
  val usuario = Usuario(id,"u","e","p","N","A",RolUsuario.USUARIO,"",null,null,null,null)
  whenever(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario))

  service.deleteUser(id)

  verify(usuarioRepository).delete(usuario)
 }*/

 @Test
 fun `deleteUser - missing throws exception`() {
  whenever(usuarioRepository.findById("no")).thenReturn(Optional.empty())

  assertThrows(UsuarioNotFoundException::class.java) {
   service.deleteUser("no")
  }
 }

 /*@Test
 fun `softDelete - sets isDelete true`() {
  val id = "u6"
  val usuario = Usuario(id,"u","e","p","N","A",RolUsuario.USUARIO,"",null,null,null,false,null)
  whenever(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario))
  whenever(usuarioRepository.save(usuario)).thenReturn(usuario)

  service.softDelete(id)

  assertTrue(usuario.isDelete)
 }

 @Test
 fun `restoreUser - sets isDelete false`() {
  val id = "u7"
  val usuario = Usuario(id,"u","e","p","N","A",RolUsuario.USUARIO,"",null,null,null,true,null)
  whenever(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario))
  whenever(usuarioRepository.save(usuario)).thenReturn(usuario)

  service.restoreUser(id)

  assertFalse(usuario.isDelete)
 }

 @Test
 fun `getCurrentUserProfile - existing returns profile`() {
  val email = "eX"
  val usuario = Usuario(
   id = "ux", username = "uX", email = email, password = "p",
   nombre = "N", apellido = "A", rol = RolUsuario.USUARIO,
   telefono = "123", fechaNacimiento = LocalDate.of(1995,5,5),
   avatar = "av.png", createdAt = null, updatedAt = null
  )
  whenever(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario))

  val profile = service.getCurrentUserProfile(email)

  assertEquals("ux", profile.id)
  assertEquals("uX", profile.username)
  assertEquals(email, profile.email)
  assertEquals("N", profile.nombre)
  assertEquals("A", profile.apellido)
  assertEquals("av.png", profile.avatar)
  assertEquals("123", profile.telefono)
  assertEquals("1995-05-05", profile.fechaNacimiento)
 }*/

 @Test
 fun `getCurrentUserProfile - missing throws exception`() {
  whenever(usuarioRepository.findByEmail("no")).thenReturn(Optional.empty())

  assertThrows(UsuarioNotFoundException::class.java) {
   service.getCurrentUserProfile("no")
  }
 }

 /*@Test
 fun `findAllFiltered - no filters returns sorted by createdAt desc`() {
  val u1 = Usuario("u1","u1","e1","p","A","A",RolUsuario.USUARIO,"",LocalDate.of(2020,1,1),null,false,null)
  val u2 = Usuario("u2","u2","e2","p","B","B",RolUsuario.USUARIO,"",LocalDate.of(2021,1,1),null,false,null)
  val list = listOf(u1, u2)
  whenever(usuarioRepository.findAll()).thenReturn(list)

  val result = service.findAllFiltered(null, null, null, 0, 2, listOf(), "")

  assertEquals(2L, result.totalItems)
  assertEquals(1, result.totalPages)
  assertEquals(0, result.currentPage)
  assertEquals(2, result.pageSize)
  assertEquals( listOf(UsuarioResponse.fromEntity(u2), UsuarioResponse.fromEntity(u1)), result.data)
 }

 @Test
 fun `findAllFiltered - search and role and isDelete filters and sorts by nombre asc`() {
  val u1 = Usuario("u1","u1","ae@e","p","Alpha","X",RolUsuario.ADMINISTRADOR,"",null,null,false,null)
  val u2 = Usuario("u2","u2","be@e","p","Beta","Y",RolUsuario.USUARIO,"",null,null,true,null)
  val u3 = Usuario("u3","u3","ce@e","p","Gamma","Z",RolUsuario.ADMINISTRADOR,"",null,null,false,null)
  whenever(usuarioRepository.findAll()).thenReturn(listOf(u1, u2, u3))

  val result = service.findAllFiltered(
   search = "a",
   rol = RolUsuario.ADMINISTRADOR,
   isDeleted = false,
   page = 0,
   size = 10,
   sortBy = listOf("nombre"),
   sortDirection = "asc"
  )

  assertEquals(2L, result.totalItems)
  assertEquals(listOf(UsuarioResponse.fromEntity(u1), UsuarioResponse.fromEntity(u3)), result.data)
 }*/

 @Test
 fun `existsByEmail and existsByUsername delegates correctly`() {
  whenever(usuarioRepository.existsByEmail("e")).thenReturn(true)
  whenever(usuarioRepository.existsByUsername("u")).thenReturn(false)
  assertTrue(service.existsByEmail("e"))
  assertFalse(service.existsByUsername("u"))
  verify(usuarioRepository).existsByEmail("e")
  verify(usuarioRepository).existsByUsername("u")
 }
}
