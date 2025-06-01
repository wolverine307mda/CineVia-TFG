package org.wolve.geofilm.producciones.ubicaciones.dto

import java.time.LocalDateTime

data class UbicacionResponse(
    val id: String,
    val nombre: String,
    val latitud: Double,
    val longitud: Double,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)