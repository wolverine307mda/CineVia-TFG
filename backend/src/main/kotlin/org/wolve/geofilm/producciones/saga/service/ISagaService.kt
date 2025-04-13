package org.wolve.geofilm.producciones.saga.service

import org.springframework.data.domain.Pageable
import org.wolve.geofilm.producciones.produccion.dto.ProduccionListResponse
import org.wolve.geofilm.producciones.produccion.dto.ProduccionRequest
import org.wolve.geofilm.producciones.saga.dto.SagaRequest
import org.wolve.geofilm.producciones.saga.dto.SagaListResponse
import org.wolve.geofilm.producciones.saga.dto.SagaResponse
import java.util.*

interface ISagaService {
    fun createSaga(request: SagaRequest): SagaResponse
    fun getSagaById(id: String): SagaResponse
    fun updateSaga(id: String, request: SagaRequest): SagaResponse
    fun deleteSaga(id: String)
    fun getAllSagas(pageable: Pageable): SagaListResponse
    fun searchSagasByNombre(nombre: String, pageable: Pageable): SagaListResponse
    fun getSagasByEstado(isAcabada: Boolean, pageable: Pageable): SagaListResponse
    fun getSagasByRangoFechas(fechaInicio: Date, fechaFin: Date, pageable: Pageable): SagaListResponse
    fun existsByNombre(nombre: String): Boolean
    fun agregarProduccionASaga(sagaId: String, produccionRequest: ProduccionRequest): SagaResponse
    fun obtenerProduccionesDeSaga(sagaId: String, pageable: Pageable): ProduccionListResponse
}