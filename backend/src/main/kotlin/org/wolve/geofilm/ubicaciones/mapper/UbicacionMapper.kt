package org.wolve.geofilm.ubicaciones.mapper

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import org.wolve.geofilm.ubicaciones.dto.UbicacionRequest
import org.wolve.geofilm.ubicaciones.dto.UbicacionResponse
import org.wolve.geofilm.ubicaciones.models.Ubicacion
import org.wolve.geofilm.utils.paginationUtils.PaginationUtils

@Component
class UbicacionMapper {
    fun toResponse(ubicacion: Ubicacion): UbicacionResponse = UbicacionResponse(
        id = ubicacion.id,
        nombre = ubicacion.nombre,
        latitud = ubicacion.latitud,
        longitud = ubicacion.longitud,
        createdAt = ubicacion.createdAt,
        updatedAt = ubicacion.updatedAt
    )

    fun toEntity(request: UbicacionRequest): Ubicacion = Ubicacion(
        nombre = request.nombre,
        latitud = request.latitud,
        longitud = request.longitud
    )

    fun toPaginatedResponse(page: Page<Ubicacion>): PaginationUtils.PaginatedResponse<UbicacionResponse> {
        return PaginationUtils.toPaginatedResponse(page.map { toResponse(it) })
    }
}