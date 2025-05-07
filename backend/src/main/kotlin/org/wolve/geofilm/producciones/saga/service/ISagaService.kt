package org.wolve.geofilm.producciones.saga.service

import org.wolve.geofilm.producciones.saga.dto.SagaFilterParams
import org.wolve.geofilm.producciones.saga.dto.SagaRequest
import org.wolve.geofilm.producciones.saga.dto.SagaResponse
import org.wolve.geofilm.utils.paginationUtils.PaginatedResponse
import org.wolve.geofilm.utils.paginationUtils.PaginationUtils

interface ISagaService {
    // CRUD básico
    fun getSagaById(id: String): SagaResponse?
    fun getAllSagas(): List<SagaResponse>
    fun createSaga(request: SagaRequest): SagaResponse
    fun updateSaga(id: String, request: SagaRequest): SagaResponse?
    fun deleteSaga(id: String)

    // Operaciones paginadas
    fun getAllSagasPaginated(
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginationUtils.PaginatedResponse<SagaResponse>

    // Filtrado
    fun filterSagas(
        params: SagaFilterParams,
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginatedResponse<SagaResponse>

    // Métodos de apoyo
    fun existsById(id: String): Boolean
    fun existsByNombre(nombre: String): Boolean
}