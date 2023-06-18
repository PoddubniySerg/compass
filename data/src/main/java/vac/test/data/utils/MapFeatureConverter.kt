package vac.test.data.utils

import vac.test.core_api.MapFeature
import vac.test.data.dto.MapFeatureDto

internal class MapFeatureConverter {

    fun convertToMapFeature(mapFeatureDto: MapFeatureDto): MapFeature {
        return object : MapFeature {
            override val id = mapFeatureDto.id
            override val type = mapFeatureDto.type
            override val cameraUrl = mapFeatureDto.properties.cameraUrl
            override val geometryType = mapFeatureDto.geometryType.type
            override val longitude = mapFeatureDto.geometryType.coordinates[0]
            override val latitude = mapFeatureDto.geometryType.coordinates[1]
        }
    }
}