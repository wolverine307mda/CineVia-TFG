package org.wolve.geofilm.producciones.profesional.mapper

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalListResponse
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalRequest
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalResponse
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalSimpleResponse
import org.wolve.geofilm.producciones.profesional.model.Profesional

@Component
class ProfesionalMapper {
    fun toEntity(request: ProfesionalRequest): Profesional = Profesional(
        nombre = request.nombre,
        foto = request.foto,
        fechaNacimiento = request.fechaNacimiento,
        fechaInicio = request.fechaInicio,
        lugarNacimiento = request.lugarNacimiento,
        biografia = request.biografia
    )

    fun toResponse(entity: Profesional): ProfesionalResponse = ProfesionalResponse(
        id = entity.id,
        nombre = entity.nombre,
        foto = entity.foto,
        fechaNacimiento = entity.fechaNacimiento,
        fechaInicio = entity.fechaInicio,
        lugarNacimiento = entity.lugarNacimiento,
        biografia = entity.biografia
    )

    fun toSimpleResponse(entity: Profesional): ProfesionalSimpleResponse = ProfesionalSimpleResponse(
        id = entity.id,
        nombre = entity.nombre,
        foto = entity.foto
    )

    fun toPageResponse(page: Page<Profesional>): ProfesionalListResponse = ProfesionalListResponse(
        profesionales = page.map { toSimpleResponse(it) }.content,
        totalElements = page.totalElements,
        totalPages = page.totalPages,
        currentPage = page.number,
        size = page.size
    )
}