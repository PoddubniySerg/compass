package vac.test.data.repositories

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import vac.test.core_api.TokenRepository
import vac.test.data.device.DeviceMemory

internal class TokenRepositoryImpl : TokenRepository {

    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
    }

    private val dataStore = DeviceMemory().getDataSource()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            dataStore.data.collect { preferences ->
                val accessToken = preferences[stringPreferencesKey(ACCESS_TOKEN_KEY)]
                if (accessToken != null) {
                    _accessTokenChannel.send(accessToken)
                }
            }
        }
    }

    private val _accessTokenChannel = Channel<String>()
    override val accessTokenFlow: Flow<String> = _accessTokenChannel.receiveAsFlow()

    override suspend fun save(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(ACCESS_TOKEN_KEY)] = accessToken
            preferences[stringPreferencesKey(REFRESH_TOKEN_KEY)] = refreshToken
        }
    }
}