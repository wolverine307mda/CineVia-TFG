package org.wolve.geofilm.rodajes.mapper

import org.springframework.stereotype.Component
import org.wolve.geofilm.rodajes.dto.response.RodajeResponse
import org.wolve.geofilm.rodajes.models.Rodaje
import org.wolve.geofilm.ubicaciones.mapper.UbicacionMapper

@Component
class RodajeMapper(
    private val ubicacionMapper: UbicacionMapper
) {
    fun toResponse(entity: Rodaje): RodajeResponse =
        RodajeResponse(
            id = entity.id,
            notas = entity.notas,
            imagenes = entity.imagenes,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            ubicacion = ubicacionMapper.toResponse(entity.ubicacion)
        )
}