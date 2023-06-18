package vac.test.compass

import vac.test.core_api.AuthComponent
import vac.test.core_api.MapsComponent
import vac.test.core_di.AuthInjector
import vac.test.core_di.MapsInjector
import vac.test.data.DataApp

class App : DataApp(), AuthInjector, MapsInjector {

    override fun getAuthDiComponent(): AuthComponent {
        return this.authComponent
    }

    override fun getMapsDiComponent(): MapsComponent {
        return this.mapsComponent
    }
}