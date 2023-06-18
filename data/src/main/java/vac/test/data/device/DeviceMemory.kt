package vac.test.data.device

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import vac.test.data.DataApp

internal class DeviceMemory {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tokens")
    }

    fun getDataSource() = DataApp.getContext().dataStore
}