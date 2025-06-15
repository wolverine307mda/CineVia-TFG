package org.wolve.geofilm.producciones.produccion.models

import jakarta.persistence.*
import org.wolve.geofilm.producciones.participacion.models.Participacion
import org.wolve.geofilm.producciones.saga.model.Saga
import org.wolve.geofilm.utils.generators.GuidGenerator
import java.util.*

@Entity
@Table(name = "producciones")
data class Produccion(
    @Id
    @Column(nullable = false, unique = true, columnDefinition = "varchar(255)")
    val id: String = GuidGenerator().generarId(),

    @Column(nullable = false, unique = true, columnDefinition = "varchar(255)")
    var titulo: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var tipo: TipoProduccion = TipoProduccion.PELICULA,

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    var estreno: Date = Date(),

    @Column(nullable = false)
    var duracion: Int = 0,

    @Column(nullable = false, length = 1000)
    var sinopsis: String = "",

    @Column(nullable = true)
    var imagen: String? = null,

    @Column(nullable = true, columnDefinition = "TEXT")
    var informacion: String? = null,

    @Column(nullable = true)
    var puntuacion: Double = 0.0,

    @ElementCollection(targetClass = Categoria::class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        name = "produccion_categorias",
        joinColumns = [JoinColumn(name = "produccion_id")]
    )
    @Column(name = "categoria", nullable = false)
    val categorias: MutableSet<Categoria> = mutableSetOf(),

    @Column(nullable = false)
    var clasificacionEdad: ClasificacionEdad = ClasificacionEdad.TODOS_LOS_PUBLICOS,

    @OneToMany(mappedBy = "produccion", cascade = [CascadeType.ALL], orphanRemoval = true)
    val participaciones: Set<Participacion> = mutableSetOf(),

    @OneToMany(mappedBy = "produccion", cascade = [CascadeType.ALL], orphanRemoval = true)
    val rodajes: Set<org.wolve.geofilm.producciones.rodajes.models.Rodaje> = mutableSetOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saga_id")
    var saga: Saga? = null
) {
    constructor() : this(
        id = GuidGenerator().generarId(),
        titulo = "",
        tipo = TipoProduccion.PELICULA,
        estreno = Date(),
        duracion = 0,
        sinopsis = "",
        imagen = null,
        informacion = null,
        puntuacion = 0.0,
        clasificacionEdad = ClasificacionEdad.TODOS_LOS_PUBLICOS
    )

    override fun equals(other: Any?) = this === other || (other is Produccion && id == other.id)
    override fun hashCode(): Int = id.hashCode()
}