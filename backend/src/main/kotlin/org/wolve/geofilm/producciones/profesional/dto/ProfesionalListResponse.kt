package org.wolve.geofilm.producciones.profesional.dto

data class ProfesionalListResponse(
    val profesionales: List<ProfesionalSimpleResponse>,
    val totalElements: Long,
    val totalPages: Int,
    val currentPage: Int,
    val size: Int
)

data class ProfesionalSimpleResponse(
    val id: String,
    val nombre: String,
    val foto: String?
)