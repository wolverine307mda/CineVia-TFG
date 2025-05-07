package org.wolve.geofilm.producciones.saga.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.wolve.geofilm.producciones.saga.model.Saga

interface SagaRepository : JpaRepository<Saga, String> {
    fun existsByNombre(nombre: String): Boolean
}