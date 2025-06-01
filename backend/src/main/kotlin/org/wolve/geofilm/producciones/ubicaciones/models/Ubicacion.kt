package org.wolve.geofilm.producciones.ubicaciones.models

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.wolve.geofilm.utils.generators.GuidGenerator
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.rodajes.models.Rodaje
import java.time.LocalDateTime

@Entity
@Table(name = "ubicaciones")
data class Ubicacion(
    @Id
    @Column(nullable = false, unique = true, columnDefinition = "varchar(255)")
    val id: String = GuidGenerator().generarId(),

    @Column(nullable = true)
    var nombre: String = "",

    @Column(nullable = false)
    var latitud: Double = 0.0,

    @Column(nullable = false)
    var longitud: Double = 0.0,

    @OneToMany(mappedBy = "ubicacion", cascade = [CascadeType.ALL], orphanRemoval = true)
    val rodajes: MutableList<org.wolve.geofilm.producciones.rodajes.models.Rodaje> = mutableListOf(),

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = LocalDateTime.now()
) {
    constructor() : this(GuidGenerator().generarId(), "", 0.0, 0.0)
}