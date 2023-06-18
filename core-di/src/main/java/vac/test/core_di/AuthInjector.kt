package vac.test.core_di

import vac.test.core_api.AuthComponent

interface AuthInjector {

    fun getAuthDiComponent(): AuthComponent
}