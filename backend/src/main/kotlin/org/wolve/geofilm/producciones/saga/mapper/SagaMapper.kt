package org.wolve.geofilm.producciones.saga.mapper

import org.springframework.stereotype.Component
import org.wolve.geofilm.producciones.produccion.mapper.ProduccionMapper
import org.wolve.geofilm.producciones.saga.dto.SagaRequest
import org.wolve.geofilm.producciones.saga.dto.SagaResponse
import org.wolve.geofilm.producciones.saga.model.Saga

@Component
class SagaMapper {

    fun toSagaEntity(request: SagaRequest): Saga {
        return Saga(
            nombre = request.nombre,
            descripcion = request.descripcion,
            isAcabada = request.isAcabada,
            fechaInicio = request.fechaInicio,
            fechaFin = request.fechaFin,
            imagen = request.imagen
        )
    }

    fun toSagaResponse(saga: Saga): SagaResponse {
        return SagaResponse(
            id = saga.id,
            nombre = saga.nombre,
            descripcion = saga.descripcion,
            isAcabada = saga.isAcabada,
            fechaInicio = saga.fechaInicio,
            fechaFin = saga.fechaFin,
            imagen = saga.imagen,
            producciones = emptyList() // No mapeamos las producciones aquí porque "ignore = true"
        )
    }

    fun toSagaResponseWithProducciones(saga: Saga, produccionMapper: ProduccionMapper): SagaResponse {
        return SagaResponse(
            id = saga.id,
            nombre = saga.nombre,
            descripcion = saga.descripcion,
            isAcabada = saga.isAcabada,
            fechaInicio = saga.fechaInicio,
            fechaFin = saga.fechaFin,
            imagen = saga.imagen,
            producciones = saga.producciones.map { produccionMapper.toProduccionResponse(it) }
        )
    }
}
