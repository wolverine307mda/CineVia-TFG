package org.wolve.geofilm.producciones.rodajes.dto.response

import org.wolve.geofilm.producciones.ubicaciones.dto.UbicacionResponse
import java.time.LocalDateTime

data class RodajeResponse(
    val id: String,
    val notas: String?,
    val imagenes: List<String>,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val ubicacion: UbicacionResponse
)