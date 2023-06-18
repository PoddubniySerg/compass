package vac.test.core_api

import kotlinx.coroutines.flow.Flow

interface UserRepository {

    val tokenFlow: Flow<Token>

    suspend fun login(login: String, password: String)
}