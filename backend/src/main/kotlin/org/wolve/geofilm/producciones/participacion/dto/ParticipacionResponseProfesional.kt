package org.wolve.geofilm.producciones.participacion.dto

import org.wolve.geofilm.producciones.participacion.models.RolParticipacion
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import java.time.LocalDateTime

data class ParticipacionResponseProfesional(
    val id: String,
    val rol: RolParticipacion,
    val papel: String?,
    val produccion: ProduccionResponse,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
