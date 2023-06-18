package vac.test.data

import android.app.Application
import android.content.Context
import vac.test.core_api.AuthComponent
import vac.test.core_api.MapsComponent
import vac.test.data.di.AuthComponentImpl
import vac.test.data.di.MapsComponentImpl

open class DataApp : Application() {

    companion object {
        private var appContext: Context? = null
        fun getContext() = appContext!!
    }

    protected lateinit var authComponent: AuthComponent
    protected lateinit var mapsComponent: MapsComponent
    val tokenRepository get() = authComponent.getTokenRepository()

    override fun onCreate() {
        super.onCreate()
        appContext = this
        authComponent = AuthComponentImpl()
        mapsComponent = MapsComponentImpl()
    }
}