package org.wolve.geofilm.users.mappers

import org.springframework.beans.factory.annotation.Autowired
import org.wolve.geofilm.users.dto.CreateUsuarioRequest
import org.wolve.geofilm.users.dto.UpdateUsuarioRequest
import org.wolve.geofilm.users.models.Usuario
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.wolve.geofilm.users.models.RolUsuario

@Component
class UsuarioMapper @Autowired constructor(
    private val passwordEncoder: PasswordEncoder
) {
    fun toEntity(dto: CreateUsuarioRequest): Usuario {
        return Usuario(
            username = dto.username,
            nombre = dto.nombre,
            apellido = dto.apellido.toString(),
            email = dto.email,
            password = passwordEncoder.encode(dto.password),
            rol = dto.rol,
            telefono = dto.telefono.toString(),
            fechaNacimiento = dto.fechaNacimiento,
            avatar = dto.avatar
        )
    }

    fun updateEntity(usuario: Usuario, dto: UpdateUsuarioRequest): Usuario {
        return usuario.apply {
            nombre = dto.nombre ?: nombre
            apellido = dto.apellido ?: apellido
            telefono = dto.telefono ?: telefono
            rol = dto.rol?.let { RolUsuario.valueOf(it.uppercase()) } ?: rol
            fechaNacimiento = dto.fechaNacimiento ?: fechaNacimiento
            avatar = dto.avatar ?: avatar
        }
    }
}