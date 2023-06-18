package vac.test.data.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


internal class NetworkClient {

    companion object {
        private const val HOST = "https://gist.githubusercontent.com"
    }

    private val retrofit =
        Retrofit.Builder()
            .baseUrl(HOST)
            .addConverterFactory(MoshiConverterFactory.create())

    fun userApi(): UserApi {
        return retrofit
            .build()
            .create(UserApi::class.java)
    }

    fun mapFeaturesApi(): MapFeaturesApi {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(OkHttpInterceptor())
            .build()
        return retrofit
            .client(httpClient)
            .build()
            .create(MapFeaturesApi::class.java)
    }
}