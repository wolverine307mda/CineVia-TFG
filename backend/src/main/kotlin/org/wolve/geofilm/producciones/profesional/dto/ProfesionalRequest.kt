package org.wolve.geofilm.producciones.profesional.dto


data class ProfesionalRequest(
    val nombre: String,
    val foto: String?,
    val fechaNacimiento: String,
    val fechaInicio: String,
    val lugarNacimiento: String,
    val biografia: String?
)