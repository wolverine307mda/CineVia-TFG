package org.wolve.geofilm.producciones.participacion.models

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.profesional.model.Profesional
import org.wolve.geofilm.utils.generators.GuidGenerator
import java.time.LocalDateTime

@Entity
@Table(name = "participaciones")
data class Participacion(
    @Id
    @Column(nullable = false, unique = true, columnDefinition = "varchar(255)")
    val id: String = GuidGenerator().generarId(),

    @ManyToOne
    @JoinColumn(name = "produccion_id", nullable = false, columnDefinition = "varchar(255)")
    var produccion: Produccion,

    @ManyToOne
    @JoinColumn(name = "profesional_id", nullable = false, columnDefinition = "varchar(255)")
    var profesional: Profesional,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var rol: RolParticipacion,

    @Column(nullable = true)
    var papel: String? = null,

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(
        id = GuidGenerator().generarId(),
        produccion = Produccion(),
        profesional = Profesional(),
        rol = RolParticipacion.ACTOR,
        papel = null
    )
}
