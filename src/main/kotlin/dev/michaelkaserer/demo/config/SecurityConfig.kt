package dev.michaelkaserer.demo.config

import dev.michaelkaserer.demo.auth.ApiKeyAuthConverter
import dev.michaelkaserer.demo.auth.ApiKeyAuthManager
import dev.michaelkaserer.demo.user.db.UserRole
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun authenticationWebFilter(
        apiKeyAuthManager: ApiKeyAuthManager,
        apiKeyAuthConverter: ApiKeyAuthConverter
    ): AuthenticationWebFilter = AuthenticationWebFilter(apiKeyAuthManager).also {
        it.setServerAuthenticationConverter(apiKeyAuthConverter)
        it.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance())
    }

    @Bean
    fun accessDeniedHandler() = ServerAccessDeniedHandler { swe, _ ->
        Mono.fromRunnable { swe.response.statusCode = HttpStatus.FORBIDDEN }
    }

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        apiKeyAuthWebFilter: AuthenticationWebFilter,
        accessDeniedHandler: ServerAccessDeniedHandler
    ): SecurityWebFilterChain = http
        .authorizeExchange()
        .matchers(EndpointRequest.toAnyEndpoint()).permitAll()
        .pathMatchers(HttpMethod.POST, "/api/v1/user").permitAll()
        .pathMatchers(HttpMethod.GET, "/api/v1/nonsecure-route").permitAll()
        .pathMatchers(HttpMethod.GET, "/api/v1/admin-only-route").hasRole(UserRole.ADMIN.name)
        .anyExchange().authenticated()
        .and()
        .addFilterAt(apiKeyAuthWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .exceptionHandling { it.accessDeniedHandler(accessDeniedHandler()) }
        .httpBasic { it.disable() }
        .csrf { it.disable() }
        .formLogin { it.disable() }
        .logout { it.disable() }
        .build()
}