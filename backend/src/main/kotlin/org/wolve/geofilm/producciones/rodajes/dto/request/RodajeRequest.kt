package org.wolve.geofilm.producciones.rodajes.dto.request

data class RodajeRequest(
    val produccionId: String,
    val ubicacionId: String,
    val notas: String?,
    val imagenes: List<String>
)