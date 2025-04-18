package org.wolve.geofilm.producciones.participacion.services

import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.wolve.geofilm.producciones.participacion.dto.*
import org.wolve.geofilm.producciones.participacion.exceptions.*
import org.wolve.geofilm.producciones.participacion.mappers.ParticipacionMapper
import org.wolve.geofilm.producciones.participacion.models.Participacion
import org.wolve.geofilm.producciones.participacion.models.RolParticipacion
import org.wolve.geofilm.producciones.participacion.repository.ParticipacionRepository
import org.wolve.geofilm.producciones.produccion.exceptions.ProduccionNotFoundException
import org.wolve.geofilm.producciones.produccion.service.ProduccionServiceImpl
import org.wolve.geofilm.producciones.profesional.exception.ProfesionalNotFoundException
import org.wolve.geofilm.producciones.profesional.service.ProfesionalServiceImpl

@Service
class ParticipacionServiceImpl(
    private val repository: ParticipacionRepository,
    private val mapper: ParticipacionMapper,
    private val produccionService: ProduccionServiceImpl,
    private val profesionalService: ProfesionalServiceImpl
) : IParticipacionService {

    @Transactional(readOnly = true)
    override fun findAll(): List<ParticipacionResponse> {
        return repository.findAll().map { mapper.toResponse(it) }
    }

    @Transactional(readOnly = true)
    override fun findById(id: String): ParticipacionResponse {
        val participacion = repository.findById(id)
            .orElseThrow { ParticipacionNotFoundException(id) }
        return mapper.toResponse(participacion)
    }

    @Transactional
    @CacheEvict(allEntries = true)
    override fun create(request: ParticipacionRequest): ParticipacionResponse {
        // Validar rol primero
        val rol = try {
            RolParticipacion.valueOf(request.rol.uppercase())
        } catch (e: IllegalArgumentException) {
            throw InvalidRolParticipacionException(request.rol)
        }

        val produccion = produccionService.
        findEntityById(request.produccionId)
        val profesional = profesionalService.findEntityById(request.profesionalId)

        // Verificar si ya existe una participación similar
        repository.findByProduccionAndProfesionalAndRol(produccion, profesional, rol)?.let {
            throw ParticipacionAlreadyExistsException(
                produccion.id,
                profesional.id,
                rol.name
            )
        }

        val participacion = mapper.toEntity(request, produccion, profesional)
        return mapper.toResponse(repository.save(participacion))
    }

    @Transactional
    @CacheEvict(allEntries = true)
    override fun update(id: String, request: ParticipacionRequest): ParticipacionResponse {
        val existing = repository.findById(id)
            .orElseThrow { ParticipacionNotFoundException(id) }

        // Validar rol
        val rol = try {
            RolParticipacion.valueOf(request.rol.uppercase())
        } catch (e: IllegalArgumentException) {
            throw InvalidRolParticipacionException(request.rol)
        }

        val produccion = produccionService.findEntityById(request.produccionId)
        val profesional = profesionalService.findEntityById(request.profesionalId)

        // Verificar duplicados (excluyendo la actual)
        repository.findByProduccionAndProfesionalAndRolAndIdNot(produccion, profesional, rol, id)?.let {
            throw ParticipacionAlreadyExistsException(
                produccion.id,
                profesional.id,
                rol.name
            )
        }

        existing.apply {
            this.produccion = produccion
            this.profesional = profesional
            this.rol = rol
            this.papel = request.papel
        }

        return mapper.toResponse(repository.save(existing))
    }

    @Transactional
    @CacheEvict(allEntries = true)
    override fun delete(id: String) {
        val participacion = repository.findById(id)
            .orElseThrow { ParticipacionNotFoundException(id) }

        // Validar restricciones antes de eliminar
        if (participacion.profesional.participaciones.size == 1) {
            throw ParticipacionAssociatedProfesionalException(id)
        }

        if (participacion.produccion.participaciones.size == 1) {
            throw ParticipacionAssociatedProduccionException(id)
        }

        repository.deleteById(id)
    }

    @Transactional(readOnly = true)
    override fun findByProfesionalId(profesionalId: String): List<ParticipacionProfesionalResponse> {
        if (!profesionalService.existsById(profesionalId)) {
            throw ProfesionalNotFoundException(profesionalId)
        }
        return repository.findByProfesionalId(profesionalId).map { mapper.toProfesionalResponse(it) }
    }

    @Transactional(readOnly = true)
    override fun findByProduccionId(produccionId: String): List<ParticipacionProduccionResponse> {
        if (!produccionService.existsById(produccionId)) {
            throw ProduccionNotFoundException(produccionId)
        }
        return repository.findByProduccionId(produccionId).map { mapper.toProduccionResponse(it) }
    }

    override fun findEntityById(id: String): Participacion {
        return repository.findById(id)
            .orElseThrow { ParticipacionNotFoundException(id) }
    }
}