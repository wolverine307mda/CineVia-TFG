package org.wolve.geofilm.producciones.produccion.service

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.wolve.geofilm.producciones.produccion.dto.ProduccionRequest
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.produccion.mapper.ProduccionMapper
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.produccion.repository.ProduccionRepository
import org.wolve.geofilm.producciones.saga.repository.SagaRepository
import java.util.*

@Service
class ProduccionServiceImpl @Autowired constructor(
    private val produccionRepository: ProduccionRepository,
    private val sagaRepository: SagaRepository,
    private val produccionMapper: ProduccionMapper
) : IProduccionService {

    @Cacheable(value = ["producciones"], key = "#id")
    override fun getProduccionById(id: String): ProduccionResponse? {
        val produccionOpt = produccionRepository.findById(id)
        return produccionOpt.map { produccionMapper.toProduccionResponse(it) }.orElse(null)
    }

    @Cacheable(value = ["producciones"], key = "'all'")
    override fun getAllProducciones(): List<ProduccionResponse> =
        produccionRepository.findAll().map { produccionMapper.toProduccionResponse(it) }

    @Cacheable(value = ["produccionesByTitulo"], key = "#titulo")
    override fun getProduccionesByTitulo(titulo: String): List<ProduccionResponse> =
        produccionRepository.findByTitulo(titulo).map { produccionMapper.toProduccionResponse(it) }

    @CacheEvict(value = ["producciones", "produccionesByTitulo"], allEntries = true)
    @Transactional
    override fun createProduccion(request: ProduccionRequest): ProduccionResponse {
        val produccion = produccionMapper.toProduccionEntity(request)
        val saved = produccionRepository.save(produccion)
        return produccionMapper.toProduccionResponse(saved)    }

    @CacheEvict(value = ["producciones", "produccionesByTitulo"], allEntries = true)
    override fun updateProduccion(id: String, request: ProduccionRequest): ProduccionResponse? {
        val optionalProduccion = produccionRepository.findById(id)
        if (optionalProduccion.isPresent) {
            val produccion = optionalProduccion.get()

            val updated = produccion.copy(
                titulo = request.titulo,
                tipo = request.tipo,
                estreno = request.estreno,
                duracion = request.duracion,
                sinopsis = request.sinopsis,
                imagen = request.imagen,
                informacion = request.informacion,
                categorias = request.categorias.toMutableSet(),
                clasificacionEdad = request.clasificacionEdad,
            )
            val saved = produccionRepository.save(updated)
            return produccionMapper.toProduccionResponse(saved)
        }
        return null
    }

    @CacheEvict(value = ["producciones", "produccionesByTitulo"], allEntries = true)
    override fun deleteProduccion(id: String) {
        produccionRepository.deleteById(id)
    }
}
