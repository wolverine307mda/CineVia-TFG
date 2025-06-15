package org.wolve.geofilm.producciones.produccion.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.wolve.geofilm.producciones.produccion.models.Categoria
import org.wolve.geofilm.producciones.produccion.models.ClasificacionEdad
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.produccion.models.TipoProduccion
import java.util.*

@Repository
interface ProduccionRepository : JpaRepository<Produccion, String>, JpaSpecificationExecutor<Produccion> {
    fun findByTituloContainingIgnoreCase(titulo: String, pageable: Pageable): Page<Produccion>
    fun findByTipo(tipo: TipoProduccion, pageable: Pageable): Page<Produccion>
    fun findByClasificacionEdad(clasificacionEdad: ClasificacionEdad, pageable: Pageable): Page<Produccion>
    fun findByCategoriasIn(categorias: Set<Categoria>, pageable: Pageable): Page<Produccion>
    @Query("SELECT COUNT(p) FROM Participacion p WHERE p.produccion.id = :produccionId")
    fun countParticipacionesById(@Param("produccionId") produccionId: String): Long
    @Query("""
        SELECT DISTINCT p FROM Produccion p
        LEFT JOIN FETCH p.saga
        LEFT JOIN FETCH p.participaciones part
        LEFT JOIN FETCH part.profesional
        LEFT JOIN FETCH p.rodajes rod
        LEFT JOIN FETCH rod.ubicacion
        WHERE p.id = :id
    """)
    fun findCompletaById(@Param("id") id: String): Produccion?
    fun findAllBySagaId(sagaId: String): List<Produccion>
}