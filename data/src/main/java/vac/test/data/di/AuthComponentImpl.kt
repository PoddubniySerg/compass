package vac.test.data.di

import vac.test.core_api.AuthComponent
import vac.test.core_api.TokenRepository
import vac.test.core_api.UserRepository
import vac.test.data.repositories.TokenRepositoryImpl
import vac.test.data.repositories.UserRepositoryImpl

internal class AuthComponentImpl : AuthComponent {

    private val tokenRepository: TokenRepository = TokenRepositoryImpl()
    private val userRepository: UserRepository = UserRepositoryImpl()

    override fun getTokenRepository(): TokenRepository {
        return tokenRepository
    }

    override fun getUserRepository(): UserRepository {
        return userRepository
    }
}