package org.wolve.geofilm.producciones.rodajes.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.wolve.geofilm.producciones.rodajes.models.Rodaje

interface RodajeRepository : JpaRepository<org.wolve.geofilm.producciones.rodajes.models.Rodaje, String>{
    fun findAllByProduccionId(produccionId: String, pageable: Pageable): Page<org.wolve.geofilm.producciones.rodajes.models.Rodaje>
}