package dev.michaelkaserer.demo.auth

import dev.michaelkaserer.demo.ApplicationProps
import dev.michaelkaserer.demo.user.domain.UserService
import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * Converter that gets the API key from the incoming headers and converts it to an {@link Authentication}
 * that can be checked by the {@link KeyAuthenticationManager}.
 */
@Component
class ApiKeyAuthConverter(
    private val userService: UserService,
    private val applicationProps: ApplicationProps
) :
    ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> = mono {
        val apiKey =
            exchange?.request?.headers?.getFirst(applicationProps.auth.headerName)
                ?: return@mono null

        val user = userService.getByApiKey(apiKey) ?: return@mono null

        ApiKeyAuthToken(apiKey = apiKey, principal = user)
    }
}