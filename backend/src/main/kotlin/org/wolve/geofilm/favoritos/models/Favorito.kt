package org.wolve.geofilm.favoritos.models

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.wolve.geofilm.users.models.Usuario
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "favoritos")
class Favorito(

    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID(),

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    val usuario: Usuario,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipo: TipoFavorito,

    @Column(nullable = true)
    val produccionId: UUID? = null,

    @Column(nullable = true)
    val ubicacionId: UUID? = null,

    @Column(nullable = true)
    val actorId: UUID? = null,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null

) {
    constructor() : this(
        id = UUID.randomUUID(),
        usuario = Usuario(),
        tipo = TipoFavorito.ACTOR,
        produccionId = null,
        ubicacionId = null,
        actorId = null,
        createdAt = null,
        updatedAt = null
    )
}
