package org.wolve.geofilm.producciones.produccion.service

import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.wolve.geofilm.producciones.produccion.dto.ProduccionCompletaResponse
import org.wolve.geofilm.producciones.produccion.dto.ProduccionRequest
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.produccion.exceptions.ProduccionNotFoundException
import org.wolve.geofilm.producciones.produccion.mapper.ProduccionMapper
import org.wolve.geofilm.producciones.produccion.models.*
import org.wolve.geofilm.producciones.produccion.repository.ProduccionRepository
import org.wolve.geofilm.producciones.saga.exception.SagaNotFoundException
import org.wolve.geofilm.utils.client.TmdbClient
import org.wolve.geofilm.utils.pagination.PaginatedResponse
import org.wolve.geofilm.utils.pagination.PaginationUtils
import java.util.*

@Service
class ProduccionServiceImpl(
    private val produccionRepository: ProduccionRepository,
    private val produccionMapper: ProduccionMapper,
    private val tmdbClient: TmdbClient
) : IProduccionService {

    private val logger = LoggerFactory.getLogger(ProduccionServiceImpl::class.java)

    companion object {
        private val ALLOWED_SORT_FIELDS = setOf(
            "id", "titulo", "estreno", "duracion", "clasificacionEdad"
        )
        private const val DEFAULT_PAGE_SIZE = 10
        private const val MAX_PAGE_SIZE = 100
        private const val DEFAULT_SORT_FIELD = "titulo"
    }

    // region Operaciones CRUD básicas
    @Cacheable(value = ["producciones"], key = "#id")
    override fun getProduccionById(id: String): ProduccionResponse? {
        return produccionRepository.findById(id)
            .map { produccionMapper.toProduccionResponse(it) }
            .orElse(null)
    }

    @Cacheable(value = ["producciones"], key = "'all'")
    override fun getAllProducciones(): List<ProduccionResponse> {
        return produccionRepository.findAll()
            .map { produccionMapper.toProduccionResponse(it) }
    }

    @CacheEvict(value = ["producciones", "produccionesByTitulo"], allEntries = true)
    @Transactional
    override fun createProduccion(request: ProduccionRequest): ProduccionResponse {
        // 1) Mapeo inicial del DTO a entidad
        val produccion: Produccion = produccionMapper.toProduccionEntity(request)

        // 2) Calculamos el año de estreno
        val calendar = Calendar.getInstance().apply { time = request.estreno }
        val year: Int = calendar.get(Calendar.YEAR)

        logger.info("[Servicio] Antes de TMDb: sinopsis='${produccion.sinopsis}', imagen='${produccion.imagen}', puntuacion=${produccion.puntuacion}")

        // 3) Llamada a TMDb para buscar datos
        val tmdbMovie = tmdbClient.searchMovie(request.titulo, year)
        if (tmdbMovie != null) {
            // 3a) Si TMDb tiene overview, lo asignamos como sinopsis
            tmdbMovie.overview?.takeIf { it.isNotBlank() }?.let {
                produccion.sinopsis = it
            }
            // 3b) Si TMDb tiene vote_average, lo asignamos
            tmdbMovie.voteAverage?.let {
                produccion.puntuacion = it
            }
            // 3c) Si TMDb tiene poster_path, construimos la URL y asignamos
            tmdbClient.buildPosterUrl(tmdbMovie.posterPath)?.let {
                produccion.imagen = it
            }
            logger.info("[Servicio] TMDb encontró: overview='${tmdbMovie.overview}', voteAverage=${tmdbMovie.voteAverage}, posterPath='${tmdbMovie.posterPath}'")
        } else {
            produccion.imagen = "https://storage.cloud.google.com/movietrip-e3a91.firebasestorage.app/producciones/imagen_defecto.png?authuser=1"
            logger.info("[Servicio] TMDb no devolvió datos para '${request.titulo}'")
        }

        // 4) Verificamos antes de guardar
        logger.info("[Servicio] Antes de guardar: sinopsis='${produccion.sinopsis}', imagen='${produccion.imagen}', puntuacion=${produccion.puntuacion}")

        // 5) Guardamos la entidad en la BD
        val saved: Produccion = produccionRepository.save(produccion)

        // 6) Confirmamos tras guardar
        logger.info("[Servicio] Guardado en BD: sinopsis='${saved.sinopsis}', imagen='${saved.imagen}', puntuacion=${saved.puntuacion}")

        // 7) Devolvemos la respuesta (mapeada con ProduccionMapper)
        return produccionMapper.toProduccionResponse(saved)
    }

    @CacheEvict(value = ["producciones", "produccionesByTitulo"], allEntries = true)
    @Transactional
    override fun updateProduccion(id: String, request: ProduccionRequest): ProduccionResponse? {
        return produccionRepository.findById(id)
            .map { existing ->
                val updated = existing.copy(
                    titulo = request.titulo,
                    tipo = request.tipo,
                    estreno = request.estreno,
                    duracion = request.duracion,
                    sinopsis = request.sinopsis,
                    imagen = request.imagen,
                    informacion = request.informacion,
                    puntuacion = request.puntuacion,
                    categorias = request.categorias.toMutableSet(),
                    clasificacionEdad = ClasificacionEdad.fromValorNumerico(request.clasificacionEdad)
                        ?: existing.clasificacionEdad
                )
                produccionMapper.toProduccionResponse(produccionRepository.save(updated))
            }
            .orElse(null)
    }

    override fun actualizarImagen(id: String, nuevaUrl: String): ProduccionResponse? {
        val profesional = produccionRepository.findById(id)
            .orElseThrow { SagaNotFoundException("Saga no encontrada con ID $id") }

        profesional.imagen = nuevaUrl
        val actualizada = produccionRepository.save(profesional)
        return produccionMapper.toResponse(actualizada)
    }

    @CacheEvict(value = ["producciones", "produccionesByTitulo"], allEntries = true)
    @Transactional
    override fun deleteProduccion(id: String) {
        produccionRepository.deleteById(id)
    }
    // endregion

    // region Operaciones paginadas
    override fun getAllProducciones(
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginationUtils.PaginatedResponse<ProduccionResponse> {
        val pageable = PaginationUtils.createPageable(
            page = page,
            size = size,
            sortBy = sortBy,
            sortDirection = sortDirection,
            allowedSortFields = ALLOWED_SORT_FIELDS
        )

        val pageResult = produccionRepository.findAll(pageable)
        return PaginationUtils.toPaginatedResponse(
            pageResult.map { produccionMapper.toProduccionResponse(it) }
        )
    }

    override fun filtrarProducciones(
        titulo: String?,
        tipo: TipoProduccion?,
        estrenoDesde: Int?,
        estrenoHasta: Int?,
        categorias: Set<Categoria>?,
        clasificacionEdad: ClasificacionEdad?,
        duracionMin: Int?,
        duracionMax: Int?,
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginatedResponse<ProduccionResponse> {
        val allProducciones = produccionRepository.findAll()

        // Filtrado
        val filtered = allProducciones.filter { produccion ->
            (titulo.isNullOrBlank() || produccion.titulo.contains(titulo, ignoreCase = true)) &&
                    (tipo == null || produccion.tipo == tipo) &&
                    (estrenoDesde == null || produccion.estreno.year + 1900 >= estrenoDesde) &&
                    (estrenoHasta == null || produccion.estreno.year + 1900 <= estrenoHasta) &&
                    (categorias.isNullOrEmpty() || produccion.categorias.any { it in categorias }) &&
                    (clasificacionEdad == null || produccion.clasificacionEdad == clasificacionEdad) &&
                    (duracionMin == null || produccion.duracion >= duracionMin) &&
                    (duracionMax == null || produccion.duracion <= duracionMax)
        }

        // Ordenación
        val sorted = when {
            sortBy.any { it.equals("titulo", ignoreCase = true) } && sortDirection.equals("asc", ignoreCase = true) ->
                filtered.sortedBy { it.titulo }
            sortBy.any { it.equals("titulo", ignoreCase = true) } ->
                filtered.sortedByDescending { it.titulo }
            sortBy.any { it.equals("estreno", ignoreCase = true) } && sortDirection.equals("asc", ignoreCase = true) ->
                filtered.sortedBy { it.estreno }
            sortBy.any { it.equals("estreno", ignoreCase = true) } ->
                filtered.sortedByDescending { it.estreno }
            else -> filtered.sortedByDescending { it.estreno }
        }

        // Paginación manual
        val totalItems = sorted.size.toLong()
        val totalPages = if (size > 0) (totalItems + size - 1) / size else 0
        val paginatedItems = sorted
            .drop(page * size)
            .take(size)
            .map { produccionMapper.toProduccionResponse(it) }

        return PaginatedResponse(
            data = paginatedItems,
            totalItems = totalItems,
            totalPages = totalPages.toInt(),
            currentPage = page,
            pageSize = size
        )
    }
    // endregion

    // region Métodos de apoyo
    override fun findEntityById(id: String): Produccion {
        return produccionRepository.findById(id)
            .orElseThrow { ProduccionNotFoundException(id) }
    }

    override fun existsById(id: String): Boolean {
        return produccionRepository.existsById(id)
    }

    override fun countParticipacionesByProduccionId(produccionId: String): Long {
        return produccionRepository.countParticipacionesById(produccionId)
    }


    // endregion

    // region Métodos para enumerados
    override fun getClasificacionesEdad(): List<ClasificacionEdad> {
        return ClasificacionEdad.values().toList()
    }

    override fun getCategoriasDisponibles(): List<Categoria> {
        return Categoria.values().toList()
    }

    override fun getTiposProduccion(): List<TipoProduccion> {
        return TipoProduccion.values().toList()
    }
    // endregion

    override fun findProduccionCompletaById(id: String): ProduccionCompletaResponse? {
        val produccion = produccionRepository.findCompletaById(id) ?: return null
        return produccionMapper.toCompletaResponse(produccion)
    }

}