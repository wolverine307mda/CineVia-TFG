package org.wolve.geofilm.users.mappers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.crypto.password.PasswordEncoder
import org.wolve.geofilm.users.dto.CreateUsuarioRequest
import org.wolve.geofilm.users.dto.UpdateUsuarioRequest
import org.wolve.geofilm.users.models.RolUsuario
import org.wolve.geofilm.users.models.Usuario
import java.time.LocalDate

class UsuarioMapperTest {

    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var mapper: UsuarioMapper

    @BeforeEach
    fun setUp() {
        passwordEncoder = mock()
        mapper = UsuarioMapper(passwordEncoder)
    }

   @Test
    fun `toEntity - convierte CreateUsuarioRequest a Usuario con password codificada`() {
        val rawPassword = "secret123"
        val encodedPassword = "encodedSecret"
        val request = CreateUsuarioRequest(
            username = "usuario1",
            email = "user1@example.com",
            password = rawPassword,
            nombre = "Nombre",
            apellido = "Apellido",
            rol = RolUsuario.ADMINISTRADOR,
            telefono = "555-1234",
            fechaNacimiento = LocalDate.of(1990, 5, 20),
            avatar = "avatar.png"
        )

        whenever(passwordEncoder.encode(eq(rawPassword))).thenReturn(encodedPassword)

        val entidad: Usuario = mapper.toEntity(request)

        assertNotNull(entidad.id)
        assertTrue(entidad.id.isNotEmpty())
        assertEquals("usuario1", entidad.getNombreUsuario())
        assertEquals("user1@example.com", entidad.email)
        assertEquals(encodedPassword, entidad.password)
        assertEquals("Nombre", entidad.nombre)
        assertEquals("Apellido", entidad.apellido)
        assertEquals(RolUsuario.ADMINISTRADOR, entidad.rol)
        assertEquals("555-1234", entidad.telefono)
        assertEquals(LocalDate.of(1990, 5, 20), entidad.fechaNacimiento)
        assertEquals("avatar.png", entidad.avatar)
    }

    @Test
    fun `updateEntity - actualiza solo campos no nulos de UpdateUsuarioRequest`() {
        val original = Usuario(
            id = "u1",
            username = "user1",
            email = "user1@example.com",
            password = "pwd",
            nombre = "Original",
            apellido = "Previo",
            rol = RolUsuario.USUARIO,
            telefono = "000-0000",
            fechaNacimiento = LocalDate.of(1980, 1, 1),
            avatar = "original.png"
        )
        val request = UpdateUsuarioRequest(
            nombre = "Nuevo",
            apellido = null,
            telefono = "999-9999",
            rol = "ADMINISTRADOR",
            fechaNacimiento = LocalDate.of(1985, 12, 31),
            avatar = null
        )

        val actualizado = mapper.updateEntity(original.copy(), request)

        assertEquals("u1", actualizado.id)
        assertEquals("user1", actualizado.getNombreUsuario())
        assertEquals("user1@example.com", actualizado.email)
        assertEquals("pwd", actualizado.password) // no se cambia aquí
        assertEquals("Nuevo", actualizado.nombre)
        assertEquals("Previo", actualizado.apellido) // null en DTO => mantiene original
        assertEquals(RolUsuario.ADMINISTRADOR, actualizado.rol)
        assertEquals("999-9999", actualizado.telefono)
        assertEquals(LocalDate.of(1985, 12, 31), actualizado.fechaNacimiento)
        assertEquals("original.png", actualizado.avatar)
    }

    @Test
    fun `updateEntity - todos campos nulos no cambian entidad`() {
        val now = LocalDate.of(2000, 1, 1)
        val original = Usuario(
            id = "u2",
            username = "user2",
            email = "user2@example.com",
            password = "pwd2",
            nombre = "A",
            apellido = "B",
            rol = RolUsuario.USUARIO,
            telefono = "111-2222",
            fechaNacimiento = now,
            avatar = "avatar2.png"
        )
        val request = UpdateUsuarioRequest(
            nombre = null,
            apellido = null,
            telefono = null,
            rol = null,
            fechaNacimiento = null,
            avatar = null
        )

        val actualizado = mapper.updateEntity(original.copy(), request)

        assertEquals(original.id, actualizado.id)
        assertEquals(original.username, actualizado.username)
        assertEquals(original.email, actualizado.email)
        assertEquals(original.password, actualizado.password)
        assertEquals("A", actualizado.nombre)
        assertEquals("B", actualizado.apellido)
        assertEquals(RolUsuario.USUARIO, actualizado.rol)
        assertEquals("111-2222", actualizado.telefono)
        assertEquals(now, actualizado.fechaNacimiento)
        assertEquals("avatar2.png", actualizado.avatar)
    }
}
