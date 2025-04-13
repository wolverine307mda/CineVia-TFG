package org.wolve.geofilm.producciones.profesional.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Excepción lanzada cuando no se encuentra un profesional en el sistema.
 *
 * @property message Mensaje descriptivo del error
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class ProfesionalNotFoundException(message: String) : RuntimeException(message) {

    /**
     * Constructor secundario que permite formatear el mensaje con parámetros.
     *
     * @param message Mensaje base
     * @param params Parámetros para formatear el mensaje
     */
    constructor(message: String, vararg params: Any) : this(String.format(message, *params))
}