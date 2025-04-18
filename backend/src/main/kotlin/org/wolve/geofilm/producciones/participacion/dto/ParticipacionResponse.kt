package org.wolve.geofilm.producciones.participacion.dto

import java.time.LocalDateTime

data class ParticipacionResponse(
    val id: String,
    val produccionId: String,
    val produccionTitulo: String,
    val profesionalId: String,
    val profesionalNombre: String,
    val rol: String,
    val papel: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)