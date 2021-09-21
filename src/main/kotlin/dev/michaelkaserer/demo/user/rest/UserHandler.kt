package dev.michaelkaserer.demo.user.rest

import dev.michaelkaserer.demo.user.domain.CreateUserOutcome
import dev.michaelkaserer.demo.user.domain.UserService
import dev.michaelkaserer.demo.web.awaitAsOkJsonBody
import dev.michaelkaserer.demo.web.awaitAuthenticatedUser
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.buildAndAwait


@Controller
class UserHandler(private val userService: UserService) {
    suspend fun create(user: CreateUserRequest): ServerResponse =
        when (userService.create(user)) {
            CreateUserOutcome.AlreadyExisting -> ServerResponse.status(HttpStatus.CREATED)
                .buildAndAwait()
            is CreateUserOutcome.Success -> ServerResponse.status(HttpStatus.CREATED)
                .buildAndAwait()
        }

    suspend fun getCurrent(): ServerResponse = awaitAuthenticatedUser().toResponse().awaitAsOkJsonBody()
}