package org.wolve.geofilm.ubicaciones.dto

import java.time.LocalDateTime

data class UbicacionResponse(
    val id: String,
    val nombre: String,
    val latitud: Double,
    val longitud: Double,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)