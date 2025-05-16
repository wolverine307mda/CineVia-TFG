package org.wolve.geofilm.producciones.participacion.dto

import org.wolve.geofilm.producciones.participacion.models.RolParticipacion
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalResponse
import java.time.LocalDateTime

data class ParticipacionResponse(
    val id: String,
    val rol: RolParticipacion,
    val papel: String?,
    val profesional: ProfesionalResponse,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)