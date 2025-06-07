package org.wolve.geofilm.producciones.produccion.controller

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
import org.springframework.web.multipart.MultipartFile
import org.wolve.geofilm.producciones.produccion.dto.ProduccionCompletaResponse
import org.wolve.geofilm.producciones.produccion.dto.ProduccionRequest
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.produccion.models.Categoria
import org.wolve.geofilm.producciones.produccion.models.ClasificacionEdad
import org.wolve.geofilm.producciones.produccion.models.TipoProduccion
import org.wolve.geofilm.producciones.produccion.service.IProduccionService
import org.wolve.geofilm.utils.pagination.PaginatedResponse
import org.wolve.geofilm.utils.pagination.PaginationUtils
import org.wolve.geofilm.utils.storage.images.FirebaseStorageService
import java.net.URI
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/producciones")
@Tag(name = "Producciones", description = "Endpoints para la gestión de Producciones")
class ProduccionController(
    private val produccionService: IProduccionService,
    private val firebaseStorageService: FirebaseStorageService
) {

    @Operation(summary = "Obtiene todas las producciones (sin paginación)")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listado completo de producciones",
                content = [Content(mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = ProduccionResponse::class)))])
        ]
    )
    @GetMapping("/all")
    fun getAllProduccionesWithoutPagination(): ResponseEntity<List<ProduccionResponse>> {
        val producciones = produccionService.getAllProducciones()
        return ResponseEntity.ok(producciones)
    }

    @Operation(summary = "Obtiene todas las producciones (paginadas)")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listado paginado de producciones",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = PaginatedResponse::class))])
        ]
    )
    @GetMapping
    fun getAllProducciones(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "titulo") sortBy: List<String>,
        @RequestParam(defaultValue = "asc") sortDirection: String
    ): ResponseEntity<PaginationUtils.PaginatedResponse<ProduccionResponse>> {
        val response = produccionService.getAllProducciones(page, size, sortBy, sortDirection)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Obtiene una producción por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Producción encontrada",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = ProduccionResponse::class))]),
            ApiResponse(responseCode = "404", description = "Producción no encontrada",
                content = [Content()])
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

    @Operation(summary = "Crea una nueva producción")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Producción creada",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = ProduccionResponse::class))]
            ),
            ApiResponse(responseCode = "400", description = "Datos inválidos", content = [Content()]),
            ApiResponse(responseCode = "500", description = "Error interno del servidor", content = [Content()])
        ]
    )
    @PostMapping
    fun createProduccion(
        @RequestBody request: ProduccionRequest
    ): ResponseEntity<ProduccionResponse> {
        return try {
            val produccionResponse = produccionService.createProduccion(request)
            val location = URI("/api/producciones/${produccionResponse.id}")
            ResponseEntity.created(location).body(produccionResponse)
        } catch (ex: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @Operation(summary = "Actualiza una producción existente")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Producción actualizada",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = ProduccionResponse::class))]
            ),
            ApiResponse(responseCode = "404", description = "Producción no encontrada", content = [Content()]),
            ApiResponse(responseCode = "400", description = "Datos inválidos", content = [Content()]),
            ApiResponse(responseCode = "500", description = "Error interno del servidor", content = [Content()])
        ]
    )
    @PutMapping("/{id}")
    fun updateProduccion(
        @PathVariable id: String,
        @RequestBody request: ProduccionRequest
    ): ResponseEntity<ProduccionResponse> {
        return try {
            val produccionResponse = produccionService.updateProduccion(id, request)
            if (produccionResponse == null) {
                ResponseEntity.status(HttpStatus.NOT_FOUND).build()
            } else {
                ResponseEntity.ok(produccionResponse)
            }
        } catch (ex: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }


    @Operation(summary = "Elimina una producción por ID")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Producción eliminada", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Producción no encontrada", content = [Content()]),
            ApiResponse(responseCode = "400", description = "Solicitud inválida", content = [Content()]),
            ApiResponse(responseCode = "500", description = "Error interno del servidor", content = [Content()])
        ]
    )
    @DeleteMapping("/{id}")
    fun deleteProduccion(@PathVariable id: String): ResponseEntity<Void> {
        return try {
            produccionService.deleteProduccion(id)
            ResponseEntity.noContent().build()
        } catch (ex: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        } catch (ex: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }
    }

    @Operation(summary = "Filtrar producciones con múltiples criterios")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Producciones filtradas",
                content = [Content(mediaType = "application/json",
                    schema = Schema(implementation = PaginatedResponse::class))])
        ]
    )
    @GetMapping("/filtrar")
    fun filtrarProducciones(
        @RequestParam(required = false) titulo: String?,
        @RequestParam(required = false) tipo: String?,
        @RequestParam(required = false) estrenoDesde: Int?,
        @RequestParam(required = false) estrenoHasta: Int?,
        @RequestParam(required = false) categorias: Set<Categoria>?,
        @RequestParam(required = false) clasificacionEdad: ClasificacionEdad?,
        @RequestParam(required = false) duracionMin: Int?,
        @RequestParam(required = false) duracionMax: Int?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "titulo") sortBy: List<String>,
        @RequestParam(defaultValue = "asc") sortDirection: String
    ): ResponseEntity<PaginatedResponse<ProduccionResponse>> {
        val tipoProduccion = tipo?.let {
            try {
                TipoProduccion.valueOf(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        }

        val response = produccionService.filtrarProducciones(
            titulo = titulo,
            tipo = tipoProduccion,
            estrenoDesde = estrenoDesde,
            estrenoHasta = estrenoHasta,
            categorias = categorias,
            clasificacionEdad = clasificacionEdad,
            duracionMin = duracionMin,
            duracionMax = duracionMax,
            page = page,
            size = size,
            sortBy = sortBy,
            sortDirection = sortDirection
        )

        return ResponseEntity.ok(response)
    }


    @Operation(summary = "Obtener todas las clasificaciones de edad disponibles")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listado de clasificaciones",
                content = [Content(mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = ClasificacionEdad::class))
                )])
        ]
    )
    @GetMapping("/clasificaciones-edad")
    fun getClasificacionesEdad(): ResponseEntity<List<ClasificacionEdad>> {
        return ResponseEntity.ok(produccionService.getClasificacionesEdad())
    }

    @Operation(summary = "Obtener todas las categorías disponibles")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listado de categorías",
                content = [Content(mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = Categoria::class)))])
        ]
    )
    @GetMapping("/categorias")
    fun getCategoriasDisponibles(): ResponseEntity<List<Categoria>> {
        return ResponseEntity.ok(produccionService.getCategoriasDisponibles())
    }

    @Operation(summary = "Obtener todos los tipos de producción disponibles")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Listado de tipos de producción",
                content = [Content(mediaType = "application/json",
                    array = ArraySchema(schema = Schema(implementation = TipoProduccion::class)))])
        ]
    )
    @GetMapping("/tipos")
    fun getTiposProduccion(): ResponseEntity<List<TipoProduccion>> {
        return ResponseEntity.ok(produccionService.getTiposProduccion())
    }

    @GetMapping("/completa/{id}")
    @Operation(summary = "Obtiene una producción completa con participaciones, saga y rodajes")
    fun getProduccionCompleta(@PathVariable id: String): ResponseEntity<ProduccionCompletaResponse> {
        val completa = produccionService.findProduccionCompletaById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(completa)
    }

    @PostMapping("/{id}/upload-image")
    fun uploadProduccionImage(
        @PathVariable id: String,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<String> {
        val produccion = produccionService.getProduccionById(id)
            ?: return ResponseEntity.notFound().build()

        // Eliminar imagen anterior si es de Firebase
        produccion.imagen?.let { urlAnterior ->
            val bucketPrefix = "https://storage-download.googleapis.com/movietrip-e3a91.appspot.com/"
            if (urlAnterior.startsWith(bucketPrefix)) {
                val relativePath = urlAnterior.removePrefix(bucketPrefix)
                try {
                    firebaseStorageService.deleteImage(relativePath)
                } catch (ex: IllegalArgumentException) {
                    println("⚠️ No se pudo borrar la imagen anterior de la profesional: ${ex.message}")
                }
            }
        }

        val time: LocalDateTime = LocalDateTime.now()
        val filename = "produccion_${id}_${time}.png"
        val nuevaUrl = firebaseStorageService.uploadImage(file, "prucciones", filename)
        produccionService.actualizarImagen(id, nuevaUrl)
        return ResponseEntity.ok(nuevaUrl)
    }

}