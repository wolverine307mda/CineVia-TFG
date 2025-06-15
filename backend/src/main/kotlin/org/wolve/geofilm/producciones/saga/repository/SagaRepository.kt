package org.wolve.geofilm.producciones.saga.repository

import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.wolve.geofilm.producciones.saga.model.Saga
import java.util.*

interface SagaRepository : JpaRepository<Saga, String> {
    @Query("""
        SELECT DISTINCT s FROM Saga s
        LEFT JOIN FETCH s.producciones p
        WHERE s.id = :id
    """)
    override fun findById(@Param("id") id: String): Optional<Saga>
    fun existsByNombre(nombre: String): Boolean
}