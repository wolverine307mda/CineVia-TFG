package org.wolve.geofilm.producciones.participacion.dto

data class ParticipacionProfesionalResponse(
    val id: String,
    val profesionalId: String,
    val profesionalNombre: String,
    val profesionalFoto: String?,
    val rol: String,
    val papel: String?
)