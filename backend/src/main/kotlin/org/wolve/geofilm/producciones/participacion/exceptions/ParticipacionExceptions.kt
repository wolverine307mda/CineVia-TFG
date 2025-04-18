package org.wolve.geofilm.producciones.participacion.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import org.wolve.geofilm.producciones.participacion.models.RolParticipacion

@ResponseStatus(HttpStatus.NOT_FOUND)
class ParticipacionNotFoundException(id: String) :
    RuntimeException("Participación no encontrada con ID: $id")

@ResponseStatus(HttpStatus.BAD_REQUEST)
class ParticipacionAlreadyExistsException(produccionId: String, profesionalId: String, rol: String) :
    RuntimeException("Ya existe una participación para el profesional $profesionalId en la producción $produccionId con el rol $rol")

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidRolParticipacionException(rol: String) :
    RuntimeException("Rol de participación no válido: $rol. Roles válidos: ${RolParticipacion.values().joinToString()}")

@ResponseStatus(HttpStatus.CONFLICT)
class ParticipacionAssociatedProfesionalException(id: String) :
    RuntimeException("No se puede eliminar la participación $id porque está asociada a un profesional")

@ResponseStatus(HttpStatus.CONFLICT)
class ParticipacionAssociatedProduccionException(id: String) :
    RuntimeException("No se puede eliminar la participación $id porque está asociada a una producción")