package org.wolve.geofilm.producciones.participacion.repository

import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.wolve.geofilm.producciones.participacion.models.Participacion
import org.wolve.geofilm.producciones.participacion.models.RolParticipacion
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.profesional.model.Profesional
import java.util.*

@Repository
@CacheConfig(cacheNames = ["participaciones"])
interface ParticipacionRepository : JpaRepository<Participacion, String> {

    override fun findById(id: String): Optional<Participacion>

    override fun deleteById(id: String)

    @Query("SELECT p FROM Participacion p WHERE p.profesional.id = :profesionalId")
    fun findByProfesionalId(@Param("profesionalId") profesionalId: String): List<Participacion>

    @Query("SELECT p FROM Participacion p WHERE p.produccion.id = :produccionId")
    fun findByProduccionId(@Param("produccionId") produccionId: String): List<Participacion>

    @Query("SELECT p FROM Participacion p WHERE p.produccion = :produccion AND p.profesional = :profesional AND p.rol = :rol")
    fun findByProduccionAndProfesionalAndRol(
        @Param("produccion") produccion: Produccion,
        @Param("profesional") profesional: Profesional,
        @Param("rol") rol: RolParticipacion
    ): Participacion?

    @Query("SELECT p FROM Participacion p WHERE p.produccion = :produccion AND p.profesional = :profesional AND p.rol = :rol AND p.id != :id")
    fun findByProduccionAndProfesionalAndRolAndIdNot(
        @Param("produccion") produccion: Produccion,
        @Param("profesional") profesional: Profesional,
        @Param("rol") rol: RolParticipacion,
        @Param("id") id: String
    ): Participacion?
}