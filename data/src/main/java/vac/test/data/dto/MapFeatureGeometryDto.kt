package vac.test.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class MapFeatureGeometryDto(
    @Json(name = "type") val type: String,
    @Json(name = "coordinates") val coordinates: List<Double>
)