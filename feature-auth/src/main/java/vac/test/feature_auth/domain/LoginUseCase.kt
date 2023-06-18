package vac.test.feature_auth.domain

import vac.test.core_api.UserRepository

internal class LoginUseCase(private val userRepository: UserRepository) {

    suspend fun execute(login: String, password: String) {
        userRepository.login(login, password)
    }
}