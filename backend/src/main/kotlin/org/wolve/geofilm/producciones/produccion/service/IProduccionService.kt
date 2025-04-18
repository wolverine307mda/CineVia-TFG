package org.wolve.geofilm.producciones.produccion.service

import org.wolve.geofilm.producciones.produccion.dto.ProduccionRequest
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.produccion.models.Produccion

interface IProduccionService {
    // Métodos existentes
    fun getProduccionById(id: String): ProduccionResponse?
    fun getAllProducciones(): List<ProduccionResponse>
    fun getProduccionesByTitulo(titulo: String): List<ProduccionResponse>
    fun createProduccion(request: ProduccionRequest): ProduccionResponse
    fun updateProduccion(id: String, request: ProduccionRequest): ProduccionResponse?
    fun deleteProduccion(id: String)

    // Nuevos métodos requeridos por ParticipacionService
    fun findEntityById(id: String): Produccion
    fun existsById(id: String): Boolean
    fun countParticipacionesByProduccionId(produccionId: String): Long
}