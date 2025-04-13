package org.wolve.geofilm.producciones.profesional.service

import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalListResponse
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalRequest
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalResponse
import org.wolve.geofilm.producciones.profesional.exception.ProfesionalNotFoundException
import org.wolve.geofilm.producciones.profesional.mapper.ProfesionalMapper
import org.wolve.geofilm.producciones.profesional.repository.ProfesionalRepository
import java.util.*

@Service
@Transactional
class ProfesionalServiceImpl(
    private val profesionalRepository: ProfesionalRepository,
    private val profesionalMapper: ProfesionalMapper
) : IProfesionalService {

    @Cacheable(value = ["profesional"], key = "#id")
    override fun findById(id: String): ProfesionalResponse {
        val profesional = profesionalRepository.findById(id)
            .orElseThrow { ProfesionalNotFoundException("Profesional no encontrado con id: $id") }
        return profesionalMapper.toResponse(profesional)
    }

    @CachePut(value = ["profesional"], key = "#result.id")
    @CacheEvict(value = ["profesionales"], allEntries = true)
    override fun create(profesionalRequest: ProfesionalRequest): ProfesionalResponse {
        val profesionalEntity = profesionalMapper.toEntity(profesionalRequest)
        val savedEntity = profesionalRepository.save(profesionalEntity)
        return profesionalMapper.toResponse(savedEntity)
    }

    @CachePut(value = ["profesional"], key = "#id")
    @CacheEvict(value = ["profesionales"], allEntries = true)
    override fun update(id: String, profesionalRequest: ProfesionalRequest): ProfesionalResponse {
        val profesional = profesionalRepository.findById(id)
            .orElseThrow { ProfesionalNotFoundException("Profesional no encontrado con id: $id") }

        val updated = profesional.copy(
            nombre = profesionalRequest.nombre,
            foto = profesionalRequest.foto,
            fechaNacimiento = profesionalRequest.fechaNacimiento,
            lugarNacimiento = profesionalRequest.lugarNacimiento,
            biografia = profesionalRequest.biografia
        )

        val savedEntity = profesionalRepository.save(updated)
        return profesionalMapper.toResponse(savedEntity)
    }

    @CacheEvict(value = ["profesional", "profesionales"], key = "#id")
    override fun delete(id: String) {
        if (!profesionalRepository.existsById(id)) {
            throw ProfesionalNotFoundException("Profesional no encontrado con id: $id")
        }
        profesionalRepository.deleteById(id)
    }

    @Cacheable(value = ["profesionales"], key = "{#pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    override fun findAll(pageable: Pageable): ProfesionalListResponse {
        val page = profesionalRepository.findAllByOrderByNombreAsc(pageable)
        return profesionalMapper.toPageResponse(page)
    }

    @Cacheable(value = ["profesionales"], key = "{#nombre, #pageable.pageNumber, #pageable.pageSize}")
    override fun searchByNombre(nombre: String, pageable: Pageable): ProfesionalListResponse {
        val page = profesionalRepository.findByNombreContainingIgnoreCase(nombre, pageable)
        return profesionalMapper.toPageResponse(page)
    }

    @Cacheable(value = ["profesionales"], key = "{#fechaInicio, #fechaFin, #pageable.pageNumber, #pageable.pageSize}")
    override fun findByFechaNacimientoBetween(
        fechaInicio: Date,
        fechaFin: Date,
        pageable: Pageable
    ): ProfesionalListResponse {
        val page = profesionalRepository.findByFechaNacimientoBetween(fechaInicio, fechaFin, pageable)
        return profesionalMapper.toPageResponse(page)
    }
}