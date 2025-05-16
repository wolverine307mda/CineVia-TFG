package org.wolve.geofilm.rodajes.exception

class RodajeNotFoundException(id: String) : RuntimeException("Rodaje con id $id no encontrado")