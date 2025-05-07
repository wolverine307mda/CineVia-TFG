package org.wolve.geofilm.ubicaciones.service

import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.wolve.geofilm.ubicaciones.dto.UbicacionRequest
import org.wolve.geofilm.ubicaciones.dto.UbicacionResponse
import org.wolve.geofilm.ubicaciones.exceptions.UbicacionNotFoundException
import org.wolve.geofilm.ubicaciones.mapper.UbicacionMapper
import org.wolve.geofilm.ubicaciones.models.Ubicacion
import org.wolve.geofilm.ubicaciones.repository.UbicacionRepository
import org.wolve.geofilm.producciones.produccion.service.IProduccionService
import org.wolve.geofilm.utils.paginationUtils.PaginatedResponse
import org.wolve.geofilm.utils.paginationUtils.PaginationUtils

@Service
class UbicacionServiceImpl(
    private val ubicacionRepository: UbicacionRepository,
    private val ubicacionMapper: UbicacionMapper,
    private val produccionService: IProduccionService
) : IUbicacionService {

    companion object {
        private val ALLOWED_SORT_FIELDS = setOf(
            "id", "nombre", "createdAt", "updatedAt"
        )
        private const val DEFAULT_PAGE_SIZE = 10
        private const val MAX_PAGE_SIZE = 100
        private const val DEFAULT_SORT_FIELD = "nombre"
    }

    // region Operaciones CRUD básicas
    @Cacheable(value = ["ubicaciones"], key = "#id")
    override fun getUbicacionById(id: String): UbicacionResponse? {
        return ubicacionRepository.findById(id)
            .map { ubicacionMapper.toResponse(it) }
            .orElse(null)
    }

    @Cacheable(value = ["ubicaciones"], key = "'all'")
    override fun getAllUbicaciones(): List<UbicacionResponse> {
        return ubicacionRepository.findAll()
            .map { ubicacionMapper.toResponse(it) }
    }

    @CacheEvict(value = ["ubicaciones", "ubicacionesByProduccion"], allEntries = true)
    @Transactional
    override fun createUbicacion(request: UbicacionRequest): UbicacionResponse {
        val ubicacion = ubicacionMapper.toEntity(request)

        val saved = ubicacionRepository.save(ubicacion)
        return ubicacionMapper.toResponse(saved)
    }

    @CacheEvict(value = ["ubicaciones", "ubicacionesByProduccion"], allEntries = true)
    @Transactional
    override fun updateUbicacion(id: String, request: UbicacionRequest): UbicacionResponse? {
        return ubicacionRepository.findById(id)
            .map { existing ->
                val updated = existing.copy(
                    nombre = request.nombre,
                    latitud = request.latitud,
                    longitud = request.longitud
                )

                ubicacionMapper.toResponse(ubicacionRepository.save(updated))
            }
            .orElse(null)
    }

    @CacheEvict(value = ["ubicaciones", "ubicacionesByProduccion"], allEntries = true)
    @Transactional
    override fun deleteUbicacion(id: String) {
        ubicacionRepository.deleteById(id)
    }
    // endregion

    // region Operaciones paginadas
    override fun getAllUbicaciones(
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginationUtils.PaginatedResponse<UbicacionResponse> {
        val pageable = PaginationUtils.createPageable(
            page = page,
            size = size,
            sortBy = sortBy,
            sortDirection = sortDirection,
            allowedSortFields = ALLOWED_SORT_FIELDS
        )

        val pageResult = ubicacionRepository.findAll(pageable)
        return ubicacionMapper.toPaginatedResponse(pageResult)
    }

    // region Métodos de apoyo
    override fun findEntityById(id: String): Ubicacion {
        return ubicacionRepository.findById(id)
            .orElseThrow { UbicacionNotFoundException(id) }
    }

    override fun existsById(id: String): Boolean {
        return ubicacionRepository.existsById(id)
    }
    // endregion
}