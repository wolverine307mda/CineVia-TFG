package org.wolve.geofilm.producciones.participacion.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionRequest
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionResponse
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionResponseProfesional
import org.wolve.geofilm.producciones.participacion.services.IParticipacionService
import org.wolve.geofilm.utils.paginationUtils.PaginatedResponse
import org.wolve.geofilm.utils.paginationUtils.PaginationUtils

@RestController
@RequestMapping("/api/participaciones")
@Tag(name = "Participaciones", description = "Endpoints para la gestión de Participaciones")
class ParticipacionController(
    private val participacionService: IParticipacionService
) {

    @Operation(summary = "Crear una nueva participación")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Participación creada",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = ParticipacionResponse::class))]),
            ApiResponse(responseCode = "400", description = "Datos inválidos",
                content = [Content()]),
            ApiResponse(responseCode = "404", description = "Producción o profesional no encontrado",
                content = [Content()])
        ]
    )
    @PostMapping
    fun createParticipacion(@RequestBody request: ParticipacionRequest): ResponseEntity<ParticipacionResponse> {
        val response = participacionService.createParticipacion(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @Operation(summary = "Obtener una participación por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Participación encontrada",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = ParticipacionResponse::class))]),
            ApiResponse(responseCode = "404", description = "Participación no encontrada",
                content = [Content()])
        ]
    )
    @GetMapping("/{id}")
    fun getParticipacionById(@PathVariable id: String): ResponseEntity<ParticipacionResponse> {
        val response = participacionService.getParticipacionById(id)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Obtener todas las participaciones (paginadas)")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listado paginado de participaciones",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = PaginatedResponse::class))])
        ]
    )
    @GetMapping
    fun getAllParticipaciones(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "createdAt") sortBy: List<String>,
        @RequestParam(defaultValue = "desc") sortDirection: String
    ): ResponseEntity<PaginationUtils.PaginatedResponse<ParticipacionResponse>> {
        val response = participacionService.getAllParticipaciones(page, size, sortBy, sortDirection)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Obtener participaciones por producción")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listado paginado de participaciones",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = PaginatedResponse::class))]),
            ApiResponse(responseCode = "404", description = "Producción no encontrada",
                content = [Content()])
        ]
    )
    @GetMapping("/produccion/{produccionId}")
    fun getByProduccion(
        @PathVariable produccionId: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "createdAt") sortBy: List<String>,
        @RequestParam(defaultValue = "desc") sortDirection: String
    ): ResponseEntity<PaginationUtils.PaginatedResponse<ParticipacionResponse>> {
        val response = participacionService.getByProduccion(
            produccionId, page, size, sortBy, sortDirection
        )
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Obtener participaciones por profesional")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listado paginado de participaciones",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = PaginatedResponse::class))]),
            ApiResponse(responseCode = "404", description = "Profesional no encontrado",
                content = [Content()])
        ]
    )
    @GetMapping("/profesional/{profesionalId}")
    fun getByProfesional(
        @PathVariable profesionalId: String
    ): ResponseEntity<List<ParticipacionResponseProfesional>> {
        val responseList = participacionService.getByProfesional(profesionalId)
        return ResponseEntity.ok(responseList)
    }

    @Operation(summary = "Actualizar una participación")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Participación actualizada",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = ParticipacionResponse::class))]),
            ApiResponse(responseCode = "404", description = "Participación, producción o profesional no encontrado",
                content = [Content()])
        ]
    )
    @PutMapping("/{id}")
    fun updateParticipacion(
        @PathVariable id: String,
        @RequestBody request: ParticipacionRequest
    ): ResponseEntity<ParticipacionResponse> {
        val response = participacionService.updateParticipacion(id, request)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Eliminar una participación")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Participación eliminada",
                content = [Content()]),
            ApiResponse(responseCode = "404", description = "Participación no encontrada",
                content = [Content()])
        ]
    )
    @DeleteMapping("/{id}")
    fun deleteParticipacion(@PathVariable id: String): ResponseEntity<Void> {
        participacionService.deleteParticipacion(id)
        return ResponseEntity.noContent().build()
    }
}