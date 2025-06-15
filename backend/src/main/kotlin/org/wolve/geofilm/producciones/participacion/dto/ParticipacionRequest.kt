package org.wolve.geofilm.producciones.participacion.dto

import org.wolve.geofilm.producciones.participacion.models.RolParticipacion

data class ParticipacionRequest(
    val produccionId: String,
    val profesionalId: String,
    val rol: RolParticipacion,
    val papel: String? = null
)