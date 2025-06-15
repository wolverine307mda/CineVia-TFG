package org.wolve.geofilm.producciones.saga.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class SagaNotFoundException(message: String) : RuntimeException(message)