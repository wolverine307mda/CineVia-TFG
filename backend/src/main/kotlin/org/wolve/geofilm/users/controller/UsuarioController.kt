package org.wolve.geofilm.users.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.wolve.geofilm.users.dto.*
import org.wolve.geofilm.users.services.UsuarioService
import java.util.*

@RestController
@RequestMapping("/api/users")
class UsuarioController(
    private val usuarioService: UsuarioService
) {

    @PostMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    fun createUsuario(@RequestBody dto: CreateUsuarioRequest): ResponseEntity<UsuarioResponse> {
        val usuario = usuarioService.createUsuario(dto)
        return ResponseEntity.ok(UsuarioResponse.fromEntity(usuario))
    }

    @GetMapping("/me")
    fun getCurrentUserProfile(authentication: Authentication): ResponseEntity<UsuarioProfileResponse> {
        val email = authentication.name
        val profile = usuarioService.getCurrentUserProfile(email)
        return ResponseEntity.ok(profile)
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    fun listAll(): ResponseEntity<List<UsuarioResponse>> {
        val users = usuarioService.findAll()
        return ResponseEntity.ok(users.map { UsuarioResponse.fromEntity(it) })
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or #id == authentication.principal.id")
    fun getById(@PathVariable id: UUID): ResponseEntity<UsuarioResponse> {
        val user = usuarioService.findById(id)
        return ResponseEntity.ok(UsuarioResponse.fromEntity(user))
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR') or #id == authentication.principal.id")
    fun update(
        @PathVariable id: UUID,
        @RequestBody dto: UpdateUsuarioRequest
    ): ResponseEntity<UsuarioResponse> {
        val updated = usuarioService.updateUser(id, dto)
        return ResponseEntity.ok(UsuarioResponse.fromEntity(updated))
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        usuarioService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }
}