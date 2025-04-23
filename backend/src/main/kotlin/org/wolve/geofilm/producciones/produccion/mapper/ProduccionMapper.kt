package org.wolve.geofilm.producciones.produccion.mapper

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import org.wolve.geofilm.producciones.produccion.dto.ProduccionRequest
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.produccion.models.ClasificacionEdad
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.utils.paginationUtils.PaginationUtils

@Component
class ProduccionMapper {
    fun toResponse(produccion: Produccion): ProduccionResponse = ProduccionResponse(
        id = produccion.id,
        titulo = produccion.titulo,
        tipo = produccion.tipo,
        estreno = produccion.estreno,
        duracion = produccion.duracion,
        sinopsis = produccion.sinopsis,
        imagen = produccion.imagen,
        informacion = produccion.informacion,
        puntuacion = produccion.puntuacion,
        categorias = produccion.categorias,
        clasificacionEdad = produccion.clasificacionEdad.valorNumerico
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
        puntuacion = entity.puntuacion,
        categorias = entity.categorias,
        clasificacionEdad = entity.clasificacionEdad.valorNumerico
    )

    fun toProduccionEntity(request: ProduccionRequest): Produccion = Produccion(
        titulo = request.titulo,
        tipo = request.tipo,
        estreno = request.estreno,
        duracion = request.duracion,
        sinopsis = request.sinopsis,
        imagen = request.imagen,
        informacion = request.informacion,
        puntuacion = request.puntuacion,
        categorias = request.categorias.toMutableSet(),
        clasificacionEdad = ClasificacionEdad.fromValorNumerico(request.clasificacionEdad)
            ?: throw IllegalArgumentException("Clasificación de edad no válida")
    )

    fun toPaginatedResponse(page: Page<Produccion>): PaginationUtils.PaginatedResponse<ProduccionResponse> {
        return PaginationUtils.toPaginatedResponse(page.map { toResponse(it) })
    }
}