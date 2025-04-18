package org.wolve.geofilm.producciones.profesional.dto

import java.util.*

data class ProfesionalResponse(
    val id: String,
    val nombre: String,
    val foto: String? = null,
    val fechaNacimiento: Date,
    val fechaInicio: Date,
    val lugarNacimiento: String,
    val biografia: String? = null
)