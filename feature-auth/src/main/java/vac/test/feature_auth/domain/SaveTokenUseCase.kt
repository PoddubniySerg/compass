package vac.test.feature_auth.domain

import vac.test.core_api.Token
import vac.test.core_api.TokenRepository

internal class SaveTokenUseCase(private val tokenRepository: TokenRepository) {

    suspend fun execute(token: Token) {
        tokenRepository.save(token.accessToken, token.refreshToken)
    }
}