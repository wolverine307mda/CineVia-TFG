package org.wolve.geofilm.producciones.saga.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.saga.model.Saga
import java.util.*

interface SagaRepository : JpaRepository<Saga, String> {
    fun existsByNombre(nombre: String): Boolean
    fun findByNombreContainingIgnoreCase(nombre: String, pageable: Pageable): Page<Saga>

    @Query("SELECT s FROM Saga s WHERE s.isAcabada = :isAcabada")
    fun findByEstado(@Param("isAcabada") isAcabada: Boolean, pageable: Pageable): Page<Saga>

    @Query("SELECT s FROM Saga s WHERE s.fechaInicio BETWEEN :fechaInicio AND :fechaFin")
    fun findByRangoFechas(
        @Param("fechaInicio") fechaInicio: Date,
        @Param("fechaFin") fechaFin: Date,
        pageable: Pageable
    ): Page<Saga>

    fun findByNombre(nombre: String): Saga?

    @Query("SELECT p FROM Saga s JOIN s.producciones p WHERE s.id = :sagaId")
    fun findProduccionesBySagaId(@Param("sagaId") sagaId: String, pageable: Pageable): Page<Produccion>
}