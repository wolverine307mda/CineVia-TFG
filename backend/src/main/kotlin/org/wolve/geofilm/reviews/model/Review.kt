package org.wolve.geofilm.reviews.model

import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.wolve.geofilm.users.models.Usuario
import org.wolve.geofilm.utils.generators.GuidGenerator
import java.time.LocalDateTime
import jakarta.persistence.*

@Entity
@Table(name = "reviews")
data class Review(
    @Id
    @Column(nullable = false, unique = true, columnDefinition = "varchar(255)")
    val id: String = GuidGenerator().generarId(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    val usuario: Usuario,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produccion_id", nullable = false)
    val produccion: Produccion,

    @Column(nullable = false)
    val puntuacion: Int,

    @Column(nullable = true, columnDefinition = "TEXT")
    val comentario: String? = null,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = LocalDateTime.now()
) {
    constructor() : this(
        id = GuidGenerator().generarId(),
        usuario = Usuario(),
        produccion = Produccion(),
        puntuacion = 1,
        comentario = null,
    )
}
