package org.wolve.geofilm.producciones.saga.dto

import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import java.util.*

data class SagaResponse(
    val id: String,
    val nombre: String,
    val descripcion: String?,
    val fechaInicio: String,
    val fechaFin: String?,
    val imagen: String?,
    val isAcabada: Boolean,
    val producciones: List<ProduccionResponse>
)