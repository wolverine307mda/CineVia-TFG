package org.wolve.geofilm.ubicaciones.dto

data class UbicacionRequest(
    val nombre: String,
    val latitud: Double,
    val longitud: Double,
)