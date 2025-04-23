package org.wolve.geofilm.producciones.profesional.dto

import java.util.*

data class ProfesionalRequest(
    val nombre: String,
    val foto: String?,
    val fechaNacimiento: Date,
    val fechaInicio: Date,
    val lugarNacimiento: String,
    val biografia: String?
)