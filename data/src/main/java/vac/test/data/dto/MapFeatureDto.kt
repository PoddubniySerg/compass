package vac.test.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class MapFeatureDto(
    @Json(name = "id") val id: Long,
    @Json(name = "type") val type: String,
    @Json(name = "properties") val properties: MapFeaturePropertiesDto,
    @Json(name = "geometry") val geometryType: MapFeatureGeometryDto
)