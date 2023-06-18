package vac.test.data.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import vac.test.data.DataApp

internal class OkHttpInterceptor : Interceptor {

    private var token: String? = null

    init {
        val tokenRepository =
            (DataApp.getContext() as DataApp).tokenRepository//TokenRepositoryImpl()
        CoroutineScope(Dispatchers.IO).launch {
            tokenRepository.accessTokenFlow.collect {
                token = it
            }
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if (token == null) {
            Log.d("OkHttpInterceptor origin", originalRequest.toString())
            return chain.proceed(originalRequest)
        }
        val modifiedRequest = originalRequest.newBuilder()
            .addHeader("X-Authorization", "Bearer $token")
            .build()
        Log.d("OkHttpInterceptor modified", modifiedRequest.toString())
        return chain.proceed(modifiedRequest)
    }
}