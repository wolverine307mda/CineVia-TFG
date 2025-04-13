package org.wolve.geofilm.producciones.profesional.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.wolve.geofilm.producciones.profesional.model.Profesional
import java.util.*

interface ProfesionalRepository : JpaRepository<Profesional, String> {

    @Query("SELECT p FROM Profesional p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    fun findByNombreContainingIgnoreCase(
        @Param("nombre") nombre: String,
        pageable: Pageable
    ): Page<Profesional>

    fun findAllByOrderByNombreAsc(pageable: Pageable): Page<Profesional>

    @Query("SELECT p FROM Profesional p WHERE p.fechaNacimiento BETWEEN :fechaInicio AND :fechaFin")
    fun findByFechaNacimientoBetween(
        @Param("fechaInicio") fechaInicio: Date,
        @Param("fechaFin") fechaFin: Date,
        pageable: Pageable
    ): Page<Profesional>
}