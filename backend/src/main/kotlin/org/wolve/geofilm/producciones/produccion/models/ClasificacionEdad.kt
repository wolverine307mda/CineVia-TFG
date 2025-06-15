package org.wolve.geofilm.producciones.produccion.models

enum class ClasificacionEdad(val valorNumerico: Int) {
    TODOS_LOS_PUBLICOS(0),
    MAYORES_7(7),
    MAYORES_12(12),
    MAYORES_16(16),
    MAYORES_18(18);

    companion object {
        fun fromValorNumerico(valor: Int): ClasificacionEdad? {
            return values().firstOrNull { it.valorNumerico == valor }
        }
    }
}