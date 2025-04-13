package org.wolve.geofilm.producciones.saga.dto

import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import java.util.Date

data class SagaResponse(
    val id: String,
    val nombre: String,
    val descripcion: String?,
    val isAcabada: Boolean,
    val fechaInicio: Date,
    val fechaFin: Date?,
    val imagen: String?,  // Nuevo campo
    val producciones: List<ProduccionResponse>
)