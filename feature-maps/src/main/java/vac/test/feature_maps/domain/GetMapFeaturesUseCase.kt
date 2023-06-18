package vac.test.feature_maps.domain

import vac.test.core_api.MapFeaturesRepository

internal class GetMapFeaturesUseCase(private val mapFeaturesRepository: MapFeaturesRepository) {

    suspend fun execute() {
        mapFeaturesRepository.getFeatures()
    }
}