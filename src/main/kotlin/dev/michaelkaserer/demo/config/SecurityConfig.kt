package dev.michaelkaserer.demo.config

import dev.michaelkaserer.demo.auth.ApiKeyAuthConverter
import dev.michaelkaserer.demo.auth.ApiKeyAuthManager
import dev.michaelkaserer.demo.user.db.UserRole
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository

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
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        apiKeyAuthWebFilter: AuthenticationWebFilter
    ): SecurityWebFilterChain = http
        .authorizeExchange()
        .pathMatchers("/actuator/**").permitAll()
        .pathMatchers(HttpMethod.POST, "/api/v1/user").hasRole(UserRole.ADMIN.name)
        .pathMatchers(HttpMethod.GET, "/api/v1/user").hasRole(UserRole.ADMIN.name)
        .anyExchange().authenticated()
        .and()
        .addFilterAt(apiKeyAuthWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        .httpBasic { it.disable() }
        .csrf { it.disable() }
        .formLogin { it.disable() }
        .logout { it.disable() }
        .build()
}