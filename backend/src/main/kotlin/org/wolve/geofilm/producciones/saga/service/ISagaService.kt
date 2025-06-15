package org.wolve.geofilm.producciones.saga.service

import org.wolve.geofilm.producciones.saga.dto.SagaFilterParams
import org.wolve.geofilm.producciones.saga.dto.SagaRequest
import org.wolve.geofilm.producciones.saga.dto.SagaResponse
import org.wolve.geofilm.utils.pagination.PaginatedResponse
import org.wolve.geofilm.utils.pagination.PaginationUtils

interface ISagaService {
    // CRUD básico
    fun getSagaById(id: String): SagaResponse?
    fun getAllSagas(): List<SagaResponse>
    fun createSaga(request: SagaRequest): SagaResponse
    fun updateSaga(id: String, request: SagaRequest): SagaResponse?
    fun actualizarImagenSaga(id: String, nuevaUrl: String): SagaResponse

    fun deleteSaga(id: String)

    // Operaciones paginadas
    fun getAllSagasPaginated(page: Int, size: Int, sortBy: List<String>, sortDirection: String ): PaginationUtils.PaginatedResponse<SagaResponse>
    fun filterSagas( params: SagaFilterParams, page: Int, size: Int, sortBy: List<String>, sortDirection: String ): PaginatedResponse<SagaResponse>

    // Creacion de la relación
    fun agregarProduccionASaga(sagaId: String, produccionId: String)
    fun eliminarProduccionDeSaga(produccionId: String)

    // Métodos de apoyo
    fun existsById(id: String): Boolean
    fun existsByNombre(nombre: String): Boolean
}