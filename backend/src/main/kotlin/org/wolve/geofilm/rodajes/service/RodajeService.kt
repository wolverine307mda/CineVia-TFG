package org.wolve.geofilm.rodajes.service

import org.wolve.geofilm.rodajes.dto.response.RodajeResponse

interface RodajeService {
    fun findById(id: String): RodajeResponse
}