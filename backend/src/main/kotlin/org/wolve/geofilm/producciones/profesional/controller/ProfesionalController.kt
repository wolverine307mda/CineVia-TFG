package org.wolve.geofilm.producciones.profesional.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalRequest
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalResponse
import org.wolve.geofilm.producciones.profesional.service.IProfesionalService
import org.wolve.geofilm.utils.pagination.PaginatedResponse
import org.wolve.geofilm.utils.pagination.PaginationUtils
import org.wolve.geofilm.utils.storage.images.FirebaseStorageService
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/profesionales")
@Tag(name = "Profesionales", description = "Endpoints para la gestión de Profesionales")
class ProfesionalController(
    private val profesionalService: IProfesionalService,
    private val firebaseStorageService: FirebaseStorageService
) {

    @Operation(summary = "Obtiene todos los profesionales (paginados)")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listado paginado de profesionales",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = PaginatedResponse::class))])
        ]
    )
    @GetMapping
    fun getAllProfesionales(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "nombre") sortBy: List<String>,
        @RequestParam(defaultValue = "asc") sortDirection: String
    ): ResponseEntity<PaginationUtils.PaginatedResponse<ProfesionalResponse>> {
        val response = profesionalService.getAllProfesionales(page, size, sortBy, sortDirection)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Busca profesionales por nombre")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Profesionales encontrados",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = PaginatedResponse::class))])
        ]
    )
    @GetMapping("/buscar")
    fun searchByNombre(
        @RequestParam nombre: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "nombre") sortBy: List<String>,
        @RequestParam(defaultValue = "asc") sortDirection: String
    ): ResponseEntity<PaginationUtils.PaginatedResponse<ProfesionalResponse>> {
        val response = profesionalService.searchByNombre(nombre, page, size, sortBy, sortDirection)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Obtiene un profesional por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Profesional encontrado",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = ProfesionalResponse::class))]),
            ApiResponse(responseCode = "404", description = "Profesional no encontrado",
                content = [Content()])
        ]
    )
    @GetMapping("/{id}")
    fun getProfesionalById(@PathVariable id: String): ResponseEntity<ProfesionalResponse> {
        val profesional = profesionalService.getProfesionalById(id)
        return if (profesional != null) {
            ResponseEntity.ok(profesional)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @Operation(summary = "Crea un nuevo profesional")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Profesional creado",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = ProfesionalResponse::class))]),
            ApiResponse(responseCode = "400", description = "Datos inválidos",
                content = [Content()])
        ]
    )
    @PostMapping
    fun createProfesional(@RequestBody request: ProfesionalRequest): ResponseEntity<ProfesionalResponse> {
        val profesionalResponse = profesionalService.createProfesional(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(profesionalResponse)
    }

    @Operation(summary = "Actualiza un profesional existente")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Profesional actualizado",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = ProfesionalResponse::class))]),
            ApiResponse(responseCode = "404", description = "Profesional no encontrado",
                content = [Content()])
        ]
    )
    @PutMapping("/{id}")
    fun updateProfesional(
        @PathVariable id: String,
        @RequestBody request: ProfesionalRequest
    ): ResponseEntity<ProfesionalResponse> {
        val profesionalResponse = profesionalService.updateProfesional(id, request)
        return if (profesionalResponse != null) {
            ResponseEntity.ok(profesionalResponse)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @Operation(summary = "Elimina un profesional por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Profesional eliminado",
                content = [Content()]),
            ApiResponse(responseCode = "404", description = "Profesional no encontrado",
                content = [Content()])
        ]
    )
    @DeleteMapping("/{id}")
    fun deleteProfesional(@PathVariable id: String): ResponseEntity<Void> {
        profesionalService.deleteProfesional(id)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Filtrar profesionales con múltiples criterios")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Profesionales filtrados",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = PaginatedResponse::class))])
        ]
    )
    @GetMapping("/filtrar")
    fun filtrarProfesionales(
        @RequestParam(required = false) nombre: String?,
        @RequestParam(required = false) fechaNacimientoDesde: String?,
        @RequestParam(required = false) fechaNacimientoHasta: String?,
        @RequestParam(required = false) fechaInicioDesde: String?,
        @RequestParam(required = false) fechaInicioHasta: String?,
        @RequestParam(required = false) lugarNacimiento: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "nombre") sortBy: List<String>,
        @RequestParam(defaultValue = "asc") sortDirection: String
    ): ResponseEntity<PaginationUtils.PaginatedResponse<ProfesionalResponse>> {

        val response = profesionalService.filtrarProfesionales(
            nombre = nombre,
            fechaNacimientoDesde = fechaNacimientoDesde,
            fechaNacimientoHasta = fechaNacimientoHasta,
            fechaInicioDesde = fechaInicioDesde,
            fechaInicioHasta = fechaInicioHasta,
            lugarNacimiento = lugarNacimiento,
            page = page,
            size = size,
            sortBy = sortBy,
            sortDirection = sortDirection
        )

        return ResponseEntity.ok(response)
    }

    @PostMapping("/upload-image-temp")
    fun uploadTempProfesionalImage(
        @RequestParam("file") file: MultipartFile,
        @RequestParam(name = "oldUrl", required = false) oldUrl: String?
    ): ResponseEntity<String> {
        // Borrar imagen anterior si existe y es de Firebase
        oldUrl?.let { url ->
            val bucketPrefix = "https://storage-download.googleapis.com/movietrip-e3a91.appspot.com/"
            if (url.startsWith(bucketPrefix)) {
                val relativePath = url.removePrefix(bucketPrefix)
                try {
                    firebaseStorageService.deleteImage(relativePath)
                } catch (ex: IllegalArgumentException) {
                    println("⚠️ No se pudo borrar la imagen anterior: ${ex.message}")
                }
            }
        }

        val filename = "temp_profesional_${LocalDateTime.now()}.png"
        val newUrl = firebaseStorageService.uploadImage(file, "profesionales", filename)
        return ResponseEntity.ok(newUrl)
    }
}