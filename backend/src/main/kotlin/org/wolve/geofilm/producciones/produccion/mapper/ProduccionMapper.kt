package org.wolve.geofilm.producciones.produccion.mapper

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import org.wolve.geofilm.producciones.produccion.dto.ProduccionListResponse
import org.wolve.geofilm.producciones.produccion.dto.ProduccionRequest
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.produccion.models.Produccion

@Component
class ProduccionMapper {
    fun Produccion.toResponse(): ProduccionResponse = ProduccionResponse(
        id = this.id,
        titulo = this.titulo,
        tipo = this.tipo,
        estreno = this.estreno,
        duracion = this.duracion,
        sinopsis = this.sinopsis,
        imagen = this.imagen,
        informacion = this.informacion,
        categorias = this.categorias,
        clasificacionEdad = this.clasificacionEdad,
    )

    fun toProduccionResponse(entity: Produccion): ProduccionResponse = ProduccionResponse(
        id = entity.id,
        titulo = entity.titulo,
        tipo = entity.tipo,
        estreno = entity.estreno,
        duracion = entity.duracion,
        sinopsis = entity.sinopsis,
        imagen = entity.imagen,
        informacion = entity.informacion,
        categorias = entity.categorias,
        clasificacionEdad = entity.clasificacionEdad
    )

    fun toProduccionEntity(request: ProduccionRequest): Produccion = Produccion(
        titulo = request.titulo,
        tipo = request.tipo,
        estreno = request.estreno,
        duracion = request.duracion,
        sinopsis = request.sinopsis,
        imagen = request.imagen,
        informacion = request.informacion,
        categorias = request.categorias.toMutableSet(), // Aseguramos mutableSet
        clasificacionEdad = request.clasificacionEdad
    ).apply {
        // Aquí puedes inicializar relaciones si es necesario
    }

    fun toProduccionPageResponse(page: Page<Produccion>): ProduccionListResponse =
        ProduccionListResponse(
            producciones = page.content.map { it.toResponse() },
            total = page.totalElements,
            page = page.number,
            size = page.size
        )
}