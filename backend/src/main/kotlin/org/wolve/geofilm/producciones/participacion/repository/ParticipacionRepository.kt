package org.wolve.geofilm.producciones.participacion.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.wolve.geofilm.producciones.participacion.models.Participacion
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.profesional.model.Profesional

interface IParticipacionRepository : JpaRepository<Participacion, String> {
    fun findByProduccion(produccion: Produccion, pageable: Pageable): Page<Participacion>
    fun findByProfesional(profesional: Profesional, pageable: Pageable): Page<Participacion>

    @Query("SELECT p FROM Participacion p WHERE p.produccion.id = :produccionId AND p.profesional.id = :profesionalId")
    fun findByProduccionAndProfesional(
        @Param("produccionId") produccionId: String,
        @Param("profesionalId") profesionalId: String
    ): List<Participacion>
}