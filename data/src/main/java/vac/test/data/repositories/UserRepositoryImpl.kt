package vac.test.data.repositories

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import vac.test.core_api.Token
import vac.test.core_api.UserRepository
import vac.test.data.exceptions.AuthException
import vac.test.data.network.NetworkClient

internal class UserRepositoryImpl : UserRepository {

    companion object {
        private const val ERROR_MESSAGE = "Ошибка авторизации"
    }

    private val userApi = NetworkClient().userApi()

    private val _tokenChannel = Channel<Token>()
    override val tokenFlow = _tokenChannel.receiveAsFlow()

    override suspend fun login(login: String, password: String) {
        try {
            val token = userApi.login().body() ?: throw AuthException(ERROR_MESSAGE)
            _tokenChannel.send(token)
        } catch (ex: Exception) {
            throw AuthException(ERROR_MESSAGE)
        }
    }
}