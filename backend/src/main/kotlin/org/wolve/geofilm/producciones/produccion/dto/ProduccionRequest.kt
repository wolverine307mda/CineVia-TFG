package org.wolve.geofilm.producciones.produccion.dto

import org.wolve.geofilm.producciones.produccion.models.Categoria
import org.wolve.geofilm.producciones.produccion.models.TipoProduccion
import java.util.*

data class ProduccionRequest(
    val titulo: String,
    val tipo: TipoProduccion,
    val estreno: Date,
    val duracion: Int,
    val sinopsis: String,
    val imagen: String? = null,
    val informacion: String? = null,
    val puntuacion: Double = 0.0,
    val categorias: Set<Categoria>,
    val clasificacionEdad: Int,
)