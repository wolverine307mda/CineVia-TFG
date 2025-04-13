package org.wolve.geofilm.producciones.produccion.models

import jakarta.persistence.*
import org.wolve.geofilm.producciones.participacion.models.Participacion
import org.wolve.geofilm.producciones.saga.model.Saga
import org.wolve.geofilm.ubicaciones.models.Ubicacion
import org.wolve.geofilm.utils.GuidGenerator
import java.util.*

@Entity
@Table(name = "producciones")
data class Produccion(
    @Id
    @Column(nullable = false, unique = true, columnDefinition = "varchar(255)")
    val id: String = GuidGenerator().generarId(),

    @Column(nullable = false, unique = true)
    var titulo: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var tipo: TipoProduccion = TipoProduccion.PELICULA,  // Cambiado a var

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    var estreno: Date = Date(),  // Cambiado a var

    @Column(nullable = false)
    var duracion: Int = 0,  // Cambiado a var

    @Column(nullable = false, length = 1000)
    var sinopsis: String = "",  // Cambiado a var

    @OneToMany(mappedBy = "produccion", cascade = [CascadeType.ALL], orphanRemoval = true)
    val participaciones: MutableList<Participacion> = mutableListOf(),  // MutableList explícito

    @Column(nullable = true)
    var imagen: String? = null,  // Cambiado a var

    @Column(nullable = true, columnDefinition = "TEXT")
    var informacion: String? = null,  // Cambiado a var

    @ElementCollection(targetClass = Categoria::class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
        name = "produccion_categorias",
        joinColumns = [JoinColumn(name = "produccion_id")]
    )
    @Column(name = "categoria", nullable = false)
    val categorias: MutableSet<Categoria> = mutableSetOf(),  // MutableSet explícito

    @Column(nullable = false)
    var clasificacionEdad: Int = 0,  // Cambiado a var

    @OneToMany(mappedBy = "produccion", cascade = [CascadeType.ALL], orphanRemoval = true)
    val ubicaciones: MutableList<Ubicacion> = mutableListOf(),  // MutableList explícito

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saga_id")
    var saga: Saga? = null  // Relación con Saga añadida
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
        clasificacionEdad = 0
    )
}