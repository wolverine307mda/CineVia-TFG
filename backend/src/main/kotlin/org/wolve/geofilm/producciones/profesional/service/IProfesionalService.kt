package org.wolve.geofilm.producciones.profesional.service

import org.springframework.data.domain.Pageable
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalListResponse
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalRequest
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalResponse
import org.wolve.geofilm.producciones.profesional.model.Profesional
import java.util.*

interface IProfesionalService {
    // Operaciones CRUD básicas
    fun findById(id: String): ProfesionalResponse
    fun create(profesionalRequest: ProfesionalRequest): ProfesionalResponse
    fun update(id: String, profesionalRequest: ProfesionalRequest): ProfesionalResponse
    fun delete(id: String)

    // Métodos de listado con paginación
    fun findAll(pageable: Pageable): ProfesionalListResponse
    fun searchByNombre(nombre: String, pageable: Pageable): ProfesionalListResponse
    fun findByFechaNacimientoBetween(
        fechaInicio: Date,
        fechaFin: Date,
        pageable: Pageable
    ): ProfesionalListResponse

    fun findEntityById(id: String): Profesional
    fun existsById(id: String): Boolean
}