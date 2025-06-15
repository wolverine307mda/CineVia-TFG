package org.wolve.geofilm.producciones.produccion.mapper

import org.springframework.data.domain.Page
import org.wolve.geofilm.producciones.produccion.dto.ProduccionCompletaResponse
import org.wolve.geofilm.producciones.produccion.dto.ProduccionRequest
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.produccion.models.ClasificacionEdad
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.utils.pagination.PaginationUtils
import org.springframework.stereotype.Component
import org.wolve.geofilm.producciones.produccion.dto.*


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

    fun toCompletaResponse(p: Produccion): ProduccionCompletaResponse = ProduccionCompletaResponse(
        id = p.id,
        titulo = p.titulo,
        tipo = p.tipo.name,
        estreno = p.estreno.toString(),
        duracion = p.duracion,
        sinopsis = p.sinopsis.toString(),
        imagen = p.imagen.toString(),
        informacion = p.informacion.toString(),
        puntuacion = p.puntuacion.toDouble(),
        clasificacionEdad = p.clasificacionEdad.valorNumerico,
        categorias = p.categorias,
        saga = p.saga?.let { SagaDto(it.id, it.nombre) },
        participaciones = p.participaciones.map {
            ParticipacionDto(
                id = it.id,
                profesionalId = it.profesional.id,
                profesionalNombre = it.profesional.nombre,
                rol = it.rol.name
            )
        },
        rodajes = p.rodajes.map {
            RodajeDto(
                id = it.id,
                fecha = it.createdAt?.toString(),
                ubicacion = UbicacionDto(
                    id = it.ubicacion.id,
                    nombre = it.ubicacion.nombre,
                    latitud = it.ubicacion.latitud,
                    longitud = it.ubicacion.longitud
                )
            )
        }
    )

    fun toPaginatedResponse(page: Page<Produccion>): PaginationUtils.PaginatedResponse<ProduccionResponse> {
        return PaginationUtils.toPaginatedResponse(page.map { toResponse(it) })
    }
}