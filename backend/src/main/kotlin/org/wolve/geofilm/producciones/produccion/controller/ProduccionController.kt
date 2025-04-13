package org.wolve.geofilm.producciones.produccion.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.wolve.geofilm.producciones.produccion.dto.ProduccionRequest
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.produccion.service.IProduccionService

@RestController
@RequestMapping("/api/producciones")
@Tag(name = "Producciones", description = "Endpoints para la gestión de Producciones")
class ProduccionController(
    private val produccionService: IProduccionService
) {

    @Operation(summary = "Obtiene todas las producciones")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listado de producciones",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ProduccionResponse::class))])
        ]
    )
    @GetMapping
    fun getAllProducciones(): ResponseEntity<List<ProduccionResponse>> {
        val producciones = produccionService.getAllProducciones()
        return ResponseEntity.ok(producciones)
    }

    @Operation(summary = "Obtiene una producción por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Producción encontrada",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ProduccionResponse::class))]),
            ApiResponse(responseCode = "404", description = "Producción no encontrada", content = [Content()])
        ]
    )
    @GetMapping("/{id}")
    fun getProduccionById(@PathVariable id: String): ResponseEntity<ProduccionResponse> {
        val produccion = produccionService.getProduccionById(id)
        return if (produccion != null) {
            ResponseEntity.ok(produccion)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @Operation(summary = "Obtiene producciones por Título")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listado de producciones encontrado",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ProduccionResponse::class))]),
            ApiResponse(responseCode = "404", description = "Producción no encontrada", content = [Content()])
        ]
    )
    @GetMapping("/titulo/{titulo}")
    fun getProduccionesByTitulo(@PathVariable titulo: String): ResponseEntity<List<ProduccionResponse>> {
        val producciones = produccionService.getProduccionesByTitulo(titulo)
        return if (producciones.isNotEmpty()) {
            ResponseEntity.ok(producciones)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @Operation(summary = "Crea una nueva producción")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Producción creada",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ProduccionResponse::class))]),
            ApiResponse(responseCode = "400", description = "Datos inválidos", content = [Content()])
        ]
    )
    @PostMapping
    fun createProduccion(@RequestBody request: ProduccionRequest): ResponseEntity<ProduccionResponse> {
        val produccionResponse = produccionService.createProduccion(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(produccionResponse)
    }

    @Operation(summary = "Actualiza una producción existente")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Producción actualizada",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ProduccionResponse::class))]),
            ApiResponse(responseCode = "404", description = "Producción no encontrada", content = [Content()])
        ]
    )
    @PutMapping("/{id}")
    fun updateProduccion(
        @PathVariable id: String,
        @RequestBody request: ProduccionRequest
    ): ResponseEntity<ProduccionResponse> {
        val produccionResponse = produccionService.updateProduccion(id, request)
        return if (produccionResponse != null) {
            ResponseEntity.ok(produccionResponse)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @Operation(summary = "Elimina una producción por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Producción eliminada", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Producción no encontrada", content = [Content()])
        ]
    )
    @DeleteMapping("/{id}")
    fun deleteProduccion(@PathVariable id: String): ResponseEntity<Void> {
        produccionService.deleteProduccion(id)
        return ResponseEntity.noContent().build()
    }
}
