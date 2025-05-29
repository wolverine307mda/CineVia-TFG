package org.wolve.geofilm.producciones.participacion.mappers

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionRequest
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionResponse
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionResponseProfesional
import org.wolve.geofilm.producciones.participacion.models.Participacion
import org.wolve.geofilm.producciones.produccion.mapper.ProduccionMapper
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.profesional.mapper.ProfesionalMapper
import org.wolve.geofilm.producciones.profesional.model.Profesional
import org.wolve.geofilm.utils.pagination.PaginationUtils

@Component
class ParticipacionMapper(
    private val profesionalMapper: ProfesionalMapper,
    private val produccionMapper: ProduccionMapper
) {
    fun toResponse(participacion: Participacion): ParticipacionResponse = ParticipacionResponse(
        id = participacion.id,
        papel = participacion.papel,
        rol = participacion.rol,
        profesional = profesionalMapper.toResponse(participacion.profesional),
        createdAt = participacion.createdAt,
        updatedAt = participacion.updatedAt,
    )

    fun toResponseProfesional(participacion: Participacion): ParticipacionResponseProfesional = ParticipacionResponseProfesional (
        id = participacion.id,
        papel = participacion.papel,
        rol = participacion.rol,
        produccion = produccionMapper.toResponse(participacion.produccion),
        createdAt = participacion.createdAt,
        updatedAt = participacion.updatedAt,
    )

    fun toEntity(
        request: ParticipacionRequest,
        produccion: Produccion,
        profesional: Profesional
    ): Participacion = Participacion(
        produccion = produccion,
        profesional = profesional,
        rol = request.rol,
        papel = request.papel
    )

    fun toPaginatedResponse(page: Page<Participacion>):
            PaginationUtils.PaginatedResponse<ParticipacionResponse> {
        return PaginationUtils.toPaginatedResponse(page.map { toResponse(it) })
    }



    fun toResponseProfesional(lista: List<Participacion>): List<ParticipacionResponseProfesional> {
        return lista.map { toResponseProfesional(it) }
    }
}