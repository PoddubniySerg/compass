package vac.test.core_di

import vac.test.core_api.MapsComponent

interface MapsInjector {

    fun getMapsDiComponent(): MapsComponent
}