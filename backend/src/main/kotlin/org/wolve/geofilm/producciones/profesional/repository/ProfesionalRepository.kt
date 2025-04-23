package org.wolve.geofilm.producciones.profesional.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.wolve.geofilm.producciones.profesional.model.Profesional
import java.util.*

interface IProfesionalRepository : JpaRepository<Profesional, String> {
    fun findByNombreContainingIgnoreCase(nombre: String, pageable: Pageable): Page<Profesional>
}