package org.wolve.geofilm.producciones.participacion.dto

data class ParticipacionRequest(
    val produccionId: String,
    val profesionalId: String,
    val rol: String,
    val papel: String?
)