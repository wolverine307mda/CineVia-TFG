package org.wolve.geofilm.producciones.saga.service

import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.wolve.geofilm.producciones.produccion.exceptions.ProduccionNotFoundException
import org.wolve.geofilm.producciones.produccion.repository.ProduccionRepository
import org.wolve.geofilm.producciones.saga.dto.SagaFilterParams
import org.wolve.geofilm.producciones.saga.dto.SagaRequest
import org.wolve.geofilm.producciones.saga.dto.SagaResponse
import org.wolve.geofilm.producciones.saga.exception.SagaNotFoundException
import org.wolve.geofilm.producciones.saga.exceptions.SagaValidationException
import org.wolve.geofilm.producciones.saga.mapper.SagaMapper
import org.wolve.geofilm.producciones.saga.repository.SagaRepository
import org.wolve.geofilm.utils.pagination.PaginatedResponse
import org.wolve.geofilm.utils.pagination.PaginationUtils

@Service
class SagaServiceImpl(
    private val sagaRepository: SagaRepository,
    private val produccionRepository: ProduccionRepository,
    private val sagaMapper: SagaMapper
) : ISagaService {

    companion object {
        private val ALLOWED_SORT_FIELDS = setOf(
            "id", "nombre", "fechaInicio", "fechaFin", "isAcabada"
        )
        private const val DEFAULT_PAGE_SIZE = 10
        private const val MAX_PAGE_SIZE = 100
        private const val DEFAULT_SORT_FIELD = "nombre"
    }

    //CRUD básico
    @Cacheable(value = ["sagas"], key = "#id")
    override fun getSagaById(id: String): SagaResponse? {
        return sagaRepository.findById(id)
            .map { sagaMapper.toSagaResponse(it) }
            .orElse(null)
    }

    @Cacheable(value = ["sagas"], key = "'all'")
    override fun getAllSagas(): List<SagaResponse> {
        return sagaRepository.findAll()
            .map { sagaMapper.toSagaResponse(it) }
    }

    @CacheEvict(value = ["sagas", "filteredSagas"], allEntries = true)
    @Transactional
    override fun createSaga(request: SagaRequest): SagaResponse {
        validateSagaRequest(request)

        val saga = sagaMapper.toSagaEntity(request)
        val saved = sagaRepository.save(saga)
        return sagaMapper.toSagaResponse(saved)
    }

    @CacheEvict(value = ["sagas", "filteredSagas"], allEntries = true)
    @Transactional
    override fun updateSaga(id: String, request: SagaRequest): SagaResponse? {
        validateSagaRequest(request, id)

        return sagaRepository.findById(id)
            .map { existing ->
                val updated = existing.copy(
                    nombre = request.nombre,
                    descripcion = request.descripcion,
                    isAcabada = request.isAcabada,
                    fechaInicio = request.fechaInicio,
                    fechaFin = request.fechaFin,
                    imagen = request.imagen
                )
                sagaMapper.toSagaResponse(sagaRepository.save(updated))
            }
            .orElse(null)
    }

    override fun actualizarImagenSaga(id: String, nuevaUrl: String): SagaResponse {
        val saga = sagaRepository.findById(id)
            .orElseThrow { SagaNotFoundException("Saga no encontrada con ID $id") }

        saga.imagen = nuevaUrl
        val actualizada = sagaRepository.save(saga)
        return sagaMapper.toSagaResponse(actualizada)
    }

    @CacheEvict(value = ["sagas", "filteredSagas"], allEntries = true)
    @Transactional
    override fun deleteSaga(id: String) {
        val saga = sagaRepository.findByIdOrNull(id)
            ?: throw SagaNotFoundException("Saga con ID $id no encontrada")

        val producciones = produccionRepository.findAllBySagaId(id)
        producciones.forEach { it.saga = null }

        sagaRepository.delete(saga)
    }

    @Transactional
    override fun agregarProduccionASaga(sagaId: String, produccionId: String) {
        val saga = sagaRepository.findById(sagaId)
            .orElseThrow { SagaNotFoundException("Saga con ID $sagaId no encontrada") }

        val produccion = produccionRepository.findById(produccionId)
            .orElseThrow { ProduccionNotFoundException("Producción con ID $produccionId no encontrada") }

        produccion.saga = saga
        produccionRepository.save(produccion)
    }

    @Transactional
    override fun eliminarProduccionDeSaga(produccionId: String) {
        val produccion = produccionRepository.findById(produccionId)
            .orElseThrow { ProduccionNotFoundException("Producción con ID $produccionId no encontrada") }

        if (produccion.saga == null) {
            throw IllegalStateException("La producción no pertenece a ninguna saga.")
        }

        produccion.saga = null
        produccionRepository.save(produccion)
    }

    // region Operaciones paginadas
    override fun getAllSagasPaginated( page: Int, size: Int, sortBy: List<String>, sortDirection: String ): PaginationUtils.PaginatedResponse<SagaResponse> {
        val pageable = PaginationUtils.createPageable(
            page = page,
            size = size,
            sortBy = sortBy,
            sortDirection = sortDirection,
            allowedSortFields = ALLOWED_SORT_FIELDS
        )

        val pageResult = sagaRepository.findAll(pageable)
        return PaginationUtils.toPaginatedResponse(
            pageResult.map { sagaMapper.toSagaResponse(it) }
        )
    }

    override fun filterSagas( params: SagaFilterParams, page: Int, size: Int, sortBy: List<String>, sortDirection: String ): PaginatedResponse<SagaResponse> {
        val allSagas = sagaRepository.findAll()

        val filtered = allSagas.filter { saga ->
            (params.nombre.isNullOrBlank() || saga.nombre.contains(params.nombre, ignoreCase = true)) &&
                    (params.isAcabada == null || saga.isAcabada == params.isAcabada) &&
                    (params.fechaInicioDesde == null || saga.fechaInicio >= params.fechaInicioDesde) &&
                    (params.fechaInicioHasta == null || saga.fechaInicio <= params.fechaInicioHasta) &&
                    (params.tieneImagen == null || (params.tieneImagen && saga.imagen != null) || (!params.tieneImagen && saga.imagen == null))
        }

        val sorted = when {
            sortBy.any { it.equals("nombre", ignoreCase = true) } && sortDirection.equals("asc", ignoreCase = true) ->
                filtered.sortedBy { it.nombre }
            sortBy.any { it.equals("nombre", ignoreCase = true) } ->
                filtered.sortedByDescending { it.nombre }
            sortBy.any { it.equals("fechaInicio", ignoreCase = true) } && sortDirection.equals("asc", ignoreCase = true) ->
                filtered.sortedBy { it.fechaInicio }
            sortBy.any { it.equals("fechaInicio", ignoreCase = true) } ->
                filtered.sortedByDescending { it.fechaInicio }
            sortBy.any { it.equals("isAcabada", ignoreCase = true) } && sortDirection.equals("asc", ignoreCase = true) ->
                filtered.sortedBy { it.isAcabada }
            sortBy.any { it.equals("isAcabada", ignoreCase = true) } ->
                filtered.sortedByDescending { it.isAcabada }
            else -> filtered.sortedByDescending { it.fechaInicio }
        }

        val totalItems = sorted.size.toLong()
        val totalPages = if (size > 0) (totalItems + size - 1) / size else 0
        val paginatedItems = sorted
            .drop(page * size)
            .take(size)
            .map { sagaMapper.toSagaResponse(it) }

        return PaginatedResponse(
            data = paginatedItems,
            totalItems = totalItems,
            totalPages = totalPages.toInt(),
            currentPage = page,
            pageSize = size
        )
    }

    override fun existsById(id: String): Boolean {
        return sagaRepository.existsById(id)
    }

    override fun existsByNombre(nombre: String): Boolean {
        return sagaRepository.existsByNombre(nombre)
    }

    private fun validateSagaRequest(request: SagaRequest, id: String? = null) {
        if (request.nombre.isBlank()) {
            throw SagaValidationException("El nombre de la saga no puede estar vacío")
        }

        if (request.fechaFin != null && request.fechaFin < request.fechaInicio) {
            throw SagaValidationException("La fecha de fin no puede ser anterior a la fecha de inicio")
        }

        if (id == null) {
            if (sagaRepository.existsByNombre(request.nombre)) {
                throw SagaValidationException("Ya existe una saga con el nombre ${request.nombre}")
            }
        } else {
            val existingSaga = sagaRepository.findById(id)
                .orElseThrow { SagaNotFoundException(id) }

            if (existingSaga.nombre != request.nombre && sagaRepository.existsByNombre(request.nombre)) {
                throw SagaValidationException("Ya existe otra saga con el nombre ${request.nombre}")
            }
        }
    }
}