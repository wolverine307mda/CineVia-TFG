package org.wolve.geofilm.producciones.participacion.models

import jakarta.persistence.*
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.profesional.model.Profesional
import org.wolve.geofilm.utils.GuidGenerator

@Entity
@Table(name = "participaciones")
data class Participacion(
    @Id
    @Column(nullable = false, unique = true, columnDefinition = "varchar(255)")
    val id: String = GuidGenerator().generarId(),

    @ManyToOne
    @JoinColumn(name = "produccion_id", nullable = false, columnDefinition = "varchar(255)")
    val produccion: Produccion,

    @ManyToOne
    @JoinColumn(name = "profesional_id", nullable = false, columnDefinition = "varchar(255)")
    val profesional: Profesional,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val rol: RolParticipacion,

    @Column(nullable = true)
    val papel: String? = null
) {
    constructor() : this(
        id = GuidGenerator().generarId(),
        produccion = Produccion(),
        profesional = Profesional(),
        rol = RolParticipacion.ACTOR,
        papel = null
    )
}