package org.wolve.geofilm.producciones.saga.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.wolve.geofilm.producciones.produccion.dto.ProduccionRequest
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.saga.dto.SagaListResponse
import org.wolve.geofilm.producciones.saga.dto.SagaRequest
import org.wolve.geofilm.producciones.saga.dto.SagaResponse
import org.wolve.geofilm.producciones.saga.service.ISagaService
import org.wolve.geofilm.utils.paginationUtils.PaginationUtils
import java.util.*

@RestController
@RequestMapping("/api/sagas")
@Tag(name = "Sagas", description = "API para gestión de sagas cinematográficas")
class SagaController(
    private val sagaService: ISagaService
) {

    @Operation(summary = "Crear una nueva saga")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Saga creada exitosamente"),
        ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        ApiResponse(responseCode = "409", description = "Ya existe una saga con ese nombre")
    ])
    @PostMapping
    fun createSaga(
        @RequestBody
        @Parameter(description = "Datos de la saga a crear", required = true)
        request: SagaRequest
    ): ResponseEntity<SagaResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(sagaService.createSaga(request))
    }

    @Operation(summary = "Obtener una saga por su ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Saga encontrada"),
        ApiResponse(responseCode = "404", description = "Saga no encontrada")
    ])
    @GetMapping("/{id}")
    fun getSagaById(
        @PathVariable
        @Parameter(description = "ID de la saga", required = true)
        id: String
    ): ResponseEntity<SagaResponse> {
        return ResponseEntity.ok(sagaService.getSagaById(id))
    }

    @Operation(summary = "Actualizar una saga existente")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Saga actualizada exitosamente"),
        ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        ApiResponse(responseCode = "404", description = "Saga no encontrada"),
        ApiResponse(responseCode = "409", description = "Ya existe una saga con ese nombre")
    ])
    @PutMapping("/{id}")
    fun updateSaga(
        @PathVariable
        @Parameter(description = "ID de la saga a actualizar", required = true)
        id: String,
        @RequestBody
        @Parameter(description = "Datos actualizados de la saga", required = true)
        request: SagaRequest
    ): ResponseEntity<SagaResponse> {
        return ResponseEntity.ok(sagaService.updateSaga(id, request))
    }

    @Operation(summary = "Eliminar una saga")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Saga eliminada exitosamente"),
        ApiResponse(responseCode = "404", description = "Saga no encontrada")
    ])
    @DeleteMapping("/{id}")
    fun deleteSaga(
        @PathVariable
        @Parameter(description = "ID de la saga a eliminar", required = true)
        id: String
    ): ResponseEntity<Void> {
        sagaService.deleteSaga(id)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Obtener todas las sagas (paginado)")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Lista de sagas obtenida exitosamente",
            content = [Content(schema = Schema(implementation = SagaListResponse::class))])
    ])
    @GetMapping
    fun getAllSagas(
        @RequestParam(defaultValue = "0")
        @Parameter(description = "Número de página (0-based)", example = "0")
        page: Int,
        @RequestParam(defaultValue = "10")
        @Parameter(description = "Tamaño de la página", example = "10")
        size: Int,
        @RequestParam(defaultValue = "nombre,asc")
        @Parameter(description = "Criterio de ordenación (campo,dirección)", example = "nombre,asc")
        sort: String
    ): ResponseEntity<SagaListResponse> {
        val sortDirection = if (sort.contains("desc")) Sort.Direction.DESC else Sort.Direction.ASC
        val sortProperty = sort.split(",")[0]
        val pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortProperty))
        return ResponseEntity.ok(sagaService.getAllSagas(pageable))
    }

    @Operation(summary = "Buscar sagas por nombre (paginado)")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Lista de sagas obtenida exitosamente",
            content = [Content(schema = Schema(implementation = SagaListResponse::class))])
    ])
    @GetMapping("/search")
    fun searchSagasByNombre(
        @RequestParam
        @Parameter(description = "Texto a buscar en el nombre", required = true)
        nombre: String,
        @RequestParam(defaultValue = "0")
        @Parameter(description = "Número de página (0-based)", example = "0")
        page: Int,
        @RequestParam(defaultValue = "10")
        @Parameter(description = "Tamaño de la página", example = "10")
        size: Int
    ): ResponseEntity<SagaListResponse> {
        val pageable = PageRequest.of(page, size)
        return ResponseEntity.ok(sagaService.searchSagasByNombre(nombre, pageable))
    }

    @Operation(summary = "Filtrar sagas por estado (paginado)")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Lista de sagas obtenida exitosamente",
            content = [Content(schema = Schema(implementation = SagaListResponse::class))])
    ])
    @GetMapping("/filter/estado")
    fun getSagasByEstado(
        @RequestParam
        @Parameter(description = "Estado de finalización de las sagas", example = "false")
        isAcabada: Boolean,
        @RequestParam(defaultValue = "0")
        @Parameter(description = "Número de página (0-based)", example = "0")
        page: Int,
        @RequestParam(defaultValue = "10")
        @Parameter(description = "Tamaño de la página", example = "10")
        size: Int
    ): ResponseEntity<SagaListResponse> {
        val pageable = PageRequest.of(page, size)
        return ResponseEntity.ok(sagaService.getSagasByEstado(isAcabada, pageable))
    }

    @Operation(summary = "Filtrar sagas por rango de fechas (paginado)")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Lista de sagas obtenida exitosamente",
            content = [Content(schema = Schema(implementation = SagaListResponse::class))])
    ])
    @GetMapping("/filter/fechas")
    fun getSagasByRangoFechas(
        @RequestParam
        @Parameter(description = "Fecha de inicio (formato: yyyy-MM-dd)", example = "2020-01-01")
        fechaInicio: Date,
        @RequestParam
        @Parameter(description = "Fecha de fin (formato: yyyy-MM-dd)", example = "2023-12-31")
        fechaFin: Date,
        @RequestParam(defaultValue = "0")
        @Parameter(description = "Número de página (0-based)", example = "0")
        page: Int,
        @RequestParam(defaultValue = "10")
        @Parameter(description = "Tamaño de la página", example = "10")
        size: Int
    ): ResponseEntity<SagaListResponse> {
        val pageable = PageRequest.of(page, size)
        return ResponseEntity.ok(sagaService.getSagasByRangoFechas(fechaInicio, fechaFin, pageable))
    }

    @Operation(summary = "Agregar producción a una saga")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Producción agregada exitosamente"),
        ApiResponse(responseCode = "404", description = "Saga no encontrada")
    ])
    @PostMapping("/{sagaId}/producciones")
    fun agregarProduccionASaga(
        @PathVariable
        @Parameter(description = "ID de la saga", required = true)
        sagaId: String,
        @RequestBody
        @Parameter(description = "Datos de la producción a agregar", required = true)
        produccionRequest: ProduccionRequest
    ): ResponseEntity<SagaResponse> {
        return ResponseEntity.ok(sagaService.agregarProduccionASaga(sagaId, produccionRequest))
    }

    @Operation(summary = "Obtener producciones de una saga")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Lista de producciones obtenida exitosamente")
    ])
    @GetMapping("/{sagaId}/producciones")
    fun obtenerProduccionesDeSaga(
        @PathVariable
        @Parameter(description = "ID de la saga", required = true)
        sagaId: String,
        @RequestParam(defaultValue = "0")
        @Parameter(description = "Número de página (0-based)", example = "0")
        page: Int,
        @RequestParam(defaultValue = "10")
        @Parameter(description = "Tamaño de la página", example = "10")
        size: Int
    ): ResponseEntity<PaginationUtils.PaginatedResponse<ProduccionResponse>> {
        val pageable = PageRequest.of(page, size)
        return ResponseEntity.ok(sagaService.obtenerProduccionesDeSaga(sagaId, pageable))
    }
}