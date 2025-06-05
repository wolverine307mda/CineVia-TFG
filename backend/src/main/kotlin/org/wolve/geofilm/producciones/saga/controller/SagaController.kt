package org.wolve.geofilm.producciones.saga.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.wolve.geofilm.producciones.saga.dto.SagaFilterParams
import org.wolve.geofilm.producciones.saga.dto.SagaRequest
import org.wolve.geofilm.producciones.saga.dto.SagaResponse
import org.wolve.geofilm.producciones.saga.service.ISagaService
import org.wolve.geofilm.utils.pagination.PaginatedResponse
import org.wolve.geofilm.utils.pagination.PaginationUtils
import org.wolve.geofilm.utils.storage.images.FirebaseStorageService
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/sagas")
@Tag(name = "Sagas", description = "API para gestión de sagas cinematográficas")
class SagaController(
    private val sagaService: ISagaService,
    private val firebaseStorageService: FirebaseStorageService
) {

    @Operation(summary = "Obtener una saga por ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Saga encontrada"),
        ApiResponse(responseCode = "404", description = "Saga no encontrada", content = [Content()])
    ])
    @GetMapping("/{id}")
    fun getSagaById(
        @Parameter(description = "ID de la saga") @PathVariable id: String
    ): ResponseEntity<SagaResponse> {
        return sagaService.getSagaById(id)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @Operation(summary = "Obtener todas las sagas")
    @GetMapping
    fun getAllSagas(): ResponseEntity<List<SagaResponse>> {
        return ResponseEntity.ok(sagaService.getAllSagas())
    }

    @Operation(summary = "Obtener sagas paginadas")
    @GetMapping("/paginated")
    fun getAllSagasPaginated(
        @Parameter(description = "Número de página (0-based)", example = "0") @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Tamaño de página", example = "10") @RequestParam(defaultValue = "10") size: Int,
        @Parameter(description = "Campos para ordenar (ej. nombre,fechaInicio)") @RequestParam(required = false) sortBy: List<String>?,
        @Parameter(description = "Dirección de ordenación (asc/desc)", example = "asc") @RequestParam(defaultValue = "asc") sortDirection: String
    ): ResponseEntity<PaginationUtils.PaginatedResponse<SagaResponse>> {
        return ResponseEntity.ok(
            sagaService.getAllSagasPaginated(
                page = page,
                size = size,
                sortBy = sortBy ?: listOf("nombre"),
                sortDirection = sortDirection
            )
        )
    }

    @Operation(summary = "Filtrar sagas con paginación")
    @GetMapping("/filter")
    fun filterSagas(
        @Parameter(description = "Nombre o parte del nombre de la saga") @RequestParam(required = false) nombre: String?,
        @Parameter(description = "Indica si la saga está finalizada") @RequestParam(required = false) isAcabada: Boolean?,
        @Parameter(description = "Fecha de inicio mínima (YYYY-MM-DD)") @RequestParam(required = false) fechaInicioDesde: String?,
        @Parameter(description = "Fecha de inicio máxima (YYYY-MM-DD)") @RequestParam(required = false) fechaInicioHasta: String?,
        @Parameter(description = "Indica si la saga tiene imagen") @RequestParam(required = false) tieneImagen: Boolean?,
        @Parameter(description = "Número de página (0-based)", example = "0") @RequestParam(defaultValue = "0") page: Int,
        @Parameter(description = "Tamaño de página", example = "10") @RequestParam(defaultValue = "10") size: Int,
        @Parameter(description = "Campos para ordenar (ej. nombre,fechaInicio)") @RequestParam(required = false) sortBy: List<String>?,
        @Parameter(description = "Dirección de ordenación (asc/desc)", example = "asc") @RequestParam(defaultValue = "asc") sortDirection: String
    ): ResponseEntity<PaginatedResponse<SagaResponse>> {
        val params = SagaFilterParams(
            nombre = nombre,
            isAcabada = isAcabada,
            fechaInicioDesde = fechaInicioDesde?.let { java.sql.Date.valueOf(it) },
            fechaInicioHasta = fechaInicioHasta?.let { java.sql.Date.valueOf(it) },
            tieneImagen = tieneImagen
        )

        return ResponseEntity.ok(
            sagaService.filterSagas(
                params = params,
                page = page,
                size = size,
                sortBy = sortBy ?: listOf("nombre"),
                sortDirection = sortDirection
            )
        )
    }

    @Operation(summary = "Crear una nueva saga")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Saga creada exitosamente"),
        ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = [Content()])
    ])
    @PostMapping
    fun createSaga(
        @RequestBody request: SagaRequest
    ): ResponseEntity<SagaResponse> {
        val created = sagaService.createSaga(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    @Operation(summary = "Actualizar una saga existente")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Saga actualizada exitosamente"),
        ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = [Content()]),
        ApiResponse(responseCode = "404", description = "Saga no encontrada", content = [Content()])
    ])
    @PutMapping("/{id}")
    fun updateSaga(
        @Parameter(description = "ID de la saga") @PathVariable id: String,
        @RequestBody request: SagaRequest
    ): ResponseEntity<SagaResponse> {
        return sagaService.updateSaga(id, request)?.let {
            ResponseEntity.ok(it)
        } ?: ResponseEntity.notFound().build()
    }

    @Operation(summary = "Eliminar una saga")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Saga eliminada exitosamente"),
        ApiResponse(responseCode = "404", description = "Saga no encontrada", content = [Content()])
    ])
    @DeleteMapping("/{id}")
    fun deleteSaga(
        @Parameter(description = "ID de la saga") @PathVariable id: String
    ): ResponseEntity<Void> {
        sagaService.deleteSaga(id)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Verificar si existe una saga por ID")
    @GetMapping("/exists/{id}")
    fun existsById(
        @Parameter(description = "ID de la saga") @PathVariable id: String
    ): ResponseEntity<Boolean> {
        return ResponseEntity.ok(sagaService.existsById(id))
    }

    @Operation(summary = "Verificar si existe una saga por nombre")
    @GetMapping("/exists/nombre/{nombre}")
    fun existsByNombre(
        @Parameter(description = "Nombre de la saga") @PathVariable nombre: String
    ): ResponseEntity<Boolean> {
        return ResponseEntity.ok(sagaService.existsByNombre(nombre))
    }

    @PostMapping("/{sagaId}/producciones/{produccionId}")
    fun agregarProduccionASaga(
        @PathVariable sagaId: String,
        @PathVariable produccionId: String
    ): ResponseEntity<Void> {
        sagaService.agregarProduccionASaga(sagaId, produccionId)
        return ResponseEntity.ok().build()
    }
    @DeleteMapping("/{sagaId}/producciones/{produccionId}")
    fun eliminarProduccionDeSaga(
        @PathVariable produccionId: String
    ): ResponseEntity<Void> {
        sagaService.eliminarProduccionDeSaga(produccionId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/upload-image-temp")
    fun uploadTempSagaImage(
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

        val filename = "temp_saga_${LocalDateTime.now()}.png"
        val newUrl = firebaseStorageService.uploadImage(file, "sagas", filename)
        return ResponseEntity.ok(newUrl)
    }
}