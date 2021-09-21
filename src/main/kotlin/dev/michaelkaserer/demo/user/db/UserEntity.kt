package dev.michaelkaserer.demo.user.db

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.Instant

@Document("user")
data class UserEntity(
    @Id
    val id: String? = null,
    val email: String,
    val apiKey: String,
    val role: UserRole = UserRole.USER,
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val updatedAt: Instant = Instant.now(),
    // inherits from UserDetails
    private val isAccountNonExpired: Boolean = true,
    private val isAccountNonLocked: Boolean = true,
    private val isCredentialsNonExpired: Boolean = true,
    private val isEnabled: Boolean = true
) : UserDetails {
    override fun getAuthorities(): Set<GrantedAuthority> =
        setOf(SimpleGrantedAuthority(getAuthority()))
    override fun getPassword(): String = apiKey
    override fun getUsername(): String = email
    override fun isAccountNonExpired(): Boolean = isAccountNonExpired
    override fun isAccountNonLocked(): Boolean = isAccountNonLocked
    override fun isCredentialsNonExpired(): Boolean = isCredentialsNonExpired
    override fun isEnabled(): Boolean = isEnabled
    private fun getAuthority(): String = "ROLE_$role"
}