package vac.test.core_api

import kotlinx.coroutines.flow.Flow


interface TokenRepository {

    val accessTokenFlow: Flow<String>

    suspend fun save(accessToken: String, refreshToken: String)
}