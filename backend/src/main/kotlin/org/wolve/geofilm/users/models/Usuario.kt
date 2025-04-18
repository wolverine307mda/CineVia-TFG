package org.wolve.geofilm.users.models

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import java.util.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp

@Entity
@Table(name = "usuarios")
data class Usuario(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true)
    private val username: String = "",

    @Column(nullable = false)
    val nombre: String = "",

    @Column(nullable = true)
    val apellido: String = "",

    @Column(nullable = false, unique = true)
    val email: String = "",

    @Column(nullable = true)
    val telefono: String = "",

    @Column(nullable = false)
    private val password: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val rol: RolUsuario = RolUsuario.USUARIO,

    @Column(nullable = true)
    val fechaNacimiento: LocalDateTime? = null,

    @Column(nullable = true)
    val avatar: String? = null,

    @Column(nullable = true)
    val idDelete: UUID? = null,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(GrantedAuthority { rol.name })

    override fun getPassword(): String = password
    override fun getUsername(): String = username
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}

enum class RolUsuario {
    USUARIO, ADMINISTRADOR
}
