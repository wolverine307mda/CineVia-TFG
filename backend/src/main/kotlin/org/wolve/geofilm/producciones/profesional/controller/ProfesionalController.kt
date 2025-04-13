package org.wolve.geofilm.producciones.profesional.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalListResponse
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalRequest
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalResponse
import org.wolve.geofilm.producciones.profesional.service.IProfesionalService
import java.util.*

@RestController
@RequestMapping("/api/profesionales")
@Tag(name = "Profesionales", description = "Endpoints para la gestión de profesionales")
class ProfesionalController(
    private val profesionalService: IProfesionalService
) {

    @Operation(summary = "Obtener un profesional por ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Profesional encontrado"),
        ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    ])
    @GetMapping("/{id}")
    fun getProfesionalById(
        @PathVariable
        @Parameter(description = "ID del profesional", example = "123e4567-e89b-12d3-a456-426614174000")
        id: String
    ): ResponseEntity<ProfesionalResponse> {
        return ResponseEntity.ok(profesionalService.findById(id))
    }

    @Operation(summary = "Crear un nuevo profesional")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Profesional creado exitosamente"),
        ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    ])
    @PostMapping
    fun createProfesional(
        @RequestBody
        @Parameter(description = "Datos del profesional a crear", required = true)
        profesionalRequest: ProfesionalRequest
    ): ResponseEntity<ProfesionalResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(profesionalService.create(profesionalRequest))
    }

    @Operation(summary = "Actualizar un profesional existente")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Profesional actualizado exitosamente"),
        ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    ])
    @PutMapping("/{id}")
    fun updateProfesional(
        @PathVariable
        @Parameter(description = "ID del profesional a actualizar", example = "123e4567-e89b-12d3-a456-426614174000")
        id: String,
        @RequestBody
        @Parameter(description = "Datos actualizados del profesional", required = true)
        profesionalRequest: ProfesionalRequest
    ): ResponseEntity<ProfesionalResponse> {
        return ResponseEntity.ok(profesionalService.update(id, profesionalRequest))
    }

    @Operation(summary = "Eliminar un profesional")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Profesional eliminado exitosamente"),
        ApiResponse(responseCode = "404", description = "Profesional no encontrado")
    ])
    @DeleteMapping("/{id}")
    fun deleteProfesional(
        @PathVariable
        @Parameter(description = "ID del profesional a eliminar", example = "123e4567-e89b-12d3-a456-426614174000")
        id: String
    ): ResponseEntity<Void> {
        profesionalService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Listar todos los profesionales (paginado)")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Lista de profesionales obtenida exitosamente",
            content = [Content(schema = Schema(implementation = ProfesionalListResponse::class))])
    ])
    @GetMapping
    fun getAllProfesionales(
        @RequestParam(defaultValue = "0")
        @Parameter(description = "Número de página (0-based)", example = "0")
        page: Int,
        @RequestParam(defaultValue = "10")
        @Parameter(description = "Tamaño de página", example = "10")
        size: Int,
        @RequestParam(defaultValue = "nombre,asc")
        @Parameter(description = "Criterio de ordenación (campo,dirección)", example = "nombre,asc")
        sort: String
    ): ResponseEntity<ProfesionalListResponse> {
        val sortDirection = if (sort.contains("desc")) Sort.Direction.DESC else Sort.Direction.ASC
        val sortProperty = sort.split(",")[0]
        val pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortProperty))
        return ResponseEntity.ok(profesionalService.findAll(pageable))
    }

    @Operation(summary = "Buscar profesionales por nombre (paginado)")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Lista de profesionales obtenida exitosamente",
            content = [Content(schema = Schema(implementation = ProfesionalListResponse::class))])
    ])
    @GetMapping("/search")
    fun searchProfesionalesByNombre(
        @RequestParam
        @Parameter(description = "Texto a buscar en el nombre", required = true)
        nombre: String,
        @RequestParam(defaultValue = "0")
        @Parameter(description = "Número de página (0-based)", example = "0")
        page: Int,
        @RequestParam(defaultValue = "10")
        @Parameter(description = "Tamaño de página", example = "10")
        size: Int
    ): ResponseEntity<ProfesionalListResponse> {
        val pageable = PageRequest.of(page, size)
        return ResponseEntity.ok(profesionalService.searchByNombre(nombre, pageable))
    }

    @Operation(summary = "Filtrar profesionales por rango de fechas de nacimiento (paginado)")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Lista de profesionales obtenida exitosamente",
            content = [Content(schema = Schema(implementation = ProfesionalListResponse::class))])
    ])
    @GetMapping("/filter/fecha-nacimiento")
    fun filterByFechaNacimiento(
        @RequestParam
        @Parameter(description = "Fecha de inicio (formato: yyyy-MM-dd)", example = "1980-01-01")
        fechaInicio: Date,
        @RequestParam
        @Parameter(description = "Fecha de fin (formato: yyyy-MM-dd)", example = "2000-12-31")
        fechaFin: Date,
        @RequestParam(defaultValue = "0")
        @Parameter(description = "Número de página (0-based)", example = "0")
        page: Int,
        @RequestParam(defaultValue = "10")
        @Parameter(description = "Tamaño de página", example = "10")
        size: Int
    ): ResponseEntity<ProfesionalListResponse> {
        val pageable = PageRequest.of(page, size)
        return ResponseEntity.ok(profesionalService.findByFechaNacimientoBetween(fechaInicio, fechaFin, pageable))
    }
}