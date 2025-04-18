package org.wolve.geofilm.producciones.produccion.repository

import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.wolve.geofilm.producciones.produccion.models.Produccion

@Repository
interface ProduccionRepository : JpaRepository<Produccion, String> {
    fun findByTitulo(titulo: String): List<Produccion>
    fun findByTipo(tipo: String): List<Produccion>
    fun findByClasificacionEdad(clasificacionEdad: Int): List<Produccion>
    @Query("SELECT COUNT(p) FROM Participacion p WHERE p.produccion.id = :produccionId")
    fun countParticipacionesById(@Param("produccionId") produccionId: String): Long

}