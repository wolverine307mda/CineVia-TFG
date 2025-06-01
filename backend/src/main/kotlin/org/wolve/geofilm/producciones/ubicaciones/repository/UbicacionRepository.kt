package org.wolve.geofilm.producciones.ubicaciones.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import org.wolve.geofilm.producciones.ubicaciones.models.Ubicacion

@Repository
interface UbicacionRepository : JpaRepository<Ubicacion, String>, JpaSpecificationExecutor<Ubicacion> {
    fun findByNombreContainingIgnoreCase(nombre: String, pageable: Pageable): Page<Ubicacion>
}