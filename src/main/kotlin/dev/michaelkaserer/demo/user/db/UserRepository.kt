package dev.michaelkaserer.demo.user.db

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: CoroutineCrudRepository<UserEntity, String> {
    suspend fun existsByEmail(email: String): Boolean
    suspend fun findByApiKey(apiKey: String): UserEntity?
}