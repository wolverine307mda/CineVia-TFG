package org.wolve.geofilm.producciones.rodajes.mapper

import org.springframework.stereotype.Component
import org.wolve.geofilm.producciones.rodajes.dto.response.RodajeResponse
import org.wolve.geofilm.producciones.rodajes.models.Rodaje
import org.wolve.geofilm.producciones.ubicaciones.mapper.UbicacionMapper

@Component
class RodajeMapper(
    private val ubicacionMapper: UbicacionMapper
) {
    fun toResponse(entity: Rodaje): RodajeResponse {
        return RodajeResponse(
            id = entity.id,
            notas = entity.notas,
            imagenes = entity.imagenes,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            ubicacion = ubicacionMapper.toResponse(entity.ubicacion)
        )
    }
}
