package org.wolve.geofilm.producciones.participacion.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.wolve.geofilm.producciones.participacion.dto.*
import org.wolve.geofilm.producciones.participacion.services.IParticipacionService

@RestController
@RequestMapping("/api/v1/participaciones")
@Tag(name = "Participaciones", description = "API para gestionar participaciones de profesionales en producciones")
class ParticipacionController(
    private val participacionService: IParticipacionService
) {

    @Operation(summary = "Obtener todas las participaciones", description = "Retorna una lista de todas las participaciones registradas")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Lista de participaciones obtenida exitosamente",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = ParticipacionResponse::class))])])
        @GetMapping
        fun getAllParticipaciones(): ResponseEntity<List<ParticipacionResponse>> {
            return ResponseEntity.ok(participacionService.findAll())
        }

        @Operation(summary = "Obtener una participación por ID", description = "Retorna una participación específica basada en su ID")
        @ApiResponses(value = [
            ApiResponse(responseCode = "200", description = "Participación encontrada",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = ParticipacionResponse::class))]),
            ApiResponse(responseCode = "404", description = "Participación no encontrada",
                content = [Content()])])
        @GetMapping("/{id}")
        fun getParticipacionById(
            @Parameter(description = "ID de la participación a buscar", required = true)
            @PathVariable id: String
        ): ResponseEntity<ParticipacionResponse> {
            return ResponseEntity.ok(participacionService.findById(id))
        }

        @Operation(summary = "Crear una nueva participación", description = "Registra una nueva participación de un profesional en una producción")
        @ApiResponses(value = [
            ApiResponse(responseCode = "201", description = "Participación creada exitosamente",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = ParticipacionResponse::class))]),
            ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o participación ya existe",
                content = [Content()])])
        @PostMapping
        fun createParticipacion(
            @Parameter(description = "Datos de la participación a crear", required = true)
            @RequestBody request: ParticipacionRequest
        ): ResponseEntity<ParticipacionResponse> {
            return ResponseEntity.status(HttpStatus.CREATED).body(participacionService.create(request))
        }

        @Operation(summary = "Actualizar una participación", description = "Actualiza los datos de una participación existente")
        @ApiResponses(value = [
            ApiResponse(responseCode = "200", description = "Participación actualizada exitosamente",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = ParticipacionResponse::class))]),
            ApiResponse(responseCode = "404", description = "Participación no encontrada",
                content = [Content()]),
            ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o conflicto en roles",
                content = [Content()])])
        @PutMapping("/{id}")
        fun updateParticipacion(
            @Parameter(description = "ID de la participación a actualizar", required = true)
            @PathVariable id: String,

            @Parameter(description = "Datos actualizados de la participación", required = true)
            @RequestBody request: ParticipacionRequest
        ): ResponseEntity<ParticipacionResponse> {
            return ResponseEntity.ok(participacionService.update(id, request))
        }

        @Operation(summary = "Eliminar una participación", description = "Elimina una participación específica del sistema")
        @ApiResponses(value = [
            ApiResponse(responseCode = "204", description = "Participación eliminada exitosamente",
                content = [Content()]),
            ApiResponse(responseCode = "404", description = "Participación no encontrada",
                content = [Content()]),
            ApiResponse(responseCode = "409", description = "Conflicto - Participación asociada a profesional o producción",
                content = [Content()])])
        @DeleteMapping("/{id}")
        fun deleteParticipacion(
            @Parameter(description = "ID de la participación a eliminar", required = true)
            @PathVariable id: String
        ): ResponseEntity<Unit> {
            participacionService.delete(id)
            return ResponseEntity.noContent().build()
        }

        @Operation(summary = "Obtener participaciones por profesional", description = "Retorna todas las participaciones de un profesional específico")
        @ApiResponses(value = [
            ApiResponse(responseCode = "200", description = "Lista de participaciones obtenida exitosamente",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = ParticipacionProfesionalResponse::class))]),
            ApiResponse(responseCode = "404", description = "Profesional no encontrado",
                content = [Content()])])
        @GetMapping("/profesional/{profesionalId}")
        fun getParticipacionesByProfesional(
            @Parameter(description = "ID del profesional a consultar", required = true)
            @PathVariable profesionalId: String
        ): ResponseEntity<List<ParticipacionProfesionalResponse>> {
            return ResponseEntity.ok(participacionService.findByProfesionalId(profesionalId))
        }

        @Operation(summary = "Obtener participaciones por producción", description = "Retorna todas las participaciones de una producción específica")
        @ApiResponses(value = [
            ApiResponse(responseCode = "200", description = "Lista de participaciones obtenida exitosamente",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = ParticipacionProduccionResponse::class))]),
            ApiResponse(responseCode = "404", description = "Producción no encontrada",
                content = [Content()])])
        @GetMapping("/produccion/{produccionId}")
        fun getParticipacionesByProduccion(
            @Parameter(description = "ID de la producción a consultar", required = true)
            @PathVariable produccionId: String
        ): ResponseEntity<List<ParticipacionProduccionResponse>> {
            return ResponseEntity.ok(participacionService.findByProduccionId(produccionId))
        }
}