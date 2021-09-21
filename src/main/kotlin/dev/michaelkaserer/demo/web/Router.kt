package dev.michaelkaserer.demo.web

import dev.michaelkaserer.demo.user.rest.UserHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.awaitBodyOrNull
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.reactive.function.server.queryParamOrNull
import org.springframework.web.server.ResponseStatusException

@Configuration
class Router(private val userHandler: UserHandler) {

    @Bean
    fun routes() = coRouter {
        (accept(MediaType.APPLICATION_JSON) and "/api/v1").nest {
            "/user".nest {
                GET("") {
                    userHandler.getCurrent()
                }
                (POST("") and contentType(MediaType.APPLICATION_JSON)) {
                    userHandler.create(it.requiredBody())
                }
            }
        }
    }

    private suspend inline fun <reified T : Any> ServerRequest.requiredBody(): T = awaitBodyOrNull()
        ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "missing required body ")

    private fun ServerRequest.optionalPathVariable(name: String) = pathVariables()[name]

    private fun ServerRequest.optionalQueryParameter(name: String) = queryParamOrNull(name)

    private fun ServerRequest.requiredQueryParameter(name: String) = queryParamOrNull(name)
        ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "missing required query parameter '$name'"
        )

    private fun ServerRequest.requiredPathVariable(name: String) = optionalPathVariable(name)
        ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "missing required '$name' path variable"
        )
}