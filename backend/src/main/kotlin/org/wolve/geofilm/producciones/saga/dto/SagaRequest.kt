package org.wolve.geofilm.producciones.saga.dto

import java.util.*

data class SagaRequest(
    val nombre: String,
    val descripcion: String? = null,
    val isAcabada: Boolean = false,
    val fechaInicio: Date,
    val fechaFin: Date? = null,
    val imagen: String? = null
)