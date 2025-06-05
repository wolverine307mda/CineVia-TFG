package org.wolve.geofilm.users.models

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.wolve.geofilm.utils.generators.GuidGenerator
import java.time.LocalDate

@Entity
@Table(name = "usuarios")
data class Usuario(
    @Id
    @Column(nullable = false, unique = true, columnDefinition = "varchar(255)")
    val id: String = GuidGenerator().generarId(),

    @Column(nullable = false, unique = true)
    private val username: String = "",

    @Column(nullable = false)
    var nombre: String = "",

    @Column(nullable = true)
    var apellido: String = "",

    @Column(nullable = false, unique = true)
    val email: String = "",

    @Column(nullable = true)
    var telefono: String = "",

    @Column(nullable = false)
    @get:JvmName("getPasswordField")
    var password: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var rol: RolUsuario = RolUsuario.USUARIO,

    @Column(nullable = true)
    var fechaNacimiento: LocalDate? = null,

    @Column(nullable = true, length = 1024)
    var avatar: String? = null,

    @Column(nullable = true)
    var isDelete: Boolean = false,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null,

    @Column(name = "reset_pin")
    var resetPin: String? = null,

    @Column(name = "reset_pin_expiration")
    var resetPinExpiration: LocalDateTime? = null

) : UserDetails {
    override fun getUsername(): String = email
    override fun getPassword(): String = password
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_${rol.name}"))
    }
    fun getNombreUsuario(): String = username
    fun generateResetPin(): String {
        this.resetPin = (100000..999999).random().toString()
        this.resetPinExpiration = LocalDateTime.now().plusMinutes(15)
        return this.resetPin!!
    }

    fun isResetPinValid(pin: String): Boolean {
        return this.resetPin == pin &&
                this.resetPinExpiration != null &&
                this.resetPinExpiration!!.isAfter(LocalDateTime.now())
    }

    fun clearResetPin() {
        this.resetPin = null
        this.resetPinExpiration = null
    }
}

enum class RolUsuario {
    USUARIO, ADMINISTRADOR
}
