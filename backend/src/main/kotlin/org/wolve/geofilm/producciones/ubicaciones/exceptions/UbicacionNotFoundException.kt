package org.wolve.geofilm.ubicaciones.exceptions

class UbicacionNotFoundException(id: String) : RuntimeException("Ubicación con ID $id no encontrada")