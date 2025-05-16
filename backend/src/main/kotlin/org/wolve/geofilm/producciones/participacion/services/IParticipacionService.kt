package org.wolve.geofilm.producciones.participacion.services

import org.wolve.geofilm.producciones.participacion.dto.ParticipacionRequest
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionResponse
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionResponseProfesional
import org.wolve.geofilm.utils.paginationUtils.PaginationUtils

interface IParticipacionService {
    fun createParticipacion(request: ParticipacionRequest): ParticipacionResponse
    fun getParticipacionById(id: String): ParticipacionResponse
    fun getAllParticipaciones(
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginationUtils.PaginatedResponse<ParticipacionResponse>
    fun getByProduccion(
        produccionId: String,
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginationUtils.PaginatedResponse<ParticipacionResponse>

    fun getByProfesional(profesionalId: String): List<ParticipacionResponseProfesional>

    fun updateParticipacion(id: String, request: ParticipacionRequest): ParticipacionResponse
    fun deleteParticipacion(id: String)
}