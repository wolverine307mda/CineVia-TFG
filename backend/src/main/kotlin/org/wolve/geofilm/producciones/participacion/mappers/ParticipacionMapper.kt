package org.wolve.geofilm.producciones.participacion.mappers

import org.springframework.stereotype.Component
import org.wolve.geofilm.producciones.participacion.dto.*
import org.wolve.geofilm.producciones.participacion.models.Participacion
import org.wolve.geofilm.producciones.participacion.models.RolParticipacion
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.profesional.model.Profesional

@Component
class ParticipacionMapper {

    fun toEntity(request: ParticipacionRequest, produccion: Produccion, profesional: Profesional): Participacion {
        return Participacion(
            produccion = produccion,
            profesional = profesional,
            rol = RolParticipacion.valueOf(request.rol.uppercase()),
            papel = request.papel
        )
    }

    fun toResponse(participacion: Participacion): ParticipacionResponse {
        return ParticipacionResponse(
            id = participacion.id,
            produccionId = participacion.produccion.id,
            produccionTitulo = participacion.produccion.titulo,
            profesionalId = participacion.profesional.id,
            profesionalNombre = participacion.profesional.nombre,
            rol = participacion.rol.name,
            papel = participacion.papel,
            createdAt = participacion.createdAt,
            updatedAt = participacion.updatedAt
        )
    }

    fun toProfesionalResponse(participacion: Participacion): ParticipacionProfesionalResponse {
        return ParticipacionProfesionalResponse(
            id = participacion.id,
            profesionalId = participacion.profesional.id,
            profesionalNombre = participacion.profesional.nombre,
            profesionalFoto = participacion.profesional.foto,
            rol = participacion.rol.name,
            papel = participacion.papel
        )
    }

    fun toProduccionResponse(participacion: Participacion): ParticipacionProduccionResponse {
        return ParticipacionProduccionResponse(
            id = participacion.id,
            produccionId = participacion.produccion.id,
            produccionTitulo = participacion.produccion.titulo,
            produccionImagen = participacion.produccion.imagen,
            rol = participacion.rol.name,
            papel = participacion.papel,
            estreno = participacion.produccion.estreno
        )
    }
}