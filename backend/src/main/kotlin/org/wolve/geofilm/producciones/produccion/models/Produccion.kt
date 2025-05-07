package org.wolve.geofilm.producciones.produccion.models

import org.wolve.geofilm.producciones.participacion.models.Participacion
import org.wolve.geofilm.producciones.saga.model.Saga
import org.wolve.geofilm.ubicaciones.models.Ubicacion
import org.wolve.geofilm.utils.generators.GuidGenerator
import jakarta.persistence.*
import org.wolve.geofilm.rodajes.models.Rodaje
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

    @OneToMany(mappedBy = "produccion", cascade = [CascadeType.ALL], orphanRemoval = true)
    val participaciones: MutableList<Participacion> = mutableListOf(),

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
    val rodajes: MutableList<Rodaje> = mutableListOf(),

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
}