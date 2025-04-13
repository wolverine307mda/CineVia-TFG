package org.wolve.geofilm.favoritos.models

import jakarta.persistence.*
import org.wolve.geofilm.users.models.Usuario
import java.util.UUID

@Entity
@Table(name = "favoritos")
data class Favorito(
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
    val produccionId: UUID?,

    @Column(nullable = true)
    val ubicacionId: UUID?,

    @Column(nullable = true)
    val actorId: UUID?
)


