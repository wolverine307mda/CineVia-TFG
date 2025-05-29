package org.wolve.geofilm.users.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.wolve.geofilm.users.dto.*
import org.wolve.geofilm.users.models.RolUsuario
import org.wolve.geofilm.users.services.UsuarioService
import org.wolve.geofilm.utils.pagination.PaginatedResponse
import org.wolve.geofilm.utils.storage.images.FirebaseStorageService
import java.time.LocalDateTime
import java.util.*


@RestController
@RequestMapping("/api/users")
class UsuarioController(
    private val usuarioService: UsuarioService,
    private val firebaseStorageService: FirebaseStorageService
) {
    private val bucketName = "movietrip-e3a91.firebasestorage.app"

    @PostMapping
    //@PreAuthorize("hasAuthority('ADMINISTRADOR')")
    fun createUsuario(@RequestBody dto: CreateUsuarioRequest): ResponseEntity<UsuarioResponse> {
        val usuario = usuarioService.createUsuario(dto)
        return ResponseEntity.ok(UsuarioResponse.fromEntity(usuario))
    }

    @PostMapping("/admin")
    //@PreAuthorize("hasAuthority('ADMINISTRADOR')")
    fun createAdmin(@RequestBody dto: CreateUsuarioRequest): ResponseEntity<UsuarioResponse> {
        val usuario = usuarioService.createAdmin(dto)
        return ResponseEntity.ok(UsuarioResponse.fromEntity(usuario))
    }

    @GetMapping("/me")
    fun getCurrentUserProfile(authentication: Authentication): ResponseEntity<UsuarioProfileResponse> {
        val email = authentication.name
        val profile = usuarioService.getCurrentUserProfile(email)
        return ResponseEntity.ok(profile)
    }

    @GetMapping("/{id}")
    //@PreAuthorize("hasAuthority('ADMINISTRADOR') or #id == authentication.principal.id")
    fun getById(@PathVariable id: String): ResponseEntity<UsuarioResponse> {
        val user = usuarioService.findById(id)
        return ResponseEntity.ok(UsuarioResponse.fromEntity(user))
    }

    @GetMapping
    //@PreAuthorize("hasAuthority('ADMINISTRADOR')")
    fun getAll(pageable: Pageable): ResponseEntity<Page<UsuarioResponse>> {
        val users = usuarioService.findAll(pageable)
        return ResponseEntity.ok(users)
    }

    //@PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @GetMapping("/filter")
    fun getAllFiltered(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false) search: String?,
        @RequestParam(required = false) rol: String?,
        @RequestParam(required = false) isDeleted: Boolean?,
        @RequestParam(required = false) sortBy: List<String> = listOf("createdAt"),
        @RequestParam(defaultValue = "desc") sortDirection: String
    ): ResponseEntity<PaginatedResponse<UsuarioResponse>> {

        val rolEnum = rol?.let {
            try {
                RolUsuario.valueOf(it.uppercase())
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Rol inválido: $it. Valores permitidos: ${RolUsuario.entries.joinToString()}")
            }
        }

        val usuarios = usuarioService.findAllFiltered(
            search = search,
            rol = rolEnum,
            isDeleted = isDeleted,
            page = page,
            size = size,
            sortBy = sortBy,
            sortDirection = sortDirection
        )

        return ResponseEntity.ok(usuarios)
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasAuthority('ADMINISTRADOR') or #id == authentication.principal.id")
    fun update(
        @PathVariable id: String,
        @RequestBody dto: UpdateUsuarioRequest
    ): ResponseEntity<UsuarioResponse> {
        val updated = usuarioService.updateUser(id, dto)
        return ResponseEntity.ok(UsuarioResponse.fromEntity(updated))
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAuthority('ADMINISTRADOR')")
    fun delete(@PathVariable id: String): ResponseEntity<Void> {
        usuarioService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/soft/{id}")
    //@PreAuthorize("hasAuthority('ADMINISTRADOR')")
    fun softDelete(@PathVariable id: String): ResponseEntity<Void> {
        usuarioService.softDelete(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/restore/{id}")
    //@PreAuthorize("hasAuthority('ADMINISTRADOR')")
    fun restoreUser(@PathVariable id: String): ResponseEntity<Void> {
        usuarioService.restoreUser(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/check-username")
    fun checkUsernameAvailability(@RequestParam username: String): ResponseEntity<Map<String, Boolean>> {
        return try {
            val exists = usuarioService.existsByUsername(username)
            ResponseEntity.ok(Collections.singletonMap("available", !exists))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("available", false))
        }
    }

    @GetMapping("/check-email")
    fun checkEmailAvailability(@RequestParam email: String): ResponseEntity<Map<String, Boolean>> {
        return try {
            val exists = usuarioService.existsByEmail(email)
            ResponseEntity.ok(Collections.singletonMap("available", !exists))
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("available", false))
        }
    }

    @PostMapping("/{id}/upload-avatar")
    fun uploadAvatar(
        @PathVariable id: String,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<String> {
        val usuario = usuarioService.findById(id)

        // Si ya tenía un avatar, lo eliminamos
        usuario.avatar?.let { urlAnterior ->
            val bucketPrefix = "https://storage-download.googleapis.com/$bucketName/"
            if (urlAnterior.startsWith(bucketPrefix)) {
                val relativePath = urlAnterior.removePrefix(bucketPrefix)
                try {
                    firebaseStorageService.deleteImage(relativePath)
                } catch (ex: IllegalArgumentException) {
                    println("⚠️ No se pudo borrar el avatar anterior: ${ex.message}")
                }
            }
        }

        val time: LocalDateTime = LocalDateTime.now()
        val filename = "usuario_${id}_${time}.png"
        val imageUrl = firebaseStorageService.uploadImage(file, "usuarios", filename)
        usuarioService.updateAvatar(id, imageUrl)
        return ResponseEntity.ok(imageUrl)
    }
}