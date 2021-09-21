package dev.michaelkaserer.demo.web

import dev.michaelkaserer.demo.user.db.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.reactive.function.server.*
import org.springframework.web.server.ResponseStatusException

suspend inline fun Response.awaitAsOkJsonBody() = ServerResponse.ok().json().bodyValueAndAwait(this)
suspend inline fun Collection<Response>.awaitAsOkJsonBody() =
    ServerResponse.ok().json().bodyValueAndAwait(this)

suspend inline fun Flow<Response>.awaitAsOkJsonBody() =
    ServerResponse.ok().json().bodyAndAwait(this)

suspend inline fun awaitOk() = ServerResponse.ok().buildAndAwait()

suspend inline fun awaitAuthenticatedUser() =
    ReactiveSecurityContextHolder.getContext().map { it.authentication.principal }
        .cast(UserEntity::class.java).awaitFirstOrNull() ?: throw ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "Principal missing for authenticated call"
    )