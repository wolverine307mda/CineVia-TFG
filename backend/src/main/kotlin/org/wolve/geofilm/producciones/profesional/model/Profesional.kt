package org.wolve.geofilm.producciones.profesional.model

import jakarta.persistence.*
import org.wolve.geofilm.producciones.participacion.models.Participacion
import org.wolve.geofilm.utils.GuidGenerator
import java.util.*

@Entity
@Table(name = "profesionales")
data class Profesional(
    @Id
    @Column(nullable = false, unique = true, columnDefinition = "varchar(255)")
    val id: String = GuidGenerator().generarId(),

    @Column(nullable = false)
    val nombre: String,

    @Column(nullable = true)
    val foto: String? = null,

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    val fechaNacimiento: Date,

    @Temporal(TemporalType.DATE)
    @Column(nullable = false, columnDefinition = "date default CURRENT_DATE")
    val fechaInicio: Date,

    @Column(nullable = false)
    val lugarNacimiento: String,

    @Column(columnDefinition = "TEXT")
    val biografia: String? = null,

    @OneToMany(mappedBy = "profesional", cascade = [CascadeType.ALL], orphanRemoval = true)
    val participaciones: List<Participacion> = mutableListOf()
) {
    constructor() : this(
        id = GuidGenerator().generarId(),
        nombre = "",
        fechaNacimiento = Date(),
        fechaInicio = Date(),
        lugarNacimiento = "",
        biografia = ""
    )
}