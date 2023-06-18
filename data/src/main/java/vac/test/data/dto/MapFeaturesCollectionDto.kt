package vac.test.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class MapFeaturesCollectionDto(
    @Json(name = "type") val type: String,
    @Json(name = "features") val features: List<MapFeatureDto>
)