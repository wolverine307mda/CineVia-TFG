package org.wolve.geofilm.producciones.profesional.dto

import java.util.*

data class ProfesionalRequest(
    val nombre: String,
    val foto: String? = null,
    val fechaNacimiento: Date,
    val lugarNacimiento: String,
    val biografia: String? = null
)