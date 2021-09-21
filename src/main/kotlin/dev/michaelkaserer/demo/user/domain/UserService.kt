package dev.michaelkaserer.demo.user.domain

import dev.michaelkaserer.demo.user.db.UserEntity
import dev.michaelkaserer.demo.user.db.UserRepository
import dev.michaelkaserer.demo.user.rest.CreateUserRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository
) {
    suspend fun getByApiKey(apiKey: String) = userRepository.findByApiKey(apiKey)

    suspend fun create(user: CreateUserRequest): CreateUserOutcome {
        if (userRepository.existsByEmail(user.email)) return CreateUserOutcome.AlreadyExisting
        val createdUser = userRepository.save(
            UserEntity(
                email = user.email,
                apiKey = generateApiKey(),
            )
        )
        // TODO: Send email with api key to user
        return CreateUserOutcome.Success(createdUser)
    }

    companion object {
        fun generateApiKey() = UUID.randomUUID().toString().replace("-", "")
    }
}