package org.wolve.geofilm.producciones.participacion.services

import org.wolve.geofilm.producciones.participacion.dto.*
import org.wolve.geofilm.producciones.participacion.models.Participacion

interface IParticipacionService {
    fun findAll(): List<ParticipacionResponse>
    fun findById(id: String): ParticipacionResponse
    fun create(request: ParticipacionRequest): ParticipacionResponse
    fun update(id: String, request: ParticipacionRequest): ParticipacionResponse
    fun delete(id: String)
    fun findByProfesionalId(profesionalId: String): List<ParticipacionProfesionalResponse>
    fun findByProduccionId(produccionId: String): List<ParticipacionProduccionResponse>
    fun findEntityById(id: String): Participacion
}
