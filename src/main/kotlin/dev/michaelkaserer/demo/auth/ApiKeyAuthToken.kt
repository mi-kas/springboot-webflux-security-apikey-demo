package dev.michaelkaserer.demo.auth

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

/**
 * Represents an authentication token within the example application.
 */
class ApiKeyAuthToken(private val apiKey: String, private val principal: UserDetails) :
    Authentication {
    private var authenticated = false

    override fun getAuthorities(): Collection<GrantedAuthority> = principal.authorities

    override fun getCredentials() = apiKey

    override fun getDetails() = principal

    override fun getPrincipal() = principal

    override fun isAuthenticated() = authenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated = isAuthenticated
    }

    override fun getName(): String = principal.username
}