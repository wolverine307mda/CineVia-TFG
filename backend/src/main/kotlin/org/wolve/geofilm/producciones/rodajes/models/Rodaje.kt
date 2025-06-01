package org.wolve.geofilm.rodajes.models

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.ubicaciones.models.Ubicacion
import org.wolve.geofilm.utils.generators.GuidGenerator
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "rodajes")
data class Rodaje(
    @Id
    @Column(nullable = false, unique = true, columnDefinition = "varchar(255)")
    val id: String = GuidGenerator().generarId(),

    @ManyToOne
    @JoinColumn(name = "produccion_id", nullable = false)
    val produccion: Produccion,

    @ManyToOne
    @JoinColumn(name = "ubicacion_id", nullable = false)
    val ubicacion: Ubicacion,

    @Column(nullable = true, length = 1000)
    val notas: String? = null,

    @ElementCollection
    @CollectionTable(
        name = "rodaje_imagenes",
        joinColumns = [JoinColumn(name = "rodaje_id")]
    )
    @Column(name = "imagen_url", nullable = false)
    val imagenes: MutableList<String> = mutableListOf(),

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = LocalDateTime.now()
) {
    constructor() : this(
        id = GuidGenerator().generarId(),
        produccion = Produccion(),
        ubicacion = Ubicacion(),
        notas = null,
        imagenes = mutableListOf()
    )
}