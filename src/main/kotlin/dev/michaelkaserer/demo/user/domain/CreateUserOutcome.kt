package dev.michaelkaserer.demo.user.domain

import dev.michaelkaserer.demo.user.db.UserEntity

sealed class CreateUserOutcome {
    object AlreadyExisting: CreateUserOutcome()
    data class Success(val user: UserEntity): CreateUserOutcome()
}