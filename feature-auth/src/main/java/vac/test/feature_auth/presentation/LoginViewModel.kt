package vac.test.feature_auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import vac.test.core_api.Token
import vac.test.core_api.UserRepository
import vac.test.feature_auth.domain.LoginUseCase
import vac.test.feature_auth.domain.SaveTokenUseCase

internal class LoginViewModel(
    userRepository: UserRepository,
    private val loginUseCase: LoginUseCase,
    private val saveTokenUseCase: SaveTokenUseCase
) : ViewModel() {

    val accessTokenFlow = userRepository.tokenFlow

    private val _stateMutableStateFlow = MutableStateFlow<State>(State.Complete)
    val stateFlow = _stateMutableStateFlow.asStateFlow()

    private val _errorChannel = Channel<State.Error>()
    val errorFlow = _errorChannel.receiveAsFlow()

    fun login(login: String, password: String) {
        viewModelScope.launch {
            try {
                _stateMutableStateFlow.value = State.Loading
                val loginIsValid = isCredentialValid(login, "Недопустимое значение логина")
                val passwordIsValid = isCredentialValid(password, "Недопустимое значение пароля")
                if (loginIsValid && passwordIsValid) {
                    loginUseCase.execute(login, password)
                }
            } catch (ex: Exception) {
                _errorChannel.send(State.Error(ex.message))
            } finally {
                _stateMutableStateFlow.value = State.Complete
            }
        }
    }

    fun saveToken(token: Token) {
        viewModelScope.launch {
            try {
                saveTokenUseCase.execute(token)
            } catch (ex: Exception) {
                _errorChannel.send(State.Error(ex.message))
            }
        }
    }

    private suspend fun isCredentialValid(credential: String, errorMessage: String): Boolean {
        return try {
            val isValid = credential.isNotEmpty()
                    && credential.isNotBlank()
                    && !credential.contains(' ')
            if (!isValid) {
                _errorChannel.send(State.Error(errorMessage))
            }
            isValid
        } catch (ex: Exception) {
            _errorChannel.send(State.Error(errorMessage))
            false
        }
    }
}