package org.wolve.geofilm.producciones.produccion.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ProduccionNotFoundException(id: String) :
    RuntimeException("Producción no encontrada con ID: $id")