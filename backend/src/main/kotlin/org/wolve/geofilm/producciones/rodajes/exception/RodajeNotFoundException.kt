package org.wolve.geofilm.producciones.rodajes.exception

class RodajeNotFoundException(id: String) : RuntimeException("Rodaje con id $id no encontrado")