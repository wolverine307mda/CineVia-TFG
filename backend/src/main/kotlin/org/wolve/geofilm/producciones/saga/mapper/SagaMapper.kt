package org.wolve.geofilm.producciones.saga.mapper

import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.saga.dto.SagaListResponse
import org.wolve.geofilm.producciones.saga.dto.SagaRequest
import org.wolve.geofilm.producciones.saga.dto.SagaResponse
import org.wolve.geofilm.producciones.saga.dto.SagaSimpleResponse
import org.wolve.geofilm.producciones.saga.model.Saga

@Component
class SagaMapper {
    fun toEntity(request: SagaRequest): Saga = Saga(
        nombre = request.nombre,
        descripcion = request.descripcion,
        isAcabada = request.isAcabada,
        fechaInicio = request.fechaInicio,
        fechaFin = request.fechaFin,
        imagen = request.imagen
    )

    fun toResponse(entity: Saga): SagaResponse = SagaResponse(
        id = entity.id,
        nombre = entity.nombre,
        descripcion = entity.descripcion,
        isAcabada = entity.isAcabada,
        fechaInicio = entity.fechaInicio,
        fechaFin = entity.fechaFin,
        imagen = entity.imagen,
        producciones = entity.producciones.map { toProduccionResponse(it) }
    )

    fun toSimpleResponse(entity: Saga): SagaSimpleResponse = SagaSimpleResponse(
        id = entity.id,
        nombre = entity.nombre,
        isAcabada = entity.isAcabada
    )

    fun toPageResponse(page: Page<Saga>): SagaListResponse = SagaListResponse(
        sagas = page.map { toSimpleResponse(it) },
        total = page.totalElements,
        page = page.number,
        size = page.size
    )

    private fun toProduccionResponse(produccion: Produccion): ProduccionResponse = ProduccionResponse(
        id = produccion.id,
        titulo = produccion.titulo,
        tipo = produccion.tipo,
        estreno = produccion.estreno,
        duracion = produccion.duracion,
        sinopsis = produccion.sinopsis,
        imagen = produccion.imagen,
        informacion = produccion.informacion,
        categorias = produccion.categorias,
        clasificacionEdad = produccion.clasificacionEdad
    )
}