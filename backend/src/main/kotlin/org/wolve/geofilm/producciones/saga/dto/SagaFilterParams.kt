package org.wolve.geofilm.producciones.saga.dto

import java.util.*

data class SagaFilterParams(
    val nombre: String? = null,
    val isAcabada: Boolean? = null,
    val fechaInicioDesde: Date? = null,
    val fechaInicioHasta: Date? = null,
    val tieneImagen: Boolean? = null
)