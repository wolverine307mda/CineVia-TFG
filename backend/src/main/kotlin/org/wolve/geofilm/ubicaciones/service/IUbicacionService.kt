package org.wolve.geofilm.ubicaciones.service

import org.wolve.geofilm.ubicaciones.dto.UbicacionFilterParams
import org.wolve.geofilm.ubicaciones.dto.UbicacionRequest
import org.wolve.geofilm.ubicaciones.dto.UbicacionResponse
import org.wolve.geofilm.ubicaciones.models.Ubicacion
import org.wolve.geofilm.utils.pagination.PaginatedResponse
import org.wolve.geofilm.utils.pagination.PaginationUtils

interface IUbicacionService {
    fun getUbicacionById(id: String): UbicacionResponse?
    fun getAllUbicaciones(): List<UbicacionResponse>
    fun createUbicacion(request: UbicacionRequest): UbicacionResponse
    fun updateUbicacion(id: String, request: UbicacionRequest): UbicacionResponse?
    fun deleteUbicacion(id: String)

    fun getAllUbicaciones(
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginationUtils.PaginatedResponse<UbicacionResponse>
    fun filterUbicaciones(
        params: UbicacionFilterParams,
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginatedResponse<UbicacionResponse>

    fun findEntityById(id: String): Ubicacion
    fun existsById(id: String): Boolean
}