package dev.michaelkaserer.demo.user.rest

import dev.michaelkaserer.demo.user.db.UserEntity
import dev.michaelkaserer.demo.web.Response

data class UserResponse(val id: String, val email: String, val apiKey: String): Response

fun UserEntity.toResponse() = UserResponse(id = id!!, email = email, apiKey = password)
