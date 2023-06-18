package vac.test.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class MapFeaturePropertiesDto(
    @Json(name = "camera_url") val cameraUrl: String
)