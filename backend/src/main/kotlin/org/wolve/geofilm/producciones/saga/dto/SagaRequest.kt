package org.wolve.geofilm.producciones.saga.dto

import java.util.Date

data class SagaRequest(
    val nombre: String,
    val descripcion: String?,
    val isAcabada: Boolean,
    val fechaInicio: Date,
    val fechaFin: Date?,
    val imagen: String?  // Nuevo campo
)