package org.wolve.geofilm.producciones.saga.dto

data class SagaSimpleResponse(
    val id: String,
    val nombre: String,
    val isAcabada: Boolean
)