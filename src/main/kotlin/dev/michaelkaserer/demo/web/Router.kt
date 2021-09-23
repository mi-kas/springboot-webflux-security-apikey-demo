package dev.michaelkaserer.demo.web

import dev.michaelkaserer.demo.user.rest.UserHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.*
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
            GET("/nonsecure-route") {
                ServerResponse.ok().buildAndAwait()
            }
            GET("/admin-only-route") {
                ServerResponse.ok().buildAndAwait()
            }
            GET("/secure-route") {
                ServerResponse.ok().buildAndAwait()
            }
        }
    }

    private suspend inline fun <reified T : Any> ServerRequest.requiredBody(): T = awaitBodyOrNull()
        ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "missing required body ")
}