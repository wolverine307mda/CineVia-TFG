package org.wolve.geofilm.producciones.saga.service

import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.wolve.geofilm.producciones.produccion.dto.ProduccionListResponse
import org.wolve.geofilm.producciones.produccion.dto.ProduccionRequest
import org.wolve.geofilm.producciones.produccion.mapper.ProduccionMapper
import org.wolve.geofilm.producciones.saga.dto.*
import org.wolve.geofilm.producciones.saga.exception.SagaNotFoundException
import org.wolve.geofilm.producciones.saga.mapper.SagaMapper
import org.wolve.geofilm.producciones.saga.repository.SagaRepository
import java.util.*

@Service
@Transactional
class SagaServiceImpl(
    private val repository: SagaRepository,
    private val mapper: SagaMapper,
    private val prodMapper: ProduccionMapper

) : ISagaService {

    @CacheEvict(value = ["sagas"], allEntries = true)
    override fun createSaga(request: SagaRequest): SagaResponse {
        if (repository.existsByNombre(request.nombre)) {
            throw IllegalArgumentException("Ya existe una saga con ese nombre")
        }
        val saga = mapper.toEntity(request)
        return mapper.toResponse(repository.save(saga))
    }

    @Cacheable(value = ["saga"], key = "#id")
    override fun getSagaById(id: String): SagaResponse {
        val saga = repository.findById(id).orElseThrow {
            SagaNotFoundException("Saga no encontrada con id: $id")
        }
        return mapper.toResponse(saga)
    }

    @CacheEvict(value = ["saga", "sagas"], key = "#id", allEntries = true)
    override fun updateSaga(id: String, request: SagaRequest): SagaResponse {
        val existingSaga = repository.findById(id).orElseThrow {
            SagaNotFoundException("Saga no encontrada con id: $id")
        }

        if (repository.existsByNombre(request.nombre) && existingSaga.nombre != request.nombre) {
            throw IllegalArgumentException("Ya existe una saga con ese nombre")
        }

        val updatedSaga = existingSaga.copy(
            nombre = request.nombre,
            descripcion = request.descripcion,
            isAcabada = request.isAcabada,
            fechaInicio = request.fechaInicio,
            fechaFin = request.fechaFin,
            imagen = request.imagen
        )

        return mapper.toResponse(repository.save(updatedSaga))
    }

    @CacheEvict(value = ["saga", "sagas"], key = "#id", allEntries = true)
    override fun deleteSaga(id: String) {
        if (!repository.existsById(id)) {
            throw SagaNotFoundException("Saga no encontrada con id: $id")
        }
        repository.deleteById(id)
    }

    @Cacheable(value = ["sagas"])
    override fun getAllSagas(pageable: Pageable): SagaListResponse {
        return mapper.toPageResponse(repository.findAll(pageable))
    }

    @Cacheable(value = ["sagas"], key = "#nombre")
    override fun searchSagasByNombre(nombre: String, pageable: Pageable): SagaListResponse {
        val page = repository.findByNombreContainingIgnoreCase(nombre, pageable)
        return mapper.toPageResponse(page)
    }

    @Cacheable(value = ["sagas"], key = "#isAcabada")
    override fun getSagasByEstado(isAcabada: Boolean, pageable: Pageable): SagaListResponse {
        return mapper.toPageResponse(repository.findByEstado(isAcabada, pageable))
    }

    @Cacheable(value = ["sagas"], key = "{#fechaInicio, #fechaFin}")
    override fun getSagasByRangoFechas(
        fechaInicio: Date,
        fechaFin: Date,
        pageable: Pageable
    ): SagaListResponse {
        return mapper.toPageResponse(repository.findByRangoFechas(fechaInicio, fechaFin, pageable))
    }

    override fun existsByNombre(nombre: String): Boolean {
        return repository.existsByNombre(nombre)
    }

    @CacheEvict(value = ["saga", "sagas"], key = "#sagaId", allEntries = true)
    override fun agregarProduccionASaga(sagaId: String, produccionRequest: ProduccionRequest): SagaResponse {
        val saga = repository.findById(sagaId).orElseThrow {
            SagaNotFoundException("Saga no encontrada con id: $sagaId")
        }

        val produccion = prodMapper.toProduccionEntity(produccionRequest)
        saga.producciones.add(produccion)

        return mapper.toResponse(repository.save(saga))
    }

    @Cacheable(value = ["sagaProducciones"], key = "#sagaId")
    override fun obtenerProduccionesDeSaga(sagaId: String, pageable: Pageable): ProduccionListResponse {
        val produccionesPage = repository.findProduccionesBySagaId(sagaId, pageable)
        return prodMapper.toProduccionPageResponse(produccionesPage)
    }
}