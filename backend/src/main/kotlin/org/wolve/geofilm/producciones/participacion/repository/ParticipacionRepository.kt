package org.wolve.geofilm.producciones.participacion.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.wolve.geofilm.producciones.participacion.models.Participacion
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.profesional.model.Profesional
import java.util.*

interface IParticipacionRepository : JpaRepository<Participacion, String> {
    fun findByProduccion(produccion: Produccion, pageable: Pageable): Page<Participacion>
    fun findAllByProfesional(profesional: Profesional): List<Participacion>

    @Query("""
        SELECT DISTINCT s FROM Participacion s
        LEFT JOIN FETCH s.profesional p
        WHERE s.id = :id
    """)
    override fun findById(@io.lettuce.core.dynamic.annotation.Param("id") id: String): Optional<Participacion>
}