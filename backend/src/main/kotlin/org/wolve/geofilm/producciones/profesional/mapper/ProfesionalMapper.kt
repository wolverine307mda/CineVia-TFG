package org.wolve.geofilm.producciones.profesional.mapper

import org.springframework.stereotype.Component
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalRequest
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalResponse
import org.wolve.geofilm.producciones.profesional.model.Profesional
import org.wolve.geofilm.utils.pagination.PaginationUtils
import java.text.SimpleDateFormat

@Component
class ProfesionalMapper {
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd")

    fun toResponse(profesional: Profesional): ProfesionalResponse = ProfesionalResponse(
        id = profesional.id,
        nombre = profesional.nombre,
        foto = profesional.foto,
        fechaNacimiento = profesional.fechaNacimiento,
        fechaInicio = profesional.fechaInicio,
        lugarNacimiento = profesional.lugarNacimiento,
        biografia = profesional.biografia,
        participacionesCount = profesional.participaciones.size
    )

    fun toEntity(request: ProfesionalRequest): Profesional = Profesional(
        nombre = request.nombre,
        foto = request.foto,
        fechaNacimiento = dateFormatter.parse(request.fechaNacimiento),
        fechaInicio = dateFormatter.parse(request.fechaInicio),
        lugarNacimiento = request.lugarNacimiento,
        biografia = request.biografia
    )

    fun toPaginatedResponse(page: org.springframework.data.domain.Page<Profesional>):
            PaginationUtils.PaginatedResponse<ProfesionalResponse> {
        return PaginationUtils.toPaginatedResponse(page.map { toResponse(it) })
    }
}