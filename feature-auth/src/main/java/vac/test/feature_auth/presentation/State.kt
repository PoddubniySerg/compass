package vac.test.feature_auth.presentation

internal sealed class State {

    object Loading : State()

    object Complete : State()

    data class Error(val message: String?) : State()
}
