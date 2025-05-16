package org.wolve.geofilm.producciones.produccion.dto

import org.wolve.geofilm.producciones.produccion.models.Categoria

data class ProduccionCompletaResponse(
    val id: String,
    val titulo: String,
    val tipo: String,
    val estreno: String?,
    val duracion: Int?,
    val sinopsis: String?,
    val imagen: String?,
    val informacion: String?,
    val puntuacion : Double,
    val clasificacionEdad : Int?,
    val categorias: Set<Categoria>,
    val saga: SagaDto?,
    val participaciones: List<ParticipacionDto>,
    val rodajes: List<RodajeDto>
)

data class SagaDto(
    val id: String,
    val nombre: String?
)

data class ParticipacionDto(
    val id: String,
    val profesionalId: String,
    val profesionalNombre: String,
    val rol: String
)

data class RodajeDto(
    val id: String,
    val fecha: String?,
    val ubicacion: UbicacionDto
)

data class UbicacionDto(
    val id: String,
    val nombre: String?,
    val latitud: Double,
    val longitud: Double
)