package org.wolve.geofilm.ubicaciones.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.wolve.geofilm.ubicaciones.dto.UbicacionRequest
import org.wolve.geofilm.ubicaciones.dto.UbicacionResponse
import org.wolve.geofilm.ubicaciones.service.IUbicacionService
import org.wolve.geofilm.utils.paginationUtils.PaginatedResponse
import org.wolve.geofilm.utils.paginationUtils.PaginationUtils

@RestController
@RequestMapping("/api/ubicaciones")
@Tag(name = "Ubicaciones", description = "Endpoints para la gestión de Ubicaciones")
class UbicacionController(
    private val ubicacionService: IUbicacionService
) {

    @Operation(summary = "Obtiene todas las ubicaciones (sin paginación)")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listado completo de ubicaciones",
                content = [Content(mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = UbicacionResponse::class)))])
        ]
    )
    @GetMapping("/all")
    fun getAllUbicacionesWithoutPagination(): ResponseEntity<List<UbicacionResponse>> {
        val ubicaciones = ubicacionService.getAllUbicaciones()
        return ResponseEntity.ok(ubicaciones)
    }

    @Operation(summary = "Obtiene todas las ubicaciones (paginadas)")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listado paginado de ubicaciones",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = PaginatedResponse::class))])
        ]
    )
    @GetMapping
    fun getAllUbicaciones(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "nombre") sortBy: List<String>,
        @RequestParam(defaultValue = "asc") sortDirection: String
    ): ResponseEntity<PaginationUtils.PaginatedResponse<UbicacionResponse>> {
        val response = ubicacionService.getAllUbicaciones(page, size, sortBy, sortDirection)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Obtiene una ubicación por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Ubicación encontrada",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = UbicacionResponse::class))]),
            ApiResponse(responseCode = "404", description = "Ubicación no encontrada",
                content = [Content()])
        ]
    )
    @GetMapping("/{id}")
    fun getUbicacionById(@PathVariable id: String): ResponseEntity<UbicacionResponse> {
        val ubicacion = ubicacionService.getUbicacionById(id)
        return if (ubicacion != null) {
            ResponseEntity.ok(ubicacion)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @Operation(summary = "Crea una nueva ubicación")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Ubicación creada",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = UbicacionResponse::class))]),
            ApiResponse(responseCode = "400", description = "Datos inválidos",
                content = [Content()])
        ]
    )
    @PostMapping
    fun createUbicacion(@RequestBody request: UbicacionRequest): ResponseEntity<UbicacionResponse> {
        val ubicacionResponse = ubicacionService.createUbicacion(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(ubicacionResponse)
    }

    @Operation(summary = "Actualiza una ubicación existente")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Ubicación actualizada",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = UbicacionResponse::class))]),
            ApiResponse(responseCode = "404", description = "Ubicación no encontrada",
                content = [Content()])
        ]
    )
    @PutMapping("/{id}")
    fun updateUbicacion(
        @PathVariable id: String,
        @RequestBody request: UbicacionRequest
    ): ResponseEntity<UbicacionResponse> {
        val ubicacionResponse = ubicacionService.updateUbicacion(id, request)
        return if (ubicacionResponse != null) {
            ResponseEntity.ok(ubicacionResponse)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @Operation(summary = "Elimina una ubicación por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Ubicación eliminada",
                content = [Content()]),
            ApiResponse(responseCode = "404", description = "Ubicación no encontrada",
                content = [Content()])
        ]
    )
    @DeleteMapping("/{id}")
    fun deleteUbicacion(@PathVariable id: String): ResponseEntity<Void> {
        ubicacionService.deleteUbicacion(id)
        return ResponseEntity.noContent().build()
    }
}