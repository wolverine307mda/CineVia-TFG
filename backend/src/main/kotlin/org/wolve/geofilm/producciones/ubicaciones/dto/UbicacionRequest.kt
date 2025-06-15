package org.wolve.geofilm.producciones.ubicaciones.dto

data class UbicacionRequest(
    val nombre: String,
    val latitud: Double,
    val longitud: Double,
)