package org.wolve.geofilm.producciones.participacion.mappers

import org.springframework.stereotype.Component
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionRequest
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionResponse
import org.wolve.geofilm.producciones.participacion.models.Participacion
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.profesional.model.Profesional
import org.wolve.geofilm.utils.paginationUtils.PaginationUtils

@Component
class ParticipacionMapper {
    fun toResponse(participacion: Participacion): ParticipacionResponse = ParticipacionResponse(
        id = participacion.id,
        produccionId = participacion.produccion.id,
        produccionTitulo = participacion.produccion.titulo,
        profesionalId = participacion.profesional.id,
        profesionalNombre = participacion.profesional.nombre,
        rol = participacion.rol,
        papel = participacion.papel,
        createdAt = participacion.createdAt,
        updatedAt = participacion.updatedAt
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

    fun toPaginatedResponse(page: org.springframework.data.domain.Page<Participacion>):
            PaginationUtils.PaginatedResponse<ParticipacionResponse> {
        return PaginationUtils.toPaginatedResponse(page.map { toResponse(it) })
    }
}