package org.wolve.geofilm.producciones.rodajes.service

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.wolve.geofilm.producciones.produccion.exceptions.ProduccionNotFoundException
import org.wolve.geofilm.producciones.produccion.repository.ProduccionRepository
import org.wolve.geofilm.producciones.rodajes.dto.request.RodajeRequest
import org.wolve.geofilm.producciones.rodajes.dto.response.RodajeResponse
import org.wolve.geofilm.producciones.rodajes.exception.RodajeNotFoundException
import org.wolve.geofilm.producciones.rodajes.mapper.RodajeMapper
import org.wolve.geofilm.producciones.rodajes.models.Rodaje
import org.wolve.geofilm.producciones.rodajes.repository.RodajeRepository
import org.wolve.geofilm.producciones.ubicaciones.exceptions.UbicacionNotFoundException
import org.wolve.geofilm.producciones.ubicaciones.repository.UbicacionRepository

@Service
class RodajeServiceImpl(
    private val rodajeRepository: org.wolve.geofilm.producciones.rodajes.repository.RodajeRepository,
    private val produccionRepository: ProduccionRepository,
    private val ubicacionRepository: org.wolve.geofilm.producciones.ubicaciones.repository.UbicacionRepository,
    private val mapper: org.wolve.geofilm.producciones.rodajes.mapper.RodajeMapper
) : IRodajeService {

    override fun findById(id: String) = rodajeRepository.findById(id)
        .map { mapper.toResponse(it) }
        .orElseThrow { RodajeNotFoundException(id) }

    override fun delete(id: String) {
        val rodaje = rodajeRepository.findById(id)
            .orElseThrow { RodajeNotFoundException(id) }
        rodajeRepository.delete(rodaje)
    }

    override fun update(id: String, request: RodajeRequest): RodajeResponse {
        val rodaje = rodajeRepository.findById(id)
            .orElseThrow { RodajeNotFoundException(id) }

        val produccion = produccionRepository.findById(request.produccionId)
            .orElseThrow { ProduccionNotFoundException(request.produccionId) }

        val ubicacion = ubicacionRepository.findById(request.ubicacionId)
            .orElseThrow { UbicacionNotFoundException(request.ubicacionId) }

        val updatedRodaje = rodaje.copy(
            produccion = produccion,
            ubicacion = ubicacion,
            notas = request.notas,
            imagenes = request.imagenes.toMutableList()
        )

        return mapper.toResponse(rodajeRepository.save(updatedRodaje))
    }

    override fun create(request: RodajeRequest): RodajeResponse {
        val produccion = produccionRepository.findById(request.produccionId)
            .orElseThrow { ProduccionNotFoundException(request.produccionId) }

        val ubicacion = ubicacionRepository.findById(request.ubicacionId)
            .orElseThrow { UbicacionNotFoundException(request.ubicacionId) }

        val rodaje = org.wolve.geofilm.producciones.rodajes.models.Rodaje(
            produccion = produccion,
            ubicacion = ubicacion,
            notas = request.notas,
            imagenes = request.imagenes.toMutableList()
        )

        return mapper.toResponse(rodajeRepository.save(rodaje))
    }

    override fun findByProduccion(produccionId: String, page: Int, size: Int): Map<String, Any> {
        val pageable = PageRequest.of(page, size)
        val rodajes = rodajeRepository.findAllByProduccionId(produccionId, pageable)

        return mapOf(
            "data" to rodajes.content.map { mapper.toResponse(it) },
            "totalItems" to rodajes.totalElements,
            "totalPages" to rodajes.totalPages
        )
    }

}