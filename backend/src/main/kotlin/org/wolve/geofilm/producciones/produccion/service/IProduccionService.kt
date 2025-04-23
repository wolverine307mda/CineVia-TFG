// src/main/kotlin/org/wolve/geofilm/producciones/produccion/service/IProduccionService.kt
package org.wolve.geofilm.producciones.produccion.service

import org.wolve.geofilm.producciones.produccion.dto.ProduccionRequest
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.produccion.models.*
import org.wolve.geofilm.utils.paginationUtils.PaginatedResponse
import org.wolve.geofilm.utils.paginationUtils.PaginationUtils

interface IProduccionService {
    // region Operaciones CRUD básicas
    fun getProduccionById(id: String): ProduccionResponse?
    fun getAllProducciones(): List<ProduccionResponse>
    fun createProduccion(request: ProduccionRequest): ProduccionResponse
    fun updateProduccion(id: String, request: ProduccionRequest): ProduccionResponse?
    fun deleteProduccion(id: String)
    // endregion

    // region Operaciones paginadas
    fun getAllProducciones(
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginationUtils.PaginatedResponse<ProduccionResponse>

    fun filtrarPorCategorias(
        categorias: Set<Categoria>,
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginationUtils.PaginatedResponse<ProduccionResponse>

    fun filtrarProducciones(
        titulo: String?,
        tipo: TipoProduccion?,
        estrenoDesde: Int?,
        estrenoHasta: Int?,
        categorias: Set<Categoria>?,
        clasificacionEdad: ClasificacionEdad?,
        duracionMin: Int?,
        duracionMax: Int?,
        page: Int,
        size: Int,
        sortBy: List<String>,
        sortDirection: String
    ): PaginatedResponse<ProduccionResponse>
    // endregion

    // region Métodos de apoyo
    fun findEntityById(id: String): Produccion
    fun existsById(id: String): Boolean
    fun countParticipacionesByProduccionId(produccionId: String): Long
    // endregion

    // region Métodos para enumerados
    fun getClasificacionesEdad(): List<ClasificacionEdad>
    fun getCategoriasDisponibles(): List<Categoria>
    fun getTiposProduccion(): List<TipoProduccion>
    // endregion
}