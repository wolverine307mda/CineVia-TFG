package org.wolve.geofilm.producciones.saga.mapper

import org.springframework.stereotype.Component
import org.wolve.geofilm.producciones.produccion.mapper.ProduccionMapper
import org.wolve.geofilm.producciones.saga.dto.SagaRequest
import org.wolve.geofilm.producciones.saga.dto.SagaResponse
import org.wolve.geofilm.producciones.saga.model.Saga

@Component
class SagaMapper(
    private val produccionMapper: ProduccionMapper
) {

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

    fun toSagaResponse(s: Saga): SagaResponse {
        return SagaResponse(
            id = s.id,
            nombre = s.nombre,
            descripcion = s.descripcion,
            fechaInicio = s.fechaInicio.toString(),
            fechaFin = s.fechaFin?.toString(),
            imagen = s.imagen,
            isAcabada = s.isAcabada,
            producciones = s.producciones.map { produccionMapper.toResponse(it) }
        )
    }
}
