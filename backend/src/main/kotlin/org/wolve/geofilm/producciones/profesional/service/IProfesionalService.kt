package org.wolve.geofilm.producciones.profesional.service

import org.wolve.geofilm.producciones.profesional.dto.ProfesionalRequest
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalResponse
import org.wolve.geofilm.producciones.profesional.model.Profesional
import org.wolve.geofilm.utils.paginationUtils.PaginationUtils

interface IProfesionalService {
    fun getAllProfesionales(
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginationUtils.PaginatedResponse<ProfesionalResponse>

    fun searchByNombre(
        nombre: String,
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginationUtils.PaginatedResponse<ProfesionalResponse>

    fun filtrarProfesionales(
        nombre: String?,
        fechaNacimientoDesde: String?,
        fechaNacimientoHasta: String?,
        fechaInicioDesde: String?,
        fechaInicioHasta: String?,
        lugarNacimiento: String?,
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginationUtils.PaginatedResponse<ProfesionalResponse>

    fun getProfesionalById(id: String): ProfesionalResponse?
    fun createProfesional(request: ProfesionalRequest): ProfesionalResponse
    fun updateProfesional(id: String, request: ProfesionalRequest): ProfesionalResponse?
    fun deleteProfesional(id: String)
    fun findEntityById(id: String): Profesional?
}