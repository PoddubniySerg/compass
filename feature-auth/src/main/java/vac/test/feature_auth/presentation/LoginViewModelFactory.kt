package vac.test.feature_auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import vac.test.core_api.AuthComponent
import vac.test.feature_auth.domain.LoginUseCase
import vac.test.feature_auth.domain.SaveTokenUseCase

internal class LoginViewModelFactory(
    private val authComponent: AuthComponent,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = LoginViewModel(
            authComponent.getUserRepository(),
            LoginUseCase(authComponent.getUserRepository()),
            SaveTokenUseCase(authComponent.getTokenRepository())
        )
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return viewModel as T
        } else {
            throw IllegalArgumentException("Unknown class name")
        }
    }
}