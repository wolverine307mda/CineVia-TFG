package org.wolve.geofilm.producciones.participacion.services

import jakarta.transaction.Transactional
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionRequest
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionResponse
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionResponseProfesional
import org.wolve.geofilm.producciones.participacion.exceptions.ParticipacionNotFoundException
import org.wolve.geofilm.producciones.participacion.exceptions.RelacionNotFoundException
import org.wolve.geofilm.producciones.participacion.mappers.*
import org.wolve.geofilm.producciones.participacion.repository.IParticipacionRepository
import org.wolve.geofilm.producciones.produccion.service.IProduccionService
import org.wolve.geofilm.producciones.profesional.service.IProfesionalService
import org.wolve.geofilm.utils.pagination.PaginationUtils

@Service
class ParticipacionServiceImpl(
    private val participacionRepository: IParticipacionRepository,
    private val participacionMapper: ParticipacionMapper,
    private val produccionService: IProduccionService,
    private val profesionalService: IProfesionalService
) : IParticipacionService {

    companion object {
        private val ALLOWED_SORT_FIELDS = setOf(
            "id", "createdAt", "updatedAt", "rol", "papel"
        )
        private const val DEFAULT_PAGE_SIZE = 10
        private const val MAX_PAGE_SIZE = 100
        private const val DEFAULT_SORT_FIELD = "createdAt"
    }

    @Transactional
    @CacheEvict(value = ["participaciones"], allEntries = true)
    override fun createParticipacion(request: ParticipacionRequest): ParticipacionResponse {
        val produccion = produccionService.findEntityById(request.produccionId)

        val profesional = profesionalService.findEntityById(request.profesionalId)
            ?: throw RelacionNotFoundException("Profesional no encontrado con ID: ${request.profesionalId}")

        val participacion = participacionMapper.toEntity(request, produccion, profesional)
        val saved = participacionRepository.save(participacion)
        return participacionMapper.toResponse(saved)
    }

    @Cacheable(value = ["participaciones"], key = "#id")
    override fun getParticipacionById(id: String): ParticipacionResponse {
        val participacion = participacionRepository.findById(id)
            .orElseThrow { ParticipacionNotFoundException(id) }
        return participacionMapper.toResponse(participacion)
    }

    override fun getAllParticipaciones(
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginationUtils.PaginatedResponse<ParticipacionResponse> {
        val pageable = PaginationUtils.createPageable(
            page = page,
            size = size,
            sortBy = sortBy,
            sortDirection = sortDirection,
            allowedSortFields = ALLOWED_SORT_FIELDS
        )
        val pageResult = participacionRepository.findAll(pageable)
        return participacionMapper.toPaginatedResponse(pageResult)
    }

    override fun getByProduccion(
        produccionId: String,
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginationUtils.PaginatedResponse<ParticipacionResponse> {
        val produccion = produccionService.findEntityById(produccionId)

        val pageable = PaginationUtils.createPageable(
            page = page,
            size = size,
            sortBy = sortBy,
            sortDirection = sortDirection,
            allowedSortFields = ALLOWED_SORT_FIELDS
        )

        val pageResult = participacionRepository.findByProduccion(produccion, pageable)
        return participacionMapper.toPaginatedResponse(pageResult)
    }

    override fun getByProfesional(
        profesionalId: String
    ): List<ParticipacionResponseProfesional> {
        val findByProfesional = profesionalService.findEntityById(profesionalId)
            ?: throw RelacionNotFoundException("Profesional no encontrado con ID: $profesionalId")

        val resultList = participacionRepository.findAllByProfesional(findByProfesional)
        return participacionMapper.toResponseProfesional(resultList)
    }

    @Transactional
    @CacheEvict(value = ["participaciones"], allEntries = true)
    override fun updateParticipacion(id: String, request: ParticipacionRequest): ParticipacionResponse {
        val existing = participacionRepository.findById(id)
            .orElseThrow { ParticipacionNotFoundException(id) }

        val produccion = produccionService.findEntityById(request.produccionId)

        val profesional = profesionalService.findEntityById(request.profesionalId)
            ?: throw RelacionNotFoundException("Profesional no encontrado con ID: ${request.profesionalId}")

        existing.apply {
            this.produccion = produccion
            this.profesional = profesional
            this.rol = request.rol
            this.papel = request.papel
        }

        val updated = participacionRepository.save(existing)
        return participacionMapper.toResponse(updated)
    }

    @Transactional
    @CacheEvict(value = ["participaciones"], allEntries = true)
    override fun deleteParticipacion(id: String) {
        if (!participacionRepository.existsById(id)) {
            throw ParticipacionNotFoundException(id)
        }
        participacionRepository.deleteById(id)
    }
}