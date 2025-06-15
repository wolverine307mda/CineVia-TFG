package org.wolve.geofilm.producciones.ubicaciones.exceptions

class UbicacionNotFoundException(id: String) : RuntimeException("Ubicación con ID $id no encontrada")