package vac.test.data.network

import retrofit2.Response
import retrofit2.http.GET
import vac.test.data.dto.TokenDto

internal interface UserApi {

    companion object {
        private const val TOKEN_PATH =
            "/PoddubniySerg/595c8bad76bd0f51eac7032abe4b6fd6/raw/c076a80245d9d1f63c66ad4f9d500ab61041c452/token.json"
    }

    @GET(TOKEN_PATH)
    suspend fun login(): Response<TokenDto>
}