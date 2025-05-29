package org.wolve.geofilm.rodajes.service

import org.wolve.geofilm.rodajes.dto.request.RodajeRequest
import org.wolve.geofilm.rodajes.dto.response.RodajeResponse

interface IRodajeService {
    fun findById(id: String): RodajeResponse
    fun create(request: RodajeRequest): RodajeResponse
    fun update(id: String, request: RodajeRequest): RodajeResponse
    fun delete(id: String)
    fun findByProduccion(produccionId: String, page: Int, size: Int): Map<String, Any>
}