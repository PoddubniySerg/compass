package vac.test.core_api

interface AuthComponent {

    fun getTokenRepository(): TokenRepository

    fun getUserRepository(): UserRepository
}