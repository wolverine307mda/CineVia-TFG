package org.wolve.geofilm.producciones.saga.dto

import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import java.util.*

data class SagaResponse(
    val id: String,
    val nombre: String,
    val descripcion: String?,
    val isAcabada: Boolean,
    val fechaInicio: Date,
    val fechaFin: Date?,
    val imagen: String?,
    val producciones: List<ProduccionResponse> = emptyList()
)