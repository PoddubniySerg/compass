package vac.test.core_api

import kotlinx.coroutines.flow.Flow

interface MapFeaturesRepository {

    val featuresFlow: Flow<List<MapFeature>>

    suspend fun getFeatures()
}