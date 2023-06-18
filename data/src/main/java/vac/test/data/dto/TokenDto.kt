package vac.test.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import vac.test.core_api.Token

@JsonClass(generateAdapter = true)
internal data class TokenDto(
    @Json(name = "access_token") override val accessToken: String,
    @Json(name = "refresh_token") override val refreshToken: String
) : Token
