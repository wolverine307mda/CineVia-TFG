package org.wolve.geofilm.producciones.produccion.dto

import org.wolve.geofilm.producciones.produccion.models.Categoria
import org.wolve.geofilm.producciones.produccion.models.TipoProduccion
import java.util.*

data class ProduccionResponse(
    val id: String,
    val titulo: String,
    val tipo: TipoProduccion,
    val estreno: Date,
    val duracion: Int,
    val sinopsis: String,
    val imagen: String?,
    val informacion: String?,
    val categorias: Set<Categoria>,
    val clasificacionEdad: Int,
)