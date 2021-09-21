package dev.michaelkaserer.demo.auth

import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

/**
 * Checks the incoming {@link Authentication} and verifies that the request should be allowed.
 */
@Component
class ApiKeyAuthManager : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> = mono {
        if (authentication.credentials != null) {
            authentication.isAuthenticated = true
        }
        authentication
    }
}