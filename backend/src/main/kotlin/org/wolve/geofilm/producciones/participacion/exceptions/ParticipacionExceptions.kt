package org.wolve.geofilm.producciones.participacion.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ParticipacionNotFoundException(id: String) :
    RuntimeException("Participación con ID $id no encontrada")

@ResponseStatus(HttpStatus.NOT_FOUND)
class RelacionNotFoundException(message: String) : RuntimeException(message)
