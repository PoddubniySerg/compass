package vac.test.data.repositories

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import vac.test.core_api.MapFeature
import vac.test.core_api.MapFeaturesRepository
import vac.test.data.network.NetworkClient
import vac.test.data.utils.MapFeatureConverter

internal class MapFeaturesRepositoryImpl : MapFeaturesRepository {

    private val mapFeaturesApi = NetworkClient().mapFeaturesApi()
    private val converter = MapFeatureConverter()

    private val _featuresChannel = Channel<List<MapFeature>>()
    override val featuresFlow = _featuresChannel.receiveAsFlow()

    override suspend fun getFeatures() {
        val mapFeatures = mapFeaturesApi.getFeatures().body() ?: return
        val result = mapFeatures.features.map { converter.convertToMapFeature(it) }
        _featuresChannel.send(result)
    }
}