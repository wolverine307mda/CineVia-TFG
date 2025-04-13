package org.wolve.geofilm.producciones.saga.model

import jakarta.persistence.*
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.utils.GuidGenerator
import java.util.*

@Entity
@Table(name = "sagas")
data class Saga(
    @Id
    @Column(nullable = false, unique = true)
    val id: String = GuidGenerator().generarId(),

    @Column(nullable = false, unique = true)
    val nombre: String,

    @Column(length = 1000)
    val descripcion: String? = null,

    @Column(nullable = false)
    val isAcabada: Boolean = false,

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    val fechaInicio: Date,

    @Temporal(TemporalType.DATE)
    @Column(nullable = true)
    val fechaFin: Date? = null,

    @Column(nullable = true)
    val imagen: String? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "saga_id")
    val producciones: MutableList<Produccion> = mutableListOf()
) {
    constructor() : this(
        id = GuidGenerator().generarId(),
        nombre = "",
        fechaInicio = Date()
    )
}