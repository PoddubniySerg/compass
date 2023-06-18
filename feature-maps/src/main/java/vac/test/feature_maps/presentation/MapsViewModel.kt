package vac.test.feature_maps.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.mapbox.geojson.Point
import kotlinx.coroutines.launch
import vac.test.core_api.MapFeaturesRepository
import vac.test.feature_maps.domain.GetMapFeaturesUseCase

internal class MapsViewModel(
    mapFeaturesRepository: MapFeaturesRepository,
    private val getMapFeaturesUseCase: GetMapFeaturesUseCase
) : ViewModel() {

    val mapFeaturesFlow = mapFeaturesRepository.featuresFlow

    private val playersMutableMap = mutableMapOf<Point, ExoPlayer>()
    val players get() = playersMutableMap

    fun getMarkers() {
        viewModelScope.launch {
            try {
                getMapFeaturesUseCase.execute()
            } catch (ex: Exception) {
                return@launch
            }
        }
    }

    fun addPlayer(point: Point, player: ExoPlayer) {
        playersMutableMap[point] = player
    }

    fun removePlayer(point: Point) {
        playersMutableMap.remove(point)
    }
}