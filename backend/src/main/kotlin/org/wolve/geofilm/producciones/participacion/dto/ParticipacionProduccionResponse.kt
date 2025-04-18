package org.wolve.geofilm.producciones.participacion.dto

import java.util.*

data class ParticipacionProduccionResponse(
    val id: String,
    val produccionId: String,
    val produccionTitulo: String,
    val produccionImagen: String?,
    val rol: String,
    val papel: String?,
    val estreno: Date
)