package org.wolve.geofilm.ubicaciones.models

import jakarta.persistence.*
import org.wolve.geofilm.utils.GuidGenerator
import org.wolve.geofilm.producciones.produccion.models.Produccion

@Entity
@Table(name = "ubicaciones")
data class Ubicacion(
    @Id
    @Column(nullable = false, unique = true, columnDefinition = "varchar(255)")
    val id: String = GuidGenerator().generarId(),

    @Column(nullable = false)
    var nombre: String = "",

    @Column(nullable = false)
    var latitud: Double = 0.0,

    @Column(nullable = false)
    var longitud: Double = 0.0,

    @ManyToOne
    @JoinColumn(name = "produccion_id", columnDefinition = "varchar(255)")
    var produccion: Produccion? = null
) {
    constructor() : this(GuidGenerator().generarId(), "", 0.0, 0.0, null)
}