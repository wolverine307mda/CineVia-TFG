package org.wolve.geofilm.rodajes.service.impl

import org.springframework.stereotype.Service
import org.wolve.geofilm.rodajes.exception.RodajeNotFoundException
import org.wolve.geofilm.rodajes.mapper.RodajeMapper
import org.wolve.geofilm.rodajes.repository.RodajeRepository
import org.wolve.geofilm.rodajes.service.RodajeService

@Service
class RodajeServiceImpl(
    private val rodajeRepository: RodajeRepository,
    private val mapper: RodajeMapper
) : RodajeService {

    override fun findById(id: String) = rodajeRepository.findById(id)
        .map { mapper.toResponse(it) }
        .orElseThrow { RodajeNotFoundException(id) }
}