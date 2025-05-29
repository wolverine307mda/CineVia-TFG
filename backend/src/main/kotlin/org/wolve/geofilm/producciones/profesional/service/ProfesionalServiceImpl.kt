package org.wolve.geofilm.producciones.profesional.service

import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalRequest
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalResponse
import org.wolve.geofilm.producciones.profesional.mapper.ProfesionalMapper
import org.wolve.geofilm.producciones.profesional.model.Profesional
import org.wolve.geofilm.producciones.profesional.repository.IProfesionalRepository
import org.wolve.geofilm.producciones.saga.dto.SagaResponse
import org.wolve.geofilm.producciones.saga.exception.SagaNotFoundException
import org.wolve.geofilm.utils.pagination.PaginationUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Service
class ProfesionalServiceImpl(
    private val profesionalRepository: IProfesionalRepository,
    private val profesionalMapper: ProfesionalMapper
) : IProfesionalService {

    companion object {
        private val ALLOWED_SORT_FIELDS = setOf(
            "id", "nombre", "fechaNacimiento", "fechaInicio"
        )
        private const val DEFAULT_PAGE_SIZE = 10
        private const val MAX_PAGE_SIZE = 100
        private const val DEFAULT_SORT_FIELD = "nombre"
    }

    @Cacheable(value = ["profesionales"], key = "#id")
    override fun getProfesionalById(id: String): ProfesionalResponse? {
        return profesionalRepository.findById(id)
            .map { profesionalMapper.toResponse(it) }
            .orElse(null)
    }

    @CacheEvict(value = ["profesionales"], allEntries = true)
    @Transactional
    override fun createProfesional(request: ProfesionalRequest): ProfesionalResponse {
        val profesional = profesionalMapper.toEntity(request)
        val saved = profesionalRepository.save(profesional)
        return profesionalMapper.toResponse(saved)
    }

    @CacheEvict(value = ["profesionales"], allEntries = true)
    @Transactional
    override fun updateProfesional(id: String, request: ProfesionalRequest): ProfesionalResponse? {
        return profesionalRepository.findById(id)
            .map { existing ->
                val updated = existing.copy(
                    nombre = request.nombre,
                    foto = request.foto,
                    fechaNacimiento = request.fechaNacimiento,
                    fechaInicio = request.fechaInicio,
                    lugarNacimiento = request.lugarNacimiento,
                    biografia = request.biografia
                )
                profesionalMapper.toResponse(profesionalRepository.save(updated))
            }
            .orElse(null)
    }

    override fun actualizarImagen(id: String, nuevaUrl: String): ProfesionalResponse? {
        val profesional = profesionalRepository.findById(id)
            .orElseThrow { SagaNotFoundException("Saga no encontrada con ID $id") }

        profesional.foto = nuevaUrl
        val actualizada = profesionalRepository.save(profesional)
        return profesionalMapper.toResponse(actualizada)
    }

    @CacheEvict(value = ["profesionales"], allEntries = true)
    @Transactional
    override fun deleteProfesional(id: String) {
        profesionalRepository.deleteById(id)
    }

    override fun getAllProfesionales(
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginationUtils.PaginatedResponse<ProfesionalResponse> {
        val pageable = PaginationUtils.createPageable(
            page = page,
            size = size,
            sortBy = sortBy,
            sortDirection = sortDirection,
            allowedSortFields = ALLOWED_SORT_FIELDS
        )

        val pageResult = profesionalRepository.findAll(pageable)
        return profesionalMapper.toPaginatedResponse(pageResult)
    }

    override fun searchByNombre(
        nombre: String,
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginationUtils.PaginatedResponse<ProfesionalResponse> {
        val pageable = PaginationUtils.createPageable(
            page = page,
            size = size,
            sortBy = sortBy,
            sortDirection = sortDirection,
            allowedSortFields = ALLOWED_SORT_FIELDS
        )

        val pageResult = profesionalRepository.findByNombreContainingIgnoreCase(nombre, pageable)
        return profesionalMapper.toPaginatedResponse(pageResult)
    }

    override fun filtrarProfesionales(
        nombre: String?,
        fechaNacimientoDesde: String?,
        fechaNacimientoHasta: String?,
        fechaInicioDesde: String?,
        fechaInicioHasta: String?,
        lugarNacimiento: String?,
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginationUtils.PaginatedResponse<ProfesionalResponse> {

        // Obtener todos los profesionales (sin filtro)
        val allProfesionales = profesionalRepository.findAll()

        // Parsear fechas
        val fechaNacDesde = fechaNacimientoDesde?.let { parseDate(it) }
        val fechaNacHasta = fechaNacimientoHasta?.let { parseDate(it) }
        val fechaIniDesde = fechaInicioDesde?.let { parseDate(it) }
        val fechaIniHasta = fechaInicioHasta?.let { parseDate(it) }

        // Aplicar filtros
        val filtered = allProfesionales.filter { profesional ->
            (nombre.isNullOrBlank() || profesional.nombre.contains(nombre, ignoreCase = true)) &&
                    (fechaNacDesde == null || profesional.fechaNacimiento >= fechaNacDesde) &&
                    (fechaNacHasta == null || profesional.fechaNacimiento <= fechaNacHasta) &&
                    (fechaIniDesde == null || profesional.fechaInicio >= fechaIniDesde) &&
                    (fechaIniHasta == null || profesional.fechaInicio <= fechaIniHasta) &&
                    (lugarNacimiento.isNullOrBlank() ||
                            profesional.lugarNacimiento.contains(lugarNacimiento, ignoreCase = true))
        }

        // Aplicar ordenación
        val sorted = when {
            sortBy.contains("nombre") && sortDirection.equals("asc", ignoreCase = true) ->
                filtered.sortedBy { it.nombre }
            sortBy.contains("nombre") ->
                filtered.sortedByDescending { it.nombre }
            sortBy.contains("fechaNacimiento") && sortDirection.equals("asc", ignoreCase = true) ->
                filtered.sortedBy { it.fechaNacimiento }
            sortBy.contains("fechaNacimiento") ->
                filtered.sortedByDescending { it.fechaNacimiento }
            sortBy.contains("fechaInicio") && sortDirection.equals("asc", ignoreCase = true) ->
                filtered.sortedBy { it.fechaInicio }
            sortBy.contains("fechaInicio") ->
                filtered.sortedByDescending { it.fechaInicio }
            else -> filtered
        }

        // Aplicar paginación manual
        val totalItems = sorted.size.toLong()
        val totalPages = if (size > 0) (totalItems + size - 1) / size else 0
        val paginatedItems = sorted
            .drop(page * size)
            .take(size)
            .map { profesionalMapper.toResponse(it) }

        return PaginationUtils.PaginatedResponse(
            data = paginatedItems,
            totalItems = totalItems,
            totalPages = totalPages.toInt(),
            currentPage = page,
            pageSize = size
        )
    }

    private fun parseDate(dateStr: String): Date {
        return try {
            SimpleDateFormat("yyyy-MM-dd").parse(dateStr)
        } catch (e: ParseException) {
            throw IllegalArgumentException("Formato de fecha inválido. Use yyyy-MM-dd")
        }
    }

    override fun findEntityById(id: String): Profesional? {
        return profesionalRepository.findById(id).orElse(null)
    }
}